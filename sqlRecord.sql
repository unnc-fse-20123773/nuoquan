-- +++++++++++++++++++++
-- +  Database update  +
-- +++++++++++++++++++++
-- v19.9.7 @author: Jerrio
ALTER TABLE `nuoquan`.`user_article_comment` 
ADD COLUMN `sign_flag` INT NULL DEFAULT 0 COMMENT '评论消息是否被签收 0: 未签收 1：签收' AFTER `under_comment_id`;

-- v19.9.6 @author: Jerrio
ALTER TABLE `nuoquan`.`user_article_comment` 
ADD COLUMN `under_comment_id` VARCHAR(45) NULL DEFAULT NULL AFTER `comment_num`;

-- v19.9.4 @author: Jerrio
-- 为用户点赞评论表 添加索引
-- ！以后创建日期一律用 create_date
ALTER TABLE `nuoquan`.`user_like_comment` 
ADD UNIQUE INDEX `user_comment_rel` (`user_id` ASC, `comment_id` ASC);

ALTER TABLE `nuoquan`.`user_like_comment` 
CHANGE COLUMN `create_time` `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `nuoquan`.`user_like_article` 
CHANGE COLUMN `create_time` `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `nuoquan`.`user_article_comment` 
CHANGE COLUMN `create_time` `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

-- v19.9.1 @author: Jerrio
-- 添加点赞评论数据表, 点赞文章签收, 评论父评论和对方id
CREATE TABLE `nuoquan`.`user_like_comment` (
  `id` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(45) NOT NULL COMMENT '点赞人',
  `comment_id` VARCHAR(45) NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sign_flag` INT NOT NULL DEFAULT 0 COMMENT '点赞消息是否被签收\\n 0: 未签收 1：签收',
  PRIMARY KEY (`id`));

ALTER TABLE `nuoquan`.`user_like_article` 
ADD COLUMN `sign_flag` INT NULL DEFAULT 0 COMMENT '点赞消息是否被签收\\\\n 0: 未签收 1：签收' AFTER `create_time`;

ALTER TABLE `nuoquan`.`user_article_comment` 
ADD COLUMN `to_user_id` VARCHAR(45) NOT NULL AFTER `from_user_id`,
ADD COLUMN `father_comment_id` VARCHAR(45) NULL COMMENT '复式评论，父评论，子评论无 article_id' AFTER `to_user_id`,
CHANGE COLUMN `article_id` `article_id` VARCHAR(45) NULL ;


-- v19.8.28 @author: deyan
-- 修改img_path VARCHAR(45) to VARCHAR(255) 
ALTER TABLE `nuoquan`.`article_image` 
CHANGE COLUMN `image_path` `image_path` VARCHAR(255) NOT NULL ;


-- v19.8.27 @author: Jerrio
-- 添加文章浏览量,点赞创建时间, 以支持热度逻辑
ALTER TABLE `nuoquan`.`article` 
ADD COLUMN `view_num` INT NOT NULL DEFAULT 0 COMMENT '浏览量' AFTER `is_anonymous`;

ALTER TABLE `nuoquan`.`user_like_article` 
ADD COLUMN `create_time` DATETIME NOT NULL AFTER `article_id`;

ALTER TABLE `nuoquan`.`user_like_article` 
CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- v19.8.26 @author: Deyan
-- 在article中删除article_path
ALTER TABLE `nuoquan`.`article` 
DROP COLUMN `article_path`;
-- 新建article_image表
CREATE TABLE `nuoquan`.`article_image` (
  `id` VARCHAR(45) NOT NULL,
  `article_id` VARCHAR(45) NOT NULL,
  `image_path` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

-- v19.8.22 @author: Jerrio
-- 为 sign_flag 添加注释
ALTER TABLE `nuoquan`.`chat_msg` 
CHANGE COLUMN `sign_flag` `sign_flag` INT(11) NOT NULL COMMENT '0: 未签收\n1：签收';

-- v19.8.20 @author: Deyan
-- 修改： comment表添加comment_num属性
ALTER TABLE `nuoquan`.`user_article_comment` 
ADD COLUMN `comment_num` INT(11) NOT NULL DEFAULT '0' AFTER `dislike_num`;

-- v19.8.16 @author: Jerrio
-- 修改：email 可为空，昵称不唯一(匹配微信昵称逻辑，后期针对业务需要可进行调整)
ALTER TABLE `nuoquan`.`user` 
CHANGE COLUMN `email` `email` VARCHAR(45) NULL ;

ALTER TABLE `nuoquan`.`user` 
DROP INDEX `nickname_UNIQUE` ;


-- v19.8.9 @author: Jerrio
ALTER TABLE `nuoquan`.`user` 
ADD COLUMN `fans_num` INT(11) NOT NULL DEFAULT '0' COMMENT '粉丝数' AFTER `follow_num` ,
CHANGE COLUMN `follow_num` `follow_num` INT(11) NOT NULL DEFAULT '0' COMMENT '关注数' ,
CHANGE COLUMN `graduation_year` `graduation_year` INT(5) NULL DEFAULT NULL COMMENT 'Example: 2022' ;
