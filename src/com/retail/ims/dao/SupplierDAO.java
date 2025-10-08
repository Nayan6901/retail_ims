package com.retail.ims.dao;

import com.retail.ims.config.DatabaseConfig;
import com.retail.ims.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Supplier operations
 */
public class SupplierDAO {
    
    /**
     * Get all suppliers
     * @return List of suppliers
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY supplier_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                suppliers.add(extractSupplierFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting suppliers: " + e.getMessage());
        }
        
        return suppliers;
    }
    
    /**
     * Get active suppliers only
     * @return List of active suppliers
     */
    public List<Supplier> getActiveSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE is_active = TRUE ORDER BY supplier_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                suppliers.add(extractSupplierFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active suppliers: " + e.getMessage());
        }
        
        return suppliers;
    }
    
    /**
     * Get supplier by ID
     * @param supplierId Supplier ID
     * @return Supplier object
     */
    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSupplierFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting supplier: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create new supplier
     * @param supplier Supplier object
     * @return true if successful
     */
    public boolean createSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (supplier_name, contact_person, phone, email, " +
                     "address, city, state, pincode, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactPerson());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setString(6, supplier.getCity());
            pstmt.setString(7, supplier.getState());
            pstmt.setString(8, supplier.getPincode());
            pstmt.setBoolean(9, supplier.isActive());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating supplier: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update supplier
     * @param supplier Supplier object
     * @return true if successful
     */
    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET supplier_name = ?, contact_person = ?, " +
                     "phone = ?, email = ?, address = ?, city = ?, state = ?, " +
                     "pincode = ?, is_active = ? " +
                     "WHERE supplier_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactPerson());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setString(6, supplier.getCity());
            pstmt.setString(7, supplier.getState());
            pstmt.setString(8, supplier.getPincode());
            pstmt.setBoolean(9, supplier.isActive());
            pstmt.setInt(10, supplier.getSupplierId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete supplier (soft delete)
     * @param supplierId Supplier ID
     * @return true if successful
     */
    public boolean deleteSupplier(int supplierId) {
        String sql = "UPDATE suppliers SET is_active = FALSE WHERE supplier_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, supplierId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract Supplier from ResultSet
     * @param rs ResultSet
     * @return Supplier object
     * @throws SQLException if database error occurs
     */
    private Supplier extractSupplierFromResultSet(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(rs.getInt("supplier_id"));
        supplier.setSupplierName(rs.getString("supplier_name"));
        supplier.setContactPerson(rs.getString("contact_person"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        supplier.setCity(rs.getString("city"));
        supplier.setState(rs.getString("state"));
        supplier.setPincode(rs.getString("pincode"));
        supplier.setActive(rs.getBoolean("is_active"));
        supplier.setCreatedAt(rs.getTimestamp("created_at"));
        supplier.setUpdatedAt(rs.getTimestamp("updated_at"));
        return supplier;
    }
}
