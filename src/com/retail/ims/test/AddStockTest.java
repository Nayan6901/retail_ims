package com.retail.ims.test;

import com.retail.ims.config.DatabaseConfig;
import java.sql.*;

/**
 * Add stock to existing products
 */
public class AddStockTest {
    
    public static void main(String[] args) {
        System.out.println("=== Adding Stock to Products ===");
        
        if (!DatabaseConfig.testConnection()) {
            System.err.println("Database connection failed!");
            return;
        }
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Check current products and stock
            String selectSql = "SELECT p.product_id, p.product_name, COALESCE(s.quantity, 0) as stock " +
                              "FROM products p LEFT JOIN stock s ON p.product_id = s.product_id";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                System.out.println("Current products:");
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Stock: %d\n", 
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("stock"));
                }
            }
            
            // Add stock entries for products that don't have stock records
            String insertStockSql = "INSERT INTO stock (product_id, quantity) " +
                                   "SELECT p.product_id, 25 FROM products p " +
                                   "WHERE p.product_id NOT IN (SELECT product_id FROM stock)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertStockSql)) {
                int rowsInserted = pstmt.executeUpdate();
                System.out.println("\nInserted " + rowsInserted + " stock records with quantity = 25");
            }
            
            // Update existing stock records that have 0 quantity
            String updateSql = "UPDATE stock SET quantity = 25 WHERE quantity = 0";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                int rowsUpdated = pstmt.executeUpdate();
                System.out.println("Updated " + rowsUpdated + " existing stock records to quantity = 25");
            }
            
            // Check again
            System.out.println("\nUpdated products:");
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Stock: %d\n", 
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("stock"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Stock Update Complete ===");
    }
}