<template>
	<view id="profile-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.profile" :height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }"></view>

		<view class="personal_info_title">
			<view class="left_title">{{lang.profile}}</view>
			<view class="right_title">{{lang.editInfo}}</view>
		</view>

		<view class="personal_info_box">
			<view class="top">
				<view class="nickname">
					<view class="text">{{lang.nickname}}</view>
					<view class="second_line" @click="toggleIsEditNickname" v-if="!isEditNickname">{{ userInfo.nickname }}</view>
					<input :focus="true" class="second_line" @input="onNickName" style="font-size:17px;min-height: unset;" @blur="formSubmit"
					 name="nickname" maxlength="8" :value="userInfo.nickname" v-if="isEditNickname" />
					<view class="line" v-if="isEditNickname"></view>
				</view>
				<view class="gender">
					<view class="text">{{lang.gender}}</view>
					<view class="second_line" @click="toggleIsEditGender" v-if="!isEditGender">{{ lang.genderList[gender] }}</view>
					<view @tap="formSubmit" v-if="isEditGender" style="width:100px;display: flex;justify-content: space-between;position: relative;left:-6px;height:34px;">
						<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">{{lang.male}}</view>
						<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">{{lang.female}}</view>

					</view>
				</view>
			</view>
			<view class="middle">
				<view class="graduationYear">
					<view class="text">{{lang.graduationYear}}</view>
					<view class="second_line" @click="toggleIsEditGraduationYear" v-if="!isEditGraduationYear">{{ userInfo.graduationYear }}</view>
					<mypicker class="year-pick-style" v-if="isEditGraduationYear" :mode="'year'" :range="years" :value="year" @change="pickerChange"></mypicker>
				</view>
				<view class="major">
					<view class="text">{{lang.major}}</view>
					<view class="second_line" @click="toggleIsEditMajor" v-if="!isEditMajor">{{ userInfo.major }}</view>
					<mypicker v-if="isEditMajor" class="major-pick-style" :mode="'major'" :range="majors" :value="major" @change="pickerChange"></mypicker>
				</view>
			</view>
			<view class="bottom">
				<view class="signature">
					<view class="text">{{lang.signature}}</view>
					<view class="second_line" @click="toggleIsEditSignature" v-if="!isEditSignature">{{ userInfo.signature }}</view>
					<input :focus="true" class="second_line" @input="onSignature" style="font-size:17px;min-height: unset;" @blur="formSubmit"
					 name="signature" maxlength="20" :value="userInfo.signature" v-if="isEditSignature" />
					<view class="line" v-if="isEditSignature"></view>
				</view>
			</view>
		</view>


		<view class="correlation_info_title">
			<view class="left_title">{{lang.relevantInfo}}</view>
			<view class="right_title_email">{{lang.editEmail}}</view>
		</view>
		<view class="correlation_info_box">
			<view class="email">
				<view class="text">{{lang.schoolEmail}}</view>
				<view class="second_line">
					<view @click="editEmail" v-if='!isEditEmail&&userInfo.email!=null'>{{ userInfo.email}}</view>
					<view @click="editEmail" v-if="!isEditEmail&&userInfo.email==null">{{ lang.bindEmail}}</view>
					<input :focus="true" maxlength="35" style="min-height:unset;height:30px; font-size:17px;" :value="userInfo.email"
					 @input="onEmailInput" v-if="isEditEmail&& !showCaptcha" />
					<view class="line" style="width: 250px;" v-if="isEditEmail&&!showCaptcha"></view>
					<view v-if="isEditEmail&&showCaptcha" class="text">{{email}}</view>
				</view>
			</view>
			<view class="row" v-if="isEditEmail">
				<input style="color:rgba(53,53,53,1);min-height: unset;width: 80px;height:20px;font-size: 17px;" @blur="confirmCode"
				 v-if="isEditEmail&& showCaptcha" maxlength="6" :placeholder="lang.captcha" @input="onCaptcha" />
				<whCaptcha style="display: inline-block;" class="waiting" ref="captcha" :secord="60" :title="lang.getCaptcha"
				 :waitTitle="lang.waitCaptcha" normalClass="editEmail" disabledClass="waiting60s" @click="getCaptcha" v-if="isEditEmail"></whCaptcha>
			</view>
		</view>
	</view>
