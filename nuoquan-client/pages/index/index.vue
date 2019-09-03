<template>
	<view class="index">
		<mainpagetop :userInfo='userInfo' :topArticles='topArticles' ></mainpagetop>
		<articlebrief v-for="i in showlist" :key="i.id" v-bind:articleCard="i"></articlebrief>
		<view style="margin:750upx auto 0;font-size:13px;text-align: center;">到~底~线~啦~！</view>
	</view>
</template>

<script>
	import articlebrief from '../../components/articlebrief';
	import mainpagetop from '../../components/mainpagetop.vue';
	import mainpageleft from '@/components/mainpageleft.vue'
	
	import {mapState} from 'vuex';
	
	export default {
		data() {
			return {
				title: 'Hello',
				hottitlelist: ['热门标题111', '热门标题222', '热门标题333'],
				showlist: '',
				topArticles: '',

				userInfo: { // 默认user设置
					id: 'test-id123',
					nickname: 'test-name',
					faceImg: '',
					faceImgThumb: '',
					email: 'zy22089@nottingham.edu.cn',
					emailPrefix: 'zy22089',
					emailSuffix: '@nottingham.edu.cn'
				},
				
			};
		},
		components: {
			articlebrief,
			mainpagetop,
			mainpageleft,
		},
		
		onLoad() {			
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.navigateTo({
					url: "../wechatLogin/wechatLogin"
				})
				return;
			}
			// 更新用户信息缓存... 查询用户信息，并分割邮箱更新到缓存
			this.queryUserInfo(userInfo.id)
			
			this.mySocket.init(); // 初始化 Socket, 离线调试请注释掉
			
			// [测试代码块]
		},
		onShow() {
			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				// 设置 userInfo 传给 mainpagetop 组件
				this.userInfo = this.getGlobalUserInfo();
			}
			
			this.showArticles(); // 显示文章流
			
			this.getTop3Articles(); // 获取热度榜
		},
		methods: {
			showArticles() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/queryAllArticles',
					method: "POST",
					success: (res) => {
						that.showlist = res.data.data.rows;
						// console.log(res)
					},
					fail: (res) => {
						console.log("index unirequest fail");
						console.log(res);
					}
				});
			},
			
			getTop3Articles(){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getHotTop3',
					method: "POST",
					success: (res) => {
						that.topArticles = res.data.data;
						console.log(res)
					}
				})
			},
			
			/**
			 * 查询用户信息，并分割邮箱更新到缓存
			 */
			queryUserInfo(userId){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/queryUser',
					method: "POST",
					data: {
						userId: userId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if(res.data.status == 200){
							var user = res.data.data;
							var finalUser = this.myUser(user);// 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							// console.log(this.userInfo);
						}
					},
				});
			}
		}
	};
</script>

<style>
	page{
		height:100%;
	}
	.index {
		background-color: #f3f3f3;
		height:100%;
	}

	image {
		width: 60px;
		height: 60px;
		display: inline-block;
	}

	.touxiang {
		border-radius: 30px;
	}

	.search {
		display: inline-block;
		width: 70%;
		box-shadow: inset 0 0 20px #ccc;
		border-radius: 20upx;
		margin-left: 50upx;
	}

	.hot {
		background-color: #e4e4e4;
		width: 100%;
	}

	.hotitem {
		margin-bottom: 20px;
		background: white;
		height: 27px;
		border-color: rgb(255, 255, 255);
		box-shadow: rgb(170, 170, 170) 0px 0px 5px 0px;
		font-size: 14px;
		padding: 0px;
		border-width: 1px;
		border-style: solid;
		text-align: left;
		line-height: 20px;
		font-weight: normal;
		font-style: normal;
	}

	.arrow {
		width: 100upx;
		height: 60upx;
	}
</style>
