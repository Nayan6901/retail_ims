-- Retail Inventory Management System - Database Schema
-- Author: DBMS Project
-- Date: October 2025

-- Create database
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Drop tables if exist (for clean setup)
DROP TABLE IF EXISTS sale_items;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS purchase_items;
DROP TABLE IF EXISTS purchases;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- =====================================================
-- Table: users
-- Description: Store user authentication information
-- =====================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role ENUM('admin', 'manager', 'cashier') DEFAULT 'cashier',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
);

-- =====================================================
-- Table: categories
-- Description: Product categories
-- =====================================================
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_name (category_name)
);

-- =====================================================
-- Table: suppliers
-- Description: Supplier information
-- =====================================================
CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    pincode VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_supplier_name (supplier_name)
);

-- =====================================================
-- Table: products
-- Description: Product master data
-- =====================================================
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id INT,
    supplier_id INT,
    unit VARCHAR(20) DEFAULT 'Piece',
    cost_price DECIMAL(10, 2) NOT NULL,
    selling_price DECIMAL(10, 2) NOT NULL,
    min_stock_level INT DEFAULT 10,
    max_stock_level INT DEFAULT 1000,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL,
    INDEX idx_product_code (product_code),
    INDEX idx_product_name (product_name),
    INDEX idx_category (category_id),
    INDEX idx_supplier (supplier_id)
);

-- =====================================================
-- Table: stock
-- Description: Current stock levels
-- =====================================================
CREATE TABLE stock (
    stock_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_product (product_id),
    INDEX idx_product (product_id),
    INDEX idx_quantity (quantity)
);

-- =====================================================
-- Table: purchases
-- Description: Purchase order headers
-- =====================================================
CREATE TABLE purchases (
    purchase_id INT PRIMARY KEY AUTO_INCREMENT,
    purchase_number VARCHAR(50) UNIQUE NOT NULL,
    supplier_id INT,
    purchase_date DATE NOT NULL,
    total_amount DECIMAL(12, 2) DEFAULT 0,
    payment_status ENUM('pending', 'partial', 'paid') DEFAULT 'pending',
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_purchase_number (purchase_number),
    INDEX idx_purchase_date (purchase_date),
    INDEX idx_supplier (supplier_id)
);

-- =====================================================
-- Table: purchase_items
-- Description: Purchase order line items
-- =====================================================
CREATE TABLE purchase_items (
    purchase_item_id INT PRIMARY KEY AUTO_INCREMENT,
    purchase_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchases(purchase_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    INDEX idx_purchase (purchase_id),
    INDEX idx_product (product_id)
);

-- =====================================================
-- Table: sales
-- Description: Sales transaction headers
-- =====================================================
CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_number VARCHAR(50) UNIQUE NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    subtotal DECIMAL(12, 2) DEFAULT 0,
    discount DECIMAL(10, 2) DEFAULT 0,
    tax DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(12, 2) DEFAULT 0,
    payment_method ENUM('cash', 'card', 'upi', 'other') DEFAULT 'cash',
    payment_status ENUM('pending', 'paid') DEFAULT 'paid',
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_sale_number (sale_number),
    INDEX idx_sale_date (sale_date),
    INDEX idx_customer_phone (customer_phone)
);

