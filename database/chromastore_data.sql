-- ChromaStore Sample Data Script
-- Run this script in MySQL Workbench to populate the database with ChromaStore products and suppliers

USE inventory_db;

-- Clear existing data (be careful - this will remove all existing data)
DELETE FROM purchase_items;
DELETE FROM sale_items;
DELETE FROM sales;
DELETE FROM purchases;
DELETE FROM stock;
DELETE FROM products;
DELETE FROM suppliers;

-- Reset auto increment counters
ALTER TABLE suppliers AUTO_INCREMENT = 1;
ALTER TABLE products AUTO_INCREMENT = 1;
ALTER TABLE purchases AUTO_INCREMENT = 1;
ALTER TABLE sales AUTO_INCREMENT = 1;

-- Insert ChromaStore Suppliers
INSERT INTO suppliers (supplier_name, contact_person, phone, email, address, city, state, pincode) VALUES
('TechVision Electronics', 'Arjun Mehta', '9876543210', 'arjun@techvision.com', '15 Tech Park, Sector 62', 'Noida', 'Uttar Pradesh', '201301'),
('UrbanStyle Fashion Hub', 'Kavya Singh', '9876543211', 'kavya@urbanstyle.com', '45 Fashion Street', 'Mumbai', 'Maharashtra', '400001'),
('HealthFirst Wellness', 'Dr. Priya Sharma', '9876543212', 'priya@healthfirst.com', '78 Wellness Plaza', 'Bangalore', 'Karnataka', '560001'),
('HomeEssentials Pro', 'Rahul Gupta', '9876543213', 'rahul@homeessentials.com', '23 Home Center Mall', 'Delhi', 'Delhi', '110001'),
('GourmetChoice Foods', 'Neha Patel', '9876543214', 'neha@gourmetchoice.com', '56 Food Hub Complex', 'Pune', 'Maharashtra', '411001');

-- Insert ChromaStore Products (25 Modern Retail Items)
INSERT INTO products (product_code, product_name, description, category_id, supplier_id, unit, cost_price, selling_price, min_stock_level, max_stock_level) VALUES
-- Electronics from TechVision Electronics (Supplier 1)
('CHR001', 'Wireless Bluetooth Earbuds', 'Premium TWS earbuds with noise cancellation', 1, 1, 'Piece', 2500.00, 3999.00, 15, 100),
('CHR002', 'Smart Fitness Watch', 'Health tracking smartwatch with GPS', 1, 1, 'Piece', 4500.00, 6999.00, 10, 50),
('CHR003', 'Portable Bluetooth Speaker', 'Waterproof portable speaker', 1, 1, 'Piece', 1800.00, 2999.00, 20, 150),
('CHR004', 'Wireless Charging Pad', 'Fast wireless charger 15W', 1, 1, 'Piece', 800.00, 1299.00, 25, 200),
('CHR005', 'LED Smart Bulb RGB', 'WiFi controlled color changing bulb', 1, 1, 'Piece', 600.00, 999.00, 30, 300),

-- Fashion & Accessories from UrbanStyle Fashion Hub (Supplier 2) - Using Stationery category as closest match
('CHR006', 'Designer Sunglasses', 'UV protection polarized sunglasses', 8, 2, 'Piece', 800.00, 1499.00, 20, 150),
('CHR007', 'Premium Leather Wallet', 'Genuine leather bi-fold wallet', 8, 2, 'Piece', 1200.00, 1999.00, 15, 100),
('CHR008', 'Casual Cotton T-Shirt', 'Comfort fit round neck t-shirt', 8, 2, 'Piece', 300.00, 599.00, 50, 500),
('CHR009', 'Sports Cap Adjustable', 'Breathable mesh sports cap', 8, 2, 'Piece', 200.00, 399.00, 40, 400),
('CHR010', 'Fashion Wrist Watch', 'Analog display stainless steel watch', 8, 2, 'Piece', 1500.00, 2499.00, 12, 80),

-- Health & Wellness from HealthFirst Wellness (Supplier 3) - Using Personal Care category
('CHR011', 'Vitamin C Tablets 60s', 'Immunity booster vitamin C 500mg', 4, 3, 'Bottle', 120.00, 199.00, 50, 500),
('CHR012', 'Protein Powder 1kg', 'Whey protein isolate chocolate flavor', 4, 3, 'Container', 1800.00, 2999.00, 15, 100),
('CHR013', 'Digital Thermometer', 'Infrared non-contact thermometer', 4, 3, 'Piece', 800.00, 1299.00, 20, 200),
('CHR014', 'Face Mask N95 10pcs', 'Medical grade protective face masks', 4, 3, 'Pack', 150.00, 249.00, 100, 1000),
('CHR015', 'Hand Sanitizer 500ml', 'Alcohol based hand sanitizer 70%', 4, 3, 'Bottle', 80.00, 149.00, 80, 800),

