-- Fixed ChromaStore Stock Data Script
-- Run this AFTER the main chromastore_data.sql script to fix stock issues

USE inventory_db;

-- First, let's clear any existing stock data and re-insert it properly
DELETE FROM stock;

-- Insert stock data directly by finding the product IDs first
-- We'll do this in steps to avoid subquery issues

-- Step 1: Get the actual product IDs and insert stock manually
-- (You can run these one by one or all together)

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 12 FROM products p WHERE p.product_code = 'CHR001';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 8 FROM products p WHERE p.product_code = 'CHR002';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 45 FROM products p WHERE p.product_code = 'CHR003';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 80 FROM products p WHERE p.product_code = 'CHR004';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 120 FROM products p WHERE p.product_code = 'CHR005';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 65 FROM products p WHERE p.product_code = 'CHR006';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 35 FROM products p WHERE p.product_code = 'CHR007';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 180 FROM products p WHERE p.product_code = 'CHR008';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 95 FROM products p WHERE p.product_code = 'CHR009';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 25 FROM products p WHERE p.product_code = 'CHR010';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 25 FROM products p WHERE p.product_code = 'CHR011';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 8 FROM products p WHERE p.product_code = 'CHR012';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 65 FROM products p WHERE p.product_code = 'CHR013';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 450 FROM products p WHERE p.product_code = 'CHR014';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 320 FROM products p WHERE p.product_code = 'CHR015';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 180 FROM products p WHERE p.product_code = 'CHR016';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 45 FROM products p WHERE p.product_code = 'CHR017';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 85 FROM products p WHERE p.product_code = 'CHR018';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 75 FROM products p WHERE p.product_code = 'CHR019';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 55 FROM products p WHERE p.product_code = 'CHR020';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 25 FROM products p WHERE p.product_code = 'CHR021';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 220 FROM products p WHERE p.product_code = 'CHR022';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 15 FROM products p WHERE p.product_code = 'CHR023';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 180 FROM products p WHERE p.product_code = 'CHR024';

INSERT INTO stock (product_id, quantity) 
SELECT p.product_id, 120 FROM products p WHERE p.product_code = 'CHR025';

-- Verify the stock was inserted
SELECT 'Stock verification:' as Info;
SELECT COUNT(*) as total_stock_records FROM stock;

SELECT 'Products with stock:' as Info;
SELECT p.product_code, p.product_name, s.quantity 
FROM products p 
JOIN stock s ON p.product_id = s.product_id 
ORDER BY p.product_code;