<template>
	<view style="width:100%;height:100%;margin:auto;">
		<button class="reload" @click="reload()">
			<text class="refresh">刷新</text>
		</button>
		<view class="mainbody">
			<articleInfo v-for="item in myArticleList" :key="item.id" :thisArticle="item"></articleInfo>

		</view>
	</view>
</template>

<script>
import articleInfo from './articleInfo.vue'
	export default {
		components:{articleInfo,},
		data() {
			return {
				totalPage: 1,
				currentPage: 1,
			loadArticleFlag:false,
			userInfo: '',
			myArticleList:[],
		};
	},

	onLoad() {
		var userInfo = this.getGlobalUserInfo();
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
	},

	methods: {
		reload: function() {
			this.showArticles(1);
		},
		// 锁
		showArticles: function(page) {
			console.log(this.loadArticleFlag);
			
			if ( this.loadArticleFlag ) {
			
			}

			this.loadArticleFlag = true;

			uni.showLoading({
				title: '加载中...'
			});
			setTimeout(() => {
				if (this.loadArticleFlag) {
					this.loadArticleFlag = false; //解锁
					uni.hideLoading();
					uni.showToast({
						title: '网络未知错误',
						icon: 'none',
						duration: 1000
					});
				}
			}, 5000); //延时五秒timeout

			var that = this;
			uni.request({
				url: that.$serverUrl + '/article/queryAllArticles',
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
					this.loadArticleFlag = false;

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
				this.loadArticleFlag = false;

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
	page{
		background: #F8F8F8;
		position:relative;
	}
.top-bar{
	width:calc(100% - 58px);
	height:30px;
	padding:24px 0;
	display: flex;
	justify-content: space-between;
	font:Source Han Sans CN;
	margin:auto;
}

.totalNum {
	color: #888888;
	font-size: 18px;
	text-spacing: 80;
}
	

.mainbody{
	width:calc(100% - 26px);
	margin: auto;
}
.reload {
		margin-top:20px;
		margin-left:80%;
		width: 60px;
		height: 30px;
		line-height:30px;
		background-color: #FFCF3C;
		border-width: 1upx;
		border-radius: 20upx;
	}

	.refresh{
		font-size:15px;
	}

</style>
