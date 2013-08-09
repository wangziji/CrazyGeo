/*
 * BitmapResource.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-26.
 */
public class BitmapResource {

    private Bitmap bitmap;
    private PointF center, location;
    private Matrix matrix, testMatrix;
    private float scaleFactor = 1, degree = 0;
    private float offsetX = 0, offsetY = 0, tempScaleFactor = 1, tempDegree = 0;
    private float minSize;

    public BitmapResource(Resources resources, int id, float minSize) {
        bitmap = BitmapFactory.decodeResource(resources, id);
        center = new PointF();
        location = new PointF(0, 0);
        matrix = new Matrix();
        testMatrix = new Matrix();
        this.minSize = minSize;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public PointF getCenter() {
        float x = bitmap.getWidth() * scaleFactor * tempScaleFactor / 2 + (location.x + offsetX);
        float y = bitmap.getHeight() * scaleFactor * tempScaleFactor / 2 + (location.y + offsetY);
        center.set(x, y);
        return center;
    }

    public PointF getLocation() {
        return location;
    }

    public float[] getSize() {
        float[] size = new float[2];
        size[0] = bitmap.getWidth() * scaleFactor * tempScaleFactor;
        size[1] = bitmap.getHeight() * scaleFactor * tempScaleFactor;
        return size;
    }

    public float getDegree() {
        return degree;
    }


    public Matrix getMatrix() {
        return matrix;
    }

    public Matrix getTestMatrix() {
        return testMatrix;
    }

    public void setDegree(float degree) {
        this.tempDegree = degree;
    }

    public void setScaleFactor(float scaleFactor) {
        this.tempScaleFactor = scaleFactor;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setLocation(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void prepareToDraw() {
        bitmap.prepareToDraw();
        getCenter();
        matrix.reset();
        matrix.postTranslate(location.x + offsetX, location.y + offsetY);
        float sf = tempScaleFactor * scaleFactor;
        if (sf * bitmap.getWidth() < minSize) {
            sf = minSize / bitmap.getWidth();
            scaleFactor = sf;
            tempScaleFactor = 1;
        }
        matrix.postScale(sf, sf, location.x + offsetX, location.y + offsetY);
        matrix.postRotate(degree + tempDegree, center.x, center.y);
    }

    public void simulation() {
        getCenter();
        testMatrix.reset();
        testMatrix.postTranslate(location.x + offsetX, location.y + offsetY);
        testMatrix.postScale(tempScaleFactor * scaleFactor, tempScaleFactor * scaleFactor, location.x + offsetX, location.y + offsetY);
        testMatrix.postRotate(degree + tempDegree, center.x, center.y);
    }

    public void saveState() {
        location.set(location.x + offsetX, location.y + offsetY);
        scaleFactor = scaleFactor * tempScaleFactor;
        degree = degree + tempDegree;
        if (degree > 360) {
            degree = degree - 360;
        } else if (degree < -360) {
            degree = degree + 360;
        }
        offsetX = offsetY = tempDegree = 0;
        tempScaleFactor = 1;
        getCenter();
    }

    public void reset(float targetX, float targetY, float height) {
        float h = bitmap.getHeight();
        this.location = new PointF(targetX, targetY);
        this.scaleFactor = height / h;
        this.degree = 0;
    }

    public void reset(float x, float y, float width, float height) {
        this.location = new PointF(x, y);
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        float sw = width / w;
        float sh = height / h;
        this.scaleFactor = sw < sh ? sw : sh;
        this.degree = 0;
    }

    public void recycle() {
        bitmap.recycle();
    }
}
