Component({
	data: {
		selected: 0,
		color: "#7A7E83",
		selectedColor: "#3cc51f",
		rotateStatus: true,//旋转状态,判断静止or顺时针or逆时针
		midIconClass: "midIcon",//动态icon类名
		list: [{
				pagePath: 'index',
				iconPath: '../static/icon/home_d4d4d4.png',
				selectedIconPath: '../static/icon/home_ffffff.png',
				text: '主页'
			},
			{
				pagePath: 'votePage',
				iconPath: '../static/icon/chartbar_d4d4d4.png',
				selectedIconPath: '../static/icon/chartbar_ffffff.png',
				text: '投票'
			},{
				pagePath: '',
				iconPath: '../static/icon/comment_dots_d4d4d4.png',
				selectedIconPath: '../static/icon/comment_dots_ffffff.png',
				isSpecial: 'true',
				text: ''
			},{
				pagePath: 'messagelist',
				iconPath: '../static/icon/comment_dots_d4d4d4.png',
				selectedIconPath: '../static/icon/comment_dots_ffffff.png',
				text: '动态'
			},{
				pagePath: 'mine',
				iconPath: '../static/icon/user_d4d4d4.png',
				selectedIconPath: '../static/icon/user_ffffff.png',
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
		    },
			
		rotate(){
			this.setData({
			rotateStatus : false
			})
		},
	}
})
