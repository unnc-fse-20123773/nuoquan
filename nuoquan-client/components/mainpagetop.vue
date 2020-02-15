<template>
	<view class="mainPageTopAll" style="height:100%;">
		<!-- 盛放top功能区，height屏幕高度，宽度0 -->
		<view class="bottomLayerOfLeft" v-if="showMainPageLeft" @click="controlShowLeft(0)" @touchmove="controlShowLeft(0)">
			<mainpageleft :userInfo="userInfo"></mainpageleft>
		</view>

		<searchpage v-if="showSearch" class="searchPage" @exitSearchSignal="controlShowSearch"></searchpage>

		<view class="mainPageTop" style="max-height: 160px;" :style="{ height: topHeight + 'px;' }">
			<!-- 搜索行 -->
			<view class="topBar">
				<!-- 头像，备用 <image class="topBarTouxiang" :src='userInfo.faceImg' @click="controlShowLeft(1)"></image> -->
				<view class="topBarSearch" @click="controlShowSearch(1)">
					<image src="../static/icon/search_B79144.png" mode="aspectFit"></image>
				</view>
				<!-- 新建，备用 <view class="topBarPlus" @click="jumpToSubmit()">
					<view style="font-size: 20px;color:#FDD041;border-radius: 3px;">+</view>
				</view> -->
				<view class="topBarwaiting"></view>
			</view>

			<!-- 热门卡片 -->
			<view class="hotestBox">
				<view class="hotestCard">
					<!-- 左侧图标 -->
					<view class="iconBox" @click="jumpTohot">
						<image src="../static/BG/hotest.png" mode="aspectFit" class="fireIcon"></image>
						<image src="../static/icon/hotText.png" mode="aspectFit" class="hotText"></image>
					</view>
					
					<!-- 分割线 -->
					<view class="grayline"></view>
					
					<!-- 热门标题 -->
					<swiper class="swiperCard" :indicator-dots="false" :autoplay="true" :interval="3000" :vertical="true" :duration="1000">
						<view >
							<swiper-item>
								<view class="itemCard">
									<view class="hotTitle">{{ topArticles[0].articleTitle }}</view>
									<view class="userInfo">
										<image src="../static/icon/logo_app.png" mode="aspectFit"></image>
										<view class="userid_mainpagetop">
											陈仅仅一号
										</view>
									</view>
								</view>
							</swiper-item>
						</view>
					</swiper>
				</view>
			</view>

			<!-- <view class="topicArea">
				<view class="hot1">
					<view class="hotNum1" style="font-size: 21px;margin-bottom: 14px;">1</view>
					<view class="hotContent1" style="font-size: 15px;margin-bottom: 14px;" @tap="goToDetail(topArticles[0])">{{topArticles[0].articleTitle}}</view>
				</view>
				<view class="hot2">
					<view class="hotNum2" style="font-size: 17px;margin-bottom: 12px;">2</view>
					<view class="hotContent2" style="font-size: 13px;margin-bottom: 12px;" @tap="goToDetail(topArticles[1])">{{topArticles[1].articleTitle}}</view>
				</view>
				<view class="hot3">
					<view class="hotNum3" style="font-size: 15px;">3</view>
					<view class="hotContent3" style="font-size: 13px;" @tap="goToDetail(topArticles[2])">{{topArticles[2].articleTitle}}</view>
				</view>
			</view> -->
		</view>
		<view style="width: 100%;height: 30px;background-color: #007AFF;position: fixed;top: 150px;"></view>
	</view>
</template>

