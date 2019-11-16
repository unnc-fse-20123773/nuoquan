<template>
	<view class="articlecard" id="'+articleCard.id+'" @click="jumpToDetail()">
		<view class="title">{{ articleCard.articleTitle }}</view>
		<view class="briefarticleCard">{{ articleCard.articleContent }}</view>
		<view class="picturearea">
			<!-- 这里是文章配图的位置 -->
			
		</view>
		<view class="tags">
			<view class="tag" :style="{background: tagColorList[index]}" v-for="(i, index) in articleCard.tagList" v-bind:key="index">{{ i}}</view>
		</view>
		<view class="menubar">
			<image :src="articleCard.faceImg" class="touxiang"></image>
			<view class="name">{{ articleCard.nickname }}</view>
			<view class="time">{{ timeDeal(articleCard.createDate) }}</view>
		</view>
		<view style="margin:0 25px;border-top:1px solid #DCDCDC;"></view>
	</view>
</template>

<script>
	export default {
		name: 'aticlebrief',
		props: {
			articleCard: {}
		},
		data() {
			return {
				tagColorList: [],
			};
		},
		created() {
			// 随机生成颜色
			if (!this.isNull(this.articleCard.tagList)){
				var tagColors = this.tagColors;
				for (var i = 0; i < this.articleCard.tagList.length; i++) {
					var random = Math.floor(Math.random() * tagColors.length);
					this.tagColorList.push(tagColors[random]);
				}
			}
		},
		
		methods: {
			jumpToDetail() {
				var navData = JSON.stringify(this.articleCard); // 这里转换成 字符串
				uni.navigateTo({
					url: '/pages/detail/detail?data=' + navData
				});
			}
		},
	};
</script>

<style>
	image {
		border: none;
		outline: none;
	}
</style>
<style scoped>
	.articlecard {
		width: 750upx;
		border-radius: 8px;
		margin: 0 auto ;
		background-color: #ffffff;
	}
	.title {
		margin: 0px 25px 0 25px;
		font-size: 15px;
		font: MicrosoftYaHei;
		font-weight: bold;
		line-height: 19px;
		padding-top: 10px;
	}
	.briefarticleCard {
		max-height: 500upx;
		margin: 10px 25px 15px;
		font-size: 13px;
		line-height: 15px;
		margin-bottom: 15px;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 15;
		overflow: hidden;
	}
	.tags {
		margin-left: 21px;
	}
	.tag {
		display: inline-block;
		border-radius: 4px;
		padding-left: 5px;
		padding-right: 5px;
		margin-left: 5px;
		height: 15px;
		color: #ffffff;
		font-size: 10px;
		background: #621E81;
	}
	.menubar {
		position: relative;
		vertical-align: middle;
		margin:0 25px;
	}
	.touxiang {
		border-radius: 30px;
		width: 20px;
		height: 20px;
		margin-right: 5px;
		vertical-align: middle;
	}
	.name {
		display: inline-block;
		font-size: 10px;
		margin-left: 7px;
		color: #888888;
		padding-bottom: 5px;
		max-width: 85px;
		text-overflow: ellipsis;
	}
	.time {
		position: absolute;
		right: 0;
		bottom: 5px;
		display: inline-block;
		font-size: 10px;
		margin-left: 25px;
		color: #888888;
		max-width: 80px;
		text-overflow: ellipsis;
	}
	
	.picturearea {
		margin: auto;
		display: flex;
		justify-content: center;
	}
	image {
		width: 30%;
		height: 200upx;
		margin: auto;
	}
</style>