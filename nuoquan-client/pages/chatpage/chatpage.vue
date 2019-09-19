<!-- 本页面的 websocket 应写在 messagelist 里-->
<template>
	<view style="height:100%;width:100%;background: #F5F5F5;">
		<scroll-view class="messageArea" scroll-y="true" @scrolltoupper="loadMore" @scroll="scroll" :scroll-into-view="scrollToView">
			<onemessage v-for="(item,index) in chatContent" :key=index :thisMessage='item' :userInfo="userInfo" :friendInfo="friendInfo"
			 :id="item.id"></onemessage>
			<view class="marginHelper"></view>
		</scroll-view>
		<view class="bottomBar">
			<textarea auto-height="true" v-model="textMsg" :show-confirm-bar="false"/>
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

	var page = 1; // PS: 非显示属性不放在渲染层
	var chatKey;
	var chatHistory;
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
				
				chatContent: [],

				socketMsgQueue: [], // 未发送的消息队列
				textMsg: '', // 输入框中的text
				windowHeight: '',
				scrollToView: '',
				
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
			// 监听收到的消息
			chatMessageCard(newVal, oldVal) {
				// console.log("newVal:");
				// console.log(newVal);
				// 渲染到窗口
				this.chatContent.push(newVal);
				this.scrollToBottom();
			},
			
			// 监听发送的消息
			flashChatPage(newVal, oldVal) {
				// 载入最后一条消息
				var list = this.getListByKey(chatKey);
				var msg = list[list.length-1];
				msg.id = this.generateId(0);
				this.chatContent.push(msg);
				this.scrollToBottom();
			}
		},
		
		onLoad(opt) {
			page = 1; //初始化page,
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
			chatKey = "chat-" + this.userInfo.id + "-" + this.friendInfo.id;
			chatHistory = this.getListByKey(chatKey);
			
			this.getChatHistory();
			this.$nextTick(function(){
				this.scrollToBottom();
			})
		},
		
		methods: {
			scroll(e){
				// console.log(e.detail);
			},
			
			sendText() {
				if(!this.textMsg){
					return;
				}
				this.mySocket.sendObj(this.netty.CHAT, this.friendInfo.id, this.textMsg, null);
				this.textMsg = '';//清空输入框
				
				// 渲染到窗口
				// var list = this.getListByKey(chatKey);
				// this.chatContent.push(list[list.length-1]);
				// this.scrollToBottom();
				
			},
			
			// 进入获取聊天
			getChatHistory(){
				var localChatHistory = this.getChatHistoryPage(page);
				// 为每个消息设置唯一id
				var that = this;
				localChatHistory.forEach(function(item,index){
					item.id = that.generateId(index);
				});
				this.chatContent = localChatHistory;
				// console.log(this.chatContent);
			},
			
			loadMore(){
				if(this.isHistoryLoading){
					return ;
				}
				this.isHistoryLoading = true;//参数作为进入请求标识，防止重复请求
				// this.scrollAnimation = false;//关闭滑动动画
				const viewId = this.chatContent[0].id; //记住第一个信息ID
				uni.showLoading({
					title: '加载中'
				});
				var p = page+1;
				var list = this.chat.getUserChatHistory(this.userInfo.id, this.friendInfo.id, p);
				setTimeout(()=> {
					if(list != null){
						var that = this;
						list.forEach(function(item,index){
							item.id = that.generateId(index);
						});
						this.chatContent = list.concat(this.chatContent);
						page++;
					}
					uni.hideLoading();
					
					//稳住页面，这段代码很重要，不然每次加载历史数据都会跳到顶部
					this.$nextTick(function() {
						this.scrollToView = viewId;//跳转上次的第一行信息位置
						// this.$nextTick(function() {
						// 	this.scrollAnimation = true;//恢复滚动动画
						// });
					});
					this.isHistoryLoading = false;
				}, 300);
			},
			
			/**
			 * 分页获取聊天历史，从列表尾部开始读取(反取) 静态历史
			 * @param {Object} myId
			 * @param {Object} friendId
			 * @param {Object} page
			 */
			getChatHistoryPage(page) {
				var list = chatHistory.reverse();
				var size = 20;
				var start = (page-1) * size;
				var newList = [];
				if (list.length < start){
					return null;
				}else{
					for (var i=0; i<size; i++){
						if(!this.isNull(list[start+i])){
							newList.unshift(list[start+i]);
						}
					}
					return newList;
				}
			},
			
			generateId(index){
				var id = "m" + Math.floor(Math.random()*1000)+ "-" + index + new Date().valueOf();
				return id;
			},
			
			scrollToBottom(){
				// 将页面滚动到底部，
				console.log(this.chatContent)
				this.scrollToView = this.chatContent[this.chatContent.length-1].id;
			}
		}
	}
</script>
<style>
	page{
		height:100%;
		width:100%;
		background-color: #F5F5F5;
	}
</style>

<style scoped>
	/**
	 * TODO： 滚动区域高度需固定
	 * 					by Jerrio
	 */
	.messageArea {
		display: flex;
		margin-top: 30upx;
		margin-bottom: 90upx;
		background: #F5F5F5;
		width: 100%;
		height: 94%;
	}
	
	.bottomBar {
		position: fixed;
		display: flex;
		align-items: center;
		bottom: 0;
		min-height: 6%;
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
		font-size: 13px;
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
	
	.marginHelper{
		height: 40upx;
		width: 100%;
		background-color: #f5f5f5;
	}
	
</style>
