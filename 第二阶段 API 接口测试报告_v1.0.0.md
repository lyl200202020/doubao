# 第二阶段 API 接口测试报告

## 测试环境

| 项目 | 值 |
|------|------|
| 测试日期 | 2026-03-07 |
| 后端服务 | http://localhost:8080 |
| 数据库 | MySQL 8.0 (113.221.57.26:3306) |
| 测试工具 | Postman / cURL |

## 测试准备

### 1. 数据库初始化

```bash
# 执行数据库脚本
mysql -u root -p cust < sql/第二阶段 schema_v1.0.0.sql
mysql -u root -p cust < sql/第二阶段 data_v1.0.0.sql
```

### 2. 启动后端服务

```bash
# 方式 1: 使用 Maven
cd customer-admin/customer-admin-platform/platform-start
mvn spring-boot:run

# 方式 2: 直接运行 jar
cd customer-admin/customer-admin-platform/platform-start
java -jar target/platform-start-1.0.0-SNAPSHOT.jar
```

### 3. 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |
| user1 | 123456 | 普通用户 |

---

## 接口测试详情

### 1. 认证接口

#### 1.1 登录接口

**请求**：
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "userId": "admin",
      "userName": "管理员",
      "orgId": "1",
      "roleId": "1"
    }
  }
}
```

**测试状态**: ⏳ 待测试

---

### 2. 机构管理接口

#### 2.1 获取机构树

**请求**：
```http
GET http://localhost:8080/api/sys/org/tree
Authorization: Bearer {token}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "orgId": "1",
      "orgCode": "HEAD",
      "orgName": "总行",
      "orgLevel": 0,
      "parentOrgId": null,
      "sortOrder": 1,
      "orgStatus": 1,
      "children": [
        {
          "orgId": "2",
          "orgCode": "BRANCH_001",
          "orgName": "北京分行",
          "orgLevel": 1,
          "parentOrgId": "1",
          "sortOrder": 1,
          "orgStatus": 1,
          "children": []
        }
      ]
    }
  ]
}
```

**测试状态**: ⏳ 待测试

#### 2.2 新增机构

**请求**：
```http
POST http://localhost:8080/api/sys/org
Authorization: Bearer {token}
Content-Type: application/json

{
  "orgCode": "TEST_ORG_001",
  "orgName": "测试机构",
  "orgLevel": 2,
  "parentOrgId": "1",
  "sortOrder": 10,
  "remark": "自动化测试创建"
}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": null
}
```

**测试状态**: ⏳ 待测试

#### 2.3 修改机构

**请求**：
```http
PUT http://localhost:8080/api/sys/org/TEST_ORG_001
Authorization: Bearer {token}
Content-Type: application/json

{
  "orgCode": "TEST_ORG_001",
  "orgName": "测试机构 - 已修改",
  "orgLevel": 2,
  "parentOrgId": "1",
  "sortOrder": 10,
  "remark": "已修改"
}
```

**测试状态**: ⏳ 待测试

#### 2.4 删除机构

**请求**：
```http
DELETE http://localhost:8080/api/sys/org/TEST_ORG_001
Authorization: Bearer {token}
```

**测试状态**: ⏳ 待测试

---

### 3. 用户管理接口

#### 3.1 分页查询用户

**请求**：
```http
GET http://localhost:8080/api/sys/user/list?pageNum=1&pageSize=10
Authorization: Bearer {token}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "total": 5,
    "records": [
      {
        "userId": "admin",
        "userCode": "admin",
        "userName": "管理员",
        "mobile": "13800138000",
        "email": "admin@example.com",
        "orgId": "1",
        "orgName": "总行",
        "roleId": "1",
        "roleName": "超级管理员",
        "authLevel": 16,
        "userStatus": 1
      }
    ]
  }
}
```

**测试状态**: ⏳ 待测试

#### 3.2 新增用户

**请求**：
```http
POST http://localhost:8080/api/sys/user
Authorization: Bearer {token}
Content-Type: application/json

