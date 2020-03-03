<template>
	<view style="width: calc(100% - 24px);height: 100%;margin: auto;">


		<view style="position: relative;margin-bottom: 20px;margin-top: 25px;">
			<textarea class="content" v-model="commentContent" maxlength="140" :auto-height="true" :show-confirm-bar="false" @click="controlInput(1)"></textarea>
			<!-- 			<view style="position: absolute;bottom: 8px;right:8px;font-size: 11px;color:#888888;">{{140 - commentContent.length}}</view>-->
			<view style="width:28px;height:14px;line-height:14px;font-size:14px;color:rgba(252,192,65,1);position: absolute;top:11px;right:12px;">结束</view>
			<image src="../static/icon/emoji.png" style="position: absolute;left:12px;top:8px;width:20px;height:20px;"></image>
		</view>

		<nqSwitch style="margin-top: 30px;" :options="['时间','热度']" @onChange="change_comment_order"></nqSwitch>
		<view style="height:50px;width: 100%;"></view><!-- 占位块 -->

		<!--循环评论卡片  来源 @pages/detail/oneComment.css -->
		<view class="comments">
			<block v-for="voteComment in voteCommentList" :key="voteComment.id">
				<view class="comment">
					<view class="comment-info">
						<image :src="pathFilter(voteComment.faceImg)" @tap="goToPersonPublic(voteComment.fromUserId)"></image>
						<view class="name_text">{{ voteComment.nickname }}</view>
						<view class="time_text">{{ timeDeal( voteComment.createDate)}}</view>
					</view>
					<view class="comment-content" @tap="goToCommentDetail(voteComment)">{{ voteComment.comment }}</view>
					<!-- 					<reComment :voteCommentid="voteComment.id" :voteComment="voteComment" style='width: 100%;height:100%;'></reComment> -->
					<view class="comment-menu">
						<view class="son-comment-num" @tap="goToCommentDetail(voteComment)">{{voteComment.commentNum}}</view>
						<view class="like-num" :class="{liked:voteComment.isLike}" @tap="swLikevoteComment(voteComment)">{{ voteComment.likeNum }}</view>
					</view>
				</view>
			</block>
		</view>
		<!--循环评论卡片  来源 @pages/detail/oneComment.css END -->

		<!--触底提示和功能  from detail start-->
		<view class="comment-bottom">
			<view class="comment-bottom-notice">划到底部啦</view>
			<view class="comment-bottom-buttons">
				<image class="to-top" @tap="scrollTo('top')" src="../static/icon/arrow-left-fcc041.png"></image>
				<view class="active-input-button" @tap="saveComment()" @click="scrollTo('input')">发表评论</view>
			</view>
		</view>
		<!--触底提示和功能  END-->

	</view>
</template>

<script>
	import nqSwitch from "@/components/nq-switch.vue";

	var uploadFlag = false;
	export default {
		props: {
			voteId: "",
			index: '',
			userId: "",

		},
		data() {
			return {
				userInfo: {},
				commentContent: "",
				voteCommentList: {},
				submitData: {
					//这个是从子组件传来的数据，回复评论的评论之类
				},

				totalPage: 1,
				currentPage: 1,

				serverUrl: this.$serverUrl
			};
		},
		components: {
			nqSwitch,
		},
		created() {
			this.userInfo = this.getGlobalUserInfo();
		},
		async onload() {
			var page = this.currentPage;
			this.getComments(page);
		},
		onReachBottom() {
			this.loadMore();
		},
		methods: {
			change_comment_order() {
				// 				nqSwitch的返回函数,用法同detail_2_comments.vue  :options是否需要请咨询 MQ，  ------yao
				// this.$emit("onChange", {
				// 	type: e.status
				// });
			},
			goToCommentDetail(voteComment) {
				//是否需要跳转评论主页？
			},
			scrollTo(a) {
				if (a == 'top') {
					console.log('scrolltotop');
					uni.pageScrollTo({
						scrollTop: 0,
						duration: 300
					});
				} else if (a == 'input') {

				}

			},
			saveComment: function() {
				// console.log("trigger saveComment");
				if (uploadFlag) {
					console.log("正在上传...")
					return;
				}
				uploadFlag = true;
				uni.showLoading({
					title: "正在上传..."
				})

				this.submitData.comment = this.commentContent;
				this.submitData.fromUserId = this.userInfo.id;
				this.submitData.voteId = this.voteId;
				console.log(this.submitData);
				console.log(this.index);
				var that = this;
				if (this.commentContent == "") {
					uni.showToast({
						title: '好像忘写评论了哦~',
						duration: 1000,
						icon: 'none',
					});
				} else {
					uni.request({
						url: that.$serverUrl + '/vote/saveVoteComment',
						method: 'POST',
						data: this.submitData,
						success(res) {
							console.log(res);
							if (res.data.status == 200) {
								uni.hideLoading();
								uploadFlag = false;

								that.getComments(1);

								that.commentContent = "";
								// 父组件中的comment_num += 1
								that.$emit('changeCommentNum', that.index);
							} else if (res.data.status == 500) {
								that.contentIllegal();
							}
						},
					})
				}
			},

			contentIllegal() {
				// 内容非法 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '内容涉嫌违规，请联系管理员。',
					duration: 2000,
					icon: 'none',
				})
			},

			getComments: function(page) {
				var that = this;
				uni.showLoading({
					title: "加载中..."
				})
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/vote/getMainVoteComments',
					data: {
						voteId: that.voteId,
						userId: that.userInfo.id,
						page: page,
						// type后端还未更改,暂时按时间倒序排列
						// type: that.type
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							if (page == 1) {
								that.voteCommentList = [];
							}
							console.log(res);
							var newCommentList = res.data.data.rows;
							var oldCommentList = that.voteCommentList;
							that.voteCommentList = oldCommentList.concat(newCommentList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
						} else {
							console.log(res);
						}
						uni.hideLoading();

					}
				})
			},

			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				var totalPage = that.totalPage;
				// 判断当前页是否等于总页数
				if (that.voteCommentList.length < 10) {
					return;
				} else if (currentPage == totalPage) {
					uni.showToast({
						title: "没有更多评论了",
						icon: "none",
						duration: 1000
					})
				} else {
					var page = currentPage + 1;
					that.getComments(page);
				}
			},
			
			controlInput(a){
				if(a!=0&&a!=1){
					this.placeholderText='回复 @'+a.nickname+' 的评论';
					delete(a.nickname);				
					this.submitData=a;
					// this.writingComment = true;
					// this.showInput= true;
				}else if(a==1){ //a==1 当前页面调用，直接评论投票
					this.submitData.toUserId=this.userId;
					// this.showInput = true;
					// this.writingComment = true; 
				}else{ //a==0, 关闭输入框，一切恢复默认状态
				    console.log('this is control input in detail. a ==0, EXIT');
					this.submitData = {};
					this.placeholderText="评论点什么吧......";
					// this.showInput = false;
					// this.writingComment =false;
				}
			},
			
		},
	}
