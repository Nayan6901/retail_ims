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

-- Insert Suppliers
INSERT INTO suppliers (supplier_name, contact_person, phone, email, address, city, state, pincode) VALUES
('ABC Electronics Pvt Ltd', 'Rajesh Kumar', '9876543210', 'rajesh@abc.com', '123 MG Road', 'Mumbai', 'Maharashtra', '400001'),
('Fresh Foods Distributors', 'Priya Sharma', '9876543211', 'priya@freshfoods.com', '456 Park Street', 'Delhi', 'Delhi', '110001'),
('Global Beverages Co', 'Amit Patel', '9876543212', 'amit@globalbev.com', '789 Ring Road', 'Ahmedabad', 'Gujarat', '380001'),
('Care Products India', 'Sneha Reddy', 'Reddy', '9876543213', 'sneha@careproducts.com', '321 Lake View', 'Bangalore', 'Karnataka', '560001'),
('Household Essentials', 'Vikram Singh', '9876543214', 'vikram@household.com', '654 Civil Lines', 'Pune', 'Maharashtra', '411001');

-- Insert Products
INSERT INTO products (product_code, product_name, description, category_id, supplier_id, unit, cost_price, selling_price, min_stock_level, max_stock_level) VALUES
-- Electronics
('ELEC001', 'LED Bulb 9W', 'Energy saving LED bulb', 1, 1, 'Piece', 150.00, 199.00, 20, 200),
('ELEC002', 'USB Cable Type-C', '1 meter USB Type-C cable', 1, 1, 'Piece', 80.00, 149.00, 30, 300),
('ELEC003', 'Power Bank 10000mAh', 'Portable power bank', 1, 1, 'Piece', 600.00, 899.00, 10, 100),
('ELEC004', 'Earphones Wired', 'Standard wired earphones', 1, 1, 'Piece', 150.00, 249.00, 25, 250),

-- Groceries
('GROC001', 'Rice Premium 1kg', 'Basmati rice', 2, 2, 'Kg', 80.00, 110.00, 50, 500),
('GROC002', 'Wheat Flour 1kg', 'Whole wheat flour', 2, 2, 'Kg', 40.00, 55.00, 50, 500),
('GROC003', 'Sugar 1kg', 'White sugar', 2, 2, 'Kg', 45.00, 60.00, 40, 400),
('GROC004', 'Salt 1kg', 'Iodized salt', 2, 2, 'Kg', 20.00, 28.00, 30, 300),
('GROC005', 'Cooking Oil 1L', 'Refined oil', 2, 2, 'Liter', 120.00, 160.00, 40, 400),
('GROC006', 'Pulses Mix 500g', 'Dal mix', 2, 2, 'Pack', 60.00, 85.00, 30, 300),

-- Beverages
('BEV001', 'Soft Drink 500ml', 'Carbonated drink', 3, 3, 'Bottle', 20.00, 35.00, 100, 1000),
('BEV002', 'Mineral Water 1L', 'Packaged drinking water', 3, 3, 'Bottle', 15.00, 20.00, 150, 1500),
('BEV003', 'Fruit Juice 200ml', 'Mixed fruit juice', 3, 3, 'Pack', 15.00, 25.00, 80, 800),
('BEV004', 'Energy Drink 250ml', 'Energy booster drink', 3, 3, 'Can', 50.00, 80.00, 50, 500),
('BEV005', 'Tea Powder 250g', 'Premium tea powder', 3, 3, 'Pack', 100.00, 145.00, 30, 300),
('BEV006', 'Coffee Powder 200g', 'Instant coffee', 3, 3, 'Pack', 180.00, 250.00, 25, 250),

-- Personal Care
('CARE001', 'Toothpaste 100g', 'Dental care', 4, 4, 'Piece', 40.00, 60.00, 50, 500),
('CARE002', 'Soap Bar 100g', 'Bathing soap', 4, 4, 'Piece', 25.00, 40.00, 60, 600),
('CARE003', 'Shampoo 200ml', 'Hair care shampoo', 4, 4, 'Bottle', 120.00, 180.00, 30, 300),
('CARE004', 'Hand Sanitizer 100ml', 'Antibacterial sanitizer', 4, 4, 'Bottle', 40.00, 65.00, 40, 400),
('CARE005', 'Face Wash 50g', 'Facial cleanser', 4, 4, 'Tube', 80.00, 130.00, 25, 250),

