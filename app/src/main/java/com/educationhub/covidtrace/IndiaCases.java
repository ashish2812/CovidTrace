package com.educationhub.covidtrace;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndiaCases extends AppCompatActivity {
    TextView coronaCountTv, deathCountTv, recoveredCountTv;
    AnyChartView anyChartView;
    ImageView refreshIcon1;
    Cartesian cartesian;
    SimpleArcLoader loader,loader2;
    RelativeLayout relativeLayoutIntro,relativeLayoutAnyChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_cases);
        relativeLayoutIntro = findViewById(R.id.relativeLayoutIntro);
        anyChartView = findViewById(R.id.anyCharView);
        coronaCountTv = findViewById(R.id.coronaCasesTvCount);
        deathCountTv = findViewById(R.id.deathsTvCount);
        recoveredCountTv = findViewById(R.id.recoveredTvCount);
        refreshIcon1 = findViewById(R.id.refreshIcon1);
        relativeLayoutAnyChart = findViewById(R.id.relativeLayoutAnyChart);
        loader = findViewById(R.id.loader);
        loader2 = findViewById(R.id.loader2);

        cartesian = AnyChart.line();
        cartesian.background("#424242");
        refreshIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coronaCountTv.setText("0");
                deathCountTv.setText("0");
                recoveredCountTv.setText("0");
                cartesian.background("#424242");
                fetchData();
            }
        });

        fetchData();

        coronaFetchDataPerDay();

    }

    private void coronaFetchDataPerDay() {

        String url1 = "https://api.covid19india.org/data.json";
        loader2.start();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cases_time_series");


                            cartesian.animation(true);


                            cartesian.padding(10d, 20d, 5d, 20d);

                            cartesian.crosshair().enabled(true);
                            cartesian.crosshair()
                                    .yLabel(true)
                                    // TODO ystroke
                                    .yStroke((Stroke) null, null, null, (String) null, (String) null);

                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                            cartesian.title("Corona Virus perday");

                            List<DataEntry> seriesData = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject coronaCasesObject = jsonArray.getJSONObject(i);
                                seriesData.add(new CustomDataEntry(coronaCasesObject.getString("date"), Integer.parseInt(coronaCasesObject.getString("dailyconfirmed")),
                                        Integer.parseInt(coronaCasesObject.getString("dailyrecovered")), Integer.parseInt(coronaCasesObject.getString("dailydeceased"))));
                            }


                            Set set = Set.instantiate();
                            set.data(seriesData);

                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                            Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                            Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

                            Line series1 = cartesian.line(series1Mapping).colorScale("#424242");

                            series1.name("Total Cases");

                            series1.hovered().markers().enabled(true);
                            series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series1.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);


                            Line series2 = cartesian.line(series2Mapping);
                            series2.name("Recovered");
                            series2.hovered().markers().enabled(true);
                            series2.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series2.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            Line series3 = cartesian.line(series3Mapping);
                            series3.name("Deaths");
                            series3.hovered().markers().enabled(true);
                            series3.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series3.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(13d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView.setChart(cartesian);
                            loader2.stop();
                            loader2.setVisibility(View.GONE);
                            relativeLayoutAnyChart.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            loader2.stop();
                            loader2.setVisibility(View.GONE);
                            relativeLayoutAnyChart.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader2.stop();
                loader2.setVisibility(View.GONE);
                relativeLayoutAnyChart.setVisibility(View.VISIBLE);
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/countries/India/";
        loader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            coronaCountTv.setText(jsonObject.getString("cases"));
                            deathCountTv.setText(jsonObject.getString("deaths"));
                            recoveredCountTv.setText(jsonObject.getString("recovered"));
                            loader.stop();
                            loader.setVisibility(View.GONE);
                            relativeLayoutIntro.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loader.stop();
                            loader.setVisibility(View.GONE);
                            relativeLayoutIntro.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.stop();
                loader.setVisibility(View.GONE);
                relativeLayoutIntro.setVisibility(View.VISIBLE);
                Toast.makeText(IndiaCases.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

}

