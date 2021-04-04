package com.example.foodconsumption.ui.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;
import com.example.foodconsumption.ui.home.HomeViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BarChart barChart = findViewById(R.id.barChart);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<HomeViewModel> pastMonthData = (ArrayList<HomeViewModel>) databaseHelper.getPastMonthData();
        ArrayList<BarEntry> calories = new ArrayList<>();
        Iterator iter = pastMonthData.iterator();
        while (iter.hasNext()) {
            HomeViewModel current = (HomeViewModel) iter.next();
            calories.add(new BarEntry(Float.parseFloat(current.getQuantity()), Float.parseFloat(current.getCalories())));
        }


        BarDataSet barDataSet = new BarDataSet(calories, "Calories");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("calories against the food quantities");
        barChart.animateY(2000);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}