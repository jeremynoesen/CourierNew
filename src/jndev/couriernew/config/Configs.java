package jndev.couriernew.config;

/**
 * initialize all configs
 */
public class Configs {
    
    /**
     * main config instance
     */
    private static ConfigManager config = new ConfigManager(ConfigType.CONFIG);
    
    /**
     * message config instance
     */
    private static ConfigManager message = new ConfigManager(ConfigType.MESSAGE);
    
    /**
     * @param type config type
     * @return config manager for the selected type
     */
    public static ConfigManager getConfig(ConfigType type) {
        
        switch (type) {
            case MESSAGE:
                return message;
            
            case CONFIG:
                return config;
            
        }
        
        return null;
    }
    
}
