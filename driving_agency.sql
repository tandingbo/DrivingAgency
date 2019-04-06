Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : driving_demo

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 06/04/2019 16:42:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_acl
-- ----------------------------
DROP TABLE IF EXISTS `tb_acl`;
CREATE TABLE `tb_acl`
(
  `id`          int(11)                                                 NOT NULL AUTO_INCREMENT,
  `acl_name`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acl_url`     varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acl_remark`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status`      int(11)                                                 NULL DEFAULT NULL,
  `type`        int(11)                                                 NULL DEFAULT NULL,
  `update_time` datetime(0)                                             NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM
  AUTO_INCREMENT = 17
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_acl
-- ----------------------------
INSERT INTO `tb_acl`
VALUES (1, ''添加代理'', '' / manage / agent / add '', ''Admin'', ''只有超管和一级代理能添加代理'', 1, 1, ''2019-04-03 21:29:50'');
INSERT INTO `tb_acl`
VALUES (2, ''审核代理'', '' / manage / agent / examine '', ''Admin'', ''只有超管才能审核代理'', 1, 0, ''2019-04-03 21:32:58'');
INSERT INTO `tb_acl`
VALUES (3, ''导出Excel'', '' / manage / derived / excel '', ''Admin'', ''超管和所有代理都能导出Excel'', 1, 2, ''2019-04-03 21:48:08'');
INSERT INTO `tb_acl`
VALUES (4, ''发布公告'', '' / manage / announcement / publish '', ''Admin'', ''只有超管才能发布公告'', 1, 0, ''2019-04-03 21:50:52'');
INSERT INTO `tb_acl`
VALUES (5, ''查看代理'', '' / manage / agent / listall, /manage / agent / listbydaily, /manage / agent / listbytotal '',
        ''Admin'', ''只有超管和一级代理才能查看其下的代理'', 1, 1, ''2019-04-03 21:52:53'');
INSERT INTO `tb_acl`
VALUES (6, ''给代理点赞'', '' / manage / agent / star '', ''Admin'', ''所有代理都能给代理点赞'', 1, 2, ''2019-04-03 21:53:38'');
INSERT INTO `tb_acl`
VALUES (7, ''给代理评论'', '' / manage / agent / comment '', ''Admin'', ''所有代理都能给代理评论'', 1, 2, ''2019-04-03 21:54:35'');
INSERT INTO `tb_acl`
VALUES (8, ''添加学员'', '' / student / add '', ''Admin'', ''所有代理都能添加学员'', 1, 2, ''2019-04-03 21:56:55'');
INSERT INTO `tb_acl`
VALUES (9, ''查看所有未审核的代理'', '' / manage / agent / unexamine / listall '', ''Admin'', ''只有超管能看到所有未审核的代理'', 1, 0, ''2019-04-04 21:24:08'');
INSERT INTO `tb_acl`
VALUES (10, ''查看最新公告'', '' / manage / announcement / getlatest '', ''Admin'', ''所有代理都能查看公告'', 1, 2, ''2019-04-05 10:41:43'');
INSERT INTO `tb_acl`
VALUES (11, ''查看指定代理下的子代理'', '' / manage / agent / listbyname '', ''Admin'', ''只有超管才有这个权利'', 1, 0, ''2019-04-05 15:41:53'');
INSERT INTO `tb_acl`
VALUES (12, ''查看学员'', '' / student / listall, /student / listbypage '', ''Admin'', ''所有代理都能访问'', 1, 2,
        ''2019-04-05 21:41:23'');
INSERT INTO `tb_acl`
VALUES (13, ''查看所有未审核的学员'', '' / student / listunexamine '', ''Admin'', ''只有超管才能看到这个界面'', 1, 0, ''2019-04-05 21:52:33'');
INSERT INTO `tb_acl`
VALUES (14, ''审核学员'', '' / student / examine '', ''Admin'', ''只有超管才能审核学员'', 1, 0, ''2019-04-05 21:56:51'');
INSERT INTO `tb_acl`
VALUES (15, ''查看排行榜'', '' / ranking / listbydaily, /ranking / listbytotal '', ''Admin'', ''所有代理都能访问'', 1, 2,
        ''2019-04-06 10:15:44'');
INSERT INTO `tb_acl`
VALUES (16, ''上传文件'', '' / file / upload '', ''Admin'', ''所有代理都能访问'', 1, 2, ''2019-04-06 16:01:46'');

-- ----------------------------
-- Table structure for tb_agent
-- ----------------------------
DROP TABLE IF EXISTS `tb_agent`;
CREATE TABLE `tb_agent`
(
  `id`               int(11)                                                  NOT NULL AUTO_INCREMENT,
  `agent_achieve`    int(11)                                                  NULL DEFAULT NULL,
  `agent_email`      varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci   NULL DEFAULT NULL,
  `agent_idcard`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `agent_idcard_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_name`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `agent_password`   varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `agent_phone`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci   NULL DEFAULT NULL,
  `agent_school`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `parent_id`        int(11)                                                  NULL DEFAULT NULL,
  `status`           int(11)                                                  NULL DEFAULT NULL,
  `role_id`          int(11)                                                  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK87oi6qqndbhjr95l34sq1t7dv` (`role_id`) USING BTREE
) ENGINE = MyISAM
  AUTO_INCREMENT = 15
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_agent
-- ----------------------------
INSERT INTO `tb_agent`
VALUES (1, 0, ''beautifulsoup@163.com'', ''372330000007777663220'', ''http://47.95.244.237:8888/driving/M00/00/00/111'', ''Admin'', ''e10adc3949ba59abbe56e057f20f883e'', ''17864195200'', ''SDNU'', 0, 1, 1);
INSERT INTO `tb_agent`
VALUES (9, 1, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''1号代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 1, 1, 2);
INSERT INTO `tb_agent`
VALUES (10, 0, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''2号代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 1, 1, 2);
INSERT INTO `tb_agent`
VALUES (11, 0, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''2-1号代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 10, 1, 3);
INSERT INTO `tb_agent`
VALUES (12, 0, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''1-1号代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 9, 0, 3);
INSERT INTO `tb_agent`
VALUES (13, 0, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''1-2号代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 9, 1, 3);
INSERT INTO `tb_agent`
VALUES (14, 0, ''beautifulsoup@126.com'', ''372230000000000'', ''http://www.aa.jpg'', ''91代理'', ''670b14728ad9902aecba32e22fa4f6bd'', ''17864195552'', ''山东师范大学'', 1, 1, 2);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`
(
  `id`          int(11)                                                 NOT NULL AUTO_INCREMENT,
  `operator`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark`      varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_name`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status`      int(11)                                                 NULL DEFAULT NULL,
  `type`        int(11)                                                 NULL DEFAULT NULL,
  `update_time` datetime(0)                                             NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role`
VALUES (1, ''Admin'', '' 超级管理员最最高层级, 可以添加代理和学员 '', ''超级管理员'', 1, 0, ''2019-04-03 19:57:05'');
INSERT INTO `tb_role`
VALUES (2, ''Admin'', ''一级代理可以招收学员、添加二级代理等, 必须受管理员管理 '', ''一级代理'', 1, 1, ''2019-04-04 10:20:56'');
INSERT INTO `tb_role`
VALUES (3, ''Admin'', '' 二级代理只能招收学员, 必须受管理员和相应的一级代理的管理 '', ''二级代理'', 1, 2, ''2019-04-04 10:23:21'');

-- ----------------------------
-- Table structure for tb_role_acl
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_acl`;
CREATE TABLE `tb_role_acl`
(
  `role_id` int(11) NOT NULL,
  `acl_id`  int(11) NOT NULL
) ENGINE = MyISAM
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of tb_role_acl
-- ----------------------------
INSERT INTO `tb_role_acl`
VALUES (1, 1);
INSERT INTO `tb_role_acl`
VALUES (1, 2);
INSERT INTO `tb_role_acl`
VALUES (1, 3);
INSERT INTO `tb_role_acl`
VALUES (1, 4);
INSERT INTO `tb_role_acl`
VALUES (1, 5);
INSERT INTO `tb_role_acl`
VALUES (1, 6);
INSERT INTO `tb_role_acl`
VALUES (1, 7);
INSERT INTO `tb_role_acl`
VALUES (1, 8);
INSERT INTO `tb_role_acl`
VALUES (2, 1);
INSERT INTO `tb_role_acl`
VALUES (2, 3);
INSERT INTO `tb_role_acl`
VALUES (2, 5);
INSERT INTO `tb_role_acl`
VALUES (2, 6);
INSERT INTO `tb_role_acl`
VALUES (2, 7);
INSERT INTO `tb_role_acl`
VALUES (2, 8);
INSERT INTO `tb_role_acl`
VALUES (3, 3);
INSERT INTO `tb_role_acl`
VALUES (3, 8);
INSERT INTO `tb_role_acl`
VALUES (1, 9);
INSERT INTO `tb_role_acl`
VALUES (1, 10);
INSERT INTO `tb_role_acl`
VALUES (2, 10);
INSERT INTO `tb_role_acl`
VALUES (3, 10);
INSERT INTO `tb_role_acl`
VALUES (3, 6);
INSERT INTO `tb_role_acl`
VALUES (3, 7);
INSERT INTO `tb_role_acl`
VALUES (1, 12);
INSERT INTO `tb_role_acl`
VALUES (2, 12);
INSERT INTO `tb_role_acl`
VALUES (3, 12);
INSERT INTO `tb_role_acl`
VALUES (1, 13);
INSERT INTO `tb_role_acl`
VALUES (1, 14);
INSERT INTO `tb_role_acl`
VALUES (1, 15);
INSERT INTO `tb_role_acl`
VALUES (2, 15);
INSERT INTO `tb_role_acl`
VALUES (3, 15);
INSERT INTO `tb_role_acl`
VALUES (1, 16);
INSERT INTO `tb_role_acl`
VALUES (2, 16);
INSERT INTO `tb_role_acl`
VALUES (3, 16);

-- ----------------------------
-- Table structure for tb_student
-- ----------------------------
DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student`
(
  `id`             int(11)                                                  NOT NULL AUTO_INCREMENT,
  `operator`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `status`         int(11)                                                  NULL DEFAULT NULL,
  `student_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `student_img`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `student_name`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `student_phone`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci   NULL DEFAULT NULL,
  `student_price`  decimal(19, 2)                                           NULL DEFAULT NULL,
  `student_school` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `update_time`    datetime(0)                                              NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM
  AUTO_INCREMENT = 22
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_student
-- ----------------------------
INSERT INTO `tb_student`
VALUES (6, ''Admin'', 1, ''333656665'', ''http://www.naodi.png'', ''5号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 14:46:24'');
INSERT INTO `tb_student`
VALUES (7, ''Admin'', 1, ''333656665'', ''http://www.naodi.png'', ''4号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 14:51:17'');
INSERT INTO `tb_student`
VALUES (8, ''2-1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''2-1-1号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 14:54:35'');
INSERT INTO `tb_student`
VALUES (9, ''1-2号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-2-1号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:06:32'');
INSERT INTO `tb_student`
VALUES (10, ''1-2号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-2-1-1号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:08:10'');
INSERT INTO `tb_student`
VALUES (11, ''1号代理'', 0, ''333656665'', ''http://www.naodi.png'', ''1-3号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:12:43'');
INSERT INTO `tb_student`
VALUES (12, ''1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-4号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:12:52'');
INSERT INTO `tb_student`
VALUES (13, ''1-1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-1-1号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:13:46'');
INSERT INTO `tb_student`
VALUES (14, ''1-1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-1-2号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:13:50'');
INSERT INTO `tb_student`
VALUES (15, ''1-1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-1-3号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 15:13:54'');
INSERT INTO `tb_student`
VALUES (16, ''1-1号代理'', 1, ''333656665'', ''http://www.naodi.png'', ''1-1-4号学员'', ''17864553321'', NULL, ''北京师范大学'', ''2019-04-05 22:15:44'');
INSERT INTO `tb_student`
VALUES (21, ''1-2号代理'', 0, ''372330111198888'', ''http://goudao.png'', ''李狗蛋'', ''17864195555'', 6669.50, ''山东师范大学'', ''2019-04-05 18:36:14'');

SET FOREIGN_KEY_CHECKS = 1;
