<template>
	<view style="width: 100%;height: 100%;">
		123
		<view class="comments-order">
		       <view class="order-in-time" :class="{ chosen : order == 0}" @tap="change_comment_order(0)">
				   时间
			   </view>
			   <view class="order-in-hot" :class="{ chosen : order != 0}" @tap="change_comment_order(1)">
			   	   热度
			   </view>
			   <view class="bg-of-order" :style="{'left':order == 0 ? '-13px;' :'41px' ,}"></view>
		</view>
		
		<!--循环评论卡片  来源 @pages/detail/oneComment.css -->
		<view class="comments">
			<block v-for="mainComment in mainCommentList" :key="mainComment.id">
		
				<view class="comment">
					<view class="comment-info">
						<image :src="pathFilter(mainComment.faceImg)" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
						<view class="name_text">{{ mainComment.nickname }}</view>
						<view class="time_text">{{ mainComment.timeAgo }}</view>
					</view>
					<view class="comment-content" @tap="goToCommentDetail(mainComment)">{{ mainComment.comment }}</view>
					<reComment :mainCommentid="mainComment.id"  :mainComment="mainComment"  style='width: 100%;height:100%;'></reComment>
					<view class="comment-menu">
						<view class="son-comment-num" @tap="goToCommentDetail(mainComment)">{{mainComment.commentNum}}</view>
						<view class="like-num" :class="{liked:mainComment.isLike}" @tap="swLikeMainComment(mainComment)">{{ mainComment.likeNum }}</view>
					</view>
				</view>
			</block>
		</view>
		<!--循环评论卡片  来源 @pages/detail/oneComment.css END -->
		
	</view>
</template>

<script>
	export default {
		props:{
		
		},
		data() {
			return {
				
			};
		},
		onLoad() {
			
		},
		methods:{
		},
	}
</script>

<style> /* 排序方式 */
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
