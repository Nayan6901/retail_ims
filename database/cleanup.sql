-- Retail IMS Database Cleanup Script
-- Run this first to clean up any existing tables

USE inventory_db;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all existing tables (including any pre-existing ones)
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS sale_items;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS purchase_items;
DROP TABLE IF EXISTS purchases;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Drop any views that might exist
DROP VIEW IF EXISTS v_product_stock;
DROP VIEW IF EXISTS v_sales_summary;
DROP VIEW IF EXISTS v_purchase_summary;
DROP VIEW IF EXISTS v_low_stock_products;

-- Drop any stored procedures that might exist
DROP PROCEDURE IF EXISTS sp_get_dashboard_stats;
DROP PROCEDURE IF EXISTS sp_generate_sale_number;
DROP PROCEDURE IF EXISTS sp_generate_purchase_number;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Show success message
SELECT 'Database cleanup completed successfully!' AS Status;