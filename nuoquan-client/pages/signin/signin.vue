<template>
	<view id="signin-container">
		<!-- 简介 -->
		<view id="introduction" class="super_center" :style="{left: swiperLeft +'%;'}">
			<!-- 用户协议签订页 -->
			<view class="introduction-contentBox">
				<view class="email-intro">
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						亲爱的 UNNCer，
					</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						欢迎回家。
					</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						请您先接受《用户协议》。
					</view>
				</view>
				<!-- <view style="position: absolute;top: 45%;height: 45%;width: 100%;" class="super_center">
					<view class="icon-logoBox super_center">
						<image src="../../static/icon/logo_app.png" mode="aspectFill" class="icon-logo"></image>
					</view>
				</view> -->
				<view class="introduction-bottom-sign">
					<checkbox-group @change="changeAgreement">
						<label style="display: flex;" class="super_center">
							<checkbox class="wx-sign-checkbox" value="cb" :checked="agreement" style="transform: scale(0.5);" />
							<text style="font-size: 14px;font-weight: 550;color: #3d3d3d;">我已阅读并同意遵守</text>
							<view style="font-size: 14px;font-weight: 550;color: #007AFF;" @click="toUserDeal">
								《用户协议》
							</view>
						</label>
					</checkbox-group>
				</view>
			</view>
			<!-- 邮箱认证页 -->
			<view class="email-Box">
				<view class="email-intro">
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						亲爱的 UNNCer，
					</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						最后一步。
					</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">
						请认证您的 UNNC 邮箱。
					</view>
				</view>

				<view class="email-content">
					<input class="email-input" type="number" placeholder=" 请输入 UNNC 邮箱" placeholder-style="color: #C0C0C0;font-size:15px;"
					 @input="onEmailInput" :disabled="auth"/>
					<input class="email-input" type="text" placeholder=" 验证码" placeholder-style="color: #C0C0C0;font-size:15px;"
					 @input="onCaptchaInput" :maxlength="captchaLength" :disabled="auth"/>
					<wh-captcha style="font-size: 15px;margin-top: 15px;" 
					ref="captcha" 
					:secord="60" 
					title="获取验证码" 
					waitTitle="稍等(SECORD)秒"
					normalClass="captcha-normal" 
					disabledClass="captcha-disabled" @click="getCaptcha"></wh-captcha>
				</view>
			</view>
		</view>
		<view id="confirm" class="super_center">
			<view class="confirm-rel" v-if="swiperLeft == 0">
				<button v-if="agreement == false" class="confirm-button-before super_center" hover-class="button-hover">
					<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">微信绑定</view>
				</button>
				<button v-else class="confirm-button-checked super_center" hover-class="button-hover" open-type="getUserInfo"
				 @getuserinfo="getUserInfo">
					<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">微信绑定</view>
				</button>
				<view class="conform-bgBox"></view>
			</view>
			<!-- 邮箱认证 -->
			<view class="confirm-rel" v-else v-if="swiperLeft == -100">
				<view v-if="!auth">
					<button class="" hover-class="button-hover" @click="lastStep(true)">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">上一步</view>
					</button>
					<button class="confirm-button-checked super_center" hover-class="button-hover" @click="confirmCode">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">邮箱认证</view>
					</button>
				</view>
				<view v-else>
					<button class="" hover-class="button-hover" @click="cancleAuth">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">上一步</view>
					</button>
					<button class="confirm-button-checked super_center" hover-class="button-hover" @click="login">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">确认登陆</view>
					</button>
				</view>
				<!-- <view class="conform-bgBox"></view> -->
			</view>
		</view>

		<image src="../../static/BG/signin_bottom.png" mode="scaleToFill" class="bottom-picBox"></image>

		<!-- example 步骤条 -->
		<!-- <view class="example-body">
			<uni-steps :options="activeList" :active="active" />
		</view> -->
		<!-- 进度条 fixed -->
		<view style="position: fixed; width: 48%;left: 26%;top: 64%;height: 5px;background-color: #C0C0C0;z-index: 30;">
			<view style="position: relative;width: 100%;height: 100%;">
				<view style="position: absolute;left: 0;height: 5px;background-color: #FCC041;z-index: 40;" :style="{width: swiperLineWidth + '%;'}">
				</view>
			</view>
		</view>

		<!-- 进度条文字 -->
		<view style="position: fixed;width: 70%;top: 66%;left: 15%;height: 30px;">
			<view style="display: flex;justify-content: space-around;width: 100%;height: 100%;">
				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">
					微信绑定
				</view>
				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">
					邮箱认证
				</view>
				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">
					登陆完成
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import whCaptcha from '../../components/wh-captcha/wh-captcha.vue';
	import uniSteps from '@/components/uni-steps/uni-steps.vue'

	var isLoding = false;
	var timer = null;
	var timer_ = null;
	//邮箱验证
	var util = require('../../common/util.js');
	var email; //输入的email值
	var captcha; //输入的captcha值
	export default {
		components: {
			whCaptcha,
			uniSteps
		},
		data() {
			return {
				agreement: false,	// 是否同意用户协议
				auth: false,
				
				swiperLeft: 0,		// 块滑动
				swiperLineWidth: 0,	// 进度条滑动
				captchaLength: 6,	// 验证码长度

				active: 0,
				activeList: [{
					title: '微信绑定'
				}, {
					title: '邮箱认证'
				}, {
					title: '登陆'
				}],
				
				userInfo: '',
			};
		},
		
		onBackPress(e) {
			// return true 表示禁止默认返回
			console.log("监听到返回")
			return false
		},
		
		methods: {
			changeAgreement() {
				this.agreement = !this.agreement;
			},
			
			changeAuth(){
				this.auth = !this.auth;
			},
			
			cancleAuth(){
				this.auth = false;
				this.lastStep(false);
			},
			
			/**
			 * 微信小程序登陆
			 */
			getUserInfo(e) {
				// console.log("getting UserInfo...")
				// console.log(e);
			
				// 加锁
				if (isLoding) {
					return;
				}
				isLoding = true;
			
				uni.showLoading({
					title: '载入中...',
				});
				setTimeout(() => {
					if (isLoding) {
						isLoding = false // 解锁
						uni.hideLoading();
						uni.showToast({
							title: "网络未知错误",
							icon: "none",
							duration: 1000
						})
					}
				}, 5000); // 延时5s timeout
			
				var info = e.detail;
				var that = this;
				uni.login({
					success: res_login => {
						// console.log('-------res_login，获取code-------')
						// console.log(res_login);
						// 后端获取openid 并设置用户信息
						uni.request({
							url: that.$serverUrl + '/user/getWxUserInfo',
							method: "POST",
							data: {
								encryptedData: info.encryptedData,
								iv: info.iv,
								code: res_login.code,
			
								nickname: info.userInfo.nickName,
								faceImg: info.userInfo.avatarUrl
							},
							header: {
								'content-type': 'application/x-www-form-urlencoded'
							},
							success: (res) => {
								// console.log(res)
								if (res.data.status == 200) {
									console.log("暂存用户信息");
									that.userInfo = res.data.data;
									console.log(this.userInfo);
									
									isLoding = false;
									uni.hideLoading();
			
									// 下一步
									that.nextStep(true);
								}
							}
						});
					}, // end of login success
					fail: (res) => {
						isLoding = false;
						uni.hideLoading();
						uni.showToast({
							title: "login fail",
							icon: "none",
							duration: 1000
						})
					}
			
				});
			},
			
			onEmailInput(event) {
				email = event.target.value;
			},

			onCaptchaInput(event) {
				captcha = event.target.value;
			},

			/**
			 * true: 不匹配, false: 匹配
			 */
			checkUNNCEmail(str) {
				return !RegExp(/^\w+([-+.]\w+)*@nottingham\.edu\.cn+$/).test(str);
			},

			getCaptcha() {
				if (email) {
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

			confirmCode() {
				if (captcha) {
					uni.showLoading({
						title: "请等待"
					})
					uni.request({
						url: this.$serverUrl + '/user/confirmCode',
						method: "POST",
						data: {
							userId: this.userInfo.id,
							code: captcha,
							email: email,
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: (res) => {
							if (res.data.status == 200) {
								// 3.获取返回的用户信息
								var finalUser = res.data.data;
								// 4.分割邮箱地址, 重构 user
								this.userInfo = this.myUser(finalUser);
								console.log(this.userInfo);
								
								uni.showToast({
									title: '认证成功',
									icon: 'none'
								});
								this.changeAuth();
								this.nextStep(false);
								
							} else {
								console.log("验证失败 " + res.data.msg);
								uni.showToast({
									title: '验证失败',
									icon: 'none'
								});
							}
						},
						complete: () =>{
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
			
			login(){
				this.nextStep(false);
				uni.showLoading({
					title: "正在登陆..."
				});
				
				// 5.写入缓存
				this.setGlobalUserInfo(this.userInfo);
				
				setTimeout(()=>{
					uni.hideLoading();
					uni.redirectTo({
						url: "../index/index"
					});
				},1000)
			},
			
			/**
			 * 转场动画,需计算转场时间以保证进度条和块内容运动一致，
			 * 当前运动时间为 500 ms
			 * TODO: 暂未动态设置块移动距离,和同步运动时间 -Jerrio
			 * @param {Object} sync 是否控制“块”同步运动
			 */
			nextStep(sync) {
				this.active++;
				var point = 100.00 / this.activeList.length * this.active; //计算进度条节点 单位%
				// console.log(that.swiperLineWidth);
				// 进度条右滑
				clearInterval(timer_); //清空定时器，防止重复操作
				var that = this;
				timer_ = setInterval(function() {
					if (that.swiperLineWidth >= point) {
						clearInterval(timer_);
					} else {
						that.swiperLineWidth = that.swiperLineWidth + 2;
						// console.log(that.swiperLineWidth);
					}
				}, 20)

				if (sync) {
					// 块右滑
					clearInterval(timer); //清空定时器，防止重复操作
					var that = this;
					timer = setInterval(function() {
						if (that.swiperLeft <= -100) {
							clearInterval(timer);
						} else {
							that.swiperLeft = that.swiperLeft - 2;
							// console.log(that.swiperLeft);
						}
					}, 10)
				}
			},

			lastStep(sync) {
				this.active--;
				var point = 100.00 / this.activeList.length * this.active; //计算进度条节点 单位%
				// 进度条左滑
				clearInterval(timer_); //清空定时器，防止重复操作
				var that = this;
				timer_ = setInterval(function() {
					if (that.swiperLineWidth <= point) {
						clearInterval(timer_);
					} else {
						that.swiperLineWidth = that.swiperLineWidth - 2;
						// console.log(that.swiperLineWidth);
					}
				}, 20)

				if (sync) {
					// 块左滑
					clearInterval(timer); //清空定时器，防止重复操作
					var that = this;
					timer = setInterval(function() {
						if (that.swiperLeft >= 0) {
							clearInterval(timer);
						} else {
							that.swiperLeft = that.swiperLeft + 2;
							// console.log(that.swiperLeft);
						}
					}, 10)
				}
			},

			toUserDeal() {
				uni.navigateTo({
					url: "../userDeal/userDeal"
				});
			}
		}
	};
</script>

<style>
	page {
		width: 100%;
		height: 100%;
	}

	button::after {
		border: none;
		outline: none;
	}

	.wx-sign-checkbox {
		margin-left: 3%;
		height: 100%;
	}

	/* 按百分比分配父组件区域 */
	/* 如更改样式需重新计算百分比分配 */
	#signin-container {
		width: 100%;
		height: 100%;
		position: relative;
	}

	#introduction {
		position: absolute;
		display: flex;
		justify-content: space-around;
		top: 0%;
		width: 200%;
		height: 54%;
	}

	.email-Box {
		position: relative;
		width: 35%;
		height: 90%;
	}

	.email-intro {
		position: absolute;
		top: 15%;
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 30%;
	}

	.email-content {
		position: absolute;
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 50%;
		top: 50%;
	}

	.email-input {
		width: 100%;
		height: 40px;
		background-color: white;
		box-shadow: 0px 0px 5px 1px #C0C0C0;
		margin-top: 15px;
		font-size: 15px;
		color: #888888;
	}

	.introduction-contentBox {
		position: relative;
		width: 35%;
		height: 90%;
	}

	.introduction-content {
		position: absolute;
		left: 5%;
		top: 4%;
		width: 90%;
		height: 86%;
	}

	.introduction-bottom-sign {
		position: absolute;
		bottom: 0%;
		height: 10%;
		width: 100%;
		/* background-color: #000000; */
		/* opacity: 0.8; */
		border-bottom-right-radius: 30upx;
		border-bottom-left-radius: 30upx;
	}

	#confirm {
		position: absolute;
		top: 52%;
		width: 100%;
		height: 10%;
	}

	.confirm-rel {
		position: relative;
		width: 100%;
		height: 100%;
	}

	.confirm-button-before {
		position: absolute;
		top: 24%;
		left: 26%;
		width: 48%;
		height: 52%;
		border-radius: 10upx;
		background-color: #CCCCCC;
		z-index: 20;
	}

	.confirm-button-checked {
		position: absolute;
		top: 24%;
		left: 26%;
		width: 48%;
		height: 52%;
		border-radius: 10upx;
		background-color: #FFCD2E;
		z-index: 20;
	}

	.conform-bgBox {
		position: absolute;
		top: 24%;
		left: 26%;
		width: 48%;
		height: 52%;
		background-color: #ffcd2e;
		border-radius: 10upx;
		z-index: 10;
	}

	.bottom-picBox {
		position: absolute;
		bottom: 0;
		width: 100%;
		height: 26%;
	}

	.example-body {
		border-top: 1px #f5f5f5 solid;
		padding: 30upx;
		background: #fff
	}
</style>
