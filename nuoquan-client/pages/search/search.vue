<template>
	<view class="weui-search-bar">
		<view class="input-bar">
			<image class="back" src="../../static/icon/angle-left.png" @tap="exitSearch"></image>
			<input type="text" v-model="searchKeyWords" focus placeholder="  搜索" confirm-type="search" @confirm="search" />
		</view>

		<view class="wxSearchKey" v-show="searching">
			<text class="exSearchTitle">搜索热点:</text>
			<view class="searchList">
				<view class="item" v-for="(i,index) in hotList" :key="index">
					{{i}}
				</view>
			</view>
		</view>
		
		<!-- 历史搜索标题行及区域 -->
		<view class="SearchHistoryItem" v-show="searching">
			<view class="SearchHistoryItemTitle">历史搜索:</view>
			<icon type="clear" @tap="SearchDeleteAll" size="11"></icon>
			<!-- <view v-for="key in searchHisKeyList" :key="key">{{key}}</view> -->
			<view class="searchList">
				<view class="item" v-for="(item,index) in searchHisKeyList" :key="index">{{item}}</view>
			</view>
		</view>
		
		<!-- 搜索结果标题行 -->
        <view class="searchResultWrods column_center">
			<view style="color: #888888;font-size: 13px;font-weight: 300;">搜索结果:</view>
			<icon type="clear" style="position: absolute;right:28px;" v-show="!searching" @click="searchCancle(searching)" size="11"></icon>
		</view>
		
		<!-- 搜索结果显示区域 -->
		<view class="searchResult" v-show="!searching">			
			<searchResultArticle v-for="i in searchedArticleList" :key="i.id" v-bind:articleCard="i"></searchResultArticle>
		</view>
	</view>
</template>

<script>
	import searchResultArticle from '../../components/searchResultArticle.vue';
	export default {
		data() {
			return {
				hotList: {},
				searchKeyWords: '',
				searchedArticleList: {},
				searching:true,
				searchHisKeyList: uni.getStorageSync('search_history'),
			}
		},
		components: {
			searchResultArticle,
		},
		onLoad: function() {
			// 查询热搜词
			var that = this;
			uni.request({
				url: that.$serverUrl + '/article/hot',
				method: "POST",
				success: (res) => {
					console.log(res);
					that.hotList = res.data.data;
					console.log(this.hotList);
				}

			})
		},
		
		methods: {
			search: function(res) {
				var that = this;
				var isSaveRecord = 1;
				
				// console.log(that.searchKeyWords);
				if (this.searchKeyWords == '' || this.searchKeyWords == null){
					uni.showToast({
						title: '搜索内容不能为空',
						duration: 1000,
						icon:'none',
					})
					return;
				}
				
				
				uni.getStorage({
					key:'search_history',
					success(res){
						let list = res.data;
						console.log(list);
						if(list.length > 10){
							for(let item of list){
								if(item == that.searchKeyWords){
									return false;
								}
							}
							list.pop();
							list.unshift(that.searchKeyWords);
						} else {
							for(let item of list){
								if(item == that.searchKeyWords){
									return false;
								}
							}
							list.unshift(that.searchKeyWords);
						}
						that.searchHisKeyList = list;
						uni.setStorage({
							key:'search_history',
							data: that.searchHisKeyList
						})
					},
					fail() {
						that.searchHisKeyList = [];
						that.searchHisKeyList.push(that.searchKeyWords);
						uni.setStorage({
							key:'search_history',
							data: that.searchHisKeyList
						});
					}
				});
				
				uni.request({
					url: this.$serverUrl + '/article/searchArticleYANG?isSaveRecord=' + isSaveRecord,
					method: "POST",
					data: {
						articleContent: that.searchKeyWords
					},
					success: function(result) {
						console.log(result.data);
						that.searchedArticleList = result.data.data.rows;
						that.searching=false;
					}
				})
			},
			searchCancle: function(searching){
					this.searching = !searching;
					console.log(this.searching);
			},
			exitSearch(){
				this.hotList="",
				this.searchKeyWords="",
				this.searchedArticleList="",
				this.$emit("exitSearchSignal",0)
			},
		}
	}
</script>

<style scoped>
	.weui-search-bar{
		display: fixed;
		top:0;
		left:0;
		width: 100%;
		height:100%;
		background: #FFFFFE;
		z-index: 10;
	}
	.input-bar {
		margin-top: 10px;
		margin-left: 23px;
		height: 32px;
	}

	.back {
		display: inline-block;
		width: 32px;
		height: 32px;
		background: #FDD041;
		border-radius: 8px;

	}


	.input-bar input {
		font-size: 15px;
		display: inline-block;
		width: 190px;
		height: 28px;
		border-radius: 8px;
		margin-left: 13px;
		padding-left: 2px;
		padding-bottom: 2px;
		background: white;
		letter-spacing: 1px;
		color: #b2b2b2;
		font-family: MicrosoftYaHei;
		line-height: 10px;
		box-shadow: 0px 2px 15px 0px rgba(0, 0, 0, 0.16);
	}

	.wxSearchKey, .SearchHistoryItem{
		margin-top: 26px;
		width: calc(750upx-56px);
		padding: 0 28px;
		position: relative;
	}

	.exSearchTitle,.SearchHistoryItemTitle
	 {
		color: #888888;
		font-size: 13px;
		font-weight: 300;
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
	}
		
	.SearchHistoryItem icon{
		position: absolute;
		right:28px;
		top:0;
	}
</style>
