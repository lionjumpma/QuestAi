<!--
@Author: 唐莺芸
@Date: 2024/4/29
@Description: 定义了一些数据、方法、计算属性和侦听器，用于处理聊天界面的交互和功能。
该聊天界面逻辑，包括处理用户输入、发送聊天消息、接收聊天响应、处理聊天记录的加载状态和滚动聊天界面等功能。
定义了计算属性，用于获取和设置 Vuex store 中的状态，用于在组件中简化对 Vuex store 的访问
-->
<script>
import {mapActions, mapGetters} from "vuex";
// import bus from "@/eventbus/eventbus.ts";
import {bus} from "vue3-eventbus";
import {aiChat} from "@/api/chatapi.ts";
export default {
  data() {
    return {
      model: 'GLM3',
      options: [
        { value: 'GLM3', text: 'GLM3' },
        { value: 'GLM4', text: 'GLM4' },
      ],
      //chatMsg: "",
      convLoading: false,
      source: undefined,
      rsource: undefined,
    };
  },
  watch: {
    //watch the chatMsg in vuex
    chatMsg(newVal, oldVal) {
      //console.log(newVal, oldVal)
      if (newVal === oldVal) {
        return;
      }
      this.changeHeight();
    }
  },
  methods: {
    onChange() {
      this.$emit('change', this.model);
    },

    preprocessText(text) {
  return text.split('').map((char, index, arr) => {
    // 判断当前字符是否为中文
    if (/[\u4e00-\u9fa5]/.test(char)) {
      // 中文字符，去除前后空格（如果有）
      return char;
    } else {
      // 非中文字符，保留原样，包括空格
      return char;
    }
  }).join('');
},
    initConvs(convs) {
      for (let i = 0; i < convs.length; i++) {
        var conv = convs[i];
        if (conv.role == "assistant") {
          continue
        }
        conv["idx"] = conv["msgList"].length - 1;
      }
      return convs;
    },
    addInitMessage(){
      this.addMessage({
        "idx": 0,
        "role": "user",
        "message": this.chatMsg
      })
      var message = {
        "idx": 0,
        "loading": true,
        "role": "assistant",
        "suitable": [0],
        "speeches": [""]
      }
      this.addMessage(message)
    },


    chat(){
      // console.log("bus-send",that.conversation)
      bus.emit('bus-send')
      const chatMsgCopy = this.chatMsg;
      this.chatMsg = "";
       aiChat(this.model,chatMsgCopy,this.conversation.id).then((res)=>{
          var tmpMessage=this.conversation.msgList[this.conversation.msgList?.length - 1]
          tmpMessage["speeches"][0] += res.data.message
         tmpMessage["loading"] = false;
          this.conversation.msgList[this.conversation.msgList?.length - 1]=tmpMessage;
          this.updateConversation(this.conversation);
         this.handleScrollBottom();
         console.log(res.data.message)

       }).catch((err)=>{
         console.log(err)
       })
      this.convLoading = false;

    },

    streamChat(){
      var that = this;
      var source = this.source = new EventSource(`/api/chat/stream?content=${this.chatMsg}&&model=GLM3`);
      var message
      source.addEventListener("open", function () {
        console.log("connect");
        that.chatMsg = "";
        // console.log("bus-send",that.conversation)
        bus.emit('bus-send')
      });
      //如果服务器响应报文中没有指明事件，默认触发message事件
      source.addEventListener("message", function (e) {
        message = that.conversation.msgList[that.conversation.msgList?.length - 1];
        //console.log("conversation",that.conversation)
        var content = e.data;
        //console.log(`resp:(${e.data})`);
        // 滚动到最下面
        that.handleScrollBottom();
        message["speeches"][0] += content

        //that.refreshConversation();
      });
      source.addEventListener('[complete]', (event) => {
        source.close();
        message["loading"] = false;
        that.convLoading = false;
        var conv = that.conversation;
        conv.msgList[conv.msgList.length-1]=message;
        this.updateConversation(conv)
        that.source = undefined;
      });
      //发生错误，则会触发error事件
      source.addEventListener("error", function (e) {
        console.log("error:" + e.data);
        source.close();
        that.convLoading = false;
        that.source = undefined;
      });
    },
    send() {

      if (this.chatMsg.trim().length == 0) {
        return;
      }

      if (this.convLoading) {
        return;
      }

      this.convLoading = true;
      this.chatMsg = this.chatMsg.trim().replace(/\n/g, "")
      this.addInitMessage()
      // 滚动到最下面
      this.handleScrollBottom();
      //this.streamChat();
      this.chat();
      //this.convLoading = false;

    },
    refreshListByConv() {
       var idx;
      for (let idx in this.getConversationList) {
        if (this.getConversationList[idx].id == this.conversation.id) {
          this.setConversationByIndex({idx: idx, newConv: this.conversation});
          //console.log("refreshListByConv",this.getConversationList[idx])
          break;
        }
      }
    },
    refreshConversation() {
      this.setConversation(JSON.parse(JSON.stringify(this.getConversation)));
    },

    handleScrollBottom() {
      this.$nextTick(() => {
        bus.emit('scroll-to-bottom');
      });
    },

    judgeInput(e) {
      if (!e.shiftKey && e.keyCode == 13) {
        e.cancelBubble = true;  //ie阻止冒泡行为
        e.stopPropagation(); //Firefox阻止冒泡行为
        e.preventDefault(); //取消事件的默认动作*换行
        this.send();
      }
    },
    closeSource() {
      var that = this;
      if (that.source) {
        that.source.close();
        that.source = undefined;
      }
      if (that.tsource) {
        that.tsource.close();
        that.tsource = undefined;

      }
      if (that.rsource) {
        that.rsource.close();
        that.rsource = undefined;
      }
      this.convLoading = false;
    },
    chatRepeat() {
      if (this.convLoading) {
        return
      }

      var that = this;
      this.convLoading = true;

      var rconv = this.getMessageList[this.getMessageList.length - 1];
      rconv["idx"] = rconv["suitable"].length;
      rconv["loading"] = true;
      rconv["suitable"].push(0);
      rconv["msgList"].push("");
      //that.refreshConversation()

      var rsource = this.rsource = new EventSource(`/api/chat/repeat/${this.cid}`);
      rsource.addEventListener("open", function () {
        console.log("connect");
      });

      //如果服务器响应报文中没有指明事件，默认触发message事件
      rsource.addEventListener("content", function (e) {
        console.log(`resp:(${e.data})`);
        var rconv = that.messageList[that.messageList.length - 1];
        if (e.data == "complete") {
          rsource.close();

          rconv["loading"] = false;
          that.convLoading = false;
          //that.refreshConversation();
          that.rsource = undefined;
          return;
        }

        var content = e.data;
        if (content.includes("[ENTRY]")) {
          content = content.replaceAll("[ENTRY]", "\n");
        }

        // 滚动到最下面
        that.handleScrollBottom();

        var idx = rconv.idx;
        rconv["msgList"][idx] += content;
        //that.refreshConversation()
      });

      //发生错误，则会触发error事件
      rsource.addEventListener("error", function (e) {
        console.log("error:" + e.data);
        rsource.close();
        that.rsource = undefined;
      });
    },
    stopChat() {
      this.closeSource()
      this.convLoading = false;
    },
    changeHeight() {
      var elem = this.$refs.inputChat;
      elem.style.height = '24px';
      var scrollHeight = elem.scrollHeight;
      if (24 >= scrollHeight || this.chatMsg.length == 0) {
        this.resetHeight();
        return;
      }

      elem.style.removeProperty("overflow-y")
      elem.style.height = scrollHeight + 'px';
      console.log(scrollHeight)

    },
    resetHeight() {
      var elem = this.$refs.inputChat;
      elem.style.height = '24px';
      elem.style["overflow-y"] = 'hidden';
    },

    ...mapActions([
      'addMessage',  // 使用 action 添加消息
        'setConversation',
        'unshiftConversationList',
        'setConversationByIndex',
        'updateConversation'
    ])
  },

  computed: {
    conversation: {
      get() {
        return this.getConversation?this.getConversation:{};
      },
      set(value) {
        this.$store.commit('setConversation', value);  // updateChatMsg 是你的 mutation 名称
      }
    },
    messageList: {
      get() {
        return this.getMessageList?this.getMessageList:[];
      },
      set(value) {
        this.$store.commit('setMessageList', value);  // updateChatMsg 是你的 mutation 名称
      }
    },
    ...mapGetters([

      'getMessageList' // 使用 getter 获取 conversation
        ,'getChatMsg',
        'getConversation',
        'getConversationList'
    ]),
    chatMsg: {
      get() {
        return this.getChatMsg;  // moduleName 是你的模块名称，如果没有模块则直接 this.$store.state.chatMsg
      },
      set(value) {
        this.$store.commit('setChatMsg', value);  // updateChatMsg 是你的 mutation 名称
      }
    }

  },
}
</script>

