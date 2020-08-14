<template>
	<view id="public-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.explore" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }"></view>

		<view id="public-message-futherbox">
			<scroll-view class="top-menu-view" scroll-x="true" scroll-left="scrollLeft">
				<block v-for="(menuTab, index) in [{ name: lang.hotDoor }, { name: lang.academic }]" :key="index">
					<view class="menu-one-view" v-bind:id="'tabNum' + index" @click="swichMenu(index)">
						<view :class="[currentTab == index ? 'menu-one-act' : 'menu-one']">
							<view class="menu-one-txt" @tap="goTop">{{ menuTab.name }}</view>
							<view class="menu-one-bottom">
								<view class="menu-one-bottom-color1" v-if="index == 0"></view>
								<view class="menu-one-bottom-color2" v-else></view>
							</view>
						</view>
					</view>
				</block>
			</scroll-view>
			<swiper :current="currentTab" class="swiper-box-list" duration="300" @change="swiperChange">
				<swiper-item class="swiper-box">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" enable-back-to-top="true">

					</scroll-view>
				</swiper-item>
				<swiper-item class="swiper-box">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" enable-back-to-top="true">
						<academicarea></academicarea>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
		<tab-bar :current="1" @clickTab="onClickTab"></tab-bar>
	</view>
</template>

<script>
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import academicarea from '../../components/nq-explore/academic_area.vue'



	export default {
		components: {
			uniNavBar,
			tabBar,
			academicarea,
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				currentTab: 0,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				scrollLeft: 0,
			};
		},
		onLoad() {

		},
		methods: {
			goTop: function(e) {
				this.scrollTop = this.old.scrollTop;
				this.$nextTick(function() {
					this.scrollTop = 0;
				});
			},
			swichMenu: async function(current) {
				//点击其中一个 menu
				if (this.currentTab == current) {
					return false;
				} else {
					if (current == 0) {
						// console.log("点了点赞"); 刷新 list 并设置计数值
						this.likePage = 1;
						this.likeList = this.notification.getLikeMsg(this.likePage);
						this.$store.commit('setLikeMsgCount', 0);
					} else {
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
				this.scrollLeft = leftWidthSum > winWidth ? leftWidthSum - winWidth : 0;
			},
			getWidth: function(id) {
				//得到元素的宽高
				return new Promise((res, rej) => {
					uni.createSelectorQuery()
						.select('#' + id)
						.fields({
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
		}

	}
</script>

<style>
	#public-container {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: #FFFFFF;
	}

	#public-message-futherbox

	/* 这里是帖子块最高级父组件*/
		{
		/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
		position: fixed;
		width: 100%;
		height: 100%;
	}

	.top-menu-view {
		border-bottom: 1px solid rgba(236, 236, 236, 1);
		display: flex;
		justify-content: space-around;
		width: 100%;
		height: 46px;
		/* 在这里设置导航条高度 */
	}

	.menu-one-view {
		display: inline-block;
		white-space: nowrap;
		height: 100%;
		margin-left: 8px;
		margin-right: 8px;
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
		font-weight: bold;
		color: #353535;
		line-height: 40upx;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom {
		/* 在这里设置底部横条宽度 */
		position: absolute;
		bottom: 5px;
		width: 5px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 2px;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color1 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
		border-radius: 2px;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color2 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
	}

	.swiper-box-list {
		flex: 1;
		height: calc(100% - 46px);
		width: 100%;
		background: #FAFAFA;
	}

	/* swiper */

	.swiper {
		height: 360upx;
	}

	.scroll-test {
		height: 100%;
	}

	.swiper-box {
		width: 100%;
		height: 100%;
	}
</style>
