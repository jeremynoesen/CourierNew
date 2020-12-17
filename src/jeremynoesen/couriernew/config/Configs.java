package jeremynoesen.couriernew.config;

/**
 * initialize all configs
 *
 * @author Jeremy Noesen
 */
public class Configs {
    
    /**
     * main config instance
     */
    private static final Config main = new Config(ConfigType.MAIN);
    
    /**
     * outgoing config instance
     */
    private static final Config outgoing = new Config(ConfigType.OUTGOING);
    
    /**
     * couriers config instance
     */
    private static final Config couriers = new Config(ConfigType.COURIERS);
    
    /**
     * message config instance
     */
    private static final Config message = new Config(ConfigType.MESSAGE);
    
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
                return couriers;
            
            case OUTGOING:
                return outgoing;
            
        }
        
        return null;
    }
    
}
