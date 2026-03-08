-- =====================================================
-- Complete Database Schema Fix Script for Phase 2
-- Version: v1.0.0
-- Date: 2026-03-07
-- Purpose: Fix all database schema mismatches with backend code
-- =====================================================

USE cust;

-- =====================================================
-- 1. Fix sys_menu table structure
-- =====================================================

-- Rename parent_id to parent_menu_id
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'parent_menu_id');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu CHANGE COLUMN parent_id parent_menu_id VARCHAR(32) DEFAULT NULL COMMENT ''Parent Menu ID''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Rename permission to perms
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'perms');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu CHANGE COLUMN permission perms VARCHAR(128) DEFAULT NULL COMMENT ''Permission''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Rename route_path to menu_url
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'menu_url');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu CHANGE COLUMN route_path menu_url VARCHAR(256) DEFAULT NULL COMMENT ''Menu URL''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add visible column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'visible');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu ADD COLUMN visible TINYINT NOT NULL DEFAULT 1 COMMENT ''Visible'' AFTER sort_order', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'created_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu ADD COLUMN created_by VARCHAR(32) NOT NULL DEFAULT ''system'' COMMENT ''Created By'' AFTER visible', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Rename create_time to created_time
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'created_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu CHANGE COLUMN create_time created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''Created Time''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_by column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'updated_by');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu ADD COLUMN updated_by VARCHAR(32) DEFAULT NULL COMMENT ''Updated By'' AFTER created_time', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Rename update_time to updated_time
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'updated_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu CHANGE COLUMN update_time updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''Updated Time''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add deleted column
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'deleted');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT ''Deleted Flag''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Drop component column if exists (not needed)
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'component');
SET @sql = IF(@col_exists > 0, 'ALTER TABLE sys_menu DROP COLUMN component', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Drop menu_status column if exists (use visible instead)
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu' AND column_name = 'menu_status');
SET @sql = IF(@col_exists > 0, 'ALTER TABLE sys_menu DROP COLUMN menu_status', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Update existing data
UPDATE sys_menu SET deleted = 0 WHERE deleted IS NULL;
UPDATE sys_menu SET created_by = 'system' WHERE created_by IS NULL OR created_by = '';
UPDATE sys_menu SET visible = 1 WHERE visible IS NULL;

-- =====================================================
-- 2. Fix sys_role_menu to sys_menu_role (rename table)
-- =====================================================

-- Check if sys_menu_role exists, if not rename sys_role_menu
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
                     WHERE table_schema = 'cust' AND table_name = 'sys_menu_role');
SET @sql = IF(@table_exists = 0, 'RENAME TABLE sys_role_menu TO sys_menu_role', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_time to sys_menu_role if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu_role' AND column_name = 'created_time');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu_role ADD COLUMN created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''Created Time''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add id column to sys_menu_role if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.columns 
                   WHERE table_schema = 'cust' AND table_name = 'sys_menu_role' AND column_name = 'id');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE sys_menu_role ADD COLUMN id BIGINT PRIMARY KEY AUTO_INCREMENT FIRST', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- =====================================================
-- 3. Insert menu_role data for admin role (ROLE000001)
-- =====================================================

-- Insert all menus for admin role
INSERT IGNORE INTO sys_menu_role (menu_id, role_id, created_time)
SELECT menu_id, 'ROLE000001', NOW() FROM sys_menu WHERE deleted = 0;

-- =====================================================
-- Schema fix completed
-- =====================================================
