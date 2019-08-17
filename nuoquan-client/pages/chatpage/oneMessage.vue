<template>
	<view>
		<view class="send" v-if="thisMessage.flag == ME">
			<navigator url="../personpublic/personpublic">
				<image class="touxiang" src="../../static/touxiang.jpg"></image>
			</navigator>
			<view class="content">
				<view class="contentText">
					{{thisMessage.msg}}
				</view>
				<view class="bottomBar">
					<view class="time">
						{{thisMessage.messageTime}}
					</view>
					<view class="status" v-if="!thisMessage.messageStatus">
						...
					</view>
					<view class="status" style="color:#3FC24A" v-if="thisMessage.messageStatus">
						√
					</view>

				</view>

			</view>
		</view>

		<view class="receve" v-if="thisMessage.flag == FRIEND">
			<navigator url="../personpublic/personpublic">
				<image class="touxiang" src="../../static/touxiang.jpg"></image>
			</navigator>
			<view class="content">
				<view class="contentText">
					{{thisMessage.msg}}
				</view>
				<view class="bottomBar">
					<view class="time">
						{{thisMessage.messageTime}}
					</view>

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
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

	.time {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		max-width: 60px;
		color: #939393;
		margin-right: 6px;
	}

	.status {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		margin-right: 6px;
	}

	.contentText {
		width: 400upx;
		font-size: 12px;
		word-break: break-all;
	}
</style>
<style>
	.send {
		display: flex;
		flex-direction: row-reverse;
		margin-right: 30upx;
	}

	.send .content {
		display: inline-flex;
		flex-direction: column;
		width: 400upx;
		background: #FCCB2E;
		border-radius: 10pt 0pt 10pt 10pt;
		margin-top: 30upx;
		margin-right: 20upx;
		padding: 16upx 14upx 0upx 16upx;
	}
</style>

<style>
	.receve {
		display: flex;
		flex-direction: row;
		margin-right: 30upx;
	}

	.receve .content {
		display: inline-flex;
		flex-direction: column;
		width: 400upx;
		background: #FFFFFF;
		border-radius: 10pt 0pt 10pt 10pt;
		margin-top: 30upx;
		margin-right: 20upx;
		padding: 16upx 16upx 0upx 14upx;
		justify-content: flex-end;
	}
</style>
