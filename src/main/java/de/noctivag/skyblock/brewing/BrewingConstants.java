package de.noctivag.skyblock.brewing;

/**
 * Constants for brewing system
 */
public class BrewingConstants {
    
    // Default brewing times (in ticks)
    public static final int DEFAULT_BREWING_TIME = 200; // 10 seconds
    public static final int FAST_BREWING_TIME = 100; // 5 seconds
    public static final int SLOW_BREWING_TIME = 300; // 15 seconds
    public static final int VERY_SLOW_BREWING_TIME = 500; // 25 seconds
    
    // Default experience rewards
    public static final int DEFAULT_EXPERIENCE = 10;
    public static final int LOW_EXPERIENCE = 5;
    public static final int HIGH_EXPERIENCE = 25;
    public static final int VERY_HIGH_EXPERIENCE = 50;
    
    // Brewing limits
    public static final int MAX_INGREDIENTS = 5;
    public static final int MAX_BREWING_TIME = 1000; // 50 seconds
    public static final int MAX_EXPERIENCE = 100;
    
    // Brewing types
    public static final String POTION_TYPE = "potion";
    public static final String SPLASH_POTION_TYPE = "splash_potion";
    public static final String LINGERING_POTION_TYPE = "lingering_potion";
    public static final String TIPPED_ARROW_TYPE = "tipped_arrow";
    
    // Brewing stages
    public static final String BREWING_STAGE_START = "start";
    public static final String BREWING_STAGE_PROGRESS = "progress";
    public static final String BREWING_STAGE_COMPLETE = "complete";
    public static final String BREWING_STAGE_FAILED = "failed";
    
    // Brewing messages
    public static final String BREWING_START_MESSAGE = "§aStarted brewing: ";
    public static final String BREWING_COMPLETE_MESSAGE = "§aBrewing completed: ";
    public static final String BREWING_FAILED_MESSAGE = "§cBrewing failed: ";
    public static final String BREWING_PROGRESS_MESSAGE = "§eBrewing progress: ";
    
    // Brewing permissions
    public static final String BREWING_USE_PERMISSION = "skyblock.brewing.use";
    public static final String BREWING_ADMIN_PERMISSION = "skyblock.brewing.admin";
    public static final String BREWING_CREATE_PERMISSION = "skyblock.brewing.create";
    public static final String BREWING_DELETE_PERMISSION = "skyblock.brewing.delete";
    
    // Brewing configuration keys
    public static final String BREWING_ENABLED_KEY = "brewing.enabled";
    public static final String BREWING_DEFAULT_TIME_KEY = "brewing.default_time";
    public static final String BREWING_DEFAULT_EXPERIENCE_KEY = "brewing.default_experience";
    public static final String BREWING_CUSTOM_RECIPES_KEY = "brewing.custom_recipes";
    
    // Brewing file names
    public static final String BREWING_CONFIG_FILE = "brewing.yml";
    public static final String BREWING_RECIPES_FILE = "brewing_recipes.yml";
    public static final String BREWING_DATA_FILE = "brewing_data.yml";
    
    // Brewing database tables
    public static final String BREWING_RECIPES_TABLE = "brewing_recipes";
    public static final String BREWING_DATA_TABLE = "brewing_data";
    public static final String BREWING_METRICS_TABLE = "brewing_metrics";
    
    // Brewing API endpoints
    public static final String BREWING_API_BASE = "/api/brewing";
    public static final String BREWING_API_RECIPES = BREWING_API_BASE + "/recipes";
    public static final String BREWING_API_DATA = BREWING_API_BASE + "/data";
    public static final String BREWING_API_METRICS = BREWING_API_BASE + "/metrics";
}
