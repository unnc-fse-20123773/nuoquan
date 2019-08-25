-- +++++++++++++++++++++
-- +  Database update  +
-- +++++++++++++++++++++

-- v19.8.20 @author:xdy
-- 修改：tag表
ALTER TABLE `nuoquan`.`tags` 
CHANGE COLUMN `tag_content` `article_id` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `article_id` `tag_content` VARCHAR(45) NOT NULL , RENAME TO  `nuoquan`.`article_tgs` ;


-- v19.8.20 @author:xdy
-- 修改： comment表添加comment_num属性
ALTER TABLE `nuoquan`.`user_article_comment` 
ADD COLUMN `comment_num` INT(11) NOT NULL DEFAULT '0' AFTER `dislike_num`;

-- v19.8.16 @author:Jerrio
-- 修改：email 可为空，昵称不唯一(匹配微信昵称逻辑，后期针对业务需要可进行调整)
ALTER TABLE `nuoquan`.`user` 
CHANGE COLUMN `email` `email` VARCHAR(45) NULL ;

ALTER TABLE `nuoquan`.`user` 
DROP INDEX `nickname_UNIQUE` ;


-- v19.8.9 @author:Jerrio
ALTER TABLE `nuoquan`.`user` 
ADD COLUMN `fans_num` INT(11) NOT NULL DEFAULT '0' COMMENT '粉丝数' AFTER `follow_num` ,
CHANGE COLUMN `follow_num` `follow_num` INT(11) NOT NULL DEFAULT '0' COMMENT '关注数' ,
CHANGE COLUMN `graduation_year` `graduation_year` INT(5) NULL DEFAULT NULL COMMENT 'Example: 2022' ;
