-- Database Verification Script
-- Run this to check if everything is set up correctly

USE inventory_db;

-- Check if tables exist
SHOW TABLES;

-- Check if users table has data
SELECT 'Users table data:' as Info;
SELECT user_id, username, full_name, role, is_active FROM users;

-- Check if products table has data
SELECT 'Products count:' as Info;
SELECT COUNT(*) as product_count FROM products;

-- Check if the admin user exists
SELECT 'Admin user check:' as Info;
SELECT * FROM users WHERE username = 'admin';

-- Test database connection
SELECT 'Database connection test:' as Info;
SELECT NOW() as current_time, DATABASE() as current_database, USER() as current_user;