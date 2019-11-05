<template>
	<view id="about-container">
		<!--顶部名称部分-->
		<view class="top-picture">
			<!--logo-->
			<view><image class="logo-app" src="../../static/icon/logo_app.png" ></image></view>
			<!--Nottinghome-->
			<view class="company-name">Nottinghome</view>
			<!--版本-->
			<view class="version">1.0.3</view>
		</view>
		
		<!--表单部分-->
		<view class="three-forms">
			
			<!--表单1-->
			<view class="form1">
				<view class="form1-fold">
					<view class="contact-us">联系我们</view>
					<image @click="control1" v-if="contentShow1" src="../../static/icon/angle-down.png"></image>
					<image @click="control1" v-if="!contentShow1" src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="form1-expand" v-if="!contentShow1">
					<view class="subtitle">副标题副标题副标题副标题副标题</view>
					<view class="body">
						正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文最多六行剩余滚动正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文最多六行剩余滚动
					</view>
				</view>
			</view>
			
			<!--表单2-->
			<view class="form2">
				<view class="form2-fold">
					<view class="update-notice">更新公告</view>
					<image @click="control2" v-if="contentShow2" src="../../static/icon/angle-down.png"></image>
					<image @click="control2" v-if="!contentShow2" src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="form2-expand" v-if="!contentShow2">
					<view class="subtitle">副标题副标题副标题副标题副标题</view>
					<view class="body">
						正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文最多六行剩余滚动
					</view>
				</view>
			</view>
			
			<!--表单3-->
			<view class="form3">
				<view class="form3-fold">
					<view class="tipping">打赏</view>
					<image @click="control3" v-if="contentShow3" src="../../static/icon/angle-down.png"></image>
					<image @click="control3" v-if="!contentShow3" src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="form3-expand" v-if="!contentShow3">
					<view class="subtitle">副标题副标题副标题副标题副标题</view>
					<view class="body">
						正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文 正文正文正文正文最多六行剩余滚动
					</view>
				</view>
			</view>
			
		</view>
		
		
		<!--底部图片部分-->
		<view class="bottom-picture"><image src="../../static/BG/signin_bottom.png" mode="aspectFit"></image></view>
	
	</view>
</template>

<script>
export default {
	data() {
		return {
			contentShow1: true,
			contentShow2: true,
			contentShow3: true
		};
	},
	methods: {
		control1() {
			this.contentShow1 = !this.contentShow1;
		},
		control2() {
			this.contentShow2 = !this.contentShow2;
		},
		control3() {
			this.contentShow3 = !this.contentShow3;
		}
	}
};
</script>

<style>
page {
	width: 100%;
	height: 100%;
	overflow: scroll;
}
#about-container{
	width:100%;
	height:100%;
}
.top-picture{
	height:30%;
}
.three-forms{
	min-height:30%;
}
.bottom-picture {
	height:40%;
}
.logo-app {
	display: block;
	margin: auto;
	margin-top: 80upx;
	height: 144upx;
	width: 144upx;
}
.company-name {
	margin-top: 16upx;
	text-align: center;
	font-family: 'Source Han Sans CN';
	font-size: 34upx;
	color: #888888;
	opacity: 1;
}
.version {
	text-align: center;
	font-family: 'Source Han Sans CN';
	font-size: 34upx;
	color: #888888;
	opacity: 1;
	margin-top: 10upx;
}
.form1 {
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
	margin-top: 5px;
	margin-left: 20px;
	margin-right: 30px;
	margin-bottom: 12px;
}
.form2 {
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
	margin-left: 20px;
	margin-right: 30px;
	margin-bottom: 12px;
}
.form3 {
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
	margin-left: 20px;
	margin-right: 30px;
	margin-bottom: 12px;
}
.form1-fold {
	width: 300px;
	height: 35px;
}
.form1-fold image {
	width: 30px;
	height: 35px;
	vertical-align: middle;
}
.form2-fold {
	width: 300px;
	height: 35px;
}
.form2-fold image {
	width: 30px;
	height: 35px;
	vertical-align: middle;
}
.form3-fold {
	width: 300px;
	height: 35px;
}
.form3-fold image {
	width: 30px;
	height: 35px;
	vertical-align: middle;
}
.contact-us {
	height: 35px;
	width: 260px;
	display: inline-block;
	font-family: 'Source Han Sans CN';
	font-size: 17px;
	color: #888888;
	opacity: 1;
	margin-left: 10px;
}
.update-notice {
	height: 35px;
	width: 260px;
	display: inline-block;
	font-family: 'Source Han Sans CN';
	font-size: 17px;
	color: #888888;
	opacity: 1;
	margin-left: 10px;
}
.tipping {
	height: 35px;
	width: 260px;
	display: inline-block;
	font-family: 'Source Han Sans CN';
	font-size: 17px;
	color: #888888;
	opacity: 1;
	margin-left: 10px;
}
.subtitle {
	font-family: 'Source Han Sans CN';
	font-size: 14px;
	font-weight: 600;
	color: #353535;
	margin-left: 10px;
	margin-bottom: 10px;
	opacity: 1;
}
.body {
	font-family: 'Source Han Sans CN';
	font-size: 14px;
	color: #353535;
	margin-left: 10px;
	opacity: 1;
	line-height: 20px;
	max-height: 120px;
	overflow: scroll;
}
.bottom-picture image {
	width: 100%;
}
</style>
