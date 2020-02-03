/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.22 : Database - mysql
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mysql` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mysql`;

/*Table structure for table `emp` */

DROP TABLE IF EXISTS `emp`;

CREATE TABLE `emp` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `join_time` datetime DEFAULT NULL COMMENT '入职时间',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别 0 未知 1 男 2 女',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `education_background` varchar(64) DEFAULT NULL COMMENT '教育背景',
  `age` tinyint(3) unsigned DEFAULT NULL COMMENT '年龄',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `emp` */

insert  into `emp`(`id`,`name`,`join_time`,`sex`,`address`,`education_background`,`age`,`remark`) values (1,'无忧','2020-01-28 21:17:07',1,'陕西省/西安市/雁塔区/某某街道','本科',23,'创始人'),(2,'忘我','2020-01-28 21:18:12',1,'陕西省/西安市/雁塔区/某某街道','本科',22,'合伙人');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
