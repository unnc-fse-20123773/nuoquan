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
				<view class="msglist-card column_center" v-for="(cardlist,index1) in cardlist" :key="index1">
					<image class="msglist-Touxiang" mode="aspectFill" src="../../static/touxiang2.jpg"></image>
					<view class="msglist-content">
						<view class="msglist-id font-family">
							陈仅仅一号111
						</view>
						<view class="msglist-brief font-family">
							用来模拟场景中天空的光线，也可以使用真...
						</view>
					</view>
					<view class="time-numicon">
						<view class="msglist-time">
							11-26
						</view>
						<view class="msglist-icon super_center">
							12
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<navigator url="../chatpage/chatpage">
			<button type="primary" size="mini">进入聊天窗口</button>
		</navigator>
	</view>

</template>

<script>
	export default {
		data() {
			return {
			cardlist:[1,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]
			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "私信列表"
			});
		},
		methods: {

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
		width: 56upx;
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
		display: flex;
		flex-direction: column;
	}

	.msglist-id {
		width: 100%;
		height: 32upx;
		font-size: x-small;
		font-weight: 550;
		color: #353535;
	}

	.msglist-brief {
		width: 100%;
		height: 32upx;
		font-size: xx-small;
		color: #6f6f6f;
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
