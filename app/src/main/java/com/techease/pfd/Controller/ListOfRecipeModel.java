package com.techease.pfd.Controller;

/**
 * Created by Adam Noor on 01-Jan-18.
 */

public class ListOfRecipeModel {

    private  String id;
    private  String RecipeName;
    private  String RecipeCatgory;
    private  String RecipeTime;
    private String RecipeIns;
    private String RecipeIng;
    private String RecipeImage;

    public String getRecipeIns() {
        return RecipeIns;
    }

    public void setRecipeIns(String recipeIns) {
        RecipeIns = recipeIns;
    }

    public String getRecipeIng() {
        return RecipeIng;
    }

    public void setRecipeIng(String recipeIng) {
        RecipeIng = recipeIng;
    }




    public String getRecipeImage() {
        return RecipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        RecipeImage = recipeImage;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getRecipeCatgory() {
        return RecipeCatgory;
    }

    public void setRecipeCatgory(String recipeCatgory) {
        RecipeCatgory = recipeCatgory;
    }

    public String getRecipeTime() {
        return RecipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        RecipeTime = recipeTime;
    }







}
