<template>
	<view class="bottomCover">
		<view class="articlecard" id="'+articleCard.id+'">
			<image src="../../static/icon/star.png" mode="" class="star"></image>

			<view class="user">{{ articleCard.nickname }}</view>
			<view class="content">{{ articleCard.articleContent }}</view>
			<view class="menubar">
				<view class="time">05-20 14:30</view>

				<view class="icons">
					<image class="comment" src="/static/icon/comment.svg">{{ articleCard.commentNum }}</image>
					<image class="like" src="/static/icon/like.svg">{{ articleCard.likeNum }}</image>
				</view>
				<view class="bendingAndShadow"></view>
			</view>
		</view>
		<blackcomment></blackcomment>
		<blackcomment></blackcomment>

		<blackcomment></blackcomment>

		<blackcomment></blackcomment>

		<blackcomment></blackcomment>
	</view>
</template>

<script>
import blackcomment from './black-comment';
export default {
	name: 'aticlebrief',
	props: {
		articleCard: {}
	},
	components: {
		blackcomment
	},
	data() {
		return {};
	},
	created() {},
	methods: {
		jumpToDetail() {
			var navData = JSON.stringify(this.articleCard); // 这里转换成 字符串
			uni.navigateTo({
				url: '/pages/detail/detail?data=' + navData
			});
		}
	}
};
</script>

<style>
.bottomCover {
	position: fixed;
	top: 0;
	left: 0;
	width: 750upx;
	height: 100%;
	background: #621e81;
	z-index: 3;
}
.articlecard {
	position: relative;
	color: #ffffff;
	width: 676upx;
	background-color: #313131;
	margin: 136upx auto 17px;
	border-radius: 8px 8px 0 0;
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
	font-size: 15px;
	font: MicrosoftYaHei;
	font-weight: bold;
	line-height: 19px;
	margin: 16px 13px 0 15px;
	display: inline-block;
}

.content {
	margin: auto;
	margin-top: 20px;
	font-size: 13px;
	line-height: 15px;
	padding-bottom: 22px;
	width: 548upx;
}

.menubar {
	position: relative;
	vertical-align: middle;
	width: 100%;
	height: 13px;
	margin: auto;
	background: #313131;
	vertical-align: top;
}
.menubar::after {
	content: '';
	width: 100%;
	height: 14px;
	position: absolute;
	left: 0;
	top: 7px;
	border-radius: 100%/14px;
	background: #313131;
	z-index: -1;
	margin-bottom: 20px;
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
.bendingAndShadow {
	position: absolute;
	top:0;
	left:21upx;
	right:21upx;
	width: 634upx;
	height: 30px;
	z-index: -2;
	background: #b2b2b2;
	margin: auto;
}
</style>
