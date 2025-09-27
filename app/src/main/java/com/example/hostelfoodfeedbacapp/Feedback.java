package com.example.hostelfoodfeedbacapp;

import java.util.Map;

public class Feedback {
    private String day;
    private String meal;
    private Map<String, Float> ratings;

    public Feedback() {}  // Required for Firebase

    public Feedback(String day, String meal, Map<String, Float> ratings) {
        this.day = day;
        this.meal = meal;
        this.ratings = ratings;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public Map<String, Float> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Float> ratings) {
        this.ratings = ratings;
    }
}
