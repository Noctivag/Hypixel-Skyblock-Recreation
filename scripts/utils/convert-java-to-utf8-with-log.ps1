# convert-java-to-utf8-with-log.ps1
# Wrapper around convert-java-to-utf8.ps1 that records SHA256 hashes before and after
# and writes a timestamped log listing which .java files were changed.
# Usage (from repo root):
#   powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\convert-java-to-utf8-with-log.ps1

$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path "$(Join-Path $PSScriptRoot "..")"
$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$logPath = Join-Path $PSScriptRoot "convert-java-to-utf8-log-$timestamp.txt"

Write-Output "Scanning .java files and computing pre-conversion hashes..."
$javaFiles = Get-ChildItem -Path $repoRoot -Recurse -Filter *.java -ErrorAction SilentlyContinue | Where-Object { -not $_.PSIsContainer }
$before = @{}
foreach ($f in $javaFiles) {
    try {
        $h = Get-FileHash -Path $f.FullName -Algorithm SHA256
        $before[$f.FullName] = $h.Hash
    } catch {
        $before[$f.FullName] = $null
    }
}

# Run the existing converter script (which rewrites files as UTF8 without BOM)
$converter = Join-Path $PSScriptRoot "convert-java-to-utf8.ps1"
if (-Not (Test-Path $converter)) {
    Write-Error "Converter script not found at $converter"
    exit 1
}

Write-Output "Running converter: $converter"
try {
    & $converter
} catch {
    Write-Error "Converter script failed: $_"
    exit 2
}

Write-Output "Computing post-conversion hashes..."
# Re-scan files to catch new/removed files as well
$javaFilesAfter = Get-ChildItem -Path $repoRoot -Recurse -Filter *.java -ErrorAction SilentlyContinue | Where-Object { -not $_.PSIsContainer }
$after = @{}
foreach ($f in $javaFilesAfter) {
    try {
        $h = Get-FileHash -Path $f.FullName -Algorithm SHA256
        $after[$f.FullName] = $h.Hash
    } catch {
        $after[$f.FullName] = $null
    }
}

$changed = @()
$deleted = @()
$new = @()

foreach ($path in $before.Keys) {
    if (-not $after.ContainsKey($path)) {
        $deleted += $path
    } elseif ($before[$path] -ne $after[$path]) {
        $changed += $path
    }
}
foreach ($path in $after.Keys) {
    if (-not $before.ContainsKey($path)) {
        $new += $path
    }
}

$report = @()
$report += "Convert Java to UTF-8 without BOM - Log: $timestamp"
$report += "Repository root: $repoRoot"
$report += "Converter script: $converter"
$report += ""
$report += "Summary:"
$report += "  Total scanned before: $($before.Keys.Count)"
$report += "  Total scanned after:  $($after.Keys.Count)"
$report += "  Files changed:         $($changed.Count)"
$report += "  Files created:         $($new.Count)"
$report += "  Files deleted:         $($deleted.Count)"
$report += ""
if ($changed.Count -gt 0) {
    $report += "Changed files:"
    foreach ($p in $changed) { $report += "  $p" }
}
if ($new.Count -gt 0) {
    $report += "New files:"
    foreach ($p in $new) { $report += "  $p" }
}
if ($deleted.Count -gt 0) {
    $report += "Deleted files:"
    foreach ($p in $deleted) { $report += "  $p" }
}

$report | Out-File -FilePath $logPath -Encoding UTF8

Write-Output "Conversion finished. Log written to: $logPath"
Write-Output "Files changed: $($changed.Count)  Created: $($new.Count)  Deleted: $($deleted.Count)"

exit 0

