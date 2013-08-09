/*
 * ReplayLayout.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameStateReporter;
import com.standrews.mscproject.main.R;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-13.
 */
public class ReplayLayout extends LinearLayout implements GameEventListener, GameStateReporter, View.OnClickListener {

    private GameStateMonitor mGameStateMonitor;

    public ReplayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onClick(View view) {
        reportStateChanged(GameStateMonitor.GAME_REPLAY);
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        ImageButton button = (ImageButton) findViewById(R.id.replayImageButton);
        button.setOnClickListener(this);
        setVisibility(GONE);
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGamePause() {
        setVisibility(VISIBLE);
    }

    @Override
    public void onGameResume() {
        setVisibility(GONE);
    }

    @Override
    public void onGameWin() {

    }

    @Override
    public void onGameLose() {
        setVisibility(VISIBLE);
    }

    @Override
    public void onGameQuit() {
        destroyDrawingCache();
    }

    @Override
    public void setMonitor(GameStateMonitor gameStateMonitor) {
        mGameStateMonitor = gameStateMonitor;
    }

    @Override
    public void reportStateChanged(int state) {
        mGameStateMonitor.onStateChange(state);
    }
}
