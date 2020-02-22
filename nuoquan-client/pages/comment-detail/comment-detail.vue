<template>
	<view class="comment-detail-page">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}" left-icon="back" left-text="返回"
		:title="pageTitle" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<!-- 主评论区域 -->

		<view class="comment-Box">
			<view class="comment-info">
				<image :src="mainComment.faceImg" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
				<view class="name_text">{{ mainComment.nickname }}</view>
				<view class="time_text">{{ mainComment.timeAgo }}</view>
			</view>
			<view class="comment-content" @tap="controlInput(mainComment)">{{ mainComment.comment }}</view>
			<view class="comment-menu">
				<view class="son-comment-num" @tap="controlInput(mainComment)">{{mainComment.commentNum}}</view>
				<view class="like-num" :class="{liked:mainComment.isLike}" @tap="swLikeComment(mainComment)">{{ mainComment.likeNum }}</view>
			</view>
		</view>


		<!-- 子评论区域 -->
		<view style="width: 100%;">
			<!--移到了sonCommentBox组件，考虑评论之间的点赞方程容易混淆，做了组件，就互不影响了-->
			<sonCommentBox v-for="i in commentList" :key="i.id" :reCommentDetail="i" @controlInputSignal="controlInput"
			 @swLikeComment="swLikeComment" @goToPersonPublic="goToPersonPublic"></sonCommentBox>
			<!-- 占位块 -->
			<view style="width: 100%; height: 40px;"></view>
		</view>





		<!--触底提示和功能  start   COPY FROM DETAIL-->
		<view class="comment-bottom">
			<view class="comment-bottom-notice">划到底部啦</view>
			<view class="comment-bottom-buttons">
				<image class="back" @tap="backToLastPage" src="../../static/icon/arrow-left-fcc041.png" mode="aspectFit"></image>
				<image class="to-top" @tap="scrollToTop" src="../../static/icon/arrow-left-fcc041.png"></image>
				<view class="active-input-button" @click="controlInput(1)">发表评论</view>
			</view>
		</view>
		<!--触底提示和功能  END-->

		<!-- 旧输入框
			<view class="bottomLayerOfSubmit">
				<view class="submitComment" @click="controlInput(1)">发 表 评 论</view>
			</view>
-->





		<!-- 输入框 -->
		<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)">
			<view class="commentPart" @click.stop="" :style="{bottom: textAreaAdjust }">
				<view class="emoji"></view>
				<view class="submit" @click="saveComment()"></view>
				<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
				 adjust-position="false" v-model="commentContent" @click.stop="" :show-confirm-bar="false" cursor-spacing='20' />
				</view>
		</view>
		<!-- 输入框  End-->
	</view>
</template>

