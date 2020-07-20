const message = {
	mutations:{
		// 消息提示相关
		setChatMessageCard(state, value) {
			state.chatMessageCard = value.newValue;
			//修改totalChatMsgCount
			if (value.newValue.unreadCount == 0) {
				state.totalChatMsgCount = state.totalChatMsgCount - value.oldValue.unreadCount;
			} else {
				state.totalChatMsgCount++
			}
			uni.setStorageSync(totalChatMsgCountKey, state.totalChatMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
		
		setLikeMsgCount(state, value) {
			if (value == undefined) {
				state.likeMsgCount++;
			} else {
				state.likeMsgCount = value;
			}
			uni.setStorageSync(likeMsgCountKey, state.likeMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
		
		setCommentMsgCount(state, value) {
			if (value == undefined) {
				state.commentMsgCount++;
			} else {
				state.commentMsgCount = value;
			}
			uni.setStorageSync(commentMsgCountKey, state.commentMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
		
	}
}

export default message;