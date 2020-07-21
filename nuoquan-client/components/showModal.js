const showModal = {
	state:{
		show:false,
		title:"标题",
		content:'内容',
		showCancel:true,
		cancelText:"取消",
		cancelColor:"#000000",
		confirmText:"确定",
		confirmColor:"#576b95",
		success:null,
		
	},

	mutations: {
		hideModal(state) {
			state.show = false
		},
		showModal(state, data) {
			console.log('in mutation -->showModal function');
			console.log(state);
			state = Object.assign(state, data)
			console.log(state.show);
			state.show = true;
			console.log(state.show);
		},
		success(state, res) {
			let cb = state.success
			let resObj = {
				cancel: false,
				confirm: false
			}
			res == "confirm" ? resObj.confirm = true : resObj.cancel = true
			cb && cb(resObj)
		}
	}
};

export default showModal;
