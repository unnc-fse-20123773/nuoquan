<template>
	<view style="height:100%;">
		<view class="messageArea">
			<onemessage v-for="(i,index) in chatContent" :key="index" :thisMessage="i"></onemessage>
		</view>
		<view class="bottomBar">
			<textarea auto-height="true" v-model="textMsg" />
			<view class="icons">
				<image src="../../static/icon/viewLocalPic.png"></image>
				<image src="../../static/icon/emoji.png"></image>
				<image src="../../static/icon/littlePlane.png" @click="sendText()()"></image>

			</view>
		</view>

	</view>
</template>

<script>
	import onemessage from './oneMessage'
	
	var socketTask;
	var socketOpen = false;
	
	export default {
		name:'chatpage',
		components: {
			onemessage,
		},
		data() {
			return {

				chatContent: [{
					messageId: '0001',
					messageType: true,
					messageTime: '11:29',
					messageStatus:true,
					messageContent:'TT里是1233聊天内容，巴123213拉巴拉一大堆,这里123是聊天内2，21巴拉21一大堆,1里是3聊天内容，巴拉巴拉一大堆,这里是聊天内容，巴拉巴拉一大'
				}, {
					messageId: '0002',
					messageType: false,
					messageTime: '11:29',
					messageStatus: false,
					messageContent:'这是第二条消息，FF'
				}, {
					messageId: '0003',
					messageType: true,
					messageTime: '11:29',
					messageStatus:false,
										messageContent:'这是第san条消息，TF'
					
				}, {
					messageId: '0004',
					messageType: false,
					messageTime: '11:29',
					messageStatus: true,
															messageContent:'这是第444条消息FT'
					
				}, {
					messageId: '0006',
					messageType: false,
					messageTime: '11:29',
					messageStatus: true,
															messageContent:'这是第5555条消息，ft'
					
				}, {
					messageId: '0006',
					messageType: true,
					messageTime: '11:29',
					messageStatus: false,
															messageContent:'tf这是第san6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666条消息，status 0,type1'
					
				}],

				socketMsgQueue: [], // 未发送的消息队列
				textMsg: '', // 输入框中的text
			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "XXXX（聊天人的昵称）"
			});
			
			this.socketInit();
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
					for (var i = 0; i < that.socketMsgQueue.length; i++) {
						console.log(that.socketMsgQueue[i]);
						socketTask.send({
							data: that.socketMsgQueue[i]
						});
					}
					that.socketMsgQueue = [];
				});
				
				socketTask.onError(function(res) {
					console.log('WebSocket连接打开失败，请检查！');
				});
				socketTask.onMessage(function(res) {	
					debugger;
					var receveMessage = {
						messageType: false,                                   ///////发送为true,接受flase
						messageTime: new Date().getHours() +":"+new Date().getMinutes(),
						messageStatus:true,                                  //////////未读为false，已读true
						messageContent:res.data				
					};
					this.chatContent.unshift({messageContent:"123",messageType:false});
					console.log('收到服务器内容：' + res.data);
				});
				socketTask.onClose(function(res) {
					console.log('WebSocket 已关闭！');
					// 三秒一次重连
					// console.log("重连中..");
					// socketOpen = false;
					// setTimeout(function(){
					// 	that.socketInit();
					// },3000);
				});
			},
			sendText() {
				debugger;
				if(!this.textMsg){
					return;
				}
				var that = this;	
				if (socketOpen == true) {
					console.log("1=:"+socketOpen)
					socketTask.send({
						data: that.textMsg
					});
					//本地尝试模拟发送信息    发送为true,接受flase////////未读为true，已读false
					var newLocalMessage={
						messageType: true,                                   ///////发送为true,接受flase
						messageTime: new Date().getHours() +":"+ new Date().getMinutes(),
						messageStatus:false,                                  //////////未读为false，已读true
						messageContent:this.textMsg					
					};
					this.chatContent.unshift(newLocalMessage);
					//模拟发送结束
					that.textMsg = '';//清空输入框
				} else {
					console.log("2=:"+socketOpen)
					that.socketMsgQueue.push(this.textMsg);
					that.textMsg = '';//清空输入框
					console.log(that.socketMsgQueue);
					
				}
			}
		}
	}
</script>
<style>
	page{height:100%;		background: #F5F5F5;
}
</style>
<style scoped>
	.messageArea {
		display: flex;
		height:100%;
		flex-flow: column-reverse;
		overflow: hidden;
		padding-bottom: 120upx;
	}

	.bottomBar {
		position: fixed;
		display: flex;
		align-items: center;
		bottom: 0;
		min-height: 90upx;
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
		max-height: 100px;
		border-radius: 24upx;
		border: solid 1px #C6C6C6;
		margin-left: 14upx;
		padding:3px 2px;
		
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