<script>
import mainpageleft from '@/components/mainpageleft.vue';
import searchpage from '../pages/search/search';
export default {
	props: {
		// 渲染时候替换默认值会被替换
		userInfo: {
			faceImg: '../static/touxiang.jpg'
		},
		topArticles: '',
		topHeight: ''
	},
	components: {
		mainpageleft,
		searchpage
	},

		data() {
			return {
				showMainPageLeft: 0,
				showSearch: 0,
			};
		},


	methods: {
		controlShowLeft(a) {
			this.showMainPageLeft = a;
			// console.log(this.showMainPageLeft);
		},
		controlShowSearch(a) {
			console.log('this is controlShowSearch, receive data will be appied');
			console.log(a);
			this.showSearch = a;
		},
		jumpToSubmit() {
			uni.navigateTo({
				url: '/pages/submit/submit'
			});
		},
		goToDetail(article) {
			uni.navigateTo({
				url: '/pages/detail/detail?data=' + JSON.stringify(article)
			});
		},
		jumpTohot() {
			uni.navigateTo({
				url: '/pages/hot/hot'
			})
		}
	}
};
</script>
<style>
page {
	width: 100%;
}
</style>
<style scoped>
.searchPage {
	height: 100%;
	width: 100%;
	position: fixed;
	top: 0;
	left: 0;
	background: #ffffff;
	z-index: 10;
}

.bottomLayerOfLeft {
	position: fixed;
	width: 750upx;
	height: 1000px;
	z-index: 3;
}

.mainPageTop {
	padding-top: 4px;
	position: fixed;
	left: 0;
	top: 0;
	overflow: hidden;
	width: 100%;
	background-size: 100% 100%;
	box-shadow: 0px 0px 10px 1px #c0c0c0;
	background-image: linear-gradient(#ffc95a, #f89d4d);
}

.topBar {
	width: 100%;
	height: 30px;
	margin-top: 23px;
	/* 此处需要兼容性处理 47px */
}

/* 头像,备用
	.topBarTouxiang {
		width: 30px;
		height: 30px;
		border-radius: 30px;
		display: inline-block;
		vertical-align: middle;
		margin-left: 12px;
	} */

.topBarSearch {
	width: 256px;
	height: 30px;
	background: rgba(255, 247, 231, 1);
	opacity: 1;
	border-radius: 75px;
	font-size: 15px;
	display: inline-block;
	vertical-align: middle;
	margin-left: 3.47%;
}

.topBarSearch image {
	width: 16px;
	height: 16px;
	opacity: 1;
	margin: 7px 3px;
}

.topBarPlus {
	font-size: 25px;
	background: #ffffff;
	display: inline-block;
	height: 25px;
	width: 25px;
	vertical-align: middle;
	text-align: center;
	margin-left: 10px;
	border-radius: 3px;
	line-height: 23px;
}

/* 热门话题卡片 */
.hotestBox {
	height: 62px;
	width: 100%;
}

.hotestCard {
	display: flex;
	width: 93.07%;
	height: 62px;
	background: rgba(252, 252, 252, 1);
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
	opacity: 1;
	border-radius: 16px;
	margin-top: 12px;
	margin-left: 3.47%;
}

.iconBox {
	position: relative;
	width: 62px;
	height: 62px;
}

.fireIcon {
	position: absolute;
	z-index: 10;
	height: 62px;
	width: 62px;
}

.hotText {
	position: absolute;
	top: 8px;
	left: 29px;
	height: 14px;
	width: 26px;
	z-index: 20;
}

.grayline {
	height: 41px;
	width: 2px;
	background-color: #ececec;
	margin-top: 14px;
}

.swiperCard{
	width: 74.21%;
	height: 62px;
	margin-left: 11px;
}

.itemCard{
	width: 100%;
	height: 62px;
}

.userInfo{
	position: relative;
	width: 100%;
	height: 17px;
	margin-top: 8px;
}

.userInfo image{
	position: absolute;
	width: 17px;
	height: 17px;
	border-radius: 50%;
}

.userid_mainpagetop{
	position: absolute;
	left: 25px;
	height:12px;
	width: 100%;
	font-size:12px;
	font-weight:400;
	color:rgba(155,155,155,1);
	opacity:1;
}

.hotTitle {
	width: 100%;
	height: 17px;
	margin-top: 12px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 21px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
}
</style>
