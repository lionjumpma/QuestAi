<template>
  <div id="app">

    <div id="__next">
      <div class="overflow-hidden w-full h-full relative">
        <div class="flex h-full flex-1 flex-col md:pl-[260px]">
          <div
              class="sticky top-0 z-10 flex items-center border-b border-white/20 bg-gray-800 pl-1 pt-1 text-gray-200 sm:pl-3 md:hidden">
            <div>
              <button @click="showSlideMethod" type="button"
                      class="-ml-0.5 -mt-0.5 inline-flex h-10 w-10 items-center justify-center rounded-md hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white dark:hover:text-white"><span
                  class="sr-only">Open sidebar</span>
                <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24" stroke-linecap="round"
                     stroke-linejoin="round" class="h-6 w-6" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
                  <line x1="3" y1="12" x2="21" y2="12"></line>
                  <line x1="3" y1="6" x2="21" y2="6"></line>
                  <line x1="3" y1="18" x2="21" y2="18"></line>
                </svg>
              </button>
            </div>
            <h1 class="flex-1 text-center text-base font-normal">{{ chatTitle }}</h1>
            <button @click.stop="newChat" type="button" class="px-3">
              <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24" stroke-linecap="round"
                   stroke-linejoin="round" class="h-6 w-6" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
            </button>
          </div>

          <main class="relative h-full w-full transition-width flex flex-col overflow-hidden items-stretch flex-1">
            <chatbox></chatbox>

            <!-- 底部输入 -->
            <inputBox></inputBox>

          </main>
        </div>

<!--        菜单导航-->
        <SideBar></SideBar>
      </div>
      <div class="absolute top-0 left-0 right-0 z-[2]"></div>
    </div>

    <div v-show="showSlide" class="semi-portal" style="z-index: 1000;">
      <div class="">
        <div class="semi-modal-mask"></div>
        <div role="none" class="semi-modal-wrap">
          <div class="semi-modal semi-modal-small" id="dialog-3" style="width: 0px;">
            <div role="dialog" aria-modal="true" aria-labelledby="semi-modal-title" aria-describedby="semi-modal-body"
                 class="semi-modal-content">
              <div class="semi-modal-body-wrapper">
                <div class="semi-modal-body" x-semi-prop="children">
                  <div class="fixed inset-0 z-40 flex">
                    <div class="relative flex w-full max-w-xs flex-1 flex-col bg-gray-900 translate-x-0"
                         id="headlessui-dialog-panel-:r1:" data-headlessui-state="open">
                      <div class="absolute top-0 right-0 -mr-12 pt-2 opacity-100">
                        <button @click="closeShowSlide" type="button"
                                class="ml-1 flex h-10 w-10 items-center justify-center focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"><span
                            class="sr-only">Close sidebar</span>
                          <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                               stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6 text-white" height="1em"
                               width="1em" xmlns="http://www.w3.org/2000/svg">
                            <line x1="18" y1="6" x2="6" y2="18"></line>
                            <line x1="6" y1="6" x2="18" y2="18"></line>
                          </svg>
                        </button>
                      </div>
                      <div ref="slideNavContainer" style="width:320px"
                           class="flex h-full flex-1 items-start border-white/20">
                      </div>
                    </div>
                    <div @click="closeShowSlide" style="width:calc(100% - 320px)" class="flex-shrink-0"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div portal-container="">
      <span
          class="pointer-events-none fixed inset-0 z-[60] mx-auto my-2 flex max-w-[560px] flex-col items-stretch justify-start md:pb-5">
      </span>
    </div>

  </div>
</template>


<script>

import { marked } from 'marked';
import hljs from "highlight.js";
import '@/assets/index.css'
import 'highlight.js/styles/github.css';
import inputBox from "@/components/chat/inputBox.vue";
import chatbox from "@/components/chat/chatbox.vue";
import SideBar from "@/components/chat/SideBar.vue";

const renderer = {
  code(code, infostring, escaped) {
    var codeHtml = code
    if(infostring && infostring == "html"){
      codeHtml = encodeURIComponent(code);
    }
    if (infostring) {
      codeHtml = hljs.highlightAuto(code).value
    }

    console.log(code, infostring, escaped, codeHtml);

    return `<div class="bg-black mb-4 rounded-md">
      <div class="code_header flex items-center relative text-gray-200 bg-gray-800 px-4 py-2 text-xs font-sans">
        <span>${infostring || ""}</span>
        <button onclick="copy(this)" class="flex ml-auto gap-2">
          <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
            <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path><rect x="8" y="2" width="8" height="4" rx="1" ry="1"></rect>
          </svg>
          <span>Copy code</span>
          <code style="display:none">${encodeURIComponent(code)}</code>
        </button>
      </div>
      <div class="p-4 overflow-y-auto">
        <code class="!whitespace-pre hljs language-${infostring}">${codeHtml}</code>
      </div>
    </div>`;
  },
  paragraph(text) {
    return `<p style="white-space:pre-wrap;">${text}</p>`
  }
};
marked.use({ renderer });

