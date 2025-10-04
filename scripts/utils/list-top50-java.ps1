# list-top50-java.ps1
# Lists the top 50 largest .java files under the current repository root.
# Usage (from repo root):
#   powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\list-top50-java.ps1

$limit = 50
n
Write-Output "Scanning for .java files..."
Get-ChildItem -Path . -Recurse -Filter *.java -ErrorAction SilentlyContinue |
    Where-Object { -not $_.PSIsContainer } |
    Sort-Object Length -Descending |
    Select-Object -First $limit |
    ForEach-Object {
        $mb = [math]::Round($_.Length / 1MB, 3)
        "{0,-10} MB {1}" -f $mb, $_.FullName
    } | Out-Host

Write-Output "Done."

