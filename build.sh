#!/bin/bash

# Build and Run Script for Retail IMS
# Make sure you have Java 8+ and MySQL installed

echo "=== Retail Inventory Management System - Build Script ==="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 8 or higher."
    exit 1
fi

# Check if MySQL is running
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL client not found. Make sure MySQL server is running."
fi

echo "☕ Java version:"
java -version

# Create directories
echo "📁 Creating directories..."
mkdir -p bin
mkdir -p lib

# Download MySQL Connector/J if not exists
if [ ! -f "lib/mysql-connector-j-8.1.0.jar" ]; then
    echo "📥 Downloading MySQL Connector/J..."
    curl -L -o lib/mysql-connector-j-8.1.0.jar "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.1.0/mysql-connector-j-8.1.0.jar"
fi

# Compile Java files
echo "🔨 Compiling Java files..."
find src -name "*.java" > sources.txt

javac -cp "lib/*" -d bin @sources.txt

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
else
    echo "❌ Compilation failed!"
    exit 1
fi

# Setup database
echo "🗄️  Setting up database..."
echo "Please make sure MySQL is running and execute:"
echo "mysql -u ims_user -p < database/schema.sql"
echo "mysql -u ims_user -p < database/sample_data.sql"

# Run the application
echo "🚀 Starting application..."
java -cp "bin:lib/*" com.retail.ims.Main

echo "Done!"