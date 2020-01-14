<!-- TODO: 取消添加图片, 标签输入不能含有特殊字符，颜色变化 -->
<template>
	<view class="submitMain">

		<!-- 当失去焦点时，将输入内容存入articleTitle -->
		<view style="position: relative;">
			<input class="title" v-model="articleTitle" placeholder="标题" maxlength="20" placeholder-class="title-placeholder">
			<view class="titleTextLeft">{{20 - articleTitle.length}}</view>
		</view>

		<view class="tagsArea">
			<!-- 展示标签区域 -->
			<view class="tag1_area" style="display: inline-block;">
				<view class="tag" v-for="(item,index) in tagList_1" :key="index" @click="deleteTag(index)" style="box-shadow:0px 0px 6px rgba(167,75,220,0.43);;">{{item}}</view>
			</view>
			<view class="submit-button" @tap="upload()">完成</view>
			<view class="tag2_area">
				<view class="tag" v-for="(item,index) in tagList_2" :key="index" @click="deleteTag(index)">{{item}}</view>
			</view>
		</view>


		<textarea placeholder="内容" class="content" v-model="articleContent" maxlength="140" :show-confirm-bar="false"></textarea>
		<view style="display: flex;justify-content: space-between;color: #353535;font-size: 13px;line-height: 28px;height: 24px;">
			<view>还可以输入</view>
			<view>{{140 - articleContent.length}}字</view>
		</view>

		<view class="picturearea">
			<block v-for="(image,index) in imageList" :key="index">
				<view style="position: relative;">
					<!-- todo 预览图片缩放 -->
					<image :src="image" :data-src="image" @tap="previewImage" mode="aspectFill"></image>
					<view style="width:15px;height: 15px;font-size: 10px;line-height: 10px;border-bottom-left-radius: 3px;background: rgba(166, 169, 168,0.3);color:#FFFFFF;position: absolute;top:6px;right:0;text-align: center;"
					 @click="deleteImg(index)">✕</view>
				</view>
			</block>
			<view v-show="isAddImage(this.imageList.length)" id="clickToChooseImage" class="addPic" @click="chooseImg">+</view>
			<view v-if="imageList.length==1||imageList.length==4||imageList.length==7" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
		</view>

	</view>
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

	var uploadFlag = false; // 标志文章正在上传，为 true 时 block 该方法
	var requestTask;
	var uploadTasks = [];
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
				tagList_1: ["标签实例", "标签1"],
				tagList_2: ["标签。第二行", "哈哈哈", "上下左右"],

				finalTag: '',
				tagIndex: 0,

				imageList: [],
				sourceTypeIndex: 2,
				sourceType: ['拍照', '相册', '拍照或相册'],
				sizeTypeIndex: 0,
				sizeType: ['压缩', '原图', '压缩或原图'],
				countIndex: 8,
				count: [1, 2, 3, 4, 5, 6, 7, 8, 9],
				windowHeight: 0,
			}
		},
		onUnload() {
			this.imageList = [],
				this.sourceTypeIndex = 2,
				this.sourceType = ['拍照', '相册', '拍照或相册'],
				this.sizeTypeIndex = 0,
				this.sizeType = ['压缩', '原图', '压缩或原图'],
				this.countIndex = 8;
		},
		onLoad() {
			this.userInfo = this.getGlobalUserInfo();
			// 获取屏幕高度
			var that = this;
			uni.getSystemInfo({
				success: function(res) {
					that.windowHeight = res.windowHeight;
				}
			});

		},
		methods: {
			addTag: function() {
				this.showInputTagArea = 1;
				this.showAddTagButton = 0;
			},
			manageTag: function() {

			},
			// 检查tagList的数量
			checkInput: function(res) {
				var that = this;
				var tag = this.articleTag;
				//console.log(tag)
				if (this.isNull(tag)) {
					that.showAddTagButton = 1;
					that.showInputTagArea = 0;
				} else {
					// 显示标签区域 = 1
					that.showTagArea = 1;

					//console.log(that.tagIndex);
					that.tagList[that.tagIndex] = tag;
					that.tagIndex = that.tagIndex + 1;
					that.showAddTagButton = 1;
					that.showInputTagArea = 0;
					that.articleTag = '';
				}

			},

			combineTagToString: function(res) {
				var that = this;
				for (var i = 0; i < that.tagList.length; i++) {
					that.finalTag = that.finalTag + '#' + that.tagList[i];
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

				uni.chooseImage({
					sourceType: sourceType[this.sourceTypeIndex],
					sizeType: sizeType[this.sizeTypeIndex],
					count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
					success: (res) => {
						this.imageList = this.imageList.concat(res.tempFilePaths);

						console.log(res)
					}
				})
			},
			previewImage: function(e) {
				var current = e.target.dataset.src
				// console.log(e)
				console.log(this.imageList);
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

			// TODO：图片上传需加上大小限制，后台限制10M
			upload: function(e) {
				var me = this;
				if (this.isBlank(me.articleTitle) || this.isNull(me.articleTitle)) {
					uni.showToast({
						icon: 'none',
						title: '文章标题不能为空～',
						duration: 1000
					});
					return;
				}

				if (this.isBlank(me.articleContent) || this.isNull(me.articleContent)) {
					uni.showToast({
						icon: 'none',
						title: '文章内容不能为空～',
						duration: 1000
					});
					return;
				}

				if (uploadFlag) {
					console.log("正在上传...")
					return;
				}
				uploadFlag = true;
				uni.showLoading({
					title: "正在上传..."
				})

				setTimeout(() => {
					if (uploadFlag) {
						uploadFlag = false // 解锁
						uni.hideLoading();
						uni.showToast({
							title: "网络未知错误",
							icon: "none",
							duration: 1000
						})
						// TODO: 终止上传
						for (let task in uploadTasks) {
							console.log(task)
							task.abort();
						};
						requestTask.abort();
					}
				}, 5000); // 延时5s timeout

				setTimeout(() => {
					me.combineTagToString();

					var serverUrl = me.$serverUrl;
					requestTask = uni.request({
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
							// console.log(res);
							me.finalTag = ""; // 清空组装tag
							if (res.data.status == 200) {
								if (me.imageList.length > 0) {
									const articleId = res.data.data;
									var resCount = 0
									for (var i = 0; i < me.imageList.length; i++) {
										uploadTasks[i] = uni.uploadFile({
											url: this.$serverUrl + '/article/uploadArticleImg',
											filePath: me.imageList[i],
											name: 'file',
											formData: {
												userId: me.userInfo.id,
												articleId: articleId,
												order: i
											},
											success: (uploadFileRes) => {
												resCount++;
												if (resCount == me.imageList.length) {
													me.uploadSuccess();
												}
											}
										});
									}
								} else {
									me.uploadSuccess();
								}
							} else if (res.data.status == 501) {
								me.contentIllegal();
							} else {
								me.uploadFail();
							}
						},
						fail: (res) => {
							me.uploadFail();
						}
					})
				}, 100) //延时执行等待上锁
			},

			uploadSuccess() {
				uploadFlag = false;
				uni.hideLoading();
				uni.$emit("flash"); // 给 index 发送刷新信号
				uni.navigateBack({
					delta: 1
				})
				uni.showToast({
					title: '已提交审核',
					duration: 2000,
					icon: 'success',
				})
			},

			uploadFail() {
				// 上传失败 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '出现未知错误，上传失败',
					duration: 2000,
					icon: 'none',
				})
			},

			contentIllegal() {
				// 内容非法 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '内容涉嫌违规，请联系管理员。',
					duration: 2000,
					icon: 'none',
				})
			},

			deleteTag: function(index) {
				console.log(index);
				var targetTag = this.tagList[index];
				this.tagList.splice(index, 1);
				console.log(this.tagList.length);
				this.tagIndex = this.tagList.length;
			},
			deleteImg: function(index) {
				// console.log(index);
				// console.log(this.imageList[index]);
				this.imageList.splice(index, 1);
			},
		}
	};
