/*
 * GameEventListener.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

/**
 * MSc project
 * <p/>
 * This is the interface for game event listener
 *
 * Created by Ziji Wang on 13-7-11.
 */
public interface GameEventListener {

    /**
     * Defined how the component should responds when game lose
     */
    public void onGameLose();

    /**
     * Defined how the component should responds when game pause
     */
    public void onGamePause();

    /**
     * Defined how the component should responds in game prepare stage
     * @param country Country
     * @param continent int, continent, refer to Continent class
     * @param round int, current round
     */
    public void onGamePrepare(Country country, int continent, int round);

    /**
     * Defined how the component should responds when game quit
     */
    public void onGameQuit();

    /**
     * Defined how the component should responds when game resume
     */
    public void onGameResume();

    /**
     * Defined how the component should responds when game start
     */
    public void onGameStart();

    /**
     * Defined how the component should responds when game win
     */
    public void onGameWin();
}
