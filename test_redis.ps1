$redisHost = "113.221.57.26"
$redisPort = 6379
$redisPassword = "mm123456"

try {
    $tcp = New-Object System.Net.Sockets.TcpClient
    $tcp.Connect($redisHost, $redisPort)
    $tcp.Close()
    Write-Host "Redis connection successful: ${redisHost}:${redisPort}"
} catch {
    Write-Host "Redis connection failed: ${redisHost}:${redisPort}"
    Write-Host "Error: $_"
}