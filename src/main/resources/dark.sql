/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : dark

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 31/12/2020 18:00:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dark_photo_comment
-- ----------------------------
DROP TABLE IF EXISTS `dark_photo_comment`;
CREATE TABLE `dark_photo_comment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评论内容',
  `userId` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dark_photo_comment
-- ----------------------------
INSERT INTO `dark_photo_comment` VALUES (1, '1', 1, '2020-12-31 16:55:25');

-- ----------------------------
-- Table structure for dark_photo_gallery
-- ----------------------------
DROP TABLE IF EXISTS `dark_photo_gallery`;
CREATE TABLE `dark_photo_gallery`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `img_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片url地址',
  `img_type` int(0) NULL DEFAULT NULL COMMENT '图片类型 0 表示封面 1表示图片内容',
  `img_parent` int(0) NULL DEFAULT NULL COMMENT '图片集的文件名 父类Id',
  `userId` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dark_photo_gallery
-- ----------------------------
INSERT INTO `dark_photo_gallery` VALUES (1, '111', 'bbb', 10, 10, 1, NULL, NULL);
INSERT INTO `dark_photo_gallery` VALUES (2, '2', '2', 2, 2, 2, NULL, NULL);
INSERT INTO `dark_photo_gallery` VALUES (3, '3', '3', 3, 3, 2, NULL, NULL);
INSERT INTO `dark_photo_gallery` VALUES (10, '  <b>123</b>', '1111', 1, 1, 1, '2020-12-31 08:46:42', '2020-12-31 08:46:42');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账户',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `roles` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户角色 vip 会员 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'dark', 'dark123', 'vip');
INSERT INTO `user` VALUES (2, '123', '150920ccedc34d24031cdd3711e43310', NULL);

SET FOREIGN_KEY_CHECKS = 1;
