/*
 * GameActivity.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.standrews.mscproject.custom_ui_componets.BackToMenuLayout;
import com.standrews.mscproject.custom_ui_componets.ClockImageView;
import com.standrews.mscproject.custom_ui_componets.GameSurfaceView;
import com.standrews.mscproject.custom_ui_componets.GameTextView;
import com.standrews.mscproject.custom_ui_componets.HelpImageButton;
import com.standrews.mscproject.custom_ui_componets.MySeekBar;
import com.standrews.mscproject.custom_ui_componets.PauseButton;
import com.standrews.mscproject.custom_ui_componets.ReplayLayout;
import com.standrews.mscproject.custom_ui_componets.ResumeLayout;
import com.standrews.mscproject.custom_ui_componets.ScoreTextView;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameWorld;
import com.standrews.mscproject.tcpconnection.FileTransmitService;
import com.standrews.mscproject.game.ui_async_handler.UIAsyncHandler;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

/**
 * MSc project
 * <p/>
 * The activity of game play screen
 *
 * Created by Ziji Wang on 13-7-8.
 */
public class GameActivity extends Activity implements View.OnClickListener {

    private GameWorld gameWorld;
    private GameSurfaceView surfaceView;
    private GameTextView textView;
    private ClockImageView clock;
    private UIAsyncHandler handler;
    private GameStateMonitor gameStateMonitor;
    private PauseButton pauseButton;
    private ResumeLayout resumeLayout;
    private ReplayLayout replayLayout;
    private BackToMenuLayout backToMenuLayout;
    private MySeekBar seekBar;
    private ScoreTextView scoreTextView;
    private boolean isFirstTime = true, hasFocus = false;
    private FrameLayout fl;
    private Handler mHandler;
    private HelpImageButton help;

    @Override
    public void onClick(View view) {
        if (view == help) {
            gameWorld.pauseGame();
            dialog();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("me", "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FileTransmitService.gamePlaying=true;
        fl = (FrameLayout) findViewById(R.id.loadingLayoutGame);
        mHandler = new MyHandler();

        DisplayManager dm = new DisplayManager();
        TextView crazy = (TextView) findViewById(R.id.logoTextViewCrazyGame);
        TextView geo = (TextView) findViewById(R.id.logoTextViewGeoGame);
        TextView loading = (TextView) findViewById(R.id.loadingTextViewGame);

        dm.setFont(this, crazy);
        dm.setFont(this, geo);
        dm.setFont(this, loading);

        int[] size = dm.getDPSize(this);
        int padding = Math.round(size[1] * 0.1f);
        crazy.setPadding(0, 0, padding, 0);
        geo.setPadding(padding, 0, 0, 0);


        //set text size
        float textSizeRate = size[1] / 320f;
        int bigSize = getResources().getInteger(R.integer.Logo_big);
        crazy.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigSize * textSizeRate);
        int smallSize = getResources().getInteger(R.integer.Logo_small);
        geo.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize * textSizeRate);
        int textSize = getResources().getInteger(R.integer.text_size);
        loading.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fl.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasFocus) {
            gameWorld.pauseGame();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasFocus) {
            gameWorld.quitGame();
            System.gc();
        }
        FileTransmitService.gamePlaying=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (gameWorld.getState() == GameStateMonitor.GAME_PAUSE) {
                gameWorld.changeState(GameStateMonitor.GAME_RESUME);
            } else if (gameWorld.getState() != GameStateMonitor.GAME_WIN && gameWorld.getState() != GameStateMonitor.GAME_LOSE) {
                gameWorld.changeState(GameStateMonitor.GAME_PAUSE);
            }
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.hasFocus = hasFocus;

