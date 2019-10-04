<template>
	<view class="uni-common-mt">
		<view class="uni-form-item uni-column">
			<input class="uni-input" style="background-color: #A6A6A6;" type="number" placeholder="邮箱" @input="onEmailInput" />
			<input class="uni-input" style="background-color: #F0AD4E;" type="text" placeholder="验证码" @input="onCaptchaInput" :maxlength="captchaLength"/>
			<wh-captcha
				ref="captcha"
				:secord="60"
				:title="title"
				waitTitle="稍等(SECORD)秒"
				normalClass="captcha-normal"
				disabledClass="captcha-disabled"
				@click="getCaptcha"
			></wh-captcha>
			<button @click="confirmCode">提交认证</button>
		</view>
	</view>
</template>

<script>
	import whCaptcha from '../../components/wh-captcha/wh-captcha.vue';
	
	var util = require('../../common/util.js');
	
	var email; //输入的email值
	var captcha; //输入的captcha值
	export default {
		components: {
			whCaptcha
		},
		data() {
			return {
				captchaLength: 6,
				title: "获取验证码"
			}
		},
		onLoad() {
			
		},
		methods: {
			onEmailInput(event) {
				email = event.target.value;
			},
			
			onCaptchaInput(event) {
				captcha = event.target.value;
			},
			
			/**
			 * true: 不匹配, false: 匹配
			 */
			checkUNNCEmail(str){
				return !RegExp(/^\w+([-+.]\w+)*@nottingham\.edu\.cn+$/).test(str);
			},
			
			getCaptcha(){
				if(email){
					// 检测邮箱
					if(util.regEmail(email) || this.checkUNNCEmail(email)){
						uni.showToast({
							title: '非 UNNC 邮箱地址！',
							icon: 'none'
						});
					}else{
						if(this.$refs.captcha.canSend()){
							console.log("获取验证码 email=" + email);
							this.$refs.captcha.begin();
							this.title="重新发送"
							
							var that =this;
							uni.request({
								url: that.$serverUrl + '/user/getCode',
								method: "POST",
								data: {
									userId: that.getGlobalUserInfo().id,
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
				}else{
					uni.showToast({
						title: 'Email不能为空',
						icon: 'none'
					});
				}
			},
			
			confirmCode(){
				if(captcha){
					var that = this;
					uni.request({
						url: that.$serverUrl + '/user/confirmCode',
						method: "POST",
						data: {
							userId: that.getGlobalUserInfo().id,
							code: captcha,
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: (res) => {
							if(res.data.status == 200){
								console.log("验证邮箱成功，下一步");
							}else{
								console.log("验证失败 "+res.data.msg);
							}
						},
					});
				}else{
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
</style>
