(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/followlist/followlist"],{"5f71":function(t,e,n){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var o,s=i(n("a34a"));function i(t){return t&&t.__esModule?t:{default:t}}function r(t,e,n,o,s,i,r){try{var a=t[i](r),l=a.value}catch(c){return void n(c)}a.done?e(l):Promise.resolve(l).then(o,s)}function a(t){return function(){var e=this,n=arguments;return new Promise(function(o,s){var i=t.apply(e,n);function a(t){r(i,o,s,a,l,"next",t)}function l(t){r(i,o,s,a,l,"throw",t)}a(void 0)})}}var l={data:function(){return{scrollLeft:0,isClickChange:!1,currentTab:"",menuTabs:[{name:"他关注的"},{name:"关注他的"}],swiperDataList:[[],[]],followList:"",fansList:"",myId:"",screenWidth:350,serverUrl:"",scrollTop:0,old:{scrollTop:0}}},changeIndicatorDots:function(t){this.indicatorDots=!this.indicatorDots},changeAutoplay:function(t){this.autoplay=!this.autoplay},intervalChange:function(t){this.interval=t.target.value},durationChange:function(t){this.duration=t.target.value},onLoad:function(e){var n=JSON.parse(e.data),s=n.thisUserInfo,i=n.currentTab;t.setNavigationBarTitle({title:s.nickname+"的主页"}),o=this.getGlobalUserInfo(),this.myId=o.id;var r=s.id;this.queryFansFollow(r),this.currentTab=i;var a=t.getSystemInfoSync().screenWidth;this.screenWidth=a},onPullDownRefresh:function(){console.log("refresh"),setTimeout(function(){t.stopPullDownRefresh()},1e3)},methods:{swichMenu:function(){var t=a(s.default.mark(function t(e){return s.default.wrap(function(t){while(1)switch(t.prev=t.next){case 0:if(this.currentTab!=e){t.next=4;break}return t.abrupt("return",!1);case 4:this.currentTab=e,this.setScrollLeft(e);case 6:case"end":return t.stop()}},t,this)}));function e(e){return t.apply(this,arguments)}return e}(),swiperChange:function(){var t=a(s.default.mark(function t(e){var n;return s.default.wrap(function(t){while(1)switch(t.prev=t.next){case 0:n=e.target.current,this.setScrollLeft(n),this.currentTab=n;case 3:case"end":return t.stop()}},t,this)}));function e(e){return t.apply(this,arguments)}return e}(),setScrollLeft:function(){var e=a(s.default.mark(function e(n){var o,i,r,a;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:o=0,i=0;case 2:if(!(i<=n)){e.next=10;break}return e.next=5,this.getWidth("tabNum"+i);case 5:r=e.sent,o+=r.width;case 7:i++,e.next=2;break;case 10:a=t.getSystemInfoSync().windowWidth,this.scrollLeft=o>a?o-a:0;case 12:case"end":return e.stop()}},e,this)}));function n(t){return e.apply(this,arguments)}return n}(),getWidth:function(e){return new Promise(function(n,o){t.createSelectorQuery().select("#"+e).fields({size:!0,scrollOffset:!0},function(t){n(t)}).exec()})},loadMore:function(t){console.log("正在加载更多数据。。。"),this.getDateList(t)},upper:function(t){console.log(t)},lower:function(t){console.log(t)},scroll:function(t){console.log(t),this.old.scrollTop=t.detail.scrollTop},goTop:function(t){this.scrollTop=this.old.scrollTop,this.$nextTick(function(){this.scrollTop=0})},addFollow:function(e,n){var s;console.log("加关注..."),0==e?s=this.followList:1==e&&(s=this.fansList);var i=s[n],r=this;t.request({url:r.$serverUrl+"/user/follow",method:"POST",data:{userId:i.id,fanId:o.id},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){200==t.data.status&&(i.follow=!0)}})},cancelFollow:function(e,n){var s;console.log("取关..."),0==e?s=this.followList:1==e&&(s=this.fansList);var i=s[n],r=this;t.request({url:r.$serverUrl+"/user/dontFollow",method:"POST",data:{userId:i.id,fanId:o.id},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){200==t.data.status&&(i.follow=!1)}})},queryFansFollow:function(e){var n=this;t.request({url:n.$serverUrl+"/user/queryFansAndFollow",method:"POST",data:{userId:e,myId:o.id},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){if(console.log(t),200==t.data.status){var e=t.data.data;n.followList=e.followList,n.fansList=e.fansList}}})},goToPersonPublic:function(e,n){var o;0==e?o=this.followList:1==e&&(o=this.fansList);var s=o[n].id;t.redirectTo({url:"../personpublic/personpublic?userId="+s})}}};e.default=l}).call(this,n("6e42")["default"])},"621f":function(t,e,n){"use strict";n("5ad9");var o=i(n("b0ce")),s=i(n("8833"));function i(t){return t&&t.__esModule?t:{default:t}}Page((0,o.default)(s.default))},8833:function(t,e,n){"use strict";n.r(e);var o=n("dfb9"),s=n("9f03");for(var i in s)"default"!==i&&function(t){n.d(e,t,function(){return s[t]})}(i);n("cfe9");var r=n("2877"),a=Object(r["a"])(s["default"],o["a"],o["b"],!1,null,null,null);e["default"]=a.exports},9084:function(t,e,n){},"9f03":function(t,e,n){"use strict";n.r(e);var o=n("5f71"),s=n.n(o);for(var i in o)"default"!==i&&function(t){n.d(e,t,function(){return o[t]})}(i);e["default"]=s.a},cfe9:function(t,e,n){"use strict";var o=n("9084"),s=n.n(o);s.a},dfb9:function(t,e,n){"use strict";var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("view",{attrs:{id:"public-container"}},[n("view",{attrs:{id:"public-message-futherbox"}},[n("scroll-view",{staticClass:"top-menu-view",attrs:{"scroll-x":"true","scroll-left":"scrollLeft"}},t._l(t.menuTabs,function(e,o){return n("block",{key:o},[n("view",{staticClass:"menu-one-view",attrs:{id:"tabNum"+o,eventid:"1b0af484-1-"+o},on:{click:function(e){t.swichMenu(o)}}},[n("view",{class:[t.currentTab==o?"menu-one-act":"menu-one"]},[n("view",{staticClass:"menu-one-txt",attrs:{eventid:"1b0af484-0-"+o},on:{tap:t.goTop}},[t._v(t._s(e.name))]),n("view",{staticClass:"menu-one-bottom"},[n("view",{staticClass:"menu-one-bottom-color"})])])])])})),n("swiper",{staticClass:"swiper-box-list",attrs:{current:t.currentTab,duration:"300",eventid:"1b0af484-6"},on:{change:t.swiperChange}},t._l(t.swiperDataList,function(e,o){return n("swiper-item",{key:o,staticClass:"swiper-box",attrs:{mpcomid:"1b0af484-0-"+o}},[n("scroll-view",{staticClass:"scroll-test",attrs:{"scroll-top":t.scrollTop,"scroll-y":"true","enable-back-to-top":"true",eventid:"1b0af484-5-"+o},on:{scrolltoupper:t.upper,scrolltolower:t.lower,scroll:t.scroll}},t._l(0==o?t.followList:t.fansList,function(e,s){return n("view",{key:s,staticClass:"user-operation-line"},[n("view",{staticClass:"user-one-line column_center"},[n("view",{staticClass:"touxiangBox",attrs:{eventid:"1b0af484-2-"+o+"-"+s},on:{tap:function(e){t.goToPersonPublic(o,s)}}},[n("image",{staticClass:"publicTouxiang",attrs:{mode:"scaleToFill",src:e.faceImg}})]),n("view",{staticClass:"userid"},[t._v(t._s(e.nickname))]),n("view",{staticStyle:{height:"52rpx",position:"absolute",right:"0",width:"30%"}},[e.id!=t.myId?n("view",{staticStyle:{position:"relative",width:"100%",height:"52rpx"}},[1==e.follow?n("view",{staticClass:"attentionButton super_center",attrs:{eventid:"1b0af484-3-"+o+"-"+s},on:{tap:function(e){t.cancelFollow(o,s)}}},[n("text",{staticClass:"attentionButton-text"},[t._v("已关注")])]):t._e(),0==e.follow?n("view",{staticClass:"attentionButton super_center",attrs:{eventid:"1b0af484-4-"+o+"-"+s},on:{tap:function(e){t.addFollow(o,s)}}},[n("text",{staticClass:"attentionButton-text"},[t._v("关注")])]):t._e()]):t._e()])]),n("view",{staticClass:"border-bottom-line"})])}))],1)}))],1)])},s=[];n.d(e,"a",function(){return o}),n.d(e,"b",function(){return s})}},[["621f","common/runtime","common/vendor"]]]);