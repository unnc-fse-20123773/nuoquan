<template>
	<view class="detail-page">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" left-text="返回"
		:title="pageTitle" :showLeftIcon="true" :isNavHome="isNavHome"
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{'height': this.getnavbarHeight().bottom + 5 + 'px'}" style="width: 100%;"></view>
		
		<!-- 第一个大块二，文章本体 -->
		<detail_1_article class="article-area" :articleCard='articleCard' @controlInputSignal="controlInput" :userInfo="userInfo"
		 @swLikeArticleSignal="changeLikeStatus" @backToLastPage="backToLastPage()"></detail_1_article>

		<view style="border-bottom: 4px solid #ECECEC;height:0;width:750upx;font-size: 0;position: relative;left: -16px;"
		 @controlInputSignal="controlInput">这是分割线</view>
		<!--第一个大块二，评论区域-->

		<detail_2_comments class="comment-area" :commentList='commentList' @controlInputSignal="controlInput" :userInfo="userInfo"
		@onChange="changeType" :articleCardCommentNum='articleCard.commentNum'></detail_2_comments>



		<!--触底提示和功能  start-->
		<view class="comment-bottom" v-if="control_scroll_button_flag">
			<view class="comment-bottom-notice">划到底部啦</view>
			<view class="comment-bottom-buttons">
				<image class="back" @tap="backToLastPage" src="../../static/icon/arrow-left-fcc041.png" mode="aspectFit"></image>
				<image class="to-top" @tap="scrollToTop" src="../../static/icon/arrow-left-fcc041.png"></image>
				<!-- <view class="active-input-button" @click="controlInput(1)">发表评论</view> -->
			</view>
		</view>
		<!--触底提示和功能  END-->



		<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)" >
			<view class="commentPart"  :style="{bottom: textAreaAdjust }">
				<!--<view class="emoji"></view><view class="add-pic"></view>-->
				<view class="submit" @tap="saveComment()">发送</view>
				<view class="commentSth">
					<textarea class="comment-text" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
					 adjust-position="false" v-model="commentContent"  :show-confirm-bar="false" @focus="popTextArea"
					 @blur="unpopTextArea" cursor-spacing='20' />
					<!-- <view class="comment-pic-area"><image src="../../static/BG/indexBG.png"></image><image src="../../static/icon/about.png"></image><image src="../../static/icon/1575235531(1).png"></image></view> -->
					 <view class="word-count-left">{{wordNotice}}</view>
				</view>
            </view>
		</view> 
		
<!--常驻input--> 
    <view class="permanent_input_BG" v-if="!showInput" @click="controlInput(1)">
        <input class="permanent_input" :placeholder="placeholderText" v-model="commentContent" disabled="true" min-height="10px" />
    </view>
	<view style="height:80px;width:100%"></view><!-- 占位块 -->
	
 
 
 
 </view>
</template>

