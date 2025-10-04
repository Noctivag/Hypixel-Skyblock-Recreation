package de.noctivag.plugin.messages;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.config.ConfigManager;
import de.noctivag.plugin.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Message Manager - Centralized message system
 * 
 * Features:
 * - Multi-language support
 * - Message templates
 * - Player-specific messages
 * - Broadcast functionality
 * - Message formatting
 */
public class MessageManager {
    
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final Map<String, String> messages;
    private final Map<String, String> playerLanguages;
    private String defaultLanguage;
    
    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
        this.configManager = null; // Will be set later if needed
        this.messages = new HashMap<>();
        this.playerLanguages = new HashMap<>();
        this.defaultLanguage = "en";
        
        loadMessages();
    }
    
    public MessageManager(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messages = new HashMap<>();
        this.playerLanguages = new HashMap<>();
        this.defaultLanguage = "en";
        
        loadMessages();
    }
    
    /**
     * Load messages from configuration
     */
    private void loadMessages() {
        // Load default messages
        loadLanguageMessages(defaultLanguage);
        
        // Load other languages if available
        // This would typically load from language files
    }
    
    /**
     * Load messages for a specific language
     */
    private void loadLanguageMessages(String language) {
        // This would load from language-specific config files
        // For now, we'll use hardcoded messages
        loadDefaultMessages();
    }
    
    /**
     * Load default English messages
     */
    private void loadDefaultMessages() {
        // Common messages
        messages.put("prefix", "&6[BasicsPlugin] &r");
        messages.put("no_permission", "&cYou don't have permission to use this command!");
        messages.put("player_not_found", "&cPlayer not found!");
        messages.put("invalid_arguments", "&cInvalid arguments!");
        messages.put("command_disabled", "&cThis command is currently disabled!");
        
        // Economy messages
        messages.put("insufficient_funds", "&cYou don't have enough coins!");
        messages.put("money_received", "&aYou received &6{amount} &acoins!");
        messages.put("money_sent", "&aYou sent &6{amount} &acoins to &e{player}&a!");
        messages.put("balance", "&aYour balance: &6{amount} &acoins");
        
        // Skill messages
        messages.put("skill_level_up", "&a&lSKILL LEVEL UP! &r&e{skill} &aLevel &6{level}&a!");
        messages.put("experience_gained", "&a+{amount} &e{skill} &aXP");
        messages.put("skill_info", "&e{skill}: &aLevel {level} &7({current}/{needed} XP)");
        
        // Pet messages
        messages.put("pet_summoned", "&aYou summoned your &e{pet} &apet!");
        messages.put("pet_dismissed", "&aYou dismissed your pet!");
        messages.put("pet_fed", "&aYou fed your pet!");
        messages.put("pet_level_up", "&a&lPET LEVEL UP! &r&e{pet} &ais now level &6{level}&a!");
        
        // Collection messages
        messages.put("collection_added", "&a+{amount} &e{material} &a({total} total)");
        messages.put("collection_milestone", "&a&lCOLLECTION MILESTONE! &r&e{material} &a{amount}!");
        
        // Error messages
        messages.put("error_generic", "&cAn error occurred! Please try again later.");
        messages.put("error_database", "&cDatabase error! Please contact an administrator.");
        messages.put("error_config", "&cConfiguration error! Please contact an administrator.");
        
        // Success messages
        messages.put("success_generic", "&aOperation completed successfully!");
        messages.put("success_saved", "&aData saved successfully!");
        messages.put("success_loaded", "&aData loaded successfully!");
    }
    
    /**
     * Get a message by key
     */
    public String getMessage(String key) {
        return getMessage(key, defaultLanguage);
    }
    
    /**
     * Get a message by key for specific language
     */
    public String getMessage(String key, String language) {
        String message = messages.get(key);
        if (message == null) {
            plugin.getLogger().log(Level.WARNING, "Message key not found: " + key);
            return "&cMessage not found: " + key;
        }
        return message;
    }
    
    /**
     * Get a message with replacements
     */
    public String getMessage(String key, Map<String, String> replacements) {
        return getMessage(key, defaultLanguage, replacements);
    }
    
    /**
     * Get a message with replacements for specific language
     */
    public String getMessage(String key, String language, Map<String, String> replacements) {
        String message = getMessage(key, language);
        
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        
        return message;
    }
    
    /**
     * Get a message as Component
     */
    public Component getMessageComponent(String key) {
        return ColorUtils.translate(getMessage(key));
    }
    
    /**
     * Get a message as Component with replacements
     */
    public Component getMessageComponent(String key, Map<String, String> replacements) {
        return ColorUtils.translate(getMessage(key, replacements));
    }
    
    /**
     * Send message to player
     */
    public void sendMessage(Player player, String key) {
        player.sendMessage(getMessageComponent(key));
    }
    
    /**
     * Send message to player with replacements
     */
    public void sendMessage(Player player, String key, Map<String, String> replacements) {
        player.sendMessage(getMessageComponent(key, replacements));
    }
    
    /**
     * Send message to player with prefix
     */
    public void sendMessageWithPrefix(Player player, String key) {
        Component prefix = ColorUtils.translate(getMessage("prefix"));
        Component message = getMessageComponent(key);
        player.sendMessage(prefix.append(message));
    }
    
    /**
     * Send message to player with prefix and replacements
     */
    public void sendMessageWithPrefix(Player player, String key, Map<String, String> replacements) {
        Component prefix = ColorUtils.translate(getMessage("prefix"));
        Component message = getMessageComponent(key, replacements);
        player.sendMessage(prefix.append(message));
    }
    
    /**
     * Broadcast message to all players
     */
    public void broadcastMessage(String key) {
        Component message = getMessageComponent(key);
        plugin.getServer().broadcast(message);
    }
    
    /**
     * Broadcast message to all players with replacements
     */
    public void broadcastMessage(String key, Map<String, String> replacements) {
        Component message = getMessageComponent(key, replacements);
        plugin.getServer().broadcast(message);
    }
    
    /**
     * Broadcast message with prefix
     */
    public void broadcastMessageWithPrefix(String key) {
        Component prefix = ColorUtils.translate(getMessage("prefix"));
        Component message = getMessageComponent(key);
        plugin.getServer().broadcast(prefix.append(message));
    }
    
    /**
     * Set player language
     */
    public void setPlayerLanguage(Player player, String language) {
        playerLanguages.put(player.getUniqueId().toString(), language);
    }
    
    /**
     * Get player language
     */
    public String getPlayerLanguage(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId().toString(), defaultLanguage);
    }
    
    /**
     * Set default language
     */
    public void setDefaultLanguage(String language) {
        this.defaultLanguage = language;
    }
    
    /**
     * Get default language
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }
    
    /**
     * Add or update a message
     */
    public void setMessage(String key, String message) {
        messages.put(key, message);
    }
    
    /**
     * Remove a message
     */
    public void removeMessage(String key) {
        messages.remove(key);
    }
    
    /**
     * Check if message exists
     */
    public boolean hasMessage(String key) {
        return messages.containsKey(key);
    }
    
    /**
     * Get all message keys
     */
    public String[] getMessageKeys() {
        return messages.keySet().toArray(new String[0]);
    }
    
    /**
     * Reload messages
     */
    public void reloadMessages() {
        messages.clear();
        loadMessages();
    }
    
    /**
     * Set language (alias for setDefaultLanguage)
     */
    public void setLanguage(String language) {
        setDefaultLanguage(language);
    }
    
    /**
     * Get language (alias for getDefaultLanguage)
     */
    public String getLanguage() {
        return getDefaultLanguage();
    }
}
