/*
 * GameDemo.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.standrews.mscproject.main.R;
import com.standrews.mscproject.multitouch.MultitouchHandler;
import com.standrews.mscproject.multitouch.MultitouchListener;

/**
 * Created by ziji on 13-6-13.
 */
public class GameDemo extends Activity implements View.OnTouchListener {

    private ImageView taget, orig;
    private ScaleGestureDetector sgd;
    private Matrix matrix;
    private float oldDistance;
    private float newDistance;
    private MultitouchHandler mMyGestureDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamedemo);
        taget = (ImageView) findViewById(R.id.test_target);
        taget.setOnTouchListener(new MultitouchListener(GameDemo.this));

        orig = (ImageView) findViewById(R.id.test_orin32);

//        orig.setOnTouchListener(new MultitouchListener(GameDemo.this));


        mMyGestureDetector = new MultitouchHandler(GameDemo.this, orig);
        matrix = new Matrix();
        orig.setOnTouchListener(this);
        //savedMatrix = new Matrix();


    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mMyGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mMyGestureDetector.getLocation();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mMyGestureDetector.getLocation(taget);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyGestureDetector.onDestroy();
    }


}