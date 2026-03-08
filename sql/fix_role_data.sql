-- Fix sys_role table structure using dynamic SQL

-- Add role_desc column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_role' AND column_name = 'role_desc');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_role ADD COLUMN role_desc VARCHAR(256) DEFAULT NULL COMMENT ''Role Description''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_role' AND column_name = 'created_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_role ADD COLUMN created_by VARCHAR(32) NOT NULL DEFAULT ''system'' COMMENT ''Created By''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_role' AND column_name = 'updated_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_role ADD COLUMN updated_by VARCHAR(32) DEFAULT NULL COMMENT ''Updated By''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_time column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_role' AND column_name = 'updated_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_role ADD COLUMN updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT ''Updated Time''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add deleted column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_role' AND column_name = 'deleted');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_role ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT ''Deleted Flag''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Update admin user role_id from sys_user_role table
UPDATE sys_user u SET role_id = (SELECT role_id FROM sys_user_role WHERE user_id = u.user_id LIMIT 1) WHERE role_id IS NULL;
