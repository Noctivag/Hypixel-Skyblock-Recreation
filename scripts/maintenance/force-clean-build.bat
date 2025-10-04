@echo off
echo Cleaning up Maven and Java processes...

REM Kill any running Maven processes
taskkill /F /IM mvn.exe 2>nul
taskkill /F /IM java.exe 2>nul

REM Wait for processes to fully terminate
timeout /t 2 /nobreak >nul

REM Ensure target directory is unlocked and remove it
echo Removing target directory...
rd /s /q "target" 2>nul

REM Wait for filesystem
timeout /t 1 /nobreak >nul

REM Run Maven clean install with file locking debug
echo Running Maven build...
call mvnw.cmd -Dorg.apache.maven.fileLockDebug=true clean install

echo Build complete.
pause
