<template>
	<view id="profile-container">
		<view id="yellow-box"></view>
		<form @submit="formSubmit" @reset="formReset">
			<view class="profile-basicinfo-card">
				<image class="profileTouxiang" mode="aspectFill" :src="userInfo.faceImg" height:160upx width:160upx></image>
				<!-- 一般状态 -->
				<button class="editProfile super_center" v-if="isEdit == false" @click="editProfile(isEdit)">
					<text class="editProfile-text">修改</text>
				</button>
				<button class="editProfile-edit super_center" v-if="isEdit == true" formType="submit">
					<text class="editProfile-edit-text">完成</text>
				</button>


				<view class="nickname">
					<view class="text">昵称</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.nickname}}</view>
					<input class="right-profileText1-1" maxlength="8" :value="userInfo.nickname" v-if="isEdit" />
					<!-- 					<text v-if="isEdit == true" class="text_limit"> 最长 8 位</text>
 -->
				</view>

				<view class="gender">
					<view class="text"> 性别</view>
					<!-- 					<text class="right-profileTexta"  v-if="userInfo.gender==null || userInfo.gender==-1">待设置</text> -->
					<view class="second_line" v-if="!isEdit">{{genderList[userInfo.gender]}}</view>
					<view v-if="isEdit" style="display: flex;justify-content: space-between;position: relative;left:-6px;height:34px;">
						<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">
							男
						</view>
						<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">
							女
						</view>
					</view>
				</view>


				<view class="nickname">
					<view class="text">毕业年份</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.graduationYear}}</view>
					<mypicker class="year-pick-style" v-show="isEdit" :mode="'year'" :range="years" :value="year" @change="pickerChange"></mypicker>
				</view>
				<view class="nickname">
					<view class="text">专业</view>
					<view class="second_line" v-if="!isEdit">{{userInfo.major}}</view>
						<mypicker v-show="isEdit" class="year-pick-style" :mode="'major'" :range="majors" :value="major" @change="pickerChange"></mypicker>
				</view>
				<!-- 				<view class="graduationYear" v-if="isEdit == false">
					<text class="left-profileText2">毕业年份</text>
					<text class="left-profileTextb">{{userInfo.graduationYear}}</text>
				</view> -->
				<!-- 				<view class="graduationYear-edit" v-if="isEdit == true">
					<text class="left-profileText2">毕业年份</text>
					<view class="yearPicker">
						<mypicker class="year-pick-style" :mode="'year'" :range="years" :value="year" @change="pickerChange"></mypicker>
					</view>
				</view> -->
				<!-- 				<view class="major" v-if="isEdit == false">
					<text class="left-profileText2">专业</text>
					<text class="left-profileTextb">{{userInfo.major}}</text>
				</view> -->
				<!-- 				<view class="major-edit" v-if="isEdit == true">
					<text class="right-profileText2">专业</text>
					<view class="majorPicker">
						<mypicker class="major-pick-style" :mode="'major'" :range="majors" :value="major" @change="pickerChange"></mypicker>
					</view>
				</view> -->
				<!-- 				<view class="degree" v-if="isEdit == false">
					<text class="right-profileText2">学位</text>
					<text class="right-profileTextb">{{degrees[userInfo.degree]}}</text>
				</view> -->
				<!-- 				<view class="degree-edit" v-if="isEdit == true">
					<text class="left-profileText2">学位</text>
					<view class="degreePicker">
						<mypicker class="degree-pick-style" :mode="'degree'" :range="degrees" :value="degree" @change="pickerChange"></mypicker>
					</view>
				</view> -->

				<!-- 修改时状态 -->
				<!-- <view class="profileText-box" v-show="isEdit == true"> -->
			</view>
			<view class="profile-moreinfo-card super_center">
				<!-- 一般状态 -->
				<view class="profilemoreText-box" v-if="isEdit == false">
					<view class="email">
						<text class="profilemoreText1">学校邮箱</text>
						<text class="profilemoreTexta" v-if="userInfo.email!=null">{{userInfo.email}}</text>
						<text class="profilemoreTexta" v-else>scyzl2@nottingham.edu.cn</text>
					</view>
				</view>

				<!-- 修改时状态 -->
				<view class="profilemoreText-box" v-if="isEdit == true">
					<view class="email">
						<text class="profilemoreText1">学校邮箱</text>
						<input class="profilemoreTexta" style="width: 260px;text-align:left;min-height: 1upx;height:26px;" maxlength="26"
						 :value="userInfo.email" name="email" />
						<view class="input-border2"></view>
					</view>
				</view>

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

			for (let i = 2004; i <= thisYear + 4; i++) {
				years.push(i);
			};

			// major
			const majors = ['AEE', 'ABE', 'CS', 'CEE', 'CIVE', 'ECON', 'EEE', 'ENGL',
				'GEOG', 'IC', 'IS', 'MATH', 'PDM', 'NUBS'
			];

			// degree 顺序和数据库保持一致
			const degrees = ['高中', '本科', '研究生'];

			return {
				genderList: ['女', '男'], // 顺序和数据库保持一致

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
			console.log(this.getGlobalUserInfo());
			var a = this.getGlobalUserInfo();
			this.userInfo = a;
			// 按已有信息修改默认值
			var gender = this.userInfo.gender;
			var year = this.userInfo.graduationYear;
			var major = this.userInfo.major;
			var degree = this.userInfo.degree;
			if (!this.isNull(gender)) { // 判空，防止默认值被刷掉
				this.gender = gender;
			}

			if (!this.isNull(year)) {
				this.year = year;
			}

			if (!this.isNull(major)) {
				this.major = major;
			}

			if (!this.isNull(degree)) {
				this.degree = this.degrees[degree];
				this.degreeDB = degree; // 修改对数据库的默认值
			}
		},

		onShow() {
			this.setTabBarIndex(3) //index为当前tab的索引
		},

		methods: {
			pickerChange: function(res) {
				if (res.mode == 'major') {
					this.major = res.newPickerValue;
				} else if (res.mode == 'degree') {
					this.degree = res.newPickerValue;
				} else if (res.mode == 'year') {
					this.year = res.newPickerValue;
				}
			},
			genderChanger: function(gender) {
				if (this.gender == gender) {
					this.gender = -1;
					console.log(gender);
				} else {
					this.gender = gender;
				}
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
				var data = {
					id: this.userInfo.id,
					nickname: form.nickname,
					gender: this.gender,
					email: form.email,
					graduationYear: this.year,
					major: this.major,
					degree: this.degreeDB
				};
				console.log(data);
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/updateUser',
					method: "POST",
					data: JSON.stringify(data),
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						if (res.data.status == 200) {
							var user = res.data.data;
							var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
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
		position: relative;
		/* width: 86%;
	height: 300px; */
		width: 93%;
		height: 225px;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
		/* justify-content: center; */
		margin-left: auto;
		margin-right: auto;
		margin-top: 80px;
		margin-bottom: 10px;
	}

	.profileTouxiang {
		position: absolute;
		top: -100upx;
		left: calc(50% - 80upx);
		width: 160upx;
		height: 160upx;
		border-radius: 120px;
	}




	.left-profileText1 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
		display: block;
	}

	.right-remind {
		margin-left: 10px;
		color: #888888;
		font-size: 8px;
		font-weight: 500;
	}

	.left-profileTexta {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}



	/* 在这里修改输入框样式 */
	.right-profileText1 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
		display: block;

	}

	.right-profileTexta {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}

	.graduationYear {
		width: 128upx;
		position: absolute;
		left: 13%;
		top: 45%;
	}

	.left-profileText2 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
	}

	.right-profileText2 {
		color: #888888;
		font-size: 11px;
		/*margin-top: 210upx;*/
		font-weight: 500;

	}

	.major {
		width: 50%;
		/* 	position: absolute;
		right: -5%;
		top: 45%; */
	}

	.left-profileTextb {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}

	.right-profileTextb {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}

	.degree {
		width: 50%;
		/* position: absolute;
		left: 13%;
		top: 70%; */
	}



	.input-border2 {
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

	.profile-moreinfo-card {
		height: 200upx;
		margin-left: 7%;
		position: relative;
		width: 86%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}

	.email {
		position: absolute;
		left: 13%;
		top: 25%;
	}

	.profilemoreText1 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
		display: block;
	}

	.profilemoreTexta {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}

	.editProfile {
			position: absolute;
		right: 5%;
		top: 3%;
		width: 68px;
		height: 26px;
		background: rgba(255, 201, 90, 1);
		opacity: 1;
		border-radius: 4px;
	}

	.editProfile-edit {
		position: absolute;
		right: 10%;
		top: -10%;
		width: 68px;
		height: 26px;
		border: 1px solid rgba(255, 201, 90, 1);
		opacity: 1;
		border-radius: 4px;
	}

	.editProfile-text {
		color: white;
		font-size: 20upx;
		font-weight: 400;
	}

	.editProfile-edit-text {
		color: #FFCF3C;
		font-size: 20upx;
		font-weight: 400;
	}

	.genderPicker {
		width: 100px;
		height: 28%;
		display: flex;
		margin-left: -10px;
		justify-content: space-around;
	}





	.gender-text {}


	.major-pick-style {
		position: absolute;
		z-index: 999;
		top: 30px;
		left: -10px;
		/* 	top:30px;
		right: -20upx;
		width:72px;
		height:69px; */
		/* background:rgba(255,255,255,1);
		box-shadow:0px 0px 6px rgba(0,0,0,0.16);
		opacity:1;
		border-radius:8px;
		overflow:auto; */
	}

	.yearPicker {
		position: absolute;
		width: 5;
		/* 在此更改按钮高度 */
		height: 30px;
		/* 保证内容剧中 */
		display: flex;
		justify-content: space-between;
	}

	.yearPicker-button {
		height: 100%;
		width: 122upx;
		display: flex;
		justify-content: left;
		align-item: center;
		position: relative;
	}

	.year-pointer {
		width: 22upx;
		height: 100%;
	}

	.year-pointerIcon {
		width: 36upx;
		height: 36upx;
		margin-top: 10upx;
	}

	.yearPicker-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 500;
		color: #888888;
	}

	.degreePicker {
		/* 在此更改按钮高度 */
		height: 30px;
		/* 保证内容剧中 */
		display: flex;
		justify-content: space-between;
	}

	.degree-pick-style {
		position: absolute;
		z-index: 888;
		left: -7px;
		top: 30px;
		/* width:72px;
		height:69px;
		background:rgba(255,255,255,1);
		box-shadow:0px 0px 6px rgba(0,0,0,0.16);
		opacity:1;
		border-radius:8px;
		overflow:auto; */
	}
