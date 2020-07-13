<template>
	<view class="voteCard">
		<view class="voteProjectName">{{vote.voteContent}}</view>
		<block class="voteOptions" v-for="option in vote.optionList" :key="option.index">
			<view class="options">
				<view class="optionText">{{option.optionContent}}</view>
				<view class="voteNum">{{option.count}}</view>
				<view class="voteRate">{{option.percent}}%</view>
				<view class="progressBar" :style="{width: option.percent * maxPercent +'%'}"></view>
			</view>
		</block>


		<view class="bottomBar">
			<view class="time">{{timeDeal(vote.createDate)}}</view>
			<view class="comment">
				<image src="../../static/icon/comment-alt.png"></image>
				<view>{{vote.commentNum}}</view>
			</view>
			<view class="view">
				<image src="../../static/icon/eye-888888.png"></image>
				<view>{{vote.viewNum}}</view>
			</view>
		</view>
		<!-- 审核状态 -->
		<view class="status pass" style="min-width:49px;background: #09BB07;" v-if="vote.status == 1">{{lang.passCheck}}</view>
		<view class="status pending" style="min-width:36px;background: #3370FF;" v-if="vote.status == 2">{{lang.underCheck}}</view>
		<view class="status fail" style="min-width:49px;background: #888888;" v-if="vote.status == 3">{{lang.failCheck}}</view>

	</view>
</template>

<script>
	export default {
		props: {
			vote: "",
			lang: '',
		},
		data() {
			return {
				maxPercent: '',

			};
		},
		methods: {

		},
		mounted() {
			for (var i = 0; i < this.vote.optionList.length; i++) {
				if(this.vote.optionList[i].percent > this.maxPercent){
					this.maxPercent = 90/this.vote.optionList[i].percent;
				}

			}
		}
	};
</script>

<style>
	.voteCard {
		width: calc(100% - 26px);
		margin: auto;
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		margin-top: 20px;
		position: relative;
	}

	.voteProjectName {
		padding: 20px 16px 12px;
		font-size: 17px;
		font-weight: 500;
		color: rgba(74, 74, 74, 1);

	}

	.options {
		padding-bottom: 17px;
		position: relative;
		margin-left: 24px;
		margin-right: 19px;
	}

	.optionText {
		font-size: 12px;
		line-height: 16px;
		color: rgba(136, 136, 136, 1);
		max-width: calc(100% - 67px);
	}

	.voteRate {
		width: 25px;
		height: 16px;
		font-size: 12px;
		line-height: 16px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		line-height: 16px;
		right: 3px;
		bottom: 17px;
		text-align: right;
	}

	.voteNum {
		width: 30px;
		height: 16px;
		font-size: 12px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		right: 37px;
		bottom: 17px;
		text-align: right;
	}

	.progressBar {
		border: 1px solid rgba(155, 155, 155, 1);
		border-radius: 10px;
		position: absolute;
		right: 0;
		bottom: 9px;
	}




	.bottomBar {
		width: calc(100% - 32px);
		margin: auto;
		height: 10px;
		padding-top: 12px;
		padding-bottom: 12px;
		position: relative;
		color: #9b9b9b;

	}

	.time {
		width: 84px;
		/* 暂时,到  月-日 时:分*/
		height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 0;
		bottom: 12px;
	}

	.comment {
		position: absolute;
		bottom: 12px;
		/* bottom, 对于comment time like view ,数值应等于menu-area的padding-bottom: ; */
		right: 0;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.view {
		position: absolute;
		bottom: 12px;
		right: 39px;
		width: 25px;
		height: 11px;
	}

	.comment image,
	.view image {
		width: 11px;
		height: 11px;
		right: 14px;
		position: absolute;
	}

	.comment view,
	.view view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}

	.status {
		padding: 4px 14px 4px 35px;
		font-size: 12px;
		height: 12px;
		line-height: 12px;
		position: absolute;
		top: -10px;
		left: 16px;
		color: #ffffff;
		border-radius: 25px;
		box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
	}

	.pass::after {
		content: '';
		position: absolute;
		width: 14px;
		height: 12px;
		background: url(../../static/icon/check-pass.png);
		background-size: contain;
		background-repeat: no-repeat;
		top: 4px;
		left: 14px;
	}

	.pending::after {
		content: '';
		position: absolute;
		width: 16px;
		height: 4px;
		background: url(../../static/icon/check-pending.png);
		background-repeat: no-repeat;
		background-size: contain;
		top: 8px;
		left: 14px;
	}

	.fail::after {
		content: '';
		position: absolute;
		width: 12px;
		height: 12px;
		background: url(../../static/icon/check-fail.png);
		background-size: contain;
		background-repeat: no-repeat;
		top: 4px;
		left: 14px;
	}
</style>
