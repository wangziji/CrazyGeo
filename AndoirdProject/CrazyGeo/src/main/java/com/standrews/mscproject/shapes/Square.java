/*
 * Square.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.shapes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.standrews.mscproject.main.R;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-29.
 */
public class Square extends Shape {
    public Square(Resources resources, int type) {
        super(resources, type);
        if (type == 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.china_src);
            super.setBitmap(bitmap);
        } else if (type == 1) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.asia);
            super.setBitmap(bitmap);
        }
    }
}
