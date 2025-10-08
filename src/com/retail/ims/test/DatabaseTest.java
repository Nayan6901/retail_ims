package com.retail.ims.test;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.dao.UserDAO;
import com.retail.ims.model.User;

/**
 * Database connection test utility
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        
        // Test 1: Basic connection
        System.out.println("\n1. Testing basic database connection...");
        if (DatabaseConfig.testConnection()) {
            System.out.println("✅ Database connection: SUCCESS");
        } else {
            System.out.println("❌ Database connection: FAILED");
            return;
        }
        
        // Test 2: Test user authentication
        System.out.println("\n2. Testing user authentication...");
        UserDAO userDAO = new UserDAO();
        
        try {
            User user = userDAO.authenticate("admin", "admin123");
            if (user != null) {
                System.out.println("✅ User authentication: SUCCESS");
                System.out.println("   User: " + user.getFullName() + " (" + user.getRole() + ")");
            } else {
                System.out.println("❌ User authentication: FAILED - User not found or wrong credentials");
                
                // Check if admin user exists
                System.out.println("\n3. Checking if admin user exists in database...");
                // This would require a method to check user existence
                System.out.println("   Please verify admin user exists in users table");
            }
        } catch (Exception e) {
            System.out.println("❌ User authentication: ERROR");
            System.out.println("   Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}