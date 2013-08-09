/*
 * GameActivity.java
 *
 * Created on: 8 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.standrews.mscproject.UIAsyncHandler.UIAsyncHandler;
import com.standrews.mscproject.custom_ui_componets.ClockImageView;
import com.standrews.mscproject.custom_ui_componets.GameSurfaceView;
import com.standrews.mscproject.custom_ui_componets.GameTextView;
import com.standrews.mscproject.custom_ui_componets.PauseButton;
import com.standrews.mscproject.custom_ui_componets.ReplayLayout;
import com.standrews.mscproject.custom_ui_componets.ResumeLayout;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameWorld;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-8.
 */
public class GameActivity extends Activity {

    private GameWorld gameWorld;
    private GameSurfaceView surfaceView;
    private GameTextView textView;
    private ClockImageView clock;
    private UIAsyncHandler handler;
    private GameStateMonitor gameStateMonitor;
    private PauseButton pauseButton;
    private ResumeLayout resumeLayout;
    private ReplayLayout replayLayout;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        initialize();

        setMonitor();

        setListener();

        adjustComponents();
    }

    private void initialize() {
        int continent = getIntent().getIntExtra("continent", -1);
        handler = new UIAsyncHandler();
        gameWorld = new GameWorld(getResources(), continent);
        gameStateMonitor = new GameStateMonitor(gameWorld);

        surfaceView = (GameSurfaceView) findViewById(R.id.gameSurface);
        surfaceView.setPackName(getPackageName());

        textView = (GameTextView) findViewById(R.id.gameTextView);

        clock = (ClockImageView) findViewById(R.id.clockImageView);

        //test
        Configuration configuration = new Configuration();
        int handedness = Integer.parseInt(configuration.getConfigProperties(this).getProperty("HANDEDNESS"));
        FrameLayout ll = (FrameLayout) findViewById(R.id.actionBar);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if(handedness==1){
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }else {
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        }

        ll.setLayoutParams(params);

        pauseButton = (PauseButton) findViewById(R.id.pauseButton);
        resumeLayout = (ResumeLayout) findViewById(R.id.resumeLayout);
        replayLayout = (ReplayLayout) findViewById(R.id.replayLayout);
    }

    private void setMonitor() {
        surfaceView.setMonitor(gameStateMonitor);
        handler.setMonitor(gameStateMonitor);
        clock.setMonitor(gameStateMonitor);
        pauseButton.setMonitor(gameStateMonitor);
        resumeLayout.setMonitor(gameStateMonitor);
        replayLayout.setMonitor(gameStateMonitor);

    }

    private void setListener() {
        gameWorld.addGameEventListener(surfaceView);
        gameWorld.addGameEventListener(textView);
        gameWorld.addGameEventListener(clock);
        gameWorld.addGameEventListener(pauseButton);
        gameWorld.addGameEventListener(resumeLayout);
        gameWorld.addGameEventListener(replayLayout);
        //last one
        gameWorld.addGameEventListener(handler);
    }

    private void adjustComponents() {
        DisplayManager dm = new DisplayManager();
        dm.setFont(this, textView);
        int[] size = dm.getDPSize(this);
        float textSizeRate = size[1] / 320f;
        int textSize = getResources().getInteger(R.integer.board_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);

        int padding = Math.round(size[1] * 0.1f);
        clock.setPadding(0, padding, 0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            gameWorld.prepareGame();
//            gameWorld.startGame();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clock.recycle();
    }
}