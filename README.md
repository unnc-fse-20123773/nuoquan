README
======
Current Dev Verion: `v1.0.3`

项目后端采用分层架构：common <- pojo <- mapper(DAO) <- service <- api
```
.
├── README.md  
├── nuoquan-client app 前端代码
│   ├── App.vue  
│   ├── common  
│   ├── components  
│   ├── custom-tab-bar  
│   ├── main.js  
│   ├── manifest.json  
│   ├── pages  
│   ├── pages.json  
│   ├── static  
│   ├── store  
│   └── uni.scss  
├── nuoquan-dev app 后端代码
│   ├── nuoquan-dev-api 顶层请求模块
│   │   ├── src
│   │	  │	  ├── Application.java 启动类 [运行这个启动]
│   │	  │	  ├── Swagger2.java
│   │	  │ 	├── WarStartApplication.java tomcat启动类
│   │ 	│ 	├── admin 后台管理
│   │	  │	  ├── config 配置类
│   │	  │	  ├── controller 控制类
│   │	  │	  ├── email 邮件服务
│   │	  │	  ├── netty netty消息队列服务
│   │	  │ 	└── scheduler 定时任务
│   │   │
│   │	  └── resources 配置文件夹
│   │	      ├── META-INF
│   │	      ├── application-dev.properties 开发环境配置
│   │	      ├── application-prod.properties 生产环境配置
│   │	      ├── application.properties springboot配置
│   │	      ├── log4j.properties
│   │	      ├── mapper 自动/手写Mapper.xml文件夹
│   │	      ├── resource-dev.properties 资源开发环境配置
│   │	      ├── resource-prod.properties 资源生产环境配置
│   │       ├── static 静态文件存放文件夹[后台模版就放在此文件夹下面。所有的模版页面都在下面]
│   │	      │   ├── admin 后台目录存放
│   │	      │   │   ├── assets js、css存放路径
│   │	      │   │   ├── assets js、css存放路径
│   │	      │   │   └── bootstarp 后台模板存放路径
│   │	      │   ├── js js存放
│   │	      │   └── login 登录页面js、css、image
│   │	      │
│   │	      └── templates 前台HTML存放文件夹
│   │	          ├── admin 动态后台html模板
│   │           ├── error 错误页面html模板
│   │	    		  └── login.html 登录html页面
│   │
│   ├── nuoquan-dev-common 公共模块
│   │   ├── utils 工具类
│   │ 	└── pom.xml 项目所有依赖写在这里
│   │
│   ├── nuoquan-dev-mapper Dao模块
│   ├── nuoquan-dev-pojo 对象模块
│   ├── nuoquan-dev-service 服务层模块
│   ├── nuoquan-mybatisTool mybatis半自动生成工具 [考虑到自动生成文件会覆盖原有文件，因此把它独立成一个项目，运行后需手动复制文件]
│   ├── pom.xml  
│   └── ssl_cert  
├── sqlRecord.sql 数据库更新文档
├── testBugs Bug记录文档 
└── updateLog.md  
```
******
## *TODO：* ##
- 暂无

## [Recent Update](./updateLog.md): ##
**v20.1.30 更新**
1. 新增 后台管理模板代码
2. 修复 后端热部署与 mybatis 依赖的冲突，现可正常使用热部署
3. 新增 admin.controller

**v19.11.8 更新**
1. 优化 personpublic 页面
2. 新增 关于页面
3. 新增 myPublish 页面
