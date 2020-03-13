<template>
	<view id="about-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" 
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		:left-text="lang.back"
		:title="lang.about" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}"></view>
		<!--顶部名称部分-->
		<view class="top-picture">
			<!--logo-->
			<view><image class="logo-app" src="../../static/icon/logo_app.png" ></image></view>
			<!--Nottinghome-->
			<view class="company-name">Nottinghome</view>
			<!--版本-->
			<view class="version">{{versionNum}}</view>
		</view>
		
		<!--表单部分-->
		<view class="three-forms">
			
			<!--表单1-->
			<view class="form1">
				<view class="form1-fold">
					<view class="contact-us">{{lang.contact}}</view>
					<image @click="control1" v-if="contentShow1" src="../../static/icon/angle-down.png"></image>
					<image @click="control1" v-if="!contentShow1" src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="form1-expand" v-if="!contentShow1">
					<!-- <view class="subtitle">商务合作</view> -->
					<view class="body">
						“Share beyond Moments”
						Nottinghome 期待与校内各组织社团、学生团队、alumni 创业企业合作。
						合作形式包括但不限于联名活动、官方账号入驻 Nottinghome、资源共享等。
						欢迎更多朋友与我们一起为 UNNC 创造无限可能！
						举报和反馈请联系：
						wx: honggh-
						
						商务合作请联系：
						wx: qian4223x
						email: guetta.xia@outlook.com
						
						更多资讯，请关注官方微信公众号：
						Jumboxtech
					</view>
				</view>
			</view>
			
			<!--表单2-->
			<view class="form2">
				<view class="form2-fold">
					<view class="update-notice">{{lang.updateLog}}</view>
					<image @click="control2" v-if="contentShow2" src="../../static/icon/angle-down.png"></image>
					<image @click="control2" v-if="!contentShow2" src="../../static/icon/angle-up.png"></image>
				</view>
				<view class="form2-expand" v-if="!contentShow2">
					<view class="subtitle">{{versionNum}}</view>
					<view class="body">
						Nottinghome v1.1.0 系列现已全面升级！
						1. 全新UI，更加流畅的交互体验
						2. 新增投票板块，更多有趣的话题等你发现
						3. 优化私信功能，轻松结识圈中大佬
						4. 中英切换，信息编辑... 更多全新功能现已加入
						
						20.3.13 更新：
						1. 修复部分已知 Bug
						2. 优化卡片生成功能
						3. 修复私信功能外观显示问题
					</view>
				</view>
			</view>
			
			<!--表单3-->
			<!-- <view class="form3">
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
			</view> -->
			
		</view>
		
		
		<!--底部图片部分-->
		<view class="bottom-picture"><image src="../../static/BG/signin_bottom.png" mode="aspectFit"></image></view>
	
	</view>
</template>

<script>
import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue";	
import { mapState, mapMutations } from 'vuex';
	
export default {
	components:{
		uniNavBar
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			versionNum: 'v1.1.3',
			contentShow1: true,
			contentShow2: true,
			contentShow3: true,
			isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
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
	line-height: 35px;
	width: 100%;
	height: 35px;
	display: flex;
	align-items:center;
	justify-content: space-between;
}
.form1-fold image {
	width: 30px;
	height: 35px;
	vertical-align: middle;
}
.form2-fold {
	width: 100%;
	height: 35px;
	line-height: 35px;
	display: flex;
	align-items:center;
	justify-content: space-between;
}
.form2-fold image {
	width: 30px;
	height: 35px;
	vertical-align: middle;
}
.contact-us {
	height: 35px;
	display: inline-block;
	font-family: 'Source Han Sans CN';
	font-size: 17px;
	color: #888888;
	opacity: 1;
	margin-left: 16px;
}
.update-notice {
	height: 35px;
	display: inline-block;
	font-family: 'Source Han Sans CN';
	font-size: 17px;
	color: #888888;
	opacity: 1;
	margin-left: 16px;
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
	/* 保证文章正常显示 */
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/* display: -webkit-box; */
	/* -webkit-box-orient: vertical; */
	/* -webkit-line-clamp: 15; */
}
.bottom-picture image {
	width: 100%;
}
</style>
