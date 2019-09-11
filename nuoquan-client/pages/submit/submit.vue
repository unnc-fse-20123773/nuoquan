<!-- TODO: 取消添加图片, 标签输入不能含有特殊字符，颜色变化 -->
<template>
	<viwe>
		<view style="height:45px;width:100%;">
			<view class="submit" @click="upload">
				发 表
			</view>
		</view>

		<view class="submitMain">
			<!-- 当失去焦点时，将输入内容存入articleTitle -->
			<input class="title" v-model="articleTitle" placeholder="  标题" />
			<view class="tagsArea">
				<!-- 展示标签区域 -->
				<view class="tag" v-if="showTagArea" v-for="i in tagList" :key="i">{{i}}</view>
				<!-- 添加标签区域 -->
				<view class="addTag">
					<view v-if="showAddTagButton" @click="addTag">
						+ 添加标签
					</view>
					<input v-if="showInputTagArea" v-model="articleTag" focus="true" placeholder="请输入标签..." @blur="checkInput" />

				</view>
			</view>
			<textarea placeholder="内容[最多2048字]" class="content" v-model="articleContent" maxlength=2048></textarea>
			<view style="display: flex;justify-content: space-between;color: #353535;font-size: 13px;line-height: 28px;height: 24px;">
				<view>点击可预览选好的图片</view>
				<view>{{imageList.length}}/9</view>
			</view>

			<view class="picturearea">

				<block v-for="(image,index) in imageList" :key="index">
					<view>
						<!-- todo 预览图片缩放 -->
						<image :src="image" :data-src="image" @tap="previewImage"></image>
					</view>

				</block>
				<view v-show="isAddImage(this.imageList.length)" id="clickToChooseImage" class="addPic" @click="chooseImg">+</view>
				<view v-if="imageList.length==1||imageList.length==4||imageList.length==7" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>

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
				userInfo: '',
				articleTitle: '',
				articleContent: '',
				articleTag: '',
				imgPath: '',
				showInputTagArea: 0,
				showAddTagButton: 1,
				showTagArea: 0,
				tagList: [],
				finalTag: '',
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
			this.userInfo = this.getGlobalUserInfo();
		},
		methods: {
			addTag: function(res) {
				this.showInputTagArea = 1;
				this.showAddTagButton = 0;
			},
			// 检查tagList的数量
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
					that.articleTag = '';
				}
			},
			combineTagToString: function(res) {
				var that = this;
				for(var i = 0; i < that.tagList.length; i++) {
					that.finalTag = that.finalTag + '#' + that.tagList[i];
				}
				console.log(that.finalTag);
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
				
				if (me.articleTitle == '' || me.articleTitle == null) {
					uni.showToast({
						icon: 'none',
						title: '文章标题不能为空～',
						duration: 1000
					});
					return;
				}

				if (me.articleContent == '' || me.articleContent == null) {
					uni.showToast({
						icon: 'none',
						title: '文章内容不能为空～',
						duration: 1000
					});
					return;
				}
				
				me.combineTagToString();
				
				var serverUrl = me.$serverUrl;
				uni.request({
					url: serverUrl + '/article/uploadArticle',
					method: 'POST',
					data: {
						userId: me.userInfo.id,
						articleTag: me.finalTag,
						articleTitle: me.articleTitle,
						articleContent: me.articleContent
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// console.log(res.data.data);
						if (res.data.status == 200){
							if (me.imageList.length <= 0) {
								uni.navigateBack({
									url: '../index/index'
								})
							} else {
								const articleId = res.data.data;
								for (var i = 0; i < me.imageList.length; i++) {
									uni.uploadFile({
										url: this.$serverUrl + '/article/uploadArticleImg',
										filePath: me.imageList[i],
										name: 'file',
										formData: {
											userId: me.userInfo.id,
											articleId: articleId,
											order: i
										},
										success: (uploadFileRes) => {
											uni.navigateBack({
												delta: 1
											})
										}
									});
								}
							}
						}else{
							// 上传失败 用户提醒
							uni.showToast({
								title: '出现未知错误，上传失败',
								duration: 2000,
								icon: 'none',
							})
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
		vertical-align: bottom;
		min-height: 42px;
	}

	.tag {
		display: inline-block;
		vertical-align: bottom;
		color: #353535;
		font-size: 13px;
		line-height: 24px;
		height: 24px;
		padding-right: 27px;
		padding-left: 12px;
		border: solid 2px #FE5F55;
		border-radius: 14px;
		position: relative;
		margin-right: 12px;
		margin-bottom: 6px;
	}

	.tag::after {
		position: absolute;
		content: "✕";
		right: 12px;
		color: #939393;
		font-size: 13px;
	}

	.addTag {
		height: 30px;
		display: inline-block;
		vertical-align: bottom;
		margin-bottom: 6px;

	}

	.addTag input {
		display: inline-block;
		color: #353535;
		font-size: 13px;
		line-height: 24px;
		height: 24px;
		padding-right: 12px;
		padding-left: 12px;
		border: solid 2px #FE5F55;
		border-radius: 14px;
		min-height: 24px;
		vertical-align: bottom;
		margin-top: 2px;

	}

	.addTag view {
		display: inline-block;
		color: #353535;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
		padding-right: 12px;
		padding-left: 12px;
		border-radius: 14px;
		background: #FDD041;
		margin-right: 12px;
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
		width: 178upx;
		height: 178upx;
		line-height: 160upx;
		margin: 6px 0;
		border: dashed 3px #BEBCB5;
		text-align: center;
		vertical-align: middle;
		color: #BEBCB5;
		font-size: 70px;
		font-weight: 200;
	}
</style>
