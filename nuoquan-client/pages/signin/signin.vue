<template>
	<view id="signin-container">
		<!-- Logo 框 -->
		<view id="icon" class="super_center">
				<view class="icon-logoBox super_center">
					<image src="../../static/icon/logo_app.png" mode="aspectFill" class="icon-logo"></image>
				</view>
		</view>
		<!-- 简介 -->
		<view id="introduction" class="super_center">
			<view class="introduction-contentBox super_center">
				<scroll-view scroll-y="true" class="introduction-content">
					<!-- 此处待完工 -Guetta -->
					<text>
					{{introduction_text1}}
					{{introduction_text2}}
					{{introduction_text3}}
					</text>
					<!-- ! -->
				</scroll-view>
			</view>
		</view>
		<view id="confirm" class="super_center">
			<view class="confirm-rel">
			<button class="confirm-button super_center" hover-class="button-hover" open-type="getUserInfo" @getuserinfo="getUserInfo" >
				<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">确认绑定</view>
			</button>
			<view class="conform-bgBox"></view>
			</view>
		</view>
		
		<image src="../../static/BG/signin_bottom.png" mode="scaleToFill" class="bottom-picBox"></image>
	</view>
</template>

<script>
export default {
	data() {
		return {
			introduction_text1 : 'Nottinghome1111是XXXXXXXXXXX',
			introduction_text2: 'Nottinghome是XXXXXXXXXXX哈哈哈哈哈哈哈哈哈',
			introduction_text3 : 'Nottinghome是XXXXXXXXXXX我看看长啥样',
		};
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
		
										// 6.返回 发出重载事件
										uni.$emit("reloadIndex");
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
	page{
		width: 100%;
		height: 100%;
	}
	
	button::after{ 
		border: none;
		outline:none;
		}
	/* 按百分比分配父组件区域 */
	/* 如更改样式需重新计算百分比分配 */
	#signin-container{
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	#icon{
		position: absolute;
		top: 4%;
		width: 100%;
		height: 16%;
	}
	
	.icon-logoBox{
		height: 80px;
		width: 80px;
		box-shadow: 0px 0px 10px 1px #CCCCCC;
		border-radius: 30upx;
	}
	
	.icon-logo{
		height: 70px;
		width: 70px;
	}
	
	#introduction{
		position: absolute;
		top: 20%;
		width: 100%;
		height: 40%;
	}
	
	.introduction-contentBox{
		width: 70%;
		height: 90%;
		background-color: #EEEEEE;
		opacity: 0.5;
		border-radius: 30upx;
	}
	
	.introduction-content{
		width: 90%;
		height: 90%;
		background-color: #09BB07;
	}
	
	#confirm{
		position: absolute;
		top: 62%;
		width: 100%;
		height: 10%;
	}
	
	.confirm-rel{
		position: relative;
		width: 100%;
		height: 100%;
	}
	
	.confirm-button{
		position: absolute;
		top: 24%;
		left: 26%;
		width: 48%;
		height: 52%;
		border-radius: 10upx;
		background-color: #FFCD2E;
		z-index: 20;
	}
	
	.conform-bgBox{
		position: absolute;
		top: 24%;
		left: 26%;
		width: 48%;
		height: 52%;
		background-color: #ffcd2e;
		border-radius: 10upx;
		z-index: 10;
	}
	
	.bottom-picBox{
		position: absolute;
		bottom: 0;
		width: 100%;
		height: 26%;
	}

</style>
