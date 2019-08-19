<template>
	<view>
		<view class="send">
			<navigator url="../personpublic/personpublic">
				<image class="touxiang" src="../../static/touxiang.jpg"></image>
			</navigator>
			<view class="content">
				<view class="contentText">
					这里是1233聊天内容，巴123213拉巴拉一大堆,这里123是聊天内2，21巴拉21一大堆,1里是3聊天内容，巴拉巴拉一大堆,这里是聊天内容，巴拉巴拉一大堆
				</view>
				<view class="bottomBar">
					<view class="time">
						11:29
					</view>
					<view class="status">
						...
					</view>
					<view class="status" style="color:#3FC24A">
						√ 
					</view>
					
				</view>

			</view>
		</view>


		<view class="receve">
			<navigator url="../personpublic/personpublic">
				<image class="touxiang" src="../../static/touxiang.jpg"></image>
			</navigator>
			<view class="content">
				<view class="contentText">
					这里是1233聊天内容，巴123213拉巴拉一大堆,这里123是聊天内2，21巴拉21一大堆,1里是3聊天内容，巴拉巴拉一大堆,这里是聊天内容，巴拉巴拉一大堆
				</view>
				<view class="bottomBar">
					<view class="time">
						11:29
					</view>
					
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			thisMEssage: {},
		},
		data() {
			return {};
		},
		onLoad: function() {
			console.log(this.thisMessage);
			debugger;
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
		word-break: break-word;
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
