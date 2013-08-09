/*
 * SurfaceViewExample.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.os.Bundle;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-25.
 */
public class SurfaceViewExample extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int level = getIntent().getIntExtra("level", -1);
//        GameSurfaceView surfaceView = new GameSurfaceView(this);
//        setContentView(surfaceView);
    }

}