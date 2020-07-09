package jndev.couriernew.config;

import jndev.couriernew.CourierNew;

import java.io.File;
import java.io.InputStream;

/**
 * enum to make getting separate configs easier
 */
public enum ConfigType {
    MAIN("config.yml"), MESSAGE("messages.yml"), COURIERS("couriers.yml"), OUTGOING("outgoing.yml");
    
    /**
     * config type file name
     */
    public String fileName;
    
    /**
     * @param file file name associated with the type
     */
    ConfigType(String file) {
        this.fileName = file;
    }
    
    /**
     * @return config file of the matching file name
     */
    public File getFile() {
        return new File(CourierNew.getInstance().getDataFolder(), fileName);
    }
    
    /**
     * @return input stream of resource from inside plugin jar
     */
    public InputStream getResource() {
        return CourierNew.getInstance().getResource(fileName);
    }
    
    /**
     * save resource from plugin jar to plugin folder
     */
    public void saveResource() {
        CourierNew.getInstance().saveResource(fileName, false);
    }
}
