import Vue from 'vue'
import App from './App'


Vue.config.productionTip = false
Vue.prototype.SeverUrl="http://127.0.0.1:8080"

App.mpType = 'app'

Vue.prototype.setGlobalUserInfo = function(user) {
	uni.setStorageSync('userInfo', user);
}

Vue.prototype.getGlobalUserInfo = function() {
	// var value = uni.getStorageSync('userInfo');
	var value = {
		id: 'test-id123',
		nickname: 'testname'
	}
	return value;
}

Vue.prototype.removeGlobalUserInfo = function() {
	uni.removeStorageSync('userInfo');
}

const app = new Vue({
    ...App
})
app.$mount()


