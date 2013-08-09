/*
 * CreditsActivity.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-29.
 */
public class CreditsActivity extends Activity implements View.OnClickListener {

    private ImageButton quit;

    @Override
    public void onClick(View view) {
        if (view == quit) {
            this.finish();
            overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        quit = (ImageButton) findViewById(R.id.quitCDImageButton);
        quit.setOnClickListener(this);
    }
}