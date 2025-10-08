package com.retail.ims.service;

import com.retail.ims.dao.ProductDAO;
import com.retail.ims.dao.SaleDAO;
import com.retail.ims.dao.CategoryDAO;
import com.retail.ims.dao.SupplierDAO;
import com.retail.ims.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.retail.ims.config.DatabaseConfig;

/**
 * Dashboard service for analytics and statistics
 */
public class DashboardService {
    
    private ProductDAO productDAO;
    private SaleDAO saleDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    
    public DashboardService() {
        this.productDAO = new ProductDAO();
        this.saleDAO = new SaleDAO();
        this.categoryDAO = new CategoryDAO();
        this.supplierDAO = new SupplierDAO();
    }
    
    /**
     * Get dashboard statistics
     * @return Map containing all dashboard stats
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Call stored procedure for dashboard stats
            String sql = "CALL sp_get_dashboard_stats()";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                stats.put("totalProducts", rs.getInt("total_products"));
                stats.put("lowStockCount", rs.getInt("low_stock_count"));
                stats.put("todaySales", rs.getDouble("today_sales"));
                stats.put("monthSales", rs.getDouble("month_sales"));
                stats.put("todayTransactions", rs.getInt("today_transactions"));
                stats.put("totalSuppliers", rs.getInt("total_suppliers"));
                stats.put("totalCategories", rs.getInt("total_categories"));
                stats.put("inventoryValue", rs.getDouble("inventory_value"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting dashboard stats: " + e.getMessage());
            // Return default values
            stats.put("totalProducts", 0);
            stats.put("lowStockCount", 0);
            stats.put("todaySales", 0.0);
            stats.put("monthSales", 0.0);
            stats.put("todayTransactions", 0);
            stats.put("totalSuppliers", 0);
            stats.put("totalCategories", 0);
            stats.put("inventoryValue", 0.0);
        }
        
        return stats;
    }
    
    /**
     * Get sales chart data for last 7 days
     * @return Map with dates and sales amounts
     */
    public Map<String, Double> getWeeklySalesChart() {
        Map<String, Double> salesData = new HashMap<>();
        String sql = "SELECT DATE(sale_date) as sale_date, SUM(total_amount) as daily_total " +
                     "FROM sales " +
                     "WHERE sale_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                     "GROUP BY DATE(sale_date) " +
                     "ORDER BY sale_date";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                salesData.put(rs.getString("sale_date"), rs.getDouble("daily_total"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting weekly sales data: " + e.getMessage());
        }
        
        return salesData;
    }
    
    /**
     * Get top selling products
     * @param limit Number of products to return
     * @return Map with product names and quantities sold
     */
    public Map<String, Integer> getTopSellingProducts(int limit) {
        Map<String, Integer> topProducts = new HashMap<>();
        String sql = "SELECT p.product_name, SUM(si.quantity) as total_quantity " +
                     "FROM sale_items si " +
                     "JOIN products p ON si.product_id = p.product_id " +
                     "JOIN sales s ON si.sale_id = s.sale_id " +
                     "WHERE s.sale_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                     "GROUP BY p.product_id, p.product_name " +
                     "ORDER BY total_quantity DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                topProducts.put(rs.getString("product_name"), rs.getInt("total_quantity"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting top selling products: " + e.getMessage());
        }
        
        return topProducts;
    }
}