<template>
	<view>
		<!-- 主评论区域 -->
		<view class="commentBox">
			<view class="cmtdetail-IDline">
				<view class="cmtdetail-IDrel">
					<view class="cmtdetail-profilePic">
						<image src="../../static/icon/about.png" mode="aspectFill" class="profilePic"></image>
					</view>
					<view class="cmtdetail-middle">
						<view class="cmtdetail-IDtext">
							IDIDIDIDIID
						</view>
						<view class="cmtdetail-time">
							2019.9.15 23:59
						</view>
					</view>
					<view class="cmtdetail-right">
						<view class="cmtdetail-rightrel">
							<image src="../../static/icon/like.png" mode="aspectFill" class="likeIcon"></image>
							<!-- 此处点赞数量最长5位数，如超出样式出错 -->
							<text class="likeNum">1223</text>
							<image src="../../static/icon/message.png" mode="aspectFill" class="commentIcon"></image>
						</view>
					</view>
				</view>
			</view>
			<view class="cmtdetail-contentBox" id="contentBox">
				这里没有一百字。这里没有一百字。这里没有一百字。这里没有一百字。这里没有一百字。
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
			<view class="son-commentBox">
				<view class="cmtdetail-IDline">
					<view class="cmtdetail-IDrel">
						<view class="son-cmtdetail-profilePic">
							<image src="../../static/icon/about.png" mode="aspectFill" class="son-profilePic"></image>
						</view>
						<view class="cmtdetail-middle">
							<view class="cmtdetail-IDtext">
								IDIDIDIDIID
							</view>
							<view class="cmtdetail-time">
								2019.9.15 23:59
							</view>
						</view>
						<view class="cmtdetail-right">
							<view class="cmtdetail-rightrel">
								<image src="../../static/icon/like.png" mode="aspectFill" class="son-likeIcon"></image>
								<!-- 此处点赞数量最长5位数，如超出样式出错 -->
								<text class="son-likeNum">1223</text>
							</view>
						</view>
					</view>
				</view>
				<view class="cmtdetail-contentBox" id="contentBox">
					这里没有一百字。这里没有一百字。这里没有一百字。这里没有一百字。这里没有一百字。
				</view>
				<view style="height: 2px;width: 74%;margin-left: 62px;background-color: #E4E4E4;margin-top: 10px;"></view>
			</view>
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
	export default {
		data() {
			// 我抄了一小部分代码过来，还没改 -Guetta
			// O(∩_∩)O
			// 😄
			// 🤭
			// (●'◡'●)
			// 页面有点丑（高仿微博），回头让仅仅优化一下
			return {
				userInfo: '',
				commentContent:"",  //用户准备提交的评论内容
				commentList: {},  //返回值，获取评论列表信息
				showInput:false,  //控制输入框，true时显示输入框
				writingComment:false,  //控制输入框，true时自动获取焦点，拉起输入法
			}
		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "回复"
			});
			var userInfo = this.getGlobalUserInfo()
			this.userInfo = userInfo;
			console.log(this.userInfo);
		},
		methods: {
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
