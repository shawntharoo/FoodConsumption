package com.example.foodconsumption.ui.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;
import com.example.foodconsumption.ui.home.HomeViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RadarChart radarChart = findViewById(R.id.radarChart);
        String[] labels = new String[30];
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<HomeViewModel> pastMonthData = (ArrayList<HomeViewModel>) databaseHelper.getPastMonthData();
        ArrayList<RadarEntry> calories = new ArrayList<>();
        Iterator iter = pastMonthData.iterator();
        int count = 0;
        while (iter.hasNext()) {
            HomeViewModel current = (HomeViewModel) iter.next();
            System.out.println(current.getQuantity());
            labels[count] = current.getFood_name();
            calories.add(new RadarEntry(Float.parseFloat(current.getQuantity())));
            count++;
        }


//        calories.add(new RadarEntry(2015));

        RadarDataSet radarDataSet = new RadarDataSet(calories, "Food quantity");
        radarDataSet.setColors(Color.RED);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(16f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);



        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter((labels)));

        radarChart.setData(radarData);
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