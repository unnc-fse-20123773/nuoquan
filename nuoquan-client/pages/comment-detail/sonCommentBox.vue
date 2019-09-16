<template>
	<view>
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
</template>

<script>
	export default{
		props:{
		  reCommentDetail:"",//沿用之前组件名字了，可改，记得本组件内全替换
		},
		data(){
			
		},
		methods:{
				
			controlIputInSonCommentBox(){
				var dataOfRecomment={
					toUserId:this.reCommentDetail.fromUserId,
					fatherCommentId:this.reCommentDetail.id,
					underCommentId:this.reCommentDetail.underCommentId,//这条检查一下是否有传进来这个组件
					nickname:this.reCommentDetail.nickname,
				}
				this.$emit('controlInputSignal',dataOfRecomment)
			}
		},
		
		
	}
	
	
</script>

<style>
</style>
