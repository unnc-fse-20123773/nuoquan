<template>
	<view>
			
			<view class="tabs">
				<view class="itsFocus ">
					他关注的
				</view>
				<view class="focusER">
					关注他的
				</view>
				<view class="devidingLine"></view>
	
			</view>
			<view class="list">
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
				<view class="listItem">
					<image src="../../static/touxiang.jpg" class="touxiang"></image>
					<view class="name">陈仅仅11号</view>
					<view class="goFocus">关注</view>
					<view class="listItemBottomLine"></view>
				</view>
			</view>
		</view>
	</template>
	


<script>
	export default {
		data() {
			return {
			}
		},
		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "粉丝列表"
			});
		},
		methods: {
		}
	}
</script>

<style>
	page {
		background-color: #F3F3F3;
	}
</style>
<style scoped>
		.tabs {
		margin: 26upx 0 0;
	}
	.focusER,
	.itsFocus {
		color: #353535;
		font-size: 15px;
		display: inline-block;
		width: 50%;
		margin: auto;
		text-align: center;
	}
	.devidingLine {
		border-top: solid 3px #FDD453;
		width: 258upx;
		margin-left: 58.5upx;
	}
	.listItem {
		position: relative;
		height: 45px;
		display: flex;
		align-items: center;
	}
	.touxiang {
		display: inline-flex;
		margin-left: 13px;
		width: 33px;
		height: 33px;
		border-radius: 33px;
		vertical-align: middle;
		line-height: 45px;
	}
	.name {
		display: inline-flex;
		color: #353535;
		font-size: 13px;
		margin-left: 13px;
		vertical-align: middle;
	}
	.goFocus {
		border: solid 1px #ECC138;
		border-radius: 5px;
		font-size: 13px;
		color: #ECC138;
		position: absolute;
		right: 26px;
		height: 26px;
		line-height: 26px;
		width: 51px;
		top: 9px;
		vertical-align: middle;
		text-align: center;
	}
	.listItemBottomLine {
		border-top: solid 1px #DCDCDC;
		width: 590upx;
		position: absolute;
		bottom: 0;
		left: 108upx;
	}
</style>