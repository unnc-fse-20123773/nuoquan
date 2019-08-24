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
			</view>		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image :src="commentDetail.faceImage" class="touxiang"></image>
				<view class="name">{{ commentDetail.nickname }}</view>

				<view class="time">{{ commentDetail.timeAgo }}</view>
			</view>
			<view class="icons">
				<!-- 点赞按钮 -->
				<image class="icon" src="../../../static/icon/like.svg"></image>
				<view class="icom">{{ commentDetail.likeNum }}</view>
			</view>
		</view>
			<commentbox v-for="i in commentList" :key="i.id" v-bind:commentDetail="i"></commentbox>
			<view class="fengexian" style="height: 1px;width: 90%;background-color: #d6d6d6;margin:auto;"></view>
			<!-- <view class="commentPart">
				<input class="commentSth" placeholder="评论点什么..." confirm-type="send" @confirm="saveComment" />
			</view> -->
		</view>

	</view>
</template>

<script>
	import comment from '../../components/comment';
	export default {
		data() {
			return {
				userInfo: {},
				comments: [
					// ['00001', '评论者ID', '楼主好棒', '一小时前', '60', '7'],
					// ['00003', '评论者ID', '楼主求微信', '一小时前', '90', '7'],
					// ['00005', '评论者ID', '楼主求微信', '一小时前', '60', '7'],
					// ['00009', '评论者ID', '楼主求微信', '一小时前', '9', '70']
				],
				articleCard: "",
				// taglist: [
				// 	['123', 'background:red'],
				// 	['13', 'background:blue'],
				// 	['163', 'background:yellow']
				// ],
				commentList: {},
			};
		},

		components: {

			commentbox: comment
		},
		methods: {
			saveComment: function(e) {
				var that = this;
				var content = e.detail.value;
				var userInfoTemp = this.getGlobalUserInfo();
				if (this.isNull(userInfoTemp)) {
					uni.navigateTo({
						url: "../wechatLogin/wechatLogin"
					})
				} else {
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
							// uni.redirectTo({
							// 	url: '/pages/detail/detail'
							// })
						}
					})
				}
			},
			getComments: function() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/getArticleComments',
					method: "POST",
					data: {
						articleId: that.articleCard.id,
						// page: '',
						// pageSize: ''
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						that.commentList = res.data.data.rows;
						console.log(that.articleCard.id),
							console.log(res)
					},
				});
			},
		},
		onLoad(options) {
			// console.log('detail receved');
			// console.log(options.data);

			this.articleCard = JSON.parse(options.data);
			// console.log(this.articleCard);
			// console.log(this.articleCard.artiticleTitle);

			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				this.userInfo = this.getGlobalUserInfo();
			}
			// console.log(this.articleCard.id);
			// console.log(this.userInfo.nickname);
			this.getComments();
		}
	};
</script>

<style>
	page {
		height: 100%;
		width: 100%;
	}

	.topbar {
		height: 100px;
		background-color: RGB(253, 217, 108);
	}

	.detailtitle {
		width: 85%;
		color: #f5f5f5;
		font-size: 14px;
		margin: auto;
		font-weight: 400;
	}

	.drtailmain {
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
		margin-top: -20px;
		background: white;
		box-shadow: 0 -1px 8px grey;
		height: 100%;
	}

	.detailcontent {
		padding-top: 25px;
		font-size: 13px;
		width: 85%;
		margin: 0px auto 10px;
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
		max-height:20px;
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
		height: 25px;
		margin-top: 20px;
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
		color:#888888;
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
		padding-right:8upx;
		
	}
	
</style>
