<template>
	<view class="submitVoteMain">
		<!-- 当失去焦点时，将输入内容存入voteTitle -->
		<view style="position: relative;margin-top: 20px;">
			<input class="title" v-model="voteTitle" placeholder="标题" maxlength="20" placeholder-class="title-placeholder">
			<view class="titleTextLeft">{{20 - voteTitle.length}}</view>
		</view>

		<button class="addContentButton" @tap="showContent()">添加正文</button>

		<!-- 正文内容 -->
		<view style="position: relative;">
			<textarea class="content" v-model="voteContent" maxlength="140" :auto-height="true" :show-confirm-bar="false"></textarea>
			<view style="position: absolute;bottom: 8px;right:8px;font-size: 11px;color:#888888;">{{140 - voteContent.length}}</view>
			<image src="../../static/icon/emoji.png" style="position: absolute;left:12px;top:8px;width:20px;height:20px;"></image>
		</view>
		<view class="picturearea">
			<block v-for="(image,index) in imageList" :key="index">
				<view style="position: relative;">
					<!-- todo 预览图片缩放 -->
					<image :src="image" :data-src="image" @tap="previewImage" mode="aspectFill"></image>
					<view style="width:15px;height: 15px;font-size: 10px;line-height: 10px;border-bottom-left-radius: 3px;background: rgba(166, 169, 168,0.3);color:#FFFFFF;position: absolute;top:6px;right:0;text-align: center;"
					 @click="deleteImg(index)">✕</view>
				</view>
			</block>
			<view v-show="imageList.length < 9" id="clickToChooseImage" class="addPic" @click="chooseImg">+</view>
			<view v-if="imageList.length==1||imageList.length==4||imageList.length==7" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
		</view>
		
		
		
		
		
		
		
		<view class="menu">
		<mypicker class="major-pick-style" :mode="pickerMode" :range="pickerRange" :value="pickerValue" @change="pickerChange"></mypicker>

			<view style="display:inline-block;height:12px;font-size:12px;font-weight:400;color:rgba(154,154,154,1);">天后截止</view>
			<view style="display:inline-block;height:12px;font-size:12px;font-weight:400;color:rgba(154,154,154,1);">最多是个选项，每个选项限制20字</view>
		</view>
		<view class="optionArea">
			<input v-for="(i,index) in voteOptions" :key="index" v-model="voteOptions[index]" placeholder="选项内容" class="optionItem" />
		</view>
		<view class="addOptionButton" @tap="addOptionFunc()">添加选项</view>
		<view class="submitVoteButton">提交</view>


@tapBackground
	</view>
</template>

<script>
	import mypicker from '../../components/mypicker.vue';

	export default {
		data() {
			return {
				showContengEdit: false,

				voteTitle: "",
				voteContent: "",
				votePeriod: "",
				voteOptions: [''],
				
				pickerMode:"submitVote",
				pickerRange:['1','2','3','4','5','6','7','8','9','10'],
				pickerValue:'1',
				
				//图片相关？DY看一下不需要的变量删掉。
				imageList: [],
				sourceTypeIndex: 2,
				sourceType: ['拍照', '相册', '拍照或相册'],
				sizeTypeIndex: 0,
				sizeType: ['压缩', '原图', '压缩或原图'],
				countIndex: 8,
				count: [1, 2, 3, 4, 5, 6, 7, 8, 9],
				windowHeight: 0,

			}
		},
		components: {
			mypicker,
		},
		methods: {
			showContent() {
				this.showContengEdit = true;
			},
			addOptionFunc(){
				if(this.voteOptions.length < 10){
				this.voteOptions.push("");
					
				}else{
					uni.showToast({
						duration:1000,
						title:"不能再多啦~嘻嘻~",
					})
				}
			},
pickerChange(res){
	console.log(res);
	this.pickerValue = res.newPickerValue;
	
}
		}
	}
</script>

<style>
	page {
		height: 100%;
	}

	.submitVoteMain {
		width: 698upx;
		margin: auto;
	}

	.title {
		width: calc(698upx - 36px);
		height: 38px;
		border: 2px solid #FCC041;
		border-radius: 8px;
		padding-left: 12px;
		padding-right: 24px;
	}

	.title-placeholder {
		height: 14px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(199, 199, 199, 1);
		letter-spacing: 17px;
	}

	.titleTextLeft {
		position: absolute;
		right: 8px;
		top: 15px;
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		color: rgba(199, 199, 199, 1);
	}

	.addContentButton {
		display: inline-block;
		width: 100px;
		margin: 16px 0 0 0;
		padding: 6px 32px 6px 10px;
		height: 26px;
		font-size: 14px;
		line-height: 14px;
		color: #FFFFFF;
		background: linear-gradient(90deg, rgba(83, 224, 219, 1) 0%, rgba(72, 177, 233, 1) 100%);
		border-radius: 8px;
	}

	.addContentButton::before {
		width: 13px;
		content: "";
		background: url(../../static/icon/edit_file.png);
		background-size: 13px 13px;
		background-repeat: no-repeat;
		position: absolute;
		right: 13px;
		height: 13px;
	}

	.content {
		min-height: 51px;
		max-height: 300px;
		margin-top: 13px;
		width: calc(100% - 12px);
		overflow: scroll;
		padding: 36px 4px 24px;

		border: 2px solid rgba(252, 192, 65, 1);
		border-radius: 8px;
		font-size: 14px;
	}
	.picturearea {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		margin-top: 10px;
	}
	
	.picturearea image {
		width: 190upx;
		height: 190upx;
		margin: 6px 0;
	}
	
	.addPic {
		width: 178upx;
		height: 178upx;
		line-height: 160upx;
		margin: 6px 0;
		text-align: center;
		vertical-align: middle;
		color: #888888;
		font-size: 50px;
		font-weight: 200;
		background: #ECECEC;
	}
	

	.optionItem {
		margin-top:12px;
		width: 634upx;
		height: 14px;
		font-size: 14px;
		line-height: 14px;
		padding: 24upx 31upx;
		border: 1upx solid rgba(188, 188, 188, 1);
		border-radius: 20px;
		min-height: auto;
	}

	.addOptionButton {
		margin-top: 13px;
		width: 698upx;
		height: 14px;
		line-height: 14px;
		font-size: 14px;
		line-height: 14px;
		color: rgba(255, 255, 255, 1);
		background: rgba(72, 177, 233, 1);
		border-radius: 20px;
		padding: 24upx 0;
		text-align: center;
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
	}

	.submitVoteButton {
		margin-top: 13px;
		width: 698upx;
		height: 14px;
		line-height: 14px;
		font-size: 14px;
		line-height: 14px;
		color: rgba(255, 255, 255, 1);
		background: rgba(252, 192, 65, 1);
		border-radius: 20px;
		padding: 24upx 0;
		text-align: center;
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);

	}
</style>
