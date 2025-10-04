Param(
    [string]$Target = ".\target",
    [int]$Retries = 10,
    [int]$DelaySeconds = 1
)

try {
    $full = Resolve-Path -Path $Target -ErrorAction Stop
} catch {
    Write-Host "Target path '$Target' not found. Nothing to delete." -ForegroundColor Yellow
    exit 0
}

Write-Host "Attempting to force-remove $full"

function Set-NormalAttributes($path) {
    Get-ChildItem -Path $path -Recurse -Force -ErrorAction SilentlyContinue | ForEach-Object {
        try { $_.Attributes = 'Normal' } catch {}
    }
}

function Remove-WithRetry($path) {
    for ($i=0; $i -lt $Retries; $i++) {
        try {
            Remove-Item -LiteralPath $path -Recurse -Force -ErrorAction Stop
            Write-Host "Deleted $path" -ForegroundColor Green
            return $true
        } catch {
            Write-Host "Delete failed (attempt $($i+1)/$Retries): $($_.Exception.Message)" -ForegroundColor Yellow
            Start-Sleep -Seconds $DelaySeconds
            # attempt to clear attributes again before retrying
            try { Set-NormalAttributes $path } catch {}
        }
    }
    return $false
}

# Ensure attributes are writable (clears read-only)
Set-NormalAttributes $full

if (-not (Remove-WithRetry $full)) {
    Write-Host "Failed to delete after $Retries attempts." -ForegroundColor Red
    Write-Host "Trying to show which processes may be locking files using Sysinternals 'handle.exe' (if installed)." -ForegroundColor Yellow

    $possibleHandles = @(
        "$env:ProgramFiles\Sysinternals\handle.exe",
        "$env:ProgramFiles(x86)\Sysinternals\handle.exe",
        "$env:ProgramFiles\SysinternalsSuite\handle.exe",
        "$env:ProgramFiles(x86)\SysinternalsSuite\handle.exe",
        "C:\\Windows\\System32\\handle.exe"
    )

    $found = $false
    foreach ($h in $possibleHandles) {
        if (Test-Path $h) {
            $found = $true
            Write-Host "Running: $h -accepteula $full" -ForegroundColor Cyan
            & $h -accepteula $full
        }
    }

    if (-not $found) {
        Write-Host "handle.exe not found. Use Resource Monitor (resmon) or Process Explorer to find file locks, then close the process or reboot." -ForegroundColor Yellow
        Write-Host "Common culprits: your IDE (IntelliJ), a running Minecraft server, or an attached debugger." -ForegroundColor Yellow
    }

    exit 1
}

exit 0

