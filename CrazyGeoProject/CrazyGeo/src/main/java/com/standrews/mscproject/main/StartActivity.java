/*
 * StartActivity.java
 *
 * Created on: 17 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.standrews.mscproject.custom_ui_componets.MapImageView;
import com.standrews.mscproject.game.Continent;
import com.standrews.mscproject.music.MusicPlayer;
import com.standrews.mscproject.tcpconnection.AlarmReceiver;
import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

import java.util.Properties;


/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-7-6.
 */
public class StartActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {

    private final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private MapImageView map;
    private GestureDetector mGestureDetector;
    private int continent;
    private TextView textView;
    private String[] continentName;
    private Configuration configuration;
    private int autoDetect;
    private FrameLayout fl;
    private Handler mHandler;
    private DisplayManager dm;
    private MusicPlayer musicPlayer;
    private boolean isFirstTime = true;
    private ImageView left, right;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId() == R.id.playButton) {
            intent.setClass(StartActivity.this, GameActivity.class);
            intent.putExtra("continent", continent);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        } else if (view.getId() == R.id.settingImageButton) {
            intent.setClass(StartActivity.this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
        } else if (view.getId() == R.id.leaderBoardImageButton) {
            intent.setClass(StartActivity.this, LeaderBoardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_start);

        continent = Continent.ASIA;

        fl = (FrameLayout) findViewById(R.id.loadingLayout);
        mHandler = new MyHandler();

        dm = new DisplayManager();
        TextView crazy = (TextView) findViewById(R.id.logoTextViewCrazy);
        TextView geo = (TextView) findViewById(R.id.logoTextViewGeo);
        TextView loading = (TextView) findViewById(R.id.loadingTextView);

        dm.setFont(this, crazy);
        dm.setFont(this, geo);
        dm.setFont(this, loading);

        int[] size = dm.getDPSize(StartActivity.this);
        int padding = Math.round(size[1] * 0.1f);
        crazy.setPadding(0, 0, padding, 0);
        geo.setPadding(padding, 0, 0, 0);


        //set text size
        float textSizeRate = size[1] / 320f;
        int bigSize = getResources().getInteger(R.integer.Logo_big);
        crazy.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigSize * textSizeRate);
        int smallSize = getResources().getInteger(R.integer.Logo_small);
        geo.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize * textSizeRate);
        int textSize = getResources().getInteger(R.integer.text_size);
        loading.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("me", "recycle");
        super.onDestroy();
        if (map != null) {
            map.recycle();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            configuration = new Configuration();
            if (isFirstTime) {
                Log.i("me", "first time");
                Thread t = new Thread(new Initialize());
                t.start();
            }
            int sound = Integer.parseInt(configuration.getConfigProperties(StartActivity.this).getProperty("SOUND"));
            musicPlayer = new MusicPlayer(R.raw.background1, this);
            if (sound == 0) {
                musicPlayer.start();
            }

        } else {
            if (musicPlayer != null) {
                musicPlayer.release();
            }
        }
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
                    case LEFT:
                        continent = Continent.AMERICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_america);
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
                        continent = Continent.AFRICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_africa);
                        break;
//                    case UP:
//                        continent = Continent.AFRICA;
//                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_africa);
//                        break;
                }
                break;
            case Continent.AFRICA:

                switch (direction) {
                    case LEFT:
                        continent = Continent.EUROPE;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_europe);
                        break;
                    case RIGHT:
                        continent = Continent.AMERICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_america);
                        break;
                    case DOWN:
