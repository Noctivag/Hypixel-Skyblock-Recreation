# Remove UTF-8 BOM (EF BB BF) from text files recursively
# Usage: powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\remove-bom.ps1

$extensions = @('*.java','*.properties','*.yml','*.yaml','*.txt','*.md','*.json','*.xml','*.gradle')
$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Write-Host "Scanning for files with BOM in $repoRoot ..."
$files = @()
foreach ($ext in $extensions) {
    $files += Get-ChildItem -Path $repoRoot -Filter $ext -Recurse -ErrorAction SilentlyContinue | Where-Object { -not $_.PSIsContainer }
}

$modified = 0
foreach ($file in $files) {
    try {
        $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
        if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
            Write-Host "Removing BOM: $($file.FullName)"
            $newBytes = $bytes[3..($bytes.Length - 1)]
            [System.IO.File]::WriteAllBytes($file.FullName, $newBytes)
            $modified++
        }
    } catch {
        Write-Warning "Failed to process $($file.FullName): $_"
    }
}

if ($modified -eq 0) {
    Write-Host "No BOMs found."
} else {
    Write-Host "Removed BOM from $modified files."
}

