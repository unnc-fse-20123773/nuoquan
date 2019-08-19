import Vue from 'vue'
import App from './App'

const app = new Vue({
	...App
})
app.$mount()

Vue.config.productionTip = false
Vue.prototype.SeverUrl = "http://127.0.0.1:8080"

App.mpType = 'app'

Vue.prototype.setGlobalUserInfo = function(user) {
	uni.setStorageSync('userInfo', user);
}

Vue.prototype.getGlobalUserInfo = function() {
	var value = uni.getStorageSync('userInfo');
	return value;
}

Vue.prototype.removeGlobalUserInfo = function() {
	uni.removeStorageSync('userInfo');
}

/**
 * 从服务器查询用户信息, 并拼接额外信息。
 * [还不完善，需要设置同步否则得不到值，暂时弃用，请手动调用myUser拼接]
 */
// Vue.prototype.myQueryUserInfo = function(userId) {
// 	var that = this;
// 	var finalUser;
// 	uni.request({
// 		url: 'http://127.0.0.1:8080/user/queryUser',
// 		method: "POST",
// 		data: {
// 			userId: userId
// 		},
// 		header: {
// 			'content-type': 'application/x-www-form-urlencoded'
// 		},
// 		success: (res) => {
// 			// console.log(res)
// 			if (res.data.status == 200) {
// 				// 3.获取返回的用户信息
// 				finalUser = res.data.data;
// 				// 4.分割邮箱地址, 重构user
// 				finalUser = that.myUser(finalUser);
// 			}
// 		}
// 	});
// 	
// 	console.log(finalUser);
// 	return finalUser;
// }

/**
 * 分割邮箱地址, 重构user
 * @param {Object} user
 */
Vue.prototype.myUser = function(user) {
	// 分割邮箱地址
	var email = user.email;
	if (this.isNull(email)) {
		email = "[Email]@nottingham.edu.cn";
	}
	var list = email.split('@');
	user.emailPrefix = list[0];
	user.emailSuffix = "@" + list[1];

	return user
}

Vue.prototype.isNull = function(str) {
	if (str == null || str == "" || str == undefined) {
		return true;
	}
	return false;
}


Vue.prototype.chat = {

	ME: 1, // 我的消息-右边
	FRIEND: 2, // 对方消息-左边

	/**
	 * 保存用户的聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag 判断本条消息是谁发送的, 1：我 2：朋友
	 */
	saveUserChatHistory: function(myId, friendId, msg, flag) {

		var chatKey = "chat-" + myId + "-" + friendId;
		// 从本地缓存获取聊天记录是否存在
		var chatHistoryListStr = uni.getStorageSync(chatKey);
		var chatHistoryList;
		if (app.isNull(chatHistoryListStr)) {
			// 为空，赋一个空的list；
			chatHistoryList = [];
		} else {
			// 不为空
			chatHistoryList = JSON.parse(chatHistoryListStr);
		}

		// 构建聊天记录对象
		var singleMsg = new this.ChatHistory(myId, friendId, msg, flag);
		// 添加到list尾部
		chatHistoryList.push(singleMsg);

		uni.setStorageSync(chatKey, JSON.stringify(chatHistoryList));

	},

	getUserChatHistory: function(myId, friendId) {
		var chatKey = "chat-" + myId + "-" + friendId;
		var chatHistoryListStr = uni.getStorageSync(chatKey);
		var chatHistoryList;
		if (app.isNull(chatHistoryListStr)) {
			// 为空，赋一个空的list；
			chatHistoryList = [];
		} else {
			// 不为空
			chatHistoryList = JSON.parse(chatHistoryListStr);
		}

		return chatHistoryList;
	},

	ChatHistory: function(myId, friendId, msg, flag) {
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.flag = flag;
	}

}

Vue.prototype.netty = {
	/**
	 * 和后端的枚举对应
	 */
	CONNECT: 1, // 第一次(或重连)初始化连接
	CHAT: 2, // 聊天消息
	SIGNED: 3, // 消息签收
	KEEPALIVE: 4, // 客户端保持心跳

	/**
	 * 和后端 ChatMessage 聊天模型的对象保持一致
	 * @param {Object} senderId
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} msgId
	 */
	ChatMessage: function(senderId, receiverId, msg, msgId) {
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
	DataContent: function(action, chatMessage, extand) {
		this.action = action;
		this.chatMessage = chatMessage;
		this.extand = extand;
	},

}
