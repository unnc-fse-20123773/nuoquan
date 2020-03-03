<template>
	<view>
		<!--标题-->
		<view class="detail-title">{{ articleCard.articleTitle }}</view>
		<!--作者信息，头像名字时间-->
		<view class="author-info-bar">
			<image :src="pathFilter(articleCard.faceImg)" class="touxiang" @click="goToPersonPublic()"></image>
			<view class="name" @tap="goToPersonPublic()">{{ articleCard.nickname }}</view>
			<view class="time">{{ timeDeal(articleCard.createDate)}}</view>
		</view>
		<!--标签-->
		<view class="detail-tags">
			<view class="tag" :style="{background: tagColorList[index]}" v-for="(i,index) in articleCard.tagList" v-bind:key="index">{{i}}</view>
		</view>
		<!--内容-->
		<view class="detailcontent">{{ articleCard.articleContent }}</view>
		<!--图片区域-->
		<view>
			<!-- 单图显示 -->
			<view v-if="articleCard.imgList.length==1" class="detailpics" style="width:100%;height:124px;display: flex;margin-left: 0;">
				<image v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill" :style="{'width': (singleImgWidth)+'px','height':'124px'}"
				 @tap="previewImg(index)" @longpress="aboutImg(index)" @load="singleImgeFit"></image>
			</view>
			<!-- 其他数量 -->
			<view v-else-if="articleCard.imgList.length==4" class="detailpics" style="max-width: 400upx;margin-left: 0;">
				<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
				 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
			</view>

			<view v-else class="detailpics">
				<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
				 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				<view v-if="articleCard.imgList.length==2||imageList.length==5||imageList.length==8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>

			</view>

		</view>
		<!--4个ICON, 点赞评论分享返回-->
		<view class="menu-bar">
			<view  :class="[ articleCard.isLike ? 'liked':'like']" @tap="swLikeArticle()">{{articleCard.likeNum}}</view>
			<view class="comment" @tap="controlInputInDetailArticle">{{articleCard.commentNum}}</view>
			<view class="share" @tap="toggleShare()"></view>
			<view class="back" @tap="menu_back()"></view>
		</view>
		
		<view v-if="share">
			<mySharePoster :articleCard='articleCard' @unShow="toggleShare"></mySharePoster>
		</view>
	</view>
</template>

<script>
	import mySharePoster from 'components/shareposter/myshareposter.vue'
	
	export default {
		name: "detail_1_article",
		props: {
			articleCard: "",
			userInfo:"",
		},
		components: {
			mySharePoster
		},
		data() {
			return {
				serverUrl: this.$serverUrl,
				singleImgWidth:"", //一图调整宽度
				tagColorList: [],
				share: false, // 隐藏/显示share画布
			};
		},
		mounted() {
			// 随机生成颜色
			if(!this.isNull(this.articleCard.tagList)){
				var tagColors = this.tagColors;
				for (var i=0; i<this.articleCard.tagList.length; i++){
					var random = Math.floor(Math.random()*tagColors.length); // 0~tagColors.length-1
					this.tagColorList.push(tagColors[random]);
				}
			}
		},

		methods:{
			singleImgeFit(e) {
				var height = e.detail.height;
				var width = e.detail.width;
				var rateWith = 124*width/height;
				if (rateWith <= 186) {
					this.singleImgWidth = rateWith;
				} else {
					this.singleImgWidth = 186;
				}
				// console.log(e.detail);
			},
			goToPersonPublic(){
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + this.articleCard.userId,
				})
			},
			swLikeArticle() {
				if (this.articleCard.isLike) {
					this.unLikeArticle();
				} else {
					this.likeArticle();
				}
			// 	this.thisArticle.isLike = !this.thisArticle.isLike;
			// 
			},
			
			likeArticle() {
				console.log("点赞文章");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userLikeArticle',
					data: {
						userId: that.userInfo.id,
						articleId: that.articleCard.id,
						articleCreaterId: that.articleCard.userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						
						console.log(res);

						this.$emit('swLikeArticleSignal', true);
						
					},
				});
			},
			
			unLikeArticle() {
				console.log("取消点赞文章");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userUnLikeArticle',
					data: {
						userId: that.userInfo.id,
						articleId: that.articleCard.id,
						articleCreaterId: that.articleCard.userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						
						console.log(res);
						this.$emit('swLikeArticleSignal', false);
					},
				});
			},
			
			previewImg: function(index) {
				var imgIndex = index;
				// console.log(res)
				// 获取全部图片路径
				var imgList = this.articleCard.imgList;
				var arr = [];
				var path;
				for (var i=0; i<imgList.length; i++){
					// console.log(imgList[i].imagePath);
					path = this.serverUrl + imgList[i].imagePath
					arr = arr.concat(path);
				}
				// console.log(arr);
				
				uni.previewImage({
					current: index,
					urls:arr,
				})
			},
			
			toggleShare(){
				this.share = !this.share;
			},
			
			aboutImg: function(index){
				var that = this;
				console.log(this.articleCard.imgList[index].imagePath);
				uni.showActionSheet({
					itemList: ['保存图片到本地'],
					success: function(res) {
						console.log(res.tapIndex);
						// 保存图片至本地
						if(res.tapIndex == 0) {
							uni.showLoading({
								title:'下载中...'
							})
							uni.downloadFile({
								url: that.serverUrl + that.articleCard.imgList[index].imagePath,
								success: function(res) {
									if(res.statusCode == 200){
										uni.saveImageToPhotosAlbum({
											filePath: res.tempFilePath,
											success: function () {
												console.log('save success');
												uni.hideLoading();
											},
											fail: function() {
												console.log('save failed');
												uni.hideLoading();
												uni.showToast({
													title:'保存失败',
													icon:'none',
													duration:1000,
												})
											}
										});
									}
								}
							})
						}
					}
				});
			},
			controlInputInDetailArticle(){
				this.$emit("controlInputSignal",1);
			},
			menu_back(){
				this.$emit("backToLastPage");
				
			}
		},//method

	};
