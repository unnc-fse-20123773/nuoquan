<template>
	<view id="public-container">
		<!-- 黄色头部 -->
		<view id="yellowTop" :style="{ bottom: yellowBottom }"></view>
		<!-- 简介块 -->
		<view id="public-infoBox" class="column_center">
				<!-- 基本信息内容 -->
				<view id="idCard" class="idCard" :style="{ width: cardWidth }">
					<!-- ID -->
					<view class="nameBox super_center">
						<text class="name-text">{{ thisUserInfo.nickname }}</text>
					</view>
					<!-- 个人简介 -->
					<view class="introBox super_center"><text class="introBox-text">个人简介：这个人很懒，什么都没写哦...</text></view>
					<!-- 判断是否为本人主页 -->
					<view class="guanzhuBox super_center" v-if="!myPublic">
						<view class="guanzhuLine super_center">
							<view style="height: 100%;display: flex;">
							<!-- 关注&已关注 -->
							<button class="guanzhuButton super_center" @tap="addFollow(thisUserInfo.id)" v-if="thisUserInfo.follow == false">
								<text class="guanzhuButton-text">关注</text>
							</button>
							<button
								plain="true"
								class="guanzhuButton-after super_center"
								style="border:1px solid rgba(255,201,90,1);"
								@tap="cancelFollow(thisUserInfo.id)"
								v-if="thisUserInfo.follow == true"
							>
								<text class="guanzhuButton-text-after">已关注</text>
							</button>
							<!-- 私信 -->
							<view class="messageButton super_center" @tap="goToChatPage">
								<image
									src="../../static/icon/comment-personpublic.png"
									mode="scaleToFill"
									style="width:18px;
								height:18px;
								opacity:1;"
								></image>
							</view>
							</view>
						</view>
					</view>
					<!-- 操作行 -->
					<view class="operationLine">
						<!-- 粉丝 -->
						<view class="operationCard" @tap="goToFansFollow(1)">
							<view class="operationNum super_center">
								<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{thisUserInfo.fansNum}}</text>
							</view>
							<view class="operationTitle super_center">
								<text class="operationTitle-text">粉丝</text>
							</view>
						</view>
						<!-- 影响力 -->
						<view class="operationCard">
							<view class="operationNum super_center">
								<text class="operationNum-text" style="color:rgba(254,95,85,1);">999999</text>
							</view>
							<view class="operationTitle super_center">
								<text class="operationTitle-text">影响力</text>
							</view>
						</view>
						<!-- 关注 -->
						<view class="operationCard" @tap="goToFansFollow(0)">
							<view class="operationNum super_center">
								<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{thisUserInfo.followNum}}</text>
							</view>
							<view class="operationTitle super_center">
								<text class="operationTitle-text">关注</text>
							</view>
						</view>
					</view>
				</view>
		</view>
		<!-- 头像 -->
		<view style="width: 100%;height: 74px;position: fixed;z-index: 30;top: 16px;" class="super_center">
			<image class="publicTouxiang" mode="aspectFill" :src="thisUserInfo.faceImg"></image>
		</view>
		<!-- 信息块标题 -->
		<view id="public-titleBox" :style="{positionHelper}">
			<text class="public-title-text">他的信息</text>
		</view>
		<!-- 信息块 -->
		<view id="publix-profileLine" :style="{ width: cardWidth }">
			<view class="profileCard">
					<text class="profile-content-text">2020</text>
					<text class="profile-title-text">毕业年份</text>
			</view>
			<view class="profileCard">
					<text class="profile-content-text">PDM</text>
					<text class="profile-title-text">专业</text>
			</view>
		</view>
		<!-- 发布块标题 -->
		<view id="public-titleBox" :style="{positionHelper}">
			<text class="public-title-text">他的发布</text>
		</view>
		<!-- 发布块 -->
		
		<!-- <view id="public-message-futherbox">
			<scroll-view class="top-menu-view" scroll-x="true" scroll-left="scrollLeft">
				<block v-for="(menuTabs,index) in menuTabs" :key="index">
					<view class="menu-one-view" v-bind:id="'tabNum'+index" @click="swichMenu(index)">
						<view :class="[currentTab==index ? 'menu-one-act' : 'menu-one']">
							<view class="menu-one-txt">{{menuTabs.name}}</view>
							<view class="menu-one-bottom">
								<view class="menu-one-bottom-color"></view>
							</view>
						</view>
					</view>
				</block>
			</scroll-view>
			<swiper :current="currentTab" class="swiper-box-list" duration="300" @change="swiperChange">
				<block v-for="(swiperDate,index1) in swiperDateList" :key="index1">
					<swiper-item>
						<scroll-view class="swiper-one-list" scroll-y="true" @scrolltolower="loadMore(index1)">
							<view class="warning-text-box super_center">
								<view class="warning-text">
									Function under developed.
								</view>
							</view>
						</scroll-view>
					</swiper-item>
				</block>
			</swiper>
		</view> -->
	</view>
