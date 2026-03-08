$redisHost = "113.221.57.26"
$ports = @(6379, 6378, 6380)

foreach ($port in $ports) {
    try {
        $tcp = New-Object System.Net.Sockets.TcpClient
        $tcp.Connect($redisHost, $port)
        $tcp.Close()
        Write-Host "Redis connection successful: ${redisHost}:${port}"
    } catch {
        Write-Host "Redis connection failed: ${redisHost}:${port}"
    }
}