package com.example.foodconsumption.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    final MutableLiveData<List<String>> list = new MutableLiveData<>();
    private String food_name;
    private String dateTime;
    private String quantity;
    private String ingredients;
    private String calories;
    private String meal_type;

    private int id;

    public HomeViewModel(int id, String food_name, String dateTime, String quantity, String ingredients, String calories, String meal_type) {
        this.id = id;
        this.food_name = food_name;
        this.dateTime = dateTime;
        this.quantity = quantity;
        this.ingredients = ingredients;
        this.calories = calories;
        this.meal_type = meal_type;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public int getRecord_id() {
        return id;
    }

    public void setRecord_id(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HomeViewModel{" +
                "food_name='" + food_name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", calories='" + calories + '\'' +
                ", meal_type='" + meal_type + '\'' +
                '}';
    }

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("New");
    }

    public LiveData<String> getText() {
        return mText;
    }
}