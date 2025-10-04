# convert-java-to-utf8.ps1
# Re-saves all .java files as UTF-8 without BOM.
# It reads files as bytes to avoid read errors on mixed encodings, strips a UTF-8 BOM if present,
# and writes them back using UTF8 without BOM.
# Usage (from repo root in cmd.exe):
#   powershell -ExecutionPolicy Bypass -File .\scripts\convert-java-to-utf8.ps1

param(
    [switch]$WhatIf
)

Write-Output "Converting all .java files to UTF-8 (no BOM)..."
Get-ChildItem -Path . -Recurse -Filter *.java | ForEach-Object {
    $path = $_.FullName
    try {
        $bytes = [System.IO.File]::ReadAllBytes($path)
        if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
            # Strip BOM
            $bytes = $bytes[3..($bytes.Length - 1)]
        }
        $text = [System.Text.Encoding]::UTF8.GetString($bytes)
        if ($WhatIf) {
            Write-Output "Would rewrite: $path"
        } else {
            # Use UTF8Encoding(false) to write without BOM
            [System.IO.File]::WriteAllText($path, $text, New-Object System.Text.UTF8Encoding($false))
            Write-Output "Rewrote: $path"
        }
    } catch {
        Write-Output "Failed to process $path: $_"
    }
}
Write-Output "Conversion finished."
