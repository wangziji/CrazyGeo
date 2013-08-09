/*
 * PauseButton.java
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

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameStateReporter;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-13.
 */
public class PauseButton extends ImageButton implements GameStateReporter, GameEventListener, View.OnClickListener {

    private boolean isActive = false;
    private GameStateMonitor mGameStateMonitor;

    public PauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isActive) {
            reportStateChanged(GameStateMonitor.GAME_PAUSE);
        }
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        setEnabled(false);
    }

    @Override
    public void onGameStart() {
        isActive = true;
        setEnabled(true);
    }

    @Override
    public void onGamePause() {
        isActive = false;
        setEnabled(false);
    }

    @Override
    public void onGameResume() {
        isActive = true;
        setEnabled(true);
    }

    @Override
    public void onGameWin() {
        isActive = false;
        setEnabled(false);
    }

    @Override
    public void onGameLose() {
        isActive = false;
        setEnabled(false);
    }

    @Override
    public void onGameQuit() {
        isActive = false;
        setEnabled(false);
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
