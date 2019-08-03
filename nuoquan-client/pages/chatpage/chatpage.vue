<template>
	<view>
		这是聊天窗口，点击对方头像进入对方主页
		<navigator url="../personpublic/personpublic">
			<button type="primary" size="mini">personpublic</button>
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
				title: "XXXX（聊天人的昵称）"
			});
		},
		methods: {

		}
	}
</script>

<style>

</style>
