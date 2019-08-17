<template>
	<view>
		<button type="primary" open-type="getUserInfo" @getuserinfo="getUserInfo" withCredentials="true">微信登陆授权</button>

		<!-- 用于开发阶段清除用户信息的缓存，发布时需要注释掉 -->
		<button type="primary" @tap="removeUserInfo">[dev]清除用户信息缓存</button>
		<!-- 用于开发阶段清除所有缓存，发布时需要注释掉 -->
		<button type="primary" @tap="clearStorage">[dev]清除缓存</button>
	</view>
</template>

<script>
	/* User Example
	 userInfo: {
	 	id: 'test-id123',
	 	nickname: 'test-name',
	 	faceImg: '../static/touxiang.jpg',
	 	faceImgThumb: '../static/touxiang.jpg',
	 	email: 'zy22089@nottingham.edu.cn',
	 	emailPrefix: 'zy22089',
	 	emailSuffix: '@nottingham.edu.cn'
	}*/
	
	export default {
		data() {
			return {

			}
		},

		onLoad() {

		},
		methods: {
			/**
			 * 微信小程序登陆
			 */
			getUserInfo() {
				var that = this;
				uni.login({
					success: res_login => {
						console.log('-------获取code-------')
						console.log(res_login.code);
						uni.getUserInfo({
							success: info => {
								console.log('-------获取sessionKey、openid(unionid)-------')
								console.log(info);
								that.setUser(info.userInfo);
							}
						});
					}
				});
			},
			
			setUser(wUserInfo) {
				// 1.微信用户信息 用于上传
				var weUser = {
					// id: wUserInfo.openid
					id: 'test-id123',
					nickname: wUserInfo.nickName,
					faceImg: wUserInfo.avatarUrl
				}
				console.log(weUser)
				var finalUser;
				// 2.把微信信息上传给服务器
				var that = this;
				uni.request({
					url: 'http://127.0.0.1:8080/user/updateUser',
					method: "POST",
					data: JSON.stringify(weUser),
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						console.log(res)
						if(res.data.status == 200){
							// 3.获取返回的用户信息
							finalUser = res.data.data;
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
			},
			
			removeUserInfo() {
				this.removeGlobalUserInfo();
				console.log("用户信息缓存已清除")
			},
			
			clearStorage(){
				uni.clearStorage();
				console.log("所有缓存已清除")
			}
		}
	}
</script>

<style>

</style>