<script>
import detail_1_article from "./detail_1_article.vue"
import detail_2_comments from "./detail_2_comments.vue"
import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"

	var uploadFlag = false;
	export default {
		data() {
			return {  
				pageTitle: '详情',
				imgList: [],				
				userInfo: {},
				articleCard: "",  //detail的主角，由index传过来的单个文章信息
                commentContent:"",  //用户准备提交的评论内容
				commentList: {},  //返回值，获取评论列表信息
				
				showInput:false,  //控制输入框，true时显示输入框
				writingComment:false,  //控制输入框，true时自动获取焦点，拉起输入法
				placeholderText: "评论点什么吧......",
				wordNotice:"48",
				inputData:{},  //localData,用于拼接不同情况下的savecomment请求的数据
				
				submitData:{
					//这个是从子组件传来的数据，回复评论的评论之类
				},
				imgIndex: '',
				serverUrl: this.$serverUrl,
				
				textAreaAdjust:"",
				
				totalPage: 1,
				currentPage: 1,
				type: 0, //查询评论接口参数，0：按时间查询, 1：按热度查询
				control_scroll_button_flag:0,
				
				isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
			};
		},
		
		components: {
			detail_1_article,
			detail_2_comments,
			uniNavBar,
		},

		onReachBottom() {
			this.loadMore();
		},
		
		onUnload() {
			// 更新本文章信息给上级页面（主页）
			uni.$emit("updateArticle", this.articleCard);
			console.log("返回")
		},
		
		async onLoad(options) {
			//获取全局用户信息
			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				this.userInfo = this.getGlobalUserInfo();
			}else{
				uni.redirectTo({
				    url: '../signin/signin'
				});
				return;
			}
			
			var articleId = options.data;
			var res = await this.getArticleById(articleId, this.userInfo.id);
			// console.log(res);
			this.articleCard = res;
			
			var page = this.currentPage;
			this.getComments(page);

			this.addViewCount();
		},

		onShareAppMessage(res) {
			if (res.from === 'menu') {// 来自右上角菜单的分享
				return {
					title: '来，给老子看！',
					path: '/pages/detail/detail?data=' + this.articleCard.id
				}
			}
		},
		
		methods: {
			getArticleById(articleId, userId){
				var that = this;
				return new Promise((resolve, reject) => {
					uni.request({
						url: this.$serverUrl + '/article/getArticleById',
						method: 'POST',
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						data: {
							articleId: articleId,
							userId: userId,
						},
						success: (res) => {
							resolve(res.data.data)
						},
					})
				})
			},
			
			addViewCount(){
				uni.request({
					url: this.$serverUrl + '/article/addViewCount',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						userId: this.userInfo.id,
						articleId: this.articleCard.id
					},
					success: (res) => {
						
					},
				})
			},
			
			popTextArea(e){
				console.log("展开");
				console.log(e);
				console.log(e.detail.height);
				this.textAreaAdjust =  e.detail.height/3 + 'px' ;
				// this.textAreaAdjust = '0' ;
			},

			unpopTextArea(e){
				console.log("收起");
				console.log(e);
				
				this.textAreaAdjust = "";
			},
			changeLikeStatus(status){
			this.articleCard.isLike = status;
			 if(status){
			  this.articleCard.likeNum++;
			  }else{
		      this.articleCard.likeNum--;
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
				console.log('tragger savecomment');
				if (uploadFlag) {
					console.log("正在上传...")
					return;
				}
				uploadFlag = true;
				uni.showLoading({
					title: "正在上传..."
				})
				
				this.submitData.comment=this.commentContent;
				this.submitData.fromUserId=this.userInfo.id;
				this.submitData.articleId=this.articleCard.id;
				console.log(this.submitData);
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
							if (res.data.status == 200) {
								uni.hideLoading();
								uploadFlag = false;
								
								that.writingComment = false;
								that.commentContent = "";
								that.showInput = false;
								// 强制子组件重新刷新
								// that.commentList = '';
								// that.$nextTick(function() {
								// 	that.getComments(1);
								// });
								that.getComments(1);
								
								that.articleCard.commentNum++; // 文章评论数累加
							}else if (res.data.status == 500){
								that.contentIllegal();
							}
							
						},
					})
				}
			},
			
			contentIllegal(){
				// 内容非法 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '内容涉嫌违规，请联系管理员。',
					duration: 2000,
					icon: 'none',
				})
			},
			
			changeType(e){
				this.type = e.type;
				this.getComments(1);
			},
			
			getComments: function(page) {
				var that = this;
				uni.showLoading({
					title:"加载中..."
				})
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getMainComments',
					data: {
						articleId: that.articleCard.id,
						userId: that.userInfo.id,
						page: page,
						type: that.type
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if(res.data.status == 200){
							if (page == 1) {
								that.commentList = [];
							}
							console.log(res);
							var newCommentList = res.data.data.rows;
							var oldCommentList = that.commentList;
							that.commentList = oldCommentList.concat(newCommentList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
							// console.log(that.articleCard.id);
						}else{
							console.log(res);
						}
						uni.hideLoading();
						this.control_scroll_butoon();//获取评论数据后，生成卡片后，判断总页面高度，控制是否显示回到顶部按钮
					}
				});
			},

			loadMore: function(){
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (that.commentList.length < 10){
					return;
				} else if(currentPage == totalPage){
					// that.showArticles(1);
					uni.showToast({
						title:"没有更多评论了",
						icon:"none",
						duration:1000
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
					this.writingComment = true;
					this.showInput= true;
				}else if(a==1){ //a==1 当前页面调用，直接评论文章
					this.submitData.toUserId=this.articleCard.userId;
					this.showInput = true;
					this.writingComment = true; 
				}else{ //a==0, 关闭输入框，一切恢复默认状态
				    console.log('this is control input in detail. a ==0, EXIT');
					this.submitData = {};
					this.placeholderText="评论点什么吧......";
					this.showInput = false;
					this.writingComment =false;
				}
			},
			
			
			goToPersonPublic(){
				uni.navigateTo({
					url: '/pages/personpublic/personpublic?userId=' + this.articleCard.userId,
				})
			},
			
			backToLastPage(){
				uni.navigateBack({
				})
			},
			scrollToTop(){
				uni.pageScrollTo({
				    scrollTop: 0,
				    duration: 300
				});
       		},
			control_scroll_butoon(){
				var content_height;
				var page_height;
				let info1 = uni.createSelectorQuery().select(".article-area");
				info1.boundingClientRect(function(data) { //data - 各种参数
                    content_height = data.height;
					// console.log(content_height);
				  　}).exec();
				let info2 = uni.createSelectorQuery().select(".comment-area");
				info2.boundingClientRect(function(data) { //data - 各种参数
				    if(data.height!=0){
					    content_height = content_height + data.height;
						// console.log(content_height);
				     }            
				}).exec();
				uni.getSystemInfo({
				    success: function (res) {
				        page_height = res.screenHeight;
				    }
				    });
				if(content_height > (page_height * 2)){
					console.log(page_height);
					console.log(content_height);
					this.control_scroll_button_flag = 1 ;
				}
				},//获取评论数据后，生成卡片后，判断总页面高度，控制是否显示回到顶部按钮
		},//method
	};
