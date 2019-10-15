<template>
	<view class="comment" :id="mainComment.id">
		<view class="contentarea" @tap="controlInputInComment('inComment')">{{ mainComment.comment }}</view>
		<!-- ID 行 -->
		<view class="bottombar">
			<!-- 蒙层,用于优化体验 -->
			<view style="position: absolute;z-index: 20;height: 100%;width: 80upx;right: 0;" @tap="swLikeMainComment(mainComment)"></view>
			<view style="position: absolute;z-index: 20;height: 100%;width: 80upx;right: 80upx;" @tap="goToCommentDetail(mainComment)"></view>
			<!-- 蒙层结束 -->
			<view class="id">
				<image :src="mainComment.faceImg" class="touxiang" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
				<text class="name">{{ mainComment.nickname }}</text>
			</view>
			<view class="time super_center">
				<text class="time_text">{{ mainComment.timeAgo }}</text>
			</view>
			<view class="icons">
				<view @tap="goToCommentDetail(mainComment)">
					<!-- 评论按钮 -->
					<image v-if="!RECOMMENT || mainComment.commentNum == 0" class="icon" src="../../../../static/icon/comment.png" 
					style="padding-right: 30px;"></image>
					
					<image v-if="RECOMMENT && mainComment.commentNum" src="../../../../static/icon/ReComment.png" 
					style="height:23px;width:50px;position:relative;bottom:-5px;padding-right:4px;"></image>
				</view>
				<!-- 点赞按钮 -->
				<view @tap="swLikeMainComment(mainComment)">
					<image v-if="!mainComment.isLike" class="icon" src="../../../static/icon/like.png"></image>
					<image v-if="mainComment.isLike" class="icon" src="../../../static/icon/liked.png"></image>
					<text class="icom" :class="{'liked':mainComment.isLike}">{{ mainComment.likeNum }}</text>
				</view>
			</view>
		</view>

		<view v-show="RECOMMENT && mainComment.commentNum > 0" class="reCommentsArea">
			<reComment v-for="(item,index) in reCommentList" v-bind:key="index" :reCommentDetail='item' @controlInputSignal="controlInputInComment"
			 @goToPersonPublic="goToPersonPublic" @goToCommentDetail="goToCommentDetail(mainComment)"></reComment>
			<view style="font-size: 10px;color:#007AFF;text-align: right;margin-top:5px;" @tap="goToCommentDetail(mainComment)">共{{mainComment.commentNum}}条评论</view>
			<!-- <view class="submitComment">发 表 评 论</view> -->
		</view>
		<view class="fengexian1"></view>
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
				RECOMMENT: true,
				reCommentList: [],
				isPassingReComment: false,

				mainComment: this.commentDetail, // 为了动态修改数值，对对象重新赋值，转换组件内部对象
				userInfo: this.getGlobalUserInfo(),

				totalPage: 1,
				currentPage: 1,
			};
		},

		created() {
			// console.log(this.commentDetail);
			var page = this.currentPage;
			// 监听刷新次级评论事件
			uni.$on('flashSubComment', (underCommentId) => {
				if (this.mainComment.id == underCommentId) {
					this.mainComment.commentNum++;
					this.getSubComments(page);
				};
			})

			// 获取子评论
			this.getSubComments(page);
		},

		methods: {
			showRecommentArea() {
				this.RECOMMENT = !this.RECOMMENT
				if (this.RECOMMENT) {
					this.getSubComments(this.currentPage);
				}
			},
			getSubComments(page) {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getSubComments',
					data: {
						underCommentId: that.mainComment.id,
						userId: that.userInfo.id,
						page: page
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// that.isPassingReComment = false;
						// that.reCommentListFromDetail = '';
						if (res.data.status == 200) {
							// 强制子组件重新刷新
							that.reCommentList = '';
							that.$nextTick(function() {
								const showCommentNum = 2;
								if(res.data.data.rows.length>showCommentNum){
									that.reCommentList = res.data.data.rows.slice(0,showCommentNum);
								}else{
									that.reCommentList = res.data.data.rows;	
								}
							});
							// console.log(res);
						}

						// if (page == 1) {
						// 	that.reCommentList = [];
						// }
						// 
						// var newCommentList = res.data.data.rows;
						// var oldCommentList = that.reCommentList;
						// that.reCommentList = oldCommentList.concat(newCommentList);
						// that.currentPage = page;
						// that.totalPage = res.data.data.total;
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

			goToPersonPublic(userId) {
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + userId,
				});
			},
			
			goToCommentDetail(mainComment){
				uni.navigateTo({
					url: '/pages/comment-detail/comment-detail?data=' + JSON.stringify(mainComment),
				})
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

	.fengexian1 {
		height: 1px;
		width: 100%;
		background-color: #d6d6d6;
	}

	.contentarea {
		padding-top: 14px;
		background: white;
		font-size: 16px;
		color: #3D3D3D;
	}

	.bottombar {
		/* position: relative;
		border-radius: 20px;
		height: 25px;
		margin-top: 10px;
		display: flex;
		justify-content: space-between; */
		position: relative;
		border-radius: 20px;
		margin-top: 20px;
		padding-bottom: 5px;
		height: 30px;
	}

	.touxiang {
		border-radius: 30px;
		width: 20px;
		height: 20px;
		margin-right: 5px;
		vertical-align: middle;
	}

	.id{
		position: absolute;
		left: 0;
		width:42%;
		display:inline-block;
	}

	.time {
		position: absolute;
		left: 42%;
		height: 30px;
		max-width: 85px;
	}

	.time_text{
		font-size: 15px;
		color: #888888;
		text-overflow: ellipsis;
	}
	
	.name {
		display: inline-block;
		font-size: 15px;
		margin-left: 7px;
		color: #888888;
		padding-bottom: 5px;
		max-width: 80px;
		text-overflow: ellipsis;
	}

	.icons {
		position: absolute;
		right: 0;
		width: 30%;
		font-size: 10px;
		height: 30px;
		z-index: 10;
	}

	.icon {
		position: absolute;
		right: 26%;
		top: 9px;
		width: 12px;
		height: 12px;
		font-size: 2px;
		z-index: 10;
		/* padding-right: 8upx; */
	}
	
	.liked{
		position: absolute;
		right: 20upx;
		height: 12px;
		top: 9px;
		color: #FDD041;
		z-index: 10;
	}
	
	.icom{
		position: absolute;
		right: 20upx;
		height: 15px;
		font-size: 15px;
		top: 5px;
		z-index: 10;
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
		width: 500upx;
		padding: 14px 25upx 11px;
		border-radius: 10px;
		margin-left: 87.5upx;
		margin-bottom: 10px;
	}
</style>
