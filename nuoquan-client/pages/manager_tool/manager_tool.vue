<template>
	<view class="page-view">
	
		
	<articlebrief v-for="i in articleList" :key="i.id" v-bind:articleCard="i"></articlebrief>
	
	</view>
</template>

<script>
import articlebrief from '../../components/articlebrief';
	export default {
		components:{
			articlebrief,
		},
		data() {
			return {
				articleList:"", //所有卡片，到这个变量，  = res.data.data.rows
				userInfo:"",
			}
		},
		onLoad() {
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../signin/signin'
				});
				return;
			} else {
				this.userInfo = userInfo; // 刷去默认值(若有)
			}
			this.showArticles(1);
		},
		methods: {
			check_pass(){
				//审核通过函数
				
			},	
			check_fail(){
				//审核    不    通过函数
				
			},
			
			
			
			showArticles: function(page) {
				//发送的page信息为定值，保留传入但需要连接到 request 的 data   ---yao
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/queryAllArticles',
					method: 'POST',
					data: {
						page: "1",
						userId: that.userInfo.id
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						this.articleList = res.data.data.rows;
						//从请求的数据赋予前端
					},
				});
			},//showArticle
		}//method
	}
</script>

<style>
	page {
		height: 100%;
		width: 100%;
		background: #FCFCFC;
	}

	.page-view{
		width: calc(100% - 32px);
		margin:auto;
		background: #FCFCFC;
	}
</style>
