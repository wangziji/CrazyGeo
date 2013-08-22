
/*
 * MainActivity.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DeviceInformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * MSc project
 * <p/>
 * The activity for splash screen
 *
 * Created by Ziji Wang on 13-7-26.
 */
public class MainActivity extends Activity {


    private AnimationDrawable logoAnimation;
    private MyHandler mHandler;
    private ImageView logo;

    /**
     * Initialize, loading config files
     */
    private void initialize() {
        Configuration mConfiguration = new Configuration();
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
            config.setProperty("ASIA_BEST", 0 + "");
            config.setProperty("EUROPE_BEST", 0 + "");
            config.setProperty("AMERICA_BEST", 0 + "");
            config.setProperty("AFRICA_BEST", 0 + "");
            config.setProperty("HANDEDNESS", 1 + "");
            config.setProperty("AUTO_DETECT", 1 + "");
            config.setProperty("SOUND", 0 + "");
            config.setProperty("USER_PERMISSION", 0 + "");
        }
        Properties props = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/assets/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            System.out.println();
            e.printStackTrace();
        }
        String ip = props.getProperty("SERV_IP");
        String port = props.getProperty("SERV_PORT");
        String dir = props.getProperty("DATA_DIR");
        config.setProperty("SERV_IP", ip);
        config.setProperty("SERV_PORT", port);
        config.setProperty("DATA_DIR", dir);
        played++;
        config.setProperty("PLAYED", played + "");
        mConfiguration.saveConfigProperties(MainActivity.this, config);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    initialize();
                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, StartActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    break;

            }
        }
    }

    private class Initialize implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = (ImageView) findViewById(R.id.logoImageView);
        logo.setBackgroundResource(R.drawable.logo);
        logoAnimation = (AnimationDrawable) logo.getBackground();
        mHandler = new MyHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logo.destroyDrawingCache();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            logoAnimation.stop();
            logoAnimation.start();
            Thread t = new Thread(new Initialize());
            t.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
