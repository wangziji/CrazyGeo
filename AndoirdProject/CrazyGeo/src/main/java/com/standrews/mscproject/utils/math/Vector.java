/*
 * Vector.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils.math;

import android.graphics.PointF;
import android.util.FloatMath;

/**
 * Created by ziji on 13-6-25.
 */
public class Vector {

    private PointF start, end;
    public final static int TOP_LEFT = 0;
    public final static int TOP_RIGHT = 1;
    public final static int BOTTOM_LEFT = 3;
    public final static int BOTTOM_RIGHT = 2;

    public Vector(PointF start, PointF end) {
        this.start = start;
        this.end = end;
    }

    public PointF getStart() {
        return start;
    }

    public void setStart(PointF start) {
        this.start = start;
    }

    public PointF getEnd() {
        return end;
    }

    public void setEnd(PointF end) {
        this.end = end;
    }

    public float getDistance() {
        float x = start.x - end.x;
        float y = start.y - end.y;
        return FloatMath.sqrt(x * x + y * y);
    }

    private float getArc() {
        float delta_x = (start.x - end.x);
        float delta_y = (start.y - end.y);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) radians;
    }

    public float getDegree() {
        return (float) Math.toDegrees(getArc());
    }

    public int getDirection() {
        float degree = getDegree();
        if (degree > 0 && degree <= 90) {
            return TOP_LEFT;
        } else if (degree > 90 && degree <= 180) {
            return TOP_RIGHT;
        } else if (degree > -180 && degree <= -90) {
            return BOTTOM_RIGHT;
        } else {
            return BOTTOM_LEFT;
        }
    }

    @Override
    public String toString() {
        return "(" + start.x + "," + start.y + ") -> (" + end.x + "," + end.y + ")";
    }
}
