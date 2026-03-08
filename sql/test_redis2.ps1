$tcp = New-Object System.Net.Sockets.TcpClient
try {
    $tcp.Connect("113.221.57.26", 6379)
    Write-Host "Redis connection OK! Port 6379 is reachable"
    $tcp.Close()
} catch {
    Write-Host "Redis connection failed: $_"
}
