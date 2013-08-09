/*
 * LeaderBoardSurfaceView.java
 *
 * Created on: 9 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.custom_ui_componets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.standrews.mscproject.utils.Configuration;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-26.
 */
public class LeaderBoardSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Typeface typeface;
    private Context context;
    private int asia;
    private int europe;
    private int africa;
    private int america;
    private float lineHeight;
    private float textSize;
    private float[] asiaPos, europePos, africaPos, americaPos;

    public LeaderBoardSurfaceView(Context context) {
        super(context);
    }

    public LeaderBoardSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //
    public LeaderBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        holder = this.getHolder();
        if (holder != null) {
            holder.addCallback(this);
        }
    }

    public void drawChart() {
        Canvas canvas = holder.lockCanvas(null);
        if (canvas != null) {
            canvas.drawARGB(255, 255, 250, 205);
            Paint p1 = new Paint();
            p1.setColor(Color.rgb(26, 26, 26));
            canvas.drawRect(0, getHeight() - lineHeight, getWidth(), getHeight() - lineHeight + 3, p1);
            p1.setColor(Color.rgb(231, 66, 12));
            canvas.drawRect(asiaPos[0], asiaPos[1], asiaPos[2], asiaPos[3], p1);
            canvas.drawRect(europePos[0], europePos[1], europePos[2], europePos[3], p1);
            canvas.drawRect(africaPos[0], africaPos[1], africaPos[2], africaPos[3], p1);
            canvas.drawRect(americaPos[0], americaPos[1], americaPos[2], americaPos[3], p1);

            Paint text = new Paint();
            text.setColor(Color.rgb(26, 26, 26));
            text.setTypeface(typeface);
            text.setTextSize(textSize);
            float string_w;
            String asia_s = asia + "";
            string_w = asia_s.length() * textSize;
            canvas.drawText(asia_s, (asiaPos[2] - asiaPos[0]) / 2 - string_w / 2 + asiaPos[0], getHeight() - lineHeight + textSize, text);

            String europe_s = europe + "";
            string_w = europe_s.length() * textSize;
            canvas.drawText(europe_s, (europePos[2] - europePos[0]) / 2 - string_w / 2 + europePos[0], getHeight() - lineHeight + textSize, text);

            String africa_s = africa + "";
            string_w = africa_s.length() * textSize;
            canvas.drawText(africa_s, (africaPos[2] - africaPos[0]) / 2 - string_w / 2 + africaPos[0], getHeight() - lineHeight + textSize, text);

            String america_s = america + "";
            string_w = america_s.length() * textSize;
            canvas.drawText(america_s, (americaPos[2] - americaPos[0]) / 2 - string_w / 2 + americaPos[0], getHeight() - lineHeight + textSize, text);

            canvas.drawText("Asia/Oceania", asiaPos[0], asiaPos[1] - 30, text);
            canvas.drawText("Europe", europePos[0], europePos[1] - 30, text);
            canvas.drawText("Africa", africaPos[0], africaPos[1] - 30, text);
            canvas.drawText("America", americaPos[0], americaPos[1] - 30, text);

        }

        holder.unlockCanvasAndPost(canvas);
        this.invalidate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initialize();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    private void initialize() {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Comics_font_by_kernalphage.ttf");
        Configuration configuration = new Configuration();
        asia = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("ASIA_BEST"));
        europe = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("EUROPE_BEST"));
        africa = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("AFRICA_BEST"));
        america = Integer.parseInt(configuration.getConfigProperties(getContext()).getProperty("AMERICA_BEST"));

        int highest = Math.max(Math.max(asia, europe), Math.max(africa, america));

        int MAX = 30;
        int max = highest > MAX ? highest : MAX;
        float height = getHeight() * 0.618f;
        float weight = getWidth() / 8f;
        lineHeight = getHeight() * 0.25f;
        asiaPos = new float[4];
        asiaPos[0] = weight / 2;
        asiaPos[1] = (getHeight() - lineHeight) - (float) asia / (float) max * height;
        asiaPos[2] = asiaPos[0] + weight;
        asiaPos[3] = getHeight() - lineHeight;
        europePos = new float[4];
        europePos[0] = asiaPos[2] + weight;
        europePos[1] = (getHeight() - lineHeight) - (float) europe / (float) max * height;
        europePos[2] = europePos[0] + weight;
        europePos[3] = getHeight() - lineHeight;
        africaPos = new float[4];
        africaPos[0] = europePos[2] + weight;
        africaPos[1] = (getHeight() - lineHeight) - (float) africa / (float) max * height;
        africaPos[2] = africaPos[0] + weight;
        africaPos[3] = getHeight() - lineHeight;
        americaPos = new float[4];
        americaPos[0] = africaPos[2] + weight;
        americaPos[1] = (getHeight() - lineHeight) - (float) america / (float) max * height;
        americaPos[2] = americaPos[0] + weight;
        americaPos[3] = getHeight() - lineHeight;

        int TEXT_SIZE = 40;
        textSize = TEXT_SIZE * (getWidth() / 1280f);
    }


}
