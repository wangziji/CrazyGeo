/*
 * Logger.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.logging;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.ExternalFileManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by ziji on 13-6-21.
 */
public class Logger {

    private ExternalFileManager efm;
    private Context context;
    private Configuration config;

    public Logger(Context context) {
        this.context = context;
        efm = new ExternalFileManager("data", System.currentTimeMillis() + ".xml");
        config = new Configuration();
    }

    public void writeHeader() {
        Properties info = config.getConfigProperties(context);
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String date = sDateFormat.format(curDate);
        sb.append("<root>\n");
        sb.append("\t<header id=\"" + info.getProperty("ID") + "\" data=\"" + date + "\">\n");
        sb.append("\t\t<screen_width>" + info.getProperty("DISPLAY_WIDTH") + "</screen_width>\n");
        sb.append("\t\t<screen_height>" + info.getProperty("DISPLAY_HEIGHT") + "</screen_height>\n");
        sb.append("\t\t<manufacturer>" + info.getProperty("MANUFACTURER") + "</manufacturer>\n");
        sb.append("\t\t<model>" + info.getProperty("MODEL") + "</model>\n");
        sb.append("\t\t<os_version>" + info.getProperty("OS_VERSION") + "</os_version>\n");
        sb.append("\t\t<memory>" + info.getProperty("MEMORY") + "</memory>\n");
        sb.append("\t</header>\n");
        efm.writeFile(sb.toString());

    }

    public void writeShapes(ImageView init, ImageView tar) {
        int[] init_start = new int[2];
        init.getLocationInWindow(init_start);
        Drawable init_d = init.getDrawable();
        float init_w = init_d.getIntrinsicWidth();
        float init_h = init_d.getIntrinsicHeight();

        int[] tar_start = new int[2];
        tar.getLocationInWindow(tar_start);
        Drawable tar_d = tar.getDrawable();
        float tar_w = tar_d.getIntrinsicWidth();
        float tar_h = tar_d.getIntrinsicHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("\t<body>\n");
        sb.append("\t\t<initial_shape time=\"" + System.currentTimeMillis() + "\" type=\"rect\" start_x=\"" + init_start[0] + "\" start_y=\"" + init_start[1] + "\" width=\"" + init_w + "\" height=\"" + init_h + "\" orientation=\"\"/>\n");
        sb.append("\t\t<target_shape time=\"" + System.currentTimeMillis() + "\" type=\"rect\" start_x=\"" + tar_start[0] + "\" start_y=\"" + tar_start[1] + "\" width=\"" + tar_w + "\" height=\"" + tar_h + "\" orientation=\"\"/>\n");
        efm.writeFile(sb.toString());


    }

    public void writeMovement(String msg) {
        efm.writeFile(msg);
    }

    public void writeEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t</body>\n");
        sb.append("</root>");
        efm.writeFile(sb.toString());
    }

}
