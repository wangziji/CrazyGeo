/*
 * GestureAnalyser.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.multitouch;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.standrews.mscproject.utils.math.Vector;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-20.
 */
public class GestureAnalyser {

    public final static int ACTION_NONE = 0;
    public final static int ACTION_MOVE = 1;
    public final static int ACTION_SCALE = 2;
    public final static int ACTION_ROTATE = 3;

    private final int MIN_SIZE = 4;
    private final int DEFAULT_SIZE = 10;
    private MotionEvent[] event_array;
    private MotionEvent current_event;
    private int size;
    private int state = ACTION_NONE;

    private Vector baseVector;


    public GestureAnalyser(int size) {
        if (size < MIN_SIZE) {
            this.size = MIN_SIZE;
        } else {
            this.size = size;
        }
        event_array = new MotionEvent[size];
    }

    public GestureAnalyser() {
        this.size = DEFAULT_SIZE;
        event_array = new MotionEvent[size];
    }

    public void eventUpdate(MotionEvent e) {
        if (current_event != null) {
            current_event.recycle();
        }
        current_event = MotionEvent.obtain(e);

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("gesture","d");
                event_array = new MotionEvent[size];
                if (event_array[0] != null) {
                    event_array[0].recycle();
                }
                event_array[0] = MotionEvent.obtain(e);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.i("gesture","pd");
                event_array = new MotionEvent[size];
                if (event_array[0] != null) {
                    event_array[0].recycle();
                }
                event_array[0] = MotionEvent.obtain(e);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("gesture","m");
                for (int i = 1; i < size; i++) {
                    if (event_array[i] == null) {
                        if (event_array[i] != null) {
                            event_array[i].recycle();
                        }
                        event_array[i] = MotionEvent.obtain(e);
                        break;
                    }
                }

        }

    }

    public int getAction() {
        switch (current_event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                state = ACTION_MOVE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                state = ACTION_NONE;
                PointF start = new PointF(event_array[0].getX(0), event_array[0].getY(0));
                PointF end = new PointF(event_array[0].getX(1), event_array[0].getY(1));
                swapPoint(start, end);
                baseVector = new Vector(start, end);

                Log.i("gesture", "base:" + baseVector.getDegree());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                state = ACTION_NONE;

                break;
            case MotionEvent.ACTION_MOVE:
                if (current_event.getPointerCount() > 1) {
                    if (arraySizeChecker() < size) {
                        state = ACTION_NONE;
                    } else {
                        analysis();
                    }
                }
        }
        return state;
    }

    private void analysis() {
        if (state == ACTION_NONE) {
            float scale = 0.5f;
            float rotate = 0.5f;
            for (int i = 1; i < size; i++) {
                PointF start1 = new PointF(event_array[i - 1].getX(0), event_array[i - 1].getY(0));
                PointF end1 = new PointF(event_array[i].getX(0), event_array[i].getY(0));
                Vector v1 = new Vector(start1, end1);

                PointF start2 = new PointF(event_array[i - 1].getX(1), event_array[i - 1].getY(1));
                PointF end2 = new PointF(event_array[i].getX(1), event_array[i].getY(1));
                Vector v2 = new Vector(start2, end2);

                float degree1 = getAngleBetweenTwoVectors(baseVector, v1);
                float degree2 = getAngleBetweenTwoVectors(baseVector, v2);

                if (degree1 * degree2 > 0) {
                    scale += 0.1;
                    rotate -= 0.1;
                } else {
                    scale -= 0.1;
                    rotate += 0.1;
                }
            }
            state = scale > rotate ? ACTION_SCALE : ACTION_ROTATE;
        }
    }

    private void printVectors() {
        Vector first = null;
        Vector v1 = null;
        Vector v2 = null;
        Vector prev1 = null;
        Vector prev2 = null;

        for (int i = 0; i < size; i++) {
            PointF start = new PointF(event_array[i].getX(0), event_array[i].getY(0));
            PointF end = new PointF(event_array[i].getX(1), event_array[i].getY(1));
            if (i == 0) {
                swapPoint(start, end);
                first = new Vector(start, end);
            } else if (i == 1) {
                PointF start1 = new PointF(event_array[i - 1].getX(0), event_array[i - 1].getY(0));
                PointF end1 = new PointF(event_array[i - 1].getX(1), event_array[i - 1].getY(1));
                v1 = new Vector(start1, start);
                v2 = new Vector(end1, end);
            } else {
                PointF start1 = new PointF(event_array[i - 1].getX(0), event_array[i - 1].getY(0));
                PointF end1 = new PointF(event_array[i - 1].getX(1), event_array[i - 1].getY(1));
                v1 = new Vector(start1, start);
                v2 = new Vector(end1, end);
                PointF start2 = new PointF(event_array[i - 2].getX(0), event_array[i - 2].getY(0));
                PointF end2 = new PointF(event_array[i - 2].getX(1), event_array[i - 2].getY(1));
                prev1 = new Vector(start2, start1);
                prev2 = new Vector(end2, end1);

            }


            if (i == 0) {
                Log.i("gesture", "=======================================================================");
                Log.i("gesture", "= base:");
                Log.i("gesture", "= \tvector:" + first.toString());
                Log.i("gesture", "= \tdegree:" + first.getDegree() + "\tdirection:" + first.getDirection());
            } else if (i == size - 1) {
                Log.i("gesture", "=----------------------------------------------------------------------");
                Log.i("gesture", "= id:" + i);
                Log.i("gesture", "= v1:");
                Log.i("gesture", "= \tvector:" + v1.toString());
                Log.i("gesture", "= \tdegree:" + v1.getDegree() + "\tdirection:" + v1.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v1));
                Log.i("gesture", "= \tangle between prev:" + getAngleBetweenTwoVectors(prev1, v1));
                Log.i("gesture", "= v2:");
                Log.i("gesture", "= \tvector:" + v2.toString());
                Log.i("gesture", "= \tdegree:" + v2.getDegree() + "\tdirection:" + v2.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v2));
                Log.i("gesture", "= \tangle between prev:" + getAngleBetweenTwoVectors(prev2, v2));
                Log.i("gesture", "=======================================================================");
                Log.i("gesture", "");
                Log.i("gesture", "");
                Log.i("gesture", "");
            } else if (i == 1) {
                Log.i("gesture", "=----------------------------------------------------------------------");
                Log.i("gesture", "= id:" + i);
                Log.i("gesture", "= v1:");
                Log.i("gesture", "= \tvector:" + v1.toString());
                Log.i("gesture", "= \tdegree:" + v1.getDegree() + "\tdirection:" + v1.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v1));

                Log.i("gesture", "= v2:");
                Log.i("gesture", "= \tvector:" + v2.toString());
                Log.i("gesture", "= \tdegree:" + v2.getDegree() + "\tdirection:" + v2.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v2));

            } else {
                Log.i("gesture", "=----------------------------------------------------------------------");
                Log.i("gesture", "= id:" + i);
                Log.i("gesture", "= v1:");
                Log.i("gesture", "= \tvector:" + v1.toString());
                Log.i("gesture", "= \tdegree:" + v1.getDegree() + "\tdirection:" + v1.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v1));
                Log.i("gesture", "= \tangle between prev:" + getAngleBetweenTwoVectors(prev1, v1));
                Log.i("gesture", "= v2:");
                Log.i("gesture", "= \tvector:" + v2.toString());
                Log.i("gesture", "= \tdegree:" + v2.getDegree() + "\tdirection:" + v2.getDirection());
                Log.i("gesture", "= \tangle between base:" + getAngleBetweenTwoVectors(first, v2));
                Log.i("gesture", "= \tangle between prev:" + getAngleBetweenTwoVectors(prev2, v2));
            }

        }
    }

    private float getAngleBetweenTwoVectors(Vector first, Vector second) {
        float degree;
        if (first.getDirection() < 2) {
            degree = second.getDegree() - first.getDegree();
            if (degree < -180) {
                degree = 360 + degree;
            }
        } else {
            degree = second.getDegree() - first.getDegree();
            if (degree > 180) {
                degree = -(360 - degree);
            }
        }
        return degree;
    }

    private void swapPoint(PointF start, PointF end) {
        PointF temp = new PointF();
        if (start.x > end.x) {
            // Log.i("gesture","swap");
            temp.set(start);
            start.set(end);
            end.set(temp);
        } else if (start.x == end.x) {
            if (start.y > end.y) {
                temp.set(start);
                start.set(end);
                end.set(temp);
            }
        }
    }

    protected int arraySizeChecker() {
        int i;
        for (i = 0; i < event_array.length; i++) {
            if (event_array[i] == null)
                break;
        }
        return i;
    }

}
