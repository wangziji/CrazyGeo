/*
 * GameStateReporter.java
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
 * This is the interface for state change reporter
 *
 * Created by Ziji Wang on 13-7-11.
 */
public interface GameStateReporter {

    /**
     * Report state change event to GameStateMonitor
     * @param state int, state
     */
    public void reportStateChanged(int state);

    /**
     * Set GameStateMonitor
     * @param gameStateMonitor GameStateMonitor
     */
    public void setMonitor(GameStateMonitor gameStateMonitor);

}
