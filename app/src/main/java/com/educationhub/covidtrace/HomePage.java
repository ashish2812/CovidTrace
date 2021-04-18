package com.educationhub.covidtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {
    TextView tvCases,tvDeath,tvCritical,tvAffectedCountries,tvRecovered,tvTodayDeath,tvActiveCases;
            SimpleArcLoader loader;
            ScrollView scrollView;
            PieChart mPieChart;
            SwitchCompat switchCompat;
            BarChart mBarChart;
            LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_home_page);


        mPieChart =  findViewById(R.id.piechart);
        linearLayout =  findViewById(R.id.linearLayoutCases);
        mBarChart = findViewById(R.id.barchart);
        switchCompat = findViewById(R.id.switchCompact);
        tvCases = findViewById(R.id.tvCases);
        tvDeath = findViewById(R.id.tvDeath);
        tvTodayDeath = findViewById(R.id.tvTodayDeath);
        tvCritical = findViewById(R.id.tvCritical);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);
        tvActiveCases = findViewById(R.id.tvActiveCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        scrollView = findViewById(R.id.scrollStats);
        loader=findViewById(R.id.loader);
        fetchData();

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){

                    Log.d("switchCompat","is Checked");
                    mBarChart.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    mBarChart.startAnimation();
                }
                else{
                    Log.d("switchCompat","is unChecked");
                    linearLayout.setVisibility(View.VISIBLE);
                    mBarChart.setVisibility(View.GONE);
                    mPieChart.startAnimation();
                }


            }
        });

    }

   private void fetchData() {
         String url ="https://disease.sh/v3/covid-19/all/";
         loader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            tvCases.setText(jsonObject.getString("cases"));
                            tvActiveCases.setText(jsonObject.getString("active"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvTodayDeath.setText(jsonObject.getString("todayDeaths"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));
                            tvDeath.setText(jsonObject.getString("deaths"));

                            mPieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#EF5350")));
                            mPieChart.addPieSlice(new PieModel("Sleep", Integer.parseInt(tvDeath.getText().toString()), Color.parseColor("#FFF59D")));
                            mPieChart.addPieSlice(new PieModel("Active Cases", Integer.parseInt(tvActiveCases.getText().toString()), Color.parseColor("#FFC400")));
                            mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#FFFFFF")));



                            mBarChart.addBar(new BarModel(Float.parseFloat(tvCases.getText().toString()),0xFF63CBB0));
                            mBarChart.addBar(new BarModel(Float.parseFloat(tvRecovered.getText().toString()),  R.color.recovered));
                            mBarChart.addBar(new BarModel(Float.parseFloat(tvActiveCases.getText().toString()),  R.color.active_cases));
                            mBarChart.addBar(new BarModel(Float.parseFloat(tvDeath.getText().toString()),  R.color.death));


                            mBarChart.startAnimation();

                            mPieChart.startAnimation();
                            loader.stop();
                            loader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            loader.stop();
                            loader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.stop();
                loader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                Toast.makeText(HomePage.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void refreshButton(View view) {

        mPieChart.clearChart();
        mBarChart.clearChart();
        fetchData();
    }


    public void indiaCases(View view) {
      startActivity(new Intent(getApplicationContext(),IndiaCases.class));
    }



}