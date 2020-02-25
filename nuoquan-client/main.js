import Vue from 'vue'
import App from './App'
import store from './store' // 引入 vuex 的 store 对象
import router from 'common/router.js'

const app = new Vue({
	store,
	...App
})
app.$mount()
App.mpType = 'app'

Vue.config.productionTip = false

Vue.prototype.version = "v1.0.3 - 公测版"
Vue.prototype.tagColors = ['#FE5F55','#40A792','#FDD041','#5CA0D3','#621E81','#738598','#F3AE4B']

Vue.prototype.$store = store // 挂载 vueX

Vue.prototype.$serverUrl = "http://127.0.0.1:8080"
Vue.prototype.$wsServerUrl = "wss://127.0.0.1:8088/ws"

// 服务器地址
// Vue.prototype.$serverUrl = "http://129.28.130.27:8080/nottinghome"
// Vue.prototype.$wsServerUrl = "ws://129.28.130.27:8088/ws"

// 安全服务器地址
// Vue.prototype.$serverUrl = "https://www.checkchack.cn:8443/nottinghome"
// Vue.prototype.$wsServerUrl = "wss://www.checkchack.cn:8088/ws"

/**
 * 获取当前用户信息（我）
 * @param {Object} user
 */
Vue.prototype.setGlobalUserInfo = function(user) {
	uni.setStorageSync('userInfo', user);
}

/**
 * 设置当前用户信息
 */
Vue.prototype.getGlobalUserInfo = function() {
	var value = uni.getStorageSync('userInfo');
	return value;
}

/**
 * 清空当前用户信息
 */
Vue.prototype.removeGlobalUserInfo = function() {
	uni.removeStorageSync('userInfo');
}

/**
 * 把对象添加到列表尾部，并储存在缓存里
 * @param {Object} obj
 * @param {Object} list
 */
Vue.prototype.setIntoList = function(obj, listName) {
	var listStr = uni.getStorageSync(listName)
	// 从本地缓存获取列表名是否存在
	var list;
	if (app.isNull(listStr)) {
		// 为空，赋一个空的list；
		list = [];
	} else {
		// 不为空
		list = JSON.parse(listStr);
	}
	// 插入对象到尾部
	list.push(obj);
	uni.setStorageSync(listName, JSON.stringify(list));
}
/**
 * 把对象添加到列表头部，并储存在缓存里
 * @param {Object} obj
 * @param {Object} listName
 */
Vue.prototype.addIntoList = function(obj, listName) {
	var listStr = uni.getStorageSync(listName)
	// 从本地缓存获取列表名是否存在
	var list;
	if (app.isNull(listStr)) {
		// 为空，赋一个空的list；
		list = [];
	} else {
		// 不为空
		list = JSON.parse(listStr);
	}
	// 插入对象到头部
	list.unshift(obj);
	uni.setStorageSync(listName, JSON.stringify(list));
}
/**
 * 从缓存中按名字获取改名列表
 * @param {Object} listName
 */
Vue.prototype.getListByKey = function(listName){
	var listStr = uni.getStorageSync(listName);
	var list;
	if (app.isNull(listStr)) {
		// 为空，赋一个空的list；
		list = [];
	} else {
		// 不为空
		list = JSON.parse(listStr);
	}
	
	return list;
}


/** TODO: 可使用 Map 代替 List 提升查询性能（暂时不知道 map 在 uniapp 中怎么写）
 *	 															by Jerrio
 * 把该用户信息添加到本地缓存的 userlist 中，如果存在则替换
 * @param {Object} userInfo
 */
Vue.prototype.setUserInfoToUserList = function(userInfo) {
	var userListStr = uni.getStorageSync("userList");
	var userList;
	if (app.isNull(userListStr)) {
		// 为空，赋一个空的list；
		userList = [];
	} else {
		// 不为空，查看该用户是否存在
		var userList = JSON.parse(userListStr);
		for (var i = 0; i < userList.length; i++) {
			var user = userList[i];
			if (user.id == userInfo.id) {
				userList.splice(i, 1, userInfo); // 替换
				uni.setStorageSync("userList", JSON.stringify(userList));
				return;
			}
		}
	}
	// 用户不存在
	userList.push(userInfo);
	uni.setStorageSync("userList", JSON.stringify(userList));
}

