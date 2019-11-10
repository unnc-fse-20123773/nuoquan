<template>
	<!--评论区域-->
	<view class="comments-area" :style="{'opacity':mainCommentList.length != undefined ? 1:0}">
		<!--评论数，排序方式-->
		<view class="comments-menu">
			<view class="comments-num">{{mainCommentList.length}}条评论</view>
			<view class="comments-order"></view>
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
					<view class="comment-content" @tap="controlInputInComment('inComment')">{{ mainComment.comment }}</view>
					<view class="comment-menu">
						<view class="son-comment-num">{{mainComment.commentNum}}</view>
						<view class="like-num" :class="{liked:mainComment.isLike}" @tap="swLikeMainComment(mainComment)">{{ mainComment.likeNum }}</view>
					</view>
				</view>

			</block>
		</view>

	</view>
</template>

<script>
	export default {
		props: {
			commentList:{
				type:Object,
			},
			userInfo: "",
		},
		data() {
			return {
				mainCommentList:this.commentList,
			}
		},
	 watch: {
            'commentList': function (val) { //监听props中的属性
                this.mainCommentList = this.commentList;
				console.log("watch data changes,")
				console.log(this.mainCommentList);
				console.log(this.commentList);
            }
        },
		methods: {
			swLikeMainComment(comment) {
				debugger;
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
		},






	}
</script>

<style>
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

	.comment-info {
		height: 24px;
		width: 100%;
		position: relative;
	}

	.comment-info image {
		width: 24px;
		height: 24px;
		border-radius: 12px;
	}

	.name_text {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 24px;
		height: 24px;
		color: #9B9B9B;
		display: inline-block;
		position: absolute;
		top: 0;
		left: 32px;
		width: 96px;
	}

	.time_text {
		position: absolute;
		right: 0;
		top: 0;
		text-align: right;
		font-size: 12px;
		color: #9B9B9B;
		line-height: 24px;
		width: 102px;
	}

	.comment-content {
		padding-top: 12px;
		padding-bottom: 8px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(53, 53, 53, 1);
	}

	.comment-menu {
		height: 20px;
		position: relative;
		padding-bottom: 9px;
		border-bottom: 1px solid rgba(236, 236, 236, 1);
	}

	.son-comment-num {
		color: rgba(136, 136, 136, 1);
		font-size: 12px;
		height: 14px;
		line-height: 14px;
		display: inline-block;
		position: absolute;
		right: 60px;
		top: 0;
		padding: 3px 8px 3px 30px;
		text-align: center;
		border-radius: 50px;
	}

	.son-comment-num::after {
		position: absolute;
		width: 14px;
		height: 14px;
		left: 9px;
		top: 3px;
		content: "";
		background: url(../../static/icon/comment-alt-888888.png);
		background-size: 14px 14px;
		background-repeat: no-repeat;
	}

	.like-num {
		width: 14px;
		height: 14px;
		font-size: 12px;
		line-height: 14px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		right: 0;
		top: 0;
		padding: 3px 8px 3px 30px;
		 border-radius:50px;
		 text-align: center;
	}

	.liked {
		background: linear-gradient(130deg, rgba(254, 110, 110, 0.84) 0%, rgba(245, 60, 60, 1) 100%);
	}

	.like-num::after {
		position: absolute;
		width: 14px;
		height: 14px;
		left: 9px;
		top: 3px;
		content: "";
		background: url(../../static/icon/thumbs-up-888888.png);
		background-size: 14px 14px;
		background-repeat: no-repeat;
	}
	
</style>
