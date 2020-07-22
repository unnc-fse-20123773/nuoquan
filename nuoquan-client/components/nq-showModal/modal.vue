<template>
	<view class="container" v-show="show">
		<view class="modalBox" @click="closeModal" @touchmove.stop.prevent="">
			<!-- 			<image class="pic" v-if="modalPic" :src="modalPic"></image> -->
			<view class="modal-title" v-show="title">{{title}}</view>
			<view class="modal-content">{{content}}</view>
			<view class="modal-menu" v-show="mode == 'modal'">
				<button class="cancle" v-show="showCancel"  @click.stop="clickBtn('cancel')">{{cancelText}}</button>
				<button class="confirm"  @click.stop="clickBtn('confirm')">{{confirmText}}</button>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'

	export default {
		computed: {
			mode() {
				return this.$store.state.showModal.mode;
			},
			show() {
				return this.$store.state.showModal.show;
			},
			title() {
				return this.$store.state.showModal.title;
			},
			content() {
				return this.$store.state.showModal.content;
			},
			showCancel() {
				return this.$store.state.showModal.showCancel;
			},
			cancelText() {
				return this.$store.state.showModal.cancelText;
			},
			// cancelColor() {
			// 	return "color:" + this.$store.state.showModal.cancelColor;
			// },
			confirmText() {
				return this.$store.state.showModal.confirmText;
			},
			// confirmColor() {
			// 	return "color:" + this.$store.state.confirmColor;
			// },
			duration() {
				return this.$store.state.showModal.duration;
			}
		},

		methods: {
			closeModal() {
				this.$store.commit('hideModal')
			},
			clickBtn(res) {
				if (res == 'confirm') {
					this.$store.commit('success', res)
				} else if (res == 'cancle') {
					this.$store.commit('fail', res)
				}
				this.$store.commit('hideModal')
			}
		},
		beforeDestroy() {
			this.$store.commit('hideModal')
		},
		watch:{
			show(newValue,oldValue){
				if(newValue){
					var that = this;
					console.log(this.$store.state.showModal);
					if (this.$store.state.showModal.mode == 'toast') {
						console.log("mode == toast, timing");
						console.log(that.duration);
						debugger;
						setTimeout(function() {
							that.$store.commit('hideModal')
						}, that.duration);
					}
				}
				
			}
		},
		mounted() {
			var that = this;
			console.log(this.$store.state.showModal);
			if (this.$store.state.showModal.mode == 'toast') {
				console.log("mode == toast, timing");
				console.log(that.duration)
				setTimeout(function() {
					that.$store.commit('hideModal')
				}, that.duration);
			}
		}
	};
</script>


<style>
	.container {
		background-color: #ECECECEC;
		position: fixed;
		top: 0;
		left: 0;
		width: 750upx;
		height: 100%;
	}

	.modalBox {
		border-radius: 12px;
		background: #FFFFFF;
		width: 670upx;
		position: absolute;
		left: 40upx;
		top: 400px;
	}

	.pic {
		display: block;
		margin: 36px auto 0;
		width: 280upx;
		heihgt: 280upx;
	}

	.modal-title {
		font-size: 17px;
		color: #353535;
		font-weight: 600;
		height: 20px;
		ling-height: 20px;
		text-align: center;
		margin: 28px auto 20px;
	}

	.modal-menu {
		display: flex;
		width: 606upx;
		margin: 16px auto;
		justify-content: space-between''
	}

	.cancle {
		color: #888888;
		width: 292upx;
		height: 41px;
		background: rgba(236, 236, 236, 1);
		border-radius: 8px;
		line-height: 41px;
	}

	.confirm {
		color: #FFFFFF;
		width: 292upx;
		height: 41px;
		background: rgba(252, 192, 65, 1);
		border-radius: 8px;
		line-height: 41px;
	}
</style>
