/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : canteen_review

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 19/03/2026 13:59:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品名称',
  `window_id` bigint(20) NOT NULL COMMENT '所属窗口ID',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类：早餐/午餐/晚餐',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜品描述',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜品图片',
  `average_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '平均评分',
  `review_count` int(11) NULL DEFAULT 0 COMMENT '评价数量',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_window_id`(`window_id` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_average_rating`(`average_rating` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES (1, '三鲜包', 1, '早餐', '鲜肉、虾仁、香菇馅', 2.50, NULL, 4.5, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (2, '鲜肉包', 1, '早餐', '纯肉馅，汁多味美', 2.00, NULL, 4.3, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (3, '豆沙包', 1, '早餐', '红豆沙馅，甜而不腻', 1.50, NULL, 4.0, 1, 1, '2026-03-18 15:34:07', '2026-03-18 20:24:03', 0);
INSERT INTO `dish` VALUES (4, '八宝粥', 1, '早餐', '八种食材熬制，营养丰富', 4.00, NULL, 4.2, 12, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (5, '小米粥', 1, '早餐', '养胃小米粥', 2.00, NULL, 4.1, 8, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (6, '豆浆', 1, '早餐', '现磨豆浆，香浓可口', 2.00, '/images/1773836568148_1544170012893364.jpg', 4.7, 3, 1, '2026-03-18 15:34:07', '2026-03-18 17:12:07', 0);
INSERT INTO `dish` VALUES (7, '油条', 1, '早餐', '金黄酥脆', 1.50, NULL, 4.0, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (8, '鸡蛋灌饼', 1, '早餐', '鸡蛋灌入饼中，外酥里嫩', 5.00, NULL, 4.3, 14, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (9, '牛肉拉面', 2, '午餐', '正宗兰州牛肉拉面，汤清味鲜', 12.00, '/images/1773845849899_O1CN01ki6XQU1bksKXfW3gj_!!6000000003504-0-fliggyimage.jpg', 4.6, 30, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (10, '红烧牛肉面', 2, '午餐', '红烧牛肉，软烂入味', 15.00, NULL, 4.5, 25, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (11, '番茄鸡蛋面', 2, '午餐', '番茄鸡蛋卤，酸甜可口', 10.00, NULL, 4.2, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (12, '炸酱面', 2, '午餐', '老北京炸酱面', 11.00, NULL, 4.3, 22, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (13, '刀削面', 2, '午餐', '手工刀削面，筋道爽滑', 12.00, NULL, 4.4, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (14, '炒拉条', 2, '晚餐', '新疆风味炒拉条', 13.00, NULL, 4.1, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (15, '宫保鸡丁', 3, '午餐', '经典川菜，麻辣鲜香', 16.00, NULL, 4.5, 28, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (16, '鱼香肉丝', 3, '午餐', '酸甜微辣，下饭神器', 14.00, NULL, 4.4, 32, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (17, '麻婆豆腐', 3, '午餐', '麻辣豆腐，嫩滑爽口', 10.00, NULL, 4.3, 26, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (18, '回锅肉', 3, '午餐', '川菜经典，肥而不腻', 18.00, NULL, 4.0, 1, 1, '2026-03-18 15:34:07', '2026-03-18 17:28:54', 0);
INSERT INTO `dish` VALUES (19, '水煮鱼', 3, '晚餐', '麻辣水煮鱼，鲜嫩可口', 25.00, NULL, 4.0, 1, 1, '2026-03-18 15:34:07', '2026-03-18 23:01:08', 0);
INSERT INTO `dish` VALUES (20, '辣子鸡', 3, '午餐', '重庆辣子鸡，麻辣酥脆', 22.00, NULL, 4.5, 20, 1, '2026-03-18 15:34:07', '2026-03-18 18:47:52', 0);
INSERT INTO `dish` VALUES (21, '烧鸭饭', 4, '午餐', '广式烧鸭，皮脆肉嫩', 18.00, NULL, 4.4, 22, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (22, '叉烧饭', 4, '午餐', '蜜汁叉烧，甜而不腻', 16.00, NULL, 4.3, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (23, '白切鸡饭', 4, '午餐', '清淡鲜美，原汁原味', 15.00, NULL, 4.2, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (24, '烧鹅饭', 4, '晚餐', '广式烧鹅，香气四溢', 22.00, NULL, 4.6, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (25, '烧腊双拼', 4, '晚餐', '烧鸭+叉烧', 25.00, NULL, 4.5, 16, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (26, '猪肉白菜饺', 5, '午餐', '经典口味，皮薄馅大', 12.00, NULL, 4.3, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (27, '韭菜鸡蛋饺', 5, '午餐', '素馅饺子，清香可口', 10.00, NULL, 4.1, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (28, '三鲜饺', 5, '午餐', '虾仁、鸡蛋、韭菜', 15.00, NULL, 4.4, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (29, '牛肉饺', 5, '晚餐', '牛肉馅，鲜美多汁', 16.00, NULL, 4.5, 22, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (30, '酸菜饺', 5, '晚餐', '东北酸菜，酸爽开胃', 12.00, NULL, 4.2, 12, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (31, '自选麻辣烫', 9, '午餐', '自选菜品，称重计费', 15.00, NULL, 4.2, 40, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (32, '麻辣拌', 9, '午餐', '干拌麻辣烫', 14.00, NULL, 4.3, 25, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (33, '酸辣粉', 9, '晚餐', '重庆酸辣粉', 12.00, NULL, 4.1, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (34, '黄焖鸡米饭', 11, '午餐', '招牌黄焖鸡，米饭管饱', 16.00, NULL, 4.5, 35, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (35, '黄焖排骨', 11, '午餐', '排骨软烂，汤汁浓郁', 18.00, NULL, 4.4, 22, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (36, '黄焖猪蹄', 11, '晚餐', '猪蹄软糯，胶原蛋白', 20.00, NULL, 4.3, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (37, '青椒肉丝', 13, '午餐', '家常小炒，下饭神器', 14.00, NULL, 4.2, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (38, '番茄炒蛋', 13, '午餐', '经典家常菜', 10.00, NULL, 4.3, 25, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (39, '蒜蓉西兰花', 13, '午餐', '清淡健康', 12.00, NULL, 4.0, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (40, '糖醋里脊', 13, '晚餐', '酸甜可口，外酥里嫩', 18.00, NULL, 4.4, 22, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (41, '干锅花菜', 13, '晚餐', '干锅风味，香辣可口', 14.00, NULL, 4.1, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (42, '铁板牛肉', 14, '午餐', '现场铁板烧制，香气四溢', 22.00, NULL, 4.5, 28, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (43, '铁板鱿鱼', 14, '午餐', '新鲜鱿鱼，口感Q弹', 20.00, NULL, 4.4, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (44, '铁板豆腐', 14, '晚餐', '外焦里嫩，香气扑鼻', 12.00, NULL, 4.2, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (45, '麻辣锅底', 16, '晚餐', '重庆麻辣锅底', 8.00, NULL, 4.3, 30, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (46, '番茄锅底', 16, '晚餐', '番茄汤底，酸甜可口', 6.00, NULL, 4.1, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (47, '清汤锅底', 16, '晚餐', '清淡汤底，养生健康', 5.00, NULL, 4.0, 10, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (48, '炸酱面', 18, '午餐', '老北京炸酱面', 12.00, NULL, 4.2, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (49, '打卤面', 18, '午餐', '西红柿鸡蛋卤', 11.00, NULL, 4.1, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (50, '油泼面', 18, '晚餐', '陕西油泼面', 13.00, NULL, 4.3, 18, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (51, '皮蛋瘦肉粥', 19, '早餐', '经典广式粥品', 8.00, NULL, 4.4, 25, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (52, '南瓜粥', 19, '早餐', '香甜南瓜粥', 5.00, NULL, 4.2, 12, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (53, '蔬菜粥', 19, '晚餐', '清淡蔬菜粥', 6.00, NULL, 4.0, 10, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (54, '珍珠奶茶', 20, '午餐', '经典珍珠奶茶', 10.00, NULL, 4.5, 40, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (55, '芒果班戟', 20, '午餐', '港式甜品', 12.00, NULL, 4.3, 15, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (56, '双皮奶', 20, '晚餐', '广东传统甜品', 8.00, NULL, 4.4, 20, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `dish` VALUES (57, '多样菜品', 24, '午餐', '新鲜蔬菜，肉类，多样选择', 15.00, NULL, 0.0, 0, 1, '2026-03-18 18:55:47', '2026-03-18 19:20:12', 0);
INSERT INTO `dish` VALUES (58, '1', 1, '早餐', '', 1.00, NULL, 0.0, 0, 0, '2026-03-18 18:56:23', '2026-03-18 18:56:28', 1);

-- ----------------------------
-- Table structure for dish_tag
-- ----------------------------
DROP TABLE IF EXISTS `dish_tag`;
CREATE TABLE `dish_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品ID',
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dish_tag`(`dish_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜品标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish_tag
-- ----------------------------
INSERT INTO `dish_tag` VALUES (1, 1, 3);
INSERT INTO `dish_tag` VALUES (2, 1, 4);
INSERT INTO `dish_tag` VALUES (3, 2, 3);
INSERT INTO `dish_tag` VALUES (4, 2, 4);
INSERT INTO `dish_tag` VALUES (5, 3, 2);
INSERT INTO `dish_tag` VALUES (6, 3, 5);
INSERT INTO `dish_tag` VALUES (7, 6, 2);
INSERT INTO `dish_tag` VALUES (8, 6, 7);
INSERT INTO `dish_tag` VALUES (37, 9, 2);
INSERT INTO `dish_tag` VALUES (38, 9, 3);
INSERT INTO `dish_tag` VALUES (39, 9, 6);
INSERT INTO `dish_tag` VALUES (11, 10, 3);
INSERT INTO `dish_tag` VALUES (12, 10, 4);
INSERT INTO `dish_tag` VALUES (13, 15, 1);
INSERT INTO `dish_tag` VALUES (14, 15, 4);
INSERT INTO `dish_tag` VALUES (15, 16, 1);
INSERT INTO `dish_tag` VALUES (16, 16, 4);
INSERT INTO `dish_tag` VALUES (17, 17, 1);
INSERT INTO `dish_tag` VALUES (18, 17, 4);
INSERT INTO `dish_tag` VALUES (19, 18, 1);
INSERT INTO `dish_tag` VALUES (20, 18, 4);
INSERT INTO `dish_tag` VALUES (21, 19, 1);
INSERT INTO `dish_tag` VALUES (22, 19, 6);
INSERT INTO `dish_tag` VALUES (23, 19, 8);
INSERT INTO `dish_tag` VALUES (24, 21, 3);
INSERT INTO `dish_tag` VALUES (25, 21, 6);
INSERT INTO `dish_tag` VALUES (26, 22, 3);
INSERT INTO `dish_tag` VALUES (27, 22, 6);
INSERT INTO `dish_tag` VALUES (28, 35, 3);
INSERT INTO `dish_tag` VALUES (29, 35, 4);
INSERT INTO `dish_tag` VALUES (30, 35, 8);
INSERT INTO `dish_tag` VALUES (31, 36, 3);
INSERT INTO `dish_tag` VALUES (32, 36, 4);
INSERT INTO `dish_tag` VALUES (33, 40, 3);
INSERT INTO `dish_tag` VALUES (34, 40, 6);
INSERT INTO `dish_tag` VALUES (35, 48, 3);
INSERT INTO `dish_tag` VALUES (36, 48, 8);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_type` int(11) NOT NULL COMMENT '收藏类型：1-菜品，2-窗口',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_target`(`user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (1, 1, 1, 6, NULL, 0);

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '发送通知的用户ID',
  `type` int(11) NOT NULL COMMENT '通知类型：1-系统通知，2-活动通知',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 1, 1, '欢迎使用校园食堂评价系统！', '2026-03-18 21:56:40', 0);
INSERT INTO `notification` VALUES (2, 1, 2, '本周推出新菜品，欢迎品尝！', '2026-03-18 21:56:40', 0);
INSERT INTO `notification` VALUES (3, 1, 2, '周六周日，二楼餐厅晚间全部8折！', '2026-03-19 00:40:01', 0);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品ID',
  `rating` decimal(2, 1) NOT NULL COMMENT '评分：0.5-5',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价内容',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价图片',
  `is_anonymous` int(11) NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-待审核，1-已通过，2-已拒绝',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1, 2, 1, 5.0, '三鲜包真的很好吃，馅料很足！', NULL, 0, 6, 1, '2026-03-18 15:34:07', '2026-03-18 22:04:42', 0);
INSERT INTO `review` VALUES (2, 2, 2, 4.5, '鲜肉包汁水丰富，推荐！', NULL, 0, 3, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (3, 3, 9, 5.0, '牛肉拉面汤清味鲜，面条劲道', NULL, 0, 8, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (4, 3, 10, 4.0, '红烧牛肉面牛肉很多，不错', NULL, 0, 2, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (5, 4, 15, 5.0, '宫保鸡丁麻辣鲜香，下饭神器！', NULL, 0, 10, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (6, 4, 16, 4.5, '鱼香肉丝酸甜可口', NULL, 0, 4, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (7, 2, 21, 4.0, '烧鸭皮脆肉嫩，不错', NULL, 1, 3, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (8, 3, 26, 5.0, '猪肉白菜饺皮薄馅大', NULL, 0, 6, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (9, 4, 32, 4.5, '麻辣烫自选菜品很丰富', NULL, 0, 5, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (10, 2, 35, 5.0, '黄焖鸡米饭是招牌，必点！', NULL, 0, 12, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (11, 3, 40, 4.0, '铁板牛肉现场制作，很香', NULL, 1, 4, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (12, 4, 48, 5.0, '珍珠奶茶很好喝', NULL, 0, 8, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `review` VALUES (13, 2, 6, 4.0, '味道不错', NULL, 0, 6, 1, '2026-03-18 17:11:20', '2026-03-18 23:10:32', 0);
INSERT INTO `review` VALUES (14, 2, 6, 5.0, '很好', NULL, 1, 0, 1, '2026-03-18 17:12:00', '2026-03-18 17:13:48', 1);
INSERT INTO `review` VALUES (15, 2, 6, 5.0, '很好', NULL, 0, 0, 1, '2026-03-18 17:12:08', '2026-03-18 17:14:41', 1);
INSERT INTO `review` VALUES (16, 2, 19, 4.0, '味道很好，就是有点贵', NULL, 0, 0, 1, '2026-03-18 17:18:50', '2026-03-18 17:18:50', 0);
INSERT INTO `review` VALUES (17, 2, 19, 5.0, '1', NULL, 0, 0, 1, '2026-03-18 17:19:07', '2026-03-18 17:19:14', 1);
INSERT INTO `review` VALUES (18, 2, 18, 4.0, '不错', NULL, 0, 0, 1, '2026-03-18 17:28:54', '2026-03-18 17:28:54', 0);
INSERT INTO `review` VALUES (19, 2, 3, 4.0, '味道可以', NULL, 0, 0, 1, '2026-03-18 17:29:40', '2026-03-18 17:29:40', 0);
INSERT INTO `review` VALUES (20, 2, 3, 5.0, '真的很不错', NULL, 0, 0, 1, '2026-03-18 17:30:14', '2026-03-18 18:58:37', 1);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, '辣', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (2, '清淡', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (3, '招牌', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (4, '推荐', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (5, '实惠', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (6, '特色', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (7, '健康', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (8, '人气', '2026-03-18 21:56:40', 0);
INSERT INTO `tag` VALUES (16, '营养1', '2026-03-18 23:13:00', 1);
INSERT INTO `tag` VALUES (23, '新品', '2026-03-18 23:43:01', 0);
INSERT INTO `tag` VALUES (24, '2', '2026-03-18 23:45:26', 1);
INSERT INTO `tag` VALUES (27, '营养', '2026-03-18 23:51:03', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `role` int(11) NULL DEFAULT 0 COMMENT '角色：0-学生，1-管理员',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_id`(`student_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_role`(`role` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '$2a$10$ifrwryLxBigfimoLonA03O5Ld0k8on1iIUxxN25evXPW2SVKxzXvG', '13800138000', 'admin@nynu.edu.cn', '/images/avatar/1773845895968_眼巴巴.png', 1, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `user` VALUES (2, '2026001', 'user', '$2a$10$Ojd.DoTlFkVWFHGA8nnl2uhyY.sm2lQJ6OvTWTrmgl6YXO6leFTaS', '13800138001', 'zhangsan@nynu.edu.cn', '/images/avatar/1773839733832_眼巴巴.png', 0, 1, '2026-03-18 15:34:07', '2026-03-18 17:31:27', 0);
INSERT INTO `user` VALUES (3, '2026002', '张三', '$2a$10$fUZWE6LZ0z4rvBSTGaYdMOrmnmUGGSOg9IPxVJ2uDo.K.UYr3x/YG', '13800138002', 'lisi@campus.edu.cn', NULL, 0, 1, '2026-03-18 15:34:07', '2026-03-18 18:07:11', 0);
INSERT INTO `user` VALUES (4, '2026003', '王五', '$2a$10$QG47cw8y2QVYJcVLvp9TPeF8z8kynAIEnINHr90g9A585ZSI0Sfs6', '13800138003', 'wangwu@campus.edu.cn', NULL, 0, 1, '2026-03-18 15:34:07', '2026-03-18 17:31:33', 0);
INSERT INTO `user` VALUES (5, '2026004', 'user2', '$2a$10$zgW8i0r91Yarnk0AhQKIXuyf6nIPjCoW9cx25lWCCYuqrLP9Pc0.q', '13800138004', '', NULL, 0, 1, '2026-03-18 17:31:50', '2026-03-18 17:45:15', 0);

-- ----------------------------
-- Table structure for window
-- ----------------------------
DROP TABLE IF EXISTS `window`;
CREATE TABLE `window`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '窗口名称',
  `floor` int(11) NOT NULL COMMENT '楼层：1-一楼，2-二楼',
  `window_number` int(11) NOT NULL COMMENT '窗口编号',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '窗口描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '窗口图片',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-休息中，1-营业中',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_floor`(`floor` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '窗口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of window
-- ----------------------------
INSERT INTO `window` VALUES (1, '营养早餐', 1, 1, '提供各类营养早餐，包括包子、粥品等', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 19:43:25', 0);
INSERT INTO `window` VALUES (2, '兰州牛肉拉面', 1, 2, '正宗兰州拉面，汤鲜味美', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (3, '川味小炒', 1, 3, '地道川菜，麻辣鲜香', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (4, '粤式烧腊', 1, 4, '广式烧腊，皮脆肉嫩', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (5, '东北饺子', 1, 5, '手工水饺，皮薄馅大', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (6, '日式料理', 1, 6, '寿司、拉面、盖饭', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (7, '韩式拌饭', 1, 7, '石锅拌饭、泡菜汤', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (8, '西式快餐', 1, 8, '汉堡、炸鸡、薯条', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (9, '麻辣烫', 1, 9, '自选麻辣烫，麻辣鲜香', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (10, '砂锅米线', 1, 10, '云南过桥米线，汤鲜味美', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (11, '黄焖鸡米饭', 1, 11, '招牌黄焖鸡，米饭管饱', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (12, '盖浇饭', 1, 12, '各类盖浇饭，经济实惠', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (13, '精品小炒', 2, 1, '现炒小炒，锅气十足', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (14, '铁板烧', 2, 2, '现场铁板烧制，香气四溢', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (15, '烤鱼', 2, 3, '重庆烤鱼，麻辣鲜香', NULL, 2, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (16, '火锅', 2, 4, '小火锅，一人一锅', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (17, '烧烤', 2, 5, '各类烤串，现烤现卖', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (18, '面食', 2, 6, '各类面食，手工制作', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (19, '粥品', 2, 7, '养生粥品，清淡养胃', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (20, '甜品站', 2, 8, '奶茶、蛋糕、冰淇淋', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (21, '水果捞', 2, 9, '新鲜水果，健康美味', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (22, '素食', 2, 10, '健康素食，清淡养生', NULL, 0, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (23, '海鲜', 2, 11, '各类海鲜，新鲜美味', NULL, 2, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (24, '自助餐', 2, 12, '自选菜品，称重计费', NULL, 1, '2026-03-18 15:34:07', '2026-03-18 15:34:07', 0);
INSERT INTO `window` VALUES (25, '1', 1, 1, '1', NULL, 1, '2026-03-18 18:57:20', '2026-03-18 18:57:27', 1);

SET FOREIGN_KEY_CHECKS = 1;
