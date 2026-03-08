-- =====================================================
-- 客户运营管理后台系统 - 数据初始化SQL
-- 版本: v1.0.0
-- 日期: 2026-03-07
-- 说明: 初始化菜单、角色、机构、用户数据
-- =====================================================

-- 清空现有数据（按外键依赖顺序删除）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE sys_role_menu;
TRUNCATE TABLE sys_user_role;
TRUNCATE TABLE sys_user;
TRUNCATE TABLE sys_role;
TRUNCATE TABLE sys_menu;
TRUNCATE TABLE sys_org;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. 机构数据初始化
-- =====================================================

INSERT INTO sys_org (org_id, parent_id, org_code, org_name, org_level, org_status, create_time, update_time) VALUES
-- 总行
('ORG0000001', '0', '00000', '总行', 0, 1, NOW(), NOW()),

-- 分行
('ORG0000002', 'ORG0000001', '10000', '北京分行', 1, 1, NOW(), NOW()),
('ORG0000003', 'ORG0000001', '20000', '上海分行', 1, 1, NOW(), NOW()),

-- 支行
('ORG0000004', 'ORG0000002', '11000', '北京大红门支行', 2, 1, NOW(), NOW()),
('ORG0000005', 'ORG0000003', '21000', '上海虹桥支行', 2, 1, NOW(), NOW()),
('ORG0000006', 'ORG0000003', '22000', '上海闵行支行', 2, 1, NOW(), NOW()),
('ORG0000007', 'ORG0000003', '23000', '上海黄埔支行', 2, 1, NOW(), NOW()),

-- 分理处
('ORG0000008', 'ORG0000006', '22001', '上海闵行梅陇镇分理处', 3, 1, NOW(), NOW());

-- =====================================================
-- 2. 菜单数据初始化
-- =====================================================

INSERT INTO sys_menu (menu_id, parent_id, menu_name, menu_type, permission, route_path, component, menu_icon, sort_order, menu_status, create_time, update_time) VALUES

-- 一级菜单：系统管理
('MENU000001', '0', '系统管理', 1, NULL, '/sys', NULL, 'Setting', 1, 1, NOW(), NOW()),

-- 二级菜单：系统管理下
('MENU000011', 'MENU000001', '用户管理', 2, 'sys:user:list', '/sys/user', '/sys/user/index', 'User', 1, 1, NOW(), NOW()),
('MENU000012', 'MENU000001', '角色管理', 2, 'sys:role:list', '/sys/role', '/sys/role/index', 'UserFilled', 2, 1, NOW(), NOW()),
('MENU000013', 'MENU000001', '菜单管理', 2, 'sys:menu:list', '/sys/menu', '/sys/menu/index', 'Menu', 3, 1, NOW(), NOW()),
('MENU000014', 'MENU000001', '机构管理', 2, 'sys:org:list', '/sys/org', '/sys/org/index', 'OfficeBuilding', 4, 1, NOW(), NOW()),

-- 一级菜单：授权管理
('MENU000002', '0', '授权管理', 1, NULL, '/auth', NULL, 'Lock', 2, 1, NOW(), NOW()),

-- 二级菜单：授权管理下
('MENU000021', 'MENU000002', '待办任务', 2, 'auth:task:todo', '/auth/task/todo', '/auth/task/todo', 'List', 1, 1, NOW(), NOW()),
('MENU000022', 'MENU000002', '我的已办', 2, 'auth:task:done', '/auth/task/done', '/auth/task/done', 'Finished', 2, 1, NOW(), NOW()),
('MENU000023', 'MENU000002', '授权历史', 2, 'auth:history:list', '/auth/history', '/auth/history/index', 'Clock', 3, 1, NOW(), NOW()),

-- 一级菜单：客户管理
('MENU000003', '0', '客户管理', 1, NULL, '/crm', NULL, 'User', 3, 1, NOW(), NOW()),

-- 二级菜单：客户管理下
('MENU000031', 'MENU000003', '客户管理', 2, 'crm:customer:list', '/crm/customer', '/crm/customer/index', 'Avatar', 1, 1, NOW(), NOW()),
('MENU000032', 'MENU000003', '客户签约', 2, 'crm:contract:list', '/crm/contract', '/crm/contract/index', 'Document', 2, 1, NOW(), NOW()),

-- 一级菜单：账户管理
('MENU000004', '0', '账户管理', 1, NULL, '/account', NULL, 'Wallet', 4, 1, NOW(), NOW()),

