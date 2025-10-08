package com.retail.ims.controller;

import com.retail.ims.dao.UserDAO;
import com.retail.ims.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication controller
 */
public class AuthController implements HttpHandler {
    
    private UserDAO userDAO;
    
    public AuthController() {
        this.userDAO = new UserDAO();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        if ("POST".equalsIgnoreCase(method) && "/auth/api/login".equals(path)) {
            handleLogin(exchange);
        } else if ("POST".equalsIgnoreCase(method) && "/auth/api/logout".equals(path)) {
            handleLogout(exchange);
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        try {
            // Read request body
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            // Parse form data
            Map<String, String> params = parseFormData(requestBody);
            String username = params.get("username");
            String password = params.get("password");
            
            if (username == null || password == null) {
                sendJsonResponse(exchange, 400, "{\"success\": false, \"message\": \"Username and password required\"}");
                return;
            }
            
            // Authenticate user
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {
                // Create session (simplified - in real app use proper session management)
                String sessionData = String.format(
                    "{\"success\": true, \"user\": {\"id\": %d, \"username\": \"%s\", \"fullName\": \"%s\", \"role\": \"%s\"}}",
                    user.getUserId(), user.getUsername(), user.getFullName(), user.getRole()
                );
                
                // Set session cookie
                exchange.getResponseHeaders().add("Set-Cookie", "user_session=" + user.getUserId() + "; Path=/; HttpOnly");
                sendJsonResponse(exchange, 200, sessionData);
            } else {
                sendJsonResponse(exchange, 401, "{\"success\": false, \"message\": \"Invalid credentials\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error\"}");
        }
    }
    
    private void handleLogout(HttpExchange exchange) throws IOException {
        // Clear session cookie
        exchange.getResponseHeaders().add("Set-Cookie", "user_session=; Path=/; HttpOnly; Max-Age=0");
        sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Logged out successfully\"}");
    }
    
    private Map<String, String> parseFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = formData.split("&");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    params.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return params;
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }
}