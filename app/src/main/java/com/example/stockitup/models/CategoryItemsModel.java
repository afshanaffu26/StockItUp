package com.example.stockitup.models;

public class CategoryItemsModel {
    String name;
    String image;

    public CategoryItemsModel() {
    }

    public CategoryItemsModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
