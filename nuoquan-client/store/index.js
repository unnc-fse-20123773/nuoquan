import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

var isNull = function(str) { // 数字0会被判定为true
	if (str == null || str == "" || str == undefined) {
		return true;
	}
	return false;
}

// 消息提示相关变量
var sMyMsg = uni.getStorageSync('myMsgCount'); // 其实返回 null 会被当成 0
var sLikeMsg = uni.getStorageSync('likeMsgCount');
var sCommentMsg = uni.getStorageSync('commentMsgCount');

// 国际化相关代码
try {
	var langList = ['简体中文', 'English'];
	var langCode = ['zh-CN', 'en'];
	// 1. 分析用户已经选择的语言 
	var userLang = uni.getStorageSync("userLang");
	// 2. 如果用户没有选择过获取用户手机的语言
	if(!userLang){
		const sys = uni.getSystemInfoSync();
		userLang = sys.language;
	}
	console.log(userLang);
	// 以中英文切换为例, 其他语言请使用 getSystemInfoSync 获取语言对应的字符串
	// 然后扩展语言包即可
	if(userLang.substring(0,2) == 'zh'){
		var lang = require('../common/language/zh.js');
	}else{
		var lang = require('../common/language/en.js');
	}
} catch (e) {
    // error
}

const store = new Vuex.Store({
	state: {
		chatMessageCard: '', // 暂存一条socket接收的聊天消息 & 刷新消息列表的条件
		flashChatPage: "doFlash", // 作为触发 chatPage 刷新的条件
		myMsgCount: sMyMsg==null? 0 : sMyMsg, // 左侧栏通用未读消息计数
		likeMsgCount: sLikeMsg==null? 0 : sLikeMsg, // 点赞未读消息计数
		commentMsgCount: sCommentMsg==null? 0 : sCommentMsg, // 评论未读消息计数
		
		lang : lang //语言
	},
	mutations: {
		// 消息提示相关
		setChatMessageCard(state, value) {
			state.chatMessageCard = value;
		},

		doFlashChatPage(state, value) {
			// 获取当前时间，使数据变化
			state.flashChatPage = new Date().getTime();
		},

		setMyMsgCount(state, value) {
			if (value == undefined) {
				state.myMsgCount++;
				uni.setStorageSync('myMsgCount', state.myMsgCount);
				// console.log("value未传值，当前myMsgCount=" + state.myMsgCount);
			} else {
				state.myMsgCount = value;
				uni.setStorageSync('myMsgCount', state.myMsgCount);
				// console.log("获取到value值，当前myMsgCount=" + state.myMsgCount);
			}
		},
		
		setLikeMsgCount(state, value){
			if (value == undefined) {
				state.likeMsgCount++;
				uni.setStorageSync('likeMsgCount', state.likeMsgCount);
			} else {
				state.likeMsgCount = value;
				uni.setStorageSync('likeMsgCount', state.likeMsgCount);
			}
		},
		
		setCommentMsgCount(state, value){
			if (value == undefined) {
				state.commentMsgCount++;
				uni.setStorageSync('commentMsgCount', state.commentMsgCount);
			} else {
				state.commentMsgCount = value;
				uni.setStorageSync('commentMsgCount', state.commentMsgCount);
			}
		},
		
		/**
		 * 更改语言包
		 * @param {Object} state
		 */
		changeLang: function(state){
			uni.showActionSheet({
				itemList: langList,
				success:function(e){
					if(e.tapIndex == 0){
						lang = require('../common/language/zh.js');
					}else{
						lang = require('../common/language/en.js');
					}
					state.lang = lang;
					uni.setStorageSync('userLang', langCode[e.tapIndex]);
				}
			})
		}
	},

})

export default store