<script>
	import sonCommentBox from './sonCommentBox.vue'
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"
	
	export default {
		components: {
			sonCommentBox,
			uniNavBar
		},
		
		data() {
			return {
				pageTitle: '评论详情',
				mainComment:'',    //用于接受跳转传过来的underCommentId,然后申请获取sonComment  yaoyao 9.16 
				userInfo: '',
				commentContent: '',  //用户准备提交的评论内容
				commentList: '',  //返回值，获取评论列表信息,循环展示的东西，sonComment
				showInput:false,  //控制输入框，true时显示输入框
				writingComment:false,  //控制输入框，true时自动获取焦点，拉起输入法
				submitData:{},
				placeholderText: '评论点什么吧......',
				
				totalPage: 1,
				currentPage: 1,
			}
		},

		onLoad: function(options) {
			uni.setNavigationBarTitle({
				title: "回复"
			});
			var userInfo = this.getGlobalUserInfo()
			this.userInfo = userInfo;
			// 接收传过来的主角评论ID
			this.mainComment = JSON.parse(decodeURIComponent(options.data));
			console.log(this.mainComment);
			// 获取次评论
			this.getSubComments(1);
		},
		
		onReachBottom() {
			this.loadMore();
		},

		methods: {
			getSubComments(page) {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getSubComments',
					data: {
						underCommentId: that.mainComment.id,
						userId: that.userInfo.id,
						page: page
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							if (page == 1) {
								that.commentList = [];
							}
							var newCommentList = res.data.data.rows;
							var oldCommentList = that.commentList;
							that.commentList = oldCommentList.concat(newCommentList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;

						}
					}
				});
			},
			
			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// that.showArticles(1);
					uni.showToast({
						title: "没有更多评论了",
						icon: "none",
						duration: 1000
					})
				} else {
					var page = currentPage + 1;
					that.getSubComments(page);
				}
			},
			
			controlInput(a){
				if(a!=0&&a!=1){ //a!=0, !=1， 从子组件传来，包含被回复对象：被回复人ID，被回复评论ID，被回复人昵称
					this.placeholderText='回复 @'+a.nickname+' 的评论';
					delete(a.nickname);
					this.submitData=a;
					this.showInput= true;
					this.writingComment = true ;
					console.log(this.writingComment);
				}else if(a==1){ //a==1 当前页面调用，回复主评论
					this.submitData.toUserId=this.mainComment.fromUserId;
					this.submitData.fatherCommentId=this.mainComment.id;
					this.submitData.underCommentId=this.mainComment.id;
					
					this.showInput = true;
					this.writingComment = true; 
					console.log('this is control input in detail. a ==' + a);
					console.log(this.submitData);
				}else{ //a==0, 关闭输入框，一切恢复默认状态
				    console.log('this is control input in detail. a ==0, EXIT');
					this.submitData = {};
					this.placeholderText="评论";
					this.showInput = false;
					this.writingComment =false;
				}
			},
			
			/**
			 * fromUserId 必填
			 * toUserId 必填
			 * articleId 必填 // 为了计算文章总评论数
			 * underCommentId // 显示在该主评论层ID下
			 * fatherCommentId // 父级评论ID
			 * comment 必填
			 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId, underCommentId;
			 *     子级评论有 fatherCommentId, underCommentId;
			 */
			saveComment: function() {
				this.submitData.comment=this.commentContent;
				this.submitData.fromUserId=this.userInfo.id;
				this.submitData.articleId=this.mainComment.articleId;
				// console.log(this.submitData);
				var that = this;
				if(this.commentContent==""){
					uni.showToast({
						title: '好像忘写评论了哦~',
						duration: 1000,
						icon: 'none',
					});
				}else{
					uni.request({
						url: that.$serverUrl + '/article/saveComment',
						method: 'POST',
						data: this.submitData,
						success: (res) => {
							that.writingComment = false;
							that.commentContent = "";
							that.showInput = false;
							
							// 强制子组件重新刷新
							that.commentList = '';
							that.$nextTick(function() {
								that.getSubComments(1);
							});
							uni.$emit('flashSubComment',this.mainComment.id);
							uni.$emit('updateArticle',this.mainComment.articleId);
						},
					})
				}
			},
			
			swLikeComment(comment) {
				if (comment.isLike) {
					this.unLikeComment(comment);
					comment.likeNum--;
				} else {
					this.likeComment(comment);
					comment.likeNum++;
				}
				comment.isLike = !comment.isLike;
			},
			
			likeComment(comment) {
				console.log("点赞评论");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userLikeComment',
					data: {
						userId: that.userInfo.id,
						commentId: comment.id,
						createrId: comment.fromUserId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
					},
				});
			},
			
			unLikeComment(comment) {
				console.log("取消点赞评论");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userUnLikeComment',
					data: {
						userId: that.userInfo.id,
						commentId: comment.id,
						createrId: comment.fromUserId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res);
					},
				});
			},
			
			goToPersonPublic(){
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + this.mainComment.fromUserId,
				})
			},
			backToLastPage(){
				uni.navigateBack({
				})
			},//COPY FROM DETAIL -- YAO
			scrollToTop(){
				uni.pageScrollTo({
				    scrollTop: 0,
				    duration: 300
				});
			},//COPY FROM DETAIL -- YAO
		}//method --妖
	}
