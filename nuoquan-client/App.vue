<script>
export default {
	globalData: {
		isNavHome: false //导航栏左侧是否为 home
	},
	onLaunch: function() {},
	onShow: function(options) {
		console.log('App Show');
		console.log('scene=' + options.scene);
		var sceneList = [1007, 1008, 1011, 1012, 1013, 1014, 1047, 1048, 1049]
		for (let scene of sceneList){
			if(options.scene == scene){
				console.log('set isNavHome true');
				this.globalData.isNavHome = true;
				break;
			}
		}
		// if (options.scene != null && (options.scene == (1007 || 1008 || 1011 || 
		// 		1012 || 1013 || 1014 || 1047 || 1048 || 1049))) {
		// 	console.log('get scene=' + options.scene);
		// 	this.globalData.isNavHome = true;
		// }
		// let pages = getCurrentPages(); //当前页面栈
		// if(options.path != "pages/tabPages/index"
		// && options.path != "pages/tabPages/messagelist"
		// && options.path != "pages/tabPages/mine"
		// && options.path != "pages/tabPages/votePage"
		// && pages.length > 1){
		// 	console.log(options.path);
		// 	this.globalData.isNavHome = true;
		// }
	},
	onHide: function() {
		console.log('App Hide');
	}
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
