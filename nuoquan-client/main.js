import Vue from 'vue'
import App from './App'


Vue.prototype.netty={
	/**
	 * 和后端的枚举对应
	 */
	CONNECT: 1,		// 第一次(或重连)初始化连接
	CHAT: 2,		// 聊天消息
	SIGNED: 3,		// 消息签收
	KEEPALIVE: 4, 	// 客户端保持心跳
	
	/**
	 * 和后端 ChatMessage 聊天模型的对象保持一致
	 * @param {Object} senderId
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} msgId
	 */
	ChatMessage: function(senderId, receiverId, msg, msgId){
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.msg = msg;
		this.msgId = msgId; // 前端不需要用到
	},
	
	/**
	 * 构建消息 DataContent 模型对象
	 * @param {Object} action
	 * @param {Object} chatMsg
	 * @param {Object} extand
	 */
	DataContent: function(action, chatMessage, extand){
		this.action = action;
		this.chatMessage = chatMessage;
		this.extand = extand;
	}
}

Vue.config.productionTip = false
Vue.prototype.SeverUrl="http://127.0.0.1:8080"

App.mpType = 'app'

Vue.prototype.setGlobalUserInfo = function(user) {
	uni.setStorageSync('userInfo', user);
}

Vue.prototype.getGlobalUserInfo = function() {
	// var value = uni.getStorageSync('userInfo');
	var value = {
		id: 'test-id123',
		nickname: 'testname'
	}
	return value;
}

Vue.prototype.removeGlobalUserInfo = function() {
	uni.removeStorageSync('userInfo');
}

const app = new Vue({
    ...App
})
app.$mount()


