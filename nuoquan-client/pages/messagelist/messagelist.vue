<template>
	<view id="container">
		<!-- 由两个超圆，用 fixed 定位做成的背景黄色 -->
		<view id="msglist-yellowshadowbg">
		</view>
		<view id="msglist-yellowbg">
		</view>
		<!-- 点赞和评论数量 -->
		<view id="msglist-likecommentnum">
			<view class="msglist-like column_center">
				<view class="msglist-like-bg super_center">
					<image src="../../static/icon/like.png" class="msglist-like-icon" mode=""></image>
				</view>
				<text class="msglist-like-text font-family">点赞</text>
				<view class="msglist-like-num super_center">
					121
				</view>
			</view>
			<view class="msglist-likecommentnum-border">
			</view>
			<view class="msglist-comment column_center">
				<view class="msglist-comment-bg super_center">
					<image src="../../static/icon/comment.png" class="msglist-comment-icon" mode=""></image>
				</view>
				<text class="msglist-comment-text font-family">评论</text>
				<view class="msglist-comment-num super_center">
					12
				</view>
			</view>
			<!-- 消息内容滑块区 -->
			<scroll-view scroll-y="true" style="position: fixed;left: 0upx;top: 314upx;width: 100%;height: 1132upx;">
				<!-- 新消息卡片 -->
				<view>
					<view class="message-list">
						<wkiwi-swipe-action :options="options" :messagesList="messages"></wkiwi-swipe-action>
					</view>
				</view>

				<!-- <view v-for="(item, index) in chatSnapShotList" :key="index">
					<view class="msglist-card column_center" v-if="item.isRead == UNREAD" @tap="goToChatpage(item)">
						<image class="msglist-Touxiang" mode="aspectFill" :src="item.friendInfo.faceImg"></image>
						<view class="msglist-content">
							<view class="msglist-id font-family">
								{{item.friendInfo.nickname}}
							</view>
							<view class="msglist-brief font-family">
								{{item.msg}}
							</view>
						</view>
						<view class="time-numicon">
							<view class="msglist-time">
								{{item.createDate}}
							</view>
							<view class="msglist-icon super_center"> -->
								<!-- 12 -->
							<!-- </view>
						</view>
					</view>

					<view class="msglist-card-read column_center" v-if="item.isRead == READ" @tap="goToChatpage(item)">
						<image class="msglist-Touxiang" mode="aspectFill" :src="item.friendInfo.faceImg"></image>
						<view class="msglist-content">
							<view class="msglist-id-read font-family">
								{{item.friendInfo.nickname}}
							</view>
							<view class="msglist-brief-read font-family">
								{{item.msg}}
							</view>
						</view>
						<view class="time-numicon">
							<view class="msglist-time">
								{{item.createDate}}
							</view> -->
							<!-- 预留的icon位置 
											by Guetta -->
							<!-- <view class="msglist-icon-read super_center">
								{{msgicon}}
							</view> -->
						<!-- </view>
					</view>
				</view> -->
			</scroll-view>
		</view>
	</view>

</template>

