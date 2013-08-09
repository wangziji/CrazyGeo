/*
 * MultitouchListener.java
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ziji on 13-6-9.
 */
public class MultitouchListener implements View.OnTouchListener {

    private Context context;

    private static final int NONE = 0;
    private static final int MOVE = 1;
    private static final int ZOOM = 2;


    private int mode = NONE;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();

    private float oldDistance;
    private float oldRotation;

//    private ScaleGestureDetector sgd;

    /*
    * DELETE!
    */
    public MultitouchListener(Context context) {
        this.context = context;
//        sgd = new ScaleGestureDetector(context, new ScaleListener());
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {


        ImageView imageView = (ImageView) view;
//        MyGestureDetector gd = new MyGestureDetector(imageView,event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = MOVE;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                gd.initialize();
                oldDistance = spacing(event);
                oldRotation = rotation(event);
                if (oldDistance > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                gd.respones();
                //mode=actionAnalysis(event);
                if (mode == MOVE) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
//                    gd.respones();
                    float newDist = spacing(event);
                    float newRotation = rotation(event) - oldRotation;
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);

                        float scale = newDist / oldDistance;
                        matrix.postScale(scale, scale, imageView.getScaleX(), imageView.getScaleY());
                        if (Math.abs(newRotation) > 10f) {
                            matrix.postRotate(newRotation, mid.x, mid.y);
                        }

                    }


                }
                break;
        }

        imageView.setImageMatrix(matrix);


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

//    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//
//
//
//            return true;
//        }
//
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
//            return true;
//        }
//
//        @Override
//        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
//
//        }
//    }
}
