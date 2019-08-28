<template>
	<view id="profile-container">
		<view id="yellow-box">
		</view>
		<view class="profile-basicinfo-card column_center">
			<image class="profileTouxiang" mode="aspectFill" :src="userInfo.faceImg"></image>
			<view class="profileText-box">
				<text class="left-profileText1">昵称</text>
				<text class="right-profileText1" v-if="userInfo.nickname!=null" >{{userInfo.nickname}}</text>
				<text class="right-profileText1" v-else >待设置</text>
				<text class="left-profileText2">性别</text>
				<text class="right-profileText2" v-if="userInfo.gender!=null">{{userInfo.gender}}</text>
				<text class="right-profileText2" v-else >待设置</text>
			</view>
		</view>
		<view class="profile-moreinfo-card super_center">
			<view class="profilemoreText-box ">
				<text class="left-profileText1">邮箱</text>
				<text class="right-profileText1" v-if="userInfo.email!=null">{{userInfo.email}}</text>
				<text class="right-profileText1" v-else >待设置</text>
				<text class="left-profileText2">年级/专业</text>
				<text class="right-profileText2">Y4 PDM</text>
				<text class="left-profileText3">电话</text>
				<text class="right-profileText3">18000000000</text>
			</view>
		</view>
		<view class="editprofile super_center">
			<text class="editprofile-text">修改资料</text>
		</view>
	</view>
</template>
<script>
	var userInfo;
	export default {
		data() {
			return {
				userInfo: '',
			}
		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "个人信息"
			});
			
			userInfo = this.getGlobalUserInfo();
			this.userInfo = userInfo;
			console.log(userInfo);
		},
		methods: {

		}
	}
</script>

<style>
	page {
		height: 100%;
		width: 100%;
	}

	#profile-container {
		position: fixed;
		width: 100%;
		height: 100%;
		background-color: #F3F3F3;
	}

	#yellow-box {
		position: fixed;
		top: 0;
		height: 16%;
		width: 100%;
		background-color: #FFCF3C;
	}

	.profile-basicinfo-card {
		position: fixed;
		top: 8%;
		left: 7%;
		width: 86%;
		height: 300upx;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}

	.profileTouxiang {
		margin-left: 6%;
		width: 160upx;
		height: 160upx;
		border-radius: 120px;
		display: inline-block;
		vertical-align: middle;
	}

	.profileText-box {
		position: relative;
		height: 180upx;
		width: 55%;
		margin-left: 6%;
	}

	.left-profileText1 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		font-weight: 550;
	}

	.right-profileText1 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 46upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}
	
	.left-profileText2 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		margin-top: 110upx;
		font-weight: 550;
	}
	
	.right-profileText2 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 146upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.left-profileText3 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		margin-top: 210upx;
		font-weight: 550;
	}
	
	.right-profileText3 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 246upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}
	
	.profile-moreinfo-card{
		height: 22%;
		margin-left: 7%;
		margin-top: 61%;
		width: 86%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}
	
	.profilemoreText-box{
		position: relative;
		height: 90%;
		width: 80%;
	}
	
	.editprofile{
		margin-top: 8%;
		margin-left: 7%;
		width: 86%;
		height: 5%;
		background-color: #FFCF3C;
		border-radius: 20upx;
	}
	
	.editprofile-text{
		color: white;
		font-size: 36upx;
		font-weight: 400;
	}
</style>
