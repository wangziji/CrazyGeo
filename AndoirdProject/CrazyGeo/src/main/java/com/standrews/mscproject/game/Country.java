/*
 * Country.java
 *
 * Created on: 8 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-8.
 */
public class Country {

    private String name, resID;
    private int tarPosX, tarPosY, tarWidth, tarHeight;

    public Country() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public int getTarPosX() {
        return tarPosX;
    }

    public void setTarPosX(int tarPosX) {
        this.tarPosX = tarPosX;
    }

    public int getTarPosY() {
        return tarPosY;
    }

    public void setTarPosY(int tarPosY) {
        this.tarPosY = tarPosY;
    }

    public int getTarWidth() {
        return tarWidth;
    }

    public void setTarWidth(int tarWidth) {
        this.tarWidth = tarWidth;
    }

    public int getTarHeight() {
        return tarHeight;
    }

    public void setTarHeight(int tarHeight) {
        this.tarHeight = tarHeight;
    }

    @Override
    public String toString() {
        return "name:" + name + "\tres:" + resID + "\nx:" + getTarPosX() + "\ty:" + getTarPosY() + "\nw:" + tarWidth + "\th:" + tarHeight;
    }
}
