@echo off
REM Wrapper to run the PowerShell conversion-with-log script from cmd.exe
SETLOCAL
if not exist "%~dp0convert-java-to-utf8-with-log.ps1" (
    echo Error: convert-java-to-utf8-with-log.ps1 not found in %~dp0
    exit /b 1
)
echo Running convert-java-to-utf8-with-log.ps1 (produces a timestamped log)...
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0convert-java-to-utf8-with-log.ps1" %*
ENDLOCAL

