package com.example.stockitup.models;

/**
 * Model file for Category Items
 */
public class CategoryItemsModel {
    private String name;
    private String image;
    private String desc;
    private String price;
    private String quantity;

    /**
     * Non - parameterized constructor
     */
    public CategoryItemsModel() {
    }

    /**
     * Constructor to initialize name, image, description, price, quantity of a category item
     * @param name category name
     * @param image category image
     * @param desc category description
     * @param price category price
     * @param quantity category quantity
     */
    public CategoryItemsModel(String name, String image, String desc, String price, String quantity) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Getter method to get name of a category item
     * @return Name of category item
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name for a category item
     * @param name Name to be set to a category item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get image of a category item
     * @return Image of category item
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter method to set the image for a category item
     * @param image Image to be set to a category item.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Getter method to get description of a category item
     * @return description of category item
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Setter method to set the description for a category item
     * @param desc the description to be set to a category item.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Getter method to get price of a category item
     * @return price of category item
     */
    public String getPrice() {
        return price;
    }

    /**
     * Setter method to set the price for a category item
     * @param price the price to be set to a category item.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Getter method to get quantity of a category item
     * @return quantity of category item
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Setter method to set the quantity for a category item
     * @param quantity the quantity to be set to a category item.
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}