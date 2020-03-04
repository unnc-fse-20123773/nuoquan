<template>
	<view class="submitVoteMain">
		<!-- 导航栏 -->
		<uni-nav-bar
			class="navigationBar"
			:style="{ height: this.getnavbarHeight() + 'px' }"
			:showLeftIcon="true"
			:isNavHome="isNavHome"
			:left-text="lang.back"
			:title="lang.tabList[1]"
			:height="navbarHeight"
		></uni-nav-bar>

		<view :style="{ height: navbarHeight + 'px' }"></view>
		<!-- 当失去焦点时，将输入内容存入voteTitle -->
		<view style="position: relative;margin-top: 20px;">
			<input class="title" v-model="voteTitle" placeholder="标题" maxlength="20" placeholder-class="title-placeholder" />
			<view class="titleTextLeft">{{ 20 - voteTitle.length }}</view>
		</view>

		<button class="addContentButton" @tap="showContent()">{{ lang.addBody }}</button>

		<!-- 正文内容 -->
		<view style="position: relative;" v-if="showContengEdit">
			<textarea class="content" v-model="voteContent" maxlength="140" :auto-height="true" :show-confirm-bar="false"></textarea>
			<view style="position: absolute;bottom: 8px;right:8px;font-size: 11px;color:#888888;">{{ 140 - voteContent.length }}</view>
			<image src="../../static/icon/emoji.png" style="position: absolute;left:12px;top:8px;width:20px;height:20px;"></image>
		</view>
		<view class="picturearea">
			<block v-for="(image, index) in imageList" :key="index">
				<view style="position: relative;">
					<!-- todo 预览图片缩放 -->
					<image :src="image" :data-src="image" @tap="previewImage" mode="aspectFill"></image>
					<view
						style="width:15px;height: 15px;font-size: 10px;line-height: 10px;border-bottom-left-radius: 3px;background: rgba(166, 169, 168,0.3);color:#FFFFFF;position: absolute;top:6px;right:0;text-align: center;"
						@click="deleteImg(index)"
					>
						✕
					</view>
				</view>
			</block>
			<view v-show="isAddImage(this.imageList.length)" id="clickToChooseImage" class="addPic" @click="chooseImg">+</view>
			<view v-if="imageList.length == 1 || imageList.length == 4 || imageList.length == 7" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
		</view>

		<view class="menu">
			<mypicker class="major-pick-style" :mode="pickerMode" :range="pickerRange" :value="pickerValue" @change="pickerChange"></mypicker>

			<view style="display:inline-block;height:12px;font-size:12px;font-weight:400;color:rgba(154,154,154,1);">{{ lang.day }}</view>
			<view style="margin-left: 25px;display:inline-block;height:12px;font-size:12px;font-weight:400;color:rgba(154,154,154,1);">{{ lang.votePrompt2 }}</view>
		</view>
		<view v-for="(item, index) in voteOptions" :key="index">
			<view class="optionArea"><input class="optionItem" v-model="voteOptions[index]" placeholder="选项内容" /></view>
			<button @click="deleteOpt(index)">{{ lang.delete }}</button>
		</view>
		<view class="addOptionButton" @tap="addOptionFunc()">{{ lang.addOption }}</view>
		<view class="submitVoteButton" @tap="upload()">{{ lang.submit }}</view>
	</view>
</template>

<script>
import mypicker from '../../components/mypicker.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import { mapState, mapMutations } from 'vuex';

var uploadFlag = false; // 标志文章正在上传，为 true 时 block 该方法
var requestTask;
var uploadTasks = [];

var sourceType = [['camera'], ['album'], ['camera', 'album']];
var sizeType = [['compressed'], ['original'], ['compressed', 'original']];

