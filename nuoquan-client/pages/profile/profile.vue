<template>
	<view id="profile-container">
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}" left-icon="back" left-text="返回" :title="pageTitle" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}"></view>
		<view id="yellow-box"></view>
		<form @submit="formSubmit" @reset="formReset">
			<view class="profile-basicinfo-card">
				<avatar class="profileTouxiang"
					selWidth="200px" selHeight="400upx" @upload="uploadFace" :avatarSrc="pathFilter(userInfo.faceImg)"
					avatarStyle="width: 160upx; height: 160upx; border-radius: 100%;">
				</avatar>
				<!-- 完成/修改按钮 -->
				<button class="editProfile super_center" v-if="isEdit == false" @click="editProfile(isEdit)">
					<view @click="editProfile(isEdit)" class="editProfile-text">修改</view>
				</button>
				<button class="editProfile-edit super_center" v-if="isEdit == true" formType="submit">
					<view class="editProfile-edit-text">完成</view>
				</button>

				<!-- 编辑框 -->
				<view class="nickname">
					<view class="text">昵称</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.nickname}}</view>
					<input name="nickname" maxlength="8" :value="userInfo.nickname" v-if="isEdit" />
					<!-- <text v-if="isEdit == true" class="text_limit"> 最长 8 位</text> -->
				</view>

				<view class="gender">
					<view class="text"> 性别</view>
					<!-- 					<text class="right-profileTexta"  v-if="userInfo.gender==null || userInfo.gender==-1">待设置</text> -->
					<view class="second_line" v-if="!isEdit">{{genderList[userInfo.gender]}}</view>
					<view v-if="isEdit" style="display: flex;justify-content: space-between;position: relative;left:-6px;height:34px;">
						<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">
							男
						</view>
						<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">
							女
						</view>
					</view>
				</view>


				<view class="nickname">
					<view class="text">毕业年份</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.graduationYear}}</view>
					<mypicker class="year-pick-style" v-if="isEdit" :mode="'year'" :range="years" :value="year" @change="pickerChange"></mypicker>
				</view>
				<view class="nickname">
					<view class="text">专业</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.major}}</view>
					<mypicker v-if="isEdit" class="major-pick-style" :mode="'major'" :range="majors" :value="major" @change="pickerChange"></mypicker>
				</view>
				<view class="nickname">
					<view class="text">学位</view>
					<view class="second_line" v-if="!isEdit">{{degrees[userInfo.degree]}}</view>
					<mypicker v-if="isEdit" class="degree-pick-style" :mode="'degree'" :range="degrees" :value="degree" @change="pickerChange"></mypicker>
				</view> 
				
			</view>
			<view class="profile-moreinfo-card">
				<view class="email">
					<view class="text">学校邮箱</view>
					<view class="second_line" v-if="!isModify">{{userInfo.email}}</view>
					<input v-if="isModify" maxlength="26" :value="userInfo.email" />
						<!-- <text class="profilemoreTexta" v-else>scyzl2@nottingham.edu.cn</text> -->
				</view>
				<button class="editEmail super_center" v-if="isModify == false&&isSent==false" @click="editEmail(isModify)">
					<view @click="editModify(isModify)" class="editProfile-text">更改邮箱</view>
				</button>
				<button class="editEmail-edit super_center" v-if="isModify == true&&isSent==false" @click="getCaptcha(isSent)" >
					<view class="editEmail-edit-text">发送验证码</view>
				</button>
			</view>
		</form>
	</view>
