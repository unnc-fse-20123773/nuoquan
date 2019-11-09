<template>
	<view style="width:100%;height:100%;margin:auto;background: #FFFFFF;">
		<view class="top-bar">
			<view class="totalNum">{{ totalNum }}篇文章</view>
			<!-- <view class="bin">
				<image src="../../static/icon/bin.png"></image>
				<view>回收站  {{binNum}}</view>
			</view> -->
		</view>
		<view class="mainbody"><myArticles v-bind:myArticleList="myArticleList"></myArticles></view>
	</view>
</template>

<script>
import myArticles from './myArticles.vue';

var loadArticleFlag = false;
export default {
	components: {
		myArticles
	},
	data() {
		return {
			userInfo: '',
			binNum: '12',
			
			totalPage: 1,
			currentPage: 1,
			totalNum: '0',
			myArticleList: '',
		};
	},

	onLoad() {
		var userInfo = this.getGlobalUserInfo();
		console.log(userInfo);
		if (this.isNull(userInfo)) {
			uni.redirectTo({
				url: '../signin/signin'
			});
			return;
		} else {
			this.userInfo = userInfo; // 刷去默认值(若有)
		}

		this.mySocket.init(); // 初始化 Socket, 离线调试请注释掉
		var page = this.currentPage;
		this.showArticles(page);
		
		uni.$on("refresh", () => {
			this.showArticles(1);
		});
	},

	methods: {
		// 锁
		showArticles: function(page) {
			console.log(loadArticleFlag);

			if (loadArticleFlag) {
				loadArticleFlag = false;
			}

			loadArticleFlag = true;

			uni.showLoading({
				title: '加载中...'
			});
			setTimeout(() => {
				if (loadArticleFlag) {
					loadArticleFlag = false; //解锁
					uni.hideLoading();
					uni.showToast({
						title: '网络未知错误',
						icon: 'none',
						duration: 1000
					});
				}
			}, 5000); //延时五秒timeout

			var that = this;
			console.log(that.userInfo);
			uni.request({
				url: that.$serverUrl + '/article/queryPublishHistory',
				method: 'POST',
				data: {
					page: page,
					userId: that.userInfo.id,
					targetId: that.userInfo.id //应该为targetId,但缺少publicProfile的数据传输,暂时用userId测试
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);

					setTimeout(() => {
						//延时加载
						uni.hideLoading();
						loadArticleFlag = false;

						console.log(res);
						if (page == 1) {
							that.myArticleList = [];
						}
						var newArticleList = res.data.data.rows;
						var oldArticleList = that.myArticleList;
						that.myArticleList = oldArticleList.concat(newArticleList);
						that.currentPage = page;
						that.totalPage = res.data.data.total;
						that.totalNum = res.data.data.records;
						console.log(that.totalNum);
					}, 300);
				},
				fail: res => {
					uni.hideLoading();
					loadArticleFlag = false;

					console.log('index unirequest fail');
					console.log(res);
				}
			});
		},
		loadMore: function() {
			var that = this;
			var currentPage = that.currentPage;
			console.log(currentPage);
			var totalPage = that.totalPage;
			console.log(totalPage);
			// 判断当前页数和总页数是否相等
			if (currentPage == totalPage) {
				// that.showArticles(1);
				uni.showToast({
					title: '没有更多文章了',
					icon: 'none',
					duration: 1000
				});
			} else {
				var page = currentPage + 1;
				that.showArticles(page);
			}
		}
	}
};
</script>

<style>
page {
	background: #f8f8f8;
}
.top-bar {
	width: calc(100% - 58px);
	height: 30px;
	padding: 24px 0;
	display: flex;
	justify-content: space-between;
	font: Source Han Sans CN;
	margin: auto;
}

.totalNum {
	color: #888888;
	font-size: 18px;
	text-spacing: 80;
}

/* .bin{
	position: relative;
	display: inline-flex;
	background: #888888;
	border-radius: 10px;
	height: 30px;
	width: 108px;
	box-shadow: ;
}
.bin image {
	position: absolute;
	width: 14px;
	height: 15px;
	top: 7.5px;
	left: 9px;
	align-items: center;
}
.bin view {
	position: absolute;
	right: 14px;
	color: #ffffff;
	font-size: 14px;
	text-spacing: 45;
	align-items: center;
	line-height: 30px;
} */
.mainbody {
	width: calc(100% - 26px);
	margin: auto;
}
</style>
