<template>
	<view class="message-list">
		<block v-for="(it,i) in messagesList" :key="i">
			<view class="uni-swipe-action" style="height: 138upx;">
				<view class="delete-button super_center" v-if="showdelete == 1 && messageIndex == i" @tap="tapDelete(it)">
					<text style="color: white;text-align: center;font-size: small;">删除</text>
				</view>
				<view class="uni-swipe-action__container" style="width: 88%;
				margin-left: 6%;
				margin-top: 12upx;
				height: 120upx;
				background-color: white;"
				@touchstart="touchStart" @touchmove="touchMove" @touchend="touchEnd" @touchcancel="touchEnd" 
				 :style="{'transform':messageIndex == i ? transformX : 'translateX(0px)',
				 '-webkit-transform':messageIndex == i ? transformX : 'translateX(0px)',
				 'box-shadow': it.isRead == UNREAD ? '0upx 0upx 12upx 1upx #D1D1D1':''}" 
				 :data-index="i" :data-disabled="it.disabled">

					<view class="uni-swipe-action__content " @click="toMessageDetail(i,it.type)">
						<view class="item" :class="it.stick  ? 'stick' : ''"  @tap="tapCard(it)">
							<block>
								<view class="item-left">
									<!-- <avator-group :type="it.type" :avator="it.url"></avator-group> -->
									<view class="avator">
										<image class="img" :src="it.friendInfo.faceImg"></image>
									</view>
									
								</view>
								<view class="item-middle">
									<text :class="it.isRead == UNREAD? 'title' : 'title-read'">{{it.friendInfo.nickname}}</text>
									<text :class="it.isRead == UNREAD? 'message' : 'message-read'">{{it.msg}}</text>
								</view>
								
								<view class="item-right">
									<view class="time">{{timeDeal(it.createDate)}}</view>
									<view class="mark" v-if="it.isRead==UNREAD">{{it.count}}</view>									
								</view>
							</block>
						</view>
					</view>
				</view>
			</view>
		</block>
	</view>
</template>

<script>
	export default {
		name: 'swipe-action',
		props: {
			messagesList: '',
		},
		data() {
			const elId = `Uni_${Math.ceil(Math.random() * 10e5).toString(36)}`
			return {
				elId: elId,
				transformX: 'translateX(0px)',
				messageIndex: -1,
				showdelete: 0,
				
				READ: this.chat.READ,
				UNREAD: this.chat.UNREAD,
			}
		},
		created() {
			this.direction = ''
			this.startX = 0
			this.startY = 0
			this.btnGroupWidth = 0
			this.isMoving = false
		},
		// #ifdef H5
		mounted() {
			this.getSize()
		},
		// #endif
		// #ifndef H5
		onReady() {},
		// #endif
		methods: {
			toMessageDetail(i, type) {
				if (type == 2) {
					console.log('进入到聊天界面' + i)
					uni.navigateTo({
						url: "../message_info/message_info"
					})
				} else if (type == 1) {
					console.log('进入系统消息界面' + i)
				}
			},
			bindClickBtn(item, index) {
				this.messageIndex = -1;
				console.log(item.text + 'message第' + index + '项');

			},
			touchStart(event) {
				if (event.currentTarget.dataset.disabled === true) {
					return;
				}
				this.startX = event.touches[0].pageX;
				this.startY = event.touches[0].pageY;
			},
			touchMove(event) {
				if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
					this.direction = '';
					return;
				}
				var moveY = event.touches[0].pageY - this.startY,
					//  moveX用于判断方向
					moveX = -(event.touches[0].pageX - this.startX);
				if (!this.isMoving && Math.abs(moveY) > Math.abs(moveX) || Math.abs(moveY) > 100 || Math.abs(moveX) < 50) { //纵向滑动//参数100与50可调节侧滑灵敏度
					this.direction = 'Y';
					return;
				}
				// 移动距离
				console.log(moveX);
				this.direction = moveX > 0 ? 'right' : 'left';
				// 输出方向
				console.log(this.direction);
				this.messageIndex = moveX < 0 ? event.currentTarget.dataset.index : -1;
				this.isMoving = true;
			},
			touchEnd(event) {
				this.isMoving = false;
				if (this.direction !== 'right' && this.direction !== 'left') {
					this.direction = '';
					return;
				}

				// right -> left
				if (this.direction == 'right') {
					this.messageIndex = -1;
				}
				console.log(this.messageIndex);
				this.endMove(event)
			},
			endMove(event) {
				if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
					this.direction = '';
					return;
				}
				if (this.messageIndex !== -1) {
					// 负号控制块左右移动
					// this.transformX = `translateX(${-this.btnGroupWidth}px)`;
					this.transformX = `translateX(${58}px)`;
					this.$emit('opened');
					this.showdelete = 1;
				} else {
					this.transformX = 'translateX(0px)';
					this.$emit('closed');
					this.showdelete = 0;
				}
				this.direction = '';
			},
			
			tapCard(it){
				this.$emit("tapCard", it);
			},
			tapDelete(it){
				this.$emit("tapDelete", it);
				this.showdelete = 0;
				this.transformX = 'translateX(0px)';
			}
		}
	}
