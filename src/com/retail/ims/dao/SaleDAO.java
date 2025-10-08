package com.retail.ims.dao;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.model.Sale;
import com.retail.ims.model.SaleItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Sale operations
 */
public class SaleDAO {
    
    /**
     * Get all sales
     * @return List of sales
     */
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM v_sales_summary ORDER BY sale_date DESC LIMIT 100";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                sales.add(extractSaleFromView(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sales: " + e.getMessage());
        }
        
        return sales;
    }
    
    /**
     * Get sale by ID with items
     * @param saleId Sale ID
     * @return Sale object with items
     */
    public Sale getSaleById(int saleId) {
        String sql = "SELECT * FROM sales WHERE sale_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Sale sale = extractSaleFromResultSet(rs);
                sale.setSaleItems(getSaleItems(saleId));
                return sale;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sale: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get sale items for a sale
     * @param saleId Sale ID
     * @return List of sale items
     */
    public List<SaleItem> getSaleItems(int saleId) {
        List<SaleItem> items = new ArrayList<>();
        String sql = "SELECT si.*, p.product_name, p.product_code " +
                     "FROM sale_items si " +
                     "JOIN products p ON si.product_id = p.product_id " +
                     "WHERE si.sale_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                items.add(extractSaleItemFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sale items: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Create new sale with items
     * @param sale Sale object with items
     * @return sale ID if successful, -1 otherwise
     */
    public int createSale(Sale sale) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Generate sale number
            String saleNumber = generateSaleNumber(conn);
            sale.setSaleNumber(saleNumber);
            
            // Insert sale header
            String saleSql = "INSERT INTO sales (sale_number, customer_name, customer_phone, " +
                           "discount, tax, payment_method, payment_status, notes, created_by) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement salePstmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            salePstmt.setString(1, sale.getSaleNumber());
            salePstmt.setString(2, sale.getCustomerName());
            salePstmt.setString(3, sale.getCustomerPhone());
            salePstmt.setDouble(4, sale.getDiscount());
            salePstmt.setDouble(5, sale.getTax());
            salePstmt.setString(6, sale.getPaymentMethod());
            salePstmt.setString(7, sale.getPaymentStatus());
            salePstmt.setString(8, sale.getNotes());
            salePstmt.setInt(9, sale.getCreatedBy());
            
            salePstmt.executeUpdate();
            
            ResultSet rs = salePstmt.getGeneratedKeys();
            int saleId = -1;
            if (rs.next()) {
                saleId = rs.getInt(1);
            }
            
            // Insert sale items
            String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, " +
                           "unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement itemPstmt = conn.prepareStatement(itemSql);
            
            for (SaleItem item : sale.getSaleItems()) {
                itemPstmt.setInt(1, saleId);
                itemPstmt.setInt(2, item.getProductId());
                itemPstmt.setInt(3, item.getQuantity());
                itemPstmt.setDouble(4, item.getUnitPrice());
                itemPstmt.setDouble(5, item.getTotalPrice());
                itemPstmt.addBatch();
            }
            
            itemPstmt.executeBatch();
            
            conn.commit();
            return saleId;
            
        } catch (SQLException e) {
            System.err.println("Error creating sale: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Get today's sales total
     * @return Total sales amount
     */
    public double getTodaySales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total " +
                     "FROM sales WHERE DATE(sale_date) = CURDATE()";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting today's sales: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    /**
     * Get this month's sales total
     * @return Total sales amount
     */
    public double getMonthSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total " +
                     "FROM sales WHERE MONTH(sale_date) = MONTH(CURDATE()) " +
                     "AND YEAR(sale_date) = YEAR(CURDATE())";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting month sales: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    /**
     * Generate new sale number
     * @param conn Database connection
     * @return Sale number
     * @throws SQLException if database error occurs
     */
    private String generateSaleNumber(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(sale_number, 6) AS UNSIGNED)), 0) + 1 as next_num FROM sales";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            int nextNum = rs.getInt("next_num");
            return String.format("SALE-%06d", nextNum);
        }
        
        return "SALE-000001";
    }
    
    /**
     * Extract Sale from ResultSet
     * @param rs ResultSet
     * @return Sale object
     * @throws SQLException if database error occurs
     */
    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSaleId(rs.getInt("sale_id"));
        sale.setSaleNumber(rs.getString("sale_number"));
        sale.setSaleDate(rs.getTimestamp("sale_date"));
        sale.setCustomerName(rs.getString("customer_name"));
        sale.setCustomerPhone(rs.getString("customer_phone"));
        sale.setSubtotal(rs.getDouble("subtotal"));
        sale.setDiscount(rs.getDouble("discount"));
        sale.setTax(rs.getDouble("tax"));
        sale.setTotalAmount(rs.getDouble("total_amount"));
        sale.setPaymentMethod(rs.getString("payment_method"));
        sale.setPaymentStatus(rs.getString("payment_status"));
        sale.setNotes(rs.getString("notes"));
        sale.setCreatedBy(rs.getInt("created_by"));
        return sale;
    }
    
    /**
     * Extract Sale from view ResultSet
     * @param rs ResultSet
     * @return Sale object
     * @throws SQLException if database error occurs
     */
    private Sale extractSaleFromView(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSaleId(rs.getInt("sale_id"));
        sale.setSaleNumber(rs.getString("sale_number"));
        sale.setSaleDate(rs.getTimestamp("sale_date"));
        sale.setCustomerName(rs.getString("customer_name"));
        sale.setCustomerPhone(rs.getString("customer_phone"));
        sale.setTotalAmount(rs.getDouble("total_amount"));
        sale.setPaymentMethod(rs.getString("payment_method"));
        sale.setPaymentStatus(rs.getString("payment_status"));
        sale.setCreatedByName(rs.getString("created_by_name"));
        return sale;
    }
    
    /**
     * Extract SaleItem from ResultSet
     * @param rs ResultSet
     * @return SaleItem object
     * @throws SQLException if database error occurs
     */
    private SaleItem extractSaleItemFromResultSet(ResultSet rs) throws SQLException {
        SaleItem item = new SaleItem();
        item.setSaleItemId(rs.getInt("sale_item_id"));
        item.setSaleId(rs.getInt("sale_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setProductCode(rs.getString("product_code"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setTotalPrice(rs.getDouble("total_price"));
        return item;
    }
}
