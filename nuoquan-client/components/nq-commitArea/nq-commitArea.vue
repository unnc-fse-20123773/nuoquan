<template>
	<view class="nq-commitArea">
			<textarea class="inputArea" 
				placeholder-style="font-size:14px;font-weight:400;color:rgba(177,177,177,1);" 
				placeholder="回复+昵称" 
				cursor-spacing="100" auto-focus/>
			<view class="replyText">
				{{replyText}}
			</view>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';

export default {
	name: 'commitArea',
	data() {
		return {
			replyText: '',
		} 
	},
	props:{
		openOrigin:{
			type: String,
			default: ''
		}
	},
	computed: {
		...mapState(['lang'])
	},
	created() {
		console.log(this.openOrigin);
		if(this.openOrigin == "cmt-likedetail"){
			this.replyText = this.lang.submit;
			console.log(this.replyText);
		}
		
	},
	methods: {
		
	}
};
</script>

<style>
	.nq-commitArea{
		position: fixed;
		z-index: 999;
		bottom: 16px;
		width:calc(100% - 26px);
		height:160px;
		background:rgba(252,252,252,1);
		box-shadow:0px 0px 4px rgba(121,121,121,0.42);
		opacity:1;
	}
	
	.inputArea{
		position: absolute;
		top: 13px;
		width:calc(100% - 24px);
		height:110px;
		border:2px solid rgba(252,192,65,1);
		opacity:1;
		border-radius:8px;
	}
	
	.nq-commitArea, .inputArea{
		left: 0;
		right: 0;
		margin-left: auto;
		margin-right: auto;
	}
	
	.replyText{
		position: absolute;
		right: 23px;
		bottom: 5px;
		height:23px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:23px;
		color:rgba(252,192,65,1);
	}
</style>
