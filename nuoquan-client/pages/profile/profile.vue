<template>
	<view id="profile-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.profile" :height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }"></view>
		<view id="yellow-box"></view>
		<form @submit="formSubmit" @reset="formReset">
			<view class="profile-basicinfo-card">
				<avatar class="profileTouxiang" selWidth="200px" selHeight="400upx" @upload="uploadFace" :avatarSrc="pathFilter(userInfo.faceImg)"
				 avatarStyle="width: 160upx; height: 160upx; border-radius: 100%;"></avatar>
				<!-- 完成/修改按钮 -->
				<button class="editProfile super_center" v-if="isEdit == false" @click="toggleIsEdit">
					<view class="editProfile-text">{{lang.edit}}</view>
				</button>
				<button class="editProfile-edit super_center" v-if="isEdit == true" formType="submit">
					<view class="editProfile-edit-text">{{lang.ok}}</view>
				</button>

				<!-- 编辑框 -->
				<view style="block;width: 100%;">
					<view class="nickname" style="display: block;">
						<view class="text">{{lang.nickname}}</view>
						<view class="second_line" v-if="!isEdit">{{ userInfo.nickname }}</view>
						<input name="nickname" maxlength="8" :value="userInfo.nickname" v-if="isEdit" />
						<!-- <text v-if="isEdit == true" class="text_limit"> 最长 8 位</text> -->
					</view>
				</view>


				<view style="display: inline-block;width: 50%;">
					<view class="gender">
						<view class="text">{{lang.gender}}</view>
						<!-- 					<text class="right-profileTexta"  v-if="userInfo.gender==null || userInfo.gender==-1">待设置</text> -->
						<view class="second_line" v-if="!isEdit">{{ genderList[userInfo.gender] }}</view>
						<view v-if="isEdit" style="display: flex;justify-content: space-between;position: relative;left:-6px;height:34px;">
							<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">{{lang.male}}</view>
							<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">{{lang.female}}</view>
						</view>
					</view>
					<view class="major">
						<view class="text">{{lang.major}}</view>
						<view class="second_line" v-if="!isEdit">{{ userInfo.major }}</view>
						<mypicker v-if="isEdit" class="major-pick-style" :mode="'major'" :range="majors" :value="major" @change="pickerChange"></mypicker>
					</view>
				</view>
				<view style="display: inline-block;width:50%;">
					<view class="graduationYear">
						<view class="text">{{lang.graduationYear}}</view>
						<view class="second_line" v-if="!isEdit">{{ userInfo.graduationYear }}</view>
						<mypicker class="year-pick-style" v-if="isEdit" :mode="'year'" :range="years" :value="year" @change="pickerChange"></mypicker>
					</view>
					<view class="degree">
						<view class="text">{{lang.degree}}</view>
						<view class="second_line" v-if="!isEdit">{{ degrees[userInfo.degree] }}</view>
						<mypicker v-if="isEdit" class="degree-pick-style" :mode="'degree'" :range="degrees" :value="degree" @change="pickerChange"></mypicker>
					</view>
				</view>


			</view>
		</form>
		<view class="profile-moreinfo-card">
			<view class="text row">{{lang.schoolEmail}}</view>
			<view class="row">
				<view class="full_email" v-if='!isEditEmail'>{{ userInfo.email}}</view>
				<input maxlength="6" :value="userInfo.emailPrefix" @input="onEmailInput" v-if="isEditEmail&& !showCaptcha" />
				<view v-if="isEditEmail&& !showCaptcha" class="text">{{ userInfo.emailSuffix }}</view>
				<view v-if="isEditEmail&&showCaptcha" class="text">{{email}}@nottingham.edu.cn</view>
			</view>
			<view class="row">
				<button class="editEmail super_center" @click="editEmail" v-if="!isEditEmail">
					<view class="editProfile-text">{{lang.changeEmail}}</view>
				</button>

				<input v-if="isEditEmail&& showCaptcha" maxlength="6" :placeholder="lang.captcha" @input="onCaptcha" />
				<button v-if="isEditEmail&& showCaptcha" @click="confirmCode" class="confirmButton">
					<view>{{lang.ok}}</view>
				</button>
				<whCaptcha class="waiting" ref="captcha" :secord="60" :title="lang.getCaptcha" :waitTitle="lang.waitCaptcha"
				 normalClass="editEmail" disabledClass="waiting60s" @click="getCaptcha" v-if="isEditEmail"></whCaptcha>



			</view>


			<!-- 			<view class="email">
				<view class="text">{{lang.schoolEmail}}</view>
				<view v-if="!isEditEmail">
					<view class="second_line">{{ userInfo.email }}</view>
					<button class="editEmail super_center" @click="editEmail">
						<view @click="editModify(isEditEmail)" class="editProfile-text">{{lang.changeEmail}}</view>
					</button>
				</view>
				<view v-else-if="isEditEmail">
					<input maxlength="26" :value="userInfo.email" @input="onEmailInput" />

					<whCaptcha style="font-size: 15px;margin-top: 15px;" ref="captcha" :secord="60" :title="lang.getCaptcha"
					 :waitTitle="lang.waitCaptcha" normalClass="editEmail" disabledClass="" @click="getCaptcha"></whCaptcha>

					<view v-if="showCaptcha">
						<input style="width:75px;" maxlength="6" :placeholder="lang.captcha" @input="onCaptcha" />
						<button @click="confirmCode">
							<view>{{lang.ok}}</view>
						</button>
					</view>
				</view>
			</view> -->



		</view>
	</view>
