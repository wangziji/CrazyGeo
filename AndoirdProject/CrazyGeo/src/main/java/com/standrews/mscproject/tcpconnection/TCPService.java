/*
 * TCPService.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tcpconnection;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.standrews.mscproject.utils.NetworkChecker;

/**
 * Created by ziji on 13-6-8.
 */
public class TCPService extends Service {

    private Handler handler;
    private Runnable mTimerTask;
    private NetworkChecker mNetworkChecker;


    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
        mNetworkChecker = new NetworkChecker(TCPService.this);
        handler = new Handler();
        mTimerTask = new Runnable() {
            @Override
            public void run() {
//                if (mNetworkChecker.isWifiReachable()) {
                FileSender fs = new FileSender(TCPService.this);
                int result = fs.send();
                switch (result) {
                    case 0:
                        Toast.makeText(TCPService.this, "no file", Toast.LENGTH_SHORT).show();
                    case 1:
                        Toast.makeText(TCPService.this, "success", Toast.LENGTH_SHORT).show();
                    case 2:
                        Toast.makeText(TCPService.this, "exception", Toast.LENGTH_SHORT).show();
                    case 3:
                        Toast.makeText(TCPService.this, "network", Toast.LENGTH_SHORT).show();
                }
//                } else {
//                    Toast.makeText(TCPService.this, "WIFI disabled", Toast.LENGTH_SHORT).show();
//                }
                handler.postDelayed(mTimerTask, 1000 * 5);
            }
        };
        handler.post(mTimerTask);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(TCPService.this, "Service stopped!", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(mTimerTask);
    }


    public IBinder onBind(Intent intent) {
        return null;
    }


}
