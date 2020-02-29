<template>
	<view class="mainPageTopAll" style="height:100%;">
		<!-- 盛放top功能区，height屏幕高度，宽度0 -->
		<view class="bottomLayerOfLeft" v-if="showMainPageLeft" @click="controlShowLeft(0)" @touchmove="controlShowLeft(0)">
			<mainpageleft :userInfo="userInfo"></mainpageleft>
		</view>

		<searchpage v-if="showSearch" class="searchPage" @exitSearchSignal="controlShowSearch"></searchpage>

		<view class="mainPageTop" :style="{ height: roleup == false ? height + 'px' : height_roled + 'px' }">
			<!-- 搜索行 -->
			<view class="topBar" :style="{'margin-top': this.getnavbarHeight().top - 2 + 'px'}">
				<!-- 头像，备用 <image class="topBarTouxiang" :src='pathFilter(userInfo.faceImg)' @click="controlShowLeft(1)"></image> -->
				<view class="topBarSearch" @click="controlShowSearch(1)">
					<image src="../static/icon/search_B79144.png" mode="aspectFit"></image>
				</view>
				<!-- 新建，备用 <view class="topBarPlus" @click="jumpToSubmit()">
					<view style="font-size: 20px;color:#FDD041;border-radius: 3px;">+</view>
				</view> -->
				<view class="topBarwaiting"></view>
			</view>

			<!-- 热门卡片 -->
			<view class="hotestBox" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
				<view :class="[roleup == false ? 'hotestCard' : 'hotestCard_roled']">
					<!-- 左侧图标 -->
					<view class="iconBox" @click="jumpTohot" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
						<image v-if="roleup == false" src="../static/BG/hotest.png" mode="aspectFit" class="fireIcon"></image>
						<image :class="[roleup == false ? 'hotText' : 'hotText_roled']" src="../static/icon/hotText.png" mode="aspectFit"></image>
					</view>
					
					<!-- 分割线 -->
					<view v-if="roleup == false" class="grayline"></view>
					
					<!-- 热门标题 -->
					<swiper :class="[roleup == false ? 'swiperCard' : 'swiperCard_roled']" :indicator-dots="false" :autoplay="true" :interval="3000" :vertical="true" :duration="1000">
						<view >
							<swiper-item v-for="(item, index) in topArticles" :key="index">
								<view class="itemCard" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
									<view :class="[roleup == false ? 'hotTitle' : 'hotTitle_roled']">{{ item.articleTitle }}</view>
									<view v-if="roleup == false" class="userInfo">
										<image :src=pathFilter(item.faceImg) mode="aspectFit"></image>
										<view class="userid_mainpagetop">
											{{item.nickname}}
										</view>
									</view>
								</view>
							</swiper-item>
						</view>
					</swiper>
				</view>
			</view>
		</view>
		<!-- 主页操作行 -->
		<view v-if="roleup == false" class="optionLine_mpt column_center" :style="{top:height + 11 + 'px'}">
			<!-- 标签选择 -->
			<view v-if="!selectedTag" class="tagchoose column_center" @click="toggleShowTag">
				<text>标签</text>
				<image class="tagicon" src="../static/icon/angle-down.png" mode="aspectFit"></image>
			</view>
			<tagSelected v-if="selectedTag" :tag='selectedTag' @click="deleteTag"></tagSelected>
			<tagSelectBox
				:style="{position: 'fixed', 'margin-top': 6 + 'px' , left: 3.47 + '%' , width: 93.07 + '%' , top: height + 41 + 'px' }"
				:tagList="tagList" 
				@selected="getSelectedTag" 
				v-if="showTagBox">
			</tagSelectBox>
			<!-- 排序方式1-->
			<nqSwitch :options='options1' :initStatus='iniStatus1' @onChange="change_article_order1"></nqSwitch>
			<!-- 排序方式2 -->
			<nqSwitch :options='options2' :initStatus='iniStatus2' @onChange="change_article_order2"></nqSwitch>
		</view>
		<!-- Add background for option bar-->
		<view v-if="roleup == false" class="optionLinebg_mpt" :style="{top: height + 4 + 'px'}">
		</view>
		<!-- 标签选择 -->
		
	</view>
</template>

