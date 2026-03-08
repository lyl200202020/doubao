-- =====================================================
-- Fix user_code column issue
-- Version: v1.0.0
-- Date: 2026-03-08
-- Purpose: Add user_code column and sync with username
-- =====================================================

USE cust;

-- Check if user_code column exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'user_code');

-- Add user_code column if not exists
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE sys_user ADD COLUMN user_code VARCHAR(32) DEFAULT NULL COMMENT ''User Code'' AFTER user_id', 
    'SELECT 1');
PREPARE stmt FROM @sql; 
EXECUTE stmt; 
DEALLOCATE PREPARE stmt;

-- Sync user_code from username
UPDATE sys_user SET user_code = username WHERE user_code IS NULL OR user_code = '';

-- Make user_code NOT NULL and UNIQUE
ALTER TABLE sys_user MODIFY COLUMN user_code VARCHAR(32) NOT NULL COMMENT 'User Code';
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_code ON sys_user(user_code);

-- Update admin password (SM3 hash of 'admin123')
-- SM3('admin123') = 207cf410532f92a47db2452543548c221b9e540243b1312128cd8dae2877ea72
UPDATE sys_user SET password = '207cf410532f92a47db2452543548c221b9e540243b1312128cd8dae2877ea72' WHERE user_code = 'admin';

-- Verify
SELECT user_id, user_code, username, real_name, password FROM sys_user WHERE user_code = 'admin';
