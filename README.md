# Update Log:
**v19.8.16 更新**

1. 整合 yaoyao 分支代码
2. 大改 chatpage: 
	- 使用 scroll-view 组件
	- 添加滚动到页面底部方法
	- TODO [yaoyao] 页面css待修改
3. 新增 使用 vuex 状态管理模式, 响应式获取聊天信息
4. 修正 main.js 全局变量 ServerUrl -> $serverUrl

**v19.8.16 更新**
1. 整合 deyan & guetta-new 分支代码
2. 新增 发布功能雏形，暂时只能上传一张图片 [deyan]
3. 新增 messageList page [guetta]
4. 新增 主页动态获取微信用户信息并和服务器用户数据整合
5. 新增 主页两组件传参，传递用户信息
6. 新增 主页->我的消息->聊天列表跳转逻辑
7. 小改 数据库，详情见 sqlRecord.sql
8. TODO [yao]需把mainpageleft组件中使用button代替，详情见改页注释

**v19.8.11 更新**
1. 整合 yaoyao分支代码
2. 在 chatpage 添加 websocket 相关业务雏形

**v19.8.9 更新**
1. 新增 取消/关注用户接口
2. 新增 sqlRecord.sql 记录数据库修改
3. 小改 数据库，详情见 sqlRecord.sql

**v19.8.8 更新**
1. 新增 fastDFS 模块及 uploadFace 接口实例方法。
2. 新增 UserVO 对象代替 User 对象来面向表达层
3. 修正 login 返回错误对象的 bug
4. 升级 commons-fileupload 到 1.3.3 以避免长字符串拒绝服务漏洞
