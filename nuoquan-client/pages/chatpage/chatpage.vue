<!-- 本页面的 websocket 应写在 messagelist 里-->
<template>
	<view>
		<view class="messageArea">
			<onemessage v-for="(i,index) in chatContent" :key=index :thisMessage='i'></onemessage>
		</view>
		<view class="bottomBar">
			<textarea auto-height="true" v-model="textMsg" />
			<view class="icons">
				<image src="../../static/icon/viewLocalPic.png"></image>
				<image src="../../static/icon/emoji.png"></image>
				<image src="../../static/icon/littlePlane.png" @click="sendText(textMsg)"></image>

			</view>
		</view>

	</view>
</template>

<script>
	import onemessage from './oneMessage'
	
	var userInfo;
	var frindInfo;
	
	var socketTask;
	var socketOpen = false;
	
	export default {
		components: {
			onemessage,
		},
		data() {
			return {

				chatContent: [{
					messageId: '0001',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0002',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0003',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0004',
					messageType: '0',
					messageTime: '11:29',
					messageStatus: '1'
				}, {
					messageId: '0006',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0006',
					messageType: '0',
					messageTime: '11:29',
					messageStatus: '1'
				}],

				socketMsgQueue: [], // 未发送的消息队列
				textMsg: '', // 输入框中的text
			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "XXXX（聊天人的昵称）"
			});
			
			userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				console.log("No userInfo!!");
				return;
			} 
			
			// 暂时把 frind 等同于 user
			frindInfo = userInfo;
			
			console.log(userInfo);
			this.socketInit();
			this.iniChatHistory();
		},
		onHide() {
			// socketTask.close();
		},
		
		methods: {
			socketInit(){
				var that = this;
				// 创建websocket长连接
				socketTask = uni.connectSocket({
					url: 'ws://localhost:8088/ws',
					complete: () => {}
				});
				socketTask.onOpen(function(res) {
					console.log('WebSocket连接已打开！');
					socketOpen = true;
					
					//发送连接消息，向服务器注册信息
					that.sendObj(that.netty.CONNECT, null, null);
					// 发送未发送的信息
					for (var i = 0; i < that.socketMsgQueue.length; i++) {
						// console.log(that.socketMsgQueue[i]);
						that.sendObj(that.netty.CHAT, that.socketMsgQueue[i], null);
					}
					that.socketMsgQueue = [];
				});
				
				socketTask.onError(function(res) {
					console.log('WebSocket连接打开失败，请检查！');
				});
				socketTask.onMessage(function(res) {
					var dataContent = JSON.parse(res.data);
					var chatMessage = dataContent.chatMessage;
					console.log("收到服务器内容：");
					console.log(dataContent);
					
					// 发送签收消息
					that.sendObj(that.netty.SIGNED, null, chatMessage.msgId);
				
					// 保存聊天历史记录到本地缓存
					var myId = chatMessage.receiverId;
					var friendId = chatMessage.senderId;
					var msg = chatMessage.msg
				
					that.chat.saveUserChatHistory(myId, friendId, msg, that.chat.FRIEND);
				});
				socketTask.onClose(function(res) {
					console.log('WebSocket 已关闭！');
					socketOpen = false;
					// 三秒一次重连
					// console.log("重连中..");
					// setTimeout(function(){
					// 	that.socketInit();
					// },3000);
				});
				
			},
			
			sendText() {
				if(!this.textMsg){
					return;
				}
				
				this.sendObj(this.netty.CHAT, this.textMsg, null);
				this.textMsg = '';//清空输入框
			},
			
			sendObj(type, msg, extand){
				var chatMessage = new this.netty.ChatMessage(userInfo.id, frindInfo.id, msg, null);
				var dataContent = new this.netty.DataContent(type, chatMessage, extand)
				
				var data = JSON.stringify(dataContent);
				if (socketOpen == true) {
					console.log("1="+socketOpen);
					socketTask.send({
						data: data,
					});
					
					//保存聊天历史到 本地缓存	
					this.chat.saveUserChatHistory(userInfo.id, frindInfo.id, msg, this.chat.ME);
				} else {
					console.log("2="+socketOpen);
					this.socketMsgQueue.push(data);
					console.log(this.socketMsgQueue);
				}
			},
			
			iniChatHistory(){
				var localChatHistory = this.chat.getUserChatHistory(userInfo.id, frindInfo.id);
				console.log(localChatHistory);
			}
		}
	}
</script>

<style scoped>
	.messageArea {
		display: flex;
		flex-flow: column-reverse;
		margin-bottom: 90upx;
		overflow: hidden;
		background: #F5F5F5;
	}

	.bottomBar {
		position: fixed;
		display: flex;
		align-items: center;
		bottom: 0;
		height: 90upx;
		width: 100%;
		margin: 0;
		padding: 0;
		box-shadow: 0px -2px 2px 0px rgba(130, 130, 130, 0.20);
		background: #FFFFFF;
	}

	.bottomBar textarea {
		display: inline-block;
		width: 534upx;
		height: 50upx;
		border-radius: 24upx;
		border: solid 1px #C6C6C6;
		margin-left: 14upx;
	}

	.icons {
		display: inline-flex;
		width: 146upx;
		height: 90upx;
		margin-left: 18upx;
		justify-content: space-between;
		align-items: center;
	}

	.icons image {
		width: 34upx;
		height: 34upx;
		vertical-align: middle;
	}
</style>
