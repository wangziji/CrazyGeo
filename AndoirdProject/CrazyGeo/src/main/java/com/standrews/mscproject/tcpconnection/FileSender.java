/*
 * FileSender.java
 *
 * Created on: 29 /6 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tcpconnection;

import android.content.Context;
import android.os.Environment;

import com.standrews.mscproject.utils.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by ziji on 13-6-7.
 */
public class FileSender {

    private String ip, port, dir;
    private Configuration mConfiguration;

    public FileSender(Context context) {
        mConfiguration = new Configuration();
        Properties properties = mConfiguration.getConfigProperties(context);
        this.ip = properties.getProperty("SERV_IP");
        this.port = properties.getProperty("SERV_PORT");
        this.dir = properties.getProperty("DATA_DIR");
    }

    public int send() {
        Socket socket = null;
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            socket = new Socket(serverAddr, Integer.parseInt(port));
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }


        File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
        if (!rootPath.exists()) {
            rootPath.mkdir();
            return 0;
        } else {
            File[] list = rootPath.listFiles();
            if (list.length == 0) {
                return 0;
            } else {
                for (File file : list) {
                    FileInputStream fis = null;
                    BufferedReader br = null;
                    DataOutputStream dos = null;
                    try {
                        fis = new FileInputStream(file);
                        br = new BufferedReader(new InputStreamReader(fis));
                        dos = new DataOutputStream(socket.getOutputStream());

                        int buffSize = 8192;
                        byte[] buff = new byte[buffSize];
                        while (true) {
                            int read = 0;
                            if (br != null) {
                                read = fis.read(buff);
                            }
                            if (read == -1) {
                                break;
                            }
                            dos.write(buff, 0, read);
                        }
                        dos.flush();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return 2;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 2;
                    } finally {
                        file.delete();
                        try {
                            br.close();
                            fis.close();
                            dos.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;
            }

        }
    }


}
