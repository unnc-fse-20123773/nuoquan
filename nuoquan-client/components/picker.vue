<template>
	<view>
		<view style="height: 100%;width: 100%;position: fixed;left: 0;top: 0;">
		</view>
		<view style="position: relative;height: 180px;width: 70px;">
			<view class="picker-shadow">
			</view>
			<picker-view style="width: 70px;height: 180px;position: relative;top: 2px;left: 2px;" v-if="visible"
			 :indicator-style="indicatorStyle" :value="value" @change="bindChange" :mask-style="maskStyle">
				<picker-view-column>
					<view class="item" v-for="(item,index) in years" :key="index">{{item}}</view>
				</picker-view-column>
			</picker-view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			const date = new Date()
			const years = []
			const year = date.getFullYear()

			for (let i = year; i <= year + 4; i++) {
				years.push(i)
			}

			return {
				title: 'picker-view',
				years,
				year,
				value: [1],
				/**
				 * 解决动态设置indicator-style不生效的问题
				 */
				visible: true,
				indicatorStyle: `height: ${Math.round(uni.getSystemInfoSync().screenWidth/(850/100))}px;`,
				maskStyle: '',
			}
		},
		methods: {
			bindChange(e) {
				const val = e.detail.value
				this.year = this.years[val[0]]
			}
		}
	}
</script>

<style>
	picker-view {
		width: 100%;
		height: 600upx;
	}

	.item {
		line-height: 90upx;
		text-align: center;
		font-size: 16px;
	}
</style>

<style>
	.picker-shadow {
		height: 184px;
		width: 74px;
		position: absolute;
		top: 0px;
		left: 0px;
		box-shadow: 0px 0px 10px 1px #A6A6A6;
		background-color: white;
		border-radius: 15upx;
		z-index: 0;
	}
</style>
