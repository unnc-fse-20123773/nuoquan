<template>
	<view style="height:100%;width:100%;">
		<view class="topbar">
			<view class="detailtitle">{{ articleCard.articleTitle }}</view>
		</view>

		<view class="drtailmain">
			<view class="detailcontent">{{ articleCard.articleContent }}</view>
			<view class="detailpics">
				<!-- <image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image>
				<image class="detailpic" src="../../static/0001/pic3.jpg"></image> -->
			</view>
			<view class="tags">
				<view class="tag" v-for="(i,index) in articleCard.tags" v-bind:key="index">{{i}}</view>
			</view>
			<view class="bottombar">
				<view style="width:70%;display:inline-block;">
					<image :src="articleCard.faceImg" class="touxiang"></image>
					<view class="name">{{ articleCard.nickname }}</view>

					<view class="time">{{ articleCard.createDate | timeDeal}}</view>
				</view>
				<view class="icons">
					<!-- 点赞MM按钮 -->
					<!-- 					MMMMMMMM
 -->
					<image class="icon" src="../../static/icon/like.png"></image>
					<view class="icom">{{ articleCard.likeNum }}</view>
				</view>
			</view>
			<commentbox v-for="i in commentList" :key="i.id" v-bind:commentDetail="i"></commentbox>
			<view class="fengexian" style="height: 1px;width: 100%;background-color: #d6d6d6;margin:auto;"></view>
			<view class="submitComment" @click="controlInput()">发 表 评 论</view>

			<view class="bottoLayerOfInput" v-show="writingComment"  @click="controlInput()" @touchmove="controlInput()">
				<view class="commentPart" @click.stop="">
					<view class="emoji"></view>
					<view class="submit" @click="saveComment()"></view>
					<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
					 confirm-type="send" @confirm="saveComment()" adjust-position="false" v-model="commentContent" />
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
				userInfo: {},
				articleCard: "",  //detail的主角，由index传过来的单个文章信息
                commentContent:"",  //用户准备提交的评论内容
				commentList: {},  //返回值，获取评论列表信息
				writingComment:false,  //控制输入框，true时显示输入框同时输入框自动获取焦点，拉起输入法
				placeholderText:"评论点什么吧......",
			};
		},
		components: {
			commentbox: comment
		},
		methods: {
			saveComment: function(e) {
				var that = this;
				var content = this.commentContent;
				var userInfoTemp = this.getGlobalUserInfo();
				if (this.isNull(userInfoTemp)) {
					uni.navigateTo({
						url: "../wechatLogin/wechatLogin"
					})
				} else {
					console.log(content)
					uni.request({
						url: that.$serverUrl + '/saveComment',
						method: 'POST',
						data: {
							fromUserId: that.userInfo.id,
							articleId: that.articleCard.id,
							comment: content
						},
						success: function(res) {
							console.log(res.data)
							that.writingComment = false;
							that.commentContent = "";
							// uni.redirectTo({
							// 	url: '/pages/detail/detail'
							// })
						},

					})
				}
			},
			getComments: function() {
				var that = this;
				uni.request({
					method: "POST",
					url: that.$serverUrl + '/article/getArticleComments',
					data: {
						articleId: that.articleCard.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {	console.log(res);
						that.commentList = res.data.data.rows;
						console.log(that.articleCard.id);
						
					},
				});
			},
			controlInput(){
				this.writingComment =!this.writingComment;
				console.log(this.writingComment)
			}
		},
		onLoad(options) {
			// console.log('detail receved');
			// console.log(options.data);

			this.articleCard = JSON.parse(options.data);
			console.log(this.articleCard);
			// console.log(this.articleCard);
			// console.log(this.articleCard.artiticleTitle);

			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				this.userInfo = this.getGlobalUserInfo();
			}
			// console.log(this.articleCard.id);
			// console.log(this.userInfo.nickname);
			this.getComments();
		},
		filters: {
			timeDeal(timediff) {
				timediff = new Date(timediff);
				var parts = [timediff.getFullYear(), timediff.getMonth(), timediff.getDate(), timediff.getHours(), timediff.getMinutes(),
					timediff.getSeconds()
				];
				var oldTime = timediff.getTime();
				var newTime = new Date().getTime();
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
				} else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && year == now.getFullYear()) {
					timeSpanStr = parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				} else {
					timeSpanStr = parts[0] + '-' + parts[1] + '-' + parts[2] + ' ' + parts[3] + ':' + parts[4];
				}
				return timeSpanStr;
			}

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
	}

	.detailcontent {
		padding-top: 25px;
		font-size: 13px;
		/* width: 85%;
		margin: 0px auto 10px; */
		font-weight: 400;
	}

	.detailpics {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-wrap: wrap;
	}

	.detailpic {
		width: 180upx;
		height: 180upx;
		margin: 6px;
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
	}

	.time {
		display: inline-block;
		font-size: 10px;
		margin-left: 25px;
		color: #888888;
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
	}

	.emoji {
		background: url(../../static/icon/emoji.png);
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
