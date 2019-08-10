<template>
	<view class="navigatorhub-position">
		<text>不可移动的传送门</text>
		<view class="" v-for="item in pagename" :key="item.value">
			<button type="primary" size="mini">{{item.name}}</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				pagename: [
					{
						name: 'index',
						value: '1',
						url: '../../pages/index/index'
					},
					{
						name: 'detail',
						value: '2'
					},
					{
						name: 'fanslist',
						value: '3'
					},
					{
						name: 'followlist',
						value: '4'
					},
					{
						name: 'personpublic',
						value: '5'
					},
					{
						name: 'profile',
						value: '6'
					},
					{
						name: 'search',
						value: '7',
					},
					{
						name: 'signin',
						value: '8'
					},
					{
						name: 'sublit',
						value: '9'
					},
					{
						name: 'black-index',
						value: '10'
					}
				]
			}
		},
		methods: {
			
		}
	}
</script>

<style>
	.navigatorhub-position{
		width: 300upx;
		background-color: yellow;
		position: fixed;
	}
</style>
