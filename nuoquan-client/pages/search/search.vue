<template>
	<view class="weui-search-bar" style="background: FFFFFE;" >
		<view class="input-bar">
			<image class="back" src="../../static/icon/angle-left.png"></image>
			<input type="text" placeholder="  搜索" confirm-type="done" @blur="saveAsSearchKeyWords" @confirm="search()" />
		</view>

		<view class="wxSearchKey" v-show="searching">
			<text class="exSearchTitle">搜索热点:</text>
			<view class="searchList">
				<view class="item" v-for="(i,index) in hotList" :key="index">
					{{i}}
				</view>
			</view>

		</view>
		<view class="SearchHistoryItem" v-show="searching">
			<view class="SearchHistoryItemTitle">历史搜索:</view>
			<icon type="clear" @tap="SearchDeleteAll" size="11"></icon>
			<view class="searchList">
				<view class="item" v-for="(i,index) in historySearchedArticles" :key="index"></view>
			</view>
		</view>
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
				historySearchedArticles: {},
				hotList: {},
				searchKeyWords: '',
				searchedArticleList: {},
				searching:true,
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
			saveAsSearchKeyWords: function(event) {
				this.searchKeyWords = event.target.value;
				console.log(this.searchKeyWords);
			},
			search: function(res) {
				var that = this;
				var isSaveRecord = 1;
				uni.request({
					url: this.$serverUrl + '/article/searchArticleYANG?isSaveRecord=' + isSaveRecord,
					method: "POST",
					data: {
						articleContent: this.searchKeyWords
					},
					success: function(result) {
						console.log(result.data);
						that.searchedArticleList = result.data.data.rows;
						that.searching=false;
					}
				})
			},
		}
	}
</script>

<style scoped>
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
