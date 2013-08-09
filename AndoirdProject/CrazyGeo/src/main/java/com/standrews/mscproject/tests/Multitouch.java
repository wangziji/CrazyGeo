/*
 * Multitouch.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.standrews.mscproject.multitouch.MultitouchListener;

/**
 * Created by ziji on 13-6-9.
 */
public class Multitouch extends Activity {

    ImageView iv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MultiTouchView(Multitouch.this));


    }

    class MultiTouchView extends View {

        public MultiTouchView(Context context) {
            super(context);
            this.setOnTouchListener(new MultitouchListener(context));
        }

    }
}