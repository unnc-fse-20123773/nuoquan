<template>
	<view class="comment" id="'+commentDetail.id'">
		<view class="fengexian"></view>
		<view class="contentarea">{{ commentDetail.comment }}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="commentDetail.faceImage" class="touxiang"></image>
				<text class="name">{{ commentDetail.nickname }}</text>
				<text class="time">{{ commentDetail.timeAgo }}</text>
			</view>
			<view class="icons">
				<!-- 评论按钮 -->
				<image v-show="!RECOMMENT" class="icon" src="../../../../static/icon/comment.png" style="padding-right: 23px;"
				 @click="testRE"></image>

				<image v-if="RECOMMENT" style="height:23px;width:50px;position:relative;bottom:-5px;padding-right:4px;" src="../../../../static/icon/ReComment.png"
				 @click="testRE"></image>

				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.png"></image>
				<text class="icom">{{ commentDetail.likeNum }}</text>
			</view>
		</view>

		<view v-show="RECOMMENT" class="reCommentsArea">

			<view class="contentarea">{{ commentDetail.comment }}</view>
			<view class="bottombar">
				<view style="width:70%;display:inline-block;">
					<image :src="commentDetail.faceImage" class="touxiang"></image>
					<text class="name">{{ commentDetail.nickname }}</text>
					<text class="time">{{ commentDetail.timeAgo }}</text>
				</view>
				<view class="icons">
					<!-- 点赞按钮 -->
					<image class="icon" src="../../../static/icon/like.png"></image>
					<text class="icom">{{ commentDetail.likeNum }}</text>
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		name: 'comment',
		props: {
			commentDetail: {}
		},
		data() {
			return {
				RECOMMENT: false,
			};
		},
		methods: {
			testRE() {
				this.RECOMMENT = !this.RECOMMENT
			}
		}
	};
</script>

<style scoped>
	.comment {
		/* 	background-color:gray;
 */
		border-radius: 20px;
		width: 100%;
		margin: auto;
	}

	.fengexian {
		height: 1px;
		width: 100%;
		background-color: #d6d6d6;
	}

	.contentarea {
		padding-top: 14px;
		background: white;
		font-size: 16px;
	}

	.bottombar {
		position: relative;
		border-radius: 20px;
		height: 25px;
		margin-top: 10px;
		display: flex;
		justify-content: space-between;
		padding-bottom: 5px;

	}

	.touxiang {
		width: 20px;
		height: 20px;
		border-radius: 20px;
		display: inline-block;
		vertical-align: middle;
		margin-right: 5px;
	}

	.time,
	.name {
		font-size: 10px;
		margin-right: 10px;
		color: #888888;
	}

	.icons {
		justify-content: flex-end;
		display: inline-flex;
		align-items: flex-end;
		width: 30%;
		font-size: 10px;

	}

	.icon {
		width: 12px;
		height: 12px;
		font-size: 2px;
		padding-right: 4px;
	}

	.icom {
		padding-right: 17px;
		align-items: flex-end;
	}

	.reCommentsArea {
		background: #EEEEEE;
		width: 400upx;
		padding: 14px 25upx 11px;
		border-radius: 10px;
		margin-left: 187.5upx;
		margin-bottom: 10px;
	}
</style>
