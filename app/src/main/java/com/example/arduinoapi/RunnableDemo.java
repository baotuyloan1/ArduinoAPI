package com.example.arduinoapi;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class RunnableDemo implements Runnable {

    private Thread t;
    private String threadName;
    private LineGraphSeries<DataPoint> series;
    private GraphView graphNhietDo;
    private MainActivity mainActivity;
    RunnableDemo(String name) {
        threadName = name;
    }

    public RunnableDemo(MainActivity mainActivity ) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ArrayList<Double> mangNhietDo = new ArrayList<Double>();
                ArrayList<Double> mangDoAm = new ArrayList<Double>();

                mainActivity.getDuLieu(mangNhietDo,mangDoAm);

                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
