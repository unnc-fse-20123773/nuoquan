<template>
	<view style="position: relative;height:100%;width:100%;">
		<view class="topbar column_center">
			<view class="detailtitle">{{ articleCard.articleTitle }}</view>
		</view>
		<!-- 9.16 by Guetta -->
		<!-- drtailmain 修改拼写错误为 detailmain，并修改高度为 90% 以保证页面不会错误滚动 -->
		<view class="detailmain">
			<view class="detailcontent">{{ articleCard.articleContent }}</view>

			<view>
				<!-- 单图显示 -->
				<view v-if="articleCard.imgList.length==1" class="detailpics 1pic" style="width: 100%;max-height: 400upx;display: flex;">
					<image v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
					 style="height: 360upx;max-width:180px;display: inline-block;" @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				</view>
				<!-- 其他数量 -->
				<view v-else-if="articleCard.imgList.length==4" class="detailpics" style="max-width: 400upx;margin-left: 0;">
					<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
					 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				</view>

				<view v-else class="detailpics">
					<image class="detailpic" v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill"
					 @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
					<view v-if="articleCard.imgList.length==2||imageList.length==5||imageList.length==8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>

				</view>

			</view>
			<view class="tags">
				<view class="tag" :style="{background: tagColorList[index]}" v-for="(i,index) in articleCard.tagList" v-bind:key="index">{{i}}</view>
			</view>

			<!-- ID 行 -->
			<view class="bottombar">
				<!-- 蒙层,用于优化体验 -->
				<view style="position: absolute;z-index: 20;height: 100%;width: 90upx;right: 16upx;" @tap="swLikeArticle()"></view>
				<!-- 蒙层结束 -->
				<view class="touxiang column_center">
					<image :src="articleCard.faceImg" class="touxiang_pic" @click="goToPersonPublic()"></image>
				</view>
				<view class="text_line">
					<view class="text_line_rel">
						<view class="name column_center">
							<view class="name_text">{{ articleCard.nickname }}</view>
						</view>
						<view class="time column_center">
							<view class="time_text">{{ articleCard.createDate | timeDeal}}</view>
						</view>
						<view class="icons column_center" @tap="swLikeArticle()">
							<view class="column_center" style="position: relative;width: 100%;height: 100%;">
								<image v-if="!articleCard.isLike" class="icon" src="../../static/icon/like.png"></image>
								<image v-if="articleCard.isLike" class="icon" src="../../static/icon/liked-red.png"></image>
								<view class="icom" :class="{'liked':articleCard.isLike}">{{ articleCard.likeNum }}</view>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view style="width: 100%;height: 12px;display: flex;" class="column_center">
				<view style="width:20%;color: RGB(253, 217, 108);font-size: 15px;">
					最新评论
				</view>
				<view class="fengexian" style="height: 1px;width: 80%;background-color: RGB(253, 217, 108);"></view>
			</view>
			<commentbox v-for="i in commentList" :key="i.id" v-bind:commentDetail="i" @controlInputSignal="controlInput">
			</commentbox>
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
	import comment from '../../components/comment';
	
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
			commentbox: comment
		},
		
		filters: {
			timeDeal(timediff) {
				timediff = new Date(timediff);
				var parts = [timediff.getFullYear(), timediff.getMonth()+1, timediff.getDate(), timediff.getHours(), timediff.getMinutes(),timediff.getSeconds()];
				var oldTime = timediff.getTime();
				var now = new Date();
				var newTime = now.getTime();
				var milliseconds = 0;
				var timeSpanStr;
				milliseconds = newTime - oldTime;
				if (milliseconds <= 1000 * 60 * 1) {
					timeSpanStr = '刚刚';
				} else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
					timeSpanStr = Math.round((milliseconds / (1000 * 60))) + '分钟前';
				} else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + '小时前';
				} else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
					timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + '天前';
				} else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && parts[0] == now.getFullYear()) {
					timeSpanStr = parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				} else {
					timeSpanStr = parts[0] + '-' + parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				}
				return timeSpanStr;
			}
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
			this.articleCard = JSON.parse(options.data);
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

	.topbar {
		background: url(../../static/BG/detailBG.png);
		background-repeat: no-repeat;
		background-position-y: -5px;
		background-size: cover;
		height: 16%;
	}

	.detailtitle {
		width: 85%;
		color: #f5f5f5;
		font-size: 20px;
		margin: auto;
		font-weight: 550;
		padding-bottom:22px;
		word-break: break-all;
		word-wrap: break-word;
		
	}

	.detailmain {
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
		margin-top: -20px;
		background: white;
		/* box-shadow: 0px 0px 10px 1px #c0c0c0; */
		/* 高度90%才可以保证页面不会无故滚动 */
		height: 84%;
		width: 85%;
		padding: 0 7.5%;
		overflow: scroll;
	}

	.detailcontent {
		padding-top: 25px;
		padding-bottom: 15px;
		font-size: 17px;
		font-family: Source Han Sans CN;
/* 		width: 85%;
		margin: 0px auto 10px; */
		/* font-weight: 400;
		word-break:break-all;
		white-space:pre-line;
	}

/*	.picturearea-one {
		margin: auto;
		display: flex;
		width: 94%;
		margin-left: 3%;
	}
	
	.picturearea-mul {
		position: relative;
		margin: auto;
		display: flex; */
		/* 在此设置图片区域宽度 */
		/* width: 94%;
		margin-left: 3%;
	} */
	
		font-weight: 400;
		word-break: break-all;
		word-wrap: break-word;
		white-space:pre-line;
	}

	.detailpics {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		align-items: center;
		flex-wrap: wrap;
		width: 100%;
		margin: 0 auto;
		margin-bottom: 9px;
	}

	.detailpic {
		width: 190upx;
		height: 190upx;
		margin: 6px 0;
	}

	.tags {
		max-height: 20px;
		line-height: 15px;
		width: 85%;
		margin-left: -5px;
	}

	.tag {
		display: inline-block;
		border-radius: 4px;
		padding-left: 5px;
		padding-right: 5px;
		margin-left: 5px;
		height: 15px;
		line-height: 15px;
		color: #ffffff;
		font-size: 10px;
		background: #621E81;
		vertical-align: middle;
	}

	.articleCard {
		margin: 2px auto 0;
		width: 90%;
		border-radius: 5px;
	}

	.bottombar {
		position: relative;
		border-radius: 20px;
		margin-top: 20px;
		padding-bottom: 5px;
		height: 30px;
	}
	
	.touxiang{
		position: absolute;
		left: 0;
		height: 30px;
		width: 10%;
	}
	
	.touxiang_pic {
		border-radius: 300px;
		width: 20px;
		height: 20px;
		margin-right: 5px;
		vertical-align: middle;
	}
	
	
	.text_line{
		position: absolute;
		left: 10%;
		height: 30px;
		width: 90%;
	}
	
	.text_line_rel{
		position: relative;
	}
	
	.name {
		position: absolute;
		left: 0;
		height: 30px;
		width: 38%;
	}
	
	.name_text{
		font-size: 13px;
		color: #888888;
		text-overflow: ellipsis;
		max-width: 80px;
	}

	.time {
		position: absolute;
		left: 38%;
		height: 30px;
		max-width: 85px;
	}

	.time_text{
		margin-top: 1px;
		font-size: 12px;
		color: #888888;
		text-overflow: ellipsis;
	}

	.icons {
		position: absolute;
		right: -8%;
		width: 36%;
		font-size: 10px;
		height: 30px;
		z-index: 10;
	}

	.icon {
		position: absolute;
		right: 46%;
		width: 15px;
		height: 15px;
		font-size: 2px;
		z-index: 10;
		/* padding-right: 8upx; */
	}
	
	.liked{
		position: absolute;
		right: 56%;
		color: #fe5f55;
		z-index: 10;
	}
	
	.icom{
		position: absolute;
		left: 60%;
		font-size: 13px;
		z-index: 10;
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
