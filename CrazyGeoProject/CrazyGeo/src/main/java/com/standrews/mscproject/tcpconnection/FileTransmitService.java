/*
 * FileTransmitService.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tcpconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.standrews.mscproject.utils.NetworkChecker;

/**
 * MSc project
 * <p/>
 * This class extends the BroadcastReceiver responsible for sent file to server
 *
 * Created by Ziji Wang on 13-8-9.
 */
public class FileTransmitService extends BroadcastReceiver {

    public static boolean gamePlaying = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("me", "start send");
        Thread t = new Thread(new MyRunnable(context));
        t.start();

    }

    /**
     * File transmit thread
     */
    private class MyRunnable implements Runnable {

        private Context context;

        private MyRunnable(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            NetworkChecker mNetworkChecker = new NetworkChecker(context);
            Looper.prepare();
            if(gamePlaying)
                Log.i("me", "playing");
            if (mNetworkChecker.isWifiReachable()&&!gamePlaying) {
                FileSender fs = new FileSender(context);
                int result = fs.send();
                Log.i("me", "result" + result);
            }
            Looper.loop();
        }
    }
}
