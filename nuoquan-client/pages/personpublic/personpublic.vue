<template>
	<view>
		这是他人主页
		<navigator url="../fanslist/fanslist">
			<button type="primary" size="mini">点击这里查看他的粉丝</button>
		</navigator>
		<navigator url="../followlist/followlist">
			<button type="primary" size="mini">点击这里查看他关注的人</button>
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
				title: "XXX的主页"
			});
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
