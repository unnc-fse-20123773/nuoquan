(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/profile/profile"],{"105b":function(e,r,i){"use strict";i.r(r);var t=i("8cea"),n=i("d665");for(var a in n)"default"!==a&&function(e){i.d(r,e,function(){return n[e]})}(a);i("c847");var s=i("2877"),o=Object(s["a"])(n["default"],t["a"],t["b"],!1,null,null,null);r["default"]=o.exports},"8cea":function(e,r,i){"use strict";var t=function(){var e=this,r=e.$createElement;e._self._c},n=[];i.d(r,"a",function(){return t}),i.d(r,"b",function(){return n})},a626:function(e,r,i){},c4bf:function(e,r,i){"use strict";(function(e){Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var t=function(){return i.e("components/mypicker").then(i.bind(null,"5cbb"))},n={data:function(){for(var e=new Date,r=[],i=e.getFullYear(),t=i;t<=i+4;t++)r.push(t);var n=["AEE","ABE","CS","CEE","CIVE","ECON","EEE","ENGL","GEOG","IC","IS","MATH","PDM","NUBS"],a=["高中","本科","研究生"];return{genderList:["女","男","其他"],years:r,yearPickerVal:[],majors:n,majorPickerVal:[],degrees:a,degreePickerVal:[],gender:2,year:r[0],major:n[0],degree:a[1],degreeDB:"1",isEdit:!1,userInfo:"",yearPicker:!1,majorPicker:!1,degreePicker:!1}},components:{mypicker:t},onLoad:function(){e.setNavigationBarTitle({title:"个人信息"});var r=this.getGlobalUserInfo();this.userInfo=r;var i=r.gender,t=r.graduationYear,n=r.major,a=r.degree;this.isNull(i)||(this.gender=i),this.isNull(t)||(this.year=t),this.isNull(n)||(this.major=n),this.isNull(a)||(this.degree=this.degrees[a],this.degreeDB=a)},methods:{genderChanger:function(e){this.gender==e?(this.gender=-1,console.log(e)):this.gender=e},yearPickerChanger:function(){0==this.yearPicker?this.yearPicker=!0:this.yearPicker=!1},majorPickerChanger:function(){0==this.majorPicker?this.majorPicker=!0:this.majorPicker=!1},degreePickerChanger:function(){0==this.degreePicker?this.degreePicker=!0:this.degreePicker=!1},editProfile:function(e){0==e?this.isEdit=!0:1==e&&(this.isEdit=!1),console.log(this.isEdit)},cancle:function(){this.editProfile(this.isEdit)},yearChange:function(e){var r=this.years[e];this.year=r,this.yearPickerVal[0]=e},majorChange:function(e){var r=this.majors[e];this.major=r,this.majorPickerVal[0]=e},degreeChange:function(e){var r=this.degrees[e];this.degree=r,this.degreeDB=e[0],this.degreePickerVal[0]=e},formSubmit:function(r){var i=this,t=r.detail.value,n={id:this.userInfo.id,nickname:t.nickname,gender:this.gender,email:t.email,graduationYear:this.year,major:this.major,degree:this.degreeDB},a=this;e.request({url:a.$serverUrl+"/user/updateUser",method:"POST",data:JSON.stringify(n),header:{"content-type":"application/json"},success:function(e){if(200==e.data.status){var r=e.data.data,t=i.myUser(r);i.setGlobalUserInfo(t),i.userInfo=t,console.log(i.userInfo)}}}),this.editProfile(this.isEdit)},getIndex:function(e,r){for(var i=0;i<e.length;i++)if(e[i]==r)return i;return-1},test:function(e){console.log(e)}}};r.default=n}).call(this,i("543d")["default"])},c847:function(e,r,i){"use strict";var t=i("a626"),n=i.n(t);n.a},d665:function(e,r,i){"use strict";i.r(r);var t=i("c4bf"),n=i.n(t);for(var a in t)"default"!==a&&function(e){i.d(r,e,function(){return t[e]})}(a);r["default"]=n.a}},[["7153","common/runtime","common/vendor"]]]);