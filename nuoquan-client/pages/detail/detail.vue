<template>
	<view class="detail-page">
<!-- 		<article :article="this.articleCard"></article>
 -->
 <detail_1_article class="article-area" :articleCard='articleCard' ></detail_1_article>
			<!-- 用于推出评论下方空白 -->
			<view name="marginHelper" style="height: 50px;width: 100%;background-color: white;"></view>
<!-- 			发表评论按钮 -->
			<view class="bottomLayerOfSubmit">
				<view class="submitComment" @click="controlInput(1)">发 表 评 论</view>
			</view>

			<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)">
				<view class="commentPart" @click.stop="" :style="{bottom: textAreaAdjust }">
					<view class="emoji"></view>
					<view class="submit" @click="saveComment()">发表</view>
					<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
					 adjust-position="false" v-model="commentContent" @click.stop="" :show-confirm-bar="false" @focus="popTextArea"
					 @blur="unpopTextArea" cursor-spacing='20' />
					</view>
            </view>
		</view> 
 </view>
</template>

<script>
import detail_1_article from "./detail_1_article.vue"
	var uploadFlag = false;
	export default {
		data() {
			return {  
				imgList: [],
				singleImgState: '0',
				
				userInfo: {},
				articleCard: "",  //detail的主角，由index传过来的单个文章信息
                commentContent:"",  //用户准备提交的评论内容
				commentList: {},  //返回值，获取评论列表信息
				showInput:false,  //控制输入框，true时显示输入框
				writingComment:false,  //控制输入框，true时自动获取焦点，拉起输入法
				placeholderText: "评论点什么吧......",
				inputData:{},  //localData,用于拼接不同情况下的savecomment请求的数据
				
				submitData:{
					//这个是从子组件传来的数据，回复评论的评论之类
				},
				imgIndex: '',
				serverUrl: this.$serverUrl,
				
				textAreaAdjust:"",
				tagColorList: [],
				
				totalPage: 1,
				currentPage: 1,
			};
		},
		components: {
			detail_1_article,
		},
		
		
		
		onReachBottom() {
			this.loadMore();
		},
		
		onUnload() {
			// 更新本文章信息给上级页面（主页）
			uni.$emit("updateArticle", this.articleCard);
			console.log("返回")
		},
		
		onLoad(options) {
			console.log(options);
			this.articleCard = JSON.parse(options.data);
			console.log("receive data from onload");
			console.log(this.articleCard);
			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				this.userInfo = this.getGlobalUserInfo();
			}
			var page = this.currentPage;
			this.getComments(page);

			// 随机生成颜色
			if(!this.isNull(this.articleCard.tagList)){
				var tagColors = this.tagColors;
				for (var i=0; i<this.articleCard.tagList.length; i++){
					var random = Math.floor(Math.random()*tagColors.length); // 0~tagColors.length-1
					this.tagColorList.push(tagColors[random]);
				}
			}
			
			this.addViewCount();
		},
		
		onShareAppMessage(res) {
			if (res.from === 'menu') {// 来自右上角菜单的分享
				var navData = JSON.stringify(this.articleCard);
				console.log(navData)
				return {
					title: '来，给老子看！',
					path: '/pages/detail/detail?data=' + navData
				}
			}
		},
		
		methods: {
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
								that.commentList = '';
								that.$nextTick(function() {
									that.getComments(1);
								});
								
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

			singleImgeFit(e){
				var height = e.detail.height;
				var width = e.detail.width;
				if(height >= width){
					this.singleImgState = 0;
				}else{
					this.singleImgState = 1;
				}
				// console.log(e.detail);
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
					this.placeholderText="评论";
					this.showInput = false;
					this.writingComment =false;
				}
			},
			
			swLikeArticle(){
				if (this.articleCard.isLike){
					this.unLikeArticle();
					this.articleCard.likeNum--;
				}else{
					this.likeArticle();
					this.articleCard.likeNum++;
				}
				this.articleCard.isLike = !this.articleCard.isLike;
			},
			
			likeArticle(){
				console.log("点赞文章");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userLikeArticle',
					data: {
						userId: that.userInfo.id,
						articleId: that.articleCard.id,
						articleCreaterId: that.articleCard.userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {	
						console.log(res);
					},
				});
			},
			
			unLikeArticle(){
				console.log("取消点赞文章");
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/userUnLikeArticle',
					data: {
						userId: that.userInfo.id,
						articleId: that.articleCard.id,
						articleCreaterId: that.articleCard.userId,
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
					url: '/pages/personpublic/personpublic?userId=' + this.articleCard.userId,
				})
			},
			
			previewImg: function(index) {
				var imgIndex = index;
				// console.log(res)
				// 获取全部图片路径
				var imgList = this.articleCard.imgList;
				var arr = [];
				var path;
				for (var i=0; i<imgList.length; i++){
					// console.log(imgList[i].imagePath);
					path = this.serverUrl + imgList[i].imagePath
					arr = arr.concat(path);
				}
				// console.log(arr);
				
				uni.previewImage({
					current: index,
					urls:arr,
				})
			},

			aboutImg: function(index){
				var that = this;
				console.log(this.articleCard.imgList[index].imagePath);
				uni.showActionSheet({
					itemList: ['保存图片到本地'],
					success: function(res) {
						console.log(res.tapIndex);
						// 保存图片至本地
						if(res.tapIndex == 0) {
							uni.showLoading({
								title:'下载中...'
							})
							uni.downloadFile({
								url: that.serverUrl + that.articleCard.imgList[index].imagePath,
								success: function(res) {
									if(res.statusCode == 200){
										uni.saveImageToPhotosAlbum({
											filePath: res.tempFilePath,
											success: function () {
												console.log('save success');
												uni.hideLoading();
											},
											fail: function() {
												console.log('save failed');
												uni.hideLoading();
												uni.showToast({
													title:'保存失败',
													icon:'none',
													duration:1000,
												})
											}
										});
									}
								}
							})
						}
					}
				});
			},
		},
	};
</script>
<style>	page {
		height: 100%;
		width: 100%;
	}</style>

<style scoped>
	.detail-page{
		width: calc(100% - 32px);
		margin:auto;
	}
	
	/* 底部栏 */
    .bottomLayerOfSubmit{
		display: flex;
		position: absolute;
		height: 48px;
		width: 750upx;
		left:0;
		bottom: 0;
		background: #FFFFff;
		justify-content: center;
		align-items: center;
		z-index: 30;
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
		width: 670upx;
		padding:11px 40upx;
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
.submit{
	display: inline-block;
	width: 42px;
	position: relative;
	top:3px;
	height:21px;
/* 	background: url(../../static/icon/arrow-right.png);
 */	background-size: 14px 14px;
	background-repeat: no-repeat;
	background-position:center;
	float:right;
	font-size: 14px;
	color: #FCC041;
}
	.commentSth {
		width: calc(670upx - 20px);
		border: solid 1px #FCC041;
		border-radius: 10px;
		line-height: 20px;
		font-size: 14px;
		padding:8px 10px;
		max-height: 95px;
	}
</style>
