/*
	#接入式网页展示
	#url 由外部传入
	#用于展示各种广告推文网站
	Author: Guetta
	Time: 24 May 2020
*/
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
