package com.retail.ims.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration and connection management
 */
public class DatabaseConfig {
    
    private static String DB_HOST;
    private static String DB_NAME;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_URL;
    
    static {
        loadConfig();
    }
    
    /**
     * Load database configuration from .env file
     */
    private static void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
            DB_HOST = props.getProperty("DB_HOST", "localhost");
            DB_NAME = props.getProperty("DB_NAME", "inventory_db");
            DB_USER = props.getProperty("DB_USER", "root");
            DB_PASSWORD = props.getProperty("DB_PASSWORD", "");
            
            DB_URL = "jdbc:mysql://" + DB_HOST + ":3306/" + DB_NAME 
                   + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Database configuration loaded successfully!");
            
        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Test database connection
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection test: SUCCESS");
            System.out.println("Connected to: " + DB_NAME);
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED");
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Close database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    // Getters
    public static String getDbUrl() {
        return DB_URL;
    }
    
    public static String getDbName() {
        return DB_NAME;
    }
}
