/*
 * TCPTest.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.standrews.mscproject.main.R;
import com.standrews.mscproject.tcpconnection.TCPService;

/**
 * Created by ziji on 13-6-8.
 */
public class TCPTest extends Activity implements View.OnClickListener {
    private TCPService mTCPService;
    private Intent mIntent;
    private Button enable, disable;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcptest);
        mIntent = new Intent(this, TCPService.class);
        enable = (Button) findViewById(R.id.tcpenable);
        disable = (Button) findViewById(R.id.tcpdisable);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        startService(mIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tcpenable:
            case R.id.tcpdisable:
        }
    }
}