/*
 * GameTextView.java
 *
 * Created on: 11 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-11.
 */
public class GameTextView extends TextView implements GameEventListener {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    setText("3");
                    invalidate();
                    break;
                case 2:
                    setText("2");
                    invalidate();
                    break;
                case 1:
                    setText("1");
                    invalidate();
                    break;
            }
        }
    };

    public GameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        this.setText(null);
        this.invalidate();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.obtainMessage(3).sendToTarget();
                    Thread.sleep(1000);
                    mHandler.obtainMessage(2).sendToTarget();
                    Thread.sleep(1000);
                    mHandler.obtainMessage(1).sendToTarget();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    @Override
    public void onGameStart() {
        this.setText(null);
        this.invalidate();
    }

    @Override
    public void onGamePause() {
        String pause = "Pause";
        this.setText(pause);
        this.invalidate();
    }

    @Override
    public void onGameResume() {
        this.setText(null);
        this.invalidate();
    }

    @Override
    public void onGameWin() {
        String win = "Congratulation!";
        this.setText(win);
        this.invalidate();
    }

    @Override
    public void onGameLose() {
        String lose = "Sorry You Lose";
        this.setText(lose);
        this.invalidate();

    }

    @Override
    public void onGameQuit() {

    }

}
