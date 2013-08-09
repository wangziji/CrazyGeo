/*
 * DeviceInformation.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-21.
 */
public class DeviceInformation {

    public DeviceInformation() {
    }

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point temp = new Point();
        display.getRealSize(temp);
        return temp;
    }

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
        }
        return Formatter.formatFileSize(context, initial_memory);
    }


}
