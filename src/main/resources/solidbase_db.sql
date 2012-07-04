/*
Navicat MySQL Data Transfer

Source Server         : MySql
Source Server Version : 50511
Source Host           : localhost:3306
Source Database       : solidbase_db

Target Server Type    : MYSQL
Target Server Version : 50511
File Encoding         : 65001

Date: 2012-06-10 11:23:53
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `t_code`
-- ----------------------------
DROP TABLE IF EXISTS `t_code`;
CREATE TABLE `t_code` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODE_VALUE` varchar(32) NOT NULL,
  `TYPE_ID` bigint(10) DEFAULT NULL,
  `PARENT_ID` bigint(10) DEFAULT NULL,
  `REMARK` varchar(50) NOT NULL DEFAULT '无',
  `ADD_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE_VALUE` (`CODE_VALUE`),
  KEY `TYPE_ID` (`TYPE_ID`),
  KEY `PARENT_ID` (`PARENT_ID`),
  CONSTRAINT `t_code_ibfk_1` FOREIGN KEY (`TYPE_ID`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_code_ibfk_2` FOREIGN KEY (`PARENT_ID`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_code
-- ----------------------------
INSERT INTO `t_code` VALUES ('1', '元类型代码', '1', '1', '元类型代码', null, null);
INSERT INTO `t_code` VALUES ('3', 'depart-level', '1', '1', '部门级别', null, '2012-02-28 22:01:47');
INSERT INTO `t_code` VALUES ('4', 'depart', '1', '1', '部门', null, '2012-02-28 22:01:25');
INSERT INTO `t_code` VALUES ('5', 'depart-cate', '1', '1', '部门类别', null, '2012-02-28 22:01:30');
INSERT INTO `t_code` VALUES ('10', 'business', '5', '1', '业务部门', '2012-02-28 22:18:36', '2012-06-09 22:47:32');
INSERT INTO `t_code` VALUES ('11', 'level-1', '3', '1', '一级', '2012-02-28 22:18:59', '2012-02-28 22:27:19');
INSERT INTO `t_code` VALUES ('17', 'http-method', '1', '1', '资源操作', null, '2012-02-28 23:46:35');
INSERT INTO `t_code` VALUES ('18', 'perm-cate', '1', '1', '权限分类', null, '2012-02-28 23:49:20');
INSERT INTO `t_code` VALUES ('19', 'GET', '17', '1', '获取资源', '2012-02-28 23:46:53', '2012-02-28 23:46:53');
INSERT INTO `t_code` VALUES ('20', 'POST', '17', '1', '添加资源', '2012-02-28 23:47:04', '2012-02-28 23:47:04');
INSERT INTO `t_code` VALUES ('21', 'PUT', '17', '1', '更新资源', '2012-02-28 23:47:16', '2012-02-28 23:47:16');
INSERT INTO `t_code` VALUES ('22', 'DELETE', '17', '1', '删除资源', '2012-02-28 23:47:29', '2012-02-28 23:47:29');
INSERT INTO `t_code` VALUES ('23', 'user-module', '18', '1', '用户模块', '2012-02-28 23:49:54', '2012-03-08 08:29:46');
INSERT INTO `t_code` VALUES ('25', 'tech', '5', '1', '技术部门', '2012-03-06 22:05:58', '2012-03-06 22:05:58');
INSERT INTO `t_code` VALUES ('27', 'default-dev', '4', '31', '默认开发部', null, null);
INSERT INTO `t_code` VALUES ('28', 'default-mark', '4', '31', '默认市场部', null, null);
INSERT INTO `t_code` VALUES ('30', 'company', '5', '1', '公司', '2012-03-07 13:57:30', '2012-03-07 13:57:30');
INSERT INTO `t_code` VALUES ('31', 'xx-company', '4', '4', 'XX公司', null, null);
INSERT INTO `t_code` VALUES ('35', 'code-module', '18', '1', '代码模块', '2012-03-08 07:13:32', '2012-03-08 08:28:30');
INSERT INTO `t_code` VALUES ('36', 'depart-module', '18', '1', '部门模块', '2012-03-08 07:17:53', '2012-03-08 08:28:44');
INSERT INTO `t_code` VALUES ('37', 'main-module', '18', '1', '主模块', '2012-03-08 07:43:31', '2012-03-08 08:28:57');
INSERT INTO `t_code` VALUES ('38', 'navmenu-module', '18', '1', '导航菜单模块', '2012-03-08 07:45:20', '2012-03-08 08:29:17');
INSERT INTO `t_code` VALUES ('39', 'treemenu-module', '18', '1', '树形菜单模块', '2012-03-08 07:45:33', '2012-03-08 08:29:31');
INSERT INTO `t_code` VALUES ('40', 'perm-module', '18', '1', '权限模块', '2012-03-08 08:29:57', '2012-03-08 08:29:57');
INSERT INTO `t_code` VALUES ('41', 'preference-module', '18', '1', '系统参数模块', '2012-03-08 08:30:40', '2012-03-08 08:30:40');
INSERT INTO `t_code` VALUES ('42', 'role-module', '18', '1', '角色模块', '2012-03-08 08:30:51', '2012-03-08 08:30:51');
INSERT INTO `t_code` VALUES ('43', 'resource-module', '18', '1', '资源模块', '2012-03-08 08:31:37', '2012-03-08 08:31:37');
INSERT INTO `t_code` VALUES ('44', 'winon-tech-company', '4', '4', '万安科技有限公司', null, null);
INSERT INTO `t_code` VALUES ('45', 'dev-dept', '4', '44', '开发部', null, null);
INSERT INTO `t_code` VALUES ('46', 'business-dept', '4', '44', '业务部', null, null);
INSERT INTO `t_code` VALUES ('47', 'level-2', '3', '1', '二级', '2012-06-09 19:29:40', '2012-06-09 19:29:53');
INSERT INTO `t_code` VALUES ('48', 'test', '4', '44', '测试部门', null, null);

-- ----------------------------
-- Table structure for `t_department`
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODE_ID` bigint(20) NOT NULL,
  `DEPART_LEVEL` bigint(20) NOT NULL,
  `DEPART_CATE` bigint(20) NOT NULL,
  `ADD_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE_ID` (`CODE_ID`) USING BTREE,
  KEY `DEPART_LEVEL` (`DEPART_LEVEL`),
  KEY `DEPART_CATE` (`DEPART_CATE`),
  CONSTRAINT `t_department_ibfk_1` FOREIGN KEY (`CODE_ID`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_department_ibfk_2` FOREIGN KEY (`DEPART_LEVEL`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_department_ibfk_3` FOREIGN KEY (`DEPART_CATE`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES ('14', '44', '11', '30', '2012-03-14 13:48:29', '2012-03-14 13:48:29');
INSERT INTO `t_department` VALUES ('15', '45', '11', '25', '2012-03-14 13:48:51', '2012-03-14 13:48:51');
INSERT INTO `t_department` VALUES ('16', '46', '11', '10', '2012-03-14 13:49:17', '2012-06-09 19:25:38');
INSERT INTO `t_department` VALUES ('17', '48', '47', '10', '2012-06-09 19:30:19', '2012-06-09 22:48:06');

-- ----------------------------
-- Table structure for `t_department_ext`
-- ----------------------------
DROP TABLE IF EXISTS `t_department_ext`;
CREATE TABLE `t_department_ext` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SEAL_NAME` varchar(100) DEFAULT NULL,
  `CONTACT` varchar(20) DEFAULT NULL,
  `CONTACT_PHONE` varchar(20) DEFAULT NULL,
  `FAX` varchar(20) DEFAULT NULL,
  `ZIP` varchar(20) DEFAULT NULL,
  `CONTACT_ADDR` varchar(100) DEFAULT NULL,
  `POLICE_NUMBER` int(11) DEFAULT NULL,
  `ASSIST_NUMBER` int(11) DEFAULT NULL,
  `POLICE_KIND` bigint(20) NOT NULL,
  `IS_TEMPORARY` enum('no','yes') NOT NULL DEFAULT 'no',
  `DEPARTMENT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`) USING BTREE,
  KEY `POLICE_KIND` (`POLICE_KIND`),
  CONSTRAINT `t_department_ext_ibfk_4` FOREIGN KEY (`POLICE_KIND`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_department_ext_ibfk_5` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `t_department` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_department_ext
-- ----------------------------

-- ----------------------------
-- Table structure for `t_nav_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_nav_menu`;
CREATE TABLE `t_nav_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(16) NOT NULL,
  `HREF` varchar(255) DEFAULT NULL,
  `RANK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_nav_menu
-- ----------------------------
INSERT INTO `t_nav_menu` VALUES ('1', '系统管理', 'main/switchEnv/1', '0');
INSERT INTO `t_nav_menu` VALUES ('4', '文章发布', 'main/switchEnv/4', '1');
INSERT INTO `t_nav_menu` VALUES ('5', '站点维护', 'main/switchEnv/5', '2');
INSERT INTO `t_nav_menu` VALUES ('6', '数据采集', 'main/switchEnv/6', '3');
INSERT INTO `t_nav_menu` VALUES ('8', '打开JSP', 'main/switchEnv/hello.jsp', '5');
INSERT INTO `t_nav_menu` VALUES ('9', '打开HTML', 'main/switchEnv/hello.html', '4');

-- ----------------------------
-- Table structure for `t_perm_http_method`
-- ----------------------------
DROP TABLE IF EXISTS `t_perm_http_method`;
CREATE TABLE `t_perm_http_method` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `perm_id` bigint(20) NOT NULL,
  `http_method` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `perm_id` (`perm_id`),
  KEY `http_method` (`http_method`),
  CONSTRAINT `t_perm_http_method_ibfk_1` FOREIGN KEY (`perm_id`) REFERENCES `t_permission` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_perm_http_method_ibfk_2` FOREIGN KEY (`http_method`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_perm_http_method
-- ----------------------------
INSERT INTO `t_perm_http_method` VALUES ('152', '185', '20');
INSERT INTO `t_perm_http_method` VALUES ('153', '186', '22');
INSERT INTO `t_perm_http_method` VALUES ('154', '187', '19');
INSERT INTO `t_perm_http_method` VALUES ('155', '187', '20');
INSERT INTO `t_perm_http_method` VALUES ('156', '188', '19');
INSERT INTO `t_perm_http_method` VALUES ('157', '188', '20');
INSERT INTO `t_perm_http_method` VALUES ('158', '189', '19');
INSERT INTO `t_perm_http_method` VALUES ('159', '189', '20');
INSERT INTO `t_perm_http_method` VALUES ('160', '190', '19');
INSERT INTO `t_perm_http_method` VALUES ('161', '190', '20');
INSERT INTO `t_perm_http_method` VALUES ('162', '191', '22');
INSERT INTO `t_perm_http_method` VALUES ('163', '192', '19');
INSERT INTO `t_perm_http_method` VALUES ('164', '192', '20');
INSERT INTO `t_perm_http_method` VALUES ('165', '193', '19');
INSERT INTO `t_perm_http_method` VALUES ('166', '193', '20');
INSERT INTO `t_perm_http_method` VALUES ('167', '194', '21');
INSERT INTO `t_perm_http_method` VALUES ('169', '195', '20');
INSERT INTO `t_perm_http_method` VALUES ('170', '196', '22');
INSERT INTO `t_perm_http_method` VALUES ('171', '197', '19');
INSERT INTO `t_perm_http_method` VALUES ('172', '197', '20');
INSERT INTO `t_perm_http_method` VALUES ('173', '198', '19');
INSERT INTO `t_perm_http_method` VALUES ('174', '198', '20');
INSERT INTO `t_perm_http_method` VALUES ('175', '199', '21');
INSERT INTO `t_perm_http_method` VALUES ('176', '200', '22');
INSERT INTO `t_perm_http_method` VALUES ('177', '201', '19');
INSERT INTO `t_perm_http_method` VALUES ('178', '201', '20');
INSERT INTO `t_perm_http_method` VALUES ('179', '183', '19');
INSERT INTO `t_perm_http_method` VALUES ('180', '202', '19');
INSERT INTO `t_perm_http_method` VALUES ('181', '202', '20');
INSERT INTO `t_perm_http_method` VALUES ('182', '203', '19');
INSERT INTO `t_perm_http_method` VALUES ('183', '203', '20');
INSERT INTO `t_perm_http_method` VALUES ('184', '204', '19');
INSERT INTO `t_perm_http_method` VALUES ('185', '204', '20');
INSERT INTO `t_perm_http_method` VALUES ('186', '205', '19');
INSERT INTO `t_perm_http_method` VALUES ('187', '205', '20');
INSERT INTO `t_perm_http_method` VALUES ('188', '206', '20');
INSERT INTO `t_perm_http_method` VALUES ('189', '207', '22');
INSERT INTO `t_perm_http_method` VALUES ('190', '208', '19');
INSERT INTO `t_perm_http_method` VALUES ('191', '208', '20');
INSERT INTO `t_perm_http_method` VALUES ('192', '209', '19');
INSERT INTO `t_perm_http_method` VALUES ('193', '209', '20');
INSERT INTO `t_perm_http_method` VALUES ('194', '210', '19');
INSERT INTO `t_perm_http_method` VALUES ('195', '210', '20');
INSERT INTO `t_perm_http_method` VALUES ('196', '211', '19');
INSERT INTO `t_perm_http_method` VALUES ('197', '211', '20');
INSERT INTO `t_perm_http_method` VALUES ('198', '212', '19');
INSERT INTO `t_perm_http_method` VALUES ('199', '212', '20');
INSERT INTO `t_perm_http_method` VALUES ('200', '213', '19');
INSERT INTO `t_perm_http_method` VALUES ('201', '213', '20');
INSERT INTO `t_perm_http_method` VALUES ('202', '214', '22');
INSERT INTO `t_perm_http_method` VALUES ('203', '215', '19');
INSERT INTO `t_perm_http_method` VALUES ('204', '215', '20');
INSERT INTO `t_perm_http_method` VALUES ('205', '216', '21');
INSERT INTO `t_perm_http_method` VALUES ('206', '217', '19');
INSERT INTO `t_perm_http_method` VALUES ('207', '217', '20');
INSERT INTO `t_perm_http_method` VALUES ('208', '218', '19');
INSERT INTO `t_perm_http_method` VALUES ('209', '218', '20');
INSERT INTO `t_perm_http_method` VALUES ('210', '219', '22');
INSERT INTO `t_perm_http_method` VALUES ('211', '220', '20');
INSERT INTO `t_perm_http_method` VALUES ('212', '221', '19');
INSERT INTO `t_perm_http_method` VALUES ('213', '221', '20');
INSERT INTO `t_perm_http_method` VALUES ('214', '222', '19');
INSERT INTO `t_perm_http_method` VALUES ('215', '222', '20');
INSERT INTO `t_perm_http_method` VALUES ('216', '223', '22');
INSERT INTO `t_perm_http_method` VALUES ('217', '224', '21');
INSERT INTO `t_perm_http_method` VALUES ('218', '225', '19');
INSERT INTO `t_perm_http_method` VALUES ('219', '225', '20');
INSERT INTO `t_perm_http_method` VALUES ('220', '226', '20');
INSERT INTO `t_perm_http_method` VALUES ('221', '226', '21');
INSERT INTO `t_perm_http_method` VALUES ('222', '227', '19');
INSERT INTO `t_perm_http_method` VALUES ('223', '227', '20');
INSERT INTO `t_perm_http_method` VALUES ('224', '228', '19');
INSERT INTO `t_perm_http_method` VALUES ('225', '228', '20');
INSERT INTO `t_perm_http_method` VALUES ('226', '229', '20');
INSERT INTO `t_perm_http_method` VALUES ('227', '230', '22');
INSERT INTO `t_perm_http_method` VALUES ('228', '231', '19');
INSERT INTO `t_perm_http_method` VALUES ('229', '231', '20');
INSERT INTO `t_perm_http_method` VALUES ('230', '232', '20');
INSERT INTO `t_perm_http_method` VALUES ('232', '234', '19');
INSERT INTO `t_perm_http_method` VALUES ('233', '234', '20');
INSERT INTO `t_perm_http_method` VALUES ('234', '233', '20');
INSERT INTO `t_perm_http_method` VALUES ('235', '235', '20');
INSERT INTO `t_perm_http_method` VALUES ('236', '236', '22');
INSERT INTO `t_perm_http_method` VALUES ('237', '237', '20');
INSERT INTO `t_perm_http_method` VALUES ('238', '238', '20');
INSERT INTO `t_perm_http_method` VALUES ('239', '239', '19');
INSERT INTO `t_perm_http_method` VALUES ('240', '239', '20');
INSERT INTO `t_perm_http_method` VALUES ('241', '240', '22');
INSERT INTO `t_perm_http_method` VALUES ('242', '241', '19');
INSERT INTO `t_perm_http_method` VALUES ('243', '241', '20');
INSERT INTO `t_perm_http_method` VALUES ('244', '242', '19');
INSERT INTO `t_perm_http_method` VALUES ('245', '242', '20');
INSERT INTO `t_perm_http_method` VALUES ('246', '243', '19');
INSERT INTO `t_perm_http_method` VALUES ('247', '243', '20');
INSERT INTO `t_perm_http_method` VALUES ('248', '244', '22');
INSERT INTO `t_perm_http_method` VALUES ('249', '245', '19');
INSERT INTO `t_perm_http_method` VALUES ('250', '245', '20');
INSERT INTO `t_perm_http_method` VALUES ('251', '246', '19');
INSERT INTO `t_perm_http_method` VALUES ('252', '246', '20');
INSERT INTO `t_perm_http_method` VALUES ('253', '247', '19');
INSERT INTO `t_perm_http_method` VALUES ('254', '247', '20');
INSERT INTO `t_perm_http_method` VALUES ('255', '248', '19');
INSERT INTO `t_perm_http_method` VALUES ('256', '248', '20');
INSERT INTO `t_perm_http_method` VALUES ('257', '249', '21');
INSERT INTO `t_perm_http_method` VALUES ('258', '250', '19');
INSERT INTO `t_perm_http_method` VALUES ('259', '250', '20');
INSERT INTO `t_perm_http_method` VALUES ('260', '251', '21');
INSERT INTO `t_perm_http_method` VALUES ('263', '254', '21');
INSERT INTO `t_perm_http_method` VALUES ('266', '257', '21');
INSERT INTO `t_perm_http_method` VALUES ('267', '258', '21');
INSERT INTO `t_perm_http_method` VALUES ('268', '259', '21');
INSERT INTO `t_perm_http_method` VALUES ('269', '260', '21');
INSERT INTO `t_perm_http_method` VALUES ('270', '261', '22');
INSERT INTO `t_perm_http_method` VALUES ('271', '262', '19');
INSERT INTO `t_perm_http_method` VALUES ('272', '262', '20');
INSERT INTO `t_perm_http_method` VALUES ('273', '263', '19');
INSERT INTO `t_perm_http_method` VALUES ('274', '263', '20');
INSERT INTO `t_perm_http_method` VALUES ('276', '265', '19');
INSERT INTO `t_perm_http_method` VALUES ('277', '265', '20');
INSERT INTO `t_perm_http_method` VALUES ('278', '266', '20');
INSERT INTO `t_perm_http_method` VALUES ('279', '267', '22');
INSERT INTO `t_perm_http_method` VALUES ('280', '268', '20');
INSERT INTO `t_perm_http_method` VALUES ('281', '269', '22');
INSERT INTO `t_perm_http_method` VALUES ('282', '270', '19');
INSERT INTO `t_perm_http_method` VALUES ('283', '270', '20');
INSERT INTO `t_perm_http_method` VALUES ('284', '271', '19');
INSERT INTO `t_perm_http_method` VALUES ('285', '271', '20');
INSERT INTO `t_perm_http_method` VALUES ('286', '272', '22');
INSERT INTO `t_perm_http_method` VALUES ('287', '273', '19');
INSERT INTO `t_perm_http_method` VALUES ('288', '273', '20');
INSERT INTO `t_perm_http_method` VALUES ('289', '274', '19');
INSERT INTO `t_perm_http_method` VALUES ('290', '274', '20');
INSERT INTO `t_perm_http_method` VALUES ('291', '275', '22');
INSERT INTO `t_perm_http_method` VALUES ('292', '276', '21');
INSERT INTO `t_perm_http_method` VALUES ('293', '277', '19');
INSERT INTO `t_perm_http_method` VALUES ('294', '277', '20');
INSERT INTO `t_perm_http_method` VALUES ('295', '278', '20');
INSERT INTO `t_perm_http_method` VALUES ('296', '279', '22');
INSERT INTO `t_perm_http_method` VALUES ('297', '280', '19');
INSERT INTO `t_perm_http_method` VALUES ('298', '280', '20');
INSERT INTO `t_perm_http_method` VALUES ('299', '281', '20');
INSERT INTO `t_perm_http_method` VALUES ('300', '282', '22');
INSERT INTO `t_perm_http_method` VALUES ('301', '283', '19');
INSERT INTO `t_perm_http_method` VALUES ('302', '283', '20');
INSERT INTO `t_perm_http_method` VALUES ('303', '284', '19');
INSERT INTO `t_perm_http_method` VALUES ('304', '284', '20');
INSERT INTO `t_perm_http_method` VALUES ('305', '285', '22');
INSERT INTO `t_perm_http_method` VALUES ('306', '286', '21');
INSERT INTO `t_perm_http_method` VALUES ('307', '287', '19');
INSERT INTO `t_perm_http_method` VALUES ('308', '287', '20');
INSERT INTO `t_perm_http_method` VALUES ('309', '288', '19');
INSERT INTO `t_perm_http_method` VALUES ('310', '288', '20');
INSERT INTO `t_perm_http_method` VALUES ('311', '289', '19');
INSERT INTO `t_perm_http_method` VALUES ('312', '289', '20');
INSERT INTO `t_perm_http_method` VALUES ('313', '264', '19');
INSERT INTO `t_perm_http_method` VALUES ('314', '264', '20');

-- ----------------------------
-- Table structure for `t_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  `TYPE` bigint(20) NOT NULL,
  `REMARK` varchar(255) DEFAULT '无',
  `ADD_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `TYPE` (`TYPE`),
  KEY `RESOURCE_ID` (`RESOURCE_ID`),
  CONSTRAINT `t_permission_ibfk_1` FOREIGN KEY (`TYPE`) REFERENCES `t_code` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_permission_ibfk_2` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_resource` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('183', '访问后台主页', '5', '37', '', '2012-03-06 23:24:08', '2012-03-08 07:43:42');
INSERT INTO `t_permission` VALUES ('185', '添加代码信息', '7', '35', '', '2012-03-08 07:13:54', '2012-03-08 07:13:54');
INSERT INTO `t_permission` VALUES ('186', '批量删除代码', '8', '35', '', '2012-03-08 07:14:05', '2012-03-08 07:14:05');
INSERT INTO `t_permission` VALUES ('187', '查看代码列表', '9', '35', '', '2012-03-08 07:14:23', '2012-03-08 07:14:23');
INSERT INTO `t_permission` VALUES ('188', '访问添加代码页面', '10', '35', '', '2012-03-08 07:14:44', '2012-03-08 07:14:44');
INSERT INTO `t_permission` VALUES ('189', '查询代码', '11', '35', '', '2012-03-08 07:14:57', '2012-03-08 07:14:57');
INSERT INTO `t_permission` VALUES ('190', '获取代码类型Json格式数据', '12', '35', '', '2012-03-08 07:15:27', '2012-03-08 07:15:27');
INSERT INTO `t_permission` VALUES ('191', '删除某项代码', '13', '35', '', '2012-03-08 07:15:45', '2012-03-08 07:15:45');
INSERT INTO `t_permission` VALUES ('192', '编辑某项代码', '14', '35', '', '2012-03-08 07:16:01', '2012-03-08 07:16:01');
INSERT INTO `t_permission` VALUES ('193', '获取某类型父代码', '15', '35', '', '2012-03-08 07:16:17', '2012-03-08 07:16:17');
INSERT INTO `t_permission` VALUES ('194', '更新某代码', '16', '35', '', '2012-03-08 07:16:34', '2012-03-08 07:16:34');
INSERT INTO `t_permission` VALUES ('195', '添加部门', '17', '36', '', '2012-03-08 07:16:43', '2012-03-08 07:25:22');
INSERT INTO `t_permission` VALUES ('196', '批量删除部门', '18', '36', '\n', '2012-03-08 07:25:53', '2012-03-08 07:25:53');
INSERT INTO `t_permission` VALUES ('197', '查看部门列表', '19', '36', '', '2012-03-08 07:26:35', '2012-03-08 07:26:35');
INSERT INTO `t_permission` VALUES ('198', '访问添加部门页面', '20', '36', '', '2012-03-08 07:26:50', '2012-03-08 07:26:50');
INSERT INTO `t_permission` VALUES ('199', '更新某部门', '21', '36', '', '2012-03-08 07:27:02', '2012-03-08 07:27:02');
INSERT INTO `t_permission` VALUES ('200', '删除某部门', '21', '36', '', '2012-03-08 07:27:38', '2012-03-08 07:27:38');
INSERT INTO `t_permission` VALUES ('201', '编辑某部门', '22', '36', '', '2012-03-08 07:42:12', '2012-03-08 07:42:12');
INSERT INTO `t_permission` VALUES ('202', '关于我们', '23', '37', '', '2012-03-08 07:43:59', '2012-03-08 07:43:59');
INSERT INTO `t_permission` VALUES ('203', '切换左边树形菜单', '24', '37', '', '2012-03-08 07:44:17', '2012-03-08 07:44:17');
INSERT INTO `t_permission` VALUES ('204', '切换左边显示html页面', '25', '37', '', '2012-03-08 07:44:39', '2012-03-08 07:44:39');
INSERT INTO `t_permission` VALUES ('205', '切换左边显示JSP页面', '26', '37', '', '2012-03-08 07:44:53', '2012-03-08 07:44:53');
INSERT INTO `t_permission` VALUES ('206', '添加导航菜单', '27', '38', '', '2012-03-08 07:45:48', '2012-03-08 07:45:48');
INSERT INTO `t_permission` VALUES ('207', '批量删除导航菜单', '28', '38', '', '2012-03-08 07:46:01', '2012-03-08 07:46:01');
INSERT INTO `t_permission` VALUES ('208', '查看导航菜单列表', '29', '38', '', '2012-03-08 08:22:50', '2012-03-08 08:22:50');
INSERT INTO `t_permission` VALUES ('209', '查找带回导航菜单', '30', '38', '', '2012-03-08 08:23:09', '2012-03-08 08:23:09');
INSERT INTO `t_permission` VALUES ('210', '查找搜索带回导航菜单', '31', '38', '', '2012-03-08 08:23:29', '2012-03-08 08:23:29');
INSERT INTO `t_permission` VALUES ('211', '访问添加导航菜单页面', '32', '38', '', '2012-03-08 08:23:44', '2012-03-08 08:23:44');
INSERT INTO `t_permission` VALUES ('212', '查询导航菜单', '33', '38', '', '2012-03-08 08:23:56', '2012-03-08 08:23:56');
INSERT INTO `t_permission` VALUES ('213', '下拉获取导航菜单Json格式信息', '34', '38', '', '2012-03-08 08:24:37', '2012-03-08 08:24:37');
INSERT INTO `t_permission` VALUES ('214', '删除某导航菜单', '35', '38', '', '2012-03-08 08:24:57', '2012-03-08 08:24:57');
INSERT INTO `t_permission` VALUES ('215', '编辑某导航菜单', '36', '38', '', '2012-03-08 08:25:11', '2012-03-08 08:25:11');
INSERT INTO `t_permission` VALUES ('216', '更新某导航菜单', '37', '38', '', '2012-03-08 08:25:25', '2012-03-08 08:25:25');
INSERT INTO `t_permission` VALUES ('217', '查看用户日志', '38', '23', '', '2012-03-08 08:33:00', '2012-03-08 08:33:00');
INSERT INTO `t_permission` VALUES ('218', '获取某用户的角色', '39', '23', '', '2012-03-08 08:34:02', '2012-03-08 08:34:02');
INSERT INTO `t_permission` VALUES ('219', '批量删除权限', '40', '40', '', '2012-03-08 08:34:26', '2012-03-08 08:34:26');
INSERT INTO `t_permission` VALUES ('220', '添加权限', '41', '40', '', '2012-03-08 08:34:38', '2012-03-08 08:34:38');
INSERT INTO `t_permission` VALUES ('221', '查看权限列表', '42', '40', '', '2012-03-08 08:34:52', '2012-03-08 08:34:52');
INSERT INTO `t_permission` VALUES ('222', '访问添加权限页面', '43', '40', '', '2012-03-08 08:35:09', '2012-03-08 08:35:09');
INSERT INTO `t_permission` VALUES ('223', '删除某权限', '44', '40', '', '2012-03-08 08:35:25', '2012-03-08 08:35:25');
INSERT INTO `t_permission` VALUES ('224', '更新某权限', '44', '40', '', '2012-03-08 08:35:36', '2012-03-08 08:35:36');
INSERT INTO `t_permission` VALUES ('225', '编辑某权限', '45', '40', '', '2012-03-08 08:35:49', '2012-03-08 08:35:49');
INSERT INTO `t_permission` VALUES ('226', '更新系统参数', '46', '41', '', '2012-03-08 08:36:19', '2012-03-08 08:36:19');
INSERT INTO `t_permission` VALUES ('227', '查看系统参数列表', '47', '41', '', '2012-03-08 08:36:32', '2012-03-08 08:36:32');
INSERT INTO `t_permission` VALUES ('228', '编辑某系统参数', '48', '41', '', '2012-03-08 08:36:43', '2012-03-08 08:36:43');
INSERT INTO `t_permission` VALUES ('229', '添加资源', '49', '43', '', '2012-03-08 08:36:53', '2012-03-08 08:36:53');
INSERT INTO `t_permission` VALUES ('230', '批量删除资源', '50', '43', '', '2012-03-08 08:37:32', '2012-03-08 08:37:32');
INSERT INTO `t_permission` VALUES ('231', '查看资源列表', '51', '43', '', '2012-03-08 08:37:48', '2012-03-08 08:37:48');
INSERT INTO `t_permission` VALUES ('232', '添加角色', '52', '42', '', '2012-03-08 08:38:01', '2012-03-08 08:38:01');
INSERT INTO `t_permission` VALUES ('233', '访问分配权限页面', '53', '40', '', '2012-03-08 08:38:18', '2012-03-08 08:39:07');
INSERT INTO `t_permission` VALUES ('234', '查看角色列表', '54', '42', '', '2012-03-08 08:38:35', '2012-03-08 08:38:35');
INSERT INTO `t_permission` VALUES ('235', '分配权限', '55', '40', '', '2012-03-08 08:39:25', '2012-03-08 08:39:25');
INSERT INTO `t_permission` VALUES ('236', '删除角色权限', '56', '40', '', '2012-03-08 08:39:51', '2012-03-08 08:39:51');
INSERT INTO `t_permission` VALUES ('237', '添加树形菜单', '57', '39', '', '2012-03-08 08:40:09', '2012-03-08 08:40:09');
INSERT INTO `t_permission` VALUES ('238', '添加用户', '58', '23', '', '2012-03-08 08:40:26', '2012-03-08 08:40:26');
INSERT INTO `t_permission` VALUES ('239', '查看用户列表', '59', '23', '', '2012-03-08 08:40:42', '2012-03-08 08:40:42');
INSERT INTO `t_permission` VALUES ('240', '批量删除树形菜单', '61', '39', '', '2012-06-09 23:31:29', '2012-06-09 23:31:29');
INSERT INTO `t_permission` VALUES ('241', '查看树形菜单列表', '62', '39', '', '2012-06-09 23:32:48', '2012-06-09 23:32:48');
INSERT INTO `t_permission` VALUES ('242', '访问添加树形菜单页面', '63', '39', '', '2012-06-09 23:33:10', '2012-06-09 23:33:10');
INSERT INTO `t_permission` VALUES ('243', '查询树形菜单', '64', '39', '', '2012-06-09 23:33:29', '2012-06-09 23:33:29');
INSERT INTO `t_permission` VALUES ('244', '删除某树形菜单', '65', '39', '', '2012-06-09 23:33:50', '2012-06-09 23:33:50');
INSERT INTO `t_permission` VALUES ('245', '编辑某树形菜单', '66', '39', '', '2012-06-09 23:34:53', '2012-06-09 23:34:53');
INSERT INTO `t_permission` VALUES ('246', '查找带回某导航菜单下的树形菜单', '67', '39', '', '2012-06-09 23:35:28', '2012-06-09 23:35:28');
INSERT INTO `t_permission` VALUES ('247', '在查找带回中查询树形菜单', '68', '39', '', '2012-06-09 23:37:10', '2012-06-09 23:37:10');
INSERT INTO `t_permission` VALUES ('248', '获取某导航菜单下的树形菜单JSON数据', '69', '39', '', '2012-06-09 23:37:41', '2012-06-09 23:37:41');
INSERT INTO `t_permission` VALUES ('249', '更新某树形菜单', '70', '39', '', '2012-06-09 23:37:56', '2012-06-09 23:37:56');
INSERT INTO `t_permission` VALUES ('250', '查看我的资料', '71', '23', '', '2012-06-09 23:42:10', '2012-06-09 23:42:10');
INSERT INTO `t_permission` VALUES ('251', '更新某用户', '72', '23', '', '2012-06-09 23:42:32', '2012-06-09 23:42:32');
INSERT INTO `t_permission` VALUES ('254', '退出后台', '74', '23', '', '2012-06-10 00:35:03', '2012-06-10 00:35:03');
INSERT INTO `t_permission` VALUES ('257', '锁定用户', '76', '23', '', '2012-06-10 00:35:55', '2012-06-10 00:35:55');
INSERT INTO `t_permission` VALUES ('258', '解锁用户', '77', '23', '', '2012-06-10 00:36:14', '2012-06-10 00:36:14');
INSERT INTO `t_permission` VALUES ('259', '批量锁定用户', '78', '23', '', '2012-06-10 00:36:35', '2012-06-10 00:36:35');
INSERT INTO `t_permission` VALUES ('260', '批量解锁用户', '79', '23', '', '2012-06-10 00:36:48', '2012-06-10 00:36:48');
INSERT INTO `t_permission` VALUES ('261', '批量删除用户', '80', '23', '', '2012-06-10 00:37:03', '2012-06-10 00:37:03');
INSERT INTO `t_permission` VALUES ('262', '分配部门', '81', '23', '', '2012-06-10 00:37:16', '2012-06-10 00:37:16');
INSERT INTO `t_permission` VALUES ('263', '分配角色', '82', '23', '', '2012-06-10 00:37:28', '2012-06-10 00:37:28');
INSERT INTO `t_permission` VALUES ('264', '访问添加用户页', '83', '23', '', '2012-06-10 00:38:22', '2012-06-10 01:34:03');
INSERT INTO `t_permission` VALUES ('265', '查询用户', '84', '23', '', '2012-06-10 00:38:32', '2012-06-10 00:38:32');
INSERT INTO `t_permission` VALUES ('266', '为用户分配部门', '85', '23', '', '2012-06-10 00:38:59', '2012-06-10 00:38:59');
INSERT INTO `t_permission` VALUES ('267', '删除用户分配的部门', '86', '23', '', '2012-06-10 00:39:23', '2012-06-10 00:39:23');
INSERT INTO `t_permission` VALUES ('268', '为用户分配角色', '87', '23', '', '2012-06-10 00:39:38', '2012-06-10 00:39:38');
INSERT INTO `t_permission` VALUES ('269', '删除用户分配的角色', '88', '23', '', '2012-06-10 00:39:54', '2012-06-10 00:39:54');
INSERT INTO `t_permission` VALUES ('270', '编辑某用户', '89', '23', '', '2012-06-10 00:41:00', '2012-06-10 00:41:00');
INSERT INTO `t_permission` VALUES ('271', '获取为某用户已分配的部门', '90', '23', '', '2012-06-10 00:41:32', '2012-06-10 00:41:32');
INSERT INTO `t_permission` VALUES ('272', '删除某用户', '72', '23', '', '2012-06-10 01:15:17', '2012-06-10 01:15:17');
INSERT INTO `t_permission` VALUES ('273', '获取URI资源JSON格式', '91', '43', '', '2012-06-10 01:15:54', '2012-06-10 01:15:54');
INSERT INTO `t_permission` VALUES ('274', '访问添加URI资源页', '92', '43', '', '2012-06-10 01:16:10', '2012-06-10 01:16:10');
INSERT INTO `t_permission` VALUES ('275', '删除某URI资源', '60', '43', '', '2012-06-10 01:16:26', '2012-06-10 01:16:26');
INSERT INTO `t_permission` VALUES ('276', '更新某URI资源', '60', '43', '', '2012-06-10 01:17:03', '2012-06-10 01:17:03');
INSERT INTO `t_permission` VALUES ('277', '编辑某URI资源', '93', '43', '', '2012-06-10 01:17:18', '2012-06-10 01:17:18');
INSERT INTO `t_permission` VALUES ('278', '分配菜单', '94', '40', '', '2012-06-10 01:20:11', '2012-06-10 01:20:11');
INSERT INTO `t_permission` VALUES ('279', '批量删除角色', '95', '42', '', '2012-06-10 01:20:24', '2012-06-10 01:20:24');
INSERT INTO `t_permission` VALUES ('280', '访问添加角色页', '96', '42', '', '2012-06-10 01:20:37', '2012-06-10 01:20:37');
INSERT INTO `t_permission` VALUES ('281', '为角色分配菜单', '97', '40', '', '2012-06-10 01:20:54', '2012-06-10 01:20:54');
INSERT INTO `t_permission` VALUES ('282', '删除角色已分配的菜单', '98', '40', '', '2012-06-10 01:21:15', '2012-06-10 01:21:15');
INSERT INTO `t_permission` VALUES ('283', '查询角色', '99', '42', '', '2012-06-10 01:21:26', '2012-06-10 01:21:26');
INSERT INTO `t_permission` VALUES ('284', '获取角色JSON数据', '100', '42', '', '2012-06-10 01:21:42', '2012-06-10 01:21:42');
INSERT INTO `t_permission` VALUES ('285', '删除某角色', '101', '42', '', '2012-06-10 01:21:53', '2012-06-10 01:21:53');
INSERT INTO `t_permission` VALUES ('286', '更新某角色', '102', '42', '', '2012-06-10 01:22:06', '2012-06-10 01:22:06');
INSERT INTO `t_permission` VALUES ('287', '编辑某角色', '103', '42', '', '2012-06-10 01:22:17', '2012-06-10 01:22:17');
INSERT INTO `t_permission` VALUES ('288', '获取某角色已分配的菜单', '104', '42', '', '2012-06-10 01:22:42', '2012-06-10 01:22:42');
INSERT INTO `t_permission` VALUES ('289', '获取某角色已分配的权限', '105', '42', '', '2012-06-10 01:23:00', '2012-06-10 01:23:00');

-- ----------------------------
-- Table structure for `t_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uri` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uri` (`uri`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
INSERT INTO `t_resource` VALUES ('7', 'codes');
INSERT INTO `t_resource` VALUES ('8', 'codes/batchRemove');
INSERT INTO `t_resource` VALUES ('9', 'codes/list');
INSERT INTO `t_resource` VALUES ('10', 'codes/new');
INSERT INTO `t_resource` VALUES ('11', 'codes/search');
INSERT INTO `t_resource` VALUES ('12', 'codes/types.json');
INSERT INTO `t_resource` VALUES ('13', 'codes/{codeId}');
INSERT INTO `t_resource` VALUES ('14', 'codes/{codeId}/edit');
INSERT INTO `t_resource` VALUES ('15', 'codes/{codeTypeId}/parents.json');
INSERT INTO `t_resource` VALUES ('16', 'codes/{editCodeId}');
INSERT INTO `t_resource` VALUES ('17', 'departments');
INSERT INTO `t_resource` VALUES ('18', 'departments/batchRemove');
INSERT INTO `t_resource` VALUES ('19', 'departments/list');
INSERT INTO `t_resource` VALUES ('20', 'departments/new');
INSERT INTO `t_resource` VALUES ('21', 'departments/{departId}');
INSERT INTO `t_resource` VALUES ('22', 'departments/{departId}/edit');
INSERT INTO `t_resource` VALUES ('5', 'main');
INSERT INTO `t_resource` VALUES ('23', 'main/about-us');
INSERT INTO `t_resource` VALUES ('24', 'main/switchEnv/{navMenuId}');
INSERT INTO `t_resource` VALUES ('25', 'main/switchEnv/{pageName}.html');
INSERT INTO `t_resource` VALUES ('26', 'main/switchEnv/{pageName}.jsp');
INSERT INTO `t_resource` VALUES ('27', 'navmenus');
INSERT INTO `t_resource` VALUES ('28', 'navmenus/batchRemove');
INSERT INTO `t_resource` VALUES ('29', 'navmenus/list');
INSERT INTO `t_resource` VALUES ('30', 'navmenus/lookup');
INSERT INTO `t_resource` VALUES ('31', 'navmenus/lookupSearch');
INSERT INTO `t_resource` VALUES ('32', 'navmenus/new');
INSERT INTO `t_resource` VALUES ('33', 'navmenus/search');
INSERT INTO `t_resource` VALUES ('34', 'navmenus/suggest.json');
INSERT INTO `t_resource` VALUES ('35', 'navmenus/{id}');
INSERT INTO `t_resource` VALUES ('36', 'navmenus/{id}/edit');
INSERT INTO `t_resource` VALUES ('37', 'navmenus/{navMenuId}');
INSERT INTO `t_resource` VALUES ('41', 'permissions');
INSERT INTO `t_resource` VALUES ('40', 'permissions/batchRemove');
INSERT INTO `t_resource` VALUES ('42', 'permissions/list');
INSERT INTO `t_resource` VALUES ('43', 'permissions/new');
INSERT INTO `t_resource` VALUES ('44', 'permissions/{permId}');
INSERT INTO `t_resource` VALUES ('45', 'permissions/{permId}/edit');
INSERT INTO `t_resource` VALUES ('46', 'preferences');
INSERT INTO `t_resource` VALUES ('47', 'preferences/list');
INSERT INTO `t_resource` VALUES ('48', 'preferences/{preferId}/edit');
INSERT INTO `t_resource` VALUES ('49', 'resources');
INSERT INTO `t_resource` VALUES ('50', 'resources/batchRemove');
INSERT INTO `t_resource` VALUES ('91', 'resources/json');
INSERT INTO `t_resource` VALUES ('51', 'resources/list');
INSERT INTO `t_resource` VALUES ('92', 'resources/new');
INSERT INTO `t_resource` VALUES ('60', 'resources/{resId}');
INSERT INTO `t_resource` VALUES ('93', 'resources/{resId}/edit');
INSERT INTO `t_resource` VALUES ('52', 'roles');
INSERT INTO `t_resource` VALUES ('94', 'roles/alloc-menu');
INSERT INTO `t_resource` VALUES ('53', 'roles/alloc-permission');
INSERT INTO `t_resource` VALUES ('95', 'roles/batchRemove');
INSERT INTO `t_resource` VALUES ('54', 'roles/list');
INSERT INTO `t_resource` VALUES ('96', 'roles/new');
INSERT INTO `t_resource` VALUES ('97', 'roles/role-menu');
INSERT INTO `t_resource` VALUES ('98', 'roles/role-menu/remove');
INSERT INTO `t_resource` VALUES ('55', 'roles/role-permission');
INSERT INTO `t_resource` VALUES ('56', 'roles/role-permission/remove');
INSERT INTO `t_resource` VALUES ('99', 'roles/search');
INSERT INTO `t_resource` VALUES ('100', 'roles/suggest.json');
INSERT INTO `t_resource` VALUES ('101', 'roles/{id}');
INSERT INTO `t_resource` VALUES ('102', 'roles/{roleId}');
INSERT INTO `t_resource` VALUES ('103', 'roles/{roleId}/edit');
INSERT INTO `t_resource` VALUES ('104', 'roles/{roleId}/menus');
INSERT INTO `t_resource` VALUES ('105', 'roles/{roleId}/permissions');
INSERT INTO `t_resource` VALUES ('57', 'treemenus');
INSERT INTO `t_resource` VALUES ('61', 'treemenus/batchRemove');
INSERT INTO `t_resource` VALUES ('62', 'treemenus/list');
INSERT INTO `t_resource` VALUES ('63', 'treemenus/new');
INSERT INTO `t_resource` VALUES ('64', 'treemenus/search');
INSERT INTO `t_resource` VALUES ('65', 'treemenus/{id}');
INSERT INTO `t_resource` VALUES ('66', 'treemenus/{id}/edit');
INSERT INTO `t_resource` VALUES ('67', 'treemenus/{navMenuId}/lookup');
INSERT INTO `t_resource` VALUES ('68', 'treemenus/{navMenuId}/lookupSearch');
INSERT INTO `t_resource` VALUES ('69', 'treemenus/{navMenuId}/suggest.json');
INSERT INTO `t_resource` VALUES ('70', 'treemenus/{treeMenuId}');
INSERT INTO `t_resource` VALUES ('58', 'users');
INSERT INTO `t_resource` VALUES ('81', 'users/alloc-department');
INSERT INTO `t_resource` VALUES ('82', 'users/alloc-role');
INSERT INTO `t_resource` VALUES ('78', 'users/batchLock');
INSERT INTO `t_resource` VALUES ('80', 'users/batchRemove');
INSERT INTO `t_resource` VALUES ('79', 'users/batchUnLock');
INSERT INTO `t_resource` VALUES ('59', 'users/list');
INSERT INTO `t_resource` VALUES ('73', 'users/login');
INSERT INTO `t_resource` VALUES ('74', 'users/logout');
INSERT INTO `t_resource` VALUES ('38', 'users/logs');
INSERT INTO `t_resource` VALUES ('83', 'users/new');
INSERT INTO `t_resource` VALUES ('71', 'users/profile');
INSERT INTO `t_resource` VALUES ('75', 'users/register');
INSERT INTO `t_resource` VALUES ('84', 'users/search');
INSERT INTO `t_resource` VALUES ('85', 'users/user-department');
INSERT INTO `t_resource` VALUES ('86', 'users/user-department/remove');
INSERT INTO `t_resource` VALUES ('87', 'users/user-role');
INSERT INTO `t_resource` VALUES ('88', 'users/user-role/remove');
INSERT INTO `t_resource` VALUES ('72', 'users/{id}');
INSERT INTO `t_resource` VALUES ('89', 'users/{id}/edit');
INSERT INTO `t_resource` VALUES ('76', 'users/{id}/lock');
INSERT INTO `t_resource` VALUES ('77', 'users/{id}/unLock');
INSERT INTO `t_resource` VALUES ('90', 'users/{userId}/departments');
INSERT INTO `t_resource` VALUES ('39', 'users/{userId}/roles');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL DEFAULT '无',
  `ADD_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('5', '默认角色', '用来配置权限的角色', '2012-03-07 00:07:32', '2012-03-30 14:34:16');
INSERT INTO `t_role` VALUES ('6', '代码管理员', '只能看到代码管理部分的菜单', '2012-03-28 01:50:43', '2012-03-28 01:50:43');
INSERT INTO `t_role` VALUES ('7', '网站管理员', '网站管理', '2012-03-28 13:31:38', '2012-03-28 13:31:38');
INSERT INTO `t_role` VALUES ('8', '文章发布员', '文章发布', '2012-03-28 13:31:48', '2012-03-28 13:31:48');

-- ----------------------------
-- Table structure for `t_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint(20) NOT NULL,
  `MENU_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  KEY `MENU_ID` (`MENU_ID`),
  CONSTRAINT `t_role_menu_ibfk_3` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_role` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_role_menu_ibfk_4` FOREIGN KEY (`MENU_ID`) REFERENCES `t_tree_menu` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('166', '8', '381');
INSERT INTO `t_role_menu` VALUES ('167', '8', '382');
INSERT INTO `t_role_menu` VALUES ('168', '7', '384');
INSERT INTO `t_role_menu` VALUES ('169', '7', '385');
INSERT INTO `t_role_menu` VALUES ('170', '7', '387');
INSERT INTO `t_role_menu` VALUES ('171', '7', '389');
INSERT INTO `t_role_menu` VALUES ('172', '6', '192');
INSERT INTO `t_role_menu` VALUES ('173', '6', '193');
INSERT INTO `t_role_menu` VALUES ('176', '6', '185');
INSERT INTO `t_role_menu` VALUES ('177', '6', '186');
INSERT INTO `t_role_menu` VALUES ('178', '5', '185');
INSERT INTO `t_role_menu` VALUES ('179', '5', '186');
INSERT INTO `t_role_menu` VALUES ('180', '5', '379');
INSERT INTO `t_role_menu` VALUES ('181', '5', '192');
INSERT INTO `t_role_menu` VALUES ('182', '5', '193');
INSERT INTO `t_role_menu` VALUES ('183', '5', '342');
INSERT INTO `t_role_menu` VALUES ('184', '5', '343');
INSERT INTO `t_role_menu` VALUES ('185', '5', '180');
INSERT INTO `t_role_menu` VALUES ('186', '5', '367');
INSERT INTO `t_role_menu` VALUES ('187', '5', '187');
INSERT INTO `t_role_menu` VALUES ('188', '5', '361');
INSERT INTO `t_role_menu` VALUES ('189', '5', '362');
INSERT INTO `t_role_menu` VALUES ('190', '5', '370');
INSERT INTO `t_role_menu` VALUES ('191', '5', '377');
INSERT INTO `t_role_menu` VALUES ('192', '5', '188');
INSERT INTO `t_role_menu` VALUES ('193', '5', '375');
INSERT INTO `t_role_menu` VALUES ('194', '5', '376');
INSERT INTO `t_role_menu` VALUES ('195', '5', '368');
INSERT INTO `t_role_menu` VALUES ('196', '5', '369');
INSERT INTO `t_role_menu` VALUES ('197', '5', '380');
INSERT INTO `t_role_menu` VALUES ('198', '5', '189');
INSERT INTO `t_role_menu` VALUES ('199', '5', '381');
INSERT INTO `t_role_menu` VALUES ('200', '5', '382');
INSERT INTO `t_role_menu` VALUES ('201', '5', '384');
INSERT INTO `t_role_menu` VALUES ('202', '5', '385');
INSERT INTO `t_role_menu` VALUES ('203', '5', '387');
INSERT INTO `t_role_menu` VALUES ('204', '5', '389');
INSERT INTO `t_role_menu` VALUES ('205', '5', '395');

-- ----------------------------
-- Table structure for `t_role_navmenu`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_navmenu`;
CREATE TABLE `t_role_navmenu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint(20) NOT NULL,
  `NAVMENU_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  KEY `NAVMENU_ID` (`NAVMENU_ID`),
  CONSTRAINT `t_role_navmenu_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_role` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_role_navmenu_ibfk_2` FOREIGN KEY (`NAVMENU_ID`) REFERENCES `t_nav_menu` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_navmenu
-- ----------------------------
INSERT INTO `t_role_navmenu` VALUES ('10', '8', '8');
INSERT INTO `t_role_navmenu` VALUES ('11', '5', '9');
INSERT INTO `t_role_navmenu` VALUES ('12', '5', '8');

-- ----------------------------
-- Table structure for `t_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint(20) NOT NULL,
  `PERM_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  KEY `PERM_ID` (`PERM_ID`),
  CONSTRAINT `t_role_permission_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_role` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_role_permission_ibfk_2` FOREIGN KEY (`PERM_ID`) REFERENCES `t_permission` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=458 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('203', '7', '194');
INSERT INTO `t_role_permission` VALUES ('204', '7', '193');
INSERT INTO `t_role_permission` VALUES ('205', '7', '192');
INSERT INTO `t_role_permission` VALUES ('206', '7', '191');
INSERT INTO `t_role_permission` VALUES ('207', '7', '190');
INSERT INTO `t_role_permission` VALUES ('208', '7', '189');
INSERT INTO `t_role_permission` VALUES ('209', '7', '188');
INSERT INTO `t_role_permission` VALUES ('210', '7', '187');
INSERT INTO `t_role_permission` VALUES ('211', '7', '186');
INSERT INTO `t_role_permission` VALUES ('212', '7', '185');
INSERT INTO `t_role_permission` VALUES ('213', '7', '201');
INSERT INTO `t_role_permission` VALUES ('214', '7', '200');
INSERT INTO `t_role_permission` VALUES ('215', '7', '199');
INSERT INTO `t_role_permission` VALUES ('216', '7', '198');
INSERT INTO `t_role_permission` VALUES ('217', '7', '197');
INSERT INTO `t_role_permission` VALUES ('218', '7', '196');
INSERT INTO `t_role_permission` VALUES ('219', '7', '195');
INSERT INTO `t_role_permission` VALUES ('220', '7', '205');
INSERT INTO `t_role_permission` VALUES ('221', '7', '204');
INSERT INTO `t_role_permission` VALUES ('222', '7', '203');
INSERT INTO `t_role_permission` VALUES ('223', '7', '202');
INSERT INTO `t_role_permission` VALUES ('224', '7', '183');
INSERT INTO `t_role_permission` VALUES ('225', '7', '216');
INSERT INTO `t_role_permission` VALUES ('226', '7', '215');
INSERT INTO `t_role_permission` VALUES ('227', '7', '214');
INSERT INTO `t_role_permission` VALUES ('228', '7', '213');
INSERT INTO `t_role_permission` VALUES ('229', '7', '212');
INSERT INTO `t_role_permission` VALUES ('230', '7', '211');
INSERT INTO `t_role_permission` VALUES ('231', '7', '210');
INSERT INTO `t_role_permission` VALUES ('232', '7', '209');
INSERT INTO `t_role_permission` VALUES ('233', '7', '208');
INSERT INTO `t_role_permission` VALUES ('234', '7', '207');
INSERT INTO `t_role_permission` VALUES ('235', '7', '206');
INSERT INTO `t_role_permission` VALUES ('236', '7', '236');
INSERT INTO `t_role_permission` VALUES ('237', '7', '235');
INSERT INTO `t_role_permission` VALUES ('238', '7', '233');
INSERT INTO `t_role_permission` VALUES ('239', '7', '225');
INSERT INTO `t_role_permission` VALUES ('240', '7', '224');
INSERT INTO `t_role_permission` VALUES ('241', '7', '223');
INSERT INTO `t_role_permission` VALUES ('242', '7', '222');
INSERT INTO `t_role_permission` VALUES ('243', '7', '221');
INSERT INTO `t_role_permission` VALUES ('244', '7', '220');
INSERT INTO `t_role_permission` VALUES ('245', '7', '219');
INSERT INTO `t_role_permission` VALUES ('246', '7', '228');
INSERT INTO `t_role_permission` VALUES ('247', '7', '227');
INSERT INTO `t_role_permission` VALUES ('248', '7', '226');
INSERT INTO `t_role_permission` VALUES ('249', '7', '231');
INSERT INTO `t_role_permission` VALUES ('250', '7', '230');
INSERT INTO `t_role_permission` VALUES ('251', '7', '229');
INSERT INTO `t_role_permission` VALUES ('252', '7', '234');
INSERT INTO `t_role_permission` VALUES ('253', '7', '232');
INSERT INTO `t_role_permission` VALUES ('254', '7', '237');
INSERT INTO `t_role_permission` VALUES ('255', '7', '239');
INSERT INTO `t_role_permission` VALUES ('256', '7', '238');
INSERT INTO `t_role_permission` VALUES ('257', '7', '218');
INSERT INTO `t_role_permission` VALUES ('258', '7', '217');
INSERT INTO `t_role_permission` VALUES ('259', '6', '194');
INSERT INTO `t_role_permission` VALUES ('260', '6', '193');
INSERT INTO `t_role_permission` VALUES ('261', '6', '192');
INSERT INTO `t_role_permission` VALUES ('262', '6', '191');
INSERT INTO `t_role_permission` VALUES ('263', '6', '190');
INSERT INTO `t_role_permission` VALUES ('264', '6', '189');
INSERT INTO `t_role_permission` VALUES ('265', '6', '188');
INSERT INTO `t_role_permission` VALUES ('266', '6', '187');
INSERT INTO `t_role_permission` VALUES ('267', '6', '186');
INSERT INTO `t_role_permission` VALUES ('268', '6', '185');
INSERT INTO `t_role_permission` VALUES ('280', '6', '183');
INSERT INTO `t_role_permission` VALUES ('292', '6', '236');
INSERT INTO `t_role_permission` VALUES ('293', '6', '235');
INSERT INTO `t_role_permission` VALUES ('294', '6', '233');
INSERT INTO `t_role_permission` VALUES ('302', '6', '228');
INSERT INTO `t_role_permission` VALUES ('303', '6', '227');
INSERT INTO `t_role_permission` VALUES ('305', '6', '231');
INSERT INTO `t_role_permission` VALUES ('306', '6', '230');
INSERT INTO `t_role_permission` VALUES ('307', '6', '229');
INSERT INTO `t_role_permission` VALUES ('308', '6', '234');
INSERT INTO `t_role_permission` VALUES ('309', '6', '232');
INSERT INTO `t_role_permission` VALUES ('310', '6', '237');
INSERT INTO `t_role_permission` VALUES ('311', '6', '239');
INSERT INTO `t_role_permission` VALUES ('312', '6', '238');
INSERT INTO `t_role_permission` VALUES ('427', '6', '208');
INSERT INTO `t_role_permission` VALUES ('428', '6', '250');
INSERT INTO `t_role_permission` VALUES ('429', '6', '197');
INSERT INTO `t_role_permission` VALUES ('430', '6', '203');
INSERT INTO `t_role_permission` VALUES ('431', '6', '202');
INSERT INTO `t_role_permission` VALUES ('432', '6', '212');
INSERT INTO `t_role_permission` VALUES ('433', '6', '221');
INSERT INTO `t_role_permission` VALUES ('434', '6', '283');
INSERT INTO `t_role_permission` VALUES ('435', '6', '241');
INSERT INTO `t_role_permission` VALUES ('436', '6', '265');
INSERT INTO `t_role_permission` VALUES ('437', '6', '254');
INSERT INTO `t_role_permission` VALUES ('438', '6', '251');
INSERT INTO `t_role_permission` VALUES ('439', '5', '189');
INSERT INTO `t_role_permission` VALUES ('440', '5', '187');
INSERT INTO `t_role_permission` VALUES ('441', '5', '197');
INSERT INTO `t_role_permission` VALUES ('442', '5', '203');
INSERT INTO `t_role_permission` VALUES ('443', '5', '202');
INSERT INTO `t_role_permission` VALUES ('444', '5', '183');
INSERT INTO `t_role_permission` VALUES ('445', '5', '212');
INSERT INTO `t_role_permission` VALUES ('446', '5', '208');
INSERT INTO `t_role_permission` VALUES ('447', '5', '221');
INSERT INTO `t_role_permission` VALUES ('448', '5', '227');
INSERT INTO `t_role_permission` VALUES ('449', '5', '231');
INSERT INTO `t_role_permission` VALUES ('450', '5', '283');
INSERT INTO `t_role_permission` VALUES ('451', '5', '234');
INSERT INTO `t_role_permission` VALUES ('452', '5', '241');
INSERT INTO `t_role_permission` VALUES ('453', '5', '265');
INSERT INTO `t_role_permission` VALUES ('454', '5', '254');
INSERT INTO `t_role_permission` VALUES ('455', '5', '251');
INSERT INTO `t_role_permission` VALUES ('456', '5', '250');
INSERT INTO `t_role_permission` VALUES ('457', '5', '239');

-- ----------------------------
-- Table structure for `t_tree_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_tree_menu`;
CREATE TABLE `t_tree_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(16) NOT NULL,
  `TARGET` varchar(20) DEFAULT NULL,
  `REL` varchar(20) DEFAULT NULL,
  `RELOAD_FLAG` varchar(1) NOT NULL DEFAULT '1',
  `HREF` varchar(255) DEFAULT NULL,
  `PID` bigint(20) DEFAULT NULL,
  `RANK` int(11) NOT NULL DEFAULT '0',
  `NAV_MENU_ID` bigint(20) DEFAULT NULL,
  `EXTERNAL` varchar(5) NOT NULL DEFAULT 'false',
  `WIDTH` int(11) NOT NULL DEFAULT '400',
  `HEIGHT` int(11) NOT NULL DEFAULT '200',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `NAV_MENU_ID` (`NAV_MENU_ID`),
  KEY `PID` (`PID`),
  CONSTRAINT `t_tree_menu_ibfk_1` FOREIGN KEY (`NAV_MENU_ID`) REFERENCES `t_nav_menu` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_tree_menu_ibfk_2` FOREIGN KEY (`PID`) REFERENCES `t_tree_menu` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=396 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tree_menu
-- ----------------------------
INSERT INTO `t_tree_menu` VALUES ('1', '顶级菜单', null, null, '1', null, '1', '0', '1', 'false', '400', '200');
INSERT INTO `t_tree_menu` VALUES ('150', '角色管理', 'navTab', '', '0', '#', '1', '3', '1', 'false', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('151', '用户管理', 'navTab', '', '1', '', '1', '4', '1', 'false', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('152', '菜单管理', 'navTab', '', '0', '', '1', '0', '1', 'false', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('153', '权限管理', 'navTab', '', '0', '', '1', '6', '1', 'false', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('155', '代码管理', 'navTab', '', '0', '', '1', '1', '1', 'false', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('180', '列出所有角色', 'navTab', 'xssyjs', '1', 'roles/list', '150', '0', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('185', '导航菜单', 'navTab', 'xssydhcd', '1', 'navmenus/list', '152', '0', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('186', '树形菜单', 'navTab', 'xssysxcd', '1', 'treemenus/list', '152', '1', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('187', '列出所有用户', 'navTab', 'xssyyh', '1', 'users/list', '151', '0', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('188', '我的个人资料', 'dialog', 'wdgrzl', '1', 'users/profile', '151', '2', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('189', '定义权限', 'dialog', 'dyqx', '1', 'permissions/new', '153', '3', '1', 'false', '800', '300');
INSERT INTO `t_tree_menu` VALUES ('192', '列出所有代码', 'navTab', 'xssydm', '1', 'codes/list', '155', '0', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('193', '新增代码', 'dialog', 'xzdm', '0', 'codes/new', '155', '0', '1', ' ', '400', '300');
INSERT INTO `t_tree_menu` VALUES ('341', '部门管理', 'navTab', 'bmgl', '0', '', '1', '2', '1', 'false', '500', '300');
INSERT INTO `t_tree_menu` VALUES ('342', '列出全部部门', 'navTab', 'xssybm', '1', 'departments/list', '341', '0', '1', 'false', '500', '300');
INSERT INTO `t_tree_menu` VALUES ('343', '添加部门', 'dialog', 'tjbm', '0', 'departments/new', '341', '0', '1', 'false', '500', '300');
INSERT INTO `t_tree_menu` VALUES ('361', '分配部门', 'navTab', 'fpbm', '1', 'users/alloc-department', '151', '0', '1', 'false', '100', '100');
INSERT INTO `t_tree_menu` VALUES ('362', '分配角色', 'navTab', 'fpjs', '1', 'users/alloc-role', '151', '0', '1', 'false', '100', '100');
INSERT INTO `t_tree_menu` VALUES ('364', '文章管理', 'navTab', '', '0', '', '1', '7', '4', 'false', '1', '1');
INSERT INTO `t_tree_menu` VALUES ('367', '新增角色', 'dialog', 'xzjs', '0', 'roles/new', '150', '0', '1', 'false', '300', '200');
INSERT INTO `t_tree_menu` VALUES ('368', '分配权限', 'navTab', 'fpqx', '0', 'roles/alloc-permission', '153', '0', '1', 'false', '1', '1');
INSERT INTO `t_tree_menu` VALUES ('369', '列出全部权限定义', 'navTab', 'xssyqxdy', '0', 'permissions/list', '153', '0', '1', 'false', '1', '1');
INSERT INTO `t_tree_menu` VALUES ('370', '添加用户', 'dialog', 'xzyh', '0', 'users/new', '151', '0', '1', 'false', '500', '600');
INSERT INTO `t_tree_menu` VALUES ('374', '资源管理', 'navTab', '', '0', '', '1', '5', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('375', '列出所有资源', 'navTab', 'xssyzy', '0', 'resources/list', '374', '0', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('376', '新增资源', 'dialog', 'xzzy', '0', 'resources/new', '374', '0', '1', 'false', '300', '150');
INSERT INTO `t_tree_menu` VALUES ('377', '查看用户日志', 'navTab', 'xssyyhrz', '1', 'users/logs', '151', '0', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('378', '系统参数', 'navTab', '', '0', '', '1', '0', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('379', '全部参数', 'navTab', 'xssyxtcs', '0', 'preferences/list', '378', '0', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('380', '分配菜单', 'navTab', 'fpcd', '0', 'roles/alloc-menu', '153', '0', '1', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('381', '全部文章', 'navTab', 'xssywz', '0', 'articles/list', '364', '0', '4', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('382', '发表文章', 'navTab', 'xzwz', '0', 'articles/new', '364', '0', '4', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('383', '网站栏目', 'navTab', '', '0', '', '1', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('384', '全部栏目', 'navTab', 'xssylm', '0', 'columns/list', '383', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('385', '新增栏目', 'navTab', 'xzlm', '0', 'columns/new', '383', '0', '5', 'false', '500', '400');
INSERT INTO `t_tree_menu` VALUES ('386', '信息维护', 'navTab', '', '0', '', '1', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('387', '基础信息设置', 'navTab', 'jcxxsz', '0', 'infos/setting', '386', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('388', 'SEO设置', 'navTab', '', '0', '', '1', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('389', '关键词设置', 'navTab', 'gjcsz', '0', 'keywords/setting', '388', '0', '5', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('390', '数据采集', 'navTab', '', '0', '', '1', '0', '6', 'false', '0', '0');
INSERT INTO `t_tree_menu` VALUES ('395', 'Web爬虫抓取', 'navTab', '', '0', '', '390', '0', '6', 'false', '0', '0');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACCOUNT` varchar(16) NOT NULL,
  `PASSWORD` varchar(32) NOT NULL,
  `STATUS` enum('锁定','正常') NOT NULL DEFAULT '正常',
  `LAST_LOGIN_TIME` varchar(19) DEFAULT NULL,
  `LAST_LOGIN_IP` varchar(19) NOT NULL DEFAULT '',
  `REGISTER_TIME` varchar(20) DEFAULT NULL,
  `ADD_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  `TRUE_NAME` varchar(20) NOT NULL DEFAULT '匿名',
  `EMAIL` varchar(20) NOT NULL DEFAULT '无',
  `FAX` varchar(20) NOT NULL DEFAULT '无',
  `ADDR` varchar(20) DEFAULT NULL,
  `OFFICE_PHONE` varchar(20) DEFAULT NULL,
  `MOBILE_NUM` varchar(20) DEFAULT NULL,
  `HOME_PHONE` varchar(20) DEFAULT NULL,
  `AVAILABLE_PERIOD` varchar(20) DEFAULT NULL,
  `ID_NUM` varchar(20) DEFAULT NULL,
  `SUPER_POWER` enum('yes','no') NOT NULL DEFAULT 'yes',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCOUNT` (`ACCOUNT`),
  KEY `TRUE_NAME` (`TRUE_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('9', 'solidbase', '4c5531126fd5b96261ebb2637c299e34', '正常', '2012-06-10 01:33:04', '127.0.0.1', '2012-03-06 21:55:51', '2012-03-06 21:55:51', '2012-06-10 01:32:52', '演示账号', 'eweb4j@163.com', '', '中国广东', '', '', '', '', '', 'yes');
INSERT INTO `t_user` VALUES ('17', 'weiwei', '7bc16d8f67f52d74f0fc2df48096204a', '正常', '2012-06-10 11:09:34', '127.0.0.1', '2012-03-07 20:33:21', '2012-03-07 20:33:21', '2012-06-10 11:10:06', '匿名', '无', '无', '', '', '', '', '', '44162544554878985', 'no');
INSERT INTO `t_user` VALUES ('18', 'code', 'c13367945d5d4c91047b3b50234aa7ab', '正常', '2012-06-10 00:42:14', '127.0.0.1', '2012-03-28 01:51:02', '2012-03-28 01:51:02', '2012-06-09 23:43:25', 'weiwei2', '', '', '', '', '', '', '', '', 'no');
INSERT INTO `t_user` VALUES ('19', 'article', '92a2b5cb9c6906035c2864fa225e1940', '正常', '2012-03-30 17:03:44', '192.168.0.25', '2012-03-28 13:32:38', '2012-03-28 13:32:38', '2012-03-28 22:31:19', '', '', '', '', '', '', '', '', '', 'no');
INSERT INTO `t_user` VALUES ('20', 'website', 'd1befa03c79ca0b84ecc488dea96bc68', '正常', '2012-03-30 14:58:22', '192.168.0.25', '2012-03-28 13:32:50', '2012-03-28 13:32:50', '2012-03-28 21:59:57', '', '', '', '', '', '', '', '', '', 'no');
INSERT INTO `t_user` VALUES ('21', 'test', '098f6bcd4621d373cade4e832627b4f6', '正常', '2012-03-28 15:44:31', '127.0.0.1', '2012-03-28 14:03:22', '2012-03-28 14:03:22', '2012-03-28 14:03:22', '', '', '', '', '', '', '', '', '', 'yes');
INSERT INTO `t_user` VALUES ('22', 'testweiwei', '71f62e874c426cfc9169e58a4e87cb4a', '正常', '2012-06-09 18:34:19', '127.0.0.1', '2012-06-09 18:34:10', '2012-06-09 18:34:10', '2012-06-09 18:36:35', '匿名', '无', '无', '', '', '', '', '', '', 'yes');
INSERT INTO `t_user` VALUES ('23', 'test2', 'e10adc3949ba59abbe56e057f20f883e', '锁定', '2012-06-09 22:46:51', '127.0.0.1', '2012-06-09 22:46:40', '2012-06-09 22:46:40', '2012-06-09 22:46:40', '匿名', '无', '无', null, null, null, null, null, null, 'yes');

-- ----------------------------
-- Table structure for `t_user_activity_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_activity_log`;
CREATE TABLE `t_user_activity_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) DEFAULT NULL,
  `TIME` varchar(20) NOT NULL,
  `ACTIVITY` varchar(500) NOT NULL DEFAULT '',
  `RESULT` enum('success','false') NOT NULL,
  `FAILURE_CAUSE` varchar(1000) NOT NULL DEFAULT '',
  `USER_NAME` varchar(20) NOT NULL DEFAULT '''佚名''',
  `USER_ACCOUNT` varchar(32) NOT NULL DEFAULT '''匿名账号''',
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `t_user_activity_log_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `t_user` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_activity_log
-- ----------------------------
INSERT INTO `t_user_activity_log` VALUES ('1', '9', '2012-03-07 14:04:17', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('2', '9', '2012-03-07 14:11:04', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('3', '9', '2012-03-07 14:11:05', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('4', '9', '2012-03-07 14:11:31', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('5', '9', '2012-03-07 14:15:11', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('6', '9', '2012-03-07 14:15:15', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('7', '9', '2012-03-07 18:24:16', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('8', '9', '2012-03-07 18:24:31', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('9', '9', '2012-03-07 18:54:44', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('10', '9', '2012-03-07 18:55:03', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('11', '9', '2012-03-07 18:56:03', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('12', '9', '2012-03-07 19:02:01', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('13', '9', '2012-03-07 19:02:09', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('14', '9', '2012-03-07 19:08:44', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('15', '9', '2012-03-07 19:22:52', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('16', '9', '2012-03-07 19:25:12', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('17', '9', '2012-03-07 19:25:37', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('18', '9', '2012-03-07 19:25:38', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('19', '9', '2012-03-07 19:25:43', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('20', '9', '2012-03-07 19:25:54', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('21', '9', '2012-03-07 19:26:59', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('22', '9', '2012-03-07 19:27:28', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('23', '9', '2012-03-07 19:27:41', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('24', '9', '2012-03-07 19:28:22', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('25', '9', '2012-03-07 19:28:29', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('26', '9', '2012-03-07 19:28:39', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('27', '9', '2012-03-07 19:29:09', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('28', '9', '2012-03-07 19:29:22', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('29', '9', '2012-03-07 19:30:17', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('30', '9', '2012-03-07 19:30:23', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('31', '9', '2012-03-07 19:30:50', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('32', '9', '2012-03-07 19:31:58', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('33', '9', '2012-03-07 19:45:59', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('34', '9', '2012-03-07 19:46:05', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('35', '9', '2012-03-07 20:49:27', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('36', '9', '2012-03-07 20:52:25', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('37', '9', '2012-03-07 20:59:09', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('38', '9', '2012-03-07 21:11:25', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('39', '9', '2012-03-07 21:12:26', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('40', '9', '2012-03-07 21:16:02', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('41', '9', '2012-03-07 21:16:22', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('42', '9', '2012-03-08 06:25:02', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('43', '9', '2012-03-08 06:25:06', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('44', '9', '2012-03-08 06:26:05', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('45', '9', '2012-03-08 06:26:17', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('46', '9', '2012-03-08 06:27:02', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('47', '9', '2012-03-08 06:27:41', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('48', '9', '2012-03-08 06:40:32', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('49', '9', '2012-03-08 07:12:33', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('50', '9', '2012-03-08 07:16:59', '访问添加代码页面', 'false', '用户权限不足, 无法执行[访问添加代码页面]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('51', '9', '2012-03-28 21:56:38', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('52', '9', '2012-03-28 21:56:44', '查看导航菜单列表', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('53', '9', '2012-03-28 21:56:45', '访问添加导航菜单页面', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('54', '9', '2012-03-28 21:56:51', '添加导航菜单', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('55', '9', '2012-03-28 21:56:52', '查看导航菜单列表', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('56', '9', '2012-03-28 21:56:54', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('57', '9', '2012-03-28 21:56:56', '查看导航菜单列表', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('58', '9', '2012-03-28 21:57:13', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('59', '20', '2012-03-28 22:00:14', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('60', '20', '2012-03-28 22:00:20', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('61', '20', '2012-03-28 22:21:57', '访问后台主页', 'success', '', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('62', '20', '2012-03-28 22:22:00', '查看导航菜单列表', 'success', '', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('63', '9', '2012-03-28 22:25:21', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('64', '19', '2012-03-28 22:31:27', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('65', '19', '2012-03-28 22:34:11', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('66', '19', '2012-03-28 22:35:59', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('67', '20', '2012-03-28 23:20:39', '访问后台主页', 'success', '', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('68', '19', '2012-03-28 23:52:13', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('69', '18', '2012-03-28 23:53:45', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('70', '18', '2012-03-28 23:54:40', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('71', '18', '2012-03-28 23:55:46', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('72', '18', '2012-03-28 23:56:19', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('73', '18', '2012-03-29 19:22:21', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('74', '18', '2012-03-29 19:23:05', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('75', '18', '2012-03-29 19:23:58', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('76', '18', '2012-03-29 19:31:26', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('77', '18', '2012-03-30 11:53:13', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('78', '9', '2012-03-30 11:53:36', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('79', '18', '2012-03-30 11:54:08', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('80', '18', '2012-03-30 11:55:46', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('81', '18', '2012-03-30 12:03:40', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('82', '18', '2012-03-30 12:07:14', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('83', '18', '2012-03-30 12:07:46', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('84', '18', '2012-03-30 12:09:10', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('85', '18', '2012-03-30 12:11:42', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('86', '18', '2012-03-30 12:11:46', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('87', '18', '2012-03-30 12:11:47', '访问添加代码页面', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('88', '18', '2012-03-30 12:11:48', '获取代码类型Json格式数据', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('89', '18', '2012-03-30 12:11:52', '添加代码信息', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('90', '18', '2012-03-30 12:11:52', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('91', '18', '2012-03-30 12:11:57', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('92', '18', '2012-03-30 12:12:00', '批量删除代码', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('93', '18', '2012-03-30 12:12:00', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('94', '18', '2012-03-30 12:12:01', '获取代码类型Json格式数据', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('95', '18', '2012-03-30 12:12:03', '查询代码', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('96', '19', '2012-03-30 12:12:18', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('97', '18', '2012-03-30 12:13:28', '访问后台主页', 'false', '用户权限不足, 无法执行[访问后台主页]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('98', '18', '2012-03-30 14:34:25', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('99', '19', '2012-03-30 14:34:36', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('100', '19', '2012-03-30 14:34:47', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('101', '19', '2012-03-30 14:42:24', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('102', '18', '2012-03-30 14:44:43', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('103', '18', '2012-03-30 14:44:45', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('104', '18', '2012-03-30 14:57:48', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('105', '18', '2012-03-30 14:57:51', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('106', '19', '2012-03-30 14:58:05', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('107', '20', '2012-03-30 14:58:22', '访问后台主页', 'success', '', '', 'website');
INSERT INTO `t_user_activity_log` VALUES ('108', '18', '2012-03-30 14:59:39', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('109', '18', '2012-03-30 14:59:43', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('110', '19', '2012-03-30 17:03:44', '访问后台主页', 'success', '', '', 'article');
INSERT INTO `t_user_activity_log` VALUES ('111', '18', '2012-06-09 22:51:35', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('112', '18', '2012-06-09 22:51:37', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('113', '18', '2012-06-09 22:51:40', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('114', '18', '2012-06-09 22:51:41', '访问添加代码页面', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('115', '18', '2012-06-09 22:51:44', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('116', '18', '2012-06-09 22:51:45', '访问添加导航菜单页面', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('117', '18', '2012-06-09 22:51:47', '添加导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('118', '18', '2012-06-09 22:51:48', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('119', '18', '2012-06-09 22:51:51', '批量删除导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('120', '18', '2012-06-09 22:51:51', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('121', '18', '2012-06-09 22:54:54', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('122', '18', '2012-06-09 22:54:56', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('123', '18', '2012-06-09 22:54:57', '访问添加导航菜单页面', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('124', '18', '2012-06-09 22:54:59', '添加导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('125', '18', '2012-06-09 22:54:59', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('126', '18', '2012-06-09 22:55:03', '批量删除导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('127', '18', '2012-06-09 22:55:03', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('128', '18', '2012-06-09 22:55:12', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('129', '18', '2012-06-09 22:55:22', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('130', '18', '2012-06-09 22:55:27', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('131', '18', '2012-06-09 22:55:32', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('132', '18', '2012-06-09 22:55:38', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('133', '18', '2012-06-09 22:55:41', '关于我们', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('134', '18', '2012-06-09 22:57:28', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('135', '18', '2012-06-09 22:57:34', '关于我们', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('136', '18', '2012-06-09 22:57:53', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('137', '18', '2012-06-09 22:57:54', '访问添加导航菜单页面', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('138', '18', '2012-06-09 22:57:58', '添加导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('139', '18', '2012-06-09 22:57:58', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('140', '18', '2012-06-09 22:58:01', '批量删除导航菜单', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('141', '18', '2012-06-09 22:58:01', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('142', '18', '2012-06-09 22:58:03', '查看代码列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('143', '18', '2012-06-09 23:12:29', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('144', '18', '2012-06-09 23:13:31', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('145', '18', '2012-06-09 23:14:18', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('146', '18', '2012-06-09 23:19:47', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('147', '18', '2012-06-09 23:20:36', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('148', '18', '2012-06-09 23:25:26', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('149', '18', '2012-06-09 23:25:34', '查看导航菜单列表', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('150', '18', '2012-06-09 23:27:27', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('151', '18', '2012-06-09 23:27:30', '查看导航菜单列表', 'false', '用户权限不足, 无法执行[查看导航菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('152', '18', '2012-06-09 23:29:42', '访问后台主页', 'success', '', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('153', '18', '2012-06-09 23:30:18', '查看导航菜单列表', 'false', '用户权限不足, 无法执行[查看导航菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('154', '18', '2012-06-09 23:38:07', '访问后台主页', 'success', '用户权限不足, 无法执行[查看导航菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('155', '18', '2012-06-09 23:38:09', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('156', '18', '2012-06-09 23:39:50', '查看导航菜单列表', 'false', '用户权限不足, 无法执行[查看导航菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('157', '18', '2012-06-09 23:39:53', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('158', '18', '2012-06-09 23:43:00', '访问后台主页', 'success', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('159', '18', '2012-06-09 23:43:02', '查看我的资料', 'success', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('160', '18', '2012-06-09 23:43:07', '查看我的资料', 'success', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('161', '18', '2012-06-09 23:43:16', '查看我的资料', 'success', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('162', '18', '2012-06-09 23:43:53', '查看导航菜单列表', 'success', '用户权限不足, 无法执行[查看树形菜单列表]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('163', '18', '2012-06-09 23:43:54', '访问添加导航菜单页面', 'false', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', '', 'code');
INSERT INTO `t_user_activity_log` VALUES ('164', '18', '2012-06-09 23:46:25', '访问后台主页', 'success', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('165', '18', '2012-06-09 23:46:27', '查看导航菜单列表', 'success', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('166', '18', '2012-06-09 23:47:59', '查看导航菜单列表', 'success', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('167', '18', '2012-06-09 23:48:22', '查看导航菜单列表', 'success', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('168', '18', '2012-06-09 23:48:29', '查看导航菜单列表', 'success', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('169', '18', '2012-06-09 23:53:17', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('170', '18', '2012-06-09 23:54:17', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('171', '18', '2012-06-09 23:54:43', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('172', '18', '2012-06-09 23:54:45', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('173', '18', '2012-06-09 23:54:55', '批量删除导航菜单', 'false', '用户权限不足, 无法执行[批量删除导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('174', '18', '2012-06-09 23:55:00', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('175', '18', '2012-06-09 23:55:56', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('176', '18', '2012-06-09 23:55:59', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('177', '18', '2012-06-09 23:56:47', '批量删除导航菜单', 'false', '用户权限不足, 无法执行[批量删除导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('178', '18', '2012-06-10 00:07:13', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('179', '18', '2012-06-10 00:07:16', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('180', '18', '2012-06-10 00:07:18', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('181', '18', '2012-06-10 00:23:28', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('182', '18', '2012-06-10 00:23:32', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('183', '18', '2012-06-10 00:23:46', '访问添加导航菜单页面', 'false', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('184', '18', '2012-06-10 00:23:50', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('185', '18', '2012-06-10 00:27:17', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('186', '18', '2012-06-10 00:27:18', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('187', '18', '2012-06-10 00:27:20', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('188', '18', '2012-06-10 00:27:32', '删除某导航菜单', 'false', '用户权限不足, 无法执行[删除某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('189', '18', '2012-06-10 00:27:34', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('190', '18', '2012-06-10 00:28:24', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('191', '18', '2012-06-10 00:28:25', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('192', '18', '2012-06-10 00:28:28', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('193', '18', '2012-06-10 00:28:30', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('194', '18', '2012-06-10 00:28:34', '查看代码列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('195', '18', '2012-06-10 00:28:36', '访问添加代码页面', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('196', '18', '2012-06-10 00:28:41', '编辑某项代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('197', '18', '2012-06-10 00:28:42', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('198', '18', '2012-06-10 00:28:46', '编辑某项代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('199', '18', '2012-06-10 00:28:47', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('200', '18', '2012-06-10 00:28:48', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('201', '18', '2012-06-10 00:28:48', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('202', '18', '2012-06-10 00:28:49', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('203', '18', '2012-06-10 00:28:49', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('204', '18', '2012-06-10 00:28:49', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('205', '18', '2012-06-10 00:28:49', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('206', '18', '2012-06-10 00:28:49', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('207', '18', '2012-06-10 00:28:50', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('208', '18', '2012-06-10 00:29:01', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('209', '18', '2012-06-10 00:29:02', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('210', '18', '2012-06-10 00:29:03', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('211', '18', '2012-06-10 00:29:04', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('212', '18', '2012-06-10 00:29:06', '访问添加代码页面', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('213', '18', '2012-06-10 00:29:07', '获取代码类型Json格式数据', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('214', '18', '2012-06-10 00:29:09', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('215', '18', '2012-06-10 00:29:17', '编辑某项代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('216', '18', '2012-06-10 00:29:20', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('217', '18', '2012-06-10 00:29:21', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('218', '18', '2012-06-10 00:29:21', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('219', '18', '2012-06-10 00:29:22', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('220', '18', '2012-06-10 00:29:22', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('221', '18', '2012-06-10 00:29:22', '获取某类型父代码', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('222', '18', '2012-06-10 00:29:29', '查看我的资料', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('223', '18', '2012-06-10 00:29:30', '更新某用户', 'false', '用户权限不足, 无法执行[更新某用户]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('224', '18', '2012-06-10 00:29:37', '关于我们', 'false', '用户权限不足, 无法执行[关于我们]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('225', '18', '2012-06-10 00:31:08', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('226', '18', '2012-06-10 00:31:47', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('227', '18', '2012-06-10 00:32:01', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('228', '18', '2012-06-10 00:32:06', '关于我们', 'false', '用户权限不足, 无法执行[关于我们]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('229', '18', '2012-06-10 00:32:36', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('230', '18', '2012-06-10 00:32:40', '关于我们', 'false', '用户权限不足, 无法执行[关于我们]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('231', '18', '2012-06-10 00:32:44', '查看我的资料', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('232', '18', '2012-06-10 00:32:46', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('233', '18', '2012-06-10 00:32:48', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('234', '18', '2012-06-10 00:32:50', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('235', '18', '2012-06-10 00:32:56', '查询导航菜单', 'false', '用户权限不足, 无法执行[查询导航菜单]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('236', '18', '2012-06-10 00:33:03', '查看导航菜单列表', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('237', '18', '2012-06-10 00:33:05', '查看树形菜单列表', 'false', '用户权限不足, 无法执行[查看树形菜单列表]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('238', '18', '2012-06-10 00:42:14', '访问后台主页', 'success', '', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('239', '18', '2012-06-10 00:42:24', '退出后台', 'false', '用户权限不足, 无法执行[退出后台]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('240', '18', '2012-06-10 00:42:32', '访问登陆页', 'false', '用户权限不足, 无法执行[访问登陆页]功能', 'weiwei2', 'code');
INSERT INTO `t_user_activity_log` VALUES ('241', '17', '2012-06-10 01:28:51', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('242', '17', '2012-06-10 01:28:53', '切换左边树形菜单', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('243', '17', '2012-06-10 01:28:56', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('244', '17', '2012-06-10 01:28:58', '切换左边显示JSP页面', 'false', '用户权限不足, 无法执行[切换左边显示JSP页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('245', '17', '2012-06-10 01:28:59', '切换左边显示JSP页面', 'false', '用户权限不足, 无法执行[切换左边显示JSP页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('246', '17', '2012-06-10 01:29:00', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('247', '17', '2012-06-10 01:29:01', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('248', '17', '2012-06-10 01:29:02', '切换左边树形菜单', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('249', '17', '2012-06-10 01:29:06', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('250', '17', '2012-06-10 01:29:08', '查看导航菜单列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('251', '17', '2012-06-10 01:29:09', '访问添加导航菜单页面', 'false', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('252', '17', '2012-06-10 01:29:13', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('253', '17', '2012-06-10 01:29:16', '编辑某导航菜单', 'false', '用户权限不足, 无法执行[编辑某导航菜单]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('254', '17', '2012-06-10 01:29:20', '删除某导航菜单', 'false', '用户权限不足, 无法执行[删除某导航菜单]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('255', '17', '2012-06-10 01:29:24', '批量删除导航菜单', 'false', '用户权限不足, 无法执行[批量删除导航菜单]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('256', '17', '2012-06-10 01:29:27', '查询导航菜单', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('257', '17', '2012-06-10 01:29:30', '查询导航菜单', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('258', '17', '2012-06-10 01:29:33', '查看树形菜单列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('259', '17', '2012-06-10 01:29:38', '编辑某树形菜单', 'false', '用户权限不足, 无法执行[编辑某树形菜单]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('260', '17', '2012-06-10 01:29:44', '查看用户日志', 'false', '用户权限不足, 无法执行[查看用户日志]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('261', '17', '2012-06-10 01:30:05', '查看资源列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('262', '17', '2012-06-10 01:30:12', '查看资源列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('263', '17', '2012-06-10 01:30:19', '查看用户列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('264', '17', '2012-06-10 01:31:28', '查看权限列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('265', '17', '2012-06-10 01:31:34', '查看权限列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('266', '17', '2012-06-10 01:31:43', '查看权限列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('267', '17', '2012-06-10 01:31:54', '查看权限列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('268', '17', '2012-06-10 01:31:56', '查看权限列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('269', '17', '2012-06-10 01:32:06', '编辑某权限', 'false', '用户权限不足, 无法执行[编辑某权限]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('270', '17', '2012-06-10 01:32:10', '退出后台', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('271', '9', '2012-06-10 01:32:21', '访问后台主页', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('272', '9', '2012-06-10 01:32:26', '查看权限列表', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('273', '9', '2012-06-10 01:32:36', '查看权限列表', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('274', '9', '2012-06-10 01:32:41', '编辑某权限', 'false', '用户权限不足, 无法执行[编辑某权限]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('275', '9', '2012-06-10 01:32:49', '查看我的资料', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('276', '9', '2012-06-10 01:32:51', '更新某用户', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('277', '9', '2012-06-10 01:32:54', '编辑某权限', 'false', '用户权限不足, 无法执行[编辑某权限]功能', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('278', '9', '2012-06-10 01:32:57', '退出后台', 'success', '', '演示账号', 'solidbase');
INSERT INTO `t_user_activity_log` VALUES ('279', '17', '2012-06-10 01:35:35', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('280', '17', '2012-06-10 01:35:39', '访问添加用户页', 'false', '用户权限不足, 无法执行[访问添加用户页]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('281', '17', '2012-06-10 01:35:43', '分配部门', 'false', '用户权限不足, 无法执行[分配部门]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('282', '17', '2012-06-10 11:09:34', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('283', '17', '2012-06-10 11:09:39', '查看导航菜单列表', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('284', '17', '2012-06-10 11:09:41', '访问添加导航菜单页面', 'false', '用户权限不足, 无法执行[访问添加导航菜单页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('285', '17', '2012-06-10 11:09:47', '查看用户日志', 'false', '用户权限不足, 无法执行[查看用户日志]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('286', '17', '2012-06-10 11:09:49', '查看我的资料', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('287', '17', '2012-06-10 11:10:06', '更新某用户', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('288', '17', '2012-06-10 11:10:09', '查看我的资料', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('289', '17', '2012-06-10 11:10:20', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('290', '17', '2012-06-10 11:10:22', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('291', '17', '2012-06-10 11:10:23', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('292', '17', '2012-06-10 11:10:23', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('293', '17', '2012-06-10 11:10:24', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('294', '17', '2012-06-10 11:10:25', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('295', '17', '2012-06-10 11:10:25', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('296', '17', '2012-06-10 11:10:30', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('297', '17', '2012-06-10 11:10:33', '切换左边显示html页面', 'false', '用户权限不足, 无法执行[切换左边显示html页面]功能', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('298', '17', '2012-06-10 11:10:35', '访问后台主页', 'success', '', '匿名', 'weiwei');
INSERT INTO `t_user_activity_log` VALUES ('299', '17', '2012-06-10 11:10:48', '查看系统参数列表', 'success', '', '匿名', 'weiwei');

-- ----------------------------
-- Table structure for `t_user_department`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_department`;
CREATE TABLE `t_user_department` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `DEPARTMENT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  CONSTRAINT `t_user_department_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `t_user` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_user_department_ibfk_2` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `t_department` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_department
-- ----------------------------
INSERT INTO `t_user_department` VALUES ('11', '17', '15');
INSERT INTO `t_user_department` VALUES ('12', '17', '16');
INSERT INTO `t_user_department` VALUES ('13', '9', '15');
INSERT INTO `t_user_department` VALUES ('14', '9', '16');
INSERT INTO `t_user_department` VALUES ('22', '20', '15');
INSERT INTO `t_user_department` VALUES ('23', '20', '16');
INSERT INTO `t_user_department` VALUES ('24', '21', '17');
INSERT INTO `t_user_department` VALUES ('25', '20', '17');
INSERT INTO `t_user_department` VALUES ('26', '17', '17');
INSERT INTO `t_user_department` VALUES ('27', '9', '17');

-- ----------------------------
-- Table structure for `t_user_ext`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_ext`;
CREATE TABLE `t_user_ext` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `BADGE_NUM` varchar(20) DEFAULT NULL,
  `LOGIN_TYPE` enum('normal','pki') NOT NULL DEFAULT 'normal',
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `t_user_ext_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `t_user` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_ext
-- ----------------------------
INSERT INTO `t_user_ext` VALUES ('2', '3', '123123123', 'normal');
INSERT INTO `t_user_ext` VALUES ('7', '9', 'asfasf', 'normal');

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `t_user_role_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `t_user` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `t_user_role_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_role` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('4', '9', '5');
INSERT INTO `t_user_role` VALUES ('5', '18', '6');
INSERT INTO `t_user_role` VALUES ('6', '20', '7');
INSERT INTO `t_user_role` VALUES ('11', '21', '8');
INSERT INTO `t_user_role` VALUES ('12', '21', '7');
INSERT INTO `t_user_role` VALUES ('16', '20', '5');
INSERT INTO `t_user_role` VALUES ('17', '19', '5');
INSERT INTO `t_user_role` VALUES ('20', '17', '5');
INSERT INTO `t_user_role` VALUES ('21', '19', '8');
INSERT INTO `t_user_role` VALUES ('22', '21', '6');
INSERT INTO `t_user_role` VALUES ('23', '21', '5');
