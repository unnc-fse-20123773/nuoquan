<template>
	<view class="comment" id="'+commentDetail.id'">
		<view class="fengexian"></view>
		<view class="contentarea">{{ commentDetail.comment }}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="commentDetail.faceImage" class="touxiang"></image>
				<text class="name">{{ commentDetail.nickname }}</text>

				<text class="time">{{ commentDetail.timeAgo }}</text>
			</view>
			<view class="icons">
				<!-- 评论按钮 -->
				<image class="icon" src="../../../../static/icon/comment.svg"></image>
				<text class="icom">{{ commentDetail.commentNum }}</text>
				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.svg"></image>
				<text class="icom">{{ commentDetail.likeNum }}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'comment',
	props:{
			commentDetail: {}
	},
	data() {
		return {};
	},
	methods:{
		
	}
};
</script>

<style scoped>
.comment {
	/* 	background-color:gray;
 */
	border-radius: 20px;
	width: 90%;
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
	border-radius: 20px;
}
.bottombar {
	position: relative;
	border-radius: 20px;
	height: 25px;
	margin-top: 10px;
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
	color:#888888;
}
.icons {
	justify-content: flex-end;
	display: inline-flex;
	align-items: center;
	width: 30%;
	font-size: 10px;
	
}
.icon {
	width: 11px;
	height: 11px;
	font-size: 2px;
		padding-left: 45upx;
	padding-right:8upx;
	
}


</style>
