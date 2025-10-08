package com.retail.ims.test;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.dao.ProductDAO;
import com.retail.ims.dao.CategoryDAO;
import com.retail.ims.dao.SupplierDAO;
import com.retail.ims.model.Product;
import com.retail.ims.model.Category;
import com.retail.ims.model.Supplier;

import java.util.List;

/**
 * Test products API and add sample data if needed
 */
public class ProductTest {
    
    public static void main(String[] args) {
        System.out.println("=== Product API Test ===");
        
        // Test database connection
        if (!DatabaseConfig.testConnection()) {
            System.err.println("Database connection failed!");
            return;
        }
        
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        
        // Check existing data
        List<Product> products = productDAO.getAllProducts();
        List<Category> categories = categoryDAO.getAllCategories();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        
        System.out.println("Existing data:");
        System.out.println("- Products: " + products.size());
        System.out.println("- Categories: " + categories.size());
        System.out.println("- Suppliers: " + suppliers.size());
        
        // Add sample data if none exists
        if (categories.isEmpty()) {
            System.out.println("\nAdding sample categories...");
            addSampleCategories(categoryDAO);
        }
        
        if (suppliers.isEmpty()) {
            System.out.println("Adding sample suppliers...");
            addSampleSuppliers(supplierDAO);
        }
        
        if (products.isEmpty()) {
            System.out.println("Adding sample products...");
            addSampleProducts(productDAO);
        }
        
        // Check again
        products = productDAO.getAllProducts();
        System.out.println("\nFinal product count: " + products.size());
        
        if (!products.isEmpty()) {
            System.out.println("\nFirst few products:");
            for (int i = 0; i < Math.min(3, products.size()); i++) {
                Product p = products.get(i);
                System.out.printf("- %s (Stock: %d, Price: ₹%.2f)\n", 
                    p.getProductName(), p.getCurrentStock(), p.getSellingPrice());
            }
        }
        
        System.out.println("\n=== Test Complete ===");
    }
    
    private static void addSampleCategories(CategoryDAO categoryDAO) {
        String[] categoryNames = {"Electronics", "Grocery", "Clothing", "Books", "Home & Garden"};
        
        for (String name : categoryNames) {
            Category category = new Category();
            category.setCategoryName(name);
            category.setDescription("Sample " + name + " category");
            categoryDAO.createCategory(category);
        }
    }
    
    private static void addSampleSuppliers(SupplierDAO supplierDAO) {
        String[][] supplierData = {
            {"TechCorp Ltd.", "John Smith", "9876543210", "john@techcorp.com"},
            {"Fresh Foods Co.", "Sarah Wilson", "8765432109", "sarah@freshfoods.com"},
            {"Fashion House", "Mike Johnson", "7654321098", "mike@fashionhouse.com"}
        };
        
        for (String[] data : supplierData) {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(data[0]);
            supplier.setContactPerson(data[1]);
            supplier.setPhone(data[2]);
            supplier.setEmail(data[3]);
            supplier.setActive(true);
            supplierDAO.createSupplier(supplier);
        }
    }
    
    private static void addSampleProducts(ProductDAO productDAO) {
        String[][] productData = {
            {"LAPTOP001", "Gaming Laptop", "25000.00", "30000.00", "10", "2", "20"},
            {"PHONE001", "Smartphone", "15000.00", "18000.00", "25", "5", "50"},
            {"RICE001", "Basmati Rice 1kg", "80.00", "100.00", "100", "10", "200"},
            {"SHIRT001", "Cotton T-Shirt", "200.00", "350.00", "50", "5", "100"},
            {"BOOK001", "Programming Book", "400.00", "500.00", "20", "3", "50"}
        };
        
        for (String[] data : productData) {
            Product product = new Product();
            product.setProductCode(data[0]);
            product.setProductName(data[1]);
            product.setCostPrice(Double.parseDouble(data[2]));
            product.setSellingPrice(Double.parseDouble(data[3]));
            product.setCurrentStock(Integer.parseInt(data[4]));
            product.setMinStockLevel(Integer.parseInt(data[5]));
            product.setMaxStockLevel(Integer.parseInt(data[6]));
            product.setCategoryId(1); // Assuming categories exist
            product.setSupplierId(1); // Assuming suppliers exist
            product.setActive(true);
            
            productDAO.createProduct(product);
        }
    }
}