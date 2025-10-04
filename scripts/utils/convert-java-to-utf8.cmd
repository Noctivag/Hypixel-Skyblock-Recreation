@echo off
REM Wrapper to run the PowerShell UTF-8 conversion script from cmd.exe
SETLOCAL
if not exist "%~dp0convert-java-to-utf8.ps1" (
    echo Error: convert-java-to-utf8.ps1 not found in %~dp0
    exit /b 1
)
echo Running PowerShell script to convert .java files to UTF-8 without BOM...
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0convert-java-to-utf8.ps1" %*
ENDLOCAL

