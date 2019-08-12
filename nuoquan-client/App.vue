<script>


	
	export default {
		onLaunch: function () {
			console.log('App Launch')
		},
		onShow: function () {
			console.log('App Show')
		},
		onHide: function () {
			console.log('App Hide')
		},
		
		/**
		 * 和后端的枚举对应
		 */
		CONNECT: 1,		// 第一次(或重连)初始化连接
		CHAT: 2,		// 聊天消息
		SIGNED: 3,		// 消息签收
		KEEPALIVE: 4, 	// 客户端保持心跳
		
		/**
		 * 和后端 ChatMessage 聊天模型的对象保持一致
		 * @param {Object} senderId
		 * @param {Object} receiverId
		 * @param {Object} msg
		 * @param {Object} msgId
		 */
		ChatMessage(senderId, receiverId, msg, msgId){
			this.senderId = senderId;
			this.receiverId = receiverId;
			this.msg = msg;
			this.msg = msg;
		},
		
		/**
		 * 构建消息 DataContent 模型对象
		 * @param {Object} action
		 * @param {Object} chatMsg
		 * @param {Object} extand
		 */
		DataContent(action, chatMsg, extand){
			this.action = action;
			this.chatMsg = chatMsg;
			this.extand = extand;
		}
		
		
	}
</script>

<style>
	/*每个页面公共css */
	.container {
		position: relative;
		width: 100%;
		height: auto;
		display: flex;
	}
	.column_center{
	display: flex;
	align-items: center;
	}
	
	.hor_center {
		display: flex;
		align-items: center;
	}
	
	.super_center {
		display: flex;
		justify-content: center;
		align-content: center;
		align-items: center;
	}
	
	.row {
		display: flex;
		flex-direction: row;
	}
	
	.column{
		display: flex;
		flex-direction: column;
	}
</style>
