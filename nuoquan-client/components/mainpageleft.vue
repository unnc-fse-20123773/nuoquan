<template>
	<!-- 	<view class="bottomLayerCoverScreen" >
 -->
	<view class="mainPageLeft" @click.stop="">
		<view class="topInfoArea">
			<navigator class="topLeft" url="../../pages/profile/profile">
				<view class="topLeft">
					<image class="touxiang" src="../../../static/touxiang.jpg"></image>
				</view>
			</navigator>
			<view class="topRight">
				<view style="font-size: 13px;margin-bottom:9px;width:100%;vertical-align: bottom;">壹贰叁</view>
				<view style="font-size:8px ;font-weight: bold;margin-bottom:3px;width:100%;vertical-align: bottom;">zy21000</view>
				<view style="color:#B2B2B2;font-size: 6px;width:80px;position: relative;left: 10px;width:100%;vertical-align: bottom;">@nottingham.edu.cn</view>
			</view>
		</view>
		<view class="personPageList">
			<view class="pageLine">
				<image src="../../../static/touxiang.jpg"></image>
				<view>我的发布</view>
			</view>

			<view class="pageLine">
				<navigator class="" url="../../pages/messagelist/messagelist">
					<image src="../../../static/touxiang.jpg"></image>
					<view>我的消息</view>
				</navigator>
			</view>

			<view class="pageLine">
				<image src="../../../static/touxiang.jpg"></image>
				<view>我的收藏</view>
			</view>
			<view class="pageLine">
				<image src="../../../static/touxiang.jpg"></image>
				<view>举报投诉</view>
			</view>
			<view class="pageLine">
				<image src="../../../static/touxiang.jpg"></image>
				<view>关于</view>
			</view>
		</view>
	</view>
	<!-- 	</view>
 -->
</template>

<script>
	name: 'mainpageleft'

	export default {

		data() {
			return {};
		},

	};
</script>

<style>
	@keyframes slidefromleft {
		from {
			display: none;
		}

		to {
			display: block;
		}
	}

	.bottomLayerCoverScreen {
		position: fixed;
		width: 750upx;
		height: 100%;
		z-index: 5;

	}

	.mainPageLeft {
		background: #fcfcfc;
		animation: slidefromleft 3s;
		width: 174px;
		height: 100%;
		position: fixed;
		left: 0;
		top: 0;
		box-shadow: 1px 0px 5px #a6a6a6;
		border-bottom-right-radius: 80px;
	}

	.topInfoArea {
		height: 60px;
		margin-top: 29px;
	}

	.topLeft {
		margin-left: 20px;
		display: inline-block;
		width: 60px;
		vertical-align: top;
	}

	.touxiang {
		height: 60px;
		width: 60px;
		border-radius: 60px;
		display: block;
	}

	.topRight {
		display: inline-block;
		margin-left: 8px;
		height: 60px;
		width: 80px;
		vertical-align: bottom;
	}

	.personPageList {
		margin-top: 40px;
	}

	.pageLine {
		margin-left: 34px;
		margin-bottom: 20px;
	}

	.pageLine image {
		width: 20px;
		height: 20px;
		display: inline-block;
		vertical-align: middle;
	}

	.pageLine view {
		display: inline-block;
		font-size: 13px;
		font-weight: bold;
		margin-left: 17px;
	}
</style>
