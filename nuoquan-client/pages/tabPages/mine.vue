<template>
	<view id="public-container">
		<!-- 黄色头部 -->
		<!-- <view id="yellowTopBox"> -->
			<!-- <view class="yellowTop" :style="{ bottom: yellowBottom }"></view> -->
			<!-- <view class="yellowTop" style="bottom: -240rpx"></view> -->
		<!-- </view> -->
		<!-- 黄色背景 -->
		<view id="msglist-yellowshadowbg">
			<image src="../../static/BG/msgList_BG.png" mode="scaleToFill"></image>
		</view>
		<!-- 简介块 -->
		<view id="public-infoBox" class="column_center">
			<!-- 基本信息内容 -->
			<view id="idCard" class="idCard" :style="{ width: cardWidth }">
				<view style="width: 100%;height: 74px;margin-top: -46px;" class="super_center">
					<image class="publicTouxiang" mode="aspectFill" :src="pathFilter(thisUserInfo.faceImg)"></image>
				</view>
				<!-- ID -->
				<view class="nameBox super_center">
					<text class="name-text">{{ thisUserInfo.nickname }}</text>
				</view>
				<!-- 个人简介 -->
				<view class="introBox super_center"><text class="introBox-text">个人简介：这个人很懒，什么都没写哦...</text></view>
				<!-- 操作行 -->
				<view class="operationLine">
					<!-- 关注 -->
					<view class="operationCard" @tap="goToFansFollow(0)">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.followNum }}</text>
						</view>
						<view class="operationTitle super_center">
							<text class="operationTitle-text">{{ lang.follow }}</text>
						</view>
					</view>
					<!-- 影响力 -->
					<view class="operationCard">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:rgba(254,95,85,1);">{{ thisUserInfo.reputation }}</text>
						</view>
						<view class="operationTitle super_center">
							<text class="operationTitle-text">{{ lang.reputation }}</text>
						</view>
					</view>
					<!-- 粉丝 -->
					<view class="operationCard" @tap="goToFansFollow(1)">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.fansNum }}</text>
						</view>
						<view class="operationTitle super_center">
							<text class="operationTitle-text">{{ lang.fans }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="pagejump_box">
			<!-- TODO:这里应该有更灵活优雅的写法 @jerrio -->
			<view class="item1 column_center" @click="jumpToProfile()">{{ lang.profile }}</view>
			<view class="line"></view>
			<view class="item2 column_center" @click="jumpToMyPublish()">{{ lang.myPublish }}</view>
			<view class="line"></view>
			<view class="item3 column_center" @click="jumpToAbout()">{{ lang.about }}</view>
			<view class="line"></view>
			<view class="language column_center" @tap="changeLang">{{lang.changeLang}}</view>
		</view>
		
		<tab-bar :current="4"></tab-bar>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';

export default {
	data() {
		return {
			screenWidth: 350,

			thisUserInfo: '',
			windowHeight: 0,
			windowWidth: 0,
			yellowBottom: '',
			cardWidth: ''
		};
	},
	components: {
		tabBar
	},

	computed: {
		...mapState(['lang'])
	},

	onLoad() {
		this.thisUserInfo = this.getGlobalUserInfo();

		var screenWidth = uni.getSystemInfoSync().screenWidth;
		this.screenWidth = screenWidth;

		// 获取当前分页
		var page = this.page;

		//获取屏幕宽高
		var that = this;
		uni.getSystemInfo({
			success: function(res) {
				that.windowHeight = res.windowHeight;
				that.windowWidth = res.windowWidth;
			}
		});
		//获取黄色头部位置
		if (that.windowHeight <= 1000) {
			if (that.windowHeight < 667) {
				that.yellowBottom = -that.windowHeight * 0.25 + 'px';
				// console.log("超小屏幕，黄色头部上移了" + that.yellowBottom);
			} else {
				that.yellowBottom = -that.windowHeight * 0.22 + 'px';
				// console.log("手机屏幕，黄色头部上移了" + that.yellowBottom);
			}
		} else {
			that.yellowBottom = -that.windowHeight * 0.2 + 'px';
			// console.log("平板屏幕，黄色头部上移了" + that.yellowBottom);
		}

		// 获取卡片宽度
		that.cardWidth = that.windowWidth - 26 + 'px';
	},

	onShow() {
		//更新用户数据
		console.log('更新用户数据');
		this.queryUserInfo(this.thisUserInfo.id);
	},

	onPullDownRefresh() {
		console.log('refresh');
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 1000);
	},

	methods: {
		...mapMutations(['changeLang']),
		
		/**
		 * 查询用户信息，并分割邮箱更新到缓存
		 */
		queryUserInfo(userId) {
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/queryUser',
				method: 'POST',
				data: {
					userId: userId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					if (res.data.status == 200) {
						var user = res.data.data;
						var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
						this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
						this.thisUserInfo = finalUser; // 更新页面用户数据
						// console.log(this.thisUserInfo);
					}
				}
			});
		},

		//粉丝数是否改变
		isFansNumChange() {
			//TODO: 多了就加小红点获其他动效
		},

		jumpToAbout: function() {
			uni.navigateTo({
				url: '../about/about'
			});
		},
		jumpToMyPublish: function() {
			uni.navigateTo({
				url: '../myPublish/myPublish'
			});
		},
		jumpToProfile: function() {
			uni.navigateTo({
				url: '../profile/profile'
			});
		},
		goToChatPage: function() {
			var encodeData = encodeURIComponent(JSON.stringify(this.thisUserInfo)); // 对数据字符串化并转码，防止特殊字符影响传参
			uni.navigateTo({
				url: '../chatpage/chatpage?friendInfo=' + encodeData
			});
		},

		/**
		 * @param {Object} currentTab 0: 关注 1: 粉丝
		 */
		goToFansFollow: function(currentTab) {
			console.log('goToFansFollow...');
			var data = {
				currentTab: currentTab,
				thisUserInfo: this.thisUserInfo
			};
			var encodeData = encodeURIComponent(JSON.stringify(data)); // 对数据字符串化并转码，防止特殊字符影响传参
			uni.navigateTo({
				url: '../followlist/followlist?data=' + encodeData
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

#public-container {
	height: 100%;
	width: 100%;
	margin-top: 30px;
	overflow: hidden;
}

/* 黄色头部 */
#yellowTopBox {
	width: 100%;
	position: relative;
	z-index: -30;
}

#msglist-yellowshadowbg {
		position: fixed;
		top: 0;
		width: 100%;
		height: 200px;
		z-index: -10;
	}

