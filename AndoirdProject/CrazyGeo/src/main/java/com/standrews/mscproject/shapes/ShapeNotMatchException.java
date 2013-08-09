/*
 * ShapeNotMatchException.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.shapes;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-29.
 */
public class ShapeNotMatchException extends Exception {
    public ShapeNotMatchException() {
        super("these two shape are not match!");
    }
}
