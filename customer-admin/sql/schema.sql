USE cust;

CREATE TABLE IF NOT EXISTS sys_org (
    org_id VARCHAR(32) PRIMARY KEY,
    parent_id VARCHAR(32) DEFAULT '0',
    org_code VARCHAR(50) NOT NULL,
    org_name VARCHAR(100) NOT NULL,
    org_level TINYINT NOT NULL,
    org_status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent (parent_id),
    INDEX idx_code (org_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
    role_id VARCHAR(32) PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    role_status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user (
    user_id VARCHAR(32) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    org_id VARCHAR(32) NOT NULL,
    auth_level TINYINT NOT NULL DEFAULT 1,
    user_status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_org (org_id),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id VARCHAR(32) PRIMARY KEY,
    parent_id VARCHAR(32) DEFAULT '0',
    menu_name VARCHAR(100) NOT NULL,
    menu_type TINYINT NOT NULL,
    permission VARCHAR(100),
    route_path VARCHAR(200),
    component VARCHAR(200),
    menu_icon VARCHAR(50),
    sort_order INT DEFAULT 0,
    menu_status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent (parent_id),
    INDEX idx_permission (permission)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id VARCHAR(32) NOT NULL,
    role_id VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    INDEX idx_user (user_id),
    INDEX idx_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id VARCHAR(32) NOT NULL,
    menu_id VARCHAR(32) NOT NULL,
    PRIMARY KEY (role_id, menu_id),
    INDEX idx_role (role_id),
    INDEX idx_menu (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO sys_org (org_id, parent_id, org_code, org_name, org_level, org_status) VALUES
('ORG001', '0', 'HEAD', 'HeadOffice', 0, 1),
('ORG002', 'ORG001', 'BRANCH001', 'Branch001', 1, 1),
('ORG003', 'ORG001', 'BRANCH002', 'Branch002', 1, 1);

INSERT INTO sys_role (role_id, role_code, role_name, role_status) VALUES
('ROLE001', 'ADMIN', 'Admin', 1),
('ROLE002', 'USER', 'User', 1);

INSERT INTO sys_user (user_id, username, password, real_name, org_id, auth_level, user_status) VALUES
('USER001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'AdminUser', 'ORG001', 16, 1),
('USER002', 'test', 'e10adc3949ba59abbe56e057f20f883e', 'TestUser', 'ORG002', 8, 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES
('USER001', 'ROLE001'),
('USER002', 'ROLE002');

INSERT INTO sys_menu (menu_id, parent_id, menu_name, menu_type, permission, route_path, component, sort_order, menu_status) VALUES
('MENU001', '0', 'System', 1, NULL, '/system', 'system/index', 1, 1),
('MENU002', 'MENU001', 'UserManage', 2, 'sys:user:list', '/system/user', 'system/user/index', 1, 1),
('MENU003', 'MENU001', 'RoleManage', 2, 'sys:role:list', '/system/role', 'system/role/index', 2, 1),
('MENU004', 'MENU001', 'MenuManage', 2, 'sys:menu:list', '/system/menu', 'system/menu/index', 3, 1);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
('ROLE001', 'MENU001'),
('ROLE001', 'MENU002'),
('ROLE001', 'MENU003'),
('ROLE001', 'MENU004'),
('ROLE002', 'MENU002');
