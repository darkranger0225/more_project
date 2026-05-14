/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : wc_friend

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 22/12/2025 15:56:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (4, '付浩源', '13613871547', '$2a$10$lERCE9Atr9yNiV98f78yR.WsMezhUjlI821SJJm/7ocG6a/AjQzcy');
INSERT INTO `admins` VALUES (5, '付浩源02', '13133311111', '$2a$10$UC/Db8DZHg0T..YSvOLzYu8tamkTNeChgoSmwJqWfJGiN2WFLfQGO');

-- ----------------------------
-- Table structure for conversations
-- ----------------------------
DROP TABLE IF EXISTS `conversations`;
CREATE TABLE `conversations`  (
  `conversation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  `last_message_time` datetime(6) NULL DEFAULT NULL,
  `last_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user1_unread_count` int(11) NOT NULL DEFAULT 0,
  `user2_unread_count` int(11) NOT NULL DEFAULT 0,
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`conversation_id`) USING BTREE,
  INDEX `idx_user1_conversation`(`user1_id` ASC) USING BTREE,
  INDEX `idx_user2_conversation`(`user2_id` ASC) USING BTREE,
  CONSTRAINT `fk_conversation_user1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_conversation_user2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of conversations
-- ----------------------------
INSERT INTO `conversations` VALUES (2, 47, 42, '2025-10-23 09:07:22.260000', NULL, 0, 0, b'1');
INSERT INTO `conversations` VALUES (3, 47, 37, '2025-10-23 09:48:55.674000', '你好', 0, 0, b'1');
INSERT INTO `conversations` VALUES (4, 37, 38, '2025-10-31 09:16:02.470000', '你好张伟', 0, 1, b'1');
INSERT INTO `conversations` VALUES (5, 25, 37, '2025-12-16 05:31:19.817000', '你好', 0, 3, b'1');
INSERT INTO `conversations` VALUES (6, 25, 45, '2025-12-16 08:33:36.231000', '你好', 2, 0, b'1');

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `feedback_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_response` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `feedback_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `feedback_time` datetime(6) NOT NULL,
  `feedback_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `response_time` datetime(6) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `admin_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`feedback_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE,
  CONSTRAINT `fk_feedback_admin` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (11, '我们看到了你的回复', '这是一个建议', '2025-10-23 11:13:04.569082', '功能建议', '2025-12-16 07:38:37.286012', 'RESOLVED', 39, 4);
INSERT INTO `feedback` VALUES (12, NULL, '这是一个测试', '2025-12-16 08:05:19.257292', '使用问题', NULL, 'PENDING', 45, NULL);
INSERT INTO `feedback` VALUES (13, NULL, '希望界面更加美观', '2025-12-16 08:55:04.436352', '界面优化', NULL, 'PENDING', 49, NULL);

-- ----------------------------
-- Table structure for fuzzy_match_records
-- ----------------------------
DROP TABLE IF EXISTS `fuzzy_match_records`;
CREATE TABLE `fuzzy_match_records`  (
  `fuzzy_match_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  `interestsMatch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `matchScore` double NULL DEFAULT NULL,
  `matchTime` datetime(6) NULL DEFAULT NULL,
  `otherPreferencesMatch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `personalityMatch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fuzzy_match_id`) USING BTREE,
  INDEX `idx_user1_id`(`user1_id` ASC) USING BTREE,
  INDEX `idx_user2_id`(`user2_id` ASC) USING BTREE,
  CONSTRAINT `fk_fuzzy_match_user1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_fuzzy_match_user2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 523 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fuzzy_match_records
-- ----------------------------
INSERT INTO `fuzzy_match_records` VALUES (250, 46, 37, '运动,旅行,音乐,阅读', 0.55, '2025-10-22 13:49:23.373949', '喜欢宠物,幽默感,热爱旅行', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (251, 46, 38, '运动,音乐,旅行,游戏', 1, '2025-10-22 13:49:23.388772', '幽默感,喜欢宠物,喜欢美食', '活泼,乐观');
INSERT INTO `fuzzy_match_records` VALUES (252, 46, 39, '音乐,旅行,阅读,摄影', 0.4, '2025-10-22 13:49:23.403886', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (253, 46, 40, '游戏,科技,阅读,电影', 1, '2025-10-22 13:49:23.417879', '幽默感,喜欢宠物,喜欢美食', '活泼,乐观');
INSERT INTO `fuzzy_match_records` VALUES (254, 46, 41, '阅读,音乐,艺术,旅行', 0.4, '2025-10-22 13:49:23.432885', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (255, 46, 42, '美食,摄影,旅行,运动', 1, '2025-10-22 13:49:23.448124', '幽默感,喜欢宠物,喜欢美食', '活泼,乐观');
INSERT INTO `fuzzy_match_records` VALUES (256, 46, 43, '运动,音乐,旅行,美食', 0.4, '2025-10-22 13:49:23.464127', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (257, 46, 44, '游戏,电影,科技,音乐', 1, '2025-10-22 13:49:23.479443', '幽默感,喜欢宠物,喜欢美食', '活泼,乐观');
INSERT INTO `fuzzy_match_records` VALUES (258, 46, 45, '旅行,艺术,摄影,音乐', 0.4, '2025-10-22 13:49:23.495448', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (259, 46, 47, '美食,音乐,阅读,旅行', 0.4, '2025-10-22 13:49:23.511448', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (260, 46, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 14:27:53.070616', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (261, 46, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 14:27:53.111756', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (262, 46, 39, '音乐,旅行,阅读,摄影', 0.4, '2025-10-22 14:27:53.126824', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (263, 46, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 14:27:53.142026', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (264, 46, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 14:27:53.158846', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (265, 46, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 14:27:53.174132', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (266, 46, 43, '运动,音乐,旅行,美食', 0.4, '2025-10-22 14:27:53.189143', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (267, 46, 44, '游戏,电影,科技,音乐', 0.7, '2025-10-22 14:27:53.203893', '无匹配', '理性');
INSERT INTO `fuzzy_match_records` VALUES (268, 46, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 14:27:53.219644', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (269, 46, 47, '美食,音乐,阅读,旅行', 0.4, '2025-10-22 14:27:53.234613', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (270, 47, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 14:52:04.315839', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (271, 47, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 14:52:04.337796', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (272, 47, 39, '音乐,旅行,阅读,摄影', 0.55, '2025-10-22 14:52:04.354750', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (273, 47, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 14:52:04.369711', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (274, 47, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 14:52:04.386663', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (275, 47, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 14:52:04.402621', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (276, 47, 43, '运动,音乐,旅行,美食', 0.5, '2025-10-22 14:52:04.416584', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (277, 47, 44, '游戏,电影,科技,音乐', 0.4, '2025-10-22 14:52:04.432541', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (278, 47, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 14:52:04.447501', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (279, 47, 46, '运动,游戏,音乐,美食', 0.4, '2025-10-22 14:52:04.462500', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (280, 47, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 15:20:09.848010', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (281, 47, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 15:20:09.919796', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (282, 47, 39, '音乐,旅行,阅读,摄影', 0.55, '2025-10-22 15:20:09.933758', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (283, 47, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 15:20:09.950345', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (284, 47, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 15:20:09.964323', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (285, 47, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 15:20:09.981294', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (286, 47, 43, '运动,音乐,旅行,美食', 0.5, '2025-10-22 15:20:09.995273', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (287, 47, 44, '游戏,电影,科技,音乐', 0.4, '2025-10-22 15:20:10.010979', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (288, 47, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 15:20:10.026998', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (289, 47, 46, '运动,游戏,音乐,美食', 0.4, '2025-10-22 15:20:10.041298', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (290, 47, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 15:20:43.912932', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (291, 47, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 15:20:43.941514', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (292, 47, 39, '音乐,旅行,阅读,摄影', 0.55, '2025-10-22 15:20:43.957698', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (293, 47, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 15:20:43.972626', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (294, 47, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 15:20:43.988860', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (295, 47, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 15:20:44.005074', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (296, 47, 43, '运动,音乐,旅行,美食', 0.5, '2025-10-22 15:20:44.019671', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (297, 47, 44, '游戏,电影,科技,音乐', 0.4, '2025-10-22 15:20:44.036696', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (298, 47, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 15:20:44.054385', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (299, 47, 46, '运动,游戏,音乐,美食', 0.4, '2025-10-22 15:20:44.067340', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (300, 47, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 15:21:10.103738', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (301, 47, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 15:21:10.159406', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (302, 47, 39, '音乐,旅行,阅读,摄影', 0.55, '2025-10-22 15:21:10.175432', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (303, 47, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 15:21:10.191411', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (304, 47, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 15:21:10.209342', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (305, 47, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 15:21:10.223306', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (306, 47, 43, '运动,音乐,旅行,美食', 0.5, '2025-10-22 15:21:10.238265', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (307, 47, 44, '游戏,电影,科技,音乐', 0.4, '2025-10-22 15:21:10.253225', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (308, 47, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 15:21:10.269183', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (309, 47, 46, '运动,游戏,音乐,美食', 0.4, '2025-10-22 15:21:10.284141', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (310, 47, 37, '运动,旅行,音乐,阅读', 0.4, '2025-10-22 15:22:06.392861', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (311, 47, 38, '运动,音乐,旅行,游戏', 0.4, '2025-10-22 15:22:06.421783', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (312, 47, 39, '音乐,旅行,阅读,摄影', 0.55, '2025-10-22 15:22:06.438737', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (313, 47, 40, '游戏,科技,阅读,电影', 0.4, '2025-10-22 15:22:06.452701', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (314, 47, 41, '阅读,音乐,艺术,旅行', 0.5, '2025-10-22 15:22:06.469655', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (315, 47, 42, '美食,摄影,旅行,运动', 0.4, '2025-10-22 15:22:06.485612', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (316, 47, 43, '运动,音乐,旅行,美食', 0.5, '2025-10-22 15:22:06.501569', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (317, 47, 44, '游戏,电影,科技,音乐', 0.4, '2025-10-22 15:22:06.517532', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (318, 47, 45, '旅行,艺术,摄影,音乐', 0.5, '2025-10-22 15:22:06.533485', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (319, 47, 46, '运动,游戏,音乐,美食', 0.4, '2025-10-22 15:22:06.549492', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (320, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:17:59.994341', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (321, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:18:00.015358', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (322, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:18:00.031316', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (323, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:18:00.048270', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (324, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:18:00.062233', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (325, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:18:00.078189', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (326, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:18:00.092610', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (327, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:18:00.112558', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (328, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:18:00.128514', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (329, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:18:00.145267', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (330, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:19:38.747674', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (331, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:19:38.771599', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (332, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:19:38.787857', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (333, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:19:38.803576', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (334, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:19:38.820403', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (335, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:19:38.836189', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (336, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:19:38.851327', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (337, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:19:38.867285', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (338, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:19:38.882794', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (339, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:19:38.897802', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (340, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:38:32.467640', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (341, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:38:32.488102', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (342, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:38:32.503103', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (343, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:38:32.518063', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (344, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:38:32.533022', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (345, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:38:32.549978', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (346, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:38:32.563940', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (347, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:38:32.578900', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (348, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:38:32.593860', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (349, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:38:32.609865', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (350, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:40:19.332359', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (351, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:40:19.370168', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (352, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:40:19.385130', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (353, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:40:19.400133', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (354, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:40:19.415068', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (355, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:40:19.431254', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (356, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:40:19.447297', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (357, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:40:19.462463', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (358, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:40:19.477553', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (359, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:40:19.494401', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (360, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:47:24.599974', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (361, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:47:24.679235', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (362, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:47:24.696189', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (363, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:47:24.710178', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (364, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:47:24.727107', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (365, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:47:24.743066', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (366, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:47:24.759022', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (367, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:47:24.773983', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (368, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:47:24.791099', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (369, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:47:24.807056', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (370, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:47:53.151728', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (371, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:47:53.187539', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (372, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:47:53.203530', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (373, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:47:53.217538', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (374, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:47:53.233519', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (375, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:47:53.247527', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (376, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:47:53.263871', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (377, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:47:53.278831', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (378, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:47:53.294788', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (379, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:47:53.311749', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (380, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:48:14.929701', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (381, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:48:15.040843', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (382, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:48:15.055803', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (383, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:48:15.071871', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (384, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:48:15.086700', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (385, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:48:15.103567', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (386, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:48:15.120036', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (387, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:48:15.134598', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (388, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:48:15.151515', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (389, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:48:15.166161', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (390, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:48:34.920104', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (391, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:48:35.013995', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (392, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:48:35.030155', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (393, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:48:35.044159', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (394, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:48:35.061226', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (395, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:48:35.076903', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (396, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:48:35.092404', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (397, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:48:35.107111', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (398, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:48:35.122597', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (399, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:48:35.139000', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (400, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:49:21.033721', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (401, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:49:21.090568', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (402, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:49:21.106823', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (403, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:49:21.121781', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (404, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:49:21.137739', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (405, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:49:21.152700', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (406, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:49:21.168656', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (407, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:49:21.184613', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (408, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:49:21.200571', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (409, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:49:21.215532', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (410, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 08:58:27.788012', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (411, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 08:58:27.819642', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (412, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 08:58:27.833284', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (413, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 08:58:27.849656', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (414, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 08:58:27.864716', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (415, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 08:58:27.880098', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (416, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 08:58:27.896395', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (417, 47, 44, '音乐【一般】', 0.25, '2025-10-23 08:58:27.913149', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (418, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 08:58:27.927630', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (419, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 08:58:27.943588', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (420, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 09:03:02.153718', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (421, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 09:03:02.254668', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (422, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 09:03:02.268613', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (423, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 09:03:02.284622', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (424, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 09:03:02.301204', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (425, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 09:03:02.316233', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (426, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 09:03:02.332167', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (427, 47, 44, '音乐【一般】', 0.25, '2025-10-23 09:03:02.348456', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (428, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 09:03:02.363474', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (429, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 09:03:02.378642', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (430, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 09:07:10.414477', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (431, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 09:07:10.469051', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (432, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 09:07:10.484995', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (433, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 09:07:10.500087', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (434, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 09:07:10.516430', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (435, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 09:07:10.532386', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (436, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 09:07:10.547382', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (437, 47, 44, '音乐【一般】', 0.25, '2025-10-23 09:07:10.563340', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (438, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 09:07:10.578299', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (439, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 09:07:10.593259', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (440, 47, 37, '音乐【一般】,阅读【一般】,旅行【喜欢】', 0.3055555555555556, '2025-10-23 09:15:38.469103', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (441, 47, 38, '音乐【喜欢】,旅行【喜欢】', 0.3333333333333333, '2025-10-23 09:15:38.558137', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (442, 47, 39, '音乐【喜欢】,阅读【喜欢】,旅行【喜欢】', 0.5138888888888888, '2025-10-23 09:15:38.572602', '顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (443, 47, 40, '阅读【喜欢】', 0.3333333333333333, '2025-10-23 09:15:38.586434', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (444, 47, 41, '音乐【喜欢】,阅读【喜欢】,旅行【一般】', 0.41666666666666663, '2025-10-23 09:15:38.602392', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (445, 47, 42, '美食【热爱】,旅行【喜欢】', 0.41666666666666663, '2025-10-23 09:15:38.618222', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (446, 47, 43, '美食【喜欢】,音乐【喜欢】,旅行【喜欢】', 0.4722222222222222, '2025-10-23 09:15:38.633732', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (447, 47, 44, '音乐【一般】', 0.25, '2025-10-23 09:15:38.648153', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (448, 47, 45, '音乐【喜欢】,旅行【喜欢】', 0.4583333333333333, '2025-10-23 09:15:38.665132', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (449, 47, 46, '美食【喜欢】,音乐【喜欢】', 0.375, '2025-10-23 09:15:38.678473', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (450, 37, 38, '运动【喜欢】,旅行【喜欢】,音乐【一般】', 0.5833333333333333, '2025-10-31 09:15:44.750778', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (451, 37, 39, '旅行【热爱】,音乐【喜欢】,阅读【一般】', 0.3611111111111111, '2025-10-31 09:15:44.806024', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (452, 37, 40, '阅读【一般】', 0.375, '2025-10-31 09:15:44.818987', '有幽默感', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (453, 37, 41, '旅行【喜欢】,音乐【一般】,阅读【喜欢】', 0.3055555555555555, '2025-10-31 09:15:44.833948', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (454, 37, 42, '运动【一般】,旅行【喜欢】', 0.5416666666666667, '2025-10-31 09:15:44.849905', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (455, 37, 43, '运动【一般】,旅行【热爱】,音乐【一般】', 0.41666666666666663, '2025-10-31 09:15:44.864865', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (456, 37, 44, '音乐【一般】', 0.29166666666666663, '2025-10-31 09:15:44.881820', '有幽默感', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (457, 37, 45, '旅行【热爱】,音乐【一般】', 0.375, '2025-10-31 09:15:44.895782', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (458, 37, 46, '运动【喜欢】,音乐【一般】', 0.29166666666666663, '2025-10-31 09:15:44.910975', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (459, 37, 47, '旅行【喜欢】,音乐【一般】,阅读【一般】', 0.3055555555555556, '2025-10-31 09:15:44.926401', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (460, 25, 37, '运动【一般】,音乐【一般】,旅行【喜欢】', 0.5625, '2025-12-16 04:49:10.905538', '喜欢宠物,有幽默感', '外向');
INSERT INTO `fuzzy_match_records` VALUES (461, 25, 38, '运动【喜欢】,音乐【喜欢】,旅行【一般】', 0.3680555555555555, '2025-12-16 04:49:10.945790', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (462, 25, 39, '音乐【喜欢】,旅行【喜欢】', 0.375, '2025-12-16 04:49:10.957660', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (463, 25, 40, '无共同兴趣', 0.08333333333333333, '2025-12-16 04:49:10.973659', '有幽默感', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (464, 25, 41, '音乐【喜欢】,旅行【一般】', 0.3125, '2025-12-16 04:49:10.989204', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (465, 25, 42, '运动【一般】,旅行【一般】', 0.2708333333333333, '2025-12-16 04:49:11.004204', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (466, 25, 43, '运动【一般】,音乐【喜欢】,旅行【喜欢】', 0.3055555555555555, '2025-12-16 04:49:11.019180', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (467, 25, 44, '音乐【一般】', 0.3333333333333333, '2025-12-16 04:49:11.034217', '有幽默感', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (468, 25, 45, '音乐【喜欢】,旅行【喜欢】', 0.3958333333333333, '2025-12-16 04:49:11.051197', '有责任心,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (469, 25, 46, '运动【喜欢】,音乐【喜欢】', 0.3958333333333333, '2025-12-16 04:49:11.065223', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (470, 25, 47, '音乐【喜欢】,旅行【一般】', 0.29166666666666663, '2025-12-16 04:49:11.080228', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (471, 45, 25, '旅行【喜欢】,音乐【喜欢】', 0.3958333333333333, '2025-12-16 08:02:22.654106', '有幽默感,有上进心,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (472, 45, 37, '旅行【热爱】,音乐【一般】', 0.375, '2025-12-16 08:02:22.805699', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (473, 45, 38, '旅行【喜欢】,音乐【喜欢】', 0.375, '2025-12-16 08:02:22.830633', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (474, 45, 39, '旅行【热爱】,摄影【喜欢】,音乐【喜欢】', 0.7916666666666667, '2025-12-16 08:02:22.848428', '顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (475, 45, 41, '旅行【喜欢】,艺术【喜欢】,音乐【喜欢】', 0.8611111111111112, '2025-12-16 08:02:22.867377', '有责任心,顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (476, 45, 42, '旅行【喜欢】,摄影【喜欢】', 0.6666666666666667, '2025-12-16 08:02:22.883334', '无匹配', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (477, 45, 43, '旅行【热爱】,音乐【喜欢】', 0.49999999999999994, '2025-12-16 08:02:22.897298', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (478, 45, 44, '音乐【一般】', 0.25, '2025-12-16 08:02:22.912260', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (479, 45, 46, '音乐【喜欢】', 0.41666666666666663, '2025-12-16 08:02:22.927218', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (480, 45, 47, '旅行【喜欢】,音乐【喜欢】', 0.4583333333333333, '2025-12-16 08:02:22.943174', '热爱旅行,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (481, 45, 25, '旅行【喜欢】,音乐【喜欢】', 0.3958333333333333, '2025-12-16 08:12:42.799086', '有幽默感,有上进心,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (482, 45, 37, '旅行【热爱】,音乐【一般】', 0.375, '2025-12-16 08:12:42.843415', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (483, 45, 38, '旅行【喜欢】,音乐【喜欢】', 0.375, '2025-12-16 08:12:42.865415', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (484, 45, 39, '旅行【热爱】,摄影【喜欢】,音乐【喜欢】', 0.7916666666666667, '2025-12-16 08:12:42.880815', '顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (485, 45, 41, '旅行【喜欢】,艺术【喜欢】,音乐【喜欢】', 0.8611111111111112, '2025-12-16 08:12:42.898879', '有责任心,顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (486, 45, 42, '旅行【喜欢】,摄影【喜欢】', 0.6666666666666667, '2025-12-16 08:12:42.911593', '无匹配', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (487, 45, 43, '旅行【热爱】,音乐【喜欢】', 0.49999999999999994, '2025-12-16 08:12:42.926912', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (488, 45, 44, '音乐【一般】', 0.25, '2025-12-16 08:12:42.942925', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (489, 45, 46, '音乐【喜欢】', 0.41666666666666663, '2025-12-16 08:12:42.958583', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (490, 45, 47, '旅行【喜欢】,音乐【喜欢】', 0.4583333333333333, '2025-12-16 08:12:42.974540', '热爱旅行,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (491, 45, 25, '旅行【喜欢】,音乐【喜欢】', 0.3958333333333333, '2025-12-16 08:39:53.550583', '有幽默感,有上进心,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (492, 45, 37, '旅行【热爱】,音乐【一般】', 0.375, '2025-12-16 08:39:53.692045', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (493, 45, 38, '旅行【喜欢】,音乐【喜欢】', 0.375, '2025-12-16 08:39:53.700024', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (494, 45, 39, '旅行【热爱】,摄影【喜欢】,音乐【喜欢】', 0.7916666666666667, '2025-12-16 08:39:53.716980', '顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (495, 45, 41, '旅行【喜欢】,艺术【喜欢】,音乐【喜欢】', 0.8611111111111112, '2025-12-16 08:39:53.733934', '有责任心,顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (496, 45, 42, '旅行【喜欢】,摄影【喜欢】', 0.6666666666666667, '2025-12-16 08:39:53.747923', '无匹配', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (497, 45, 43, '旅行【热爱】,音乐【喜欢】', 0.49999999999999994, '2025-12-16 08:39:53.762700', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (498, 45, 44, '音乐【一般】', 0.25, '2025-12-16 08:39:53.778291', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (499, 45, 46, '音乐【喜欢】', 0.41666666666666663, '2025-12-16 08:39:53.794249', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (500, 45, 47, '旅行【喜欢】,音乐【喜欢】', 0.4583333333333333, '2025-12-16 08:39:53.810206', '热爱旅行,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (501, 45, 25, '旅行【喜欢】,音乐【喜欢】', 0.3958333333333333, '2025-12-16 08:48:34.596783', '有幽默感,有上进心,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (502, 45, 37, '旅行【热爱】,音乐【一般】', 0.375, '2025-12-16 08:48:34.714341', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (503, 45, 38, '旅行【喜欢】,音乐【喜欢】', 0.375, '2025-12-16 08:48:34.727981', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (504, 45, 39, '旅行【热爱】,摄影【喜欢】,音乐【喜欢】', 0.7916666666666667, '2025-12-16 08:48:34.744463', '顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (505, 45, 41, '旅行【喜欢】,艺术【喜欢】,音乐【喜欢】', 0.8611111111111112, '2025-12-16 08:48:34.765417', '有责任心,顾家', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (506, 45, 42, '旅行【喜欢】,摄影【喜欢】', 0.6666666666666667, '2025-12-16 08:48:34.777384', '无匹配', '稳重');
INSERT INTO `fuzzy_match_records` VALUES (507, 45, 43, '旅行【热爱】,音乐【喜欢】', 0.49999999999999994, '2025-12-16 08:48:34.789903', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (508, 45, 44, '音乐【一般】', 0.25, '2025-12-16 08:48:34.806538', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (509, 45, 46, '音乐【喜欢】', 0.41666666666666663, '2025-12-16 08:48:34.820440', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (510, 45, 47, '旅行【喜欢】,音乐【喜欢】', 0.4583333333333333, '2025-12-16 08:48:34.835171', '热爱旅行,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (511, 49, 25, '音乐【一般】', 0.5, '2025-12-16 08:54:23.332371', '无匹配', '外向');
INSERT INTO `fuzzy_match_records` VALUES (512, 49, 37, '音乐【一般】,阅读【一般】', 0.5208333333333333, '2025-12-16 08:54:23.472126', '喜欢宠物,有幽默感', '外向');
INSERT INTO `fuzzy_match_records` VALUES (513, 49, 38, '音乐【一般】', 0.3125, '2025-12-16 08:54:23.483603', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (514, 49, 39, '音乐【喜欢】,阅读【喜欢】', 0.3333333333333333, '2025-12-16 08:54:23.498638', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (515, 49, 40, '阅读【喜欢】', 0.3333333333333333, '2025-12-16 08:54:23.515655', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (516, 49, 41, '音乐【一般】,阅读【喜欢】', 0.33333333333333337, '2025-12-16 08:54:23.530041', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (517, 49, 42, '无共同兴趣', 0.0625, '2025-12-16 08:54:23.547608', '有幽默感,喜欢宠物', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (518, 49, 43, '音乐【一般】', 0.3125, '2025-12-16 08:54:23.562600', '喜欢宠物,顾家', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (519, 49, 44, '音乐【一般】', 0.16666666666666666, '2025-12-16 08:54:23.575605', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (520, 49, 45, '音乐【一般】', 0.25, '2025-12-16 08:54:23.592912', '无匹配', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (521, 49, 46, '音乐【一般】', 0.3125, '2025-12-16 08:54:23.606924', '善良,有责任心', '无匹配');
INSERT INTO `fuzzy_match_records` VALUES (522, 49, 47, '音乐【一般】,阅读【喜欢】', 0.29166666666666663, '2025-12-16 08:54:23.621812', '无匹配', '无匹配');

-- ----------------------------
-- Table structure for match_records
-- ----------------------------
DROP TABLE IF EXISTS `match_records`;
CREATE TABLE `match_records`  (
  `match_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  `matchReason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `matchScore` double NULL DEFAULT NULL,
  `matchStatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `matchTime` datetime(6) NULL DEFAULT NULL,
  `matchType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`match_id`) USING BTREE,
  INDEX `idx_match_user1`(`user1_id` ASC) USING BTREE,
  INDEX `idx_match_user2`(`user2_id` ASC) USING BTREE,
  CONSTRAINT `fk_match_user1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_match_user2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of match_records
-- ----------------------------
INSERT INTO `match_records` VALUES (160, 25, 37, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:29.987870', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (161, 39, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.095744', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (162, 42, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.104720', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (163, 45, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.112698', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (164, 40, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.158646', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (165, 43, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.167620', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (166, 46, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.173605', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (167, 38, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.204521', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (168, 41, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.209509', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (169, 42, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.212500', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (170, 43, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.215493', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (171, 44, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.218485', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (172, 47, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.224468', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (173, 45, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.358112', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (174, 46, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.360107', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (175, 47, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:30.362100', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (176, 25, 37, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.564424', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (177, 39, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.610302', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (178, 42, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.614290', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (179, 45, 38, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.618280', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (180, 40, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.641218', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (181, 43, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.645208', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (182, 46, 39, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.649197', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (183, 38, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.672139', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (184, 41, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.676127', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (185, 42, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.679117', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (186, 43, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.681112', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (187, 44, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.683107', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (188, 47, 40, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.688093', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (189, 45, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.819741', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (190, 46, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.821736', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (191, 47, 44, '单向关注', 0, 'ATTENTION', '2025-12-16 06:48:45.823730', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (192, 39, 49, '单向关注', 0, 'ATTENTION', '2025-12-16 08:53:50.839430', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (193, 42, 49, '单向关注', 0, 'ATTENTION', '2025-12-16 08:53:50.850399', 'UNIDIRECTIONAL');
INSERT INTO `match_records` VALUES (194, 49, 45, '性别符合期望，年龄符合期望，学校完全匹配', 40, 'SUCCESS', '2025-12-16 08:53:50.857382', 'BIDIRECTIONAL');

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `conversation_id` bigint(20) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `message_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'text',
  `send_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `is_read` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_conversation_message`(`conversation_id` ASC) USING BTREE,
  INDEX `idx_sender_message`(`sender_id` ASC) USING BTREE,
  INDEX `idx_receiver_message`(`receiver_id` ASC) USING BTREE,
  CONSTRAINT `fk_message_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_message_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_message_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (2, 2, 47, 42, '你好', 'text', '2025-10-23 09:07:22.249000', b'0');
INSERT INTO `messages` VALUES (3, 3, 37, 47, '你好啊', 'text', '2025-10-23 09:34:32.689000', b'1');
INSERT INTO `messages` VALUES (4, 3, 47, 37, '你好你好', 'text', '2025-10-23 09:35:58.522000', b'1');
INSERT INTO `messages` VALUES (5, 3, 47, 37, '你好', 'text', '2025-10-23 09:48:55.654000', b'1');
INSERT INTO `messages` VALUES (6, 4, 37, 38, '你好张伟', 'text', '2025-10-31 09:16:02.465000', b'0');
INSERT INTO `messages` VALUES (7, 5, 25, 37, '你好，认识一下', 'text', '2025-12-16 04:49:44.911000', b'0');
INSERT INTO `messages` VALUES (8, 5, 25, 37, '你好', 'text', '2025-12-16 05:18:05.575000', b'0');
INSERT INTO `messages` VALUES (9, 5, 25, 37, '你好', 'text', '2025-12-16 05:31:19.815000', b'0');
INSERT INTO `messages` VALUES (10, 6, 25, 45, '你好', 'text', '2025-12-16 05:39:47.398000', b'1');
INSERT INTO `messages` VALUES (11, 6, 45, 25, '你好', 'text', '2025-12-16 08:05:36.697000', b'0');
INSERT INTO `messages` VALUES (12, 6, 45, 25, '你好', 'text', '2025-12-16 08:33:36.181000', b'0');

-- ----------------------------
-- Table structure for user_actions
-- ----------------------------
DROP TABLE IF EXISTS `user_actions`;
CREATE TABLE `user_actions`  (
  `action_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action_details` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `action_result` enum('SUCCESS','FAILED','PENDING') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `action_type` enum('USER_LOGIN','USER_REGISTER','UPDATE_USER_INFO','UPDATE_PREFERENCES','EXACT_MATCH','FUZZY_MATCH','ADMIN_MATCH_ALL','SUBMIT_FEEDBACK','SEND_MESSAGE','VIEW_PROFILE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`action_id`) USING BTREE,
  INDEX `idx_action_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_action_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 427 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_actions
-- ----------------------------
INSERT INTO `user_actions` VALUES (284, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:12:03.696429', 'USER_REGISTER', 24);
INSERT INTO `user_actions` VALUES (285, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:28:40.637813', 'USER_REGISTER', 25);
INSERT INTO `user_actions` VALUES (286, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:34:01.135485', 'USER_REGISTER', 26);
INSERT INTO `user_actions` VALUES (287, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:43:00.555367', 'USER_REGISTER', 27);
INSERT INTO `user_actions` VALUES (288, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:48:48.768416', 'USER_REGISTER', 28);
INSERT INTO `user_actions` VALUES (289, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:52:37.780576', 'USER_REGISTER', 29);
INSERT INTO `user_actions` VALUES (290, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 12:57:49.417073', 'USER_REGISTER', 30);
INSERT INTO `user_actions` VALUES (291, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:03:47.344876', 'USER_REGISTER', 31);
INSERT INTO `user_actions` VALUES (292, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:10:12.600341', 'USER_REGISTER', 32);
INSERT INTO `user_actions` VALUES (293, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:16:08.674006', 'USER_REGISTER', 33);
INSERT INTO `user_actions` VALUES (294, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:18:48.656913', 'USER_REGISTER', 34);
INSERT INTO `user_actions` VALUES (295, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:20:40.075587', 'USER_REGISTER', 35);
INSERT INTO `user_actions` VALUES (296, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 13:50:31.776375', 'USER_REGISTER', 36);
INSERT INTO `user_actions` VALUES (297, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-10-21 14:26:19.922257', 'USER_REGISTER', 37);
INSERT INTO `user_actions` VALUES (298, '用户登录: 15222222222', 'SUCCESS', '2025-10-21 14:35:21.370892', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (299, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-10-21 14:35:54.193549', 'FUZZY_MATCH', 37);
INSERT INTO `user_actions` VALUES (300, '保存用户偏好信息', 'SUCCESS', '2025-10-21 15:05:47.220049', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (301, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-21 15:13:08.651544', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (302, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-21 16:03:56.209722', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (303, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 06:08:26.560531', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (304, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 06:13:05.162557', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (305, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 06:18:47.350630', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (306, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 06:22:20.548902', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (307, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 06:41:38.263129', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (308, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 06:50:24.898459', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (309, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 06:59:51.875904', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (310, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 07:01:45.171436', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (311, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 07:12:45.444396', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (312, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 07:13:45.973967', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (313, '保存失败: A different object with the same identifier value was already associated with the session : [com.gongchan.model.UserPreferences#37]', 'FAILED', '2025-10-22 07:23:57.566987', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (314, '更新用户偏好信息', 'SUCCESS', '2025-10-22 07:42:12.638483', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (315, '更新用户偏好信息', 'SUCCESS', '2025-10-22 07:43:18.332862', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (316, '更新用户偏好信息', 'SUCCESS', '2025-10-22 07:43:37.174326', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (317, '更新用户偏好信息', 'SUCCESS', '2025-10-22 08:19:26.493867', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (318, '更新用户偏好信息', 'SUCCESS', '2025-10-22 08:34:49.581351', 'UPDATE_PREFERENCES', 37);
INSERT INTO `user_actions` VALUES (319, '更新用户信息', 'SUCCESS', '2025-10-22 12:07:33.649752', 'UPDATE_USER_INFO', 37);
INSERT INTO `user_actions` VALUES (320, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 12:08:08.534018', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (321, '用户登录: 15222222222', 'SUCCESS', '2025-10-22 12:09:25.055055', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (322, '精确匹配成功', 'SUCCESS', '2025-10-22 12:42:20.253775', 'EXACT_MATCH', 37);
INSERT INTO `user_actions` VALUES (323, '模糊匹配成功，匹配数: 0', 'SUCCESS', '2025-10-22 12:42:23.867096', 'FUZZY_MATCH', 37);
INSERT INTO `user_actions` VALUES (324, '更新用户信息', 'SUCCESS', '2025-10-22 12:43:41.690415', 'UPDATE_USER_INFO', 37);
INSERT INTO `user_actions` VALUES (325, '更新用户信息', 'SUCCESS', '2025-10-22 12:43:51.560255', 'UPDATE_USER_INFO', 37);
INSERT INTO `user_actions` VALUES (326, '更新用户信息', 'SUCCESS', '2025-10-22 12:46:38.180460', 'UPDATE_USER_INFO', 37);
INSERT INTO `user_actions` VALUES (327, '更新用户信息', 'SUCCESS', '2025-10-22 12:50:33.303055', 'UPDATE_USER_INFO', 37);
INSERT INTO `user_actions` VALUES (328, '用户登录: 13800000009', 'SUCCESS', '2025-10-22 13:47:53.251761', 'USER_LOGIN', 46);
INSERT INTO `user_actions` VALUES (329, '精确匹配成功', 'SUCCESS', '2025-10-22 13:49:19.826056', 'EXACT_MATCH', 46);
INSERT INTO `user_actions` VALUES (330, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 13:49:23.525452', 'FUZZY_MATCH', 46);
INSERT INTO `user_actions` VALUES (331, '更新用户偏好信息', 'SUCCESS', '2025-10-22 13:51:19.460537', 'UPDATE_PREFERENCES', 46);
INSERT INTO `user_actions` VALUES (332, '更新用户偏好信息', 'SUCCESS', '2025-10-22 14:05:04.866509', 'UPDATE_PREFERENCES', 46);
INSERT INTO `user_actions` VALUES (333, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 14:27:53.248634', 'FUZZY_MATCH', 46);
INSERT INTO `user_actions` VALUES (340, '用户登录: 13800000010', 'SUCCESS', '2025-10-22 14:50:57.045695', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (341, '精确匹配成功', 'SUCCESS', '2025-10-22 14:52:01.259788', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (342, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 14:52:04.476462', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (343, '更新用户偏好信息', 'SUCCESS', '2025-10-22 15:13:08.709981', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (344, '精确匹配成功', 'SUCCESS', '2025-10-22 15:18:43.695010', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (345, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 15:20:10.055333', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (346, '精确匹配成功', 'SUCCESS', '2025-10-22 15:20:41.171627', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (347, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 15:20:44.081302', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (348, '精确匹配成功', 'SUCCESS', '2025-10-22 15:21:01.165932', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (349, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 15:21:10.298105', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (350, '精确匹配成功', 'SUCCESS', '2025-10-22 15:22:04.275654', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (351, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-22 15:22:06.563438', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (352, '更新用户信息', 'SUCCESS', '2025-10-22 15:23:59.310694', 'UPDATE_USER_INFO', 47);
INSERT INTO `user_actions` VALUES (353, '用户登录: 13800000010', 'SUCCESS', '2025-10-23 08:00:19.640472', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (354, '更新用户偏好信息', 'SUCCESS', '2025-10-23 08:00:58.694350', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (355, '更新用户偏好信息', 'SUCCESS', '2025-10-23 08:01:06.498503', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (356, '更新用户偏好信息', 'SUCCESS', '2025-10-23 08:01:27.850592', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (357, '更新用户偏好信息', 'SUCCESS', '2025-10-23 08:02:12.867314', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (358, '更新用户偏好信息', 'SUCCESS', '2025-10-23 08:15:47.161196', 'UPDATE_PREFERENCES', 47);
INSERT INTO `user_actions` VALUES (359, '精确匹配成功', 'SUCCESS', '2025-10-23 08:16:10.073968', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (360, '精确匹配成功', 'SUCCESS', '2025-10-23 08:17:12.929601', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (361, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:18:00.158232', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (362, '更新用户信息', 'SUCCESS', '2025-10-23 08:19:20.625737', 'UPDATE_USER_INFO', 47);
INSERT INTO `user_actions` VALUES (363, '精确匹配成功', 'SUCCESS', '2025-10-23 08:19:25.788154', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (364, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:19:38.911812', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (365, '用户登录: 13800000010', 'SUCCESS', '2025-10-23 08:38:16.323172', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (366, '精确匹配成功', 'SUCCESS', '2025-10-23 08:38:28.942315', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (367, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:38:32.624603', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (368, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:40:19.507382', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (369, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:47:24.820022', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (370, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:47:53.324708', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (371, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:48:15.179113', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (372, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:48:35.153188', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (373, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:49:21.229985', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (374, '精确匹配成功', 'SUCCESS', '2025-10-23 08:58:24.667330', 'EXACT_MATCH', 47);
INSERT INTO `user_actions` VALUES (375, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 08:58:27.956610', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (376, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 09:03:02.391644', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (377, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 09:07:10.607221', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (378, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-23 09:15:38.691470', 'FUZZY_MATCH', 47);
INSERT INTO `user_actions` VALUES (379, '用户登录: 15222222222', 'SUCCESS', '2025-10-23 09:25:50.859769', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (380, '用户登录: 13800000010', 'SUCCESS', '2025-10-23 09:35:31.672436', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (381, '用户登录: 15222222222', 'SUCCESS', '2025-10-23 09:49:27.677611', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (382, '用户登录: 13800000010', 'SUCCESS', '2025-10-23 09:57:38.992559', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (383, '用户登录: 13800000010', 'SUCCESS', '2025-10-23 11:01:37.889502', 'USER_LOGIN', 47);
INSERT INTO `user_actions` VALUES (384, '用户登录: 13800000002', 'SUCCESS', '2025-10-23 11:10:29.548423', 'USER_LOGIN', 39);
INSERT INTO `user_actions` VALUES (385, '提交反馈：功能建议', 'SUCCESS', '2025-10-23 11:13:04.631302', 'SUBMIT_FEEDBACK', 39);
INSERT INTO `user_actions` VALUES (386, '用户登录: 15222222222', 'SUCCESS', '2025-10-31 09:15:31.817450', 'USER_LOGIN', 37);
INSERT INTO `user_actions` VALUES (387, '精确匹配成功', 'SUCCESS', '2025-10-31 09:15:36.607958', 'EXACT_MATCH', 37);
INSERT INTO `user_actions` VALUES (388, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-10-31 09:15:44.939377', 'FUZZY_MATCH', 37);
INSERT INTO `user_actions` VALUES (389, '用户登录: 15211111111', 'SUCCESS', '2025-12-16 04:34:42.127045', 'USER_LOGIN', 25);
INSERT INTO `user_actions` VALUES (390, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:39:04.960322', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (391, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:39:08.647081', 'FUZZY_MATCH', 25);
INSERT INTO `user_actions` VALUES (392, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:39:50.182713', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (393, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:39:58.568367', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (394, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:05.536214', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (395, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:18.083515', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (396, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:32.958898', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (397, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:38.570052', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (398, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:47.709102', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (399, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:44:51.297132', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (400, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:45:33.137836', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (401, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:46:23.475682', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (402, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:46:30.143866', 'FUZZY_MATCH', 25);
INSERT INTO `user_actions` VALUES (403, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:46:44.849033', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (404, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:47:01.831458', 'FUZZY_MATCH', 25);
INSERT INTO `user_actions` VALUES (405, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 04:47:02.924794', 'FUZZY_MATCH', 25);
INSERT INTO `user_actions` VALUES (406, '更新用户偏好信息', 'SUCCESS', '2025-12-16 04:47:50.183225', 'UPDATE_PREFERENCES', 25);
INSERT INTO `user_actions` VALUES (407, '精确匹配成功', 'SUCCESS', '2025-12-16 04:48:59.840092', 'EXACT_MATCH', 25);
INSERT INTO `user_actions` VALUES (408, '模糊匹配成功，匹配数: 11', 'SUCCESS', '2025-12-16 04:49:11.093231', 'FUZZY_MATCH', 25);
INSERT INTO `user_actions` VALUES (409, '用户登录: 13800000008', 'SUCCESS', '2025-12-16 07:58:41.033548', 'USER_LOGIN', 45);
INSERT INTO `user_actions` VALUES (410, '精确匹配成功', 'SUCCESS', '2025-12-16 07:58:42.868759', 'EXACT_MATCH', 45);
INSERT INTO `user_actions` VALUES (411, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-12-16 08:02:22.955144', 'FUZZY_MATCH', 45);
INSERT INTO `user_actions` VALUES (412, '提交反馈：使用问题', 'SUCCESS', '2025-12-16 08:05:19.353646', 'SUBMIT_FEEDBACK', 45);
INSERT INTO `user_actions` VALUES (413, '注册失败: detached entity passed to persist: com.gongchan.model.User', 'FAILED', '2025-12-16 08:12:25.522296', 'USER_REGISTER', 49);
INSERT INTO `user_actions` VALUES (414, '用户登录: 13800000008', 'SUCCESS', '2025-12-16 08:12:40.493616', 'USER_LOGIN', 45);
INSERT INTO `user_actions` VALUES (415, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-12-16 08:12:42.990310', 'FUZZY_MATCH', 45);
INSERT INTO `user_actions` VALUES (416, '精确匹配成功', 'SUCCESS', '2025-12-16 08:12:54.019050', 'EXACT_MATCH', 45);
INSERT INTO `user_actions` VALUES (417, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-12-16 08:39:53.824883', 'FUZZY_MATCH', 45);
INSERT INTO `user_actions` VALUES (418, '模糊匹配成功，匹配数: 10', 'SUCCESS', '2025-12-16 08:48:34.851829', 'FUZZY_MATCH', 45);
INSERT INTO `user_actions` VALUES (419, '用户登录: 13137322421', 'SUCCESS', '2025-12-16 08:49:59.865951', 'USER_LOGIN', 49);
INSERT INTO `user_actions` VALUES (420, '模糊匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 08:50:03.445538', 'FUZZY_MATCH', 49);
INSERT INTO `user_actions` VALUES (421, '匹配失败: 用户未填写偏好信息！', 'FAILED', '2025-12-16 08:50:05.518900', 'EXACT_MATCH', 49);
INSERT INTO `user_actions` VALUES (422, '更新用户偏好信息', 'SUCCESS', '2025-12-16 08:53:48.525054', 'UPDATE_PREFERENCES', 49);
INSERT INTO `user_actions` VALUES (423, '精确匹配成功', 'SUCCESS', '2025-12-16 08:53:50.880320', 'EXACT_MATCH', 49);
INSERT INTO `user_actions` VALUES (424, '模糊匹配成功，匹配数: 12', 'SUCCESS', '2025-12-16 08:54:23.635058', 'FUZZY_MATCH', 49);
INSERT INTO `user_actions` VALUES (425, '提交反馈：界面优化', 'SUCCESS', '2025-12-16 08:55:04.462005', 'SUBMIT_FEEDBACK', 49);
INSERT INTO `user_actions` VALUES (426, '用户登录: 13137322421', 'SUCCESS', '2025-12-18 09:03:16.048006', 'USER_LOGIN', 49);

-- ----------------------------
-- Table structure for user_index_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_index_permission`;
CREATE TABLE `user_index_permission`  (
  `user_id` int(11) NOT NULL,
  `allow_indexing` bit(1) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `fk_index_permission_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_index_permission
-- ----------------------------
INSERT INTO `user_index_permission` VALUES (24, b'1');
INSERT INTO `user_index_permission` VALUES (25, b'1');
INSERT INTO `user_index_permission` VALUES (26, b'1');
INSERT INTO `user_index_permission` VALUES (27, b'1');
INSERT INTO `user_index_permission` VALUES (28, b'1');
INSERT INTO `user_index_permission` VALUES (29, b'1');
INSERT INTO `user_index_permission` VALUES (30, b'1');
INSERT INTO `user_index_permission` VALUES (31, b'1');
INSERT INTO `user_index_permission` VALUES (32, b'1');
INSERT INTO `user_index_permission` VALUES (33, b'1');
INSERT INTO `user_index_permission` VALUES (34, b'1');
INSERT INTO `user_index_permission` VALUES (35, b'1');
INSERT INTO `user_index_permission` VALUES (36, b'1');
INSERT INTO `user_index_permission` VALUES (37, b'1');
INSERT INTO `user_index_permission` VALUES (38, b'1');
INSERT INTO `user_index_permission` VALUES (39, b'1');
INSERT INTO `user_index_permission` VALUES (40, b'1');
INSERT INTO `user_index_permission` VALUES (41, b'1');
INSERT INTO `user_index_permission` VALUES (42, b'1');
INSERT INTO `user_index_permission` VALUES (43, b'1');
INSERT INTO `user_index_permission` VALUES (44, b'1');
INSERT INTO `user_index_permission` VALUES (45, b'1');
INSERT INTO `user_index_permission` VALUES (46, b'1');
INSERT INTO `user_index_permission` VALUES (47, b'1');

-- ----------------------------
-- Table structure for user_interests
-- ----------------------------
DROP TABLE IF EXISTS `user_interests`;
CREATE TABLE `user_interests`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `interest_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `interest_level` int(1) NULL DEFAULT 1 COMMENT '1-一般 2-喜欢 3-热爱',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_interest`(`user_id` ASC) USING BTREE,
  INDEX `idx_interest_name`(`interest_name` ASC) USING BTREE,
  CONSTRAINT `fk_user_interests_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_interests
-- ----------------------------
INSERT INTO `user_interests` VALUES (10, 37, '运动', 1);
INSERT INTO `user_interests` VALUES (11, 37, '旅行', 3);
INSERT INTO `user_interests` VALUES (12, 37, '音乐', 1);
INSERT INTO `user_interests` VALUES (13, 37, '阅读', 1);
INSERT INTO `user_interests` VALUES (14, 38, '运动', 3);
INSERT INTO `user_interests` VALUES (15, 38, '音乐', 2);
INSERT INTO `user_interests` VALUES (16, 38, '旅行', 2);
INSERT INTO `user_interests` VALUES (17, 38, '游戏', 1);
INSERT INTO `user_interests` VALUES (21, 39, '音乐', 3);
INSERT INTO `user_interests` VALUES (22, 39, '旅行', 3);
INSERT INTO `user_interests` VALUES (23, 39, '阅读', 2);
INSERT INTO `user_interests` VALUES (24, 39, '摄影', 2);
INSERT INTO `user_interests` VALUES (28, 40, '游戏', 3);
INSERT INTO `user_interests` VALUES (29, 40, '科技', 3);
INSERT INTO `user_interests` VALUES (30, 40, '阅读', 2);
INSERT INTO `user_interests` VALUES (31, 40, '电影', 2);
INSERT INTO `user_interests` VALUES (35, 41, '阅读', 3);
INSERT INTO `user_interests` VALUES (36, 41, '音乐', 2);
INSERT INTO `user_interests` VALUES (37, 41, '艺术', 2);
INSERT INTO `user_interests` VALUES (38, 41, '旅行', 1);
INSERT INTO `user_interests` VALUES (42, 42, '美食', 3);
INSERT INTO `user_interests` VALUES (43, 42, '摄影', 3);
INSERT INTO `user_interests` VALUES (44, 42, '旅行', 2);
INSERT INTO `user_interests` VALUES (45, 42, '运动', 1);
INSERT INTO `user_interests` VALUES (49, 43, '运动', 2);
INSERT INTO `user_interests` VALUES (50, 43, '音乐', 2);
INSERT INTO `user_interests` VALUES (51, 43, '旅行', 3);
INSERT INTO `user_interests` VALUES (52, 43, '美食', 2);
INSERT INTO `user_interests` VALUES (56, 44, '游戏', 3);
INSERT INTO `user_interests` VALUES (57, 44, '电影', 3);
INSERT INTO `user_interests` VALUES (58, 44, '科技', 2);
INSERT INTO `user_interests` VALUES (59, 44, '音乐', 1);
INSERT INTO `user_interests` VALUES (63, 45, '旅行', 3);
INSERT INTO `user_interests` VALUES (64, 45, '艺术', 3);
INSERT INTO `user_interests` VALUES (65, 45, '摄影', 2);
INSERT INTO `user_interests` VALUES (66, 45, '音乐', 2);
INSERT INTO `user_interests` VALUES (70, 46, '运动', 3);
INSERT INTO `user_interests` VALUES (71, 46, '游戏', 2);
INSERT INTO `user_interests` VALUES (72, 46, '音乐', 2);
INSERT INTO `user_interests` VALUES (73, 46, '美食', 2);
INSERT INTO `user_interests` VALUES (77, 47, '美食', 3);
INSERT INTO `user_interests` VALUES (78, 47, '音乐', 2);
INSERT INTO `user_interests` VALUES (79, 47, '阅读', 2);
INSERT INTO `user_interests` VALUES (80, 47, '旅行', 2);
INSERT INTO `user_interests` VALUES (81, 25, '运动', 1);
INSERT INTO `user_interests` VALUES (82, 25, '音乐', 2);
INSERT INTO `user_interests` VALUES (83, 25, '旅行', 1);
INSERT INTO `user_interests` VALUES (84, 49, '音乐', 1);
INSERT INTO `user_interests` VALUES (85, 49, '阅读', 2);

-- ----------------------------
-- Table structure for user_preferences
-- ----------------------------
DROP TABLE IF EXISTS `user_preferences`;
CREATE TABLE `user_preferences`  (
  `user_id` int(11) NOT NULL,
  `preferred_sex` int(1) NULL DEFAULT NULL COMMENT '期望性别: 0-女, 1-男, null-不限',
  `preferred_age_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `preferred_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `preferred_school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `preferred_personality` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `other_preferences` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `fk_preference_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_preferences
-- ----------------------------
INSERT INTO `user_preferences` VALUES (25, 0, '21-23', '山西省-太原市-迎泽区', '南阳师范学院', '外向', '有幽默感,有上进心,有责任心');
INSERT INTO `user_preferences` VALUES (37, 0, '21-23', '天津市-天津市-和平区', '南阳师范学院', '外向', '喜欢宠物,有幽默感');
INSERT INTO `user_preferences` VALUES (38, 1, '21-23', '天津市-天津市-和平区', '中山大学', '乐观', '有幽默感,喜欢宠物');
INSERT INTO `user_preferences` VALUES (39, 1, '21-23', '天津市-天津市-和平区', '北京大学', '稳重', '顾家');
INSERT INTO `user_preferences` VALUES (40, 0, '21-23', '广东-广州市-天河区', '复旦大学', '乐观', '有幽默感');
INSERT INTO `user_preferences` VALUES (41, 0, '21-23', '广东-广州市-天河区', '中山大学', '稳重', '有责任心,顾家');
INSERT INTO `user_preferences` VALUES (42, 1, '21-23', '广东-广州市-天河区', '北京大学', '稳重', '有幽默感,喜欢宠物');
INSERT INTO `user_preferences` VALUES (43, 1, '21-23', '广东-广州市-天河区', '复旦大学', '乐观', '喜欢宠物,顾家');
INSERT INTO `user_preferences` VALUES (44, 0, '18-20', '湖北-武汉市-武昌区', '中山大学', '理性', '有幽默感');
INSERT INTO `user_preferences` VALUES (45, 1, '18-20', '湖北-武汉市-武昌区', '北京大学', '稳重', '有责任心,顾家');
INSERT INTO `user_preferences` VALUES (46, 0, '18-20', '湖北-武汉市-武昌区', '复旦大学', '理性', '善良,有责任心');
INSERT INTO `user_preferences` VALUES (47, NULL, '21-23', '湖北-武汉市-武昌区', '中山大学', '感性', '热爱旅行,顾家');
INSERT INTO `user_preferences` VALUES (49, 0, '18-20', '陕西省-西安市-雁塔区', '西安交通大学', '外向', '喜欢宠物,善于沟通,善良');

-- ----------------------------
-- Table structure for user_privacy_settings
-- ----------------------------
DROP TABLE IF EXISTS `user_privacy_settings`;
CREATE TABLE `user_privacy_settings`  (
  `user_id` int(11) NOT NULL,
  `show_sex` bit(1) NOT NULL DEFAULT b'1',
  `show_birthday` bit(1) NOT NULL DEFAULT b'0',
  `show_address` bit(1) NOT NULL DEFAULT b'0',
  `show_phone` bit(1) NOT NULL DEFAULT b'0',
  `show_qq` bit(1) NOT NULL DEFAULT b'0',
  `show_wechat` bit(1) NOT NULL DEFAULT b'0',
  `show_school` bit(1) NOT NULL DEFAULT b'1',
  `show_interests` bit(1) NOT NULL DEFAULT b'1',
  `show_bio` bit(1) NOT NULL DEFAULT b'1',
  `allow_stranger_message` bit(1) NOT NULL DEFAULT b'1',
  `allow_search_by_phone` bit(1) NOT NULL DEFAULT b'0',
  `allow_match_recommendation` bit(1) NOT NULL DEFAULT b'1',
  `show_on_discover_page` bit(1) NOT NULL DEFAULT b'1',
  `require_approval_for_view` bit(1) NOT NULL DEFAULT b'0',
  `show_last_active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `fk_privacy_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_privacy_settings
-- ----------------------------
INSERT INTO `user_privacy_settings` VALUES (38, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (39, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (40, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (41, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (42, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (43, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (44, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (45, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (46, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');
INSERT INTO `user_privacy_settings` VALUES (47, b'1', b'0', b'0', b'0', b'0', b'0', b'1', b'1', b'1', b'1', b'0', b'1', b'1', b'0', b'1');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `birthday` datetime(6) NULL DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `qq` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wechat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sex` int(1) NOT NULL,
  `avatar_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `last_login_time` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (24, '111', '2025-10-21 00:00:00.000000', '15238501509', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$5zwKZi7Vv1qVXp4LaE43ZeVJIfN4X/GnyFPS76rOuMzww7n0biqMW', 0, NULL, '2025-10-21 12:12:03.368000', NULL);
INSERT INTO `users` VALUES (25, '王圆', '2025-10-21 00:00:00.000000', '15211111111', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$53Q0LoLaAkgStvV24Bg5d.wWnvt778gNLY6qX2fXb6BZ0UhHSBPcC', 1, NULL, '2025-10-21 12:28:40.493000', '2025-12-16 04:34:41.841000');
INSERT INTO `users` VALUES (26, '王元', '2025-10-21 00:00:00.000000', '15211111112', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$CB0kTvHj9XwDs6w/wQmlu.Z0QSEUP0Iqc8pAttxYr44xACAFaRk8S', 1, NULL, '2025-10-21 12:34:01.007000', NULL);
INSERT INTO `users` VALUES (27, '王一', '2025-10-21 00:00:00.000000', '15211111114', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$pslFupyJxlkNnUXNyVtXEuuUHvX1n/GxbfcPHj/h0Hdo7CwitRL0u', 0, NULL, '2025-10-21 12:43:00.415000', NULL);
INSERT INTO `users` VALUES (28, '王圆业', '2025-10-21 00:00:00.000000', '15211111115', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$8F6c2xPwvvJitqweQk3LIukumMejheUB8FNCxbLyecR9XBONjRjmq', 1, NULL, '2025-10-21 12:48:48.658000', NULL);
INSERT INTO `users` VALUES (29, '王圆', '2025-10-21 00:00:00.000000', '15211111116', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$wq6bOqsl6ipeulwKl5T7auepiPH1LA/leWlcgkQX9mlUdb0Ug6W6W', 1, NULL, '2025-10-21 12:52:37.651000', NULL);
INSERT INTO `users` VALUES (30, '王圆业', '2025-10-21 00:00:00.000000', '15211111118', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$Gm3GuqKvH77/x4REcqjbVundGgUGVLS5dUSWiQgBYFhtzyUVGBLL6', 1, NULL, '2025-10-21 12:57:49.330000', NULL);
INSERT INTO `users` VALUES (31, '王圆业', '2025-10-21 00:00:00.000000', '15211111121', '北京市-北京市-东城区', NULL, NULL, NULL, NULL, '$2a$10$QcsDvBVy04IQF9dKgjduSOPcG183Sw7YyLKPV/s10TOv/FLMpQGKG', 1, NULL, '2025-10-21 13:03:47.213000', NULL);
INSERT INTO `users` VALUES (32, '王圆业', '2025-10-21 00:00:00.000000', '15211111122', '北京市-北京市-东城区', '1111111', '1111111', '你好', '南阳师范学院', '$2a$10$4UpkoBchKTCyEHCw2VQLeuJLSv1QaaIlP3qzzBMVBT0ngMkKpjqCe', 1, NULL, '2025-10-21 13:10:12.439000', NULL);
INSERT INTO `users` VALUES (33, '王圆业', '2025-10-21 00:00:00.000000', '15211111123', '北京市-北京市-东城区', '', '', '', '', '$2a$10$QFrmYN5x1vKFCMfkWSiY2upQ.ZhZqHKlrxVgeFuWytI0WCnqoQ/W6', 1, NULL, '2025-10-21 13:16:08.587000', NULL);
INSERT INTO `users` VALUES (34, '我问问', '2025-10-21 00:00:00.000000', '15238511111', '北京市-北京市-东城区', '', '', '', '', '$2a$10$9r1uLlLk2ij0V3BJOTpuJ.jwp0.ch6lUAVerDxqdFmo1RQpXvPbOy', 1, NULL, '2025-10-21 13:18:48.518000', NULL);
INSERT INTO `users` VALUES (35, '王圆业', '2025-10-21 00:00:00.000000', '15231111111', '北京市-北京市-东城区', '', '', '你好啊', '', '$2a$10$GzQVJpciGbSiKnfoMtmFV.0Tzm0eAsKVwrPlSMqJ2QECDgLcXEXyO', 1, NULL, '2025-10-21 13:20:39.941000', NULL);
INSERT INTO `users` VALUES (36, '王圆业', '2025-10-21 00:00:00.000000', '15211132111', '北京市-北京市-东城区', '', '', '最后一次测试', '', '$2a$10$RyAtGOjSEg8xY2oSu3Y9/uvW81hoENwoDicUb0A1X8.qDLi5VDr2i', 1, NULL, '2025-10-21 13:50:31.599000', NULL);
INSERT INTO `users` VALUES (37, '付皓元', '2006-02-01 00:00:00.000000', '15222222222', '河北省-石家庄市-长安区', '12345', '', '大家好', '南阳师范学院', '$2a$10$JP.aNaY3s2wBn3t63/r.T.wXcMSZ/JFuigJViTGNRPbbrupI/Pbmq', 1, NULL, '2025-10-21 14:26:19.781000', '2025-10-31 09:15:31.624000');
INSERT INTO `users` VALUES (38, '张伟', '1995-03-15 00:00:00.000000', '13800000001', '北京-北京市-朝阳区', '1234567890', 'zhangwei_wx', '热爱运动，喜欢交友', '北京大学', '$2a$10$hs2AgmZkmN6XwdHHauZisOyU5j5dg6o41IaVQwqINpient8GoTF6m', 1, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (39, '李娜', '1998-06-20 00:00:00.000000', '13800000002', '上海-上海市-浦东新区', '2345678901', 'lina_wx', '喜欢音乐和旅行', '复旦大学', '$2a$10$XBUb1GIDzb5XP1XtDLN5ruTQ33SYQ9PTWvR.npGcBvfR6nYJ3i.Qi', 0, NULL, '2025-10-22 21:13:22.000000', '2025-10-23 11:10:29.518000');
INSERT INTO `users` VALUES (40, '王强', '1996-09-10 00:00:00.000000', '13800000003', '广东-广州市-天河区', '3456789012', 'wangqiang_wx', '技术宅，热爱编程', '中山大学', '$2a$10$ChutxhI.omEY1Re2jmT2quuW79U3.b3bh1nfsy2GxhSbrXRuPWca.', 1, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (41, '刘芳', '1997-12-05 00:00:00.000000', '13800000004', '浙江-杭州市-西湖区', '4567890123', 'liufang_wx', '爱好阅读，喜欢安静', '浙江大学', '$2a$10$nW6WKCD1i/T3jcF/A8yI7ObKxYDCDrJhnFYCdnDiSDu6R5bHz3.9u', 0, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (42, '陈明', '1995-04-25 00:00:00.000000', '13800000005', '江苏-南京市-鼓楼区', '5678901234', 'chenming_wx', '热爱美食，喜欢摄影', '南京大学', '$2a$10$UbfLKE.nkHGl/j3ZirU4A.W6.V6Zb.K/7q1XNPFblmNvepddjfzhO', 1, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (43, '赵敏', '1999-08-18 00:00:00.000000', '13800000006', '四川-成都市-武侯区', '6789012345', 'zhaomin_wx', '外向开朗，喜欢交朋友', '四川大学', '$2a$10$6lRBgPpYSC9xzGF0db0drujWRRw2SpIykFj7zS6qSP0I5uR1tXxPS', 0, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (44, '孙磊', '1994-11-30 00:00:00.000000', '13800000007', '湖北-武汉市-武昌区', '7890123456', 'sunlei_wx', '喜欢游戏和电影', '武汉大学', '$2a$10$t7LJJpCsEg3S5tSddxPLRuZQFRjBVtzhzfQUdM.i8GnCtHohbrSZG', 1, NULL, '2025-10-22 21:13:22.000000', NULL);
INSERT INTO `users` VALUES (45, '周婷', '2006-02-14 00:00:00.000000', '13800000008', '陕西-西安市-雁塔区', '8901234567', 'zhouting_wx', '热爱旅行，喜欢艺术', '西安交通大学', '$2a$10$tkNrLvtS2e624gAF7T0O2u2UZh6XKRzkCOh.Nf0Kbn1Yluj4uAy5C', 0, NULL, '2025-10-22 21:13:22.000000', '2025-12-16 08:12:40.405000');
INSERT INTO `users` VALUES (46, '吴刚', '1996-07-22 00:00:00.000000', '13800000009', '湖南-长沙市-岳麓区', '9012345678', 'wugang_wx', '健身达人，喜欢运动', '湖南大学', '$2a$10$XGizFoX/Y4RkKCP8RNuXpu9hFEuVYxGlnrRVWi9MpJPsnc9Ff.E9C', 1, NULL, '2025-10-22 21:13:22.000000', '2025-10-22 13:47:53.065000');
INSERT INTO `users` VALUES (47, '郑丽', '2003-05-08 00:00:00.000000', '13800000010', '山东-青岛市-市南区', '0123456789', 'zhengli_wx', '温柔体贴，喜欢美食', '中国海洋大学', '$2a$10$PReYaUYWk0dMRIZQ3SWbFOm6zbQj7rsJKzEl71JSP6J3mZDnLKLzG', 1, NULL, '2025-10-22 21:13:22.000000', '2025-10-23 11:01:37.825000');
INSERT INTO `users` VALUES (49, '李子明', '2006-02-01 00:00:00.000000', '13137322421', '湖北省-武汉市-武昌区', '', '', '大家好', '北京大学', '$2a$10$p6yyynxRw.MwQVKD31ehtOomN.Qr3hEQG.fkrJaXwL6ZtVFjM5Yqe', 1, NULL, '2025-12-16 08:12:25.322000', '2025-12-18 09:03:15.982000');

SET FOREIGN_KEY_CHECKS = 1;
