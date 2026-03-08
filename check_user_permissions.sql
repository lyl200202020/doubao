SELECT u.user_id, u.user_code, u.real_name, r.role_code, m.menu_id, m.perms 
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.user_code = 'admin'
LIMIT 20;