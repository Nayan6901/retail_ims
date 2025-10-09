-- Sample Data for Retail Inventory Management System
USE inventory_db;

-- Insert Users
INSERT INTO users (username, password, full_name, email, role) VALUES
('admin', 'admin123', 'Admin User', 'admin@retail.com', 'admin'),
('manager', 'manager123', 'Store Manager', 'manager@retail.com', 'manager'),
('cashier', 'cashier123', 'Cashier One', 'cashier@retail.com', 'cashier');

-- Insert Categories
INSERT INTO categories (category_name, description) VALUES
('Electronics', 'Electronic items and gadgets'),
('Groceries', 'Food and grocery items'),
('Beverages', 'Drinks and beverages'),
('Personal Care', 'Personal care and hygiene products'),
('Household', 'Household items and cleaning supplies'),
('Snacks', 'Snacks and confectionery'),
('Dairy', 'Dairy products'),
('Stationery', 'Office and school supplies');

-- Insert Suppliers for ChromaStore
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

-- Fashion & Accessories from UrbanStyle Fashion Hub (Supplier 2)
('CHR006', 'Designer Sunglasses', 'UV protection polarized sunglasses', 8, 2, 'Piece', 800.00, 1499.00, 20, 150),
('CHR007', 'Premium Leather Wallet', 'Genuine leather bi-fold wallet', 8, 2, 'Piece', 1200.00, 1999.00, 15, 100),
('CHR008', 'Casual Cotton T-Shirt', 'Comfort fit round neck t-shirt', 8, 2, 'Piece', 300.00, 599.00, 50, 500),
('CHR009', 'Sports Cap Adjustable', 'Breathable mesh sports cap', 8, 2, 'Piece', 200.00, 399.00, 40, 400),
('CHR010', 'Fashion Wrist Watch', 'Analog display stainless steel watch', 8, 2, 'Piece', 1500.00, 2499.00, 12, 80),

-- Health & Wellness from HealthFirst Wellness (Supplier 3)
('CHR011', 'Vitamin C Tablets 60s', 'Immunity booster vitamin C 500mg', 4, 3, 'Bottle', 120.00, 199.00, 50, 500),
('CHR012', 'Protein Powder 1kg', 'Whey protein isolate chocolate flavor', 4, 3, 'Container', 1800.00, 2999.00, 15, 100),
('CHR013', 'Digital Thermometer', 'Infrared non-contact thermometer', 4, 3, 'Piece', 800.00, 1299.00, 20, 200),
('CHR014', 'Face Mask N95 10pcs', 'Medical grade protective face masks', 4, 3, 'Pack', 150.00, 249.00, 100, 1000),
('CHR015', 'Hand Sanitizer 500ml', 'Alcohol based hand sanitizer 70%', 4, 3, 'Bottle', 80.00, 149.00, 80, 800),

-- Home Essentials from HomeEssentials Pro (Supplier 4)
('CHR016', 'Microfiber Cleaning Cloth', 'Lint-free cleaning cloths pack of 5', 5, 4, 'Pack', 80.00, 149.00, 60, 600),
('CHR017', 'Aroma Diffuser Electric', 'Essential oil diffuser with LED lights', 5, 4, 'Piece', 1200.00, 1999.00, 15, 100),
('CHR018', 'Storage Container Set', 'Airtight food storage containers 4pcs', 5, 4, 'Set', 600.00, 999.00, 25, 250),
('CHR019', 'LED Desk Lamp Adjustable', 'USB rechargeable LED reading lamp', 5, 4, 'Piece', 800.00, 1399.00, 20, 200),
('CHR020', 'Memory Foam Pillow', 'Ergonomic cervical support pillow', 5, 4, 'Piece', 1000.00, 1699.00, 15, 150),

-- Gourmet Foods from GourmetChoice Foods (Supplier 5)
('CHR021', 'Organic Green Tea 100g', 'Premium organic green tea leaves', 3, 5, 'Pack', 180.00, 299.00, 40, 400),
('CHR022', 'Dark Chocolate Bar 70%', 'Belgian dark chocolate premium quality', 6, 5, 'Bar', 120.00, 199.00, 60, 600),
('CHR023', 'Artisan Coffee Beans 250g', 'Single origin roasted coffee beans', 3, 5, 'Pack', 400.00, 649.00, 30, 300),
('CHR024', 'Himalayan Pink Salt 500g', 'Natural rock salt crystals', 2, 5, 'Pack', 80.00, 149.00, 50, 500),
('CHR025', 'Organic Honey 500g', 'Pure raw unprocessed honey', 2, 5, 'Jar', 300.00, 499.00, 35, 350);

-- Initialize stock for all products
INSERT INTO stock (product_id, quantity)
SELECT product_id, 50 FROM products;

