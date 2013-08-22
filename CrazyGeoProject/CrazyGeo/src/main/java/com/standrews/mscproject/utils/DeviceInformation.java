/*
 * DeviceInformation.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * MSc project
 * <p/>
 * This class is for gain device information
 *
 * Created by Ziji Wang on 13-6-21.
 */
public class DeviceInformation {

    public DeviceInformation() {
    }

    /**
     * Get display size
     * @param activity Activity
     * @return Point, size: point.x = width; point.y = height
     */
    public Point getDisplaySize(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        Point temp = new Point(width, height);
        return temp;
    }

    /**
     * Get UUID
     * @param context Context
     * @return String, UUID
     */
    public String getID(Context context) {
        UUID uuid = UUID.randomUUID();
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("id", "android:" + androidID);
        try {
            if (!"9774d56d682e549c".equals(androidID)) {
                uuid = UUID.nameUUIDFromBytes(androidID.getBytes("utf8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return uuid.toString();
    }

    /**
     * Get memory size
     * @param context context
     * @return String, memory size
     */
    public String getMemorySize(Context context) {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Formatter.formatFileSize(context, initial_memory);
    }


}
