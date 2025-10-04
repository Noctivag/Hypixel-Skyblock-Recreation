@echo off
:: Wrapper for running the PowerShell force-clean script from CMD
:: Usage: force-clean.cmd [target-path] [retries] [delaySeconds]
setlocal
set SCRIPT_DIR=%~dp0
set PS1=%SCRIPT_DIR%force-clean.ps1
if not exist "%PS1%" (
  echo PowerShell script not found: "%PS1%"
  exit /b 1
)

rem Pass through arguments if provided
powershell -NoProfile -ExecutionPolicy Bypass -File "%PS1%" %*
exit /b %ERRORLEVEL%

