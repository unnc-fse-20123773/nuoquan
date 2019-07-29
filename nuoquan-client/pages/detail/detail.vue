<template>
	<view>
		<view class="topbar">
			<image src="../../static/icon/返回.svg" class="backk"></image>
			<view class="detailtitle">{{ articleCard.articleTitle }}</view>
		</view>

		<view class="drtailmain">
			<view style="height:5px;background-color: white;width:60%;margin:auto;"></view>
			<view class="detailcontent">{{ articleCard.articleContent }}</view>
			<view class="detailpics">
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
			</view>
			<view class="tags"><view class="tag" v-for="(i,index) in articleCard.tags" v-bind:key="index">{{i}}</view></view>

			<!-- 			<articlebrief :articlecontent="detailrole"></articlebrief>
 -->
			<commentbox v-for="comment in comments" :key="comment[0]" :comment="comment"></commentbox>
		</view>
	</view>
</template>

<script>
import articlebrief from '../../components/articlebrief';
import comment from '../../components/comment';
export default {
	data() {
		return {
			comments: [
				['00001', '评论者ID', '楼主好棒', '一小时前', '60', '7'],
				['00003', '评论者ID', '楼主求微信', '一小时前', '90', '7'],
				['00005', '评论者ID', '楼主求微信', '一小时前', '60', '7'],
				['00009', '评论者ID', '楼主求微信', '一小时前', '9', '70']
			],
            articleCard:"",
			taglist: [['123', 'background:red'], ['13', 'background:blue'], ['163', 'background:yellow']]
		};
	},
	components: {
		articlebrief,
		commentbox: comment
	},
	methods: {},
	onLoad(options) {
		console.log('detail receved');
		console.log(options.data);

		this.articleCard = JSON.parse(options.data);
		console.log(this.articleCard);
		console.log(this.articleCard.artiticleTitle);

	}
};
</script>

<style>
.topbar {
	height: 100px;
	background-color: RGB(253, 217, 108);
}
.backk {
	width: 15px;
	height: 10px;
}
.detailtitle {
	width: 85%;
	color: #f5f5f5;
	font-size: 14px;
	margin: auto;
	font-weight: 400;
}
.drtailmain {
	border-radius: 20px;
	margin-top: -20px;
	background: white;
	box-shadow: 0 -1px 8px grey;
}
.detailcontent {
	font-size: 13px;
	width: 85%;
	margin: 20px auto;
	font-weight: 400;
}
.detailpics {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-wrap: wrap;
	margin-bottom: 30px;
}
.detailpic {
	width: 180upx;
	height: 180upx;
	margin: 6px;
}
.tags {
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
}
.articleCard {
	margin: 2px auto 0;
	width: 90%;
	border-radius: 5px;
}
.touxiang {
	border-border-radius: 30px;
	width: 20px;
	height: 20px;
	margin-right: 5px;
	vertical-align: middle;
}
</style>
