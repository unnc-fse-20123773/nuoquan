<template>
	<scroll-view class="scroll-box" scroll-x="true" scroll-with-animation="true" bindtouchstart="touchStart" bindtouchend="touchEnd">
		<view class="card-box">
			<view class="voteCard">
				<scroll-view scroll-y="true" class="contentCard">
					<view class="articlecard">
						<view>
							<view class="title">标题标题标题标题标题标题标题标题标题</view>
						</view>
						<!-- 用户信息行 -->
						<view class="userLine hor_center">
							<image src="../../static/icon/viewLocalPic.png" class="touxiang" @tap="goToPersonPublic(thisArticle.userId)"></image>
							<view class="name">陈仅仅一号</view>
							<view class="time" :style="timeLeft">一天前</view>
						</view>
						<!-- 标签行 -->
						<view class="tagsLine">
							<view class="tag" :style="{ background: tagColorList[index] }" v-for="(i, index) in thisArticle.tagList" v-bind:key="index">{{ i }}</view>
						</view>
						<!-- 内容 -->
						<view class="briefarticleCard">投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容投票内容</view>
						<view :class="[thisArticle.imgList.length == 1 ? 'picturearea-one' : 'picturearea-mul']">
							<!-- *******这里是文章配图的位置*******-->

							<!-- 下面两个 view 分别为蒙版背景层和数字层，都是由 margin-left = 67.5% 精准推至第三张图位置上的 -->
							<view v-if="thisArticle.imgList.length > 3" style="margin-left: 67.5%;position: absolute;width: 30%;height: 200upx;" class="super_center">
								<view style="color: white;font-weight: 600;font-size: 24px;z-index: 20;">+{{ thisArticle.imgList.length - 3 }}</view>
							</view>
							<view
								v-if="thisArticle.imgList.length > 3"
								style="position: absolute;width: 30%;height: 200upx;
							background-color: #000000;opacity: 0.5;margin-left: 67.5%;z-index: 10;"
							></view>

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
										@tap="previewImage(0)"
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
										@tap="previewImage(0)"
									></image>
								</view>
							</view>
							<!-- 多图显示 -->

							<view style="width:30%;height: 200upx;margin-left: 2.5%;display: flex;background-color: #D1D1D1;" v-else v-for="(item, index) in imgList" :key="index">
								<image mode="aspectFill" style="height: 200upx" :src="serverUrl + item.imagePath" @tap="previewImage(index)"></image>
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</view>
	</scroll-view>
</template>

<script>
export default {
	data() {
		return {
			startPageX: 0,
			currentView: DEFAULT_PAGE,
			data: {
				toView: `card_${DEFAULT_PAGE}`,
				list: ['Javascript', 'Typescript', 'Java', 'PHP', 'Go']
			}
		};
	},

	onLoad: function() {},

	onShow() {
		this.setTabBarIndex(1); //index为当前tab的索引
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
		}
	}
};
</script>

<style>
page {
	overflow: hidden;
	background: #0d1740;
}
.scroll-box {
	white-space: nowrap;
	height: 105vh;
}

.card-box {
	display: inline-block;
}

.voteCard {
	display: flex;
	justify-content: center;
	align-items: center;
	box-sizing: border-box;
	height: 80vh;
	width: 80vw;
	margin: 5vh 10vw;
	font-size: 40px;
	background: #f8f2dc;
	border-radius: 4px;
}

.contentCard {
	width: 100%;
	height: 100%;
}

.articlecard {
	width: 100%;
	border-radius: 8px;
	margin: 3.47% auto 0;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 7px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 8px;
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
	line-height: 16px;
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
	max-width: 24%;
	font-size: 14px;
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
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 23px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
}
</style>
