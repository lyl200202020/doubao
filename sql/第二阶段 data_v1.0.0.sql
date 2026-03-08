-- =====================================================
-- 客户运营管理后台系统 - 第二阶段基础数据初始化脚本
-- 版本：v1.0.0
-- 创建日期：2026-03-08
-- 数据库：MySQL 8.0+
-- =====================================================

USE cust;

-- =====================================================
-- 1. 初始化机构数据
-- =====================================================
INSERT INTO sys_org (org_id, org_code, org_name, org_level, parent_org_id, org_status, sort_order, remark, created_by, created_time) VALUES
('1', 'HEAD', '总行', 0, NULL, 1, 0, '总行管理机构', 'system', NOW()),
('2', 'BJ_BRANCH', '北京分行', 1, '1', 1, 1, '北京分行', 'system', NOW()),
('3', 'SH_BRANCH', '上海分行', 1, '1', 1, 2, '上海分行', 'system', NOW()),
('4', 'BJ_SUB_01', '北京朝阳支行', 2, '2', 1, 1, '朝阳支行', 'system', NOW()),
('5', 'BJ_SUB_02', '北京海淀支行', 2, '2', 1, 2, '海淀支行', 'system', NOW()),
('6', 'SH_SUB_01', '上海浦东支行', 2, '3', 1, 1, '浦东支行', 'system', NOW());

-- =====================================================
-- 2. 初始化角色数据
-- =====================================================
INSERT INTO sys_role (role_id, role_code, role_name, role_desc, role_status, created_by, created_time) VALUES
('1', 'admin', '系统管理员', '拥有系统所有权限', 1, 'system', NOW()),
('2', 'org_admin', '机构管理员', '管理机构权限', 1, 'system', NOW()),
('3', 'user_admin', '用户管理员', '管理用户权限', 1, 'system', NOW()),
('4', 'menu_admin', '菜单管理员', '管理菜单权限', 1, 'system', NOW()),
('5', 'role_admin', '角色管理员', '管理角色权限', 1, 'system', NOW()),
('6', 'viewer', '普通用户', '只读权限', 1, 'system', NOW());

-- =====================================================
-- 3. 初始化菜单数据
-- =====================================================
-- 系统管理目录
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, menu_url, menu_icon, sort_order, visible, perms, created_by, created_time) 
VALUES ('1', '系统管理', 1, NULL, '/system', 'setting', 1, 1, NULL, 'system', NOW());

-- 机构管理菜单
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, menu_url, menu_icon, sort_order, visible, perms, created_by, created_time) 
VALUES ('2', '机构管理', 2, '1', '/system/org', 'org', 1, 1, 'sys:org:view', 'system', NOW());

-- 机构管理按钮
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('3', '新增机构', 3, '2', 1, 1, 'sys:org:add', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('4', '编辑机构', 3, '2', 2, 1, 'sys:org:edit', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('5', '删除机构', 3, '2', 3, 1, 'sys:org:delete', 'system', NOW());

-- 用户管理菜单
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, menu_url, menu_icon, sort_order, visible, perms, created_by, created_time) 
VALUES ('6', '用户管理', 2, '1', '/system/user', 'user', 2, 1, 'sys:user:view', 'system', NOW());

-- 用户管理按钮
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('7', '新增用户', 3, '6', 1, 1, 'sys:user:add', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('8', '编辑用户', 3, '6', 2, 1, 'sys:user:edit', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('9', '删除用户', 3, '6', 3, 1, 'sys:user:delete', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('10', '重置密码', 3, '6', 4, 1, 'sys:user:resetPwd', 'system', NOW());

-- 角色管理菜单
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, menu_url, menu_icon, sort_order, visible, perms, created_by, created_time) 
VALUES ('11', '角色管理', 2, '1', '/system/role', 'role', 3, 1, 'sys:role:view', 'system', NOW());

-- 角色管理按钮
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('12', '新增角色', 3, '11', 1, 1, 'sys:role:add', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('13', '编辑角色', 3, '11', 2, 1, 'sys:role:edit', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('14', '删除角色', 3, '11', 3, 1, 'sys:role:delete', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('15', '分配菜单', 3, '11', 4, 1, 'sys:role:assignMenu', 'system', NOW());

-- 菜单管理菜单
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, menu_url, menu_icon, sort_order, visible, perms, created_by, created_time) 
VALUES ('16', '菜单管理', 2, '1', '/system/menu', 'menu', 4, 1, 'sys:menu:view', 'system', NOW());

-- 菜单管理按钮
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('17', '新增菜单', 3, '16', 1, 1, 'sys:menu:add', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('18', '编辑菜单', 3, '16', 2, 1, 'sys:menu:edit', 'system', NOW());
INSERT INTO sys_menu (menu_id, menu_name, menu_type, parent_menu_id, sort_order, visible, perms, created_by, created_time) 
VALUES ('19', '删除菜单', 3, '16', 3, 1, 'sys:menu:delete', 'system', NOW());

-- =====================================================
-- 4. 初始化用户数据（密码：admin123，SM3 加密后的值）
-- =====================================================
-- 注意：实际密码需要使用 SM3 加密，这里使用占位符，实际部署时需要替换为真实 SM3 加密值
-- SM3("admin123") = 需要实际计算
INSERT INTO sys_user (user_id, user_code, user_name, password, mobile, email, org_id, role_id, auth_level, user_status, created_by, created_time) 
VALUES ('1', 'admin', '系统管理员', 'sm3_admin123_placeholder', '13800138000', 'admin@example.com', '1', '1', 16, 1, 'system', NOW());

-- =====================================================
-- 5. 初始化用户 - 角色关联
-- =====================================================
INSERT INTO sys_user_role (user_id, role_id) VALUES ('1', '1');

-- =====================================================
-- 6. 初始化菜单 - 角色关联（系统管理员拥有所有菜单权限）
-- =====================================================
INSERT INTO sys_menu_role (menu_id, role_id) VALUES
('1', '1'), ('2', '1'), ('3', '1'), ('4', '1'), ('5', '1'),
('6', '1'), ('7', '1'), ('8', '1'), ('9', '1'), ('10', '1'),
('11', '1'), ('12', '1'), ('13', '1'), ('14', '1'), ('15', '1'),
('16', '1'), ('17', '1'), ('18', '1'), ('19', '1');

-- =====================================================
-- 基础数据初始化完成
-- =====================================================
