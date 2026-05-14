-- 用户账户表（存储用户余额）
CREATE TABLE IF NOT EXISTS user_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL UNIQUE COMMENT '学生ID',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    total_recharge DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计充值金额',
    total_consumption DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计消费金额',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';

-- 充值记录表
-- 状态说明：0-待支付，1-已支付成功
CREATE TABLE IF NOT EXISTS recharge_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    recharge_no VARCHAR(50) NOT NULL UNIQUE COMMENT '充值编号',
    amount DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    balance_before DECIMAL(10,2) NOT NULL COMMENT '充值前余额',
    balance_after DECIMAL(10,2) NOT NULL COMMENT '充值后余额',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待支付，1-已支付成功',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_student_id (student_id),
    INDEX idx_recharge_no (recharge_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- 消费记录表（用于记录登录扣费等）
-- 类型说明：1-登录扣费，2-电费扣款
CREATE TABLE IF NOT EXISTS consumption_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    consumption_no VARCHAR(50) NOT NULL UNIQUE COMMENT '消费编号',
    amount DECIMAL(10,2) NOT NULL COMMENT '消费金额',
    type TINYINT NOT NULL COMMENT '消费类型：1-登录扣费，2-电费扣款',
    description VARCHAR(255) DEFAULT NULL COMMENT '消费描述',
    balance_before DECIMAL(10,2) NOT NULL COMMENT '消费前余额',
    balance_after DECIMAL(10,2) NOT NULL COMMENT '消费后余额',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_student_id (student_id),
    INDEX idx_consumption_no (consumption_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费记录表';

-- 为已存在的用户创建账户记录（赠送100元初始金额）
INSERT INTO user_account (student_id, balance, total_recharge, total_consumption)
SELECT id, 100.00, 100.00, 0.00 FROM sys_user WHERE role = 0 AND id NOT IN (SELECT student_id FROM user_account);
