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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(String offerPercent) {
        this.offerPercent = offerPercent;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffers(String offers) {
        this.offer = offer;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}