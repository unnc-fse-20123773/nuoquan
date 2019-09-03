<!-- 本页面的 websocket 应写在 messagelist 里-->
<template>
	<view style="height:100%;width:100%;background: #F5F5F5;">
		<scroll-view class="messageArea">
			<onemessage v-for="(i,index) in chatContent" :key=index :thisMessage='i' :userInfo="userInfo" :friendInfo="friendInfo"></onemessage>
		</scroll-view>
		<view class="bottomBar">
			<textarea auto-height="true" v-model="textMsg" />
			<view class="icons">
				<button><image src="../../static/icon/viewLocalPic.png"></image></button>
				<button><image src="../../static/icon/emoji.png"></image></button>
				<button @click="sendText(textMsg)"><image src="../../static/icon/littlePlane.png"></image></button>
			</view>
		</view>

	</view>
</template>

<script>
	import onemessage from './oneMessage';
	import {mapState} from 'vuex';

	var socketTask;
	var socketOpen = false;
	
	export default {
		components: {
			onemessage,
		},
		data() {
			return {
				/**
				 * ChatMessageCard example
				ChatMessageCard = {
					this.senderId = senderId;
					this.receiverId = receiverId;
					this.msg = msg;		// 显示
					this.msgId = msgId; // 前端不需要用到
					this.creatTime = creatTime; // 显示 flag
				},
				*/
				
				chatContent: [{
					msgId: '0001',
					flag: '1',
					msg: '第一条消息',
					createDate: '11:29',
					messageStatus: '0'
				}, {
					msgId: '0002',
					flag: '1',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '0'
				}, {
					msgId: '0003',
					flag: '1',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '0'
				}, {
					msgId: '0004',
					flag: '2',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '1'
				}, {
					msgId: '0006',
					flag: '1',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '0'
				}, {
					msgId: '0006',
					flag: '2',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '1'
				}, {
					msgId: '0001',
					flag: '1',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '0'
				}, {
					msgId: '0002',
					flag: '1',
					msg: 'abab',
					createDate: '11:29',
					messageStatus: '0'
				}],

				socketMsgQueue: [], // 未发送的消息队列
				textMsg: '', // 输入框中的text
				windowHeight: '',
				
				userInfo: '',
				friendInfo: '',
			}
		},
		
		computed: {
			...mapState([
				'chatMessageCard',
				'flashChatPage',
			])
		},
		
		watch: {
			chatMessageCard(newVal, oldVal) { //监听数据变化，即可做相关操作
				console.log("newVal:");
				console.log(newVal);
				// 渲染到窗口
				this.chatContent.push(newVal);
				this.scrollToBottom();
			},
			
			flashChatPage(newVal, oldVal) { //监听数据变化，即可做相关操作
				// 重载聊天记录就可
				// console.log("重载聊天记录就可");
				this.getChatHistory();
				this.scrollToBottom();
			}
		},
		
		onLoad: function(opt) {
			// 获取界面传参
			this.friendInfo = JSON.parse(opt.friendInfo);
			
			uni.setNavigationBarTitle({
				title: this.friendInfo.nickname
			});
			
			// 获取我的信息
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				console.log("No userInfo!!");
				return;
			}
			this.userInfo = userInfo;
			
			// 获取屏幕高度
			var that = this;
			uni.getSystemInfo({
			  success: function(res) {
				that.windowHeight = res.windowHeight;
			  }
			});
			
			// 获取与该用户的聊天历史记录
			this.getChatHistory();
			this.scrollToBottom();
		},
		
		methods: {
			
			sendText() {
				if(!this.textMsg){
					return;
				}
				this.mySocket.sendObj(this.netty.CHAT, this.friendInfo.id, this.textMsg, null);
				this.textMsg = '';//清空输入框
				
				// 渲染到窗口
				// var message = {
				// 	msgId: '',
				// 	flag: this.chat.ME,
				// 	msg: this.textMsg,
				// 	createDate: '11:29',
				// 	messageStatus: '1',
				// }
				// this.chatContent.push(message);
				
				// 直接重新加载聊天历史, 代替渲染到窗口
				// this.getChatHistory();
				// this.scrollToBottom();
				
			},
			
			getChatHistory(){
				var localChatHistory = this.chat.getUserChatHistory(this.userInfo.id, this.friendInfo.id);
				this.chatContent = localChatHistory;
				// console.log(this.chatContent);
			},
			
			scrollToBottom(){
				// 将页面滚动到底部，延时滚动,等待渲染
				// 页面高度乘以列表长度...绝对能滚到底
				this.$nextTick( function(){
					uni.pageScrollTo({
						scrollTop: this.windowHeight * this.chatContent.length,
						duration: 0
					});
				});
			}
		}
	}
</script>
<style>
	page{
		height:100%;
		width:100%;
	}
</style>

<style scoped>
	/**
	 * TODO： 滚动区域高度需固定
	 * 					by Jerrio
	 */
	.messageArea {
		display: flex;
		/* flex-flow: column-reverse; */
		margin-bottom: 90upx;
		overflow: hidden;
		background: #F5F5F5;
		height: 100%;
		margin-left: 3%;
		width: 94%;
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
		width: 530upx;
		padding:0 4upx;
		min-height: 50upx;
		max-height: 95px;
		border-radius: 24upx;
		border: solid 1px #C6C6C6;
		margin: 20upx 0 20upx 14upx;
		
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
		display: block;
		width: 34upx;
		height: 34upx;
		vertical-align: middle;
	}
	.icons button.button-hover{
	position: relative;
  top: 3rpx;
  box-shadow:0px 0px 8px #999 inset; 
  
    }
	button{
	display: inline-block;
	margin: 0;
	padding: 0;
	width: 34upx;
	height: 34upx;
	vertical-align: middle;
	background: #FFFFFF;
	}
    button::after{
	border:none;
    }
</style>
