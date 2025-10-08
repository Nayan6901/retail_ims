package com.retail.ims.dao;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product operations
 */
public class ProductDAO {
    
    /**
     * Get all products with stock information
     * @return List of products
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM v_product_stock ORDER BY product_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(extractProductFromView(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get active products only
     * @return List of active products
     */
    public List<Product> getActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM v_product_stock WHERE is_active = TRUE ORDER BY product_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(extractProductFromView(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get product by ID
     * @param productId Product ID
     * @return Product object
     */
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM v_product_stock WHERE product_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractProductFromView(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting product: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Search products by name or code
     * @param searchTerm Search term
     * @return List of matching products
     */
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM v_product_stock " +
                     "WHERE (product_name LIKE ? OR product_code LIKE ?) " +
                     "AND is_active = TRUE " +
                     "ORDER BY product_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(extractProductFromView(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get low stock products
     * @return List of low stock products
     */
    public List<Product> getLowStockProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM v_low_stock_products";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductCode(rs.getString("product_code"));
                product.setProductName(rs.getString("product_name"));
                product.setCategoryName(rs.getString("category_name"));
                product.setCurrentStock(rs.getInt("current_stock"));
                product.setMinStockLevel(rs.getInt("min_stock_level"));
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting low stock products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Create new product
     * @param product Product object
     * @return true if successful
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (product_code, product_name, description, " +
                     "category_id, supplier_id, unit, cost_price, selling_price, " +
                     "min_stock_level, max_stock_level, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, product.getProductCode());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getDescription());
            pstmt.setInt(4, product.getCategoryId());
            pstmt.setInt(5, product.getSupplierId());
            pstmt.setString(6, product.getUnit());
            pstmt.setDouble(7, product.getCostPrice());
            pstmt.setDouble(8, product.getSellingPrice());
            pstmt.setInt(9, product.getMinStockLevel());
            pstmt.setInt(10, product.getMaxStockLevel());
            pstmt.setBoolean(11, product.isActive());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int productId = rs.getInt(1);
                    
                    // Initialize stock
                    String stockSql = "INSERT INTO stock (product_id, quantity) VALUES (?, 0)";
                    PreparedStatement stockPstmt = conn.prepareStatement(stockSql);
                    stockPstmt.setInt(1, productId);
                    stockPstmt.executeUpdate();
                }
                
                conn.commit();
                return true;
            }
            
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
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
     * Update product
     * @param product Product object
     * @return true if successful
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET product_name = ?, description = ?, " +
                     "category_id = ?, supplier_id = ?, unit = ?, " +
                     "cost_price = ?, selling_price = ?, " +
                     "min_stock_level = ?, max_stock_level = ?, is_active = ? " +
                     "WHERE product_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setInt(3, product.getCategoryId());
            pstmt.setInt(4, product.getSupplierId());
            pstmt.setString(5, product.getUnit());
            pstmt.setDouble(6, product.getCostPrice());
            pstmt.setDouble(7, product.getSellingPrice());
            pstmt.setInt(8, product.getMinStockLevel());
            pstmt.setInt(9, product.getMaxStockLevel());
            pstmt.setBoolean(10, product.isActive());
            pstmt.setInt(11, product.getProductId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete product (soft delete)
     * @param productId Product ID
     * @return true if successful
     */
    public boolean deleteProduct(int productId) {
        String sql = "UPDATE products SET is_active = FALSE WHERE product_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract Product from view ResultSet
     * @param rs ResultSet
     * @return Product object
     * @throws SQLException if database error occurs
     */
    private Product extractProductFromView(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductCode(rs.getString("product_code"));
        product.setProductName(rs.getString("product_name"));
        product.setCategoryName(rs.getString("category_name"));
        product.setSupplierName(rs.getString("supplier_name"));
        product.setCostPrice(rs.getDouble("cost_price"));
        product.setSellingPrice(rs.getDouble("selling_price"));
        product.setCurrentStock(rs.getInt("current_stock"));
        product.setMinStockLevel(rs.getInt("min_stock_level"));
        product.setMaxStockLevel(rs.getInt("max_stock_level"));
        product.setActive(rs.getBoolean("is_active"));
        return product;
    }
}
