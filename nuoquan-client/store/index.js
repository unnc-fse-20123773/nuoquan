import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		chatMessageCard: '', // 暂存一条socket接收的消息
		flashChatPage: "doFlash", // 作为触发 chatPage 刷新的条件
	},
	mutations: {
		setChatMessageCard(state, value) {
			state.chatMessageCard = value;
		},
		
		doFlashChatPage(state, value){
			// 获取当前时间，使数据变化
			state.flashChatPage = new Date().getTime();
		}
	}
})

export default store
