<template>
	<view style="width:100%;height:100%;margin:auto;">
		<view class="top-bar">
			<view class="totalNum">{{totalNum}}篇文章</view>
			<!-- <view class="bin">
				<image src="../../static/icon/bin.png"></image>
				<view>回收站  {{binNum}}</view>
			</view> -->
		</view>
		<view class="mainbody">
			<myArticles  v-bind:myArticleList="myArticleList"></myArticles>
		</view>
	</view>
</template>

<script>
	import myArticles from './myArticles.vue'
	export default {
			
		components:{
			myArticles,
		},
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style>
	page{
		background: #F8F8F8;
	}
.top-bar{
	width:calc(100% - 42px);
	height:30px;
	padding:24px 0;
	display: flex;
	justify-content: space-between;
	font:Source Han Sans CN;
	margin:auto;
}
.totalNum{
	color:#888888;
	font-size: 18px;
	text-spacing: 80;
}
	
/* .bin{
	position: relative;
	display: inline-flex;
	background: #888888;
	border-radius: 10px;
	height:30px;
	width:108px;
	box-shadow: ;
}
.bin image{
	position: absolute;
	width:14px;
	height:15px;
	top:7.5px;
	left:9px;
	align-items: center;
	
}
.bin view{
	position: absolute;
	right: 14px;
	color:#FFFFFF;
	font-size: 14px;
	text-spacing: 45;
	align-items: center;
	line-height: 30px;
} */
.mainbody{
	width:calc(100% - 26px);
	margin: auto;
}
</style>
