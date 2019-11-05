<template>
	<!--评论区域-->
	        <view class="comments-area">
			<!--评论数，排序方式-->
				<view class="comment-menu">
					<view class="comment-num">{{articleCard.commentNum}}</view>
					<view class="comment-order"></view>
				</view>
			<!--循环评论卡片-->
				<view class="comments">
<!-- 					<commentbox v-for="i in commentList" :key="i.id" v-bind:commentDetail="i" @controlInputSignal="controlInput">		</commentbox>
 -->				</view>
			<!--触底提示和功能-->
			    <view class="comment-bottom">
					<view class="comment-bottom-notice">划到底部啦</view>
					<view class="comment-bottom-buttons">
						<image src="" mode=""></image>
						<image></image>
						<view class="">发表评论</view>
					</view>
					
				</view> 
			</view>
</template>

<script>
</script>

<style>
</style>
