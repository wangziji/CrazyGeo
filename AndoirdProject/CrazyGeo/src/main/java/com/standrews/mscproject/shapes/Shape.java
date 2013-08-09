/*
 * Shape.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.shapes;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-29.
 */
public abstract class Shape {

    public class ShapeType {
        public static final int SQUARE = 0;
        public static final int RECTANGLE = 1;
        public static final int TRIANGLE = 2;
        public static final int CIRCLE = 3;
    }


    public int type;
    private Bitmap mBitmap;

    public Shape(Resources resources, int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public int getType() {
        return type;
    }
}
