#!/bin/bash

echo "Fixing encoding issues for Java compilation..."

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Set environment variables for UTF-8
export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
export MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Force UTF-8 compilation
echo "Compiling with UTF-8 encoding..."
mvn compile -Dmaven.compiler.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Building JAR..."
    mvn package -Dmaven.compiler.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8
else
    echo "Compilation failed. Trying to fix specific files..."
    echo "This script will attempt to fix common encoding problems..."
fi

echo "Script completed."
