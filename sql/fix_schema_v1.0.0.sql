-- =====================================================
-- Database Schema Fix Script for Phase 2
-- Version: v1.0.0
-- Date: 2026-03-07
-- Purpose: Fix database schema mismatch with backend code
-- =====================================================

USE cust;

-- =====================================================
-- 1. Fix sys_user table - Add missing columns
-- =====================================================

-- Add mobile column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'mobile');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_user ADD COLUMN mobile VARCHAR(20) DEFAULT NULL COMMENT ''Mobile'' AFTER password', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add email column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'email');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_user ADD COLUMN email VARCHAR(128) DEFAULT NULL COMMENT ''Email'' AFTER mobile', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add role_id column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'role_id');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_user ADD COLUMN role_id VARCHAR(32) DEFAULT NULL COMMENT ''Role ID'' AFTER org_id', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add last_login_time column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'last_login_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_user ADD COLUMN last_login_time DATETIME DEFAULT NULL COMMENT ''Last Login Time'' AFTER user_status', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add deleted column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_user' AND column_name = 'deleted');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_user ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT ''Deleted Flag''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Update existing data
UPDATE sys_user SET deleted = 0 WHERE deleted IS NULL;

-- =====================================================
-- 2. Fix sys_org table - Add missing columns
-- =====================================================

-- Rename parent_id to parent_org_id if needed
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'parent_org_id');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org CHANGE COLUMN parent_id parent_org_id VARCHAR(32) DEFAULT NULL COMMENT ''Parent Org ID''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add sort_order column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'sort_order');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN sort_order INT NOT NULL DEFAULT 0 COMMENT ''Sort Order'' AFTER org_status', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add remark column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'remark');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN remark VARCHAR(500) DEFAULT NULL COMMENT ''Remark'' AFTER sort_order', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'created_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN created_by VARCHAR(32) NOT NULL DEFAULT ''system'' COMMENT ''Created By'' AFTER remark', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_time column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'created_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''Created Time'' AFTER created_by', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'updated_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN updated_by VARCHAR(32) DEFAULT NULL COMMENT ''Updated By'' AFTER created_time', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_time column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'updated_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''Updated Time'' AFTER updated_by', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add deleted column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_org' AND column_name = 'deleted');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_org ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT ''Deleted Flag''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Update existing data
UPDATE sys_org SET deleted = 0 WHERE deleted IS NULL;
UPDATE sys_org SET created_by = 'system' WHERE created_by IS NULL OR created_by = '';

-- =====================================================
-- 3. Create sys_role table if not exists
-- =====================================================

CREATE TABLE IF NOT EXISTS sys_role (
    role_id VARCHAR(32) PRIMARY KEY COMMENT 'Role ID',
    role_code VARCHAR(32) NOT NULL UNIQUE COMMENT 'Role Code',
    role_name VARCHAR(64) NOT NULL COMMENT 'Role Name',
    role_desc VARCHAR(256) DEFAULT NULL COMMENT 'Role Description',
    role_status TINYINT NOT NULL DEFAULT 1 COMMENT 'Status',
    created_by VARCHAR(32) NOT NULL COMMENT 'Created By',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT 'Updated By',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT 'Deleted Flag',
    INDEX idx_role_status (role_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Role Table';

-- =====================================================
-- 4. Create sys_menu table if not exists
-- =====================================================

CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id VARCHAR(32) PRIMARY KEY COMMENT 'Menu ID',
    menu_name VARCHAR(64) NOT NULL COMMENT 'Menu Name',
    menu_type TINYINT NOT NULL COMMENT 'Type',
    parent_menu_id VARCHAR(32) DEFAULT NULL COMMENT 'Parent Menu ID',
    menu_url VARCHAR(256) DEFAULT NULL COMMENT 'Menu URL',
    menu_icon VARCHAR(64) DEFAULT NULL COMMENT 'Menu Icon',
    perms VARCHAR(128) DEFAULT NULL COMMENT 'Permission',
    sort_order INT NOT NULL DEFAULT 0 COMMENT 'Sort Order',
    visible TINYINT NOT NULL DEFAULT 1 COMMENT 'Visible',
    created_by VARCHAR(32) NOT NULL COMMENT 'Created By',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT 'Updated By',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT 'Deleted Flag',
    INDEX idx_parent_menu (parent_menu_id),
    INDEX idx_menu_type (menu_type),
    INDEX idx_perms (perms),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Menu Table';

-- =====================================================
-- 5. Create sys_user_role table if not exists
-- =====================================================

CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id VARCHAR(32) NOT NULL COMMENT 'User ID',
    role_id VARCHAR(32) NOT NULL COMMENT 'Role ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User Role Table';

-- =====================================================
-- 6. Create sys_menu_role table if not exists
-- =====================================================

CREATE TABLE IF NOT EXISTS sys_menu_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    menu_id VARCHAR(32) NOT NULL COMMENT 'Menu ID',
    role_id VARCHAR(32) NOT NULL COMMENT 'Role ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    UNIQUE KEY uk_menu_role (menu_id, role_id),
    INDEX idx_menu_id (menu_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Menu Role Table';

-- =====================================================
-- Schema fix completed
-- =====================================================
