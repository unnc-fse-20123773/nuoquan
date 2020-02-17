<template>
	<view id="profile-container">
		<view id="yellow-box"></view>
		<form @submit="formSubmit" @reset="formReset">
			<view class="profile-basicinfo-card column_center">
				<image class="profileTouxiang" mode="aspectFill" :src="userInfo.faceImg" height:160upx width:160upx></image>
				<!-- 一般状态 -->
				<view class="profileText-box" v-if="isEdit == false">
					<view class="nickname">
						<text class="left-profileText1 ">昵称</text>
						<text class="left-profileTexta" v-if="userInfo.nickname!=null ">{{userInfo.nickname}}</text>
						<text class="left-profileTexta" v-else>待设置待设置待设</text>
					</view>
					<view class="gender">
						<text class="right-profileText1">性别</text>
						<text class="right-profileTexta" v-if="userInfo.gender==null || userInfo.gender==-1">待设置</text>
						<text class="right-profileTexta" v-else>{{genderList[userInfo.gender]}}</text>
					</view>
					<view class="graduationYear">
						<text class="left-profileText2">毕业年份</text>
						<text class="left-profileTextb">{{userInfo.graduationYear}}</text>
					</view>
					<view class="major">
						<text class="left-profileText2">专业</text>
						<text class="left-profileTextb">{{userInfo.major}}</text>
					</view>
					<view class="degree">
						<text class="right-profileText2">学位</text>
						<text class="right-profileTextb">{{degrees[userInfo.degree]}}</text>
					</view>
				</view>
				<!-- 修改时状态 -->
				<view class="profileText-box" v-if="isEdit == true">
					<view class="nickname">
						<text class="left-profileText1 ">昵称<text class="right-remind " >   最长 8 位</text></text>
						<input class="right-profileText1-1" style="text-align:left;min-height: 1upx;height:26px;" maxlength="8" :value="userInfo.nickname" name="nickname" />
						<view class="input-border"></view>
					</view>
					<view class="gender">
						<text class="right-profileText1">性别</text>
						<view class="genderPicker">
							<view :class="[gender == 1 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(1)">
								<text class="gender-text">男</text></view>
							
							<view :class="[gender == 0 ? 'genderPicker-buttonclick' : 'genderPicker-button']" @click="genderChanger(0)">
								<text class="gender-text">女</text></view>
						</view>
					</view>
					<view class="graduationYear">
						<text class="left-profileText2">毕业年份</text>
						<view class="yearPicker">
							<view class="yearPicker-button" @click="yearPickerChanger()">
							<text class="yearPicker-text">{{year}}</text>
							<view class="year-pointer">
								<image class="year-pointerIcon" src="../../static/icon/angle-down.png" mode="scaleToFill"></image>
							</view>
							<mypicker class="year-pick-style" v-if="yearPicker == true" :dataList="years" :value="yearPickerVal" @change="yearChange"
							 @tapBackground="yearPickerChanger()" ></mypicker>
						</view>
		
					</view>
	
					</view>
					
					<view class="major">
						<text class="right-profileText2">专业</text>
						<view class="majorPicker">
							<view class="yearPicker-button" @click="majorPickerChanger()">
							<text class="yearPicker-text">{{major}}</text>
							<view class="year-pointer">
								<image class="year-pointerIcon" src="../../static/icon/angle-down.png" mode="scaleToFill"></image>
							</view>
						</view>
						<mypicker class="major-pick-style" v-if="majorPicker == true" :dataList="majors" :value="majorPickerVal" @change="majorChange"
						 @tapBackground="majorPickerChanger()"></mypicker>
					</view>
					</view>
					<view class="degree">
						<text class="left-profileText2">学位</text>
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
						<mypicker class="degree-pick-style" v-if="degreePicker == true" :dataList="degrees" :value="degreePickerVal"
						 @change="degreeChange" @tapBackground="degreePickerChanger()"></mypicker>	
					</view>
					</view>
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
					 :value="userInfo.email" name="email"/>
						<view class="input-border2"></view>
					</view>
				</view>

			</view>
			<view style="display: flex; height: 5%;">
				<button class="editProfile super_center" v-if="isEdit == false" @click="editProfile(isEdit)">
					<text class="editProfile-text">修改</text>
				</button>
				<button class="editProfile-edit super_center" v-if="isEdit == true" formType="submit">
					<text class="editProfile-edit-text">完成</text>
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
		
		onShow() {
			this.setTabBarIndex(3) //index为当前tab的索引
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
		position: relative;
		width: 86%;
		height: 300px;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	    justify-content: center;
		margin-left: auto;
		margin-right: auto;
		margin-top: 50px;
		margin-bottom: 10px;
	}

	.profileTouxiang {
		position:absolute;
		top: -100upx;
		left:calc(50% - 80upx);
		width: 160upx;
		height: 160upx;
		border-radius: 120px;
	}

	.nickname {
		width:128px;
		overflow:hidden;
		position:absolute;
		left:13%;
		top:25%;
	}
	.left-profileText1 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
		display: block;
	}
	
	.right-remind {
		color: #888888;
		font-size: 20upx;
		font-weight: 500;
	}

	.left-profileTexta {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;
	}
	.gender {
		width:128upx;
		position:absolute;
		right:13%;
		top:25%;
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
		width:128upx;
		position:absolute;
		left:13%;
		top:50%;
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
		width:128upx;
		position:absolute;
		right:13%;
		top:50%;
	}
	.left-profileTextb {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display:block;
	}
	.right-profileTextb {
		color: #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display:block;
	}
	.degree {
		width:128upx;
		position:absolute;
		left:13%;
		top:75%;
	}
	
	.input-border {
		border-bottom: 1upx solid #C0C0C0;
		width: 80%;
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
		margin-left:7%;
		position:relative;
		width: 86%;
		background-color: white;
		border-radius: 20upx;
		box-shadow: 0upx 0upx 11upx 1upx #A6A6A6;
	}
	.email {
		position:absolute;
		left:13%;
		top:25%;
	}
	.profilemoreText1 {
		color: #888888;
		font-size: 11px;
		font-weight: 500;
		display: block;
	}
	.profilemoreTexta {
		color:  #3D3D3D;
		font-size: 17px;
		font-weight: 500;
		display: block;	
	}
	.editProfile {
		position:absolute;
		right:10%;
		top:10%;		
		width:68px;
		height:26px;
		background:rgba(255,201,90,1);
		opacity:1;
		border-radius:4px;
	}

	.editProfile-edit {
		position:absolute;
		right:10%;
		top:10%;
		width:68px;
		height:26px;
		border:1px solid rgba(255,201,90,1);
		opacity:1;
		border-radius:4px;
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
		width: 94%;
		height: 30%;
		display: flex;
		justify-content: space-around;
	}

	.genderPicker-button {
		width:41px;
		height:23px;
		border:1px solid rgba(255,93,93,1);
		opacity:1;
		border-radius:4px;
		margin-right: 6px;
		display: flex;
		justify-content: center;
		font-size: medium;
		font-weight: 500;
		align-item: center;
	}

	.genderPicker-buttonclick {
		width:41px;
		height:23px;
		margin-right: 6px;
		border:1px solid blue;
		border-radius: 4px;
		background-color:blue;
		justify-content: center;
		display: flex;
		align-item: center;
		font-size: medium;
		font-weight: 500;
	}

	.gender-text {
		margin-top: 3px;
		font-size: 16px;
		font-weight: 550;
	}

	.year-pick-style {
		position: absolute;
		z-index: 999;
		margin-top: 25px;
	}

	.major-pick-style {
		position: absolute;
		z-index:999;

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
		height: 30px; /* 保证内容剧中 */
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
		height: 30px; /* 保证内容剧中 */
		display: flex;
		justify-content: space-between;
	}

	.degree-pick-style {
		position: absolute;
		z-index: 999;
		/* width:72px;
		height:69px;
		background:rgba(255,255,255,1);
		box-shadow:0px 0px 6px rgba(0,0,0,0.16);
		opacity:1;
		border-radius:8px;
		overflow:auto; */
	}
</style>
