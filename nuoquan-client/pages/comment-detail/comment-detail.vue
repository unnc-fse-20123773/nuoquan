<template>
	<view>
		<!-- 主评论区域 -->
		<view class="commentBox">
			<view class="cmtdetail-IDline">
				<view class="cmtdetail-IDrel">
					<view class="cmtdetail-profilePic">
						<image :src="mainComment.faceImg" mode="aspectFill" class="profilePic"></image>
					</view>
					<view class="cmtdetail-middle">
						<view class="cmtdetail-IDtext">
							{{mainComment.nickname}}
						</view>
						<view class="cmtdetail-time">
							{{mainComment.createDate}}
						</view>
					</view>
					<view class="cmtdetail-right">
						<view class="cmtdetail-rightrel">
							<image src="../../static/icon/like.png" mode="aspectFill" class="likeIcon"></image>
							<!-- 此处点赞数量最长5位数，如超出样式出错 -->
							<text class="likeNum">{{mainComment.likeNum}}</text>
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
			 <sonCommentBox v-for="i in commentList" :key="i.id" :reCommentDetail="i" @controlInputSignal="controlInput"></sonCommentBox>
		</view>
		
		<!-- 输入框 -->
		<view class="bottoLayerOfInput" v-show="showInput" @tap="controlInput(0)" @touchmove="controlInput(0)">
			<view class="commentPart" @click.stop="" :style="{bottom: textAreaAdjust }">
				<view class="emoji"></view>
				<view class="submit" @click="saveComment()"></view>
				<textarea class="commentSth" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
				 adjust-position="false" v-model="commentContent" @click.stop="" :show-confirm-bar="false" @focus="popTextArea"
				 @blur="unpopTextArea" />
				</view>
	</view>
	</view>
</template>

<script>
	import sonCommentBox from './sonCommentBox.vue'
	export default {
		data() {
			// 我抄了一小部分代码过来，还没改 -Guetta
			// O(∩_∩)O
			// 😄
			// 🤭
			// (●'◡'●)
			// 页面有点丑（高仿微博），回头让仅仅优化一下
			return {
				mainComment:"",    //用于接受跳转传过来的underCommentId,然后申请获取sonComment  yaoyao 9.16 
				userInfo: '',
				commentContent:"",  //用户准备提交的评论内容
				commentList: '',  //返回值，获取评论列表信息,循环展示的东西，sonComment
				showInput:false,  //控制输入框，true时显示输入框
				writingComment:false,  //控制输入框，true时自动获取焦点，拉起输入法
				submitData:{},
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
							that.commentList = "";
							that.$nextTick(function(){
								that.commentList =  res.data.data.rows;	
							});
							// console.log(that.commentList);
						}
			
						// if (page == 1) {
						// 	that.reCommentList = [];
						// }
						// 
						// var newCommentList = res.data.data.rows;
						// var oldCommentList = that.reCommentList;
						// that.reCommentList = oldCommentList.concat(newCommentList);
						// that.currentPage = page;
						// that.totalPage = res.data.data.total;
					}
				});
			},
			controlInput(a){
				if(a!=0&&a!=1){ //a!=0, !=1， 从子组件传来，包含被回复对象：被回复人ID，被回复评论ID，被回复人昵称
					this.placeholderText='回复 @'+a.nickname+' 的评论';
					delete(a.nickname);
					this.submitData=a;
					this.showInput= true;
					this.writingComment = true ;
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
</style>
