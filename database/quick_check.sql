-- Quick database verification script
USE inventory_db;

-- Check what products exist
SELECT 'Products in database:' as Info;
SELECT product_id, product_code, product_name FROM products ORDER BY product_id LIMIT 10;

-- Check what stock records exist
SELECT 'Stock records in database:' as Info;
SELECT product_id, quantity FROM stock ORDER BY product_id LIMIT 10;

-- Check the view output
SELECT 'View v_product_stock output:' as Info;
SELECT product_id, product_code, product_name, current_stock, min_stock_level FROM v_product_stock ORDER BY product_id LIMIT 10;

-- Check if subquery works
SELECT 'Test subquery:' as Info;
SELECT product_id FROM products WHERE product_code = 'CHR001';