</template>
<script>
	import mypicker from '../../components/mypicker.vue';
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue";
	import avatar from "../../components/yq-avatar/yq-avatar.vue";
	
	export default {
		data() {
			// 为毕业年份 picker 传值
			const date = new Date();
			const years = [];
			const thisYear = date.getFullYear();

			for (let i = 2004; i <= thisYear + 4; i++) {
				years.push(i);
			};

			// major
			const majors = ['AEE', 'ABE', 'CS', 'CEE', 'CIVE', 'ECON', 'EEE', 'ENGL',
				'GEOG', 'IC', 'IS', 'MATH', 'PDM', 'NUBS'];

			// degree 顺序和数据库保持一致
			const degrees = ['高中', '本科', '研究生'];

			return {
				genderList: ['女', '男'], // 顺序和数据库保持一致
				pageTitle:'profile',
				years, // 传入毕业年份 picker
				yearPickerVal: [], // 毕业年份 picker 的起始值, 必须为list
				majors,
				majorPickerVal: [],
				degrees,
				degreePickerVal: [],

				gender: 2,
				year: years[0], // 默认值
				major: majors[0],
				degree: degrees[1],
				degreeDB: '1', // 数据库 degree 传值

				isEdit: false,
				isModify:false,
				isSent:false,
				userInfo: '',
				yearPicker: false,
				majorPicker: false,
				degreePicker: false,
			}
		},

		components:{
			mypicker,
			uniNavBar,
			avatar
		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "个人信息"
			});
			var a = this.getGlobalUserInfo();
			this.userInfo = a;
			// 按已有信息修改默认值
			var gender = this.userInfo.gender;
			var year = this.userInfo.graduationYear;
			var major = this.userInfo.major;
			var degree = this.userInfo.degree;
			if (!this.isNull(gender)) { // 判空，防止默认值被刷掉
				this.gender = gender;
			}

			if (!this.isNull(year)) {
				this.year = year;
			}

			if (!this.isNull(major)) {
				this.major = major;
			}

			if (!this.isNull(degree)) {
				this.degree = this.degrees[degree];
				this.degreeDB = degree; // 修改对数据库的默认值
			}
		},

		methods: {
			pickerChange: function(res) {
				if (res.mode == 'major') {
					this.major = res.newPickerValue;
				} else if (res.mode == 'degree') {
					this.degree = res.newPickerValue;
					this.degreeDB = this.degrees.indexOf(res.newPickerValue);
				} else if (res.mode == 'year') {
					this.year = res.newPickerValue;
				}
			},
			genderChanger: function(gender) {
				if (this.gender == gender) {
					this.gender = -1;
					console.log(gender);
				} else {
					this.gender = gender;
				}
			},

			editProfile: function(isEdit) {
				if (isEdit == false) {
					this.isEdit = true;
				} else if (isEdit == true) {
					this.isEdit = false;
				}
				console.log(this.isEdit);
			},
			
			editEmail: function(isModify) {
				if (isModify == false) {
					this.isModify = true;
				} else if (isModify == true) {
					this.isModify = false;
				}
				console.log(this.isModify);
			},

			cancle: function() {
				// 取消修改操作
				this.editProfile(this.isEdit);
			},

			getCaptcha:function(isSent) {
				if (isSent == false) {
					this.isSent = true;
				} else if (isSent == true) {
					this.isSent = false;
				}
				console.log(this.isSent);
				if (email) {
						// 测试账号代码
						if (email=="test@test.com"){
							uni.showToast({
								title: '认证成功',
								icon: 'none'
							});
							this.changeAuth();
							this.nextStep(false);
							return;
						}
						// 检测邮箱
						if (util.regEmail(email) || this.checkUNNCEmail(email)) {
							uni.showToast({
								title: '非 UNNC 邮箱地址！',
								icon: 'none'
							});
						} else {
							if (this.$refs.captcha.canSend()) {
								console.log("获取验证码 email=" + email);
								this.$refs.captcha.begin();
								this.title = "重新发送"
				
								uni.request({
									url: this.$serverUrl + '/user/getCode',
									method: "POST",
									data: {
										userId: this.userInfo.id,
										email: email,
									},
									header: {
										'content-type': 'application/x-www-form-urlencoded'
									},
									success: (res) => {
										console.log(res);
									},
								});
							}
						}
					} else {
						uni.showToast({
							title: 'Email不能为空',
							icon: 'none'
						});
					}
				
			},


			formSubmit: function(e) {
				// 提交表单操作
				var form = e.detail.value;
				var data = {
					id: this.userInfo.id,
					nickname: form.nickname,
					gender: this.gender,
					graduationYear: this.year,
					major: this.major,
					degree: this.degreeDB
				};
				console.log("here");
				console.log(data);
				uni.request({
					url: this.$serverUrl + '/user/updateUser',
					method: "POST",
					data: JSON.stringify(data),
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						if (res.data.status == 200) {
							var user = res.data.data;
							var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							// console.log(this.userInfo);
						}
					},
				});

				// 完成修改，更改 isEdit 为 false
				this.editProfile(this.isEdit);
			},
			/**
			 * 上传头像
			 * @param {Object} e
			 */
			uploadFace(rsp) {
				var path = rsp.path;
				uni.uploadFile({
					url: this.$serverUrl + '/user/uploadFace',
					filePath: path,
					name: 'file',
					formData: {
						userId: this.userInfo.id,
					},
					success: (res) => {
						// uploadFile 返回的 res.data 是 String
						var data = JSON.parse(res.data);
						if (data.status == 200) {
							this.userInfo.faceImg = data.data;
						}
					}
				});
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
		width: 100%;
		height: 100%;
		background-color: #F3F3F3;
	}

	#yellow-box {
		top: 0;
		height: 200rpx;
		width: 100%;
		background-color: #FFCF3C;
	}

	.profile-basicinfo-card {
		position: relative;
		/* width: 86%;
	height: 300px; */
		width: 93%;
		height: 175px;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
		/* justify-content: center; */
		margin-left: auto;
		margin-right: auto;
		margin-top: -90rpx;
		margin-bottom: 10px;
		padding-top: 50px;
	}
