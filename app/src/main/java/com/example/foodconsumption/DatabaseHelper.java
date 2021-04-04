package com.example.foodconsumption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.foodconsumption.ui.foodBase.FoodBaseViewModel;
import com.example.foodconsumption.ui.home.HomeViewModel;
import com.example.foodconsumption.ui.profile.ProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String MEAL_TABLE = "MEAL_TABLE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_FOOD_NAME = "FOOD_NAME";
    public static final String COLUMN_QUANTITY = "QUANTITY";
    public static final String COLUMN_INGREDIENTS = "INGREDIENTS";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_MEAL_TYPE = "MEAL_TYPE";
    private static final String COLUMN_EMAIL = "COLUMN_EMAIL";
    private static final String COLUMN_USER_NAME = "COLUMN_USER_NAME";
    private static final String COLUMN_DOB = "COLUMN_DOB";
    private static final String COLUMN_ADDRESS = "COLUMN_ADDRESS";
    private static final String COLUMN_IMAGE = "COLUMN_IMAGE";
    private static final String COLUMN_SEX = "COLUMN_SEX";
    private static final String COLUMN_PHONE_NUMBER = "COLUMN_PHONE_NUMBER";
    private static final String COLUMN_DATETIME = "COLUMN_DATETIME";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "food.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createMealTableStatement = "CREATE TABLE " + MEAL_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD_NAME + " TEXT, " + COLUMN_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + COLUMN_QUANTITY + " TEXT, " + COLUMN_INGREDIENTS + " TEXT, " + COLUMN_CALORIES + " TEXT, " + COLUMN_MEAL_TYPE + " TEXT)";
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_DOB + " TEXT, " + COLUMN_SEX + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_ADDRESS + " TEXT, " + COLUMN_PHONE_NUMBER + " TEXT)";
        String createFoodTableStatement = "CREATE TABLE " + FOOD_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD_NAME + " TEXT, " + COLUMN_INGREDIENTS + " TEXT)";
        sqLiteDatabase.execSQL(createMealTableStatement);
        sqLiteDatabase.execSQL(createUserTableStatement);
        sqLiteDatabase.execSQL(createFoodTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean deleteOne(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + MEAL_TABLE + " WHERE ID" + " = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteOneFood(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FOOD_TABLE + " WHERE ID" + " = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + MEAL_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteAllFoodItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FOOD_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean addOne(HomeViewModel homeViewModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COLUMN_FOOD_NAME, homeViewModel.getFood_name());
        cv.put(COLUMN_QUANTITY, homeViewModel.getQuantity());
        cv.put(COLUMN_INGREDIENTS, homeViewModel.getIngredients());
        cv.put(COLUMN_CALORIES, homeViewModel.getCalories());
        cv.put(COLUMN_MEAL_TYPE, homeViewModel.getMeal_type());
        cv.put(COLUMN_DATETIME, homeViewModel.getDateTime());

        long insert = db.insert(MEAL_TABLE,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addOneFood(FoodBaseViewModel scanViewModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COLUMN_FOOD_NAME, scanViewModel.getFood_name());
        cv.put(COLUMN_INGREDIENTS, scanViewModel.getIngredients());

        long insert = db.insert(FOOD_TABLE,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateOne(HomeViewModel homeViewModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COLUMN_FOOD_NAME, homeViewModel.getFood_name());
        cv.put(COLUMN_QUANTITY, homeViewModel.getQuantity());
        cv.put(COLUMN_INGREDIENTS, homeViewModel.getIngredients());
        cv.put(COLUMN_CALORIES, homeViewModel.getCalories());
        cv.put(COLUMN_MEAL_TYPE, homeViewModel.getMeal_type());
//        String queryString = " WHERE ID" + " = " + homeViewModel.getRecord_id();
        String queryString = String.format("%s = ?", "ID");
        String[] whereArgs = new String[]{String.valueOf(homeViewModel.getRecord_id())};

        long update = db.update(MEAL_TABLE, cv, queryString, whereArgs);

        if(update == -1){
            return false;
        }else{
            return true;
        }
    }

    public List<HomeViewModel> getAllRecords(){
        List<HomeViewModel> returnlist = new ArrayList<>();

        String queryString = "SELECT * FROM " + MEAL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                String dateTime = cursor.getString(2);
                String quantity = cursor.getString(3);
                String ingredients = cursor.getString(4);
                String calories = cursor.getString(5);
                String mealType = cursor.getString(6);

                HomeViewModel newhomeviewmodel = new HomeViewModel(foodID,foodName,dateTime,quantity,ingredients,calories,mealType);
                returnlist.add(newhomeviewmodel);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return returnlist;
    }

    public List<FoodBaseViewModel> getAllFoods(){
        List<FoodBaseViewModel> returnlist = new ArrayList<>();

        String queryString = "SELECT * FROM " + FOOD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                String ingredients = cursor.getString(2);

                FoodBaseViewModel newfoodBaseViewModel = new FoodBaseViewModel(foodID,foodName,ingredients);
                returnlist.add(newfoodBaseViewModel);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return returnlist;
    }

    public FoodBaseViewModel getOneFood(long id){
        FoodBaseViewModel oneRecord = new FoodBaseViewModel();

        String queryString = "SELECT * FROM " + FOOD_TABLE + " WHERE ID" + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                String ingredients = cursor.getString(2);

                oneRecord= new FoodBaseViewModel(foodID,foodName,ingredients);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return oneRecord;
    }

    public HomeViewModel getOneRecord(long id){
        HomeViewModel oneRecord = new HomeViewModel();

        String queryString = "SELECT * FROM " + MEAL_TABLE + " WHERE ID" + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                String dateTime = cursor.getString(2);
                String quantity = cursor.getString(3);
                String ingredients = cursor.getString(4);
                String calories = cursor.getString(5);
                String mealType = cursor.getString(6);

                oneRecord= new HomeViewModel(foodID,foodName,dateTime,quantity,ingredients,calories,mealType);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return oneRecord;
    }

    public boolean checkUserExistence(ProfileViewModel profileViewModel){
            String queryString = "SELECT * FROM " + USER_TABLE + " WHERE COLUMN_EMAIL =\'" + profileViewModel.getEmail().toString() +"\'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(!cursor.moveToFirst()){
            ContentValues cv  = new ContentValues();
            cv.put(COLUMN_EMAIL, profileViewModel.getEmail());
            cv.put(COLUMN_USER_NAME, profileViewModel.getUssername());
            cv.put(COLUMN_PHONE_NUMBER, profileViewModel.getPhone_no());
            cv.put(COLUMN_IMAGE, profileViewModel.getImage());
            cv.put(COLUMN_ADDRESS, "N/A");
            cv.put(COLUMN_DOB, "N/A");
            cv.put(COLUMN_SEX, "N/A");

            long insert = db.insert(USER_TABLE,null,cv);

            if(insert == -1){
                return false;
            }else{
                return true;
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean updateUserDetails(ProfileViewModel homeViewModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COLUMN_ADDRESS, homeViewModel.getAddress());
        cv.put(COLUMN_DOB, homeViewModel.getDob());
        cv.put(COLUMN_SEX, homeViewModel.getSex());
        cv.put(COLUMN_PHONE_NUMBER, homeViewModel.getPhone_no());
        String queryString = String.format("%s = ?", "COLUMN_EMAIL");
        String[] whereArgs = new String[]{String.valueOf(homeViewModel.getEmail())};

        long update = db.update(USER_TABLE, cv, queryString, whereArgs);

        if(update == -1){
            return false;
        }else{
            return true;
        }
    }

    public ProfileViewModel loadLoggedInUserData(String email){
        ProfileViewModel oneRecord = new ProfileViewModel();

        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE COLUMN_EMAIL =\'" + email +"\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int userID = cursor.getInt(0);
                String userEmail = cursor.getString(1);
                String username = cursor.getString(2);
                String user_dob = cursor.getString(3);
                String user_sex = cursor.getString(4);
                String user_image = cursor.getString(5);
                String user_address = cursor.getString(6);
                String user_phone = cursor.getString(7);

                oneRecord= new ProfileViewModel(userEmail,username,user_dob,user_phone,user_image,user_address,user_sex);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return oneRecord;
    }


    public List<HomeViewModel> getPastMonthData(){
        List<HomeViewModel> returnlist = new ArrayList<>();
        Date today = new Date();
        Date newDate = new Date(today.getTime() - 604800000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(newDate);
        String queryString = "SELECT * FROM " + MEAL_TABLE + " WHERE COLUMN_DATETIME > '" + strDate +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                String dateTime = cursor.getString(2);
                String quantity = cursor.getString(3);
                String ingredients = cursor.getString(4);
                String calories = cursor.getString(5);
                String mealType = cursor.getString(6);

                HomeViewModel newhomeviewmodel = new HomeViewModel(foodID,foodName,dateTime, quantity,ingredients,calories,mealType);
                returnlist.add(newhomeviewmodel);
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return returnlist;
    }

}
