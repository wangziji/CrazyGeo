/*
 * SettingActivity.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.standrews.mscproject.utils.Configuration;
import com.standrews.mscproject.utils.DisplayManager;

import java.util.Properties;

/**
 * MSc project
 * <p/>
 * Activity for setting screen
 *
 * Created by Ziji Wang on 13-7-18.
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private Configuration configuration;
    private int sound, autodetect, hand;
    private ImageButton soundButton;
    private FrameLayout checkBoxes;
    private CheckBox right, left, auto;

    /**
     * Initialize
     */
    public void initialize() {
        TextView tutorial = (TextView) findViewById(R.id.tutorialTextView);
        tutorial.setOnClickListener(this);

        TextView credits = (TextView) findViewById(R.id.creditsTextView);
        credits.setOnClickListener(this);

        TextView handedness = (TextView) findViewById(R.id.handednessTextView);
        handedness.setOnClickListener(this);

        configuration = new Configuration();
        sound = Integer.parseInt(configuration.getConfigProperties(this).getProperty("SOUND"));

        soundButton = (ImageButton) findViewById(R.id.soundImageButton);
        if (sound == 0) {
            soundButton.setBackgroundResource(R.drawable.sound_button_action);
        } else {
            soundButton.setBackgroundResource(R.drawable.muted_button_action);
        }
        soundButton.setOnClickListener(this);

        DisplayManager dm = new DisplayManager();
        //set font
        dm.setFont(this, tutorial);
        dm.setFont(this, credits);
        dm.setFont(this, handedness);

        //set padding
        int[] size = dm.getDPSize(this);
        float textSizeRate = size[1] / 320f;
        int textSize = getResources().getInteger(R.integer.board_size);
        tutorial.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);
        credits.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);
        handedness.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * textSizeRate);

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(this);

        checkBoxes = (FrameLayout) findViewById(R.id.handednessCheckbox);

        right = (CheckBox) findViewById(R.id.rightCheckBox);
        left = (CheckBox) findViewById(R.id.leftCheckBox);
        auto = (CheckBox) findViewById(R.id.autoCheckBox);

        right.setOnClickListener(this);
        left.setOnClickListener(this);
        auto.setOnClickListener(this);

        dm.setFont(this, right);
        dm.setFont(this, left);
        dm.setFont(this, auto);

        autodetect = Integer.parseInt(configuration.getConfigProperties(this).getProperty("AUTO_DETECT"));
        hand = Integer.parseInt(configuration.getConfigProperties(this).getProperty("HANDEDNESS"));

        if (autodetect == 1) {
            auto.setChecked(true);
            right.setChecked(false);
            left.setChecked(false);
        } else {
            if (hand == 0) {
                auto.setChecked(false);
                right.setChecked(false);
                left.setChecked(true);
            } else {
                auto.setChecked(false);
                right.setChecked(true);
                left.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.soundImageButton:
                Properties prop = configuration.getConfigProperties(this);
                if (sound == 0) {
                    soundButton.setBackgroundResource(R.drawable.muted_button_action);
                    sound = 1;
                    prop.setProperty("SOUND", 1 + "");
                } else {
                    soundButton.setBackgroundResource(R.drawable.sound_button_action);
                    sound = 0;
                    prop.setProperty("SOUND", 0 + "");
                }
                configuration.saveConfigProperties(this, prop);
                break;
            case R.id.backButton:
                this.finish();
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;
            case R.id.handednessTextView:
                if (checkBoxes.getVisibility() == View.GONE) {
                    checkBoxes.setVisibility(View.VISIBLE);
                } else {
                    checkBoxes.setVisibility(View.GONE);
                }
                break;
            case R.id.rightCheckBox:
                auto.setChecked(false);
                right.setChecked(true);
                left.setChecked(false);
                hand = 1;
                autodetect = 0;
                Properties prop1 = configuration.getConfigProperties(this);
                prop1.setProperty("AUTO_DETECT", 0 + "");
                prop1.setProperty("HANDEDNESS", 1 + "");
                configuration.saveConfigProperties(this, prop1);
                break;
            case R.id.leftCheckBox:
                auto.setChecked(false);
                right.setChecked(false);
                left.setChecked(true);
                hand = 0;
                autodetect = 0;
                Properties prop2 = configuration.getConfigProperties(this);
                prop2.setProperty("AUTO_DETECT", 0 + "");
                prop2.setProperty("HANDEDNESS", 0 + "");
                configuration.saveConfigProperties(this, prop2);
                break;
            case R.id.autoCheckBox:
                auto.setChecked(true);
                right.setChecked(false);
                left.setChecked(false);
                autodetect = 1;
                Properties prop3 = configuration.getConfigProperties(this);
                prop3.setProperty("AUTO_DETECT", 1 + "");
                prop3.setProperty("HANDEDNESS", hand + "");
                configuration.saveConfigProperties(this, prop3);
                break;
            case R.id.tutorialTextView:
                startActivity(new Intent(this, TutorialActivity.class));
                this.finish();
                overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
                break;
            case R.id.creditsTextView:
                startActivity(new Intent(this, CreditsActivity.class));
                this.finish();
                overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();

    }
}