</script>

<style lang="scss">
	.delete-button {
		position: fixed;
		background-color: #007AFF;
		width: 50px;
		height: 54upx;
		margin-top: 44upx;
		margin-left: 16px;
		background-color: #fccf41;
		border-radius: 25upx;
	}

	// .msglist-card {
	// 	width: 88%;
	// 	margin-left: 6%;
	// 	margin-top: 8upx;
	// 	height: 120upx;
	// 	background-color: white;
	// 	border-radius: 12upx;
	// 	box-shadow: 0upx 0upx 14upx 0upx #B2B2B2;
	// }
	// 
	// .msglist-card-read {
	// 	width: 88%;
	// 	margin-left: 6%;
	// 	margin-top: 8upx;
	// 	height: 120upx;
	// 	background-color: white;
	// 	border-radius: 12upx;
	// }

	.uni-swipe-action {
		width: 100%;
		overflow: hidden;

		&__container {
			background-color: #FFFFFF;
			width: 200%;
			display: flex;
			float: left;
			flex-direction: row;
			flex-wrap: wrap;
			transition: transform 350ms cubic-bezier(.165, .84, .44, 1);
		}

		&__content {
			width: 100%;
		}

		&__btn-group {
			display: flex;
			flex-direction: row;
			align-items: center;
			background-color: #4191ea;
		}

		&--btn {
			height: 50%;
			padding: 0 32upx;
			color: #FFFFFF;
			background-color: #fccf41;
			font-size: 28upx;
			display: inline-flex;
			text-align: center;
			flex-direction: row;
			align-items: center;
			margin-left: 20upx;
			margin-right: 20upx;
			border-radius: 25upx;
		}
	}

	.item {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		padding: 10upx 16upx;

		&.stick {
			background-color: rgba(243, 236, 221, .5);
		}

		.item-left {
			display: flex;
			align-items: center;
			text-align: center;

			.image {
				width: 100upx;
				height: 100upx;
				border-radius: 50upx;
				background-color: #eee;
			}

			
			.avator{
				width: 80upx;
				height: 80upx;
				border-radius: 50%;
				overflow: hiideen;
				background: #f1f1f1;
				.img{
					width: 100%;
					height: 100%;
					border-radius: 50%;
				}
				.iconfont {
					width: 100upx;
					height: 100upx;
					text-align: center;
					line-height: 100upx;
					color: #fff;
					font-size: 40upx;
				}
			}
		}

		.item-middle {
			display: flex;
			flex: 1;
			flex-wrap: wrap;
			flex-direction: row;
			justify-content: flex-start;
			margin-left: 15upx;
			overflow: hidden;

			.title {
				width: 100%;
				color: #353535;
				font-family: "微软雅黑";
				font-weight: 400;
				font-size: 30upx;
				height: 50upx;
				line-height: 60upx;
				overflow: hidden;
				/*自动隐藏文字*/
				text-overflow: ellipsis;
				/*文字隐藏后添加省略号*/
				white-space: nowrap;
				/**强制不换行*/
			}
			
			.title-read {
				width: 100%;
				color: #9b9b9b;
				font-family: "微软雅黑";
				font-weight: 400;
				font-size: 30upx;
				height: 50upx;
				line-height: 60upx;
				overflow: hidden;
				/*自动隐藏文字*/
				text-overflow: ellipsis;
				/*文字隐藏后添加省略号*/
				white-space: nowrap;
				/**强制不换行*/
			}

			.message {
				width: 100%;
				font-family: "微软雅黑";
				color: #6f6f6f;
				height: 50upx;
				line-height: 40upx;
				font-size: 24upx;
				overflow: hidden;
				/**自动隐藏文字*/
				text-overflow: ellipsis;
				/**文字隐藏后添加省略号*/
				white-space: nowrap;
				/**强制不换行*/
			}
			
			.message-read {
				width: 100%;
				font-family: "微软雅黑";
				color: #9b9b9b;
				height: 50upx;
				line-height: 40upx;
				font-size: 24upx;
				overflow: hidden;
				/**自动隐藏文字*/
				text-overflow: ellipsis;
				/**文字隐藏后添加省略号*/
				white-space: nowrap;
				/**强制不换行*/
			}
		}

		.item-right {
			position: relative;
			overflow: hidden;
			display: flex;
			flex-direction: column;
			align-items: center;

			.time {
				margin-top: -10upx;
				color: #808080;
				font-size: 18upx;
				height: 60upx;
				line-height: 60upx;
				max-width: 85px;
				text-overflow: ellipsis;
			}

			.mark {
				position: absolute;
				right: 0;
				bottom: 15upx;
				background-color: #fdd453;
				line-height: 35upx;
				text-align: center;
				font-size: 18upx;
				min-width: 15upx;
				min-height: 15upx;
				border-radius: 18upx;
				color: #fff;
			}
		}
	}
	
</style>
