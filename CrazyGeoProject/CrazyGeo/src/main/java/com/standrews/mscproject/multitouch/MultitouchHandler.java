/*
 * MultitouchHandler.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.multitouch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.standrews.mscproject.custom_ui_componets.GameSurfaceView;
import com.standrews.mscproject.game.BitmapResource;
import com.standrews.mscproject.logging.Logger;
import com.standrews.mscproject.utils.Configuration;


/**
 * MSc project
 * <p/>
 * This class is intent to handel the multi touch events, it is the control layer in MVC model.
 * This layer connect the display layer "GameSurfaceView" and the logic layer "BitmapResource"
 * <p/>
 * Created by Ziji Wang on 13-6-26.
 */
public class MultitouchHandler {

    private GameSurfaceView surfaceView;
    private int action = 0;//0 = none, 1 = move, 2 = scale or rotate, 3 = move target
    private float oldDistance, oldDegree, pre_scale, pre_degree, pre_offsetX, pre_offsetY, pre_tar_offsetX, pre_tar_offsetY;
    private PointF start, tarStart;
    private boolean isRotate = false, isScale = false;
    private SnapChecker mSnapChecker;
    private Logger logger;
    private Handler handler;
    private Context context;
    private int permission;

    /**
     * Constructor
     *
     * @param context     Context given by GameSurfaceView
     * @param surfaceView GameSurfaceView itself
     */
    public MultitouchHandler(Context context, GameSurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
        logger = new Logger();
        start = new PointF();
        tarStart = new PointF();
        mSnapChecker = new SnapChecker();
        handler = new Handler();

        Configuration configuration = new Configuration();
        permission = Integer.parseInt(configuration.getConfigProperties(context).getProperty("USER_PERMISSION"));
    }

