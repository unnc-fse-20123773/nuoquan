<template>
	<view style="height:100%;width:100%;">
		<view class="topbar">
			<view class="detailtitle">{{ articleCard.articleTitle }}</view>
		</view>

		<view class="drtailmain">
			<view class="detailcontent">{{ articleCard.articleContent }}</view>
				
			<view class="detailpics">
				<!-- 单图显示 -->
				<view v-if="articleCard.imgList.length==1" class="1pic" style="width: 100%;max-height: 400upx;">
					<image v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFit" style="height: 360upx;" @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				</view>
				<!-- 其他数量 -->
				<image class="detailpic" v-else v-for="(i,index) in articleCard.imgList" :key="index" :src="serverUrl + i.imagePath" mode="aspectFill" @tap="previewImg(index)" @longpress="aboutImg(index)"></image>
				<view v-if="articleCard.imgList.length==2||imageList.length==5||imageList.length==8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
			</view>
			<view class="tags">
				<view class="tag" :style="{background: tagColorList[index]}" v-for="(i,index) in articleCard.tagList" v-bind:key="index">{{i}}</view>
			</view>
			<view class="bottombar">
				<view style="width:70%;display:inline-block;">
					<image :src="articleCard.faceImg" class="touxiang" @click="goToPersonPublic()"></image>
					<view class="name">{{ articleCard.nickname }}</view>

					<view class="time">{{ articleCard.createDate | timeDeal}}</view>
				</view>
				<view class="icons" @tap="swLikeArticle()">
					<!-- 点赞MM按钮 TODO: 增加点赞后样式-->
					<image class="icon" src="../../static/icon/like.png"></image>
					<view class="icom">{{ articleCard.likeNum }}</view>
				</view>
			</view>
			
			<commentbox v-for="i in commentList" :key="i.id" v-bind:commentDetail="i" @controlInputSignal="controlInput">
			</commentbox>

			<view class="fengexian" style="height: 1px;width: 100%;background-color: #d6d6d6;margin:auto;"></view>
			<view class="submitComment" @click="controlInput(1)">发 表 评 论</view>

			<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)" >
				<view class="commentPart" @click.stop="" :style="{bottom: textAreaAdjust }">
					<view class="emoji"></view>
					<view class="submit" @click="saveComment()"></view>
					<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
					 adjust-position="false" v-model="commentContent" @click.stop="" 
					 :show-confirm-bar="false"
					  @focus="popTextArea" @blur="unpopTextArea"/>
					</view>
            </view>
		</view> 
 </view>
</template>

<script>
	import comment from '../../components/comment';
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
				placeholderText:" 评论点什么吧......",
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
		
		onLoad(options) {
			this.articleCard = JSON.parse(options.data);
			// console.log(this.articleCard);
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
		},
		
		methods: {
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
				this.submitData.comment=this.commentContent;
				this.submitData.fromUserId=this.userInfo.id;
				this.submitData.articleId=this.articleCard.id;
				console.log(this.submitData);
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/saveComment',
					method: 'POST',
					data: this.submitData,
					success: (res) => {
						that.writingComment = false;
						that.commentContent = "";
						this.showInput = false;
						
						if (res.data.status == 200) {
							// 强制子组件重新刷新
							that.commentList = '';
							that.$nextTick(function() {
								that.getComments(1);
							});
							// console.log(res);
							// if(that.isNull(that.submitData.underCommentId)){
							// 	that.getComments(that.currentPage);
							// }else{
							// 	uni.$emit("flashSubComment", that.submitData.underCommentId);
							// }
						}
					},
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
				if (currentPage == totalPage){
					// that.showArticles(1);
					uni.showToast({
						title:"没有更多评论了",
						icon:"none",
						duration:1000
					})
				} else if(that.commentList.length < 10){
					return;
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
				if(a!=0&&a!=1){ //a!=0, !=1， 从子组件传来，包含被回复对象：被回复人ID，被回复评论ID，被回复人昵称
					this.placeholderText='回复 @'+a.nickname+' 的评论';
					delete(a.nickname);
					this.submitData=a;
					if(a.mode == "re-co"){
						this.writingComment = true;
					}
					if(a.mode =="re-re"){    //mode ="re-re", from grandson RECOMMENT
						console.log(a.mode);
						this.writingComment = true ;
					}
					this.showInput= true;
					console.log(this.writingComment);
				}else if(a==1){ //a==1 当前页面调用，直接评论文章
					this.submitData.toUserId=this.articleCard.userId;
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
		
		background-color: RGB(253, 217, 108);padding-bottom: 60px;
	}

	.detailtitle {
		width: 85%;
		color: #f5f5f5;
		font-size: 14px;
		margin: auto;
		font-weight: 400;
		padding-top:12px;
		word-break: break-all;
		word-wrap: break-word;
		
	}

	.drtailmain {
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
		margin-top: -20px;
		background: white;
		box-shadow: 0 -1px 8px grey;
		height: 100%;
		width: 85%;
		padding: 0 7.5%;
		overflow: scroll;
	}

	.detailcontent {
		padding-top: 25px;
		padding-bottom: 15px;
		font-size: 13px;
		/* width: 85%;
		margin: 0px auto 10px; */
		/* font-weight: 400;
		word-break:break-all;
		white-space:pre-line;
	}

	.picturearea-one {
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
	}

	.detailpics {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		align-items: center;
		flex-wrap: wrap;
		width: 606upx;
		margin: 0 auto;
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
		margin: auto;
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
	}

	.touxiang {
		border-radius: 30px;
		width: 20px;
		height: 20px;
		margin-right: 5px;
		vertical-align: middle;

	}

	.name {
		display: inline-block;
		font-size: 10px;
		margin-left: 7px;
		color: #888888;
		padding-bottom: 5px;
		max-width: 80px;
		text-overflow: ellipsis;
	}

	.time {
		display: inline-block;
		font-size: 10px;
		margin-left: 25px;
		color: #888888;
		max-width: 85px;
		text-overflow: ellipsis;
	}


	.icons {
		justify-content: flex-end;
		display: inline-flex;
		align-items: center;
		width: 30%;
		font-size: 10px;

	}

	.icon {
		width: 11px;
		height: 11px;
		font-size: 2px;
		padding-left: 45upx;
		padding-right: 8upx;

	}

	.submitComment {
		position: fixed;
		display: block;
		left:34%;
		bottom:9px;
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
		background: #058ECC;
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