/**
 * 根据 userId，在本地缓存中获取该用户信息
 * @param {Object} userId
 */
Vue.prototype.getUserInfoFromUserList = function(userId) {
	var userListStr = uni.getStorageSync("userList");

	if (app.isNull(userListStr)) {
		// 为空，直接返回 null
		return null;
	} else {
		// 不为空
		var userList = JSON.parse(userListStr);
		for (var i = 0; i < userList.length; i++) {
			var user = userList[i];
			if (user.id == userId) {
				return user;
			}
		}
	}
	return null;
}

/**
 * 返回页面栈最后一页(当前页面)
 */
Vue.prototype.getCurrentPage = function() {
	var pages = getCurrentPages();
	var currentPage = pages[pages.length - 1];
	return currentPage;
}
/**
 * 从服务器查询用户信息, 并拼接额外信息。
 * [需要设置同步否则得不到值，还未测试，暂时弃用，请手动调用myUser拼接]
 */
// Vue.prototype.myQueryUserInfo = async function(userId) {
// 	var that = this;
// 	var finalUser;
// 	uni.request({
// 		url: that.$serverUrl + '/user/queryUser',
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
// 			console.log("里面");
// 			console.log(finalUser);
// 			return finalUser;
// 		}
// 	});
// 	
// 	// app.$nextTick(function(){
// 	// 	console.log(finalUser);
// 	// })
// 	
// 	// console.log("里面2");
// 	// console.log(finalUser);
// 	// return finalUser;
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
 * "" = true;
 * " " = true;
 */
