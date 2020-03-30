<template>
	<view style="width: 100%; height: 100%; background: linear-gradient(#f89d4d, #ffc95a);">
		<!-- 导航栏 -->
		<view class="navigatorBar_votePage super_center" :style="{ height: navigationBarHeight + 'px' }" @click="showallVote">
			<view class="navbarLine column_center">
				<text>{{lang.vote}}</text>
				<!-- <view class="allvote column_center">
					<view class="allvote_text">{{lang.allVote}}</view>
					<image src="../../static/icon/angle-down-ffffff.png" mode="aspectFit"></image>
				</view> -->
			</view>
		</view>
		<!-- 全部投票列表 -->
		<!-- <nqallvote v-if="ifallVote" style="width: 100%;height: 100%;"></nqallvote> -->
		<!-- 显示列表时的底部蒙版 -->
		<!-- <view v-if="ifallVote" style="position: fixed;background-color: #000000;opacity: 0.3;width: 100%;height: 100%;z-index: 40;"></view> -->
		<!-- 左侧按钮 -->
		<view class="tapLeft super_center" :style="{ top: navigationBarHeight + 46 + 'px' }"><image src="../../static/icon/angle-left-gradient.png" mode="aspectFit"></image></view>
		<!-- 右侧按钮 -->
		<view class="tapRight super_center" :style="{ top: navigationBarHeight + 46 + 'px' }">
			<image src="../../static/icon/angle-right-gradient.png" mode="aspectFit"></image>
		</view>
		<!-- 投票卡片 -->
		<swiper class="scroll-box" :indicator-dots="false" :autoplay="false" :current="currentVoteIndex" @change="onSwiperChange">
			<swiper-item v-for="(voteCard, index) in showList" :key="index" class="card-box" id="card_{index}">
				<scroll-view @scroll="showScrollTop" :scroll-top="scrollTop" class="card" scroll-y="true" show-scrollbar="false" :style="{ height: voteCardHeight + 'px' }">
					<!-- 标题行 -->
					<view class="title">{{ voteCard.vote.voteTitle }}</view>
					<!-- 用户信息行 -->
					<view class="userLine hor_center">
						<image :src="pathFilter(voteCard.vote.faceImg)" class="touxiang"></image>
						<view class="name">{{ voteCard.vote.nickname }}</view>
						<view class="time">{{ timeDeal(voteCard.vote.createDate) }}</view>
					</view>
					<!-- 内容 -->
					<view class="briefarticleCard">{{ voteCard.vote.voteContent }}</view>
					<!--图片区域-->
					<view>
						<!-- 单图显示 -->
						<view v-if="voteCard.vote.imgList.length == 1" class="detailpics" style="width:91.8%;margin-left: 4.1%; max-height: 400upx; display: flex;">
							<!-- 高 > 宽 -->
							<view v-if="singleImgState == 0">
								<image
									v-for="(i, imageIndex) in voteCard.vote.imgList"
									:key="imageIndex"
									:src="serverUrl + i.imagePath"
									mode="aspectFill"
									:style="{ width: singleImgWidth + 'rpx', height: singleImgHeight + 'rpx' }"
									@tap="previewImage(index, imageIndex)"
									@longpress="aboutImg(index, imageIndex)"
									@load="singleImgeFit"
								></image>
							</view>
							<!-- 宽 > 高 -->
							<view v-else style="width: 100%;">
								<image
									v-for="(i, imageIndex) in voteCard.vote.imgList"
									:key="imageIndex"
									:src="serverUrl + i.imagePath"
									mode="aspectFill"
									:style="{ width: singleImgWidth + 'rpx', height: singleImgHeight + 'rpx' }"
									@tap="previewImage(index, imageIndex)"
									@longpress="aboutImg(index, imageIndex)"
									@load="singleImgeFit"
								></image>
							</view>
						</view>
						<!-- 4宫格 -->
						<view v-else-if="voteCard.vote.imgList.length == 4" class="detailpics" style="max-width: 400upx;margin-left: 0;">
							<image
								class="detailpic"
								v-for="(i, imageIndex) in voteCard.vote.imgList"
								:key="imageIndex"
								:src="serverUrl + i.imagePath"
								mode="aspectFill"
								@tap="previewImage(index, imageIndex)"
								@longpress="aboutImg(imageIndex)"
							></image>
						</view>

						<!-- 其他数量 -->
						<view v-else class="detailpics">
							<image
								class="detailpic"
								v-for="(i, imageIndex) in voteCard.vote.imgList"
								:key="imageIndex"
								:src="serverUrl + i.imagePath"
								mode="aspectFill"
								@tap="previewImage(index, imageIndex)"
								@longpress="aboutImg(imageIndex)"
							></image>
							<view
								v-if="voteCard.vote.imgList.length == 2 || voteCard.vote.length == 5 || voteCard.vote.length == 8"
								style="width: 190upx;height: 190upx;margin: 6px 0;"
							></view>
						</view>
					</view>
					<!-- 投票选项 -->
					<!-- 确定投票前的选项展示 -->
					<view v-if="voteCard.vote.isUserVoted == false" class="voteCard">
						<!-- 双层嵌套，可适应以后扩展选项内容 -->
						<view
							v-for="(option, index2) in voteCard.vote.optionList"
							:key="index2"
							:class="[option.id != selectedOptionId ? 'oneVoteCard' : 'oneVoteCard_chosen']"
							@click="switchChoose(option.id)"
						>
							<text :class="[option.id != selectedOptionId ? 'oneVote_text' : 'oneVote_text_chosen']">{{ option.optionContent }}</text>
						</view>
					</view>
					<!-- 确定投票后的选项展示 -->
					<view v-else-if="currentVoteIndex == index" class="voteCard">
						<view v-for="(result, index3) in voteCard.vote.optionList" :key="index3">
							<view style="margin-top: 12px;width:100%;height:37px;">
								<!-- 以下代码，下个版本需重构 Guetta -->
								<!-- 非用户选项结果 -->
								<view v-if="result.id != voteCard.vote.selectedOptId" class="oneResult">
									<view class="resultTextCard">
										<view class="voteContent">{{ result.optionContent }}</view>
										<view class="voteresultNum">
											<text>{{ result.count }}</text>
											<!-- <text>{{result.barWidth}}</text> -->
											<view class="percentLine">
												<view class="percentNum">{{ reserveTwoDecimal(result.percent * 100) }}</view>
												<text>%</text>
											</view>
										</view>
									</view>
									<view style="width: 100%;height: 8px;">
										<ProgressBar :progessBarWidth="result.barWidth" Type="aqua"></ProgressBar>
										<br />
									</view>
								</view>
								<!-- 用户选项结果 -->
								<view v-else class="oneResult">
									<view class="resultTextCard">
										<view class="voteContent_chosen">{{ result.optionContent }}</view>
										<view class="voteresultNum_chosen">
											<text>{{ result.count }}</text>
											<!-- <text>{{result.barWidth}}</text> -->
											<view class="percentLine_chosen">
												<view class="percentNum_chosen">{{ reserveTwoDecimal(result.percent * 100) }}</view>
												<text>%</text>
											</view>
										</view>
									</view>
									<view style="width: 100%;height: 8px;">
										<ProgressBar :progessBarWidth="result.barWidth" Type="aquachosen"></ProgressBar>
										<br />
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 剩余时间&参与人数 -->
					<view class="voteInfoLine">
						<!-- 时间 -->
						<view class="timeLine">
							<view class="timeleft">{{ reTimeDeal(voteCard.vote.expiryDate) }}</view>
							<text>{{ lang.end }}</text>
						</view>
						<view class="numbersBar">
							<!-- 参与人数 -->
							<view class="participants">
								<view class="participants_Num">{{ voteCard.vote.sumVote }}</view>
								<text>{{ lang.votes }}</text>
							</view>
							<!-- 评论人数 -->
							<view class="commentusers">
								<view class="commentusers_Num">{{ voteCard.vote.commentNum }}</view>
								<text>{{ lang.comments }}</text>
							</view>
						</view>
					</view>
					<!-- 确认投票 -->
					<view v-if="voteCard.vote.isUserVoted == false" class="alertandconfirm super_center">
						<text v-if="ischosen[index] == false">{{ lang.votePrompt1 }}</text>
						<button v-else class="confirmButton_votePage super_center" @click="confirmVote(voteCard.vote, index)">{{ lang.ok }}</button>
					</view>

					<view v-if="voteCard.vote.isUserVoted">
						<view
							style="position: relative;margin-bottom: 20px;margin-top: 25px;
									 width: calc(100% - 24px);height: 100%;margin: auto;"
						>
							<textarea
								class="content"
								v-model="commentContent"
								maxlength="140"
								:auto-height="true"
								:show-confirm-bar="false"
								:placeholder="lang.commentPrompt1"
							></textarea>
							<!-- <view style="position: absolute;bottom: 8px;right:8px;font-size: 11px;color:#888888;">{{140 - commentContent.length}}</view>-->
							<view
								style="width:28px;height:14px;line-height:14px;font-size:14px;
								color:rgba(252,192,65,1);position: absolute;top:11px;right:12px;"
								@tap="saveComment"
							>
								{{ lang.send }}
							</view>
							<image @click="showToastEmoji()" src="../../static/icon/emoji.png" style="position: absolute;left:12px;top:8px;width:20px;height:20px;"></image>
						</view>

						<commentarea style="background-color: #00B7B8;"
							:areaWidth= '"calc(100% - 24px)"'
							:areaMargin= '"auto"'
							:commentList="voteCard.commentList"
							:commentNum="voteCard.vote.commentNum"
							@onChange="changeType"
							@like="swLikeComment"
							@goToCommentDetail="goToCommentDetail"
						></commentarea>
						<view class="comment-bottom">
							<view class="comment-bottom-notice">{{lang.onBottom}}</view>
							<view class="comment-bottom-buttons super_center">
								<!-- <image class="back" @tap="backToLastPage" src="../../static/icon/arrow-left-fcc041.png" mode="aspectFit"></image> -->
								<image class="to-top" @tap="scrollToTop" src="../../static/icon/arrow-left-fcc041.png"></image>
								<view class="active-input-button" 
								:style="{'letter-spacing': lang.langType == 'zh-CN' ? '2px':''}"
								@tap="saveComment()" 
								@click="scrollToTop">{{lang.writeComment}}</view>
							</view>
						</view>
					</view>

					<!-- <votecomment
						ref="voteComment"
						@changeCommentNum="changeCommentNum"
						@finishLoad="showCommentWhenLoad(index)"
						:ifLoad="finishVote[index]"
						:voteId="item.id"
						:index="index"
						:userId="item.userId"
					></votecomment> -->
				</scroll-view>
			</swiper-item>
		</swiper>

		<tab-bar :current="1" @clickTab="onClickTab"></tab-bar>
	</view>
