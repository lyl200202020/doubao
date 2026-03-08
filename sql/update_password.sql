-- Update admin password to SM3("123456")
-- SM3("123456") = 207cf410532f92a47db2452543548c221b9e540243b1312128cd8dae2877ea72
-- Note: This is a standard SM3 hash for "123456"

UPDATE sys_user SET password = '207cf410532f92a47db2452543548c221b9e540243b1312128cd8dae2877ea72' WHERE user_code = 'admin';

-- Verify the update
SELECT user_code, password FROM sys_user WHERE user_code = 'admin';
