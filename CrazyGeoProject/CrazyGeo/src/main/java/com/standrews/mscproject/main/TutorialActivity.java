/*
 * TutorialActivity.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.standrews.mscproject.custom_ui_componets.MySeekBar;

/**
 * MSc project
 * <p/>
 * Activity for tutorial
 *
 * Created by Ziji Wang on 13-7-23.
 */
public class TutorialActivity extends Activity implements View.OnClickListener {

    private ImageView[] images;
    private ImageView center;
    private LinearLayout prevLayout, nextLayout, demo;
    private ImageButton prev, replay, next, quit;
    private int state = 0;
    private Thread thread;
    private MyHandler handler;
    private MySeekBar seekBar;
    private Toast toast;

    @Override
    public void onClick(View view) {
        if (view == replay) {
            thread.interrupt();
            thread = null;
            stopAnimation();
            reset();
            if (thread == null) {
                thread = new Thread(new MyRunnable());
                thread.start();
            }
        } else if (view == next) {
            thread.interrupt();
            thread = null;
            stopAnimation();
            state++;
            reset();
            if (thread == null) {
                thread = new Thread(new MyRunnable());
                thread.start();
            }
        } else if (view == prev) {
            thread.interrupt();
            thread = null;
            stopAnimation();
            state--;
            reset();
            if (thread == null) {
                thread = new Thread(new MyRunnable());
                thread.start();
            }
        } else if (view == quit) {
            thread.interrupt();
            thread = null;
            stopAnimation();
            this.finish();
            overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        center = (ImageView) findViewById(R.id.tutorialImageView5);
        images = new ImageView[8];
        images[0] = (ImageView) findViewById(R.id.tutorialImageView1);
        images[1] = (ImageView) findViewById(R.id.tutorialImageView2);
        images[2] = (ImageView) findViewById(R.id.tutorialImageView3);
        images[3] = (ImageView) findViewById(R.id.tutorialImageView4);
        images[4] = (ImageView) findViewById(R.id.tutorialImageView6);
        images[5] = (ImageView) findViewById(R.id.tutorialImageView7);
        images[6] = (ImageView) findViewById(R.id.tutorialImageView8);
        images[7] = (ImageView) findViewById(R.id.tutorialImageView9);
        prevLayout = (LinearLayout) findViewById(R.id.prevLayout);
        nextLayout = (LinearLayout) findViewById(R.id.nextLayout);
        demo = (LinearLayout) findViewById(R.id.tutorialDemo);
        prev = (ImageButton) findViewById(R.id.prevImageButton);
        prev.setOnClickListener(this);
        replay = (ImageButton) findViewById(R.id.replayTutorialImageButton);
        replay.setOnClickListener(this);
        next = (ImageButton) findViewById(R.id.nextImageButton);
        next.setOnClickListener(this);
        quit = (ImageButton) findViewById(R.id.quitImageButton);
        quit.setOnClickListener(this);
        handler = new MyHandler();
        seekBar = (MySeekBar) findViewById(R.id.tutorialSeekBar);
        seekBar.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        demo.destroyDrawingCache();
        quit.destroyDrawingCache();
        seekBar.destroyDrawingCache();
        prevLayout.destroyDrawingCache();
        nextLayout.destroyDrawingCache();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            reset();
            thread = new Thread(new MyRunnable());
            thread.start();
        }

    }

    private void reset() {
        for (ImageView image : images) {
            image.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(int id) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, id, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void stopAnimation() {
        for (ImageView image : images) {
            image.clearAnimation();
        }
        demo.clearAnimation();
        center.clearAnimation();
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (state) {
                case 0:
                    if (msg.what == 0) {
                        showToast(R.string.move_country);
                        prevLayout.setVisibility(View.GONE);
                        center.setVisibility(View.VISIBLE);
                        Animation transfer = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.move_object);
                        images[4].setVisibility(View.VISIBLE);
                        images[4].setImageResource(R.drawable.up);
                        demo.startAnimation(transfer);
                    } else if (msg.what == 1) {
                        images[4].setImageResource(R.drawable.down);
                    } else {
                        images[4].setVisibility(View.INVISIBLE);
                    }
                    break;
                case 1:
                    if (msg.what == 0) {
                        showToast(R.string.scale_country);
                        prevLayout.setVisibility(View.VISIBLE);
                        Animation transfer = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.scale_object);
                        Animation transfer1 = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.move_right);
                        Animation transfer2 = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.move_left);
                        images[4].setVisibility(View.VISIBLE);
                        images[4].setImageResource(R.drawable.left);
                        images[3].setVisibility(View.VISIBLE);
                        images[3].setImageResource(R.drawable.right);
                        center.startAnimation(transfer);
                        images[3].startAnimation(transfer1);
                        images[4].startAnimation(transfer2);

                    } else if (msg.what == 1) {
                        images[4].setImageResource(R.drawable.right);
                        images[3].setImageResource(R.drawable.left);

                    } else {
                        images[4].setVisibility(View.INVISIBLE);
                        images[3].setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2:
                    if (msg.what == 0) {
                        showToast(R.string.rotate_country);
                        center.setImageResource(R.drawable.object);
                        Animation transfer = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.rotate_object);
                        images[1].setVisibility(View.VISIBLE);
                        images[1].setImageResource(R.drawable.top_left_rotate);
                        images[6].setVisibility(View.VISIBLE);
                        images[6].setImageResource(R.drawable.bottom_right_rotate);
                        demo.startAnimation(transfer);
                    } else if (msg.what == 1) {
                        images[1].setImageResource(R.drawable.top_right_rotate);
                        images[6].setImageResource(R.drawable.bottom_left_rotate);

                    } else {
                        images[1].setVisibility(View.INVISIBLE);
                        images[6].setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3:
                    if (msg.what == 0) {
                        showToast(R.string.move_map);
                        center.setImageResource(R.drawable.bgmap);
                        nextLayout.setVisibility(View.VISIBLE);
                        Animation transfer = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.move_object);
                        images[0].setVisibility(View.VISIBLE);
                        images[1].setVisibility(View.VISIBLE);
                        images[2].setVisibility(View.VISIBLE);
                        images[1].setImageResource(R.drawable.up);
                        images[6].setImageResource(R.drawable.down);
                        demo.startAnimation(transfer);
                    } else if (msg.what == 1) {
                        images[0].setVisibility(View.INVISIBLE);
                        images[1].setVisibility(View.INVISIBLE);
                        images[2].setVisibility(View.INVISIBLE);
                        images[5].setVisibility(View.VISIBLE);
                        images[6].setVisibility(View.VISIBLE);
                        images[7].setVisibility(View.VISIBLE);

                    } else {
                        images[5].setVisibility(View.INVISIBLE);
                        images[6].setVisibility(View.INVISIBLE);
                        images[7].setVisibility(View.INVISIBLE);
                    }
                    break;
                case 4:
                    if (msg.what == 0) {
                        showToast(R.string.scale_map);
                        nextLayout.setVisibility(View.GONE);
                        Animation transfer = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.scale_object);
                        seekBar.setProgress(10);
                        demo.startAnimation(transfer);
                    } else if (msg.what == 1) {
                        seekBar.setProgress(50);
                    }
                    break;
            }
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {

            try {
                handler.obtainMessage(0).sendToTarget();
                Thread.sleep(4000);
                handler.obtainMessage(1).sendToTarget();
                Thread.sleep(3000);
                handler.obtainMessage(2).sendToTarget();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}