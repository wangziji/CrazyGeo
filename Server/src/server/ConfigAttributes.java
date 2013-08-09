/*
 * This class defined the attributes in configuration file
 */
package server;

/**
 *
 * @author ziji
 */
public enum ConfigAttributes {
    /**
     * represent the dircotry of data
     */
    DATA_DIR{
        @Override
        String getInfo(){
            return "data_dir";
        }
    },   
    
    CACHE{
        @Override
        String getInfo(){
            return "local_cache";
        }
    };    
    
    /**
     * get the String value of enum element
     * 1 - DATA_DIR
     * 2 - CACHE
     * @return 
     *     the integer value
     */
    abstract String getInfo();
}
