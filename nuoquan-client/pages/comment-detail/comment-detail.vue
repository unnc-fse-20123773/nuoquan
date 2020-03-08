<template>
	<view class="comment-detail-page">
		<!-- 导航栏 -->
		<uni-nav-bar
			class="navigationBar"
			:style="{ height: this.getnavbarHeight() + 'px' }"
			:showLeftIcon="true"
			:isNavHome="isNavHome"
			:left-text="lang.back"
			:title="lang.commentDetail"
			:height="navbarHeight"
		></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }"></view>

		<!-- 主评论区域 -->

		<view class="comment-Box">
			<view class="comment-info">
				<image :src="pathFilter(mainComment.faceImg)" @tap="goToPersonPublic(mainComment.fromUserId)"></image>
				<view class="name_text">{{ mainComment.nickname }}</view>
				<view class="time_text">{{ timeDeal(mainComment.createDate) }}</view>
			</view>
			<view class="comment-content" @tap="controlInput(mainComment)">{{ mainComment.comment }}</view>
			<view class="comment-menu">
			<view class="operationBar column_center">
					<nqCmt @click.native="controlInput(mainComment)" :number="mainComment.commentNum"></nqCmt>
					<nqLike style="margin-left: 11px;" @click.native="swLikeComment(mainComment)" :status="mainComment.isLike" :number="mainComment.likeNum"></nqLike>
				</view>			</view>
		</view>

		<!-- 子评论区域 -->
		<view style="width: 100%;">
			<!--移到了sonCommentBox组件，考虑评论之间的点赞方程容易混淆，做了组件，就互不影响了-->
			<sonCommentBox
				v-for="i in commentList"
				:key="i.id"
				:reCommentDetail="i"
				@controlInputSignal="controlInput"
				@swLikeComment="swLikeComment"
				@goToPersonPublic="goToPersonPublic"
			></sonCommentBox>
			<!-- 占位块 -->
			<view style="width: 100%; height: 40px;"></view>
		</view>

		<!--触底提示和功能  start   COPY FROM DETAIL-->
		<!-- <view class="comment-bottom">
			<view class="comment-bottom-notice">{{ lang.onBottom }}</view>
			<view class="comment-bottom-buttons">
				<image class="back" @tap="backToLastPage" src="../../static/icon/arrow-left-fcc041.png" mode="aspectFit"></image>
				<image class="to-top" @tap="scrollToTop" src="../../static/icon/arrow-left-fcc041.png"></image>
				<view class="active-input-button" @click="controlInput(1)">{{ lang.writeComment }}</view>
			</view>
		</view> -->
		<!--触底提示和功能  END-->

		<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)">
			<view class="commentPart" :style="{ bottom: textAreaAdjust }">
				<!--<view class="emoji"></view><view class="add-pic"></view>-->
				<view class="submit" @tap="saveComment()">{{ lang.send }}</view>
				<view class="commentSth">
					<textarea
						class="comment-text"
						:placeholder="placeholderText"
						:focus="writingComment"
						auto-height="true"
						adjust-position="false"
						v-model="commentContent"
						:show-confirm-bar="false"
						@focus="popTextArea"
						@blur="unpopTextArea"
						cursor-spacing="20"
					/>
					<!-- <view class="comment-pic-area"><image src="../../static/BG/indexBG.png"></image><image src="../../static/icon/about.png"></image><image src="../../static/icon/1575235531(1).png"></image></view> -->
					<view class="word-count-left">{{ 140 - commentContent.length }}</view>
				</view>
			</view>
		</view>

		<!--常驻input-->
		<view class="permanent_input_BG" v-if="!showInput" @click="controlInput(1)">
			<input class="permanent_input" :placeholder="placeholderText" v-model="commentContent" disabled="true" min-height="10px" />
		</view>
	</view>
</template>

<script>
import sonCommentBox from './sonCommentBox.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import { mapState, mapMutations } from 'vuex';
import nqLike from '@/components/nq-button/nq-likeButton.vue';
import nqCmt from '@/components/nq-button/nq-cmtButton.vue';

