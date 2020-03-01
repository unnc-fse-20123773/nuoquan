<template>
	<view id="public-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar"
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		left-text="返回"
		:title="pageTitle" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}"></view>
		
		<view id="public-message-futherbox">
			<scroll-view class="top-menu-view" scroll-x="true" scroll-left="scrollLeft">
				<block v-for="(menuTabs,index) in menuTabs" :key="index">
					<view class="menu-one-view" v-bind:id="'tabNum'+index" @click="swichMenu(index)">
						<view :class="[currentTab==index ? 'menu-one-act' : 'menu-one']">
							<view class="menu-one-txt" @tap="goTop">{{menuTabs.name}}</view>
							<view class="menu-one-bottom">
								<view class="menu-one-bottom-color1" v-if="index == 0"></view>
								<view class="menu-one-bottom-color2" v-else></view>
							</view>
						</view>
					</view>
				</block>
			</scroll-view>
			<swiper :current="currentTab" class="swiper-box-list" duration="300" @change="swiperChange">
				<swiper-item class="swiper-box" v-for="(swiperData,index1) in swiperDataList" :key="index1">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" @scrolltoupper="upper" @scrolltolower="loadMore(index1)"
					 @scroll="scroll" enable-back-to-top="true" >
						<!-- 卡片部分为文档流格式  -by Guetta-->
						<!-- 详情卡片 -->
						<view v-for="(item,index2) in (index1==0 ? likeList : commentList)" :key="index2">
							<!-- ******************** 点赞文章卡片 ********************** -->
							<view v-if="item.action==LIKEARTICLE" class="cmtlikeDetail-card" >
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper1"></view>
								<!-- ID 行 -->
								<view class="id-line-abs">
									<!-- 相对绝对定位 -->
									<view class="id-line-rel">
										<view class="clTouxiang-box">
											<image class="clTouxiang" :src="pathFilter(item.data.source.faceImg)" mode="scaleToFill" @tap="goToPersonPublic(likeList[index2].data.source.userId)"></image>
										</view>
										<view class="clID-box">
											<text class="clID-text">{{item.data.source.nickname}}</text>
											<text class="clID-operation">点赞了你的文章</text>
										</view>
										<!-- 需要获取新消息时间戳 -->
										<view class="clID-time">
											{{timeDeal(item.data.source.createDate)}}
										</view>
									</view>
								</view>
								<!-- 文章预览块 -->
								<view :class="[item.data.imgList.length > 0 ? 'origin-bar-abs-img' : 'origin-bar-abs-noimg']" @tap="goToArticle(likeList[index2].data.target.id)">
									<view class="origin-bar-rel">
										<view class="origin-imageBox" v-if="item.data.target.imgList.length > 0">
											<view class="origin-imageMask"></view>
											<view class="origin-imageMasknum super_center">
												<view class="origin-imageMasknumtext">
													+{{item.data.target.imgList.length}}
												</view>
											</view>
											<image class="origin-image" :src="serverUrl + item.data.target.imgList[0].imagePath" mode="scaleToFill"></image>
										</view>
										<view :class="[item.data.imgList.length > 0 ? 'origin-briefBox-img' : 'origin-briefBox-noimg']">
											<view class="origin-briefTitlebox">
												<text class="origin-briefTitle">{{item.data.target.articleTitle}}</text>
											</view>
											<view class="origin-briefTextbox">
												<text class="origin-briefText">{{item.data.target.articleContent}}</text>
											</view>
										</view>
									</view>
								</view>
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper2"></view>
							</view>
							
							<!-- ************************* 点赞评论卡片 *************************** -->
							<view v-if="item.action==LIKECOMMENT" class="cmtlikeDetail-card" >
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper1"></view>
								<!-- ID 行 -->
								<view class="id-line-abs">
									<!-- 相对绝对定位 -->
									<view class="id-line-rel">
										<view class="clTouxiang-box">
											<image class="clTouxiang" :src="item.data.source.faceImg" mode="scaleToFill" @tap="goToPersonPublic(likeList[index2].data.source.userId)"></image>
										</view>
										<view class="clID-box">
											<text class="clID-text">{{item.data.source.nickname}}</text>
											<text class="clID-operation">点赞了你的评论</text>
										</view>
										<!-- 需要获取新消息时间戳 -->
										<view class="clID-time">
											{{timeDeal(item.data.source.createDate)}}
										</view>
									</view>
								</view>
								<!-- 点赞预览块 -->
								<view class="brief-bar-abs" @tap="goToComment(likeList[index2].data.target.articleId)">
									<view class="brief-bar-rel">
										{{item.data.target.comment}}
									</view>
								</view>

								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper2"></view>
							</view>
							
							<!--***************************** 评论文章卡片 *****************************-->
							<view v-if="item.action==COMMENTARTICLE" class="cmtlikeDetail-card" >
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper1"></view>
								<!-- ID 行 -->
								<view class="id-line-abs">
									<!-- 相对绝对定位 -->
									<view class="id-line-rel">
										<view class="clTouxiang-box">
											<image class="clTouxiang" :src="item.data.source.faceImg" mode="scaleToFill" @tap="goToPersonPublic(commentList[index2].data.source.fromUserId)"></image>
										</view>
										<view class="clID-box">
											<text class="clID-text">{{item.data.source.nickname}}</text>
											<text class="clID-operation">评论了你的文章</text>
										</view>
										<!-- 需要获取新消息时间戳 -->
										<view class="clID-time">
											{{timeDeal(item.data.source.createDate)}}
										</view>
									</view>
								</view>
								<view @tap="goToComment(commentList[index2].data.source.articleId)">
									<!-- 点赞 or 评论预览块 -->
									<view class="brief-bar-nocolor">
										<view class="brief-bar-rel">
											{{item.data.source.comment}}
										</view>
									</view>
									<!-- 原文章预览块 -->
									<view :class="[item.data.target.imgList.length > 0 ? 'origin-bar-abs-img' : 'origin-bar-abs-noimg']">
										<view class="origin-bar-rel">
											<view class="origin-imageBox" v-if="item.data.target.imgList.length > 0">
												<view class="origin-imageMask"></view>
												<view class="origin-imageMasknum super_center">
													<view class="origin-imageMasknumtext">
														+{{item.data.target.imgList.length}}
													</view>
												</view>
												<!-- 图片好像显示不出来 by Jerrio -->
												<image class="origin-image" :src="serverUrl + item.data.target.imgList[0].imagePath" mode="scaleToFill"></image>
											</view>
											<view :class="[item.data.target.imgList.length > 0 ? 'origin-briefBox-img' : 'origin-briefBox-noimg']">
												<view class="origin-briefTitlebox">
													<text class="origin-briefTitle">{{item.data.target.articleTitle}}</text>
												</view>
												<view class="origin-briefTextbox">
													<text class="origin-briefText">{{item.data.target.articleContent}}</text>
												</view>
											</view>
										</view>
									</view>
								</view>
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper2"></view>
							</view>
							
							<!-- ***************************** 评论评论卡片 ******************************** -->
							<view v-if="item.action==COMMENTCOMMENT" class="cmtlikeDetail-card" >
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper1"></view>
								<!-- ID 行 -->
								<view class="id-line-abs">
									<!-- 相对绝对定位 -->
									<view class="id-line-rel">
										<view class="clTouxiang-box">
											<image class="clTouxiang" :src="item.data.source.faceImg" mode="scaleToFill" @tap="goToPersonPublic(commentList[index2].data.source.fromUserId)"></image>
										</view>
										<view class="clID-box">
											<text class="clID-text">{{item.data.source.nickname}}</text>
											<text class="clID-operation">回复了你的评论</text>
										</view>
										<!-- 需要获取新消息时间戳 -->
										<view class="clID-time">
											{{timeDeal(item.data.source.createDate)}}
										</view>
									</view>
								</view>
								<view @tap="goToComment(commentList[index2].data.source.articleId)">
									<!-- 点赞 or 评论预览块 -->
									<view class="brief-bar-nocolor">
										<view class="brief-bar-rel">
											{{item.data.source.comment}}
										</view>
									</view>
									<!-- 原评论预览块 -->
									<view class="brief-bar-abs-cmtofcmt">
										<view class="brief-bar-rel-cmtofcmt">
											{{item.data.target.comment}}
										</view>
									</view>
								</view>
								<!-- 卡片高度未定义，上下边距会失效，用 marginHelper 填充空白 -->
								<view class="marginHelper2"></view>
							</view>
							
						</view>
						<!-- 用于添加底部空白 by Guetta 9.10 -->
						<view class="marginHelper3"></view>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
	</view>
