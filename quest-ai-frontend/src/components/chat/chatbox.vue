<!--
@Author: 何孟燃
@Date: 2024/4/30
@Description: 定义了一些方法和计算属性，用于处理聊天界面的一些功能。
              Vue 组件的聊天界面逻辑，包括处理用户输入、转换 Markdown 文本、更新聊天记录、滚动聊天界面以及与 Vuex store 交互在此定义
-->
<script>
import {mapActions, mapGetters} from "vuex";
import {bus} from "vue3-eventbus";
import {marked} from "marked";
// import bus from "@/eventbus/eventbus.ts";
import human from "@/assets/imgs/human.png";

export default {
  data() {
    return {
      human,
      isShowGoBottom: false,
    };
  },
  mounted() {
    bus.on('scroll-to-bottom', this.scrollToBottom);
  },
  beforeDestroy() {
    bus.off('scroll-to-bottom', this.scrollToBottom);
  },
  methods:{
    inputChat(msg) {
      console.log(msg);
      this.setChatMsg(msg);
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
    //transform markdown to html
    mdToHtml(md, conv) {
      if (md == "") {
        return "<p></p>"
      }

      md = this.countAndConcat(md, "```")
      md = md.replace(/(^|\n)(#+)([^\s#])/g, "$1$2 $3")  // 处理标题
          .replace(/(^|\n)(-)([^\s-])/g, "$1$2 $3");  // 处理无序列表项

      var htmlMD = marked.parse(md);
      //console.log('htmlMD', htmlMD)
      htmlMD = htmlMD.trim();
      return htmlMD;
    },
    refreshConversation() {
      this.conversation=(JSON.parse(JSON.stringify(this.conversation)));
    },
    scrollToBottom() {
      let scrollElem = this.$refs.chatContainer;
      //console.log('scroll to bottom', scrollElem)
      scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' });
    },
    ...mapActions([
        'setChatMsg',
      'addMessage',  // 使用 action 添加消息
      'setConversation'
    ])
  },
  computed: {
    conversation: {
      get() {
        return this.getConversation;
      },
      set(value) {
        this.setConversation(value);
      }
    },
    ...mapGetters([
      'getConversation'
    ])
  },
}
</script>

<template>
  <main class="relative h-full w-full transition-width flex flex-col overflow-hidden items-stretch flex-1">
    <!-- 聊天窗 -->
    <div class="flex-1 overflow-hidden">
      <div class="react-scroll-to-bottom--css-ncqif-79elbk h-full dark:bg-gray-800">
        <div ref="chatContainer" class="react-scroll-to-bottom--css-krija-1n7m0yu">
          <div class="flex flex-col items-center text-sm dark:bg-gray-800">
            <!-- 对话item -->
            <template v-for="(msg, idx) in this.conversation?.msgList">
              <!-- human -->
              <div v-if="msg.role == 'user'"
                   class="w-full border-b border-black/10 dark:border-gray-900/50 text-gray-800 dark:text-gray-100 group dark:bg-gray-800">
                <div
                    class="text-base gap-4 md:gap-6 m-auto md:max-w-2xl lg:max-w-2xl xl:max-w-3xl p-4 md:py-6 flex lg:px-0">
                  <div class="w-[30px] flex flex-col relative items-end">
                    <div class="relative flex">
                              <span
                                  style="box-sizing: border-box; display: inline-block; overflow: hidden; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; position: relative; max-width: 100%;">
                                <span
                                    style="box-sizing: border-box; display: block; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; max-width: 100%;">
                                  <img aria-hidden="true" :src="human"
                                       alt="human"
                                       style="display: block; max-width: 100%; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px;">
                                </span>
                              </span>
                    </div>
                  </div>
                  <div class="relative flex w-[calc(100%-50px)] flex-col gap-1 md:gap-3 lg:w-[calc(100%-115px)]">
                    <div class="flex flex-grow flex-col gap-3">
                      <div class="min-h-[20px] flex flex-col items-start gap-4 whitespace-pre-wrap">{{
                          msg.message
                        }}
                      </div>
                    </div>
                    <div v-if="false"
                         class="text-gray-400 flex self-end lg:self-center justify-center mt-2 gap-3 md:gap-4 lg:gap-1 lg:absolute lg:top-0 lg:translate-x-full lg:right-0 lg:mt-0 lg:pl-2 visible">
                      <button
                          class="p-1 rounded-md hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-gray-200 disabled:dark:hover:text-gray-400 md:invisible md:group-hover:visible">
                        <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                             stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em" width="1em"
                             xmlns="http://www.w3.org/2000/svg">
                          <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                          <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                      </button>
                    </div>
                    <div class="flex justify-between"></div>
                  </div>
                </div>
              </div>

              <!-- ai -->
              <div v-if="msg.role === 'assistant'"
                   class="w-full border-b border-black/10 dark:border-gray-900/50 text-gray-800 dark:text-gray-100 group bg-gray-50 dark:bg-[#444654]">
                <div
                    class="text-base gap-4 md:gap-6 m-auto md:max-w-2xl lg:max-w-2xl xl:max-w-3xl p-4 md:py-6 flex lg:px-0">
                  <div class="w-[30px] flex flex-col relative items-end">
                    <div
                        class="relative h-[30px] w-[30px] p-1 rounded-sm text-white flex items-center justify-center"
                        style="background-color: rgb(16, 163, 127);">
                      <svg width="41" height="41" viewBox="0 0 41 41" fill="none"
                           xmlns="http://www.w3.org/2000/svg" stroke-width="1.5" class="h-6 w-6">
                        <path
                            d="M37.5324 16.8707C37.9808 15.5241 38.1363 14.0974 37.9886 12.6859C37.8409 11.2744 37.3934 9.91076 36.676 8.68622C35.6126 6.83404 33.9882 5.3676 32.0373 4.4985C30.0864 3.62941 27.9098 3.40259 25.8215 3.85078C24.8796 2.7893 23.7219 1.94125 22.4257 1.36341C21.1295 0.785575 19.7249 0.491269 18.3058 0.500197C16.1708 0.495044 14.0893 1.16803 12.3614 2.42214C10.6335 3.67624 9.34853 5.44666 8.6917 7.47815C7.30085 7.76286 5.98686 8.3414 4.8377 9.17505C3.68854 10.0087 2.73073 11.0782 2.02839 12.312C0.956464 14.1591 0.498905 16.2988 0.721698 18.4228C0.944492 20.5467 1.83612 22.5449 3.268 24.1293C2.81966 25.4759 2.66413 26.9026 2.81182 28.3141C2.95951 29.7256 3.40701 31.0892 4.12437 32.3138C5.18791 34.1659 6.8123 35.6322 8.76321 36.5013C10.7141 37.3704 12.8907 37.5973 14.9789 37.1492C15.9208 38.2107 17.0786 39.0587 18.3747 39.6366C19.6709 40.2144 21.0755 40.5087 22.4946 40.4998C24.6307 40.5054 26.7133 39.8321 28.4418 38.5772C30.1704 37.3223 31.4556 35.5506 32.1119 33.5179C33.5027 33.2332 34.8167 32.6547 35.9659 31.821C37.115 30.9874 38.0728 29.9178 38.7752 28.684C39.8458 26.8371 40.3023 24.6979 40.0789 22.5748C39.8556 20.4517 38.9639 18.4544 37.5324 16.8707ZM22.4978 37.8849C20.7443 37.8874 19.0459 37.2733 17.6994 36.1501C17.7601 36.117 17.8666 36.0586 17.936 36.0161L25.9004 31.4156C26.1003 31.3019 26.2663 31.137 26.3813 30.9378C26.4964 30.7386 26.5563 30.5124 26.5549 30.2825V19.0542L29.9213 20.998C29.9389 21.0068 29.9541 21.0198 29.9656 21.0359C29.977 21.052 29.9842 21.0707 29.9867 21.0902V30.3889C29.9842 32.375 29.1946 34.2791 27.7909 35.6841C26.3872 37.0892 24.4838 37.8806 22.4978 37.8849ZM6.39227 31.0064C5.51397 29.4888 5.19742 27.7107 5.49804 25.9832C5.55718 26.0187 5.66048 26.0818 5.73461 26.1244L13.699 30.7248C13.8975 30.8408 14.1233 30.902 14.3532 30.902C14.583 30.902 14.8088 30.8408 15.0073 30.7248L24.731 25.1103V28.9979C24.7321 29.0177 24.7283 29.0376 24.7199 29.0556C24.7115 29.0736 24.6988 29.0893 24.6829 29.1012L16.6317 33.7497C14.9096 34.7416 12.8643 35.0097 10.9447 34.4954C9.02506 33.9811 7.38785 32.7263 6.39227 31.0064ZM4.29707 13.6194C5.17156 12.0998 6.55279 10.9364 8.19885 10.3327C8.19885 10.4013 8.19491 10.5228 8.19491 10.6071V19.808C8.19351 20.0378 8.25334 20.2638 8.36823 20.4629C8.48312 20.6619 8.64893 20.8267 8.84863 20.9404L18.5723 26.5542L15.206 28.4979C15.1894 28.5089 15.1703 28.5155 15.1505 28.5173C15.1307 28.5191 15.1107 28.516 15.0924 28.5082L7.04046 23.8557C5.32135 22.8601 4.06716 21.2235 3.55289 19.3046C3.03862 17.3858 3.30624 15.3413 4.29707 13.6194ZM31.955 20.0556L22.2312 14.4411L25.5976 12.4981C25.6142 12.4872 25.6333 12.4805 25.6531 12.4787C25.6729 12.4769 25.6928 12.4801 25.7111 12.4879L33.7631 17.1364C34.9967 17.849 36.0017 18.8982 36.6606 20.1613C37.3194 21.4244 37.6047 22.849 37.4832 24.2684C37.3617 25.6878 36.8382 27.0432 35.9743 28.1759C35.1103 29.3086 33.9415 30.1717 32.6047 30.6641C32.6047 30.5947 32.6047 30.4733 32.6047 30.3889V21.188C32.6066 20.9586 32.5474 20.7328 32.4332 20.5338C32.319 20.3348 32.154 20.1698 31.955 20.0556ZM35.3055 15.0128C35.2464 14.9765 35.1431 14.9142 35.069 14.8717L27.1045 10.2712C26.906 10.1554 26.6803 10.0943 26.4504 10.0943C26.2206 10.0943 25.9948 10.1554 25.7963 10.2712L16.0726 15.8858V11.9982C16.0715 11.9783 16.0753 11.9585 16.0837 11.9405C16.0921 11.9225 16.1048 11.9068 16.1207 11.8949L24.1719 7.25025C25.4053 6.53903 26.8158 6.19376 28.2383 6.25482C29.6608 6.31589 31.0364 6.78077 32.2044 7.59508C33.3723 8.40939 34.2842 9.53945 34.8334 10.8531C35.3826 12.1667 35.5464 13.6095 35.3055 15.0128ZM14.2424 21.9419L10.8752 19.9981C10.8576 19.9893 10.8423 19.9763 10.8309 19.9602C10.8195 19.9441 10.8122 19.9254 10.8098 19.9058V10.6071C10.8107 9.18295 11.2173 7.78848 11.9819 6.58696C12.7466 5.38544 13.8377 4.42659 15.1275 3.82264C16.4173 3.21869 17.8524 2.99464 19.2649 3.1767C20.6775 3.35876 22.0089 3.93941 23.1034 4.85067C23.0427 4.88379 22.937 4.94215 22.8668 4.98473L14.9024 9.58517C14.7025 9.69878 14.5366 9.86356 14.4215 10.0626C14.3065 10.2616 14.2466 10.4877 14.2479 10.7175L14.2424 21.9419ZM16.071 17.9991L20.4018 15.4978L24.7325 17.9975V22.9985L20.4018 25.4983L16.071 22.9985V17.9991Z"
                            fill="currentColor"></path>
                      </svg>
                    </div>

                    <!-- 多个消息 -->
                    <div v-if="msg?.speeches.length > 1"
                         class="text-xs flex items-center justify-center gap-1 invisible absolute left-0 top-2 -ml-4 -translate-x-full group-hover:visible">
                      <button @click.stop="last(msg)" :disabled="!(msg.idx > 0)"
                              class="dark:text-white disabled:text-gray-300 dark:disabled:text-gray-400">
                        <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                             stroke-linecap="round" stroke-linejoin="round" class="h-3 w-3" height="1em" width="1em"
                             xmlns="http://www.w3.org/2000/svg">
                          <polyline points="15 18 9 12 15 6"></polyline>
                        </svg>
                      </button>
                      <span class="flex-grow flex-shrink-0">{{ msg.idx + 1 }} / {{
                          msg.speeches.length
                        }}</span>
                      <button @click.stop="next(msg)" :disabled="!(msg.idx < msg.speeches.length - 1)"
                              class="dark:text-white disabled:text-gray-300 dark:disabled:text-gray-400">
                        <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                             stroke-linecap="round" stroke-linejoin="round" class="h-3 w-3" height="1em" width="1em"
                             xmlns="http://www.w3.org/2000/svg">
                          <polyline points="9 18 15 12 9 6"></polyline>
                        </svg>
                      </button>
                    </div>
                  </div>
                  <div class="relative flex w-[calc(100%-50px)] flex-col gap-1 md:gap-3 lg:w-[calc(100%-115px)]">
                    <div class="flex flex-grow flex-col gap-3">
                      <!--  whitespace-pre-wrap -->
                      <div class="min-h-[20px] flex flex-col items-start gap-4">
                        <div v-html="mdToHtml(msg?.speeches[msg.idx], msg)"
                             :class="{ 'result-streaming': msg.loading }"
                             class="markdown prose-r w-full break-words dark:prose-invert light">
                        </div>
                      </div>
                    </div>
                    <div class="flex justify-between">
                      <div
                          class="text-gray-400 flex self-end lg:self-center justify-center mt-2 gap-3 md:gap-4 lg:gap-1 lg:absolute lg:top-0 lg:translate-x-full lg:right-0 lg:mt-0 lg:pl-2 visible">
                        <button @click.stop="suitable(idx, msg, 1)"
                                v-if="msg.suitable[msg.idx] == 0 || msg.suitable[msg.idx] == 1"
                                :class="{ 'suitable_selected': msg.suitable[msg.idx] == 1 }"
                                class="p-1 rounded-md hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-gray-200 disabled:dark:hover:text-gray-400">
                          <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                               stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em"
                               width="1em" xmlns="http://www.w3.org/2000/svg">
                            <path
                                d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3">
                            </path>
                          </svg>
                        </button>
                        <button @click.stop="suitable(idx, msg, -1)"
                                v-if="msg.suitable[msg.idx] == 0 || msg.suitable[msg.idx] == -1"
                                :class="{ 'suitable_selected': msg.suitable[msg.idx] == -1 }"
                                class="p-1 rounded-md hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-gray-200 disabled:dark:hover:text-gray-400">
                          <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24"
                               stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4" height="1em"
                               width="1em" xmlns="http://www.w3.org/2000/svg">
                            <path
                                d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3zm7-13h2.67A2.31 2.31 0 0 1 22 4v7a2.31 2.31 0 0 1-2.33 2H17">
                            </path>
                          </svg>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </template>

            <div v-if="this.conversation?.msgList.length == 0"
                 class="text-gray-800 w-full md:max-w-2xl lg:max-w-3xl md:h-full md:flex md:flex-col px-6 dark:text-gray-100">
              <h1
                  class="text-4xl font-semibold text-center mt-6 sm:mt-[20vh] ml-auto mr-auto mb-10 sm:mb-16 flex gap-2 items-center justify-center">
                QuestAi</h1>
              <div class="md:flex items-start text-center gap-3.5">
                <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                  <h2 class="flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                    <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                         stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6" height="1em" width="1em"
                         xmlns="http://www.w3.org/2000/svg">
                      <circle cx="12" cy="12" r="5"></circle>
                      <line x1="12" y1="1" x2="12" y2="3"></line>
                      <line x1="12" y1="21" x2="12" y2="23"></line>
                      <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
                      <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
                      <line x1="1" y1="12" x2="3" y2="12"></line>
                      <line x1="21" y1="12" x2="23" y2="12"></line>
                      <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
                      <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
                    </svg>
                    Examples
                  </h2>
                  <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                    <button @click="inputChat('解释一下法律中的双重危险原则。')"
                            class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                      "解释一下法律中的双重危险原则" →
                    </button>
                    <button @click="inputChat('宪法保障的基本权利有哪些？')"
                            class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                      "宪法保障的基本权利有哪些？" →
                    </button>
                    <button @click="inputChat('如何提起民事诉讼？')"
                            class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                      "如何提起民事诉讼？" →
                    </button>
                    <button @click="inputChat('律师和法律顾问有什么区别？')"
                            class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                      "律师和法律顾问有什么区别？" →
                    </button>
                    <button @click="inputChat('申请专利的步骤是什么？')"
                            class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                      "申请专利的步骤是什么？" →
                    </button>
                  </ul>

                </div>
                <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                  <h2 class="flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" aria-hidden="true" class="h-6 w-6">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z">
                      </path>
                    </svg>
                    Capabilities
                  </h2>
                  <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      提供准确的法律解释和建议
                    </li>
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      快速回答关于法律条款的问题
                    </li>
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      维护用户隐私和保密
                    </li>
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      帮助用户准备法律文件和合同
                    </li>
                  </ul>

                </div>
                <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                  <h2 class="flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                    <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                         stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6" height="1em" width="1em"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                          d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z">
                      </path>
                      <line x1="12" y1="9" x2="12" y2="13"></line>
                      <line x1="12" y1="17" x2="12.01" y2="17"></line>
                    </svg>
                    Limitations
                  </h2>
                  <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      可能偶尔会生成不准确的信息
                    </li>
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      可能偶尔会产生有害的指令或带有偏见的内容
                    </li>
                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                      对2023年之后的事件和世界情况了解有限
                    </li>
                  </ul>

                </div>
              </div>
            </div>

            <div class="w-full h-32 md:h-48 flex-shrink-0"></div>
          </div>

          <transition name="el-fade-in-linear">
            <!-- 回到底部 -->
            <button v-show="isShowGoBottom" @click="handleScrollBottom"
                    class="cursor-pointer absolute right-6 bottom-[124px] md:bottom-[120px] z-10 rounded-full border border-gray-200 bg-gray-50 text-gray-600 dark:border-white/10 dark:bg-white/10 dark:text-gray-200">
              <svg stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 24 24" stroke-linecap="round"
                   stroke-linejoin="round" class="h-4 w-4 m-1" height="1em" width="1em"
                   xmlns="http://www.w3.org/2000/svg">
                   xmlns="http://www.w3.org/2000/svg">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <polyline points="19 12 12 19 5 12"></polyline>
              </svg>
            </button>
          </transition>
        </div>
      </div>
    </div>
  </main>

</template>

<style scoped>
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

</style>
