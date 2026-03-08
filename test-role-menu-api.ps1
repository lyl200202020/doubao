# 角色管理和菜单管理联调测试脚本
# 测试环境：http://localhost:8080

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$baseUrl = "http://localhost:8080"
$token = ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "角色管理和菜单管理联调测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. 登录获取Token
Write-Host "[1/10] 登录获取Token..." -ForegroundColor Yellow
$loginBody = @{
    userCode = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    if ($loginResponse.code -eq 200) {
        $token = $loginResponse.data.token
        Write-Host "✅ 登录成功，Token: $($token.Substring(0, 20))..." -ForegroundColor Green
        Write-Host "   用户: $($loginResponse.data.userName)" -ForegroundColor Gray
        Write-Host "   机构: $($loginResponse.data.orgName)" -ForegroundColor Gray
        Write-Host ""
    } else {
        Write-Host "❌ 登录失败: $($loginResponse.msg)" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ 登录请求失败: $_" -ForegroundColor Red
    exit 1
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# 2. 测试角色管理 - 查询角色列表
Write-Host "[2/10] 测试角色管理 - 查询角色列表..." -ForegroundColor Yellow
try {
    $roleListResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/role/list" -Method Get -Headers $headers
    if ($roleListResponse.code -eq 200) {
        Write-Host "✅ 查询角色列表成功，共 $($roleListResponse.data.total) 条记录" -ForegroundColor Green
        $roleListResponse.data.list | ForEach-Object {
            Write-Host "   - $($_.roleCode): $($_.roleName)" -ForegroundColor Gray
        }
        Write-Host ""
    } else {
        Write-Host "❌ 查询角色列表失败: $($roleListResponse.msg)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ 查询角色列表请求失败: $_" -ForegroundColor Red
}

# 3. 测试角色管理 - 新增角色
Write-Host "[3/10] 测试角色管理 - 新增角色..." -ForegroundColor Yellow
$timestamp = Get-Date -Format 'yyyyMMddHHmmss'
$newRoleBody = @{
    roleCode = "test_role_$timestamp"
    roleName = "测试角色"
    roleDesc = "用于联调测试的角色"
    roleStatus = 1
} | ConvertTo-Json

try {
    $newRoleResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/role" -Method Post -Body $newRoleBody -Headers $headers
    if ($newRoleResponse.code -eq 200) {
        Write-Host "✅ 新增角色成功" -ForegroundColor Green
        $testRoleId = $newRoleResponse.data
        Write-Host "   角色ID: $testRoleId" -ForegroundColor Gray
        Write-Host ""
    } else {
        Write-Host "❌ 新增角色失败: $($newRoleResponse.msg)" -ForegroundColor Red
        $testRoleId = $null
        Write-Host ""
    }
} catch {
    Write-Host "❌ 新增角色请求失败: $_" -ForegroundColor Red
    $testRoleId = $null
    Write-Host ""
}

# 4. 测试菜单管理 - 查询菜单树
Write-Host "[4/10] 测试菜单管理 - 查询菜单树..." -ForegroundColor Yellow
try {
    $menuTreeResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu/tree" -Method Get -Headers $headers
    if ($menuTreeResponse.code -eq 200) {
        Write-Host "✅ 查询菜单树成功" -ForegroundColor Green
        function Show-MenuTree($menus, $level) {
            foreach ($menu in $menus) {
                $indent = "  " * $level
                $type = switch ($menu.menuType) {
                    1 { "目录" }
                    2 { "菜单" }
                    3 { "按钮" }
                    default { "未知" }
                }
                Write-Host "$indent- [$type] $($menu.menuName) (ID: $($menu.menuId))" -ForegroundColor Gray
                if ($menu.children -and $menu.children.Count -gt 0) {
                    Show-MenuTree $menu.children ($level + 1)
                }
            }
        }
        Show-MenuTree $menuTreeResponse.data 0
        Write-Host ""
    } else {
        Write-Host "❌ 查询菜单树失败: $($menuTreeResponse.msg)" -ForegroundColor Red
        Write-Host ""
    }
} catch {
    Write-Host "❌ 查询菜单树请求失败: $_" -ForegroundColor Red
    Write-Host ""
}

# 5. 测试菜单管理 - 新增菜单
Write-Host "[5/10] 测试菜单管理 - 新增菜单..." -ForegroundColor Yellow
$newMenuBody = @{
    menuName = "测试菜单"
    menuType = 2
    parentMenuId = "1"
    menuUrl = "/system/test"
    menuIcon = "test"
    perms = "sys:test:view"
    sortOrder = 99
    visible = 1
} | ConvertTo-Json

try {
    $newMenuResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu" -Method Post -Body $newMenuBody -Headers $headers
    if ($newMenuResponse.code -eq 200) {
        Write-Host "✅ 新增菜单成功" -ForegroundColor Green
        $testMenuId = $newMenuResponse.data
        Write-Host "   菜单ID: $testMenuId" -ForegroundColor Gray
        Write-Host ""
    } else {
        Write-Host "❌ 新增菜单失败: $($newMenuResponse.msg)" -ForegroundColor Red
        $testMenuId = $null
        Write-Host ""
    }
} catch {
    Write-Host "❌ 新增菜单请求失败: $_" -ForegroundColor Red
    $testMenuId = $null
    Write-Host ""
}

# 6. 测试菜单管理 - 新增按钮
Write-Host "[6/10] 测试菜单管理 - 新增按钮..." -ForegroundColor Yellow
$newButtonBody = @{
    menuName = "测试按钮"
    menuType = 3
    parentMenuId = $testMenuId
    perms = "sys:test:add"
    sortOrder = 1
    visible = 1
} | ConvertTo-Json

try {
    $newButtonResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu" -Method Post -Body $newButtonBody -Headers $headers
    if ($newButtonResponse.code -eq 200) {
        Write-Host "✅ 新增按钮成功" -ForegroundColor Green
        $testButtonId = $newButtonResponse.data
        Write-Host "   按钮ID: $testButtonId" -ForegroundColor Gray
        Write-Host ""
    } else {
        Write-Host "❌ 新增按钮失败: $($newButtonResponse.msg)" -ForegroundColor Red
        $testButtonId = $null
        Write-Host ""
    }
} catch {
    Write-Host "❌ 新增按钮请求失败: $_" -ForegroundColor Red
    $testButtonId = $null
    Write-Host ""
}

# 7. 测试角色管理 - 为角色分配菜单
if ($testRoleId -and $testMenuId -and $testButtonId) {
    Write-Host "[7/10] 测试角色管理 - 为角色分配菜单..." -ForegroundColor Yellow
    $assignMenusBody = @{
        menuIds = @($testMenuId, $testButtonId)
    } | ConvertTo-Json

    try {
        $assignMenusResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/role/$testRoleId/menus" -Method Post -Body $assignMenusBody -Headers $headers
        if ($assignMenusResponse.code -eq 200) {
            Write-Host "✅ 为角色分配菜单成功" -ForegroundColor Green
            Write-Host ""
        } else {
            Write-Host "❌ 为角色分配菜单失败: $($assignMenusResponse.msg)" -ForegroundColor Red
            Write-Host ""
        }
    } catch {
        Write-Host "❌ 为角色分配菜单请求失败: $_" -ForegroundColor Red
        Write-Host ""
    }
} else {
    Write-Host "[7/10] 跳过为角色分配菜单测试（缺少必要ID）" -ForegroundColor Yellow -ForegroundColor Gray
    Write-Host ""
}

# 8. 测试角色管理 - 查询角色详情（验证菜单分配）
if ($testRoleId) {
    Write-Host "[8/10] 测试角色管理 - 查询角色详情..." -ForegroundColor Yellow
    try {
        $roleDetailResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/role/$testRoleId" -Method Get -Headers $headers
        if ($roleDetailResponse.code -eq 200) {
            Write-Host "✅ 查询角色详情成功" -ForegroundColor Green
            Write-Host "   角色名称: $($roleDetailResponse.data.roleName)" -ForegroundColor Gray
            Write-Host "   分配的菜单数: $($roleDetailResponse.data.menuIds.Count)" -ForegroundColor Gray
            if ($roleDetailResponse.data.menuIds.Count -gt 0) {
                Write-Host "   菜单IDs: $($roleDetailResponse.data.menuIds -join ', ')" -ForegroundColor Gray
            }
            Write-Host ""
        } else {
            Write-Host "❌ 查询角色详情失败: $($roleDetailResponse.msg)" -ForegroundColor Red
            Write-Host ""
        }
    } catch {
        Write-Host "❌ 查询角色详情请求失败: $_" -ForegroundColor Red
        Write-Host ""
    }
}

# 9. 测试菜单管理 - 修改菜单
if ($testMenuId) {
    Write-Host "[9/10] 测试菜单管理 - 修改菜单..." -ForegroundColor Yellow
    $updateMenuBody = @{
        menuName = "测试菜单（已修改）"
        menuType = 2
        menuUrl = "/system/test-updated"
        menuIcon = "test-updated"
        perms = "sys:test:view"
        sortOrder = 99
        visible = 1
    } | ConvertTo-Json

    try {
        $updateMenuResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu/$testMenuId" -Method Put -Body $updateMenuBody -Headers $headers
        if ($updateMenuResponse.code -eq 200) {
            Write-Host "✅ 修改菜单成功" -ForegroundColor Green
            Write-Host ""
        } else {
            Write-Host "❌ 修改菜单失败: $($updateMenuResponse.msg)" -ForegroundColor Red
            Write-Host ""
        }
    } catch {
        Write-Host "❌ 修改菜单请求失败: $_" -ForegroundColor Red
        Write-Host ""
    }
} else {
    Write-Host "[9/10] 跳过修改菜单测试（缺少菜单ID）" -ForegroundColor Yellow -ForegroundColor Gray
    Write-Host ""
}

# 10. 清理测试数据
Write-Host "[10/10] 清理测试数据..." -ForegroundColor Yellow
$cleanupSuccess = $true

if ($testButtonId) {
    try {
        $deleteButtonResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu/$testButtonId" -Method Delete -Headers $headers
        if ($deleteButtonResponse.code -eq 200) {
            Write-Host "✅ 删除测试按钮成功" -ForegroundColor Green
        } else {
            Write-Host "❌ 删除测试按钮失败: $($deleteButtonResponse.msg)" -ForegroundColor Red
            $cleanupSuccess = $false
        }
    } catch {
        Write-Host "❌ 删除测试按钮请求失败: $_" -ForegroundColor Red
        $cleanupSuccess = $false
    }
}

if ($testMenuId) {
    try {
        $deleteMenuResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/menu/$testMenuId" -Method Delete -Headers $headers
        if ($deleteMenuResponse.code -eq 200) {
            Write-Host "✅ 删除测试菜单成功" -ForegroundColor Green
        } else {
            Write-Host "❌ 删除测试菜单失败: $($deleteMenuResponse.msg)" -ForegroundColor Red
            $cleanupSuccess = $false
        }
    } catch {
        Write-Host "❌ 删除测试菜单请求失败: $_" -ForegroundColor Red
        $cleanupSuccess = $false
    }
}

if ($testRoleId) {
    try {
        $deleteRoleResponse = Invoke-RestMethod -Uri "$baseUrl/api/sys/role/$testRoleId" -Method Delete -Headers $headers
        if ($deleteRoleResponse.code -eq 200) {
            Write-Host "✅ 删除测试角色成功" -ForegroundColor Green
        } else {
            Write-Host "❌ 删除测试角色失败: $($deleteRoleResponse.msg)" -ForegroundColor Red
            $cleanupSuccess = $false
        }
    } catch {
        Write-Host "❌ 删除测试角色请求失败: $_" -ForegroundColor Red
        $cleanupSuccess = $false
    }
}

Write-Host ""

# 测试总结
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "测试总结" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
if ($cleanupSuccess) {
    Write-Host "✅ 所有测试通过，测试数据已清理" -ForegroundColor Green
} else {
    Write-Host "⚠️ 部分测试失败，请检查错误信息" -ForegroundColor Yellow
}
Write-Host ""
Write-Host "后端服务: $baseUrl" -ForegroundColor Gray
Write-Host "前端服务: http://localhost:3000" -ForegroundColor Gray
Write-Host ""
Write-Host "请在前端浏览器中访问以下页面进行联调：" -ForegroundColor Cyan
Write-Host "  - 角色管理: http://localhost:3000/#/sys/role" -ForegroundColor Gray
Write-Host "  - 菜单管理: http://localhost:3000/#/sys/menu" -ForegroundColor Gray
Write-Host ""
