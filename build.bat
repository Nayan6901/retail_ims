@echo off
REM Build and Run Script for Retail IMS (Windows)
REM Make sure you have Java 8+ and MySQL installed

echo === Retail Inventory Management System - Build Script ===

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 8 or higher.
    pause
    exit /b 1
)

echo ☕ Java version:
java -version

REM Create directories
echo 📁 Creating directories...
if not exist "bin" mkdir bin
if not exist "lib" mkdir lib

REM Download MySQL Connector/J if not exists
if not exist "lib\mysql-connector-j-8.1.0.jar" (
    echo 📥 Downloading MySQL Connector/J...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.1.0/mysql-connector-j-8.1.0.jar' -OutFile 'lib\mysql-connector-j-8.1.0.jar'"
)

REM Compile Java files
echo 🔨 Compiling Java files...
dir /s /b src\*.java > sources.txt

javac -cp "lib\*;src" -d bin @sources.txt

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
) else (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

REM Setup database
echo 🗄️ Setting up database...
echo Please make sure MySQL is running and execute:
echo mysql -u ims_user -p ^< database\schema.sql
echo mysql -u ims_user -p ^< database\sample_data.sql

REM Run the application
echo 🚀 Starting application...
java -cp "bin;lib\*" com.retail.ims.Main

echo Done!
pause