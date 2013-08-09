/*
 * GameEventReceiver.java
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
public interface GameEventReceiver {

    public void addGameEventListener(GameEventListener gameEventListener);

    public void removeGameEventListener(GameEventListener gameEventListener);

}
