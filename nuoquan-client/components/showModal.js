const showModal = {
	state:{
		mode:'modal',  //mode = "toast/modal"
		show:false,
		title:"默认标题",
		content:'',
		duration:'1500',
		showCancel:true,
		cancelText:"取消",
	//	cancelColor:"#000000",
		confirmText:"确定",
	//	confirmColor:"#576b95",
	//	icon/image:null, not finished,
		success:null,
		fail:null,
	},

	mutations: {
		hideModal(state) {
			console.log('hideModal');
			state.show = false;
			const defaultState = {
				mode:'modal',  //mode = "toast/modal"
					show:false,
					title:"默认标题",
					content:'',
					duration:'1500',
					showCancel:true,
					cancelText:"取消",
				//	cancelColor:"#000000",
					confirmText:"确定",
				//	confirmColor:"#576b95",
				//	icon/image:null, not finished,
					success:null,
					fail:null,
			}
			state = Object.assign(state, defaultState);
			console.log(state);
		},
		showModal(state, data) {
			state.mode = 'modal',
			console.log('showModal function triggered');
			state = Object.assign(state, data)
			state.show = true;
			console.log(state);
		},
		showToast(state,data){
			state.mode = 'toast',
			console.log('showToast function triggered');
			state = Object.assign(state, data);
			state.show = true;
			console.log(state);
		},
		showLoading(state,data){
			state.show = true;
		},
		success(state, res) {
			let cb = state.success
			let resObj = {
				cancel: false,
				confirm: false
			}
			res == "confirm" ? resObj.confirm = true : resObj.cancel = true
			cb && cb(resObj)
		},
		fail(state,res){
			let cb = state.success
			let resObj = {
				cancel: false,
				confirm: false
			}
			res == "confirm" ? resObj.confirm = true : resObj.cancel = true
			cb && cb(resObj)
		},
	},
};

export default showModal;
