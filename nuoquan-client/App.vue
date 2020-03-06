<script>
export default {
	globalData:{
		isNavHome: false //导航栏左侧是否为 home
	},
	onLaunch: function() {
		
	},
	onShow: function(options) {
		let pages = getCurrentPages(); //当前页面栈
		console.log('App Show');
		console.log(options.scene);
		// if(options.path != "pages/tabPages/index" 
		// && options.path != "pages/tabPages/messagelist" 
		// && options.path != "pages/tabPages/mine" 
		// && options.path != "pages/tabPages/votePage"
		// && pages.length == 0){
		// 	console.log(options.path);
		// 	this.globalData.isNavHome = true;
		// }
		if(options.scene == 1007
		|| options.scene == 1008
		|| options.scene == 1011
		|| options.scene == 1012
		|| options.scene == 1013
		|| options.scene == 1014){
			console.log(options.path);
			this.globalData.isNavHome = true;
		}
	},
	onHide: function() {
		console.log('App Hide');
	},
};
</script>

<style>
/* #ifndef APP-PLUS-NVUE */
/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
@import './common/uni.css';
/* css3 */
@import url('/components/animate.css');
/* #endif*/

/*每个页面公共css */
/* @import './common/style/aliicon.css'; */
.container {
	position: relative;
	width: 100%;
	height: auto;
	display: flex;
}

.navigationBar {
	width: 100%;
	position: fixed;
	z-index: 50;
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
