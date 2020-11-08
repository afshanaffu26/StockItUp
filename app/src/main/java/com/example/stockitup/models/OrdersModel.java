package com.example.stockitup.models;

import java.util.Date;

/**
 * Model file for Orders
 */
public class OrdersModel {
    private Date date;
    private String subtotal;
    private String offerPercent;
    private String offer;
    private String tax;
    private String deliveryCharge;
    private String total;
    private String address;
    private String status;

    /**
     * Non - parameterized constructor
     */
    public OrdersModel() {
    }

    public OrdersModel(Date date, String subtotal,String offer, String tax, String deliveryCharge, String total, String address, String status) {
        this.date = date;
        this.subtotal = subtotal;
        this.offer = offer;
        this.tax = tax;
        this.deliveryCharge = deliveryCharge;
        this.total = total;
        this.address = address;
        this.status = status;
    }

    /**
     * Constructor to initialize date, subtotal, offerPercent, offer, tax, deliveryCharge, total, address, status of an order
     * @param date order date
     * @param subtotal order subtotal
     * @param offerPercent order offerPercent
     * @param offer order offer
     * @param tax order tax
     * @param deliveryCharge order deliveryCharge
     * @param total order total
     * @param address order address
     * @param status order status
     */
    public OrdersModel(Date date, String subtotal, String offerPercent, String offer, String tax, String deliveryCharge, String total, String address, String status) {
        this.date = date;
        this.subtotal = subtotal;
        this.offerPercent = offerPercent;
        this.offer = offer;
        this.tax = tax;
        this.deliveryCharge = deliveryCharge;
        this.total = total;
        this.address = address;
        this.status = status;
    }

    /**
     * Getter method to get date of an order
     * @return date of  an order
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter method to set the date for an order
     * @param date the date to be set to an order.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter method to get subtotal of an order
     * @return subtotal of  an order
     */
    public String getSubtotal() {
        return subtotal;
    }

    /**
     * Setter method to set the subtotal for an order
     * @param subtotal the subtotal to be set to an order.
     */
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Getter method to get offerPercent of an order
     * @return offerPercent of  an order
     */
    public String getOfferPercent() {
        return offerPercent;
    }

    /**
     * Setter method to set the offerPercent for an order
     * @param offerPercent the offerPercent to be set to an order.
     */
    public void setOfferPercent(String offerPercent) {
        this.offerPercent = offerPercent;
    }

    /**
     * Getter method to get offer of an order
     * @return offer of  an order
     */
    public String getOffer() {
        return offer;
    }

    /**
     * Setter method to set the offer for an order
     * @param offer the offer to be set to an order.
     */
    public void setOffers(String offer) {
        this.offer = offer;
    }

    /**
     * Getter method to get tax of an order
     * @return tax of an order
     */
    public String getTax() {
        return tax;
    }

    /**
     * Setter method to set the tax for an order
     * @param tax the tax to be set to an order.
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    /**
     * Getter method to get deliveryCharge of an order
     * @return deliveryCharge of an order
     */
    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    /**
     * Setter method to set the deliveryCharge for an order
     * @param deliveryCharge the deliveryCharge to be set to an order.
     */
    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    /**
     * Getter method to get total of an order
     * @return total of an order
     */
    public String getTotal() {
        return total;
    }

    /**
     * Setter method to set the total for an order
     * @param total the total to be set to an order.
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * Getter method to get address of an order
     * @return address of an order
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter method to set the address for an order
     * @param address the address to be set to an order.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter method to get status of an order
     * @return status of an order
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter method to set the status for an order
     * @param status the status to be set to an order.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}