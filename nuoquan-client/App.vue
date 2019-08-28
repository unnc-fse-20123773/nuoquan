<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
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
	/*每个页面公共css */
	@import './common/style/aliicon.css';

	.container {
		position: relative;
		width: 100%;
		height: auto;
		display: flex;
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