</script>

<style>
	.detail-article {
		width: 100%;
		background: #FCFCFC;
	}

	.detail-title {
		width: 100%;
		color: #4A4A4A;
		font-size: 17px;
		line-height: 21px;
		margin: auto;
		font-weight: 500;
		word-break: break-all;
		word-wrap: break-word;
		padding-top: 10px;
		padding-bottom: 8px;
		max-height: 42px;
		text-align: justify;
	}

	.author-info-bar {
		height: 24px;
		width: 100%;
		position: relative;
	}

	.touxiang {
		width: 24px;
		height: 24px;
		border-radius: 12px;
	}

	.name {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 24px;
		height: 24px;
		color: #9B9B9B;
		display: inline-block;
		position: absolute;
		top: 0;
		left: 32px;
		width: 96px;
	}

	.time {
		position: absolute;
		right: 0;
		top: 0;
		text-align: right;
		font-size: 12px;
		color: #9B9B9B;
		line-height: 24px;
		width: 102px;
	}

	.detail-tags {
		max-height: 20px;
		line-height: 15px;
		width: 100%;
		margin-top: 8px;
		margin-bottom: 12px;
	}

	.tag {
		display: inline-block;
		border-radius: 20px;
		color: #40A792;
		font-size: 11px;
		height: 11px;
		line-height: 11px;
		padding: 6px 10px;
		background: #621E81;
		vertical-align: middle;
		margin-right: 9px;
		color:#FFFFFF;
	}

	.detailcontent {
		word-break: break-all;
		word-wrap: break-word;
		white-space: pre-line;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(53, 53, 53, 1);
		padding-bottom: 8px;
	}

	.detailpics {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		align-items: center;
		flex-wrap: wrap;
		width: calc(648upx + 12px);
		margin: 0 auto;
		margin-bottom: 9px;
	}

	.detailpic {
		/* 			width: calc((100% - 12px) / 3);*/
		width: 216upx;
		height: 216upx;
		margin: 6px 0;
	}

	.menu-bar {
		height: 68px;
		width: calc(176px + 144upx);
		margin: auto;
		display: flex;
		justify-content: space-between;
		padding-bottom: 3px;
	}

	.comment,
	.like,
	.share,
	.back {
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 11px;
		text-align: center;
		color: #353535;
		padding: 26px 16px 7px;
		border-radius: 22px;
		background: #F6E3BA;
		position: relative;
	}
	.liked{
		background: #FE5F55;
		
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 11px;
		text-align: center;
		color: #353535;
		padding: 26px 16px 7px;
		border-radius: 22px;
		position: relative;
	}
.like::after{
	content: "";
	width: 16px;
	height: 16px;
	background: url(../../static/icon/heart_353535.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left:14px;
	top:8px;
}
.liked::after{
	content: "";
	width: 16px;
	height: 16px;
	background: url(../../static/icon/heart_ffffff.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left:14px;
	top:8px;
}
	.comment::after {
		content: "";
		width: 16px;
		height: 16px;
		background: url(../../static/icon/comment-alt-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:14px;
		top:8px;
	}
	.share::after{
		content: "";
		width: 16px;
		height: 16px;
		background: url(../../static/icon/share-alt-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:14px;
		top:14px;
	}
	.back::after{
		content: "";
		width: 12px;
		height: 12px;
		background: url(../../static/icon/angle-left-353535.png);
		background-repeat: no-repeat;
		background-size: contain;
		position: absolute;
		left:16px;
		top:16px;
	}
</style>