</template>

<script>
var me;

export default {
	data() {
		return {
			scrollLeft: 0,
			isClickChange: false,
			currentTab: 0,
			menuTabs: [
				{
					name: '动态'
				},
				{
					name: '帖子'
				},
				{
					name: '点赞'
				},
				{
					name: '评论'
				}
			],

			swiperDateList: [[], [], [], [], [], []],

			screenWidth: 350,
			serverUrl: '',

			thisUserInfo: '',
			myPublic: false,
			windowHeight: 0,
			windowWidth: 0,
			yellowBottom: '',
			cardWidth: '',
		};
	},

	changeIndicatorDots(e) {
		this.indicatorDots = !this.indicatorDots;
	},
	changeAutoplay(e) {
		this.autoplay = !this.autoplay;
	},
	intervalChange(e) {
		this.interval = e.target.value;
	},
	durationChange(e) {
		this.duration = e.target.value;
	},

	onLoad(opt) {
		var userId = opt.userId;

		me = this.getGlobalUserInfo();
		if (me.id == userId) {
			// 如果打开自己的页面，屏蔽关注和发私信按钮
			this.myPublic = true;
		}

		var screenWidth = uni.getSystemInfoSync().screenWidth;
		this.screenWidth = screenWidth;

		// 获取当前分页
		var page = this.page;

		// 获取这个人的信息, TODO: 更新本地用户信息缓存
		this.queryUserWithFollow(userId);

		// [测试代码块]
		// this.mySocket.init()

		//获取屏幕宽高
		var that = this;
		uni.getSystemInfo({
			success: function(res) {
				that.windowHeight = res.windowHeight;
				that.windowWidth = res.windowWidth;
			}
		});
		//获取黄色头部位置
		that.yellowBottom = that.windowHeight - 150 * (that.windowHeight / 720) + 'px';
		console.log(that.yellowBottom);
		
		// 获取卡片宽度
		that.cardWidth = that.windowWidth - 26 + 'px';
		console.log(that.cardWidth);
	},

	onPullDownRefresh() {
		console.log('refresh');
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 1000);
	},

	methods: {
		swichMenu: async function(current) {
			//点击其中一个 menu
			if (this.currentTab == current) {
				return false;
			} else {
				this.currentTab = current;
				this.setScrollLeft(current);
			}
		},
		swiperChange: async function(e) {
			let index = e.target.current;
			this.setScrollLeft(index);
			this.currentTab = index;
		},
		setScrollLeft: async function(tabIndex) {
			let leftWidthSum = 0;
			for (var i = 0; i <= tabIndex; i++) {
				let nowElement = await this.getWidth('tabNum' + i);
				leftWidthSum = leftWidthSum + nowElement.width;
			}
			let winWidth = uni.getSystemInfoSync().windowWidth;
			this.scrollLeft = leftWidthSum > winWidth ? leftWidthSum - winWidth : 0;
		},
		getWidth: function(id) {
			//得到元素的宽高
			return new Promise((res, rej) => {
				uni.createSelectorQuery()
					.select('#' + id)
					.fields(
						{
							size: true,
							scrollOffset: true
						},
						data => {
							res(data);
						}
					)
					.exec();
			});
		},
		loadMore: function(tabIndex) {
			console.log('正在加载更多数据。。。');
			this.getDateList(tabIndex);
		},
		/**
		 * 添加关注
		 */
		addFollow: function(userId) {
			console.log('加关注...');
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/follow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if (res.data.status == 200) {
						// 刷新用户信息
						that.queryUserWithFollow(userId);
					}
				}
			});
		},
		/**
		 * 取消关注
		 */
		cancelFollow: function(userId) {
			console.log('取关...');
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/dontFollow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if (res.data.status == 200) {
						// 刷新用户信息
						that.queryUserWithFollow(userId);
					}
				}
			});
		},

		goToChatPage: function() {
			var friendInfo = this.thisUserInfo;
			uni.redirectTo({
				url: '../chatpage/chatpage?friendInfo=' + JSON.stringify(friendInfo)
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
			uni.redirectTo({
				url: '../followlist/followlist?data=' + JSON.stringify(data)
			});
		},

		/**
		 * 查询该用户信息和我是否关注该用户
		 * @param {Object} userId 该用户 id
		 */
		queryUserWithFollow: function(userId) {
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/queryUserWithFollow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					// console.log(res)
					if (res.data.status == 200) {
						that.thisUserInfo = res.data.data;
						that.setUserInfoToUserList(res.data.data); //更新缓存
						// console.log(res.data.data)
						// console.log(that.getUserInfoFromUserList(res.data.data.id))
						// console.log(that.getListByKey("userList"))

						// 设置title
						uni.setNavigationBarTitle({
							title: that.thisUserInfo.nickname + '的主页'
						});
					}
				}
			});
		}
	}
};
</script>

