<template>
	<view class="reComment">
		<view class="contentarea" @tap="controlInputInRecomment()">回复@{{reCommentDetail.toNickname}}: {{reCommentDetail.comment}}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="reCommentDetail.faceImg" class="touxiang"></image>
				<text class="name">{{ reCommentDetail.nickname }}</text>
				<text class="time">{{ reCommentDetail.timeAgo }}</text>
			</view>
			<view class="icons">
				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.png"></image>
				<text class="icom">{{ reCommentDetail.likeNum }}</text>
			</view>
		</view>
		<view style="border-top:1px solid #DCDCDC"></view>
	
	</view>
</template>

<script>
	export default {
			
		props:{
			reCommentDetail:{},
		},
		data() {
			return {
				
			};
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
