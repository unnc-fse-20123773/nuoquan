(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/detail/detail"],{"117f":function(t,e,i){"use strict";var n=function(){var t=this,e=t.$createElement,i=(t._self._c,t._f("timeDeal")(t.articleCard.createDate));t.$mp.data=Object.assign({},{$root:{f0:i}})},o=[];i.d(e,"a",function(){return n}),i.d(e,"b",function(){return o})},"266a":function(t,e,i){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var n=function(){return i.e("components/comment").then(i.bind(null,"f271"))},o={data:function(){return{imgList:[],singleImgState:"0",userInfo:{},articleCard:"",commentContent:"",commentList:{},showInput:!1,writingComment:!1,placeholderText:"评论点什么吧......",inputData:{},submitData:{},imgIndex:"",serverUrl:this.$serverUrl,textAreaAdjust:"",tagColorList:[],totalPage:1,currentPage:1}},components:{commentbox:n},filters:{timeDeal:function(t){t=new Date(t);var e,i=[t.getFullYear(),t.getMonth()+1,t.getDate(),t.getHours(),t.getMinutes(),t.getSeconds()],n=t.getTime(),o=new Date,a=o.getTime(),r=0;return r=a-n,e=r<=6e4?"刚刚":6e4<r&&r<=36e5?Math.round(r/6e4)+"分钟前":36e5<r&&r<=864e5?Math.round(r/36e5)+"小时前":864e5<r&&r<=1296e6?Math.round(r/864e5)+"天前":r>1296e6&&i[0]==o.getFullYear()?i[1]+"-"+i[2]+" "+i[3]+":"+i[4]:i[0]+"-"+i[1]+"-"+i[2]+" "+i[3]+":"+i[4],e}},onReachBottom:function(){this.loadMore()},onUnload:function(){t.$emit("updateArticle",this.articleCard),console.log("返回")},onLoad:function(t){this.articleCard=JSON.parse(t.data);var e=this.getGlobalUserInfo();this.isNull(e)||(this.userInfo=this.getGlobalUserInfo());var i=this.currentPage;if(this.getComments(i),!this.isNull(this.articleCard.tagList))for(var n=this.tagColors,o=0;o<this.articleCard.tagList.length;o++){var a=Math.floor(Math.random()*n.length);this.tagColorList.push(n[a])}},methods:{popTextArea:function(t){console.log("展开"),console.log(t),console.log(t.detail.height),this.textAreaAdjust=t.detail.height/3+"px"},unpopTextArea:function(t){console.log("收起"),console.log(t),this.textAreaAdjust=""},saveComment:function(){this.submitData.comment=this.commentContent,this.submitData.fromUserId=this.userInfo.id,this.submitData.articleId=this.articleCard.id,console.log(this.submitData);var e=this;""==this.commentContent?t.showToast({title:"好像忘写评论了哦~",duration:1e3,icon:"none"}):t.request({url:e.$serverUrl+"/article/saveComment",method:"POST",data:this.submitData,success:function(t){e.writingComment=!1,e.commentContent="",e.showInput=!1,e.commentList="",e.$nextTick(function(){e.getComments(1)})}}),this.articleCard.commentNum++},getComments:function(e){var i=this;t.showLoading({title:"加载中..."}),t.request({method:"POST",url:i.$serverUrl+"/article/getMainComments",data:{articleId:i.articleCard.id,userId:i.userInfo.id,page:e},header:{"content-type":"application/x-www-form-urlencoded"},success:function(n){if(200==n.data.status){1==e&&(i.commentList=[]),console.log(n);var o=n.data.data.rows,a=i.commentList;i.commentList=a.concat(o),i.currentPage=e,i.totalPage=n.data.data.total}else console.log(n);t.hideLoading()}})},loadMore:function(){var e=this,i=e.currentPage;console.log(i);var n=e.totalPage;if(console.log(n),!(e.commentList.length<10))if(i==n)t.showToast({title:"没有更多评论了",icon:"none",duration:1e3});else{var o=i+1;e.getComments(o)}},singleImgeFit:function(t){var e=t.detail.height,i=t.detail.width;this.singleImgState=e>=i?0:1},controlInput:function(t){0!=t&&1!=t?(this.placeholderText="回复 @"+t.nickname+" 的评论",delete t.nickname,this.submitData=t,this.writingComment=!0,this.showInput=!0):1==t?(this.submitData.toUserId=this.articleCard.userId,this.showInput=!0,this.writingComment=!0):(console.log("this is control input in detail. a ==0, EXIT"),this.submitData={},this.placeholderText="评论",this.showInput=!1,this.writingComment=!1)},swLikeArticle:function(){this.articleCard.isLike?(this.unLikeArticle(),this.articleCard.likeNum--):(this.likeArticle(),this.articleCard.likeNum++),this.articleCard.isLike=!this.articleCard.isLike},likeArticle:function(){console.log("点赞文章");var e=this;t.request({method:"POST",url:e.$serverUrl+"/article/userLikeArticle",data:{userId:e.userInfo.id,articleId:e.articleCard.id,articleCreaterId:e.articleCard.userId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},unLikeArticle:function(){console.log("取消点赞文章");var e=this;t.request({method:"POST",url:e.$serverUrl+"/article/userUnLikeArticle",data:{userId:e.userInfo.id,articleId:e.articleCard.id,articleCreaterId:e.articleCard.userId},header:{"content-type":"application/x-www-form-urlencoded"},success:function(t){console.log(t)}})},goToPersonPublic:function(){t.navigateTo({url:"/pages/personpublic/personpublic?userId="+this.articleCard.userId})},previewImg:function(e){for(var i,n=this.articleCard.imgList,o=[],a=0;a<n.length;a++)i=this.serverUrl+n[a].imagePath,o=o.concat(i);t.previewImage({current:e,urls:o})},aboutImg:function(e){var i=this;console.log(this.articleCard.imgList[e].imagePath),t.showActionSheet({itemList:["保存图片到本地"],success:function(n){console.log(n.tapIndex),0==n.tapIndex&&(t.showLoading({title:"下载中..."}),t.downloadFile({url:i.serverUrl+i.articleCard.imgList[e].imagePath,success:function(e){200==e.statusCode&&t.saveImageToPhotosAlbum({filePath:e.tempFilePath,success:function(){console.log("save success"),t.hideLoading()},fail:function(){console.log("save failed"),t.hideLoading(),t.showToast({title:"保存失败",icon:"none",duration:1e3})}})}}))}})}}};e.default=o}).call(this,i("543d")["default"])},"2d8c":function(t,e,i){"use strict";var n=i("8747"),o=i.n(n);o.a},8747:function(t,e,i){},a2a9:function(t,e,i){},b26a:function(t,e,i){"use strict";i.r(e);var n=i("266a"),o=i.n(n);for(var a in n)"default"!==a&&function(t){i.d(e,t,function(){return n[t]})}(a);e["default"]=o.a},bc41:function(t,e,i){"use strict";var n=i("a2a9"),o=i.n(n);o.a},c1b7:function(t,e,i){"use strict";i.r(e);var n=i("117f"),o=i("b26a");for(var a in o)"default"!==a&&function(t){i.d(e,t,function(){return o[t]})}(a);i("2d8c"),i("bc41");var r=i("2877"),s=Object(r["a"])(o["default"],n["a"],n["b"],!1,null,"25e4d525",null);e["default"]=s.exports}},[["1030","common/runtime","common/vendor"]]]);