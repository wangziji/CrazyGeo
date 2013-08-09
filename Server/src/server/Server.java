/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ziji
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean flag = true;
        try {
            byte[] ip = InetAddress.getLocalHost().getAddress();
            System.out.println((int)ip[0]+"."+(int)ip[1]+"."+(int)ip[2]+"."+(int)ip[3]);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        try {
            
            ConfigReader cr = new ConfigReader("/home/zw21/config/config.xml");
            String path = cr.read(ConfigAttributes.DATA_DIR);
            String cache = cr.read(ConfigAttributes.CACHE);

            // create the thread pool and socket
            ExecutorService pool = Executors.newCachedThreadPool();
            ServerSocket server = new ServerSocket(12345);

            System.out.println("Server Started");
            while (flag) {

                // if the server socket accepted start a new thread from thread
                // pool
                Socket socket = server.accept();
                pool.execute(new ServerThread(socket, path,cache));
            }

            // close the socket and thread pool
            server.close();
            pool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}