</script>
<style>	page {
		height: 100%;
		width: 100%;
		background: #FCFCFC;
	}</style>

<style scoped>
	.detail-page{
		width: calc(100% - 32px);
		margin:auto;
		background: #FCFCFC;
	}
	
/* 滑到底了等提示
 */	.comment-bottom{
		height:160px;
		width:calc(88px + 40upx);
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



	
	/* 以下五条为底部输入框样式 */
	.bottoLayerOfInput{
		position: absolute;
		width: 750upx;
		height: 1000px;
		bottom:0;
		left:0;
		z-index: 40;
	}
	
	.commentPart {
		box-shadow: 0px 1px 5px 0px rgba(139, 139, 139, 0.32);
		position:absolute;
		bottom: 0;
		z-index: 999;
		left: 0;
		width: 702upx;
		padding:10px 24upx 4px;
		min-height: 50px;
		background: white;
	}

	.emoji{
		background-repeat: no-repeat;
		background-position: center;
		border: none;
		width: 21px;
		height: 21px;
		background-size: 21px 21px;
		margin-bottom: 7px;
		display: inline-block;
	}
	.add-pic{
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
.submit{
	display: inline-block;
	width: 42px;
	position: absolute;
	top:15px;
	height:21px;
	right: 15px;
/* 	background: url(../../static/icon/arrow-right.png);
 */	background-size: 14px 14px;
	background-repeat: no-repeat;
	background-position:center;
	float:right;
	font-size: 14px;
	color: #FCC041;
	z-index: 50;
}
	.commentSth {
		
		border: solid 2px #FCC041;
		border-radius: 8px;
		line-height: 20px;
		padding:12px 12px 0px;
		position: relative;

	}
	.comment-text{
		width: calc(670upx - 60px);	
		font-size: 14px;
		max-height: 95px;
		line-height: 20px;
		max-height: 100px;
		padding-bottom: 14px;
		
	}
	.comment-pic-area{
		position: relative;
		height:45px;
	}
	.comment-pic-area image{
		width:35px;
		height:35px;
		margin-right: 18px;

	}

	.word-count-left{
	position: absolute;
		width:15px;
		height:11px;
		font-size: 11px;
		font-weight: 400;
		color:#9B9B9B;
		right:11px;
		bottom:8px;
		line-height: 11px;
	}
	.permanent_input_BG{
		position: fixed;
		bottom: 0px;
		left: 0;
		width: 100%;
		height:58px;
	    box-shadow:0px 0px 10px rgba(0,0,0,0.16);
		background: #FFFFFF;
line-height: 14px;
	}
	.permanent_input{
	height:30px ;
	display: flex;
	vertical-align: top;
	color:#888888;overflow: hidden;
	text-overflow: ellipsis;
	width:calc(100% - 48px);
	
	padding: 3px 12px 4px;
	margin:12px auto 0;
	border-radius:8px;
	border:2px solid rgba(252,192,65,1);
    font-size: 12px;
	line-height: 30px;
}
</style>
