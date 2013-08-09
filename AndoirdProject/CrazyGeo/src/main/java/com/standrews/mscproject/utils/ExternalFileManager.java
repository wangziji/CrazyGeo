/*
 * ExternalFileManager.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * MSc project
 * <p/>
 * Created by Ziji Wang on 13-6-2.
 */
public class ExternalFileManager {

    private String path;
    private String fileName;
    private File rootPath;

    public ExternalFileManager(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        rootPath = new File(Environment.getExternalStorageDirectory(), path);
        if (!rootPath.exists()) {
            rootPath.mkdir();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        rootPath = new File(Environment.getExternalStorageDirectory(), path);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private boolean isExternalStorageAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void writeFile(String str) {
        File dataFile = new File(rootPath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(dataFile, true);
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteFile() {
        File dataFile = new File(rootPath, fileName);
        dataFile.delete();
    }


}
