-- 修复 sys_user 表结构
-- 问题：后端代码期望 user_code 字段，但数据库创建的是 username 字段

USE cust;

-- 方案 1：修改现有表结构（推荐）
-- 添加 user_code 字段
ALTER TABLE sys_user ADD COLUMN user_code VARCHAR(50) COMMENT '用户工号' AFTER user_id;

-- 将 username 的值复制到 user_code
UPDATE sys_user SET user_code = username WHERE user_code IS NULL;

-- 添加索引
ALTER TABLE sys_user ADD INDEX idx_user_code (user_code);

-- 添加唯一约束
ALTER TABLE sys_user ADD UNIQUE KEY uk_user_code (user_code);

-- 或者方案 2：如果不想保留 username 字段，可以重命名
-- ALTER TABLE sys_user CHANGE COLUMN username user_code VARCHAR(50) NOT NULL;

-- 验证修改
DESC sys_user;
SELECT user_id, user_code, username, real_name FROM sys_user;
