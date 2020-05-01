<template>
	<view @click="receiveClick(obj)" hover-class="hoverColor">
		<view class="generic_item">
		<view class="left_icon" :style="{background: obj.style, display: 'inline-block', width:'28px', height:'28px', borderRadius:'50%'}">
			<image :src="'../static/icon/' + obj.lefticon_src + '.png'" mode="scaleToFill"></image>
		</view>
		<view class="page" v-if="obj.name=='profile'">{{lang.profile}}</view>
		<view class="page" v-if="obj.name=='myPublish'">{{lang.myPublish}}</view>
		<view class="page" v-if="obj.name=='myFavorite'">{{lang.myFavorite}}</view>
		<view class="page" v-if="obj.name=='changeLang'">{{lang.changeLang}}</view>
		<view class="page" v-if="obj.name=='about'">{{lang.about}}</view>
		<view class="right_icon0" v-if="obj.type==0">
			<image :src="'../static/icon/' + obj.righticon_src + '.png'" mode="scaleToFill"></image>
		</view>
		<view class="right_icon1" v-else>
			<nqSwitch
				:bgSwitchLeft = "'-13px'"
				:bgSwitchRight = "'41px'"
				:options='[lang.chinese, lang.english]' 
				:initStatus="lang.langType == 'zh-CN' ? 0: 1"
				@onChange="receiveSwitch()">
			</nqSwitch>
		</view>
		</view>
		<view class="line" v-if="obj.id!=5 && obj.id!=2"></view>
	</view>
	
</template>

<script>
import nqSwitch from '@/components/nq-switch.vue';

	export default {
		name: 'pagejump',
		components:{
			nqSwitch,
		},
		props: {
			objList: {},
			lang:"",
			
		},
	data(){
		return {
			obj: this.objList, 
		};
	},

	methods:{
		
		receiveSwitch(event){
			var eventStruct={
				type:'switchLang',
				event,
			}
			this.$emit('trigger', eventStruct);
		},
		
		receiveClick(event){
			var eventStruct={
				type:'click',
				event,
			}
			this.$emit('trigger', eventStruct);
		}
	}
}
</script>

<style>
.generic_item{
	position:relative;
	height:50px;
	display:flex;
	align-items: center;
	align-content: center;
}

.left_icon image{
	margin:auto;
	margin-top:6px;
	width:16px;
	height:16px;
}
.page{
	position: absolute;
	left: 15%;
	display: inline-block;
	font-size:14px;
}
.right_icon0{
	position:absolute;
	right:0;
	display: inline-block;
	width:20px;
	height:20px;
	opacity:1;
}
.right_icon1{
	position:absolute;
	right:0;
	display: inline-block;
	width:58px;
	height:24px;
	opacity:1;
}
.line{
	margin-left: auto;
		margin-right: auto;
		height: 0px;
		/* 高度 1 像素 */
		border: 0.5px solid rgba(236, 236, 236, 1); 
		opacity: 1;
}
</style>
