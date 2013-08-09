/*
 * GameStateMonitor.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

import android.os.Handler;
import android.os.Message;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-11.
 */
public class GameStateMonitor extends Handler {

    public final static int GAME_PREPARE = 0;
    public final static int GAME_START = 1;
    public final static int GAME_PAUSE = 2;
    public final static int GAME_RESUME = 3;
    public final static int GAME_WIN = 4;
    public final static int GAME_LOSE = 5;
    public final static int GAME_QUIT = 6;
    public final static int GAME_REPLAY = 7;
    public final static int MEG_STATE_CHANGE = 0;
    private GameStateHandler gameStateHandler;

    public GameStateMonitor(GameStateHandler gameStateHandler) {
        this.gameStateHandler = gameStateHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MEG_STATE_CHANGE:

                onStateChange(msg.arg1);
                break;
        }
    }

    public void onStateChange(int state) {
        gameStateHandler.changeState(state);
    }
}
