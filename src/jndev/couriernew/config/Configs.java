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
     * outgoing config instance
     */
    private static Config outgoing = new Config(ConfigType.OUTGOING);
    
    /**
     * postmen config instance
     */
    private static Config postmen = new Config(ConfigType.POSTMEN);
    
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
                
            case POSTMEN:
                return postmen;
                
            case OUTGOING:
                return outgoing;
            
        }
        
        return null;
    }
    
}
