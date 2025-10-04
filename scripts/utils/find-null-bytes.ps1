# find-null-bytes.ps1
# Scans recursively for .java files that contain NUL (0x00) bytes and prints them.
# Usage (from repo root in cmd.exe):
#   powershell -ExecutionPolicy Bypass -File .\scripts\find-null-bytes.ps1

Write-Output "Scanning for .java files containing NUL bytes..."
Get-ChildItem -Path . -Recurse -Filter *.java | ForEach-Object {
    try {
        $bytes = [System.IO.File]::ReadAllBytes($_.FullName)
        if ($bytes -contains 0) {
            Write-Output "NUL byte(s) found in: $($_.FullName)"
        }
    } catch {
        Write-Output "Failed to read $($_.FullName): $_"
    }
}
Write-Output "Scan complete."
