import Vue from 'vue'
import App from './App'
import store from './store' // 引入 vuex 的 store 对象
import {mapMutations} from 'vuex';

const app = new Vue({
	store,
	...App
})
app.$mount()
App.mpType = 'app'

Vue.config.productionTip = false

Vue.prototype.$store = store
Vue.prototype.$serverUrl = "http://127.0.0.1:8080"

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

/**
 * 自定义封装 socket 供全局调用
 * @author: Jerrio
 */
Vue.prototype.mySocket = {

	isOpen: false,
	socketMsgQueue: [], // 未发送的消息队列	

	init: function() {
		var that = this;
		// 创建websocket长连接
		uni.connectSocket({
			url: 'ws://localhost:8088/ws',
			complete: () => {}
		});

		uni.onSocketOpen(function(res) {
			that.isOpen = true;
			console.log('WebSocket连接已打开！isSocketOpen=' + that.isOpen);
			//发送连接消息，向服务器注册信息
			that.sendObj(app.netty.CONNECT, null, null);
			// 发送未发送的信息
			for (var i = 0; i < that.socketMsgQueue.length; i++) {
				// console.log(that.socketMsgQueue[i]);
				that.sendObj(app.netty.CHAT, that.socketMsgQueue[i], null);
			}
			that.socketMsgQueue = [];
		});

		uni.onSocketError(function(res) {
			console.log('WebSocket连接打开失败，请检查！');
		});
		
		// 已在index页面重写
// 		uni.onSocketMessage(function(res) {
// 			var dataContent = JSON.parse(res.data);
// 			var chatMessage = dataContent.chatMessage;
// 			console.log("收到服务器内容：");
// 			console.log(dataContent);
// 
// 			// 发送签收消息
// 			that.sendObj(app.netty.SIGNED, null, null, chatMessage.msgId);
// 
// 
// 			if (dataContent.action == app.netty.CHAT) {
// 				// 修改 store
// 				// that.setChatMessageCard(dataContent); 好像调用不了，决定在index页面重写
// 
// 				// 保存聊天历史记录到本地缓存
// 				var myId = chatMessage.receiverId;
// 				var friendId = chatMessage.senderId;
// 				var msg = chatMessage.msg
// 
// 				app.chat.saveUserChatHistory(myId, friendId, msg, app.chat.FRIEND);
// 			}
// 		});

		uni.onSocketClose(function(res) {
			that.isOpen = false;
			console.log('WebSocket 已关闭！isSocketOpen=' + that.isOpen);
			// 三秒一次重连
			console.log("重连中..");
			setTimeout(function() {
				that.init();
			}, 3000);
		});

	},
	/**
	 * 向服务器发送JSON数据对象
	 * @param {Object} type
	 * @param {Object} toUserId
	 * @param {Object} msg
	 * @param {Object} extand
	 */
	sendObj(type, toUserId, msg, extand) {
		var myUserId = app.getGlobalUserInfo().id; // 调用全局用户缓存，需要先请求获取
		if (app.isNull(myUserId)) {
			console.log("请先获取用户数据");
		}

		var chatMessage = new app.netty.ChatMessage(myUserId, toUserId, msg, null);
		var dataContent = new app.netty.DataContent(type, chatMessage, extand)

		var data = JSON.stringify(dataContent);
		var isSocketOpen = app.mySocket.isOpen;
		if (isSocketOpen == true) {
			console.log("isSocketOpen=" + isSocketOpen);
			uni.sendSocketMessage({
				data: data,
			});

			//保存聊天历史到 本地缓存
			if (type == app.netty.CHAT) {
				app.chat.saveUserChatHistory(myUserId, toUserId, msg, app.chat.ME);
			}
		} else {
			console.log("isSocketOpen=" + isSocketOpen);
			this.socketMsgQueue.push(data);
			console.log(this.socketMsgQueue);

		}
	},
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
