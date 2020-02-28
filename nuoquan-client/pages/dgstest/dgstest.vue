<template>
	<view>
		<drag-sorts style="margin-top: 100px;" :list="list" :props="props" @change="onDragSortChange"></drag-sorts>
	</view>
</template>
<script>
import dragSorts from '@/components/drag-sorts/index.vue';
export default {
	components: {
		dragSorts
	},
	data() {
		return {
			// 对应需要显示的标题
			props: {
				label: 'label'
			},
			list: [
				{
					label: '标题1'
				},
				{
					label: '标题2'
				},
				{
					label: '标题3'
				},
				{
					label: '标题4'
				},
				{
					label: '标题5'
				},
				{
					label: '标题6'
				},
				{
					label: '标题7'
				},
				{
					label: '标题8'
				},
				{
					label: '标题9'
				},
				{
					label: '标题10'
				},
				{
					label: '标题11'
				},
				{
					label: '标题12'
				},
				{
					label: '标题13'
				}
			]
		};
	},
	methods: {
		onDragSortChange(e) {
			console.log(e);
		}
	}
};
</script>

<style>
page{
	height: 100%;
	width: 100%;
}
</style>
