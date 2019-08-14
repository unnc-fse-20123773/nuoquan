<template>
	<view>
		这是我的所有消息列表，点击消息进入聊天窗口
		<navigator url="../chatpage/chatpage">
			<button type="primary" size="mini">进入聊天窗口</button>
		</navigator>
	</view>

</template>

<script>
	export default {
		data() {
			return {

			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "私信列表"
			});
		},
		methods: {

		}
	}
</script>

<style>

</style>
