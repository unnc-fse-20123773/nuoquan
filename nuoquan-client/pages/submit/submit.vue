<template>
	<viwe>
		<view style="height:45px;width:100%;">
			<view class="submit" @click="upload">
				发 表
			</view>
		</view>

		<view class="submitMain">
			<!-- 当失去焦点时，将输入内容存入articleTitle -->
			<input class="title" @blur="saveAsArticleTitle" placeholder="  标题" />
			<view class="tagsArea">
				<!-- 展示标签区域 -->
				<view class="tag" v-if="showTagArea" v-for="i in tagList" :key="i">{{i}}</view>
				<!-- 添加标签区域 -->
				<view class="addTag">
					<view v-if="showAddTagButton" @click="addTag">
						+ 添加标签
					</view>
					<view v-if="showInputTagArea">
						<input focus="true" placeholder="请输入标签..." @blur="checkInput" />
					</view>
				</view>
			</view>
			<textarea placeholder="内容" class="content" @blur="saveAsArticleContent"></textarea>
			<view class="picturearea">
				<view>点击可预览选好的图片</view>
				<view>{{imageList.length}}/9</view>
				<block v-for="(image,index) in imageList" :key="index">
					<view>
						<!-- todo 预览图片缩放 -->
						<image :src="image" :data-src="image" @tap="previewImage"></image>
					</view>
				</block>
				<view v-show="isAddImage(this.imageList.length)" id="clickToChooseImage" class="addPic" @click="chooseImg">+</view>
			</view>
		</view>
	</viwe>
</template>

<script>
	// #ifdef APP-PLUS
	import permision from "@/common/permission.js"
	// #endif
	var sourceType = [
		['camera'],
		['album'],
		['camera', 'album']
	]
	var sizeType = [
		['compressed'],
		['original'],
		['compressed', 'original']
	]
	export default {
		data() {
			return {
				userName: 'xdy123123123123',
				articleTitle: '',
				articleContent: '',
				articleTag: '',
				imgPath: '',
				showInputTagArea: 0,
				showAddTagButton: 1,
				showTagArea: 0,
				tagList: [],
				tagIndex: 0,

				imageList: [],
				sourceTypeIndex: 2,
				sourceType: ['拍照', '相册', '拍照或相册'],
				sizeTypeIndex: 2,
				sizeType: ['压缩', '原图', '压缩或原图'],
				countIndex: 8,
				count: [1, 2, 3, 4, 5, 6, 7, 8, 9]
			}
		},
		onUnload() {
			this.imageList = [],
				this.sourceTypeIndex = 2,
				this.sourceType = ['拍照', '相册', '拍照或相册'],
				this.sizeTypeIndex = 2,
				this.sizeType = ['压缩', '原图', '压缩或原图'],
				this.countIndex = 8;
		},
		onLoad() {

		},
		methods: {
			// 将标题存放在articleTitle中
			saveAsArticleTitle: function(event) {
				this.articleTitle = event.target.value;
				// console.log(this.articleTitle);
			},
			// 将内容存放在articleContent中
			saveAsArticleContent: function(event) {
				this.articleContent = event.target.value;
				// console.log(this.articleContent);
			},
			addTag: function(res) {
				this.showInputTagArea = 1;
				this.showAddTagButton = 0;
			},
			checkInput: function(res) {
				var that = this;
				var tag = res.target.value;
				if (this.isNull(tag)) {
					that.showAddTagButton = 1;
					that.showInputTagArea = 0;
				} else {
					that.showTagArea = 1;
					that.tagList[that.tagIndex] = tag;
					that.tagIndex = that.tagIndex + 1;
					that.showAddTagButton = 1;
					that.showInputTagArea = 0;
				}
			},
			sourceTypeChange: function(e) {
				this.sourceTypeIndex = parseInt(e.target.value)
			},
			sizeTypeChange: function(e) {
				this.sizeTypeIndex = e.target.value
			},
			countChange: function(e) {
				this.countIndex = e.target.value;
			},
			chooseImg: async function() {
				if (this.imageList.length === 9) {
					let isContinue = await this.isFullImg();
					console.log("是否继续?", isContinue);
					if (!isContinue) {
						return
					}
				}

				uni.chooseImage({
					sourceType: sourceType[this.sourceTypeIndex],
					sizeType: sizeType[this.sizeTypeIndex],
					count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
					success: (res) => {
						this.imageList = this.imageList.concat(res.tempFilePaths);

						console.log(res)
						// for(var i = 0; i < 9; i++){
						// 	console.log(this.imageList[i]);
						// }
					}
				})
			},
			isFullImg: function() {
				return new Promise((res) => {
					uni.showModal({
						content: "已经有9张图片了,是否清空现有图片？",
						success: (e) => {
							if (e.confirm) {
								this.imageList = [];
								res(true);
							} else {
								res(false)
							}
						},
						fail: () => {
							res(false)
						}
					})
				})
			},
			previewImage: function(e) {
				var current = e.target.dataset.src
				uni.previewImage({
					current: current,
					urls: this.imageList
				})
			},
			isAddImage: function() {
				if (this.imageList.length >= 9) {
					return false;
				} else {
					return true;
				}
			},
			upload: function(e) {
				var me = this;

				console.log(me.articleTitle);
				console.log(me.articleContent);
				console.log(me.userName);

				var serverUrl = me.$serverUrl;
				uni.request({
					url: serverUrl + '/article/uploadArticle',
					method: 'POST',
					data: {
						userId: me.userName,
						articleTitle: me.articleTitle,
						articleContent: me.articleContent
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// console.log(res.data.data);
						const articleId = res.data.data;
						for (var i = 0; i < me.imageList.length; i++) {
							uni.uploadFile({
								url: this.$serverUrl + '/article/uploadArticleImg',
								filePath: me.imageList[i],
								name: 'file',
								formData: {
									userId: me.userName,
									articleId: articleId
								},
								success: (uploadFileRes) => {
									uni.redirectTo({
										url: '../index/index'
									})
								}
							});
						}
					}
				})
			},

			/* 以下为 Jerrio 测试代码块 */

		}
	};
