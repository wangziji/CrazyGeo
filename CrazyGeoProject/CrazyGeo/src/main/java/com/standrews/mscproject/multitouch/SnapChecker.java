/*
 * SnapChecker.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.multitouch;

import android.graphics.PointF;
import android.util.FloatMath;

import com.standrews.mscproject.custom_ui_componets.GameSurfaceView;
import com.standrews.mscproject.game.BitmapResource;
import com.standrews.mscproject.game.Country;

/**
 * MSc project
 * <p/>
 * This class responsible for check whether the object could match the area.
 *
 * Created by Ziji Wang on 13-7-13.
 */
public class SnapChecker {


    public SnapChecker() {
    }

    /**
     * Change the value of isFitted for the GameSurfaceView when all the condition been satisfied
     * @param view GameSurfaceView
     */
    public void snapInto(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        if (locationMatch(view) && sizeMatch(view) && rotationMatch(view)) {
            float targetX = country.getTarPosX() * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor();
            float targetY = country.getTarPosY() * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor();
            float height = country.getTarHeight() * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor();
            src.reset(targetX + tar.getLocation().x, targetY + tar.getLocation().y, height);
            view.setFitted(true);

        }

    }

    /**
     * Calculate the distance
     * @param p1 PointF, p1
     * @param p2 PointF, p2
     * @return float, the distance
     */
    private float getDistance(PointF p1, PointF p2) {
        return Math.abs(FloatMath.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }

    /**
     * Check the location
     * @param view GameSurfaceView
     * @return boolean, true = match, false = not match
     */
    private boolean locationMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        float targetX = country.getTarPosX();
        float targetY = country.getTarPosY();
        float tX = targetX * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor() + tar.getLocation().x;
        float tY = targetY * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor() + tar.getLocation().y;
        PointF target = new PointF(tX, tY);
        PointF current = src.getLocation();
        return getDistance(target, current) < 25 * tar.getSeekBarValue();
    }

    /**
     * Check the rotation degree
     * @param view GameSurfaceView
     * @return boolean, true = match, false = not match
     */
    private boolean rotationMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        return Math.abs(src.getDegree()) < 15 || Math.abs(Math.abs(src.getDegree()) - 360) < 15;
    }

    /**
     * Check the size
     * @param view GameSurfaceView
     * @return boolean, true = match, false = not match
     */
    private boolean sizeMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        float[] size = src.getSize();
        float width = country.getTarWidth() * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor();
        float height = country.getTarHeight() * tar.getScaleFactor() * tar.getSeekBarValue() * view.getMapScaleFactor();
        return Math.abs(size[0] - width) < 25 * tar.getSeekBarValue() && Math.abs(size[1] - height) < 25 * tar.getSeekBarValue();
    }
}
