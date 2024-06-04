import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "@/router";
import "./assets/tailwind.css";
import store from "@/store";
import eventBus from 'vue3-eventbus'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'



createApp(App).use(eventBus)
    .use(store)
    .use(ElementPlus)
    .use(router).mount('#app')
