*TODO：*
- 点赞开灯效果 (guetta)
- ~~submit 删除图片 (deyan)~~
- ~~detail 图片显示优化（单图，四图，其他）(yaoyao)~~
- 登录页面修改 (guetta & ~~jerrio~~)
- submit 背景图 (yaoyao)
- Bug-100024 (yaoyao)

# Update Log: #
**v19.9.20 更新**
1. 修复[严重] Bug-100025
2. 修复 index 数据加载顺序错误，导致加载错误信息

**v19.9.19 更新**
1. 新增 上传锁，上传图压缩
2. 优化 index 触顶刷新体验
3. 整合 yaoyao
4. 优化 detail 单四多图显示 (yaoyao)
5. 优化 index 文章加载，sumbit->index 跳转体验
6. 修复 index 崩溃问题，原因：跳转 detail 后返回页面加载冗余
7. 修正 拆分 updateUser 接口业务

**v19.9.18 更新**
1. 整合 deyan
2. 修复[严重] Bug-100022
3. 新增 submit 删除图片 (deyan && yaoyao)

**v19.9.17 更新**
1. 整合 deyan & yaoyao
2. 新增 sumbit 删除标签 (deyan && yaoyao)
3. 新增 服务器相关包及配置
4. 新增 搜索页面点击文本到输入框（deyan）
5. 新增 comment-detail 评论点赞功能和分页，点击头像跳转
6. 新增 detail -> comment-detail 跳转
7. 修复 detail tag 显示问题 (guetta)
8. 修复 detail 页面可滚动问题 (guetta)
9. 添加 articlebrief 限高 500upx，文字限长 15 行 (guetta)
10. 优化 detail 评论分割线 (guetta)
11. 添加 detail marginHelper 块 (guetta)
12. 优化 detail 页面字体大小、粗细，高精度还原设计稿 (guetta)
13. 修复 已知bug

**v19.9.15 更新**
1. 整合 guetta
2. 修复 文章换行（guetta）
3. 优化 主页文章图片方块显示（guetta）
4. 修复[严重] Bug-100022

**v19.9.14 更新**
1. 整合 deyan & yaoyao
2. 新增 chatpage 分页
3. 新增 首页点赞
4. 新增 评论 分页 (deyan)
5. 新增 主页查看原图 (deyan)
6. 新增 detail 长按图片保存到本地 (deyan)
7. 修复[中等] Bug-100018
8. 修复[中等] Bug-100019
9. 修复[严重] Bug-100021
10. 小改 数据库, 详情见 sqlRecord.sql
11. 删除 多余文件
12. 修改 详情页取消关闭展开次评论按钮事件，如果某主评论含有次评论，直接显示

**v19.9.12 更新**
1. 新增 cmt-like 分页加载，极大提升渲染速度
2. 新增 话题榜点击title跳转详情页逻辑
3. 新增 彩色标签，按预设颜色标随机取色
4. 查出[严重] Bug-100021
5. 修复 cmt-like页面分页已知Bug
6. 新增 查看 personPublic 时，刷新该用户缓存
7. 新增 主页触底分页，触顶刷新 (deyan)
8. 新增 删除历史搜索记录缓存 (deyan)
9. 修复 搜索页显示热搜词 (deyan)

**v19.9.11 更新**
1. 整合 guetta & deyan 分支
2. 新增 文章和评论储存和显示表情
3. 新增 文章换行显示
4. 查出[严重] Bug-100014，[严重] Bug-100015，[轻微] Bug-100016，[中等] Bug-100017
5. 中改 数据库，支持表情储存，需要修改配置，详情见 sqlRecord.sql
6. 新增 个人信息修改，昵称字数上线提醒及限制 (8位),邮箱字数上线 (26位, 学校邮箱共25位) (guetta)
7. 修复[严重] Bug-100003 (guetta)
8. 修复[严重] Bug-100007 (guetta)
9. 修复[中等] Bug-100008 (guetta)
10. 修复[中等] Bug-100012 (guetta)
11. 优化 cmt-like 页面 (guetta)
12. 修复[严重] Bug-100005 标签显示异常
13. 新增 cmt-like 跳转他人页和文章详情页逻辑
14. 新增 详情页查看原图 (deyan)
15. 新增 刷新次级评论
15. PS: 出现文章详情页头像和点赞图片放大的请况重新编译一下就可以了

**v19.9.10 更新**
1. 新增 主页和详情页点头像跳转 personPublic 逻辑
2. 新增 次级评论点赞功能

**v19.9.9 更新**
1. 修改 获取评论接口名
2. 新增 文章详情页显示图片
3. 新增 查询用户与文章或评论的点赞关系
4. 新增 文章和主评论的动态点赞功能
5. 新增 复式评论功能
6. 查出Bug-100011，Bug-100012，详情见 testBugs 文本
7. 新增 文章和评论的评论数计数