</template>

<script>
	// TODO 查询列表分页操作
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"
	export default {
		components:{
			uniNavBar
		},
		data() {
			return {
				pageTitle: '评论详情',
				scrollLeft: 0,
				isClickChange: false,
				currentTab: '', // 切换 list 0/1
				menuTabs: [{
					name: '点赞'
				}, {
					name: '评论'
				}],

				// 点赞评论 swiper
				swiperDataList: [
					[], // 点赞 把数据写进里面首次进入页面加载不出，所以写到外面
					[]  // 评论
				],
				likeList: '',
				commentList: '',

				screenWidth: 350,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				
				// 类型枚举
				LIKEARTICLE: this.netty.LIKEARTICLE, // 点赞文章通知
				LIKECOMMENT: this.netty.LIKECOMMENT, // 点赞评论通知
				COMMENTARTICLE: this.netty.COMMENTARTICLE, //评论文章通知
				COMMENTCOMMENT: this.netty.COMMENTCOMMENT, // 评论评论通知
				
				serverUrl: this.$serverUrl,
				userInfo: '',
				likePage: 1,
				commentPage: 1,
				
				isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
			}
		},
		
		onLoad(opt) {
			var currentTab = JSON.parse(opt.currentTab);
			uni.setNavigationBarTitle({
				title: "消息列表"
			});
			
			// 设置列表 index
			this.currentTab = currentTab;
			
			var screenWidth = uni.getSystemInfoSync().screenWidth;
			this.screenWidth = screenWidth;
			
			// [测试用代码块]
			var userInfo = this.getGlobalUserInfo()
			this.userInfo = userInfo;
			
			// 加载点赞评论通知缓存
			this.likeList = this.notification.getLikeMsg(this.likePage);
			this.commentList = this.notification.getCommentMsg(this.commentPage);
			console.log(this.likeList)
			console.log(this.commentList)
		},
		
		onShow() {
			
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
					if(current==0){
						// console.log("点了点赞"); 刷新 list 并设置计数值
						this.likePage = 1;
						this.likeList = this.notification.getLikeMsg(this.likePage);
						this.$store.commit('setLikeMsgCount', 0);
					}else{
						// console.log("点了评论");
						this.commentPage = 1;
						this.commentList = this.notification.getCommentMsg(this.commentPage);
						this.$store.commit('setCommentMsgCount', 0);
					}
					
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

			upper: function(e) {
				console.log(e)
			},
			
			scroll: function(e) {
				console.log(e)
				this.old.scrollTop = e.detail.scrollTop
			},
			goTop: function(e) {
				this.scrollTop = this.old.scrollTop
				this.$nextTick(function() {
					this.scrollTop = 0
				});
				// uni.showToast({
				// 	icon: "none",
				// 	title: "回到顶部喽~"
				// })
			},
			
			loadMore(tabIndex) {
				console.log('正在加载更多数据。。。');
				uni.showLoading({
					title: '加载中'
				});
				if(tabIndex == 0){ // like
					var page = this.likePage + 1;
					var list = this.notification.getLikeMsg(page);
					if(list != null){
						// console.log(list)
						this.likeList = this.likeList.concat(list);
						this.likePage++;
					}else{
						uni.showToast({
							title: 'No more',
							duration: 2000,
							icon: 'none',
						});
					}
					uni.hideLoading();
				}else{
					var page = this.commentPage + 1;
					var list = this.notification.getCommentMsg(page);
					if(list != null){
						// console.log(list)
						this.commentList = this.commentList.concat(list);
						this.commentPage++;
					}else{
						uni.showToast({
							title: 'No more',
							duration: 2000,
							icon: 'none',
						});
					}
					uni.hideLoading();
				}
			},
			
			goToPersonPublic(userId){
				uni.navigateTo({
					url:'/pages/personpublic/personpublic?userId=' + userId,
				});
			},
			
			goToArticle(articleId){
				// console.log(article)
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getArticleById',
					method: "POST",
					data:{
						articleId: articleId,
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							var article = res.data.data;
							console.log(article)
							uni.navigateTo({
								url:'../detail/detail?data=' + JSON.stringify(article),
							})
						}
					},
				});
			},
			
			goToComment(articleId){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getArticleById',
					method: "POST",
					data:{
						articleId: articleId,
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							var article = res.data.data;
							uni.navigateTo({
								url:'../detail/detail?data=' + JSON.stringify(article),
							})
						}
					},
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
		background-color: #f3f3f3;
	}

	#public-infobox {
		position: fixed;
		height: 20%;
		width: 100%;
	}

	#public-message-futherbox

	/* 这里是帖子块最高级父组件*/
		{
		/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
		position: fixed;
		width: 100%;
		height: 100%;
	}

	/* 以下是帖子展示块的样式 */

	.top-menu-view {
		display: flex;
		justify-content: space-around;
		width: 100%;
		background-color: #f3f3f3;
		height: 6%;
		/* 在这里设置导航条高度 */
	}

	.menu-one-view {
		display: inline-block;
		white-space: nowrap;
		height: 100%;
		width: 48%;
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

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color1 {
		/* 在这里设置底部横条高度和颜色 */
		width: 60%;
		height: 6upx;
		background: #ff5d6c;
	}
	
	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color2 {
		/* 在这里设置底部横条高度和颜色 */
		width: 60%;
		height: 6upx;
		background: #00a0e9;
	}

	.swiper-box-list {
		flex: 1;
		min-height: 94%;
		height: 94%;
		width: 100%;
		background-color: #f3f3f3;
	}

	/* swiper */

	.swiper {
		height: 360upx;
	}

	.scroll-test {
		height: 100%
	}

	.swiper-box {
		width: 100%;
		height: 100%;
	}

	/* 卡片父组件 */
	.cmtlikeDetail-card {
		margin-left: 5%;
		width: 90%;
		border-radius: 25upx;
		min-height: 150upx;
		background-color: white;
		box-shadow: 0upx 0upx 10upx 1upx #A6A6A6;
		margin-top: 15px;
	}
	
	/* margingHelper */
	.marginHelper1{
		height: 15upx;
		width: 60%;
		margin-left: 20%;
		background-color: white;
	}
	
	.marginHelper2{
		height: 28upx;
		width: 60%;
		margin-left: 20%;
		background-color: white;
	}
	
	.marginHelper3{
		height: 15upx;
		margin-top: 15upx;
		width: 100%;
		background-color: #f3f3f3;
	}
		

	/* --------------------ID 行--------------------- */
	.id-line-abs {
		margin-bottom: 15upx;
		width: 92%;
		margin-left: 4%;
		height: 30px;
	}

	.id-line-rel {
		position: relative;
	}

	/* 头像 */
	.clTouxiang-box{
		position: absolute;
		height: 22px;
		width: 22px;
		left: 0;
		top: 4px;
	}
	
	.clTouxiang{
		width: 100%;
		height: 100%;
		border-radius: 999px;
	}
	
	/* ID */
	.clID-box{
		position: absolute;
		left: 30px;
	}
	
	.clID-text{
		font-size: 13px;
		font-weight: 500;
		color: #3d3d3d;
	}
	
	.clID-operation{
		font-size: 10px;
		color: #919191;
		margin-left: 8upx;
	}
	
	/* 时间 */
	.clID-time{
		position: absolute;
		right: 0;
		font-size: 12px;
		color: #919191;
		bottom: -23px;
	}
	
	/* ---------------------------预览行---------------------- */
	.brief-bar-abs {
		/* 底部边距需要动态设置 */
		overflow: hidden;
		width: 92%;
		margin-left: 4%;
		border-radius: 20upx;
		background-color: #f8eced;
	}
	
	.brief-bar-abs-cmtofcmt {
		/* 底部边距需要动态设置 */
		overflow: hidden;
		width: 92%;
		margin-left: 4%;
		border-radius: 20upx;
		background-color: #e5f3f9;
	}
	
	.brief-bar-nocolor {
		/* 底部边距需要动态设置 */
		overflow: hidden;
		width: 96%;
		margin-left: 2%;
		border-radius: 20upx;
	}

	.brief-bar-rel {
		position: relative;
		width: 94%;
		margin-left: 3%;
		margin-top: 15upx;
		margin-bottom: 15upx;
		height: 100%;
		font-size: 12px;
		color: #3d3d3d;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
		word-break:break-all;
	}
	
	.brief-bar-rel-cmtofcmt {
		position: relative;
		width: 94%;
		margin-left: 3%;
		margin-top: 15upx;
		margin-bottom: 15upx;
		height: 100%;
		font-size: 12px;
		color: #aba8a0;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
		word-break:break-all;
	}

	/* -------------------原评论预览行 -----------------*/
	.origin-bar-abs-img {
		height: 146upx;
		width: 92%;
		margin-left: 4%;
		border-radius: 20upx;
		background-color: #f5f4ed;
	}

	.origin-bar-abs-noimg {
		width: 92%;
		margin-left: 4%;
		border-radius: 20upx;
		background-color: #f5f4ed;
	}

	.origin-bar-rel {
		max-height: 140upx;
		width: 100%;
		height: 100%;
		display: flex;
	}
	
	/* 图片 */
	.origin-imageBox{
		position: relative;
		height: 146upx;
		width: 146upx;
		marging-left: 20upx;
	}
	
	/* 黑色盖板 */
	.origin-imageMask{
		position: absolute;
		height: 106upx;
		width: 106upx;
		left: 20upx;
		top: 20upx;
		background-color: #000000;
		opacity:0.5;
		z-index: 10;
	}
	
	.origin-imageMasknum{
		position: absolute;
		height: 106upx;
		width: 106upx;
		left: 20upx;
		top: 20upx;
		z-index: 20;
	}
	
	.origin-imageMasknumtext{
		color: white;
		font-size: 14px;
	}
	
	.origin-image{
		position: absolute;
		height: 106upx;
		width: 106upx;
		left: 20upx;
		top: 20upx;
	}
	
	/* 预览块 */
	.origin-briefBox-img{
		max-height: 140upx;
		width: 79%;
		/* margin-left: 4%; */
	}	
	
	.origin-briefBox-noimg{
		max-height: 140upx;
		width: 79%;
		margin-bottom: 15upx;
		margin-left: 3%;
		/* margin-left: 4%; */
	}
	
	/* 标题 */
	.origin-briefTitlebox{
		width: 98%;
		margin-top: 15upx;
	}
	
	.origin-briefTitle{
		font-size: 12px;
		font-weight: 550;
		color: #b1ada6;
		/* 设置弹性盒子布局 */
		display: -webkit-box;
		/* 盒子内容布局方式 */
		-webkit-box-orient: vertical;
		/* 文本行数设置 */
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
	
	/* 正文 */
	.origin-briefText{
		font-size: 9px;
		color: #919191;
		height: 100%;
		width: 98%;
	}
	
	.origin-briefTextbox{
		max-height: 80upx;
		width: 96%;
		font-size: 10px;
		color: #919191;
		/* overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal; */
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
	
</style>
