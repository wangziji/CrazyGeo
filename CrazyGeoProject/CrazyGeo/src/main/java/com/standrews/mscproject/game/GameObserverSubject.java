/*
 * GameObserverSubject.java
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
 * This is the interface for the subject in Observer design pattern
 *
 * Created by Ziji Wang on 13-7-11.
 */
public interface GameObserverSubject {

    /**
     * Add Observer
     * @param gameEventListener GameEventListener, the observer
     */
    public void addGameEventListener(GameEventListener gameEventListener);

    /**
     * remove the Observer
     * @param gameEventListener GameEventListener, the observer
     */
    public void removeGameEventListener(GameEventListener gameEventListener);

}
