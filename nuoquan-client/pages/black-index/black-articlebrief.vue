<template>
	<view class="">
		<view class="articlecard" id="'+articleCard.id+'">
			<image src="../../static/icon/star.png" mode="" class="star"></image>

			<view class="user">{{ articleCard.nickname }}</view>
			<view class="content">{{ articleCard.articleContent }}</view>
			<!-- <view class="picturearea">
				<image src="../static/0001/pic1.jpg" v-if="articleCard[3]"></image>
				<image src="../static/0001/pic2.jpg" v-if="articleCard[4]"></image>
				<image src="../../static/0001/pic3.jpg" v-if="articleCard[5]"></image>
			</view> -->
		</view>
		<view class="menubar">
			<view class="time">05-20 14:30</view>

			<view class="icons">
				<image class="comment" src="/static/icon/comment.svg">{{ articleCard.commentNum }}</image>
				<image class="like" src="/static/icon/like.svg">{{ articleCard.likeNum }}</image>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'aticlebrief',
	props: {
		articleCard: {}
	},
	data() {
		return {};
	},
	created() {
		console.log(this.articleCard);
		console.log(typeof this.articleCard.tags);
	},
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
.articlecard {
	position: relative;
	color: #ffffff;
	width: 650upx;
	background-color: #313131;
	margin: auto;
	border-radius: 8px 8px 0 0;
}
.star {
	display: inline-block;
	width: 14px;
	height: 14px;
	color: #ffffff;
	margin-left: 13px;
}
.content::after {
	content: '';
	width: 100%;
	height: 10px;
	position: absolute;
	left: 0;
	bottom: 0;
	border-radius: 50% 50% 0 0;
	background: #000000;
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
	margin: 10px 13px 0 15px;
	font-size: 13px;
	line-height: 15px;
	padding-bottom: 31px;
}

.menubar {
	position: relative;
	vertical-align: middle;
	border-radius: 0 0 8px 8px;
	width: 630upx;
	height: 13px;
	margin: auto;
	padding-bottom: 5px;
	background: #585858;
	vertical-align: top;
}
.menubar::after {
	content: '';
	width: 100%;
	height: 10px;
	position: absolute;
	left: 0;
	top: -10px;
	border-radius: 53.65% 50% 0 0;
	background: #585858;
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
</style>