</style>

<style>
	.nickname,
	.gender {
		position: relative;
		vertical-align: top;
		display: inline-block;
		width: 95px;
		height: 45px;
		margin: 0 calc((50% - 95px)/2) 18px;
		position: relative;
		overflow: visible;
	}

	.nickname .text,
	.gender .text {
		height: 11px;
		line-height: 11px;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
		overflow: visible;
	}

	.nickname .second_line,
	.gender .second_line {
		height: 34px;
		font-size: 17px;
		line-height: 34px;
		color: rgba(53, 53, 53, 1);
	}

	.nickname .text_limit {
		width: 46px;
		height: 11px;
		position: absolute;
		left: 25px;
		top: 0;
		font-size: 11px;
		color: rgba(178, 178, 178, 1);
	}

	.nickname input {
		padding-top: 6px;
		border-bottom: 1upx solid #C0C0C0;
		height: 27px;
		font-size: 17px;
		line-height: 27px;
		color: rgba(53, 53, 53, 1);
		text-align: left;
		min-height: 1upx;
		overflow: visible;
	}

	.genderPicker-button {
		width: 41px;
		height: 23px;
		border: 1px solid rgba(255, 93, 93, 1);
		opacity: 1;
		border-radius: 4px;
		color: rgba(255, 93, 93, 1);
		font-weight: 500;
		text-align: center;
		line-height: 23px;
		align-self: flex-end;
		font-size: 16px;
		font-weight: 550;
	}

	.genderPicker-buttonclick {
		width: 41px;
		height: 23px;
		border: 1px solid blue;
		border-radius: 4px;
		background-color: blue;
		color: rgba(255, 255, 255, 1);
		text-align: center;
		align-self: flex-end;
		font-weight: 500;
		line-height: 23px;

		font-size: 16px;
		font-weight: 550;
	}
	.year-pick-style {
		position: absolute;
		z-index: 999;
		bottom: 8px;
		left: 0;
	}
	
</style>
