<template>
	<view class="comment" :id="commentDetail.id">
		<view class="fengexian"></view>
		<view class="contentarea" @tap="controlInputInComment('inComment')">{{ commentDetail.comment }}</view>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="commentDetail.faceImage" class="touxiang"></image>
				<text class="name">{{ commentDetail.nickname }}</text>
				<text class="time">{{ commentDetail.timeAgo }}</text>
			</view>
			<view class="icons">
				<!-- 评论按钮 -->
				<image v-show="!RECOMMENT" class="icon" src="../../../../static/icon/comment.png" style="padding-right: 23px;"
				 @click="showRecommentArea"></image>

				<image v-if="RECOMMENT" style="height:23px;width:50px;position:relative;bottom:-5px;padding-right:4px;" src="../../../../static/icon/ReComment.png"
				 @click="showRecommentArea"></image>

				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.png"></image>
				<text class="icom">{{ commentDetail.likeNum }}</text>
			</view>
		</view>


		<view v-show="RECOMMENT" class="reCommentsArea">

			<reComment v-for="(i,index) in reCommentList" v-bind:key="index" :reCommentDetail='i' @controlInputSignal="controlInputSignal"></reComment>
			<view class="submitComment">发 表 评 论</view>
		</view>
	</view>
</template>

<script>
	import reComment from './reComment'
	export default {
		name: 'comment',
		props: {
			commentDetail: {}
		},
		components: {
			reComment,
		},
		data() {
			return {
				RECOMMENT: false,
				reCommentList: [{
					faceImage: "https://wx.qlogo.cn/mmopen/vi_32/956oQqnmpuCiaF2ia1LWsPdpj2ZBqGOXgw2ymtQlxEfKDfoHxH1icCfZibtia28ibQqYXbpgZ10wSJvicV2fficctezcJQ/132",
					comment: "123321",
					nickname: "回复的妖",
					timeAge: "12323154214",
					likeNum: "3",
				}, {
					faceImage: "https://wx.qlogo.cn/mmopen/vi_32/956oQqnmpuCiaF2ia1LWsPdpj2ZBqGOXgw2ymtQlxEfKDfoHxH1icCfZibtia28ibQqYXbpgZ10wSJvicV2fficctezcJQ/132",

					comment: "123321",
					nickname: "回复的妖",
					timeAge: "12323154214",
					likeNum: "3",
				}, {
					faceImage: "https://wx.qlogo.cn/mmopen/vi_32/956oQqnmpuCiaF2ia1LWsPdpj2ZBqGOXgw2ymtQlxEfKDfoHxH1icCfZibtia28ibQqYXbpgZ10wSJvicV2fficctezcJQ/132",

					comment: "123321",
					nickname: "回复的妖",
					timeAge: "12323154214",
					likeNum: "3",
				}, {
					faceImage: "https://wx.qlogo.cn/mmopen/vi_32/956oQqnmpuCiaF2ia1LWsPdpj2ZBqGOXgw2ymtQlxEfKDfoHxH1icCfZibtia28ibQqYXbpgZ10wSJvicV2fficctezcJQ/132",

					comment: "123321",
					nickname: "回复的妖",
					timeAge: "12323154214",
					likeNum: "3",
				}, {
					faceImage: "https://wx.qlogo.cn/mmopen/vi_32/956oQqnmpuCiaF2ia1LWsPdpj2ZBqGOXgw2ymtQlxEfKDfoHxH1icCfZibtia28ibQqYXbpgZ10wSJvicV2fficctezcJQ/132",

					comment: "123321",
					nickname: "回复的妖",
					timeAge: "12323154214",
					likeNum: "3",
				}],

			};
		},
		mounted() {
              this.getComments()
		},
		methods: {
			getComments() {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getSonArticleComments',
					data: {
						fatherCommentId: that.commentDetail.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
						that.commentList = res.data.data.rows;
						console.log(that.articleCard.id);
						debugger

					},
				});

			},
			showRecommentArea() {
				this.RECOMMENT = !this.RECOMMENT
				if (this.RECOMMENT) {
					// this.getComments()
					this.controlInputInComment('inComment');
				} else {
					this.controlInputInComment(0);
				}
			},

			controlInputInComment(a) {
				if (a == "inComment") {
					var dataOfRecomment = {
						mode: "re-co",
						toUserId: this.commentDetail.fromUserId,
						fatherCommentId: this.commentDetail.id,
						nickname: this.commentDetail.nickname,
					}
				} else {
					var dataOfRecomment = a;
				}
				console.log("receive control input request, in comment");
				console.log(dataOfRecomment);
				this.$emit('controlInputSignal', dataOfRecomment)
			},
		},

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

	.submitComment {
		background: #FFCC30;
		border-radius: 5px;
		width: 120px;
		height: 30px;
		font-size: 10px;
		font-weight: bold;
		color: #FFFFFF;
		margin: auto;
		text-align: center;
		line-height: 30px;
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