Vue.prototype.isBlank = function(str) {
	return RegExp(/^\s*$/).test(str);
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
			url: app.$wsServerUrl,
			complete: () => {}
		});

		uni.onSocketOpen(function(res) {
			that.isOpen = true;
			console.log('WebSocket连接已打开！isSocketOpen=' + that.isOpen);
			//发送连接消息，向服务器注册信息
			var myUserId = app.getGlobalUserInfo().id; // 调用全局用户缓存，需要先请求获取
			if (app.isNull(myUserId)) {
				console.log("请先获取用户数据");
				return;
			}
			var dataContent = new app.netty.DataContent(app.netty.CONNECT, null, myUserId);
			that.sendDataContent(dataContent);
			// 发送未发送的信息
			for (var i = 0; i < that.socketMsgQueue.length; i++) {
				if (that.socketMsgQueue[i].action == app.netty.CHAT){
					that.sendDataContent(that.socketMsgQueue[i]);
				}
			}
			that.socketMsgQueue = [];

			// 签收未签收消息
			app.chat.fetchUnsignedChatMsg(); // 获取聊天信息
			app.notification.fetchUnsignedLikeMsg(); // 获取点赞通知
			app.notification.fetchUnsignedCommentMsg(); // 获取评论通知
			// 定时发送心跳
			setInterval(that.keepAlive, 50000);
		});

		uni.onSocketError(function(res) {
			console.log('WebSocket连接打开失败，请检查！');
		});

		uni.onSocketMessage(function(res) {
			var dataContent = JSON.parse(res.data);
			console.log("收到服务器内容：");
			console.log(dataContent);
			var action = dataContent.action;

			if (action == app.netty.CHAT 
				|| action == app.netty.LIKEARTICLE 
				|| action == app.netty.LIKECOMMENT
				|| action == app.netty.COMMENTARTICLE
				|| action == app.netty.COMMENTCOMMENT) {
				
				app.$store.commit('setMyMsgCount'); // 累加 msgCount in index.js
				
				switch (action){
					case app.netty.CHAT: // 如果消息类型为 CHAT
						var chatMessage = dataContent.data;
						// 发送签收消息
						that.signMsgList(chatMessage.msgId);
						
						// 保存聊天历史记录到本地缓存
						var myId = chatMessage.receiverId;
						var friendId = chatMessage.senderId;
						var msg = chatMessage.msg;
						var createDate = chatMessage.createDate; // 对时间戳进行格式化
						
						app.chat.saveUserChatHistory(myId, friendId, msg, app.chat.FRIEND, createDate);
						
						// 判断当前页面，保存聊天快照
						var page = app.getCurrentPage();
						if (page.route == 'pages/chatpage/chatpage') {
							var pageFriendId = page.data.friendInfo.id; // 打开页面才有对象，越层判断会报对象为空的错
							/* 
							【BUG】ISO 虚拟机获取 friendInfo.id 报错信息：
							 undefined is not an object (evaluating 'page.data.friendInfo.id')
							 不知道真机会不会有这种情况
							*/
							// var pagestr = JSON.stringify(page.data);
							// console.log(pagestr);
							if (pageFriendId == friendId) {
								// 与该用户在聊天，标记为已读
								console.log("与该用户在聊天，标记为已读");
								app.chat.saveUserChatSnapshot(myId, friendId, msg, app.chat.READ, createDate);
								
								// 修改 store，发送信号，把消息卡片渲染到对话窗口 和 消息列表
								var newMessage = new app.chat.ChatHistory(myId, friendId, msg, app.chat.FRIEND, createDate);
								app.$store.commit('setChatMessageCard', newMessage);
							}else{
								//不是与该用户聊天，标记为未读
								console.log("不是与该用户聊天，标记为未读");
								app.chat.saveUserChatSnapshot(myId, friendId, msg, app.chat.UNREAD, createDate);
							}
						} else {
							// 聊天页面未打开，标记为未读
							console.log("聊天页面未打开，标记为未读");
							app.chat.saveUserChatSnapshot(myId, friendId, msg, app.chat.UNREAD, createDate);
						}
						break;
					case app.netty.LIKEARTICLE:
						console.log("获取点赞文章");
						// 签收消息
						that.signLikeArticleList(dataContent.data.source.id);
						// 存入缓存 (TODO：登陆时获取未签收点赞消息)
						app.notification.saveLikeMsg(dataContent);
						app.$store.commit('setLikeMsgCount');
						break;
					case app.netty.LIKECOMMENT:
						console.log("获取点赞评论");
						// 签收消息
						that.signLikeCommentList(dataContent.data.source.id);
						// 存入缓存
						app.notification.saveLikeMsg(dataContent);
						app.$store.commit('setLikeMsgCount');
						break;
					case app.netty.COMMENTARTICLE:
						console.log("获取评论文章");
						// 签收消息
						that.signCommentList(dataContent.data.source.id);
						// 存入缓存
						app.notification.saveCommentMsg(dataContent);
						app.$store.commit('setCommentMsgCount');
						break;
					case app.netty.COMMENTCOMMENT:
						console.log("获取评论评论");
						// 签收消息
						that.signCommentList(dataContent.data.source.id);
						// 存入缓存
						app.notification.saveCommentMsg(dataContent);
						app.$store.commit('setCommentMsgCount');
						break;
					default:
						break;
				}
			}
		});

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
	 * 向 netty 服务器发送 聊天信息的方法 sendChatObj
	 * @param {Object} type
	 * @param {Object} toUserId
	 * @param {Object} msg
	 * @param {Object} extand
	 */
	sendObj(action, toUserId, msg, extand) {
		var myUserId = app.getGlobalUserInfo().id; // 调用全局用户缓存，需要先请求获取
		if (app.isNull(myUserId)) {
			console.log("请先获取用户数据");
			return;
		}

		// 获取当前时间戳，传输时间戳
		var timeStamp = new Date().getTime();

		// 构建载体
		var chatMessage = new app.netty.ChatMessage(myUserId, toUserId, msg, null, timeStamp);
		var dataContent = new app.netty.DataContent(action, chatMessage, extand);

		var isSocketOpen = app.mySocket.isOpen;
		if (isSocketOpen == true) {
			this.sendDataContent(dataContent);
		} else {
			// 加到待发送列表
			console.log("isSocketOpen=" + isSocketOpen);
			this.socketMsgQueue.push(dataContent);
			console.log(this.socketMsgQueue);
		}
	},
	
	/**
	 * 向 netty 服务器发送 DataConten 对象
	 * @param {Object} dataContent
	 */
	sendDataContent(dataContent) {
		var data = JSON.stringify(dataContent);
		uni.sendSocketMessage({
			data: data,
		});
		
		if (dataContent.action == app.netty.CHAT) {
			// 保存聊天历史到本地缓存，保存聊天快照到本地
			var chatMessage = dataContent.data;
			
			// console.log("发消息的时间戳：" + createDate);
			app.chat.saveUserChatHistory(chatMessage.senderId, 
										 chatMessage.receiverId, 
										 chatMessage.msg, 
										 app.chat.ME, 
										 chatMessage.createDate);
			app.chat.saveUserChatSnapshot(chatMessage.senderId, 
										  chatMessage.receiverId, 
										  chatMessage.msg, 
										  app.chat.READ, 
										  chatMessage.createDate);
			// 刷到对话窗口
			app.$store.commit('doFlashChatPage');
		}
	},

	/**
	 * 批量签收聊天消息
	 * @param {Object} msgIds
	 */
	signMsgList(msgIds) {
		var dataContent = new app.netty.DataContent(app.netty.SIGNED, null, msgIds);
		this.sendDataContent(dataContent);
	},
	
	/**
	 * 批量签收点赞文章消息
	 * @param {Object} msgIds
	 */
	signLikeArticleList(msgIds) {
		var dataContent = new app.netty.DataContent(app.netty.LIKEARTICLE_SIGN, null, msgIds);
		this.sendDataContent(dataContent);
	},
	
	/**
	 * 批量签收点赞评论消息
	 * @param {Object} msgIds
	 */
	signLikeCommentList(msgIds) {
		var dataContent = new app.netty.DataContent(app.netty.LIKECOMMENT_SIGN, null, msgIds);
		this.sendDataContent(dataContent);
	},
	
	/**
	 * 批量签收评论消息
	 * @param {Object} msgIds
	 */
	signCommentList(msgIds) {
		var dataContent = new app.netty.DataContent(app.netty.COMMENT_SIGN, null, msgIds);
		this.sendDataContent(dataContent);
	},

	/**
	 * 发送心跳
	 */
	keepAlive() {
		// 用 setInterval 调用时，使用 this 获取不到实例，故用 app
		var dataContent = new app.netty.DataContent(app.netty.KEEPALIVE, null, null);
		app.mySocket.sendDataContent(dataContent);
	},
}