-- Add ChromaStore sample purchases
INSERT INTO purchases (purchase_number, supplier_id, purchase_date, payment_status, created_by, notes) VALUES
('PUR-000001', 1, '2025-10-01', 'paid', 1, 'Initial stock purchase - TechVision Electronics'),
('PUR-000002', 2, '2025-10-01', 'paid', 1, 'Initial stock purchase - UrbanStyle Fashion Hub'),
('PUR-000003', 3, '2025-10-02', 'paid', 1, 'Initial stock purchase - HealthFirst Wellness'),
('PUR-000004', 4, '2025-10-03', 'pending', 1, 'Stock replenishment - HomeEssentials Pro'),
('PUR-000005', 5, '2025-10-05', 'paid', 1, 'Monthly purchase - GourmetChoice Foods');

-- Add purchase items
INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price, total_price) VALUES
-- Purchase 1 - Electronics
(1, 1, 100, 150.00, 15000.00),
(1, 2, 150, 80.00, 12000.00),
(1, 3, 50, 600.00, 30000.00),
(1, 4, 100, 150.00, 15000.00),

-- Purchase 2 - Groceries
(2, 5, 200, 80.00, 16000.00),
(2, 6, 200, 40.00, 8000.00),
(2, 7, 150, 45.00, 6750.00),
(2, 8, 150, 20.00, 3000.00),
(2, 9, 150, 120.00, 18000.00),
(2, 10, 100, 60.00, 6000.00),

-- Purchase 3 - Beverages
(3, 11, 300, 20.00, 6000.00),
(3, 12, 500, 15.00, 7500.00),
(3, 13, 200, 15.00, 3000.00),
(3, 14, 150, 50.00, 7500.00),
(3, 15, 100, 100.00, 10000.00),
(3, 16, 80, 180.00, 14400.00),

-- Purchase 4 - Personal Care
(4, 17, 150, 40.00, 6000.00),
(4, 18, 200, 25.00, 5000.00),
(4, 19, 100, 120.00, 12000.00),
(4, 20, 120, 40.00, 4800.00),
(4, 21, 80, 80.00, 6400.00),

-- Purchase 5 - Household
(5, 22, 120, 100.00, 12000.00),
(5, 23, 200, 15.00, 3000.00),
(5, 24, 100, 60.00, 6000.00),
(5, 25, 150, 40.00, 6000.00),
(5, 26, 100, 70.00, 7000.00);

-- Add some sample sales
INSERT INTO sales (sale_number, sale_date, customer_name, customer_phone, discount, tax, payment_method, payment_status, created_by) VALUES
('SALE-000001', '2025-10-05 10:30:00', 'Rahul Verma', '9876543301', 0, 18.00, 'cash', 'paid', 3),
('SALE-000002', '2025-10-05 11:45:00', 'Anita Desai', '9876543302', 10.00, 25.20, 'upi', 'paid', 3),
('SALE-000003', '2025-10-05 14:20:00', 'Suresh Patel', '9876543303', 0, 15.30, 'card', 'paid', 3),
('SALE-000004', '2025-10-06 09:15:00', 'Meena Shah', '9876543304', 5.00, 22.50, 'cash', 'paid', 3),
('SALE-000005', '2025-10-06 16:30:00', 'Vijay Kumar', '9876543305', 0, 12.00, 'upi', 'paid', 3),
('SALE-000006', '2025-10-07 10:00:00', 'Pooja Singh', '9876543306', 15.00, 28.80, 'cash', 'paid', 3),
('SALE-000007', '2025-10-07 12:30:00', 'Amit Joshi', '9876543307', 0, 20.70, 'card', 'paid', 3);

-- Add sale items
INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, total_price) VALUES
-- Sale 1
(1, 11, 2, 35.00, 70.00),
(1, 12, 3, 20.00, 60.00),
(1, 27, 5, 35.00, 175.00),
(1, 32, 2, 50.00, 100.00),

-- Sale 2
(2, 5, 2, 110.00, 220.00),
(2, 6, 2, 55.00, 110.00),
(2, 7, 1, 60.00, 60.00),
(2, 17, 2, 60.00, 120.00),
(2, 18, 3, 40.00, 120.00),

-- Sale 3
(3, 1, 3, 199.00, 597.00),
(3, 2, 2, 149.00, 298.00),

-- Sale 4
(4, 22, 2, 145.00, 290.00),
(4, 23, 4, 25.00, 100.00),
(4, 24, 2, 95.00, 190.00),
(4, 19, 1, 180.00, 180.00),

-- Sale 5
(5, 13, 5, 25.00, 125.00),
(5, 14, 3, 80.00, 240.00),
(5, 28, 4, 50.00, 200.00),

-- Sale 6
(6, 9, 2, 160.00, 320.00),
(6, 10, 2, 85.00, 170.00),
(6, 31, 3, 65.00, 195.00),
(6, 33, 2, 65.00, 130.00),
(6, 34, 1, 145.00, 145.00),

-- Sale 7
(7, 3, 1, 899.00, 899.00),
(7, 4, 2, 249.00, 498.00);

COMMIT;

SELECT 'Sample data inserted successfully!' AS Status;
