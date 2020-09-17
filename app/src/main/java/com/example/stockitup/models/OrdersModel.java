package com.example.stockitup.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Model file for Orders
 */
public class OrdersModel {
    private String date;
    private String subtotal;
    private String tax;
    private String deliveryCharge;
    private String total;
    private String address;

    public OrdersModel() {
    }

    /**
     * Constructor to initialize name, image, price, desciption and ingredients of product
     * @param address Order address
     * @param date Order date
     * @param deliveryCharge Delivery fees
     * @param subtotal Amount before taxes
     * @param tax tax amount
     * @param total Order total
     */
    public OrdersModel(String date, String subtotal, String tax, String deliveryCharge, String total, String address) {
        this.date = date;
        this.subtotal = subtotal;
        this.tax = tax;
        this.deliveryCharge = deliveryCharge;
        this.total = total;
        this.address = address;
    }

    /**
     * Getter method for order date
     * @return Order date
     */
    public String getDate() {
        return date;
    }

    /**
     * setter method for order date
     * @param date Order date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter method for subtotal of order
     * @return subtotal
     */
    public String getSubtotal() {
        return subtotal;
    }

    /**
     * setter method for subtotal for an order
     * @param subtotal total before taxes
     */
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Getter method for tax amount
     * @return Tax on order
     */
    public String getTax() {
        return tax;
    }

    /**
     * Setter method for tax amount
     * @param tax Tax to be applied on order
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    /**
     * Getter method for delivery fees
     * @return Delivery fees on order
     */
    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    /**
     * Setter method for delivery fees
     * @param deliveryCharge Delivery fees to be applied on order
     */
    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    /**
     * Getter method for Total amount
     * @return Total amount after all charges
     */
    public String getTotal() {
        return total;
    }

    /**
     * Setter method for Total amount
     * @param total Total amount after all charges
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * Getter method to get address of an order
     * @return Address to which order was placed
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter method to set the address for a Order
     * @param address address to to which Order is placed.
     */
    public void setAddress(String address) {
        this.address = address;
    }
}