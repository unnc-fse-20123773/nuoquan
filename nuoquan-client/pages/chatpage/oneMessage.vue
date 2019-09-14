<template>
	<view>
		<view>
		<view class="send" v-if="thisMessage.flag == ME">
			<view @tap="goToPersonPublic(userInfo.id)">
				<image class="touxiang" :src="userInfo.faceImg"></image>
			</view>
			<view class="content">
				<view class="contentText-send">
					{{thisMessage.msg}}
				</view>
				<view class="bottomBar">
					<view class="time-send">
						{{thisMessage.createDate}}
					</view>
					<view class="status-send" v-if="!thisMessage.isRead">
						<!-- ... -->
					</view>
					<view class="status-send" style="color:#3FC24A" v-if="thisMessage.messageStatus">
						√
					</view>
				</view>
			</view>
		</view>
		</view>

		<view>
		<view class="recieve" v-if="thisMessage.flag == FRIEND">
			<view @tap="goToPersonPublic(friendInfo.id)">
				<image class="touxiang" :src="friendInfo.faceImg"></image>
			</view>
			<view class="content">
				<view class="contentText-recieve">
					{{thisMessage.msg}}
				</view>
				<view class="bottomBar">
					<view class="time-recieve">
						{{thisMessage.createDate}}
					</view>

				</view>
			</view>
		</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			userInfo: '',
			friendInfo: '',
			thisMessage: {},
		},
		data() {
			return {
				ME: this.chat.ME,
				FRIEND: this.chat.FRIEND,
			};
		},
		onReady: function() {
			// console.log(this.thisMessage);
		},
		methods: {
			goToPersonPublic(userId) {
				uni.redirectTo({ // 关闭页面再跳转，防止页面栈满, 无法返回
					url: '../personpublic/personpublic?userId='+ userId,
				});
			}
		},
	};
</script>

<style scoped>
	.touxiang {
		width: 60upx;
		height: 60upx;
		border-radius: 60upx;
	}

	.bottomBar {
		display: flex;
		width: 400upx;
		align-items: center;
		height: 18px;
		justify-content: inherit;
	}

</style>
<style>
	.send {
		display: flex;
		flex-direction: row-reverse;
		width: 100%;
		margin-right: 30upx;
	}

	.send .content {
		display: inline-flex;
		flex-direction: column;
		width: 400upx;
		background: #FCCB2E;
		border-radius: 6pt 0pt 6pt 6pt;
		margin-top: 30upx;
		margin-right: 20upx;
		padding: 16upx 14upx 0upx 16upx;
		box-shadow: 0px 0px 3px 1px #CCCCCC;
	}
	
	.time-send {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		max-width: 100px;
		color: white;
		margin-right: 6px;
	}

	.status-send {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		margin-right: 6px;
		color: white;
	}

	.contentText-send {
		width: 400upx;
		font-size: 12px;
		word-break: break-all;
		color: white;
	}
</style>

<style>
	.recieve {
		display: flex;
		flex-direction: row;
		margin-right: 30upx;
	}

	.recieve .content {
		display: inline-flex;
		flex-direction: column;
		width: 400upx;
		background: #FFFFFF;
		border-radius: 0pt 6pt 6pt 6pt;
		margin-top: 30upx;
		margin-left: 20upx;
		padding: 16upx 16upx 0upx 14upx;
		justify-content: flex-end;
		box-shadow: 0px 0px 3px 1px #CCCCCC;
	}
	
	.time-recieve {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		max-width: 100px;
		color: #939393;
		margin-right: 6px;
	}

	.contentText-recieve {
		width: 400upx;
		font-size: 12px;
		word-break: break-all;
	}
</style>
