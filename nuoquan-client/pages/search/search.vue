<template>
	<view class="weui-search-bar">
		<view class="input-bar">
			<image class="back" src="../../static/icon/angle-left.png" @tap="exitSearch"></image>
			<view class="input-background"></view>
			<input type="text" v-model="searchKeyWords" focus placeholder="搜索" placeholder-style="color: #b2b2b2;" confirm-type="search" @confirm="search(1)" />
		</view>

		<view class="wxSearchKey" v-show="searching">
			<text class="exSearchTitle">搜索热点:</text>
			<view class="searchList">
				<view class="item" v-for="(i,index) in hotList" :key="index" @click="putHotIntoInput(index)">
					{{i}}
				</view>
			</view>
		</view>

		<!-- 历史搜索标题行及区域 -->
		<view class="SearchHistoryItem" v-show="searching">
			<view class="SearchHistoryItemTitle">历史搜索:</view>
			<icon type="clear" @tap="searchDeleteAll" size="11"></icon>
			<!-- <view v-for="key in searchHisKeyList" :key="key">{{key}}</view> -->
			<view class="searchList">
				<view class="item" v-for="(item,index) in searchHisKeyList" :key="index" @click="putHisIntoInput(index)">{{item}}</view>
			</view>
		</view>

		<!-- 搜索结果标题行 -->
		<view class="searchResultWrods column_center">
			<view style="color: #888888;font-size: 13px;font-weight: 300;">搜索结果:</view>
			<icon type="clear" style="position: absolute;right:28px;" v-show="!searching" @click="searchCancle(searching)" size="11"></icon>
		</view>

		<!-- 搜索结果显示区域 -->
		<scroll-view class="searchResult" v-show="!searching" scroll-y="true" @scrolltolower="loadMore()">
			<searchResultArticle v-for="i in searchedArticleList" :key="i.id" v-bind:articleCard="i"></searchResultArticle>
		</scroll-view>
	</view>
</template>

<script>
	import searchResultArticle from '../../components/searchResultArticle.vue';
	
	var isSearching = false; //搜索锁
	export default {
		data() {
			return {
				hotList: [],
				searchKeyWords: '',
				searchedArticleList: [],
				searching: true,
				searchHisKeyList: uni.getStorageSync('search_history'),

				userInfo: this.getGlobalUserInfo(),

				totalPage: 1,
				currentPage: 1,
			}
		},
		components: {
			searchResultArticle,
		},
		created() {
			// 查询热搜词
			this.getHotWords();
		},
		destroyed() { // 组件退出隐藏加载
			uni.hideLoading();
		},
		methods: {
			getHotWords: function() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/hot',
					method: "POST",
					success: (res) => {
						console.log(res);
						that.hotList = res.data.data;
						console.log(that.hotList);
					}

				})
			},
			search: function(page) {
				if(isSearching){
					return;
				}
				isSearching = true;
				
				var that = this;
				var isSaveRecord = 1;
				// console.log(page);
				if (this.searchKeyWords == '' || this.searchKeyWords == null) {
					uni.showToast({
						title: '搜索内容不能为空',
						duration: 1000,
						icon: 'none',
					})
					return;
				}

				uni.getStorage({
					key: 'search_history',
					success(res) {
						let list = res.data;
						// console.log(list);
						if (list.length > 10) {
							for (let item of list) {
								if (item == that.searchKeyWords) {
									return false;
								}
							}
							list.pop();
							list.unshift(that.searchKeyWords);
						} else {
							for (let item of list) {
								if (item == that.searchKeyWords) {
									return false;
								}
							}
							list.unshift(that.searchKeyWords);
						}
						that.searchHisKeyList = list;
						uni.setStorage({
							key: 'search_history',
							data: that.searchHisKeyList
						})
					},
					fail() {
						that.searchHisKeyList = [];
						that.searchHisKeyList.push(that.searchKeyWords);
						uni.setStorage({
							key: 'search_history',
							data: that.searchHisKeyList
						});
					}
				});

				uni.showLoading({
					title: "搜索中..."
				})
				setTimeout(()=>{
					if(isSearching){
						isSearching = false // 解锁
						uni.hideLoading();
						uni.showToast({
							title: "网络未知错误",
							icon: "none",
							duration: 1000
						})
					}
				}, 5000); // 延时5s timeout	
				
				uni.request({
					url: that.$serverUrl + '/article/searchArticleYANG',
					method: "POST",
					data: {
						searchText: that.searchKeyWords,
						userId: that.userInfo.id,
						isSaveRecord: isSaveRecord,
						page: page,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: function(result) {
						isSearching = false // 解锁
						uni.hideLoading();
						
						that.searching = false;
						if(result.data.status == 200){
							// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
							if (that.currentPage == 1) {
								that.searchedArticleList = [];
							}
							
							var newArticleList = result.data.data.rows;
							var oldArticleList = that.searchedArticleList;
							that.searchedArticleList = oldArticleList.concat(newArticleList);
							// console.log(result.data.data.page);
							that.currentPage = page;
							that.totalPage = result.data.data.total;
						}
						
					},
					fail: (res) => {
						console.log("index unirequest fail");
						console.log(res);
					}
				})
			},
			
			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				console.log('currentpage is ' + currentPage);
				var totalPage = that.totalPage;
				console.log('totalpage is ' + totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// that.showArticles(1);
					uni.showToast({
						title: "没有更多文章了",
						icon: "none",
						duration: 1000
					})
				} else {
					var page = currentPage + 1;
					that.search(page);
				}
			},
			
			searchDeleteAll: function() {
				var that = this;
				uni.showModal({
					title: "提示",
					content: '确定删除所有历史记录吗?',
					success: function(res) {
						if (res.confirm) {
							that.searchHisKeyList = [];
							uni.setStorage({
								key: 'search_history',
								data: that.searchHisKeyList,
							})
						} else if (res.cancle) {

						}
					}
				})
			},
			searchCancle: function(searching) {
				this.searching = !searching;
				// console.log(this.searching);
			},
			exitSearch() {
				this.hotList = "",
					this.searchKeyWords = "",
					this.searchedArticleList = "",
					this.$emit("exitSearchSignal", 0)
			},
			putHotIntoInput: function(index){
				// console.log(index);
				var keywords = this.hotList[index];
				// console.log(keywords);
				this.searchKeyWords = keywords;
			},
			putHisIntoInput: function(index){
				// console.log(index);
				var keywords = this.searchHisKeyList[index];
				// console.log(keywords);
				this.searchKeyWords = keywords;
			}
		}
	}
