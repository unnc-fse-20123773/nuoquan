/*
	#主页文章预览卡片
	#Author: Yaoyao
	#Update Log: Guetta 2020.6.6 - Update to UI v1.2.2 
*/

<template>
	<view class="articlecard" ref="articleCard" @click="goToDetail()">
		<view>
			<text selectable="true" class="title">{{ thisArticle.articleTitle }}</text>
			<!-- <view class="briefarticleCard">{{ thisArticle.articleContent }}</view> -->
		</view>
		<!-- 用户信息行 -->
		<view class="userLine hor_center">
			<image :src="pathFilter(thisArticle.faceImg)" class="touxiang" @tap.stop="goToPersonPublic(thisArticle.userId)"></image>
			<view class="name" @tap.stop="goToPersonPublic(thisArticle.userId)">{{ thisArticle.nickname }}</view>
			<view class="time" :style="timeLeft">{{ timeDeal(thisArticle.createDate) }}</view>
		</view>
		<!-- 标签行 -->
		<view class="tagsLine">
			<view class="tag" :style="{ background: tagColorList[index] }" v-for="(i, index) in thisArticle.tagList" v-bind:key="index">{{ i }}</view>
		</view>
		<!-- 内容 -->
		<text selectable="true" class="briefarticleCard">{{ thisArticle.articleContent }}</text>
		<view :class="[thisArticle.imgList.length == 1 ? 'picturearea-one' : 'picturearea-mul']">
			<!-- *******这里是文章配图的位置*******-->

			<view @click.stop="goToDetail()">
				<!-- 下面两个 view 分别为蒙版背景层和数字层，都是由 margin-left = 67.5% 精准推至第三张图位置上的 -->
				<view v-if="thisArticle.imgList.length > 3" style="margin-left: 67.5%;position: absolute;width: 30%;height: 200upx;" class="super_center">
					<view style="color: white;font-weight: 600;font-size: 24px;z-index: 20;">+{{ thisArticle.imgList.length - 3 }}</view>
				</view>
				<view
					v-if="thisArticle.imgList.length > 3"
					style="position: absolute;width: 30%;height: 200upx;
				background-color: #000000;opacity: 0.5;margin-left: 67.5%;z-index: 10;"
				></view>
			</view>

			<!-- 宽高和 image 保持一致 -->
			<!-- 单图显示 -->
			<view style="width: 100%;max-height: 400upx;" v-if="thisArticle.imgList.length == 1">
				<!-- 高 ＞ 宽 -->
				<view v-if="singleImgState == 0">
					<!-- :style在编译到微信小程序时，不会将'upx'编译为'rpx'，故 width 失效 -Guetta -->
					<image
						mode="aspectFit"
						:style="{ height: singleImgHeight + 'rpx', width: singleImgWidth + 'rpx' }"
						:src="serverUrl + thisArticle.imgList[0].imagePath"
						@load="singleImgeFit"
						@tap.stop="previewImage(0)"
					></image>
				</view>

				<!-- 宽 > 高 -->
				<view v-else style="width: 100%;">
					<image
						mode="aspectFit"
						style="margin-left: 10upx;"
						:style="{ height: singleImgHeight + 'rpx', width: singleImgWidth + 'rpx' }"
						:src="serverUrl + thisArticle.imgList[0].imagePath"
						@load="singleImgeFit"
						@tap.stop="previewImage(0)"
					></image>
				</view>
			</view>
			<!-- 多图显示 -->

			<view style="width:30%;height: 200upx;margin-left: 2.5%;display: flex;background-color: #D1D1D1;" v-else v-for="(item, index) in imgList" :key="index">
				<image mode="aspectFill" style="height: 200upx" :src="serverUrl + item.imagePath" @tap.stop="previewImage(index)"></image>
			</view>
		</view>
		<!-- 操作行 -->
		<view class="menubar">
			<view class="menubar_rel">
				<!-- 搞笑大赛 -->
				<!-- <image v-if="isfunCom" class="menubar_left" style="width: 75px;height: 36px;" src="../static/BG/funCom.png" mode="aspectFit"></image> -->
				<!-- 分享 -->
				<!-- <image class="menubar_left" style="width:18px;height:18px;" src="../static/icon/share-alt-353535.png" mode="aspectFit"></image> -->
				<!-- 评论和点赞 -->
				<view class="operationBar column_center">
					<nqCmt @click.native.stop="goToDetail()" :number="thisArticle.commentNum"></nqCmt>
					<nqLike style="margin-left: 11px;" @click.native.stop="swLikeArticle" :status="thisArticle.isLike" :number="thisArticle.likeNum"></nqLike>
				</view>
			</view> 
		</view>
	</view>
</template>

<script>
import nqLike from '../components/nq-button/nq-likeButton.vue';
import nqCmt from '../components/nq-button/nq-cmtButton.vue';