Vue.prototype.chat = {

	ME: 1, // 我的消息-右边
	FRIEND: 2, // 对方消息-左边
	READ: 3,
	UNREAD: 4,
	/**
	 * 历史记录对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag 是我的消息还是朋友的消息
	 * @param {Object} createDate
	 */
	ChatHistory: function(myId, friendId, msg, flag, createDate) {
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.flag = flag;
		this.createDate = createDate;
	},

	/**
	 * 快照对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead 用于判断消息是已读还是未读
	 * @param {Object} createDate
	 */
	ChatSnapshot: function(myId, friendId, msg, isRead, createDate) {
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.isRead = isRead;
		this.createDate = createDate;
	},
	/**
	 * 保存用户的聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag 判断本条消息是谁发送的, 1：我 2：朋友
	 * @param {Object} createDate
	 */
	saveUserChatHistory: function(myId, friendId, msg, flag, createDate) {

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
		var singleMsg = new this.ChatHistory(myId, friendId, msg, flag, createDate);

		// 添加到list尾部
		chatHistoryList.push(singleMsg);

		uni.setStorageSync(chatKey, JSON.stringify(chatHistoryList));

	},
	
	/**
	 * 分页获取聊天历史，从列表尾部开始读取(反取)
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} page
	 */
	getUserChatHistory: function(myId, friendId, page) {
		var chatKey = "chat-" + myId + "-" + friendId;
		var list = app.getListByKey(chatKey).reverse();
		var size = 20;
		var start = (page-1) * size;
		var newList = [];
		if (list.length < start){
			return null;
		}else{
			for (var i=0; i<size; i++){
				if(!app.isNull(list[start+i])){
					newList.unshift(list[start+i]);
				}
			}
			return newList;
		}
	},
	
	/**
	 * 删除我和朋友的聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	deletUserChatHistory: function(myId, friendId) {
		var chatKey = "chat-" + myId + "-" + friendId;
		uni.removeStorageSync(chatKey);
	},

	/**
	 * 聊天记录快照，仅保存每次和朋友聊天的最后一条消息
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead
	 * @param {Object} createDate
	 */
	saveUserChatSnapshot: function(myId, friendId, msg, isRead, createDate) {

		var chatKey = "chat-snapshot" + myId;

		// 从本地缓存获取聊天快照的 list
		var chatSnapshotListStr = uni.getStorageSync(chatKey);
		var chatSnapshotList;
		if (app.isNull(chatSnapshotListStr)) {
			// 为空，赋一个空的list；
			chatSnapshotList = [];
		} else {
			// 不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 循环快照list，删除含 friendId 的项
			for (var i = 0; i < chatSnapshotList.length; i++) {
				if (chatSnapshotList[i].friendId == friendId) {
					chatSnapshotList.splice(i, 1); // 从i项往后删，只删一个
					break;
				}
			}
		}
		// 构建聊天快照对象
		var singleMsg = new this.ChatSnapshot(myId, friendId, msg, isRead, createDate);
		// 添加到 list 第一项
		chatSnapshotList.unshift(singleMsg);

		uni.setStorageSync(chatKey, JSON.stringify(chatSnapshotList));
	},

	/**
	 * 获取用户快照记录列表
	 */
	getUserChatSnapShot: function(myId) {
		var chatKey = "chat-snapshot" + myId;

		// 从本地缓存获取聊天快照的 list
		var chatSnapshotListStr = uni.getStorageSync(chatKey);
		var chatSnapshotList;
		if (app.isNull(chatSnapshotListStr)) {
			// 为空，赋一个空的list；
			chatSnapshotList = [];
		} else {
			// 不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
		}

		return chatSnapshotList;
	},

	/**
	 * 删除与该用户的聊天快照记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	deletUserChatSnapShot: function(myId, friendId) {
		var chatKey = "chat-snapshot" + myId;
		// 从本地缓存获取聊天快照的 list
		var chatSnapshotListStr = uni.getStorageSync(chatKey);
		var chatSnapshotList;
		if (app.isNull(chatSnapshotListStr)) {
			// 为空，不作处理
			return;
		} else {
			// 不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 循环快照list，删除含 friendId 的项
			for (var i = 0; i < chatSnapshotList.length; i++) {
				if (chatSnapshotList[i].friendId == friendId) {
					chatSnapshotList.splice(i, 1); // 从i项往后删，只删一个
					break;
				}
			}
		}

		uni.setStorageSync(chatKey, JSON.stringify(chatSnapshotList));
	},

	/**
	 * 把快照标记为已读，并且不改变在列表中的位置
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	readUserChatSnapShot: function(myId, friendId) {
		var chatKey = "chat-snapshot" + myId;

		// 从本地缓存获取聊天快照的 list
		var chatSnapshotListStr = uni.getStorageSync(chatKey);
		var chatSnapshotList;
		if (app.isNull(chatSnapshotListStr)) {
			// 为空，赋一个空的list；
			return;
		} else {
			// 不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 删除快照对象并放入新的在原有位置
			for (var i = 0; i < chatSnapshotList.length; i++) {
				var item = chatSnapshotList[i];
				if (item.friendId == friendId) {
					item.isRead = this.READ;
					chatSnapshotList.splice(i, 1, item); // 替换
					break;
				}
			}

			uni.setStorageSync(chatKey, JSON.stringify(chatSnapshotList));
		}
	},

	fetchUnsignedChatMsg: function() {
		var user = app.getGlobalUserInfo();
		var msgIds = "," // 格式: ,1001,1002,1003,
		var that = this;
		uni.request({
			url: app.$serverUrl + '/user/getUnsignedMsg',
			method: "POST",
			data: {
				userId: user.id
			},
			header: {
				'content-type': 'application/x-www-form-urlencoded'
			},
			success: (res) => {
				// console.log(res)
				if (res.data.status == 200) {
					var unsignedMsgList = res.data.data;
					console.log(unsignedMsgList);
					if (!app.isNull(unsignedMsgList)) {
						app.$store.commit('setMyMsgCount', unsignedMsgList.length); // 增加 msgCount in index.js
						for (var i = 0; i < unsignedMsgList.length; i++) {
							var msgObj = unsignedMsgList[i];
							// 1.逐条存入聊天记录
							this.saveUserChatHistory(msgObj.acceptUserId,
								msgObj.sendUserId,
								msgObj.msg,
								this.FRIEND,
								msgObj.createDate);
							// 2.保存聊天快照到本地
							this.saveUserChatSnapshot(msgObj.acceptUserId,
								msgObj.sendUserId,
								msgObj.msg,
								this.UNREAD,
								msgObj.createDate);
							// 3.拼接批量签收id的字符串
							msgIds += msgObj.id + ",";
						}

						// 调用批量签收方法
						app.mySocket.signMsgList(msgIds);
					}
				}
			}
		});
	}
}

/**
 * 点赞评论通知
 */
Vue.prototype.notification={	
	LIKEMSG_KEY : "likeMsg" + app.getGlobalUserInfo().id,
	COMMENTMSG_KEY : "commentMsg" + app.getGlobalUserInfo().id,
	/**
	 * 把点赞通知存入缓存
	 * @param {Object} dataContent
	 */
	saveLikeMsg(dataContent){
		app.addIntoList(dataContent, this.LIKEMSG_KEY);
	},
	
	getLikeMsg(page){
		var size = 10;
		var list = app.getListByKey(this.LIKEMSG_KEY);
		var start = (page-1) * size;
		var newList = [];
		if (list.length < start){
			return null;
		}else{
			for (var i=0; i<size; i++){
				if(!app.isNull(list[start+i])){
					newList.push(list[start+i]);
				}
			}
			return newList;
		}
	},
	
	/**
	 * 把评论通知存入缓存
	 * @param {Object} dataContent
	 */
	saveCommentMsg(dataContent){
		app.addIntoList(dataContent, this.COMMENTMSG_KEY);
	},
	
	getCommentMsg(page){
		var size = 10;
		var list = app.getListByKey(this.COMMENTMSG_KEY);
		var start = (page-1) * size;
		var newList = [];
		if (list.length < start){
			return null;
		}else{
			for (var i=0; i<size; i++){
				if(!app.isNull(list[start+i])){
					newList.push(list[start+i]);
				}
			}
			return newList;
		}
	},
	
	/**
	 * 获取并签收未签收的点赞通知
	 */
	fetchUnsignedLikeMsg(){
		var user = app.getGlobalUserInfo();
		var likeArticleIds = "," // 格式: ,1001,1002,1003,
		var likeCommentIds = ","
		var that = this;
		uni.request({
			url: app.$serverUrl + '/article/getUnsignedLikeMsg',
			method: "POST",
			data: {
				userId: user.id
			},
			header: {
				'content-type': 'application/x-www-form-urlencoded'
			},
			success: (res) => {
				// console.log(res)
				if (res.data.status == 200) {
					var unsignedMsgList = res.data.data;
					console.log(unsignedMsgList);
					if (!app.isNull(unsignedMsgList)) {
						for (var i = 0; i < unsignedMsgList.length; i++) {
							var dataContent = unsignedMsgList[i];
							//累加信息
							app.$store.commit('setMyMsgCount'); // 累加通用信息
							app.$store.commit('setLikeMsgCount'); //累加点赞信息
							// 逐条存入缓存
							app.notification.saveLikeMsg(dataContent);
							// 拼接批量签收id的字符串
							switch (dataContent.action){
								case app.netty.LIKEARTICLE:
									likeArticleIds += dataContent.data.source.id + ",";
									break;
								case app.netty.LIKECOMMENT:
									likeCommentIds += dataContent.data.source.id + ",";
									break;
								default:
									break;
							}
						}
		
						// 调用批量签收方法
						app.mySocket.signLikeArticleList(likeArticleIds);
						app.mySocket.signLikeCommentList(likeCommentIds);
					}
				}
			}
		});
	},
	
	/**
	 * 获取并签收未签收的评论通知
	 */
	fetchUnsignedCommentMsg(){
		var user = app.getGlobalUserInfo();
		var commentIds = "," // 格式: ,1001,1002,1003,
		var that = this;
		uni.request({
			url: app.$serverUrl + '/article/getUnsignedCommentMsg',
			method: "POST",
			data: {
				userId: user.id
			},
			header: {
				'content-type': 'application/x-www-form-urlencoded'
			},
			success: (res) => {
				// console.log(res)
				if (res.data.status == 200) {
					var unsignedMsgList = res.data.data;
					console.log(unsignedMsgList);
					if (!app.isNull(unsignedMsgList)) {
						for (var i = 0; i < unsignedMsgList.length; i++) {
							var dataContent = unsignedMsgList[i];
							//累加信息
							app.$store.commit('setMyMsgCount'); // 累加通用信息
							app.$store.commit('setCommentMsgCount'); //累加评论信息
							// 逐条存入缓存
							app.notification.saveCommentMsg(dataContent);
							// 拼接批量签收id的字符串
							commentIds += dataContent.data.source.id + ",";
						}
		
						// 调用批量签收方法
						app.mySocket.signCommentList(commentIds);
					}
				}
			}
		});
	}
}

Vue.prototype.netty = {
	/**
	 * 和后端的枚举对应
	 */
	CONNECT: 1, 			// 第一次(或重连)初始化连接
	CHAT: 2,				// 聊天消息
	SIGNED: 3, 				// 消息签收
	KEEPALIVE: 4, 			// 客户端保持心跳
	LIKEARTICLE: 5, 		// 点赞文章通知
	LIKECOMMENT: 6, 		// 点赞评论通知
	COMMENTARTICLE: 7, 		// 评论文章通知
	COMMENTCOMMENT: 8,		// 评论评论通知
	LIKEARTICLE_SIGN: 9, 	// 签收点赞文章通知
	LIKECOMMENT_SIGN: 10, 	// 签收点赞评论通知
	COMMENT_SIGN: 11, 		// 签收评论文章通知

	/**
	 * 和后端 ChatMessage 聊天模型的对象保持一致
	 * @param {Object} senderId
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} msgId
	 * @param {Object} createDate
	 */
	ChatMessage: function(senderId, receiverId, msg, msgId, createDate) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.msg = msg;
		this.msgId = msgId; // 前端发送设置null就好
		this.createDate = createDate;
	},
	
	/**
	 * 构建消息 DataContent 模型对象
	 * @param {Object} action
	 * @param {Object} data
	 * @param {Object} extand
	 */
	DataContent: function(action, data, extand) {
		this.action = action;
		this.data = data;
		this.extand = extand;
	},
}

