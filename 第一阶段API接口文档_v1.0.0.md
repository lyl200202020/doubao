# 客户运营管理后台系统 - 第一阶段API接口文档

## 文档信息

| 项目 | 内容 |
|------|------|
| 版本 | v1.0.0 |
| 阶段 | 第一阶段（基础框架搭建） |
| 日期 | 2026-03-05 |

---

## 1 接口概览

### 1.1 基础信息

| 项目 | 内容 |
|------|------|
| 基础URL | http://localhost:8080 |
| 数据格式 | JSON |
| 编码 | UTF-8 |
| 认证方式 | JWT Token |

### 1.2 接口列表

| 序号 | 模块 | 接口名称 | 方法 | 路径 |
|------|------|----------|------|------|
| 1 | 认证 | 登录 | POST | /api/auth/login |
| 2 | 认证 | 退出 | POST | /api/auth/logout |
| 3 | 认证 | 获取当前用户 | GET | /api/auth/current |
| 4 | 系统 | 获取用户信息 | GET | /api/sys/user/info |
| 5 | 系统 | 获取菜单树 | GET | /api/sys/menu/tree |

---

## 2 认证模块接口

### 2.1 用户登录

**接口地址**：`POST /api/auth/login`

**请求头**：
```
Content-Type: application/json
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| username | String | 是 | 用户名 | admin |
| password | String | 是 | 密码（明文，后端SM3加密） | admin123 |

**请求示例**：
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码（200=成功，500=失败） |
| msg | String | 消息 |
| data | Object | 响应数据 |
| data.token | String | JWT Token |
| data.userInfo | Object | 用户信息 |
| data.userInfo.userId | String | 用户ID |
| data.userInfo.username | String | 用户名 |
| data.userInfo.realName | String | 真实姓名 |
| data.userInfo.orgId | String | 机构ID |
| data.userInfo.orgName | String | 机构名称 |
| data.userInfo.authLevel | Integer | 授权级别（1-16） |
| data.userInfo.permissions | Array | 权限列表 |

**成功响应示例**：
  "code": 200,
 ```json
{
 "msg": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDAxIiwidXNlcm5hbWUiOiJhZG1pbiIsImlhdCI6MTcwNjc0MDAwMCwiZXhwIjoxNzA2ODI2NDAwfQ.xxxxxxxxxxxxxx",
    "userInfo": {
      "userId": "1001",
      "username": "admin",
      "realName": "系统管理员",
      "orgId": "0000",
      "orgName": "总行",
      "authLevel": 16,
      "permissions": ["sys:user:list", "sys:user:add", "sys:user:edit"]
    }
  },
  "timestamp": 1706740000000
}
```

**失败响应示例**：
```json
{
  "code": 401,
  "msg": "用户名或密码错误",
  "data": null,
  "timestamp": 1706740000000
}
```

---

### 2.2 用户退出

**接口地址**：`POST /api/auth/logout`

**请求头**：
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码（200=成功） |
| msg | String | 消息 |
| data | Object | 响应数据（通常为null） |

**成功响应示例**：
```json
{
  "code": 200,
  "msg": "退出成功",
  "data": null,
  "timestamp": 1706740000000
}
```

---

### 2.3 获取当前用户

**接口地址**：`GET /api/auth/current`

**请求头**：
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码（200=成功，401=未登录） |
| msg | String | 消息 |
| data | Object | 用户信息 |
| data.userId | String | 用户ID |
| data.username | String | 用户名 |
| data.realName | String | 真实姓名 |
| data.orgId | String | 机构ID |
| data.orgName | String | 机构名称 |
| data.authLevel | Integer | 授权级别（1-16） |
| data.permissions | Array | 权限列表 |

**成功响应示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "userId": "1001",
    "username": "admin",
    "realName": "系统管理员",
    "orgId": "0000",
    "orgName": "总行",
    "authLevel": 16,
    "permissions": ["sys:user:list", "sys:user:add", "sys:user:edit"]
  },
  "timestamp": 1706740000000
}
```

**未登录响应示例**：
```json
{
  "code": 401,
  "msg": "未登录或Token已过期",
  "data": null,
  "timestamp": 1706740000000
}
```

---

## 3 系统模块接口

### 3.1 获取用户信息（带权限）

**接口地址**：`GET /api/sys/user/info`

**请求头**：
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码（200=成功） |
| msg | String | 消息 |
| data | Object | 用户信息 |
| data.userId | String | 用户ID |
| data.username | String | 用户名 |
| data.realName | String | 真实姓名 |
| data.orgId | String | 机构ID |
| data.orgName | String | 机构名称 |
| data.authLevel | Integer | 授权级别 |
| data.roles | Array | 角色列表 |
| data.permissions | Array | 权限列表（按钮级别） |

