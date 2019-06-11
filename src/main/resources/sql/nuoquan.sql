/*
MySQL Data Transfer
Source Server         : .
Source Server Version : .
Source Host           : localhost:3306
Source Database       : nuoquan
Target Server Type    : MYSQL
Target Server Version : .
File Encoding         : .
Date: 2019-06-8 16:29:17
*/
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `nuoquan`.`sys_user` (
  `user_id` VARCHAR(20) NOT NULL,
  `username` VARCHAR(45) NOT NULL COMMENT '昵称',
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `student_id` VARCHAR(20) NULL COMMENT '学号\neg. 16512345',
  `admission_year` INT NULL COMMENT '大一：1\n大二：2\n大三：3\n大四：4\n研/博：5',
  `create_date` DATETIME NOT NULL,
  PRIMARY KEY (`user_id`) COMMENT '用户',
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '用户';


