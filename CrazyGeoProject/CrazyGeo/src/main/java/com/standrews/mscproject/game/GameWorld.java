/*
 * GameWorld.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.standrews.mscproject.main.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-8.
 */
public class GameWorld implements GameEventReceiver, GameStateHandler {

    private Resources resource;
    private int continent, randomCountry = -1, round = 1, state = GameStateMonitor.GAME_PREPARE;
    private ArrayList<Country> countries;
    private ArrayList<GameEventListener> listeners;
    private boolean isActive = false;
    private Activity activity;


    public GameWorld(Resources resource, int continent, Activity activity) {
        this.resource = resource;
        this.continent = continent;
        this.listeners = new ArrayList<GameEventListener>();
        this.activity = activity;
        initialize();
    }

    @Override
    public void addGameEventListener(GameEventListener gameEventListener) {
        listeners.add(gameEventListener);
    }

    @Override
    public void removeGameEventListener(GameEventListener gameEventListener) {
        listeners.remove(gameEventListener);
    }

    @Override
    public void changeState(int state) {
        this.state = state;
        switch (state) {
            case GameStateMonitor.GAME_PREPARE:
                this.prepareGame();
                break;
            case GameStateMonitor.GAME_START:
                this.startGame();
                break;
            case GameStateMonitor.GAME_PAUSE:
                this.pauseGame();
                break;
            case GameStateMonitor.GAME_RESUME:
                this.resumeGame();
                break;
            case GameStateMonitor.GAME_WIN:
                this.winGame();
                isActive = false;
                break;
            case GameStateMonitor.GAME_LOSE:
                loseGame();
                break;
            case GameStateMonitor.GAME_QUIT:
                activity.finish();
                activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case GameStateMonitor.GAME_REPLAY:
                round = 1;
                setActive(false);
                this.prepareGame();
                break;
        }
    }

    public int getState() {
        return state;
    }

    public void loseGame() {
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGameLose();
        }
    }

    public void pauseGame() {
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGamePause();
        }
    }

    public void prepareGame() {
        if (!isActive) {
            randomCountry = getRandom();
            Country tempC = countries.get(randomCountry);
            for (GameEventListener gameEventListener : listeners) {
                gameEventListener.onGamePrepare(tempC, continent, round);
            }
            setActive(true);
        }
    }

    public void quitGame() {
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGameQuit();
        }
    }

    public void resumeGame() {
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGameResume();
        }
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void startGame() {
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGameStart();
        }
    }

    public void winGame() {
        round++;
        for (GameEventListener gameEventListener : listeners) {
            gameEventListener.onGameWin();
        }
    }

    private int getRandom() {
        Random r = new Random();
        int temp = Math.abs(r.nextInt()) % countries.size();

        //ensure we get different countries
        return temp != randomCountry ? temp : getRandom();
    }

    private void initialize() {
        loadCountries();
    }

    private void loadCountries() {
        XmlResourceParser xml = null;
        switch (continent) {
            case Continent.ASIA:
                xml = resource.getXml(R.xml.asia);
                break;
            case Continent.AFRICA:
                xml = resource.getXml(R.xml.africa);
                break;
            case Continent.EUROPE:
                xml = resource.getXml(R.xml.europe);
                break;
            case Continent.AMERICA:
                xml = resource.getXml(R.xml.america);
                break;
        }

        try {
            int eventType = xml.getEventType();
            Country c = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:
                        countries = new ArrayList<Country>();
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = xml.getName();
                        if (tag.equalsIgnoreCase("country")) {
                            c = new Country();
                            c.setName(xml.getAttributeValue(null, "name"));
                        } else if (c != null) {
                            if (tag.equalsIgnoreCase("src")) {
                                c.setResID(xml.nextText());
                            } else if (tag.equalsIgnoreCase("tar")) {
                                c.setTarPosX(Integer.parseInt(xml.getAttributeValue(null, "posX")));
                                c.setTarPosY(Integer.parseInt(xml.getAttributeValue(null, "posY")));
                                c.setTarWidth(Integer.parseInt(xml.getAttributeValue(null, "width")));
                                c.setTarHeight(Integer.parseInt(xml.getAttributeValue(null, "height")));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xml.getName().equalsIgnoreCase("country") && c != null) {
                            countries.add(c);
                            c = null;
                        }
                        break;
                    default:
                        break;
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
