import Vue from 'vue'
import Vuex from 'vuex'
import userUtil from '../common/userUtil.js'
import message from '../components/message.js'
import language from '../components/language.js'
import showModal from '../components/showModal.js'

Vue.use(Vuex)

/**
 * 如果为空返回0，否则返回本身
 * @param {Object} str
 */
var isNullThenZero = function(str) {
	if (str == null || str == "" || str == undefined) {
		return 0;
	}
	return str;
}
var userInfo = userUtil.getGlobalUserInfo();
// 消息提示相关变量
var myMsgCountKey = userInfo.id + ":myMsgCount";
var totalChatMsgCountKey = userInfo.id + ":totalChatMsgCount"
var likeMsgCountKey = userInfo.id + ":likeMsgCount"
var commentMsgCountKey = userInfo.id + ":commentMsgCount" 
// 国际化相关代码
try {
	var langList = ['简体中文', 'English'];
	var langCode = ['zh-CN', 'en'];
	// 1. 分析用户已经选择的语言 
	var userLang = uni.getStorageSync("userLang");
	// 2. 如果用户没有选择过获取用户手机的语言
	if (!userLang) {
		const sys = uni.getSystemInfoSync();
		userLang = sys.language;
	}
	console.log(userLang);
	// 以中英文切换为例, 其他语言请使用 getSystemInfoSync 获取语言对应的字符串
	// 然后扩展语言包即可
	if (userLang.substring(0, 2) == 'zh') {
		var lang = require('../common/language/zh.js');
	} else {
		var lang = require('../common/language/en.js');
	}
} catch (e) {
	// error
}


const store = new Vuex.Store({

	state: {
		lang: lang ,//语言
		
		chatMessageCard: '', // 暂存一条socket接收的聊天消息 & 刷新消息列表的条件
		// flashChatPage: "doFlash", // 作为触发 chatPage 刷新的条件
		myMsgCount: isNullThenZero(uni.getStorageSync(myMsgCountKey)), // 所有消息计数
		totalChatMsgCount: isNullThenZero(uni.getStorageSync(totalChatMsgCountKey)), //聊天消息总计数
		likeMsgCount: isNullThenZero(uni.getStorageSync(likeMsgCountKey)), // 点赞未读消息计数
		commentMsgCount: isNullThenZero(uni.getStorageSync(commentMsgCountKey)), // 评论未读消息计数
		
		//控制弹窗相关
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
	modules:{
		message:message,
		language:language,
		showModal,showModal,
	},
})

export default store
