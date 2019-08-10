<template>
	<view class="index">
		<view class="displayArea">
			<input placeholder=" 搜索" class="search" />
			<view class="timing">
				<view class="timingHour">10</view>
				<view class="timingHourText">  小时</view>
				<view class="timingMin">10</view>
				<view class="timingMinText">  分后清空</view>
			</view>
			<view class="articles"></view>
		</view>

		<view class="" style="width:20px;height:20px;position: fixed;left: 350upx;top:300px;background: #621E81;" @click="loadcss()"></view>
		<blackarticlebrief v-for="i in showlist" :key="i.id" v-bind:articleCard="i"></blackarticlebrief>
		<blackpopup :articleCard="showlist[0]"></blackpopup>
	</view>
</template>

<script>
import blackarticlebrief from './black-articlebrief'
import blackpopup from './black-popup'
export default {
	data() {
		return {
			title: 'Hello',
			hottitlelist: ['热门标题111', '热门标题222', '热门标题333'],
			showlist: ''
		};
	},
	components: {
		blackarticlebrief,
		blackpopup,
	},

	created: function() {
		var _this = this;
		uni.request({
			url: this.SeverUrl+'/queryAllArticles',
			method: 'POST',
			success: res => {
				_this.showlist = res.data.data.rows;
			},
			fail: res => {
				console.log('index unirequest fail');
				console.log(res);
			}
		});
	},
	onLoad() {},
	methods: {
		loadcss() {
			console.log('black-index');
			require('./black-index.css');
			console.log('blac-index');
		}
	}
};
</script>
<style>
body {
}
.index {
	background-color: #000000;
	height:100%;
	width:100%;
}
.displayArea {
	width: 87.2%;
	margin-top: 5px;
	margin: auto;
}
.search {
	font-size: 15px;
	display: inline-block;
	width: 190px;
	height: 28px;
	vertical-align: middle;
	border-radius: 8px;
	background: white;
	letter-spacing: 1px;
	color: #b2b2b2;
	font-family: MicrosoftYaHei;
	line-height: 10px;
	min-height: 28px;
}
.timing {
	height: 86px;
	width: 100%;
}
.timing view {
	display: inline-block;
	color: #b2b2b2;
	margin-top: 27px;
	margin-bottom: 25px;
}
.timingHour,
.timingMin {
	font-size: 25px;
	height: 35px;
	width: 37px;
border-radius: 5PX;
 	background: linear-gradient(#4D4D4D 49%, #5F5F5F 49%);
	text-align: center;
}
.timingHourText,
.timingMinText {
	font-size: 10px;
	height: 10px;
	margin: 0 5px;
}
</style>
