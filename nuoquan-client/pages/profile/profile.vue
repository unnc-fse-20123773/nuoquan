<template>
	<view id="profile-container">
		<view id="yellow-box">
		</view>
		<form @submit="formSubmit" @reset="formReset">
			<view class="profile-basicinfo-card column_center">
				<image class="profileTouxiang" mode="aspectFill" :src="userInfo.faceImg"></image>
				<!-- 一般状态 -->
				<view class="profileText-box" v-if="isEdit == false">
					<text class="left-profileText1 ">昵称</text>
					<text class="right-profileText1" v-if="userInfo.nickname!=null ">{{userInfo.nickname}}</text>
					<text class="right-profileText1" v-else>待设置</text>
					<text class="left-profileText2">性别</text>
					<text class="right-profileText2" v-if="userInfo.gender==null || userInfo.gender==-1">待设置</text>
					<text class="right-profileText2" v-else>{{genderList[userInfo.gender]}}</text>
				</view>
				<!-- 修改时状态 -->
				<view class="profileText-box" v-if="isEdit == true">
					<text class="left-profileText1 ">昵称</text>
					<input class="right-profileText1-1" style="text-align:right;min-height: 1upx;height:26px;" maxlength="16" :value="userInfo.nickname"
					 name="nickname" />
					<view class="input-border"></view>
					<text class="left-profileText2">性别</text>
					<view class="genderPicker">
						<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">
							<text class="gender-text">男</text></view>
						<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">
							<text class="gender-text">女</text></view>
						<view :class="[gender == 2 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(2)">
							<text class="gender-text">其他</text></view>
					</view>
				</view>
			</view>

			<view class="profile-moreinfo-card super_center">
				<!-- 一般状态 -->
				<view class="profilemoreText-box" v-if="isEdit == false">
					<text class="left-profileText1">学校邮箱</text>
					<text class="right-profileText1" v-if="userInfo.email!=null">{{userInfo.email}}</text>
					<text class="right-profileText1" v-else>待设置</text>
					<text class="left-profileText2">毕业年份/专业</text>
					<text class="right-profileText2">{{userInfo.graduationYear}} {{userInfo.major}}</text>
					<text class="left-profileText3">学位</text>
					<text class="right-profileText3">{{degrees[userInfo.degree]}}</text>
				</view>

				<!-- 修改时状态 -->
				<view class="profilemoreText-box" v-if="isEdit == true">
					<text class="left-profileText1">学校邮箱</text>
					<input class="right-profileText1-1" style="width: 260px;text-align:right;min-height: 1upx;height:26px;" maxlength="32"
					 :value="userInfo.email" name="email"/>
					<view class="input-border2"></view>
					<text class="left-profileText2">毕业年份/专业</text>

					<view class="yearPicker">
						<view class="yearPicker-button" @click="yearPickerChanger()">
							<text class="yearPicker-text">{{year}}</text>
							<view class="year-pointer">
								<image class="year-pointerIcon" src="../../static/icon/angle-down.png" mode="scaleToFill"></image>
							</view>
						</view>
						<view class="yearPicker-button" @click="majorPickerChanger()">
							<text class="yearPicker-text">{{major}}</text>
							<view class="year-pointer">
								<image class="year-pointerIcon" src="../../static/icon/angle-down.png" mode="scaleToFill"></image>
							</view>
						</view>
					</view>

					<text class="left-profileText3">学位</text>
					<view class="degreePicker">
						<view class="yearPicker-button" @click="degreePickerChanger()">
							<text class="yearPicker-text">{{degree}}</text>
							<view class="year-pointer">
								<image class="year-pointerIcon" src="../../static/icon/angle-down.png" mode="scaleToFill"></image>
							</view>
						</view>
					</view>
					<!-- <input class="right-profileText3-3" style="width: 260px;text-align:right;min-height: 1upx;height:26px;" maxlength="11"
				 :value="18888888888" />
				<view class="input-border3"></view> -->

					<mypicker class="year-pick-style" v-if="yearPicker == true" :dataList="years" :value="yearPickerVal" @change="yearChange"
					 @tapBackground="yearPickerChanger()" ></mypicker>

					<mypicker class="major-pick-style" v-if="majorPicker == true" :dataList="majors" :value="majorPickerVal" @change="majorChange"
					 @tapBackground="majorPickerChanger()"></mypicker>

					<mypicker class="degree-pick-style" v-if="degreePicker == true" :dataList="degrees" :value="degreePickerVal"
					 @change="degreeChange" @tapBackground="degreePickerChanger()"></mypicker>

				</view>

			</view>
			<view style="display: flex; height: 5%;">
				<view class="editProfile super_center" v-if="isEdit == false" @click="editProfile(isEdit)">
					<text class="editProfile-text">编辑信息</text>
				</view>
				<button class="editProfile-edit super_center" v-if="isEdit == true" formType="submit">
					<text class="editProfile-text">完成修改</text>
				</button>
				<button class="editProfile-cancle super_center" v-if="isEdit == true" @click="cancle">
					<text class="editProfile-cancletext">取消</text>
				</button>
			</view>
		</form>
	</view>
