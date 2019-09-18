<template>
	<view class="index">
		<mainpagetop :userInfo='userInfo' :topArticles='topArticles' :topHeight="topHeight" style="position: fixed;z-index: 30;height:100%;"></mainpagetop>

		<view class="indexSelf" style="height:100%;">
			<scroll-view class="indexArticleArea" scroll-y="true" @scroll="linkageWithTop" @scrolltolower="loadMore" @scrolltoupper="refreshArticle">
				<view style="height:160px;width:100%;"></view>
				<articlebrief v-for="i in showlist" :key="i.id" v-bind:articleCard="i"></articlebrief>
				<!-- 用于添加底部空白 by Guetta 9.10 -->
				<view class="marginHelper"></view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import articlebrief from '../../components/articlebrief';
	import mainpagetop from '../../components/mainpagetop.vue';
	import mainpageleft from '@/components/mainpageleft.vue'
	
	import {mapState} from 'vuex';
	
	export default {
		data() {
			return {
				title: 'Hello',
				hottitlelist: ['热门标题111', '热门标题222', '热门标题333'],
				showlist: [],
				topArticles: '',
				topHeight: "160",

				userInfo: { // 默认user设置
					id: 'test-id123',
					nickname: 'test-name',
					faceImg: '',
					faceImgThumb: '',
					email: 'zy22089@nottingham.edu.cn',
					emailPrefix: 'zy22089',
					emailSuffix: '@nottingham.edu.cn'
				},
				totalPage: 1,
				currentPage: 1,

			};
		},
		components: {
			articlebrief,
			mainpagetop,
			mainpageleft,
		},
		
		onLoad() {			
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.navigateTo({
					url: "../wechatLogin/wechatLogin"
				})
				return;
			}
			
			this.mySocket.init(); // 初始化 Socket, 离线调试请注释掉
			// [测试代码块]
		},
		
		onShow() {
			var that = this;
			var userInfo = this.getGlobalUserInfo(); // 查看用户是否登录
			if (!this.isNull(userInfo)) {
				// 设置 userInfo 传给 mainpagetop 组件
				// 更新用户信息缓存... 查询用户信息，并分割邮箱更新到缓存
				this.queryUserInfo(userInfo.id)
			}
			
			var page = that.currentPage;
			this.showArticles(page); // 显示文章流
			
			this.getTop3Articles(); // 获取热度榜
		},
		
		methods: {
			
			showArticles: function(page) {
				var that = this;
				uni.showLoading({
					title:"加载中..."
				})
				// var page = that.currentPage;
				uni.request({
					url: that.$serverUrl + '/article/queryAllArticles',
					method: "POST",
					data:{
						page: page,
						// pageSize: '', 
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						setTimeout(()=>{ // 延时加载
							uni.hideLoading();
							console.log(res);
							// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
							if (page == 1) {
								that.showlist = [];
							}
							var newArticleList = res.data.data.rows;
							var oldArticleList = that.showlist;
							that.showlist = oldArticleList.concat(newArticleList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
						}, 300);
					},
					fail: (res) => {
						console.log("index unirequest fail");
						console.log(res);
					}
				});
			},
			
			loadMore: function(){
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage){
					// that.showArticles(1);
					uni.showToast({
						title:"没有更多文章了",
						icon:"none",
						duration:1000
					})
				} else {
					var page = currentPage + 1;
					that.showArticles(page);
				}
			},
			refreshArticle: function(){
				uni.showNavigationBarLoading();
				this.showArticles(1);
				uni.hideNavigationBarLoading();
			},
			getTop3Articles(){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getHotTop3',
					method: "POST",
					data:{
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						that.topArticles = res.data.data;
					}
				})
			},
			
			/**
			 * 查询用户信息，并分割邮箱更新到缓存
			 */
			queryUserInfo(userId){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/queryUser',
					method: "POST",
					data: {
						userId: userId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if(res.data.status == 200){
							var user = res.data.data;
							var finalUser = this.myUser(user);// 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							// console.log(this.userInfo);
						}
					},
				});
			},
			
			linkageWithTop(e) {
				var y = e.detail.scrollTop;
				console.log(y);
				if (this.topHeight >= 36) {
					if (160 - y >= 36) {
						this.topHeight = 160 - y;
					} else {
						this.topHeight = 36;
					}

				}
			},
		}
	};
</script>
<style>
	page {
		height: 100%;
	}
</style>

<style scoped>
	.index {
		/* 页面高度由内容扩充，最低值为100%（page 定义的）- by Guetta */
		height: 100%;
		background-color: #f3f3f3;
	}

	.indexArticleArea {
		height: 100%;
	}

	image {
		width: 60px;
		height: 60px;
		display: inline-block;
	}

	.touxiang {
		border-radius: 30px;
	}

	.search {
		display: inline-block;
		width: 70%;
		box-shadow: inset 0 0 20px #ccc;
		border-radius: 20upx;
		margin-left: 50upx;
	}

	.hot {
		background-color: #e4e4e4;
		width: 100%;
	}

	.hotitem {
		margin-bottom: 20px;
		background: white;
		height: 27px;
		border-color: rgb(255, 255, 255);
		box-shadow: rgb(170, 170, 170) 0px 0px 5px 0px;
		font-size: 14px;
		padding: 0px;
		border-width: 1px;
		border-style: solid;
		text-align: left;
		line-height: 20px;
		font-weight: normal;
		font-style: normal;
	}

	.arrow {
		width: 100upx;
		height: 60upx;
	}

	.marginHelper {
		height: 15upx;
		margin-top: 15upx;
		width: 100%;
		background-color: #f3f3f3;
	}
</style>
