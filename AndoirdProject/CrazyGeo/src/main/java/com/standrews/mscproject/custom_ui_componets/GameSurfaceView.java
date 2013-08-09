/*
 * GameSurfaceView.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

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
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, GameEventListener, GameStateReporter {

    private SurfaceHolder holder;
    private BitmapResource src, tar;
    private Boolean isFitted = false, isPaused = true, isFirstTime = true;
    private MultitouchHandler2 mHandler;
    private Country country;
    private Bitmap background;
    private DisplayManager dm;
    private String packName;
    private GameStateMonitor gameStateMonitor;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        holder = this.getHolder();
        if (holder != null) {
            holder.addCallback(this);
        }
        mHandler = new MultitouchHandler2(context, this);
        dm = new DisplayManager();

    }

    public void setBackgroundBitmap() {
        Configuration configuration = new Configuration();
        int dpi = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("DPI"));
        int sampleSize = 1;
        if (dpi > 160 && dpi <= 240) {
            int tempA = 1920 / getWidth();
            int tempB = 1200 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        } else if (dpi > 240) {
            int tempA = 2560 / getWidth();
            int tempB = 1600 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        } else if (dpi <= 160) {
            int tempA = 1280 / getWidth();
            int tempB = 800 / getHeight();
            sampleSize = tempA < tempB ? tempA : tempB;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.big_bk);
        float sw = (float) getWidth() / (float) background.getWidth();
        float sh = (float) getHeight() / (float) background.getHeight();
        float sf = sw > sh ? sw : sh;
        int w = (int) (background.getWidth() * sf);
        int h = (int) (background.getHeight() * sf);
        background = Bitmap.createScaledBitmap(background, w, h, true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setBackgroundBitmap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        background.recycle();
        src.recycle();
        tar.recycle();
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setFitted(Boolean fitted) {
        isFitted = fitted;
    }

    private void drawOnPaused(){
        Canvas canvas = holder.lockCanvas(null);
        if (canvas != null) {
            canvas.drawBitmap(background, 0, 0, null);
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
            canvas.drawARGB(200,10,10,10);
        }

        holder.unlockCanvasAndPost(canvas);
        this.invalidate();
    }

    public void rePaint() {
        Canvas canvas = holder.lockCanvas(null);
        if (canvas != null) {
            canvas.drawBitmap(background, 0, 0, null);
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
            reportStateChanged(GameStateMonitor.GAME_WIN);
        }
    }

    public BitmapResource getCurrentBitmap() {
        return src;
    }

    public BitmapResource getTargetBitmap() {
        return tar;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!isPaused) {
            return mHandler.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setTarget(int id) {
        float minSize = dm.DIPtoPixel(100, getContext());
        tar = new BitmapResource(getResources(), id, minSize);
        isFirstTime = false;
    }

    public void resetTarget() {
        tar.reset(0, 0, getWidth(), getHeight());
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

    public void setSource() {
        if (src != null) {
            src.recycle();
        }
        float minSize = dm.DIPtoPixel(20, getContext());
        src = new BitmapResource(getResources(), findResourceID(country.getResID()), minSize);
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
    }

    private int findResourceID(String name) {
        return getResources().getIdentifier(name, "drawable", packName);
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        if (isFirstTime) {
            switch (continent) {
                case Continent.ASIA:
                    setTarget(R.drawable.asia);
                    break;
                //TODO other continent
            }
            isFirstTime = false;
        }
        setCountry(country);
        setSource();
        resetSource();
        resetTarget();
        isFitted = false;
        rePaint();
    }

    @Override
    public void onGameStart() {

        isPaused = false;
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
        onGamePause();
        Log.i("me","win");
        Toast.makeText(getContext(),country.getName().toUpperCase(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameLose() {
        onGamePause();
    }

    @Override
    public void onGameQuit() {

    }

    @Override
    public void setMonitor(GameStateMonitor gameStateMonitor) {
        this.gameStateMonitor = gameStateMonitor;
    }

    @Override
    public void reportStateChanged(int state) {
        gameStateMonitor.onStateChange(state);
    }
}
