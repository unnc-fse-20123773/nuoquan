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
							{{reCommentDetail.nickname}}
						</view>
						<view class="cmtdetail-time">
							{{reCommentDetail.createDate}}
						</view>
					</view>
					<view class="cmtdetail-right">
						<view class="cmtdetail-rightrel">
							<image src="../../static/icon/like.png" mode="aspectFill" class="son-likeIcon"></image>
							<!-- 此处点赞数量最长5位数，如超出样式出错 -->
							<text class="son-likeNum">{{reCommentDetail.likeNum}}</text>
						</view>
					</view>
				</view>
			</view>
			<view class="cmtdetail-contentBox" id="contentBox">
				{{reCommentDetail.comment}}
			</view>
			<view style="height: 2px;width: 74%;margin-left: 62px;background-color: #E4E4E4;margin-top: 10px;"></view>
		</view>
	</view>
</template>

<script>
	export default{
		props:{
		  reCommentDetail: '',//沿用之前组件名字了，可改，记得本组件内全替换
		},
		
		data(){
			
		},
		
		created() {
			console.log(this.reCommentDetail);
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
