# Retail Inventory Management System

A comprehensive inventory management system for retail shops built with Java, JDBC, and MySQL.

## 📋 Features

1. **Product Management** - Add, Edit, Delete, and Search products
2. **Category Management** - Organize products by categories
3. **Supplier Management** - Manage supplier information
4. **Stock Management** - Track inventory levels with low stock alerts
5. **Sales/Billing** - Process sales transactions
6. **Purchase Orders** - Manage product purchases
7. **Reports** - Generate sales and inventory reports
8. **User Authentication** - Secure login system
9. **Dashboard** - Visual statistics and insights

## 🛠️ Technology Stack

- **Backend**: Java with JDBC
- **Database**: MySQL
- **Frontend**: HTML, CSS, JavaScript, Bootstrap 5
- **Web Server**: Java HttpServer (or deploy with Tomcat/Servlet container)

## 📦 Prerequisites

- Java JDK 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC Driver)
- Web browser

## 🚀 Setup Instructions

### 1. Database Setup

Run the SQL script to create the database:

```bash
mysql -u ims_user -p < database/schema.sql
```

### 2. Configure Database Connection

Database credentials are in `.env` file (already configured):

- Host: localhost
- Database: inventory_db
- User: ims_user
- Password: Dhavan@6901

### 3. Compile and Run

```bash
# Compile Java files
javac -cp "lib/*;src" -d bin src/**/*.java

# Run the application
java -cp "bin;lib/*" com.retail.ims.Main
```

### 4. Access the Application

Open your browser and navigate to:

```
http://localhost:8080
```

**Default Login:**

- Username: admin
- Password: admin123

## 📁 Project Structure

```
retail_ims/
├── src/
│   └── com/retail/ims/
│       ├── config/         # Database configuration
│       ├── model/          # Data models
│       ├── dao/            # Data Access Objects
│       ├── service/        # Business logic
│       ├── controller/     # HTTP handlers
│       └── util/           # Utility classes
├── web/
│   ├── css/               # Stylesheets
│   ├── js/                # JavaScript files
│   ├── index.html         # Login page
│   └── dashboard.html     # Main dashboard
├── database/
│   ├── schema.sql         # Database schema
│   └── sample_data.sql    # Sample data
├── lib/                   # External libraries (JDBC driver)
└── .env                   # Environment variables
```

## 📊 Database Schema

The system uses a normalized database design with the following tables:

- users
- categories
- suppliers
- products
- stock
- sales
- sale_items
- purchases
- purchase_items

## 🎓 DBMS Concepts Demonstrated

1. **Database Normalization** (3NF)
2. **Primary and Foreign Keys**
3. **CRUD Operations**
4. **SQL Joins** (INNER, LEFT, RIGHT)
5. **Aggregate Functions** (SUM, COUNT, AVG)
6. **Subqueries**
7. **Transactions**
8. **Indexes for Performance**
9. **Views**
10. **Stored Procedures** (optional)

## 📝 License

This project is for educational purposes (DBMS Class Project).

## 👥 Authors

- Your Name
- Course: Database Management Systems
- Date: October 2025
