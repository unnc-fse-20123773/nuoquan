-- +++++++++++++++++++++
-- +  Database update  +
-- +++++++++++++++++++++

-- v19.8.15
ALTER TABLE `nuoquan`.`user` 
CHANGE COLUMN `email` `email` VARCHAR(45) NULL ;

ALTER TABLE `nuoquan`.`user` 
DROP INDEX `nickname_UNIQUE` ;


-- v19.8.9
ALTER TABLE `nuoquan`.`user` 
ADD COLUMN `fans_num` INT(11) NOT NULL DEFAULT '0' COMMENT '粉丝数' AFTER `follow_num` ,
CHANGE COLUMN `follow_num` `follow_num` INT(11) NOT NULL DEFAULT '0' COMMENT '关注数' ,
CHANGE COLUMN `graduation_year` `graduation_year` INT(5) NULL DEFAULT NULL COMMENT 'Example: 2022' ;
