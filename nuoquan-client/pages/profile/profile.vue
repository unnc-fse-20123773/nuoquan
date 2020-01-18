<template>
	<view>
		<view>
			这是个人主页
		</view>
		<navigator url="../fanslist/fanslist">
			<button type="primary" size="mini">点击这里查看我的粉丝</button>
		</navigator>
		<navigator url="../followlist/followlist">
			<button type="primary" size="mini">点击这里查看我关注的人</button>
		</navigator>
	</view>
</template>
<script>
	export default {
		data() {
			return {}

		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "个人主页"
			});
		},
		methods: {

		}
	}
</script>

<style>
	.uni-list-cell {
		justify-content: flex-start
	}
</style>