/**
 * Timestamp 渲染
 * @param {Object} timediff
 */
Vue.prototype.timeDeal = function(timediff) {
	timediff = new Date(timediff);
	var parts = [timediff.getFullYear(), timediff.getMonth() + 1, timediff.getDate(), timediff.getHours(), timediff.getMinutes(),
		timediff.getSeconds()
	];
	var oldTime = timediff.getTime();
	var now = new Date();
	var newTime = now.getTime();
	var milliseconds = 0;
	var timeSpanStr;
	milliseconds = newTime - oldTime;
	if (milliseconds <= 1000 * 60 * 1) {
		timeSpanStr = '刚刚';
	} else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
		timeSpanStr = Math.round((milliseconds / (1000 * 60))) + '分钟前';
	} else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
		timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + '小时前';
	} else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
		timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + '天前';
		
	} else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && parts[0] == now.getFullYear()) {
		timeSpanStr = parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
	} else {
		timeSpanStr = parts[0] + '-' + parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
	}
	return timeSpanStr;
}

// 封装tabbar索引，避免重复书写
Vue.mixin({
  methods:{
    setTabBarIndex(index){
      if (typeof this.$mp.page.getTabBar === 'function' &&
        this.$mp.page.getTabBar()) {
        this.$mp.page.getTabBar().setData({
			selected:index
        })
      }
    }
  }
})
//获取导航栏各种信息
Vue.prototype.getnavbarHeight =  function(){
	var navbarInfo = uni.getMenuButtonBoundingClientRect();
	return navbarInfo;
}

//判断屏幕尺寸并分类,实现兼容不同设备
Vue.prototype.getScreenSize = function(){
	uni.getSystemInfo({
		success: function (res) {
			console.log(res.model);
			console.log(res.pixelRatio);
			console.log(res.windowWidth);
			console.log(res.windowHeight);
			console.log(res.language);
			console.log(res.platform);
		}
	})
}
