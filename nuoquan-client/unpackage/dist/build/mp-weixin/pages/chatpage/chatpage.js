(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/chatpage/chatpage"],{"12ea":function(t,n,e){},"3b90":function(t,n,e){},"8d42":function(t,n,e){"use strict";var i=e("3b90"),o=e.n(i);o.a},"92ff":function(t,n,e){"use strict";e.r(n);var i=e("d011"),o=e.n(i);for(var s in i)"default"!==s&&function(t){e.d(n,t,function(){return i[t]})}(s);n["default"]=o.a},"9bb2":function(t,n,e){"use strict";var i=e("12ea"),o=e.n(i);o.a},a542:function(t,n,e){"use strict";e.r(n);var i=e("b7f2"),o=e("92ff");for(var s in o)"default"!==s&&function(t){e.d(n,t,function(){return o[t]})}(s);e("8d42"),e("9bb2");var r=e("2877"),a=Object(r["a"])(o["default"],i["a"],i["b"],!1,null,"331542e7",null);n["default"]=a.exports},b7f2:function(t,n,e){"use strict";var i=function(){var t=this,n=t.$createElement;t._self._c},o=[];e.d(n,"a",function(){return i}),e.d(n,"b",function(){return o})},d011:function(t,n,e){"use strict";(function(t){Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i=e("2f62");function o(t){for(var n=1;n<arguments.length;n++){var e=null!=arguments[n]?arguments[n]:{},i=Object.keys(e);"function"===typeof Object.getOwnPropertySymbols&&(i=i.concat(Object.getOwnPropertySymbols(e).filter(function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),i.forEach(function(n){s(t,n,e[n])})}return t}function s(t,n,e){return n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t}var r,a,c=function(){return e.e("pages/chatpage/oneMessage").then(e.bind(null,"208d"))},f=1,u={components:{onemessage:c},data:function(){return{chatContent:[],socketMsgQueue:[],textMsg:"",windowHeight:"",scrollToView:"",userInfo:"",friendInfo:""}},computed:o({},(0,i.mapState)(["chatMessageCard","flashChatPage"])),watch:{chatMessageCard:function(t,n){this.chatContent.push(t),this.scrollToBottom()},flashChatPage:function(t,n){var e=this.getListByKey(r),i=e[e.length-1];i.id=this.generateId(0),this.chatContent.push(i),this.scrollToBottom()}},onLoad:function(n){f=1,this.friendInfo=JSON.parse(n.friendInfo),t.setNavigationBarTitle({title:this.friendInfo.nickname});var e=this.getGlobalUserInfo();if(this.isNull(e))console.log("No userInfo!!");else{this.userInfo=e;var i=this;t.getSystemInfo({success:function(t){i.windowHeight=t.windowHeight}}),r="chat-"+this.userInfo.id+"-"+this.friendInfo.id,a=this.getListByKey(r),this.getChatHistory(),this.$nextTick(function(){this.scrollToBottom()})}},methods:{scroll:function(t){},sendText:function(){this.textMsg&&(this.mySocket.sendObj(this.netty.CHAT,this.friendInfo.id,this.textMsg,null),this.textMsg="")},getChatHistory:function(){var t=this.getChatHistoryPage(f),n=this;t.forEach(function(t,e){t.id=n.generateId(e)}),this.chatContent=t},loadMore:function(){var n=this;if(!this.isHistoryLoading){this.isHistoryLoading=!0;var e=this.chatContent[0].id;t.showLoading({title:"加载中"});var i=f+1,o=this.chat.getUserChatHistory(this.userInfo.id,this.friendInfo.id,i);setTimeout(function(){if(null!=o){var i=n;o.forEach(function(t,n){t.id=i.generateId(n)}),n.chatContent=o.concat(n.chatContent),f++}t.hideLoading(),n.$nextTick(function(){this.scrollToView=e}),n.isHistoryLoading=!1},300)}},getChatHistoryPage:function(t){var n=a.reverse(),e=20,i=(t-1)*e,o=[];if(n.length<i)return null;for(var s=0;s<e;s++)this.isNull(n[i+s])||o.unshift(n[i+s]);return o},generateId:function(t){var n="m"+Math.floor(1e3*Math.random())+"-"+t+(new Date).valueOf();return n},scrollToBottom:function(){console.log(this.chatContent),this.scrollToView=this.chatContent[this.chatContent.length-1].id}}};n.default=u}).call(this,e("543d")["default"])}},[["85d4","common/runtime","common/vendor"]]]);