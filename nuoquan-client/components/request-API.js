const request = {
	namespaced: false,
	state: {

	},

	mutations: {
	},
	actions: {
		request(context, payload) {
			console.log(context);
			console.log(payload);
			context.commit('showLoading');
			console.log('in API action'),
			console.log(payload),
			uni.request({
				url: 'https://www.checkchack.cn:8443/nottinghome/article/queryArticles',
				method: 'POST',
				data:payload.data,
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log('in API res');
					console.log(res);
					if(res.data.status == 200){
						console.log('status 200, start callback');
						//context.commit('successCB',payload);
						payload.success(res.data.data.rows);
						console.log('after success check');
					}else{
						console.log('request error, code ' + res.data.status);
					}
				},
				fail: res => {
					console.log('requst failed, net error');
					console.log(res);
				}
			});
		},
	},
};

export default request;
