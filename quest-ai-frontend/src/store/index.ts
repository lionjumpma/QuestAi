import {createStore} from 'vuex'
import chat from './module/chat'
import page from './module/page'
// 创建一个新的 index 实例
const store = createStore({
    modules:{
        chat,
        page
    }
    }
)
export default store
