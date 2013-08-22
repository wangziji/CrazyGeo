/*
 * GameStateHandler.java
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
 * The interface for state handler
 *
 * Created by Ziji Wang on 13-7-11.
 */
public interface GameStateHandler {

    /**
     * Defined what handler should do when state changed
     * @param state int, state
     */
    public void changeState(int state);

}
