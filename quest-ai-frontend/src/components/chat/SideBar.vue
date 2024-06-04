<!--
@Author: 何孟燃
@Date: 2024/4/23
@Description: 定义了一个 Vue 组件的聊天应用程序逻辑，包括处理对话列表、保存和加载对话到本地存储、编辑对话标题、删除对话以及与 Vuex store 交互。
通过使用事件总线 bus，组件之间可以实现通信。它定义了组件的数据、方法、计算属性和生命周期钩子，用于处理聊天应用程序的逻辑和界面
-->

<script>
import {mapActions, mapGetters, mapState} from "vuex";
import {bus} from "vue3-eventbus";

export default {
  data() {
    return {
      convTitletmp: "",
      //conversations: [],
      oldConv: undefined,
    };

  },
  methods: {
    busSend() {
      var id=this.getConversation.id;
      var idx = this.findCidxById(id);
      console.log("bus-send",id,idx)
      if (idx < 0) {
        let msgList;
        msgList=this.getMessageList
        console.log("msgList",msgList)
        this.newChat(msgList)
      }
    },
    //find the position in the list
    findCidxById(id) {
      for (let idx in this.conversations) {
        if (this.conversations[idx].id == id) {
          return idx;
        }
      }
      return -1;
    },


    newChat(msgList) {
      this.chatTitle = "New chat";
      document.title = "New chat";

      let newConv = {
        "id": Date.now(),
        "title": "New chat",
        "msgList": msgList,
      }

      //add the conv to the list
      this.unshiftConversationList(newConv);
      //save the conv list to local storage
      this.saveConversations();
      this.selectConversation(this.conversations.length - 1);
    },
    loadConversationFromLocal() {
      // 尝试从localStorage获取保存的对话列表
      const savedConversations = localStorage.getItem('conversations');

      // 检查是否有数据，如果有，解析JSON格式的字符串转换回数组
      if (savedConversations) {
        try {
          // 将JSON字符串转换成JavaScript对象
          this.setConversationList(JSON.parse(savedConversations));

        } catch (e) {
          // 如果解析出错，可能是数据损坏或格式不正确
          console.error('Failed to parse conversations from localStorage:', e);
          // 可以选择清空损坏的数据或给出错误提示
          this.conversations = [];
        }
      } else {
        // 如果本地存储中没有对话列表，初始化为空数组
        this.conversations = [];
      }
    },
    saveConversations() {
      var conversations = JSON.parse(JSON.stringify(this.getConversationList));
      for (let idx in conversations) {
        var conv = conversations[idx];
        delete conv.editable;
        delete conv.selected;
        delete conv.delete;
      }
      let convs = JSON.stringify(conversations);
      localStorage.setItem("conversations", convs);
    },
    clearConversations() {
      this.conversations = []
      this.saveConversations();
    },
    selectConversation(cidx) {
      if (cidx < 0 || cidx >= this.getConversationList.length) {
        console.log("invalid index")
        return;
      }
      //get the conversation which you want to select by index
      var conv = this.getConversationByIndex(cidx);
      //if the conversation is already selected, return
      if (this.oldConv && this.oldConv.id == conv.id) {
        console.log("same conversation")
        return;
      }
      //set the old conversation to unselected
      if (this.oldConv) {
        this.oldConv.selected = false;
      }
      //set the new conversation to selected
      conv.selected = true
      this.setSelectedIdx(cidx);
      this.oldConv = conv;

      document.title = conv.title || "chatai";
      this.chatTitle = conv.title || "chatai";

      //set the conversation to vuex
      this.setConversation(conv);
      this.conversations[cidx] = conv;

    },
    editTitle(cidx) {
      if (cidx < 0 || cidx >= this.conversations.length) {
        alert("invalid index")
        console.log("invalid index")
        return;
      }
      var conv = this.conversations[cidx];
      this.convTitletmp = conv.title;
      conv.editable = true;
      this.conversations[cidx] = conv;
      setTimeout(() => {
        document.getElementById("titleInput").focus();
      }, 150)
    },
    titleInputBlur(cidx, conv) {
      setTimeout(() => {
        this.cancelChangeConvTitle(cidx, conv);
      }, 1000);
    },
    resetConversation(){
      var newConv=this.getConversation;
      newConv.msgList=[];
      this.updateConversation(newConv);
    },
    changeConvTitle(idx, conv) {
      conv.title = this.convTitletmp;
      this.setConversationByIndex({cidx:idx,conv:conv});
      this.saveConversations();
      this.cancelChangeConvTitle(idx, conv)
    },
    cancelChangeConvTitle(idx, conv) {
      conv.editable = false;
      //this.conversations[idx] = conv;
      },
    delConv(cidx) {
      this.delConversationByIndex(cidx);
      this.resetConversation();
      this.saveConversations();
    },
    cancelDelConv(idx, conv) {
      conv.delete = false;
      this.$set(this.conversations, idx, conv);
    },
    ...mapActions([
      'setShowSlide',
      'setConversationList',
      'setConversation',
      'unshiftConversationList',
        'setConversationByIndex',
        'delConversationByIndex',
        'setSelectedIdx'
        ,'addMessage',
        'updateConversation'
    ])

  },
  mounted() {
    bus.on('slideNavContainer', (ref) => {
      console.log(ref.innerHTML); // 使用 ref
    });
    bus.on('bus-send', () => {
      console.log("bus select-conversation");
      this.busSend();
    });
  },
  computed: {
    conversations: {
      get() {
        return this.getConversationList;
      },
      set(value) {
        this.setConversationList(value);
      }
    },
    ...mapGetters([
          'getShowSlide',
          'getConversationList',
          'getConversation',
          'getConversationByIndex',
          'getMessageList'
        ]
    ),
  },
}

