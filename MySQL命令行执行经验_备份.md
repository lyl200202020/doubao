# MySQL命令行执行经验总结

## 环境说明

- 操作系统：Windows
- 终端：PowerShell
- MySQL：远程服务器 113.221.57.26:3306

---

## 踩坑记录

### ❌ 错误1：PowerShell 不支持 `<` 重定向

```bash
# 错误写法
mysql -h 113.221.57.26 -u app -p123456 cust < schema.sql
```

**错误信息：**
```
"<" 运算符是为将来使用而保留的
```

**原因：** PowerShell 中 `<` 重定向不被支持

---

### ❌ 错误2：命令行参数中带引号被错误解析

```bash
# 错误写法
mysql -h 113.221.57.26 -u app -p123456 -e "SELECT VERSION();"
mysql -h 113.221.57.26 -u app -p123456 -e "SHOW DATABASES"
```

**错误信息：**
```
unexpected argument 'VERSION();' found
unexpected argument 'DATABASES' found
```

**原因：** Windows 环境下通过沙箱执行命令时，引号内的参数被错误解析

---

### ❌ 错误3：MySQL 客户端命令参数不带 `-e` 时需要用管道

```bash
# 错误写法
mysql -h 113.221.57.26 -u app -p123456 cust SHOW TABLES
```

**原因：** MySQL 客户端需要通过 stdin 接收 SQL 语句

---

### ❌ 错误4：SQL文件中含中文注释导致编码错误

```sql
-- 含中文注释的SQL
CREATE TABLE sys_user (
    ...
    user_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0-禁用,1-启用'
);
```

**错误信息：**
```
ERROR 1064: You have an error in your SQL syntax... near '????0-???'
```

**原因：** Windows 环境下通过 `type` 管道传输中文时编码问题

**解决方案：** 移除 SQL 文件中的中文注释

---

## ✅ 正确写法

### 方式1：使用管道（推荐）

```bash
# 适用于执行 SQL 文件
type d:\path\to\schema.sql | mysql -h 113.221.57.26 -u app -p123456 cust

# 适用于执行单条SQL
echo SELECT VERSION() | mysql -h 113.221.57.26 -u app -p123456
```

### 方式2：创建临时SQL文件

```sql
-- verify_data.sql
SELECT username, real_name, auth_level FROM sys_user;
```

```bash
type d:\study\doubao\sql\verify_data.sql | mysql -h 113.221.57.26 -u app -p123456 cust
```

### 方式3：MySQL交互式客户端（最推荐）

```bash
# 进入MySQL客户端
mysql -h 113.221.57.26 -u app -p123456 cust

# 在客户端内执行
mysql> source d:/study/doubao/customer-admin/sql/schema.sql
mysql> SELECT * FROM sys_user;
```

---

## SQL文件编码规范

在 Windows 环境下通过 `type` 管道执行 SQL 时：

1. **避免使用中文注释**，或使用英文
2. **避免使用特殊字符**
3. **推荐格式：**

```sql
USE cust;

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

INSERT INTO sys_user (user_id, username, password, real_name, org_id, auth_level, user_status) VALUES
('USER001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'AdminUser', 'ORG001', 16, 1);
```

---

## 常用命令速查

| 场景 | 命令 |
|------|------|
| 查看数据库 | `echo SHOW DATABASES \| mysql -h host -u user -ppass` |
| 查看表 | `echo SHOW TABLES \| mysql -h host -u user -ppass dbname` |
| 查询数据 | 创建SQL文件后用管道执行 |
| 执行SQL文件 | `type file.sql \| mysql -h host -u user -ppass dbname` |
| 交互式客户端 | `mysql -h host -u user -ppass dbname` |

---

**编制日期**：2026-03-05
