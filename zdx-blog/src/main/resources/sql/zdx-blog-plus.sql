/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : zdx-blog-plus

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 25/07/2023 09:47:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tk_config
-- ----------------------------
DROP TABLE IF EXISTS `tk_config`;
CREATE TABLE `tk_config` (
  `id` bigint NOT NULL COMMENT '配置id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '值',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `tk_config_pk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for tk_dict
-- ----------------------------
DROP TABLE IF EXISTS `tk_dict`;
CREATE TABLE `tk_dict` (
  `id` bigint NOT NULL COMMENT '数据字典id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据字段名称',
  `key_` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据字典key',
  `type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据字典类型',
  `properties` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '字典内容',
  `invoke` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展执行类',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tk_dict_pk` (`key_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for tk_file
-- ----------------------------
DROP TABLE IF EXISTS `tk_file`;
CREATE TABLE `tk_file` (
  `id` bigint NOT NULL COMMENT '文件id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'md5',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件名',
  `size` bigint DEFAULT NULL COMMENT '文件大小',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'mino文件夹名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `tk_file_pk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for tk_menu
-- ----------------------------
DROP TABLE IF EXISTS `tk_menu`;
CREATE TABLE `tk_menu` (
  `id` bigint NOT NULL COMMENT '菜单id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单名',
  `order_` int NOT NULL AUTO_INCREMENT COMMENT '排序',
  `parent_id` bigint DEFAULT NULL COMMENT '父级菜单',
  `action` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '绑定动作',
  `icon` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  `type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单类型',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用',
  `params` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限标志',
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_tk_menu_order` (`order_`) USING BTREE COMMENT '排序索引'
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for tk_schedule
-- ----------------------------
DROP TABLE IF EXISTS `tk_schedule`;
CREATE TABLE `tk_schedule` (
  `id` bigint NOT NULL COMMENT '任务id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务名',
  `group_` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分组',
  `invoke` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行类',
  `cron` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行字符串',
  `misfire` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行策略',
  `status` bit(1) DEFAULT NULL COMMENT '状态',
  `concurrent` bit(1) DEFAULT NULL COMMENT '是否并发执行',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for tk_schedule_log
-- ----------------------------
DROP TABLE IF EXISTS `tk_schedule_log`;
CREATE TABLE `tk_schedule_log` (
  `id` bigint NOT NULL COMMENT '执行日志id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务名称',
  `status` bit(1) DEFAULT NULL COMMENT '状态',
  `start_time` datetime DEFAULT NULL COMMENT '执行开始时间',
  `old_time` datetime DEFAULT NULL COMMENT '执行结束时间',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '异常信息',
  `job_id` bigint DEFAULT NULL COMMENT '任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_account
-- ----------------------------
DROP TABLE IF EXISTS `us_account`;
CREATE TABLE `us_account` (
  `id` bigint NOT NULL COMMENT '账号id',
  `username` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用',
  `is_locked` bit(1) DEFAULT b'0' COMMENT '是否锁定',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `us_account_pk` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_acl
-- ----------------------------
DROP TABLE IF EXISTS `us_acl`;
CREATE TABLE `us_acl` (
  `id` bigint NOT NULL COMMENT '权限id',
  `subject` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '主体',
  `resource` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源',
  `params` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限编码',
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_auth
-- ----------------------------
DROP TABLE IF EXISTS `us_auth`;
CREATE TABLE `us_auth` (
  `id` bigint DEFAULT NULL,
  `username` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `source` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `client_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `secret` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `callback` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '回调地址',
  `type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型',
  `icon` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_log
-- ----------------------------
DROP TABLE IF EXISTS `us_log`;
CREATE TABLE `us_log` (
  `id` bigint NOT NULL COMMENT '日志id',
  `event` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '日志事件',
  `username` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `url` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求路径',
  `content` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '日志内容',
  `ip` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip',
  `source` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源',
  `os` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统名',
  `browser` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `status` bit(1) DEFAULT NULL COMMENT '是否成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_role
-- ----------------------------
DROP TABLE IF EXISTS `us_role`;
CREATE TABLE `us_role` (
  `id` bigint NOT NULL COMMENT '角色id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名',
  `display` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色',
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `us_role_pk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_role_user
-- ----------------------------
DROP TABLE IF EXISTS `us_role_user`;
CREATE TABLE `us_role_user` (
  `role_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for us_user
-- ----------------------------
DROP TABLE IF EXISTS `us_user`;
CREATE TABLE `us_user` (
  `id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `nickname` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `email` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `gender` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
  `person_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'person_id',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用',
  `is_locked` bit(1) DEFAULT b'0' COMMENT '是否锁定',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `description` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `us_user_pk` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_album
-- ----------------------------
DROP TABLE IF EXISTS `zdx_album`;
CREATE TABLE `zdx_album` (
  `id` bigint NOT NULL COMMENT '相册主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '相册名',
  `cover` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '相册封面',
  `description` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `status` bit(1) DEFAULT b'1' COMMENT '是否公开',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_article
-- ----------------------------
DROP TABLE IF EXISTS `zdx_article`;
CREATE TABLE `zdx_article` (
  `id` bigint NOT NULL COMMENT '博客id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `category_id` bigint DEFAULT NULL COMMENT '分类id',
  `cover` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章封面',
  `title` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章标题',
  `description` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章简介',
  `type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章类型',
  `is_top` bit(1) DEFAULT b'0' COMMENT '是否置顶',
  `like_count` bigint DEFAULT NULL COMMENT '点赞量',
  `view_count` bigint DEFAULT NULL COMMENT '浏览量',
  `trash` bit(1) DEFAULT b'0' COMMENT '是否回收',
  `is_recommend` bit(1) DEFAULT b'0' COMMENT '是否推荐',
  `status` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_article_content
-- ----------------------------
DROP TABLE IF EXISTS `zdx_article_content`;
CREATE TABLE `zdx_article_content` (
  `id` bigint DEFAULT NULL COMMENT '内容id',
  `content` longtext COLLATE utf8mb4_bin COMMENT '文章内容',
  `article_id` bigint DEFAULT NULL COMMENT '博客id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `zdx_article_tag`;
CREATE TABLE `zdx_article_tag` (
  `article_id` bigint DEFAULT NULL,
  `tag_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_category
-- ----------------------------
DROP TABLE IF EXISTS `zdx_category`;
CREATE TABLE `zdx_category` (
  `id` bigint NOT NULL COMMENT '分类id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `zdx_chat_record`;
CREATE TABLE `zdx_chat_record` (
  `id` bigint NOT NULL COMMENT '聊天id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户头像',
  `content` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天内容',
  `ip` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip',
  `source` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_comment
-- ----------------------------
DROP TABLE IF EXISTS `zdx_comment`;
CREATE TABLE `zdx_comment` (
  `id` bigint NOT NULL COMMENT '评论id',
  `comment_type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '评论类型',
  `type_id` bigint DEFAULT NULL COMMENT '类型',
  `parent_id` bigint DEFAULT NULL COMMENT '父评论id',
  `reply_id` bigint DEFAULT NULL COMMENT '回复评论id',
  `content` text COLLATE utf8mb4_bin COMMENT '评论内容',
  `from_uid` bigint DEFAULT NULL COMMENT '评论用户id',
  `to_uid` bigint DEFAULT NULL COMMENT '回复用户id',
  `is_check` bit(1) DEFAULT b'1' COMMENT '是否通过审核',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `like_count` int DEFAULT NULL COMMENT '评论点赞量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_friend
-- ----------------------------
DROP TABLE IF EXISTS `zdx_friend`;
CREATE TABLE `zdx_friend` (
  `id` bigint NOT NULL COMMENT '友链id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `color` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '颜色',
  `avatar` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `url` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接',
  `introduction` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '友链介绍',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_like
-- ----------------------------
DROP TABLE IF EXISTS `zdx_like`;
CREATE TABLE `zdx_like` (
  `id` bigint NOT NULL COMMENT '点赞id',
  `type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '点赞类型',
  `type_id` bigint DEFAULT NULL COMMENT '类型id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_message
-- ----------------------------
DROP TABLE IF EXISTS `zdx_message`;
CREATE TABLE `zdx_message` (
  `id` bigint DEFAULT NULL COMMENT '留言id',
  `nickname` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `content` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `ip` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip',
  `source` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源',
  `is_check` bit(1) DEFAULT b'1' COMMENT '是否审核通过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_photo
-- ----------------------------
DROP TABLE IF EXISTS `zdx_photo`;
CREATE TABLE `zdx_photo` (
  `id` bigint DEFAULT NULL COMMENT '照片id',
  `album_id` bigint DEFAULT NULL COMMENT '相册id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `url` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '照片链接',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_tag
-- ----------------------------
DROP TABLE IF EXISTS `zdx_tag`;
CREATE TABLE `zdx_tag` (
  `id` bigint NOT NULL COMMENT '标签id',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for zdx_talk
-- ----------------------------
DROP TABLE IF EXISTS `zdx_talk`;
CREATE TABLE `zdx_talk` (
  `id` bigint NOT NULL COMMENT '说说id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `content` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `images` varchar(2500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片',
  `like_count` bigint DEFAULT NULL COMMENT '说说点赞',
  `is_top` bit(1) DEFAULT b'0' COMMENT '是否置顶',
  `status` bit(1) DEFAULT b'1' COMMENT '是否公开',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
