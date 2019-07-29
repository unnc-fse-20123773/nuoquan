<template>
	<view class="signinpage">
		<view class="spacearea"></view>
		<image src="../../static/touxiang.jpg" class="touxiang"></image>
		<image class="fengexian" src="../../static/icon/signinline.png"></image>
		<view class="mailbox">
			<input class="prefix" />
			<text class="suffix">@nottingham.edu.cn</text>
		</view>
		<view class="verifycode">
			<input class="code" />
			<button>发送验证码</button>
		</view>
		<view class="binding">确认绑定</view>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	methods: {}
};
</script>

<style>
.signinpage {
	width: 100%;
	margin: auto;
}
.spacearea {
	height: 150px;
	display: block;
	width: 750upx;
}
.touxiang {
	height: 100px;
	width: 100px;
	border-radius: 50px;
	display: block;
	margin: auto;
}
.fengexian {
	margin-top: 50px;
	height: 50px;
}
.mailbox {
	display: block;
	margin: auto;
	width:210px;
}
.prefix {
	width: 60px;
	display: inline-block;
}
.suffix {
	font-size: 12px;
	color: #d6d6d6;
	display: inline-block;
	width:150px;
}
.verifycode {
	display: block;
	margin: auto;
	width:120px;
	font-size: 14px;
}
.code{
	display: inline-block;
	width:80px;
}
.verifycode button{
	width:120px;
	font-size: 14px;
	
	background: RGB(253, 217, 108);
}
</style>
