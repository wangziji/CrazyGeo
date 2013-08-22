/*
 * DisplayManager.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * MSc project
 * <p/>
 * This utility is for manage several display issue
 *
 * Created by Ziji Wang on 13-7-6.
 */
public class DisplayManager {

    public DisplayManager() {
    }

    /**
     * Transform density-independent dip of baseline to pixels of real display
     *
     * @param dip     dip
     * @param context Context
     * @return DPI
     */
    public int DIPtoPixel(int dip, Context context) {
        Configuration configuration = new Configuration();
        int DPI = Integer.parseInt(configuration.getConfigProperties(context).getProperty("DPI"));
        float density = DPI / 160f;
        return Math.round((dip * density) + 0.5f);
    }

    public int[] getDPSize(Activity a) {
        DisplayMetrics metric = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;
        int[] size = new int[2];
        size[0] = Math.round((width / density) + 0.5f);
        size[1] = Math.round((height / density) + 0.5f);
        return size;
    }

    /**
     * Set font for text view
     * @param c Context
     * @param v TextView
     */
    public void setFont(Context c, TextView v) {
        Typeface face = Typeface.createFromAsset(c.getAssets(), "fonts/Comics_font_by_kernalphage.ttf");
        v.setTypeface(face);
    }

}
