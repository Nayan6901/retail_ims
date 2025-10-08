package com.retail.ims.dao;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category operations
 */
public class CategoryDAO {
    
    /**
     * Get all categories
     * @return List of categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object
     */
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting category: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create new category
     * @param category Category object
     * @return true if successful
     */
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO categories (category_name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating category: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update category
     * @param category Category object
     * @return true if successful
     */
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getCategoryId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete category
     * @param categoryId Category ID
     * @return true if successful
     */
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract Category from ResultSet
     * @param rs ResultSet
     * @return Category object
     * @throws SQLException if database error occurs
     */
    private Category extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setUpdatedAt(rs.getTimestamp("updated_at"));
        return category;
    }
}
