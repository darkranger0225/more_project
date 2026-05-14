/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : restaurant_pos

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 19/03/2026 13:26:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ENABLED' COMMENT '状态: ENABLED-启用, DISABLED-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_sort`(`status` ASC, `sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '热菜', '各类热炒菜品', 1, 'ENABLED', '2026-03-17 23:19:38', '2026-03-18 02:01:04', 0);
INSERT INTO `category` VALUES (2, '凉菜', '凉拌菜、冷盘', 2, 'ENABLED', '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `category` VALUES (3, '汤羹', '各类汤品', 3, 'ENABLED', '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `category` VALUES (4, '主食', '米饭、面条等', 4, 'ENABLED', '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `category` VALUES (5, '饮品', '酒水饮料', 5, 'ENABLED', '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `category` VALUES (6, '甜点', '甜品、水果', 6, 'ENABLED', '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜品描述',
  `image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ON_SALE' COMMENT '状态: ON_SALE-在售, OFF_SALE-下架',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_category_status`(`category_id` ASC, `status` ASC) USING BTREE,
  CONSTRAINT `dish_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES (1, '宫保鸡丁', 38.00, '经典川菜，鸡肉鲜嫩，花生酥脆', NULL, 1, 'ON_SALE', 1, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (2, '鱼香肉丝', 32.00, '酸甜可口，下饭神器', NULL, 1, 'ON_SALE', 2, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (3, '麻婆豆腐', 22.00, '麻辣鲜香，豆腐嫩滑', NULL, 1, 'ON_SALE', 3, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (4, '回锅肉', 42.00, '肥而不腻，香气四溢', NULL, 1, 'ON_SALE', 4, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (5, '糖醋里脊', 36.00, '外酥里嫩，酸甜适中', NULL, 1, 'ON_SALE', 5, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (6, '凉拌黄瓜', 12.00, '清爽解腻，开胃小菜', NULL, 2, 'ON_SALE', 1, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (7, '拍黄瓜', 10.00, '蒜香浓郁，清凉爽口', NULL, 2, 'ON_SALE', 2, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (8, '凉拌木耳', 16.00, '营养丰富，口感爽脆', NULL, 2, 'ON_SALE', 3, '2026-03-17 23:19:38', '2026-03-18 02:01:34', 0);
INSERT INTO `dish` VALUES (9, '西红柿蛋汤', 15.00, '家常美味，营养丰富', NULL, 3, 'ON_SALE', 1, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (10, '紫菜蛋花汤', 12.00, '清淡可口，暖胃佳品', NULL, 3, 'ON_SALE', 2, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (11, '玉米排骨汤', 36.00, '汤鲜味美，滋补养生', '', 3, 'ON_SALE', 3, '2026-03-17 23:19:38', '2026-03-18 01:26:40', 0);
INSERT INTO `dish` VALUES (12, '白米饭', 3.00, '香喷喷的白米饭', NULL, 4, 'ON_SALE', 1, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (13, '蛋炒饭', 15.00, '粒粒分明，香气扑鼻', NULL, 4, 'ON_SALE', 2, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (14, '牛肉面', 25.00, '汤浓面劲，牛肉软烂', NULL, 4, 'ON_SALE', 3, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (15, '可乐', 8.00, '可口可乐', '/images/dishes/cfab853e-4ea3-4362-bd64-10b8dbacd637.jpg', 5, 'ON_SALE', 1, '2026-03-17 23:19:38', '2026-03-18 03:34:30', 0);
INSERT INTO `dish` VALUES (16, '雪碧', 8.00, '清爽柠檬味', NULL, 5, 'ON_SALE', 2, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (17, '橙汁', 12.00, '鲜榨橙汁', NULL, 5, 'ON_SALE', 3, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `dish` VALUES (18, '啤酒', 10.00, '冰镇啤酒', NULL, 5, 'ON_SALE', 4, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `points` int(11) NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_6ithqvsvrcawbi9dtxu0ttsny`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, '2026-03-18 03:05:01.063810', 0, '2026-03-18 03:06:40.274656', '狄仁杰', '13222222222', 375, 'ENABLED');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品ID',
  `dish_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称(快照)',
  `dish_price` decimal(10, 2) NOT NULL COMMENT '菜品单价(快照)',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-18 01:21:48', '2026-03-18 01:21:48', 0);
INSERT INTO `order_item` VALUES (2, 1, 5, '糖醋里脊', 36.00, 2, 72.00, NULL, '2026-03-18 01:21:48', '2026-03-18 01:21:48', 0);
INSERT INTO `order_item` VALUES (3, 1, 7, '拍黄瓜', 10.00, 1, 10.00, NULL, '2026-03-18 01:21:48', '2026-03-18 01:21:48', 0);
INSERT INTO `order_item` VALUES (4, 1, 12, '白米饭', 3.00, 2, 6.00, NULL, '2026-03-18 01:21:48', '2026-03-18 01:21:48', 0);
INSERT INTO `order_item` VALUES (5, 2, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (6, 2, 10, '紫菜蛋花汤', 12.00, 1, 12.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (7, 2, 14, '牛肉面', 25.00, 1, 25.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (8, 2, 4, '回锅肉', 42.00, 1, 42.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (9, 2, 6, '凉拌黄瓜', 12.00, 2, 24.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (10, 2, 15, '可乐', 8.00, 2, 16.00, NULL, '2026-03-18 01:32:31', '2026-03-18 01:32:31', 0);
INSERT INTO `order_item` VALUES (11, 3, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-18 01:58:26', '2026-03-18 01:58:26', 0);
INSERT INTO `order_item` VALUES (12, 3, 2, '鱼香肉丝', 32.00, 1, 32.00, NULL, '2026-03-18 01:58:26', '2026-03-18 01:58:26', 0);
INSERT INTO `order_item` VALUES (13, 3, 3, '麻婆豆腐', 22.00, 1, 22.00, NULL, '2026-03-18 01:58:26', '2026-03-18 01:58:26', 0);
INSERT INTO `order_item` VALUES (14, 3, 15, '可乐', 8.00, 1, 8.00, NULL, '2026-03-18 01:58:26', '2026-03-18 01:58:26', 0);
INSERT INTO `order_item` VALUES (15, 3, 12, '白米饭', 3.00, 1, 3.00, NULL, '2026-03-18 01:58:26', '2026-03-18 01:58:26', 0);
INSERT INTO `order_item` VALUES (16, 4, 7, '拍黄瓜', 10.00, 1, 10.00, NULL, '2026-03-18 01:58:44', '2026-03-18 01:58:44', 0);
INSERT INTO `order_item` VALUES (17, 4, 10, '紫菜蛋花汤', 12.00, 1, 12.00, NULL, '2026-03-18 01:58:44', '2026-03-18 01:58:44', 0);
INSERT INTO `order_item` VALUES (18, 4, 2, '鱼香肉丝', 32.00, 1, 32.00, NULL, '2026-03-18 01:58:44', '2026-03-18 01:58:44', 0);
INSERT INTO `order_item` VALUES (19, 4, 15, '可乐', 8.00, 1, 8.00, NULL, '2026-03-18 01:58:44', '2026-03-18 01:58:44', 0);
INSERT INTO `order_item` VALUES (20, 4, 12, '白米饭', 3.00, 1, 3.00, NULL, '2026-03-18 01:58:44', '2026-03-18 01:58:44', 0);
INSERT INTO `order_item` VALUES (21, 5, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-18 02:16:33', '2026-03-18 02:16:33', 0);
INSERT INTO `order_item` VALUES (22, 5, 6, '凉拌黄瓜', 12.00, 1, 12.00, NULL, '2026-03-18 02:16:33', '2026-03-18 02:16:33', 0);
INSERT INTO `order_item` VALUES (23, 5, 9, '西红柿蛋汤', 15.00, 1, 15.00, NULL, '2026-03-18 02:16:33', '2026-03-18 02:16:33', 0);
INSERT INTO `order_item` VALUES (24, 5, 13, '蛋炒饭', 15.00, 1, 15.00, NULL, '2026-03-18 02:16:33', '2026-03-18 02:16:33', 0);
INSERT INTO `order_item` VALUES (25, 5, 12, '白米饭', 3.00, 1, 3.00, NULL, '2026-03-18 02:16:33', '2026-03-18 02:16:33', 0);
INSERT INTO `order_item` VALUES (26, 6, 13, '蛋炒饭', 15.00, 2, 30.00, NULL, '2026-03-18 02:17:18', '2026-03-18 02:17:18', 0);
INSERT INTO `order_item` VALUES (27, 6, 3, '麻婆豆腐', 22.00, 1, 22.00, NULL, '2026-03-18 02:17:18', '2026-03-18 02:17:18', 0);
INSERT INTO `order_item` VALUES (28, 6, 8, '凉拌木耳', 16.00, 1, 16.00, NULL, '2026-03-18 02:17:18', '2026-03-18 02:17:18', 0);
INSERT INTO `order_item` VALUES (29, 6, 15, '可乐', 8.00, 1, 8.00, NULL, '2026-03-18 02:17:18', '2026-03-18 02:17:18', 0);
INSERT INTO `order_item` VALUES (30, 7, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-18 03:06:29', '2026-03-18 03:06:29', 0);
INSERT INTO `order_item` VALUES (31, 7, 6, '凉拌黄瓜', 12.00, 1, 12.00, NULL, '2026-03-18 03:06:29', '2026-03-18 03:06:29', 0);
INSERT INTO `order_item` VALUES (32, 7, 9, '西红柿蛋汤', 15.00, 1, 15.00, NULL, '2026-03-18 03:06:29', '2026-03-18 03:06:29', 0);
INSERT INTO `order_item` VALUES (33, 8, 15, '可乐', 8.00, 1, 8.00, NULL, '2026-03-19 13:19:34', '2026-03-19 13:19:34', 0);
INSERT INTO `order_item` VALUES (34, 8, 1, '宫保鸡丁', 38.00, 1, 38.00, NULL, '2026-03-19 13:19:34', '2026-03-19 13:19:34', 0);
INSERT INTO `order_item` VALUES (35, 8, 6, '凉拌黄瓜', 12.00, 1, 12.00, NULL, '2026-03-19 13:19:34', '2026-03-19 13:19:34', 0);
INSERT INTO `order_item` VALUES (36, 8, 4, '回锅肉', 42.00, 1, 42.00, NULL, '2026-03-19 13:19:34', '2026-03-19 13:19:34', 0);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `table_id` bigint(20) NOT NULL COMMENT '桌台ID',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `person_count` int(11) NOT NULL DEFAULT 1 COMMENT '用餐人数',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, COOKING-制作中, SERVED-已上菜, PAID-已支付, CANCELLED-已取消',
  `pay_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式: CASH-现金, WECHAT-微信, ALIPAY-支付宝',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `cashier_id` bigint(20) NOT NULL COMMENT '收银员ID',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `points_discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '使用积分数量',
  `points_used` int(11) NOT NULL COMMENT '积分抵扣金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_table_id`(`table_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_cashier`(`cashier_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `FKpktxwhj3x9m4gth5ff6bkqgeb`(`member_id` ASC) USING BTREE,
  CONSTRAINT `FKpktxwhj3x9m4gth5ff6bkqgeb` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`table_id`) REFERENCES `restaurant_table` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'O202603180121487900', 1, 126.00, 2, 'PAID', 'WECHAT', '2026-03-18 01:24:24', 2, '', '2026-03-18 01:21:48', '2026-03-18 01:24:24', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (2, 'O202603180132317409', 9, 157.00, 6, 'PAID', 'ALIPAY', '2026-03-18 01:46:11', 2, '可乐要冰镇的', '2026-03-18 01:32:31', '2026-03-18 01:46:11', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (3, 'O202603180158258681', 1, 103.00, 2, 'PAID', 'WECHAT', '2026-03-18 01:59:36', 2, '', '2026-03-18 01:58:26', '2026-03-18 01:59:36', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (4, 'O202603180158437694', 2, 65.00, 2, 'PAID', 'CASH', '2026-03-18 01:59:28', 2, '', '2026-03-18 01:58:44', '2026-03-18 01:59:28', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (5, 'O202603180216336129', 11, 83.00, 2, 'PAID', 'WECHAT', '2026-03-18 02:16:48', 2, '', '2026-03-18 02:16:33', '2026-03-18 02:16:48', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (6, 'O202603180217185154', 3, 76.00, 2, 'PAID', 'ALIPAY', '2026-03-18 02:17:28', 2, '可乐要冰镇的', '2026-03-18 02:17:18', '2026-03-18 02:17:28', 0, NULL, NULL, 0);
INSERT INTO `orders` VALUES (7, 'O202603180306285381', 1, 64.50, 1, 'PAID', 'WECHAT', '2026-03-18 03:06:40', 3, '', '2026-03-18 03:06:29', '2026-03-18 03:06:40', 0, 1, 0.50, 50);
INSERT INTO `orders` VALUES (8, 'O202603191319347156', 1, 100.00, 2, 'PAID', 'WECHAT', '2026-03-19 13:19:43', 2, '', '2026-03-19 13:19:34', '2026-03-19 13:19:43', 0, NULL, 0.00, 0);

-- ----------------------------
-- Table structure for restaurant_table
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_table`;
CREATE TABLE `restaurant_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '桌号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '桌台名称',
  `capacity` int(11) NOT NULL DEFAULT 4 COMMENT '容纳人数',
  `area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域(大厅/包间等)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'FREE' COMMENT '状态: FREE-空闲, OCCUPIED-占用',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_no`(`table_no` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_table_no`(`table_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '桌台表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of restaurant_table
-- ----------------------------
INSERT INTO `restaurant_table` VALUES (1, 'A01', '大厅1号桌', 4, '大厅', 'FREE', 1, '2026-03-17 23:19:38', '2026-03-19 13:19:43', 0);
INSERT INTO `restaurant_table` VALUES (2, 'A02', '大厅2号桌', 4, '大厅', 'FREE', 2, '2026-03-17 23:19:38', '2026-03-18 01:59:28', 0);
INSERT INTO `restaurant_table` VALUES (3, 'A03', '大厅3号桌', 4, '大厅', 'FREE', 3, '2026-03-17 23:19:38', '2026-03-18 02:17:28', 0);
INSERT INTO `restaurant_table` VALUES (4, 'A04', '大厅4号桌', 6, '大厅', 'FREE', 4, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (5, 'A05', '大厅5号桌', 6, '大厅', 'FREE', 5, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (6, 'A06', '大厅6号桌', 8, '大厅', 'FREE', 6, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (7, 'B01', '包间1号', 6, '包间', 'FREE', 7, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (8, 'B02', '包间2号', 8, '包间', 'FREE', 8, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (9, 'B03', '包间3号', 10, '包间', 'FREE', 9, '2026-03-17 23:19:38', '2026-03-18 01:46:11', 0);
INSERT INTO `restaurant_table` VALUES (10, 'B04', '包间4号', 12, '包间', 'FREE', 10, '2026-03-17 23:19:38', '2026-03-17 23:19:38', 0);
INSERT INTO `restaurant_table` VALUES (11, 'C01', '靠窗一号桌', 4, '大厅', 'FREE', 11, '2026-03-18 02:02:47', '2026-03-18 02:16:48', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'CASHIER' COMMENT '角色: ADMIN-管理员, CASHIER-收银员',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ENABLED' COMMENT '状态: ENABLED-启用, DISABLED-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_role`(`role` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$3CyPwsWEFg3SVULXrOvBc.DRfAUJhttqBN395qlUqhdhqi72HPZI2', '系统管理员', '13800138000', 'ADMIN', 'ENABLED', '2026-03-17 23:19:38', '2026-03-18 00:19:58', 0);
INSERT INTO `sys_user` VALUES (2, 'user', '$2a$10$nOyZzXJjEyHBPhQv5JzOjOrGB/233nUapQ/octQQjBS6EWTQIiBje', '黎明', '13111111111', 'CASHIER', 'ENABLED', '2026-03-18 01:15:04', '2026-03-18 02:00:50', 0);
INSERT INTO `sys_user` VALUES (3, 'user2', '$2a$10$5NUeEar0HkjLjkPIRU.7yuP8EvarUduEvTMB79X6tmHAv1j3zjdpe', '彭于晏', '13111111112', 'CASHIER', 'ENABLED', '2026-03-18 03:00:54', '2026-03-18 03:00:54', 0);

SET FOREIGN_KEY_CHECKS = 1;
