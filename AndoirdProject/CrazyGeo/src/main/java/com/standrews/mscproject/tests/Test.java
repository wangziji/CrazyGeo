/*
 * Test.java
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

/**
 * Created by Ziji on 13-5-29.
 */
public class Test extends Activity {

    private Button button_test;
    private Button config;
    private Button external_test, tcp, mt, game, surfaceview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        button_test = (Button) findViewById(R.id.buttonwifitest);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, WifiTest.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });

        config = (Button) findViewById(R.id.config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, ConfigTest.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });
        external_test = (Button) findViewById(R.id.external);
        external_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, ExternalFileTest.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });
        tcp = (Button) findViewById(R.id.tcptest);
        tcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, TCPTest.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });
        mt = (Button) findViewById(R.id.multitouch);
        mt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, Multitouch.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });
        game = (Button) findViewById(R.id.gamedemo);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Test.this, GameDemo.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });
        surfaceview = (Button) findViewById(R.id.surfaceview);
        surfaceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("level", 0);
                intent.setClass(Test.this, SurfaceViewExample.class);
                startActivity(intent);
                //Test.this.finish();
            }
        });

    }
}