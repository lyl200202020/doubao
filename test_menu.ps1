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
    Write-Host "Token: $($data.token.Substring(0, 20))..."
    Write-Host "User: $($data.userName)"
    Write-Host "Permissions count: $($data.permissions.Count)"
    
    Write-Host "`nMenus:"
    $data.menus | ForEach-Object {
        Write-Host "  - $($_.menuName) ($($_.menuType)) - $($_.menuUrl)"
        if ($_.children) {
            $_.children | ForEach-Object {
                Write-Host "    - $($_.menuName) ($($_.menuType)) - $($_.menuUrl)"
            }
        }
    }
} catch {
    Write-Host "Error: $_"
    Write-Host "Exception: $($_.Exception.Message)"
}