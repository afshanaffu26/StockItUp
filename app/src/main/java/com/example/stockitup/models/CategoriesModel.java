package com.example.stockitup.models;

    public class CategoriesModel {
        String name;
        String image;

        public CategoriesModel() {
        }

        /**
         * Constructor to initialize name, image of product
         * @param name category name
         * @param image category image
         */
        public CategoriesModel(String name, String image) {
            this.name = name;
            this.image = image;
        }

        /**
         * Getter method to get name of category
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
         * Getter method to get image of category
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
