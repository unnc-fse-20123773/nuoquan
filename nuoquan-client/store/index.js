import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		ChatMessageCard: '', // 暂存一条socket接收的消息
	},
	mutations: {
		setChatMessageCard(state, value) {
			state.ChatMessageCard = value;
		}
	}
})

export default store