-- Home Essentials from HomeEssentials Pro (Supplier 4) - Using Household category
('CHR016', 'Microfiber Cleaning Cloth', 'Lint-free cleaning cloths pack of 5', 5, 4, 'Pack', 80.00, 149.00, 60, 600),
('CHR017', 'Aroma Diffuser Electric', 'Essential oil diffuser with LED lights', 5, 4, 'Piece', 1200.00, 1999.00, 15, 100),
('CHR018', 'Storage Container Set', 'Airtight food storage containers 4pcs', 5, 4, 'Set', 600.00, 999.00, 25, 250),
('CHR019', 'LED Desk Lamp Adjustable', 'USB rechargeable LED reading lamp', 5, 4, 'Piece', 800.00, 1399.00, 20, 200),
('CHR020', 'Memory Foam Pillow', 'Ergonomic cervical support pillow', 5, 4, 'Piece', 1000.00, 1699.00, 15, 150),

-- Gourmet Foods from GourmetChoice Foods (Supplier 5) - Using appropriate food categories
('CHR021', 'Organic Green Tea 100g', 'Premium organic green tea leaves', 3, 5, 'Pack', 180.00, 299.00, 40, 400),
('CHR022', 'Dark Chocolate Bar 70%', 'Belgian dark chocolate premium quality', 6, 5, 'Bar', 120.00, 199.00, 60, 600),
('CHR023', 'Artisan Coffee Beans 250g', 'Single origin roasted coffee beans', 3, 5, 'Pack', 400.00, 649.00, 30, 300),
('CHR024', 'Himalayan Pink Salt 500g', 'Natural rock salt crystals', 2, 5, 'Pack', 80.00, 149.00, 50, 500),
('CHR025', 'Organic Honey 500g', 'Pure raw unprocessed honey', 2, 5, 'Jar', 300.00, 499.00, 35, 350);

-- Initialize stock for all products with varied quantities
INSERT INTO stock (product_id, quantity) VALUES
-- Electronics (Low stock for some items to test low stock alerts)
(1, 12),  -- CHR001 - Below min level (15)
(2, 8),   -- CHR002 - Below min level (10)
(3, 45),  -- CHR003 - Normal
(4, 80),  -- CHR004 - Normal
(5, 120), -- CHR005 - Normal

-- Fashion & Accessories
(6, 65),  -- CHR006 - Normal
(7, 35),  -- CHR007 - Normal
(8, 180), -- CHR008 - Normal
(9, 95),  -- CHR009 - Normal
(10, 25), -- CHR010 - Normal

-- Health & Wellness (Some low stock items)
(11, 25), -- CHR011 - Below min level (50)
(12, 8),  -- CHR012 - Below min level (15)
(13, 65), -- CHR013 - Normal
(14, 450), -- CHR014 - Normal
(15, 320), -- CHR015 - Normal

-- Home Essentials
(16, 180), -- CHR016 - Normal
(17, 45),  -- CHR017 - Normal
(18, 85),  -- CHR018 - Normal
(19, 75),  -- CHR019 - Normal
(20, 55),  -- CHR020 - Normal

-- Gourmet Foods (Some low stock)
(21, 25), -- CHR021 - Below min level (40)
(22, 220), -- CHR022 - Normal
(23, 15), -- CHR023 - Below min level (30)
(24, 180), -- CHR024 - Normal
(25, 120); -- CHR025 - Normal

-- Add ChromaStore sample purchases
INSERT INTO purchases (purchase_number, supplier_id, purchase_date, payment_status, created_by, notes) VALUES
('CHR-PUR-001', 1, '2025-10-01', 'paid', 1, 'Initial stock purchase - TechVision Electronics'),
('CHR-PUR-002', 2, '2025-10-01', 'paid', 1, 'Initial stock purchase - UrbanStyle Fashion Hub'),
('CHR-PUR-003', 3, '2025-10-02', 'paid', 1, 'Initial stock purchase - HealthFirst Wellness'),
('CHR-PUR-004', 4, '2025-10-03', 'pending', 1, 'Stock replenishment - HomeEssentials Pro'),
('CHR-PUR-005', 5, '2025-10-05', 'paid', 1, 'Monthly purchase - GourmetChoice Foods');

