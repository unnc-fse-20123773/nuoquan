<template>
	<view class="index">
		<mainpagetop></mainpagetop>
		<articlebrief v-for="i in showlist" :key="i.id" v-bind:articleCard="i"></articlebrief>
	</view>
</template>

<script>
import articlebrief from '../../components/articlebrief';
import mainpagetop from '../../components/mainpagetop.vue';
import mainpageleft from '@/components/mainpageleft.vue'
export default {
	data() {
		return {
			title: 'Hello',
			hottitlelist: ['热门标题111', '热门标题222', '热门标题333'],
			showlist:"",
		
		};
	},
	components: {
		articlebrief: articlebrief,
		mainpagetop,
		mainpageleft,
	},
	created: function(){
		var _this=this;
		uni.request({
			url:'http://127.0.0.1:8080/queryAllArticles',
			method:"POST",
			success:(res)=>{
				_this.showlist = res.data.data.rows;
				console.log(res)
			},
			fail:(res)=>{
				console.log("index unirequest fail");
				console.log(res);
			}
		});
	},
	onLoad() {},
	methods: {
		show() {
			if (this.showmenu == 0) {
				this.showmenu = 1;
			} else {
				this.showmenu = 0;
			}
		}
	}
};
</script>

<style>
.index {
	background-color: #f3f3f3;
}
image {
	width: 60px;
	height: 60px;
	display: inline-block;
}
.touxiang {
	border-radius: 30px;
}
.search {
	display: inline-block;
	width: 70%;
	box-shadow: inset 0 0 20px #ccc;
	border-radius: 20upx;
	margin-left: 50upx;
}
.hot {
	background-color: #e4e4e4;
	width: 100%;
}
.hotitem {
	margin-bottom: 20px;
	background: white;
	height: 27px;
	border-color: rgb(255, 255, 255);
	box-shadow: rgb(170, 170, 170) 0px 0px 5px 0px;
	font-size: 14px;
	padding: 0px;
	border-width: 1px;
	border-style: solid;
	text-align: left;
	line-height: 20px;
	font-weight: normal;
	font-style: normal;
}
.arrow {
	width: 100upx;
	height: 60upx;
}
</style>