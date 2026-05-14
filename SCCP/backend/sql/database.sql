-- 家校沟通平台数据库设计
-- 数据库: school_communication

CREATE DATABASE IF NOT EXISTS school_communication DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE school_communication;

-- 1. 用户表 (家长、教师、管理员)
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role ENUM('PARENT', 'TEACHER', 'ADMIN') NOT NULL COMMENT '角色:家长/教师/管理员',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role (role),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 班级表
CREATE TABLE class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称(如:一年级1班)',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    description VARCHAR(255) COMMENT '班级描述',
    head_teacher_id BIGINT COMMENT '班主任ID(教师ID)',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (head_teacher_id) REFERENCES sys_user(id),
    INDEX idx_grade (grade),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 3. 学生表
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    student_name VARCHAR(50) NOT NULL COMMENT '学生姓名',
    student_no VARCHAR(30) UNIQUE COMMENT '学号',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    parent_id BIGINT NOT NULL COMMENT '家长ID',
    gender TINYINT COMMENT '性别:0-女,1-男',
    birth_date DATE COMMENT '出生日期',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (class_id) REFERENCES class(id),
    FOREIGN KEY (parent_id) REFERENCES sys_user(id),
    INDEX idx_class_id (class_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 4. 教师-班级关联表(一个教师可教多个班级)
CREATE TABLE teacher_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    subject VARCHAR(30) COMMENT '任教科目',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (teacher_id) REFERENCES sys_user(id),
    FOREIGN KEY (class_id) REFERENCES class(id),
    UNIQUE KEY uk_teacher_class (teacher_id, class_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_class_id (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师班级关联表';

-- 5. 通知表
CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    publisher_id BIGINT NOT NULL COMMENT '发布人ID',
    target_type ENUM('ALL', 'CLASS', 'PARENT') NOT NULL COMMENT '目标类型:全部/班级/家长',
    target_ids VARCHAR(500) COMMENT '目标ID列表(逗号分隔,如班级ID或家长ID)',
    priority TINYINT DEFAULT 0 COMMENT '优先级:0-普通,1-重要,2-紧急',
    read_count INT DEFAULT 0 COMMENT '已读人数',
    status TINYINT DEFAULT 1 COMMENT '状态:0-草稿,1-已发布,2-已撤回',
    publish_time DATETIME COMMENT '发布时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (publisher_id) REFERENCES sys_user(id),
    INDEX idx_publisher (publisher_id),
    INDEX idx_target_type (target_type),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 6. 通知已读记录表
CREATE TABLE notice_read (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    notice_id BIGINT NOT NULL COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    read_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
    FOREIGN KEY (notice_id) REFERENCES notice(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    UNIQUE KEY uk_notice_user (notice_id, user_id),
    INDEX idx_notice_id (notice_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知已读记录表';

-- 7. 作业表
CREATE TABLE homework (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '作业ID',
    title VARCHAR(100) NOT NULL COMMENT '作业标题',
    content TEXT NOT NULL COMMENT '作业内容',
    subject VARCHAR(30) NOT NULL COMMENT '科目',
    teacher_id BIGINT NOT NULL COMMENT '布置教师ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    attachment_url VARCHAR(255) COMMENT '附件URL',
    deadline DATETIME NOT NULL COMMENT '截止时间',
    status TINYINT DEFAULT 1 COMMENT '状态:0-已取消,1-进行中,2-已截止',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (teacher_id) REFERENCES sys_user(id),
    FOREIGN KEY (class_id) REFERENCES class(id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_class_id (class_id),
    INDEX idx_deadline (deadline),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业表';

-- 8. 作业提交表
CREATE TABLE homework_submit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '提交ID',
    homework_id BIGINT NOT NULL COMMENT '作业ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    content TEXT COMMENT '提交内容',
    attachment_url VARCHAR(255) COMMENT '附件URL',
    submit_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    score DECIMAL(5,2) COMMENT '得分',
    comment VARCHAR(500) COMMENT '教师评语',
    status TINYINT DEFAULT 0 COMMENT '状态:0-未提交,1-已提交,2-已批改',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (homework_id) REFERENCES homework(id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    UNIQUE KEY uk_homework_student (homework_id, student_id),
    INDEX idx_homework_id (homework_id),
    INDEX idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交表';

-- 9. 成绩表
CREATE TABLE score (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    exam_name VARCHAR(100) NOT NULL COMMENT '考试名称',
    subject VARCHAR(30) NOT NULL COMMENT '科目',
    score DECIMAL(5,2) NOT NULL COMMENT '分数',
    full_score DECIMAL(5,2) DEFAULT 100 COMMENT '满分',
    teacher_id BIGINT NOT NULL COMMENT '录入教师ID',
    exam_date DATE NOT NULL COMMENT '考试日期',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(id),
    INDEX idx_student_id (student_id),
    INDEX idx_exam_name (exam_name),
    INDEX idx_exam_date (exam_date),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 10. 请假申请表
CREATE TABLE leave_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '请假ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    parent_id BIGINT NOT NULL COMMENT '申请人(家长)ID',
    leave_type ENUM('SICK', 'PERSONAL', 'OTHER') NOT NULL COMMENT '请假类型:病假/事假/其他',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    days INT NOT NULL COMMENT '请假天数',
    reason VARCHAR(500) NOT NULL COMMENT '请假原因',
    attachment_url VARCHAR(255) COMMENT '附件(如病假条)',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '状态:待审批/已通过/已拒绝',
    approver_id BIGINT COMMENT '审批人ID',
    approve_time DATETIME COMMENT '审批时间',
    approve_remark VARCHAR(255) COMMENT '审批备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0-未删除,1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (parent_id) REFERENCES sys_user(id),
    FOREIGN KEY (approver_id) REFERENCES sys_user(id),
    INDEX idx_student_id (student_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- 插入初始管理员账号 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, phone, role, status, deleted) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '系统管理员', '13800138000', 'ADMIN', 1, 0);

-- 密码说明: admin123 经过BCrypt加密后的值