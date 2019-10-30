<template>
	<view style="width:690upx;height:100%;margin:auto;">
		<view class="top-bar">
			<view class="totalNum">{{ totalNum }}篇文章</view>
			<view class="bin">
				<image src="../../static/icon/bin.png"></image>
				<view>回收站 {{ binNum }}</view>
			</view>
		</view>
		<view class="mainbody"><oneArticle v-for="i in myArticleList" :key="i.id" v-bind:articleCard="i"></oneArticle></view>
	</view>
</template>

<script>
import oneArticle from './oneArticle.vue';

var loadArticleFlag = false; // 为加载文章加锁
export default {
	components: {
		oneArticle
	},
	data() {
		return {
			userInfo: '',
			totalPage: 1,
			currentPage: 1,

			totalNum: '5',
			binNum: '12',
			myArticleList: [
				{
					articleContent: '哈哈哈↵↵哈哈哈↵哈哈哈哈',
					articleTitle: '分段测试',
					commentNum: 0,
					createDate: 1571836849000,
					dislikeNum: 0,
					faceImg: 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKIk4xLLJS0Ksg5gr65QKG1gGsf0V4eFLVyXOh9sU0XsOqib4xVGCDZjlU8ugB1Ooial8ibnPtxtatHQ/132',
					faceImgThumb: null,
					id: '191023G6HF6C8P6W',
					imgList: ['1'],
					isAnonymous: 0,
					isLike: false,
					likeNum: 0,
					nickname: 'Jerrio❤️',
					popularity: 0,
					status: 1,
					tagList: null,
					tags: '',
					userId: 'oDwsO5HhYCIAP0AVpS4jRTLCvFlM',
					viewNum: 0,
					status: 1
				},
				{
					articleContent:
						'我是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣辉↵',
					articleTitle: 'hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2',
					commentNum: 1,
					createDate: 1571745782000,
					dislikeNum: 0,
					faceImg: 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIvu8KVDia1G08KbQMeycm8Uf6dicb39xq9bwChM1qaRGGdWgL8qWSeROShbt3yVvOPMpib9ObfZurxA/132',
					faceImgThumb: null,
					id: '191022F8N2S98ARP',
					imgList: [],
					isAnonymous: 0,
					isLike: false,
					likeNum: 1,
					nickname: '我是Guetta',
					popularity: 0,
					tagList: ['tag1', 'tag2'],
					tags: '#找对象',
					userId: 'oDwsO5Mjxnj2-O1yXl064bOQbELQ',
					viewNum: 0,
					status: 0
				},
				{
					articleContent:
						'我是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣是渣渣辉↵',
					articleTitle: 'hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2',
					commentNum: 1,
					createDate: 1571745782000,
					dislikeNum: 0,
					faceImg: 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIvu8KVDia1G08KbQMeycm8Uf6dicb39xq9bwChM1qaRGGdWgL8qWSeROShbt3yVvOPMpib9ObfZurxA/132',
					faceImgThumb: null,
					id: '191022F8N2S985RP',
					imgList: [],
					isAnonymous: 0,
					isLike: false,
					likeNum: 1,
					nickname: '我是Guetta',
					popularity: 0,
					tagList: ['tag1', 'tag2'],
					tags: '#找对象',
					userId: 'oDwsO5Mjxnj2-O1yXl064bOQbELQ',
					viewNum: 0,
					status: -1
				}
			]
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
		// 锁
		showArticles: function(page) {
			if (loadArticleFlag) {
				return;
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
						that.totalPage = res.dat.data.total;
						that.totalNum = res.data.ata.records;
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
	height: 30px;
	padding: 24px 0;
	display: flex;
	justify-content: space-between;
	font: Source Han Sans CN;
}

.totalNum {
	color: #888888;
	font-size: 18px;
	text-spacing: 80;
}

.bin {
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
}
.mainbody {
	width: 690upx;
	margin: auto;
}
</style>
