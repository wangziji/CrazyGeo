/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testServer;

import org.junit.After;
import org.junit.Before;
import server.ConfigAttributes;
import server.ConfigReader;
import static org.junit.Assert.*;

/**
 *
 * @author ziji
 */
public class ConfigReaderTest {
    
    String expect = null;
    ConfigReader cr = null;
    
    public ConfigReaderTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        this.cr = new ConfigReader("/home/ziji/Android/project/Server/config/config.xml");
        this.expect = "/home/ziji/Android/project/Server/data";
    }
    
    @After
    public void tearDown() {
    }

    @org.junit.Test
    public void testRead() {
        String result = cr.read(ConfigAttributes.DATA_DIR);
        assertEquals(expect,result);
    }
}
