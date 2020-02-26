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