//                        continent = Continent.EUROPE;
//                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_europe);
//                        break;
                }
                break;
            case Continent.AMERICA:
                switch (direction) {
                    case LEFT:
                        continent = Continent.AFRICA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_africa);
                        break;
                    case RIGHT:
                        continent = Continent.ASIA;
                        transfer = AnimationUtils.loadAnimation(this, R.anim.zoom_in_asia);
                        break;
                }
                break;
        }

        if (transfer != null) {
            map.clearAnimation();
            map.startAnimation(transfer);
            textView.setText(continentName[continent]);
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return true;
    }

    private int flingDirection(MotionEvent event, MotionEvent event2) {
        float degree = rotation(event, event2);
        int hand;
        if (degree > 45 && degree <= 135) {
            if (degree >= 90) {
                hand = 1;
            } else {
                hand = 0;
            }
            Log.i("me",hand+"hand");
            setHandedness(hand);
            return UP;
        } else if (degree > 135 || degree < -135) {
            if (degree <= 180 && degree>135) {
                hand = 1;
            } else {
                hand = 0;
            }
            setHandedness(hand);
            Log.i("me",hand+"hand");
            return RIGHT;
        } else if (degree >= -135 && degree < -45) {
            if (degree >= -90) {
                hand = 1;
            } else {
                hand = 0;
            }
            setHandedness(hand);
            return DOWN;
        } else {
            if (degree <= 0) {
                hand = 1;
            } else {
                hand = 0;
            }
            setHandedness(hand);
            Log.i("me",hand+"hand");
            return LEFT;
        }

    }

    private void initialize() {
        //delete window background
        getWindow().setBackgroundDrawable(null);

        textView = (TextView) findViewById(R.id.startContinentTextView);

        //set font
        dm.setFont(StartActivity.this, textView);

        //set padding
        int[] size = dm.getDPSize(StartActivity.this);
        int padding = Math.round(size[1] * 0.1f);
        textView.setPadding(0, padding, 0, 0);

        //set text size
        float textSizeRate = size[1] / 320f;
        int textSize = getResources().getInteger(R.integer.board_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);

        FrameLayout mLayout = (FrameLayout) findViewById(R.id.myLayout);
        map = (MapImageView) findViewById(R.id.map);
        map.setOnTouchListener(StartActivity.this);
        map.setImageResource(R.drawable.map, mLayout.getHeight());


        mGestureDetector = new GestureDetector(StartActivity.this, StartActivity.this);
        //set image button for play
        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(StartActivity.this);
        ImageButton setting = (ImageButton) findViewById(R.id.settingImageButton);
        setting.setOnClickListener(StartActivity.this);
        ImageButton lb = (ImageButton) findViewById(R.id.leaderBoardImageButton);
        lb.setOnClickListener(StartActivity.this);

        autoDetect = Integer.parseInt(configuration.getConfigProperties(StartActivity.this).getProperty("AUTO_DETECT"));


        continentName = new String[4];
        continentName[0] = getResources().getString(R.string.asian);
        continentName[1] = getResources().getString(R.string.europe);
        continentName[2] = getResources().getString(R.string.africa);
        continentName[3] = getResources().getString(R.string.america);

        //set navigation icon
        left = (ImageView) findViewById(R.id.navLeft);
        right = (ImageView) findViewById(R.id.navRight);

        fl.setVisibility(View.VISIBLE);

        int permission = Integer.parseInt(configuration.getConfigProperties(StartActivity.this).getProperty("USER_PERMISSION"));

        if (permission == 0) {
            dialog();
        } else {
            Intent mIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int delay = getResources().getInteger(R.integer.service_delay);
            long interval = delay * 1000 * 60;
//            long interval = 1000 * 5;
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pendingIntent);

        }


        isFirstTime = false;
    }

    private void navigationAnimation() {
        left.clearAnimation();
        right.clearAnimation();
        Animation transfer1 = AnimationUtils.loadAnimation(StartActivity.this, R.anim.appear);
        left.setVisibility(View.VISIBLE);
        left.startAnimation(transfer1);
        right.setVisibility(View.VISIBLE);
        right.startAnimation(transfer1);
    }

    private float rotation(MotionEvent event, MotionEvent event2) {
        float delta_x = (event.getX(0) - event2.getX(0));
        float delta_y = (event.getY(0) - event2.getY(0));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void setHandedness(int hand) {
        if (autoDetect == 1) {
            Properties prop = configuration.getConfigProperties(this);
            prop.setProperty("HANDEDNESS", hand + "");
            configuration.saveConfigProperties(this, prop);
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initialize();
                    break;
                case 2:
                    fl.setVisibility(View.GONE);
                    //set animation

                    Animation transfer = null;
                    switch (continent) {
                        case Continent.ASIA:
                            transfer = AnimationUtils.loadAnimation(StartActivity.this, R.anim.zoom_in_asia);
                            break;
                        case Continent.EUROPE:
                            transfer = AnimationUtils.loadAnimation(StartActivity.this, R.anim.zoom_in_europe);
                            break;
                        case Continent.AFRICA:
                            transfer = AnimationUtils.loadAnimation(StartActivity.this, R.anim.zoom_in_africa);
                            break;
                        case Continent.AMERICA:
                            transfer = AnimationUtils.loadAnimation(StartActivity.this, R.anim.zoom_in_america);
                            break;
                    }

                    if (transfer != null) {
                        map.startAnimation(transfer);
                    }

                    textView.setText(continentName[continent]);
                    navigationAnimation();
                    break;
            }
        }
    }

    private class Initialize implements Runnable {

        @Override
        public void run() {
            if (isFirstTime) {
                mHandler.sendEmptyMessage(1);
            }
            mHandler.sendEmptyMessage(2);

        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_dialog_title);
        builder.setMessage(R.string.permission_dialog_msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Properties config = configuration.getConfigProperties(StartActivity.this);
                config.setProperty("USER_PERMISSION", 1 + "");
                configuration.saveConfigProperties(StartActivity.this, config);
                Intent mIntent = new Intent(StartActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(StartActivity.this, 0, mIntent, 0);
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int delay = getResources().getInteger(R.integer.service_delay);
                long interval = delay * 1000 * 60;
//                long interval = 1000 * 5;
                manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pendingIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}