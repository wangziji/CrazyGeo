/*
 * MapImageView.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.standrews.mscproject.utils.bitmap_decoder.BitmapDecoder;

/**
 * MSc project
 * <p/>
 * This class defined the background map for the main menu
 *
 * Created by Ziji Wang on 13-7-10.
 */
public class MapImageView extends ImageView {

    private Bitmap mBitmap;

    /**
     * Constructor, inherited from android.widget.ImageView
     * @param context context
     * @param attrs AttributeSet
     */
    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Recycle the bitmap
     */
    public void recycle() {
        mBitmap.recycle();
    }

    /**
     * Load and resize the bitmap using BitmapDecoder
     * @param resID int, ID of the drawable resource
     * @param desHeight int, target height
     */
    public void setImageResource(int resID, int desHeight) {
        mBitmap = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resID, getWidth(), getHeight());
        float scaleFactor = (float) desHeight / (float) mBitmap.getHeight();
        int width = Math.round(mBitmap.getWidth() * scaleFactor);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, width, desHeight, true);
        setImageBitmap(mBitmap);
    }
}
