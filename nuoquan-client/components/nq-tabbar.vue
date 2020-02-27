<template name="tabBar">
	<view
		class="tabbar"
		:style="{
			color: tabBar.color, 
			background: tabBar.backgroundColor, 
			top: tabBar.position=='top' ? '0' : '',
			bottom: tabBar.position=='bottom' ? '0' : '',
			'border-bottom': tabBar.position=='top' ? 'solid 1px' + tabBar.borderStyle : '',
			'border-top': tabBar.position=='top' ? 'solid 1rpx' + tabBar.borderStyle : ''
		}"
	>
		<view v-for="item in tabBar.list" :key="item.pagePath">
			<navigator
				:url="item.pagePath"
				open-type="switchTab"
				:class="item.clas"
				:style="{
					color: item.active ? (item.selectedColor ? item.selectedColor : tabBar.selectedColor) : ''
				}"
			>
				<image :src="item.selectedIconPath" v-if="item.active" class="tabbar-icon"></image>
				<image :src="item.iconPath" v-if="!item.active" class="tabbar-icon"></image>
				<text class="tabbar-text">{{ item.text }}</text>
			</navigator>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	}
};
</script>

<style></style>
