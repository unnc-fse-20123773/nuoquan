<template name="tagSelect">
	<view class="tagsArea" v-if="editingTag">
		<text>所有标签</text>
		<view class="tag" v-for="(item, index) in tagList" 
		:key="index" 
		:style="{ background: tagColorList[index] }" 
		@tap="sendselectedTag(item) ">{{ item }}</view>
	</view>
</template>

<script>
export default {
	props: {
		tagList: []
	},
	data() {
		return {
			tagColorList: [], // 储存每个备选tag的颜色
			selectedTags: [],
			selectedTagColorList: [] // 储存每个已选tag的颜色
		};
	},
	created() {
		// 随机生成颜色
		var tagColors = this.tagColors;
		for (var i = 0; i < 6; i++) {
			var random_1 = Math.floor(Math.random() * tagColors.length);
			// 0~tagColors.length-1
			this.tagColorList.push(tagColors[random_1]);
		}
	},

	methods: {
		tagManager(a) {
			for (var i = 0; i < a.length; i++) {
				this.tagList[i] = a[i].tag;
			}

			// 随机生成颜色
			var tagColors = this.tagColors;
			for (var i = 0; i < 6; i++) {
				var random_1 = Math.floor(Math.random() * tagColors.length);
				var random_2 = Math.floor(Math.random() * tagColors.length);
				// 0~tagColors.length-1
				this.tagColorList.push(tagColors[random_1]);
				this.selectedTagColorList.push(tagColors[random_2]);
			}
		},
		
		sendselectedTag(item) {
			var a=this.selectedTags.indexOf(item);
			
			if(a == -1){
				this.selectedTags.push(item);
				this.$emit('send' , this.selectedTags);
			}else{
				uni.showToast({
					icon: 'none',
					title: '已经添加～',
					duration: 200
				});
			}
		},
	}
};
</script>

<style>
.tagsArea {
	margin-top: 13px;
	padding-bottom: 16px;
	vertical-align: bottom;
	min-height: 130px;
	max-height: 192px;
	overflow: hidden;
	position: relative;
	background: url(../static/BG/submit_BG.png);
	background-repeat: no-repeat;
	background-size: 100% 100%;
	border-radius: 10px;
}

.tagsArea text {
	display: block;
	height: 12px;
	font-size: 12px;
	color: rgba(155, 155, 155, 1);
	padding-top: 16px;
	padding-left: 12px;
	padding-bottom: 8px;
}

.tag {
	display: inline-block;
	vertical-align: bottom;
	color: #ffffff;
	font-size: 14px;
	line-height: 26px;
	height: 26px;

	position: relative;
	margin-left: 8px;
	margin-top: 6px;
	padding-right: 12px;
	padding-left: 12px;
	/* 用于解决空格换行问题 */
	white-space: nowrap;

	border-radius: 20px;
}
</style>
