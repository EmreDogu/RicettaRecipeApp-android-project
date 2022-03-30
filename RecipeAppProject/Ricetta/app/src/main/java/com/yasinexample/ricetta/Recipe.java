package com.yasinexample.ricetta;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

//Emre Doğu
public class Recipe {
    @Exclude
    private String id;
    private String type;
    private String mealname;
    private String link;
    private String recipe;
    private String ingredients;
    private String cookingtime;
    private String calories;
    private String favourite;
    private String basket;
    private Timestamp date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMealName() {
        return mealname;
    }

    public void setMealName(String mealName) {
        this.mealname = mealName;
    }

    public String getLink() { return link; }

    public void setLink(String Link) { this.link = Link; }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCookingTime() {
        return cookingtime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingtime = cookingTime;
    }

    public String getCalories() { return calories; }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getFavouriteState() {
        return favourite;
    }

    public void setFavouriteState(String favourite) { this.favourite = favourite; }

    public String getBasketState() { return basket; }

    public void setBasketState(String basket) { this.basket = basket;}

    public Timestamp getDate() { return date; }

    public void setDate(Timestamp date) { this.date = date; }
}
