<template>
	<view class="oneArticle" :class="{ oneArticleWithPic : thisArticle.imgList.length!=0 }" @click="jumpToDetail()">
		<!-- 跳转 Deyan -->
		<view class="title">
			{{ thisArticle.articleTitle }}
		</view>
		<view class="cardBody">
			<view class="left-body" :class="{ leftBodyWithPic : thisArticle.imgList.length!=0 }">
				<view class="content">
					{{ thisArticle.articleContent }}
				</view>
				<view class="bottomBar">
					<view class="time">
						{{ thisArticle.createDate | timeDeal}}
					</view>
					<view class="comment">
						<image src="../../static/icon/comment.png"></image>
						<view> {{thisArticle.likeNum}}</view>
					</view>
					<view class="like">
						<image v-if="!thisArticle.isLike"  src="../../static/icon/like.png" ></image>
						<image v-if="thisArticle.isLike"  src="../../static/icon/liked-red.png" ></image>
						<view> {{thisArticle.likeNum}}</view>
					</view>
				</view>
			</view>
			<view class="picArea" v-if="thisArticle.imgList.length">
				<image src="../../static/icon/about.png"></image>
				<view class="picNum"></view>
			</view>
		</view>
		
		<!-- 审核状态 -->
		<view class="status" style="width:45px;background: #09BB07;" v-if="articleCard.status==1">√ 审核通过</view>
		<view class="status" style="width:36.5px;background: #3370FF;" v-if="articleCard.status==0">···审核中</view>
		<view class="status" style="width:51.5px;background: #888888;" v-if="articleCard.status==-1">❌  审核失败</view>
		
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
				thisArticle: this.articleCard, // 转为局部变量
				serverUrl: this.$serverUrl,
				userInfo: this.getGlobalUserInfo(),
				// singleImgState: '0',
				singleImgHeight: 0,
				singleImgWidth: 0,
				heightWidthRate: 0,
				imgList: [],
				tagColorList: [], // 储存每个tag的颜色
				timeLeft: "",
			};
		},
		created() {
			// console.log(this);
			if (this.thisArticle.imgList.length > 3) {
				// 只取前三
				for (var i = 0; i < 3; i++) {
					this.imgList.push(this.thisArticle.imgList[i]);
				}
			} else {
				this.imgList = this.thisArticle.imgList;
			}
	
			// 随机生成颜色
			if (!this.isNull(this.thisArticle.tagList)) {
				var tagColors = this.tagColors;
				for (var i = 0; i < this.thisArticle.tagList.length; i++) {
					var random = Math.floor(Math.random() * tagColors.length); // 0~tagColors.length-1
					this.tagColorList.push(tagColors[random]);
				}
			}
			
			uni.$on("updateArticle", (article) => { // from detail
				if(article.id == this.thisArticle.id){
					console.log("get")
					this.thisArticle = article ;
				}
			})
			},
		filters: {
			timeDeal(timediff) {
				// console.log(timediff);
				timediff = new Date(timediff);
				// console.log(timediff);
				var parts = [timediff.getFullYear(), timediff.getMonth(), timediff.getDate(), timediff.getHours(), timediff.getMinutes(),
					timediff.getSeconds()
				];
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
			singleImgeFit(e) {
				var height = e.detail.height;
				var width = e.detail.width;
				var rate;
				if (height >= width) {
					this.singleImgState = 0;
					this.singleImgHeight = 400;
					rate = width / height;
					this.heightWidthRate = rate;
					this.singleImgWidth = 400 * rate;
					// console.log(this.singleImgState);
					// console.log(rate);
					// console.log(this.singleImgHeight);
					// console.log(this.singleImgWidth);
				} else {
					this.singleImgState = 1;
					this.singleImgWidth = 400;
					rate = height / width;
					this.heightWidthRate = rate;
					this.singleImgHeight = 400 * rate;
					// console.log(this.singleImgState);
					// console.log(rate);
					// console.log(this.singleImgHeight);
					// console.log(this.singleImgWidth);
				}
				// console.log(e.detail);
			},
			swLikeArticle() {
				if (this.thisArticle.isLike) {
					this.unLikeArticle();
					this.thisArticle.likeNum--;
				} else {
					this.likeArticle();
					this.thisArticle.likeNum++;
				}
				this.thisArticle.isLike = !this.thisArticle.isLike;
			},
			likeArticle() {
				console.log("点赞文章");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userLikeArticle',
					data: {
						userId: that.userInfo.id,
						articleId: that.thisArticle.id,
						articleCreaterId: that.thisArticle.userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
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
						articleId: that.thisArticle.id,
						articleCreaterId: that.thisArticle.userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
					},
				});
			},
			jumpToDetail() {
				var navData = JSON.stringify(this.thisArticle); // 这里转换成 字符串
				uni.navigateTo({
					url: '/pages/detail/detail?data=' + navData
				});
			},
			goToPersonPublic(userId) {
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + userId,
				});
			},
			previewImage: function(index) {
				var imgIndex = index;
				// console.log(res)
				// 获取全部图片路径
				var imgList = this.thisArticle.imgList;
				var arr = [];
				var path;
				for (var i = 0; i < imgList.length; i++) {
					// console.log(imgList[i].imagePath);
					path = this.serverUrl + imgList[i].imagePath
					arr = arr.concat(path);
				}
				// console.log(arr);
				uni.previewImage({
					current: index,
					urls: arr,
				})
			},
			buttomCaculation(timeWidth) {
				var bottmWidth = this.$refs.articleCard.offsetWidth;
				console.log(bottmWidth);
				debugger;
			},
		},
	};
