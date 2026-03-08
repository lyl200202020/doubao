$ErrorActionPreference = "SilentlyContinue"
try {
    $redis = Test-NetConnection -ComputerName "113.221.57.26" -Port 6379
    if ($redis.TcpTestSucceeded) {
        Write-Host "Redis connection OK!"
    } else {
        Write-Host "Redis connection failed"
    }
} catch {
    Write-Host "Error: $_"
}
