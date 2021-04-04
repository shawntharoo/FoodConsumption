package com.example.foodconsumption.ui.foodBase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class FoodBaseViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    final MutableLiveData<List<String>> list = new MutableLiveData<>();
    private String food_name;
    private String ingredients;

    private int id;

    public FoodBaseViewModel(int id, String food_name, String ingredients) {
        this.id = id;
        this.food_name = food_name;
        this.ingredients = ingredients;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getRecord_id() {
        return id;
    }

    public void setRecord_id(int id) {
        this.id = id;
    }

    public FoodBaseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("New");
    }

    public LiveData<String> getText() {
        return mText;
    }
}