# Update Log:
**v19.8.11 更新**
1. 整合 yaoyao分支代码
2. 在 chatpage 添加 websocket 相关业务雏形

**v19.8.9 更新**
1. 新增 取消/关注用户接口
2. 新增 sqlRecord.sql 记录数据库修改
3. 小幅修改数据库，详情见 sqlRecord.sql

**v19.8.8 更新**
1. 新增 fastDFS 模块及 uploadFace 接口实例方法。
2. 新增 UserVO 对象代替 User 对象来面向表达层
3. 修正 login 返回错误对象的 bug
4. 升级 commons-fileupload 到 1.3.3 以避免长字符串拒绝服务漏洞
