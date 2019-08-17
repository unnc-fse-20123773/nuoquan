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
		
		onLoad: function(opt) {
			uni.setNavigationBarTitle({
				title: "XXXX（聊天人的昵称）"
			});
			// 获取界面传参
			var data = JSON.parse(opt.data);
			userInfo = data.userInfo;
			frindInfo = data.frindInfo;
			
			// 获取与该用户的聊天历史记录
			this.iniChatHistory();
			
			uni.onSocketMessage(function(){
				console.log("this on messge");
			})
		},
		
		methods: {
			
			sendText() {
				if(!this.textMsg){
					return;
				}
				this.mySocket.sendObj(this.netty.CHAT, frindInfo.id, this.textMsg, null);
				this.textMsg = '';//清空输入框
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
