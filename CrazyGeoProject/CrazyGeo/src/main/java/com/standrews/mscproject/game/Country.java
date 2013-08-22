/*
 * Country.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.game;

/**
 * MSc project
 * <p/>
 * Object of country, contains information about country's name, position, size, resource ID
 *
 * Created by Ziji Wang on 13-7-8.
 */
public class Country {

    private String name, resID;
    private int tarPosX, tarPosY, tarWidth, tarHeight;

    /**
     * Constructor
     */
    public Country() {
    }

    /**
     * Getter for Name
     * @return String, name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name String, name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for resource ID
     * @return String, resource ID
     */
    public String getResID() {
        return resID;
    }

    /**
     * Setter for resource ID
     * @param resID String resource ID
     */
    public void setResID(String resID) {
        this.resID = resID;
    }

    /**
     * Getter for target height
     * @return int, target height
     */
    public int getTarHeight() {
        return tarHeight;
    }

    /**
     * Setter for target height
     * @param tarHeight int, target height
     */
    public void setTarHeight(int tarHeight) {
        this.tarHeight = tarHeight;
    }

    /**
     * Getter for position X
     * @return int, position X
     */
    public int getTarPosX() {
        return tarPosX;
    }

    /**
     * Setter for position X
     * @param tarPosX int, Position X
     */
    public void setTarPosX(int tarPosX) {
        this.tarPosX = tarPosX;
    }

    /**
     * Getter for position Y
     * @return int, position Y
     */
    public int getTarPosY() {
        return tarPosY;
    }

    /**
     * Setter for position Y
     * @param tarPosY int, Position Y
     */
    public void setTarPosY(int tarPosY) {
        this.tarPosY = tarPosY;
    }

    /**
     * Getter for target width
     * @return int, target width
     */
    public int getTarWidth() {
        return tarWidth;
    }

    /**
     * Setter for target width
     * @param tarWidth int, target width
     */
    public void setTarWidth(int tarWidth) {
        this.tarWidth = tarWidth;
    }

    @Override
    public String toString() {
        return "name:" + name + "\tres:" + resID + "\nx:" + getTarPosX() + "\ty:" + getTarPosY() + "\nw:" + tarWidth + "\th:" + tarHeight;
    }
}