export default {
  components: {
    chatbox,
    inputBox,
    SideBar,
  },
  data() {
    return {
      theme: "light",
      popupShow: false,
      avatarIdx: 1,
      conversations: [],
      conversation: [],
      chatTitle: "New chat",
      convLoading: false,
      showSlide: false,
      isShowGoBottom: false,
      oldConv: undefined,
      convTitletmp: "",
      source: undefined,
      rsource: undefined,
      tsource: undefined
    };
  },
  methods: {
    closeShowSlide() {
      this.showSlide = false;
      this.$refs.menu.appendChild(this.$refs.navEle);
    },
    showSlideMethod() {
      this.showSlide = true;
      this.$refs.slideNavContainer.appendChild(this.$refs.navEle);
    },

    vueCopy(node) {
      var code = node.getElementsByTagName("code")[0].innerHTML
      var text = decodeURIComponent(code);
      this.$copyText(text).then(
          res => {
            var svg = `<svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
                        <polyline points="20 6 9 17 4 12"></polyline>
                    </svg>
                    <span>Copied!</span>`
            const nodeInnerHtml = node.innerHTML
            node.innerHTML = svg;

            setTimeout(() => {
              node.innerHTML = nodeInnerHtml
            }, 1000);
          },
          err => {
            console.log('复制失败');
          }
      );
    },
    changeTheme(theme) {
      this.theme = theme;
      var html = document.getElementsByTagName("html")[0]
      html.classList.remove("light", "dark");
      html.classList.add(theme);
      html.style["color-scheme"] = theme;
      localStorage.setItem("theme", theme)
    },
    last(conv) {
      if (conv.idx == 0) {
        return;
      }
      conv.idx--;
      this.refreshConversation();
    },

    next(conv) {
      if (conv.idx == conv["msgList"].length - 1) {
        return;
      }
      conv.idx++;
      this.refreshConversation();
    },
    inputChat(msg) {
      console.log(msg);
      this.chatMsg = msg;
    },
    countAndConcat(str, substr) {
      // 使用正则表达式的全局匹配来查找子字符串
      const matches = str.match(new RegExp(substr, 'g'));

      // 判断子字符串的个数是奇数还是偶数
      const count = matches ? matches.length : 0;
      const isOdd = count % 2 === 1;

      // 根据判断结果返回相应的字符串
      return isOdd ? str + "\n" + substr : str;
    },
    mdToHtml(md, conv) {
      if (md == "") {
        return "<p></p>"
      }

      md = this.countAndConcat(md, "```")

      var htmlMD = marked.parse(md);
      htmlMD = htmlMD.trim();
      return htmlMD;
    },
    refreshConversation() {
      this.conversation = JSON.parse(JSON.stringify(this.conversation));
    },
    generateConvTitle(conv) {
      var that = this;
      var tsource = this.tsource = new EventSource(`/api/chat/title/${this.cid}`);

      //如果服务器响应报文中没有指明事件，默认触发message事件
      conv.title = ""
      tsource.addEventListener("message", function (e) {
        if (e.data == "[DONE]") {
          tsource.close();
          that.selectConversation(conv, false);
          that.saveConversations();
          that.tsource = undefined;
          return
        }

        conv.title += e.data;
        that.selectConversation(conv, false);
      });

      tsource.addEventListener("error", function (e) {
        console.log("error:" + e.data);
        tsource.close();
        that.tsource = undefined;
      });

    },
    newChat() {
      if (this.conversation.length == 0) {
        return
      }

      this.chatTitle = "New chat";
      document.title = "New chat";
      var conversations = this.conversations;
      for (let idx in conversations) {
        var conv = conversations[idx];
        delete conv.editable;
        delete conv.selected;
        delete conv.delete;
      }

      this.loadId()
    },
    loadId() {
      var that = this;
      this.axios.post(`/api/generate/id`, {})
          .then((result) => {
            console.log(result);
            var resp = result.data;

            that.cid = resp.data;
            this.conversation = []
          })
          .catch((err) => {
          });
    },
    loadConversations() {
      let convs = localStorage.getItem("conversations") || "[]";
      this.conversations = JSON.parse(convs);
    },
    saveConversations() {
      var conversations = JSON.parse(JSON.stringify(this.conversations));
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
    selectConversation(conv, loadConv) {
      var that = this;
      if (this.oldConv) {
        this.oldConv.selected = false;
      }
      conv.selected = true
      this.oldConv = conv;

      document.title = conv.title || "chatai";
      this.chatTitle = conv.title || "chatai";

      if (!loadConv) {
        return;
      }

      this.axios.get(`/api/conv/${conv.id}`)
          .then((result) => {
            console.log(result);
            var resp = result.data;
            var content = resp.data;

            that.cid = conv.id;
            that.conversation = that.initConvs(content.convs)
            setTimeout(() => {
              that.isScrollAndNotBottom();
            }, 300)
          })
          .catch((err) => {
          });
    },
    editTitle(idx, conv) {
      this.convTitletmp = conv.title;
      conv.editable = true;
      this.$set(this.conversations, idx, conv);
      setTimeout(() => {
        document.getElementById("titleInput").focus();
      }, 150)
    },
    titleInputBlur(idx, conv) {
      setTimeout(() => {
        this.cancelChangeConvTitle(idx, conv);
      }, 100);
    },
    changeConvTitle(idx, conv) {
      conv.title = this.convTitletmp;
      this.saveConversations();
      this.cancelChangeConvTitle(idx, conv)
    },
    cancelChangeConvTitle(idx, conv) {
      conv.editable = false;
      this.$set(this.conversations, idx, conv);
    },
    delConv(cidx) {
      this.conversations.splice(cidx, 1);
      this.saveConversations();
    },
    cancelDelConv(idx, conv) {
      conv.delete = false;
      this.$set(this.conversations, idx, conv);
    },
    loadAvatar() {
      let avatar = localStorage.getItem("avatar") || Math.ceil(Math.random() * 9);
      this.avatarIdx = avatar;
    },
    handleScrollBottom() {
      this.$nextTick(() => {
        let scrollElem = this.$refs.chatContainer;
        scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' });
      });
    },
    isScrollAndNotBottom() {
      let chatDivEle = this.$refs.chatContainer;
      if (!chatDivEle) {
        return;
      }

      if (chatDivEle.scrollHeight <= chatDivEle.clientHeight) {
        this.isShowGoBottom = false;
        return;
      }

      const scrollTop = chatDivEle.scrollTop;
      const windowHeight = chatDivEle.clientHeight;
      const scrollHeight = chatDivEle.scrollHeight;
      if (scrollTop + windowHeight >= scrollHeight - 50) {
        this.isShowGoBottom = false;
        return;
      }

      this.isShowGoBottom = true;
    },
  },
  computed: {},
  mounted: function () {
    // var theme = localStorage.getItem("theme") || "light"
    // this.changeTheme(theme);
    // this.loadId();
    // this.loadConversations();
    // this.loadAvatar();
    //
    // let chatDivEle = this.$refs.chatContainer;
    // chatDivEle.addEventListener('scroll', this.isScrollAndNotBottom, true)
    //
    // window.copy = this.vueCopy
  }
};
</script>


<style lang="scss">
html,
body {
  height: 100%;
  width: 100%;
}

#app {
  height: 100%;
}

