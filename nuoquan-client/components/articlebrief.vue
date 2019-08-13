<template>
	<view class="articlecard" id="'+articleCard.id+'" @click="jumpToDetail()">
		
		
		<view class="title">{{ articleCard.articleTitle }}</view>
		<view class="briefarticleCard">{{ articleCard.articleContent }}</view>
		<view class="picturearea">
			<image src="../static/0001/pic1.jpg" v-if="articleCard[3]"></image>
			<!-- G暂时用地址表示 -->
			<image src="../static/0001/pic2.jpg" v-if="articleCard[4]"></image>
			<image src="../static/0001/pic3.jpg" v-if="articleCard[5]"></image>
		</view>
		<view class="tags">
			<view class="tag" v-for="(i, index) in articleCard.tags" v-bind:key="index" >{{ i}}</view>
		</view>
		<view class="menubar">
			<image src="../static/touxiang.jpg" class="touxiang"></image>
			<view class="name">{{ articleCard.nickName }}</view>
			<view class="time">{{ articleCard[7] }}</view>

			<view class="icons">
				<image class="comment" src="../static/icon/comment.svg">{{ articleCard.commentNum }}</image>
				<image class="like" src="../static/icon/like.svg">{{ articleCard.likeNum }}</image>
			</view>
		</view>
		<!-- 		</navigator>
 -->
	</view>
</template>

<script>
export default {
	name: 'aticlebrief',
	props: {
		articleCard: {}
	},
	data() {
		return {
			taglist: [
				['123', 'background:#40A792'],
				['13', 'background:#621E81'],
				['163', 'background:#738598'],
				['标签', 'background:#F3AE4B'],
				['13', 'background:#621E81'],
				['163', 'background:#738598'],
				['123', 'background:#40A792'],
				['13', 'background:#621E81']
			]
		};
	},
	created(){
		console.log(this.articleCard.tags);
				console.log(typeof(this.articleCard.tags));
		
	},
	methods: {
		// loadcss(){
		// 	var a = Math.random()
		// console.log('brif1');
		// 	if(a==0.5){
		// 		console.log('yes');
		// 		require('./articleCardBrief.css');}else{
		// 		console.log('not');
		// 	}
		// 	console.log('brif2');
		// 	
		// },
		jumpToDetail() {
			var navData = JSON.stringify(this.articleCard); // 这里转换成 字符串
			uni.navigateTo({
				url: '/pages/detail/detail?data=' + navData
			});
		}
	},

};
</script>

<style>
	image{
		border:none;
		outline: none;
	}
.articlecard {
	width: 650upx;
	border-radius: 8px;
	margin: 11px auto 0;
	background-color: #ffffff;
}

.title {
	margin: 16px 25px 0 25px;
	font-size: 15px;
	font: MicrosoftYaHei;
	font-weight: bold;
	line-height: 19px;
	margin: 16px 13px 0 15px;
	padding-top: 16px;
}

.briefarticleCard {
	margin: 10px 13px 0 15px;
	font-size: 13px;
	line-height: 15px;
	margin-bottom: 15px;
}

.tags {
	margin-left:10px;

}

.tag {
	display: inline-block;
	border-radius: 4px;
	padding-left: 5px;
	padding-right: 5px;
	margin-left: 5px;
	height: 15px;
	color: #ffffff;
	font-size: 10px;
	background: #621E81;
}

.menubar {
	position: relative;
	vertical-align: middle;
	margin-left: 15px;
	border-radius: 8px;
}

.touxiang {
	border-radius: 30px;
	width: 20px;
	height: 20px;
	margin-right: 5px;
	vertical-align: middle;
}

.name {
	display: inline-block;
	font-size: 10px;
	margin-left: 7px;
	color: #888888;
}

.time {
	display: inline-block;
	left: 120px;
	font-size: 10px;
	margin-left: 25px;
	color: #888888;
}

.icons {
	position: absolute;
	right: 0;
	width: 206upx;
	text-align: right;
	display: inline-block;
	background-image: url(../static/icon/iconsBG.png);
/* 		background-image: url(../static/icon/signinline.png);
 */	
/* 	background-repeat: no-repeat;
 */	overflow: hidden;
	border-bottom-right-radius: 8px;
	border: 10px #621E81;
		outline : 0px solid deeppink;
	
}

.icons image {
	position: relative;
	/* G添加相对位置 */
	width: 11px;
	height: 11px;
	padding-right: 10px;
}

.picturearea {
	margin: auto;
	display: flex;
	justify-content: center;
}

image {
	width: 30%;
	height: 200upx;
	margin: auto;
}
</style>
