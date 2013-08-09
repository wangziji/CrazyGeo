/*
 * ScoreTextView.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.standrews.mscproject.game.Continent;
import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.utils.Configuration;

import java.util.Properties;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-14.
 */
public class ScoreTextView extends TextView implements GameEventListener {

    private Configuration configuration;
    private int highest = -1, continent, score;

    public ScoreTextView(Context context) {
        super(context);
        configuration = new Configuration();
    }

    public ScoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configuration = new Configuration();
    }

    public ScoreTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        configuration = new Configuration();
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        score = round - 1;
        if (round == 1) {
            this.continent = continent;
            setTextColor(Color.rgb(26, 26, 26));
            switch (continent) {
                case Continent.ASIA:
                    highest = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("ASIA_BEST"));
                    break;
                case Continent.EUROPE:
                    highest = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("EUROPE_BEST"));
                    break;
                case Continent.AFRICA:
                    highest = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("AFRICA_BEST"));
                    break;
                case Continent.AMERICA:
                    highest = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("AMERICA_BEST"));
                    break;
            }
        }
        if (score > highest) {
            setTextColor(Color.rgb(226, 81, 28));
            String text = "Best Score: " + score;
            setText(text);
        } else {
            String text = "Round: " + round;
            setText(text);
        }

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

    }

    @Override
    public void onGameLose() {
        if (score > highest) {
            Properties config = configuration.getConfigProperties(getContext());
            switch (continent) {
                case Continent.ASIA:
                    config.setProperty("ASIA_BEST", score + "");
                    break;
                case Continent.EUROPE:
                    config.setProperty("EUROPE_BEST", score + "");
                    break;
                case Continent.AFRICA:
                    config.setProperty("AFRICA_BEST", score + "");
                    break;
                case Continent.AMERICA:
                    config.setProperty("AMERICA_BEST", score + "");
                    break;
            }
            configuration.saveConfigProperties(getContext(), config);
        }
    }

    @Override
    public void onGameQuit() {
        if (score > highest) {
            Properties config = configuration.getConfigProperties(getContext());
            switch (continent) {
                case Continent.ASIA:
                    config.setProperty("ASIA_BEST", score + "");
                    break;
                case Continent.EUROPE:
                    config.setProperty("EUROPE_BEST", score + "");
                    break;
                case Continent.AFRICA:
                    config.setProperty("AFRICA_BEST", score + "");
                    break;
                case Continent.AMERICA:
                    config.setProperty("AMERICA_BEST", score + "");
                    break;
            }
            configuration.saveConfigProperties(getContext(), config);
        }
        this.destroyDrawingCache();

    }
}
