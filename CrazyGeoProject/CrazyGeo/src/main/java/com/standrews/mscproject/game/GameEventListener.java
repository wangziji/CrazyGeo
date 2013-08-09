/*
 * GameEventListener.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-11.
 */
public interface GameEventListener {

    public void onGameLose();

    public void onGamePause();

    public void onGamePrepare(Country country, int continent, int round);

    public void onGameQuit();

    public void onGameResume();

    public void onGameStart();

    public void onGameWin();
}
