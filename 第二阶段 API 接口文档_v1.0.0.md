# 客户运营管理后台系统 - 第二阶段 API 接口文档

## 文档信息

| 项目 | 内容 |
|------|------|
| 版本 | v1.0.0 |
| 阶段 | 第二阶段（v0.0.2-v0.0.4） |
| 创建日期 | 2026-03-07 |
| 编制人 | 后端开发 Agent |
| 文档用途 | 与前端团队核对接口定义 |

---

## 一、接口规范

### 1.1 基础路径

```
生产环境：https://api.example.com
测试环境：http://localhost:8080
基础路径：/api
```

### 1.2 统一返回格式

```json
{
  "code": 200,
  "msg": "success",
  "data": {},
  "timestamp": 1709827200000
}
```

**返回码说明**：

| 返回码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录或 Token 过期 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |

### 1.3 请求头

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 1.4 分页参数

所有列表接口统一使用以下分页参数：

```json
{
  "pageNum": 1,
  "pageSize": 10
}
```

### 1.5 分页返回

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  }
}
```

---

## 二、认证接口

### 2.1 用户登录

**接口路径**：`POST /api/auth/login`

**请求参数**：

```json
{
  "userCode": "admin",
  "password": "admin123"
}
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "userId": "1",
      "userCode": "admin",
      "userName": "系统管理员",
      "orgId": "1",
      "orgName": "总行",
      "roleId": "1",
      "roleName": "系统管理员",
      "authLevel": 16
    },
    "menus": [
      {
        "menuId": "1",
        "menuName": "系统管理",
        "menuType": 1,
        "menuUrl": "/system",
        "menuIcon": "setting",
        "children": [
          {
            "menuId": "2",
            "menuName": "机构管理",
            "menuType": 2,
            "menuUrl": "/system/org",
            "perms": "sys:org:view"
          }
        ]
      }
    ],
    "perms": ["sys:org:view", "sys:org:add", "sys:org:edit", "sys:org:delete"]
  }
}
```

**权限标识**：无

---

### 2.2 用户退出

**接口路径**：`POST /api/auth/logout`

**请求参数**：无（从 Token 中获取用户信息）

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：无

---

## 三、机构管理接口（v0.0.2）

### 3.1 获取机构树

**接口路径**：`GET /api/sys/org/tree`

**请求参数**：无

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "orgId": "1",
      "orgCode": "HEAD",
      "orgName": "总行",
      "orgLevel": 0,
      "parentOrgId": null,
      "orgStatus": 1,
      "sortOrder": 0,
      "remark": null,
      "children": [
        {
          "orgId": "2",
          "orgCode": "BJ_BRANCH",
          "orgName": "北京分行",
          "orgLevel": 1,
          "parentOrgId": "1",
          "orgStatus": 1,
          "sortOrder": 1,
          "remark": null,
          "children": [
            {
              "orgId": "3",
              "orgCode": "BJ_SUB",
              "orgName": "北京支行",
              "orgLevel": 2,
              "parentOrgId": "2",
              "orgStatus": 1,
              "sortOrder": 1,
              "remark": null,
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

**权限标识**：`sys:org:view`

---

### 3.2 新增机构

**接口路径**：`POST /api/sys/org`

**请求参数**：

```json
{
  "orgCode": "SH_BRANCH",
  "orgName": "上海分行",
  "parentOrgId": "1",
  "sortOrder": 2,
  "remark": "备注信息"
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| orgCode | String | 是 | 机构代码（唯一） |
| orgName | String | 是 | 机构名称 |
| parentOrgId | String | 否 | 上级机构 ID（顶级机构不传） |
| sortOrder | Integer | 否 | 排序号 |
| remark | String | 否 | 备注 |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:org:add`

---

### 3.3 修改机构

**接口路径**：`PUT /api/sys/org/{id}`

**路径参数**：

```
id: 机构 ID
```

**请求参数**：

```json
{
  "orgCode": "SH_BRANCH",
  "orgName": "上海分行",
  "sortOrder": 2,
  "remark": "备注信息"
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| orgCode | String | 是 | 机构代码（唯一） |
| orgName | String | 是 | 机构名称 |
| sortOrder | Integer | 否 | 排序号 |
| remark | String | 否 | 备注 |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:org:edit`

---

### 3.4 删除机构

**接口路径**：`DELETE /api/sys/org/{id}`

**路径参数**：

```
id: 机构 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:org:delete`

**错误提示**：
- 存在下级机构：`"code": 400, "msg": "存在下级机构，无法删除"`
- 有关联用户：`"code": 400, "msg": "机构下有关联用户，无法删除"`

---

### 3.5 启用/停用机构

**接口路径**：`PATCH /api/sys/org/{id}/status`

**路径参数**：

```
id: 机构 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:org:edit`

**说明**：
- 停用机构时，级联停用所有下级机构
- 启用机构时，只启用当前机构

---

## 四、用户管理接口（v0.0.3）

### 4.1 查询用户列表

**接口路径**：`GET /api/sys/user/list`

**查询参数**：

```
pageNum=1&pageSize=10&orgId=1&roleId=1&userStatus=1&userName=张三
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认 1 |
| pageSize | Integer | 否 | 每页数量，默认 10 |
| orgId | String | 否 | 所属机构 ID |
| roleId | String | 否 | 所属角色 ID |
| userStatus | Integer | 否 | 用户状态：0-停用，1-启用 |
| userName | String | 否 | 用户姓名（模糊查询） |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [
      {
        "userId": "1",
        "userCode": "admin",
        "userName": "系统管理员",
        "mobile": "13800138000",
        "email": "admin@example.com",
        "orgId": "1",
        "orgName": "总行",
        "roleId": "1",
        "roleName": "系统管理员",
        "authLevel": 16,
        "userStatus": 1,
        "lastLoginTime": "2026-03-07 10:00:00"
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

**权限标识**：`sys:user:view`

---

### 4.2 新增用户

**接口路径**：`POST /api/sys/user`

**请求参数**：

```json
{
  "userCode": "zhangsan",
  "userName": "张三",
  "password": "123456",
  "mobile": "13800138000",
  "email": "zhangsan@example.com",
  "orgId": "1",
  "roleId": "1",
  "authLevel": 5
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userCode | String | 是 | 用户工号（唯一） |
| userName | String | 是 | 用户姓名 |
| password | String | 是 | 密码（明文传输，后端 SM3 加密） |
| mobile | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |
| orgId | String | 是 | 所属机构 ID |
| roleId | String | 是 | 所属角色 ID |
| authLevel | Integer | 是 | 授权级别（1-16 级） |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:user:add`

---

### 4.3 修改用户

**接口路径**：`PUT /api/sys/user/{id}`

**路径参数**：

```
id: 用户 ID
```

**请求参数**：

```json
{
  "userName": "张三",
  "mobile": "13800138000",
  "email": "zhangsan@example.com",
  "orgId": "1",
  "roleId": "1",
  "authLevel": 5
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userName | String | 是 | 用户姓名 |
| mobile | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |
| orgId | String | 是 | 所属机构 ID |
| roleId | String | 是 | 所属角色 ID |
| authLevel | Integer | 是 | 授权级别（1-16 级） |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:user:edit`

---

### 4.4 删除用户

**接口路径**：`DELETE /api/sys/user/{id}`

**路径参数**：

```
id: 用户 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:user:delete`

**错误提示**：
- 有待办任务：`"code": 400, "msg": "用户有待办任务，无法删除"`

---

### 4.5 重置密码

**接口路径**：`POST /api/sys/user/{id}/reset-pwd`

**路径参数**：

```
id: 用户 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:user:edit`

**说明**：密码重置为默认密码 `123456`

---

### 4.6 启用/停用用户

**接口路径**：`PATCH /api/sys/user/{id}/status`

**路径参数**：

```
id: 用户 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:user:edit`

---

## 五、菜单管理接口（v0.0.3）

### 5.1 获取菜单树

**接口路径**：`GET /api/sys/menu/tree`

**请求参数**：无

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "menuId": "1",
      "menuName": "系统管理",
      "menuType": 1,
      "parentMenuId": null,
      "menuUrl": "/system",
      "menuIcon": "setting",
      "perms": null,
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
          "children": [
            {
              "menuId": "3",
              "menuName": "新增机构",
              "menuType": 3,
              "parentMenuId": "2",
              "menuUrl": null,
              "menuIcon": null,
              "perms": "sys:org:add",
              "sortOrder": 1,
              "visible": 1,
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

**权限标识**：`sys:menu:view`

---

### 5.2 新增菜单

**接口路径**：`POST /api/sys/menu`

**请求参数**：

```json
{
  "menuName": "机构管理",
  "menuType": 2,
  "parentMenuId": "1",
  "menuUrl": "/system/org",
  "menuIcon": "org",
  "perms": "sys:org:view",
  "sortOrder": 1,
  "visible": 1
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| menuName | String | 是 | 菜单名称 |
| menuType | Integer | 是 | 菜单类型：1-目录，2-菜单，3-按钮 |
| parentMenuId | String | 否 | 父菜单 ID（顶级菜单不传） |
| menuUrl | String | 否 | 菜单 URL/路由地址（目录和菜单必填） |
| menuIcon | String | 否 | 菜单图标（目录和菜单） |
| perms | String | 否 | 权限标识（按钮必填，如 sys:org:view） |
| sortOrder | Integer | 否 | 排序号 |
| visible | Integer | 否 | 是否可见：0-隐藏，1-显示 |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:menu:add`

---

### 5.3 修改菜单

**接口路径**：`PUT /api/sys/menu/{id}`

**路径参数**：

```
id: 菜单 ID
```

**请求参数**：

```json
{
  "menuName": "机构管理",
  "menuType": 2,
  "menuUrl": "/system/org",
  "menuIcon": "org",
  "perms": "sys:org:view",
  "sortOrder": 1,
  "visible": 1
}
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:menu:edit`

---

### 5.4 删除菜单

**接口路径**：`DELETE /api/sys/menu/{id}`

**路径参数**：

```
id: 菜单 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:menu:delete`

**错误提示**：
- 有子菜单：`"code": 400, "msg": "存在子菜单，无法删除"`
- 被角色使用：`"code": 400, "msg": "菜单被角色使用，无法删除"`

---

### 5.5 为菜单分配角色

**接口路径**：`POST /api/sys/menu/{id}/roles`

**路径参数**：

```
id: 菜单 ID
```

**请求参数**：

```json
{
  "roleIds": ["1", "2", "3"]
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleIds | Array | 是 | 角色 ID 列表 |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:menu:edit`

---

## 六、角色管理接口（v0.0.4）

### 6.1 查询角色列表

**接口路径**：`GET /api/sys/role/list`

**查询参数**：

```
pageNum=1&pageSize=10&roleStatus=1&roleName=管理员
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认 1 |
| pageSize | Integer | 否 | 每页数量，默认 10 |
| roleStatus | Integer | 否 | 角色状态：0-停用，1-启用 |
| roleName | String | 否 | 角色名称（模糊查询） |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [
      {
        "roleId": "1",
        "roleCode": "admin",
        "roleName": "系统管理员",
        "roleDesc": "拥有系统所有权限",
        "roleStatus": 1
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

**权限标识**：`sys:role:view`

---

### 6.2 新增角色

**接口路径**：`POST /api/sys/role`

**请求参数**：

```json
{
  "roleCode": "org_admin",
  "roleName": "机构管理员",
  "roleDesc": "管理机构用户",
  "roleStatus": 1
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleCode | String | 是 | 角色代码（唯一） |
| roleName | String | 是 | 角色名称 |
| roleDesc | String | 否 | 角色描述 |
| roleStatus | Integer | 否 | 角色状态：0-停用，1-启用 |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:role:add`

---

### 6.3 修改角色

**接口路径**：`PUT /api/sys/role/{id}`

**路径参数**：

```
id: 角色 ID
```

**请求参数**：

```json
{
  "roleName": "机构管理员",
  "roleDesc": "管理机构用户",
  "roleStatus": 1
}
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:role:edit`

---

### 6.4 删除角色

**接口路径**：`DELETE /api/sys/role/{id}`

**路径参数**：

```
id: 角色 ID
```

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:role:delete`

**错误提示**：
- 绑定用户：`"code": 400, "msg": "角色已绑定用户，无法删除"`

---

### 6.5 为角色分配菜单

**接口路径**：`POST /api/sys/role/{id}/menus`

**路径参数**：

```
id: 角色 ID
```

**请求参数**：

```json
{
  "menuIds": ["1", "2", "3", "4", "5"]
}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| menuIds | Array | 是 | 菜单 ID 列表（包括目录、菜单、按钮） |

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

**权限标识**：`sys:role:edit`

---

## 七、权限控制接口（内部使用）

### 7.1 获取用户菜单树

**接口路径**：`GET /api/sys/permission/menus`

**说明**：根据当前登录用户的角色，返回可访问的菜单树

**返回参数**：同 5.1 获取菜单树

**权限标识**：无（登录即可访问）

---

### 7.2 获取用户权限标识

**接口路径**：`GET /api/sys/permission/perms`

**说明**：根据当前登录用户的角色，返回所有权限标识

**返回参数**：

```json
{
  "code": 200,
  "msg": "success",
  "data": ["sys:org:view", "sys:org:add", "sys:org:edit", "sys:org:delete"]
}
```

**权限标识**：无（登录即可访问）

---

## 八、错误码说明

### 8.1 业务错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 参数错误 |
| 401 | 未登录或 Token 过期 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 8.2 业务提示信息

| 场景 | 提示信息 |
|------|----------|
| 机构代码重复 | "机构代码已存在" |
| 机构有下级 | "存在下级机构，无法删除" |
| 机构有用户 | "机构下有关联用户，无法删除" |
| 用户工号重复 | "用户工号已存在" |
| 用户有待办 | "用户有待办任务，无法删除" |
| 角色代码重复 | "角色代码已存在" |
| 角色绑定用户 | "角色已绑定用户，无法删除" |
| 菜单有子菜单 | "存在子菜单，无法删除" |
| 菜单被使用 | "菜单被角色使用，无法删除" |

---

## 九、权限标识汇总

### 9.1 机构管理

| 权限标识 | 说明 | 对应接口 |
|----------|------|----------|
| sys:org:view | 查看机构 | GET /api/sys/org/tree |
| sys:org:add | 新增机构 | POST /api/sys/org |
| sys:org:edit | 编辑机构 | PUT、PATCH /api/sys/org/* |
| sys:org:delete | 删除机构 | DELETE /api/sys/org/* |

### 9.2 用户管理

| 权限标识 | 说明 | 对应接口 |
|----------|------|----------|
| sys:user:view | 查看用户 | GET /api/sys/user/list |
| sys:user:add | 新增用户 | POST /api/sys/user |
| sys:user:edit | 编辑用户 | PUT、PATCH /api/sys/user/* |
| sys:user:delete | 删除用户 | DELETE /api/sys/user/* |

### 9.3 菜单管理

| 权限标识 | 说明 | 对应接口 |
|----------|------|----------|
| sys:menu:view | 查看菜单 | GET /api/sys/menu/tree |
| sys:menu:add | 新增菜单 | POST /api/sys/menu |
| sys:menu:edit | 编辑菜单 | PUT、POST /api/sys/menu/* |
| sys:menu:delete | 删除菜单 | DELETE /api/sys/menu/* |

### 9.4 角色管理

| 权限标识 | 说明 | 对应接口 |
|----------|------|----------|
| sys:role:view | 查看角色 | GET /api/sys/role/list |
| sys:role:add | 新增角色 | POST /api/sys/role |
| sys:role:edit | 编辑角色 | PUT、POST /api/sys/role/* |
| sys:role:delete | 删除角色 | DELETE /api/sys/role/* |

---

## 十、接口变更历史

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0.0 | 2026-03-07 | 初始版本 | 后端开发 Agent |

---

## 十一、前端对接注意事项

### 11.1 Token 处理

1. 登录成功后，将 token 存储到 localStorage 或 sessionStorage
2. 所有请求头中携带 token：`Authorization: Bearer {token}`
3. Token 过期时，返回 401 状态码，前端跳转到登录页

### 11.2 权限处理

1. 登录接口返回 `menus` 和 `perms`
2. 根据 `menus` 动态生成路由和菜单
3. 根据 `perms` 控制按钮显示/隐藏

### 11.3 树形数据处理

1. 机构树、菜单树接口返回嵌套结构
2. 前端使用树形组件展示（如 Element Plus 的 el-tree）

### 11.4 分页处理

1. 所有列表接口统一返回分页对象
2. 前端使用分页组件展示

### 11.5 错误处理

1. 后端返回 400/403/500 等错误码时，前端统一弹出错误提示
2. 业务错误信息在后端生成，前端直接展示

---

**编制人**：后端开发 Agent  
**编制日期**：2026-03-07  
**版本状态**：✅ 待与前端核对  
**下一步**：与前端团队确认接口定义，如有调整及时更新文档
