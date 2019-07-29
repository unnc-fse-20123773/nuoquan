<template>
	<view class="comment">
		<view class="fengexian"></view>
		<text class="contentarea">{{ comment[2] }}</text>
		<view class="bottombar">
			<view style="width:70%;display:inline-block;">
				<image src="../../../static/touxiang.jpg" class="touxiang"></image>
				<text class="name">{{ comment[1] }}</text>

				<text class="time">{{ comment[3] }}</text>
			</view>
			<view class="icons">
				<image class="icon" src="../../../../static/icon/comment.svg"></image>
				<text class="icom">{{ comment[4] }}</text>
				<image class="icon" src="../../../static/icon/like.svg"></image>
				<text class="icom">{{ comment[5] }}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		comment: {
			type: Array
		}
	},
	data() {
		return {};
	}
};
</script>

<style scoped>
.comment {
	/* 	background-color:gray;
 */
	border-radius: 20px;
	width: 90%;
	margin: auto;
}
.fengexian {
	height: 1px;
	width: 100%;
	background-color: #d6d6d6;
}
.contentarea {
	background: white;
	font-size: 16px;
	border-radius: 20px;
	margin-top: 20px;
}
.bottombar {
	position: relative;
	border-radius: 20px;
	height: 40px;
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
	font-size: 13px;
	margin-right: 10px;
}
.icons {
	justify-content: flex-end;
	display: inline-flex;
	align-items: center;
	width: 30%;
	
}
.icon {
	width: 35px;
	height: 15px;
	font-size: 2px;
	
}

</style>
