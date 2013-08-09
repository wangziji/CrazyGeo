/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author ziji
 */
class ServerThread implements Runnable {

    private String path, cache;
    private Socket socket;

    public ServerThread(Socket socket, String path, String cache) {
        this.path = path;
        this.socket = socket;
        this.cache = cache;
    }

    @Override
    public void run() {
        try {

            DataInputStream inputStream = null;
            try {
                inputStream = new DataInputStream(new BufferedInputStream(
                        socket.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {

                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];

                cache += "/" + System.currentTimeMillis() + socket.getInetAddress().toString().replace(".", "dot").substring(1) + ".xml";       
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(new BufferedOutputStream(
                        new FileOutputStream(cache))));
                while (true) {
                    int read = 0;
                    if (inputStream != null) {
                        read = inputStream.read(buf);
                    }
                    if (read == -1) {
                        break;
                    }
                    //System.out.println(buf.toString());
                    dos.write(buf, 0, read);
                }

                dos.flush();
                dos.close();
                inputStream.close();
                
                this.redispatch();
                System.out.println("recieve complete:\t" + path + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error handling a client: " + e);
        }
    }

    private void redispatch() throws FileNotFoundException {
        File file = new File(cache);
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            Element header = (Element) root.node(1);
            String id = header.attributeValue("id");
            String date = header.attributeValue("date");

            path += "/" + id;
            File rootPath = new File(path);
            if (!rootPath.exists()) {
                rootPath.mkdir();
            }

            int size = rootPath.listFiles().length+1;
            
            String filename = path + "/" + date + "_"+size+".xml";
            File newFile = new File(filename);
            this.fileTransfer(file, newFile);
            file.delete();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
    }

    private void fileTransfer(File ini_file, File target_file) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            int length = 2097152;
            in = new FileInputStream(ini_file);
            out = new FileOutputStream(target_file);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            ByteBuffer b = null;

            while (true) {
                if (inC.position() == inC.size()) {
                    inC.close();
                    outC.close();
                    break;
                }
                if ((inC.size() - inC.position()) < length) {
                    length = (int) (inC.size() - inC.position());
                } else {
                    length = 2097152;
                }
                b = ByteBuffer.allocateDirect(length);
                inC.read(b);
                b.flip();
                outC.write(b);
                outC.force(false);


            }
            System.out.println("file transfer comlete!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
