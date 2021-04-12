/*
 Navicat Premium Data Transfer

 Source Server         : remote_ubuntu_mysql
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 111.229.139.12:3306
 Source Schema         : demo_hospital_project

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 04/04/2021 13:11:48
*/

-- Recreate database
DROP DATABASE IF EXISTS `demo_hospital_project`;
CREATE DATABASE `demo_hospital_project` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
USE `demo_hospital_project`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department_info
-- ----------------------------
DROP TABLE IF EXISTS `department_info`;
CREATE TABLE `department_info`  (
  `department_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '医院科室id',
  `hospital_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '医院id，使科室与指定的医院进行链接',
  `department_name` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '科室名',
  PRIMARY KEY (`department_id`, `hospital_id`) USING BTREE,
  INDEX `department_name`(`department_name`) USING BTREE COMMENT '科室名'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for doctor_info
-- ----------------------------
DROP TABLE IF EXISTS `doctor_info`;
CREATE TABLE `doctor_info`  (
  `doctor_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '医生的编号id',
  `department_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '科室id，用于指定医生所在的科室',
  `doctor_name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '医生姓名',
  `reservation_price` float(10, 2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预约价格',
  `remaining_capacity` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '医生当前容量',
  `doctor_image_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '医生照片路径',
  PRIMARY KEY (`doctor_id`, `department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hospital_info
-- ----------------------------
DROP TABLE IF EXISTS `hospital_info`;
CREATE TABLE `hospital_info`  (
  `hospital_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '医院id，用于科室指定所属的医院',
  `hospital_name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '医院名，不可重复，用于医院信息进行映射',
  `hospital_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '医院位置信息',
  `hospital_phone` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '医院联系电话',
  PRIMARY KEY (`hospital_id`) USING BTREE,
  UNIQUE INDEX `hospital_id`(`hospital_id`, `hospital_name`) USING BTREE COMMENT '医院主要的查询依据'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_hospital_reservation_info
-- ----------------------------
DROP TABLE IF EXISTS `user_hospital_reservation_info`;
CREATE TABLE `user_hospital_reservation_info`  (
  `reservation_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '预约订单号',
  `user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id，预约信息必须与某个用户链接才有意义',
  `doctor_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '医生id，预约信息必须与某个医生链接才有意义',
  `reservation_data` date NOT NULL COMMENT '预约时期',
  `reservation_price` float(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '预约价格',
  `reservation_status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态，0未支付，1已支付，2已报道，3已处理，9已取消',
  PRIMARY KEY (`reservation_id`, `user_id`, `doctor_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `username` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户名，不可重复，登录时使用',
  `user_pwd` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户密码，MD5加密',
  `user_pwd_salt` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户密码在加密过程中产生的盐，用于密码校验',
  `user_email` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户邮箱，可以用于密码找回',
  `user_phone` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户手机号，可以用于密码找回',
  `user_is_login` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户登录状态，0表示还没有登录，1表示已经登录',
  `user_last_login_time` datetime(1) NOT NULL DEFAULT '1000-01-01 00:00:00.0' COMMENT '用户最后登陆时间',
  `user_image_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户头像图片路径',
  PRIMARY KEY (`username`) USING BTREE,
  UNIQUE INDEX `user_login_info`(`username`, `user_pwd`, `user_pwd_salt`) USING BTREE COMMENT '用户登录所需要的信息（使用用户名进行登录）'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('test', '123', '123', '', 0, 0, '1000-01-01 00:00:00.0', '0');

-- ----------------------------
-- Table structure for user_medical_history
-- ----------------------------
DROP TABLE IF EXISTS `user_medical_history`;
CREATE TABLE `user_medical_history`  (
  `medical_history_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '病例id',
  `user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id，病例必须和某个用户链接才有意义',
  `doctor_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '医生id，病例必须和某个医生链接才有意义',
  `medical_history_date` date NOT NULL COMMENT '病例日期',
  `medical_history_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '病例文件对应的位置',
  PRIMARY KEY (`medical_history_id`, `user_id`, `doctor_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
