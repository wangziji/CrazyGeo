/*
 * MusicPlayer.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * MSc project
 * <p/>
 * This class responsible for play back ground music
 *
 * Created by Ziji Wang on 13-7-16.
 */
public class MusicPlayer {

    private MediaPlayer music;
    private boolean loop = true;
    private Handler handler;

    /**
     * Constructor
     * @param resID int, resource ID
     * @param context Context
     */
    public MusicPlayer(int resID, Context context) {
        music = MediaPlayer.create(context, resID);
        handler = new Handler();
    }

    /**
     * Release the resource
     */
    public void release() {
        if (music != null) {
            music.release();
            music = null;
        }
    }

    /**
     * Start play music
     */
    public void start() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                music.setLooping(loop);
                music.start();
            }
        });
    }

    /**
     * Stop the music
     */
    public void stop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                music.stop();
            }
        });
    }
}
