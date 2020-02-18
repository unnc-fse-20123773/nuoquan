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
		<view class="menu">
<mypicker class="major-pick-style" :dataList="majors" :value="majorPickerVal" @change="majorChange"
						 @tapBackground="majorPickerChanger()"></mypicker>			<view>天后截止</view>
			<view>最多是个选项，每个选项限制20字</view>
		</view>

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
				voteOptions: [],
				votePeriod: "",
			}
		},
		components: {
			mypicker,
		},
		methods: {
			showContent() {
				this.showContengEdit = true;
			},
			majorChange: function(e) {
				var major = this.majors[e];
				this.major = major;
				// 给组件赋值回去，更改起始值
				this.majorPickerVal[0] = e;
			},
			degreePickerChanger: function() {
				if (this.degreePicker == false) {
					this.degreePicker = true;
				} else {
					this.degreePicker = false;
				}
				// console.log(this.degreePicker);
			},
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
</style>
