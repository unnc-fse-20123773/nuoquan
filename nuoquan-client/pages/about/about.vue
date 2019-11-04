<template>
	<view>
			<view><image class="logo_app" src="../../static/icon/logo_app.png" mode=""></image></view>
			<view class="company_name">Nottinghome</view>
			<view class="version">1.0.3</view>
			<view class="text1">
				<view class="title-line1">
				    <view class="title1">
					联系我们
					</view>
					<image @click="control1" v-if="contentShow1"      src="../../static/icon/angle-down.png"></image>
					<image @click="control1" v-if="!contentShow1"      src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="more-content" v-if="!contentShow1">
					<view class="second-title">副标题副标题副标题副标题副标题</view>
					<view class="content">正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文最多六行剩余滚动</view>				
				</view>
			</view>
			<view class="text2">
				<view class="title-line2">
				    <view class="title2">
					更新公告
					</view>
					<image @click="control2" v-if="contentShow2"      src="../../static/icon/angle-down.png"></image>
					<image @click="control2" v-if="!contentShow2"      src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="more-content" v-if="!contentShow2">
					<view class="second-title">副标题副标题副标题副标题副标题</view>
					<view class="content">正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文最多六行剩余滚动</view>
				</view>
			</view>
			<view class="text3">
				<view class="title-line3">
				    <view class="title3">
					打赏
					</view>
					<image @click="control3" v-if="contentShow3"      src="../../static/icon/angle-down.png"></image>
					<image @click="control3" v-if="!contentShow3"      src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="more-content" v-if="!contentShow3">
					<view class="second-title">副标题副标题副标题副标题副标题</view>
					<view class="content">正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					正文正文正文正文最多六行剩余滚动</view>
				</view>
			</view>
			<view class="picture"><image src="../../static/BG/signin_bottom.png"></image></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				contentShow1:true,
				contentShow2:true,
				contentShow3:true,
			}
		},
		methods: {
			control1(){
				this.contentShow1 = ! this.contentShow1;
			},
			control2(){
				this.contentShow2 = ! this.contentShow2;
			},
			control3(){
				this.contentShow3 = ! this.contentShow3;
			}
		}
	}
</script>

<style>
.logo_app{
	display:block;
	border:1px;
	border-style:dashed;
	margin:auto;
	margin-top:40px;
	height:72px;
	width: 72px;	
}
.company_name{
	margin-top:12px;
	text-align: center;
	font-family:"Source Han Sans CN";
	font-size:17px;
	color:#888888;
	opacity:1;
	//line-height: 30px;
	
}
.version{
	text-align: center;
	font-family:"Source Han Sans CN";
	font-size: 17px;
	color:#888888;
	opacity:1;
	padding-bottom: 30px;
}
.signin_bottom{
	display: block;
	border-style:dashed;
	border:1px;
	width:375px;
	height:191px;
	margin-bottom: 0px;
}
.text1{
	line-height:35px;
	position: relative;
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0,0,0,0.16);
	margin-top: 5px;
	margin-left: 20px;
	margin-right: 20px;
	margin-bottom: 12px;
}
.text2{
	line-height:35px;
	position:relative;
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0,0,0,0.16);
	margin-left: 20px;
	margin-right: 20px;
	margin-bottom: 12px;
}
.text3{
	line-height:35px;
	position:relative;
	border-radius: 10px;
	box-shadow: 0px 0px 3px rgba(0,0,0,0.16);
	margin-left: 20px;
	margin-right: 20px;
	margin-bottom: 12px;
}
.title-line1{
	
	width:300px;	
}
.title-line1 image{
	width:30px;
	height: 30px;
	position:absolute;
	//top:20px;
	right:10px;
}
.title-line2{
	
	width:300px;
}
.title-line2 image{
	width:30px;
	height: 30px;
	position:absolute;
	//top:0px;
	right:10px;
}
.title-line3{
	
	width:300px;
}
.title-line3 image{
	width:30px;
	height: 30px;
	position:absolute;
	//top:20px;
	right:10px;
}
.title1{
	h1eight:35px;
	width:260px;
	display: inline-block;
	font-family:"Source Han Sans CN";
	font-size: 17px;
	color:#888888;
	opacity:1;
	margin-left: 10px;
}
.title2{
	height:35px;
	width:260px;
	display: inline-block;
	font-family:"Source Han Sans CN";
	font-size: 17px;
	color:#888888;
	opacity:1;
	margin-left: 10px;
}
.title3{
	height:35px;
	width:260px;
	display: inline-block;
	font-family:"Source Han Sans CN";
	font-size: 17px;
	color:#888888;
	opacity:1;
	margin-left: 10px;
}
.second-title{
	font-family:"Source Han Sans CN";
	font-size:14px;
	font-weight: 600;
	color:#353535;
	margin-left: 10px;
	opacity:1;
}
.content{
	font-family:"Source Han Sans CN";
	font-size:14px;
	color:#353535;
	margin-left: 10px;
	opacity:1;
	line-height: 20px;
	max-height: 120px;
	overflow: scroll;
}
.picture{
	border:1px;
	border-style: dashed;
	margin: 70px auto 10px;
}
.picture image{
	margin: 60px auto 60px;
	height:170px;
	display:block;
}
</style>
