<template>
	<view class="reComment">
		<view class="contentarea" @tap="controlInputInRecomment()">回复@{{subComment.toNickname}}: {{subComment.comment}}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="subComment.faceImg" class="touxiang" @tap="goToPersonPublic"></image>
				<text class="name">{{ subComment.nickname }}</text>
				<text class="time">{{ subComment.timeAgo }}</text>
			</view>
			<view class="icons" @tap="swLikeSubComment()">
				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.png"></image>
				<text class="icom">{{ subComment.likeNum }}</text>
			</view>
		</view>
		<view style="border-top:1px solid #DCDCDC"></view>
	
	</view>
</template>

<script>
	export default {
		props:{
			reCommentDetail: {},
		},
		data() {
			return {
				subComment: this.reCommentDetail, // 为了动态修改数值，对对象重新赋值，转换组件内部对象
				
				userInfo: this.getGlobalUserInfo(),
			};
		},
		created() {
			// 监听刷新次级评论事件
			// uni.$on('flashFinish', ()=>{
			// 	this.subComment = this.reCommentDetail;
			// })
		},
		methods:{
			controlInputInRecomment(){
				var dataOfRecomment={
					mode:"re-re",
					toUserId:this.reCommentDetail.fromUserId,
					fatherCommentId:this.reCommentDetail.id,
					underCommentId:this.reCommentDetail.underCommentId,
					nickname:this.reCommentDetail.nickname,
				}
				this.$emit('controlInputSignal',dataOfRecomment)
			},
			
			/**
			 * 点赞或取消点赞二级评论
			 * @param {Object} comment
			 */
			swLikeSubComment(){
				if(this.subComment.isLike){
					this.unLikeComment(this.subComment);
					this.subComment.likeNum--;
					console.log(this.subComment.likeNum);
				} else {
					this.likeComment(this.subComment);
					this.subComment.likeNum++;
				}
				this.subComment.isLike = !this.subComment.isLike;
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
			
			goToPersonPublic(){
				this.$emit("goToPersonPublic", this.subComment.fromUserId);
			}
		}
	}
</script>

<style>
	
	.contentarea {
		padding-top: 9px;
		
		font-size: 10px;
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
		align-items: flex-end;
		padding-right: 5px;
	}
	
</style>