export default {
	components: {
		sonCommentBox,
		uniNavBar,
		nqLike,
		nqCmt,
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			mainComment: '', //用于接受跳转传过来的underCommentId,然后申请获取sonComment  yaoyao 9.16
			type: '', //文章 or 投票
			userInfo: '',
			commentContent: '', //用户准备提交的评论内容
			commentList: '', //返回值，获取评论列表信息,循环展示的东西，sonComment
			showInput: false, //控制输入框，true时显示输入框
			writingComment: false, //控制输入框，true时自动获取焦点，拉起输入法
			// wordNotice: '48',
			submitData: {},
			placeholderText: '评论点什么吧......',
			textAreaAdjust: '',

			totalPage: 1,
			currentPage: 1,

			saveCommentFlag: false,
			isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮

			navbarHeight: 0 //一次性储存 navbarheight
		};
	},
	/**
	 * data:{
	 * 		mainCommentId:
	 * 		type: article / vote
	 * }
	 * @param {Object} options
	 */
	onLoad: function(options) {
		// 一次性储存 navbar 高度
		this.navbarHeight = this.getnavbarHeight().bottom + 5;

		uni.setNavigationBarTitle({
			title: '回复'
		});
		var userInfo = this.getGlobalUserInfo();
		this.userInfo = userInfo;
		// 接收传过来的主角评论ID
		var data = JSON.parse(decodeURIComponent(options.data));
		this.mainComment = data.mainComment;
		this.type = data.type;
		// 获取次评论
		this.getSubComments(1);
	},

	onReachBottom() {
		this.loadMore();
	},
	methods: {
		getSubComments(page){
			var type = this.type;
			//判断是文章评论还是投票评论
			if(type == 'article'){
				console.log("这是文章评论");
				this.getArticleSubComments(page);
			}else if(type == 'vote'){
				console.log("这是投票评论");
				this.getVoteSubComments(page);
			}
		},
		
		getArticleSubComments(page) {
			uni.request({
				method: 'POST',
				url: this.$serverUrl + '/article/getSubComments',
				data: {
					underCommentId: this.mainComment.id,
					userId: this.userInfo.id,
					page: page,
					type: 0
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if (res.data.status == 200) {
						if (page == 1) {
							this.commentList = [];
						}
						var newCommentList = res.data.data.rows;
						var oldCommentList = this.commentList;
						this.commentList = oldCommentList.concat(newCommentList);
						this.currentPage = page;
						this.totalPage = res.data.data.total;
					}
				}
			});
		},

		getVoteSubComments(page) {
			uni.request({
				method: "POST",
				url: this.$serverUrl + '/vote/getSubComments',
				data: {
					underCommentId: this.mainComment.id,
					userId: this.userInfo.id,
					page: page,
					type: 0,
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: (res) => {
					// console.log(res);
					if (res.data.status == 200) {
						if (page == 1) {
							this.commentList = [];
						}
						var newCommentList = res.data.data.rows;
						var oldCommentList = this.commentList;
						this.commentList = oldCommentList.concat(newCommentList);
						this.currentPage = page;
						this.totalPage = res.data.data.total;
					}
				}
			});
		},

		loadMore: function() {
			// 判断当前页数和总页数是否相等
			if (this.currentPage == this.totalPage) {
				// this.showArticles(1);
				uni.showToast({
					title: '没有更多评论了',
					icon: 'none',
					duration: 1000
				});
			} else {
				var page = this.currentPage + 1;
				this.getSubComments(page);
			}
		},
		
		controlInput(a) {
			if (a != 0 && a != 1) {
				//a!=0, !=1， 从子组件传来，包含被回复对象：被回复人ID，被回复评论ID，被回复人昵称
				this.placeholderText = '回复 @' + a.nickname + ' 的评论';
				// delete a.nickname;
				this.submitData = a;
				this.showInput = true;
				this.writingComment = true;
				console.log(this.writingComment);
			} else if (a == 1) {
				//a==1 当前页面调用，回复主评论
				this.submitData.toUserId = this.mainComment.fromUserId;
				this.submitData.fatherCommentId = this.mainComment.id;
				this.submitData.underCommentId = this.mainComment.id;

				this.showInput = true;
				this.writingComment = true;
				console.log('this is control input in detail. a ==' + a);
				console.log(this.submitData);
			} else {
				//a==0, 关闭输入框，一切恢复默认状态
				console.log('this is control input in detail. a ==0, EXIT');
				this.submitData = {};
				this.placeholderText = '评论';
				this.showInput = false;
				this.writingComment = false;
			}
		},
		popTextArea(e) {
			console.log('展开');
			console.log(e);
			console.log(e.detail.height);
			// this.textAreaAdjust = e.detail.height / 3 + 'px';
			// this.textAreaAdjust = '0' ;
		},
		unpopTextArea(e) {
			console.log('收起');
			console.log(e);

			// this.textAreaAdjust = '';
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
			if (this.saveCommentFlag) {
				console.log('正在上传...');
				return;
			}
			this.saveCommentFlag = true;
			uni.showLoading({
				title: '正在提交...',
				duration: 1000
			});
			console.log('tragger savecomment');
			if (this.commentContent == '') {
				uni.showToast({
					title: '好像忘写评论了哦~',
					duration: 1000,
					icon: 'none'
				});
			} else {
				var url = "";
				// var submitData;
				if(this.type == 'article'){
					url = "/article/saveComment"
					this.submitData.comment = this.commentContent;
					this.submitData.fromUserId = this.userInfo.id;
					this.submitData.articleId = this.mainComment.articleId;
				}else if(this.type == 'vote'){
					url = "/vote/saveVoteComment"
					this.submitData.comment = this.commentContent;
					this.submitData.fromUserId = this.userInfo.id;
					this.submitData.voteId = this.mainComment.voteId;
				}
				uni.request({
					url: this.$serverUrl + url,
					method: 'POST',
					data: this.submitData,
					success: res => {
						this.writingComment = false;
						this.commentContent = '';
						this.showInput = false;

						// 强制子组件重新刷新
						this.commentList = '';
						this.$nextTick(function() {
							this.getSubComments(1);
						});
						uni.$emit('flashSubComment', this.mainComment.id);
						// uni.$emit('updateArticle', this.mainComment.articleId);
					}
				});
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
			console.log('点赞评论');
			var url = "";
			if(this.type == 'article'){
				url = "/article/userLikeComment"
			}else if(this.type == 'vote'){
				url = "/vote/userLikeVoteComment"
			}
			uni.request({
				method: 'POST',
				url: this.$serverUrl + url,
				data: {
					userId: this.userInfo.id,
					commentId: comment.id,
					createrId: comment.fromUserId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},

		unLikeComment(comment) {
			console.log('取消点赞评论');
			var url = "";
			if(this.type == 'article'){
				url = "/article/userUnLikeComment"
			}else if(this.type == 'vote'){
				url = "/vote/userUnLikeVoteComment"
			}
			uni.request({
				method: 'POST',
				url: this.$serverUrl + url,
				data: {
					userId: this.userInfo.id,
					commentId: comment.id,
					createrId: comment.fromUserId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},

		goToPersonPublic() {
			uni.navigateTo({
				url: '/pages/personpublic/personpublic?userId=' + this.mainComment.fromUserId
			});
		},
		backToLastPage() {
			uni.navigateBack({});
		}, //COPY FROM DETAIL -- YAO
		scrollToTop() {
			uni.pageScrollTo({
				scrollTop: 0,
				duration: 300
			});
		} //COPY FROM DETAIL -- YAO
	} //method --妖
};
</script>

<style scoped>
@import url('../../components/nq-comment/oneComment.css');
page {
	width: 100%;
	background-color: #f3f3f3;
}
.comment-detail-page {
	padding: 0 15px;
	width: calc(100% - 30px);
	margin-top: 15px;
}
.son-comment-num {
	background: linear-gradient(313deg, rgba(255, 184, 32, 0.84) 0%, rgba(240, 240, 122, 1) 100%);
}
/* 滑到底了等提示
 */
.comment-bottom {
	height: 160px;
	width: calc(202px + 80upx);
	margin: auto;
}

.comment-bottom-notice {
	width: 71px;
	height: 14px;
	line-height: 14px;
	font-size: 14px;
	color: #b2b2b2;
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
	background: #fff1d5;
	border-radius: 22px;
}
.comment-bottom-buttons .to-top {
	width: 18px;
	height: 12px;
	background: #fff1d5;
	border-radius: 22px;
	padding: 16px 11px 16px 15px;
	position: relative;
	transform: rotate(90deg);
}
.comment-bottom-buttons .to-top::after {
	content: '';
	position: absolute;
	top: 14px;
	left: 12px;
	width: 2px;
	height: 16px;
	background: #fcc041;
	border-radius: 2px;
}
.active-input-button {
	color: #ffffff;
	width: 76px;
	height: 17px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 17px;
	color: rgba(255, 255, 255, 1);
	padding: 10px 22px;
	border-radius: 10px;
	box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.16);
	background: #fcc041;
	letter-spacing: 2px;
}
/* 滑到底了等提示
	 */
/* 以下五条为底部输入框样式 */
.bottoLayerOfInput {
	position: absolute;
	width: 750upx;
	height: 1000px;
	bottom: 0;
	left: 0;
	z-index: 40;
}

.commentPart {
	box-shadow: 0px 1px 5px 0px rgba(139, 139, 139, 0.32);
	position: absolute;
	bottom: 0;
	z-index: 900;
	left: 0;
	width: 702upx;
	padding: 10px 24upx 4px;
	min-height: 50px;
	background: white;
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
.add-pic {
	background: url(../../static/icon/image-888888.png);
	background-repeat: no-repeat;
	background-position: center;
	border: none;
	width: 21px;
	height: 21px;
	background-size: 20px 16px;
	margin-bottom: 7px;
	margin-left: 11px;
	display: inline-block;
}
.submit {
	display: inline-block;
	width: 42px;
	position: absolute;
	top: 15px;
	right: 15px;
	height: 21px;
	/* 	background: url(../../static/icon/arrow-right.png);
 */
	background-size: 14px 14px;
	background-repeat: no-repeat;
	background-position: center;
	float: right;
	font-size: 14px;
	color: #fcc041;
	z-index: 999;
}
.commentSth {
	border: solid 2px #fcc041;
	border-radius: 8px;
	line-height: 20px;
	padding: 12px 12px 0px;
	position: relative;
}
.comment-text {
	width: calc(670upx - 60px);
	font-size: 14px;
	max-height: 95px;
	line-height: 20px;
	max-height: 100px;
	padding-bottom: 14px;
}
.comment-pic-area {
	position: relative;
	height: 45px;
}
.comment-pic-area image {
	width: 35px;
	height: 35px;
	margin-right: 18px;
}
.word-count-left {
	position: absolute;
	width: 15px;
	height: 11px;
	font-size: 11px;
	font-weight: 400;
	color: #9b9b9b;
	right: 11px;
	bottom: 8px;
	line-height: 11px;
}
.permanent_input_BG {
	position: fixed;
	bottom: 0px;
	left: 0;
	width: 100%;
	height: 58px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.16);
	background: #ffffff;
	line-height: 14px;
}
.permanent_input {
	height: 30px;
	display: flex;
	vertical-align: top;
	color: #888888;
	overflow: hidden;
	text-overflow: ellipsis;
	width: calc(100% - 48px);

	padding: 3px 12px 4px;
	margin: 12px auto 0;
	border-radius: 8px;
	border: 2px solid rgba(252, 192, 65, 1);
	font-size: 12px;
	line-height: 30px;
}
</style>
