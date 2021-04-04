package com.example.foodconsumption.ui.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;
import com.example.foodconsumption.ui.home.HomeViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> calories = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<HomeViewModel> pastMonthData = (ArrayList<HomeViewModel>) databaseHelper.getPastMonthData();
        Iterator iter = pastMonthData.iterator();
        int count = 0;
        while (iter.hasNext()) {
            HomeViewModel current = (HomeViewModel) iter.next();
            calories.add(new PieEntry(Float.parseFloat(current.getCalories()), current.getFood_name()));
            count++;
        }

        PieDataSet pieDataSet = new PieDataSet(calories, "Calories");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Calories of each food");
        pieChart.animate();
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