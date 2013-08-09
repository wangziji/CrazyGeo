/*
 * InternetService.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tcpconnection;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.standrews.mscproject.utils.NetworkChecker;

import java.util.Timer;
import java.util.TimerTask;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-18.
 */
public class InternetService extends IntentService {


    private NetworkChecker mNetworkChecker;


    public InternetService() {
        super("intent");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("me", "service create");
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
        mNetworkChecker = new NetworkChecker(InternetService.this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        onHandleIntent(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Toast.makeText(this, "Service start!", Toast.LENGTH_SHORT).show();
        //handler.post(new SendFiles());

        Timer t = new Timer();

        t.schedule(new SendFiles(), 0, 5000);
//        ScheduledExecutorService

        Log.i("me", "service start");
    }

    private class SendFiles extends TimerTask {
        @Override
        public void run() {
            Log.i("me", "start send");
            Looper.prepare();
            if (mNetworkChecker.isWifiReachable()) {
                FileSender fs = new FileSender(InternetService.this);
                int result = fs.send();
                Log.i("me", "result" + result);
                switch (result) {
                    case 0:
                        Toast.makeText(InternetService.this, "no file", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(InternetService.this, "success", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(InternetService.this, "exception", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(InternetService.this, "time out", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(InternetService.this, "thread", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(InternetService.this, "IO", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(InternetService.this, "WIFI disabled", Toast.LENGTH_SHORT).show();
            }
            //handler.postDelayed(this, 1000 * 5);
            Looper.loop();
        }
    }
}