</script>

<style>
	.oneArticle {
		position: relative;
		width: 626upx;
		padding-left: 28upx;
		padding-right: 36upx;
		height:117.5px;
/* 		height: 130px;
 */		box-shadow: 0 0 6px rgba(0, 0, 0, 1);
		border-radius: 12.5px;
		margin-bottom: 15px;
	}
	.oneArticleWithPic{
		height:130px;
	}
	.title {
		height: 14px;
		font-size: 11.5px;
		line-height: 14px;
		color: ##4A4A4A;
		padding-top: 20.5px;
		text-overflow: ellipsis;
		text-wrap: none;
		overflow: hidden;
	}
	.cardBody {
		position: relative;
		width: 100%;
		/* height:83px; */
		padding-top:12.5px;
		height:0;
	}
	.picArea {
		display: inline-block;
		width:73px;
		height:73px;
		position: absolute;
		top:0;
		right:4px;
	}
	.picArea image {
		position: absolute;
		right: 0;
		top: 0;
		width: 73px;
		height: 73px;
	}
	.left-body {
		width: 100%;
		height: 70px;
	}
	.leftBodyWithPic {
		width: calc(100% - 89px);
		height:83px;
	}
	.content {
		height: calc(100% - 31px);
		width: 100%;
		font-size: 9.5px;
		line-height: 13px;
		text-overflow: ellipsis;
		overflow: hidden;
	}
	.bottomBar {
		width: 100%;
		height: 11px;
		padding-top: 10px;
		padding-bottom: 9px;
		position: relative;
		color:#9B9B9B;
	}
	.time {
		width: 77px;
		/* 暂时,到  月-日 时:分*/
		height: 11px;
		font-size: 10px;
		position: absolute;
		left: 0;
		bottom: 9px;
	}
	.comment {
		position: absolute;
		bottom: 9px;
		right: 40px;
		width: 25px;
		height: 11px;
	}
	.like {
		position: absolute;
		bottom: 9px;
		right: 1px;
		width: 25px;
		height: 11px;
	}
	.comment image,.like image {
		width: 11px;
		height: 11px;
		right:14px;
		position: absolute;
	
	}
	.like view,
	.comment view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}
	.status{
		padding-left:9px;
		padding-right: 9px;
		font-size: 9px;
		height:20px;
		position: absolute;
		top:-10px;
		left:14px;
		line-height: 20px;
		color:#FFFFFF;
		border-radius: 12.5px;
/* 		box-shadow: ;
 */	}
</style>

