<template>
	<view class="articlecard" id="'+articleCard.id+'" @click="jumpToDetail()">
		<view class="title">{{ articleCard.articleTitle }}</view>
		<view class="briefarticleCard">{{ articleCard.articleContent }}</view>
		<view :class="[articleCard.imgList.length == 1 ? 'picturearea-one' : 'picturearea-mul']">
			<!-- *******这里是文章配图的位置*******-->

			<!-- 下面两个 view 分别为蒙版背景层和数字层，都是由 margin-left = 67.5% 精准推至第三张图位置上的 -->
			<view v-if="articleCard.imgList.length > 3" style="margin-left: 67.5%;position: absolute;width: 30%;height: 200upx;" class="super_center">
				<view style="color: white;font-weight: 600;font-size: 24px;z-index: 20;">
				+{{articleCard.imgList.length-3}}
				</view>
			</view>
			<view v-if="articleCard.imgList.length > 3" style="position: absolute;width: 30%;height: 200upx;background-color: #000000;opacity: 0.5;margin-left: 67.5%;z-index: 10;"></view>
			
			<!-- 宽高和 image 保持一致 -->
			<!-- 单图显示 -->
			<view style="width: 100%;max-height: 400upx;" v-if="articleCard.imgList.length == 1">
				<!-- 高 ＞ 宽 -->
				<view v-if="singleImgState == 0" style="width: 360upx;">
					<image mode="aspectFit" style="height: 360upx;" :src="serverUrl + articleCard.imgList[0].imagePath" @load="singleImgeFit"></image>
				</view>
				<!-- 宽 > 高 -->
				<view v-else style="max-height: 400upx;display: flex;">
					<image mode="aspectFit" style="width: 90%;" :src="serverUrl + articleCard.imgList[0].imagePath" @load="singleImgeFit"></image>
				</view>
			</view>
			<!-- 多图显示 -->
			
			<view style="width:30%;height: 200upx;margin-left: 2.5%;display: flex;background-color: #D1D1D1;" v-else v-for="(item,index) in imgList" :key="index">
				<image mode="aspectFit" :src="serverUrl + item.imagePath"></image>				
			</view>
			
		</view>
		<view class="tags">
			<view class="tag" v-for="(i, index) in articleCard.tags" v-bind:key="index">{{i}}</view>
		</view>
		<view class="menubar">
			<image :src="articleCard.faceImg" class="touxiang" @tap="goToPersonPublic(articleCard.userId)"></image>
			<view class="name">{{ articleCard.nickname }}</view>
			<view class="time">{{ articleCard.createDate | timeDeal}}</view>

			<view class="icons">
				<image class="comment" src="../static/icon/comment.png"></image>
				<view class="icon">{{articleCard.commentNum}}</view>
				<image class="like" src="../static/icon/like.png"></image>
				<view class="icon">{{articleCard.likeNum}}</view>
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

				serverUrl: this.$serverUrl,
				singleImgState: '0',
				
				imgList: [],
				// atags: JSON.(this.articleCard.tags);
			};
		},
		created() {
			if(this.articleCard.imgList.length > 3){
				// 只取前三
				for (var i=0; i < 3; i++){
					this.imgList.push(this.articleCard.imgList[i]);
				}
			}else{
				this.imgList = this.articleCard.imgList;
			}
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
			singleImgeFit(e){
				var height = e.detail.height;
				var width = e.detail.width;
				if(height >= width){
					this.singleImgState = 0;
				}else{
					this.singleImgState = 1;
				}
				// console.log(e.detail);
			},
			
			jumpToDetail() {
				var navData = JSON.stringify(this.articleCard); // 这里转换成 字符串
				uni.navigateTo({
					url: '/pages/detail/detail?data=' + navData
				});
			},
			
			goToPersonPublic(userId){
				uni.navigateTo({
					url:'/pages/personpublic/personpublic?userId=' + userId,
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
	}
	.icons image {
		position: relative;
		/* G添加相对位置 */
		width: 11px;
		height: 11px;
		padding-right: 5px;
	}
	.icon {
		display: inline-block;
		color: #353535;
		font-size: 10px;
		padding-right: 11px;
		text-align: center;
		vertical-align: middle;
	}
	.picturearea-one {
		margin: auto;
		display: flex;
		width: 94%;
		margin-left: 3%;
	}
	
	.picturearea-mul {
		position: relative;
		margin: auto;
		display: flex;
		/* 在此设置图片区域宽度 */
		width: 94%;
		margin-left: 3%;
	}
	
	image {
		width: 100%;
		height: 200upx;
		margin: auto;
	}
</style>
