/*
 * ClockImageView.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameStateReporter;
import com.standrews.mscproject.main.R;
import com.standrews.mscproject.utils.Configuration;

import java.util.ArrayList;


/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-13.
 */
public class ClockImageView extends ImageView implements GameEventListener, GameStateReporter, SoundPool.OnLoadCompleteListener {

    private ArrayList<Bitmap> clocks;
    private int delay;
    private MyHandler handler;
    private int count = 0, index = 1;
    private MyRunnable mRunnable;
    private GameStateMonitor mGameStateMonitor;
    private boolean isFirstTime = true, soundLoaded = false;
    private SoundPool soundPool;
    private int soundID_tick, soundID_lose, streamID_tick = -1, sound;

    public ClockImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new MyHandler();
        mRunnable = new MyRunnable();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        soundID_tick = soundPool.load(getContext(), R.raw.ticktock, 1);
        soundID_lose = soundPool.load(getContext(), R.raw.lose1, 1);
        Configuration configuration = new Configuration();
        sound = Integer.parseInt(configuration.getConfigProperties(context).getProperty("SOUND"));
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        if (isFirstTime) {
            loadImages();
            isFirstTime = false;
        }
        if (round <= 10) {
            delay = 10;
        } else if (round <= 20) {
            delay = 6;
        } else if (round <= 30) {
            delay = 4;
        } else if (round <= 40) {
            delay = 3;
        } else {
            delay = 2;
        }
        count = 1;
        index = 1;
        setImageBitmap(clocks.get(0));
        invalidate();
    }

    @Override
    public void onGameStart() {
        handler.post(mRunnable);
    }

    @Override
    public void onGamePause() {
        handler.removeCallbacks(mRunnable);
        soundPool.pause(streamID_tick);
    }

    @Override
    public void onGameResume() {
        handler.post(mRunnable);
        soundPool.resume(streamID_tick);
    }

    @Override
    public void onGameWin() {
        if (streamID_tick != -1 && sound == 0) {
            soundPool.stop(streamID_tick);
            streamID_tick = -1;
        }
        handler.removeCallbacks(mRunnable);
    }

    @Override
    public void onGameLose() {
        handler.removeCallbacks(mRunnable);
    }

    @Override
    public void onGameQuit() {
        destroyDrawingCache();
        soundPool.release();
        recycle();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
        soundLoaded = true;
    }

    public void recycle() {
        handler.removeCallbacks(mRunnable);
        for (Bitmap b : clocks) {
            b.recycle();
        }
    }

    @Override
    public void setMonitor(GameStateMonitor gameStateMonitor) {
        mGameStateMonitor = gameStateMonitor;
    }

    @Override
    public void reportStateChanged(int state) {
        mGameStateMonitor.obtainMessage(GameStateMonitor.MEG_STATE_CHANGE, state, 0, null).sendToTarget();
    }

    private void loadImages() {
        clocks = new ArrayList<Bitmap>();
        Bitmap click0 = BitmapFactory.decodeResource(getResources(), R.drawable.clock0);
        clocks.add(click0);
        Bitmap click1 = BitmapFactory.decodeResource(getResources(), R.drawable.clock1);
        clocks.add(click1);
        Bitmap click2 = BitmapFactory.decodeResource(getResources(), R.drawable.clock2);
        clocks.add(click2);
        Bitmap click3 = BitmapFactory.decodeResource(getResources(), R.drawable.clock3);
        clocks.add(click3);
        Bitmap click4 = BitmapFactory.decodeResource(getResources(), R.drawable.clock4);
        clocks.add(click4);
        Bitmap click5 = BitmapFactory.decodeResource(getResources(), R.drawable.clock5);
        clocks.add(click5);
        Bitmap click6 = BitmapFactory.decodeResource(getResources(), R.drawable.clock6);
        clocks.add(click6);
        Bitmap click7 = BitmapFactory.decodeResource(getResources(), R.drawable.clock7);
        clocks.add(click7);
        Bitmap click8 = BitmapFactory.decodeResource(getResources(), R.drawable.clock8);
        clocks.add(click8);
        Bitmap click9 = BitmapFactory.decodeResource(getResources(), R.drawable.clock9);
        clocks.add(click9);
        Bitmap click10 = BitmapFactory.decodeResource(getResources(), R.drawable.clock10);
        clocks.add(click10);
        Bitmap click11 = BitmapFactory.decodeResource(getResources(), R.drawable.clock11);
        clocks.add(click11);
        Bitmap click12 = BitmapFactory.decodeResource(getResources(), R.drawable.clock12);
        clocks.add(click12);

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (index < clocks.size()) {
                if (count == delay) {
                    count = 1;
                    setImageBitmap(clocks.get(index));
                    if (index == 9 && soundLoaded && sound == 0) {
                        streamID_tick = soundPool.play(soundID_tick, 1, 1, 0, -1, 1);
                    }
                    index++;
                    invalidate();
                }
            } else {
                if (soundLoaded && sound == 0) {
                    if (streamID_tick != -1) {
                        soundPool.stop(streamID_tick);
                    }
                    soundPool.play(soundID_lose, 1, 1, 0, 0, 1);
                }
                reportStateChanged(GameStateMonitor.GAME_LOSE);
            }
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            handler.obtainMessage(count).sendToTarget();
            count++;
            handler.postDelayed(mRunnable, 500);
        }
    }
}
