<template>
	<view class="mainPageTopAll" style="height:100%;">
		<!-- 盛放top功能区，height屏幕高度，宽度0 -->
		<view class="bottomLayerOfLeft" v-if="showMainPageLeft" @click="controlShowLeft(0)" @touchmove="controlShowLeft(0)">
			<mainpageleft :userInfo="userInfo"></mainpageleft>
		</view>

		<searchpage v-if="showSearch" class="searchPage" @exitSearchSignal="controlShowSearch"></searchpage>
		<!-- 		<image class="back" src="../static/icon/angle-left.png" 
		@tap="controlShowSearch(0)" style="display: inline-block;width: 32px;
		height: 32px;background: #FDD041;border-radius: 8px;position: fixed;
		top:10px;left:23px"></image>	备用，返回按钮在组件内和组件外两个方案		-->

		<view class="mainPageTop" style="max-height: 160px;" :style="{height: topHeight +'px;'}">
			<view class="topBar" style="width: 100%;">
				<image class="topBarTouxiang" :src='userInfo.faceImg' @click="controlShowLeft(1)"></image>
				<view class="topBarSearch" @click="controlShowSearch(1)" >⠀搜索</view>
				<view class="topBarPlus" @click="jumpToSubmit()">
					<view style="font-size: 20px;color:#FDD041;border-radius: 3px;">+</view>
				</view>
				<view class="topBarwaiting"></view>
			</view>
			<view class="topicTitle">话题榜</view>

			<view class="topicArea">
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
			</view>
		</view>
	</view>
</template>

<script>
	import mainpageleft from '@/components/mainpageleft.vue';
	import searchpage from '../pages/search/search'
	export default {
		props: {
			// 渲染时候替换默认值会被替换
			userInfo: {
				faceImg: '../static/touxiang.jpg',
			},
			topArticles: '',
			topHeight: '',
		},
		components: {
			mainpageleft,
			searchpage,
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
				console.log("this is controlShowSearch, receive data will be appied");
				console.log(a);
				this.showSearch = a;
			},
			jumpToSubmit() {
				uni.navigateTo({
					url: '/pages/submit/submit'
				});
			},
			goToDetail(article){
				uni.navigateTo({
					url: '/pages/detail/detail?data=' + JSON.stringify(article)
				});
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
		background: #FFFFFF;
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
		background: url(../static/BG/indexBG2.png), url(../static/BG/indexBG.png);
		background-size: 100% 100%;
		box-shadow: 0px 0px 10px 1px #C0C0C0;
		border-bottom-right-radius: 25px;
		border-bottom-left-radius: 25px;
	}

	.topBarTouxiang {
		width: 30px;
		height: 30px;
		border-radius: 30px;
		display: inline-block;
		vertical-align: middle;
		margin-left: 12px;
	}

	.topBarSearch {
		font-size: 15px;
		display: inline-block;
		width: 70%;
		height: 28px;
		vertical-align: middle;
		border-radius: 8px;
		margin-left: 13px;
		background: white;
		letter-spacing: 1px;
		color: #b2b2b2;
		font-family: MicrosoftYaHei;
		line-height: 30px;

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

	.topicTitle {
		color: #ffffff;
		margin-left: 29px;
		font-size: 13px;
		font-weight: bold;
		margin-top: 16px;
		margin-bottom: 10px;
	}

	.hot1,
	.hot2,
	.hot3 {
		height: 17px;
		line-height: 17px;
		/* margin-bottom: 10px;
 */
		padding-bottom: 10px;
	}

	.hotNum1,
	.hotNum2,
	.hotNum3 {
		color: #ffffff;
		width: 77px;
		font-weight: bold;
		text-align: center;
		vertical-align: middle;
		display: inline-block;
	}

	.hotContent1,
	.hotContent2,
	.hotContent3 {
		display: inline-block;
		vertical-align: middle;
		color: #ffffff;
		overflow: hidden;
		/* max-width: calc(750upx - 77px); */
		max-width: calc(700upx - 77px);
		text-overflow: ellipsis;
		white-space: nowrap;
	}
</style>
