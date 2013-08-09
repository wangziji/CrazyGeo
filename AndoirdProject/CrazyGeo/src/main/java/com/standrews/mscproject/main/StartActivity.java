/*
 * StartActivity.java
 *
 * Created on: 6 /7 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.standrews.mscproject.custom_ui_componets.MapImageView;
import com.standrews.mscproject.custom_ui_componets.MyLayout;
import com.standrews.mscproject.game.Continent;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

import java.util.Properties;


/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-6.
 */
public class StartActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {

    private MapImageView map;
    private GestureDetector mGestureDetector;
    private final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    private int continent;
    private TextView textView;
    private String[] continentName;

    private MyLayout mLayout;
    private Configuration configuration;
    private int handedness,autoDetect;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //delete window background
        getWindow().setBackgroundDrawable(null);

        mLayout = (MyLayout) findViewById(R.id.myLayout);
        configuration = new Configuration();
        handedness = Integer.parseInt(configuration.getConfigProperties(this).getProperty("HANDEDNESS"));
        autoDetect = Integer.parseInt(configuration.getConfigProperties(this).getProperty("AUTO_DETECT"));

        initialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLayout.recycle();
        map.recycle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mLayout.setBackgroundResource(R.drawable.big_bk);
            map.setImageResource(R.drawable.map, mLayout.getHeight());
            //set animation
            continent = Continent.ASIA;
            Animation zoom_in_asia = AnimationUtils.loadAnimation(this, R.anim.zoom_in_asia);
            if (zoom_in_asia != null) {
                map.startAnimation(zoom_in_asia);
            }
        }
    }

    private void initialize() {
        map = (MapImageView) findViewById(R.id.map);
        map.setOnTouchListener(this);
        textView = (TextView) findViewById(R.id.startContinentTextView);
        DisplayManager dm = new DisplayManager();

        //set font
        dm.setFont(this, textView);

        //set padding

        int[] size = dm.getDPSize(this);
        int padding = Math.round(size[1] * 0.1f);
        textView.setPadding(0, padding, 0, 0);

        //set text size
        float textSizeRate = size[1] / 320f;
        int textSize = getResources().getInteger(R.integer.board_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);

        //load continent names
        continentName = new String[4];
        continentName[0] = getResources().getString(R.string.asian);
        continentName[1] = getResources().getString(R.string.europe);
        continentName[2] = getResources().getString(R.string.africa);
        continentName[3] = getResources().getString(R.string.america);

        mGestureDetector = new GestureDetector(StartActivity.this, this);

        //set image button for play
        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onFling(MotionEvent event, MotionEvent event2, float v, float v2) {
        int direction = flingDirection(event, event2);
        Animation transfer = null;
        switch (continent) {
            case Continent.ASIA:
                switch (direction) {
                    case RIGHT:
                        continent = Continent.EUROPE;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_europe);
                        break;
                }
                break;
            case Continent.EUROPE:
                switch (direction) {
                    case LEFT:
                        continent = Continent.ASIA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_asia);
                        break;
                    case RIGHT:
                        continent = Continent.AMERICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_america);
                        break;
                    case UP:
                        continent = Continent.AFRICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_africa);
                        break;
                }
                break;
            case Continent.AFRICA:
                switch (direction) {
                    case LEFT:
                        continent = Continent.ASIA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_asia);
                        break;
                    case RIGHT:
                        continent = Continent.AMERICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_america);
                        break;
                    case DOWN:
                        continent = Continent.EUROPE;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_europe);
                        break;
                }
                break;
            case Continent.AMERICA:
                switch (direction) {
                    case LEFT:
                        continent = Continent.EUROPE;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_europe);
                        break;
                }
                break;
        }

        if (transfer != null) {
            map.startAnimation(transfer);
        }
        textView.setText(continentName[continent]);
        return true;
    }

    private int flingDirection(MotionEvent event, MotionEvent event2) {
        float degree = rotation(event, event2);
        int hand;
        if (degree > 45 && degree <= 135) {
            if(degree>=90){
                hand=1;
            }else {
                hand=0;
            }
            setHandedness(hand);
            return UP;
        } else if (degree > 135 || degree < -135) {
            if(degree<=180){
                hand=1;
            }else {
                hand=0;
            }
            setHandedness(hand);
            return RIGHT;
        } else if (degree >= -135 && degree < -45) {
            if(degree>=-90){
                hand=1;
            }else {
                hand=0;
            }
            setHandedness(hand);
            return DOWN;
        } else {
            if(degree<=0){
                hand=1;
            }else {
                hand=0;
            }
            setHandedness(hand);
            return LEFT;
        }

    }

    private void setHandedness(int hand){
        if(autoDetect==1&&hand!=handedness){
            Properties prop = configuration.getConfigProperties(this);
            prop.setProperty("HANDEDNESS",hand+"");
            configuration.saveConfigProperties(this,prop);
        }
    }

    private float rotation(MotionEvent event, MotionEvent event2) {
        float delta_x = (event.getX(0) - event2.getX(0));
        float delta_y = (event.getY(0) - event2.getY(0));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {

    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(StartActivity.this, GameActivity.class);
        intent.putExtra("continent", continent);
        startActivity(intent);
    }
}