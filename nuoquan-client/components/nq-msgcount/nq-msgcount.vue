<template>
	<view class="msgcount super_center" v-if="count">{{ count }}</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';

export default {
	props: {
		count: {
			type: Number,
			default: 0
		}
	},

	data() {
		return {};
	},
	computed: {
		...mapState(['totalMsgCount'])
	},
	watch: {
		
	},
};
</script>

<style>
.msgcount {
	position: absolute;
	padding: 1px 3px;
	right: 22px;
	min-width: 18px;
	height: 17px;
	background: rgba(242, 94, 94, 1);
	opacity: 1;
	border-radius: 4px;
	font-size: 11px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 22px;
	color: rgba(255, 255, 255, 1);
	opacity: 1;
}
</style>
