/*
 * LeaderBoardActivity.java
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

import com.standrews.mscproject.custom_ui_componets.LeaderBoardSurfaceView;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-26.
 */
public class LeaderBoardActivity extends Activity implements View.OnClickListener {

    private LeaderBoardSurfaceView lbs;

    @Override
    public void onClick(View view) {
        this.finish();
        overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        lbs = (LeaderBoardSurfaceView) findViewById(R.id.leaderboardSurfaceView);
        ImageButton quit = (ImageButton) findViewById(R.id.quitLBImageButton);
        quit.setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {

            lbs.drawChart();
        }
    }
}