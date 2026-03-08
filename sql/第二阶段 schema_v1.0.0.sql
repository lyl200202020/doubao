-- =====================================================
-- 客户运营管理后台系统 - 第二阶段数据库表结构
-- 版本：v1.0.0
-- 创建日期：2026-03-08
-- 数据库：MySQL 8.0+
-- =====================================================

USE cust;

-- =====================================================
-- 1. 机构表（sys_org）
-- =====================================================
DROP TABLE IF EXISTS sys_org;
CREATE TABLE sys_org (
    org_id VARCHAR(32) PRIMARY KEY COMMENT '机构 ID（UUID）',
    org_code VARCHAR(32) NOT NULL UNIQUE COMMENT '机构代码（唯一）',
    org_name VARCHAR(128) NOT NULL COMMENT '机构名称',
    org_level TINYINT NOT NULL COMMENT '机构级别：0-总行，1-分行，2-支行，3-分理处',
    parent_org_id VARCHAR(32) DEFAULT NULL COMMENT '上级机构 ID（顶级为 NULL）',
    org_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_by VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    INDEX idx_parent_org (parent_org_id),
    INDEX idx_org_level (org_level),
    INDEX idx_org_status (org_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- =====================================================
-- 2. 用户表（sys_user）
-- =====================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id VARCHAR(32) PRIMARY KEY COMMENT '用户 ID（UUID）',
    user_code VARCHAR(32) NOT NULL UNIQUE COMMENT '用户工号（唯一）',
    user_name VARCHAR(64) NOT NULL COMMENT '用户姓名',
    password VARCHAR(128) NOT NULL COMMENT '密码（国密 SM3 加密）',
    mobile VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    org_id VARCHAR(32) NOT NULL COMMENT '所属机构 ID',
    role_id VARCHAR(32) NOT NULL COMMENT '所属角色 ID',
    auth_level TINYINT NOT NULL DEFAULT 1 COMMENT '授权级别（1-16 级）',
    user_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    created_by VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    INDEX idx_org_id (org_id),
    INDEX idx_role_id (role_id),
    INDEX idx_user_status (user_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =====================================================
-- 3. 角色表（sys_role）
-- =====================================================
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id VARCHAR(32) PRIMARY KEY COMMENT '角色 ID（UUID）',
    role_code VARCHAR(32) NOT NULL UNIQUE COMMENT '角色代码（唯一）',
    role_name VARCHAR(64) NOT NULL COMMENT '角色名称',
    role_desc VARCHAR(256) DEFAULT NULL COMMENT '角色描述',
    role_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    created_by VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    INDEX idx_role_status (role_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- =====================================================
-- 4. 菜单表（sys_menu）
-- =====================================================
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    menu_id VARCHAR(32) PRIMARY KEY COMMENT '菜单 ID（UUID）',
    menu_name VARCHAR(64) NOT NULL COMMENT '菜单名称',
    menu_type TINYINT NOT NULL COMMENT '类型：1-目录，2-菜单，3-按钮',
    parent_menu_id VARCHAR(32) DEFAULT NULL COMMENT '父菜单 ID（顶级为 NULL）',
    menu_url VARCHAR(256) DEFAULT NULL COMMENT '菜单 URL/路由地址',
    menu_icon VARCHAR(64) DEFAULT NULL COMMENT '菜单图标',
    perms VARCHAR(128) DEFAULT NULL COMMENT '权限标识（按钮用，如 sys:org:view）',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    visible TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见：0-隐藏，1-显示',
    created_by VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    INDEX idx_parent_menu (parent_menu_id),
    INDEX idx_menu_type (menu_type),
    INDEX idx_perms (perms),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- =====================================================
-- 5. 用户 - 角色关联表（sys_user_role）
-- =====================================================
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户 ID',
    role_id VARCHAR(32) NOT NULL COMMENT '角色 ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户 - 角色关联表';

-- =====================================================
-- 6. 菜单 - 角色关联表（sys_menu_role）
-- =====================================================
DROP TABLE IF EXISTS sys_menu_role;
CREATE TABLE sys_menu_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    menu_id VARCHAR(32) NOT NULL COMMENT '菜单 ID',
    role_id VARCHAR(32) NOT NULL COMMENT '角色 ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_menu_role (menu_id, role_id),
    INDEX idx_menu_id (menu_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单 - 角色关联表';

-- =====================================================
-- 表结构创建完成
-- =====================================================