</template>
<script>
	import mypicker from '@/components/mypicker.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
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
				years, // 传入毕业年份 picker
				yearPickerVal: [], // 毕业年份 picker 的起始值, 必须为list
				majors,
				majorPickerVal: [],
				degrees: ['高中', '本科', '研究生'], //顺序和数据库保持一致
				degreePickerVal: [],

				nickname: "",
				gender: "",
				year: years[0], // 默认值
				major: majors[0],
				degree: degrees[1],
				degreeDB: '1', // 数据库 degree 传值
				signature: "",

				isEditNickname: false, //上半区属性修改
				isEditGender: false,
				isEditGraduationYear: false,
				isEditMajor: false,
				isEditEmail: false,
				isEditSignature: false,

				userInfo: '',

				email: '', //输入的邮箱
				captcha: '', //输入的验证码
				showCaptcha: false,
				navbarHeight: 0 ,//一次性储存 navbarheight
				needUpdateFlag:false,
			};
		},

		components: {
			mypicker,
			uniNavBar,
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
			this.nickname = this.userInfo.nickname;
			var gender = this.userInfo.gender;
			var year = this.userInfo.graduationYear;
			var major = this.userInfo.major;
			var degree = this.userInfo.degree;
			this.signature = this.userInfo.signature;

			if (!this.isNull(gender)) {
				// 判空，防止默认值被刷掉
				this.gender = gender;
			} else {
				this.gender = 2;
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
		onUnload() {
			if (this.needUpdateFlag) {
				this.formSubmit();
			}

		},
		methods: {
			pickerChange(res) {
				this.needUpdateFlag = true;
				if (res.mode == 'major') {
					this.major = res.newPickerValue;
				} else if (res.mode == 'degree') {
					this.degree = res.newPickerValue;
					this.degreeDB = this.degrees.indexOf(res.newPickerValue);
				} else if (res.mode == 'year') {
					this.year = res.newPickerValue;
				}
				this.formSubmit();
			},
			genderChanger(gender) {
				this.needUpdateFlag = true;
				this.gender = gender;
				this.formSubmit();
			},

			toggleIsEditNickname() {
				this.isEditNickname = !this.isEditNickname;
				this.isEditGender = false;
				this.isEditGraduationYear = false;
				this.isEditMajor = false;
				this.isEditSignature = false;
			},
			toggleIsEditGender() {
				this.isEditGender = !this.isEditGender;
				this.isEditNickname = false;
				this.isEditGraduationYear = false;
				this.isEditMajor = false;
				this.isEditSignature = false;
			},
			toggleIsEditGraduationYear() {
				this.isEditGraduationYear = !this.isEditGraduationYear;
				this.isEditNickname = false;
				this.isEditGender = false;
				this.isEditMajor = false;
				this.isEditSignature = false;
			},
			toggleIsEditMajor() {
				this.isEditMajor = !this.isEditMajor;
				this.isEditNickname = false;
				this.isEditGender = false;
				this.isEditGraduationYear = false;
				this.isEditSignature = false;
			},
			toggleIsEditSignature() {
				this.isEditSignature = !this.isEditSignature;
				this.isEditMajor = false;
				this.isEditNickname = false;
				this.isEditGender = false;
				this.isEditGraduationYear = false;
			},
			formSubmit() {
				// 提交表单操作
				if (this.nickname == "") {
					uni.showToast({
						title: '用户名不能为空',
						icon: 'none',
						duration: 2000
					});
					this.nickname = this.userInfo.nickname;
				}
				if (this.signature == "") {
					uni.showToast({
						title: '个性签名不能为空',
						icon: 'none',
						duration: 2000
					});
					this.signature = this.userInfo.signature;
				}

				var data = {
					id: this.userInfo.id,
					nickname: this.nickname,
					gender: this.gender,
					email: this.email,
					graduationYear: this.year,
					major: this.major,
					degree: this.degreeDB,
					signature: this.signature
				};
				console.log(data);
				var that = this;
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
							console.log(user);
							var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
							that.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							that.userInfo = finalUser; // 更新页面用户数据
							console.log(this.userInfo);
							that.needUpdateFlag = false;
						}
					}
				});

				// 完成修改，更改 isEdit 为 false				
				this.isEditNickname = false;
				this.isEditGender = false;
				this.isEditGraduationYear = false;
				this.isEditMajor = false;
				this.isEditSignature = false;
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
			onNickName(event) {
				this.nickname = event.target.value;
			},
			onCaptcha(event) {
				this.captcha = event.target.value;
			},
			onSignature(event) {
				this.signature = event.target.value;
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
			},

			showSignToast() {
				uni.showToast({
					title: 'Function under development.',
					icon: 'none'
				})
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
		height: 100%;
		width: calc(100% - 56px);
		margin: auto;

	}

	.personal_info_title {
		width: 100%;
		margin-top: 26px;
		margin-bottom: 12px;
	}

	.left_title {
		width: 56px;
		font-size: 14px;
		margin-left: 12px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 18px;
		color: rgba(136, 136, 136, 1);
		display: inline-block;
	}

	.right_title {
		width: 190px;
		font-size: 12px;
		margin-right: 8px;
		margin-left: calc(100% - 12px - 56px - 190px - 8px);
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(177, 177, 177, 1);
		display: inline-block;
		text-align: right;
	}

	.personal_info_box {
		width: 100%;
		height: 260px;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
	}

	.correlation_info_title {
		width: 100%;
		margin-top: 20px;
		margin-bottom: 13px;
	}

	.right_title_email {
		width: 190px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(177, 177, 177, 1);
		margin-right: 8px;
		margin-left: calc(100% - 12px - 56px - 190px - 8px);
		display: inline-block;
		text-align: right;
	}

	.correlation_info_box {
		width: 100%;
		margin-top: 13px;
		/* min-height: 82px; */
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
	}

	.top {
		width: 100%;
		height: 75px;
	}

	.middle {
		width: 100%;
		height: 75px;
	}

	.bottom {
		width: 100%;
		height: calc(260px - 150px);
	}

	.nickname,
	.gender,
	.major,
	.graduationYear {
		vertical-align: top;
		width: 130px;
		height: 55px;
		margin-left: 10%;
		margin-right: calc(50% - 130px - 10%);
		margin-top: 20px;
		overflow: visible;
		display: inline-block;
		position: relative;
	}

	.signature {
		vertical-align: top;
		width: 86%;
		height: 55px;
		margin-left: 10%;
		/* margin-right: calc(50% - 130px - 10%); */
		margin-top: 20px;
		overflow: visible;
	}

	.email {
		vertical-align: top;
		width: 86%;
		height: 55px;
		margin-left: 10%;
		/* margin-right: calc(50% - 130px - 10%); */
		padding-top: 20px;
		padding-bottom: 20px;
		overflow: visible;
	}

	.nickname .text,
	.gender .text,
	.graduationYear .text,
	.major .text,
	.email .text,
	.signature .text {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		color: rgba(177, 177, 177, 1);
	}

	.nickname .second_line,
	.gender .second_line,
	.graduationYear .second_line,
	.major .second_line,
	.signature .second_line,
	.email .second_line {
		height: 22.4px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		padding-top: 12px;
		color: rgba(112, 112, 112, 1);
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
		height: 22.4px;
	}


	.major-pick-style {
		height: 22.4px;
	}

	.row {
		display: flex;
		width: 200px;
		justify-content: space-between;
		align-items: bottom;
		height: 30px;
		vertical-align: middle;
		padding-bottom: 15px;
		margin-left: 10%;

	}

	.editEmail {
		margin-right: 10px;
		vertical-align: bottom;
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

	.waiting60s {
		text-align: center;
		width: 120px;
		height: 26px;
		border: 1px solid rgba(236, 236, 236, 1);
		opacity: 1;
		border-radius: 4px;
		font-size: 14px;
		font-weight: 400;
		line-height: 26px;
		background: rgba(236, 236, 236, 1);
		font-family: Source Han Sans CN;
		color: rgba(177, 177, 177, 1);
	}

	.line {
		width: 90px;
		height: 0px;
		border: 1px solid rgba(255, 207, 107, 1);
	}
</style>
<style>
	.major-pick-style .item,
	.year-pick-style .item {
		height: 22.4px;
		vertical-align: bottom;
	}

	.major-pick-style .arrow,
	.year-pick-style .arrow {
		height: 22.4px;
	}

	.major-pick-style .defaultPicker,
	.year-pick-style .defaultPicker {
		height: 22.4px;
		line-height: 30px;
	}
</style>
