<!-- Created by Jerrio for testing -->

<template>
	<view>
		<view class="input">
			<textarea auto-height="true" v-model="textMsg" />
			</view>
		<view>
			<button type="primary" @click="sendText">send</button>
		</view>
	</view>
	
</template>

<script>
	var socketTask;
	var socketOpen = false;
	
	export default {
		data() {
			return {
				
				socketMsgQueue: [],
				
				textMsg: '',
				
			};
		},

		onLoad() {
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
					url: 'ws://localhost:8088/ws', //仅为示例，并非真实接口地址。
					complete: ()=> {}
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
				if(!this.textMsg){
					return;
				}
				var that = this;
				
				if (socketOpen == true) {
					console.log("1=:"+socketOpen)
					socketTask.send({
						data: that.textMsg
					});
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
	.input{
		background-color: #CCCCCC;
	}
</style>