-- Household
('HOUSE001', 'Detergent 1kg', 'Washing powder', 5, 5, 'Pack', 100.00, 145.00, 40, 400),
('HOUSE002', 'Dish Wash Bar', 'Utensil cleaner', 5, 5, 'Piece', 15.00, 25.00, 60, 600),
('HOUSE003', 'Floor Cleaner 500ml', 'Floor cleaning liquid', 5, 5, 'Bottle', 60.00, 95.00, 30, 300),
('HOUSE004', 'Garbage Bags 30pcs', 'Medium size bags', 5, 5, 'Pack', 40.00, 70.00, 50, 500),
('HOUSE005', 'Toilet Cleaner 500ml', 'Bathroom cleaner', 5, 5, 'Bottle', 70.00, 110.00, 30, 300),

-- Snacks
('SNACK001', 'Chips 100g', 'Potato chips', 6, 2, 'Pack', 20.00, 35.00, 100, 1000),
('SNACK002', 'Biscuits 200g', 'Cream biscuits', 6, 2, 'Pack', 30.00, 50.00, 80, 800),
('SNACK003', 'Namkeen 150g', 'Indian savory snack', 6, 2, 'Pack', 25.00, 45.00, 70, 700),
('SNACK004', 'Chocolates 50g', 'Milk chocolate', 6, 2, 'Piece', 40.00, 60.00, 50, 500),
('SNACK005', 'Cookies 100g', 'Butter cookies', 6, 2, 'Pack', 35.00, 55.00, 60, 600),

-- Dairy
('DAIRY001', 'Milk 1L', 'Full cream milk', 7, 2, 'Liter', 50.00, 65.00, 30, 300),
('DAIRY002', 'Curd 500g', 'Fresh curd', 7, 2, 'Cup', 30.00, 45.00, 25, 250),
('DAIRY003', 'Butter 100g', 'Table butter', 7, 2, 'Pack', 45.00, 65.00, 20, 200),
('DAIRY004', 'Cheese Slice 200g', 'Processed cheese', 7, 2, 'Pack', 100.00, 145.00, 15, 150),
('DAIRY005', 'Paneer 200g', 'Fresh cottage cheese', 7, 2, 'Pack', 70.00, 100.00, 20, 200),

-- Stationery
('STAT001', 'Notebook A4', 'Single line notebook', 8, 1, 'Piece', 30.00, 50.00, 40, 400),
('STAT002', 'Pen Blue', 'Ball point pen', 8, 1, 'Piece', 5.00, 10.00, 100, 1000),
('STAT003', 'Pencil HB', 'Lead pencil', 8, 1, 'Piece', 3.00, 5.00, 100, 1000),
('STAT004', 'Eraser', 'Rubber eraser', 8, 1, 'Piece', 3.00, 5.00, 80, 800),
('STAT005', 'Sharpener', 'Pencil sharpener', 8, 1, 'Piece', 5.00, 8.00, 80, 800);

-- Initialize stock for all products
INSERT INTO stock (product_id, quantity)
SELECT product_id, 50 FROM products;

-- Add some sample purchases
INSERT INTO purchases (purchase_number, supplier_id, purchase_date, payment_status, created_by, notes) VALUES
('PUR-000001', 1, '2025-10-01', 'paid', 1, 'Initial stock purchase - Electronics'),
('PUR-000002', 2, '2025-10-01', 'paid', 1, 'Initial stock purchase - Groceries & Food'),
('PUR-000003', 3, '2025-10-02', 'paid', 1, 'Initial stock purchase - Beverages'),
('PUR-000004', 4, '2025-10-03', 'pending', 1, 'Stock replenishment - Personal Care'),
('PUR-000005', 5, '2025-10-05', 'paid', 1, 'Monthly purchase - Household items');

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
