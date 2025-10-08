package com.retail.ims.model;

import java.sql.Timestamp;

/**
 * Product model class
 */
public class Product {
    private int productId;
    private String productCode;
    private String productName;
    private String description;
    private int categoryId;
    private String categoryName; // For display
    private int supplierId;
    private String supplierName; // For display
    private String unit;
    private double costPrice;
    private double sellingPrice;
    private int minStockLevel;
    private int maxStockLevel;
    private int currentStock; // From stock table
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Product() {}
    
    public Product(String productCode, String productName, String description, 
                   int categoryId, int supplierId, String unit, 
                   double costPrice, double sellingPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.unit = unit;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.minStockLevel = 10;
        this.maxStockLevel = 1000;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public int getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public double getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public int getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    
    public int getMaxStockLevel() {
        return maxStockLevel;
    }
    
    public void setMaxStockLevel(int maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", currentStock=" + currentStock +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}