</template>
<script>
	import mypicker from '@/components/mypicker.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import avatar from '@/components/yq-avatar/yq-avatar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import whCaptcha from '@/components/wh-captcha/wh-captcha.vue';

	export default {
		data() {
			// 为毕业年份 picker 传值
			const date = new Date();
			const years = [];
			const thisYear = date.getFullYear();

			for (let i = 2004; i <= thisYear + 4; i++) {
				years.push(i);
			}

			// major
			const majors = ['AEE', 'ABE', 'CS', 'CEE', 'CIVE', 'ECON', 'EEE', 'ENGL', 'GEOG', 'IC', 'IS', 'MATH', 'PDM', 'NUBS'];

			// degree 顺序和数据库保持一致
			const degrees = ['高中', '本科', '研究生'];

			return {
				genderList: ['女', '男'], // 顺序和数据库保持一致
				years, // 传入毕业年份 picker
				yearPickerVal: [], // 毕业年份 picker 的起始值, 必须为list
				majors,
				majorPickerVal: [],
				degrees: ['高中', '本科', '研究生'], //顺序和数据库保持一致
				degreePickerVal: [],

				gender: 2,
				year: years[0], // 默认值
				major: majors[0],
				degree: degrees[1],
				degreeDB: '1', // 数据库 degree 传值

				isEdit: false, //上半区属性修改
				userInfo: '',
				yearPicker: false,
				majorPicker: false,
				degreePicker: false,
				isEditEmail: false,
				email: '', //输入的邮箱
				captcha: '', //输入的验证码
				showCaptcha: false,
				navbarHeight: 0 //一次性储存 navbarheight
			};
		},

		components: {
			mypicker,
			uniNavBar,
			avatar,
			whCaptcha
		},
		computed: {
			...mapState(['lang'])
		},
		onLoad() {
			// 一次性储存 navbar 高度
			this.navbarHeight = this.getnavbarHeight().bottom + 5;

			this.userInfo = this.getGlobalUserInfo();
			//按语言切换默认列表
			this.degrees = this.lang.degreeList;
			this.genderList = this.lang.genderList;
			// 按已有信息修改默认值
			var gender = this.userInfo.gender;
			var year = this.userInfo.graduationYear;
			var major = this.userInfo.major;
			var degree = this.userInfo.degree;
			if (!this.isNull(gender)) {
				// 判空，防止默认值被刷掉
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

			this.email = this.userInfo.email; // 改绑邮箱默认值
		},

		methods: {
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
						userId: this.userInfo.id
					},
					success: res => {
						// uploadFile 返回的 res.data 是 String
						var data = JSON.parse(res.data);
						if (data.status == 200) {
							this.userInfo.faceImg = data.data;
						}
					}
				});
			},

			pickerChange(res) {
				if (res.mode == 'major') {
					this.major = res.newPickerValue;
				} else if (res.mode == 'degree') {
					this.degree = res.newPickerValue;
					this.degreeDB = this.degrees.indexOf(res.newPickerValue);
				} else if (res.mode == 'year') {
					this.year = res.newPickerValue;
				}
			},
			genderChanger(gender) {
				if (this.gender == gender) {
					this.gender = -1;
					console.log(gender);
				} else {
					this.gender = gender;
				}
			},

			toggleIsEdit() {
				this.isEdit = !this.isEdit;
			},

			formSubmit(e) {
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
				console.log(data);
				uni.request({
					url: this.$serverUrl + '/user/updateUser',
					method: 'POST',
					data: JSON.stringify(data),
					header: {
						'content-type': 'application/json'
					},
					success: res => {
						if (res.data.status == 200) {
							var user = res.data.data;
							var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							// console.log(this.userInfo);
						}
					}
				});

				// 完成修改，更改 isEdit 为 false
				this.toggleIsEdit();
			},

			/**
			 * 修改邮箱状态切换
			 */
			editEmail() {
				this.isEditEmail = !this.isEditEmail;
			},
			/**
			 * 监听邮箱输入
			 * @param {Object} event
			 */
			onEmailInput(event) {
				this.email = event.target.value;
			},
			/**
			 * 监听验证码输入
			 * @param {Object} event
			 */
			onCaptcha(event) {
				this.captcha = event.target.value;
			},
			getCaptcha() {
				var email = this.email;
				if (this.email) {
					// 检测邮箱
					if (this.$util.regEmail(email) || this.$util.regUNNCEmail(email)) {
						uni.showToast({
							title: '非 UNNC 邮箱地址！',
							icon: 'none'
						});
					} else {
						this.showCaptcha = true;
						if (this.$refs.captcha.canSend()) {
							console.log('获取验证码 email=' + email);
							this.$refs.captcha.begin();

							uni.request({
								url: this.$serverUrl + '/user/getCode',
								method: 'POST',
								data: {
									userId: this.userInfo.id,
									email: email
								},
								header: {
									'content-type': 'application/x-www-form-urlencoded'
								},
								success: res => {
									console.log(res);
								}
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

			confirmCode() {
				if (this.captcha) {
					uni.showLoading({
						title: '请等待'
					});
					uni.request({
						url: this.$serverUrl + '/user/confirmCode',
						method: 'POST',
						data: {
							userId: this.userInfo.id,
							code: this.captcha,
							email: this.email
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							if (res.data.status == 200) {
								console.log(res.data.data);
								this.userInfo = res.data.data;

								uni.showToast({
									title: 'OK',
									icon: 'none'
								});
								this.isEditEmail = false;
								this.showCaptcha = false;
							} else {
								console.log('验证失败 ' + res.data.msg);
								uni.showToast({
									title: '验证失败',
									icon: 'none'
								});
							}
						},
						complete: () => {
							uni.hideLoading();
						}
					});
				} else {
					uni.showToast({
						title: '验证码为空',
						icon: 'none'
					});
				}
			}
		}
	};
</script>

<style>
	page {
		height: 100%;
		width: 100%;
	}

	#profile-container {
		width: 100%;
		height: 100%;
		background-color: #f3f3f3;
	}

	#yellow-box {
		top: 0;
		height: 200rpx;
		width: 100%;
		background-color: #ffc85a;
	}

	.profile-basicinfo-card {
		position: relative;
		/* width: 86%;
	height: 300px; */
		width: 93%;
		height: 175px;
		background-color: white;
		/* justify-content: center; */
		margin: 0 auto;
		margin-top: -90rpx;
		margin-bottom: 10px;
		padding-top: 50px;

		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 8px;
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
		/* border: 1px solid rgba(255, 201, 90, 1); */
		line-height: 26px;
		border-radius: 4px;
		opacity: 1;
		background-color: white;
	}

	.editProfile-text {
		color: white;
		width: 100%;
		height: 100%;
		font-size: 14px;
		font-weight: 400;
		margin: 0 auto;
	}

	.editProfile-edit-text {
		color: #ffcf3c;
		width: 100%;
		height: 100%;
		margin: 0 auto;
		font-size: 14px;
		font-weight: 400;
	}


	.editEmail-edit {
		position: absolute;
		left: calc((50% - 95px) / 2);
		width: 111px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
		line-height: 26px;
		border-radius: 4px;
	}

	.editEmail-edit-text {
		color: white;
		width: 100%;
		height: 100%;
		font-size: 14px;
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
	.nickname {
		vertical-align: top;
		display: inline-block;
		height: 45px;
		width: 140px;
		margin: 0 0 15px calc((50% - 95px) / 2);
		position: relative;
		overflow: visible;
	}

	.gender,
	.major {
		vertical-align: top;
		display: inline-block;
		width: 95px;
		height: 45px;
		margin: 0 calc((100% - 95px) / 2) 15px;
		position: relative;
		overflow: visible;
	}

	.graduationYear,
	.degree {
		vertical-align: top;
		display: inline-block;
		min-width: 95px;
		max-width: 115px;
		height: 45px;
		margin: 0 calc((100% - 115px) / 2) 15px;
		position: relative;
		overflow: visible;
	}



	.nickname .text,
	.gender .text,
	.profile-moreinfo-card .text {
		height: 11px;
		line-height: 11px;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
		overflow: visible;
	}

	.nickname .second_line,
	.gender .second_line,
	.email .second_line {
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
		{
		border-bottom: 1upx solid #c0c0c0;
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
		border: 1px solid #3370ff;
		border-radius: 4px;
		background-color: #3370ff;
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
		top: 19px;
	}

	.degree-pick-style {
		position: absolute;
		z-index: 888;
		top: 19px;
	}

	.major-pick-style {
		position: absolute;
		z-index: 888;
		top: 19px;
	}

	/* 	.year-pick-style ::after,
	.degree-pick-style::after,
	.major-pick-style::after {
		content: ".";
		display: block;
		height: 0;
		clear: both;
		visibility: hidden;
	} */





	.profile-moreinfo-card {
		height: 105px;
		width: 93%;
		background-color: white;
		margin-left: auto;
		margin-right: auto;
		position: relative;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 8px;
		padding-top: 20px;
	}

	.profile-moreinfo-card .text {
		height: 11px;
		line-height: 11px;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
		overflow: visible;
		display: inline-block;
	}

	.profile-moreinfo-card .row {
		margin-left: calc((50% - 95px)/2);
		margin-bottom: 10px;
		display: block;
		vertical-align: bottom;
	}

	.profile-moreinfo-card input {
		margin-right: 10px;
		vertical-align: bottom;
		border-bottom: 1rpx solid #c0c0c0;
		height: 17px;
		font-size: 17px;
		line-height: 17px;
		color: rgba(53, 53, 53, 1);
		text-align: left;
		min-height: 1rpx;
		overflow: visible;
		padding-bottom: 5px;
		display: inline-block;
		width: 60px;
	}

	.editEmail {
		margin-right: 10px;
		vertical-align: bottom;
		position: absolute;
		left: calc((50% - 95px) / 2);
		width: 97px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
		line-height: 26px;
		border-radius: 4px;
		text-align: center;
		font-size: 14px;
		font-weight: 400;
		line-height: 26px;
		color: rgba(255, 255, 255, 1);
	}

	.confirmButton {
		margin-right: 10px;
		vertical-align: bottom;
		display: inline-block;
		width: 68px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
		border-radius: 4px;
		font-size: 14px;
		line-height: 26px;
		color: rgba(255, 255, 255, 1);
	}

	.waiting60s {
		display: inline-block;
		text-align: center;
		width: 120px;
		height: 26px;
		border: 1px solid rgba(253, 208, 65, 1);
		opacity: 1;
		border-radius: 4px;
		font-size: 14px;
		font-weight: 400;
		line-height: 26px;
		color: rgba(255, 201, 90, 1);
	}
</style>