</script>

<template>
  <div class="dark hidden bg-gray-900 md:fixed md:inset-y-0 md:flex md:w-[260px] md:flex-col">
    <div class="flex h-full min-h-0 flex-col ">
      <div ref="menu" class="scrollbar-trigger flex h-full w-full flex-1 items-start border-white/20">

        <nav ref="navEle" class="flex h-full flex-1 flex-col space-y-1 p-2">
          <a @click.stop="newChat([])"
             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm mb-2 flex-shrink-0 border border-white/20">
            <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                 stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <line x1="12" y1="5" x2="12" y2="19"></line>
              <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
            New chat
          </a>

          <!-- 对话列表 -->
          <div class="flex-col flex-1 overflow-y-auto border-b border-white/20" style="padding-bottom: 5px;">
            <div class="flex flex-col gap-2 text-gray-100 text-sm">

              <template v-for="(conversation, cidx) in this.getConversationList" :key="conversation.id">

                <div v-if="conversation.editable"
                     class="m-focus flex py-3 px-3 items-center gap-3 relative rounded-md cursor-pointer hover:pr-14 break-all pr-14 bg-gray-800 hover:bg-gray-800">
                  <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                       stroke-linejoin="round" class="h-4 w-4 flex-shrink-0" height="1em" width="1em"
                       xmlns="http://www.w3.org/2000/svg">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                  </svg>
                  <input id="titleInput" v-model="convTitletmp" @blur="titleInputBlur(cidx, conversation)"
                         type="text" class="text-sm border-none bg-transparent p-0 m-0 w-full mr-0" autofocus="true">
                  <div class="absolute flex right-1 z-10 text-gray-300 visible">
                    <button @click="changeConvTitle(cidx, conversation)" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <polyline points="20 6 9 17 4 12"></polyline>

                      </svg>
                    </button>
                    <button @click="cancelChangeConvTitle(cidx, conversation)" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                      </svg>
                    </button>
                  </div>
                </div>

                <a v-else-if="conversation.delete" @blur="cancelDelConv(cidx, conversation)"
                   class="m-focus flex py-3 px-3 items-center gap-3 relative rounded-md cursor-pointer break-all pr-14 bg-gray-800 hover:bg-gray-800 group">
                  <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                       stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                       xmlns="http://www.w3.org/2000/svg">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    <line x1="10" y1="11" x2="10" y2="17"></line>
                    <line x1="14" y1="11" x2="14" y2="17"></line>
                  </svg>
                  <div class="flex-1 text-ellipsis max-h-5 overflow-hidden break-all relative">Delete "{{
                      conversation.title
                    }}"?
                    <div class="absolute inset-y-0 right-0 w-8 z-10 bg-gradient-to-l from-gray-800"></div>
                  </div>
                  <div class="absolute flex right-1 z-10 text-gray-300 visible">
                    <button @click="delConv(cidx)" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <polyline points="20 6 9 17 4 12"></polyline>
                      </svg>
                    </button>
                    <button @click="cancelDelConv(cidx, conversation)" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                      </svg>
                    </button>
                  </div>
                </a>


                <a v-else @click.stop.prevent="selectConversation(cidx)"
                   :class="{ 'bg-gray-800 hover:bg-gray-800 pr-14': conversation.selected, 'hover:bg-[#2A2B32] hover:pr-4': !conversation.selected }"
                   class="flex py-3 px-3 items-center gap-3 relative rounded-md cursor-pointer break-all group">
                  <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                       stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                       xmlns="http://www.w3.org/2000/svg">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z">
                    </path>
                  </svg>
                  <div class="flex-1 text-ellipsis max-h-5 overflow-hidden break-all relative">
                    {{ conversation.title }}
                    <div
                        :class="{ 'from-gray-800': conversation.selected, 'from-gray-900 group-hover:from-[#2A2B32]': !conversation.selected }"
                        class="absolute inset-y-0 right-0 w-8 z-10 bg-gradient-to-l">
                    </div>
                  </div>
                  <div v-show="conversation.selected" class="absolute flex right-1 z-10 text-gray-300 visible">
                    <button @click="editTitle(cidx)" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 20h9"></path>
                        <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
                      </svg>
                    </button>
                    <button @click="conversation.delete = true" class="p-1 hover:text-white">
                      <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                           stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                           xmlns="http://www.w3.org/2000/svg">
                        <polyline points="3 6 5 6 21 6"></polyline>
                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2">
                        </path>
                        <line x1="10" y1="11" x2="10" y2="17"></line>
                        <line x1="14" y1="11" x2="14" y2="17"></line>
                      </svg>
                    </button>
                  </div>
                </a>

              </template>


            </div>
          </div>

          <a v-if="getConversationList.length > 0" @click.stop.prevent="clearConversations"
             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm">
            <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                 stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <polyline points="3 6 5 6 21 6"></polyline>
              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2">
              </path>
              <line x1="10" y1="11" x2="10" y2="17"></line>
              <line x1="14" y1="11" x2="14" y2="17"></line>
            </svg>
            Clear conversations
          </a>
          <!--          <a v-if="theme == 'light'" @click="changeTheme('dark')"-->
          <!--             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm">-->
          <!--            <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"-->
          <!--                 stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">-->
          <!--              <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>-->
          <!--            </svg>-->
          <!--            Dark mode-->
          <!--          </a>-->

          <!--          <a v-if="theme == 'dark'" @click="changeTheme('light')"-->
          <!--             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm">-->
          <!--            <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"-->
          <!--                 stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">-->
          <!--              <circle cx="12" cy="12" r="5"></circle>-->
          <!--              <line x1="12" y1="1" x2="12" y2="3"></line>-->
          <!--              <line x1="12" y1="21" x2="12" y2="23"></line>-->
          <!--              <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>-->
          <!--              <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>-->
          <!--              <line x1="1" y1="12" x2="3" y2="12"></line>-->
          <!--              <line x1="21" y1="12" x2="23" y2="12"></line>-->
          <!--              <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>-->
          <!--              <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>-->
          <!--            </svg>-->
          <!--            Light mode</a>-->

          <!--          <a href="https://discord.gg/openai" target="_blank"-->
          <!--             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm">-->
          <!--            <svg stroke="currentColor" fill="currentColor" stroke-width="2" viewBox="0 0 640 512" class="h-4 w-4"-->
          <!--                 height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">-->
          <!--              <path-->
          <!--                  d="M524.531,69.836a1.5,1.5,0,0,0-.764-.7A485.065,485.065,0,0,0,404.081,32.03a1.816,1.816,0,0,0-1.923.91,337.461,337.461,0,0,0-14.9,30.6,447.848,447.848,0,0,0-134.426,0,309.541,309.541,0,0,0-15.135-30.6,1.89,1.89,0,0,0-1.924-.91A483.689,483.689,0,0,0,116.085,69.137a1.712,1.712,0,0,0-.788.676C39.068,183.651,18.186,294.69,28.43,404.354a2.016,2.016,0,0,0,.765,1.375A487.666,487.666,0,0,0,176.02,479.918a1.9,1.9,0,0,0,2.063-.676A348.2,348.2,0,0,0,208.12,430.4a1.86,1.86,0,0,0-1.019-2.588,321.173,321.173,0,0,1-45.868-21.853,1.885,1.885,0,0,1-.185-3.126c3.082-2.309,6.166-4.711,9.109-7.137a1.819,1.819,0,0,1,1.9-.256c96.229,43.917,200.41,43.917,295.5,0a1.812,1.812,0,0,1,1.924.233c2.944,2.426,6.027,4.851,9.132,7.16a1.884,1.884,0,0,1-.162,3.126,301.407,301.407,0,0,1-45.89,21.83,1.875,1.875,0,0,0-1,2.611,391.055,391.055,0,0,0,30.014,48.815,1.864,1.864,0,0,0,2.063.7A486.048,486.048,0,0,0,610.7,405.729a1.882,1.882,0,0,0,.765-1.352C623.729,277.594,590.933,167.465,524.531,69.836ZM222.491,337.58c-28.972,0-52.844-26.587-52.844-59.239S193.056,219.1,222.491,219.1c29.665,0,53.306,26.82,52.843,59.239C275.334,310.993,251.924,337.58,222.491,337.58Zm195.38,0c-28.971,0-52.843-26.587-52.843-59.239S388.437,219.1,417.871,219.1c29.667,0,53.307,26.82,52.844,59.239C470.715,310.993,447.538,337.58,417.871,337.58Z">-->
          <!--              </path>-->
          <!--            </svg>-->
          <!--            OpenAI Discord</a>-->
          <!--          <a href="https://help.openai.com/en/collections/3742473-chatgpt" target="_blank"-->
          <!--             class="flex py-3 px-3 items-center gap-3 rounded-md hover:bg-gray-500/10 transition-colors duration-200 text-white cursor-pointer text-sm">-->
          <!--            <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"-->
          <!--                 stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">-->
          <!--              <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>-->
          <!--              <polyline points="15 3 21 3 21 9"></polyline>-->
          <!--              <line x1="10" y1="14" x2="21" y2="3"></line>-->
          <!--            </svg>-->
          <!--            Updates &amp; FAQ</a>-->
        </nav>

      </div>
    </div>
  </div>

</template>

<style scoped>

</style>
