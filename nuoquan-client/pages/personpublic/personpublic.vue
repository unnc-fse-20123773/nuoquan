<template>
	<view id="public-container">
		<!-- 黄色头部 -->
		<view id="yellowTopBox">
			<view class="yellowTop" :style="{ bottom: yellowBottom }"></view>
			<!-- <view class="yellowTop" style="bottom: -240rpx"></view> -->
		</view>
		<!-- 简介块 -->
		<view id="public-infoBox" class="column_center">
			<!-- 基本信息内容 -->
			<view id="idCard" class="idCard" :style="{ width: cardWidth }">
				<view style="width: 100%;height: 74px;margin-top: -46px;" class="super_center">
					<image class="publicTouxiang" mode="aspectFill" :src="thisUserInfo.faceImg"></image>
				</view>
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
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.fansNum }}</text>
						</view>
						<view class="operationTitle super_center"><text class="operationTitle-text">粉丝</text></view>
					</view>
					<!-- 影响力 -->
					<view class="operationCard">
						<view class="operationNum super_center"><text class="operationNum-text" style="color:rgba(254,95,85,1);">999999</text></view>
						<view class="operationTitle super_center"><text class="operationTitle-text">影响力</text></view>
					</view>
					<!-- 关注 -->
					<view class="operationCard" @tap="goToFansFollow(0)">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.followNum }}</text>
						</view>
						<view class="operationTitle super_center"><text class="operationTitle-text">关注</text></view>
					</view>
				</view>
			</view>
		</view>
		<!-- 信息块标题 -->
		<view id="public-titleBox"><text class="public-title-text">他的信息</text></view>
		<!-- 信息块 -->
		<view id="publix-profileLine" :style="{ width: cardWidth }">
			<view class="profileCard">
				<!-- 检测是否设置 -->
				<text v-if="thisUserInfo.graduationYear != null" class="profile-content-text">{{ thisUserInfo.graduationYear }}</text>
				<text v-else class="profile-content-text">未知</text>
				<text class="profile-title-text">毕业年份</text>
			</view>
			<view class="profileCard">
				<!-- 检测是否设置 -->
				<text v-if="thisUserInfo.major != null" class="profile-content-text">{{ thisUserInfo.major }}</text>
				<text v-else class="profile-content-text">未知</text>
				<text class="profile-title-text">专业</text>
			</view>
		</view>
		<!-- 发布块标题 -->
		<view id="public-titleBox"><text class="public-title-text">他的发布</text></view>
		<!-- 有图发布块 -->
		<view id="public-articleCard" :style="{ width: cardWidth }">
			<view class="articleTitle">这里是一个标题这里是一个标题这里是一个标题这里是一个标题</view>
			<!-- 发布内容行 -->
			<view class="articleContentLine">
				<!-- 文字部分卡片 -->
				<!-- 有图跟无图只有 teamareaCard 这一个 class 的区别，判断只需要更改这一个 class -->
				<view class="textareaCard-img">
					<view class="textContent">
						这里是文字的内容这里是文字的内容这里是文字的内容这里是文字的内容这里是文字的内容这里是文字的内容这里是文字的内容容这里是文字的内容这里是文字的内容
					</view>
					<!-- 底部栏 -->
					<view class="bottomBar">
						<view style="position: relative;width: 100%;height: 100%;">
							<view class="bottom-time column_center"><text>2018.01.01 18:18</text></view>
							<view class="view column_center">
								<image src="../../static/icon/eye-888888.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
							<view class="comment column_center">
								<image src="../../static/icon/comment-alt.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
							<view class="like column_center">
								<image src="../../static/icon/like.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
						</view>
					</view>
				</view>
				<!-- 图片部分盒子 -->
				<view class="imgBox"><image mode="aspectFill" src="../../static/BG/submitBG.jpg"></image></view>
			</view>
		</view>
		<!-- 无图发布块 -->
		<view id="public-articleCard" :style="{ width: cardWidth }">
			<view class="articleTitle">这里是一个标题这里是一个标题这里是一个标题这里是一个标题</view>
			<!-- 发布内容行 -->
			<view class="articleContentLine">
				<!-- 文字部分卡片 -->
				<view class="textareaCard">
					<view class="textContent">这里是文字的内容这里是文字的内内容</view>
					<!-- 底部栏 -->
					<view class="bottomBar">
						<view style="position: relative;width: 100%;height: 100%;">
							<view class="bottom-time column_center"><text>2018.01.01 18:18</text></view>
							<view class="view column_center">
								<image src="../../static/icon/eye-888888.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
							<view class="comment column_center">
								<image src="../../static/icon/comment-alt.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
							<view class="like column_center">
								<image src="../../static/icon/like.png" mode="aspectFill"></image>
								<text>99+</text>
							</view>
						</view>
					</view>
				</view>
				<!-- 图片部分盒子 -->
				<!-- <view class="imgBox">
					<image mode="aspectFill" src="../../static/BG/submitBG.jpg"></image>
				</view> -->
			</view>
		</view>
		<!-- 占位块 -->
		<view style="height: 30px;" :style="{ width: cardWidth }"></view>
	</view>
