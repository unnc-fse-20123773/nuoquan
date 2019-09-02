<template>
	<view class="articlecard" id="'+articleCard.id+'" @click="jumpToDetail()">
		<view class="title">{{ articleCard.articleTitle }}</view>
		<view class="briefarticleCard">{{ articleCard.articleContent }}</view>
		<view class="picturearea">
			<!-- 这里是文章配图的位置
 -->
		</view>
		<view class="tags">
			<view class="tag" v-for="(i, index) in articleCard.tags" v-bind:key="index">{{ i}}</view>
		</view>
		<view class="menubar">
			<image :src="articleCard.faceImg" class="touxiang"></image>
			<view class="name">{{ articleCard.nickname }}</view>
			<view class="time">{{ articleCard.createDate | timeDeal}}</view>

			<view class="icons">
				<image class="comment" src="../static/icon/comment.png"></image>
				<view class="icom">{{articleCard.commentNum}}</view>
				<image class="like" src="../static/icon/like.png"></image>
				<view class="icom">{{articleCard.likeNum}}</view>

			</view>
		</view>
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
				// atags: JSON.(this.articleCard.tags);
			};
		},
		created() {
			// console.log(JSON.stringify(this.articleCard.tags));
			// console.log(this.articleCard);
		},
		filters: {
			timeDeal(timediff) {
				timediff = new Date(timediff);
				var parts = [timediff.getFullYear(), timediff.getMonth(), timediff.getDate(), timediff.getHours(), timediff.getMinutes(),timediff.getSeconds()];
				var oldTime = timediff.getTime();
				var now = new Date();
				var newTime = now.getTime();
				var milliseconds = 0;
				var timeSpanStr;
				milliseconds = newTime - oldTime;
				if (milliseconds <= 1000 * 60 * 1) {
					timeSpanStr = '刚刚';
				} else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
					timeSpanStr = Math.round((milliseconds / (1000 * 60))) + '分钟前';
				} else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + '小时前';
				} else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + '天前';
				} else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && parts[0] == now.getFullYear()) {
					timeSpanStr = parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				} else {
					timeSpanStr = parts[0] + '-' + parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				}
				return timeSpanStr;
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
		width: 650upx;
		border-radius: 8px;
		margin: 11px auto 0;
		background-color: #ffffff;
	}
	.title {
		margin: 16px 25px 0 25px;
		font-size: 15px;
		font: MicrosoftYaHei;
		font-weight: bold;
		line-height: 19px;
		margin: 16px 13px 0 15px;
		padding-top: 16px;
	}
	.briefarticleCard {
		margin: 10px 13px 0 15px;
		font-size: 13px;
		line-height: 15px;
		margin-bottom: 15px;
	}
	.tags {
		margin-left: 10px;
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
		margin-left: 15px;
		border-radius: 8px;
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
	}
	.time {
		display: inline-block;
		font-size: 10px;
		margin-left: 25px;
		color: #888888;
	}
	.icons {
		position: absolute;
		right: 0;
		bottom:0;
		width: 206upx;
		text-align: right;
		display: inline-block;
		background-image: url(../static/BG/iconsBG.png);
		overflow: hidden;
		border-bottom-right-radius: 8px;
		border: 10px #621E81;
	}
	.icons image {
		position: relative;
		/* G添加相对位置 */
		width: 11px;
		height: 11px;
		padding-right: 5px;
	}
	.icom {
		display: inline-block;
		color: #353535;
		font-size: 10px;
		padding-right: 11px;
		text-align: center;
		vertical-align: middle;
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