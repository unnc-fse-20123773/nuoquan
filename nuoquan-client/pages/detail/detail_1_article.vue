<template>
	<view>
		<!--标题-->
		<view class="detail-title">{{ articleCard.articleTitle }}</view>
		<!--作者信息，头像名字时间-->
		<view class="author-info-bar">
			<image :src="articleCard.faceImg" class="touxiang" @click="goToPersonPublic()"></image>
			<view class="name">{{ articleCard.nickname }}</view>
			<view class="time">{{ articleCard.createDate | timeDeal}}</view>
		</view>
		<!--标签-->
		<view class="detail-tags">
			<view class="tag" :style="{background: tagColorList[index]}" v-for="(i,index) in articleCard.tagList" v-bind:key="index">{{i}}</view>
		</view>
		<!--内容-->
		<view class="detailcontent">{{ articleCard.articleContent }}</view>
		<!--图片区域-->
		<view>
			<!-- 单图显示 -->
			<view v-if="articleCard.imgList.length==1" class="detailpics" style="width: 100%;max-height: 400upx;display: flex;">
				<image v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill" style="height: 360upx;max-width:180px;display: inline-block;"
				 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
			</view>
			<!-- 其他数量 -->
			<view v-else-if="articleCard.imgList.length==4" class="detailpics" style="max-width: 400upx;margin-left: 0;">
				<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
				 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
			</view>

			<view v-else class="detailpics">
				<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
				 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				<view v-if="articleCard.imgList.length==2||imageList.length==5||imageList.length==8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>

			</view>

		</view>
		<!--4个ICON, 点赞评论分享返回-->
		<view class="menu-bar">
			<view class="like">{{articleCard.commentNum}}</view>
			<view class="comment">{{articleCard.commentNum}}</view>
			<view class="share"></view>
			<view class="back"></view>
		</view>
		<view style="border-bottom: 4px solid #ECECEC;height:0;width:750upx;font-size: 0;position: fixed;left:0;">这是分割线</view>
	</view>
</template>

<script>
	export default {
		name: "detail_1_article",
		props: {
			articleCard: "",
		},
		components: {},
		data() {
			return {
				serverUrl: this.$serverUrl,
			};
		},
		created() {},
		filters: {
			timeDeal(timediff) {
				timediff = new Date(timediff);
				var parts = [timediff.getFullYear(), timediff.getMonth() + 1, timediff.getDate(), timediff.getHours(), timediff.getMinutes(),
					timediff.getSeconds()
				];
				var oldTime = timediff.getTime();
				var now = new Date();
				var newTime = now.getTime();
				var milliseconds = 0;
				var timeSpanStr;
				milliseconds = newTime - oldTime;
				if (milliseconds <= 1000 * 60 * 1) {
					timeSpanStr = '刚刚';
				} else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
					timeSpanStr = Math.round((milliseconds / (1000 * 60))) + '分钟前';
				} else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + '小时前';
				} else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + '天前';
				} else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && parts[0] == now.getFullYear()) {
					timeSpanStr = parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				} else {
					timeSpanStr = parts[0] + '-' + parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				}
				return timeSpanStr;
			}
		},

	};
</script>

<style>
	.detail-article {
		width: 100%;
	}

	.detail-title {
		width: 100%;
		color: #4A4A4A;
		font-size: 17px;
		line-height: 21px;
		margin: auto;
		font-weight: 500;
		word-break: break-all;
		word-wrap: break-word;
		padding-top: 10px;
		padding-bottom: 8px;
		max-height: 42px;
		text-align: justify;
	}

	.author-info-bar {
		height: 24px;
		width: 100%;
		position: relative;
	}

	.touxiang {
		width: 24px;
		height: 24px;
		border-radius: 12px;
	}

	.name {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 24px;
		height: 24px;
		color: #9B9B9B;
		display: inline-block;
		position: absolute;
		top: 0;
		left: 32px;
		width: 96px;
	}

	.time {
		position: absolute;
		right: 0;
		top: 0;
		text-align: right;
		font-size: 12px;
		color: #9B9B9B;
		line-height: 24px;
		width: 102px;
	}

	.detail-tags {
		max-height: 20px;
		line-height: 15px;
		width: 100%;
		margin-top: 8px;
		margin-bottom: 12px;
	}

	.tag {
		display: inline-block;
		border-radius: 20px;
		color: #40A792;
		font-size: 11px;
		height: 11px;
		line-height: 11px;
		padding: 6px 10px;
		background: #621E81;
		vertical-align: middle;
	}

	.detailcontent {
		word-break: break-all;
		word-wrap: break-word;
		white-space: pre-line;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(53, 53, 53, 1);
		padding-bottom: 8px;
	}

	.detailpics {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		align-items: center;
		flex-wrap: wrap;
		width: calc(648upx + 12px);
		margin: 0 auto;
		margin-bottom: 9px;
	}

	.detailpic {
		/* 			width: calc((100% - 12px) / 3);*/
		width: 216upx;
		height: 216upx;
		margin: 6px 0;
	}

	.menu-bar {
		height: 68px;
		width: calc(176px + 144upx);
		margin: auto;
		display: flex;
		justify-content: space-between;
		padding-bottom: 3px;
	}

	.comment,
	.like,
	.share,
	.back {
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 11px;
		text-align: center;
		color: #353535;
		padding: 26px 16px 7px;
		border-radius: 22px;
		background: #F6E3BA;
		position: relative;
	}
	.liked{
		background: #FE5F55;
	}
.like::after{
	content: "";
	width: 16px;
	height: 16px;
	background: url(../../static/icon/like.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left:14px;
	top:8px;
}
	.comment::after {
		content: "";
		width: 16px;
		height: 16px;
		background: url(../../static/icon/comment-alt-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:14px;
		top:8px;
	}
	.share::after{
		content: "";
		width: 16px;
		height: 16px;
		background: url(../../static/icon/share-alt-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:14px;
		top:14px;
	}
	.back::after{
		content: "";
		width: 16px;
		height: 16px;
		background: url(../../static/icon/angle-left-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:14px;
		top:14px;
	}
</style>
