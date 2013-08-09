/*
 * Configuration.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Read configuration file
 */
public class Configuration {

    public Configuration() {
    }

    /**
     * A static method which will return the config properties
     *
     * @return config properties
     */
    public Properties getConfigProperties(Context context) {
        Properties props = new Properties();
        InputStream in;
        try {
            in = context.openFileInput("config.properties");
        } catch (FileNotFoundException e) {
            in = Configuration.class.getResourceAsStream("/assets/config.properties");
            try {
                OutputStream out = context.openFileOutput("config.properties", 0);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return getConfigProperties(context);
        }
        try {
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    /**
     * Save the configuration properties
     *
     * @param context Context
     * @param props   Properties
     */
    public void saveConfigProperties(Context context, Properties props) {
        try {
            props.store(context.openFileOutput("config.properties", 0), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