<script>
import mainpageleft from '@/components/mainpageleft.vue';
import searchpage from '../pages/search/search';
import nqSwitch from '@/components/nq-switch.vue'
import tagSelectBox from '@/components/nq-tag/tagSelectBox.vue'
import tagSelected from '@/components/nq-tag/tagSelected.vue'
export default {
	components: {
		mainpageleft,
		searchpage,
		nqSwitch,
		tagSelectBox,
		tagSelected
	},
	props: {
		// 渲染时候替换默认值会被替换
		userInfo: {
			faceImg: '../static/touxiang.jpg'
		},
		topArticles: '',
		tagList: '', //标签列表
		roleup: false,
		height: 0,
		height_roled: 0,
	},
	data() {
		return {
			serverUrl: this.$serverUrl,
			showMainPageLeft: 0,
			showSearch: 0,
			showTagBox: false,
			options1: ["所有","关注"], //为switch组件设置选项标签
			iniStatus1: 0, //为switch组件设置初始值
			options2: ["时间","热度"], //为switch组件设置选项标签
			iniStatus2: 0, //为switch组件设置初始值
			selectedTag: '' //已选择的标签
		};
	},
	watch:{
		roleup(newValue, oldValue){
			this.showTagBox = false;
		}
	},

	methods: {
		toggleShowTag(){
			this.showTagBox = !this.showTagBox
		},
		controlShowLeft(a) {
			this.showMainPageLeft = a;
			// console.log(this.showMainPageLeft);
		},
		controlShowSearch(a) {
			console.log('this is controlShowSearch, receive data will be appied');
			console.log(a);
			this.showSearch = a;
		},
		jumpToSubmit() {
			uni.navigateTo({
				url: '/pages/submit/submit'
			});
		},
		goToDetail(article) {
			uni.navigateTo({
				url: '/pages/detail/detail?data=' + article.id
			});
		},
		jumpTohot() {
			uni.navigateTo({
				url: '/pages/hot/hot'
			})
		},
		
		change_article_order1(e){
			// console.log(e.status);
			this.iniStatus1=e.status;
			this.$emit('transQueryType', e.status);
		},
		
		change_article_order2(e){
			// console.log(e.status);
			this.iniStatus2=e.status;
			this.$emit('transOrderType', e.status);
		},
		
		getSelectedTag(tag){
			this.selectedTag = tag; //传值给渲染层
			this.$emit('selectedTag', tag);
		},
		
		deleteTag(){
			this.selectedTag = '';
			this.showTagBox = false;
			this.$emit('deleteTag');
		}
	}
};
</script>
<style>
page {
	width: 100%;
}
</style>
<style scoped>
.searchPage {
	height: 100%;
	width: 100%;
	position: fixed;
	top: 0;
	left: 0;
	background: #ffffff;
	z-index: 30;
}

.bottomLayerOfLeft {
	position: fixed;
	width: 750upx;
	height: 1000px;
	z-index: 3;
}

