<template>
	<view class="hotCardFlow">
		<view class="normalCard">
			<view class="titleLine">
				标题标题标题标题标题标题标题标题标题题标题标题
			</view>
			
			<view class="postTime">
				发布于15:30
			</view>
			
			<view class="articleContent">
				<view v-if="wordContent.type == 'wordImage'" class="contentItem">
					<view class="word">
						<text style="margin-right: 13px;">
							正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
						</text>
					</view>
					
					<view class="imageRow"  >
						<image
							:src="wordContent.url"
							mode="aspectFill"
							@load="singleImgeFit" 
							:style="{width:mediaSize.width + 'px',height:mediaSize.height + 'px'}">
						</image>
					</view>
				</view>
				
				<view v-if="wordContent.type == 'image'" class="singleItem">
					<image
						:src="wordContent.url"
						mode="aspectFill"
						@load="singleFit"
						:style="{width:itemSize.width + 'px',height:itemSize.height + 'px'}">
					</image>
				</view>
				
				<view v-if="wordContent.type == 'video'">
					<view class="singleVideo">
						<video
							:src="wordContent.url">
						</video>
					</view>
				</view>
				
				<view v-if="wordContent.type == 'Word'" class="wordItem">
					<text>
						正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文
					</text>
				</view>
			</view>
			
			<view class="idLine">
				
				<view v-if="idType.type == 'special'">
					<image
						:src="idType.url"
						mode="aspectFill"
						style="width: 29px; height: 29px; border-radius: 50%; margin-right: 8px;">
					</image>
					<text>
						这是特别的id
					</text>
				</view>
				
				<view v-else>
					<text>
						这是普普通通id
					</text>
				</view>
				<view class="infoBar">
				
				<view>
					<view v-if="!like" class="notLike" @click="switchStatus">
						<image 
							src="../../static/icon/like.png" 
							style="width: 13px; height: 13px; margin-top: 10px;"
							mode="aspectFit">
						</image>
						<text>
							{{ number }}
						</text>
					</view>	
					<view v-else class="isLike" @click="switchStatus">
						<image 
							src="../../static/icon/liked.png" 
							style="width: 13px; height: 13px; margin-top: 10px;"
							mode="aspectFit">
						</image>
						<text>
							{{ number + 1 }}
						</text>
					</view>
				</view>
				<view class="comment">
					<image 
						src="../../static/icon/comment-alt-353535.png"
						style="width: 13px; height: 13px; margin-top: 10px;"
						mode="aspectFit">
					</image>
					<text>66</text>
				</view>
			</view>
			</view>
			
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				wordContent: {
					type: "wordImage",
					// type: "image",
					// type: "video",
					// type: "Word",
					url: "https://www.checkchack.cn:8443/nottinghome/test10207/article/200813FSTMMP06FW/0.gif",
				},
				mediaSize:{
					width:'0',
					height:'0',
				},
				imageSize:{
					width:'0',
					height:'0',
				},
				idType:{
					type: "special",
					url: "../../static/BG/detailBG.png",
				},
				like: false
			}
		},
		props: {
			number: {
				type: Number,
				default: 99
			}
		},
		methods: {
			singleImgeFit(e) {
				var that = this;
				var height = e.detail.height;
				var width = e.detail.width;
				let info = uni.createSelectorQuery().in(this).select(".imageRow");
				var max_width = 0;
				info.boundingClientRect(function(data) { //data - 各种参数
				console.log(data);
					 max_width = data.width; // 获取元素宽度
					 if(height > (width)){
					 	that.mediaSize.height = max_width * 4/3;
					 	that.mediaSize.width = max_width;
					 }else{
					 	that.mediaSize.height = max_width * 3/4;
					 	that.mediaSize.width = max_width;
					 };
					 console.log(this.mediaSize);
				}).exec();
			},
			singleFit(e) {
				var that = this;
				var height = e.detail.height;
				var width = e.detail.width;
				let info = uni.createSelectorQuery().in(this).select(".singleItem");
				var max_width = 0;
				info.boundingClientRect(function(data) { //data - 各种参数
				console.log(data);
					 max_width = data.width; // 获取元素宽度
					 if(height > (width)){
					 	that.imageSize.height = max_width * 4/3;
					 	that.imageSize.width = max_width;
					 }else{
					 	that.imageSize.height = max_width * 3/4;
					 	that.imageSize.width = max_width;
					 };
					 console.log(this.imageSize);
				}).exec();
			},
			switchStatus(){
				this.like = !this.like;		
				console.log("switched to " + this.like);
			}
		}
	}
