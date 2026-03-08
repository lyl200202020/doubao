$mysql = "D:\tools\mysql-8.4.3-winx64\bin\mysql.exe"
$args = @(
    "-h", "113.221.57.26",
    "-P", "3306",
    "-u", "app",
    "-p123456",
    "cust",
    "-e", "SELECT 'cust database OK' as result"
)
& $mysql $args
