@echo off
echo Fixing encoding issues for Java compilation...

REM Clean previous builds
echo Cleaning previous builds...
mvn clean

REM Set environment variables for UTF-8
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
set MAVEN_OPTS=-Dfile.encoding=UTF-8

REM Force UTF-8 compilation
echo Compiling with UTF-8 encoding...
mvn compile -Dmaven.compiler.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Building JAR...
    mvn package -Dmaven.compiler.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8
) else (
    echo Compilation failed. Trying to fix specific files...
    goto :fix-files
)

goto :end

:fix-files
echo Attempting to fix encoding issues in specific files...

REM Check for files with encoding issues and recreate them
echo This script will attempt to fix common encoding problems...

:end
echo Script completed.
pause
