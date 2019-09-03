import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

var isNull = function(str) { // 数字0会被判定为true
	if (str == null || str == "" || str == undefined) {
		return true;
	}
	return false;
}

const store = new Vuex.Store({
	state: {
		chatMessageCard: '', // 暂存一条socket接收的聊天消息 & 刷新消息列表的条件
		flashChatPage: "doFlash", // 作为触发 chatPage 刷新的条件
		myMsgCount: uni.getStorageSync('myMsgCount'), // 左侧栏通用未读消息计数
	},
	mutations: {
		setChatMessageCard(state, value) {
			state.chatMessageCard = value;
		},

		doFlashChatPage(state, value) {
			// 获取当前时间，使数据变化
			state.flashChatPage = new Date().getTime();
		},

		setMyMsgCount(state, value) {
			if (value == undefined) {
				state.myMsgCount++;
				uni.setStorageSync('myMsgCount', state.myMsgCount);
				// console.log("value未传值，当前myMsgCount=" + state.myMsgCount);
			} else {
				state.myMsgCount = value;
				uni.setStorageSync('myMsgCount', state.myMsgCount);
				// console.log("获取到value值，当前myMsgCount=" + state.myMsgCount);
			}
		},
	},

})

export default store
