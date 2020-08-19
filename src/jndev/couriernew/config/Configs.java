package jndev.couriernew.config;

/**
 * initialize all configs
 */
public class Configs {
    
    /**
     * main config instance
     */
    private static Config main = new Config(ConfigType.MAIN);
    
    /**
     * outgoing config instance
     */
    private static Config outgoing = new Config(ConfigType.OUTGOING);
    
    /**
     * postmen config instance
     */
    private static Config postmen = new Config(ConfigType.COURIERS);
    
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
            
            case MAIN:
                return main;
            
            case COURIERS:
                return postmen;
            
            case OUTGOING:
                return outgoing;
            
        }
        
        return null;
    }
    
}
