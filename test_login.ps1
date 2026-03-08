$body = @{
    userCode = "admin"
    password = "123456"
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body ($body | ConvertTo-Json)
    
    Write-Host "Status: $($response.StatusCode)"
    Write-Host "Response:"
    Write-Host $response.Content
} catch {
    Write-Host "Error: $_"
    Write-Host "Exception: $($_.Exception.Message)"
}