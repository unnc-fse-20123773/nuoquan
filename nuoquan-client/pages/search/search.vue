<template>
	<view class="mainPageTop">
		<view class="topBar">
			<image class="topBarTouxiang" src="../../static/touxiang.jpg"></image>
			<input class="topBarSearch" placeholder="  搜索" v-model="searchvalue" />
			<view class="topBarPlus">
				<view style="font-size: 20px;color:#FDD041;border-radius: 3px;" @click="searchfunction">搜</view>
			</view>
			<view class="topBarwaiting"></view>
		</view>
		<view>
			<view>打印原始结果res</view>
			<view style="height:10px;"></view>
			<view class="result1 result">{{ oriData }}</view>
			<view style="height:10px;"></view>

			<view>打印res.data.data</view>

			<view class="result2 result">{{ oriData.data.data }}</view>
			<view style="height:10px;"></view>

			<view>打印其他</view>

			<view class="result3 result">
				<!-- 				其他数据
 -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				oriData: {},
				searchvalue: "",
			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "搜索"
			});
		},
		methods: {
			searchfunction() {

				debugger;
				var _this = this;
				// _this.searchvalue
				// 
				console.log(this.searchvalue);
				uni.request({
					url: 'http://127.0.0.1:8080/queryAllArticles',
					method: 'POST',
					success: res => {
						console.log('success');
						console.log(res);
						_this.oriData = res.data.data.rows;
					},
					fail: res => {
						console.log(res);
					}
				});
			}
		}
	}
</script>

<style>
	.sousuokuang {
		height: 50px;
		width: 100%;
	}

	.result {
		width: 100%;
		border: 5px;
		border-color: #b2b2b2;
	}

	.mainPageTop {
		padding-top: 4px;
		/* 	height: 168px;
	 */
		width: 100%;
		background: #fdd041;
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
</style>