</script>
<style>
	page {
		background: #FDD047;
		height: 100%;
	}

	.submit {
		float: right;
		margin-right: 80upx;
		margin-top: 14px;
		width: 55px;
		height: 26px;
		line-height: 26px;
		border: solid 1px #FDD041;
		border-radius: 5px;
		font-weight: bold;
		font-size: 15px;
		color: #FDD041;
		text-align: center;
		background: #FFFFFF;
	}

	.submitMain {
		height: 100%;
		width: 606upx;
		padding: 38upx 72upx;
		border-top-left-radius: 18px;
		border-top-right-radius: 18px;
		background: #FFFFFF;
		box-shadow: 0px 2px 10px 0px rgba(0, 0, 0, 0.35);
	}

	.title {
		height: 36px;
		border-radius: 5px;
		background: #F4F1E9;
		margin-top: 19px;
	}

	.tagsArea {
		margin-top: 13px;
	}

	.tag {
		display: inline-block;
		color: #353535;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
		padding-right: 27px;
		padding-left: 12px;
		border: solid 2px #FE5F55;
		border-radius: 14px;
		position: relative;
		margin-right: 12px;
		margin-bottom: 12px;
	}

	.tag::after {
		position: absolute;
		content: "X";
		right: 12px;
	}

	.addTag {
		display: inline-block;
		color: #353535;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
		padding-right: 12px;
		padding-left: 12px;
		border: solid 2px #FE5F55;
		border-radius: 14px;
	}

	.content {
		min-height: 136px;
		background: #F4F1E9;
		margin-top: 13px;
		width: 100%;
	}

	.picturearea {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		margin-top: 10px;
	}

	.picturearea image {
		width: 190upx;
		height: 190upx;
		margin: 6px 0;
	}

	.addPic {
		width: 190upx;
		height: 190upx;
		line-height: 180upx;
		margin: 6px 0;
		border: dashed 3px #BEBCB5;
		text-align: center;
		vertical-align: middle;
		color: #BEBCB5;
		font-size: 70px;
		font-weight: 200;
	}

	.placeHolderForPic {
		width: 190upx;
		height: 190upx;
	}
</style>
