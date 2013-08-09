/*
 * MyLayout.java
 *
 * Created on: 10 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.standrews.mscproject.utils.Configuration;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-10.
 */
public class MyLayout extends FrameLayout {

    private Bitmap bitmap;

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundResource(int resid) {
        Configuration configuration = new Configuration();
        int dpi = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("DPI"));
        int sampleSize = 1;
        if (dpi > 160 && dpi <= 240) {
            int tempA = 1920 / getWidth();
            int tempB = 1200 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        } else if (dpi > 240) {
            int tempA = 2560 / getWidth();
            int tempB = 1600 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        } else if (dpi <= 160) {
            int tempA = 1280 / getWidth();
            int tempB = 800 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        bitmap = BitmapFactory.decodeResource(getResources(), resid, options);

        float sw = (float) getWidth() / (float) bitmap.getWidth();
        float sh = (float) getHeight() / (float) bitmap.getHeight();
        float sf = sw > sh ? sw : sh;
        int w = (int) (bitmap.getWidth() * sf);
        int h = (int) (bitmap.getHeight() * sf);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        this.setBackgroundDrawable(drawable);
    }

    public void recycle() {
        bitmap.recycle();
    }


}
