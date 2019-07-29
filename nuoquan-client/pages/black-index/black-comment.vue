<template>
	<view class="comment">
		<view class="userinfo">
			<image src="../../static/icon/star.png" mode="" class="star"></image>
			
			<view class="user">1234`</view>
			<view class="reply" >回复</view>
			<view class="user">345</view>
		</view>
		<view class="content">
			{{comment.content}}
		</view>
		<view class="menubar">
			<view class="time">05-20 14:30</view>
		
			<view class="icons">
				<image class="comment" src="/static/icon/comment.svg">{{ articleCard.commentNum }}</image>
				<image class="like" src="/static/icon/like.svg">{{ articleCard.likeNum }}</image>
			</view>
		</view>
		<view class="dividingLine">
			
		</view>
		
		
	</view>
</template>

<script>
	export default{
		data(){
			return{
				comment:{
					user1:'DEYAN',
					content:'五i有人要为u如有必要从开播初期二维表结构教务科个人成绩考入个人空间窗口关闭窗口计出万全人口结构',
					user2:'YY',
					time:'05-20  14:30',
				}
			}
		}
		
		
	}
</script>

<style>
	.comment{
		width:634upx;
		margin:auto;
		background: #A6A6A6;
		
	}
	.star {
		display: inline-block;
		width: 14px;
		height: 14px;
		color: #ffffff;
		margin-left: 13px;
		margin-top: 10px;
	}
	.user {
		margin: 16px 25px 0 25px;
		font-size: 10px;
		font: MicrosoftYaHei;
		font-weight: bold;
		line-height: 19px;
		margin: 16px 13px 0 15px;
		display: inline-block;
		color: #D5D5D5;
	}
	.reply {
		margin: 16px 25px 0 25px;
		font-size: 10px;
		font: MicrosoftYaHei;
		font-weight: bold;
		line-height: 19px;
		margin: 16px 13px 0 15px;
		display: inline-block;
		color: #E7E7E7;
	}
	.content{
		width: 536upx;
		margin:12px auto;
		font-size: 13px;
		
	}
	.menubar {
		position: relative;
		vertical-align: middle;
		width: 100%;
		height: 13px;
		margin: auto;
		vertical-align: top;
	}
	
	.time {
		display: inline-block;
		left: 120px;
		font-size: 10px;
		margin-left: 25px;
		color: #888888;
		vertical-align: top;
	}
	
	.icons {
		position: absolute;
		right: 0;
		top: 0;
		height: 13px;
		width: 206upx;
		text-align: right;
		display: inline-block;
		content-flex: right;
		overflow: hidden;
		border-bottom-right-radius: 8px;
	}
	
	.icons image {
		width: 11px;
		height: 11px;
		padding-right: 10px;
		color: #ffffff;
		vertical-align: top;
	}
	
	image {
		width: 30%;
		height: 200upx;
		margin: auto;
	}
	.dividingLine{
		width:562upx;
		margin:9px auto 0;
		border-top:1px solid #838383;
		padding-bottom: 3px;
		
	}
</style>
