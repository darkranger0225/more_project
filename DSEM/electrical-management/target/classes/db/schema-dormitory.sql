-- 修改现有的楼层表，添加楼栋关联
-- 先检查列是否存在，如果不存在则添加
SET @dbname = DATABASE();
SET @tablename = 'floor_info';
SET @columnname = 'building_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = @dbname
        AND TABLE_NAME = @tablename
        AND COLUMN_NAME = @columnname
    ) > 0,
    'SELECT "Column already exists"',
    'ALTER TABLE floor_info ADD COLUMN building_id BIGINT COMMENT "楼栋ID" AFTER id'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 删除旧的唯一约束（如果存在），因为不同楼栋可以有相同楼层号
SET @dropConstraint = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = @dbname
        AND TABLE_NAME = 'floor_info'
        AND CONSTRAINT_NAME = 'uk_floor_number'
    ) > 0,
    'ALTER TABLE floor_info DROP INDEX uk_floor_number',
    'SELECT "Constraint does not exist"'
));
PREPARE dropIfExists FROM @dropConstraint;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- 添加新的联合唯一约束（楼栋ID + 楼层号）
SET @addConstraint = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = @dbname
        AND TABLE_NAME = 'floor_info'
        AND CONSTRAINT_NAME = 'uk_building_floor'
    ) = 0,
    'ALTER TABLE floor_info ADD UNIQUE KEY uk_building_floor (building_id, floor_number)',
    'SELECT "Constraint already exists"'
));
PREPARE addIfNotExists FROM @addConstraint;
EXECUTE addIfNotExists;
DEALLOCATE PREPARE addIfNotExists;

-- 删除旧的楼层数据（building_id 为 NULL 或 0 的数据）
-- 这些数据是之前初始化时插入的，现在已被新的数据替代
DELETE FROM floor_info WHERE building_id IS NULL OR building_id = 0;

-- 创建宿舍表
CREATE TABLE IF NOT EXISTS dormitory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    building_id BIGINT NOT NULL COMMENT '楼栋ID',
    floor_id BIGINT NOT NULL COMMENT '楼层ID',
    dormitory_number VARCHAR(10) NOT NULL COMMENT '宿舍号',
    description VARCHAR(200) COMMENT '描述',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_building_id (building_id),
    INDEX idx_floor_id (floor_id),
    INDEX idx_dormitory_number (dormitory_number),
    UNIQUE KEY uk_building_floor_dormitory (building_id, floor_id, dormitory_number, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍表';

-- 修改用户表，添加宿舍字段
SET @tablename2 = 'sys_user';
SET @columnname2 = 'dormitory_id';
SET @preparedStatement2 = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = @dbname
        AND TABLE_NAME = @tablename2
        AND COLUMN_NAME = @columnname2
    ) > 0,
    'SELECT "Column already exists"',
    'ALTER TABLE sys_user ADD COLUMN dormitory_id BIGINT COMMENT "宿舍ID" AFTER floor_id'
));
PREPARE alterIfNotExists2 FROM @preparedStatement2;
EXECUTE alterIfNotExists2;
DEALLOCATE PREPARE alterIfNotExists2;

-- 修改用电记录表，添加宿舍字段
SET @tablename3 = 'electricity_usage';
SET @columnname3 = 'dormitory_id';
SET @preparedStatement3 = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = @dbname
        AND TABLE_NAME = @tablename3
        AND COLUMN_NAME = @columnname3
    ) > 0,
    'SELECT "Column already exists"',
    'ALTER TABLE electricity_usage ADD COLUMN dormitory_id BIGINT COMMENT "宿舍ID" AFTER floor_id'
));
PREPARE alterIfNotExists3 FROM @preparedStatement3;
EXECUTE alterIfNotExists3;
DEALLOCATE PREPARE alterIfNotExists3;

-- 清理并重新初始化楼层数据（关联楼栋）
-- 先清空现有数据（如果有的话）
-- DELETE FROM floor_info WHERE building_id IS NOT NULL;

-- 添加楼栋数据到楼层表（用楼层表存储楼栋信息，building_id为0表示楼栋）
-- 这里我们修改策略：楼层表存储所有楼层，通过building_id区分不同楼栋
-- 使用 INSERT IGNORE 忽略重复数据
INSERT IGNORE INTO floor_info (building_id, floor_number, description) VALUES 
(1, '1层', '一栋1层'), (1, '2层', '一栋2层'), (1, '3层', '一栋3层'), 
(1, '4层', '一栋4层'), (1, '5层', '一栋5层'), (1, '6层', '一栋6层'),
(2, '1层', '二栋1层'), (2, '2层', '二栋2层'), (2, '3层', '二栋3层'), 
(2, '4层', '二栋4层'), (2, '5层', '二栋5层'), (2, '6层', '二栋6层'),
(3, '1层', '三栋1层'), (3, '2层', '三栋2层'), (3, '3层', '三栋3层'), 
(3, '4层', '三栋4层'), (3, '5层', '三栋5层'), (3, '6层', '三栋6层');

