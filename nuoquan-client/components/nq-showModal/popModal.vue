<template>
	<view class="container">
		<view class="modalBox">
			<!-- 			<image class="pic" v-if="modalPic" :src="modalPic"></image> -->
			<view class="modal-title">{{modalText.title}}</view>
<!-- 			<view class="modal-content">{{modalText.content}}</view>
 -->			<view class="modal-menu">
				<button class="cancle" @tap="modalClicked('cancle')">{{modalText.cancelText}}</button>
				<button class="confirm" @tap="modalClicked('confirm')">{{modalText.confirmText}}</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			modalText: {},
		},
		methods: {
			modalClicked(a) {
				this.$emit('modalRes', a);
			},
		},
		mounted() {
			console.log(this.modalText);
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
	
	.modal-menu{
		display: flex;
		width:606upx;
		margin:16px auto;
		justify-content:space-between''
	}

	.cancle{
		color:#888888;
		width:292upx;
		height:41px;
		background:rgba(236,236,236,1);
		border-radius:8px;
		line-height: 41px;
	}
	
	.confirm{
		color:#FFFFFF;
		width:292upx;
		height:41px;
		background:rgba(252,192,65,1);
		border-radius:8px;
		line-height: 41px;
	}
</style>
