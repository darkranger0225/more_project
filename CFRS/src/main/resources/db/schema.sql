-- 校园食堂菜品评价系统数据库脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS canteen_review CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE canteen_review;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` VARCHAR(20) NOT NULL COMMENT '学号',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role` INT DEFAULT 0 COMMENT '角色：0-学生，1-管理员',
    `status` INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_id` (`student_id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 窗口表
CREATE TABLE IF NOT EXISTS `window` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '窗口名称',
    `floor` INT NOT NULL COMMENT '楼层：1-一楼，2-二楼',
    `window_number` INT NOT NULL COMMENT '窗口编号',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '窗口描述',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '窗口图片',
    `status` INT DEFAULT 1 COMMENT '状态：0-休息中，1-营业中',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_floor` (`floor`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗口表';

-- 菜品表
CREATE TABLE IF NOT EXISTS `dish` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
    `window_id` BIGINT NOT NULL COMMENT '所属窗口ID',
    `category` VARCHAR(20) NOT NULL COMMENT '分类：早餐/午餐/晚餐',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '菜品描述',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '菜品图片',
    `average_rating` DECIMAL(2,1) DEFAULT 0.0 COMMENT '平均评分',
    `review_count` INT DEFAULT 0 COMMENT '评价数量',
    `status` INT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_window_id` (`window_id`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_average_rating` (`average_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品表';

-- 评价表
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
    `rating` DECIMAL(2,1) NOT NULL COMMENT '评分：0.5-5',
    `content` VARCHAR(200) DEFAULT NULL COMMENT '评价内容',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '评价图片',
    `is_anonymous` INT DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` INT DEFAULT 1 COMMENT '状态：0-待审核，1-已通过，2-已拒绝',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_dish_id` (`dish_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(30) NOT NULL COMMENT '标签名称',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 菜品标签关联表
CREATE TABLE IF NOT EXISTS `dish_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dish_tag` (`dish_id`, `tag_id`),
    KEY `idx_dish_id` (`dish_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品标签关联表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `target_type` INT NOT NULL COMMENT '收藏类型：1-菜品，2-窗口',
    `target_id` BIGINT NOT NULL COMMENT '目标ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 通知表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '发送通知的用户ID',
    `type` INT NOT NULL COMMENT '通知类型：1-系统通知，2-活动通知',
    `content` VARCHAR(500) NOT NULL COMMENT '通知内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';
