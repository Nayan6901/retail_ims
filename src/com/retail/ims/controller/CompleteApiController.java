package com.retail.ims.controller;

import com.retail.ims.dao.*;
import com.retail.ims.model.*;
import com.retail.ims.service.DashboardService;
import com.retail.ims.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Complete API controller for all endpoints
 */
public class CompleteApiController implements HttpHandler {
    
    private DashboardService dashboardService;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private SaleDAO saleDAO;
    private UserDAO userDAO;
    
    public CompleteApiController() {
        this.dashboardService = new DashboardService();
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
        this.supplierDAO = new SupplierDAO();
        this.saleDAO = new SaleDAO();
        this.userDAO = new UserDAO();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Set CORS headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        try {
            // Dashboard endpoints
            if (path.startsWith("/api/dashboard/")) {
                handleDashboardEndpoints(exchange, method, path);
            }
            // Product endpoints
            else if (path.startsWith("/api/products")) {
                handleProductEndpoints(exchange, method, path);
            }
            // Category endpoints
            else if (path.startsWith("/api/categories")) {
                handleCategoryEndpoints(exchange, method, path);
            }
            // Supplier endpoints
            else if (path.startsWith("/api/suppliers")) {
                handleSupplierEndpoints(exchange, method, path);
            }
            // Sale endpoints
            else if (path.startsWith("/api/sales")) {
                handleSaleEndpoints(exchange, method, path);
            }
            // Report endpoints
            else if (path.startsWith("/api/reports")) {
                handleReportEndpoints(exchange, method, path);
            }
            // Authentication endpoints
            else if (path.startsWith("/api/auth")) {
                handleAuthEndpoints(exchange, method, path);
            }
            else {
                sendJsonResponse(exchange, 404, "{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleDashboardEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        if (!"GET".equalsIgnoreCase(method)) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }
        
        switch (path) {
            case "/api/dashboard/stats":
                Map<String, Object> stats = dashboardService.getDashboardStats();
                sendJsonResponse(exchange, 200, JsonUtil.toJson(stats));
                break;
                
            case "/api/dashboard/weekly-sales":
                Map<String, Double> salesData = dashboardService.getWeeklySalesChart();
                Map<String, Object> salesResponse = new HashMap<>();
                salesResponse.putAll(salesData);
                sendJsonResponse(exchange, 200, JsonUtil.toJson(salesResponse));
                break;
                
            case "/api/dashboard/top-products":
                Map<String, Integer> topProducts = dashboardService.getTopSellingProducts(5);
                Map<String, Object> topResponse = new HashMap<>();
                topResponse.putAll(topProducts);
                sendJsonResponse(exchange, 200, JsonUtil.toJson(topResponse));
                break;
                
            default:
                sendJsonResponse(exchange, 404, "{\"error\": \"Dashboard endpoint not found\"}");
        }
    }
    
    private void handleProductEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        switch (method.toUpperCase()) {
            case "GET":
                if ("/api/products".equals(path)) {
                    List<Product> products = productDAO.getAllProducts();
                    sendJsonResponse(exchange, 200, convertProductsToJson(products));
                } else if (path.startsWith("/api/products/")) {
                    String idStr = path.substring("/api/products/".length());
                    if ("low-stock".equals(idStr)) {
                        List<Product> lowStockProducts = productDAO.getLowStockProducts();
                        sendJsonResponse(exchange, 200, convertProductsToJson(lowStockProducts));
                    } else {
                        try {
                            int productId = Integer.parseInt(idStr);
                            Product product = productDAO.getProductById(productId);
                            if (product != null) {
                                sendJsonResponse(exchange, 200, convertProductToJson(product));
                            } else {
                                sendJsonResponse(exchange, 404, "{\"error\": \"Product not found\"}");
                            }
                        } catch (NumberFormatException e) {
                            sendJsonResponse(exchange, 400, "{\"error\": \"Invalid product ID\"}");
                        }
                    }
                }
                break;
                
            case "POST":
                if ("/api/products".equals(path)) {
                    handleCreateProduct(exchange);
                }
                break;
                
            case "PUT":
                if (path.startsWith("/api/products/")) {
                    String idStr = path.substring("/api/products/".length());
                    try {
                        int productId = Integer.parseInt(idStr);
                        handleUpdateProduct(exchange, productId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid product ID\"}");
                    }
                }
                break;
                
            case "DELETE":
                if (path.startsWith("/api/products/")) {
                    String idStr = path.substring("/api/products/".length());
                    try {
                        int productId = Integer.parseInt(idStr);
                        handleDeleteProduct(exchange, productId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid product ID\"}");
                    }
                }
                break;
                
            default:
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    private void handleCategoryEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        switch (method.toUpperCase()) {
            case "GET":
                if ("/api/categories".equals(path)) {
                    List<Category> categories = categoryDAO.getAllCategories();
                    sendJsonResponse(exchange, 200, convertCategoriesToJson(categories));
                } else if (path.startsWith("/api/categories/")) {
                    String idStr = path.substring("/api/categories/".length());
                    try {
                        int categoryId = Integer.parseInt(idStr);
                        Category category = categoryDAO.getCategoryById(categoryId);
                        if (category != null) {
                            sendJsonResponse(exchange, 200, convertCategoryToJson(category));
                        } else {
                            sendJsonResponse(exchange, 404, "{\"error\": \"Category not found\"}");
                        }
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid category ID\"}");
                    }
                }
                break;
                
            case "POST":
                if ("/api/categories".equals(path)) {
                    handleCreateCategory(exchange);
                }
                break;
                
            case "PUT":
                if (path.startsWith("/api/categories/")) {
                    String idStr = path.substring("/api/categories/".length());
                    try {
                        int categoryId = Integer.parseInt(idStr);
                        handleUpdateCategory(exchange, categoryId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid category ID\"}");
                    }
                }
                break;
                
            case "DELETE":
                if (path.startsWith("/api/categories/")) {
                    String idStr = path.substring("/api/categories/".length());
                    try {
                        int categoryId = Integer.parseInt(idStr);
                        handleDeleteCategory(exchange, categoryId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid category ID\"}");
                    }
                }
                break;
                
            default:
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    private void handleSupplierEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        switch (method.toUpperCase()) {
            case "GET":
                if ("/api/suppliers".equals(path)) {
                    List<Supplier> suppliers = supplierDAO.getAllSuppliers();
                    sendJsonResponse(exchange, 200, convertSuppliersToJson(suppliers));
                } else if (path.startsWith("/api/suppliers/")) {
                    String pathSegment = path.substring("/api/suppliers/".length());
                    
                    // Check for /api/suppliers/{id}/products
                    if (pathSegment.contains("/products")) {
                        String[] parts = pathSegment.split("/");
                        if (parts.length >= 2 && "products".equals(parts[1])) {
                            try {
                                int supplierId = Integer.parseInt(parts[0]);
                                List<Product> products = productDAO.getProductsBySupplierId(supplierId);
                                sendJsonResponse(exchange, 200, convertProductsToJson(products));
                            } catch (NumberFormatException e) {
                                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid supplier ID\"}");
                            }
                        } else {
                            sendJsonResponse(exchange, 404, "{\"error\": \"Not found\"}");
                        }
                    } else {
                        // Single supplier by ID
                        try {
                            int supplierId = Integer.parseInt(pathSegment);
                            Supplier supplier = supplierDAO.getSupplierById(supplierId);
                            if (supplier != null) {
                                sendJsonResponse(exchange, 200, convertSupplierToJson(supplier));
                            } else {
                                sendJsonResponse(exchange, 404, "{\"error\": \"Supplier not found\"}");
                            }
                        } catch (NumberFormatException e) {
                            sendJsonResponse(exchange, 400, "{\"error\": \"Invalid supplier ID\"}");
                        }
                    }
                }
                break;
                
            case "POST":
                if ("/api/suppliers".equals(path)) {
                    handleCreateSupplier(exchange);
                }
                break;
                
            case "PUT":
                if (path.startsWith("/api/suppliers/")) {
                    String idStr = path.substring("/api/suppliers/".length());
                    try {
                        int supplierId = Integer.parseInt(idStr);
                        handleUpdateSupplier(exchange, supplierId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid supplier ID\"}");
                    }
                }
                break;
                
            case "DELETE":
                if (path.startsWith("/api/suppliers/")) {
                    String idStr = path.substring("/api/suppliers/".length());
                    try {
                        int supplierId = Integer.parseInt(idStr);
                        handleDeleteSupplier(exchange, supplierId);
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid supplier ID\"}");
                    }
                }
                break;
                
            default:
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    private void handleSaleEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        switch (method.toUpperCase()) {
            case "GET":
                if ("/api/sales".equals(path)) {
                    List<Sale> sales = saleDAO.getAllSales();
                    sendJsonResponse(exchange, 200, convertSalesToJson(sales));
                } else if (path.startsWith("/api/sales/")) {
                    String idStr = path.substring("/api/sales/".length());
                    try {
                        int saleId = Integer.parseInt(idStr);
                        Sale sale = saleDAO.getSaleById(saleId);
                        if (sale != null) {
                            sendJsonResponse(exchange, 200, convertSaleToJson(sale));
                        } else {
                            sendJsonResponse(exchange, 404, "{\"error\": \"Sale not found\"}");
                        }
                    } catch (NumberFormatException e) {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Invalid sale ID\"}");
                    }
                }
                break;
                
            case "POST":
                if ("/api/sales".equals(path)) {
                    handleCreateSale(exchange);
                }
                break;
                
            default:
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    private void handleAuthEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        if ("POST".equalsIgnoreCase(method) && "/api/auth/login".equals(path)) {
            handleLogin(exchange);
        } else if ("POST".equalsIgnoreCase(method) && "/api/auth/logout".equals(path)) {
            handleLogout(exchange);
        } else {
            sendJsonResponse(exchange, 404, "{\"error\": \"Auth endpoint not found\"}");
        }
    }
    
    private void handleReportEndpoints(HttpExchange exchange, String method, String path) throws IOException {
        if (!"GET".equalsIgnoreCase(method)) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }
        
        // Parse query parameters for date filters
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQueryParams(query);
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        
        try {
            switch (path) {
                case "/api/reports/quick-stats":
                    handleQuickStats(exchange);
                    break;
                case "/api/reports/sales":
                    handleSalesReport(exchange, startDate, endDate);
                    break;
                case "/api/reports/inventory":
                    handleInventoryReport(exchange);
                    break;
                case "/api/reports/low-stock":
                    handleLowStockReport(exchange);
                    break;
                case "/api/reports/products":
                    handleProductsReport(exchange, startDate, endDate);
                    break;
                case "/api/reports/categories":
                    handleCategoriesReport(exchange, startDate, endDate);
                    break;
                case "/api/reports/suppliers":
                    handleSuppliersReport(exchange);
                    break;
                case "/api/reports/profit":
                    handleProfitReport(exchange, startDate, endDate);
                    break;
                default:
                    sendJsonResponse(exchange, 404, "{\"error\": \"Report endpoint not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"Error generating report: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleQuickStats(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("todaySales", saleDAO.getTodaySales());
            stats.put("monthSales", saleDAO.getMonthSales());
            stats.put("lowStockCount", productDAO.getLowStockProducts().size());
            stats.put("totalProducts", productDAO.getAllProducts().size());
            
            sendJsonResponse(exchange, 200, convertMapToJson(stats));
        } catch (Exception e) {
            sendJsonResponse(exchange, 500, "{\"error\": \"Error loading stats\"}");
        }
    }
    
    private void handleSalesReport(HttpExchange exchange, String startDate, String endDate) throws IOException {
        List<Sale> sales;
        if (startDate != null && endDate != null) {
            sales = saleDAO.getSalesByDateRange(startDate, endDate);
        } else {
            sales = saleDAO.getAllSales();
        }
        sendJsonResponse(exchange, 200, convertSalesToJson(sales));
    }
    
    private void handleInventoryReport(HttpExchange exchange) throws IOException {
        List<Product> products = productDAO.getAllProducts();
        sendJsonResponse(exchange, 200, convertProductsToJson(products));
    }
    
    private void handleLowStockReport(HttpExchange exchange) throws IOException {
        List<Product> lowStockProducts = productDAO.getLowStockProducts();
        sendJsonResponse(exchange, 200, convertProductsToJson(lowStockProducts));
    }
    
    private void handleProductsReport(HttpExchange exchange, String startDate, String endDate) throws IOException {
        // This would require a new DAO method to get product performance data
        // For now, return product list
        List<Product> products = productDAO.getAllProducts();
        sendJsonResponse(exchange, 200, convertProductsToJson(products));
    }
    
    private void handleCategoriesReport(HttpExchange exchange, String startDate, String endDate) throws IOException {
        List<Category> categories = categoryDAO.getAllCategories();
        sendJsonResponse(exchange, 200, convertCategoriesToJson(categories));
    }
    
    private void handleSuppliersReport(HttpExchange exchange) throws IOException {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        sendJsonResponse(exchange, 200, convertSuppliersToJson(suppliers));
    }
    
    private void handleProfitReport(HttpExchange exchange, String startDate, String endDate) throws IOException {
        // This would require a new DAO method to calculate profit data
        // For now, return product list with pricing info
        List<Product> products = productDAO.getAllProducts();
        sendJsonResponse(exchange, 200, convertProductsToJson(products));
    }
    
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        params.put(keyValue[0], java.net.URLDecoder.decode(keyValue[1], "UTF-8"));
                    } catch (Exception e) {
                        // Ignore malformed parameters
                    }
                }
            }
        }
        return params;
    }
    
    private String convertMapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(escapeJson((String) entry.getValue())).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            Map<String, String> params = parseFormData(requestBody);
            String username = params.get("username");
            String password = params.get("password");
            
            if (username == null || password == null) {
                sendJsonResponse(exchange, 400, "{\"success\": false, \"message\": \"Username and password required\"}");
                return;
            }
            
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {
                String sessionData = String.format(
                    "{\"success\": true, \"user\": {\"id\": %d, \"username\": \"%s\", \"fullName\": \"%s\", \"role\": \"%s\"}}",
                    user.getUserId(), user.getUsername(), user.getFullName(), user.getRole()
                );
                
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
        exchange.getResponseHeaders().add("Set-Cookie", "user_session=; Path=/; HttpOnly; Max-Age=0");
        sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Logged out successfully\"}");
    }
    
    // CRUD Handlers for Products
    private void handleCreateProduct(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Product product = new Product();
            product.setProductCode(params.get("productCode"));
            product.setProductName(params.get("productName"));
            product.setDescription(params.get("description"));
            
            // Safe parsing with empty string checks
            product.setCategoryId(params.get("categoryId") != null && !params.get("categoryId").isEmpty() ? 
                Integer.parseInt(params.get("categoryId")) : 0);
            product.setSupplierId(params.get("supplierId") != null && !params.get("supplierId").isEmpty() ? 
                Integer.parseInt(params.get("supplierId")) : 0);
            product.setCostPrice(params.get("costPrice") != null && !params.get("costPrice").isEmpty() ? 
                Double.parseDouble(params.get("costPrice")) : 0.0);
            product.setSellingPrice(params.get("sellingPrice") != null && !params.get("sellingPrice").isEmpty() ? 
                Double.parseDouble(params.get("sellingPrice")) : 0.0);
            product.setCurrentStock(params.get("currentStock") != null && !params.get("currentStock").isEmpty() ? 
                Integer.parseInt(params.get("currentStock")) : 0);
            product.setMinStockLevel(params.get("minStockLevel") != null && !params.get("minStockLevel").isEmpty() ? 
                Integer.parseInt(params.get("minStockLevel")) : 0);
            product.setActive(true);
            
            boolean success = productDAO.createProduct(product);
            if (success) {
                sendJsonResponse(exchange, 201, "{\"success\": true, \"message\": \"Product created successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to create product\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleUpdateProduct(HttpExchange exchange, int productId) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Product not found\"}");
                return;
            }
            
            if (params.get("productCode") != null) product.setProductCode(params.get("productCode"));
            if (params.get("productName") != null) product.setProductName(params.get("productName"));
            if (params.get("description") != null) product.setDescription(params.get("description"));
            
            // Safe integer parsing
            if (params.get("categoryId") != null && !params.get("categoryId").isEmpty()) {
                product.setCategoryId(Integer.parseInt(params.get("categoryId")));
            }
            if (params.get("supplierId") != null && !params.get("supplierId").isEmpty()) {
                product.setSupplierId(Integer.parseInt(params.get("supplierId")));
            }
            if (params.get("currentStock") != null && !params.get("currentStock").isEmpty()) {
                product.setCurrentStock(Integer.parseInt(params.get("currentStock")));
            }
            if (params.get("minStockLevel") != null && !params.get("minStockLevel").isEmpty()) {
                product.setMinStockLevel(Integer.parseInt(params.get("minStockLevel")));
            }
            if (params.get("maxStockLevel") != null && !params.get("maxStockLevel").isEmpty()) {
                product.setMaxStockLevel(Integer.parseInt(params.get("maxStockLevel")));
            }
            
            // Safe double parsing
            if (params.get("costPrice") != null && !params.get("costPrice").isEmpty()) {
                product.setCostPrice(Double.parseDouble(params.get("costPrice")));
            }
            if (params.get("sellingPrice") != null && !params.get("sellingPrice").isEmpty()) {
                product.setSellingPrice(Double.parseDouble(params.get("sellingPrice")));
            }
            if (params.get("minStockLevel") != null) product.setMinStockLevel(Integer.parseInt(params.get("minStockLevel")));
            if (params.get("isActive") != null) product.setActive(Boolean.parseBoolean(params.get("isActive")));
            
            boolean success = productDAO.updateProduct(product);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Product updated successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to update product\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleDeleteProduct(HttpExchange exchange, int productId) throws IOException {
        try {
            boolean success = productDAO.deleteProduct(productId);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Product deleted successfully\"}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Product not found or could not be deleted\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    // CRUD Handlers for Categories
    private void handleCreateCategory(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Category category = new Category();
            category.setCategoryName(params.get("categoryName"));
            category.setDescription(params.get("description"));
            
            boolean success = categoryDAO.createCategory(category);
            if (success) {
                sendJsonResponse(exchange, 201, "{\"success\": true, \"message\": \"Category created successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to create category\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleUpdateCategory(HttpExchange exchange, int categoryId) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Category category = categoryDAO.getCategoryById(categoryId);
            if (category == null) {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Category not found\"}");
                return;
            }
            
            if (params.get("categoryName") != null) category.setCategoryName(params.get("categoryName"));
            if (params.get("description") != null) category.setDescription(params.get("description"));
            
            boolean success = categoryDAO.updateCategory(category);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Category updated successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to update category\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleDeleteCategory(HttpExchange exchange, int categoryId) throws IOException {
        try {
            boolean success = categoryDAO.deleteCategory(categoryId);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Category deleted successfully\"}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Category not found or could not be deleted\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    // CRUD Handlers for Suppliers
    private void handleCreateSupplier(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Supplier supplier = new Supplier();
            supplier.setSupplierName(params.get("supplierName"));
            supplier.setContactPerson(params.get("contactPerson"));
            supplier.setPhone(params.get("phone"));
            supplier.setEmail(params.get("email"));
            supplier.setAddress(params.get("address"));
            
            // Handle boolean isActive field properly
            String isActiveStr = params.get("isActive");
            boolean isActive = isActiveStr != null && ("true".equalsIgnoreCase(isActiveStr) || "1".equals(isActiveStr));
            supplier.setActive(isActive);
            
            boolean success = supplierDAO.createSupplier(supplier);
            if (success) {
                sendJsonResponse(exchange, 201, "{\"success\": true, \"message\": \"Supplier created successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to create supplier\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleUpdateSupplier(HttpExchange exchange, int supplierId) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseJsonBody(requestBody);
            
            Supplier supplier = supplierDAO.getSupplierById(supplierId);
            if (supplier == null) {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Supplier not found\"}");
                return;
            }
            
            if (params.get("supplierName") != null) supplier.setSupplierName(params.get("supplierName"));
            if (params.get("contactPerson") != null) supplier.setContactPerson(params.get("contactPerson"));
            if (params.get("phone") != null) supplier.setPhone(params.get("phone"));
            if (params.get("email") != null) supplier.setEmail(params.get("email"));
            if (params.get("address") != null) supplier.setAddress(params.get("address"));
            
            // Handle boolean isActive field properly
            if (params.get("isActive") != null) {
                String isActiveStr = params.get("isActive");
                boolean isActive = "true".equalsIgnoreCase(isActiveStr) || "1".equals(isActiveStr);
                supplier.setActive(isActive);
            }
            
            boolean success = supplierDAO.updateSupplier(supplier);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Supplier updated successfully\"}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to update supplier\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleDeleteSupplier(HttpExchange exchange, int supplierId) throws IOException {
        try {
            boolean success = supplierDAO.deleteSupplier(supplierId);
            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\": true, \"message\": \"Supplier deleted successfully\"}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\": false, \"message\": \"Supplier not found or could not be deleted\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    // Helper methods for JSON conversion
    private String convertProductsToJson(List<Product> products) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < products.size(); i++) {
            if (i > 0) json.append(",");
            json.append(convertProductToJson(products.get(i)));
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String convertProductToJson(Product product) {
        return String.format(
            "{\"productId\":%d,\"productCode\":\"%s\",\"productName\":\"%s\"," +
            "\"categoryName\":\"%s\",\"supplierName\":\"%s\"," +
            "\"costPrice\":%.2f,\"sellingPrice\":%.2f,\"currentStock\":%d," +
            "\"minStockLevel\":%d,\"maxStockLevel\":%d,\"isActive\":%s}",
            product.getProductId(),
            escapeJson(product.getProductCode()),
            escapeJson(product.getProductName()),
            escapeJson(product.getCategoryName()),
            escapeJson(product.getSupplierName()),
            product.getCostPrice(),
            product.getSellingPrice(),
            product.getCurrentStock(),
            product.getMinStockLevel(),
            product.getMaxStockLevel(),
            product.isActive()
        );
    }
    
    private String convertCategoriesToJson(List<Category> categories) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < categories.size(); i++) {
            if (i > 0) json.append(",");
            json.append(convertCategoryToJson(categories.get(i)));
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String convertCategoryToJson(Category category) {
        return String.format(
            "{\"categoryId\":%d,\"categoryName\":\"%s\",\"description\":\"%s\",\"productCount\":%d,\"createdAt\":\"%s\"}",
            category.getCategoryId(),
            escapeJson(category.getCategoryName()),
            escapeJson(category.getDescription()),
            category.getProductCount(),
            category.getCreatedAt() != null ? category.getCreatedAt().toString() : ""
        );
    }
    
    private String convertSuppliersToJson(List<Supplier> suppliers) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < suppliers.size(); i++) {
            if (i > 0) json.append(",");
            json.append(convertSupplierToJson(suppliers.get(i)));
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String convertSupplierToJson(Supplier supplier) {
        return String.format(
            "{\"supplierId\":%d,\"supplierName\":\"%s\",\"contactPerson\":\"%s\"," +
            "\"phone\":\"%s\",\"email\":\"%s\",\"address\":\"%s\",\"isActive\":%s," +
            "\"productCount\":%d}",
            supplier.getSupplierId(),
            escapeJson(supplier.getSupplierName()),
            escapeJson(supplier.getContactPerson()),
            escapeJson(supplier.getPhone()),
            escapeJson(supplier.getEmail()),
            escapeJson(supplier.getAddress()),
            supplier.isActive(),
            supplier.getProductCount()
        );
    }
    
    private String convertSalesToJson(List<Sale> sales) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < sales.size(); i++) {
            if (i > 0) json.append(",");
            Sale sale = sales.get(i);
            json.append(String.format(
                "{\"saleId\":%d,\"saleNumber\":\"%s\",\"customerName\":\"%s\"," +
                "\"totalAmount\":%.2f,\"paymentMethod\":\"%s\",\"saleDate\":\"%s\"," +
                "\"itemCount\":%d,\"totalQuantity\":%d}",
                sale.getSaleId(),
                escapeJson(sale.getSaleNumber()),
                escapeJson(sale.getCustomerName()),
                sale.getTotalAmount(),
                escapeJson(sale.getPaymentMethod()),
                sale.getSaleDate() != null ? sale.getSaleDate().toString() : "",
                sale.getTotalItems(),
                sale.getTotalQuantity()
            ));
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String convertSaleToJson(Sale sale) {
        return String.format(
            "{\"saleId\":%d,\"saleNumber\":\"%s\",\"customerName\":\"%s\"," +
            "\"totalAmount\":%.2f,\"paymentMethod\":\"%s\",\"saleDate\":\"%s\"," +
            "\"itemCount\":%d,\"totalQuantity\":%d}",
            sale.getSaleId(),
            escapeJson(sale.getSaleNumber()),
            escapeJson(sale.getCustomerName()),
            sale.getTotalAmount(),
            escapeJson(sale.getPaymentMethod()),
            sale.getSaleDate() != null ? sale.getSaleDate().toString() : "",
            sale.getTotalItems(),
            sale.getTotalQuantity()
        );
    }
    
    private void handleCreateSale(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            // Parse the JSON manually since we need to handle complex structure
            Sale sale = parseSaleFromJson(requestBody);
            
            // Set required fields
            sale.setPaymentStatus("paid");
            sale.setCreatedBy(1); // Default admin user
            
            // Create the sale
            int saleId = saleDAO.createSale(sale);
            if (saleId > 0) {
                sendJsonResponse(exchange, 201, "{\"success\": true, \"message\": \"Sale created successfully\", \"saleId\": " + saleId + "}");
            } else {
                sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Failed to create sale\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private Sale parseSaleFromJson(String jsonBody) {
        Sale sale = new Sale();
        
        try {
            // Extract basic fields
            String customerName = extractJsonValue(jsonBody, "customerName");
            String paymentMethod = extractJsonValue(jsonBody, "paymentMethod");
            double subtotal = Double.parseDouble(extractJsonValue(jsonBody, "subtotal"));
            double tax = Double.parseDouble(extractJsonValue(jsonBody, "tax"));
            double total = Double.parseDouble(extractJsonValue(jsonBody, "total"));
            
            sale.setCustomerName(customerName);
            sale.setPaymentMethod(paymentMethod);
            sale.setSubtotal(subtotal);
            sale.setTax(tax);
            sale.setTotalAmount(total);
            sale.setDiscount(0.0); // Default no discount
            
            // Extract items array
            String itemsJson = extractJsonArray(jsonBody, "items");
            List<SaleItem> saleItems = parseSaleItems(itemsJson);
            sale.setSaleItems(saleItems);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing sale JSON: " + e.getMessage());
        }
        
        return sale;
    }
    
    private String extractJsonValue(String json, String key) {
        // Simple extraction for string values
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        // Try numeric values
        pattern = "\"" + key + "\"\\s*:\\s*([0-9.]+)";
        p = java.util.regex.Pattern.compile(pattern);
        m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        return "";
    }
    
    private String extractJsonArray(String json, String key) {
        // Extract array content
        String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]+)\\]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
    
    private List<SaleItem> parseSaleItems(String itemsJson) {
        List<SaleItem> items = new ArrayList<>();
        
        if (itemsJson == null || itemsJson.trim().isEmpty()) {
            return items;
        }
        
        // Split items (simple approach)
        String[] itemParts = itemsJson.split("\\},\\s*\\{");
        
        for (String itemPart : itemParts) {
            itemPart = itemPart.replace("{", "").replace("}", "");
            
            try {
                SaleItem item = new SaleItem();
                
                // Extract values
                String productIdStr = extractValueFromPart(itemPart, "productId");
                String quantityStr = extractValueFromPart(itemPart, "quantity");
                String priceStr = extractValueFromPart(itemPart, "price");
                
                item.setProductId(Integer.parseInt(productIdStr));
                item.setQuantity(Integer.parseInt(quantityStr));
                item.setUnitPrice(Double.parseDouble(priceStr));
                item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
                
                items.add(item);
            } catch (Exception e) {
                System.err.println("Error parsing sale item: " + e.getMessage());
            }
        }
        
        return items;
    }
    
    private String extractValueFromPart(String part, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*([^,]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(part);
        if (m.find()) {
            return m.group(1).replace("\"", "").trim();
        }
        return "";
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
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
    
    private Map<String, String> parseJsonBody(String jsonBody) {
        Map<String, String> params = new HashMap<>();
        try {
            // Simple JSON parsing for key-value pairs
            jsonBody = jsonBody.trim();
            if (jsonBody.startsWith("{") && jsonBody.endsWith("}")) {
                jsonBody = jsonBody.substring(1, jsonBody.length() - 1);
                
                // Split by comma but handle quoted strings with commas
                String[] pairs = splitJsonPairs(jsonBody);
                
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2); // Split only on first colon
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replaceAll("\"", "");
                        String value = keyValue[1].trim();
                        
                        // Remove quotes from string values but preserve booleans and numbers
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        
                        params.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
    
    private String[] splitJsonPairs(String jsonContent) {
        // Simple split by comma that doesn't break quoted strings
        java.util.List<String> pairs = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (char c : jsonContent.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
            } else if (c == ',' && !inQuotes) {
                pairs.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            pairs.add(current.toString().trim());
        }
        
        return pairs.toArray(new String[0]);
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }
}