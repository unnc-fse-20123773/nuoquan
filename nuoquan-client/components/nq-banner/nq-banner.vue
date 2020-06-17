<template>
	<view v-if="showAd" class="banner-container">
		<view class="posterBox">
			<image class="poster" src="../../static/AD/BSA1.jpg" mode="aspectFit" @click="jumpToWeb"></image>
			<view class="killIcon super_center" @click="killAd">
				<image src="../../static/icon/delete.png" mode="aspectFit"></image>
			</view>
		</view>
		<view style="height: 100%;width: 100%;background-color: #000000;opacity: 0.5;"></view>
	</view>
</template>

<script>
	export default {
		name: 'nqBanner',
		data() {
			return {	
				showAd: true
			};
		},
		methods: {
			//跳转广告页
			jumpToWeb(){
				uni.navigateTo({
					url:'../../pages/adWebPage/adWebPage?url=https://mp.weixin.qq.com/s/a6dAWWMISrUGf8gPlYzLzA'
				})
			},
			//组件内伪关闭广告
			killAd() {
				this.showAd = !this.showAd;
			}
		}
	}
</script>

<style>
	.banner-container{
		height: 100%;
		width: 100%;
		position: fixed;
		/* 第五层 */
		z-index: 50; 
	}
	
	.posterBox{
		height: 500px;
		width: 300px;
		position: fixed;
		/* 第六层 */
		z-index: 60;
		background-color: white;
		opacity: 1;
		border-radius: 8px;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		margin-top: auto;
		margin-bottom: auto;
		margin-left: auto;
		margin-right: auto;
	}
	
	.poster{
		max-height: 500px;
	}
	
	.killIcon{
		position: absolute;
		top: 2px;
		right: 2px;
		width: 28px;
		height: 28px;
		border-radius: 50%;
		background-color: #e6e6e6;
	}
	
	.killIcon image{
		width: 20px;
		height: 20px;
	}

</style>
