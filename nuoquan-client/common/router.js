/**
 * 页面跳转方法
 */

function goToPersonPublic(userId){
	uni.navigateTo({
		url: '/pages/personpublic/personpublic?userId=' + userId,
	})
}

export default {
   goToPersonPublic,
}