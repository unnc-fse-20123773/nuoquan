<template>
	<web-view :src="webUrl"></web-view>
</template>

<script>
	export default {
		data() {
			return {
				webUrl: ''
			}
		},
		onLoad(option){
			console.log(option.url);
			this.webUrl = option.url;
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
