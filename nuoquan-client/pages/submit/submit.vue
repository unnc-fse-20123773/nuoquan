<!-- TODO: 取消添加图片, 标签输入不能含有特殊字符，颜色变化 -->
<template>
	<viwe>
		<view style="height:45px;width:100%;background: url(../../static/BG/submitBG.png);background-repeat: no-repeat;background-position-y: -5px;background-size: 750upx 80px;">
			<view class="submit" @click="upload">
				发 表
			</view>
		</view>
		<view class="submitMain" :style="{height: submitMainHeight}">
			<!-- 当失去焦点时，将输入内容存入articleTitle -->
			<input class="title" v-model="articleTitle" placeholder="标题" maxlength="20">
			<view style="display: flex;justify-content: space-between;color: #353535;font-size: 13px;line-height: 28px;height: 24px;">
				<view>还可以输入</view>
				<view>{{20 - articleTitle.length}}字</view>
			</view>
			<view class="tagsArea">
				<!-- 展示标签区域 -->
				<view class="tag" v-if="showTagArea" v-for="(item,index) in tagList" :key="index" @click="deleteTag(index)">{{item}}</view>
				<!-- 添加标签区域 -->
				<view class="addTag">
					<view v-if="showAddTagButton" @click="addTag">
						+ 添加标签
					</view>
					<!-- TODO: 字数未区分中英文，下一个版本要加 -->
					<input v-if="showInputTagArea" v-model="articleTag" focus="true" placeholder="请输入标签..." @blur="checkInput"
					 maxlength="10" />

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
				tagList: [],
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
				submitMainHeight: '',
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
			// 获取页面高度
			that.submitMainHeight = that.windowHeight - 45 +'px';
		},
		methods: {
			addTag: function() {
				this.showInputTagArea = 1;
				this.showAddTagButton = 0;
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
				
				setTimeout(()=>{
					if(uploadFlag){
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
												if(resCount == me.imageList.length){
													me.uploadSuccess();
												}
											}
										});
									}
								}else{
									me.uploadSuccess();
								}
							} else if(res.data.status == 501){
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
			
			uploadSuccess(){
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
			
			uploadFail(){
				// 上传失败 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '出现未知错误，上传失败',
					duration: 2000,
					icon: 'none',
				})
			},
			
			contentIllegal(){
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
		width: 606upx;
		padding: 38upx 72upx;
		border-top-left-radius: 18px;
		border-top-right-radius: 18px;
		background: #FFFFFF;
		box-shadow: 0px 2px 10px 0px rgba(0, 0, 0, 0.35);
		overflow: scroll;
	}

	.title {
		height: 36px;
		border-radius: 5px;
		background: #F4F1E9;
		margin-top: 19px;
		padding: 0 4px;
	}

	.tagsArea {
		margin-top: 13px;
		vertical-align: bottom;
		min-height: 42px;
		max-height: 62px;
		overflow: hidden;
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
		pointer-events: none;
		/* 用于解决空格换行问题 */
		white-space: nowrap;
	}

	.tag::after {
		position: absolute;
		content: "✕";
		right: 10px;
		margin-left: 4px;
		color: #939393;
		font-size: 13px;
		pointer-events: auto;
		/* 用于解决空格换行问题 */
		white-space: nowrap;
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
