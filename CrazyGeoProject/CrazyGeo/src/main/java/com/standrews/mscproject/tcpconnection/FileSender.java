/*
 * FileSender.java
 *
 * Created on: 22 /8 /2013
 *
 * Copyright (c) 2013 Ziji Wang and University of St. Andrews. All Rights Reserved.
 * This software is the proprietary information of University of St. Andrews.
 */

package com.standrews.mscproject.tcpconnection;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.standrews.mscproject.utils.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/**
 * MSc project
 * <p/>
 * This class responsible for build TCP/IP connection and send file
 *
 * Created by Ziji Wang on 13-6-7.
 */
public class FileSender {

    private String ip, port, dir;

    /**
     * Constructor
     * @param context
     */
    public FileSender(Context context) {
        Configuration mConfiguration = new Configuration();
        Properties properties = mConfiguration.getConfigProperties(context);
        this.ip = properties.getProperty("SERV_IP");
        this.port = properties.getProperty("SERV_PORT");
        this.dir = properties.getProperty("DATA_DIR");
    }

    /**
     * Build connection and send file
     * @return int, the result: 0 = no file; 1 = success; 2 = IO exception; 3 = network exception
     */
    public int send() {
        Socket socket = null;
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
                        InetAddress serverAddr = InetAddress.getByName(ip);
                        socket = new Socket(serverAddr, Integer.parseInt(port));
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
                        socket.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return 2;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                        Log.e("me", e.toString());
                        return 3;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 2;
                    } finally {
                        try {
                            br.close();
                            fis.close();
                            dos.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                for (File file : list) {
                    file.delete();
                }
                return 1;
            }

        }
    }


}