{
  "userCode": "TEST_USER_001",
  "userName": "测试用户",
  "mobile": "13800138000",
  "email": "test@example.com",
  "orgId": "1",
  "roleId": "2",
  "authLevel": 1,
  "remark": "自动化测试创建"
}
```

**测试状态**: ⏳ 待测试

#### 3.3 重置密码

**请求**：
```http
POST http://localhost:8080/api/sys/user/TEST_USER_001/reset-pwd
Authorization: Bearer {token}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "密码已重置为默认密码：123456",
  "data": null
}
```

**测试状态**: ⏳ 待测试

---

### 4. 菜单管理接口

#### 4.1 获取菜单树

**请求**：
```http
GET http://localhost:8080/api/sys/menu/tree
Authorization: Bearer {token}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "menuId": "1",
      "menuName": "系统管理",
      "menuType": 1,
      "parentMenuId": null,
      "menuUrl": "/system",
      "menuIcon": "setting",
      "perms": "sys:menu:view",
      "sortOrder": 1,
      "visible": 1,
      "children": [
        {
          "menuId": "2",
          "menuName": "机构管理",
          "menuType": 2,
          "parentMenuId": "1",
          "menuUrl": "/system/org",
          "menuIcon": "org",
          "perms": "sys:org:view",
          "sortOrder": 1,
          "visible": 1,
          "children": []
        }
      ]
    }
  ]
}
```

**测试状态**: ⏳ 待测试

#### 4.2 新增菜单

**请求**：
```http
POST http://localhost:8080/api/sys/menu
Authorization: Bearer {token}
Content-Type: application/json

{
  "menuName": "测试菜单",
  "menuType": 2,
  "parentMenuId": "1",
  "menuUrl": "/test",
  "menuIcon": "test",
  "perms": "sys:test:view",
  "sortOrder": 10,
  "visible": 1
}
```

**测试状态**: ⏳ 待测试

---

### 5. 角色管理接口

#### 5.1 分页查询角色

**请求**：
```http
GET http://localhost:8080/api/sys/role/list?pageNum=1&pageSize=10
Authorization: Bearer {token}
```

**预期响应**：
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "total": 3,
    "records": [
      {
        "roleId": "1",
        "roleCode": "super_admin",
        "roleName": "超级管理员",
        "roleDesc": "系统超级管理员",
        "roleStatus": 1,
        "createdTime": "2026-03-07T10:00:00"
      }
    ]
  }
}
```

**测试状态**: ⏳ 待测试

#### 5.2 新增角色

**请求**：
```http
POST http://localhost:8080/api/sys/role
Authorization: Bearer {token}
Content-Type: application/json

{
  "roleCode": "TEST_ROLE",
  "roleName": "测试角色",
  "roleDesc": "自动化测试创建"
}
```

**测试状态**: ⏳ 待测试

---

## 测试结果汇总

| 模块 | 接口数 | 通过 | 失败 | 通过率 |
|------|--------|------|------|--------|
| 认证接口 | 1 | - | - | - |
| 机构管理 | 6 | - | - | - |
| 用户管理 | 7 | - | - | - |
| 菜单管理 | 6 | - | - | - |
| 角色管理 | 5 | - | - | - |
| **总计** | **25** | **0** | **0** | **0%** |

---

## 测试步骤（使用 Postman）

### 1. 创建 Postman 集合

1. 打开 Postman
2. 创建新集合：第二阶段 API 测试
3. 添加请求

### 2. 设置环境变量

在 Postman 中创建环境变量：
- `base_url`: http://localhost:8080
- `token`: (登录后自动填充)

### 3. 登录获取 Token

使用登录接口，将响应中的 token 保存到环境变量：
```javascript
pm.environment.set("token", pm.response.json().data.token);
```

### 4. 测试所有接口

按顺序执行集合中的所有请求。

---

## 测试步骤（使用 cURL）

### 1. 登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 2. 获取机构树

```bash
curl -X GET http://localhost:8080/api/sys/org/tree \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3. 获取用户列表

```bash
curl -X GET "http://localhost:8080/api/sys/user/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 常见问题

### 1. 401 Unauthorized

**原因**: Token 过期或无效  
**解决**: 重新登录获取新 token

### 2. 403 Forbidden

**原因**: 权限不足  
**解决**: 使用有权限的账号测试

### 3. 数据库连接失败

**原因**: 数据库未启动或配置错误  
**解决**: 检查数据库服务和配置文件

### 4. Nacos 连接失败

**原因**: Nacos 服务未启动  
**解决**: 使用本地配置启动或启动 Nacos

---

## 下一步

1. ✅ 启动后端服务
2. ✅ 执行数据库初始化脚本
3. ⏳ 使用 Postman/cURL 测试所有接口
4. ⏳ 记录测试结果
5. ⏳ 修复发现的问题
6. ⏳ 重新测试直到所有接口通过

---

**测试状态**: 待执行  
**最后更新**: 2026-03-07
