<template>
	<view style="width: 100%;height: 100%;">
		<!-- 导航栏 -->
		<uni-nav-bar
			class="navigationBar"
			:style="{ height: this.getnavbarHeight() + 'px' }"
			:showLeftIcon="true"
			:isNavHome="isNavHome"
			:left-text="lang.back"
			:title="lang.myPublish"
			:height="this.getnavbarHeight().bottom + 5"
		></uni-nav-bar>

		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }" style="width: 100%;"></view>

		<view class="swiperMenu">
			<view :class="[swiperViewing == 'article' ? 'swiperChoosen' : 'swiperNormal']" @tap="switchSwiper('article')">{{lang.article}} {{ myArticleList.length }}</view>
			<!-- <view :class="[swiperViewing == 'vote' ? 'swiperChoosen' : 'swiperNormal']" @tap="switchSwiper('vote')">{{lang.vote}} {{ myVoteList.length }}</view> -->
		</view>
		<swiper style="width:100%;height:100%;" :current-item-id="swiperViewing">
			<swiper-item style="width: 100%;" item-id="article">
				<scroll-view scroll-y="true" class="scrollPage">
					<view class="mainbody">
						<view style="height:20px;width:100%;"></view>
							<modify-article v-for="article in myArticleList" 
								:key="article.id" 
								:thisArticle="article" 
								:lang="lang" @modifySwipedId="receiveSwiped" 
								:messageIndex="messageIndex">
							</modify-article>
					</view>
				</scroll-view>
			</swiper-item>
			<!-- <swiper-item item-id="vote" @touchmove.stop>
				<myVote :lang="lang" v-for="vote in myVoteList" :key="vote.id" :vote="vote"></myVote>
			</swiper-item> -->
		</swiper>
	</view>
</template>

<script>
import modifyArticle from '../../components/nq-card/modify-article';
import modifyVote from '../../components/nq-card/modify-vote.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import { mapState, mapMutations } from 'vuex';

var loadArticleFlag = false;
export default {
	components: {
		modifyArticle,
		modifyVote,
		uniNavBar
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			pageTitle: '我的发布',
			userInfo: '',
			binNum: '12',

			totalPage: 1,
			currentPage: 1,
			totalNum: '0',
			myArticleList: [],

			myVoteList: [],
			swiperViewing: 'article',
			isNavHome: getApp().globalData.isNavHome ,//判断导航栏左侧是否显示home按钮
			messageIndex:"",
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
		this.showVotes();

		uni.$on('refresh', () => {
			this.showArticles(1);
		});
	},

	methods: {
		// 锁
		showArticles: function(page) {
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
			uni.request({
				url: that.$serverUrl + '/article/queryPublishHistory',
				method: 'POST',
				data: {
					page: page,
					userId: that.userInfo.id,
					targetId: that.userInfo.id, //应该为targetId,但缺少publicProfile的数据传输,暂时用userId测试
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
						that.totalPage = res.data.data.total;
						that.totalNum = res.data.data.records;
						console.log(that.totalNum);
					}, 300);
				},
				fail: res => {
					uni.hideLoading();
					loadArticleFlag = false;
					console.log('index unirequest fail');
				}
			});
		},
		loadMore: function() {
			var that = this;
			var currentPage = that.currentPage;
			var totalPage = that.totalPage;
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
		},
		switchSwiper(a) {
			this.swiperViewing = a;
		},
		showVotes: function(page) {
			var that = this;
			uni.request({
				url: that.$serverUrl + '/vote/queryAllVotes',
				method: 'POST',
				data: {
					page: 1,
					userId: that.userInfo.id,
					pagesize: '10'
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					that.myVoteList = res.data.data.rows;
				},
				fail: res => {
					uni.hideLoading();
					loadArticleFlag = false;

					console.log('index unirequest fail');
					console.log(res);
				}
			});
		},
		receiveSwiped(newId){
			this.messageIndex = newId;
			console.log("refresh swiped ID = " + newId);
		}
	}
};
</script>

<style>
page {
	background: #ffffff;
	width: 100%;
	height: 100%;
	/* background: #f8f8f8; */
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

.swiperMenu {
	padding: 17px 0 17px 13px;
}
.swiperNormal {
	display: inline-block;
	height: 14px;
	width: 45px;
	margin: 0 16px;
	font-size: 11px;
	font-weight: 500;
	color: rgba(136, 136, 136, 1);
	text-align: center;
}
.swiperChoosen {
	display: inline-block;
	height: 14px;
	width: 45px;
	text-align: center;
	font-size: 11px;
	font-weight: 500;
	color: #ffffff;
	background: #ffc95a;
	padding: 5px 12px;
	margin: 0 4px;
	border-radius: 25px;
}

.scrollPage{
	width:100%;
	height: calc(100% - 28px);
	background: #FFFFFF;
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
