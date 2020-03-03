<template>
	<view>
		<!-- <view >
			<view style="position: absolute;width:210px;height:240px;left:45px;top:45px;background:#34DF78;"></view>
		</view> -->
		<!-- <posters-layer :postersData="postersData" @success="onSuccessCreatePosters" @error="onPostersError"></posters-layer>
		<image class="shareimage" mode="scaleToFill" :src="posterImg.path"></image> -->
		<!-- <canvas
			class="shareimage"
			canvas-id="canvasdrawer"
			:style="{width: width + 'px', height: height + 'px'}"
		>
		</canvas> -->

		<view>
			<image :src="shareImage" class="shareimage" mode="aspectFit"></image>
			<canvasdrawer :painting="painting" class="canvasdrawer" @getImage="eventGetImage" />
			<button @click="back">返回</button>
			<button @click="eventSave">分享到朋友圈</button>
		</view>
	</view>
</template>
<script>
import postersLayer from './posterslayer.vue';
import { pathToBase64, base64ToPath } from '../../common/image-tools/index.js';
import canvasdrawer from './uniapp-canvas-drawer.vue'
export default {
	props: {
		articleCard: ''
	},

	components: {
		postersLayer,
		canvasdrawer
	},

	data() {
		return {
			postersData: {},
			posterImg: {},

			// width: 320, // 画布宽度
			// height: 320, // 画布长度
			// context: '', // canvas 环境
			
			QrCode: '', // 二维码
			painting: {},
			shareImage: ''
		};
	},

	onReady() {
		if(this.articleCard == null){
			this.articleCard = {
				//测试用例
				articleContent: "章四季豆暗色调骄傲搜的我去教室大家啊章四季豆暗色调骄傲搜的我去教室大家啊章四季豆暗色调骄傲搜的我去教室大家啊so打卡塑料袋价款的祭扫激动isad竞赛就, 暗示大家as哦就记得再试一次暗示大家as的奇偶暗示大家哦",
				articleTitle: '再试一次暗示大家as的奇偶暗示',
				commentNum: 13,
				createDate: 1581531649000,
				dislikeNum: 0,
				faceImg:
					'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574853887894&di=8ac522ba1c324bf91c5c9fef7bd21ee4&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201803%2F24%2F20180324081023_8FVre.jpeg',
				faceImgThumb: null,
				id: '2002131MXGM2XKP0',
				imgList: null,
				isAnonymous: 0,
				isLike: null,
				likeNum: 0,
				nickname: '迪厅少男的梦',
				popularity: 0,
				status: 1,
				tagList: null,
				tags: '',
				userId: 'test1',
				viewNum: 9
			};
		}
		//生成二维码
		// this.getQrcodeUnlimit('pages/detail/detail', 'data=' + this.articleCard.id, 200, true).then(res => {
		// 	if (res == 'suc') {
		// 		this.initPostersConfig();
		// 	}
		// });

		// this.context = uni.createCanvasContext('canvasdrawer', this);
		// this.myPoster();
		
		this.getQrcodeUnlimit('pages/detail/detail', 'data=' + this.articleCard.id, 200, true).then(res => {
			if (res == 'suc') {
				this.eventDraw();
			}
		});
	},

	methods: {
		back(){
			this.$emit("back");
		},
		
		eventDraw () {
		    uni.showLoading({
				title: '绘制分享图片中',
				mask: true
		    })
		    
			this.painting = {
				width: 500,
				height: 500,
				clear: true,
				views: [
					{
						type: 'image',
						width: 500,
						height: 500,
						top: 0,
						left: 0,
						url: '/static/BG/1-blue.png'
					},
					{
						type: 'rect',
						width: 324,
						height: 366,
						top: 67,
						left: 88,
						background: '#FFFFFF',
						radius: 20
					},
					{
						//头像
						type: 'image',
						width: 37,
						height: 37,
						top: 67+23,
						left: 88+31,
						radius: 18,
						url: this.articleCard.faceImg
					},
					
					{
						type: 'text',
						content: this.articleCard.nickname,
						fontSize: 17,
						color: '#9A9A9A',
						textAlign: 'left',
						top: 67+31,
						left: 88+82,
						bolder: false
					},
					{	//标题
						type: 'text',
						content: this.articleCard.articleTitle,
						fontSize: 22,
						lineHeight: 25,
						color: '#353535',
						textAlign: 'left',
						top: 67+80,
						left: 88+29,
						width: 266,
						MaxLineNumber: 1,
						breakWord: true,
						bolder: true
					},
					{	//内容
						type: 'text',
						content: this.articleCard.articleContent,
						fontSize: 15,
						lineHeight: 22,
						color: '#888888',
						textAlign: 'left',
						top: 67+128,
						left: 88+29,
						width: 266,
						MaxLineNumber: 5,
						breakWord: true,
						bolder: false
					},
					{	//分割线
						type: 'text',
						content: "- - - - - - - - - - - - - - - - - - - -",
						fontSize: 15,
						lineHeight: 21,
						color: '#B1B1B1',
						textAlign: 'left',
						top: 67+250,
						left: 88+27,
						width: 270,
						MaxLineNumber: 1,
						breakWord: true,
						bolder: false
					},
					{
						//二维码
						type: 'image',
						width: 70,
						height: 70,
						top: 67+269,
						left: 88+27,
						url: this.QrCode
					},
					{
						type: 'text',
						content: '我在Nottinghome的分享扫码查看完整内容',
						fontSize: 15,
						lineHeight: 25,
						color: '#888888',
						textAlign: 'right',
						top: 67+282,
						left: 500-88-28,
						width: 150,
						MaxLineNumber: 2,
						breakWord: true,
						bolder: false
					}
				]
			}
		},
		eventSave () {
		    uni.saveImageToPhotosAlbum({
				filePath: this.shareImage,
				success (res) {
					uni.showToast({
						title: '保存图片成功',
						icon: 'success',
						duration: 2000
					})
				}
			})
		},
		eventGetImage(event) {
			uni.hideLoading()
			console.log(event)
			const { tempFilePath, errMsg } = event
			// const result = event.detail.__args__
			// const tempFilePath = result[0].tempFilePath
			// const errMsg = result[0].errMsg
			if (errMsg === 'canvasdrawer:ok') {
				this.shareImage = tempFilePath
				this.painting = {}
			}
		},

		// async myPoster() {
		// 	if (this.isNull(this.articleCard)) {
		// 		console.log('没有文章数据，无法生成画布');
		// 		return;
		// 	}
		// 	var prompt = '我推荐了 Nottinghome 的一篇文章，扫码查看完整内容';
		// 	var bgUrl = '';
		// 	// 加载背景
		// 	this.context.drawImage('/static/BG/1-blue.png', 0, 0, 500, 500);

		// 	this.context.setFontSize(14);
		// 	this.context.setFillStyle('#FEEDBB');
		// 	this.context.fillText(this.articleCard.nickname, 60, 50); //作者昵称
		// 	this.context.fillText(this.articleCard.articleTitle, 100, 100); //文章标题
		// 	this.context.setFontSize(18);
		// 	this.context.setFillStyle('#FEEDBB');
		// 	this.context.fillText(this.articleCard.articleContent, 80, 130); //文章内容
		// 	this.context.fillText(prompt, 100, 200); //分享提示语
		// 	// 作者获取头像
		// 	var that = this;
		// 	await new Promise((resolve, reject) => {
		// 		uni.getImageInfo({
		// 			//获取图片信息
		// 			src: that.articleCard.faceImg,
		// 			success(res) {
		// 				// console.log(res.path);
		// 				that.context.drawImage(res.path, 10, 50, 40, 40); // 设置头像位置
		// 				resolve('suc');
		// 			},
		// 			fail(error) {
		// 				console.log(error);
		// 				reject('err');
		// 			}
		// 		});
		// 	});

		// 	console.log('开始画二维码');
		// 	// 对数据字符串化并转码，防止特殊字符影响传参
		// 	// console.log(encodeData)
		// 	await this.getQrcodeUnlimit('pages/detail/detail', 'data=' + this.articleCard.id, 200, true);
		// 	this.context.drawImage(this.QrCode, 25, 200, 75, 75);
		// 	console.log('结束画二维码');
		// 	this.context.draw(); //生成图像
		// },

		/**
		 * 对应微信小程序生成二维码的场景B
		 * @param {Object} page 跳转页面，默认为空
		 * @param {Object} scene 携带参数
		 * @param {Object} width 二维码尺寸
		 * @param {Object} isHyaline 底色是否透明
		 */
		getQrcodeUnlimit(page, scene, width, isHyaline) {
			var that = this;
			return new Promise((resolve, reject) => {
				uni.request({
					url: this.$serverUrl + '/wechat/getQrcodeUnlimit',
					method: 'POST',
					data: {
						page: page,
						scene: scene,
						width: width,
						is_hyaline: isHyaline
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						var QrCode = 'data:image/png;base64,' + res.data.data;
						that.QrCode = QrCode;
						resolve('suc');
						// base64ToPath(QrCode)
						// 	.then(path => {
						// 		console.log(path)
						// 		that.QrCode = path
						// 	})
						// 	.catch(error => {
						// 		console.error(error)
						// 	})
					},
					fail: res => {
						reject('err');
					}
				});
			});
		}
	}
};
</script>

<style>
/* .posters-layer {
	position: fixed;
	background-color: #007aff;
	top: 200rpx;
	left: 100rpx;
	z-index: 30;
} */

.shareimage {
	width: 320px;
	height: 320px;
	position: fixed;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	margin-left: auto;
	margin-right: auto;
	margin-top: auto;
	margin-bottom: auto;
}
</style>
