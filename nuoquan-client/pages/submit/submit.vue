<template>
	<viwe>
		<view class="submit">
			发表
		</view>
		<view class="submitMain">
			<input class="title"/>
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
<style scoped>
	.submit {
		width: 55px;
		height: 26px;
		line-height: 26px;
		margin-left: 280px;
		margin-top: 22px;
		border: solid 1px #FDD041;
		border-radius: 5px;
		font-weight: bold;
		font-size: 15px;
		color: #FDD041;
		text-align: center;
	}
	.submitMain{
		width:606upx;
		padding:38upx 72upx;
		border-top-left-radius: 5px;
		border-top-right-radius: 5px;
		
	}
</style>