</script>
<style>
	page {
		height: 100%;
	}

	.submitMain {
		width: 698upx;
		margin: auto;
	}

	.title {
		width: 698upx;
		height: 38px;
		border: 2px solid rgba(238, 238, 238, 1);
		border-radius: 8px;
	}

	.title-placeholder {
		height: 14px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(199, 199, 199, 1);
		letter-spacing: 17px;
	}

	.titleTextLeft {
		position: absolute;
		right: 8px;
		top: 15px;
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		color: rgba(199, 199, 199, 1);
	}

	.tagsArea {
		margin-top: 13px;
		vertical-align: bottom;
		min-height: 42px;
		max-height: 62px;
		overflow: hidden;
		position: relative;
	}

	.tag {
		display: inline-block;
		vertical-align: bottom;
		color: #353535;
		font-size: 14px;
		line-height: 26px;
		height: 26px;

		position: relative;
		margin-left: 8px;
		margin-top: 6px;
		padding-right: 27px;
		padding-left: 12px;
		pointer-events: none;
		/* 用于解决空格换行问题 */
		white-space: nowrap;

		border-radius: 20px;
	}

	.tag::after {
		position: absolute;
		content: "✕";
		right: 10px;
		margin-left: 4px;
		color: #000000;
		font-size: 13px;
		pointer-events: auto;
		font-weight: 900;
		/* 用于解决空格换行问题 */
		white-space: nowrap;
	}

	.submit-button {
		width: 68px;
		height: 26px;
		background: linear-gradient(318deg, rgba(251, 118, 118, 1) 0%, rgba(254, 192, 77, 1) 100%);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		border-radius: 8px;

		font-size: 14px;
		line-height: 26px;
		color: rgba(255, 255, 255, 1);
		text-align: center;
		position: absolute;
		right: 0;
		top: 0;
	}

	.content {
		min-height: 136px;
		background: #F4F1E9;
		margin-top: 13px;
		width: 100%;
		overflow: scroll;
		padding: 4px;
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
