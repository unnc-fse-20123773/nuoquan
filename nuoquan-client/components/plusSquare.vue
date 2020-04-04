<template>
	<view id="clickToChooseImage" class="addPic" @click="add">+</view>
</template>

<script>
	import nqLike from '../components/nq-button/nq-likeButton.vue';
	import nqCmt from '../components/nq-button/nq-cmtButton.vue';

	export default {
		name: 'aticlebrief',
		props: {
			articleCard: {}
		},
		components: {
			nqLike,
			nqCmt
		},
		data() {
			return {

			};
		},
		methods: {
			add() {
				this.$emit('plusClicked');
			}
		}
	}
</script>

<style>
	.addPic {
		margin: 6px 0;
		width: 190upx;
		height: 190upx;
		line-height: 160upx;
		text-align: center;
		vertical-align: middle;
		color: #888888;
		font-size: 50px;
		font-weight: 200;
		background: #ececec;
	}
</style>
