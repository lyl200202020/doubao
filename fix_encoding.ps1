$files = @(
    "d:\study\doubao\customer-admin\customer-admin-platform\platform-biz\src\main\java\com\company\admin\platform\sys\interface\controller\AuthController.java",
    "d:\study\doubao\customer-admin\customer-admin-platform\platform-biz\src\main\java\com\company\admin\platform\sys\interface\vo\LoginVO.java"
)
foreach ($file in $files) {
    $content = Get-Content $file -Raw -Encoding UTF8
    [System.IO.File]::WriteAllText($file, $content, (New-Object System.Text.UTF8Encoding $false))
    Write-Host "Fixed: $file"
}
