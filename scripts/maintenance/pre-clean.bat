::: Pre-cleanup script to ensure resources are released
@echo off
setlocal enabledelayedexpansion

echo Running pre-build cleanup...

:: Clear any Maven daemon processes
echo Stopping Maven processes...
taskkill /F /IM mvn.exe /T >nul 2>&1
taskkill /F /IM java.exe /T >nul 2>&1

:: Remove target directory with extra retries
echo Removing target directory...
set max_attempts=5
set attempt_num=1

:RETRY_REMOVE
rd /s /q "target" 2>nul
if not exist "target" goto REMOVE_SUCCESS
if !attempt_num! geq !max_attempts! goto REMOVE_FAILED
set /a attempt_num+=1
timeout /t 2 /nobreak >nul
goto RETRY_REMOVE

:REMOVE_FAILED
echo WARNING: Could not fully remove target directory
echo Continuing anyway...
goto END

:REMOVE_SUCCESS
echo Target directory removed successfully

:END
echo Pre-cleanup complete.
timeout /t 2 /nobreak >nul
