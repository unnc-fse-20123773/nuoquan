<script>
export default {
	onLaunch: function() {
		
	},
	onShow: function() {
		console.log('App Show');
	},
	onHide: function() {
		console.log('App Hide');
	},

	globalData: {
		// 自定义显示tabbar
		onTabBar: function(key) {
			var tabBarData = {
				color: '#dbdbdb',
				selectedColor: '#1296db',
				backgroundColor: 'white',
				borderStyle: 'white',
				position: 'bottom',
				list: [
					{
						pagePath: '/pages/message/message',
						text: '消息',
						iconPath: '/pages/images/icon/static/inform.png',
						selectedIconPath: '/pages/images/icon/active/inform.png',
						clas: 'tabbar-item',
						active: true
					},
					{
						pagePath: '/pages/list/list',
						text: '列表',
						iconPath: '/pages/images/icon/static/list.png',
						selectedIconPath: '/pages/images/icon/active/list.png',
						clas: 'tabbar-item',
						active: false
					},
					{
						pagePath: '/pages/user/user',
						text: '我的',
						iconPath: '/pages/images/icon/static/user.png',
						selectedIconPath: '/pages/images/icon/active/user.png',
						clas: 'tabbar-item',
						active: false
					}
				]
			};
			
			var _curPageArr = getCurrentPages();
			var _curPage = _curPageArr[_curPageArr.length - 1];
			var _pagePath = _curPage.__route__;
			if (_pagePath.indexOf('/') != 0) {
				_pagePath = '/' + _pagePath;
			}
			
			var tabBar = tabBarData;
			for (var i = 0; i < tabBar.list.length; i++) {
				tabBar.list[i].active = false;
				if (tabBar.list[i].pagePath == _pagePath) {
					tabBar.list[i].active = true; // 根据页面地址设置当前页面状态
				}
			}
			_curPage.setData({
				tabBar: tabBar
			});
		}
	}
};
</script>

<style>
/* #ifndef APP-PLUS-NVUE */
/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
@import './common/uni.css';
/* css3 */
@import url('/components/animate.css');
/* #endif*/

/*每个页面公共css */
/* @import './common/style/aliicon.css'; */
.container {
	position: relative;
	width: 100%;
	height: auto;
	display: flex;
}

.navigationBar {
	width: 100%;
	position: fixed;
	z-index: 9999;
	left: 0;
	top: 0;
}

.column_center {
	display: flex;
	align-items: center;
}

.hor_center {
	display: flex;
	align-items: center;
}

.super_center {
	display: flex;
	justify-content: center;
	align-content: center;
	align-items: center;
}

.row {
	display: flex;
	flex-direction: row;
}

.column {
	display: flex;
	flex-direction: column;
}

.font-family {
	font-family: weiruanyahei;
}

/*  自定义tabbar样式*/
.tabbar {
	display: flex;
	flex-direction: row;
	position: fixed;
	width: 100%;
}

.tabbar-item {
	flex-grow: 1;
	padding: 6rpx 0;
	text-align: center;
}

.tabbar-icon {
	width: 46rpx;
	height: 46rpx;
	display: block;
	margin: 0 auto;
}

.tabbar-text {
	font-size: 24rpx;
}
</style>
