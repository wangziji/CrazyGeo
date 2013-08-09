/*
 * ShapeFactory.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.shapes;

import android.content.res.Resources;

import com.standrews.mscproject.game.BitmapResource;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-29.
 */
public class ShapeFactory {

    public static BitmapResource BuildBitmapResource(int shape, Resources resources, int type) {

        Shape s = null;
        switch (shape) {
            case Shape.ShapeType.CIRCLE:
                s = new Circle(resources, type);
                break;
            case Shape.ShapeType.RECTANGLE:
                s = new Rectangle(resources, type);
                break;
            case Shape.ShapeType.SQUARE:
                s = new Square(resources, type);
                break;
            case Shape.ShapeType.TRIANGLE:
                s = new Triangle(resources, type);
                break;
        }

//        return new BitmapResource(s);
        return null;
    }

}
