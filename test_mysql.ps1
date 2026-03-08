$ErrorActionPreference = "Stop"
[void][System.Reflection.Assembly]::LoadWithPartialName("MySql.Data")
$connectionString = "Server=113.221.57.26;Port=3306;Database=cust;Uid=app;Pwd=123456;"
$connection = New-Object MySql.Data.MySqlClient.MySqlConnection($connectionString)
try {
    $connection.Open()
    Write-Host "Connection OK!"
    $query = "SHOW DATABASES"
    $command = New-Object MySql.Data.MySqlClient.MySqlCommand($query, $connection)
    $reader = $command.ExecuteReader()
    while ($reader.Read()) {
        Write-Host $reader[0]
    }
    $reader.Close()
} catch {
    Write-Host "Connection failed: $_"
} finally {
    $connection.Close()
}
