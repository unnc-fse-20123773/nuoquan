<template>
	<view id="public-container">
		<view id="public-infobox" class="column_center">
			<image class="publicTouxiang" mode="aspectFill" :src="thisUserInfo.faceImg" ></image>
			<view class="personInfo">
				<view class="personName">
					<text class="personName-text">{{thisUserInfo.nickname}}</text>
				</view>
				<view class="personFans super_center" @tap="goToFansFollow(0)">
					<text class="personFans-text">他关注的</text>
					<text class="personFansNum-text">{{thisUserInfo.followNum}}</text>
				</view>
				<view class="personFans super_center" @tap="goToFansFollow(1)">
					<text class="personFans-text">关注他的</text>
					<text class="personFansNum-text">{{thisUserInfo.fansNum}}</text>
				</view>
			</view>
			<button type="default" size="mini" class="guanzhuButton" @tap="addFollow">
				<text class="guanzhuButton-text">关注</text>
			</button>
			
			<!-- TODO: 加完发私信的按钮格式乱了...要改一下
													by Jerrio-->
			<button type="default" size="mini" class="guanzhuButton" @tap="goToChatPage">
				<text class="guanzhuButton-text">发私信</text>
			</button>
		</view>

		<view id="public-message-futherbox">
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
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				scrollLeft: 0,
				isClickChange: false,
				currentTab: 0,
				menuTabs: [{
					name: '动态'
				}, {
					name: '帖子'
				}, {
					name: '点赞'
				}, {
					name: '评论'
				}],

				swiperDateList: [
					[],
					[],
					[],
					[],
					[],
					[]
				],

				// 用于分页的属性
				totalPage: 1,
				page: 1,
				videoList: [],

				screenWidth: 350,
				serverUrl: "",
				
				thisUserInfo: '',
			}
		},

		changeIndicatorDots(e) {
			this.indicatorDots = !this.indicatorDots
		},
		changeAutoplay(e) {
			this.autoplay = !this.autoplay
		},
		intervalChange(e) {
			this.interval = e.target.value
		},
		durationChange(e) {
			this.duration = e.target.value
		},

		onLoad() {
			// 获取这个人的信息 [测试展示用自己的]
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				console.log("No userInfo!!");
				return;
			}
			this.thisUserInfo = userInfo;
			console.log(this.thisUserInfo);
			
			uni.setNavigationBarTitle({
				title: this.thisUserInfo.nickname + "的主页"
			});

			var screenWidth = uni.getSystemInfoSync().screenWidth;
			this.screenWidth = screenWidth;

			// 获取当前分页
			var page = this.page;
			
			// TODO 更新本地用户信息缓存
			
			// [测试代码块]
			this.mySocket.init()
		},

		onPullDownRefresh() {
			console.log('refresh');
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 1000);
		},

		methods: {
			swichMenu: async function(current) { //点击其中一个 menu
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
				this.scrollLeft = leftWidthSum > winWidth ? (leftWidthSum - winWidth) : 0
			},
			getWidth: function(id) { //得到元素的宽高
				return new Promise((res, rej) => {
					uni.createSelectorQuery().select("#" + id).fields({
						size: true,
						scrollOffset: true
					}, (data) => {
						res(data);
					}).exec();
				})
			},
			loadMore: function(tabIndex) {
				console.log('正在加载更多数据。。。')
				this.getDateList(tabIndex);
			},
			/**
			 * 调用添加关注的接口
			 */
			addFollow: function(){
				
			},
			
			goToChatPage: function(){
				var friendInfo = this.thisUserInfo;
				uni.navigateTo({
					url: '../chatpage/chatpage?friendInfo=' + JSON.stringify(friendInfo),
				});
			},
			
			/**
			 * @param {Object} currentTab 0: 关注 1: 粉丝
			 */
			goToFansFollow: function(currentTab) {
				uni.navigateTo({
					url: '../followlist/followlist?currentTab=' + currentTab + '&userId=' + this.thisUserInfo.id,
				});
			}
		}
	}
</script>

<style>
	page {
		width: 100%;
		height: 100%;
	}

	#public-container {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: #ffcf3c;
	}

	#public-infobox {
		position: fixed;
		height: 20%;
		width: 100%;
	}
	
	#public-message-futherbox /* 这里是帖子块最高级父组件*/{
		/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
		position: fixed;
		top: 20%;
		width: 100%;
		height: 80%;
		/* 此处为背景半径，若要更改帖子块上方半径，请同时更改此处和下面导航条处 */
		border-top-left-radius: 50upx;
		border-top-right-radius: 50upx;
		box-shadow: 0upx -1upx 8upx 1upx #A6A6A6;
	}

	/* 以下是个人信息展示块的样式 */
	
	.publicTouxiang {
		margin-left: 4%;
		width: 180upx;
		height: 180upx;
		border-radius: 120px;
		display: inline-block;
		vertical-align: middle;
	}
	
	.personInfo{
		margin-left: 4%;
		height: 180upx;
		width: 180upx;
	}
	
	.personName{
		width: 100%;
		margin-top: 2%;
	}
	
	.personName-text{
		color: white;
		font-size: 32upx;
		font-weight: 550;
	}
	
	.personFans{
		height: 25%;
		width: 100%;
		margin-top: 10upx;
		background-color: #ffe285;
		border-radius: 5upx;
		display: flex;
		justify-content: space-around;
	}
	
	.personFans-text{
		color: white;
		font-size: 23upx;
		font-weight: 550;
	}
	
	.personFansNum-text{
		color: white;
		font-size: 23upx;
		font-weight: 550;
	}
	
	.guanzhuButton{
		margin-top: 13%;
		margin-left: 20%;
		width: 20%;
	}
	
	.guanzhuButton-text{
		font-size: 28upx;
		font-weight: 550;
		color: #FFCF3C;
	}
	
	
	/* 以下是帖子展示块的样式 */
	
	.top-menu-view {
		display: flex;
		justify-content: space-around;
		width: 100%;
		background-color: white;
		height: 8%; /* 在这里设置导航条高度 */
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
		background: #FFCF3C;
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
	.warning-text-box{
		height: 100%;
		background-color: #D5D5D5;
	}
	
	.warning-text-box .warning-text{
		color: black;
		font-size: 35upx;
	}
</style>
