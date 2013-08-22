/*
 * ExternalFileManager.java
 *
 * Created on: 22 /8 /2013
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
 * This utility is for read and write file to external storage
 *
 * Created by Ziji Wang on 13-6-2.
 */
public class ExternalFileManager {

    private String path;
    private String fileName;
    private File rootPath;

    /**
     * Constructor
     * @param path String, path
     * @param fileName String, filename
     */
    public ExternalFileManager(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        rootPath = new File(Environment.getExternalStorageDirectory(), path);
        if (!rootPath.exists()) {
            rootPath.mkdir();
        }
    }

    /**
     * delete file
     */
    public void deleteFile() {
        File dataFile = new File(rootPath, fileName);
        dataFile.delete();
    }

    /**
     * Get file name
     * @return String file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set file name
     * @param fileName String file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get path
     * @return String,path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set path
     * @param path String, path
     */
    public void setPath(String path) {
        this.path = path;
        rootPath = new File(Environment.getExternalStorageDirectory(), path);
    }

    /**
     * Write String the file
     * @param str String, message
     */
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

    /**
     * Check whether the external storage available
     * @return boolean, true = available; false = not available
     */
    private boolean isExternalStorageAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


}