</template>

<script>
var me;

export default {
	data() {
		return {
			screenWidth: 350,
			serverUrl: '',

			thisUserInfo: '',
			myPublic: false,
			windowHeight: 0,
			windowWidth: 0,
			yellowBottom: '',
			cardWidth: '',
			
			loadArticleFlag:false,
			userInfo: '',
			totalPage: 1,
			currentPage: 1,
			
			totalNum: '0',
			binNum: '12',
			myArticleList: "",
		};
	},

	onLoad(opt) {
		var userId = opt.userId;
		console.log(userId);
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
		if (that.windowHeight <= 1000){
			if (that.windowHeight < 667 ){
			that.yellowBottom = -that.windowHeight*0.25 + 'px';
			// console.log("超小屏幕，黄色头部上移了" + that.yellowBottom);
			}else{
			that.yellowBottom = -that.windowHeight*0.22 + 'px';
			// console.log("手机屏幕，黄色头部上移了" + that.yellowBottom);
			}
		}else{
			that.yellowBottom = -that.windowHeight*0.20 + 'px';
			// console.log("平板屏幕，黄色头部上移了" + that.yellowBottom);
		}		
		
		// 获取卡片宽度
		that.cardWidth = that.windowWidth - 26 + 'px';
		
		this.showArticles(page);
		console.log(this);
	},

	onPullDownRefresh() {
		console.log('refresh');
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 1000);
	},

	methods: {
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
}

#public-container {
	height: 100%;
	width: 100%;
}

/* 黄色头部 */
#yellowTopBox {
	width: 100%;
	position: relative;
	z-index: -30;
}

.yellowTop {
	position: absolute; /* 此处采用 fixed 定位，以保证页面流可以正常显示。层级设为 -10，以保证其显示在页面最底部。 */
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

/* 标题 */
#public-titleBox {
	width: 100%;
	margin-top: 20px;
	margin-bottom: 12px;
}

.public-title-text {
	margin-left: 13px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: bold;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
	letter-spacing: 1px;
}

/* 他的信息块 */
#publix-profileLine {
	margin-left: 13px;
	border-radius: 8px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
	display: flex;
	justify-content: space-between;
}

.profileCard {
	width: 50%;
	display: flex;
	flex-direction: column;
}

.profile-content-text {
	margin-top: 16px;
	margin-left: 38px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(53, 53, 53, 1);
	opacity: 1;
}

.profile-title-text {
	margin-bottom: 16px;
	margin-top: 8px;
	margin-left: 38px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(178, 178, 178, 1);
	opacity: 1;
}

/* 他的发布文章块 */
#public-articleCard {
	margin-bottom: 8px;
	max-height: 114px;
	margin-left: 13px;
	padding: 12px 0px;
	border-radius: 8px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}

.articleTitle {
	width: calc(100% - 32px);
	margin-left: 16px;
	font-size: 15px;
	font-family: Source Han Sans CN;
	font-weight: 550;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	line-height: 17px;
}

.articleContentLine {
	margin-top: 12px;
	width: calc(100% - 32px);
	margin-left: 16px;
	display: flex;
}

.textareaCard {
	position: relative;
	min-height: 34px;
	max-height: 63px;
	width: 100%;
}

.textareaCard-img {
	position: relative;
	height: 63px;
	width: calc(100% - 61px);
	margin-right: 12px;
}

.textContent {
	position: absolute;
	top: 0;
	width: 100%;
	max-height: 39px;
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 13px;
	color: rgba(53, 53, 53, 1);
	opacity: 1;

	word-break: break-all;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
}

.bottomBar {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 12px;
}

.bottomBar image {
	height: 12px;
	width: 12px;
}

.bottomBar text {
	margin-left: 4px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
}

.bottom-time {
	position: absolute;
	width: 84px;
	height: 12px;
}

.bottomBar .bottom-time text {
	margin-left: 0px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
	white-space: nowrap;
}

.view {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 86px;
}

.comment {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 43px;
}

.like {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 0;
}

.imgBox {
	width: 61px;
	height: 61px;
}

.imgBox image {
	width: 61px;
	height: 61px;
}
</style>
