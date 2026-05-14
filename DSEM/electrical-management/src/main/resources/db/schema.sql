-- 创建数据库
CREATE DATABASE IF NOT EXISTS electrical_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE electrical_db;

-- 用户表（统一存储学生、公寓管理员、系统管理员）
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    account VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    role TINYINT NOT NULL DEFAULT 0 COMMENT '角色：0-学生，1-公寓管理员，2-系统管理员',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    age INT DEFAULT NULL COMMENT '年龄',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    photo VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    floor_id BIGINT DEFAULT NULL COMMENT '所属楼层ID（学生和管理员）',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_account (account),
    INDEX idx_role (role),
    INDEX idx_floor_id (floor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 楼层信息表
CREATE TABLE IF NOT EXISTS floor_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    floor_number VARCHAR(20) NOT NULL COMMENT '楼层号',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_floor_number (floor_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼层信息表';

-- 用电记录表
CREATE TABLE IF NOT EXISTS electricity_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    floor_id BIGINT NOT NULL COMMENT '楼层ID',
    usage_amount DECIMAL(10,2) NOT NULL COMMENT '用电量（度）',
    amount DECIMAL(10,2) NOT NULL COMMENT '应缴金额',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '缴费后余额',
    record_time DATETIME NOT NULL COMMENT '记录时间',
    status TINYINT DEFAULT 0 COMMENT '状态：0-未缴费，1-已缴费',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_student_id (student_id),
    INDEX idx_floor_id (floor_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用电记录表';

-- 缴费单表
CREATE TABLE IF NOT EXISTS payment_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    bill_no VARCHAR(50) NOT NULL UNIQUE COMMENT '账单编号',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    usage_id BIGINT NOT NULL COMMENT '用电记录ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '缴费金额',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待支付，1-已支付',
    payment_time DATETIME DEFAULT NULL COMMENT '支付时间',
    payment_method VARCHAR(20) DEFAULT NULL COMMENT '支付方式',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_bill_no (bill_no),
    INDEX idx_student_id (student_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费单表';

-- 报修记录表
CREATE TABLE IF NOT EXISTS repair_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '报修标题',
    content TEXT NOT NULL COMMENT '报修内容',
    image VARCHAR(500) DEFAULT NULL COMMENT '图片URL（多个用逗号分隔）',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完成',
    reply TEXT DEFAULT NULL COMMENT '处理回复',
    handler_id BIGINT DEFAULT NULL COMMENT '处理人ID',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_student_id (student_id),
    INDEX idx_status (status),
    INDEX idx_handler_id (handler_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修记录表';

-- 公告表
CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    image VARCHAR(500) DEFAULT NULL COMMENT '图片URL',
    publisher_id BIGINT NOT NULL COMMENT '发布人ID',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_publisher_id (publisher_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    config_key VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
    config_value VARCHAR(255) NOT NULL COMMENT '配置值',
    description VARCHAR(255) DEFAULT NULL COMMENT '配置说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 插入默认数据

-- 插入系统管理员（密码：admin123）
INSERT INTO sys_user (account, password, name, role, gender, phone, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '系统管理员', 2, 1, '13800138000', 1);

-- 插入楼层数据
INSERT INTO floor_info (floor_number, description) VALUES
('1号楼-1层', '一号楼第一层'),
('1号楼-2层', '一号楼第二层'),
('1号楼-3层', '一号楼第三层'),
('2号楼-1层', '二号楼第一层'),
('2号楼-2层', '二号楼第二层'),
('2号楼-3层', '二号楼第三层');

-- 插入系统配置（电费单价：0.6元/度）
INSERT INTO system_config (config_key, config_value, description) VALUES
('electricity_price', '0.6', '电费单价（元/度）'),
('system_name', '学生公寓电费信息管理系统', '系统名称'),
('contact_phone', '400-123-4567', '客服电话');
