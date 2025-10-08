package com.retail.ims.test;

import com.retail.ims.config.DatabaseConfig;

/**
 * Simple test to send a sale request to the API
 */
public class SaleApiTest {
    
    public static void main(String[] args) {
        System.out.println("=== Sale API Test ===");
        
        // Test database connection first
        if (!DatabaseConfig.testConnection()) {
            System.err.println("Database connection failed!");
            return;
        }
        
        try {
            String jsonPayload = "{\n" +
                "  \"customerName\": \"Test Customer\",\n" +
                "  \"paymentMethod\": \"cash\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"productId\": 1,\n" +
                "      \"productName\": \"SAMSUNG LED\",\n" +
                "      \"price\": 12000.00,\n" +
                "      \"quantity\": 1\n" +
                "    }\n" +
                "  ],\n" +
                "  \"subtotal\": 12000.00,\n" +
                "  \"tax\": 2160.00,\n" +
                "  \"total\": 14160.00\n" +
                "}";
            
            System.out.println("JSON Payload to test:");
            System.out.println(jsonPayload);
            
            // Simple HTTP request to test the API
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8080/api/sales"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
            
            java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
            
            System.out.println("\nResponse Status: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            
        } catch (Exception e) {
            System.err.println("Error testing API: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}