/*
 * MapIamgeView.java
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
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-10.
 */
public class MapImageView extends ImageView {

    private Bitmap mBitmap;

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageResource(int resId, int desHeight) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        float scaleFactor = (float) desHeight / (float) mBitmap.getHeight();
        int width = Math.round(mBitmap.getWidth() * scaleFactor);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, width, desHeight, true);
        setImageBitmap(mBitmap);
    }

    public void recycle() {
        mBitmap.recycle();
    }
}
