/*
 * ExternalFileTest.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tests;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.standrews.mscproject.main.R;
import com.standrews.mscproject.utils.ExternalFileManager;

/**
 * Created by ziji on 13-6-2.
 */
public class ExternalFileTest extends Activity {
    private Button write;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_test);
        write = (Button) findViewById(R.id.ex_write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExternalFileManager efm = new ExternalFileManager("test", "test.txt");
                efm.writeFile("haha+++");
            }
        });
    }
}