.yellowTop {
	position: absolute; /* 此处采用 absolote 定位，以保证页面流可以正常显示。层级设为 -10，以保证其显示在页面最底部。 */
	left: -430upx;
	height: 1600upx;
	width: 1600upx;
	z-index: -10;
	border-radius: 3000upx;
	background-color: #ffc95a;
}

/* 简介块 */
#public-infoBox {
	width: 100%;
}

.publicTouxiang {
	width: 74px;
	height: 74px;
	border-radius: 120px;
	border: 4px solid white;
	display: inline-block;
	vertical-align: middle;
}

.idCard {
	margin-top: 64px;
	margin-left: 13px;
	border-radius: 8px;
	min-height: 140px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}

.nameBox {
	margin-top: 8px;
	width: 100%;
}

.nameBox .name-text {
	font-size: 17px;
	font-weight: 600;
}

.introBox {
	margin-top: 12px;
	width: 100%;
}

.introBox-text {
	max-width: 61%;
	font-size: 12px;
	font-weight: 400;
	line-height: 13px;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
	word-wrap: break-word;
}

.operationLine {
	margin: 17px 3.2%;
	width: 93.6%;
	height: 43px;
	display: flex;
	justify-content: space-between;
}

.operationCard {
	width: 33.3%;
	height: 43px;
}

.operationNum {
	width: 100%;
	height: 17px;
	overflow: visible;
}

.operationNum-text {
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 800;
	opacity: 1;
}

.operationTitle {
	margin-top: 11px;
	width: 100%;
	height: 11px;
	overflow: visible;
}

.operationTitle-text {
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(178, 178, 178, 1);
	opacity: 1;
}
.pagejump_box {
	margin-top:13px;
	margin-left: 13px;
	width: calc(100% - 26px);
	background: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 8px;
}

.item1,.item2,.item3,.language {
	width: calc(100% - 28px)%;
	height: 50px;
	padding-left: 28px;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 16px;
	color: rgba(53, 53, 53, 1);
	opacity: 1;
}

.line {
	margin-left: auto;
	margin-right: auto;
	width: 88.54%;
	height: 0px;
	border: 1px solid rgba(236, 236, 236, 1);
	opacity: 1;
}

</style>
