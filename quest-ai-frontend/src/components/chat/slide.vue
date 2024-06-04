<!--
@Author: 唐莺芸
@Date: 2024/4/23
@Description: 定义了一个 Vue 组件，用于创建一个可关闭的侧边导航栏。当 getShowSlide 计算属性为 true 时，导航栏会显示。
点击导航栏上的关闭按钮会关闭导航栏。这个组件通过事件总线 EventBus 与其它组件通信。-->
<script>
import {mapGetters, mapState} from "vuex";
import {EventBus} from "@/eventbus/eventbus.js";

export default {
  data() {
    return {
      // theme: "light",
      // popupShow: false,
      // avatarIdx: 1,
      // conversations: [],
      // conversation: [],
      // chatMsg: "",
      // chatTitle: "New chat",
      // convLoading: false,
      // showSlide: false,
      // isShowGoBottom: false,
      // oldConv: undefined,
      // convTitletmp: "",
      // source: undefined,
      // rsource: undefined,
      // tsource: undefined
    };
  },
  methods: {

    closeShowSlide() {
      this.showSlide = false;
      this.$refs.menu.appendChild(this.$refs.navEle);
    },
  },
  computed: {
    ...mapGetters([
          'getShowSlide'
        ]
    ),
  },
  mounted() {
    EventBus.$emit('slideNavContainer', this.$refs.slideNavContainer);
  }
}
</script>

<template>
  <div v-show="getShowSlide" class="semi-portal" style="z-index: 1000;">
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
                         class="flex h-full flex-1 items-start border-white/20">ll
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

</template>

<style scoped>

</style>
