<template>
	<view>
		<!-- 		<navigator url="../../pages/search/search" class="topBarSearch searchPosition">
		</navigator> -->

		<view class="bottomLayerOfLeft" v-if="showMainPageLeft" @click="controlShowLeft(0)">
			<mainpageleft></mainpageleft>
		</view>

		<view class="mainPageTop">
			<view class="topBar">
				<image class="topBarTouxiang" src="../static/touxiang.jpg" @click="controlShowLeft(1)"></image>
				<!-- 从这里开始为测试搜索下拉窗口 -->
				<!-- <view id="menu">
					<input class="topBarSearch" placeholder="  搜索" />
					<view id="list">
						<view>item</view>
						<view>item</view>
						<view>item</view>
						<view>item</view>
						<view>item</view>
					</view>
				</view> -->
				<!-- 测试到这里结束 -->
				<input class="topBarSearch" placeholder="  搜索" />
				<view class="topBarPlus">
					<!-- Guetta-直接在 icon 上加了跳轉 -->
					<navigator url="../../pages/submit/submit" style="font-size: 20px;color:#FDD041;border-radius: 3px;">+</navigator>
				</view>
				<!-- <view class="topBarwaiting"></view>  该语句未见明显效果，故注释掉 -->
			</view>
			<view class="topicTitle">话题榜</view>

			<view class="topicArea">
				<view class="hot1">
					<view class="hotNum1" style="font-size: 21px;margin-bottom: 14px;">1</view>
					<view class="hotContent1" style="font-size: 15px;margin-bottom: 14px;">麻辣鸡结婚了</view>
				</view>
				<view class="hot2">
					<view class="hotNum2" style="font-size: 17px;margin-bottom: 12px;">2</view>
					<view class="hotContent2" style="font-size: 13px;margin-bottom: 12px;">打吊瓶看熊猫被终生免票打吊瓶看熊猫被...</view>
				</view>
				<view class="hot3">
					<view class="hotNum3" style="font-size: 15px;">3</view>
					<view class="hotContent3" style="font-size: 13px;">高考成绩今日公布</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import mainpageleft from '@/components/mainpageleft.vue';
	export default {
		data() {
			return {
				showMainPageLeft: 0
			};
		},
		components: {
			mainpageleft
		},
		methods: {
			controlShowLeft(a) {
				this.showMainPageLeft = a;
				console.log(a);
				console.log(this.showMainPageLeft);
			}
		}
	};
</script>

<style>
	page {
		width: 100%;
	}

	.bottomLayerOfLeft {

		position: fixed;
		width: 750upx;
		height: 1000px;
		z-index: 3;
	}

	.mainPageTop {
		display: flex;
		flex-direction: column;
		padding-top: 4px;
		height: 168px;
		width: 100%;
		background: #FDD041;
		box-shadow: 0 -2px 10px #000000;
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
		width: 190px;
		height: 28px;
		vertical-align: middle;
		border-radius: 8px;
		margin-left: 13px;
		background: white;
		letter-spacing: 1px;
		color: #b2b2b2;
		font-family: MicrosoftYaHei;
		line-height: 10px;
		min-height: 28px;
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
		max-width: calc(750upx - 77px);
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.searchPosition {
		/* position: fixed; */
		background-color: red;
		left: 42px;
		top: 5px;
	}

	/* 以下为测试用 CSS 代码 */
	#list {
		width: 520upx;
	}

	#menu #list {
		max-height: 0;
		transition: max-height 0.25s ease-out;
		overflow: hidden;
		background: blue;
	}

	#menu:hover #list {
		max-height: 1000px;
		transition: max-height 0.25s ease-in;
	}
</style>
