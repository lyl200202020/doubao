-- Fix duplicate columns in sys_org
ALTER TABLE sys_org DROP COLUMN create_time;
ALTER TABLE sys_org DROP COLUMN update_time;
