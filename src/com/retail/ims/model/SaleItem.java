package com.retail.ims.model;

/**
 * SaleItem model class (line items in a sale)
 */
public class SaleItem {
    private int saleItemId;
    private int saleId;
    private int productId;
    private String productName; // For display
    private String productCode; // For display
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    
    // Constructors
    public SaleItem() {}
    
    public SaleItem(int productId, int quantity, double unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }
    
    // Getters and Setters
    public int getSaleItemId() {
        return saleItemId;
    }
    
    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }
    
    public int getSaleId() {
        return saleId;
    }
    
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * unitPrice;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    @Override
    public String toString() {
        return "SaleItem{" +
                "saleItemId=" + saleItemId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
