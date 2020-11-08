package com.example.stockitup.models;

/**
 * Model file for Categories
 */
public class CategoriesModel {
    private String name;
    private String image;

    /**
     * Non - parameterized constructor
     */
    public CategoriesModel() {
    }

    /**
     * Constructor to initialize name, image of a category
     * @param name category name
     * @param image category image
     */
    public CategoriesModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    /**
     * Getter method to get name of a category
     * @return Name of category
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name for a category
     * @param name Name to be set to a category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get image of a category
     * @return Image of category
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter method to set the image for a category
     * @param image Image to be set to a category .
     */
    public void setImage(String image) {
        this.image = image;
    }
}