            Thread t = new Thread(new Initialize());
            t.start();
        }
    }

    /**
     * Set font and size for TextViews
     */
    private void adjustComponents() {
        DisplayManager dm = new DisplayManager();
        dm.setFont(this, textView);
        dm.setFont(this, scoreTextView);
        int[] size = dm.getDPSize(this);
        float textSizeRate = size[1] / 320f;
        int boardSize = getResources().getInteger(R.integer.board_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, boardSize * textSizeRate);

        int textSize = getResources().getInteger(R.integer.text_size);
        scoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);

        int padding = Math.round(size[1] * 0.1f);
        clock.setPadding(0, padding, 0, 0);
        scoreTextView.setPadding(padding, padding, padding, 0);
    }

    /**
     * Initialize all the component
     */
    private void initialize() {
        int continent = getIntent().getIntExtra("continent", -1);
        handler = new UIAsyncHandler();
        gameWorld = new GameWorld(getResources(), continent, this);
        gameStateMonitor = new GameStateMonitor(gameWorld);

        surfaceView = (GameSurfaceView) findViewById(R.id.gameSurface);
        surfaceView.setPackName(getPackageName());

        textView = (GameTextView) findViewById(R.id.gameTextView);
        clock = (ClockImageView) findViewById(R.id.clockImageView);

        Configuration configuration = new Configuration();
        int handedness = Integer.parseInt(configuration.getConfigProperties(this).getProperty("HANDEDNESS"));
        Log.i("me",handedness+"");
        LinearLayout ll = (LinearLayout) findViewById(R.id.actionBar);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if (handedness == 1) {
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        } else {
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        }
        ll.setLayoutParams(params);

        FrameLayout fl = (FrameLayout) findViewById(R.id.scoreLayout);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if (handedness == 1) {
            params2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        } else {
            params2.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }
        fl.setLayoutParams(params2);

        pauseButton = (PauseButton) findViewById(R.id.pauseButton);
        resumeLayout = (ResumeLayout) findViewById(R.id.resumeLayout);
        replayLayout = (ReplayLayout) findViewById(R.id.replayLayout);
        backToMenuLayout = (BackToMenuLayout) findViewById(R.id.backToMenuLayout);
        seekBar = (MySeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(surfaceView);
        scoreTextView = (ScoreTextView) findViewById(R.id.scoreTextView);
        help = (HelpImageButton) findViewById(R.id.helpButton);
        help.setOnClickListener(this);
    }

    /**
     * Add observer for the subject
     */
    private void setListener() {
        gameWorld.addGameEventListener(surfaceView);
        gameWorld.addGameEventListener(textView);
        gameWorld.addGameEventListener(clock);
        gameWorld.addGameEventListener(pauseButton);
        gameWorld.addGameEventListener(resumeLayout);
        gameWorld.addGameEventListener(replayLayout);
        gameWorld.addGameEventListener(backToMenuLayout);
        gameWorld.addGameEventListener(seekBar);
        gameWorld.addGameEventListener(scoreTextView);
        gameWorld.addGameEventListener(help);
        //last one
        gameWorld.addGameEventListener(handler);
    }

    /**
     * Set GameStateMonitor for component
     */
    private void setMonitor() {
        surfaceView.setMonitor(gameStateMonitor);
        handler.setMonitor(gameStateMonitor);
        clock.setMonitor(gameStateMonitor);
        pauseButton.setMonitor(gameStateMonitor);
        resumeLayout.setMonitor(gameStateMonitor);
        replayLayout.setMonitor(gameStateMonitor);
        backToMenuLayout.setMonitor(gameStateMonitor);
        help.setMonitor(gameStateMonitor);

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initialize();
                    setMonitor();
                    setListener();
                    adjustComponents();
                    break;
                case 2:
                    gameWorld.prepareGame();
                    break;
                case 3:
                    surfaceView.drawOnPaused();
                    fl.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class Initialize implements Runnable {
        @Override
        public void run() {
            if (isFirstTime) {
                mHandler.sendEmptyMessage(1);
                isFirstTime = false;
            }
            mHandler.sendEmptyMessage(2);
            mHandler.sendEmptyMessage(3);
        }
    }

    /**
     * Define the dialog
     */
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.tutorial_dialog_msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                GameActivity.this.startActivity(new Intent(GameActivity.this, TutorialActivity.class));
                GameActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}