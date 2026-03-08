# 简单的登录测试
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$baseUrl = "http://localhost:8080"

Write-Host "测试登录接口..." -ForegroundColor Yellow

$loginBody = @{
    userCode = "admin"
    password = "admin123"
} | ConvertTo-Json

Write-Host "请求体: $loginBody" -ForegroundColor Gray

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "响应: $($loginResponse | ConvertTo-Json -Depth 10)" -ForegroundColor Gray
    
    if ($loginResponse.code -eq 200) {
        Write-Host "✅ 登录成功" -ForegroundColor Green
        Write-Host "Token: $($loginResponse.data.token)" -ForegroundColor Cyan
        Write-Host "用户: $($loginResponse.data.userName)" -ForegroundColor Cyan
        Write-Host "机构: $($loginResponse.data.orgName)" -ForegroundColor Cyan
    } else {
        Write-Host "❌ 登录失败: $($loginResponse.msg)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ 登录请求失败: $_" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}
