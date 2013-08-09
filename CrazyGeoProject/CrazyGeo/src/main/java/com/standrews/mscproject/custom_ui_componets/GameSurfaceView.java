/*
 * GameSurfaceView.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.standrews.mscproject.game.BitmapResource;
import com.standrews.mscproject.game.Continent;
import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;
import com.standrews.mscproject.game.GameStateMonitor;
import com.standrews.mscproject.game.GameStateReporter;
import com.standrews.mscproject.main.R;
import com.standrews.mscproject.multitouch.MultitouchHandler2;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

import java.util.Random;

/**
 * MSc project
 * <p/>
 * Created by ziji on 13-6-25.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, GameEventListener, GameStateReporter, SeekBar.OnSeekBarChangeListener, SoundPool.OnLoadCompleteListener {

    private SurfaceHolder holder;
    private BitmapResource src, tar;
    private Boolean isFitted = false, isPaused = true, isFirstTime = true, soundLoaded = false;
    private MultitouchHandler2 mHandler;
    private Country country;
    private DisplayManager dm;
    private String packName;
    private GameStateMonitor gameStateMonitor;
    private SoundPool soundPool;
    private int soundID, sound;
    private float mapScaleFactor, baseline;
//    private Integer baseline;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        holder = this.getHolder();
        if (holder != null) {
            holder.addCallback(this);
        }
        mHandler = new MultitouchHandler2(context, this);
        dm = new DisplayManager();

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        soundID = soundPool.load(getContext(), R.raw.clip, 1);

        Configuration configuration = new Configuration();
        sound = Integer.parseInt(configuration.getConfigProperties(context).getProperty("SOUND"));
    }

    public void drawOnPaused() {
        Canvas canvas = holder.lockCanvas(null);
        if (canvas != null) {
            canvas.drawARGB(255, 255, 250, 205);
            src.prepareToDraw();
            tar.prepareToDraw();
            canvas.drawBitmap(tar.getBitmap(), tar.getMatrix(), null);
            if (!isFitted) {
                Paint noAlpha = new Paint();
                noAlpha.setStyle(Paint.Style.STROKE);
                noAlpha.setAlpha(200);
                canvas.drawBitmap(src.getBitmap(), src.getMatrix(), noAlpha);
            } else {
                canvas.drawBitmap(src.getBitmap(), src.getMatrix(), null);

            }
            canvas.drawARGB(200, 10, 10, 10);
        }

        holder.unlockCanvasAndPost(canvas);
        this.invalidate();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BitmapResource getCurrentBitmap() {
        return src;
    }

    public float getMapScaleFactor() {
        return mapScaleFactor;
    }

    public BitmapResource getTargetBitmap() {
        return tar;
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        if (isFirstTime) {
            switch (continent) {
                case Continent.ASIA:
                    baseline = getResources().getInteger(R.integer.asia);
                    setTarget(R.drawable.asia);
                    break;
                case Continent.AFRICA:
                    baseline = getResources().getInteger(R.integer.africa);
                    setTarget(R.drawable.africa);
                    break;
                case Continent.EUROPE:
                    baseline = getResources().getInteger(R.integer.europe);
                    setTarget(R.drawable.europe);
                    break;
                case Continent.AMERICA:
                    baseline = getResources().getInteger(R.integer.america);
                    setTarget(R.drawable.america);
                    break;
            }
            isFirstTime = false;
        }
        setCountry(country);
        setSource();
        resetSource();
        resetTarget();
        isFitted = false;
        mHandler.loggingHeader();
        mHandler.loggingObjects();
        drawOnPaused();
    }

    @Override
    public void onGameStart() {
        isPaused = false;
        rePaint();
    }

    @Override
    public void onGamePause() {
        drawOnPaused();
        isPaused = true;
    }

    @Override
    public void onGameResume() {
        rePaint();
        isPaused = false;
    }

    @Override
    public void onGameWin() {
        mHandler.loggingEnd();
        onGamePause();
    }

    @Override
    public void onGameLose() {
        mHandler.loggingEnd();
        isPaused = true;
        tar.saveState();
        float targetX = country.getTarPosX() * tar.getScaleFactor() * tar.getSeekBarValue() * mapScaleFactor;
        float targetY = country.getTarPosY() * tar.getScaleFactor() * tar.getSeekBarValue() * mapScaleFactor;
        float height = country.getTarHeight() * tar.getScaleFactor() * tar.getSeekBarValue() * mapScaleFactor;
        src.reset(targetX + tar.getLocation().x, targetY + tar.getLocation().y, height);
        rePaint();
        onGamePause();
    }

    @Override
    public void onGameQuit() {
        mHandler.loggingEnd();
        soundPool.release();
        src.recycle();
        tar.recycle();
        destroyDrawingCache();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
        soundLoaded = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mHandler.onProgressChanged(seekBar, i, b);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.onStopTrackingTouch(seekBar);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!isPaused) {
            return mHandler.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void rePaint() {
        Canvas canvas = holder.lockCanvas(null);
        if (canvas != null) {
            canvas.drawARGB(255, 255, 250, 205);
            src.prepareToDraw();
            tar.prepareToDraw();
            canvas.drawBitmap(tar.getBitmap(), tar.getMatrix(), null);
            if (!isFitted) {
                Paint noAlpha = new Paint();
                noAlpha.setStyle(Paint.Style.STROKE);
                noAlpha.setAlpha(200);
                canvas.drawBitmap(src.getBitmap(), src.getMatrix(), noAlpha);

            } else {
                canvas.drawBitmap(src.getBitmap(), src.getMatrix(), null);

            }
        }

        holder.unlockCanvasAndPost(canvas);
        this.invalidate();
        if (isFitted) {
            isPaused = true;
            if (soundLoaded && sound == 0) {
                soundPool.play(soundID, 1, 1, 0, 0, 1);
            }
            reportStateChanged(GameStateMonitor.GAME_WIN);
        }
    }

    public void resetSource() {
        //give the source image a random rotation
        Random r = new Random();
        int i = r.nextInt() % 179;
        float degree = r.nextFloat() + i;
        src.setDegree(degree);

        //set the source image in the middle
        float w = this.getWidth();
        float h = this.getHeight();
        src.reset(w / 3, h / 3, w / 3, h / 3);
        src.saveState();
    }

    public void resetTarget() {
        tar.reset(0, 0, getWidth(), getHeight());
        tar.setSeekBarValue(1);
        float[] size = tar.getSize();
        float dw = getWidth() - size[0];
        float dh = getHeight() - size[1];
        if (dw >= dh) {
            tar.setLocation(dw / 2, 0);
        } else {
            tar.setLocation(0, dh / 2);
        }
        tar.saveState();
    }

    public void setFitted(Boolean fitted) {
        isFitted = fitted;
    }

    @Override
    public void setMonitor(GameStateMonitor gameStateMonitor) {
        this.gameStateMonitor = gameStateMonitor;
    }

    @Override
    public void reportStateChanged(int state) {
        gameStateMonitor.onStateChange(state);
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setSource() {
        if (src != null) {
            src.recycle();
        }
        float minSize = dm.DIPtoPixel(10, getContext());
        src = new BitmapResource(getResources(), findResourceID(country.getResID()), minSize);
    }

    public void setTarget(int id) {
        float minSize = dm.DIPtoPixel(100, getContext());
        tar = new BitmapResource(getResources(), id, minSize, getWidth(), getHeight());
        mapScaleFactor = tar.getBitmap().getWidth() / baseline;
        isFirstTime = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private int findResourceID(String name) {
        return getResources().getIdentifier(name, "drawable", packName);
    }
}