export default {
	data() {
		return {
			pageTitle: '新建投票',
			showContengEdit: false,

			voteTitle: '',
			voteContent: '',
			votePeriod: '',
			voteOptions: [],
			optionString: '',

			pickerMode: 'submitVote',
			pickerRange: ['1', '2', '3', '5', '7', '10', '14'],
			pickerValue: '3',

			//图片相关？DY看一下不需要的变量删掉。
			imageList: [],
			sourceTypeIndex: 2,
			sourceType: ['拍照', '相册', '拍照或相册'],
			sizeTypeIndex: 0,
			sizeType: ['压缩', '原图', '压缩或原图'],
			countIndex: 8,
			count: [1, 2, 3, 4, 5, 6, 7, 8, 9],

			windowHeight: 0,
			isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮
			navbarHeight: 0 //一次性储存 navbarheight
		};
	},
	components: {
		mypicker,
		uniNavBar
	},
	computed: {
		...mapState(['lang'])
	},
	onLoad() {
		// 一次性储存 navbar 高度
		this.navbarHeight = this.getnavbarHeight().bottom + 5;

		this.userInfo = this.getGlobalUserInfo();
		// 获取屏幕高度
		var that = this;
		uni.getSystemInfo({
			success: function(res) {
				that.windowHeight = res.windowHeight;
			}
		});
	},

	onUnload() {
		(this.imageList = []),
			(this.sourceTypeIndex = 2),
			(this.sourceType = ['拍照', '相册', '拍照或相册']),
			(this.sizeTypeIndex = 0),
			(this.sizeType = ['压缩', '原图', '压缩或原图']),
			(this.countIndex = 8);
	},

	methods: {
		showContent() {
			this.showContengEdit = !this.showContengEdit;
		},

		checkOptionInput(index) {
			if (this.isNull(this.voteOptions[index]) || this.isBlank(this.voteOptions[index])) {
				this.voteOptions.pop();
			}
		},

		addOptionFunc() {
			if (this.voteOptions.length < 10) {
				this.voteOptions.push('');
			} else {
				uni.showToast({
					duration: 1000,
					title: '不能再多啦~嘻嘻~'
				});
			}
		},

		pickerChange(res) {
			console.log(res);
			this.pickerValue = res.newPickerValue;
			// console.log("选择的天数: "+ this.pickerValue);
		},

		combineOptionsToString: function(res) {
			var that = this;
			for (var i = 0; i < that.voteOptions.length; i++) {
				if (this.isNull(that.voteOptions[i]) || this.isBlank(that.voteOptions[i])) {
					uni.showToast({
						icon: 'none',
						title: '选项不能为空',
						duration: 1000
					});
					that.optionString = '';
					return;
				} else {
					if (this.isBlank(that.optionString) || this.isNull(that.optionString)) {
						that.optionString = that.voteOptions[i];
					} else {
						that.optionString = that.optionString + '#' + that.voteOptions[i];
					}
				}
			}
			console.log(that.optionString);
		},

		chooseImg: async function() {
			console.log('choose');
			uni.chooseImage({
				sourceType: sourceType[this.sourceTypeIndex],
				sizeType: sizeType[this.sizeType],
				count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
				success: res => {
					console.log('success choose');
					this.imageList = this.imageList.concat(res.tempFilePaths);
					console.log(res);
				},
				fail: err => {
					console.log('fail');
				}
			});
		},

		previewImage: function(e) {
			var current = e.target.dataset.src;
			console.log(e);
			// console.log(this.imageList);
			uni.previewImage({
				current: current,
				urls: this.imageList
			});
		},

		isAddImage: function() {
			if (this.imageList.length >= 9) {
				return false;
			} else {
				return true;
			}
		},

		deleteImg(index) {
			// console.log(index);
			// console.log(this.imageList[index]);
			this.imageList.splice(index, 1);
		},
		deleteOpt(index) {
			this.voteOptions.splice(index, 1);
		},
		upload(res) {
			var that = this;
			console.log('--upload--');
			console.log(that.voteTitle);
			console.log(that.voteContent);
			console.log(that.pickerValue);
			console.log('----------');

			if (this.isBlank(that.voteTitle) || this.isNull(that.voteTitle)) {
				uni.showToast({
					icon: 'none',
					title: '投票标题不能为空!',
					duration: 1000
				});
				return;
			}

			// if (this.isBlank(that.voteContent)){
			// 	uni.showToast({
			// 		icon:'none',
			// 		title:'投票内容不能为空!',
			// 		duration:1000
			// 	});
			// 	return;
			// }

			if (that.voteOptions.length == 0) {
				uni.showToast({
					icon: 'none',
					title: '至少有一个选项吧!',
					duration: 1000
				});
				return;
			}
			// this.combineOptionsToString();
			// console.log(that.voteOptions);
			// console.log(that.voteOptions.length);

			if (uploadFlag) {
				console.log('正在上传...');
				return;
			}

			uploadFlag = true;
			uni.showLoading({
				title: '正在上传'
			});

			setTimeout(() => {
				if (uploadFlag) {
					uploadFlag = false; // 解锁
					uni.hideLoading();
					uni.showToast({
						title: '网络未知错误',
						icon: 'none',
						duration: 1000
					});
					// 终止上传
					for (let task in uploadTasks) {
						console.log(task);
						task.abort();
					}
					requestTask.abort();
				}
			}, 5000); // 延时五秒,timeout

			setTimeout(() => {
				// 将选项数组合并成字符串
				this.combineOptionsToString();
				console.log(that.voteOptions);
				console.log(that.voteOptions.length);

				requestTask = uni.request({
					url: this.$serverUrl + '/vote/uploadVote',
					method: 'POST',
					data: {
						userId: that.userInfo.id,
						voteTitle: that.voteTitle,
						voteContent: that.voteContent,
						optionContent: that.optionString,
						duration: that.pickerValue
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						that.optionString = ''; // 清空组装options
						if (res.data.status == 200) {
							if (that.imageList.length > 0) {
								const voteId = res.data.data;
								var resCount = 0;
								for (var i = 0; i < that.imageList.length; i++) {
									uploadTasks[i] = uni.uploadFile({
										url: this.$serverUrl + '/vote/uploadVoteImg',
										filePath: this.imageList[i],
										name: 'file',
										formData: {
											userId: that.userInfo.id,
											voteId: voteId,
											order: i
										},
										success: uploadFileRes => {
											resCount++;
											if (resCount == that.imageList.length) {
												that.uploadSuccess();
											}
										}
									});
								}
							}
							console.log('上传成功');
							that.uploadSuccess();
						} else if (res.data.status == 501) {
							that.contentIllegal();
						} else {
							that.uploadFail();
						}
					},
					fail: res => {
						that.uploadFail();
					}
				});
			}, 100);
		},

		uploadSuccess() {
			uploadFlag = false;
			uni.hideLoading();
			uni.$emit('flash'); // 给 index 发送刷新信号
			uni.navigateBack({
				delta: 1
			});
			uni.showToast({
				title: '已提交审核',
				duration: 2000,
				icon: 'success'
			});
		},

		uploadFail() {
			// 上传失败 用户提醒
			uploadFlag = false;
			uni.hideLoading();
			uni.showToast({
				title: '出现未知错误，上传失败',
				duration: 2000,
				icon: 'none'
			});
		},

		contentIllegal() {
			// 内容非法 用户提醒
			uploadFlag = false;
			uni.hideLoading();
			uni.showToast({
				title: '内容涉嫌违规，请联系管理员。',
				duration: 2000,
				icon: 'none'
			});
		}
	}
};
</script>

