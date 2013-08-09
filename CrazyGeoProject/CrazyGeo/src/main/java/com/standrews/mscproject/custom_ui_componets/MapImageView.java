/*
 * MapImageView.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.standrews.mscproject.bitmap_decoder.BitmapDecoder;

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

    public void recycle() {
        mBitmap.recycle();
    }

    public void setImageResource(int resID, int desHeight) {
        mBitmap = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resID, getWidth(), getHeight());
        float scaleFactor = (float) desHeight / (float) mBitmap.getHeight();
        int width = Math.round(mBitmap.getWidth() * scaleFactor);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, width, desHeight, true);
        setImageBitmap(mBitmap);
    }
}
