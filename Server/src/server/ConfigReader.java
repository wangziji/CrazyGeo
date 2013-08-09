/*
 * This class is for read the configuration file
 */
package server;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author ziji
 */
public class ConfigReader {

    //the path of config file
    private String path;

    /**
     * The constructor of config reader
     * @param path 
     *      the path of config file
     */
    public ConfigReader(String path) {
        this.path = path;
    }

    /**
     * Read the text content from a xml node
     * @param attr 
     *      the attrubute
     * @return 
     *      the content
     */
    public String read(ConfigAttributes attr) {
        String msg=null;
        try {
            SAXReader reader = new SAXReader();
            File file = new File(path);
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            msg = root.element(attr.getInfo()).getText();
            //System.out.println(msg);
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return msg;
    }

    /**
     * 
     * @return 
     *      the path of config file
     */
    public String getPath() {
        return path;
    }

    /**
     * 
     * @param path 
     *     the path of config file
     */
    public void setPath(String path) {
        this.path = path;
    }

    
}
