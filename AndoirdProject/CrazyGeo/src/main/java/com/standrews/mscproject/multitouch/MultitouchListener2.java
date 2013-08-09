/*
 * MultitouchListener2.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.multitouch;

import android.content.Context;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ziji on 13-6-17.
 */
public class MultitouchListener2 implements View.OnTouchListener {

    private ScaleGestureDetector mScaleGestureDetector;
    private Matrix matrix = new Matrix();
    private float oldDistance;
    private float newDistance;
    private ScaleListener sl;
    private Context mContext;
//    private ImageView mImageView;


    public MultitouchListener2(Context context) {
//        mImageView=view;
        mContext = context;
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        sl = new ScaleListener();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        sl.setImageView((ImageView) view);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        private ImageView mImageView;

        public void setImageView(ImageView v) {
            mImageView = v;
            //Toast.makeText(mContext, "success!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            try {
                newDistance = scaleGestureDetector.getCurrentSpan();
                matrix.set(mImageView.getImageMatrix());
                //缩放比例
                //float scale = detector.getScaleFactor()/3;
                float scale = newDistance / oldDistance;
                System.out.println("scale:" + scale);
                //mMatrix.setScale(scale, scale,detector.getFocusX(),detector.getFocusY());
                matrix.postScale(scale, scale, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                mImageView.setImageMatrix(matrix);
                oldDistance = newDistance;
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {

            oldDistance = scaleGestureDetector.getCurrentSpan();
            newDistance = scaleGestureDetector.getCurrentSpan();

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }

    }
}
