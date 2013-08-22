/*
 * BitmapResource.java
 *
 * Created on: 22 /8 /2013
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

import com.standrews.mscproject.utils.bitmap_decoder.BitmapDecoder;

/**
 * MSc project
 * <p/>
 * This class represent the manipulable object, like the country and the continent map.
 *
 * Created by Ziji Wang on 13-6-26.
 */
public class BitmapResource {

    private Bitmap bitmap;
    private PointF center, location;
    private Matrix matrix, testMatrix;
    private float scaleFactor = 1, degree = 0;
    private float offsetX = 0, offsetY = 0, tempScaleFactor = 1, tempDegree = 0, seekBarValue = 1f;
    private float minSize;

    /**
     * Constructor
     * @param resources Resources
     * @param id int, ID of drawable resource
     * @param minSize float, the minimum size
     */
    public BitmapResource(Resources resources, int id, float minSize) {
        bitmap = BitmapFactory.decodeResource(resources, id);
        center = new PointF();
        location = new PointF(0, 0);
        matrix = new Matrix();
        testMatrix = new Matrix();
        this.minSize = minSize;
    }

    /**
     * Constructor
     * @param resources Resources
     * @param id int, ID of drawable resource
     * @param minSize float, the minimum size
     * @param width int, target width for resize
     * @param height int, target height for resize
     */
    public BitmapResource(Resources resources, int id, float minSize, int width, int height) {
        bitmap = BitmapDecoder.decodeSampledBitmapFromResource(resources, id, width, height);
        float wsf = (float) width / (float) bitmap.getWidth();
        float hsf = (float) height / (float) bitmap.getHeight();
        float sf = wsf < hsf ? wsf : hsf;
        bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * sf), Math.round(bitmap.getHeight() * sf), true);
        center = new PointF();
        location = new PointF(0, 0);
        matrix = new Matrix();
        testMatrix = new Matrix();
        this.minSize = minSize;
    }

    /**
     * Get the bitmap
     * @return Bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Get the center point
     * @return PointF, the center point
     */
    public PointF getCenter() {
        float x = bitmap.getWidth() * scaleFactor * tempScaleFactor / 2 + (location.x + offsetX);
        float y = bitmap.getHeight() * scaleFactor * tempScaleFactor / 2 + (location.y + offsetY);
        center.set(x, y);
        return center;
    }

    /**
     * Get current degree
     * @return float, degree
     */
    public float getDegree() {
        return degree;
    }

    /**
     * Set the rotation degree
     * @param degree float, rotation degree
     */
    public void setDegree(float degree) {
        this.tempDegree = degree;
    }

    /**
     * Get current location
     * @return PointF, current location
     */
    public PointF getLocation() {
        return location;
    }

    /**
     * Get the matrix
     * @return Matrix
     */
    public Matrix getMatrix() {
        return matrix;
    }

    /**
     * Get current scale factor
     * @return float, scale factor
     */
    public float getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Set the scale factor
     * @param scaleFactor float scale factor
     */
    public void setScaleFactor(float scaleFactor) {
        this.tempScaleFactor = scaleFactor;
    }

    /**
     * Get the seek bar value
     * @return float, seek bar value
     */
    public float getSeekBarValue() {
        return seekBarValue;
    }

    /**
     * Set the seek bar value
     * @param seekBarValue float, seek bar value
     */
    public void setSeekBarValue(float seekBarValue) {
        this.seekBarValue = seekBarValue;
    }

    /**
     * Get current size
     * @return float[], [0]=width, [1]=height
     */
    public float[] getSize() {
        float[] size = new float[2];
        size[0] = bitmap.getWidth() * scaleFactor * tempScaleFactor;
        size[1] = bitmap.getHeight() * scaleFactor * tempScaleFactor;
        return size;
    }

    /**
     * Get test matrix
     * @return Matrix
     */
    public Matrix getTestMatrix() {
        return testMatrix;
    }

    /**
     * Set the affine transformation for the matrix
     */
    public void prepareToDraw() {
        bitmap.prepareToDraw();
        getCenter();
        matrix.reset();
        matrix.postTranslate(location.x + offsetX, location.y + offsetY);
        float sf = tempScaleFactor * scaleFactor * seekBarValue;
        if (sf * bitmap.getWidth() < minSize) {
            sf = minSize / bitmap.getWidth();
            scaleFactor = sf;
            tempScaleFactor = 1;
        }
        matrix.postScale(sf, sf, location.x + offsetX, location.y + offsetY);
        matrix.postRotate(degree + tempDegree, center.x, center.y);
    }

    /**
     * Recycle the bitmap
     */
    public void recycle() {
        bitmap.recycle();
    }

    /**
     * Reset to target location
     * @param x float, X for target location
     * @param y float, Y for target location
     * @param height float, height for target size
     */
    public void reset(float x, float y, float height) {
        float h = bitmap.getHeight();
        offsetX = offsetY = 0;
        tempScaleFactor = 1f;
        tempDegree = 0;
        this.location = new PointF(x, y);
        this.scaleFactor = height / h;
        this.degree = 0;
    }

    /**
     * Reset to target location
     * @param x float, X for target location
     * @param y float, Y for target location
     * @param width float, width for target size
     * @param height float, height for target size
     */
    public void reset(float x, float y, float width, float height) {
        this.location = new PointF(x, y);
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        float sw = width / w;
        float sh = height / h;
        this.scaleFactor = sw < sh ? sw : sh;
        this.degree = 0;
    }

    /**
     * Update the location, scale factor and rotation degree
     */
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

    /**
     * Set location
     * @param offsetX float, X
     * @param offsetY float, Y
     */
    public void setLocation(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Simulate the  affine transformation using test matrix
     */
    public void simulation() {
        getCenter();
        testMatrix.reset();
        testMatrix.postTranslate(location.x + offsetX, location.y + offsetY);
        testMatrix.postScale(tempScaleFactor * scaleFactor * seekBarValue, tempScaleFactor * scaleFactor * seekBarValue, location.x + offsetX, location.y + offsetY);
        testMatrix.postRotate(degree + tempDegree, center.x, center.y);
    }
}
