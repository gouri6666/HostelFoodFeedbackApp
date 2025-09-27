package com.example.hostelfoodfeedbacapp;

public class FoodItem {
    private String name;
    private float rating;
    private String imageUrl;

    // No-argument constructor required for Firebase deserialization
    public FoodItem() {}

    public FoodItem(String name, float rating, String imageUrl) {
        this.name = name;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
