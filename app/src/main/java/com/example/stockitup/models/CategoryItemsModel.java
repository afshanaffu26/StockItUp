package com.example.stockitup.models;

public class CategoryItemsModel {
    private String name;
    private String image;
    private String desc;
    private String price;
    private String quantity;

    public CategoryItemsModel() {
    }

    public CategoryItemsModel(String name, String image, String desc, String price, String quantity) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.price = price;
        this.quantity = quantity;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}