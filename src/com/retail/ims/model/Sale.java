package com.retail.ims.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale model class
 */
public class Sale {
    private int saleId;
    private String saleNumber;
    private Timestamp saleDate;
    private String customerName;
    private String customerPhone;
    private double subtotal;
    private double discount;
    private double tax;
    private double totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private int createdBy;
    private String createdByName; // For display
    private List<SaleItem> saleItems;
    
    // Constructors
    public Sale() {
        this.saleItems = new ArrayList<>();
    }
    
    public Sale(String saleNumber, String customerName, String customerPhone, 
                String paymentMethod, int createdBy) {
        this.saleNumber = saleNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.paymentMethod = paymentMethod;
        this.createdBy = createdBy;
        this.paymentStatus = "paid";
        this.saleItems = new ArrayList<>();
    }
    
    // Calculate totals
    public void calculateTotals() {
        subtotal = saleItems.stream()
                            .mapToDouble(SaleItem::getTotalPrice)
                            .sum();
        totalAmount = subtotal - discount + tax;
    }
    
    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }
    
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    
    public String getSaleNumber() {
        return saleNumber;
    }
    
    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }
    
    public Timestamp getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public double getTax() {
        return tax;
    }
    
    public void setTax(double tax) {
        this.tax = tax;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    
    public List<SaleItem> getSaleItems() {
        return saleItems;
    }
    
    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }
    
    public void addSaleItem(SaleItem item) {
        this.saleItems.add(item);
    }
    
    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", saleNumber='" + saleNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
