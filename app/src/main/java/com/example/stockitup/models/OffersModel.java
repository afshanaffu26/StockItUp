package com.example.stockitup.models;

/**
 * Model file for Offers
 */
public class OffersModel {
    private String name;
    private String value;

    /**
     * Non - parameterized constructor
     */
    public OffersModel() {
    }

    /**
     * Constructor to initialize name, value of offers
     * @param name offers name
     * @param value offers value
     */
    public OffersModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Getter method to get name of offer
     * @return name of offer
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name for a offer
     * @param name the name to be set to a offer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get value of offer
     * @return value of offer
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter method to set the value for a offer
     * @param value the value to be set to a offer.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