-- 添加宿舍数据（使用子查询动态获取楼层ID）
-- 1栋宿舍
INSERT IGNORE INTO dormitory (building_id, floor_id, dormitory_number)
SELECT 1, id, '101' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '102' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '103' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '104' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '105' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '106' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '107' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '108' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '109' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '110' FROM floor_info WHERE building_id = 1 AND floor_number = '1层' UNION ALL
SELECT 1, id, '201' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '202' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '203' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '204' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '205' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '206' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '207' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '208' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '209' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '210' FROM floor_info WHERE building_id = 1 AND floor_number = '2层' UNION ALL
SELECT 1, id, '301' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '302' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '303' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '304' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '305' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '306' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '307' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '308' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '309' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '310' FROM floor_info WHERE building_id = 1 AND floor_number = '3层' UNION ALL
SELECT 1, id, '401' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '402' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '403' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '404' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '405' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '406' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '407' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '408' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '409' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '410' FROM floor_info WHERE building_id = 1 AND floor_number = '4层' UNION ALL
SELECT 1, id, '501' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '502' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '503' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '504' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '505' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '506' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '507' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '508' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '509' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '510' FROM floor_info WHERE building_id = 1 AND floor_number = '5层' UNION ALL
SELECT 1, id, '601' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '602' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '603' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '604' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '605' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '606' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '607' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '608' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '609' FROM floor_info WHERE building_id = 1 AND floor_number = '6层' UNION ALL
SELECT 1, id, '610' FROM floor_info WHERE building_id = 1 AND floor_number = '6层';

-- 2栋宿舍
INSERT IGNORE INTO dormitory (building_id, floor_id, dormitory_number)
SELECT 2, id, '101' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '102' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '103' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '104' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '105' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '106' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '107' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '108' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '109' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '110' FROM floor_info WHERE building_id = 2 AND floor_number = '1层' UNION ALL
SELECT 2, id, '201' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '202' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '203' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '204' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '205' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '206' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '207' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '208' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '209' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '210' FROM floor_info WHERE building_id = 2 AND floor_number = '2层' UNION ALL
SELECT 2, id, '301' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '302' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '303' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '304' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '305' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '306' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '307' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '308' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '309' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '310' FROM floor_info WHERE building_id = 2 AND floor_number = '3层' UNION ALL
SELECT 2, id, '401' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '402' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '403' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '404' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '405' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '406' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '407' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '408' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '409' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '410' FROM floor_info WHERE building_id = 2 AND floor_number = '4层' UNION ALL
SELECT 2, id, '501' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '502' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '503' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '504' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '505' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '506' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '507' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '508' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '509' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '510' FROM floor_info WHERE building_id = 2 AND floor_number = '5层' UNION ALL
SELECT 2, id, '601' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '602' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '603' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '604' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '605' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '606' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '607' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '608' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '609' FROM floor_info WHERE building_id = 2 AND floor_number = '6层' UNION ALL
SELECT 2, id, '610' FROM floor_info WHERE building_id = 2 AND floor_number = '6层';

-- 3栋宿舍
INSERT IGNORE INTO dormitory (building_id, floor_id, dormitory_number)
SELECT 3, id, '101' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '102' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '103' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '104' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '105' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '106' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '107' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '108' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '109' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '110' FROM floor_info WHERE building_id = 3 AND floor_number = '1层' UNION ALL
SELECT 3, id, '201' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '202' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '203' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '204' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '205' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '206' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '207' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '208' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '209' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '210' FROM floor_info WHERE building_id = 3 AND floor_number = '2层' UNION ALL
SELECT 3, id, '301' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '302' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '303' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '304' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '305' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '306' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '307' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '308' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '309' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '310' FROM floor_info WHERE building_id = 3 AND floor_number = '3层' UNION ALL
SELECT 3, id, '401' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '402' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '403' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '404' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '405' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '406' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '407' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '408' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '409' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '410' FROM floor_info WHERE building_id = 3 AND floor_number = '4层' UNION ALL
SELECT 3, id, '501' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '502' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '503' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '504' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '505' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '506' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '507' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '508' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '509' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '510' FROM floor_info WHERE building_id = 3 AND floor_number = '5层' UNION ALL
SELECT 3, id, '601' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '602' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '603' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '604' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '605' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '606' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '607' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '608' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '609' FROM floor_info WHERE building_id = 3 AND floor_number = '6层' UNION ALL
SELECT 3, id, '610' FROM floor_info WHERE building_id = 3 AND floor_number = '6层';
