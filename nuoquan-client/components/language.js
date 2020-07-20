const language = {
	mutations: {
		
		/**
		 * 更改语言包
		 * @param {Object} state
		 */
		changeLang: function(state,index) {
			console.log(index);
			/*uni.showActionSheet({
				itemList: langList,
				success: function(e) {
					if (e.tapIndex == 0) {
						lang = require('../common/language/zh.js');
					} else {
						lang = require('../common/language/en.js');
					}
					state.lang = lang;
					uni.setStorageSync('userLang', langCode[e.tapIndex]);
				}
			})*/
			if (index == 0) {
				lang = require('../common/language/zh.js');
			} else {
				lang = require('../common/language/en.js');
			}
			uni.setStorageSync('userLang', langCode[index]);
			state.lang = lang;
		}
	},
}

export default language;