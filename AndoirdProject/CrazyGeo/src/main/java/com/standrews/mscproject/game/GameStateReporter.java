/*
 * GameStateReporter.java
 *
 * Created on: 11 /7 /2013
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
public interface GameStateReporter {

    public void setMonitor(GameStateMonitor gameStateMonitor);

    public void reportStateChanged(int state);

}