-- =====================================================
-- Table: sale_items
-- Description: Sales transaction line items
-- =====================================================
CREATE TABLE sale_items (
    sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(sale_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    INDEX idx_sale (sale_id),
    INDEX idx_product (product_id)
);

-- =====================================================
-- VIEWS for reporting and quick access
-- =====================================================

-- View: Product with stock information
CREATE OR REPLACE VIEW v_product_stock AS
SELECT 
    p.product_id,
    p.product_code,
    p.product_name,
    p.supplier_id,
    c.category_name,
    s.supplier_name,
    p.cost_price,
    p.selling_price,
    COALESCE(st.quantity, 0) AS current_stock,
    p.min_stock_level,
    p.max_stock_level,
    CASE 
        WHEN COALESCE(st.quantity, 0) <= p.min_stock_level THEN 'Low'
        WHEN COALESCE(st.quantity, 0) >= p.max_stock_level THEN 'High'
        ELSE 'Normal'
    END AS stock_status,
    p.is_active,
    p.unit,
    p.selling_price AS price
FROM products p
LEFT JOIN categories c ON p.category_id = c.category_id
LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
LEFT JOIN stock st ON p.product_id = st.product_id;

-- View: Sales summary
CREATE OR REPLACE VIEW v_sales_summary AS
SELECT 
    s.sale_id,
    s.sale_number,
    s.sale_date,
    s.customer_name,
    s.customer_phone,
    s.total_amount,
    s.payment_method,
    s.payment_status,
    u.full_name AS created_by_name,
    COUNT(si.sale_item_id) AS total_items,
    SUM(si.quantity) AS total_quantity
FROM sales s
LEFT JOIN sale_items si ON s.sale_id = si.sale_id
LEFT JOIN users u ON s.created_by = u.user_id
GROUP BY s.sale_id;

-- View: Purchase summary
CREATE OR REPLACE VIEW v_purchase_summary AS
SELECT 
    p.purchase_id,
    p.purchase_number,
    p.purchase_date,
    s.supplier_name,
    p.total_amount,
    p.payment_status,
    u.full_name AS created_by_name,
    COUNT(pi.purchase_item_id) AS total_items,
    SUM(pi.quantity) AS total_quantity
FROM purchases p
LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
LEFT JOIN purchase_items pi ON p.purchase_id = pi.purchase_id
LEFT JOIN users u ON p.created_by = u.user_id
GROUP BY p.purchase_id;

-- View: Low stock products
CREATE OR REPLACE VIEW v_low_stock_products AS
SELECT 
    p.product_id,
    p.product_code,
    p.product_name,
    c.category_name,
    COALESCE(st.quantity, 0) AS current_stock,
    p.min_stock_level,
    p.min_stock_level - COALESCE(st.quantity, 0) AS shortage
FROM products p
LEFT JOIN categories c ON p.category_id = c.category_id
LEFT JOIN stock st ON p.product_id = st.product_id
WHERE p.is_active = TRUE 
    AND COALESCE(st.quantity, 0) <= p.min_stock_level
ORDER BY shortage DESC;

-- =====================================================
-- TRIGGERS for automatic updates
-- =====================================================

-- Trigger: Update stock after purchase
DELIMITER //
CREATE TRIGGER after_purchase_item_insert
AFTER INSERT ON purchase_items
FOR EACH ROW
BEGIN
    -- Update stock quantity
    INSERT INTO stock (product_id, quantity)
    VALUES (NEW.product_id, NEW.quantity)
    ON DUPLICATE KEY UPDATE quantity = quantity + NEW.quantity;
END//
DELIMITER ;

-- Trigger: Update stock after sale
DELIMITER //
CREATE TRIGGER after_sale_item_insert
AFTER INSERT ON sale_items
FOR EACH ROW
BEGIN
    -- Decrease stock quantity
    UPDATE stock 
    SET quantity = quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END//
DELIMITER ;

-- Trigger: Calculate purchase total
DELIMITER //
CREATE TRIGGER after_purchase_item_insert_update_total
AFTER INSERT ON purchase_items
FOR EACH ROW
BEGIN
    UPDATE purchases
    SET total_amount = (
        SELECT SUM(total_price)
        FROM purchase_items
        WHERE purchase_id = NEW.purchase_id
    )
    WHERE purchase_id = NEW.purchase_id;
END//
DELIMITER ;

-- Trigger: Calculate sale total
DELIMITER //
CREATE TRIGGER after_sale_item_insert_update_total
AFTER INSERT ON sale_items
FOR EACH ROW
BEGIN
    UPDATE sales
    SET subtotal = (
        SELECT SUM(total_price)
        FROM sale_items
        WHERE sale_id = NEW.sale_id
    ),
    total_amount = (
        SELECT SUM(total_price)
        FROM sale_items
        WHERE sale_id = NEW.sale_id
    ) - discount + tax
    WHERE sale_id = NEW.sale_id;
END//
DELIMITER ;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedure: Get dashboard statistics
DELIMITER //
CREATE PROCEDURE sp_get_dashboard_stats()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM products WHERE is_active = TRUE) AS total_products,
        (SELECT COUNT(*) FROM v_low_stock_products) AS low_stock_count,
        (SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE DATE(sale_date) = CURDATE()) AS today_sales,
        (SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE MONTH(sale_date) = MONTH(CURDATE()) AND YEAR(sale_date) = YEAR(CURDATE())) AS month_sales,
        (SELECT COUNT(*) FROM sales WHERE DATE(sale_date) = CURDATE()) AS today_transactions,
        (SELECT COUNT(*) FROM suppliers WHERE is_active = TRUE) AS total_suppliers,
        (SELECT COUNT(*) FROM categories) AS total_categories,
        (SELECT COALESCE(SUM(quantity * cost_price), 0) FROM stock s JOIN products p ON s.product_id = p.product_id) AS inventory_value;
END//
DELIMITER ;

-- Procedure: Generate sale number
DELIMITER //
CREATE PROCEDURE sp_generate_sale_number(OUT sale_num VARCHAR(50))
BEGIN
    DECLARE next_num INT;
    SELECT COALESCE(MAX(CAST(SUBSTRING(sale_number, 6) AS UNSIGNED)), 0) + 1 INTO next_num FROM sales;
    SET sale_num = CONCAT('SALE-', LPAD(next_num, 6, '0'));
END//
DELIMITER ;

-- Procedure: Generate purchase number
DELIMITER //
CREATE PROCEDURE sp_generate_purchase_number(OUT purchase_num VARCHAR(50))
BEGIN
    DECLARE next_num INT;
    SELECT COALESCE(MAX(CAST(SUBSTRING(purchase_number, 5) AS UNSIGNED)), 0) + 1 INTO next_num FROM purchases;
    SET purchase_num = CONCAT('PUR-', LPAD(next_num, 6, '0'));
END//
DELIMITER ;

COMMIT;
