package com.retail.ims;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.controller.AuthController;
import com.retail.ims.controller.CompleteApiController;
import com.retail.ims.controller.StaticFileHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main application class
 */
public class Main {
    
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "web";
    
    public static void main(String[] args) {
        System.out.println("=== Retail Inventory Management System ===");
        System.out.println("Starting application...");
        
        // Test database connection
        if (!DatabaseConfig.testConnection()) {
            System.err.println("Failed to connect to database. Please check your configuration.");
            System.err.println("Make sure MySQL is running and credentials in .env file are correct.");
            return;
        }
        
        try {
            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Set up handlers
            server.createContext("/api/", new CompleteApiController());
            server.createContext("/auth/", new AuthController());
            server.createContext("/", new StaticFileHandler(WEB_ROOT));
            
            // Start server
            server.setExecutor(null);
            server.start();
            
            System.out.println("✅ Server started successfully!");
            System.out.println("🌐 Application running at: http://localhost:" + PORT);
            System.out.println("📊 Database: " + DatabaseConfig.getDbName());
            System.out.println("🔧 Web files directory: " + WEB_ROOT);
            System.out.println();
            System.out.println("Default Login Credentials:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println();
            System.out.println("Press Ctrl+C to stop the server");
            
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}