<template>
	<view>
<!--开始 		 此BLOCK,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖      -->
		<view class="comment">
			<view class="comment-info">
				<image :src="commentDetail.faceImg" @tap="goToPersonPublic(commentDetail.fromUserId)"></image>
				<view class="name_text">{{ commentDetail.nickname }}</view>
				<view class="replyTo" v-if="commentDetail.toNickname != null && commentDetail.toNickname != undefined">
					{{commentDetail.toNickname}}
				</view>
				<view class="time_text">{{ commentDetail.timeAgo }}</view>
			</view>
			<view class="comment-content" @tap="controlInputInSonCommentBox()()">{{ commentDetail.comment }}</view>
			<view class="comment-menu">
				<view class="son-comment-num" @tap="controlInputInSonCommentBox()">{{commentDetail.commentNum}}</view>
				<view class="like-num" :class="{liked:commentDetail.isLike}" @tap="swLikeCommentDetail(commentDetail)">{{ commentDetail.likeNum }}</view>
			</view>
		</view>
<!--结束 		 此BLOCK,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖      -->
		
		
	</view>
</template>

<script>
	export default{
		props:{
		  reCommentDetail: '', //沿用之前组件名字了，可改，记得本组件内全替换
		},
		
		data(){
			return{
				commentDetail: this.reCommentDetail,
			}
		},
		
		created() {
			console.log(this.reCommentDetail);
		},
		
		onLoad() {
			console.log("哈哈哈哈哈哈");
		},
		
		methods:{
			controlInputInSonCommentBox(){
				var dataOfRecomment={
					toUserId:this.reCommentDetail.fromUserId,
					fatherCommentId:this.reCommentDetail.id,
					underCommentId:this.reCommentDetail.underCommentId,//这条检查一下是否有传进来这个组件
					nickname:this.reCommentDetail.nickname,
				}
				this.$emit('controlInputSignal',dataOfRecomment);
			},
			
			swLikeCommentDetail(){
				this.$emit('swLikeComment',this.commentDetail);
			},
			
			goToPersonPublic(){
				this.$emit("goToPersonPublic", this.commentDetail.fromUserId);
			},
		},
		
		
	}
	
	
</script>

<style scoped>

	
@import url("../detail/oneComment.css");
/* 此文件,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖
 */	
.replyTo{
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 24px;
	height: 24px;
	color: #9B9B9B;
	display: inline-block;
	/* position: absolute;
	top: 0;
	left: 144px; */
	position: relative;
	left:48px;
	width: 96px;
}
.replyTo::after{
	content: "回复  @";
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 24px;
	height: 24px;
	color: #9B9B9B;
	display: inline-block;
	position: absolute;
	left:-40px;
	width:40px;
	}
</style>
