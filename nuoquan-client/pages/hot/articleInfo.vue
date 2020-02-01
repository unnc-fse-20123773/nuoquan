<template>
<view style="width:100%;">
		<view class="oneArticle">
				<view class="title">
					{{ thisArticle.articleTitle }}
				</view>
				<view class="cardBody">
					<view class="left-body" :class="{ leftBodyWithPic : thisArticle.imgList.length!=0 }">
						<view class="content">
							{{ thisArticle.articleContent }}
						</view>
						<view class="bottomBar">
							<view class="time">
								{{timeDeal(thisArticle.createDate)}}
								<!--{{ thisArticle.createDate }}-->
							</view>
							<view class="comment">
								<image src="../../static/icon/comment-alt%20353535.png"></image>
								<view> {{thisArticle.commentNum}}</view>
							</view>
							<view class="like">
								<image v-if="!thisArticle.isLike"  src="../../static/icon/like.png" ></image>
								<image v-if="thisArticle.isLike"  src="../../static/icon/liked-red.png" ></image>
								<view>{{thisArticle.likeNum}}</view>
							</view>
							<view class="view">
								<image src="../../static/icon/eye-888888.png"></image>
								<view>{{thisArticle.viewNum}}</view>
							</view>
						</view>
					</view>
					<view class="picArea" v-if="thisArticle.imgList.length">
						<image :src="serverUrl + thisArticle.imgList[0].imagePath"></image>
					</view>	
				</view>	
			</view>

</view>


</template>

<script>
	export default {
		props: {
			thisArticle:{}
		},
		data() {
			return {
				serverUrl: this.$serverUrl,
				
			};
		},
}
</script>

<style>
	.oneArticle {
		width: 100%;
		position:relative;
}
	.title {
		font-size: 15px;
		color: ##4A4A4A;
		margin-top: 10px;
		padding-top: 10px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.cardBody {
		position: relative;
		width: 100%;
		/* height:83px; */
		padding-top:10px;
	}

	.picArea {
		position: absolute;
		right: 0;
		top: 10px;;
		width: 61px;
		height: 61px;
		background:black;
	}

	.left-body {
		width: 100%;
		height: 70px;
	}

	.leftBodyWithPic {
		width: calc(100% - 73px);
	}

	.content {
		height: 39px;
		width: 100%;
		font-size: 12px;
		line-height: 13px;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.bottomBar {
		width: 100%;
		height: 15px;
		margin-bottom:15px;
		position: absolute;
		top:85px;
		right:5px;
		color:#9B9B9B;
	}

	.time {
		width: 77px;
		/* 暂时,到  月-日 时:分*/
		height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 5px;
		bottom: 12px;
	}

	.comment {
		position: absolute;
		bottom: 12px; /* bottom, 对于comment time like view ,数值应等于menu-area的padding-bottom: ; */
		right: 40px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.like {
		position: absolute;
		bottom: 12px;
		right: 1px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}
    .view {
    	position: absolute;
    	bottom: 12px;
    	right: 79px;
    	width: 25px;
    	height: 11px;
    }
	.comment image,.like image, .view image {
		width: 11px;
		height: 11px;
		right:14px;
		position: absolute;
	
	}

	.like view,
	.comment view,.view view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}

</style>
