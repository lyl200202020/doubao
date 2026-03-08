$mysql = "D:\tools\mysql-8.4.3-winx64\bin\mysql.exe"
$args = @(
    "-h", "113.221.57.26",
    "-P", "3306",
    "-u", "app",
    "-p123456",
    "-e", "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME IN ('cust', 'guanli')"
)
& $mysql $args