</template>

<script>
import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import commentarea from '@/components/nq-comment/commentarea.vue';
import nqallvote from '@/components/nq-allvote/nq-allvote.vue';
import { mapState, mapMutations } from 'vuex';

const DEFAULT_PAGE = 0;
var uploadFlag = false;
var loadVoteFlag = false;
var loadMoreFlag = false; // 确保往前翻不会再次触发loadMore方法

export default {
	components: {
		uniNavBar,
		tabBar,
		commentarea,
		nqallvote
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			//显示
			scrollTop: -1,
			old: {
				scrollTop: 0
			},
			
			pageHeight: 0, //屏幕高度
			menuButtonInfo: '',
			navigationBarHeight: 0, //导航栏高度
			ifallVote: false, //控制左上角列表显示
			isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮
			// singleImgState: ,
			singleImgHeight: 0,
			singleImgWidth: 0,
			heightWidthRate: 0,
			//投票
			serverUrl: this.$serverUrl,
			userInfo: '',
			voteCardHeight: 0, //单个投票卡片高度
			ischosen: [], //判断选项是否选中
			ischosenFlag: '',
			finishVote: [], //判断是否展示投票结果条形图

			/*页面数据结构
			showList[voteCard]
		    voteCard:{
				vote,
				currentPage,
				totalPage,
				type,
				selectedOptionId,  // 选择的选项id(opt)
				commentList[comment]
			}
			comment{
				mainComment,
				subComment:{
					subCommentList,
					subCommentNum
				}
			}
			*/
			showList: ['1', '2'], //vote卡片
			currentVoteIndex: 0, //当前在哪个vote卡片
			totalPage: 1, //投票流的分页属性
			currentPage: 1, //投票流的分页属性

			selectedOptionId: '', // 选择的选项id
			//评论
			commentContent: '', //评论框输入的内容

			// imgList: [],
			// afterSelectedResult: [], // 确认选择后刷新单个产生的单个vote的所有信息
			// afterSelectedOptionList: [], // 确认选择后刷新单个产生的单个vote的选项信息
			// ifShowComment: false, //判断是否展示评论区域
			loadedMoreIndex: [] // 用来储存已经触发过loadMore方法的投票的index.
		};
	},

	onLoad: function() {
		console.log('还没投票时,selectedOptionId= ' + this.selectedOptionId);
		var userInfo = this.getGlobalUserInfo();
		if (this.isNull(userInfo)) {
			uni.redirectTo({
				url: '../signin/signin'
			});
			return;
		} else {
			this.userInfo = userInfo;
		}

		this.showVotes(this.currentPage);
		//获取导航栏高度
		var info = this.menuButtonInfo;
		var height;
		info = uni.getMenuButtonBoundingClientRect();
		height = info.bottom;
		this.navigationBarHeight = height;
		// console.log('导航栏高度=' + this.navigationBarHeight);
		this.calculateHeight();
		// this.showCommentWhenLoad();
		console.log(showList);
	},

	methods: {
		onClickTab(e) {
			//刷新
			if (e.url == '/' + this.getCurrentPage().route) {
				this.showVotes(1);
			}
		},
		showCommentWhenLoad(index) {
			console.log(index);
			// console.log(this);
			// console.log(this.$refs);

			// console.log(ifLoad);
			// this.$refs.voteComment.getComments(1);
			this.$refs.voteComment[index].getComments(1);
		},

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
		},

		showallVote() {
			this.ifallVote = true;
		},

		switchChoose(selectedOptionId) {
			this.selectedOptionId = selectedOptionId;
			// console.log("传进来的选项id= "+ selectedOptionId);
			// var that = this;

			// // 如果记录的index和传进来的voteIndex相同, 则继续操作
			// // 如果记录的index和传进来的voteIndex不同, 则将所有inchosen变为false再进行操作
			// if (that.ischosenFlag == voteIndex) {
			// 	if (selectedOptionId == that.selectedOptionId) {
			// 		that.selectedOptionId = '';
			// 		that.ischosen[voteIndex] = false;
			// 	} else {
			// 		this.ischosen[voteIndex] = true;
			// 		that.selectedOptionId = selectedOptionId;
			// 		that.currentVoteIndex = voteIndex;
			// 	}
			// } else {
			// 	// 不相等时
			// 	for (var i = 0; i < that.ischosen.length; i++) {
			// 		that.ischosen[i] = false;
			// 	}
			// 	// console.log("改变页面后"+that.ischosen);
			// 	if (selectedOptionId == that.selectedOptionId) {
			// 		that.selectedOptionId = '';
			// 		that.ischosen[voteIndex] = false;
			// 	} else {
			// 		this.ischosen[voteIndex] = true;
			// 		that.selectedOptionId = selectedOptionId;
			// 		that.currentVoteIndex = voteIndex;
			// 	}
			// }
			// // 记录是在第几个vote进行操作
			// that.ischosenFlag = voteIndex;

			// console.log("赋值后的选项id= "+ that.selectedOptionId);
			// console.log(that.currentVoteIndex);
			// console.log(that.ischosen);
		},

		/**
		 * @param {Object} voteId 投票的id
		 * @param {Object} voteIndex 投票的index, 用来控制下面的确认投票键
		 */
		confirmVote(vote, index) {
			var that = this;
			uni.request({
				url: that.$serverUrl + '/vote/selectOption',
				method: 'POST',

				data: {
					optionId: that.selectedOptionId,
					userId: that.userInfo.id,
					voteId: vote.id
				},
				header: {
					'content-type': 'application/json'
				},
				success: res => {
					// this.ifShowComment = !this.ifShowComment;
					// this.finishVote[voteIndex] = !this.finishVote[voteIndex];
					// 更新showList
					this.showList[index].vote = res.data.data;
					// 得到投票后的数值, 根据新的OptionList的id和that.selectedOptionId来判断放在哪一个区域
					// 如果两个相等, 放在用户选择区, 其余放在非用户选择区
					// that.afterSelectedResult = res.data.data.rows[0];
					// console.log(that.afterSelectedResult);
					// that.afterSelectedOptionList = that.afterSelectedResult.optionList;
					// console.log(that.afterSelectedOptionList);
					// that.ischosen[voteIndex] = true;
					// console.log(that.showList[voteIndex]);

					// 进度条伸长
					this.votedResult(this.showList[index]);

					// for (let option of this.showList[index].vote.optionList) {
					// 	this.onePersentBarGrow(option);
					// }

					// this.voteCardHeight = 3000; //确认投票后or该投票状态为已投，改变卡片高度
				}
			});
		},
		
		
		calculateHeight() {
			var that = this;
			uni.getSystemInfo({
				success: function(res) {
					that.pageHeight = res.windowHeight;
					that.voteCardHeight = that.pageHeight - (99 + that.navigationBarHeight);
					// console.log(that.voteCardHeight);
				}
			});
			// console.log(that.pageHeight);
			// console.log(this.voteCardHeight);
		},
		
		/**
		 * @param {Object} voteCard 注意传入值的地址
		 */
		votedResult(voteCard) {
			for (let option of voteCard.vote.optionList) {
				this.onePersentBarGrow(option);
			}
			this.voteCardHeight = this.pageHeight - (79 + this.navigationBarHeight - 16); //确认投票后or该投票状态为已投，改变卡片高度
			//页面高度 - ( (navbar高度+tabbar高度+上边距 = 79) + 导航栏高度 - 卡片圆角半径16px)
		},

		/**
		 * 为选项添加进度条的长度，注意传入值的地址
		 * @param {Object} option 选项
		 * @description 监听对象 --> 在地址上进行更改(类似指针), 避免多线程操作一个地址时: 获取数据失败/混乱等问题
		 */
		onePersentBarGrow(option) {
			var widthTarget = this.reserveTwoDecimal(option.percent * 100);
			this.$set(option, 'barWidth', widthTarget); // 为选项添加barWidth属性
		},

		showVotes(page) {
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
			}, 5000); // 延时5s timeout

			// console.log(1);
			uni.request({
				url: this.$serverUrl + '/vote/queryAllVotes',
				method: 'POST',
				data: {
					page: page,
					userId: this.userInfo.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: async res => {
					uni.hideLoading();
					loadVoteFlag = false;
					// console.log(res);
					// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
					if (page == 1) {
						this.showList = [];
					}
					var showList = [];
					var votes = res.data.data.rows;
					for (let vote of votes) {
						//预设结构体
						var voteCard = {
							vote: vote,
							currentPage: 1,
							totalPage: 1,
							type: 0,
							commentList: []
						};
						//获取并拼接commentList
						await this.getComments(voteCard).then(voteCard => {
							showList.push(voteCard);
						});
					}

					// 在原showList后面嫁接新的数据
					var newShowList = showList;
					var oldShowList = this.showList;
					this.showList = oldShowList.concat(newShowList);
					this.currentPage = page;
					this.totalPage = res.data.data.total;

					console.log(this.showList);

					// if (page == 1) {
					// 	this.ischosen = [];
					// 	this.finishVote = [];
					// }
					// var tempIsChosen = [];
					// var tempFinishVote = [];
					// for (var i = 0; i < res.data.data.rows.length; i++) {
					// 	tempIsChosen[i] = false;
					// 	tempFinishVote[i] = this.showList[i].isUserVoted;
					// }
					// this.ischosen = this.ischosen.concat(tempIsChosen);
					// this.finishVote = this.finishVote.concat(tempFinishVote);

					this.votedResult(this.showList[this.currentVoteIndex]);
				},
				fail: res => {
					uni.hideLoading();
					loadVoteFlag = false;

					console.log('index unirequest fail');
					console.log(res);
				}
			});
		},

		getComments(voteCard) {
			return new Promise((resolve, reject) => {
				uni.request({
					method: 'POST',
					url: this.$serverUrl + '/vote/getMainVoteComments',
					data: {
						voteId: voteCard.vote.id,
						userId: this.userInfo.id,
						page: voteCard.currentPage,
						type: voteCard.type
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: async res => {
						if (res.data.status == 200) {
							if (voteCard.currentPage == 1) {
								voteCard.commentList = [];
							}
							var commentList = [];
							//获取主评论
							var mainComments = res.data.data.rows;
							//获取子评论
							// for(let mainComment of mainComments){
							for (var i = 0; i < mainComments.length; i++) {
								var comment;
								var mainComment = mainComments[i];
								await this.getSubComments(mainComment.id, 1).then(subComment => {
									comment = {
										mainComment: mainComment,
										subComment: subComment
									};
									commentList.push(comment);
								});
							}
							// 拼接
							var newCommentList = commentList;
							var oldCommentList = voteCard.commentList;
							voteCard.commentList = oldCommentList.concat(newCommentList);
							voteCard.totalPage = res.data.data.total;
							console.log(voteCard);
							resolve(voteCard);
							// console.log(this.commentList);
							// this.control_scroll_butoon(); //获取评论数据后，生成卡片后，判断总页面高度，控制是否显示回到顶部按钮
						} else {
							console.log(res);
						}
					}
				});
			});
		},

		getSubComments(mainCommentId, page) {
			return new Promise((resolve, reject) => {
				uni.request({
					method: 'POST',
					url: this.$serverUrl + '/vote/getSubComments',
					data: {
						underCommentId: mainCommentId,
						userId: this.userInfo.id,
						page: page,
						type: 0
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						// console.log(res);
						if (res.data.status == 200) {
							var subCommentNum = res.data.data.records;
							var subCommentList;
							if (subCommentNum <= 2) {
								subCommentList = res.data.data.rows;
							} else {
								subCommentList = res.data.data.rows.slice(0, 2);
							}
							var subComment = {
								subCommentList: subCommentList,
								subCommentNum: subCommentNum
							};
							resolve(subComment);
						} else {
							reject('Request fail');
						}
					},
					fail: res => {
						reject('Request fail');
					}
				});
			});
		},

		loadMore: function() {
			var currentPage = this.currentPage;
			var totalPage = this.totalPage;
			if (currentPage == totalPage) {
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

		/**
		 * 监听投票滑动, 当滑到新的一个投票, 返回其index
		 * @param {Object} e change事件的默认返回值, 其中包含swiper的index
		 */
		onSwiperChange: function(e) {
			// let index = e.target.current || e.detail.current;
			var index = e.detail.current;
			console.log('VoteIndex: ' + index);
			this.currentVoteIndex = index;
			this.votedResult(this.showList[index]);
			// var tmp = this.showList;
			// this.showList = '';
			// this.showList = tmp
			// // this.showList[index].vote.optionList[0].barWidth = 50;
			// console.log(this.showList[index])
			// for (let ifHadLoadMore of this.loadedMoreIndex) {
			// 	if (index == ifHadLoadMore) {
			// 		return;
			// 	}
			// }

			// this.loadMoreFlag = false;

			// // console.log(index);
			// // console.log(this.loadedMoreIndex);
			// var remainder = (index + 1) % 10;
			// if (index == 0) {
			// 	// 刷新投票
			// 	this.refreshVote();
			// } else if (remainder == 0 || index + 1 == this.showList.length) {
			// 	if (!this.loadMoreFlag) {
			// 		this.loadMore();
			// 		this.loadedMoreIndex = this.loadedMoreIndex.concat(index);
			// 		this.loadMoreFlag = true;
			// 	}
			// }
		},

		// 当有评论时, 父组件votePage让该vote的评论数+1
		changeCommentNum(index) {
			// console.log("12312312 " + index);
			this.showList[index].commentNum++;
		},

		/**
		 * fromUserId 必填
		 * toUserId 必填
		 * articleId 必填 // 为了计算文章总评论数
		 * underCommentId // 显示在该主评论层ID下
		 * fatherCommentId // 父级评论ID
		 * comment 必填
		 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId, underCommentId;
		 *     子级评论有 fatherCommentId, underCommentId;
		 */
		saveComment() {
			console.log('tragger savecomment');
			if (uploadFlag) {
				console.log('正在上传...');
				return;
			}
			setTimeout(() => {
				if (uploadFlag) {
					uploadFlag = false; // 解锁
					uni.hideLoading();
					uni.showToast({
						title: '网络未知错误',
						icon: 'none',
						duration: 1000
					});
				}
			}, 5000); // 延时5s timeout

			if (this.commentContent == '') {
				uni.showToast({
					title: '好像忘写评论了哦~',
					duration: 1000,
					icon: 'none'
				});
			} else {
				uploadFlag = true;
				uni.showLoading({
					title: '正在上传...'
				});
				var submitData = {
					comment: this.commentContent,
					fromUserId: this.userInfo.id,
					toUserId: this.showList[this.currentVoteIndex].vote.userId,
					voteId: this.showList[this.currentVoteIndex].vote.id
				};
				uni.request({
					url: this.$serverUrl + '/vote/saveVoteComment',
					method: 'POST',
					data: submitData,
					success: res => {
						if (res.data.status == 200) {
							uni.hideLoading();
							uploadFlag = false;

							this.commentContent = '';
							// this.writingComment = false;
							// this.showInput = false;
							this.getComments(this.showList[this.currentVoteIndex]);

							this.showList[this.currentVoteIndex].vote.commentNum++; // 评论数累加
						} else if (res.data.status == 500) {
							this.contentIllegal();
						}
					}
				});
			}
		},

		goToCommentDetail(mainComment) {
			var data = {
				mainComment: mainComment,
				type: 'vote'
			};
			uni.navigateTo({
				url: '/pages/comment-detail/comment-detail?data=' + JSON.stringify(data)
			});
		},

		contentIllegal() {
			// 内容非法 用户提醒
			uploadFlag = false;
			uni.hideLoading();
			uni.showToast({
				title: '内容涉嫌违规，请联系管理员。',
				duration: 2000,
				icon: 'none'
			});
		},
		
		/**
		 * 点赞或取消点赞评论
		 * @param {Object} comment
		 */
		swLikeComment(comment, index) {
			var mainComment = comment.mainComment
			if (mainComment.isLike) {
				this.unLikeComment(mainComment);
				mainComment.likeNum--;
			} else {
				this.likeComment(mainComment);
				mainComment.likeNum++;
			}
			mainComment.isLike = !mainComment.isLike;
			console.log(this.showList[this.currentVoteIndex])
			//动态修改显示层的值，直接按地址修改这里好像不可以
			this.showList[this.currentVoteIndex].commentList[index].mainComment = mainComment;
			this.showList[this.currentVoteIndex].commentList[index].subComment = comment.subComment;
		},
		
		likeComment(comment) {
			console.log('点赞评论');
			uni.request({
				method: 'POST',
				url: this.$serverUrl + '/vote/userLikeVoteComment',
				data: {
					userId: this.userInfo.id,
					commentId: comment.id,
					createrId: comment.fromUserId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},
		
		unLikeComment(comment) {
			console.log('取消点赞评论');
			uni.request({
				method: 'POST',
				url: this.$serverUrl + '/vote/userUnLikeVoteComment',
				data: {
					userId: this.userInfo.id,
					commentId: comment.id,
					createrId: comment.fromUserId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
				}
			});
		},
		scrollToTop(e) {
			this.scrollTop = this.old.scrollTop
			this.$nextTick(function() {
				this.scrollTop = 0
			});
			setTimeout(() => {
				this.scrollTop = -1;
			}, 200)
			// console.log(this.scrollTop);
			// console.log(this.old.scrollTop);
		}, 
		
		showScrollTop(){
			// console.log('=' + this.scrollTop);
		},
		
		showToastEmoji() {
			uni.showToast({
				// title: '⠀⠀⠀⠀⠀under⠀⠀⠀⠀⠀development',//不是空格，是特殊符号，莫删
				title: '开发小哥正在玩命实现中...',
				duration: 2000,
				icon: 'none'
			});
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
	position: relative;
	width: 100%;
}

.navbarLine {
	position: absolute;
	width: 100%;
	height: 38px;
	bottom: 0;
}

.navbarLine text {
	position: absolute;
	text-align: center;
	font-size: 17px;
	line-height: 23px;
	color: white;
	font-weight: 550;
	height: 23px;
	width: 100%;
	left: 0;
	right: 0;
	margin-left: auto;
	margin-right: auto;
	overflow: inherit;
}

.allvote {
	position: absolute;
	left: 23px;
	width: 111px;
	height: 29px;
	background: rgba(233, 153, 19, 1);
	opacity: 1;
	border-radius: 75px;
}

.allvote image {
	position: absolute;
	right: 12px;
	width: 18px;
	height: 18px;
	background: rgba(212, 139, 16, 1);
	border-radius: 50%;
	opacity: 1;
}

.allvote_text {
	position: absolute;
	left: 16px;
	width: 57px;
	height: 16px;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 16px;
	color: rgba(255, 255, 255, 1);
	opacity: 1;
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
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 23px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
}

.time {
	position: absolute;
	right: 3.44%;
	font-size: 12px;
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

/*评论输入框*/
.content {
	min-height: 51px;
	max-height: 300px;
	margin-top: 13px;
	width: calc(100% - 28px);
	overflow: scroll;
	padding: 36px 12px 12px;

	border: 2px solid rgba(252, 192, 65, 1);
	border-radius: 8px;
	font-size: 14px;
}

	/* 滑到底了等提示 */
	.comment-bottom {
		height: 160px;
		margin: auto;
	}

	.comment-bottom-notice {
		text-align: center;
		min-width: 71px;
		height: 14px;
		line-height: 14px;
		font-size: 14px;
		color: #B2B2B2;
		margin: 37px auto 27px;
	}

	.comment-bottom-buttons {
		display: flex;

	}

	.comment-bottom-buttons .back {
		width: 16px;
		height: 16px;
		padding: 14px;
		background: #FFF1D5;
		border-radius: 22px;
	}

	.comment-bottom-buttons .to-top {
		width: 18px;
		height: 12px;
		background: #FFF1D5;
		border-radius: 22px;
		padding: 16px 11px 16px 15px;
		position: relative;
		transform: rotate(90deg);
	}

	.comment-bottom-buttons .to-top::after {
		content: '';
		position: absolute;
		top: 14px;
		left: 12px;
		width: 2px;
		height: 16px;
		background: #FCC041;
		border-radius: 2px;
	}

	.active-input-button {
		height:14px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color: rgba(255, 255, 255, 1);
		padding: 10px 22px;
		border-radius: 10px;
		box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.16);
		background: #FCC041;
		/* letter-spacing: 2px; */
		margin-left: 26px;
	}
</style>
