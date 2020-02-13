Component({
	data: {
		selected: 0,
		color: "#7A7E83",
		selectedColor: "#3cc51f",
		list: [{
				pagePath: 'index',
				iconPath: '../static/icon/star.png',
				selectedIconPath: '../static/icon/star.png',
				text: '主页'
			},
			{
				pagePath: 'votePage',
				iconPath: '../static/icon/star.png',
				selectedIconPath: '../static/icon/star.png',
				text: '投票'
			},{
				pagePath: 'messagelist',
				iconPath: '../static/icon/star.png',
				selectedIconPath: '../static/icon/star.png',
				text: '动态'
			},{
				pagePath: 'profile',
				iconPath: '../static/icon/star.png',
				selectedIconPath: '../static/icon/star.png',
				text: '我的'
			}
		]
	},
	attached() {},
	methods: {
		switchTab(e) {
			const data = e.currentTarget.dataset
			console.log(data)
			const url = data.path
			wx.switchTab({
				url
			})
			this.setData({
				selected: data.index
			})
		},
		
		setTabBarIndex(index){
		      if (typeof this.$mp.page.getTabBar === 'function' &&
		        this.$mp.page.getTabBar()) {
		        this.$mp.page.getTabBar().setData({
		          selected:index
		        })
		      }
		    }
	}
})
