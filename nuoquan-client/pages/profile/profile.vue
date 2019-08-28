<template>
	<view id="profile-container">
		<view id="yellow-box">
		</view>
		<view class="profile-basicinfo-card column_center">
			<image class="profileTouxiang" mode="aspectFill" :src="userInfo.faceImg"></image>
			<!-- 一般状态 -->
			<view class="profileText-box" v-if="isEdit == false">
				<text class="left-profileText1 ">昵称</text>
				<text class="right-profileText1" v-if="userInfo.nickname!=null ">{{userInfo.nickname}}</text>
				<text class="right-profileText1" v-else>待设置</text>
				<text class="left-profileText2">性别</text>
				<text class="right-profileText2" v-if="userInfo.gender!=null">{{userInfo.gender}}</text>
				<text class="right-profileText2" v-else>待设置</text>
			</view>
			<!-- 修改时状态 -->
			<view class="profileText-box" v-if="isEdit == true">
				<text class="left-profileText1 ">昵称</text>
				<input class="right-profileText1-1" style="text-align:right;min-height: 1upx;height:26px;" maxlength="16" :value="userInfo.nickname" />
				<view class="input-border"></view>
				<text class="left-profileText2">性别</text>
				<view class="genderPicker">
					<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">
						<text class="gender-text">男</text></view>
					<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">
						<text class="gender-text">女</text></view>
					<view :class="[gender == 2 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(2)">
						<text class="gender-text">其他</text></view>
				</view>
			</view>
		</view>

		<view class="profile-moreinfo-card super_center">
			<!-- 一般状态 -->
			<view class="profilemoreText-box" v-if="isEdit == false">
				<text class="left-profileText1">邮箱</text>
				<text class="right-profileText1" v-if="userInfo.email!=null">{{userInfo.email}}</text>
				<text class="right-profileText1" v-else>待设置</text>
				<text class="left-profileText2">毕业年份/专业</text>
				<text class="right-profileText2">Y4 PDM</text>
				<text class="left-profileText3">电话</text>
				<text class="right-profileText3">18000000000</text>
			</view>

			<!-- 修改时状态 -->
			<view class="profilemoreText-box" v-if="isEdit == true">
				<text class="left-profileText1">邮箱</text>
				<input class="right-profileText1-1" style="width: 260px;text-align:right;min-height: 1upx;height:26px;" maxlength="32"
				 :value="userInfo.email" />
				<view class="input-border2"></view>
				<text class="left-profileText2">毕业年份/专业</text>

				<view class="yearPicker">
					<view class="yearPicker-button" v-if="picker == false" @click="pickerChanger(picker)">
						<text class="yearPicker-text">2020</text>
						<view class="year-pointer">
							<image class="year-pointerIcon" src="../../static/icon/angle-down/angle-down%20%20888888.png" mode="scaleToFill"></image>
						</view>
					</view>
					<view class="yearPicker-button" v-if="picker == false" @click="pickerChanger(picker)">
						<text class="gender-text">PDM</text></view>
				</view>
				<picker class="pick-style" v-if="picker == true"></picker>

				<text class="left-profileText3">电话</text>
				<text class="right-profileText3">18000000000</text>
			</view>

		</view>
		<view style="display: flex; height: 5%;">
			<view class="editProfile super_center" v-if="isEdit == false" @click="editProfile(isEdit)">
				<text class="editProfile-text">编辑信息</text>
			</view>
			<view class="editProfile-edit super_center" v-if="isEdit == true" @click="submit">
				<text class="editProfile-text">完成修改</text>
			</view>
			<view class="editProfile-cancle super_center" v-if="isEdit == true" @click="cancle">
				<text class="editProfile-cancletext">取消</text>
			</view>
		</view>
	</view>
