<template>
    <view>
        <!-- <posters-layer
            :postersData="postersData"
            @success="onSuccessCreatePosters"
            @error="onPostersError">
        </posters-layer> -->
		
		<canvas
			canvas-id="canvasdrawer"
			:style="{width: width + 'px', height: height + 'px'}"
			class="posters-layer">
		</canvas>
		<!-- <image :src="testImg"></image> -->
		
	</view>
</template>
<script>
import postersLayer from './posterslayer.vue';
import { pathToBase64, base64ToPath } from '../../common/image-tools/index.js'

export default {
	props: {
		articleCard: "",
	},
	
	components: {
	    postersLayer
	},

    data() {
        return {
            // postersData: {},
            // posterImg: {},
			
			width: 300, // 画布宽度
			height: 300, // 画布长度
			context: '', // canvas 环境
			
			article: '',
			
			QrCode: '' ,// 二维码
			testImg: '',
			
        };
    },
	
	onReady() {
		this.context = uni.createCanvasContext('canvasdrawer', this)
		this.myPoster();
	},
	
    methods: {
		async myPoster(){
			/* 测试案例，一个笑脸 */
			// context.setStrokeStyle("#00ff00")
			// context.setLineWidth(5)
			// context.rect(0, 0, 200, 200)
			// context.stroke()
			// context.setStrokeStyle("#ff0000")
			// context.setLineWidth(2)
			// context.moveTo(160, 100)
			// context.arc(100, 100, 60, 0, 2 * Math.PI, true)
			// context.moveTo(140, 100)
			// context.arc(100, 100, 40, 0, Math.PI, false)
			// context.moveTo(85, 80)
			// context.arc(80, 80, 5, 0, 2 * Math.PI, true)
			// context.moveTo(125, 80)
			// context.arc(120, 80, 5, 0, 2 * Math.PI, true)
			// context.stroke()
			// context.draw()
			
			if(this.isNull(this.articleCard)){
				console.log("没有文章数据，无法生成画布")
				return;
			}
			
			var prompt = "我推荐了 Nottinghome 的一篇文章，扫码查看完整内容"
			
			this.context.setFontSize(14);
			this.context.setFillStyle("#FEEDBB");
			this.context.fillText(this.articleCard.nickname, 60, 50); //作者昵称
			this.context.fillText(this.articleCard.articleTitle, 100, 100); //文章标题
			this.context.fillText(this.articleCard.articleContent, 80, 130); //文章内容
			this.context.fillText(prompt, 100, 200); //分享提示语
			// 作者获取头像
			var that = this;
			await new Promise((resolve, reject)=>{
				uni.getImageInfo({ //获取图片信息
					src: that.articleCard.faceImg,
					success(res) {
						// console.log(res.path);
						that.context.drawImage(res.path, 10, 50, 40, 40); // 设置头像位置
						resolve('suc')
					},
					fail(error) {
						console.log(error);
						reject('err')
					}
				})
			})

			console.log("开始画二维码")
			// 对数据字符串化并转码，防止特殊字符影响传参
			// console.log(encodeData)
			await this.getQrcodeUnlimit("pages/detail/detail", "data="+this.articleCard.id, 200, true);
			this.context.drawImage(this.QrCode, 25, 200, 75, 75);
			console.log("结束画二维码")
			this.context.draw() //生成图像
		},
		
		/**
		 * 对应微信小程序生成二维码的场景B
		 * @param {Object} page 跳转页面，默认为空
		 * @param {Object} scene 携带参数
		 * @param {Object} width 二维码尺寸
		 * @param {Object} isHyaline 底色是否透明
		 */
		getQrcodeUnlimit(page, scene, width, isHyaline){
			var that = this;
			return new Promise((resolve, reject)=>{
				uni.request({
					url: this.$serverUrl + '/wechat/getQrcodeUnlimit',
					method: 'POST',
					data: {
						page: page,
						scene: scene,
						width: width,
						is_hyaline: isHyaline,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res)
						var QrCode = "data:image/png;base64,"+res.data.data;
						that.QrCode = QrCode;
						resolve('suc')
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
						reject('err')
					}
				});
			})
		},
		
		canvasIdErrorCallback: function (e) {
			console.error(e.detail.errMsg)
		}
		
		
  //       initPostersConfig() {
  //           const config = {
  //               clear: true,
  //               width: 660,
  //               height: 850,
  //               background: '#ffffff',
  //               views: [
  //               {
  //                   type: 'image',
  //                   width: 660,
  //                   height: 660,
  //                   top: 0,
  //                   left: 0,
  //                   // 封面图，测试的时候填上
  //                   url: '/static/BG/detailBG.png'
  //               },
  //               {
  //                   type: 'text',
  //                   width: 400,
  //                   height: 50,
  //                   left: 20,
  //                   top: 680,
  //                   fontSize: 30,
  //                   lineHeight: 40,
  //                   bolder: true,
  //                   breakWord: true,
  //                   content: '        Apple/苹果 iPhone XR 移动联通电信全网通版 苹果xr iphonexr 苹果xr手机 iphone xr',
  //                   MaxLineNumber: 2
  //               },
  //               {
  //                   type: 'rect',
  //                   width: 70,
  //                   height: 34,
  //                   left: 20,
  //                   top: 684,
  //                   background: '#ff4201',
  //                   radius: 8
  //               },
  //               {
  //                   type: 'text',
  //                   width: 400,
  //                   height: 50,
  //                   left: 20,
  //                   top: 690,
  //                   fontSize: 24,
  //                   lineHeight: 40,
  //                   bolder: true,
  //                   breakWord: true,
  //                   content: ' 活动',
  //                   color: '#ffffff',
  //                   MaxLineNumber: 2
  //               },
  //               {
  //                   type: 'text',
  //                   width: 400,
  //                   left: 20,
  //                   top: 770,
  //                   fontSize: 54,
  //                   bolder: true,
  //                   breakWord: true,
  //                   content: '￥0.0',
  //                   color: '#F40',
  //                   MaxLineNumber: 2
  //               },
  //               {
  //                   type: 'image',
  //                   width: 140,
  //                   height: 140,
  //                   top: 680,
  //                   left: 500,
  //                   // 二维码图片路径，测试的时候填上
  //                   url: '/static/BG/detailBG.png'
  //               },
  //               ]
  //           };
  //           this.postersData = config;
  //       },
  //       onSuccessCreatePosters(res) {
  //           console.log(res);
  //           this.posterImg = res;
  //       },
  //       onPostersError(res) {}
		
    }
}
</script>

<style>
.posters-layer {
  position: fixed;
  background-color: #007AFF;
  top: 200rpx;
  left: 100rpx;
}
</style>