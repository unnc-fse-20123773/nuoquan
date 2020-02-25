<script>
	export default {
		onLaunch: function() {
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},

	}
</script>

<style>
	/* #ifndef APP-PLUS-NVUE */
	/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
	@import './common/uni.css';
	/* #endif*/
	
	/*每个页面公共css */
	/* @import './common/style/aliicon.css'; */
	.container {
		position: relative;
		width: 100%;
		height: auto;
		display: flex;
	}
	
	.navigationBar{
		width: 100%;
		position: fixed;
		z-index: 9999;
		left: 0;
		top: 0;
	}

	.column_center {
		display: flex;
		align-items: center;
	}

	.hor_center {
		display: flex;
		align-items: center;
	}

	.super_center {
		display: flex;
		justify-content: center;
		align-content: center;
		align-items: center;
	}

	.row {
		display: flex;
		flex-direction: row;
	}

	.column {
		display: flex;
		flex-direction: column;
	}

	.font-family {
		font-family: weiruanyahei;
	}
	
</style>