</template>
<script>
	import picker from '../../components/picker.vue';

	var userInfo;
	export default {
		data() {
			return {
				isEdit: false,
				userInfo: '',
				gender: -1,
				picker: false,
			}
		},

		components: {
			picker,
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
			genderChanger: function(gender) {
				if (this.gender == gender) {
					this.gender = -1;
					console.log(gender);
				} else {
					this.gender = gender;
				}
			},

			pickerChanger: function(picker) {
				if (this.picker == false) {
					this.picker = true;
				} else {
					this.picker = false;
				}
				console.log(this.picker);
			},

			editProfile: function(isEdit) {
				if (isEdit == false) {
					this.isEdit = true;
				} else if (isEdit == true) {
					this.isEdit = false;
				}
				console.log(this.isEdit);
			},

			submit: function() {
				// 提交表单操作

				// 完成修改，更改 isEdit 为 false
				this.editProfile(this.isEdit);
			},

			cancle: function() {
				// 取消修改操作

				this.editProfile(this.isEdit);
			}
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

	/* 在这里修改输入框样式 */
	.right-profileText1-1 {
		color: #888888;
		position: absolute;
		margin-top: 40upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.input-border {
		position: absolute;
		margin-top: 86upx;
		right: 0;
		border-bottom: 1upx solid #C0C0C0;
		width: 80%;
	}

	.input-border2 {
		position: absolute;
		margin-top: 86upx;
		right: 0;
		border-bottom: 1upx solid #C0C0C0;
		width: 260px;
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

	.profile-moreinfo-card {
		height: 30%;
		margin-left: 7%;
		margin-top: 61%;
		width: 86%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}

	.profilemoreText-box {
		position: relative;
		height: 90%;
		width: 80%;
	}

	.editProfile {
		margin-top: 8%;
		margin-left: 7%;
		width: 86%;
		height: 100%;
		background-color: #FFCF3C;
		border-radius: 20upx;
	}

	.editProfile-edit {
		margin-top: 8%;
		margin-left: 7%;
		width: 58%;
		height: 100%;
		background-color: #FFCF3C;
		border-radius: 20upx;
	}

	.editProfile-text {
		color: white;
		font-size: 36upx;
		font-weight: 400;
	}

	.editProfile-cancle {
		margin-top: 8%;
		margin-left: 7%;
		width: 21%;
		height: 100%;
		background-color: #dbdbdb;
		border-radius: 20upx;
	}

	.editProfile-cancletext {
		color: white;
		font-size: 36upx;
		font-weight: 400;
	}

	.genderPicker {
		position: absolute;
		margin-top: 156upx;
		right: -12upx;
		width: 94%;
		height: 30%;
		display: flex;
		justify-content: space-around;
	}

	.genderPicker-button {
		height: 100%;
		width: 96upx;
		border-radius: 8upx;
		background-color: #dedede;
		display: flex;
		justify-content: center;
		font-size: medium;
		font-weight: 550;
		align-item: center;
	}

	.genderPicker-buttonclick {
		height: 100%;
		width: 96upx;
		border-radius: 8upx;
		background-color: #FDD041;
		justify-content: center;
		display: flex;
		align-item: center;
	}

	.gender-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 550;
	}

	.pick-style {
		position: absolute;
		z-index: 999;
		right: 10px;
		margin-top: 146upx;
		top: -130upx;
		right: 126upx;
	}

	.yearPicker {
		position: absolute;
		margin-top: 156upx;
		right: -12upx;
		width: 48%;
		/* 在此更改按钮高度 */
		height: 16%;
		display: flex;
		justify-content: space-between;
	}

	.yearPicker-button {
		height: 100%;
		width: 116upx;
		border-radius: 16upx;
		background-color: #dedede;
		display: flex;
		justify-content: center;
		align-item: center;
		position: relative;
	}
	
	.year-pointer{
		position: absolute;
		right: 0;
		width: 22upx;
		height: 100%;
	}
	
	.year-pointerIcon{
		width: 36upx;
		height: 36upx;
		margin-top: 10upx;
		margin-left: -8upx;
	}
	
	.yearPicker-text{
		margin-top: 3px;
		font-size: 16px;
		font-weight: 550;
		color: #888888;
		margin-left: -16upx;
	}
	
	
</style>