</script>

<style scoped>
	 @import url("../detail/oneComment.css");
	page {
		width: 100%;
		background-color: #F3F3F3;
	}
.comment-detail-page{
	padding: 0 15px;
	width: calc(100% - 30px);
	margin-top:15px;
}

.son-comment-num{
	background:linear-gradient(313deg,rgba(255,184,32,0.84) 0%,rgba(240,240,122,1) 100%);
}

/* 滑到底了等提示
 */.comment-bottom{
		height:160px;
		width:calc(202px + 80upx);
		margin: auto;
		}
		
	.comment-bottom-notice{
		width:71px;
		height:14px;
		line-height: 14px;
		font-size:14px;
		color:#B2B2B2;
		margin:37px auto 27px;
	}
	.comment-bottom-buttons{
		display: flex;
			justify-content: space-between;
		
	}
	.comment-bottom-buttons .back{
		width:16px;
		height:16px;
		padding:14px;
		background: #FFF1D5;
		border-radius: 22px;
	}
	.comment-bottom-buttons .to-top{
		width:18px;
		height:12px;
		background: #FFF1D5;
		border-radius: 22px;
		padding: 16px 11px 16px 15px;
		position: relative;
		transform: rotate(90deg);
	}
	.comment-bottom-buttons .to-top::after{
content: "";
position: absolute;
top: 14px;
left: 12px;
width: 2px;
height: 16px;
background: #FCC041;
border-radius: 2px;

	}
	.active-input-button{
		color:#FFFFFF;
		width:76px;
		height:17px;
		font-size:17px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:17px;
		color:rgba(255,255,255,1);
		padding:10px 22px;
		border-radius: 10px;
		box-shadow:  0px 0px 8px rgba(0,0,0,0.16);
		background: #FCC041;
		letter-spacing:2px;
	}
	/* 滑到底了等提示
	 */








/* 

	.cmtdetail-loadmore {
		margin-left: 62px;
		height: 30px;
		width: 80%;
	}

	.loadmore-text {
		color: #888888;
		font-size: 12px;
	} */
	
	.bottomLayerOfSubmit{
		display: flex;
		position: fixed;
		height: 48px;
		width:750upx;
		left:0;
		bottom: 0;
		background: #FFFFff;
		justify-content: center;
		align-items: center;
	}
	.submitComment {
		background: #FFCC30;
		border-radius: 5px;
		width: 32%;
		height: 30px;
		font-size: 10px;
		font-weight: bold;
		color: #FFFFFF;
		text-align: center;
		line-height: 30px;
	}
	.submitComment::before{
		content: "";
		position: absolute;
		top:-9px;
		left: -294%;
		width:750upx;
		height:48px;
		background: #F3FFFF;
		z-index: -1;
	}
	
	/* 以下五条为底部输入框样式 */
	.bottoLayerOfInput{
		position: fixed;
		width: 750upx;
		height: 1000px;
		top:0;
		left:0;
		z-index: 3;
	}
	.commentPart {
		box-shadow: 0px 1px 5px 0px rgba(139, 139, 139, 0.32);
		position:fixed;
		bottom: 0;
		left: 0;
		width: 670upx;
		padding:11px 40upx;
		min-height: 50px;
		background: #FFFFFF;
	}
	
	.emoji {
		background-repeat: no-repeat;
		background-position: center;
		border: none;
		width: 21px;
		height: 21px;
		background-size: 21px 21px;
		margin-bottom: 7px;
		display: inline-block;
	}
	.submit{
		display: inline-block;
		width: 21px;
		height:21px;
		background: url(../../static/icon/arrow-right.png);
		background-size: 14px 14px;
		background-repeat: no-repeat;
		background-position:center;
		float:right;
	}
	.commentSth {
		width: calc(670upx - 20px);
		border: solid 1px #FCC041;
		border-radius: 10px;
		line-height: 20px;
		font-size: 14px;
		padding:8px 10px;
	}
</style>