.mainPageTop {
	padding-top: 4px;
	/* height: 139px; */
	position: fixed;
	left: 0;
	top: 0;
	overflow: hidden;
	width: 100%;
	background-size: 100% 100%;
	box-shadow: 0px 0px 10px 1px #c0c0c0;
	background-image: linear-gradient(#ffc95a, #f89d4d);
}

/* .mainPageTop_roled{
	padding-top: 4px;
	height: 110px;
	position: fixed;
	left: 0;
	top: 0;
	overflow: hidden;
	width: 100%;
	background-size: 100% 100%;
	box-shadow: 0px 0px 10px 1px #c0c0c0;
	background-image: linear-gradient(#ffc95a, #f89d4d);
}
 */
.topBar {
	width: 100%;
	height: 30px;
	/* margin-top: 23px; */
	/* 此处需要兼容性处理 47px */
}

/* 头像,备用
	.topBarTouxiang {
		width: 30px;
		height: 30px;
		border-radius: 30px;
		display: inline-block;
		vertical-align: middle;
		margin-left: 12px;
	} */

.topBarSearch {
	width: 68.27%;
	height: 30px;
	background: rgba(255, 247, 231, 1);
	opacity: 1;
	border-radius: 75px;
	font-size: 15px;
	display: inline-block;
	vertical-align: middle;
	margin-left: 3.47%;
}

.topBarSearch image {
	width: 20px;
	height: 20px;
	opacity: 1;
	margin: 5px 13px;
}

.topBarPlus {
	font-size: 25px;
	background: #ffffff;
	display: inline-block;
	height: 25px;
	width: 25px;
	vertical-align: middle;
	text-align: center;
	margin-left: 10px;
	border-radius: 3px;
	line-height: 23px;
}

/* 热门话题卡片 */
.hotestBox {
	width: 100%;
}

.hotestCard {
	display: flex;
	height:62px;
	width: 93.07%;
	background: rgba(252, 252, 252, 1);
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
	opacity: 1;
	border-radius: 16px;
	margin-top: 12px;
	margin-left: 3.47%;
}

.hotestCard_roled {
	display: flex;
	width: 93.07%;
	background: rgba(252, 252, 252, 1);
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
	opacity: 1;
	margin-top: 12px;
	margin-left: 3.47%;
	height:33px;
	border-radius:8px;
}

.iconBox {
	position: relative;
	width: 62px;
}

.fireIcon {
	position: absolute;
	z-index: 10;
	height: 62px;
	width: 62px;
}

.hotText {
	position: absolute;
	top: 8px;
	left: 29px;
	height: 14px;
	width: 26px;
	z-index: 20;
}

.hotText_roled {
	position: absolute;
	top: 11px;
	left: 12px;
	height: 14px;
	width: 26px;
	z-index: 20;
}

.grayline {
	height: 41px;
	width: 2px;
	background-color: #ececec;
	margin-top: 14px;
}

.swiperCard{
	width: 74.21%;
	height: 62px;
	margin-left: 11px;
}

.swiperCard_roled{
	width: 74.21%;
	height: 33px;
	margin-left: 11px;
}

.itemCard{
	width: 100%;
}

.userInfo{
	position: relative;
	width: 100%;
	height: 17px;
	margin-top: 8px;
}

.userInfo image{
	position: absolute;
	width: 17px;
	height: 17px;
	border-radius: 50%;
}

.userid_mainpagetop{
	position: absolute;
	left: 25px;
	height:12px;
	width: 100%;
	font-size:12px;
	font-weight:400;
	color:rgba(155,155,155,1);
	opacity:1;
}

.hotTitle {
	width: 100%;
	height: 17px;
	margin-top: 12px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 21px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
}

.hotTitle_roled {
	width: 100%;
	height: 17px;
	margin-top: 8px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 21px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
}

.optionLine_mpt{
	display: flex;
	justify-content: space-between;
	width: 93.07%;
	left: 3.47%;
	height: 30px;
	position: fixed;
	z-index: 20;
}

.optionLinebg_mpt{
	position: fixed;
	z-index: 10;
	width: 100%;
	height: 41px;
	background-color: #fdfdfd;
}

.tagchoose{
	width:74px;
	height:26px;
	background:rgba(236,236,236,1);
	opacity:1;
	/* margin-left: 3.47%; */
	border-radius: 20px;
}

.tagchoose text{
	margin-left: 12px;
	height:26px;
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:500;
	line-height:28px;
	color:rgba(53,53,53,1);
	opacity:1;
}

.tagicon{
	margin-left: 3px;
	width:24px;
	height:16px;
	opacity:1;
}

.comments-order {
		/* margin-left: 14.8%; */
		height: 22px;
		background: #ECECEC;
		border-radius: 75px;
		width: 82px;
		line-height: 28px;
		display: flex;
		vertical-align: middle;
		position: relative;
		justify-content: space-between;
		
	}
	
	.order-in-hot ,.order-in-time{
		color:#9B9B9B;
		font-size: 10px;
		line-height: 22px;
		display: inline-block;
		align-items: center;
		padding-right:10.5px;
		padding-left: 10.5px;
		z-index: 30;
		width:28px;
		text-align: center;
		transition: padding-left 200ms,
					padding-right 200ms,
					font-size  200ms,
					color 200ms;
		transition-delay: 0ms;
	}
	.bg-of-order{
		height:28px;
		width:54px;
		background: #FFFFFF;
		border-radius: 75px;
		box-shadow: 0px 0px 10px rgba(0,0,0,0.16);
		z-index: 20;
		position: absolute;
		left:-13px;
		top:-3px;
		transition: left 500ms ease;
	}
	.chosen{
		font-size: 14px;
		color:#353535;
		width:28px;
		padding-left: 0;
		padding-right: 0;
		transition: padding-left 300ms,
					padding-right 300ms,
					font-size  300ms,
					color 300ms;
		transition-delay: 100ms;
	}
</style>
