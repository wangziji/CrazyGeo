/*
 * Logger.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.logging;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

import com.standrews.mscproject.game.BitmapResource;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.ExternalFileManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-21.
 */
public class Logger {

    private ExternalFileManager efm;
    private StringBuilder sb;
    private boolean inProgress;
    private Handler handler;

    public Logger() {
        handler = new Handler();
    }

    public void writeEnd() {
        if (inProgress) {
            efm.writeFile("\t</body>\n" + "</root>");
            inProgress = false;
        }
    }

    public void writeHeader(Context context) {
        inProgress = true;
        Configuration config = new Configuration();
        Properties info = config.getConfigProperties(context);
        sb = new StringBuilder();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String date = sDateFormat.format(curDate);

        efm = new ExternalFileManager(info.getProperty("DATA_DIR"), System.currentTimeMillis() + ".xml");

        sb.append("<root>\n");
        sb.append("\t<header id=\"").append(info.getProperty("ID")).append("\" date=\"").append(date).append("\">\n");
        sb.append("\t\t<screen_width>").append(info.getProperty("DISPLAY_WIDTH")).append("</screen_width>\n");
        sb.append("\t\t<screen_height>").append(info.getProperty("DISPLAY_HEIGHT")).append("</screen_height>\n");
        sb.append("\t\t<manufacturer>").append(info.getProperty("MANUFACTURER")).append("</manufacturer>\n");
        sb.append("\t\t<model>").append(info.getProperty("MODEL")).append("</model>\n");
        sb.append("\t\t<os_version>").append(info.getProperty("OS_VERSION")).append("</os_version>\n");
        sb.append("\t\t<memory>").append(info.getProperty("MEMORY")).append("</memory>\n");
        sb.append("\t\t<dpi>").append(info.getProperty("DPI")).append("</dpi>\n");
        sb.append("\t</header>\n");
        efm.writeFile(sb.toString());

    }

    public void writeMovement(MotionEvent event, boolean rotate, boolean scale) {
        if (inProgress) {
            sb = new StringBuilder();
            sb.append("\t\t<movement time=\"").append(System.currentTimeMillis()).append("\" rotate=\"").append(rotate).append("\" scale=\"").append(scale).append("\">\n");
            int count = event.getPointerCount();
            for (int i = 0; i < count; i++) {
                sb.append("\t\t\t<pointer x=\"").append(event.getX(i)).append("\" y=\"").append(event.getY(i)).append("\"/>\n");
            }
            sb.append("\t\t</movement>\n");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    efm.writeFile(sb.toString());
                }
            });
        }
    }

    public void writeShapes(BitmapResource init, BitmapResource tar) {
        if (inProgress) {
            sb = new StringBuilder();
            sb.append("\t<body>\n");
            sb.append("\t\t<initial_shape time=\"").append(System.currentTimeMillis()).append("\" start_x=\"").append(init.getLocation().x).append("\" start_y=\"").append(init.getLocation().y).append("\" width=\"").append(init.getSize()[0]).append("\" height=\"").append(init.getSize()[1]).append("\" orientation=\"").append(init.getDegree()).append("\"/>\n");
            sb.append("\t\t<target_shape time=\"").append(System.currentTimeMillis()).append("\" start_x=\"").append(tar.getLocation().x).append("\" start_y=\"").append(tar.getLocation().y).append("\" width=\"").append(tar.getSize()[0]).append("\" height=\"").append(tar.getSize()[1]).append("\" orientation=\"").append(tar.getDegree()).append("\"/>\n");
            efm.writeFile(sb.toString());
        }
    }

}
