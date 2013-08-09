/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ziji
 */
public class TestClient {

    private FileInputStream fis;
    private Socket socket;
    private BufferedReader br;
    private DataOutputStream dos;

    public void sendMessage() {
        File file = new File("/home/ziji/Master-Project-master/TestClient/testData/test.xml");
        try {
            fis = new FileInputStream(file);
            
            socket = new Socket("mscproj02.cs.st-andrews.ac.uk", 12345);
//            socket = new Socket("138.251.214.136", 12345);

            // create the stream reader and writer
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

            System.out.println("transmission complete");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
                br.close();
                fis.close();
                dos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public byte[] encodeIP(String ip) {
        byte[] ipby = new byte[4];
        String[] ips = ip.split("[.]");
       // System.out.println(ips.length);
        for (int i = 0; i < 4; i++) {
            //System.out.println(ips[i]);
            int m = Integer.parseInt(ips[i]);
            byte b = (byte) m;
            if (m > 127) {
                b = (byte) (127 - m);
            }
            //System.out.println(b);
            ipby[i] = b;
        }

        return ipby;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        TestClient tc = new TestClient();
         //System.out.println(tc.encodeIP("192.168.0.1"));
        tc.sendMessage();
    }
}