    /**
     * Call the logger to log the end of XML
     */
    public void loggingEnd() {
        if (permission == 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    logger.writeEnd();
                }
            });

        }
    }

    /**
     * Call the logger to log the header of XML
     */
    public void loggingHeader() {
        if (permission == 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    logger.writeHeader(context);
                }
            });
        }
    }

    /**
     * Call the logger to log the end of XML
     */
    public void loggingObjects() {
        if (permission == 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    logger.writeShapes(surfaceView.getCurrentBitmap(), surfaceView.getTargetBitmap());
                }
            });
        }
    }

    /**
     * responds the seek bar change
     * @param seekBar SeekBar
     * @param i int, seek bar value
     * @param b boolean
     */
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float value = 0.5f + i * 0.01f;
        BitmapResource tar = surfaceView.getTargetBitmap();
        tar.setSeekBarValue(value);
        surfaceView.rePaint();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * This method is the main method which handle the multitouch events
     *
     * @param event MotionEvent
     * @return True for further actions
     */
    public boolean onTouchEvent(MotionEvent event) {

        //get current BitmapResource
        BitmapResource res = surfaceView.getCurrentBitmap();
        BitmapResource tar = surfaceView.getTargetBitmap();

        //no actions will taken if more than 3 pointers been detected
        if (event.getPointerCount() < 4) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                //if only one pointer been detected, the action will be move
                case MotionEvent.ACTION_DOWN:
                    start.set(event.getX(), event.getY());
                    surfaceView.setFitted(false);
                    action = 1;
                    break;

                //if two pointers been detected, the action will be scale or rotate
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 2) {
                        surfaceView.setFitted(false);
                        action = 2;
                        res.saveState();
                        midPoint(start, event);
                        oldDegree = rotation(event);
                        oldDistance = spacing(event);

                    } else if (event.getPointerCount() == 3) {
                        action = 3;
                        res.saveState();
                        midPoint(tarStart, event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    actionReset(res, tar);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    actionReset(res, tar);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == 1) {
                        float offsetX = event.getX() - start.x;
                        float offsetY = event.getY() - start.y;
                        res.setLocation(offsetX, offsetY);

                        //check the object still in the frame of view
                        res.simulation();
                        if (!checkResBound()) {
                            res.setLocation(pre_offsetX, pre_offsetY);
                        } else {
                            pre_offsetX = offsetX;
                            pre_offsetY = offsetY;
                        }

                    } else if (action == 2) {
                        scaleMagnitudeFiltering(res, event);
                        rotateMagnitudeFiltering(res, event);

                    } else if (action == 3) {
                        PointF newMid = new PointF();
                        midPoint(newMid, event);
                        float offsetX = newMid.x - tarStart.x;
                        float offsetY = newMid.y - tarStart.y;
                        tar.setLocation(offsetX, offsetY);

                        tar.simulation();
                        if (!checkTarBound()) {
                            tar.setLocation(pre_tar_offsetX, pre_tar_offsetY);
                        } else {
                            pre_tar_offsetX = offsetX;
                            pre_tar_offsetY = offsetY;
                        }
                    }
            }
        }
        if (permission == 1) {
            logger.writeMovement(event, isRotate, isScale);
        }
        //repaint the view
        surfaceView.rePaint();
        return true;
    }

    /**
     * reset everything after pointers up
     *
     * @param res BitmapResource
     * @param tar BitmapResource
     */
    private void actionReset(BitmapResource res, BitmapResource tar) {
        action = 0;
        pre_tar_offsetX = pre_tar_offsetY = pre_offsetY = pre_offsetX = pre_degree = 0;
        pre_scale = 1;
        res.saveState();
        tar.saveState();
        isRotate = false;
        isScale = false;
        mSnapChecker.snapInto(surfaceView);
    }

    /**
     * Check the shape object still inside the view
     *
     * @return True: the object still inside the view, False: the object is outside the view
     */
    private boolean checkResBound() {
        BitmapResource res = surfaceView.getCurrentBitmap();

        float width_v = surfaceView.getWidth();
        float height_v = surfaceView.getHeight();

        float width_b = res.getBitmap().getWidth();
        float height_b = res.getBitmap().getHeight();

        Matrix temp_m = res.getTestMatrix();

        //get the four vertices of the bitmap
        float[] pts = new float[8];
        pts[0] = pts[1] = pts[3] = pts[4] = 0;
        pts[2] = pts[6] = width_b;
        pts[5] = pts[7] = height_b;

        //map the vertices using the matrix of BitmapResource
        temp_m.mapPoints(pts);

        for (int i = 0; i < 8; i = i + 2) {
            float temp = pts[i];
            if (temp < -40 || temp > width_v + 40) {
                return false;
            }
        }

        for (int i = 1; i < 8; i = i + 2) {
            float temp = pts[i];
            if (temp < -40 || temp > height_v + 40) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the map object still inside the view
     *
     * @return True: the object still inside the view, False: the object is outside the view
     */
    private boolean checkTarBound() {
        BitmapResource res = surfaceView.getTargetBitmap();

        float width_v = surfaceView.getWidth() / 2;
        float height_v = surfaceView.getHeight() / 2;

        float width_b = res.getBitmap().getWidth();
        float height_b = res.getBitmap().getHeight();

        Matrix temp_m = res.getTestMatrix();

        //get the four vertices of the bitmap
        float[] pts = new float[8];
        pts[0] = pts[1] = pts[3] = pts[4] = 0;
        pts[2] = pts[6] = width_b;
        pts[5] = pts[7] = height_b;

        //map the vertices using the matrix of BitmapResource
        temp_m.mapPoints(pts);

        return pts[0] < width_v && pts[1] < height_v && pts[2] > width_v && pts[3] < height_v && pts[4] < width_v && pts[5] > height_v && pts[6] > width_v && pts[7] > height_v;
    }

    /**
     * Calculate the middle point of fingers
     * @param point PointF, stored the result
     * @param event MotionEvent
     */
    private void midPoint(PointF point, MotionEvent event) {
        int counter = event.getPointerCount();
        float x = 0;
        float y = 0;
        for (int i = 0; i < counter; i++) {
            x = x + event.getX(i);
            y = y + event.getY(i);
        }
        point.set(x / counter, y / counter);
    }

    /**
     * Use Magnitude Filtering technique to detect rotate gesture.
     * The threshold equals 15
     *
     * @param res   BitmapResource
     * @param event MotionEvent
     */
    private void rotateMagnitudeFiltering(BitmapResource res, MotionEvent event) {
        float newDegree = rotation(event) - oldDegree;
        if (Math.abs(newDegree) < 15) {
            if (isRotate) {
                res.setDegree(newDegree);
                res.simulation();
                if (!checkResBound()) {
                    res.setDegree(pre_degree);
                } else {
                    pre_degree = newDegree;
                }
            }
        } else {
            if (!isRotate) {
                oldDegree = rotation(event);
                isRotate = true;
            } else {
                res.setDegree(newDegree);
                res.simulation();
                if (!checkResBound()) {
                    res.setDegree(pre_degree);
                } else {
                    pre_degree = newDegree;
                }
            }

        }


    }

    /**
     * Detect the degree of the vector between two pointers in the screen coordinate system
     *
     * @param event MotionEvent
     * @return The degree of rotation
     */
    private float rotation(MotionEvent event) {
        float delta_x = (event.getX(0) - event.getX(1));
        float delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * Use Magnitude Filtering technique to detect scale gesture.
     * The threshold equals 0.1
     *
     * @param res   BitmapResource
     * @param event MotionEvent
     */
    private void scaleMagnitudeFiltering(BitmapResource res, MotionEvent event) {
        float newDist = spacing(event);
        float scale = newDist / oldDistance;
        if (Math.abs(1 - scale) < 0.1) {
            if (isScale) {
                res.setScaleFactor(scale);
                res.simulation();
                if (!checkResBound()) {
                    res.setScaleFactor(pre_scale);
                } else {
                    pre_scale = scale;
                }
            }
        } else {
            if (!isScale) {
                oldDistance = spacing(event);
                isScale = true;
            } else {
                res.setScaleFactor(scale);
                res.simulation();
                if (!checkResBound()) {
                    res.setScaleFactor(pre_scale);
                } else {
                    pre_scale = scale;
                }
            }

        }


    }

    /**
     * Detect the distance between two pointers
     *
     * @param event MotionEvent
     * @return The distance
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
}
