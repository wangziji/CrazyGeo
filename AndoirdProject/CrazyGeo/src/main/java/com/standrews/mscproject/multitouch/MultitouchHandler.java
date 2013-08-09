/*
 * MultitouchHandler.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.multitouch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.standrews.mscproject.logging.Logger;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-18.
 */
public class MultitouchHandler {

    private GestureAnalyser ga;
    private Matrix matrix, savedMatrix;
    private ImageView mImageView;
    private PointF start;
    private float oldDistance;
    private float oldRotation;
    private PointF mid;
    private Logger mLogger;
    private StringBuilder sb;

    public MultitouchHandler(Context context, ImageView imageView) {
        mLogger = new Logger(context);
        ga = new GestureAnalyser();
        mImageView = imageView;
        savedMatrix = new Matrix();
        matrix = new Matrix();
        start = new PointF();
        mid = new PointF();
        mLogger.writeHeader();
        sb = new StringBuilder();
    }

    public boolean onTouchEvent(MotionEvent event) {
        ga.eventUpdate(event);
        int state = ga.getAction();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                sb = new StringBuilder();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = spacing(event);
                oldRotation = rotation(event);
                savedMatrix.set(matrix);
                sb = new StringBuilder();
                midPoint(mid, event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (state == GestureAnalyser.ACTION_MOVE) {
                    matrix.set(savedMatrix);
                    float offsetX = event.getX() - start.x;
                    float offsetY = event.getY() - start.y;
                    matrix.postTranslate(offsetX, offsetY);
                    sb.append("\t\t<movement type=\"move\" time=\"").append(System.currentTimeMillis()).append("\">\n");
                    sb.append("\t\t\t<pointer1 x=\"").append(event.getX()).append("\" y=\"").append(event.getY()).append("\"/>\n");
                    sb.append("\t\t</movement>\n");
                } else if (state == GestureAnalyser.ACTION_ROTATE) {
                    float newRotation = rotation(event) - oldRotation;
                    midPoint(mid, event);
                    matrix.set(savedMatrix);
                    matrix.postRotate(newRotation, mid.x, mid.y);
                    sb.append("\t\t<movement type=\"rotate\" time=\"").append(System.currentTimeMillis()).append("\">\n");
                    sb.append("\t\t\t<pointer1 x=\"").append(event.getX(0)).append("\" y=\"").append(event.getY(0)).append("\"/>\n");
                    sb.append("\t\t\t<pointer2 x=\"").append(event.getX(1)).append("\" y=\"").append(event.getY(1)).append("\"/>\n");
                    sb.append("\t\t</movement>\n");
                    Log.i("gesture", "rotate");
                } else if (state == GestureAnalyser.ACTION_SCALE) {
                    float newDist = spacing(event);
                    midPoint(mid, event);
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDistance;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    sb.append("\t\t<movement type=\"scale\" time=\"").append(System.currentTimeMillis()).append("\">\n");
                    sb.append("\t\t\t<pointer1 x=\"").append(event.getX(0)).append("\" y=\"").append(event.getY(0)).append("\"/>\n");
                    sb.append("\t\t\t<pointer2 x=\"").append(event.getX(1)).append("\" y=\"").append(event.getY(1)).append("\"/>\n");
                    sb.append("\t\t</movement>\n");
                    Log.i("gesture", "scale");
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mLogger.writeMovement(sb.toString());
                break;
            case MotionEvent.ACTION_UP:
                mLogger.writeMovement(sb.toString());
                break;
        }

        mImageView.setImageMatrix(matrix);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        float delta_x = (event.getX(0) - event.getX(1));
        float delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    public void getLocation(ImageView tar) {
        mLogger.writeShapes(mImageView, tar);
    }

    public void onDestroy() {
        mLogger.writeEnd();
    }
}
