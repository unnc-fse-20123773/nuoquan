<template>
	<view style="width:100%;height:100%;margin:auto;">
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}" left-icon="back" left-text="返回" :title="pageTitle" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view class = "top">
			<text class="topleft">下次更新 {{minute}}分{{second}}秒</text>
			<button class="topright" @click="reload()">
				<text class="refresh">刷新</text>
				<image class="icon" src="../../static/icon/refresh-ffffff.png"></image>
			</button>
		</view>
		<view class="mainbody">
			<articleInfo :myArticleList="myArticleList"></articleInfo>
		</view>
	</view>
</template>

<script>
import articleInfo from './articleInfo.vue';
import uniNavBar from "./components/uni-nav-bar/uni-nav-bar.vue";
	export default {
		components:{
			articleInfo,
			uniNavBar
		},
		data() {
			return {
				pageTitle:'hot',
				minute:'',
				second:'',
				totalPage: 1,
				currentPage: 1,
				loadArticleFlag:false,
				userInfo: '',
				myArticleList: '',
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
		this.nextRefresh();
	},

	methods: {		
		nextRefresh:function() {
			var now = new Date();
			this.minute = 9-now.getMinutes()%10;
			this.second = 59-now.getSeconds();
			if((now.getMinutes()%10==0)&&(now.getSeconds()==0)){
				this.reload();
			}	
			setTimeout(() => {this.nextRefresh()}, 1000);
		},
		reload: function() {
			this.showArticles(1);
		},
		// 锁
		showArticles: function(page) {
			console.log(this.loadArticleFlag);
			
			if ( this.loadArticleFlag ) {
				loadArticleFlag = false;
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
		
	.top {
		width:100%;
		height:40px;
		display:flex;
		align-items:center;
		position:static;
		margin-bottom: 10px;
	}
	.topleft{
		font-size:14px;
		margin-left:30px;
		margin-right:15%;
		height: 20px;
	}
.mainbody{
	width:calc(100% - 26px);
	margin: auto;
}
.topright {
		display:inline-block;
		position:absolute;
		right:10px;
		width: 65px;
		height: 20px;
		line-height:20px;
		background-color: #FFCF3C;
		border-width: 1upx;
		border-radius: 1px;
		display: flex;
		justify-content: center;
		align-items: center;
	
	}

	.refresh{
		font-size:10px;
		color: white;

	}
.icon{
	width: 15px;
	height: 15px;
	padding-left: 2px;
	

}
</style>