</template>
<script>
	import mypicker from '../../components/mypicker.vue';

	export default {
		data() {
			// 为毕业年份 picker 传值
			const date = new Date();
			const years = [];
			const thisYear = date.getFullYear();

			for (let i = thisYear; i <= thisYear + 4; i++) {
				years.push(i);
			};

			// major
			const majors = ['AEE', 'ABE', 'CS', 'CEE', 'CIVE', 'ECON', 'EEE', 'ENGL',
				'GEOG', 'IC', 'IS', 'MATH', 'PDM', 'NUBS'
			];

			// degree 顺序和数据库保持一致
			const degrees = ['高中', '本科', '研究生'];

			return {
				genderList: ['女', '男', '其他'], // 顺序和数据库保持一致
				
				years, // 传入毕业年份 picker
				yearPickerVal: [], // 毕业年份 picker 的起始值, 必须为list
				majors,
				majorPickerVal: [],
				degrees,
				degreePickerVal: [],

				gender: 2,
				year: years[0], // 默认值
				major: majors[0],
				degree: degrees[1],
				degreeDB: '1', // 数据库 degree 传值
				
				isEdit: false,
				userInfo: '',
				yearPicker: false,
				majorPicker: false,
				degreePicker: false,
			}
		},

		components: {
			mypicker,
		},

		onLoad: function() {
			uni.setNavigationBarTitle({
				title: "个人信息"
			});
			
			var userInfo = this.getGlobalUserInfo()
			this.userInfo = userInfo;
			
			// 按已有信息修改默认值
			var gender = userInfo.gender;
			var year = userInfo.graduationYear;
			var major = userInfo.major;
			var degree = userInfo.degree;
			if(!this.isNull(gender)){ // 判空，防止默认值被刷掉
				this.gender = gender;
			}
			
			if(!this.isNull(year)){
				this.year = year;
			}
			
			if(!this.isNull(major)){
				this.major = major;
			}
			
			if(!this.isNull(degree)){
				this.degree = this.degrees[degree];
				this.degreeDB = degree; // 修改对数据库的默认值
			}
		},
		methods: {
			genderChanger: function(gender) {
				if (this.gender == gender) {
					this.gender = -1;
					console.log(gender);
				} else {
					this.gender = gender;
				}
			},

			yearPickerChanger: function() {
				if (this.yearPicker == false) {
					this.yearPicker = true;
				} else {
					this.yearPicker = false;
				}
				// console.log(this.yearPicker);
			},

			majorPickerChanger: function() {
				if (this.majorPicker == false) {
					this.majorPicker = true;
				} else {
					this.majorPicker = false;
				}
				// console.log(this.majorPicker);
			},

			degreePickerChanger: function() {
				if (this.degreePicker == false) {
					this.degreePicker = true;
				} else {
					this.degreePicker = false;
				}
				// console.log(this.degreePicker);
			},

			editProfile: function(isEdit) {
				if (isEdit == false) {
					this.isEdit = true;
				} else if (isEdit == true) {
					this.isEdit = false;
				}
				console.log(this.isEdit);
			},

			cancle: function() {
				// 取消修改操作
				this.editProfile(this.isEdit);
			},

			yearChange: function(e) {
				var year = this.years[e];
				this.year = year;
				// 给组件赋值回去，更改起始值
				this.yearPickerVal[0] = e;

			},

			majorChange: function(e) {
				var major = this.majors[e];
				this.major = major;
				// 给组件赋值回去，更改起始值
				this.majorPickerVal[0] = e;
			},

			degreeChange: function(e) {
				var degree = this.degrees[e];
				this.degree = degree;
				this.degreeDB = e[0];
				// 给组件赋值回去，更改起始值
				this.degreePickerVal[0] = e;

			},

			formSubmit: function(e) {
				// 提交表单操作
				var form = e.detail.value;
				var data={
					id: this.userInfo.id,
					nickname: form.nickname,
					gender: this.gender,
					email: form.email,
					graduationYear: this.year,
					major: this.major,
					degree: this.degreeDB
				};
				// console.log(data);
				
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/updateUser',
					method: "POST",
					data: JSON.stringify(data),
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						if(res.data.status == 200){
							var user = res.data.data;
							var finalUser = this.myUser(user);// 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							console.log(this.userInfo);
						}
					},
				});
				
				// 完成修改，更改 isEdit 为 false
				this.editProfile(this.isEdit);
			},

			getIndex(list, item) {
				for (var i = 0; i < list.length; i++) {
					if (list[i] == item) {
						return i;
					}
				}
				return -1;
			},

			test(e) {
				console.log(e);
			}
		}
	}
</script>

