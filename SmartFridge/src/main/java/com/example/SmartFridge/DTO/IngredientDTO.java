package com.example.SmartFridge.DTO;

public class IngredientDTO {
    private String food;
    private String measure;
    String grams;
    Integer calories;
    String protein;
    String fat;
    String fiber;
    String carbs;
    String category;

    public IngredientDTO(String food, String measure, String grams, Integer calories, String protein, String fat, String fiber, String carbs, String category) {
        this.food = food;
        this.measure = measure;
        this.grams = grams;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.carbs = carbs;
        this.category = category;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