<style>
page {
	height: 100%;
}

.submitVoteMain {
	width: 698upx;
	margin: auto;
}

.title {
	width: calc(698upx - 36px);
	height: 38px;
	border: 2px solid #fcc041;
	border-radius: 8px;
	padding-left: 12px;
	padding-right: 24px;
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

.addContentButton {
	display: inline-block;
	width: 100px;
	margin: 16px 0 0 0;
	padding: 6px 32px 6px 10px;
	height: 26px;
	font-size: 14px;
	line-height: 14px;
	color: #ffffff;
	background: linear-gradient(90deg, rgba(83, 224, 219, 1) 0%, rgba(72, 177, 233, 1) 100%);
	border-radius: 8px;
}

.addContentButton::before {
	width: 13px;
	content: '';
	background: url(../../static/icon/edit_file.png);
	background-size: 13px 13px;
	background-repeat: no-repeat;
	position: absolute;
	right: 13px;
	height: 13px;
}

.content {
	min-height: 51px;
	max-height: 300px;
	margin-top: 13px;
	width: calc(100% - 12px);
	overflow: scroll;
	padding: 36px 4px 24px;

	border: 2px solid rgba(252, 192, 65, 1);
	border-radius: 8px;
	font-size: 14px;
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
	text-align: center;
	vertical-align: middle;
	color: #888888;
	font-size: 50px;
	font-weight: 200;
	background: #ececec;
}

.optionItem {
	margin-top: 12px;
	width: 634upx;
	height: 14px;
	font-size: 14px;
	line-height: 14px;
	padding: 24upx 31upx;
	border: 1upx solid rgba(188, 188, 188, 1);
	border-radius: 20px;
	min-height: auto;
}

.addOptionButton {
	margin-top: 13px;
	width: 698upx;
	height: 14px;
	line-height: 14px;
	font-size: 14px;
	line-height: 14px;
	color: rgba(255, 255, 255, 1);
	background: rgba(72, 177, 233, 1);
	border-radius: 20px;
	padding: 24upx 0;
	text-align: center;
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}

.submitVoteButton {
	margin-top: 13px;
	width: 698upx;
	height: 14px;
	line-height: 14px;
	font-size: 14px;
	line-height: 14px;
	color: rgba(255, 255, 255, 1);
	background: rgba(252, 192, 65, 1);
	border-radius: 20px;
	padding: 24upx 0;
	text-align: center;
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}
</style>
