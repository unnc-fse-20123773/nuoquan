<template>
	<view style="width: 100%; height: 100%; background: linear-gradient(#f89d4d, #ffc95a);">
		<!-- 导航栏 -->
		<view class="navigatorBar_votePage super_center" :style="{ height: navigationBarHeight + 'px' }">NavigatorBar</view>
		<!-- 左侧按钮 -->
		<view class="tapLeft super_center" :style="{ top: navigationBarHeight + 46 + 'px' }">
			<image src="../../static/icon/angle-left-gradient.png" mode="aspectFit"></image>
		</view>
		<!-- 右侧按钮 -->
		<view class="tapRight super_center" :style="{ top: navigationBarHeight + 46 + 'px' }">
			<image src="../../static/icon/angle-right-gradient.png" mode="aspectFit"></image>
		</view>		
		
		
		<!-- 投票卡片 -->
		<swiper class="scroll-box" :indicator-dots="false" :autoplay="false" :current="currentVoteIndex" 
		@change="onSwiperChange">
			<swiper-item v-for="(item, index) in showList" :key="index" class="card-box" id="card_{index}">
				<scroll-view class="card" scroll-y="true" show-scrollbar="false" :style="{height: voteCardHeight + 'px'}">
					<!-- 标题行 -->
					<view @click="goToDetail()">
						<view class="title">{{item.voteTitle}}</view>
					</view>
					<!-- 用户信息行 -->
					<view class="userLine hor_center">
						<image src="../../static/icon/user_d4d4d4.png" class="touxiang"></image>
						<view class="name">{{item.nickname}}</view>
						<view class="time">{{timeDeal(item.createDate)}}</view>
					</view>
					<!-- 内容 -->
					<view class="briefarticleCard" @click="goToDetail()">
						{{item.voteContent}}
					</view>
					<!--图片区域-->
					<view>
						<!-- 单图显示 -->
						<view v-if="item.imgList.length == 1" class="detailpics" style="width:91.8%;margin-left: 4.1%; max-height: 400upx; display: flex;">
							<!-- 高 > 宽 -->
							<view v-if="singleImgState == 0">
								<image v-for="(i, imageIndex) in item.imgList" :key="imageIndex"
								 :src="serverUrl + i.imagePath" mode="aspectFill"
								 :style="{ width: singleImgWidth + 'rpx', height: singleImgHeight + 'rpx' }" 
								 @tap="previewImage(index, imageIndex)" 
								 @longpress="aboutImg(index, imageIndex)"
								 @load="singleImgeFit"
								 ></image>
							</view>
							<!-- 宽 > 高 -->
							<view v-else style="width: 100%;">
								<image v-for="(i, imageIndex) in item.imgList" :key="imageIndex"
								 :src="serverUrl + i.imagePath" mode="aspectFill"
								 :style="{ width: singleImgWidth + 'rpx', height: singleImgHeight + 'rpx' }" 
								 @tap="previewImage(index, imageIndex)" 
								 @longpress="aboutImg(index, imageIndex)"
								 @load="singleImgeFit"
								 ></image>
							</view>
						</view>
						<!-- 4宫格 -->
						<view v-else-if="item.imgList.length == 4" class="detailpics" style="max-width: 400upx;margin-left: 0;">
							<image class="detailpic" v-for="(i, imageIndex) in item.imgList" :key="imageIndex"
							 :src="serverUrl + i.imagePath"
							 mode="aspectFill" 
							 @tap="previewImage(index,imageIndex)" 
							 @longpress="aboutImg(imageIndex)"
							 ></image>
						</view>
						
						<!-- 其他数量 -->
						<view v-else class="detailpics">
							<image class="detailpic" v-for="(i, imageIndex) in item.imgList" :key="imageIndex" :src="serverUrl + i.imagePath"
							 mode="aspectFill" @tap="previewImage(index,imageIndex)" @longpress="aboutImg(imageIndex)"></image>
							<view v-if="item.imgList.length == 2 || item.length == 5 || item.length == 8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
						</view>
					</view>
					<!-- 投票选项 -->
					<!-- 确定投票前的选项展示 -->
					<view v-if="finishVote[index] == false" class="voteCard">
						<!-- 双层嵌套，可适应以后扩展选项内容 -->
						<view v-for="(option, index2) in item.optionList" :key="index2"  :class="[option.id != selectedOptionId ? 'oneVoteCard' : 'oneVoteCard_chosen']" @click="switchChoose(option.id, index)">
							<text :class="[option.id != selectedOptionId ? 'oneVote_text' : 'oneVote_text_chosen']">{{(option.optionContent)}}</text>
						</view>
					</view>
					<!-- 确定投票后的选项展示 -->
					<view v-else class="voteCard">
						<view v-for="(result, index3) in item.optionList" :key="index3">
							<view style="margin-top: 12px;width:100%;height:37px;">
								<!-- 这个地方请加上判断，准备狂写三元运算符吧。。。css语法有问题，太过冗长，下个版本我会简化 Guetta -->
								<!-- 非用户选项结果 -->
								<view v-if="result.id != selectedOptionId" class="oneResult">
									<view class="resultTextCard">
										<view class="voteContent">{{result.optionContent}}</view>
										<view class="voteresultNum">
											<text>{{result.count}}</text>
											<!-- <text>{{result.barWidth}}</text> -->
											<view class="percentLine">
												<view class="percentNum">{{reserveTwoDecimal(result.percent * 100)}}</view>
												<text>%</text>
											</view>
										</view>
									</view>
									<view class="resultBarCard">
										<view class="resultBar_border"></view>
										<view class="resultBar_bgclr" :style="{ width: result.barWidth + '%' }"></view>
									</view>
								</view>
								<!-- 用户选项结果 -->
								<view v-else class="oneResult">
									<view class="resultTextCard">
										<view class="voteContent_chosen">{{result.optionContent}}</view>
										<view class="voteresultNum_chosen">
											<text>{{result.count}}</text>
											<!-- <text>{{result.barWidth}}</text> -->
											<view class="percentLine_chosen">
												<view class="percentNum_chosen">{{reserveTwoDecimal(result.percent * 100)}}</view>
												<text>%</text>
											</view>
										</view>
									</view>
									<view class="resultBarCard">
										<view class="resultBar_border_chosen"></view>
										<view class="resultBar_bgclr_chosen" :style="{ width:  result.barWidth + '%' }"></view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 剩余时间&参与人数 -->
					<view class="voteInfoLine">
						<!-- 时间 -->
						<view class="timeLine">
							<view class="timeleft">{{reTimeDeal(item.expiryDate)}}</view>
							<text>结束</text>
						</view>
						<view class="numbersBar">
							<!-- 参与人数 -->
							<view class="participants">
								<view class="participants_Num">{{item.sumVote}}</view>
								<text>参与</text>
							</view>
							<!-- 评论人数 -->
							<view class="commentusers">
								<view class="commentusers_Num">{{item.commentNum}}</view>
								<text>评论</text>
							</view>
						</view>
					</view>
					<!-- 确认投票 -->
					<view v-if="finishVote[index] == false" class="alertandconfirm super_center">
						<text v-if="ischosen[index] == false">完成投票后才可查看评论哦</text>
						<button v-else class="confirmButton_votePage super_center" @click="confirmVote(item.id, index)">确认投票</button>
					</view>
					<votecomment :voteid = '  something         '></votecomment>
				</scroll-view>
			</swiper-item>
		</swiper>
		
		<tab-bar :current="1"></tab-bar>
	</view>
