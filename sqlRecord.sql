-- +++++++++++++++++++++
-- +  Database update  +
-- +++++++++++++++++++++

-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- 为了方便大家按顺序复制执行代码，v20.2.1之后的更新将 append 在本文
-- 档尾部。文档记录风格：
-- 
-- <SQL的更新语句>
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v<yy.mm.dd> @author: <作者>
-- <补充说明>
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- 
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `user_like_comment_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_like_comment_vote` (
  `id` varchar(45) NOT NULL,
  `user_id` varchar(45) NOT NULL COMMENT '点赞人',
  `comment_id` varchar(45) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sign_flag` int(11) NOT NULL DEFAULT '0' COMMENT '点赞消息是否被签收\\n 0: 未签收 1：签收',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_comment_rel` (`user_id`,`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_like_comment_vote`
--

LOCK TABLES `user_like_comment_vote` WRITE;
/*!40000 ALTER TABLE `user_like_comment_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_like_comment_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_vote_comment`
--

DROP TABLE IF EXISTS `user_vote_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_vote_comment` (
  `id` varchar(45) NOT NULL,
  `from_user_id` varchar(45) NOT NULL,
  `to_user_id` varchar(45) NOT NULL,
  `father_comment_id` varchar(45) DEFAULT NULL COMMENT '复式评论，父评论，子评论无 article_id',
  `vote_id` varchar(45) NOT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `like_num` int(11) NOT NULL DEFAULT '0',
  `dislike_num` int(11) NOT NULL DEFAULT '0',
  `comment_num` int(11) NOT NULL DEFAULT '0',
  `under_comment_id` varchar(45) DEFAULT NULL,
  `sign_flag` int(11) DEFAULT '0' COMMENT '评论消息是否被签收 0: 未签收 1：签收',
  `status` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_vote_comment`
--

LOCK TABLES `user_vote_comment` WRITE;
/*!40000 ALTER TABLE `user_vote_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_vote_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vote`
--

DROP TABLE IF EXISTS `vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vote` (
  `id` varchar(45) NOT NULL,
  `vote_title` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `vote_type` int(11) NOT NULL DEFAULT '0' COMMENT '0 = single choice, 1 = multiple choice',
  `user_id` varchar(45) NOT NULL,
  `vote_content` text NOT NULL,
  `comment_num` int(11) NOT NULL DEFAULT '0',
  `sum_vote` int(11) NOT NULL DEFAULT '0',
  `sum_user` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '2' COMMENT '0 = unreadable, 1 = readable, 2 = checking',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiry_date` datetime NOT NULL,
  `duration_time` int(11) NOT NULL DEFAULT '3',
  `view_num` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote`
--

LOCK TABLES `vote` WRITE;
/*!40000 ALTER TABLE `vote` DISABLE KEYS */;
INSERT INTO `vote` VALUES ('200204974M03P37C','123',0,'123','321',0,0,0,1,'2020-02-03 22:58:06','2020-02-06 22:58:06',3,0),('200204CN179CB828','Who\'s your daddy',0,'xudeyan093998','single choice',0,0,0,1,'2020-02-04 03:45:59','2020-02-07 03:45:59',3,0),('200204CNRGK0BSCH','choose a number',0,'许德琰0939','from1-10',0,0,0,1,'2020-02-04 03:48:09','2020-02-07 03:48:09',3,0);
/*!40000 ALTER TABLE `vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vote_image`
--

DROP TABLE IF EXISTS `vote_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vote_image` (
  `id` varchar(45) NOT NULL,
  `vote_id` varchar(45) NOT NULL,
  `image_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote_image`
--

LOCK TABLES `vote_image` WRITE;
/*!40000 ALTER TABLE `vote_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `vote_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vote_option`
--

DROP TABLE IF EXISTS `vote_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vote_option` (
  `id` varchar(45) NOT NULL,
  `vote_id` varchar(45) NOT NULL,
  `option_content` text NOT NULL,
  `option_index` int(11) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `count` int(11) NOT NULL DEFAULT '0',
  `percent` double unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote_option`
--

LOCK TABLES `vote_option` WRITE;
/*!40000 ALTER TABLE `vote_option` DISABLE KEYS */;
INSERT INTO `vote_option` VALUES ('200204CNRGNCMD68','200204CNRGK0BSCH','',0,'2020-02-04 17:48:11',0,0),('200204CNRGNSX214','200204CNRGK0BSCH','1',1,'2020-02-04 17:48:11',0,0),('200204CNRGP1P3XP','200204CNRGK0BSCH','2',2,'2020-02-04 17:48:11',0,0),('200204CNRGP53MW0','200204CNRGK0BSCH','3',3,'2020-02-04 17:48:11',0,0),('200204CNRGP8G5S8','200204CNRGK0BSCH','4',4,'2020-02-04 17:48:11',0,0);
/*!40000 ALTER TABLE `vote_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vote_user`
--

DROP TABLE IF EXISTS `vote_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vote_user` (
  `id` varchar(45) NOT NULL,
  `user_id` varchar(45) NOT NULL,
  `vote_id` varchar(45) NOT NULL,
  `option_id` varchar(45) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote_user`
--

LOCK TABLES `vote_user` WRITE;
/*!40000 ALTER TABLE `vote_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `vote_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.2.2 @author: deyan
-- 投票相关数据表及数据添加
-- PS：本次数据库更新代码较长，请仔细复制避免漏行
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

--
-- Table structure for table `admin_notice`
--

DROP TABLE IF EXISTS `admin_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_notice` (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '内容',
  `type` int(5) DEFAULT NULL COMMENT '类型',
  `create_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `create_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人name',
  `create_date` datetime DEFAULT NULL COMMENT '发信时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_notice`
--

LOCK TABLES `admin_notice` WRITE;
/*!40000 ALTER TABLE `admin_notice` DISABLE KEYS */;
INSERT INTO `admin_notice` VALUES ('330381411007729664','测试公告','<p>啊啊啊<img src=\"http://img.baidu.com/hi/jx2/j_0002.gif\"/><img src=\"http://img.baidu.com/hi/jx2/j_0024.gif\"/></p>',1,'1','admin','2019-09-08 02:24:37'),('330381806358630400','鲜花视频','<p>哈哈哈哈<img src=\"http://img.baidu.com/hi/jx2/j_0024.gif\"/></p>',2,'1','admin','2019-09-08 02:26:11'),('373478036928073728','顶顶顶顶顶顶顶顶顶','<p>顶顶顶顶顶顶顶顶顶顶<img src=\"http://img.baidu.com/hi/jx2/j_0014.gif\"/></p>',1,'1','admin','2020-01-05 00:35:13');
/*!40000 ALTER TABLE `admin_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_permission`
--

DROP TABLE IF EXISTS `admin_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_permission` (
  `id` varchar(255) NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `descripion` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `url` varchar(255) DEFAULT NULL COMMENT '授权链接',
  `is_blank` int(255) DEFAULT '0' COMMENT '是否跳转 0 不跳转 1跳转',
  `pid` varchar(255) DEFAULT NULL COMMENT '父节点id',
  `perms` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `visible` int(255) DEFAULT NULL COMMENT '是否可见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_permission`
--

LOCK TABLES `admin_permission` WRITE;
/*!40000 ALTER TABLE `admin_permission` DISABLE KEYS */;
INSERT INTO `admin_permission` VALUES ('1','首页','首页','#',0,'0','#',0,'fa fa-home',1,0),('10','角色集合','角色集合','/RoleController/list',0,'9','system:role:list',2,'',NULL,0),('11','角色添加','角色添加','/RoleController/add',0,'9','system:role:add',2,'entypo-plus-squared',NULL,0),('12','角色删除','角色删除','/RoleController/remove',0,'9','system:role:remove',2,'entypo-trash',NULL,0),('13','角色修改','角色修改','/RoleController/edit',0,'9','system:role:edit',2,'fa fa-wrench',NULL,0),('14','权限展示','权限展示','/PermissionController/view',0,'592059865673760768','system:permission:view',1,'fa fa-key',3,0),('15','权限集合','权限集合','/PermissionController/list',0,'14','system:permission:list',2,'',NULL,0),('16','权限添加','权限添加','/permissionController/add',0,'14','system:permission:add',2,'entypo-plus-squared',NULL,0),('17','权限删除','权限删除','/PermissionController/remove',0,'14','system:permission:remove',2,'entypo-trash',NULL,0),('18','权限修改','权限修改','/PermissionController/edit',0,'14','system:permission:edit',2,'fa fa-wrench',NULL,0),('19','文件展示','文件展示','/FileController/view',0,'592059865673760768','system:file:view',1,'fa fa-file-image-o',4,0),('20','文件添加','文件添加','/FileController/add',0,'19','system:file:add',2,'entypo-plus-squared',NULL,0),('200208FNAFB5G06W','文章审核','文章人工审核','/CheckArticle/view',0,'592059865673760768','system:article:view',1,'fa fa-newspaper-o',5,0),('200211FWF60B5THH','文章修改','修改文章状态','/CheckArticle/edit',0,'200208FNAFB5G06W','system:article:edit',2,NULL,NULL,0),('200211G0WZMCK8H0','文章集合','文章集合','/CheckArticle/list',0,'200208FNAFB5G06W','system:article:list',2,NULL,NULL,0),('200211G2SMD5AKD4','投票审核','投票人工审核','/CheckVote/view',0,'592059865673760768','system:vote:view',1,'fa fa-hand-stop-o',6,0),('200211G93398W0M8','投票修改','修改投票','/CheckVote/edit',0,'200211G2SMD5AKD4','system:vote:edit',2,NULL,NULL,0),('200211H1YF65C4H0','投票集合','投票集合','/CheckVote/list',0,'200211G2SMD5AKD4','system:vote:list',2,NULL,NULL,0),('21','文件删除','文件删除','/FileController/remove',0,'1','system:file:remove',2,'entypo-trash',NULL,0),('22','文件修改','文件修改','/FileController/edit',0,'19','system:file:edit',2,'fa fa-wrench',NULL,0),('23','文件集合','文件集合','/FileController/list',0,'19','system:file:list',2,'',NULL,0),('330365026642825216','公告管理','公告展示','/SysNoticeController/view',0,'592059865673760768','gen:sysNotice:view',1,'fa fa-telegram',10,0),('3303650266428252171','公告集合','公告集合','/SysNoticeController/list',0,'330365026642825216','gen:sysNotice:list',2,'',NULL,0),('3303650266428252182','公告添加','公告添加','/SysNoticeController/add',0,'330365026642825216','gen:sysNotice:add',2,'entypo-plus-squared',NULL,0),('3303650266428252193','公告删除','公告删除','/SysNoticeController/remove',0,'330365026642825216','gen:sysNotice:remove',2,'entypo-trash',NULL,0),('3303650266428252204','公告修改','公告修改','/SysNoticeController/edit',0,'330365026642825216','gen:sysNotice:edit',2,'fa fa-wrench',NULL,0),('331778807298134016','定时器表达式','','/quartz/view',0,'592059865673760768','#',1,'fa fa-flash',12,0),('332157860920299520','定时任务','定时任务调度表展示','/SysQuartzJobController/view',0,'592059865673760768','gen:sysQuartzJob:view',1,'fa fa-hourglass-1',13,0),('3321578609202995211','定时任务调度表集合','定时任务调度表集合','/SysQuartzJobController/list',0,'332157860920299520','gen:sysQuartzJob:list',2,'',NULL,0),('3321578609202995222','定时任务调度表添加','定时任务调度表添加','/SysQuartzJobController/add',0,'332157860920299520','gen:sysQuartzJob:add',2,'entypo-plus-squared',NULL,0),('3321578609202995233','定时任务调度表删除','定时任务调度表删除','/SysQuartzJobController/remove',0,'332157860920299520','gen:sysQuartzJob:remove',2,'entypo-trash',NULL,0),('3321578609202995244','定时任务调度表修改','定时任务调度表修改','/SysQuartzJobController/edit',0,'332157860920299520','gen:sysQuartzJob:edit',2,'fa fa-wrench',NULL,0),('332857281479839744','定时任务日志','定时任务日志','/SysQuartzJobLogController/view',0,'592059865673760768','gen:sysQuartzJobLog:view',1,'fa fa-database',14,0),('3328572814798397451','定时任务调度日志表集合','定时任务调度日志表集合','/SysQuartzJobLogController/list',0,'332857281479839744','gen:sysQuartzJobLog:list',2,'',NULL,0),('3328572814798397473','定时任务调度日志表删除','定时任务调度日志表删除','/SysQuartzJobLogController/remove',0,'332857281479839744','gen:sysQuartzJobLog:remove',2,'entypo-trash',NULL,0),('335330315113467904','Json工具','Json格式化工具','/Json/view',1,'617766548966211584','#',1,'fa fa-retweet',10,0),('340066503263129600','省份设置','省份设置',NULL,0,'1',NULL,0,'fa fa-building',4,0),('340067579836108800','省份管理','','#',0,'340066503263129600','#',1,'fa fa-building-o',NULL,0),('340068151804956672','省份表管理','省份表展示','/SysProvinceController/view',0,'340067579836108800','gen:sysProvince:view',1,'fa fa-quora',2,0),('3400681518049566731','省份表集合','省份表集合','/SysProvinceController/list',0,'340068151804956672','gen:sysProvince:list',2,'',NULL,0),('3400681518049566742','省份表添加','省份表添加','/SysProvinceController/add',0,'340068151804956672','gen:sysProvince:add',2,'entypo-plus-squared',NULL,0),('3400681518049566753','省份表删除','省份表删除','/SysProvinceController/remove',0,'340068151804956672','gen:sysProvince:remove',2,'entypo-trash',NULL,0),('3400681518049566764','省份表修改','省份表修改','/SysProvinceController/edit',0,'340068151804956672','gen:sysProvince:edit',2,'fa fa-wrench',NULL,0),('340088022018166784','城市表管理','城市设置展示','/SysCityController/view',0,'340067579836108800','gen:sysCity:view',1,'fa fa-quora',3,0),('3400880220181667851','城市设置集合','城市设置集合','/SysCityController/list',0,'340088022018166784','gen:sysCity:list',2,'',NULL,0),('3400880220181667862','城市设置添加','城市设置添加','/SysCityController/add',0,'340088022018166784','gen:sysCity:add',2,'entypo-plus-squared',NULL,0),('3400880220181667873','城市设置删除','城市设置删除','/SysCityController/remove',0,'340088022018166784','gen:sysCity:remove',2,'entypo-trash',NULL,0),('3400880220181667884','城市设置修改','城市设置修改','/SysCityController/edit',0,'340088022018166784','gen:sysCity:edit',2,'fa fa-wrench',NULL,0),('340096183135506432','地区设置管理','地区设置展示','/SysAreaController/view',0,'340067579836108800','gen:sysArea:view',1,'fa fa-quora',4,0),('3400961831355064331','地区设置集合','地区设置集合','/SysAreaController/list',0,'340096183135506432','gen:sysArea:list',2,'',NULL,0),('3400961831355064342','地区设置添加','地区设置添加','/SysAreaController/add',0,'340096183135506432','gen:sysArea:add',2,'entypo-plus-squared',NULL,0),('3400961831355064353','地区设置删除','地区设置删除','/SysAreaController/remove',0,'340096183135506432','gen:sysArea:remove',2,'entypo-trash',NULL,0),('3400961831355064364','地区设置修改','地区设置修改','/SysAreaController/edit',0,'340096183135506432','gen:sysArea:edit',2,'fa fa-wrench',NULL,0),('340127412270534656','街道设置管理','街道设置展示','/SysStreetController/view',0,'340067579836108800','gen:sysStreet:view',1,'fa fa-quora',5,0),('3401274122705346571','街道设置集合','街道设置集合','/SysStreetController/list',0,'340127412270534656','gen:sysStreet:list',2,'',NULL,0),('3401274122705346582','街道设置添加','街道设置添加','/SysStreetController/add',0,'340127412270534656','gen:sysStreet:add',2,'entypo-plus-squared',NULL,0),('3401274122705346593','街道设置删除','街道设置删除','/SysStreetController/remove',0,'340127412270534656','gen:sysStreet:remove',2,'entypo-trash',NULL,0),('3401274122705346604','街道设置修改','街道设置修改','/SysStreetController/edit',0,'340127412270534656','gen:sysStreet:edit',2,'fa fa-wrench',NULL,0),('340301160042860544','省份联动','省份联动','/ProvinceLinkageController/view',0,'340067579836108800','#',1,'fa fa-etsy',1,0),('340381240911859712','JavaScript格式化','JavaScript格式化','/static/admin/htmlformat/javascriptFormat.html',1,'617766548966211584','#',1,'fa fa-magic',11,0),('354851114446884864','七牛文件上传','七牛文件上传','/QiNiuCloudController/view',0,'592059865673760768','system:qiNiuCloud:view',1,'fa fa-globe',15,0),('354865752219717632','云文件集合','云文件集合','/QiNiuCloudController/list',0,'354851114446884864','system:qiNiuCloud:list',2,NULL,NULL,0),('373489907429150720','URL拦截管理','拦截url表展示','/SysInterUrlController/view',0,'592059865673760768','gen:sysInterUrl:view',1,'fa fa-hand-stop-o',NULL,0),('3734899074291507211','拦截url表集合','拦截url表集合','/SysInterUrlController/list',0,'373489907429150720','gen:sysInterUrl:list',2,'',NULL,0),('3734899074291507222','拦截url表添加','拦截url表添加','/SysInterUrlController/add',0,'373489907429150720','gen:sysInterUrl:add',2,'entypo-plus-squared',NULL,0),('3734899074291507233','拦截url表删除','拦截url表删除','/SysInterUrlController/remove',0,'373489907429150720','gen:sysInterUrl:remove',2,'entypo-trash',NULL,0),('3734899074291507244','拦截url表修改','拦截url表修改','/SysInterUrlController/edit',0,'373489907429150720','gen:sysInterUrl:edit',2,'fa fa-wrench',NULL,0),('4','用户管理','用户展示','/UserController/view',0,'592059865673760768','system:user:view',1,'icon icon-user',1,0),('486690002869157888','用户密码修改','用户密码修改','/UserController/editPwd',0,'4','system:user:editPwd',2,'entypo-tools',3,0),('496126970468237312','日志展示','日志管理','/LogController/view',0,'592059865673760768','system:log:view',1,'fa fa-info',9,0),('496127240363311104','日志删除','日志删除','/LogController/remove',0,'496126970468237312','system:log:remove',2,'entypo-trash',NULL,0),('496127794879660032','日志集合','日志集合','/LogController/list',0,'496126970468237312','system:log:list',2,NULL,NULL,0),('496782496638173184','系统设置','后台设置',NULL,0,'1',NULL,0,'fa fa-gear',3,0),('5','用户集合','用户集合','/UserController/list',0,'4','system:user:list',2,'',NULL,0),('581541547099553792','druid监控','druid监控','/druid/',0,'617766548966211584','user:list',1,'fa fa-line-chart',6,0),('583063272123531264','API文档','API文档','/swagger-ui.html',1,'617766548966211584','--',1,'fa fa-font',8,0),('586003694080753664','表单构建','表单构建','/ToolController/view',0,'617766548966211584','system:tool:view',1,'fa fa-list-alt',5,0),('587453033487532032','后台模板','后台模板','/static/admin/bootstarp/index.html',1,'617766548966211584','system:htmb:view',1,'fa fa-telegram',9,0),('589559475422101504','测试目录','测试目录',NULL,0,'1',NULL,0,'fa fa-etsy',5,0),('589559748521623552','一级菜单','测试菜单','#',0,'589559475422101504','#',1,'fa fa-address-book',NULL,0),('589559916704825344','二级菜单','二级菜单','#',0,'589559748521623552','#',1,'fa fa-address-book',1,0),('592059865673760768','系统管理','后台管理','#',0,'496782496638173184','#',1,'fa fa-home',1,0),('592067570522128384','测试跳转','测试跳转','http://www.baidu.com',1,'589559748521623552','#',1,'fa fa-address-book',NULL,0),('592167738407911424','系统监控','系统监控','/ServiceController/view',0,'617766548966211584','system:service:view',1,'fa fa-video-camera',7,0),('594691026430459904','电子邮件管理','电子邮件展示','/EmailController/view',0,'592059865673760768','system:email:view',1,'fa fa-envelope',8,0),('5946910264304599041','电子邮件集合','电子邮件集合','/EmailController/list',0,'594691026430459904','system:email:list',2,'',NULL,0),('5946910264304599042','电子邮件添加','电子邮件添加','/EmailController/add',0,'594691026430459904','system:email:add',2,'entypo-plus-squared',NULL,0),('5946910264304599043','电子邮件删除','电子邮件删除','/EmailController/remove',0,'594691026430459904','system:email:remove',2,'entypo-trash',NULL,0),('5946910264304599044','电子邮件修改','电子邮件修改','/EmailController/edit',0,'594691026430459904','system:email:edit',2,'fa fa-wrench',NULL,0),('6','用户添加','用户添加','/UserController/add',0,'4','system:user:add',2,'entypo-plus-squared',NULL,0),('610635485890478080','代码生成新','代码生成2','#',0,'617751079701970944','#',1,'fa fa-blind',1,0),('610635950447394816','全局配置','','/autoCodeController/global',0,'610635485890478080','system:autocode:global',1,'fa fa-university',NULL,0),('610983815791247360','单表生成','','/autoCodeController/one',0,'610635485890478080','system:autocode:one',1,'fa fa-hand-peace-o',NULL,0),('617751079701970944','代码生成','代码生成',NULL,0,'1',NULL,0,'fa fa-500px',4,0),('617766548966211584','系统工具','系统工具','#',0,'496782496638173184','#',1,'fa fa-th-large',2,0),('618918631769636864','字典管理','字典类型表展示','/DictTypeController/view',0,'592059865673760768','system:dictType:view',1,'fa fa-puzzle-piece',11,0),('6189186317738311681','字典类型表集合','字典类型表集合','/DictTypeController/list',0,'618918631769636864','system:dictType:list',2,NULL,NULL,0),('6189186317948026882','字典类型表添加','字典类型表添加','/DictTypeController/add',0,'618918631769636864','system:dictType:add',2,NULL,NULL,0),('6189186317948026883','字典类型表删除','字典类型表删除','/DictTypeController/remove',0,'618918631769636864','system:dictType:remove',2,NULL,NULL,0),('6189186317989969924','字典类型表修改','字典类型表修改','/DictTypeController/edit',0,'618918631769636864','system:dictType:edit',2,NULL,NULL,0),('6192095214866268161','字典数据表集合','字典数据表集合','/DictDataController/list',0,'618918631769636864','system:dictData:list',2,NULL,NULL,0),('6192095214866268162','字典数据表添加','字典数据表添加','/DictDataController/add',0,'618918631769636864','system:dictData:add',2,NULL,NULL,0),('6192095215075983363','字典数据表删除','字典数据表删除','/DictDataController/remove',0,'618918631769636864','system:dictData:remove',2,NULL,NULL,0),('6192095215075983364','字典数据表修改','字典数据表修改','/DictDataController/edit',0,'618918631769636864','system:dictData:edit',2,NULL,NULL,0),('619836559427895296','字典数据视图','字典数据视图','/DictDataController/view',0,'618918631769636864','system:dictData:view',2,NULL,NULL,0),('7','用户删除','用户删除','/UserController/remove',0,'4','system:user:remove',2,'entypo-trash',NULL,0),('8','用户修改','用户修改','/UserController/edit',0,'4','system:user:edit',2,'fa fa-wrench',NULL,0),('9','角色管理','角色展示','/RoleController/view',0,'592059865673760768','system:role:view',1,'fa fa-group',2,0);
/*!40000 ALTER TABLE `admin_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_role`
--

DROP TABLE IF EXISTS `admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_role` (
  `id` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role`
--

LOCK TABLES `admin_role` WRITE;
/*!40000 ALTER TABLE `admin_role` DISABLE KEYS */;
INSERT INTO `admin_role` VALUES ('0','L1系统管理员'),('1','L2管理员'),('200207FXDDHX6K40','L3组织社团');
/*!40000 ALTER TABLE `admin_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_role_permission`
--

DROP TABLE IF EXISTS `admin_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_role_permission` (
  `id` varchar(255) NOT NULL,
  `role_id` varchar(255) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(255) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限中间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role_permission`
--

LOCK TABLES `admin_role_permission` WRITE;
/*!40000 ALTER TABLE `admin_role_permission` DISABLE KEYS */;
INSERT INTO `admin_role_permission` VALUES ('200207FZ4HTP45GC','0','1'),('200207FZ4HTYZ7C0','0','21'),('200207FZ4HW5S98H','0','496782496638173184'),('200207FZ4HW95W6W','0','592059865673760768'),('200207FZ4HWCKB54','0','373489907429150720'),('200207FZ4HWH0Y3C','0','3734899074291507211'),('200207FZ4HWNCD1P','0','3734899074291507222'),('200207FZ4HWSW000','0','3734899074291507233'),('200207FZ4HWY7FY8','0','3734899074291507244'),('200207FZ4HX52HSW','0','4'),('200207FZ4HX8F3R4','0','5'),('200207FZ4HXBXMNC','0','6'),('200207FZ4HXG95KP','0','7'),('200207FZ4HXS47F8','0','8'),('200207FZ4HXXGSCH','0','486690002869157888'),('200207FZ4HY0Z9AW','0','9'),('200207FZ4HY4AW94','0','10'),('200207FZ4HY7SB7C','0','11'),('200207FZ4HYFKD40','0','12'),('200207FZ4HYFKD41','0','13'),('200207FZ4HYM1028','0','14'),('200207FZ4HYRCG0H','0','15'),('200207FZ4HYWW1YW','0','16'),('200207FZ4HZ07HX4','0','17'),('200207FZ4HZ3N3TC','0','18'),('200207FZ4HZ72MRP','0','19'),('200207FZ4HZAF5P0','0','20'),('200207FZ4HZDXPM8','0','22'),('200207FZ4HZDXPM9','0','23'),('200207FZ4HZK97HH','0','594691026430459904'),('200207FZ4HZPPSFW','0','5946910264304599041'),('200207FZ4HZW49D4','0','5946910264304599042'),('200207FZ4K02ZB9P','0','5946910264304599043'),('200207FZ4K06AY80','0','5946910264304599044'),('200207FZ4K09SD68','0','496126970468237312'),('200207FZ4K0D604H','0','496127240363311104'),('200207FZ4K0HKG2W','0','496127794879660032'),('200207FZ4K0P1214','0','330365026642825216'),('200207FZ4K0TCHZC','0','3303650266428252171'),('200207FZ4K0YW3XP','0','3303650266428252182'),('200207FZ4K127MW0','0','3303650266428252193'),('200207FZ4K15N5S8','0','3303650266428252204'),('200207FZ4K192PPH','0','618918631769636864'),('200207FZ4K1CF7MW','0','619836559427895296'),('200207FZ4K1GXSK4','0','6189186317738311681'),('200207FZ4K1GXSK5','0','6189186317948026882'),('200207FZ4K1N99GC','0','6189186317948026883'),('200207FZ4K1SPWDP','0','6189186317989969924'),('200207FZ4K1Y4BC0','0','6192095214866268161'),('200207FZ4K21GYA8','0','6192095214866268162'),('200207FZ4K24ZD8H','0','6192095215075983363'),('200207FZ4K28B06W','0','6192095215075983364'),('200207FZ4K2BSG54','0','331778807298134016'),('200207FZ4K2G623C','0','332157860920299520'),('200207FZ4K2G623D','0','3321578609202995211'),('200207FZ4K2MKK1P','0','3321578609202995222'),('200207FZ4K2S1400','0','3321578609202995233'),('200207FZ4K2XCMY8','0','3321578609202995244'),('200207FZ4K30W5WH','0','332857281479839744'),('200207FZ4K347PSW','0','3328572814798397451'),('200207FZ4K37N7R4','0','3328572814798397473'),('200207FZ4K3B2SNC','0','354851114446884864'),('200207FZ4K3FF9KP','0','354865752219717632'),('200207FZ4K3KXWH0','0','617766548966211584'),('200207FZ4K3R9BF8','0','586003694080753664'),('200207FZ4K3R9BF9','0','581541547099553792'),('200207FZ4K3WPYCH','0','592167738407911424'),('200207FZ4K404DAW','0','583063272123531264'),('200207FZ4K43H094','0','587453033487532032'),('200207FZ4K46ZG7C','0','335330315113467904'),('200207FZ4K4AB25P','0','340381240911859712'),('200207FZ4K4DSK40','0','340066503263129600'),('200207FZ4K4DSK41','0','340067579836108800'),('200207FZ4K4K6428','0','340301160042860544'),('200207FZ4K4PKN0H','0','340068151804956672'),('200207FZ4K4W15YW','0','3400681518049566731'),('200207FZ4K4ZCPX4','0','3400681518049566742'),('200207FZ4K567SRP','0','3400681518049566753'),('200207FZ4K59N9P0','0','3400681518049566764'),('200207FZ4K5D2WM8','0','340088022018166784'),('200207FZ4K5HFBHH','0','3400880220181667851'),('200207FZ4K5NXYFW','0','3400880220181667862'),('200207FZ4K5YR0BC','0','3400880220181667873'),('200207FZ4K6CB44H','0','3400880220181667884'),('200207FZ4K6N6614','0','340096183135506432'),('200207FZ4K6SKPZC','0','3400961831355064331'),('200207FZ4K6Y17XP','0','3400961831355064342'),('200207FZ4K71CSW0','0','3400961831355064353'),('200207FZ4K787WPH','0','3400961831355064364'),('200207FZ4K7BNBMW','0','340127412270534656'),('200207FZ4K7G2YK4','0','3401274122705346571'),('200207FZ4K7MFDGC','0','3401274122705346582'),('200207FZ4K7X9GC0','0','3401274122705346593'),('200207FZ4K844K8H','0','3401274122705346604'),('200207FZ4K87H46W','0','617751079701970944'),('200207FZ4K8FB63C','0','610635485890478080'),('200207FZ4K8R6800','0','610635950447394816'),('200207FZ4K8WKSY8','0','610983815791247360'),('200207FZ4K9019WH','0','589559475422101504'),('200207FZ4K93CWSW','0','589559748521623552'),('200207FZ4K96WBR4','0','592067570522128384'),('200207FZ4K9A7YNC','0','589559916704825344'),('200211FGS3W1RSY8','200207FXDDHX6K40',''),('200211H23SMF977C','1','1'),('200211H23SMR4940','1','496782496638173184'),('200211H23SMWGW28','1','592059865673760768'),('200211H23SN3AXYW','1','4'),('200211H23SN6SCX4','1','5'),('200211H23SNDKFRP','1','6'),('200211H23SNK11P0','1','7'),('200211H23SNTW3HH','1','8'),('200211H23SNZ7MFW','1','486690002869157888'),('200211H23SP2N5D4','1','9'),('200211H23SP62PBC','1','10'),('200211H23SP9F79P','1','11'),('200211H23SPCXS80','1','12'),('200211H23SPNPW4H','1','13'),('200211H23SPYGY14','1','14'),('200211H23SR1ZCZC','1','15'),('200211H23SR5AZXP','1','16'),('200211H23SR8SFW0','1','17'),('200211H23SRC61S8','1','18'),('200211H23SRGKHPH','1','200208FNAFB5G06W'),('200211H23SRN13MW','1','200211FWF60B5THH'),('200211H23SRSCMK4','1','200211G0WZMCK8H0'),('200211H23SRXW5GC','1','200211G2SMD5AKD4'),('200211H23SS4N7C0','1','200211G93398W0M8'),('200211H23SS82SA8','1','200211H1YF65C4H0');
/*!40000 ALTER TABLE `admin_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(45) DEFAULT NULL COMMENT '备注，用于标注账号所属人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

LOCK TABLES `admin_user` WRITE;
/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES ('0','admin','09d2057e436277488b71f769a9cf927d','test-account','2020-02-02 16:26:08','测试账号'),('1','admin1','09d2057e436277488b71f769a9cf927d','运营账号1','2020-02-03 20:08:34','运营账号1');
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_notice`
--

DROP TABLE IF EXISTS `admin_user_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_notice` (
  `id` varchar(255) NOT NULL COMMENT '主键',
  `notice_id` varchar(255) DEFAULT NULL COMMENT '公告id',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `state` int(2) DEFAULT NULL COMMENT '0未阅读 1 阅读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告_用户外键';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_notice`
--

LOCK TABLES `admin_user_notice` WRITE;
/*!40000 ALTER TABLE `admin_user_notice` DISABLE KEYS */;
INSERT INTO `admin_user_notice` VALUES ('330381411037089792','330381411007729664','0',1),('330381411045478400','330381411007729664','488294747442511872',0),('330381806375407616','330381806358630400','0',1),('330381806379601920','330381806358630400','488294747442511872',0),('330622143622680576','330622143597514752','0',1),('330622143626874880','330622143597514752','488294747442511872',0),('354984345649418240','354984345632641024','0',1),('373478037158760448','373478036928073728','1',1),('373478037162954752','373478036928073728','368026921239449600',0),('373478037171343360','373478036928073728','368026937181999104',0),('373478037175537664','373478036928073728','368027013392502784',0),('373478037183926272','373478036928073728','368027030899527680',0),('373478037192314880','373478036928073728','368027048402358272',0),('373478037204897792','373478036928073728','368027066563694592',0),('373478037213286400','373478036928073728','368027087866564608',0),('373478037217480704','373478036928073728','368027104895438848',0),('373478037225869312','373478036928073728','368027130728157184',0),('373478037230063616','373478036928073728','368027151624179712',0),('373478037238452224','373478036928073728','368382463233363968',0);
/*!40000 ALTER TABLE `admin_user_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_role`
--

DROP TABLE IF EXISTS `admin_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_role` (
  `id` varchar(45) NOT NULL,
  `admin_user_id` varchar(45) DEFAULT NULL,
  `role_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_role`
--

LOCK TABLES `admin_user_role` WRITE;
/*!40000 ALTER TABLE `admin_user_role` DISABLE KEYS */;
INSERT INTO `admin_user_role` VALUES ('0','0','0'),('200207GF0RC8GYNC','1','1'),('200207GF0RCMSGF8','1','200207FXDDHX6K40');
/*!40000 ALTER TABLE `admin_user_role` ENABLE KEYS */;
UNLOCK TABLES;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.2.13 @author: jerio
-- 后台管理相关数据表更新
-- PS: 本次数据库更新代码较长，请仔细复制不用漏行
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

ALTER TABLE `nuoquan`.`user` 
ADD COLUMN `reputation` INT NULL DEFAULT 0 AFTER `cid`;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.2.19 @author: jerio
-- 为用户添加影响力
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` varchar(45) NOT NULL,
  `tag` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES ('0','学术'),('1','实习'),('2','娱乐'),('3','生活'),('4','组织社团'),('5','二手交易');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.2.21 @author: jerrio
-- 添加标签表
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

ALTER TABLE `nuoquan`.`vote_user` 
ADD UNIQUE INDEX `user_option_rel` (`user_id` ASC, `option_id` ASC);

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.3.7 @author: jerrio
-- 添加user_option_rel关系
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

ALTER TABLE `nuoquan`.`vote` 
CHANGE COLUMN `vote_content` `vote_content` TEXT CHARACTER SET 'utf8mb4' NOT NULL ;

ALTER TABLE `nuoquan`.`vote_option` 
CHANGE COLUMN `option_content` `option_content` TEXT CHARACTER SET 'utf8mb4' NOT NULL ;

ALTER TABLE `nuoquan`.`article_image` 
ADD COLUMN `image_order` INT NOT NULL AFTER `image_path`;


-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.3.12 @author: deyan
-- 将vote中的vote_content和vote_option中的option_content改为utf8mb4
-- 为article_image表添加image_order属性
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


ALTER TABLE `nuoquan`.`user` 
ADD COLUMN `latest_login` DATETIME NULL DEFAULT NULL AFTER `reputation`;

ALTER TABLE `nuoquan`.`vote_image` 
ADD COLUMN `image_order` INT NOT NULL AFTER `image_path`;


-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.3.12 @author: deyan
-- 为user表添加last_login属性, 用来储存用户 最近的 登录时间
-- 为vote_image表添加image_order属性
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

ALTER TABLE `nuoquan`.`vote_option` 
ADD INDEX `vote_option_idx` (`vote_id` ASC);
;
ALTER TABLE `nuoquan`.`vote_option` 
ADD CONSTRAINT `vote_option`
  FOREIGN KEY (`vote_id`)
  REFERENCES `nuoquan`.`vote` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`vote_user` 
ADD INDEX `user_option_idx` (`option_id` ASC);
;
ALTER TABLE `nuoquan`.`vote_user` 
ADD CONSTRAINT `user_option`
  FOREIGN KEY (`option_id`)
  REFERENCES `nuoquan`.`vote_option` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`vote_image` 
ADD INDEX `vote_image_idx` (`vote_id` ASC);
;
ALTER TABLE `nuoquan`.`vote_image` 
ADD CONSTRAINT `vote_image`
  FOREIGN KEY (`vote_id`)
  REFERENCES `nuoquan`.`vote` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`user_vote_comment` 
ADD INDEX `user_vote_comment_idx` (`vote_id` ASC);
;
ALTER TABLE `nuoquan`.`user_vote_comment` 
ADD CONSTRAINT `user_vote_comment`
  FOREIGN KEY (`vote_id`)
  REFERENCES `nuoquan`.`vote` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`user_like_comment_vote` 
ADD INDEX `user_like_comment_vote_idx` (`comment_id` ASC);
;
ALTER TABLE `nuoquan`.`user_like_comment_vote` 
ADD CONSTRAINT `user_like_comment_vote`
  FOREIGN KEY (`comment_id`)
  REFERENCES `nuoquan`.`user_vote_comment` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`user_article_comment` 
CHARACTER SET = utf8mb4 ;

ALTER TABLE `nuoquan`.`user_like_comment` 
CHANGE COLUMN `comment_id` `comment_id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;

ALTER TABLE `nuoquan`.`user_like_comment` 
ADD INDEX `user_like_comment_idx` (`comment_id` ASC);
;
ALTER TABLE `nuoquan`.`user_like_comment` 
ADD CONSTRAINT `user_like_comment`
  FOREIGN KEY (`comment_id`)
  REFERENCES `nuoquan`.`user_article_comment` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`user_article_comment` 
ADD INDEX `user_article_comment_idx` (`article_id` ASC);
;
ALTER TABLE `nuoquan`.`user_article_comment` 
ADD CONSTRAINT `user_article_comment`
  FOREIGN KEY (`article_id`)
  REFERENCES `nuoquan`.`article` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`user_like_article` 
CHARACTER SET = utf8mb4 ;

ALTER TABLE `nuoquan`.`user_like_article` 
CHANGE COLUMN `article_id` `article_id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;

ALTER TABLE `nuoquan`.`user_like_article` 
ADD INDEX `user_like_article_idx` (`article_id` ASC);
;
ALTER TABLE `nuoquan`.`user_like_article` 
ADD CONSTRAINT `user_like_article`
  FOREIGN KEY (`article_id`)
  REFERENCES `nuoquan`.`article` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`user_fans` 
ADD INDEX `fans_id_idx` (`fans_id` ASC);
;
ALTER TABLE `nuoquan`.`user_fans` 
ADD CONSTRAINT `user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE,
ADD CONSTRAINT `fans_id`
  FOREIGN KEY (`fans_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`chat_msg` 
CHANGE COLUMN `send_user_id` `send_user_id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ,
CHANGE COLUMN `accept_user_id` `accept_user_id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;

ALTER TABLE `nuoquan`.`chat_msg` 
ADD INDEX `send_id_idx` (`send_user_id` ASC),
ADD INDEX `accept_user_id_idx` (`accept_user_id` ASC);
;
ALTER TABLE `nuoquan`.`chat_msg` 
ADD CONSTRAINT `send_user_id`
  FOREIGN KEY (`send_user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE,
ADD CONSTRAINT `accept_user_id`
  FOREIGN KEY (`accept_user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`article_image` 
CHANGE COLUMN `id` `id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;
ALTER TABLE `nuoquan`.`article_image` 
CHANGE COLUMN `article_id` `article_id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;

ALTER TABLE `nuoquan`.`article_image` 
ADD INDEX `article_image_idx` (`article_id` ASC);
;
ALTER TABLE `nuoquan`.`article_image` 
ADD CONSTRAINT `article_image`
  FOREIGN KEY (`article_id`)
  REFERENCES `nuoquan`.`article` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`article` 
ADD CONSTRAINT `user_article`
  FOREIGN KEY (`user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`admin_user_role` 
CHARACTER SET = utf8 ;
ALTER TABLE `nuoquan`.`admin_role` 
CHANGE COLUMN `id` `id` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;
ALTER TABLE `nuoquan`.`admin_user_role` 
CHANGE COLUMN `admin_user_id` `admin_user_id` VARCHAR(45) CHARACTER SET 'utf8' NULL DEFAULT NULL ,
CHANGE COLUMN `role_id` `role_id` VARCHAR(45) CHARACTER SET 'utf8' NULL DEFAULT NULL ;
ALTER TABLE `nuoquan`.`admin_role` 
CHARACTER SET = utf8 ;

ALTER TABLE `nuoquan`.`admin_user_role` 
ADD INDEX `user_id_idx` (`admin_user_id` ASC),
ADD INDEX `role_id_idx` (`role_id` ASC);
;
ALTER TABLE `nuoquan`.`admin_user_role` 
ADD CONSTRAINT `admin_user`
  FOREIGN KEY (`admin_user_id`)
  REFERENCES `nuoquan`.`admin_user` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `nuoquan`.`admin_user_role` 
ADD CONSTRAINT `role_id`
  FOREIGN KEY (`admin_user_id`)
  REFERENCES `nuoquan`.`admin_role` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`admin_role_permission` 
ADD INDEX `role_id_idx` (`role_id` ASC);
;
ALTER TABLE `nuoquan`.`admin_role_permission` 
ADD CONSTRAINT `role`
  FOREIGN KEY (`role_id`)
  REFERENCES `nuoquan`.`admin_role` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `nuoquan`.`admin_role_permission` 
ADD INDEX `permission_id_idx` (`permission_id` ASC);
;
ALTER TABLE `nuoquan`.`admin_role_permission` 
ADD CONSTRAINT `permission_id`
  FOREIGN KEY (`permission_id`)
  REFERENCES `nuoquan`.`admin_permission` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`vote` 
ADD INDEX `user_vote_idx` (`user_id` ASC);
;
ALTER TABLE `nuoquan`.`vote` 
ADD CONSTRAINT `user_vote`
  FOREIGN KEY (`user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`user_like_article` 
ADD CONSTRAINT `user_id_foreign`
  FOREIGN KEY (`user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `nuoquan`.`vote_user` 
ADD CONSTRAINT `vote_user`
  FOREIGN KEY (`user_id`)
  REFERENCES `nuoquan`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.3.23 @author: Jerrio
-- 添加部分外键
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

CREATE TABLE `user_collect_article` (
  `id` varchar(45) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(45) CHARACTER SET utf8 NOT NULL,
  `article_id` varchar(45) CHARACTER SET utf8 NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sign_flag` int(11) DEFAULT '0' COMMENT '消息是否被签收\\\\n 0: 未签收 1：签收',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_article_rel` (`user_id`,`article_id`) USING BTREE,
  KEY `user_collect_article_idx` (`article_id`),
  CONSTRAINT `uca_user_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `uca_article_foreign` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `nuoquan`.`article` 
ADD COLUMN `collect_num` INT NOT NULL DEFAULT 0 AFTER `comment_num`;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.4.11 @author: Jerrio
-- 添加文章收藏功能
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

alter table user 
add signature varchar(55) DEFAULT NULL COMMENT '个人简介' after nickname;

-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-- v20.6.28 @author: wangyu
-- 在USER表 添加signature列（个性签名）
-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