package com.retail.ims.dao;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.model.User;

import java.sql.*;

/**
 * Data Access Object for User operations
 */
public class UserDAO {
    
    /**
     * Authenticate user
     * @param username Username
     * @param password Password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User object
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create new user
     * @param user User object
     * @return true if successful
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());
            pstmt.setBoolean(6, user.isActive());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract User object from ResultSet
     * @param rs ResultSet
     * @return User object
     * @throws SQLException if database error occurs
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