<style>
page {
	width: 100%;
	height: 100%;
	overflow: scroll;
}

#public-container {
	height: 100%;
	width: 100%;
}

/* 黄色头部 */
#yellowTop {
	position: fixed; /* 此处采用 fixed 定位，以保证页面流可以正常显示。层级设为 -10，以保证其显示在页面最底部。 */
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

.public-infoBox-rel {
	width: 100%;
	height: 100%;
	position: relative;
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
	margin-top: 30px;
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
	word-break: keep-all;
	word-wrap: break-word;
}

.guanzhuLine {
	margin-top: 17px;
	height: 26px;
	width: 100%;
	display: flex;
}

.guanzhuButton {
	/* margin-left: 35.2%; */
	margin-right: 0px;
	width: 68px;
	height: 26px;
	background-color: rgba(255, 201, 90, 1);
	box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 4px;
}

.guanzhuButton-after {
	/* margin-left: 31.2%; */
	margin-right: 0px;
	width: 82px;
	height: 26px;
	opacity: 1;
	border-radius: 4px;
}

.guanzhuButton-text {
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(255, 255, 255, 1);
	opacity: 1;
}

.guanzhuButton-text-after {
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 13px;
	color: rgba(255, 201, 90, 1);
	opacity: 1;
}

.messageButton {
	margin-left: 8px;
	margin-top: -1px;
	width: 28px;
	height: 28px;
	background: rgba(255, 241, 213, 1);
	border-radius: 14px;
	opacity: 1;
}

.operationLine {
	margin: 17px 3.2%;
	width: 93.6%;
	height: 43px;
	display: flex;
	justify-content: space-between;
}

.operationCard{
	width: 33.3%;
	height: 43px;
}

.operationNum{
	width: 100%;
	height: 17px;
	overflow: visible;
}

.operationNum-text{
	font-size:17px;
	font-family:Source Han Sans CN;
	font-weight:800;
	opacity:1;
}

.operationTitle{
	margin-top: 11px;
	width: 100%;
	height: 11px;
	overflow: visible;
}

.operationTitle-text{
	font-size:10px;
	font-family:Source Han Sans CN;
	font-weight:500;
	color:rgba(178,178,178,1);
	opacity:1;
}

