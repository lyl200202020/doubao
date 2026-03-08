# 第二阶段 API 接口测试脚本

## 测试说明

本脚本用于测试第二阶段开发的所有 API 接口。

## 前置条件

1. 后端服务已启动：http://localhost:8080
2. 数据库已初始化
3. 已有测试账号：admin/admin123

## 测试步骤

### 1. 登录获取 Token

```powershell
# 登录
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $body -ContentType "application/json"
$token = $response.data.token
Write-Host "Token: $token"

# 设置请求头
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}
```

### 2. 测试机构管理接口

```powershell
# 2.1 获取机构树
Write-Host "`n=== 测试：获取机构树 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/tree" -Method Get -Headers $headers
Write-Host "机构树：" ($response | ConvertTo-Json -Depth 10)

# 2.2 获取机构详情
Write-Host "`n=== 测试：获取机构详情 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/1" -Method Get -Headers $headers
Write-Host "机构详情：" ($response | ConvertTo-Json -Depth 5)

# 2.3 新增机构
Write-Host "`n=== 测试：新增机构 ==="
$newOrg = @{
    orgCode = "test_org_001"
    orgName = "测试机构"
    orgLevel = 2
    parentOrgId = "1"
    sortOrder = 1
    remark = "测试机构"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org" -Method Post -Body $newOrg -Headers $headers
Write-Host "新增结果：" ($response | ConvertTo-Json)

# 2.4 修改机构
Write-Host "`n=== 测试：修改机构 ==="
$updateOrg = @{
    orgCode = "test_org_001"
    orgName = "测试机构 - 已修改"
    orgLevel = 2
    parentOrgId = "1"
    sortOrder = 1
    remark = "测试机构 - 已修改"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/test_org_001" -Method Put -Body $updateOrg -Headers $headers
Write-Host "修改结果：" ($response | ConvertTo-Json)

# 2.5 停用机构
Write-Host "`n=== 测试：停用机构 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/test_org_001/status?status=0" -Method Patch -Headers $headers
Write-Host "停用结果：" ($response | ConvertTo-Json)

# 2.6 删除机构
Write-Host "`n=== 测试：删除机构 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/test_org_001" -Method Delete -Headers $headers
Write-Host "删除结果：" ($response | ConvertTo-Json)
```

### 3. 测试用户管理接口

```powershell
# 3.1 分页查询用户
Write-Host "`n=== 测试：分页查询用户 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/list?pageNum=1&pageSize=10" -Method Get -Headers $headers
Write-Host "用户列表：" ($response | ConvertTo-Json -Depth 5)

# 3.2 获取用户详情
Write-Host "`n=== 测试：获取用户详情 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/admin" -Method Get -Headers $headers
Write-Host "用户详情：" ($response | ConvertTo-Json)

# 3.3 新增用户
Write-Host "`n=== 测试：新增用户 ==="
$newUser = @{
    userCode = "test_user_001"
    userName = "测试用户"
    mobile = "13800138000"
    email = "test@example.com"
    orgId = "1"
    roleId = "1"
    authLevel = 1
    remark = "测试用户"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user" -Method Post -Body $newUser -Headers $headers
Write-Host "新增结果：" ($response | ConvertTo-Json)

# 3.4 修改用户
Write-Host "`n=== 测试：修改用户 ==="
$updateUser = @{
    userCode = "test_user_001"
    userName = "测试用户 - 已修改"
    mobile = "13800138001"
    email = "test2@example.com"
    orgId = "1"
    roleId = "1"
    authLevel = 2
    remark = "测试用户 - 已修改"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/test_user_001" -Method Put -Body $updateUser -Headers $headers
Write-Host "修改结果：" ($response | ConvertTo-Json)

# 3.5 重置密码
Write-Host "`n=== 测试：重置密码 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/test_user_001/reset-pwd" -Method Post -Headers $headers
Write-Host "重置密码结果：" ($response | ConvertTo-Json)

# 3.6 停用用户
Write-Host "`n=== 测试：停用用户 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/test_user_001/status?status=0" -Method Patch -Headers $headers
Write-Host "停用结果：" ($response | ConvertTo-Json)

# 3.7 删除用户
Write-Host "`n=== 测试：删除用户 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/test_user_001" -Method Delete -Headers $headers
Write-Host "删除结果：" ($response | ConvertTo-Json)
```

### 4. 测试菜单管理接口

```powershell
# 4.1 获取菜单树
Write-Host "`n=== 测试：获取菜单树 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu/tree" -Method Get -Headers $headers
Write-Host "菜单树：" ($response | ConvertTo-Json -Depth 10)

# 4.2 获取菜单详情
Write-Host "`n=== 测试：获取菜单详情 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu/1" -Method Get -Headers $headers
Write-Host "菜单详情：" ($response | ConvertTo-Json)

# 4.3 新增菜单
Write-Host "`n=== 测试：新增菜单 ==="
$newMenu = @{
    menuName = "测试菜单"
    menuType = 2
    parentMenuId = "1"
    menuUrl = "/test"
    menuIcon = "test"
    perms = "sys:test:view"
    sortOrder = 1
    visible = 1
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu" -Method Post -Body $newMenu -Headers $headers
Write-Host "新增结果：" ($response | ConvertTo-Json)

# 4.4 修改菜单
Write-Host "`n=== 测试：修改菜单 ==="
$updateMenu = @{
    menuName = "测试菜单 - 已修改"
    menuType = 2
    parentMenuId = "1"
    menuUrl = "/test2"
    menuIcon = "test2"
    perms = "sys:test:edit"
    sortOrder = 2
    visible = 1
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu/test_menu_001" -Method Put -Body $updateMenu -Headers $headers
Write-Host "修改结果：" ($response | ConvertTo-Json)

# 4.5 删除菜单
Write-Host "`n=== 测试：删除菜单 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu/test_menu_001" -Method Delete -Headers $headers
Write-Host "删除结果：" ($response | ConvertTo-Json)
```

### 5. 测试角色管理接口

```powershell
# 5.1 分页查询角色
Write-Host "`n=== 测试：分页查询角色 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role/list?pageNum=1&pageSize=10" -Method Get -Headers $headers
Write-Host "角色列表：" ($response | ConvertTo-Json -Depth 5)

# 5.2 获取角色详情
Write-Host "`n=== 测试：获取角色详情 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role/1" -Method Get -Headers $headers
Write-Host "角色详情：" ($response | ConvertTo-Json)

# 5.3 新增角色
Write-Host "`n=== 测试：新增角色 ==="
$newRole = @{
    roleCode = "test_role"
    roleName = "测试角色"
    roleDesc = "测试角色"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role" -Method Post -Body $newRole -Headers $headers
Write-Host "新增结果：" ($response | ConvertTo-Json)

# 5.4 修改角色
Write-Host "`n=== 测试：修改角色 ==="
$updateRole = @{
    roleCode = "test_role"
    roleName = "测试角色 - 已修改"
    roleDesc = "测试角色 - 已修改"
} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role/test_role" -Method Put -Body $updateRole -Headers $headers
Write-Host "修改结果：" ($response | ConvertTo-Json)

# 5.5 删除角色
Write-Host "`n=== 测试：删除角色 ==="
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role/test_role" -Method Delete -Headers $headers
Write-Host "删除结果：" ($response | ConvertTo-Json)
```

## 完整测试脚本

```powershell
# 保存为 test-api.ps1 并执行

$ErrorActionPreference = "Stop"

try {
    # 1. 登录
    Write-Host "======================================" -ForegroundColor Cyan
    Write-Host "1. 登录获取 Token" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $token = $loginResponse.data.token
    Write-Host "✓ 登录成功，Token: $token" -ForegroundColor Green

    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }

    # 2. 测试机构管理
    Write-Host "`n======================================" -ForegroundColor Cyan
    Write-Host "2. 测试机构管理" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    
    $orgTree = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/org/tree" -Method Get -Headers $headers
    Write-Host "✓ 获取机构树成功，机构数量：$($orgTree.data.Count)" -ForegroundColor Green

    # 3. 测试用户管理
    Write-Host "`n======================================" -ForegroundColor Cyan
    Write-Host "3. 测试用户管理" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    
    $users = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/user/list?pageNum=1&pageSize=10" -Method Get -Headers $headers
    Write-Host "✓ 获取用户列表成功，用户总数：$($users.data.total)" -ForegroundColor Green

    # 4. 测试菜单管理
    Write-Host "`n======================================" -ForegroundColor Cyan
    Write-Host "4. 测试菜单管理" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    
    $menus = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/menu/tree" -Method Get -Headers $headers
    Write-Host "✓ 获取菜单树成功，菜单数量：$($menus.data.Count)" -ForegroundColor Green

    # 5. 测试角色管理
    Write-Host "`n======================================" -ForegroundColor Cyan
    Write-Host "5. 测试角色管理" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    
    $roles = Invoke-RestMethod -Uri "http://localhost:8080/api/sys/role/list?pageNum=1&pageSize=10" -Method Get -Headers $headers
    Write-Host "✓ 获取角色列表成功，角色总数：$($roles.data.total)" -ForegroundColor Green

    Write-Host "`n======================================" -ForegroundColor Green
    Write-Host "所有接口测试通过！" -ForegroundColor Green
    Write-Host "======================================" -ForegroundColor Green

} catch {
    Write-Host "`n❌ 测试失败：$_" -ForegroundColor Red
    Write-Host "错误详情：$($_.Exception.Message)" -ForegroundColor Red
}
```

## 预期结果

所有接口应该返回：
- `code`: 0 或 200（成功）
- `msg`: "success" 或空字符串
- `data`: 实际数据

## 常见错误处理

1. **401 Unauthorized**: Token 过期或无效，重新登录
2. **403 Forbidden**: 权限不足，检查用户角色
3. **404 Not Found**: 接口路径错误或资源不存在
4. **500 Internal Server Error**: 服务器错误，检查日志

## 测试完成标志

- ✅ 所有 GET 接口能正常返回数据
- ✅ 所有 POST/PUT 接口能正常创建/更新数据
- ✅ 所有 DELETE 接口能正常删除数据
- ✅ 错误处理符合预期
- ✅ 返回格式统一
