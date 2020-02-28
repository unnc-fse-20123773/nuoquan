<template>
	<view class="tabList_box">
		<view class="tabList">
			<botton class="super_center" style="width: 100%;height: 50px;" @click='onClick'>
				<text>发文章</text>
			</botton>
			<view style="width: 83px;
				margin-left: 8.5px;
				height: 1px;
				background-color:rgba(236,236,236,1) ;">
			</view>
			<botton class="super_center" style="width: 100%;height: 50px;">
				<text>发投票</text>
			</botton>
		</view>
	</view>
</template>

<script>
export default{
	data(){
		return {
			
		};
	},
	methods:{
		onClick(){
			console.log("click in tablist")
		}
	}
	
}

</script>

<style>
.tabList_box{
	position: absolute;
	left: 0;
	right: 0;
	bottom: 87px;
	margin-left: auto;
	margin-right: auto;	
}
	
.tabList{	
	width:115px;
	height:101px;
	display: flex;
	flex-direction: column;	
	background:rgba(255,255,255,1);
	box-shadow:0px 0px 4px rgba(0,0,0,0.16);
	opacity:1;
	border-radius:12px;
}

.tabList text{
	height: 21px;
	font-size:17px;
	font-family:Source Han Sans CN;
	font-weight:500;
	line-height:21px;
	color:rgba(74,74,74,1);
	opacity:1;
}

</style>