.flex_row_c_c {
  display: flex;
  align-content: center;
  flex-direction: row;
  justify-items: center;
}

.react-scroll-to-bottom--css-krija-1n7m0yu {
  height: 100%;
  overflow-y: auto;
  width: 100%;
}

.code_header {
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
}

.prose :where(code):not(:where([class~=not-prose] *)):before {
  content: "" !important;
}

.prose :where(code):not(:where([class~=not-prose] *)):after {
  content: "" !important;
}

#chatRepeat:focus {
  --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 var(--tw-ring-offset-width) var(--tw-ring-offset-color);
  --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(2px + var(--tw-ring-offset-width)) var(--tw-ring-color);
  --tw-ring-offset-width: 0px;
  box-shadow: var(--tw-ring-offset-shadow), var(--tw-ring-shadow), 0 0 transparent;
  box-shadow: var(--tw-ring-offset-shadow), var(--tw-ring-shadow), var(--tw-shadow, 0 0 transparent);
}

.suitable_selected {
  --tw-text-opacity: 1 !important;
  cursor: auto !important;
}

.load_dot1 {
  -webkit-animation: blink 1s steps(2, start) infinite;
  animation: blink 1s steps(2, start) infinite;
}

.load_dot2 {
  -webkit-animation: blink 1s steps(3, start) infinite;
  animation: blink 1s steps(3, start) infinite;
}

.load_dot3 {
  -webkit-animation: blink 1s steps(4, start) infinite;
  animation: blink 1s steps(4, start) infinite;
}

#app .markdown h1 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

#app .markdown h2 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

#app .markdown h3 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

#app .markdown h4 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

#app .markdown h5 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

#app .markdown h6 {
  margin-bottom: 0rem;
  margin-top: 0rem;
}

@media (max-width: 640px) {
  #app .none {
    display: none;
  }
}

.w-180px {
  width: 180px;
}

.prose-r {
  font-size: 1rem;
  line-height: 1.75;
}
</style>
