# Quick Setup Guide for Retail IMS

## Prerequisites

1. **Java JDK 8+** installed
2. **MySQL Server** running
3. **Web Browser** (Chrome, Firefox, Edge)

## Database Setup

1. **Login to MySQL as root:**

   ```bash
   mysql -u root -p
   ```

2. **Create user and database:**

   ```sql
   CREATE USER 'ims_user'@'localhost' IDENTIFIED BY 'Dhavan@6901';
   CREATE DATABASE inventory_db;
   GRANT ALL PRIVILEGES ON inventory_db.* TO 'ims_user'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   ```

3. **Import schema and sample data:**
   ```bash
   mysql -u ims_user -p inventory_db < database/schema.sql
   mysql -u ims_user -p inventory_db < database/sample_data.sql
   ```

## Running the Application

### Windows:

```bash
build.bat
```

### Linux/Mac:

```bash
chmod +x build.sh
./build.sh
```

### Manual Compilation:

```bash
# Download MySQL Connector/J to lib/ folder
# Compile
javac -cp "lib/*;src" -d bin src/**/*.java

# Run
java -cp "bin;lib/*" com.retail.ims.Main
```

## Access the Application

1. **Open browser** and go to: `http://localhost:8080`

2. **Login with default credentials:**
   - Username: `admin`
   - Password: `admin123`

## Project Features

✅ **User Authentication & Authorization**
✅ **Product Management** (CRUD operations)
✅ **Category Management**
✅ **Supplier Management**
✅ **Stock Management** with low stock alerts
✅ **Sales/Billing System**
✅ **Dashboard** with analytics
✅ **Reports** (Sales, Inventory)
✅ **Responsive Web UI** (Bootstrap 5)

## DBMS Concepts Demonstrated

1. **Database Design** - Normalized schema (3NF)
2. **CRUD Operations** - Create, Read, Update, Delete
3. **SQL Joins** - INNER, LEFT, RIGHT joins
4. **Aggregate Functions** - SUM, COUNT, AVG
5. **Subqueries** - Complex data retrieval
6. **Views** - Virtual tables for reporting
7. **Stored Procedures** - Database logic
8. **Triggers** - Automatic data updates
9. **Transactions** - Data consistency
10. **Indexes** - Performance optimization

## API Endpoints

- `POST /auth/api/login` - User authentication
- `GET /api/dashboard/stats` - Dashboard statistics
- `GET /api/dashboard/weekly-sales` - Weekly sales data
- `GET /api/dashboard/top-products` - Top selling products

## Troubleshooting

### Database Connection Issues:

1. Check MySQL is running: `systemctl status mysql` (Linux) or `net start mysql` (Windows)
2. Verify credentials in `.env` file
3. Ensure user has proper permissions

### Compilation Issues:

1. Check Java version: `java -version`
2. Ensure MySQL Connector/J is in `lib/` folder
3. Check classpath in build scripts

### Web Interface Issues:

1. Clear browser cache
2. Check browser console for JavaScript errors
3. Verify server is running on port 8080

## File Structure

```
retail_ims/
├── src/                    # Java source code
├── web/                    # Frontend files
├── database/               # SQL scripts
├── lib/                    # JAR dependencies
├── bin/                    # Compiled classes
├── .env                    # Environment variables
├── build.bat               # Windows build script
├── build.sh                # Linux/Mac build script
└── README.md               # Documentation
```

## Support

For issues or questions, check:

1. Console output for error messages
2. MySQL error logs
3. Browser developer tools
4. Java compilation errors
