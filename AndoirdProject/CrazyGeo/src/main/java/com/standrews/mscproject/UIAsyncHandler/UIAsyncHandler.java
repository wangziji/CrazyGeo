/*
 * UIAsyncHandler.java
 *
 * Created on: 12 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.UIAsyncHandler;

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameStateReporter;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-12.
 */
public class UIAsyncHandler implements GameEventListener, GameStateReporter {

    private GameStateMonitor mGameStateMonitor;

    public UIAsyncHandler() {
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    reportStateChanged(GameStateMonitor.GAME_START);
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

    }

    @Override
    public void onGamePause() {

    }

    @Override
    public void onGameResume() {

    }

    @Override
    public void onGameWin() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    reportStateChanged(GameStateMonitor.GAME_PREPARE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    @Override
    public void onGameLose() {

    }

    @Override
    public void onGameQuit() {

    }

    @Override
    public void setMonitor(GameStateMonitor gameStateMonitor) {
        mGameStateMonitor = gameStateMonitor;
    }

    @Override
    public void reportStateChanged(int state) {
        mGameStateMonitor.obtainMessage(GameStateMonitor.MEG_STATE_CHANGE, state, 0, null).sendToTarget();
    }
}
