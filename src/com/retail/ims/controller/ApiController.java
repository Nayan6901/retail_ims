package com.retail.ims.controller;

import com.retail.ims.service.DashboardService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

import com.retail.ims.util.JsonUtil;

/**
 * API controller for dashboard data
 */
public class ApiController implements HttpHandler {
    
    private DashboardService dashboardService;
    
    public ApiController() {
        this.dashboardService = new DashboardService();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Set CORS headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        if ("GET".equalsIgnoreCase(method)) {
            switch (path) {
                case "/api/dashboard/stats":
                    handleDashboardStats(exchange);
                    break;
                case "/api/dashboard/weekly-sales":
                    handleWeeklySales(exchange);
                    break;
                case "/api/dashboard/top-products":
                    handleTopProducts(exchange);
                    break;
                default:
                    sendJsonResponse(exchange, 404, "{\"error\": \"Endpoint not found\"}");
            }
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    private void handleDashboardStats(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> stats = dashboardService.getDashboardStats();
            String jsonResponse = JsonUtil.toJson(stats);
            sendJsonResponse(exchange, 200, jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"Server error\"}");
        }
    }
    
    private void handleWeeklySales(HttpExchange exchange) throws IOException {
        try {
            Map<String, Double> salesData = dashboardService.getWeeklySalesChart();
            Map<String, Object> response = new HashMap<>();
            response.putAll(salesData);
            String jsonResponse = JsonUtil.toJson(response);
            sendJsonResponse(exchange, 200, jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"Server error\"}");
        }
    }
    
    private void handleTopProducts(HttpExchange exchange) throws IOException {
        try {
            Map<String, Integer> topProducts = dashboardService.getTopSellingProducts(5);
            Map<String, Object> response = new HashMap<>();
            response.putAll(topProducts);
            String jsonResponse = JsonUtil.toJson(response);
            sendJsonResponse(exchange, 200, jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"Server error\"}");
        }
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }
}