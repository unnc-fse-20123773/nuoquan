(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/detail/detail"],{"0905":function(t,e,i){"use strict";var n=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("view",{staticStyle:{height:"100%",width:"100%"}},[i("view",{staticClass:"topbar"},[i("view",{staticClass:"detailtitle"},[t._v(t._s(t.articleCard.articleTitle))])]),i("view",{staticClass:"detailmain"},[i("view",{staticClass:"detailcontent"},[t._v(t._s(t.articleCard.articleContent))]),i("view",[1==t.articleCard.imgList.length?i("view",{staticClass:"detailpics 1pic",staticStyle:{width:"100%","max-height":"400rpx",display:"flex"}},t._l(t.articleCard.imgList,function(e,n){return i("image",{key:n,staticStyle:{height:"360rpx","max-width":"180px",display:"inline-block"},attrs:{src:t.serverUrl+e.imagePath,mode:"aspectFill",eventid:"afb8b978-0-"+n},on:{tap:function(e){t.previewImg(n)},longpress:function(e){t.aboutImg(n)}}})})):4==t.articleCard.imgList.length?i("view",{staticClass:"detailpics",staticStyle:{"max-width":"400rpx","margin-left":"0"}},t._l(t.articleCard.imgList,function(e,n){return i("image",{key:n,staticClass:"detailpic",attrs:{src:t.serverUrl+e.imagePath,mode:"aspectFill",eventid:"afb8b978-1-"+n},on:{tap:function(e){t.previewImg(n)},longpress:function(e){t.aboutImg(n)}}})})):i("view",{staticClass:"detailpics"},[t._l(t.articleCard.imgList,function(e,n){return i("image",{key:n,staticClass:"detailpic",attrs:{src:t.serverUrl+e.imagePath,mode:"aspectFill",eventid:"afb8b978-2-"+n},on:{tap:function(e){t.previewImg(n)},longpress:function(e){t.aboutImg(n)}}})}),2==t.articleCard.imgList.length||5==t.imageList.length||8==t.imageList.length?i("view",{staticStyle:{width:"190rpx",height:"190rpx",margin:"6px 0"}}):t._e()],2)]),i("view",{staticClass:"tags"},t._l(t.articleCard.tagList,function(e,n){return i("view",{key:n,staticClass:"tag",style:{background:t.tagColorList[n]}},[t._v(t._s(e))])})),i("view",{staticClass:"bottombar"},[i("view",{staticStyle:{width:"70%",display:"inline-block"}},[i("image",{staticClass:"touxiang",attrs:{src:t.articleCard.faceImg,eventid:"afb8b978-3"},on:{click:function(e){t.goToPersonPublic()}}}),i("view",{staticClass:"name"},[t._v(t._s(t.articleCard.nickname))]),i("view",{staticClass:"time"},[t._v(t._s(t._f("timeDeal")(t.articleCard.createDate)))])]),i("view",{staticClass:"icons",attrs:{eventid:"afb8b978-4"},on:{tap:function(e){t.swLikeArticle()}}},[t.articleCard.isLike?t._e():i("image",{staticClass:"icon",attrs:{src:"../../static/icon/like.png"}}),t.articleCard.isLike?i("image",{staticClass:"icon",attrs:{src:"../../static/icon/liked.png"}}):t._e(),i("view",{staticClass:"icom",class:{liked:t.articleCard.isLike}},[t._v(t._s(t.articleCard.likeNum))])])]),t._m(0),t._l(t.commentList,function(e,n){return i("commentbox",{key:e.id,attrs:{commentDetail:e,eventid:"afb8b978-5-"+n,mpcomid:"afb8b978-0-"+n},on:{controlInputSignal:t.controlInput}})}),i("view",{staticStyle:{height:"50px",width:"100%","background-color":"white"},attrs:{name:"marginHelper"}}),i("view",{staticClass:"bottomLayerOfSubmit"},[i("view",{staticClass:"submitComment",attrs:{eventid:"afb8b978-6"},on:{click:function(e){t.controlInput(1)}}},[t._v("发 表 评 论")])]),i("view",{directives:[{name:"show",rawName:"v-show",value:t.showInput,expression:"showInput"}],staticClass:"bottoLayerOfInput",attrs:{eventid:"afb8b978-10"},on:{tap:function(e){t.controlInput(0)},touchmove:function(e){t.controlInput(0)}}},[i("view",{staticClass:"commentPart",style:{bottom:t.textAreaAdjust},attrs:{eventid:"afb8b978-9"},on:{click:function(t){t.stopPropagation()}}},[i("view",{staticClass:"emoji"}),i("view",{staticClass:"submit",attrs:{eventid:"afb8b978-7"},on:{click:function(e){t.saveComment()}}}),i("textarea",{directives:[{name:"model",rawName:"v-model",value:t.commentContent,expression:"commentContent"}],staticClass:"commentSth",attrs:{placeholder:t.placeholderText,focus:t.writingComment,"auto-height":"true","adjust-position":"false","show-confirm-bar":!1,eventid:"afb8b978-8"},domProps:{value:t.commentContent},on:{click:function(t){t.stopPropagation()},focus:t.popTextArea,blur:t.unpopTextArea,input:function(e){e.target.composing||(t.commentContent=e.target.value)}}})])])],2)])},a=[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("view",{staticClass:"column_center",staticStyle:{width:"100%",height:"12px",display:"flex"}},[i("view",{staticStyle:{width:"20%",color:"#888888","font-size":"15px"}},[t._v("最新评论")]),i("view",{staticClass:"fengexian",staticStyle:{height:"1px",width:"80%","background-color":"RGB(253, 217, 108)"}})])}];i.d(e,"a",function(){return n}),i.d(e,"b",function(){return a})},"190e":function(t,e,i){"use strict";var n=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("view",{staticClass:"comment",attrs:{id:t.mainComment.id}},[i("view",{staticClass:"contentarea",attrs:{eventid:"e4cb3434-0"},on:{tap:function(e){t.controlInputInComment("inComment")}}},[t._v(t._s(t.mainComment.comment))]),i("view",{staticClass:"bottombar"},[i("view",{staticStyle:{width:"70%",display:"inline-block"}},[i("image",{staticClass:"touxiang",attrs:{src:t.mainComment.faceImg,eventid:"e4cb3434-1"},on:{tap:function(e){t.goToPersonPublic(t.mainComment.fromUserId)}}}),i("text",{staticClass:"name"},[t._v(t._s(t.mainComment.nickname))]),i("text",{staticClass:"time"},[t._v(t._s(t.mainComment.timeAgo))])]),i("view",{staticClass:"icons"},[i("view",{attrs:{eventid:"e4cb3434-2"},on:{tap:function(e){t.goToCommentDetail(t.mainComment)}}},[t.RECOMMENT&&0!=t.mainComment.commentNum?t._e():i("image",{staticClass:"icon",staticStyle:{"padding-right":"23px"},attrs:{src:"../../../../static/icon/comment.png"}}),t.RECOMMENT&&t.mainComment.commentNum?i("image",{staticStyle:{height:"23px",width:"50px",position:"relative",bottom:"-5px","padding-right":"4px"},attrs:{src:"../../../../static/icon/ReComment.png"}}):t._e()]),i("view",{attrs:{eventid:"e4cb3434-3"},on:{tap:function(e){t.swLikeMainComment(t.mainComment)}}},[t.mainComment.isLike?t._e():i("image",{staticClass:"icon",attrs:{src:"../../../static/icon/like.png"}}),t.mainComment.isLike?i("image",{staticClass:"icon",attrs:{src:"../../../static/icon/liked.png"}}):t._e(),i("text",{staticClass:"icom",class:{liked:t.mainComment.isLike}},[t._v(t._s(t.mainComment.likeNum))])])])]),i("view",{directives:[{name:"show",rawName:"v-show",value:t.RECOMMENT&&t.mainComment.commentNum>0,expression:"RECOMMENT && mainComment.commentNum > 0"}],staticClass:"reCommentsArea"},[t._l(t.reCommentList,function(e,n){return i("reComment",{key:n,attrs:{reCommentDetail:e,eventid:"e4cb3434-4-"+n,mpcomid:"e4cb3434-0-"+n},on:{controlInputSignal:t.controlInputInComment,goToPersonPublic:t.goToPersonPublic,goToCommentDetail:function(e){t.goToCommentDetail(t.mainComment)}}})}),i("view",{staticStyle:{"font-size":"10px",color:"#007AFF","text-align":"right","margin-top":"5px"},attrs:{eventid:"e4cb3434-5"},on:{tap:function(e){t.goToCommentDetail(t.mainComment)}}},[t._v("共"+t._s(t.mainComment.commentNum)+"条评论")])],2),i("view",{staticClass:"fengexian1"})])},a=[];i.d(e,"a",function(){return n}),i.d(e,"b",function(){return a})},"21c4":function(t,e,i){"use strict";var n=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("view",{staticClass:"reComment"},[i("view",{staticClass:"contentarea",attrs:{eventid:"f0394b1a-0"},on:{tap:function(e){t.goToCommentDetail()}}},[t._v("回复@"+t._s(t.subComment.toNickname)+": "+t._s(t.subComment.comment))]),i("view",{staticClass:"bottombar"},[i("view",{staticStyle:{width:"70%",display:"inline-block"}},[i("image",{staticClass:"touxiang",attrs:{src:t.subComment.faceImg,eventid:"f0394b1a-1"},on:{tap:t.goToPersonPublic}}),i("text",{staticClass:"name"},[t._v(t._s(t.subComment.nickname))]),i("text",{staticClass:"time"},[t._v(t._s(t.subComment.timeAgo))])]),i("view",{staticClass:"icons",attrs:{eventid:"f0394b1a-2"},on:{tap:function(e){t.swLikeSubComment()}}},[t.subComment.isLike?t._e():i("image",{staticClass:"icon",attrs:{src:"../../../static/icon/like.png"}}),t.subComment.isLike?i("image",{staticClass:"icon",attrs:{src:"../../../static/icon/liked.png"}}):t._e(),i("text",{staticClass:"icom",class:{liked:t.subComment.isLike}},[t._v(t._s(t.subComment.likeNum))])])]),i("view",{staticStyle:{"border-top":"1px solid #DCDCDC"}})])},a=[];i.d(e,"a",function(){return n}),i.d(e,"b",function(){return a})},2645:function(t,e,i){"use strict";i.r(e);var n=i("b1b7"),a=i.n(n);for(var o in n)"default"!==o&&function(t){i.d(e,t,function(){return n[t]})}(o);e["default"]=a.a},"46f5":function(t,e,i){"use strict";i.r(e);var n=i("190e"),a=i("62f2");for(var o in a)"default"!==o&&function(t){i.d(e,t,function(){return a[t]})}(o);i("8546");var s=i("2877"),r=Object(s["a"])(a["default"],n["a"],n["b"],!1,null,"272b0e99",null);e["default"]=r.exports},"482a":function(t,e,i){"use strict";i("5ad9");var n=o(i("b0ce")),a=o(i("7e08"));function o(t){return t&&t.__esModule?t:{default:t}}Page((0,n.default)(a.default))},"48a3":function(t,e,i){"use strict";var n=i("9ee6"),a=i.n(n);a.a},"49ec":function(t,e,i){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var i={props:{reCommentDetail:{}},data:function(){return{subComment:this.reCommentDetail,userInfo:this.getGlobalUserInfo()}},created:function(){},methods:{swLikeSubComment:function(){this.subComment.isLike?(this.unLikeComment(this.subComment),this.subComment.likeNum--,console.log(this.subComment.likeNum)):(this.likeComment(this.subComment),this.subComment.likeNum++),this.subComment.isLike=!this.subComment.isLike},likeComment:function(e){console.log("点赞评论");var i=this;t.request({method:"POST",url:i.$serverUrl+"/article/userLikeComment",data:{userId:i.userInfo.id,commentId:e.id,createrId:e.fromUserId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},unLikeComment:function(e){console.log("取消点赞评论");var i=this;t.request({method:"POST",url:i.$serverUrl+"/article/userUnLikeComment",data:{userId:i.userInfo.id,commentId:e.id,createrId:e.fromUserId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},goToPersonPublic:function(){this.$emit("goToPersonPublic",this.subComment.fromUserId)},goToCommentDetail:function(){this.$emit("goToCommentDetail")}}};e.default=i}).call(this,i("6e42")["default"])},"4f96":function(t,e,i){"use strict";i.r(e);var n=i("49ec"),a=i.n(n);for(var o in n)"default"!==o&&function(t){i.d(e,t,function(){return n[t]})}(o);e["default"]=a.a},"62f2":function(t,e,i){"use strict";i.r(e);var n=i("69b5"),a=i.n(n);for(var o in n)"default"!==o&&function(t){i.d(e,t,function(){return n[t]})}(o);e["default"]=a.a},"63d9":function(t,e,i){"use strict";var n=i("cec5"),a=i.n(n);a.a},"69b5":function(t,e,i){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var n=a(i("ec8b"));function a(t){return t&&t.__esModule?t:{default:t}}var o={name:"comment",props:{commentDetail:""},components:{reComment:n.default},data:function(){return{RECOMMENT:!0,reCommentList:[],isPassingReComment:!1,mainComment:this.commentDetail,userInfo:this.getGlobalUserInfo(),totalPage:1,currentPage:1}},created:function(){var e=this,i=this.currentPage;t.$on("flashSubComment",function(t){e.mainComment.id==t&&e.getSubComments(i)}),this.getSubComments(i)},methods:{showRecommentArea:function(){this.RECOMMENT=!this.RECOMMENT,this.RECOMMENT&&this.getSubComments(this.currentPage)},getSubComments:function(e){var i=this;t.request({method:"POST",url:i.$serverUrl+"/article/getSubComments",data:{underCommentId:i.mainComment.id,userId:i.userInfo.id,page:e},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){200==t.data.status&&(i.reCommentList="",i.$nextTick(function(){var e=2;t.data.data.rows.length>e?i.reCommentList=t.data.data.rows.slice(0,e):i.reCommentList=t.data.data.rows}))}})},controlInputInComment:function(t){if("inComment"==t)var e={mode:"re-co",toUserId:this.mainComment.fromUserId,underCommentId:this.mainComment.id,fatherCommentId:this.mainComment.id,nickname:this.mainComment.nickname};else e=t;console.log("receive control input request, in comment"),console.log(e),this.$emit("controlInputSignal",e)},swLikeMainComment:function(t){t.isLike?(this.unLikeComment(t),t.likeNum--):(this.likeComment(t),t.likeNum++),t.isLike=!t.isLike},likeComment:function(e){console.log("点赞评论");var i=this;t.request({method:"POST",url:i.$serverUrl+"/article/userLikeComment",data:{userId:i.userInfo.id,commentId:e.id,createrId:e.fromUserId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},unLikeComment:function(e){console.log("取消点赞评论");var i=this;t.request({method:"POST",url:i.$serverUrl+"/article/userUnLikeComment",data:{userId:i.userInfo.id,commentId:e.id,createrId:e.fromUserId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},goToPersonPublic:function(e){t.navigateTo({url:"/pages/personpublic/personpublic?userId="+e})},goToCommentDetail:function(e){t.navigateTo({url:"/pages/comment-detail/comment-detail?data="+JSON.stringify(e)})}}};e.default=o}).call(this,i("6e42")["default"])},"7e08":function(t,e,i){"use strict";i.r(e);var n=i("0905"),a=i("2645");for(var o in a)"default"!==o&&function(t){i.d(e,t,function(){return a[t]})}(o);i("63d9"),i("48a3");var s=i("2877"),r=Object(s["a"])(a["default"],n["a"],n["b"],!1,null,"f9f64494",null);e["default"]=r.exports},8546:function(t,e,i){"use strict";var n=i("9a8f"),a=i.n(n);a.a},"9a8f":function(t,e,i){},"9ee6":function(t,e,i){},b1b7:function(t,e,i){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var n=a(i("46f5"));function a(t){return t&&t.__esModule?t:{default:t}}var o={data:function(){return{imgList:[],singleImgState:"0",userInfo:{},articleCard:"",commentContent:"",commentList:{},showInput:!1,writingComment:!1,placeholderText:"评论点什么吧......",inputData:{},submitData:{},imgIndex:"",serverUrl:this.$serverUrl,textAreaAdjust:"",tagColorList:[],totalPage:1,currentPage:1}},components:{commentbox:n.default},filters:{timeDeal:function(t){t=new Date(t);var e,i=[t.getFullYear(),t.getMonth()+1,t.getDate(),t.getHours(),t.getMinutes(),t.getSeconds()],n=t.getTime(),a=new Date,o=a.getTime(),s=0;return s=o-n,e=s<=6e4?"刚刚":6e4<s&&s<=36e5?Math.round(s/6e4)+"分钟前":36e5<s&&s<=864e5?Math.round(s/36e5)+"小时前":864e5<s&&s<=1296e6?Math.round(s/864e5)+"天前":s>1296e6&&i[0]==a.getFullYear()?i[1]+"-"+i[2]+" "+i[3]+":"+i[4]:i[0]+"-"+i[1]+"-"+i[2]+" "+i[3]+":"+i[4],e}},onReachBottom:function(){this.loadMore()},onUnload:function(){t.$emit("updateArticle",this.articleCard),console.log("返回")},onLoad:function(t){this.articleCard=JSON.parse(t.data);var e=this.getGlobalUserInfo();this.isNull(e)||(this.userInfo=this.getGlobalUserInfo());var i=this.currentPage;if(this.getComments(i),!this.isNull(this.articleCard.tagList))for(var n=this.tagColors,a=0;a<this.articleCard.tagList.length;a++){var o=Math.floor(Math.random()*n.length);this.tagColorList.push(n[o])}},methods:{popTextArea:function(t){console.log("展开"),console.log(t),console.log(t.detail.height),this.textAreaAdjust=t.detail.height/3+"px"},unpopTextArea:function(t){console.log("收起"),console.log(t),this.textAreaAdjust=""},saveComment:function(){this.submitData.comment=this.commentContent,this.submitData.fromUserId=this.userInfo.id,this.submitData.articleId=this.articleCard.id,console.log(this.submitData);var e=this;""==this.commentContent?t.showToast({title:"好像忘写评论了哦~",duration:1e3,icon:"none"}):t.request({url:e.$serverUrl+"/article/saveComment",method:"POST",data:this.submitData,success:function(t){e.writingComment=!1,e.commentContent="",e.showInput=!1,e.commentList="",e.$nextTick(function(){e.getComments(1)})}}),this.articleCard.commentNum++},getComments:function(e){var i=this;t.showLoading({title:"加载中..."}),t.request({method:"POST",url:i.$serverUrl+"/article/getMainComments",data:{articleId:i.articleCard.id,userId:i.userInfo.id,page:e},header:{"content-type":"application/x-www-form-urlencoded"},success:function(n){if(200==n.data.status){1==e&&(i.commentList=[]),console.log(n);var a=n.data.data.rows,o=i.commentList;i.commentList=o.concat(a),i.currentPage=e,i.totalPage=n.data.data.total}else console.log(n);t.hideLoading()}})},loadMore:function(){var e=this,i=e.currentPage;console.log(i);var n=e.totalPage;if(console.log(n),!(e.commentList.length<10))if(i==n)t.showToast({title:"没有更多评论了",icon:"none",duration:1e3});else{var a=i+1;e.getComments(a)}},singleImgeFit:function(t){var e=t.detail.height,i=t.detail.width;this.singleImgState=e>=i?0:1},controlInput:function(t){0!=t&&1!=t?(this.placeholderText="回复 @"+t.nickname+" 的评论",delete t.nickname,this.submitData=t,this.writingComment=!0,this.showInput=!0):1==t?(this.submitData.toUserId=this.articleCard.userId,this.showInput=!0,this.writingComment=!0):(console.log("this is control input in detail. a ==0, EXIT"),this.submitData={},this.placeholderText="评论",this.showInput=!1,this.writingComment=!1)},swLikeArticle:function(){this.articleCard.isLike?(this.unLikeArticle(),this.articleCard.likeNum--):(this.likeArticle(),this.articleCard.likeNum++),this.articleCard.isLike=!this.articleCard.isLike},likeArticle:function(){console.log("点赞文章");var e=this;t.request({method:"POST",url:e.$serverUrl+"/article/userLikeArticle",data:{userId:e.userInfo.id,articleId:e.articleCard.id,articleCreaterId:e.articleCard.userId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},unLikeArticle:function(){console.log("取消点赞文章");var e=this;t.request({method:"POST",url:e.$serverUrl+"/article/userUnLikeArticle",data:{userId:e.userInfo.id,articleId:e.articleCard.id,articleCreaterId:e.articleCard.userId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},goToPersonPublic:function(){t.navigateTo({url:"/pages/personpublic/personpublic?userId="+this.articleCard.userId})},previewImg:function(e){for(var i,n=this.articleCard.imgList,a=[],o=0;o<n.length;o++)i=this.serverUrl+n[o].imagePath,a=a.concat(i);t.previewImage({current:e,urls:a})},aboutImg:function(e){var i=this;console.log(this.articleCard.imgList[e].imagePath),t.showActionSheet({itemList:["保存图片到本地"],success:function(n){console.log(n.tapIndex),0==n.tapIndex&&(t.showLoading({title:"下载中..."}),t.downloadFile({url:i.serverUrl+i.articleCard.imgList[e].imagePath,success:function(e){200==e.statusCode&&t.saveImageToPhotosAlbum({filePath:e.tempFilePath,success:function(){console.log("save success"),t.hideLoading()},fail:function(){console.log("save failed"),t.hideLoading(),t.showToast({title:"保存失败",icon:"none",duration:1e3})}})}}))}})}}};e.default=o}).call(this,i("6e42")["default"])},c6ae:function(t,e,i){},cec5:function(t,e,i){},ec8b:function(t,e,i){"use strict";i.r(e);var n=i("21c4"),a=i("4f96");for(var o in a)"default"!==o&&function(t){i.d(e,t,function(){return a[t]})}(o);i("fdb7");var s=i("2877"),r=Object(s["a"])(a["default"],n["a"],n["b"],!1,null,null,null);e["default"]=r.exports},fdb7:function(t,e,i){"use strict";var n=i("c6ae"),a=i.n(n);a.a}},[["482a","common/runtime","common/vendor"]]]);