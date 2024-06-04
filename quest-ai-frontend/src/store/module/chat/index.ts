// import {createStore} from 'vuex'
import {convListType, convType, messageType, stateType} from "@/types/chat";

// 创建一个新的 index 实例
const chat = {
    state: ():stateType=>({
        conversation: {
            id: 0,
            title: "",
            msgList: [],
        },
        chatStatus: "available",
        chatMsg: "",
        conversationList: [],
        selectedIdx: -1
    }),
    mutations: {
        addToConversation(state:stateType, message:messageType) {
            console.log("addToConversation",state.conversation)
            state.conversation.msgList.push(message);

        },
        setConversation(state:stateType, conversation:convType) {
            //console.log("setConversation",conversation)
            state.conversation = conversation;
        },
        setChatStatus(state:stateType, status:"chatting"|"available") {
            state.chatStatus = status;
        },
        setChatMsg(state:stateType, msg:string) {
            state.chatMsg = msg;
        },
        setConversationList(state:stateType, conversationList:convType[]) {
            state.conversationList = conversationList;
        },
        unshiftConv(state:stateType, conversation:convType) {
            if (state.conversationList != null)
            state.conversationList.push(conversation);
        },
        setConversationByIndex(state:stateType, data:{cidx: number, conv: convType}) {
            var index = data.cidx;
            var newConv = data.conv;
            if (state.conversationList.length>index) {
                state.conversationList.splice(index, 1, newConv);
                //console.log("setConversationByIndex",index,newConv)
            }
            //console.log("sconv",state.conversationList?.length,index,newConv)
            //console.log("setConversationByIndex",index,newConv)
        },


        delConversationByIndex(state:stateType, index:number) {
            if (state.conversationList != null&&state.conversationList.length>index)
            state.conversationList.splice(index, 1);

            else console.log("delConversationByIndex fail",state.conversationList,index);
        },
        setSelectedIdx(state:stateType, index:number) {
            state.selectedIdx = index;
        },

        getConvIndex(state:stateType, conversation:convType) {
            var idx = state.conversationList.findIndex((conv)=>conv.id==conversation.id);
            return idx;
        }
    },
    actions: {
        setChatMsg({commit}, msg:string) {
            commit('setChatMsg', msg);
        },
        addMessage({commit}, message) {
            commit('addToConversation', message);
        },
        setConversation({commit}, conversation:convType) {
            commit('setConversation', conversation);

        },
        setChatStatus({commit}, status) {
            commit('setChatStatus', status);
        },
        setConversationList({commit}, conversationList) {
            commit('setConversationList', conversationList);
        },
        unshiftConversationList({commit}, conversation:convType) {
            commit('unshiftConv', conversation);
        },
        setConversationByIndex({commit}, data:{cidx: number, conv: convType}) {
            commit('setConversation', data.conv);
            commit('setConversationByIndex', data);
        },
        delConversationByIndex({commit}, index) {
            commit('delConversationByIndex', index);
        },
        setSelectedIdx({commit}, index) {
            commit('setSelectedIdx', index);
        },
        updateConversation({commit}, conversation:convType) {
            var idx = commit('getConvIndex', conversation);
            commit("setConversationByIndex", {cidx: idx, conv: conversation});
        }
    },
    getters: {
        getConversation: (state: stateType) => state.conversation,
        getChatMsg: (state: stateType) => state.chatMsg,
        getConversationList: (state: stateType) => state.conversationList,
        //传入索引号，返回对应的会话
        getConversationByIndex: (state:stateType) => (index) => {
            return state.conversationList[index];
        },
        getMessageList: (state: stateType) => state.conversation?.msgList,
    }
}

export default chat