**v19.9.8 更新**
1. 整合 所有分支代码
2. 修复Bug-100006：主页卡片右下角黄色图片不同机型显示错乱

**v19.9.7 更新**
1. 修正 获取评论消息的 sql 语句
2. 修改 重构 CommentCard.java -> NoticCard.java (泛化)
3. 修正 点赞消息时间显示，不再显示点赞目标的创建时间
4. 新增 我的消息未读数量显示上限 “99+”
5. 新增 获取未签收并签收点赞/评论通知
6. 优化 cmt-likedetail 页面传入参数数据结构
7. 查出[中等] Bug-100008：submit 上传失败未提示用户。处理方法：请求结果500处理
8. 查出[轻微] Bug-1000009：submit 点击发布内容不能为空，原因：发布时未失焦。处理方法：使用v-mode代替
9. 查出[中等] Bug-1000010：获取未签收消息接口最多返回100个对象...并造成卡顿。 解决方法：分页并自动累加页数
10. 小改 数据库，详情见 sqlRecord.sql

**v19.9.6 更新**
1. 小改 数据库，详情见 sqlRecord.sql
2. 新增 点赞/评论消息列表动态参数显示

**v19.9.4 更新**
1. 整合 guetta, 修复部分页面显示bug
2. 新增 点赞/评论消息列表部分动态传参
3. 新增 文档模块 testBugs, 内附部分页面调试手册
4. 修改 comment 对象属性 faceImage -> faceImg
5. 修改 数据库和后端所有 create_time (createTime) -> create_date (createDate) 如果哪里有报错 注意看是不是这个名字的问题
6. 小改 数据库，详情见 sqlRecord.sql
7. 修复Bug-100002：submit 回跳 index 发起额外长连接
8. 修复Bug-100004：submit 9图添加显示格式错误 (yaoyao)

**v19.9.3 更新**
1. 新增 主页文章流显示图片
2. 新增 左侧栏数据动态化
3. 新增 我的消息未读数量动态显示
4. 查出 [严重]Bug-100006 详情见文档 testBugs

**v19.9.1 更新**
1. 整合 deyan & yaoyao
2. 新增 搜索功能，搜索历史缓存 [deyan]
3. 新增 部分样式修改 [yaoyao]
4. 新增 用户 openid 获取并合并入数据库
5. 新增 消息提醒 功能后端代码
6. 新增 用户点赞评论 系列接口
7. 修改 用户点赞文章 系列接口
8. 修改 接口名 userLike -> userUnLikeArticle
9. 修正 personalPage buttons 样式
10. 修正 关注粉丝列表 关注/已关注样式
11. 中改 数据库，详情见 sqlRecord.sql (8.28 和 9.1 两期)
12. 修复BUG-100000 快速点击关注/已关注出现用户受关注数递减情况，原因：未对删除关系结果做出判断就递减, 类似情况有点赞/取消点赞

**v19.8.29 更新**
1. 新增 profile 页面功能及样式 [Jerrio& Guetta]
2. 新增 messageList 页面删除聊天消息 [Jerrio& Guetta]
3. 新增 邮箱发送基本方法 

**v19.8.28 更新**
1. 新增 获取热度榜前3的接口
2. 新增 显示热度榜前三功能
3. 修改 articleController 下访问接口前缀 (/article), 前端旧的相应请求地址注意修改

**v19.8.27 更新**
1. 整合 deyan & yaoyao 分支
2. 新增 按公式（24小时中的点赞数+评论数）更新热度定时任务
3. 修改 Detail，主页的顶部背景和左侧样式 [yaoyao]
4. 修改 多图上传修改中 [deyan]
5. 中改 数据库，详情见 sqlRecord.sql (两期)

**v19.8.25 更新**
1. 新增 对上传文件大小做限制，增加文章上传中图片大小的判断逻辑
2. 新增 application 配置文件中，设置上传最大图片大小为，10MB
3. 新增 BasicController 里统一 uploadFile 方法
4. 修改 后端 fileSpace 为全局变量 
5. 修改 优化 ArticalConroller upload 接口的实现方式
6. 修改 前端 websockt 服务器地址为全局变量

**v19.8.23 更新**
1. 新增 心跳机制
2. 新增 聊天消息重发机制
3. 新增 用户关注完整逻辑
4. 修改 时间的显示，对 Date 形式的时间进行解析

**v19.8.22 更新**
1. 整合 deyan 分支代码
2. 新增 submit 页面添加标签
3. 新增 获取未签收的聊天消息, PS: 聊天剩下右滑删除，和心跳
4. 新增 聊天消息对象创建日期, 之前忘加了
5. 小改 数据库，详情见 sqlRecord.sql (两期)
6. TODO [yaoyao] 修改主页，submit，chatpage页面效果
7. TODO [guetta] 修改messageList页面效果，并协助 yaoyao 修改页面

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
