/*
 * Snaping.java
 *
 * Created on: 13 /7 /2013
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
import com.standrews.mscproject.utils.DisplayManager;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-13.
 */
public class SnapChecker {

    private DisplayManager dm;

    public SnapChecker() {
        dm = new DisplayManager();
    }

    public void snapInto(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        if (locationMatch(view) && sizeMatch(view) && rotationMatch(view)) {
            float targetX = dm.DIPtoPixel(country.getTarPosX(), view.getContext()) * tar.getScaleFactor();
            float targetY = dm.DIPtoPixel(country.getTarPosY(), view.getContext()) * tar.getScaleFactor();
            float height = dm.DIPtoPixel(country.getTarHeight(), view.getContext()) * tar.getScaleFactor();
            src.reset(targetX + tar.getLocation().x, targetY + tar.getLocation().y, height);
            view.setFitted(true);

        }

    }

    private boolean rotationMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        return Math.abs(src.getDegree()) < 10 || Math.abs(Math.abs(src.getDegree()) - 360) < 10;
    }

    private boolean sizeMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        float[] size = src.getSize();
        float width = dm.DIPtoPixel(country.getTarWidth(), view.getContext()) * tar.getScaleFactor();
        float height = dm.DIPtoPixel(country.getTarHeight(), view.getContext()) * tar.getScaleFactor();
        return Math.abs(size[0] - width) < 10 && Math.abs(size[1] - height) < 10;
    }

    private boolean locationMatch(GameSurfaceView view) {
        BitmapResource src = view.getCurrentBitmap();
        BitmapResource tar = view.getTargetBitmap();
        Country country = view.getCountry();
        float targetX = dm.DIPtoPixel(country.getTarPosX(), view.getContext());
        float targetY = dm.DIPtoPixel(country.getTarPosY(), view.getContext());
        float tX = targetX * tar.getScaleFactor() + tar.getLocation().x;
        float tY = targetY * tar.getScaleFactor() + tar.getLocation().y;
        PointF target = new PointF(tX, tY);
        PointF current = src.getLocation();
        return getDistance(target, current) < 10;
    }

    private float getDistance(PointF p1, PointF p2) {
        return Math.abs(FloatMath.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }
}
