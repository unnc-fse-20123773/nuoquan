<template>
	<view class="comment" :id="mainComment.id">
		<view class="fengexian"></view>
		<view class="contentarea" @tap="controlInputInComment('inComment')">{{ mainComment.comment }}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="mainComment.faceImg" class="touxiang" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
				<text class="name">{{ mainComment.nickname }}</text>
				<text class="time">{{ mainComment.timeAgo }}</text>
			</view>
			<view class="icons">
				<!-- 评论按钮 -->
				<image v-show="!RECOMMENT" class="icon" src="../../../../static/icon/comment.png" style="padding-right: 23px;"
				 @click="showRecommentArea"></image>

				<image v-if="RECOMMENT" style="height:23px;width:50px;position:relative;bottom:-5px;padding-right:4px;" src="../../../../static/icon/ReComment.png"
				 @click="showRecommentArea"></image>
				<!-- <text class="icom">{{mainComment.commentNum}}</text> -->
				<!-- 点赞按钮 -->
				<view @tap="swLikeMainComment(mainComment)">
					<image class="icon" src="../../../static/icon/like.png"></image>
					<text class="icom">{{ mainComment.likeNum }}</text>
				</view>
			</view>
		</view>

		<view v-show="RECOMMENT" class="reCommentsArea">

			<reComment v-for="(i,index) in reCommentList" v-bind:key="index" :reCommentDetail='i' 
			@controlInputSignal="controlInputInComment" @goToPersonPublic="goToPersonPublic"></reComment>
			<!-- <view class="submitComment">发 表 评 论</view> -->
		</view>
	</view>
</template>

<script>
	import reComment from './reComment'
	export default {
		name: 'comment',
		props: {
			commentDetail: '',
		},
		components: {
			reComment,
		},
		data() {
			return {
				RECOMMENT: false,
				reCommentList: {},
				isPassingReComment: false,
				
				mainComment: this.commentDetail, // 为了动态修改数值，对对象重新赋值，转换组件内部对象
				userInfo: this.getGlobalUserInfo(),
			};
		},
		
		created() {
			// console.log(this.commentDetail);
		},
		
		methods: {
			showRecommentArea() {
				this.RECOMMENT = !this.RECOMMENT
				if (this.RECOMMENT) {
					this.getSubComments();
				}
			},
			getSubComments(a) {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getSubComments',
					data: {
						underCommentId: that.mainComment.id,
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// that.isPassingReComment = false;
						// that.reCommentListFromDetail = '';
						if(res.data.status==200){
							that.reCommentList = res.data.data.rows;
							// console.log(res);
						}
					}
				});
			},

			controlInputInComment(a) {
				if (a == "inComment") {
					var dataOfRecomment = {
						mode: "re-co",
						toUserId: this.mainComment.fromUserId,
						underCommentId: this.mainComment.id,
						fatherCommentId: this.mainComment.id,
						nickname: this.mainComment.nickname,
					}
				} else {
					var dataOfRecomment = a;
				}
				console.log("receive control input request, in comment");
				console.log(dataOfRecomment);
				this.$emit('controlInputSignal', dataOfRecomment);
			},
			
			/**
			 * 点赞或取消点赞主评论
			 * @param {Object} comment
			 */
			swLikeMainComment(comment){
				if(comment.isLike){
					this.unLikeComment(comment);
					this.mainComment.likeNum--;
				} else {
					this.likeComment(comment);
					this.mainComment.likeNum++;
				}
				this.mainComment.isLike = !this.mainComment.isLike;
			},
			
			likeComment(comment){
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
			
			unLikeComment(comment){
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
			
			goToPersonPublic(userId){
				uni.navigateTo({
					url:'/pages/personpublic/personpublic?userId=' + userId,
				});
			}
		},

	};
</script>

<style scoped>
	.comment {
		/* 	background-color:gray;
 */
		border-radius: 20px;
		width: 100%;
		margin: auto;
	}

	.fengexian {
		height: 1px;
		width: 100%;
		background-color: #d6d6d6;
	}

	.contentarea {
		padding-top: 14px;
		background: white;
		font-size: 16px;
	}

	.bottombar {
		position: relative;
		border-radius: 20px;
		height: 25px;
		margin-top: 10px;
		display: flex;
		justify-content: space-between;
		padding-bottom: 5px;

	}

	.touxiang {
		width: 20px;
		height: 20px;
		border-radius: 20px;
		display: inline-block;
		vertical-align: middle;
		margin-right: 5px;
	}

	.time,
	.name {
		font-size: 10px;
		margin-right: 10px;
		color: #888888;
	}

	.icons {
		justify-content: flex-end;
		display: inline-flex;
		align-items: flex-end;
		width: 30%;
		font-size: 10px;

	}

	.icon {
		width: 12px;
		height: 12px;
		font-size: 2px;
		padding-right: 4px;
	}

	.icom {
		padding-right: 17px;
		align-items: flex-end;
	}
	
	.submitComment {
		background: #FFCC30;
		border-radius: 5px;
		width: 120px;
		height: 30px;
		font-size: 10px;
		font-weight: bold;
		color: #FFFFFF;
		margin: auto;
		text-align: center;
		line-height: 30px;
	}

	.reCommentsArea {
		background: #EEEEEE;
		width: 400upx;
		padding: 14px 25upx 11px;
		border-radius: 10px;
		margin-left: 187.5upx;
		margin-bottom: 10px;
	}
</style>
