$body = @{
    userCode = "admin"
    password = "123456"
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body ($body | ConvertTo-Json)
    
    $data = $response.Content | ConvertFrom-Json
    
    Write-Host "Login successful!"
    Write-Host "User: $($data.userName)"
    Write-Host "`nPermissions:"
    $data.permissions | ForEach-Object {
        Write-Host "  - $_"
    }
    
    Write-Host "`nTotal permissions: $($data.permissions.Count)"
    
    Write-Host "`nChecking for user permissions:"
    $userPermissions = @("sys:user:list", "sys:user:add", "sys:user:edit", "sys:user:delete", "sys:user:resetPwd")
    foreach ($perm in $userPermissions) {
        if ($data.permissions -contains $perm) {
            Write-Host "  ✓ $perm"
        } else {
            Write-Host "  ✗ $perm (MISSING)"
        }
    }
} catch {
    Write-Host "Error: $_"
    Write-Host "Exception: $($_.Exception.Message)"
}