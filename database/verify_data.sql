-- Verification script to check ChromaStore data
USE inventory_db;

-- Check products and their stock
SELECT 'Product and Stock Check' as Info;
SELECT 
    p.product_id, 
    p.product_code, 
    p.product_name,
    COALESCE(s.quantity, 0) as current_stock,
    p.min_stock_level
FROM products p 
LEFT JOIN stock s ON p.product_id = s.product_id 
ORDER BY p.product_id 
LIMIT 15;

-- Check stock table entries
SELECT 'Stock Table Entries' as Info;
SELECT product_id, quantity FROM stock ORDER BY product_id;

-- Check if product IDs match
SELECT 'Product ID Range' as Info;
SELECT MIN(product_id) as Min_ID, MAX(product_id) as Max_ID FROM products;

SELECT 'Stock ID Range' as Info;
SELECT MIN(product_id) as Min_ID, MAX(product_id) as Max_ID FROM stock;

-- Check how many products vs stock records
SELECT 'Count Check' as Info;
SELECT 
    (SELECT COUNT(*) FROM products) as Total_Products,
    (SELECT COUNT(*) FROM stock) as Total_Stock_Records;