</script>

<style>
	.hotCardFlow{
		width: 100%;
		background: rgba(255, 255, 255, 1);
		box-shadow:0px 3px 6px rgba(0, 0, 0, 0.16);
		border-radius: 5px;
		overflow: hidden;
	}
	
	.normalCard{
		width: calc(100% - 28px);
		margin-left: 14px;
		margin-right: 14px;
	}
	
	.titleLine{
		min-height: 24px;
		max-height: 48px;
		margin-top: 22px;
		margin-left: 0;
		font-size: 16px;
		line-height: 24px;
		font-weight: 500;
		-webkit-line-clamp:2;
	}
	
	.postTime{
		margin-top: 12px;
		margin-left: 0;
		height:10px;
		font-size:10px;
		font-weight:400;
		line-height:10px;
		color:rgba(154,154,154,1);
	}
	
	.articleContent{
		display: flex;
		justify-content: left;
		align-items: left;
		margin-top: 12px;
		margin-right: 13px;
		width: 100%;
		flex-wrap: wrap;
		margin-bottom: 24px;
	}
	
	.contentItem{
		display: flex;
		flex-direction: column;
		flex-wrap: wrap;
	}
	
	.word{
		display: flex;
		width: 75%;
		height: auto;
	}
	
	.word text{
		display: -webkit-box; 
		-webkit-box-orient:vertical;
		line-height: 16px;
		max-height: 52px;
		-webkit-line-clamp:3;
		overflow:hidden;
		font-size: 12px;
		color:rgba(74, 74, 74, 1);
		text-overflow: ellipsis;
		word-wrap: break-word;
	}
	
	.wordItem{
		display: flex;
		width: 100%;
		height: auto;
	}
	
	.wordItem text{
		display: -webkit-box; 
		-webkit-box-orient:vertical;
		line-height: 16px;
		max-height: 52px;
		-webkit-line-clamp:3;
		overflow:hidden;
		font-size: 12px;
		color:rgba(74, 74, 74, 1);
		text-overflow: ellipsis;
		word-wrap: break-word;
	}
	
	.imageRow{
		display: flex;
		width: 25%;
		height: 100%;
	}
	
	.singleItem{
		display: flex;
		width: 100%;
	}
	
	.singleVideo{
		width: 100%;
	}
	.imageRow image
	.singleItem image
	.singleVideo video{
		width: 100%;
	}
	
	.idLine{
		position: relative;
		height: 29px;
		width: 100%;
		margin-bottom: 21px;
	}
	
	.idLine text{
		position: absolute;
		top: 10px;
		height: 12px;
		font-size: 12px;
		font-weight: 500;
		line-height: 12px;
		color: rgba(74,74,74,1);
	}
	
	.infoBar{
		top: 0;
		position: absolute;
		right: 61px;
	}
	
	.notLike
	.isLike{
		height: 13px;
		position: absolute;
		display: flex;
		left: 0;
	}
	
	.notLike text{
		position: absolute;
		left: 17px;
		top: 11px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		line-height: 11px;
		color:rgba(154,154,154,1);
	}
	
	.isLike text{
		position: absolute;
		left: 17px;
		top: 11px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		line-height: 11px;
		color: rgba(252,192,65,1);
	}
	
	.comment{
		top: 0;
		position: absolute;
		left: 46px;
	}
	
	.comment text{
		position: absolute;
		left: 17px;
		top: 11px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		line-height: 11px;
		color:rgba(154,154,154,1);
	}
</style>
