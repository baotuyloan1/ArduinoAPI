package com.example.arduinoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static String url = "https://callyourfb.000webhostapp.com/docapi.php?fbclid=IwAR1t-k4L4-SQXr8kwLd7ps6hBdfCquGYSJKgdBd3TNhl8KPzE0OFdzAL9I8";
    public static String urlModeQuat = "https://callyourfb.000webhostapp.com/tah.php?chedo=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Danhsach");
                    JSONObject jsonObject = null;
                    ArrayList<Double> mangNhietDo = new ArrayList<>();
                    ArrayList<Double> mangDoAm = new ArrayList<>();
                    for (int a = 0; a < jsonArray.length(); a++) {
                        jsonObject = jsonArray.getJSONObject(a);
                        mangNhietDo.add(jsonObject.getDouble("nhietdo"));
                        mangDoAm.add(jsonObject.getDouble("doam"));
                    }

                    graphDoAm(mangDoAm);
                    graphNhietDo(mangNhietDo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);

        Switch s = (Switch) findViewById(R.id.switchQuat);
        RequestQueue requestQueueQuat = Volley.newRequestQueue(getApplicationContext());
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              suKienQuat(b);
            }
        });
    }

    private void graphDoAm(ArrayList<Double> mangDoAm) {
        GraphView graphView = (GraphView) findViewById(R.id.graphDoAm);
        DataPoint[] dataPoints1 = new DataPoint[mangDoAm.size()];
        for (int i = 0; i < mangDoAm.size(); i++) {
            dataPoints1[i] = new DataPoint(i, mangDoAm.get(i));
        }
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dataPoints1);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        ;
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(50);
        graphView.getViewport().setMaxY(100);

        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.addSeries(series2);


    }


    private void graphNhietDo(ArrayList<Double> mangNhietDo) {
        GraphView graphView = (GraphView) findViewById(R.id.graphNhietDo);
        DataPoint[] dataPoints = new DataPoint[mangNhietDo.size()];


        for (int i = 0; i < mangNhietDo.size(); i++) {
            dataPoints[i] = new DataPoint(i, mangNhietDo.get(i));
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        ;
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(20);
        graphView.getViewport().setMaxY(50);

        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.addSeries(series);

    }

    private void suKienQuat(boolean b) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongDan = null;
        if (b) {
            duongDan = urlModeQuat + "on";
        } else {
            duongDan = urlModeQuat + "off";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongDan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;

        requestQueue.add(stringRequest);

//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> param = new HashMap<String, String>();
//                param.put("id","0");
//                return super.getParams();
//            }
//        };
    }
}
