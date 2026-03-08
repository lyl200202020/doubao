-- Drop duplicate update_time column (keep updated_time)
ALTER TABLE sys_role DROP COLUMN update_time;

-- Verify
DESCRIBE sys_role;