</script>


<style>
	/* 	评论输入框
 */
	.content {
		min-height: 51px;
		max-height: 300px;
		margin-top: 13px;
		width: calc(100% - 28px);
		overflow: scroll;
		padding: 36px 12px 12px;

		border: 2px solid rgba(252, 192, 65, 1);
		border-radius: 8px;
		font-size: 14px;
	}
</style>

<style>
	/* 滑到底了等提示 */
	.comment-bottom {
		height: 160px;
		width: calc(164px + 40upx);
		margin: auto;
	}

	.comment-bottom-notice {
		width: 71px;
		height: 14px;
		line-height: 14px;
		font-size: 14px;
		color: #B2B2B2;
		margin: 37px auto 27px;
	}

	.comment-bottom-buttons {
		display: flex;
		justify-content: space-between;

	}

	.comment-bottom-buttons .back {
		width: 16px;
		height: 16px;
		padding: 14px;
		background: #FFF1D5;
		border-radius: 22px;
	}

	.comment-bottom-buttons .to-top {
		width: 18px;
		height: 12px;
		background: #FFF1D5;
		border-radius: 22px;
		padding: 16px 11px 16px 15px;
		position: relative;
		transform: rotate(90deg);
	}

	.comment-bottom-buttons .to-top::after {
		content: "";
		position: absolute;
		top: 14px;
		left: 12px;
		width: 2px;
		height: 16px;
		background: #FCC041;
		border-radius: 2px;
	}

	.active-input-button {
		color: #FFFFFF;
		width: 76px;
		height: 17px;
		font-size: 17px;
		line-height: 17px;
		color: rgba(255, 255, 255, 1);
		padding: 10px 22px;
		border-radius: 10px;
		box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.16);
		background: #FCC041;
		letter-spacing: 2px;
	}
</style>
<style>
	/* oneComment.css 可以原样import  page/detail/oneComment.css, 路径写不对,我放弃了,COPY了------yao */
	.comment-info {
		height: 24px;
		width: 100%;
		position: relative;
		margin-top: 8px;
	}

	.comment-info image {
		display: inline-block;
		width: 24px;
		height: 24px;
		border-radius: 12px;
		vertical-align: middle;
	}

	.name_text {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 24px;
		height: 24px;
		color: #9B9B9B;
		display: inline-block;
		/* position: absolute;
			top: 0;
			left: 32px; */
		max-width: 96px;
		margin-left: 8px;
	}

	.time_text {
		position: absolute;
		right: 0;
		top: 0;
		text-align: right;
		font-size: 12px;
		color: #9B9B9B;
		line-height: 24px;
		width: 102px;
	}

	.comment-content {
		padding-top: 12px;
		padding-bottom: 8px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 18px;
		color: rgba(53, 53, 53, 1);
	}

	.comment-menu {
		height: 20px;
		position: relative;
		padding-bottom: 9px;
		border-bottom: 1px solid rgba(236, 236, 236, 1);
	}

	.son-comment-num {
		width: 14px;
		height: 14px;
		font-size: 12px;

		line-height: 14px;
		color: rgba(136, 136, 136, 1);

		display: inline-block;
		position: absolute;
		right: 60px;
		top: 0;
		padding: 4px 8px 2px 30px;
		text-align: center;
		border-radius: 50px;
	}

	.like-num {
		width: 14px;
		height: 14px;
		font-size: 12px;
		line-height: 14px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		right: 0;
		top: 0;
		padding: 4px 8px 2px 30px;
		border-radius: 50px;
		text-align: center;
	}

	.son-comment-num::after {
		position: absolute;
		width: 14px;
		height: 14px;
		left: 9px;
		top: 4px;
		content: "";
		background: url('~@/static/icon/comment-alt-888888.png');
		background-size: 14px 14px;
		background-repeat: no-repeat;
	}

	.like-num::after {
		position: absolute;
		width: 14px;
		height: 14px;
		left: 9px;
		top: 4px;
		content: "";
		background: url(~@/static/icon/thumbs-up-888888.png);
		background-size: 14px 14px;
		background-repeat: no-repeat;
	}

	.liked {
		background: linear-gradient(130deg, rgba(254, 110, 110, 0.84) 0%, rgba(245, 60, 60, 1) 100%);
	}
</style>