</script>

<style scoped>
	.weui-search-bar {
		display: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: #FFFFFE;
		z-index: 10;
	}

	.input-bar {
		margin-top: 4px;
		margin-left: 12px;
		height: 30px;
		width: 100%;
		position: relative;
	}

	.back {
		display: inline-block;
		width: 30px;
		height: 30px;
		background: #FDD041;
		border-radius: 8px;
		position: absolute;
		left: 0;
	}

	.input-background{
		position: absolute;
		left: 30px;
		display: inline-block;
		width: 70%;
		height: 28px;
		border-radius: 8px;
		margin-left: 13px;
		background: white;
		line-height: 28px;
		box-shadow: 0px 2px 15px 0px rgba(0, 0, 0, 0.16);	
		z-index: 10;
	}

	.input-bar input {
		position: absolute;
		left: 40px;
		font-size: 15px;
		display: inline-block;
		width: 66%;
		height: 28px;
		margin-left: 13px;
		letter-spacing: 1px;
		color: #b2b2b2;
		font-family: MicrosoftYaHei;
		line-height: 28px;
		z-index: 20;
	}

	.wxSearchKey,
	.SearchHistoryItem {
		margin-top: 26px;
		width: calc(750upx-56px);
		padding: 0 28px;
		position: relative;
	}

	.exSearchTitle,
	.SearchHistoryItemTitle {
		color: #888888;
		font-size: 13px;
		font-weight: 300;
	}
	
	.searchResult{
		width: 100%;
		height: 100%;
	}
	
	
	.searchResultWrods {
		margin-top: 26px;
		width: calc(750upx-56px);
		padding: 0 28px;
		position: relative;
	}

	.item {
		display: inline-block;
		padding: 0 11px;
		background: #FFE9A2;
		border-radius: 8px;
		height: 24px;
		line-height: 24px;
		color: #353535;
		font-size: 13px;
		font-weight: 300;
		margin-right: 14px;
		/* 限制字数 */
		max-width: 48%;
		white-space:nowrap;
		overflow:hidden;
		text-overflow:ellipsis;
	}

	.SearchHistoryItem icon {
		position: absolute;
		right: 28px;
		top: 0;
	}
</style>
