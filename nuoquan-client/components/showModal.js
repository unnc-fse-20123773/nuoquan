const showModal = {

    mutations: {
		hideModal(state) {
			// 小程序导航条页面控制
			// #ifndef H5
			if(state.hideTabBar){
				wx.showTabBar();
			}
			// #endif
			state.show = false
		},
		showModal(state,data) {
			state = Object.assign(state,data)
			console.log(state);
			state.show = true
		},
		success(state,res) {
			let cb = state.success
			let resObj={
				cancel:false,
				confirm:false
			}
			res=="confirm"?resObj.confirm=true:resObj.cancel=true
			cb && cb(resObj)
		}
    }
  };
  
  export default showModal;