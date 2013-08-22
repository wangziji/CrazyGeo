/*
 * HelpImageButton.java
 *
 * Created on: 22 /8 /2013
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
 * This class defined the help button, which linked to the tutorial
 *
 * Created by Ziji Wang on 13-7-25.
 */
public class HelpImageButton extends ImageButton implements GameStateReporter, GameEventListener, View.OnClickListener {

    private GameStateMonitor mGameStateMonitor;

    public HelpImageButton(Context context) {
        super(context);
    }

    /**
     * Constructor, inherited from android.widget.ImageButton
     * @param context Context
     * @param attrs AttributeSet
     */
    public HelpImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public HelpImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onClick(View view) {
        reportStateChanged(GameStateMonitor.GAME_PAUSE);
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        setEnabled(false);
    }

    @Override
    public void onGameStart() {
        setEnabled(true);
    }

    @Override
    public void onGamePause() {
        setEnabled(false);
    }

    @Override
    public void onGameResume() {
        setEnabled(true);
    }

    @Override
    public void onGameWin() {
        setEnabled(false);
    }

    @Override
    public void onGameLose() {
        setEnabled(false);
    }

    @Override
    public void onGameQuit() {
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