.profile-moreinfo-card {
		height: 125px;
		width: 93%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
		margin-left: auto;
		margin-right: auto;
		position: relative;
	}
	.profileTouxiang {
		position: absolute;
		top: -100upx;
		left: calc(50% - 80upx);
		width: 160upx;
		height: 160upx;
		border-radius: 120px;
	}

	.editProfile {
		position: absolute;
		right: 3%;
		top: 3%;
		width: 68px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
	line-height: 26px;
		border-radius: 4px;
	}

	.editProfile-edit {
		position: absolute;
		right: 3%;
		top: 3%;
		width: 68px;
		height: 26px;
		border: 1px solid rgba(255, 201, 90, 1);
		line-height: 26px;
		border-radius: 4px;
	}

	.editProfile-text {
		color: white;
		width:100%;
		height:100%;
		font-size:14px;
		font-weight: 400;
			margin: 0 auto;
	}

	.editProfile-edit-text {
		color: #FFCF3C;
		width:100%;
		height:100%;
		margin: 0 auto;
		font-size:14px;
		font-weight: 400;
	}
	.editEmail {
		position: absolute;
		left:calc((50% - 95px)/2);	
		width: 97px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
		line-height: 26px;
		border-radius: 4px;
	}
	.editEmail-edit{
		position: absolute;
		left:calc((50% - 95px)/2);	
		width: 111px;
		height: 26px;
			background: rgba(255, 201, 90, 1);
		line-height: 26px;
		border-radius: 4px;
	}
	.editEmail-edit-text{
	color: white;
	width:100%;
	height:100%;
	font-size:14px;
	font-weight: 400;
		margin: 0 auto;
	}
	.genderPicker {
		width: 100px;
		height: 28%;
		display: flex;
		margin-left: -10px;
		justify-content: space-around;
	}

	.yearPicker {
		position: absolute;
		width: 5;
		/* 在此更改按钮高度 */
		height: 30px;
		/* 保证内容剧中 */
		display: flex;
		justify-content: space-between;
	}

	.yearPicker-button {
		height: 100%;
		width: 122upx;
		display: flex;
		justify-content: left;
		align-item: center;
		position: relative;
	}

	.year-pointer {
		width: 22upx;
		height: 100%;
	}

	.year-pointerIcon {
		width: 36upx;
		height: 36upx;
		margin-top: 10upx;
	}

	.yearPicker-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 500;
		color: #888888;
	}

	.degreePicker {
		/* 在此更改按钮高度 */
		height: 30px;
		/* 保证内容剧中 */
		display: flex;
		justify-content: space-between;
	}


</style>

<style>
	.nickname,
	.gender {
		vertical-align: top;
		display: inline-block;
		width: 95px;
		height: 45px;
		margin: 0 calc((50% - 95px)/2) 15px;
		position: relative;
		overflow: visible;
	}
	.email{
		position: relative;
		vertical-align: top;
		display: inline-block;
		width: 242px;
		height: 38px;
		margin: 18px calc((50% - 95px)/2) 15px;
		overflow: visible;
	}

	.nickname .text,
	.gender .text,
	.email .text{
		height: 11px;
		line-height: 11px;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
		overflow: visible;
	}

	.nickname .second_line,
	.gender .second_line,
	.email .second_line{
		height: 34px;
		font-size: 17px;
		line-height: 34px;
		color: rgba(53, 53, 53, 1);
	}

	.nickname .text_limit {
		width: 46px;
		height: 11px;
		position: absolute;
		left: 25px;
		top: 0;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
	}

	.nickname input,
	.email input{
		border-bottom: 1upx solid #C0C0C0;
		height: 29px;
		font-size: 17px;
		line-height: 29px;
		color: rgba(53, 53, 53, 1);
		text-align: left;
		min-height: 1upx;
		overflow: visible;
		padding-bottom: 5px;
	}

	.genderPicker-button {
		width: 41px;
		height: 23px;
		border: 1px solid rgba(255, 93, 93, 1);
		opacity: 1;
		border-radius: 4px;
		color: rgba(255, 93, 93, 1);
		font-weight: 500;
		text-align: center;
		line-height: 23px;
		align-self: flex-end;
		font-size: 16px;
		font-weight: 550;
	}

	.genderPicker-buttonclick {
		width: 41px;
		height: 23px;
		border: 1px solid blue;
		border-radius: 4px;
		background-color: blue;
		color: rgba(255, 255, 255, 1);
		text-align: center;
		align-self: flex-end;
		font-weight: 500;
		line-height: 23px;

		font-size: 16px;
		font-weight: 550;
	}
	.year-pick-style {
		position: absolute;
		z-index: 999;
		left: -5px;
		top:19px;
	}
.degree-pick-style{
	position: absolute;
	z-index: 888;
	left: -10px;
	top:19px;
}
.major-pick-style{
	position: absolute;
	z-index: 888;
	left: -10px;
	top:19px;
}
	
</style>
