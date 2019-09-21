<template>
	<view class="signinpage">
		<view class="spacearea"></view>
		<image src="../../static/touxiang.jpg" class="touxiang"></image>
		<image class="fengexian" src="../../static/icon/signinline.png"></image>
		<view class="mailbox">
			<input class="prefix" />
			<text class="suffix">@nottingham.edu.cn</text>
		</view>
		<view class="verifycode">
			<input class="code" />
			<button>发送验证码</button>
		</view>
		
		<!-- 登录button，弹出微信授权界面 -->
		<button class="binding" open-type="getUserInfo" @getuserinfo="getUserInfo" withCredentials="true">微信登陆</button>
		
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	methods: {
		/**
		 * 微信小程序登陆
		 */
		getUserInfo() {
			var that = this;
			uni.login({
				success: res_login => {
					// console.log('-------res_login，获取code-------')
					// console.log(res_login);
					uni.getUserInfo({
						success: info => {
							// console.log('-------获取sessionKey、openid(unionid)-------')
							// console.log(info);
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
										// 3.获取返回的用户信息
										var finalUser = res.data.data;
										// 4.分割邮箱地址, 重构 user
										finalUser = this.myUser(finalUser);
										// 5.写入缓存
										this.setGlobalUserInfo(finalUser);
										console.log(finalUser);
		
										// 6.返回
										uni.navigateBack({
											delta: 1
										});
									}
								}
							});
						}
					});
				}
			});
		},
	}
};
</script>

<style>
.signinpage {
	width: 100%;
	margin: auto;
}
.spacearea {
	height: 150px;
	display: block;
	width: 750upx;
}
.touxiang {
	height: 100px;
	width: 100px;
	border-radius: 50px;
	display: block;
	margin: auto;
}
.fengexian {
	margin-top: 50px;
	height: 50px;
}
.mailbox {
	display: block;
	margin: auto;
	width:210px;
}
.prefix {
	width: 60px;
	display: inline-block;
}
.suffix {
	font-size: 12px;
	color: #d6d6d6;
	display: inline-block;
	width:150px;
}
.verifycode {
	display: block;
	margin: auto;
	width:120px;
	font-size: 14px;
}
.code{
	display: inline-block;
	width:80px;
}
.verifycode button{
	width:120px;
	font-size: 14px;
	
	background: RGB(253, 217, 108);
}
</style>
