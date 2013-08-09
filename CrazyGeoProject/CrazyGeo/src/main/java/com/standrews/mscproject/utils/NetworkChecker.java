/*
 * NetworkChecker.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Check the network status
 */
public class NetworkChecker {


    private ConnectivityManager cm;

    /**
     * The constructor, set the connective manager
     *
     * @param con Context
     */
    public NetworkChecker(Context con) {
        cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Check the type of network connection
     *
     * @return True: WIFI
     * False: Otherwise
     */
    public boolean isWifiReachable() {
        if (isNetworkReachable()) {
            NetworkInfo current = cm.getActiveNetworkInfo();
            return (current.getType() == ConnectivityManager.TYPE_WIFI);
        } else {
            return false;
        }
    }

    /**
     * Private method in order to check the connective status
     *
     * @return True: the network is available
     * False: the network is unavailable
     */
    private boolean isNetworkReachable() {
        NetworkInfo current = cm.getActiveNetworkInfo();
        return current != null && (current.getState() == NetworkInfo.State.CONNECTED);
    }


}