-- 二级菜单：账户管理下
('MENU000041', 'MENU000004', '加挂账户', 2, 'account:add:list', '/account/add', '/account/add/index', 'Plus', 1, 1, NOW(), NOW()),
('MENU000042', 'MENU000004', '删除账户', 2, 'account:delete:list', '/account/delete', '/account/delete/index', 'Minus', 2, 1, NOW(), NOW()),

-- 按钮权限：用户管理
('MENU000111', 'MENU000011', '新增用户', 3, 'sys:user:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000112', 'MENU000011', '修改用户', 3, 'sys:user:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000113', 'MENU000011', '删除用户', 3, 'sys:user:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),
('MENU000114', 'MENU000011', '查看用户', 3, 'sys:user:view', NULL, NULL, NULL, 4, 1, NOW(), NOW()),

-- 按钮权限：角色管理
('MENU000121', 'MENU000012', '新增角色', 3, 'sys:role:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000122', 'MENU000012', '修改角色', 3, 'sys:role:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000123', 'MENU000012', '删除角色', 3, 'sys:role:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),
('MENU000124', 'MENU000012', '查看角色', 3, 'sys:role:view', NULL, NULL, NULL, 4, 1, NOW(), NOW()),

-- 按钮权限：菜单管理
('MENU000131', 'MENU000013', '新增菜单', 3, 'sys:menu:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000132', 'MENU000013', '修改菜单', 3, 'sys:menu:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000133', 'MENU000013', '删除菜单', 3, 'sys:menu:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),

-- 按钮权限：机构管理
('MENU000141', 'MENU000014', '新增机构', 3, 'sys:org:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000142', 'MENU000014', '修改机构', 3, 'sys:org:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000143', 'MENU000014', '删除机构', 3, 'sys:org:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),

-- 按钮权限：客户管理
('MENU000311', 'MENU000031', '新增客户', 3, 'crm:customer:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000312', 'MENU000031', '修改客户', 3, 'crm:customer:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000313', 'MENU000031', '删除客户', 3, 'crm:customer:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),

-- 按钮权限：客户签约
('MENU000321', 'MENU000032', '新增签约', 3, 'crm:contract:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000322', 'MENU000032', '修改签约', 3, 'crm:contract:edit', NULL, NULL, NULL, 2, 1, NOW(), NOW()),
('MENU000323', 'MENU000032', '删除签约', 3, 'crm:contract:delete', NULL, NULL, NULL, 3, 1, NOW(), NOW()),
('MENU000324', 'MENU000032', '提交授权', 3, 'crm:contract:submit', NULL, NULL, NULL, 4, 1, NOW(), NOW()),

-- 按钮权限：加挂账户
('MENU000411', 'MENU000041', '新增加挂', 3, 'account:add:add', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000412', 'MENU000041', '审批加挂', 3, 'account:add:approve', NULL, NULL, NULL, 2, 1, NOW(), NOW()),

-- 按钮权限：删除账户
('MENU000421', 'MENU000042', '申请删除', 3, 'account:delete:apply', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
('MENU000422', 'MENU000042', '审批删除', 3, 'account:delete:approve', NULL, NULL, NULL, 2, 1, NOW(), NOW());

-- =====================================================
-- 3. 角色数据初始化
-- =====================================================

INSERT INTO sys_role (role_id, role_code, role_name, role_status, create_time, update_time) VALUES
('ROLE000001', 'ADMIN', '超级管理员', 1, NOW(), NOW()),
('ROLE000002', 'YWGL', '业务管理员', 1, NOW(), NOW()),
('ROLE000003', 'KHGL', '客户管理员', 1, NOW(), NOW()),
('ROLE000004', 'ZHGL', '账户管理员', 1, NOW(), NOW());

-- =====================================================
-- 4. 角色菜单关联初始化
-- =====================================================

-- 超级管理员：拥有所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000001', menu_id FROM sys_menu;

-- 业务管理员：系统管理所有功能
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000002', menu_id FROM sys_menu WHERE menu_id LIKE 'MENU0000%' OR menu_id LIKE 'MENU0001%';

-- 客户管理员：客户管理所有功能
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000003', menu_id FROM sys_menu WHERE menu_id LIKE 'MENU00003%' OR menu_id LIKE 'MENU0003%';

-- 账户管理员：账户管理所有功能
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 'ROLE000004', menu_id FROM sys_menu WHERE menu_id LIKE 'MENU00004%' OR menu_id LIKE 'MENU0004%';

-- =====================================================
-- 5. 用户数据初始化
-- =====================================================
-- 密码规则：用户名 + '123'
-- 密码使用SM3算法加密

INSERT INTO sys_user (user_id, username, password, real_name, org_id, auth_level, user_status, create_time, update_time) VALUES
-- 超级管理员（密码：admin123）
('USER000001', 'admin', '667c756cf9334e328a56e44e906245c8e214c655a160f18fdb84d79c209c49cf', '系统管理员', 'ORG0000001', 16, 1, NOW(), NOW()),

-- 业务管理员（总行，10级）密码：ywgl01123, ywgl02123
('USER000002', 'ywgl01', 'a0b06cf7b454e6b84e793a90640de458c01e1ff3bcfb322b38b659dbb075185d', '业务管理员01', 'ORG0000001', 10, 1, NOW(), NOW()),
('USER000003', 'ywgl02', '10b807eea677405bfb77be85919cfa359f8c9c83265f4988ec09e708d7c48c47', '业务管理员02', 'ORG0000001', 10, 1, NOW(), NOW()),

-- 客户管理员（上海闵行支行，3级）密码：khgl03123, khgl04123
('USER000004', 'khgl03', '71870e01a416491e99a026cef42e3d80bbfa13b0838e874162991575f53ffad6', '客户管理员03', 'ORG0000006', 3, 1, NOW(), NOW()),
('USER000005', 'khgl04', '69649aedb52270cf8ca296d13baf08cfcc8a7c2154bf932681d3d6a2737a0efe', '客户管理员04', 'ORG0000006', 3, 1, NOW(), NOW()),

-- 客户管理员（上海闵行梅陇镇分理处，4级）密码：khgl05123, khgl06123
('USER000006', 'khgl05', '8e33a0f1665916d2ba30e5b939d16ff8519ff50ca0a3a205ac90e33866ccedb9', '客户管理员05', 'ORG0000008', 4, 1, NOW(), NOW()),
('USER000007', 'khgl06', 'a0f8b79d096d3e9d6b7d65a464126aaaa39fc0f23fff8ec8186bf9cf58952d16', '客户管理员06', 'ORG0000008', 4, 1, NOW(), NOW()),

-- 账户管理员（上海闵行支行，2级）密码：zhgl01123, zhgl02123
('USER000008', 'zhgl01', '803c5d593deedb9b6ed3de68def56d61d36ac4ee8fd423843446af9db7d83243', '账户管理员01', 'ORG0000006', 2, 1, NOW(), NOW()),
('USER000009', 'zhgl02', '6c6804f3ac88134f65deef3cabae8af6614d678f2c96defd8949f66ef50136c9', '账户管理员02', 'ORG0000006', 2, 1, NOW(), NOW());

-- =====================================================
-- 6. 用户角色关联初始化
-- =====================================================

INSERT INTO sys_user_role (user_id, role_id) VALUES
-- admin -> 超级管理员
('USER000001', 'ROLE000001'),

-- ywgl01, ywgl02 -> 业务管理员
('USER000002', 'ROLE000002'),
('USER000003', 'ROLE000002'),

-- khgl03, khgl04, khgl05, khgl06 -> 客户管理员
('USER000004', 'ROLE000003'),
('USER000005', 'ROLE000003'),
('USER000006', 'ROLE000003'),
('USER000007', 'ROLE000003'),

-- zhgl01, zhgl02 -> 账户管理员
('USER000008', 'ROLE000004'),
('USER000009', 'ROLE000004');

-- =====================================================
-- 验证数据
-- =====================================================

SELECT '=== 机构数据 ===' AS info;
SELECT org_code, org_name, org_level FROM sys_org ORDER BY org_code;

SELECT '=== 一级菜单 ===' AS info;
SELECT menu_id, menu_name, menu_type FROM sys_menu WHERE parent_id = '0' ORDER BY sort_order;

SELECT '=== 角色数据 ===' AS info;
SELECT role_code, role_name FROM sys_role;

SELECT '=== 用户数据 ===' AS info;
SELECT u.username, u.real_name, o.org_name, u.auth_level, r.role_name 
FROM sys_user u 
LEFT JOIN sys_org o ON u.org_id = o.org_id 
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id 
LEFT JOIN sys_role r ON ur.role_id = r.role_id 
ORDER BY u.username;

SELECT '=== 初始化完成 ===' AS info;