-- Add purchase items
INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price, total_price) VALUES
-- Purchase 1 - TechVision Electronics
(1, 1, 50, 2500.00, 125000.00),
(1, 2, 30, 4500.00, 135000.00),
(1, 3, 75, 1800.00, 135000.00),
(1, 4, 100, 800.00, 80000.00),
(1, 5, 150, 600.00, 90000.00),

-- Purchase 2 - UrbanStyle Fashion Hub
(2, 6, 80, 800.00, 64000.00),
(2, 7, 50, 1200.00, 60000.00),
(2, 8, 200, 300.00, 60000.00),
(2, 9, 120, 200.00, 24000.00),
(2, 10, 40, 1500.00, 60000.00),

-- Purchase 3 - HealthFirst Wellness
(3, 11, 200, 120.00, 24000.00),
(3, 12, 50, 1800.00, 90000.00),
(3, 13, 80, 800.00, 64000.00),
(3, 14, 500, 150.00, 75000.00),
(3, 15, 400, 80.00, 32000.00),

-- Purchase 4 - HomeEssentials Pro
(4, 16, 250, 80.00, 20000.00),
(4, 17, 60, 1200.00, 72000.00),
(4, 18, 100, 600.00, 60000.00),
(4, 19, 100, 800.00, 80000.00),
(4, 20, 75, 1000.00, 75000.00),

-- Purchase 5 - GourmetChoice Foods
(5, 21, 150, 180.00, 27000.00),
(5, 22, 300, 120.00, 36000.00),
(5, 23, 100, 400.00, 40000.00),
(5, 24, 250, 80.00, 20000.00),
(5, 25, 200, 300.00, 60000.00);

-- Add some sample sales for ChromaStore
INSERT INTO sales (sale_number, sale_date, customer_name, customer_phone, discount, tax, payment_method, payment_status, created_by) VALUES
('CHR-SALE-001', '2025-10-05 10:30:00', 'Anjali Sharma', '9876543301', 0, 720.00, 'upi', 'paid', 3),
('CHR-SALE-002', '2025-10-05 11:45:00', 'Vikram Singh', '9876543302', 200.00, 432.00, 'card', 'paid', 3),
('CHR-SALE-003', '2025-10-05 14:20:00', 'Meera Patel', '9876543303', 0, 89.64, 'cash', 'paid', 3),
('CHR-SALE-004', '2025-10-06 09:15:00', 'Rohit Kumar', '9876543304', 100.00, 359.28, 'upi', 'paid', 2),
('CHR-SALE-005', '2025-10-06 16:30:00', 'Priya Reddy', '9876543305', 0, 539.82, 'card', 'paid', 3);

-- Add sale items
INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, total_price) VALUES
-- Sale 1 - Electronics purchase
(1, 1, 1, 3999.00, 3999.00),

-- Sale 2 - Fashion items
(2, 6, 1, 1499.00, 1499.00),
(2, 8, 2, 599.00, 1198.00),

-- Sale 3 - Health products
(3, 11, 2, 199.00, 398.00),
(3, 15, 1, 149.00, 149.00),

-- Sale 4 - Home essentials
(4, 16, 3, 149.00, 447.00),
(4, 18, 1, 999.00, 999.00),
(4, 20, 1, 1699.00, 1699.00),

-- Sale 5 - Gourmet foods
(5, 21, 2, 299.00, 598.00),
(5, 22, 5, 199.00, 995.00),
(5, 23, 1, 649.00, 649.00),
(5, 25, 2, 499.00, 998.00);

-- Update sale totals
UPDATE sales SET 
    subtotal = (SELECT SUM(total_price) FROM sale_items WHERE sale_items.sale_id = sales.sale_id),
    total_amount = subtotal + tax - discount
WHERE sale_id IN (1, 2, 3, 4, 5);

COMMIT;

-- Verification queries (optional - you can run these to check the data)
/*
SELECT 'Suppliers Count' as Info, COUNT(*) as Count FROM suppliers
UNION ALL
SELECT 'Products Count' as Info, COUNT(*) as Count FROM products
UNION ALL
SELECT 'Low Stock Products' as Info, COUNT(*) as Count FROM v_product_stock WHERE current_stock <= min_stock_level
UNION ALL
SELECT 'Total Sales' as Info, COUNT(*) as Count FROM sales;

SELECT 'Supplier Products Distribution' as Info, s.supplier_name, COUNT(p.product_id) as Products
FROM suppliers s 
LEFT JOIN products p ON s.supplier_id = p.supplier_id 
GROUP BY s.supplier_id, s.supplier_name 
ORDER BY Products DESC;
*/