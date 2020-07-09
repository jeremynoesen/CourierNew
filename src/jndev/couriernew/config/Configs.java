package jndev.couriernew.config;

/**
 * initialize all configs
 */
public class Configs {
    
    /**
     * main config instance
     */
    private static Config config = new Config(ConfigType.CONFIG);
    
    /**
     * message config instance
     */
    private static Config message = new Config(ConfigType.MESSAGE);
    
    /**
     * @param type config type
     * @return config manager for the selected type
     */
    public static Config getConfig(ConfigType type) {
        
        switch (type) {
            case MESSAGE:
                return message;
            
            case CONFIG:
                return config;
            
        }
        
        return null;
    }
    
}
