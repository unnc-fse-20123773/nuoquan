(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/cmt-likedetail/cmt-likedetail"],{"4ee1":function(t,e,n){"use strict";n.r(e);var i=n("6b7c"),o=n("5cb6");for(var s in o)"default"!==s&&function(t){n.d(e,t,function(){return o[t]})}(s);n("c9a5");var r=n("2877"),a=Object(r["a"])(o["default"],i["a"],i["b"],!1,null,null,null);e["default"]=a.exports},"5cb6":function(t,e,n){"use strict";n.r(e);var i=n("88c9"),o=n.n(i);for(var s in i)"default"!==s&&function(t){n.d(e,t,function(){return i[t]})}(s);e["default"]=o.a},"6b7c":function(t,e,n){"use strict";var i=function(){var t=this,e=t.$createElement;t._self._c},o=[];n.d(e,"a",function(){return i}),n.d(e,"b",function(){return o})},"88c9":function(t,e,n){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var i=o(n("a34a"));function o(t){return t&&t.__esModule?t:{default:t}}function s(t,e,n,i,o,s,r){try{var a=t[s](r),c=a.value}catch(u){return void n(u)}a.done?e(c):Promise.resolve(c).then(i,o)}function r(t){return function(){var e=this,n=arguments;return new Promise(function(i,o){var r=t.apply(e,n);function a(t){s(r,i,o,a,c,"next",t)}function c(t){s(r,i,o,a,c,"throw",t)}a(void 0)})}}var a={data:function(){return{scrollLeft:0,isClickChange:!1,currentTab:"",menuTabs:[{name:"点赞"},{name:"评论"}],swiperDataList:[[],[]],likeList:"",commentList:"",screenWidth:350,scrollTop:0,old:{scrollTop:0},LIKEARTICLE:this.netty.LIKEARTICLE,LIKECOMMENT:this.netty.LIKECOMMENT,COMMENTARTICLE:this.netty.COMMENTARTICLE,COMMENTCOMMENT:this.netty.COMMENTCOMMENT,serverUrl:this.$serverUrl,userInfo:"",likePage:1,commentPage:1}},onLoad:function(e){var n=JSON.parse(e.currentTab);t.setNavigationBarTitle({title:"消息列表"}),this.currentTab=n;var i=t.getSystemInfoSync().screenWidth;this.screenWidth=i;var o=this.getGlobalUserInfo();this.userInfo=o,this.likeList=this.notification.getLikeMsg(this.likePage),this.commentList=this.notification.getCommentMsg(this.commentPage),console.log(this.likeList),console.log(this.commentList)},onShow:function(){},changeIndicatorDots:function(t){this.indicatorDots=!this.indicatorDots},changeAutoplay:function(t){this.autoplay=!this.autoplay},intervalChange:function(t){this.interval=t.target.value},durationChange:function(t){this.duration=t.target.value},onPullDownRefresh:function(){console.log("refresh"),setTimeout(function(){t.stopPullDownRefresh()},1e3)},methods:{swichMenu:function(){var t=r(i.default.mark(function t(e){return i.default.wrap(function(t){while(1)switch(t.prev=t.next){case 0:if(this.currentTab!=e){t.next=4;break}return t.abrupt("return",!1);case 4:0==e?(this.likePage=1,this.likeList=this.notification.getLikeMsg(this.likePage),this.$store.commit("setLikeMsgCount",0)):(this.commentPage=1,this.commentList=this.notification.getCommentMsg(this.commentPage),this.$store.commit("setCommentMsgCount",0)),this.currentTab=e,this.setScrollLeft(e);case 7:case"end":return t.stop()}},t,this)}));function e(e){return t.apply(this,arguments)}return e}(),swiperChange:function(){var t=r(i.default.mark(function t(e){var n;return i.default.wrap(function(t){while(1)switch(t.prev=t.next){case 0:n=e.target.current,this.setScrollLeft(n),this.currentTab=n;case 3:case"end":return t.stop()}},t,this)}));function e(e){return t.apply(this,arguments)}return e}(),setScrollLeft:function(){var e=r(i.default.mark(function e(n){var o,s,r,a;return i.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:o=0,s=0;case 2:if(!(s<=n)){e.next=10;break}return e.next=5,this.getWidth("tabNum"+s);case 5:r=e.sent,o+=r.width;case 7:s++,e.next=2;break;case 10:a=t.getSystemInfoSync().windowWidth,this.scrollLeft=o>a?o-a:0;case 12:case"end":return e.stop()}},e,this)}));function n(t){return e.apply(this,arguments)}return n}(),getWidth:function(e){return new Promise(function(n,i){t.createSelectorQuery().select("#"+e).fields({size:!0,scrollOffset:!0},function(t){n(t)}).exec()})},upper:function(t){console.log(t)},scroll:function(t){console.log(t),this.old.scrollTop=t.detail.scrollTop},goTop:function(t){this.scrollTop=this.old.scrollTop,this.$nextTick(function(){this.scrollTop=0})},loadMore:function(e){if(console.log("正在加载更多数据。。。"),t.showLoading({title:"加载中"}),0==e){var n=this.likePage+1,i=this.notification.getLikeMsg(n);null!=i?(this.likeList=this.likeList.concat(i),this.likePage++):t.showToast({title:"No more",duration:2e3,icon:"none"}),t.hideLoading()}else{n=this.commentPage+1,i=this.notification.getCommentMsg(n);null!=i?(this.commentList=this.commentList.concat(i),this.commentPage++):t.showToast({title:"No more",duration:2e3,icon:"none"}),t.hideLoading()}},goToPersonPublic:function(e){t.navigateTo({url:"/pages/personpublic/personpublic?userId="+e})},goToArticle:function(e){t.navigateTo({url:"../detail/detail?data="+JSON.stringify(e)})},goToComment:function(e){var n=this;t.request({url:n.$serverUrl+"/article/getArticleById",method:"POST",data:{articleId:e,userId:n.userInfo.id},header:{"content-type":"application/x-www-form-urlencoded"},success:function(e){if(200==e.data.status){var n=e.data.data;t.navigateTo({url:"../detail/detail?data="+JSON.stringify(n)})}}})}}};e.default=a}).call(this,n("543d")["default"])},c165:function(t,e,n){},c9a5:function(t,e,n){"use strict";var i=n("c165"),o=n.n(i);o.a}},[["fb70","common/runtime","common/vendor"]]]);