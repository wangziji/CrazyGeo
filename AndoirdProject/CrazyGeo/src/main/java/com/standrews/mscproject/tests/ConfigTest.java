/*
 * ConfigTest.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.standrews.mscproject.main.R;
import com.standrews.mscproject.utils.Configuration;

public class ConfigTest extends Activity {

    private Button read;
    private TextView ip, port, dir;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_test);
        read = (Button) findViewById(R.id.configread);
        ip = (TextView) findViewById(R.id.textip);
        port = (TextView) findViewById(R.id.textport);
        dir = (TextView) findViewById(R.id.textdir);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration configuration = new Configuration();
                String sip = configuration.getConfigProperties(ConfigTest.this).getProperty("PLAYED");
                String sport = configuration.getConfigProperties(ConfigTest.this).getProperty("SERV_PORT");
                String sdir = configuration.getConfigProperties(ConfigTest.this).getProperty("DPI");
                ip.setText(sip);
                port.setText(sport);
                dir.setText(sdir);
                Log.i("main", "id:" + configuration.getConfigProperties(ConfigTest.this).getProperty("ID"));
                Log.i("main", "model:" + configuration.getConfigProperties(ConfigTest.this).getProperty("MODEL"));
                Log.i("main", "memory:" + configuration.getConfigProperties(ConfigTest.this).getProperty("MEMORY"));
                Log.i("main", "memory:" + configuration.getConfigProperties(ConfigTest.this).getProperty("DPI"));
            }
        });
    }
}