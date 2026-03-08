-- 修复角色菜单权限数据
-- 清空现有角色菜单关联
DELETE FROM sys_role_menu;

-- 超级管理员(ROLE000001)：拥有所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000001', menu_id FROM sys_menu;

-- 业务管理员(ROLE000002)：只有系统管理菜单 (MENU000001及其子菜单)
-- MENU000001 = 系统管理（一级菜单）
-- MENU000011, 012, 013, 014 = 二级菜单
-- MENU000111-144 = 按钮权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000002', menu_id FROM sys_menu 
WHERE menu_id LIKE 'MENU0001%' OR menu_id = 'MENU000001';

-- 客户管理员(ROLE000003)：只有客户管理菜单 (MENU000003及其子菜单)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000003', menu_id FROM sys_menu 
WHERE menu_id LIKE 'MENU0003%' OR menu_id = 'MENU000003';

-- 账户管理员(ROLE000004)：只有账户管理菜单 (MENU000004及其子菜单)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000004', menu_id FROM sys_menu 
WHERE menu_id LIKE 'MENU0004%' OR menu_id = 'MENU000004';

-- 验证结果
SELECT r.role_name, COUNT(rm.menu_id) as menu_count 
FROM sys_role r 
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id 
GROUP BY r.role_id, r.role_name;
