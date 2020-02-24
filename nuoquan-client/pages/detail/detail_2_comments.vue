<template>
	<!--评论区域-->
	<view class="comments-area" :style="{'opacity':mainCommentList.length != undefined ? 1:0}">
		<!--评论数，排序方式-->
		<view class="comments-menu">
			<view class="comments-num">{{mainCommentList.length}}条评论</view>
			<view class="comments-order">
                   <view class="order-in-time" :class="{ chosen : order == 'time'}" @tap="change_comment_order('time')">
					   时间
				   </view>
				   <view class="order-in-hot" :class="{ chosen : order != 'time'}" @tap="change_comment_order('hot')">
				   	   热度
				   </view>
				   <view class="bg-of-order" :style="{'left':order == 'time' ? '-13px;' :'41px' ,}"></view>
			</view>
		</view>


		<!--循环评论卡片-->
		<view class="comments">
			<block v-for="mainComment in mainCommentList" :key="mainComment.id">

				<view class="comment">
					<view class="comment-info">
						<image :src="mainComment.faceImg" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
						<view class="name_text">{{ mainComment.nickname }}</view>
						<view class="time_text">{{ mainComment.timeAgo }}</view>
					</view>
					<view class="comment-content" @tap="goToCommentDetail(mainComment)">{{ mainComment.comment }}</view>
					<view class="comment-menu">
						<view class="son-comment-num" @tap="goToCommentDetail(mainComment)">{{mainComment.commentNum}}</view>
						<view class="like-num" :class="{liked:mainComment.isLike}" @tap="swLikeMainComment(mainComment)">{{ mainComment.likeNum }}</view>
					</view>
				</view>

			</block>
			<view style="height: 100px;width: 100%;"></view>
		</view>

	</view>
</template>

<script>
	
	export default {
		props: {
			commentList: {
				type: Object,
			},
			userInfo: "",
		},
		data() {
			return {
				mainCommentList: this.commentList,
				order:"time",//评论排序方式
			}
		},
		watch: {
			'commentList': function(val) { //监听props中的属性
				this.mainCommentList = this.commentList;
				console.log("watch data changes,")
				console.log(this.mainCommentList);
				console.log(this.commentList);
			}
		},
		methods: {
			swLikeMainComment(comment) {
				if (comment.isLike) {
					this.unLikeComment(comment);
					comment.likeNum--;
				} else {
					this.likeComment(comment);
					comment.likeNum++;
				}
				comment.isLike = !comment.isLike;
				// console.log(this.mainComment.isLike);
			},

			likeComment(comment) {
				console.log("点赞评论");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userLikeComment',
					data: {
						userId: that.userInfo.id,
						commentId: comment.id,
						createrId: comment.fromUserId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
					},
				});
			},

			unLikeComment(comment) {
				console.log("取消点赞评论");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userUnLikeComment',
					data: {
						userId: that.userInfo.id,
						commentId: comment.id,
						createrId: comment.fromUserId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
					},
				});
			},
			goToCommentDetail(mainComment) {
				uni.navigateTo({
					url: '/pages/comment-detail/comment-detail?data=' + JSON.stringify(mainComment),
				})
			},
			change_comment_order(new_order){
				this.order = new_order ;
				console.log(new_order);
				console.log(this.order);
			},
			goToPersonPublic(userId){
				// router.goToPersonPublic(); // 全局方法
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + userId,
				})
			}
			
		}, //method






	}
</script>

<style>
	@import url("./oneComment.css");

	.comments-menu {
		height: 28px;
		padding-top: 13px;
	}

	.comments-num {
		height: 17px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 17px;
		color: rgba(155, 155, 155, 1);
		padding-left: 4px;
		display: inline-block;
	}

	.comments-order {
		margin-top:3px;
		height: 22px;
		background: #ECECEC;
		border-radius: 75px;
		width: 82px;
		float: right;
		line-height: 28px;
		display: flex;
		vertical-align: middle;
		position: relative;
		justify-content: space-between;
		
	}
	.order-in-hot ,.order-in-time{
		color:#9B9B9B;
		font-size: 10px;
		line-height: 22px;
		display: inline-block;
		align-items: center;
		padding-right:10.5px;
		padding-left: 10.5px;
		z-index: 30;
		width:28px;
		text-align: center;
		transition: padding-left 200ms,
		padding-right 200ms,
		font-size  200ms,
		color 200ms;
		 transition-delay: 0ms;
	}
	.bg-of-order{
		height:28px;
		width:54px;
		background: #FFFFFF;
		border-radius: 75px;
		box-shadow: 0px 0px 10px rgba(0,0,0,0.16);
		z-index: 20;
		position: absolute;
		left:-13px;
		top:-3px;
		transition: left 500ms ease;
	}
	.chosen{
		font-size: 14px;
		color:#353535;
		width:28px;
		padding-left: 0;
		padding-right: 0;
		transition: padding-left 300ms,
		padding-right 300ms,
		font-size  300ms,
		color 300ms;
		 transition-delay: 100ms;
	}
</style>