<template>
  <div
      class="absolute bottom-0 left-0 w-full border-t md:border-t-0 dark:border-white/20 md:border-transparent md:dark:border-transparent md:bg-vert-light-gradient bg-white dark:bg-gray-800 md:!bg-transparent dark:md:bg-vert-dark-gradient">
    <form class="stretch mx-2 flex flex-row gap-3 pt-2 last:mb-2 md:last:mb-6 lg:mx-auto lg:max-w-3xl lg:pt-6">
      <div class="flex items-start p-2">
        <select
            v-model="model"
            @change="onChange"
            class="p-1 pr-3 rounded-md text-gray-500 hover:bg-gray-100 dark:hover:text-gray-400 dark:hover:bg-gray-900 disabled:hover:bg-transparent dark:disabled:hover:bg-transparent min-w-[100px]">
          <option disabled value="">model</option>
          <option v-for="option in options" :key="option.value" :value="option.value">
            {{ option.text }}
          </option>
        </select>
      </div>
      <div class="relative flex h-full flex-1 md:flex-col">
        <div class="flex ml-1 md:w-full md:m-auto md:mb-2 gap-0 md:gap-2 justify-center">

          <button v-if="!convLoading && this.messageList.length > 0" @click.stop.prevent="chatRepeat"
                  id="chatRepeat" class="btn flex justify-center gap-2 btn-neutral border-0 md:border">
            <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24" stroke-linecap="round"
                 stroke-linejoin="round" class="h-3 w-3" height="1em" width="1em"
                 xmlns="http://www.w3.org/2000/svg">
              <polyline points="1 4 1 10 7 10"></polyline>
              <polyline points="23 20 23 14 17 14"></polyline>
              <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"></path>
            </svg>
            <p class="none">Regenerate response</p>
          </button>

          <button v-if="convLoading" @click.stop.prevent="stopChat" id="stopChat"
                  class="btn relative btn-neutral border-0 md:border">
            <div class="flex w-full items-center justify-center gap-2">
              <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                   stroke-linecap="round" stroke-linejoin="round" class="h-3 w-3" height="1em" width="1em"
                   xmlns="http://www.w3.org/2000/svg">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
              </svg>
              Stop generating
            </div>
          </button>

        </div>
        <div
            class="flex flex-col w-full py-2 flex-grow md:py-3 md:pl-4 relative border border-black/10 bg-white dark:border-gray-900/50 dark:text-white dark:bg-gray-700 rounded-md shadow-[0_0_10px_rgba(0,0,0,0.10)] dark:shadow-[0_0_15px_rgba(0,0,0,0.10)]">
                    <textarea v-model="chatMsg" ref="inputChat" @keydown="judgeInput" tabindex="0" data-id="root"
                              style="max-height: 200px; height: 24px; overflow-y: hidden;" rows="1"
                              class="m-0 w-full resize-none border-0 bg-transparent p-0 pl-2 pr-7 focus:ring-0 focus-visible:ring-0 dark:bg-transparent md:pl-0"></textarea>


          <button @click.stop.prevent="send" :disabled="convLoading"
                  class="absolute p-1 rounded-md text-gray-500 bottom-1.5 right-1 md:bottom-2.5 md:right-2 hover:bg-gray-100 dark:hover:text-gray-400 dark:hover:bg-gray-900 disabled:hover:bg-transparent dark:disabled:hover:bg-transparent">
            <div v-if="convLoading" class="text-2xl" style="line-height: 1.3rem;">
              <span class="load_dot1">·</span><span class="load_dot2">·</span><span class="load_dot3">·</span>
            </div>
            <svg v-else stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                 stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4 mr-1" height="1em" width="1em"
                 xmlns="http://www.w3.org/2000/svg">
              <line x1="22" y1="2" x2="11" y2="13"></line>
              <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
            </svg>
          </button>

        </div>
      </div>
    </form>
    <div class="px-3 pt-2 pb-3 text-center text-xs text-black/50 dark:text-white/50 md:px-4 md:pt-3 md:pb-6">
      <a href="https://gitee.com/MIEAPP/chatai-vue" target="_blank" rel="noreferrer"
         class="underline"></a> 本开源项目前端使用vue，后端基于智谱清言开发
    </div>
  </div>

</template>

<style scoped>

</style>
