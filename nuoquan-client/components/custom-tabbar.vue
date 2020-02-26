<template>
	<!--miniprogram/custom-tab-bar/index.wxml-->
	<cover-view class="tab-bar">
		<cover-view class="tab-bar-border"></cover-view>
		<!-- 推出左侧空白 -->
		<cover-view style="width: 20px;"></cover-view>
	
		<cover-view v-for="index in list" :key="index" v-bind:item.pagePath class="tab-bar-item" @tap="switchTab">
			<cover-view v-if="item.isSpecial">
				<cover-view class="midbutton" @tap="rotate">
					<cover-image :class="[ rotateStatus ? 'midIcon' : 'midIconBack' ]" :style="width: '24px',height: '24px',margin: '16px 16px,transition: all 500ms linear 200ms', transform: rotate({{
						degree+'deg'}});"
					src="../static/icon/plus_tab.png"></cover-image>
				</cover-view>
			</cover-view>
		 
			<cover-view wx:else>
				<!-- 选中添加橙色背景 -->
				<cover-view>
					<cover-image src="{{selected == index ? item.selectedIconPath : item.iconPath}}"></cover-image>
				</cover-view>
			</cover-view>
		</cover-view>
	
		<!-- 推出右侧空白 -->
		<cover-view style="width: 20px;"></cover-view>
	</cover-view>
	
</template>

<script>
Component({
	data: {
		selected: 0,
		color: "#7A7E83",
		selectedColor: "#3cc51f",
		rotateStatus: false, //旋转状态,判断静止or顺时针or逆时针
		degree: 0, //旋转角度
		midIconClass: "midIcon", //动态icon类名
		list: [{
				pagePath: 'index',
				iconPath: '../static/BG/tabbarBg/home_short.png',
				selectedIconPath: '../static/BG/tabbarBg/home.png',
				text: '主页'
			},
			{
				pagePath: 'votePage',
				iconPath: '../static/BG/tabbarBg/vote_short.png',
				selectedIconPath: '../static/BG/tabbarBg/vote.png',
				text: '投票'
			}, {
				pagePath: 'pages/tabPages/testpage',
				iconPath: '../static/icon/comment_dots_d4d4d4.png',
				selectedIconPath: '../static/icon/comment_dots_ffffff.png',
				isSpecial: true,
				text: ''
			}, {
				pagePath: 'messagelist',
				iconPath: '../static/BG/tabbarBg/notif_short.png',
				selectedIconPath: '../static/BG/tabbarBg/notif.png',
				text: '动态'
			}, {
				pagePath: 'mine',
				iconPath: '../static/BG/tabbarBg/mine_short.png',
				selectedIconPath: '../static/BG/tabbarBg/mine.png',
				text: '我的'
			}
		]
	},
	attached() {},
	methods: {
		switchTab(e) {
			const data = e.currentTarget.dataset;
			console.log('-----' + e.currentTarget);
			const url = data.path;
			console.log('url' + url);
			this.setData({
				selected: data.index
			})
			
			wx.switchTab({
				url
			})
			console.log(selected);
		},

		setTabBarIndex(index) {
			if (typeof this.$mp.page.getTabBar === 'function' &&
				this.$mp.page.getTabBar()) {
				this.$mp.page.getTabBar().setData({
					selected: index
				})
			}
		},
		
		rotate() {
			var rotateStatus = this.data.rotateStatus;
			if (!rotateStatus){
				this.setData({
					degree: 45,
					rotateStatus: !this.data.rotateStatus
				})
			}
			else {
				this.setData({
					degree: 0,
					rotateStatus: !this.data.rotateStatus
				})
			}

			console.log(this.data.degree);
		},
	}
})
	
</script>

<style>
	.tab-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		display: flex;
		padding-bottom: env(safe-area-inset-bottom);
		height: 63px;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
		border-radius: 16px 16px 0px 0px;
	}
	
	.tab-bar-border {
		background-color: rgba(0, 0, 0, 0.33);
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 1px;
		transform: scaleY(0.5);
	}
	
	.tab-bar-item {
		flex: 1;
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
	}
	
	.tab-bar-item cover-image {
		/* width: 20px; */
		/* height: 20px; */
		/* border-radius: 50%; */
		opacity: 1;
	}
	
	.tab-bar-item cover-view cover-image{
	/* 	margin: 12px 12px;
		width: 20px;
		height: 20px;
		border-radius: 50%;
		opacity: 1; */
	}
	
	.tabbarFont{
		font-size: 10px;
	}
	
	.iconBackground{
		width:44px;
		height:44px;
		/* background:rgba(253,169,86,1); */
		/* border-radius:50%; */
		opacity:1;
	}
	
	.midbutton{
		position: fixed;
		z-index: 999;
		left: 0;
		right: 0;
		margin-left: auto;
		margin-right: auto;
		bottom: 22px;
		width:56px;
		height:56px;
		background:rgba(255,255,255,1);
		box-shadow:0px 0px 4px rgba(0,0,0,0.16);
		border-radius:50%;
		opacity:1;
	}
	
	/* .midIcon{ */
		/* 旋转360度 */
		/* transform: rotate(0deg); */
		/* all表示所有属性,500s表示在0.5秒的时间完成动画 */
		/* transition: all 1000ms linear 200ms; */
	/* } */
	
	/* .midIconBack{ */
		/* 旋转360度 */
		/* transform: rotate(-45deg); */
		/* all表示所有属性,500s表示在0.5秒间完成动画 */
		/* transition: all 1000ms linear 200ms; */
	/* } */
		
	.midIcon{
	      animation:midIcon 300ms linear 1;      
	    }
	    /* 
	      midIcon : 定义的动画名称
	      1s : 动画时间
	      linear : 动画以何种运行轨迹完成一个周期
	      infinite :规定动画应该无限次播放
	     */
	   @keyframes midIcon{
	      0%{-webkit-transform:rotate(0deg);}
	      25%{-webkit-transform:rotate(11.25deg);}
	      50%{-webkit-transform:rotate(22.5deg);}
	      75%{-webkit-transform:rotate(33.75deg);}
	      100%{-webkit-transform:rotate(45deg);}
	    }
	
	 .midIconBack{
	      animation:midIconBack 300ms linear 1;      
	    }
	    /* 
	      midIcon : 定义的动画名称
	      1s : 动画时间
	      linear : 动画以何种运行轨迹完成一个周期
	      infinite :规定动画应该无限次播放
	     */
	    @keyframes midIconBack{
	      0%{-webkit-transform:rotate(45deg);}
	      25%{-webkit-transform:rotate(33.75deg);}
	      50%{-webkit-transform:rotate(22.5deg);}
	      75%{-webkit-transform:rotate(11.25deg);}
	      100%{-webkit-transform:rotate(0deg);}
	    } 
</style>
