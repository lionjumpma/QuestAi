// import {createStore} from "vuex";


const  page ={
    state:{
        showSlide: false,
        isShowGoBottom: false,
    },
    mutations:{
        setShowSlide(state, showSlide){
            state.showSlide = showSlide;
        },
        setIsShowGoBottom(state, isShowGoBottom){
            state.isShowGoBottom = isShowGoBottom;
        }
    },
    actions:{
        setShowSlide({commit}, showSlide){
            commit('setShowSlide', showSlide);
        },
        setIsShowGoBottom({commit}, isShowGoBottom){
            commit('setIsShowGoBottom', isShowGoBottom);
        }
    },
    getters:{
        getShowSlide: state => state.showSlide,
        getIsShowGoBottom: state => state.isShowGoBottom,
    }
}
export default page
