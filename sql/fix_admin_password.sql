-- Update admin password to correct SM3 hash of "123456"
-- SM3("123456") = 207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb
UPDATE sys_user SET password = '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb' WHERE user_code = 'admin';

-- Verify
SELECT user_code, password FROM sys_user WHERE user_code = 'admin';