export default {
	name: 'aticlebrief',
	props: {
		articleCard: {}
	},
	components:{
		nqLike,
		nqCmt
	},
	data() {
		return {
			serverUrl: this.$serverUrl,
			userInfo: this.getGlobalUserInfo(),
			// singleImgState: '0',
			singleImgHeight: 0,
			singleImgWidth: 0,
			heightWidthRate: 0,
			imgList: [],
			thisArticle: this.articleCard, // 转为局部变量
			tagColorList: [], // 储存每个tag的颜色
			timeLeft: '',
			isfunCom: false,//显示搞笑大赛图标
		};
	},

	created() {
		// console.log(this.thisArticle);
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
		
		// 捕获特殊标签做特殊处理
		// this.catchSpecialTag();

		uni.$on('updateArticle', article => {
			// from detail
			if (article.id == this.thisArticle.id) {
				console.log('get');
				this.thisArticle = article;
			}
		});
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
		
		// 搞笑大赛
		// catchSpecialTag(){
		// 	if (this.thisArticle.tagList != null) {
		// 		for (var tag of this.thisArticle.tagList) {
		// 			//搞笑大赛
		// 			if (tag == 'UNNC搞笑大赛'){
		// 				this.isfunCom = true;
		// 			}
		// 		}
		// 	}
		// },

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
			console.log('点赞文章');
			var that = this;
			uni.request({
				method: 'POST',
				url: that.$serverUrl + '/article/userLikeArticle',
				data: {
					userId: that.userInfo.id,
					articleId: that.thisArticle.id,
					articleCreaterId: that.thisArticle.userId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},

		unLikeArticle() {
			console.log('取消点赞文章');
			var that = this;
			uni.request({
				method: 'POST',
				url: that.$serverUrl + '/article/userUnLikeArticle',
				data: {
					userId: that.userInfo.id,
					articleId: that.thisArticle.id,
					articleCreaterId: that.thisArticle.userId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},
		goToDetail() {
			// var encodeData = encodeURIComponent(JSON.stringify(this.thisArticle)); // 对数据字符串化并转码，防止特殊字符影响传参
			//传入跳转文章id和是否为搞笑大赛文章
			uni.navigateTo({
				url: '/pages/detail/detail?data=' + this.thisArticle.id
			});
		},
		goToPersonPublic(userId) {
			uni.navigateTo({
				url: '/pages/personpublic/personpublic?userId=' + userId
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
				path = this.serverUrl + imgList[i].imagePath;
				arr = arr.concat(path);
			}
			// console.log(arr);
			uni.previewImage({
				current: index,
				urls: arr
			});
		},

		buttomCaculation(timeWidth) {
			var bottmWidth = this.$refs.articleCard.offsetWidth;
			console.log(bottmWidth);
		}
	}
};
</script>

<style>
image {
	border: none;
	outline: none;
	max-height: 360upx;
	margin: auto;
}
/* image {
		
	} */
</style>
<style scoped>
.articlecard {
	width: 100%;
	border-radius: 8px;
	margin: 3.47% auto 0;
	background-color: rgba(255, 255, 255, 1);
	/* box-shadow: 0px 0px 7px rgba(0, 0, 0, 0.16); */
	opacity: 1;
	border-radius: 8px;
	border-bottom: 1px solid rgba(236,236,236,1);
}

.title {
	width: 93.12%;
	font-size: 17px;
	font-family: Source Han Sans CN;
	line-height: 21px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
	font-weight: bold;
	margin: 16px 3.44% 0 3.44%;
	padding-top: 16px;
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2;
	overflow: hidden;
}

.briefarticleCard {
	width: 93.12%;
	font-size: 14px;
	font-family: Source Han Sans CN;
	line-height: 20px;
	opacity: 1;
	font-weight: 400;
	margin: 8px 3.44% 8px 3.44%;
	color: rgba(53, 53, 53, 1);
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 15;
	overflow: hidden;
}

.tagsLine {
	width: 93.12%;
	margin-left: 3.44%;
	margin-top: 12px;
}

.tag {
	display: inline-block;
	border-radius: 20px;
	color: #40a792;
	font-size: 11px;
	height: 11px;
	line-height: 11px;
	padding: 6px 10px;
	background: #621e81;
	vertical-align: middle;
	margin-right: 9px;
	color: #ffffff;
}

.tag-empty {
	margin-left: 10px;
	height: 15px;
	width: auto;
	background-color: white;
}

.userLine {
	position: relative;
	width: 100%;
	height: 24px;
	margin-top: 12px;
}

.touxiang {
	position: absolute;
	left: 3.44%;
	border-radius: 30px;
	width: 24px;
	height: 24px;
	vertical-align: middle;
}
/* .touxiang::after{
		content: "";
		position: absolute;
		height:30px;
		width:30px;
		left:-5px;
		top:0;
	} */

.name {
	position: absolute;
	left: 12.61%;
	/* max-width: 24%; */
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 23px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.time {
	position: absolute;
	right: 3.44%;
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 23px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
}

.menubar {
	height: 36px;
	width: 93.12%;
	margin: 16px 0 0 3.44%;
}

.menubar_rel {
	position: relative;
	width: 100%;
	height: 100%;
}

.menubar_left{
	position: absolute;
	left: 0;
	bottom: 12px;
}

.operationBar{
	position: absolute;
	right: 0;
	min-width: 94px;
	display: flex;
	height: 18px;
}

.commentBar{
	height: 100%;
}

.commentBar image{
	width:13px;
	height:12px;
	margin-right: 8px;
}

.commentBar text{
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:400;
	line-height:23px;
	color:rgba(53,53,53,1);
}

.picturearea-one {
	/* margin: auto; */
	display: flex;
	width: 95.6%;
	margin-left: 2.2%;
}

.picturearea-mul {
	position: relative;
	margin: auto;
	display: flex;
	/* 在此设置图片区域宽度 */
	width: 95.6%;
	margin-left: 2.2%;
}
</style>
