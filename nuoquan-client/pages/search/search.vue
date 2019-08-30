<template>
	<view class="weui-search-bar">
		<view class="weui-search-bar__form">
			<view class="weui-search-bar__box">
				<icon class="weui-icon-search_in-box" type="search" size="14"></icon>
				<input type="text" class="weui-search-bar__input" placeholder="请输入要搜索的关键词" confirm-type="done" @blur="saveAsSearchKeyWords" />
				<view class="weui-icon-clear" @tap="searchClear">
					<icon type="clear" size="14"></icon>
				</view>
				<view class="searchButton" @tap="search">搜索</view>
			</view>
		</view>

		<view class="search" style="height: 20upx;">
			<view class="SearchHistoryItem">
				<text class="SearchHistoryItemTitle">历史搜索:</text>
				<icon type="clear" @tap="SearchDeleteAll" size="18"></icon>
				<view class="searchList">
					<block v-for="(i,index) in historySearchedArticles" :key="index"></block>
				</view>
			</view>
			<view class="wxSearchKey">
				<text class="exSearchTitle">搜索热点:</text>
				<view class="searchList">
					<view v-for="(i,index) in hotList" :key="index">
						{{i}}
					</view>
				</view>
				<view>搜索结果:</view>
				<articlebrief v-for="i in searchedArticleList" :key="i.id" v-bind:articleCard="i"></articlebrief>
			</view>
		</view>
	</view>
</template>

<script>
	import articlebrief from '../../components/articlebrief';
	export default {
		data() {
			return {
				historySearchedArticles: {},
				hotList: {},
				searchKeyWords: '',
				searchedArticleList: {},
			}
		},
		components:{
			articlebrief
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
					method:"POST",
					data: {
						articleContent: this.searchKeyWords
					},
					success: function(result) {
						console.log(result.data);
						that.searchedArticleList = result.data.data.rows;
					}
				})
			},
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
