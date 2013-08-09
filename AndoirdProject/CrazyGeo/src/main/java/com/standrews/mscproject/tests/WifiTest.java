/*
 * WifiTest.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.standrews.mscproject.main.R;
import com.standrews.mscproject.utils.NetworkChecker;

/**
 * Created by Ziji on 13-5-29.
 */
public class WifiTest extends Activity {

    private Button check;
    private TextView result;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_test);
        check = (Button) findViewById(R.id.buttonwificheck);
        result = (TextView) findViewById(R.id.wifiResult);


        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NetworkChecker nc = new NetworkChecker(WifiTest.this);
                if (nc.isWifiReachable()) {
                    result.setText("true");
                } else {
                    result.setText("false");
                }
            }
        });


    }

}