<style>
	page {
		height: 100%;
		width: 100%;
	}

	#profile-container {
		position: fixed;
		width: 100%;
		height: 100%;
		background-color: #F3F3F3;
	}

	#yellow-box {
		position: fixed;
		top: 0;
		height: 16%;
		width: 100%;
		background-color: #FFCF3C;
	}

	.profile-basicinfo-card {
		position: fixed;
		top: 8%;
		left: 7%;
		width: 86%;
		height: 300upx;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}

	.profileTouxiang {
		margin-left: 6%;
		width: 160upx;
		height: 160upx;
		border-radius: 120px;
		display: inline-block;
		vertical-align: middle;
	}

	.profileText-box {
		position: relative;
		height: 180upx;
		width: 55%;
		margin-left: 6%;
	}

	.left-profileText1 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		font-weight: 550;
	}

	.right-profileText1 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 46upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	/* 在这里修改输入框样式 */
	.right-profileText1-1 {
		color: #888888;
		position: absolute;
		margin-top: 40upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.input-border {
		position: absolute;
		margin-top: 86upx;
		right: 0;
		border-bottom: 1upx solid #C0C0C0;
		width: 80%;
	}

	.input-border2 {
		position: absolute;
		margin-top: 86upx;
		right: 0;
		border-bottom: 1upx solid #C0C0C0;
		width: 260px;
	}

	.input-border3 {
		position: absolute;
		margin-top: 292upx;
		right: 0;
		border-bottom: 1upx solid #C0C0C0;
		width: 126px;
	}

	.left-profileText2 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		margin-top: 110upx;
		font-weight: 550;
	}

	.right-profileText2 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 146upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.left-profileText3 {
		color: #888888;
		position: absolute;
		left: 0%;
		font-size: 30upx;
		margin-top: 210upx;
		font-weight: 550;
	}

	.right-profileText3 {
		color: #3D3D3D;
		position: absolute;
		margin-top: 246upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.right-profileText3-3 {
		color: #888888;
		position: absolute;
		margin-top: 246upx;
		right: 0;
		font-size: 32upx;
		font-weight: 550;
	}

	.profile-moreinfo-card {
		height: 30%;
		margin-left: 7%;
		margin-top: 61%;
		width: 86%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}

	.profilemoreText-box {
		position: relative;
		height: 90%;
		width: 80%;
	}

	.editProfile {
		margin-top: 8%;
		margin-left: 7%;
		width: 86%;
		height: 100%;
		background-color: #FFCF3C;
		border-radius: 20upx;
	}

	.editProfile-edit {
		margin-top: 8%;
		margin-left: 7%;
		width: 58%;
		height: 100%;
		background-color: #FFCF3C;
		border-radius: 20upx;
	}

	.editProfile-text {
		color: white;
		font-size: 36upx;
		font-weight: 400;
	}

	.editProfile-cancle {
		margin-top: 8%;
		margin-right: 7%;
		width: 21%;
		height: 100%;
		background-color: #dbdbdb;
		border-radius: 20upx;
	}

	.editProfile-cancletext {
		color: white;
		font-size: 36upx;
		font-weight: 400;
	}

	.genderPicker {
		position: absolute;
		margin-top: 156upx;
		right: -12upx;
		width: 94%;
		height: 30%;
		display: flex;
		justify-content: space-around;
	}

	.genderPicker-button {
		height: 100%;
		width: 96upx;
		border-radius: 8upx;
		background-color: #dedede;
		display: flex;
		justify-content: center;
		font-size: medium;
		font-weight: 550;
		align-item: center;
	}

	.genderPicker-buttonclick {
		height: 100%;
		width: 96upx;
		border-radius: 8upx;
		background-color: #FDD041;
		justify-content: center;
		display: flex;
		align-item: center;
	}

	.gender-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 550;
	}

	.year-pick-style {
		position: absolute;
		z-index: 999;
		margin-top: 146upx;
		top: -148upx;
		right: 126upx;
	}

	.major-pick-style {
		position: absolute;
		z-index: 999;
		margin-top: 146upx;
		top: -148upx;
		right: -20upx;
	}

	.yearPicker {
		position: absolute;
		margin-top: 156upx;
		right: -12upx;
		width: 48%;
		/* 在此更改按钮高度 */
		height: 16%;
		display: flex;
		justify-content: space-between;
	}

	.yearPicker-button {
		height: 100%;
		width: 122upx;
		border-radius: 16upx;
		background-color: #dedede;
		display: flex;
		justify-content: center;
		align-item: center;
		position: relative;
	}

	.year-pointer {
		position: absolute;
		right: 0;
		width: 22upx;
		height: 100%;
	}

	.year-pointerIcon {
		width: 36upx;
		height: 36upx;
		margin-top: 10upx;
		margin-left: -8upx;
	}

	.yearPicker-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 550;
		color: #888888;
		margin-left: -16upx;
	}

	.degreePicker {
		position: absolute;
		margin-top: 250upx;
		right: -14upx;
		width: 24%;
		/* 在此更改按钮高度 */
		height: 16%;
		display: flex;
		justify-content: space-between;
	}

	.degree-pick-style {
		position: absolute;
		z-index: 999;
		margin-top: 200upx;
		top: -100upx;
		right: -20upx;
	}
</style>
