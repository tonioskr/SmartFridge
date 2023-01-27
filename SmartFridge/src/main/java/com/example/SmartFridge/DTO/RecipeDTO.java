package com.example.SmartFridge.DTO;

import org.neo4j.driver.internal.cluster.MultiDatabasesRoutingProcedureRunner;

public class RecipeDTO {
    private String Name;
    private String Id ;
    private int ReviewCount;

    private String Author;
    private String PreparationTime;
    private String Cooktime;
    private String TotalTime;
    private String Ingredients;
    private String Direction;
    private String[] IngredientsList;
    private ReviewDTO[] reviews;

    public RecipeDTO(String name, String id, int reviewCount, String totalTime) {
        Name = name;
        Id = id;
        ReviewCount = reviewCount;
        TotalTime = totalTime;
    }

    public RecipeDTO(String name, String id, String author,String preptime, String cktime, String tttime,String ingre, String dire){
        Name = name;
        Id = id;
        Direction = dire;
        Author = author;
        Cooktime = cktime;
        TotalTime = tttime;
        PreparationTime = preptime;
        Ingredients = ingre;
    }

    public RecipeDTO(String name, String id, int reviewCount, String author, String preparationTime, String cooktime, String totalTime, String ingrients, String direction, String[] ingredientsList, ReviewDTO[] reviews) {
        Name = name;
        Id = id;
        Direction = direction;
        ReviewCount = reviewCount;
        Author = author;
        PreparationTime = preparationTime;
        Cooktime = cooktime;
        TotalTime = totalTime;
        Ingredients = ingrients;
        IngredientsList = ingredientsList;
        this.reviews = reviews;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getReviewCount() {
        return ReviewCount;
    }

    public void setReviewCount(int reviewCount) {
        ReviewCount = reviewCount;
    }


    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPreparationTime() {
        return PreparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        PreparationTime = preparationTime;
    }

    public String getCooktime() {
        return Cooktime;
    }

    public void setCooktime(String cooktime) {
        Cooktime = cooktime;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getIngrients() {
        return Ingredients;
    }

    public void setIngrients(String ingrients) {
        Ingredients = ingrients;
    }

    public String[] getIngredientsList() {
        return IngredientsList;
    }

    public void setIngredientsList(String[] ingredientsList) {
        IngredientsList = ingredientsList;
    }

    public ReviewDTO[] getReviews() {
        return reviews;
    }

    public void setReviews(ReviewDTO[] reviews) {
        this.reviews = reviews;
    }

    public void setDirection(String direction) {
        this.Direction = direction;
    }

    public String getDirection() {
        return Direction;
    }
}
