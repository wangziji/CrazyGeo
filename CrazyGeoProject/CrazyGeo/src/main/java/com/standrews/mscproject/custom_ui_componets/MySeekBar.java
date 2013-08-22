/*
 * MySeekBar.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.standrews.mscproject.game.Country;
import com.standrews.mscproject.game.GameEventListener;

/**
 * MSc project
 * <p/>
 * This class defined the seek bar for the game activity.
 * This seek bar responsible for adjust the size of map
 *
 * Created by Ziji Wang on 13-7-14.
 */
public class MySeekBar extends SeekBar implements GameEventListener {

    public MySeekBar(Context context) {
        super(context);
    }

    /**
     * Constructor, inherited from android.widget.SeekBar
     * @param context context
     * @param attrs AttributeSet
     */
    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(h, w, oldHeight, oldWidth);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public void onGamePrepare(Country country, int continent, int round) {
        this.setProgress(50);
        this.setEnabled(false);
    }

    @Override
    public void onGameStart() {
        setEnabled(true);
    }

    @Override
    public void onGamePause() {
        setEnabled(false);
    }

    @Override
    public void onGameResume() {
        setEnabled(true);
    }

    @Override
    public void onGameWin() {
        setEnabled(false);
    }

    @Override
    public void onGameLose() {
        setEnabled(false);
    }

    @Override
    public void onGameQuit() {
        destroyDrawingCache();
    }
}