/* 他的信息标题 */
#public-titleBox{
	width: 100%;
	margin-top: 20px;
}

.public-title-text{
	margin-left: 13px;
	font-size:17px;
	font-family:Source Han Sans CN;
	font-weight:bold;
	color:rgba(136,136,136,1);
	opacity:1;
}

/* 他的信息块 */
#publix-profileLine{
	margin-top: 12px;
	margin-left: 13px;
	border-radius: 8px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
	display: flex;
	justify-content: space-between;
}

.profileCard{
	width: 50%;
	display: flex;
	flex-direction: column;
}

.profile-content-text{
	margin-top: 16px;
	margin-left: 38px;
	font-size:17px;
	font-family:Source Han Sans CN;
	font-weight:500;
	color:rgba(53,53,53,1);
	opacity:1;
}

.profile-title-text{
	margin-bottom: 16px;
	margin-top: 8px;
	margin-left: 38px;
	font-size:10px;
	font-family:Source Han Sans CN;
	font-weight:500;
	color:rgba(178,178,178,1);
	opacity:1;
}

#public-message-futherbox

	/* 这里是帖子块最高级父组件*/
		 {
	/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
	position: fixed;
	top: 20%;
	width: 100%;
	height: 80%;
	/* 此处为背景半径，若要更改帖子块上方半径，请同时更改此处和下面导航条处 */
	border-top-left-radius: 50upx;
	border-top-right-radius: 50upx;
	box-shadow: 0upx -1upx 8upx 1upx #a6a6a6;
}

/* fengexian 以下是旧代码 */




/* 以下是帖子展示块的样式 */

.top-menu-view {
	display: flex;
	justify-content: space-around;
	width: 100%;
	background-color: white;
	height: 8%;
	/* 在这里设置导航条高度 */
	border-top-left-radius: 50upx;
	border-top-right-radius: 50upx;
}

.menu-one-view {
	display: inline-block;
	white-space: nowrap;
	height: 100%;
	width: 24%;
}

.top-menu-view .menu-one-view .menu-one {
	/* 在这里写 单个按钮样式 */
	margin-left: 8%;
	position: relative;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
}

.top-menu-view .menu-one-view .menu-one .menu-one-txt {
	height: 40upx;
	font-size: 32upx;
	font-weight: 550;
	color: #888888;
	line-height: 40upx;
}

.top-menu-view .menu-one-view .menu-one .menu-one-bottom {
	position: absolute;
	bottom: 0;
	width: 100%;
}

.top-menu-view .menu-one-view .menu-one .menu-one-bottom .menu-one-bottom-color {
	width: 60%;
	height: 4upx;
}

.top-menu-view .menu-one-view .menu-one-act {
	/* 在这里写 单个按钮样式 */
	margin-left: 8%;
	position: relative;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
}

.top-menu-view .menu-one-view .menu-one-act .menu-one-txt {
	height: 40upx;
	font-size: 36upx;
	font-weight: 550;
	color: #353535;
	line-height: 40upx;
}

.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom {
	/* 在这里设置底部横条宽度 */
	position: absolute;
	bottom: 0;
	width: 80%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color {
	/* 在这里设置底部横条高度和颜色 */
	width: 100%;
	height: 6upx;
	background: #ffcf3c;
}

.swiper-box-list {
	flex: 1;
	height: 92%;
	width: 100%;
	background-color: red;
}

.swiper-one-list {
	height: 100%;
	width: 100%;
}

/* 	.swiper-one-list .swiper-list-entity {
		width: 100%;
		height: 112upx;
		text-align: center;
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		justify-content: center;
	} */

/* swiper */

.swiper {
	height: 360upx;
}

.slideimage {
	width: 100%;
}

/* under development 盖板 */
.warning-text-box {
	height: 100%;
	background-color: #d5d5d5;
}

.warning-text-box .warning-text {
	color: black;
	font-size: 35upx;
}
</style>
