package com.example.arduinoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    public static String url = "https://callyourfb.000webhostapp.com/docapi.php";
    public static String urlModeQuat = "https://callyourfb.000webhostapp.com/tah.php?";
    private Button btnOn, btnOff;
    private int trangThai = 0;
    TextView textView;
    private int cheDoCu;
    private boolean flagSwitch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String duongDan = urlModeQuat + "trangthai=on";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("Oncreate", duongDan);
        callApi(requestQueue, duongDan);
        Switch s = (Switch) findViewById(R.id.switchTuDong);
        RequestQueue requestQueueQuat = Volley.newRequestQueue(getApplicationContext());
        btnOn = (Button) findViewById(R.id.buttonOn);
        btnOff = (Button) findViewById(R.id.buttonOff);
        textView = (TextView) findViewById(R.id.textViewTrangThai);
        btnOn.setVisibility(View.INVISIBLE);
        btnOff.setVisibility(View.INVISIBLE);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                suKienQuat(b);
                Log.d("b", b + "");
                if (b) {
                    btnOn.setVisibility(View.INVISIBLE);
                    btnOff.setVisibility(View.INVISIBLE);
                } else {
                }

            }
        });
        Thread thread = new Thread(new RunnableDemo(this));
        thread.start();
    }

    public void getDuLieu(final ArrayList<Double> mangNhietDo, final ArrayList<Double> mangDoAm) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Danhsach");
                    JSONObject jsonObject = null;
                    ArrayList<Double> mangNhietDo = new ArrayList<>();
                    int ptCuoiCung = 0;
                    for (int a = 0; a < jsonArray.length(); a++) {
                        jsonObject = jsonArray.getJSONObject(a);
                        mangNhietDo.add(jsonObject.getDouble("nhietdo"));
                        mangDoAm.add(jsonObject.getDouble("doam"));
                        if (a == 19) {
                            trangThai = jsonObject.getInt("chedo");
                            Log.d("trangthai", trangThai + "");
                            if (trangThai == 0) {
                                textView.setText("off");
                                if (flagSwitch == false) {
                                    if (cheDoCu == 1) {
                                        btnOn.setVisibility(View.VISIBLE);
                                        btnOff.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                            if (trangThai == 1) {
                                textView.setText("on");
                                if (flagSwitch == false) {
                                    if (cheDoCu == 0) {
                                        textView.setText("on");
                                        btnOn.setVisibility(View.INVISIBLE);
                                        btnOff.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            setSuKien(trangThai, mangNhietDo.get(a), mangDoAm.get(a));
                        }
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
    }

    private void graphDoAm(ArrayList<Double> mangDoAm) {
        GraphView graphView = (GraphView) findViewById(R.id.graphDoAm);
        graphView.removeAllSeries();
        DataPoint[] dataPoints1 = new DataPoint[mangDoAm.size()];
        for (int i = 0; i < mangDoAm.size(); i++) {
            dataPoints1[i] = new DataPoint(i, mangDoAm.get(i));
        }
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dataPoints1);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);
        graphView.getViewport().setScrollable(true);
        graphView.addSeries(series2);
    }

    public void graphNhietDo(ArrayList<Double> mangNhietDo) {
        GraphView graphView = (GraphView) findViewById(R.id.graphNhietDo);
        graphView.removeAllSeries();
        DataPoint[] dataPoints = new DataPoint[mangNhietDo.size()];
        for (int i = 0; i < mangNhietDo.size(); i++) {
            dataPoints[i] = new DataPoint(i, mangNhietDo.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(20);
        graphView.getViewport().setMaxY(50);
        graphView.getViewport().setScrollable(true);
        graphView.addSeries(series);
    }

    private void setSuKien(int trangthai, double nhietdo, double doam) {
        TextView textViewNhietDo = (TextView) findViewById(R.id.textViewNhietDo);
        TextView textViewDoAm = (TextView) findViewById(R.id.textViewDoAm);
        textViewNhietDo.setText(nhietdo + "");
        textViewDoAm.setText(doam + "");
    }

    private void suKienQuat(boolean b) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String[] duongDan = {null};
        duongDan[0] = urlModeQuat + "trangthai=on";
        flagSwitch = b;
        if (b) {
            duongDan[0] = urlModeQuat + "trangthai=on";
            callApi(requestQueue, duongDan[0]);

        } else {
            duongDan[0] = urlModeQuat + "trangthai=off&chedo=off";
            callApi(requestQueue, duongDan[0]);
            Log.d("switch false", duongDan[0].toString());
            btnOff.setVisibility(View.INVISIBLE);
            btnOn.setVisibility(View.INVISIBLE);
            cheDoCu = 1;
            btnOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    duongDan[0] = urlModeQuat + "trangthai=off&chedo=on";
                    cheDoCu = 0;
                    btnOn.setVisibility(View.INVISIBLE);
                    Log.d("btn ON", duongDan[0].toString());
                    callApi(requestQueue, duongDan[0]);

                }
            });
            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cheDoCu = 1;
                    btnOff.setVisibility(View.INVISIBLE);
                    duongDan[0] = urlModeQuat + "trangthai=off&chedo=off";
                    Log.d("btn off", duongDan[0].toString());
                    callApi(requestQueue, duongDan[0]);
                }
            });
            callApi(requestQueue, duongDan[0]);
        }
    }
    protected void callApi(RequestQueue requestQueue, String duongDan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongDan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("id", "0");
                return super.getParams();
            }
        };
        requestQueue.add(stringRequest);
    }
}