</template>

<script>
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue";
	import votecomment from "@/components/votecomments.vue";
	
	const DEFAULT_PAGE = 0;	
	var loadVoteFlag = false;
	var loadMoreFlag = false; // 确保往前翻不会再次触发loadMore方法
	export default {
		data() {
			return {
				serverUrl: this.$serverUrl,
				list: ['Javascript', 'Typescript', 'Java', 'PHP', 'Go'],
				voteCardHeight: 0, //单个投票卡片高度
				ischosen: [], //判断选项是否选中
				ischosenFlag: '',
				menuButtonInfo: '',
				navigationBarHeight: 0, //导航栏高度
				ifShowComment: false, //判断是否展示评论区域
				finishVote: [], //判断是否展示投票结果条形图
				persentBarWidth: [0,0,0,0], //条形图宽度
				widthTarget: [], // 进度条的宽度目标值
				
				showList: ['1','2'],
				userInfo: '',
				
				totalPage: 1,
				currentPage: 1,
				
				// singleImgState: ,
				singleImgHeight: 0,
				singleImgWidth: 0,
				heightWidthRate: 0,
				
				imgList: [],
				
				currentVoteIndex: 0,
				
				selectedOptionId: '', // 选择的选项id
				afterSelectedResult: [], // 确认选择后刷新单个产生的单个vote的所有信息
				afterSelectedOptionList: [], // 确认选择后刷新单个产生的单个vote的选项信息
				
				loadedMoreIndex: [] // 用来储存已经触发过loadMore方法的投票的index.
			};
		},
		
		components:{
			uniNavBar,
			tabBar,
			votecomment,
		},
		
		onLoad: function() {
			console.log("还没投票时,selectedOptionId= " + this.selectedOptionId);
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../signin/signin'
				})
				return;
			} else {
				this.userInfo = userInfo;
			}
			
			this.getScreenSize(); //获取手机型号
			
			this.showVotes(this.currentPage);
			//获取导航栏高度
			var info = this.menuButtonInfo;
			var height;
			info = uni.getMenuButtonBoundingClientRect();
			height = info.bottom;
			this.navigationBarHeight = height;
			console.log('导航栏高度=' + this.navigationBarHeight);
			this.calculateHeight();
		},

		onShow() {
			
		},

		methods: {
			singleImgeFit(e) {
				var height = e.detail.height;
				var width = e.detail.width;
				var rate;
				if (height >= width) {
					this.singleImgState = 0;
					this.singleImgHeight = 360;
					rate = width / height;
					this.heightWidthRate = rate;
					this.singleImgWidth = 360 * rate;
				} else {
					this.singleImgState = 1;
					this.singleImgWidth = 360;
					rate = height / width;
					this.heightWidthRate = rate;
					this.singleImgHeight = 360 * rate;

				}
			},
			
			previewImage: function(voteIndex, imageIndex) {
				// var imgIndex = index;
				// console.log(res)
				// 获取全部图片路径
				var imgList = this.showList[voteIndex].imgList;
				var arr = [];
				var path;
				for (var i = 0; i < imgList.length; i++) {
					// console.log(imgList[i].imagePath);
					path = this.$serverUrl + imgList[i].imagePath;
					arr = arr.concat(path);
				}
				// console.log(arr);
				uni.previewImage({
					current: imageIndex,
					urls: arr
				});
				arr = [];
			},
			
			calculateHeight() {
				var that = this;
				var pageHeight;
				uni.getSystemInfo({
					success: function(res) {
						pageHeight = res.windowHeight;
						that.voteCardHeight = pageHeight - (99 + that.navigationBarHeight);
						// console.log(that.voteCardHeight);
					},
				})
				// console.log(this.voteCardHeight);
			},

			switchChoose(selectedOptionId, voteIndex) {
				
				// console.log("传进来的选项id= "+ selectedOptionId);
				var that = this;
				
				// 如果记录的index和传进来的voteIndex相同, 则继续操作
				// 如果记录的index和传进来的voteIndex不同, 则将所有inchosen变为false再进行操作
				if(that.ischosenFlag == voteIndex){
					if (selectedOptionId == that.selectedOptionId){
						that.selectedOptionId = '';
						that.ischosen[voteIndex] = false;
					} else {
						this.ischosen[voteIndex] = true;
						that.selectedOptionId = selectedOptionId;
						that.currentVoteIndex = voteIndex;
					}
				} else {
					// 不相等时
					for(var i = 0; i < that.ischosen.length; i++){
						that.ischosen[i] = false;
					}
					// console.log("改变页面后"+that.ischosen);
					if (selectedOptionId == that.selectedOptionId){
						that.selectedOptionId = '';
						that.ischosen[voteIndex] = false;
					} else {
						this.ischosen[voteIndex] = true;
						that.selectedOptionId = selectedOptionId;
						that.currentVoteIndex = voteIndex;
					}
				}
				// 记录是在第几个vote进行操作
				that.ischosenFlag = voteIndex;
				
				console.log("赋值后的选项id= "+ that.selectedOptionId);
				// console.log(that.currentVoteIndex);
				// console.log(that.ischosen);
			},

			/**
			 * @param {Object} voteId 投票的id
			 * @param {Object} voteIndex 投票的index, 用来控制下面的确认投票键
			 */
			confirmVote(voteId, voteIndex) {
				this.ifShowComment = !this.ifShowComment;
				this.finishVote[voteIndex] = !this.finishVote[voteIndex];
				// console.log('确认投票');
				
				
				var that = this;
				uni.request({
					url: that.$serverUrl + '/vote/selectOption',
					method: 'POST',
						
					data:{
						optionId: that.selectedOptionId,
						userId: that.userInfo.id,
						voteId: voteId
					},
					header: {
						'content-type': 'application/json'
					},
					success: res => {
						// console.log('123');
						// console.log(that.selectedOptionId);
						// console.log(voteId);
						// console.log(res);
						// 得到投票后的数值, 根据新的OptionList的id和that.selectedOptionId来判断放在哪一个区域
						// 如果两个相等, 放在用户选择区, 其余放在非用户选择区
						that.afterSelectedResult = res.data.data.rows[0];
						that.showList[that.currentVoteIndex] = that.afterSelectedResult;
						// console.log(that.afterSelectedResult);
						that.afterSelectedOptionList = that.afterSelectedResult.optionList;
						// console.log(that.afterSelectedOptionList);
						// that.ischosen[voteIndex] = true;
						console.log(that.showList[voteIndex]);
						
						// 进度条伸长
						for (let option of that.showList[voteIndex].optionList){
							this.onePersentBarGrow(option);
						}
						
						this.voteCardHeight = 3000; //确认投票后or该投票状态为已投，改变卡片高度
					}
				})
			},
			
			votedResult(voteIndex){
				for (let option of this.showList[voteIndex].optionList){
					this.onePersentBarGrow(option);
				}
				this.voteCardHeight = 3000; //确认投票后or该投票状态为已投，改变卡片高度
			},

			/**
			 * 为选项添加进度条的长度
			 * @param {Object} option 选项
			 * @description 监听对象 --> 在地址上进行更改(类似指针), 避免多线程操作一个地址时: 获取数据失败/混乱等问题
			 */
			onePersentBarGrow(option){
				var widthTarget = (this.reserveTwoDecimal(option.percent * 100));
				this.$set(option, 'barWidth', 0); // 为选项添加barWidth属性
				var timer = setInterval(function() {
					//设置计时器
					if ( option.barWidth >= widthTarget) {
						//在 persentBarWidth 为目标值时清空计时器，暂以100代替						// console.log(option);
						console.log(option);
						clearInterval(timer);
					} else {
						option.barWidth += 0.5;
					}
				}, 6);
			},

			showVotes: function(page) {
				if (loadVoteFlag) {
					return;
				}
				loadVoteFlag = true;

				uni.showLoading({
					title: '加载中...'
				});
				setTimeout(() => {
					if (loadVoteFlag) {
						loadVoteFlag = false; //解锁
						uni.hideLoading();
						uni.showToast({
							title: '网络未知错误',
							icon: 'none',
							duration: 1000
						});
					}
				}, 5000) // 延时5s timeout

				var that = this;
				// console.log(1);
				uni.request({
					url: that.$serverUrl + '/vote/queryAllVotes',
					method: 'POST',
					data: {
						page: page,
						userId: that.userInfo.id
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						setTimeout(() => {
							// 延时加载
							uni.hideLoading();
							loadVoteFlag = false;
							// console.log(res);
							// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
							if (page == 1) {
								that.showList = [];
							}
							// 在原showList后面嫁接新的数据
							var newVoteList = res.data.data.rows;
							var oldVoteList = that.showList;
							that.showList = oldVoteList.concat(newVoteList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
							
							console.log(res.data.data.rows);
							if (page == 1){
								that.ischosen = [];
								that.finishVote = [];
							}
							var tempIsChosen = [];
							var tempFinishVote = [];
							for(var i = 0; i < res.data.data.rows.length; i++){
								tempIsChosen[i] = false;
								tempFinishVote[i] = that.showList[i].isUserVoted;
							}
							that.ischosen = that.ischosen.concat(tempIsChosen);
							that.finishVote = that.finishVote.concat(tempFinishVote);
							// console.log("每个投票是否被选择:" + that.ischosen);
							
							for(var index = 0; index < that.showList.length; index++){
								if (that.showList[index].isUserVoted == true){
									console.log("Voted: "+ index);
									that.votedResult(index);
								}
							}
						}, 300);
					},
					fail: res => {
						uni.hideLoading();
						loadVoteFlag = false;
						
						console.log('index unirequest fail');
						console.log(res);
					}
				})
			},
			
			loadMore: function(){
				var currentPage = this.currentPage;
				var totalPage = this.totalPage;
				if (currentPage == totalPage){
					uni.showToast({
						title: '没有更多投票了',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					this.showVotes(page);
				}
			},
			
			refreshVote: function() {
				uni.showNavigationBarLoading();
				this.showVotes(1);
				uni.hideNavigationBarLoading();
			},
			
			/**
			 * 监听投票滑动, 当滑到新的一个投票, 返回其index
			 * @param {Object} e change事件的默认返回值, 其中包含swiper的index
			 */
			onSwiperChange: function(e){
				let index = e.target.current || e.detail.current;
				console.log("VoteIndex: "+index);
				
				// this.votedResult(index);
				
				for(let ifHadLoadMore of this.loadedMoreIndex){
					if(index == ifHadLoadMore){
						return;
					}
				}

				this.loadMoreFlag = false;

				// console.log(index);
				// console.log(this.loadedMoreIndex);
				var remainder = (index+1) % 10;
				if(index == 0){
					// 刷新投票
					this.refreshVote();
				} else if (remainder == 0 || (index+1) == this.showList.length){
					if (!this.loadMoreFlag){
						this.loadMore();
						this.loadedMoreIndex = this.loadedMoreIndex.concat(index);
						this.loadMoreFlag = true;
					}
				}
			},
		}
	};
</script>

<style>
	page {
		height: 100%;
		width: 100%;
		overflow: hidden;
	}

	.navigatorBar_votePage {
		width: 100%;
		background-color: white;
	}

	.tapLeft {
		position: fixed;
		left: 0;
		z-index: 10;
		width: 6.4%;
		max-width: 24px;
		height: 448px;
		background: linear-gradient(90deg, rgba(243, 243, 243, 1) 0%, rgba(255, 255, 255, 1) 100%);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 0px 8px 8px 0px;
	}

	.tapRight {
		position: fixed;
		right: 0;
		z-index: 10;
		width: 6.4%;
		max-width: 24px;
		height: 448px;
		background: linear-gradient(90deg, rgba(243, 243, 243, 1) 0%, rgba(255, 255, 255, 1) 100%);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 8px 0px 0px 8px;
	}

	.tapLeft image {
		width: 5px;
		height: 8px;
		opacity: 1;
	}

	.tapRight image {
		width: 5px;
		height: 8px;
		opacity: 1;
	}

	.scroll-box {
		white-space: nowrap;
		height: 90vh;
		width: 100%;
	}

	.card-box {
		display: inline-block;
	}

	.card {
		margin: 16px 9.6%;
		width: 80.8%;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 16px;
	}

	.card text {
		height: 1000px;
	}

	.title {
		width: 93.12%;
		font-size: 17px;
		font-family: Source Han Sans CN;
		line-height: 21px;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
		font-weight: bold;
		margin: 18px 3.44% 0 3.44%;
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
	}

	.userLine {
		position: relative;
		width: 100%;
		height: 24px;
		margin-top: 12px;
	}

	.touxiang {
		position: absolute;
		left: 3.44%;
		border-radius: 30px;
		width: 24px;
		height: 24px;
		vertical-align: middle;
	}

	/* .touxiang::after{
		content: "";
		position: absolute;
		height:30px;
		width:30px;
		left:-5px;
		top:0;
	} */

	.name {
		position: absolute;
		left: calc(3.44% + 32px);
		max-width: 24%;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 23px;
		color: rgba(155, 155, 155, 1);
		opacity: 1;
	}

	.time {
		position: absolute;
		right: 3.44%;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 23px;
		color: rgba(155, 155, 155, 1);
		opacity: 1;
	}

	.briefarticleCard {
		width: 93.12%;
		font-size: 14px;
		font-family: Source Han Sans CN;
		line-height: 16px;
		opacity: 1;
		font-weight: 400;
		margin: 12px 3.44% 8px 3.44%;
		color: rgba(53, 53, 53, 1);
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 15;
		overflow: hidden;
	}

	.detailpics {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		align-items: center;
		flex-wrap: wrap;
		width: 91.8%;
		margin-left: 4.1%;
		margin: 0 auto;
		margin-bottom: 9px;
	}

	.detailpic {
		/* 			width: calc((100% - 12px) / 3);*/
		width: 180upx;
		height: 180upx;
		margin: 6px 0;
	}

	.voteCard {
		width: 93.12%;
		margin: 16px 4.29% 0 3.44%;
	}

	.oneVoteCard {
		margin: 6px 0 0 0;
		padding: 12px 20px;
		background: rgba(255, 255, 255, 1);
		border: 1px solid rgba(238, 238, 238, 1);
		opacity: 1;
		border-radius: 8px;
	}

	.oneVoteCard_chosen {
		margin: 6px 0 0 0;
		padding: 12px 20px;
		background: rgba(72, 177, 233, 1);
		border: 1px solid rgba(238, 238, 238, 1);
		opacity: 1;
		border-radius: 8px;
	}

	.oneVote_text {
		min-height: 14px;
		width: 85.61%;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: rgba(128, 142, 150, 1);
		opacity: 1;
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
	}

	.oneVote_text_chosen {
		min-height: 14px;
		width: 85.61%;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: rgba(255, 255, 255, 1);
		opacity: 1;
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
	}

	/* .oneResult {
		margin-top: 12px;
		width: 100%;
		height: 37px;
	} */

	.resultTextCard {
		position: relative;
		width: 100%;
		min-height: 12px;
	}

	.voteContent {
		position: absolute;
		left: 12px;
		bottom: 0;
		width: 50%;
		min-height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: rgba(155, 155, 155, 1);
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
	}

	.voteresultNum {
		position: absolute;
		display: flex;
		right: 0;
		bottom: 0;
		min-height: 14px;
	}

	.voteresultNum text {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.percentLine {
		margin-left: 16px;
		height: 14px;
		display: flex;
	}

	.percentNum {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.percentLine text {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.resultBarCard {
		position: relative;
		width: 100%;
		height: 8px;
		margin-top: 4px;
	}

	.resultBar_border {
		position: absolute;
		width: 100%;
		height: 8px;
		z-index: 10;
		border: 1px solid rgba(174, 174, 174, 1);
		border-radius: 20px;
	}

	.resultBar_bgclr {
		position: absolute;
		height: 8px;
		z-index: 20;
		border: 1px solid rgba(174, 174, 174, 1);
		background: rgba(174, 174, 174, 1);
		opacity: 1;
		border-radius: 20px;
	}

	/* 选项效果 */
	.voteContent_chosen {
		position: absolute;
		left: 12px;
		bottom: 0;
		width: 50%;
		min-height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: rgba(36, 130, 180, 1);
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
	}

	.voteresultNum_chosen {
		position: absolute;
		display: flex;
		right: 0;
		bottom: 0;
		min-height: 14px;
	}

	.voteresultNum_chosen text {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(36, 130, 180, 1);
	}

	.percentLine_chosen {
		margin-left: 16px;
		height: 14px;
		display: flex;
	}

	.percentNum_chosen {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(36, 130, 180, 1);
	}

	.percentLine_chosen text {
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(36, 130, 180, 1);
	}

	.resultBar_border_chosen {
		position: absolute;
		width: 100%;
		height: 8px;
		z-index: 10;
		border-radius: 20px;
		border: 1px solid rgba(72, 177, 233, 1);
	}

	.resultBar_bgclr_chosen {
		position: absolute;
		height: 8px;
		z-index: 20;
		border: 1px solid rgba(72, 177, 233, 1);
		opacity: 1;
		border-radius: 20px;
		background: linear-gradient(90deg, rgba(83, 224, 219, 1) 0%, rgba(72, 177, 233, 1) 100%);
	}

	.voteInfoLine {
		width: 88.78%;
		margin: 16px 5.61% 0 5.61%;
		position: relative;
		height: 12px;
	}

	.timeLine {
		position: absolute;
		display: flex;
		height: 12px;
		left: 0;
	}

	.timeleft {
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.voteInfoLine text {
		margin-left: 4px;
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.numbersBar {
		position: absolute;
		display: flex;
		right: 0;
		height: 12px;
	}

	.participants {
		margin-right: 3.2%;
		height: 12px;
		display: flex;
	}

	.participants_Num {
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.participants text {
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.commentusers {
		height: 12px;
		display: flex;
	}

	.commentusers_Num {
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.commentusers text {
		height: 12px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.alertandconfirm {
		width: 100%;
		height: 120px;
		background: rgba(255, 255, 255, 1);
		opacity: 1;
		border-radius: 0px 0px 8px 8px;
	}

	.alertandconfirm text {
		height: 16px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(155, 155, 155, 1);
	}

	.confirmButton_votePage {
		width: 162px;
		height: 38px;
		background: linear-gradient(90deg, rgba(83, 224, 219, 1) 0%, rgba(72, 177, 233, 1) 100%);
		opacity: 1;
		border-radius: 8px;

		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(255, 255, 255, 1);
	}

	.confirmButton_votePage_hover {
		width: 162px;
		height: 38px;
		/* background:linear-gradient(90deg,rgba(83,224,219,1) 0%,rgba(72,177,233,1) 100%); */
		opacity: 1;
		border-radius: 8px;

		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(255, 255, 255, 1);
	}
</style>
