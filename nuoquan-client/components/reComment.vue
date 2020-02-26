<template>
	<view style="
width:576upx;
height:100%;
margin-left: calc(100% - 624upx);
background:rgba(236,236,236,1);
opacity:1;
border-radius:8px;
padding:4px 24upx 12px;" v-if="subCommentNum > 0">
		<block v-for="subComment in subCommentList" :key='subComment.id'>
			<view class="reComment">


				<view class="content">
					<view class="contentarea">{{ subComment.nickname }}</view>
					<view style="color:#000000;font-size: 12px;display: inline-block;margin: 0 5px 0 0;">回复</view>
					<view class="contentarea">@{{subComment.toNickname}}</view>

					{{subComment.comment}}
				</view>


				<!-- 				<view class="bottombar">
					<view style="width:70%;display:inline-block;">
						<image :src="pathFilter(subComment.faceImg)" class="touxiang" @tap="goToPersonPublic"></image>
						<text class="time">{{ subComment.timeAgo }}</text>
					</view>
					<view class="icons" @tap="swLikeSubComment()">
						<!- 点赞按钮 ->
						<image v-if='!subComment.isLike' class="icon" src="../../../static/icon/like.png"></image>
						<image v-if='subComment.isLike' class="icon" src="../../../static/icon/liked.png"></image>
						<text class="icom" :class="{'liked':subComment.isLike}">{{ subComment.likeNum }}</text>
					</view>
				</view> -->
				<view style="border-top:1px solid #DCDCDC"></view>

			</view>
		</block>
<view class="showMoreSubComment" @tap="goToCommentDetail()">展开剩余评论</view>
	</view>

</template>

<script>
	export default {
		props: {
			mainCommentid: "",
			mainComment:"",
		},
		data() {
			return {

				userInfo: this.getGlobalUserInfo(),

				subCommentList: [],
				subCommentNum:0,
			};
		},
		mounted() {
			this.getSubComments(1);
		},
		methods: {

			getSubComments(page) {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getSubComments',
					data: {
						underCommentId: that.mainCommentid,
						userId: that.userInfo.id,
						page: page,
						type: 0,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
						if (res.data.status == 200) {
							that.subCommentNum = res.data.data.rows.length;
							
							if (res.data.data.rows.length < 3) {
								that.subCommentList = res.data.data.rows
							} else {
								that.subCommentList = res.data.data.rows[i].slice(0, 2)

							}

						}

					}
				});
			},

			// controlInputInRecomment(){
			// 	var dataOfRecomment={
			// 		mode:"re-re",
			// 		toUserId:this.reCommentDetail.fromUserId,
			// 		fatherCommentId:this.reCommentDetail.id,
			// 		underCommentId:this.reCommentDetail.underCommentId,
			// 		nickname:this.reCommentDetail.nickname,
			// 	}
			// 	this.$emit('controlInputSignal',dataOfRecomment)
			// },

			/**
			 * 点赞或取消点赞二级评论
			 * @param {Object} comment
			 */
// 			swLikeSubComment() {
// 				if (this.subComment.isLike) {
// 					this.unLikeComment(this.subComment);
// 					this.subComment.likeNum--;
// 					console.log(this.subComment.likeNum);
// 				} else {
// 					this.likeComment(this.subComment);
// 					this.subComment.likeNum++;
// 				}
// 				this.subComment.isLike = !this.subComment.isLike;
// 			},
// 
// 			likeComment(comment) {
// 				console.log("点赞评论");
// 				var that = this;
// 				uni.request({
// 					method: "POST",
// 					url: that.$serverUrl + '/article/userLikeComment',
// 					data: {
// 						userId: that.userInfo.id,
// 						commentId: comment.id,
// 						createrId: comment.fromUserId,
// 					},
// 					header: {
// 						'content-type': 'application/x-www-form-urlencoded'
// 					},
// 					success: (res) => {
// 						console.log(res);
// 					},
// 				});
// 			},
// 
// 			unLikeComment(comment) {
// 				console.log("取消点赞评论");
// 				var that = this;
// 				uni.request({
// 					method: "POST",
// 					url: that.$serverUrl + '/article/userUnLikeComment',
// 					data: {
// 						userId: that.userInfo.id,
// 						commentId: comment.id,
// 						createrId: comment.fromUserId,
// 					},
// 					header: {
// 						'content-type': 'application/x-www-form-urlencoded'
// 					},
// 					success: (res) => {
// 						console.log(res);
// 					},
// 				});
// 			},

			goToPersonPublic() {
				this.$emit("goToPersonPublic", this.subComment.fromUserId);
			},

			goToCommentDetail(mainComment) {
				console.log('going to commentDeatail');
				uni.navigateTo({
					url: '/pages/comment-detail/comment-detail?data=' + JSON.stringify(this.mainComment),
				})
			},
		}
	}
</script>

<style>
	.contentarea {
		display: inline-block;
		font-size: 12px;
		line-height: 17px;
		margin-right: 5px;
		color: #888888;
	}

	.content {
		font-size: 12px;
		color: #000000;
		line-height: 17px;

		max-height: 34px;
		overflow: hidden;
		text-overflow: ellipsis;
		margin-top: 8px;
		margin-bottom: 8px;
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
		max-width: 85px;
		text-overflow: ellipsis;
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
		align-items: flex-end;
		padding-right: 5px;
	}

	.liked {
		color: #FDD041;
	}










	.sub-comment-content-in-reComment {
		padding-top: 12px;
		padding-bottom: 8px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(53, 53, 53, 1);
	}
	.showMoreSubComment{
		margin-top: 8px;
		width:97px;
		height:20px;
		line-height: 20px;
		background:rgba(209,209,209,1);
		box-shadow:0px 0px 4px rgba(0,0,0,0.16);
		border-radius:36px;
		
		text-align: center;
		margin-left: calc(100% - 97px);
		font-size:12px;
		font-weight:500;
		color:rgba(53,53,53,1);
		}
</style>