**成功响应示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "userId": "1001",
    "username": "admin",
    "realName": "系统管理员",
    "orgId": "0000",
    "orgName": "总行",
    "authLevel": 16,
    "roles": [
      {
        "roleId": "1",
        "roleCode": "ADMIN",
        "roleName": "超级管理员"
      }
    ],
    "permissions": [
      "sys:user:list",
      "sys:user:add",
      "sys:user:edit",
      "sys:user:delete",
      "sys:role:list",
      "sys:role:add",
      "sys:menu:list"
    ]
  },
  "timestamp": 1706740000000
}
```

---

### 3.2 获取菜单树

**接口地址**：`GET /api/sys/menu/tree`

**请求头**：
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码（200=成功） |
| msg | String | 消息 |
| data | Array | 菜单树形列表 |

**菜单节点结构**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| menuId | String | 菜单ID |
| parentId | String | 父菜单ID |
| menuName | String | 菜单名称 |
| menuType | Integer | 类型（1=目录，2=菜单，3=按钮） |
| permission | String | 权限标识 |
| routePath | String | 路由路径 |
| component | String | 组件路径 |
| menuIcon | String | 菜单图标 |
| sortOrder | Integer | 排序 |
| children | Array | 子菜单 |

**成功响应示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "menuId": "1",
      "parentId": "0",
      "menuName": "系统管理",
      "menuType": 1,
      "menuIcon": "Setting",
      "sortOrder": 1,
      "children": [
        {
          "menuId": "101",
          "parentId": "1",
          "menuName": "用户管理",
          "menuType": 2,
          "permission": "sys:user:list",
          "routePath": "/sys/user",
          "component": "/sys/user/index",
          "menuIcon": "User",
          "sortOrder": 1
        },
        {
          "menuId": "102",
          "parentId": "1",
          "menuName": "角色管理",
          "menuType": 2,
          "permission": "sys:role:list",
          "routePath": "/sys/role",
          "component": "/sys/role/index",
          "menuIcon": "UserFilled",
          "sortOrder": 2
        },
        {
          "menuId": "103",
          "parentId": "1",
          "menuName": "菜单管理",
          "menuType": 2,
          "permission": "sys:menu:list",
          "routePath": "/sys/menu",
          "component": "/sys/menu/index",
          "menuIcon": "Menu",
          "sortOrder": 3
        },
        {
          "menuId": "104",
          "parentId": "1",
          "menuName": "机构管理",
          "menuType": 2,
          "permission": "sys:org:list",
          "routePath": "/sys/org",
          "component": "/sys/org/index",
          "menuIcon": "OfficeBuilding",
          "sortOrder": 4
        }
      ]
    },
    {
      "menuId": "2",
      "parentId": "0",
      "menuName": "授权管理",
      "menuType": 1,
      "menuIcon": "Lock",
      "sortOrder": 2,
      "children": [
        {
          "menuId": "201",
          "parentId": "2",
          "menuName": "待办任务",
          "menuType": 2,
          "permission": "auth:task:todo",
          "routePath": "/auth/task/todo",
          "component": "/auth/task/todo",
          "menuIcon": "List",
          "sortOrder": 1
        },
        {
          "menuId": "202",
          "parentId": "2",
          "menuName": "我的已办",
          "menuType": 2,
          "permission": "auth:task:done",
          "routePath": "/auth/task/done",
          "component": "/auth/task/done",
          "menuIcon": "Finished",
          "sortOrder": 2
        },
        {
          "menuId": "203",
          "parentId": "2",
          "menuName": "授权历史",
          "menuType": 2,
          "permission": "auth:history:list",
          "routePath": "/auth/history",
          "component": "/auth/history/index",
          "menuIcon": "Clock",
          "sortOrder": 3
        }
      ]
    },
    {
      "menuId": "3",
      "parentId": "0",
      "menuName": "客户管理",
      "menuType": 1,
      "menuIcon": "User",
      "sortOrder": 3,
      "children": [
        {
          "menuId": "301",
          "parentId": "3",
          "menuName": "客户管理",
          "menuType": 2,
          "permission": "crm:customer:list",
          "routePath": "/crm/customer",
          "component": "/crm/customer/index",
          "menuIcon": "Avatar",
          "sortOrder": 1
        },
        {
          "menuId": "302",
          "parentId": "3",
          "menuName": "客户签约",
          "menuType": 2,
          "permission": "crm:contract:list",
          "routePath": "/crm/contract",
          "component": "/crm/contract/index",
          "menuIcon": "Document",
          "sortOrder": 2
        }
      ]
    }
  ],
  "timestamp": 1706740000000
}
```

---

## 4 统一响应格式

### 4.1 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {},
  "timestamp": 1706740000000
}
```

### 4.2 失败响应

```json
{
  "code": 500,
  "msg": "服务器内部错误",
  "data": null,
  "timestamp": 1706740000000
}
```

### 4.3 分页响应（预留）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [],
    "total": 0,
    "page": 1,
    "pageSize": 20
  },
  "timestamp": 1706740000000
}
```

---

## 5 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或Token无效 |
| 403 | 没有权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 6 状态码说明

| 状态码 | 说明 |
|--------|------|
| 0 | 禁用/停用 |
| 1 | 启用/正常 |

---

## 7 注意事项

1. **Token传递**：除登录接口外，所有接口都需要在请求头中携带Token，格式为 `Authorization: Bearer {token}`
2. **密码传输**：登录密码使用明文传输，后端使用SM3加密后与数据库比对
3. **Token有效期**：默认24小时
4. **编码格式**：所有请求和响应都使用UTF-8编码

---

**编制人**：后端开发
**编制日期**：2026-03-05
**版本**：v1.0
