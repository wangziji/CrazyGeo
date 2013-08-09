
/*
 * MainActivity.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.standrews.mscproject.tests.Test;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DeviceInformation;

import java.util.Properties;

public class MainActivity extends Activity {


    private Button button_test;
    private Configuration mConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_test = (Button) findViewById(R.id.buttontest);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Test.class);
                startActivity(intent);
            }
        });
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
        initialize();

    }

    private void initialize() {
        mConfiguration = new Configuration();
        Properties config = mConfiguration.getConfigProperties(MainActivity.this);
        int played = Integer.parseInt(config.getProperty("PLAYED"));
        if (played == 0) {
            Log.i("main", "first play!");
            DeviceInformation di = new DeviceInformation();
            config.setProperty("ID", di.getID(MainActivity.this));
            Point size = di.getDisplaySize(this);
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            config.setProperty("DISPLAY_WIDTH", size.x + "");
            config.setProperty("DISPLAY_HEIGHT", size.y + "");
            config.setProperty("DPI", metric.densityDpi + "");
            config.setProperty("MANUFACTURER", Build.MANUFACTURER);
            config.setProperty("MODEL", Build.MODEL);
            config.setProperty("OS_VERSION", Build.VERSION.RELEASE);
            config.setProperty("MEMORY", di.getMemorySize(this));
            config.setProperty("ASIA_BEST", 0+"");
            config.setProperty("EUROPE_BEST", 0+"");
            config.setProperty("AMERICA_BEST", 0+"");
            config.setProperty("AFRICA_BEST", 0+"");
            config.setProperty("HANDEDNESS", 1+"");
            config.setProperty("AUTO_DETECT", 1+"");
        }
        played++;
        config.setProperty("PLAYED", played + "");
        mConfiguration.saveConfigProperties(MainActivity.this, config);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
