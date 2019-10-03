<template>
	<view>
		<!-- 主评论区域 -->
		<view class="commentBox">
			<view class="cmtdetail-IDline">
				<view class="cmtdetail-IDrel">
					<view class="cmtdetail-profilePic">
						<image :src="mainComment.faceImg" mode="aspectFill" class="profilePic" @tap="goToPersonPublic"></image>
					</view>
					<view class="cmtdetail-middle">
						<view class="cmtdetail-IDtext">
							{{mainComment.nickname}}
						</view>
						<view class="cmtdetail-time">
							{{mainComment.timeAgo}}
						</view>
					</view>
					<view class="cmtdetail-right">
						<view class="cmtdetail-rightrel">
							<view @tap="swLikeComment(mainComment)">
								<image v-if="!mainComment.isLike" src="../../static/icon/like.png" mode="aspectFill" class="likeIcon"></image>
								<image v-if="mainComment.isLike" src="../../static/icon/liked.png" mode="aspectFill" class="likeIcon"></image>
								<!-- 此处点赞数量最长5位数，如超出样式出错 -->
								<text class="likeNum" :class="{'liked':mainComment.isLike}">{{mainComment.likeNum}}</text>
							</view>
							<image src="../../static/icon/message.png" mode="aspectFill" class="commentIcon"></image>
						</view>
					</view>
				</view>
			</view>
			<view class="cmtdetail-contentBox" id="contentBox" @click="controlInput(1)">
				{{mainComment.comment}}
			</view>
			<view class="cmtdetail-loadmore column_center">
				<view class="loadmore-text">
					查看全部
				</view>
			</view>
			<view style="height: 2px;width: 74%;margin-left: 62px;background-color: #E4E4E4;margin-top: 10px;"></view>
		</view>
		<!-- 子评论区域 -->
		<view style="width: 100%;">
			<!--移到了sonCommentBox组件，考虑评论之间的点赞方程容易混淆，做了组件，就互不影响了-->
			<sonCommentBox v-for="i in commentList" :key="i.id" :reCommentDetail="i" @controlInputSignal="controlInput"
			@swLikeComment="swLikeComment" @goToPersonPublic="goToPersonPublic"></sonCommentBox>
			<!-- 占位块 -->
			<view style="width: 100%; height: 40px;"></view> 
		</view>
		
		<view class="bottomLayerOfSubmit">
			<view class="submitComment" @click="controlInput(1)">发 表 评 论</view>
		</view>
		<!-- 输入框 -->
		<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)">
			<view class="commentPart" @click.stop="" :style="{bottom: textAreaAdjust }">
				<view class="emoji"></view>
				<view class="submit" @click="saveComment()"></view>
				<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
				 adjust-position="false" v-model="commentContent" @click.stop="" :show-confirm-bar="false"/>
			</view>
		</view>
	</view>
</template>

<script>
	import sonCommentBox from './sonCommentBox.vue'
	export default {
		components: {
			sonCommentBox,
		},
		
		data() {
			// 我抄了一小部分代码过来，还没改 -Guetta
			// O(∩_∩)O
			// 😄
			// 🤭
			// (●'◡'●)
			// 页面有点丑（高仿微博），回头让仅仅优化一下
			return {
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
			this.mainComment = JSON.parse(options.data);
			// console.log(this.mainComment);
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
							
							// that.commentList = "";
							// that.$nextTick(function(){
							// 	that.commentList =  res.data.data.rows;	
							// });
							// console.log(that.commentList);
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
							that.$emit('flashSubComment',that.mainComment.id);
							debugger;
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
		}
	}
</script>

<style>
	page {
		width: 100%;
		background-color: #F3F3F3;
	}

	.commentBox {
		width: 100%;
		min-height: 200upx;
		background-color: white;
	}

	.son-commentBox {
		width: 100%;
		min-height: 200upx;
	}

	/* ID 行 */
	.cmtdetail-IDline {
		display: flex;
		height: 50px;
		width: 100%;
	}

	.cmtdetail-IDrel {
		height: 100%;
		width: 100%;
		position: relative;
	}

	.cmtdetail-profilePic {
		position: absolute;
		top: 8px;
		left: 10px;
		width: 42px;
		height: 42px;
		border: 1px solid red;
		border-radius: 200px;
	}

	.son-cmtdetail-profilePic {
		position: absolute;
		top: 8px;
		left: 16px;
		width: 36px;
		height: 36px;
		border: 1px solid red;
		border-radius: 200px;
	}

	.profilePic {
		width: 42px;
		height: 42px;
		border-radius: 200px;
	}

	.son-profilePic {
		width: 36px;
		height: 36px;
		border-radius: 200px;
	}

	.cmtdetail-middle {
		position: absolute;
		top: 10px;
		left: 62px;
		display: flex;
		flex-direction: column;
		width: 60%;
		height: 42px;
	}

	.cmtdetail-IDtext {
		width: 100%;
		height: 50%;
		color: #3d3d3d;
		font-size: 15px;
	}

	.cmtdetail-time {
		width: 100%;
		height: 50%;
		color: #888888;
		font-size: 12px;
	}

	.cmtdetail-right {
		position: absolute;
		right: 10px;
		display: flex;
		min-width: 24%;
		height: 100%;
	}

	.cmtdetail-rightrel {
		position: relative;
		width: 100%;
		height: 100%;
	}

	.likeIcon {
		position: absolute;
		left: 5%;
		width: 18px;
		height: 18px;
		top: 8px;
	}

	.likeNum {
		position: absolute;
		top: 10px;
		left: 26%;
		font-size: 12px;
		color: #888888;
	}
	.liked{
		color: #FDD041;
	}
	.son-likeIcon{
		position: absolute;
		right: 40%;
		width: 18px;
		height: 18px;
		top: 8px;
	}
	
	.son-likeNum{
		position: absolute;
		top: 10px;
		right: 9%;
		font-size: 12px;
		color: #888888;
	}
	
	.commentIcon {
		position: absolute;
		right: 5%;
		width: 18px;
		height: 18px;
		top: 8px;
	}


	.cmtdetail-contentBox {
		width: 74%;
		margin-left: 62px;
		margin-top: 10px;
		min-height: 20px;
		color: #3D3D3D;
		font-size: 15px;
		font-weight: 500;
	}

	.cmtdetail-loadmore {
		margin-left: 62px;
		height: 30px;
		width: 80%;
	}

	.loadmore-text {
		color: #888888;
		font-size: 12px;
	}
	
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