<script>
	import {
		mapState
	} from 'vuex';
	import wkiwiSwipeAction from "../../components/wkiwi-swipe-action.vue";

	var userInfo;

	var socketTask;
	var socketOpen = false;

	export default {
		components: {
			wkiwiSwipeAction
		},
		data() {
			return {
				focus: false,
				isShowView: true,
				messages: [{
					title: "系统消息",
					url: [
						"http://img1.3lian.com/gif/more/11/201212/0d1252b54be4f2d240b6b7fe4ed35054.jpg"
					],
					message: "这是一条系统消息",
					time: "15:15",
					count: 5,
					stick: false, //是否为置顶状态
					disabled: false, //是否禁止滑动
					type: 2 //通知类型消息
				},
					{
						title: "马云",
						url: [
							"http://img1.3lian.com/gif/more/11/201212/0d1252b54be4f2d240b6b7fe4ed35054.jpg"
						],
						message: "什么鬼，我有支付宝[禁止滑动]",
						time: "15:15",
						count: 5,
						stick: false, //是否为置顶状态
						disabled: false, //是否禁止滑动
						type: 2 //普通用户类型消息
					}],
				// 测试分割
				// 测试滑动单元
				// 如遇问题，删除以上


				msgicon: [],
				chatSnapShotList: [ //测试用数据
					{
						createDate: "2019/08/22 03:35:02",
						friendId: "1",
						friendInfo: {
							id: "1",
							email: "x@nottingham.edu.cn",
							nickname: "test1",
							createDate: "2019-07-05T17:17:23.000+0000",
							faceImg: "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI3ic84wG7jlib3gCOlemyy53Ribg1IJM2px221hCDNync15P0MdJcPibY4QFIOibjqrVQnrI8xZ7Vg5hg/132",
						},
						isRead: 3,
						msg: "1",
						myId: "test-id123",
					},

					{
						createDate: "2019/08/22 03:35:02",
						friendId: "1",
						friendInfo: {
							id: "1",
							email: "x@nottingham.edu.cn",
							nickname: "test1",
							createDate: "2019-07-05T17:17:23.000+0000",
							faceImg: "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI3ic84wG7jlib3gCOlemyy53Ribg1IJM2px221hCDNync15P0MdJcPibY4QFIOibjqrVQnrI8xZ7Vg5hg/132",
						},
						isRead: 3,
						msg: "1",
						myId: "test-id123",
					},
				],

				READ: this.chat.READ,
				UNREAD: this.chat.UNREAD,
			}
		},

		computed: {
			...mapState([
				'chatMessageCard',
			])
		},

		watch: {
			chatMessageCard(newVal, oldVal) { //监听数据变化，即可做相关操作
				this.loadingChatSnapshot();
				console.log("newVal:");
				console.log(newVal);
			}
		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "消息列表"
			});

			userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				console.log("No userInfo!!");
				return;
			}

			// [测试代码块]
			// this.mySocket.init();
		},

		onShow() {
			this.loadingChatSnapshot(); // 载入聊天快照
		},

		methods: {
			/**
			 * 展示聊天快照，渲染列表.
			 */
			loadingChatSnapshot() {
				var chatSnapShotList = this.chat.getUserChatSnapShot(userInfo.id);
				// 提前渲染
				this.chatSnapShotList = chatSnapShotList;
				// 拼接信息: 根据 friendId 获取用户信息
				var sendCount = 0; // 网络请求为异步，计数返回结果判断是否全部完成
				var receiveCount = 0;
				console.log(chatSnapShotList)
				for (var i = 0; i < chatSnapShotList.length; i++) {
					var thisFrindId = chatSnapShotList[i].friendId;
					// 查看缓存
					var thisFriendInfo = this.getUserInfoFromUserList(thisFrindId);
					if (this.isNull(thisFriendInfo)) {
						// 不在缓存中, 向服务器请求
						sendCount++;
						var that = this;
						uni.request({
							url: that.$serverUrl + '/user/queryUser',
							method: "POST",
							data: {
								userId: thisFrindId
							},
							header: {
								'content-type': 'application/x-www-form-urlencoded'
							},
							success: (res) => {
								// console.log(res)
								if (res.data.status == 200) {
									// 获取返回的用户信息 写到缓存里
									thisFriendInfo = res.data.data;
									that.setUserInfoToUserList(thisFriendInfo);

									// 查看 sent & receive 是否相等
									receiveCount++;
									console.log("sendCount=" + sendCount + " receiveCount=" + receiveCount);
									if (sendCount == receiveCount) {
										// 再次加载渲染
										that.loadingChatSnapshot();
									}
								}
							},
						});
					} else {
						// 添加朋友信息到快照列表对象
						chatSnapShotList[i].friendInfo = thisFriendInfo;
					}
				}
			},

			/**
			 * 右滑删除, TODO: 施工中...未测试
			 */
			deleteChat(e) {
				// 获取朋友 id
				var frindId = e.id
				// 1. 删除我和朋友的聊天记录
				this.chat.deletUserChatHistory(userInfo.id, frindId);
				// 2. 删除快照
				this.chat.deletUserChatSnapShot(userInfo.id, frindId);
				// 重载快照
			},

			goToChatpage(e) {
				// console.log(e)
				var myId = e.myId;
				var friendId = e.friendId;
				var msg = e.msg;
				var friendInfo = e.friendInfo;
				// 覆盖快照，设为已读 (myId, friendId, msg, isRead)
				this.chat.readUserChatSnapShot(myId, friendId);
				// this.chat.saveUserChatSnapshot(e.myId, e.friendId, e.msg, this.chat.READ);

				uni.navigateTo({
					url: '../chatpage/chatpage?friendInfo=' + JSON.stringify(friendInfo),
				});
			},

		}
	}
</script>

