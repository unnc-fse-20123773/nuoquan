<template>
	<view>
		<view class="messageArea">
			<onemessage v-for="(i,index) in chatContent" :key=index :thisMessage='i'></onemessage>
		</view>
		<view class="bottomBar">
			<input />
			<view class="icons">
				<image src="../../static/icon/viewLocalPic.png"></image>
				<image src="../../static/icon/emoji.png"></image>
				<image src="../../static/icon/littlePlane.png"></image>

			</view>
		</view>

	</view>
</template>

<script>
	import onemessage from './oneMessage'
	export default {
		components: {
			onemessage,
		},
		data() {
			return {

				chatContent: [{
					messageId: '0001',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0002',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0003',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0004',
					messageType: '0',
					messageTime: '11:29',
					messageStatus: '1'
				}, {
					messageId: '0006',
					messageType: '1',
					messageTime: '11:29',
					messageStatus: '0'
				}, {
					messageId: '0006',
					messageType: '0',
					messageTime: '11:29',
					messageStatus: '1'
				}, ]


			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "XXXX（聊天人的昵称）"
			});
		},
		methods: {

		}
	}
</script>

<style scoped>
	.messageArea {
		display: flex;
		flex-flow: column-reverse;
		margin-bottom: 90upx;
		overflow: hidden;
		background: #F5F5F5;
	}

	.bottomBar {
		position: fixed;
		display: flex;
		align-items: center;
		bottom: 0;
		height: 90upx;
		width: 100%;
		margin: 0;
		padding: 0;
		box-shadow: 0px -2px 2px 0px rgba(130, 130, 130, 0.20);
		background: #FFFFFF;
	}

	.bottomBar input {
		display: inline-block;
		width: 534upx;
		height: 50upx;
		border-radius: 24upx;
		border: solid 1px #C6C6C6;
		margin-left: 14upx;
	}

	.icons {
		display: inline-flex;
		width: 146upx;
		height: 90upx;
		margin-left: 18upx;
		justify-content: space-between;
		align-items: center;
	}

	.icons image {
		width: 34upx;
		height: 34upx;
		vertical-align: middle;
	}
</style>
