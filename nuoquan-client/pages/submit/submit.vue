<template>
	<viwe>
		<view style="height:45px;width:100%;">
			<view class="submit">
				发 表
			</view>
		</view>

		<view class="submitMain">
			<input class="title" placeholder="  标题" />
			<view class="tagsArea">
				<view class="tag">
					后街
				</view>
				<view class="tag">
					二手交易
				</view>

				<view class="addTag">
					+ 添加标签
				</view>
			</view>
			<textarea placeholder="滚刀肉的CSS" class="content"></textarea>
			<view class="picturearea">
				<image src="../../static/0001/pic1.jpg"></image>
				<image src="../../static/0001/pic1.jpg"></image>
				<image src="../../static/0001/pic1.jpg"></image>
				<image src="../../static/0001/pic1.jpg"></image>
				<view class="addPic">+</view>
				<view class="placeHolderForPic"></view>
			</view>

		</view>
	</viwe>
</template>

<script>
	export default {
		data() {
			return {
				contentImage: "../../static/plus-circle.png",
				imageCount: 3
			}
		},
		onLoad() {

		},
		methods: {
			formSubmit: function(e) {
				console.log('form发生了submit事件，携带数据为：' + JSON.stringify(e.detail.value))
			},
			changeImage: function() {
				var that = this;
				uni.chooseImage({
					count: 3,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: function(res) {
						var tempFilePaths = res.tempFilePaths;
						console.log(JSON.stringify(res.tempFilePaths));

						uni.showLoading({
							title: '上传中...'
						})
						var serverUrl = that.$serverUrl;
						uni.uploadFile({
							url: serverUrl + '/user/uploadFace?userId=' + 'aasa',
							filePath: tempFilePaths[0],
							name: 'file',
							header: {
								'content-type': 'application/json' //默认值
							},
							success: function(res) {
								var data = JSON.parse(res.data);
								console.log(data);
								uni.hideLoading();
								uni.showToast({
									title: '上传成功！～～',
									icon: "success"
								});

								var imageUrl = data.data;
								// console.log(serverUrl);
								// console.log(imageUrl);
								that.contentImage = serverUrl + imageUrl;
								console.log(serverUrl + imageUrl);
							}
						});

					}
				});
			},


		}
	}
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
		border-top-left-radius: 5px;
		border-top-right-radius: 5px;
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
		padding-right: 27px;
		padding-left: 12px;
		border: solid 2px #FE5F55;
		border-radius: 14px;
	}

	.content {
		min-height: 136px;
		background: #F4F1E9;
		margin-top: 13px;
		width:100%;
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
		color:#BEBCB5;
		font-size: 70px;
		font-weight: 200;
	}
		
	.placeHolderForPic{
		width: 190upx;
		height: 190upx;
	}
</style>
