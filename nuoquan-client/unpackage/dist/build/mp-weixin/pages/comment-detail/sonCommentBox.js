(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/comment-detail/sonCommentBox"],{2344:function(t,e,n){},"3d84":function(t,e,n){"use strict";n.r(e);var o=n("50fa"),i=n("b0b1");for(var a in i)"default"!==a&&function(t){n.d(e,t,function(){return i[t]})}(a);n("7518");var r=n("2877"),u=Object(r["a"])(i["default"],o["a"],o["b"],!1,null,null,null);e["default"]=u.exports},"50fa":function(t,e,n){"use strict";var o=function(){var t=this,e=t.$createElement;t._self._c},i=[];n.d(e,"a",function(){return o}),n.d(e,"b",function(){return i})},7518:function(t,e,n){"use strict";var o=n("2344"),i=n.n(o);i.a},aa69:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var o={props:{reCommentDetail:""},data:function(){return{commentDetail:this.reCommentDetail}},created:function(){console.log(this.reCommentDetail)},onLoad:function(){console.log("哈哈哈哈哈哈")},methods:{controlIputInSonCommentBox:function(){var t={toUserId:this.reCommentDetail.fromUserId,fatherCommentId:this.reCommentDetail.id,underCommentId:this.reCommentDetail.underCommentId,nickname:this.reCommentDetail.nickname};this.$emit("controlInputSignal",t)},swLikeComment:function(){this.$emit("swLikeComment",this.commentDetail)},goToPersonPublic:function(){this.$emit("goToPersonPublic",this.commentDetail.fromUserId)}}};e.default=o},b0b1:function(t,e,n){"use strict";n.r(e);var o=n("aa69"),i=n.n(o);for(var a in o)"default"!==a&&function(t){n.d(e,t,function(){return o[t]})}(a);e["default"]=i.a}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'pages/comment-detail/sonCommentBox-create-component',
    {
        'pages/comment-detail/sonCommentBox-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('543d')['createComponent'](__webpack_require__("3d84"))
        })
    },
    [['pages/comment-detail/sonCommentBox-create-component']]
]);                