<style>
	page {
		width: 100%;
		height: 100%;
	}

	#container {
		width: 100%;
		height: 100%;
		background-color: #f3f3f3;
	}

	#msglist-yellowbg {
		position: fixed;
		width: 3100upx;
		height: 3100upx;
		border-radius: 2000upx;
		background-color: #FDD041;
		top: -2860upx;
		left: -1520upx;
	}

	#msglist-yellowshadowbg {
		position: fixed;
		width: 2980upx;
		height: 2980upx;
		border-radius: 3000upx;
		background-color: #dcb436;
		top: -2694upx;
		left: -1520upx;
	}

	#msglist-likecommentnum {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		position: fixed;
		width: 88%;
		left: 6%;
		height: 224upx;
		top: 65upx;
		background-color: white;
		border-radius: 25upx;
		box-shadow: 0upx 0upx 8upx 0upx #888888;
	}

	.msglist-like {
		position: relative;
		width: 100%;
		height: 49%;
		border-radius: 25upx;
	}

	.msglist-like-bg {
		position: absolute;
		height: 58upx;
		width: 58upx;
		left: 40upx;
		border-radius: 999upx;
		background-color: #ff5d6c;
	}

	.msglist-like-icon {
		height: 32upx;
		width: 32upx;
		margin-bottom: 5upx;
		margin-left: 1upx;
	}

	.msglist-likecommentnum-border {
		width: 90%;
		height: 2upx;
		background-color: #d1d1d1;
		margin-left: 5%;
	}

	.msglist-comment-bg {
		position: absolute;
		height: 58upx;
		width: 58upx;
		left: 40upx;
		border-radius: 999upx;
		background-color: #058ecc;
	}

	.msglist-comment-icon {
		height: 32upx;
		width: 32upx;
		margin-top: 5upx;
		margin-left: 1upx;
	}

	.msglist-comment {
		width: 100%;
		height: 49%;
		border-radius: 25upx;
	}

	.msglist-like-text {
		font-size: 28upx;
		position: absolute;
		left: 120upx;
		font-family: weiruanyahei;
	}

	.msglist-like-num {
		position: absolute;
		font-size: small;
		margin-left: 360upx;
		font-family: weiruanyahei;
		background-color: #e4505d;
		color: white;
		border-radius: 7upx;
		width: 56upx;
		right: 40upx;
	}

	.msglist-comment-text {
		font-size: 28upx;
		position: absolute;
		left: 120upx;
		font-family: weiruanyahei;
	}

	.msglist-comment-num {
		position: absolute;
		font-size: small;
		margin-left: 360upx;
		font-family: weiruanyahei;
		background-color: #058ecc;
		color: white;
		border-radius: 7upx;
		width: 36upx;
		right: 40upx;
	}

	.msglist-card {
		width: 88%;
		margin-left: 6%;
		margin-top: 8upx;
		height: 120upx;
		background-color: white;
		border-radius: 12upx;
		box-shadow: 0upx 0upx 14upx 0upx #B2B2B2;
	}

	.msglist-card-read {
		width: 88%;
		margin-left: 6%;
		margin-top: 8upx;
		height: 120upx;
		background-color: white;
		border-radius: 12upx;
	}

	.msglist-Touxiang {
		margin-left: 24upx;
		width: 76upx;
		height: 76upx;
		border-radius: 999upx;
		display: inline-block;
		vertical-align: middle;
	}

	.msglist-content {
		margin-left: 20upx;
		width: 480upx;
		height: 70upx;
		/* display: flex; */
		display: inline-block;
	}

	.msglist-id {
		width: 100%;
		height: 32upx;
		font-size: x-small;
		font-weight: 550;
		color: #353535;
	}

	.msglist-id-read {
		width: 100%;
		height: 32upx;
		font-size: x-small;
		font-weight: 500;
		color: #9b9b9b;
	}

	.msglist-brief {
		width: 100%;
		font-weight: 500;
		height: 32upx;
		font-size: xx-small;
		color: #6f6f6f;
		margin-top: 6upx;
	}

	.msglist-brief-read {
		width: 100%;
		height: 32upx;
		font-size: xx-small;
		color: #9b9b9b;
		margin-top: 6upx;
	}

	.time-numicon {
		position: relative;
		margin-left: 2upx;
		width: 48upx;
		height: 76upx;
	}

	.msglist-time {
		position: absolute;
		right: 0;
		height: 36upx;
		width: 100%;
		font-size: 17upx;
	}

	.msglist-icon {
		position: absolute;
		right: 0upx;
		bottom: 0upx;
		height: 17px;
		width: 17px;
		color: white;
		background-color: #FDD453;
		font-size: 20upx;
		border-radius: 999upx;
	}
</style>
