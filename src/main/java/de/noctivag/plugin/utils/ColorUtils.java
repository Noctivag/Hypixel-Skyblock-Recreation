package de.noctivag.plugin.utils;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * Color Utilities - Text formatting and color management
 * 
 * Features:
 * - Legacy color code support
 * - Modern Adventure API integration
 * - Gradient text support
 * - Rainbow text effects
 * - Hex color support
 */
public class ColorUtils {
    
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = 
        LegacyComponentSerializer.builder().character('&').build();
    
    /**
     * Translate legacy color codes to Adventure Component
     */
    public static Component translate(String text) {
        if (text == null) return Component.empty();
        return LEGACY_SERIALIZER.deserialize(text);
    }
    
    /**
     * Translate legacy color codes to legacy ChatColor
     */
    public static String translateLegacy(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    /**
     * Remove all color codes from text
     */
    public static String stripColor(String text) {
        if (text == null) return "";
        return ChatColor.stripColor(text);
    }
    
    /**
     * Create gradient text
     */
    public static Component gradient(String text, String startColor, String endColor) {
        if (text == null || text.isEmpty()) return Component.empty();
        
        Component result = Component.empty();
        int length = text.length();
        
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            double ratio = (double) i / (length - 1);
            
            // Simple gradient implementation
            String color = ratio < 0.5 ? startColor : endColor;
            result = result.append(translate(color + c));
        }
        
        return result;
    }
    
    /**
     * Create rainbow text
     */
    public static Component rainbow(String text) {
        if (text == null || text.isEmpty()) return Component.empty();
        
        String[] colors = {"&c", "&6", "&e", "&a", "&b", "&9", "&d"};
        Component result = Component.empty();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String color = colors[i % colors.length];
            result = result.append(translate(color + c));
        }
        
        return result;
    }
    
    /**
     * Format number with color based on value
     */
    public static Component formatNumber(double number) {
        String color;
        if (number >= 1000000) {
            color = "&6"; // Gold for millions
        } else if (number >= 1000) {
            color = "&e"; // Yellow for thousands
        } else if (number >= 100) {
            color = "&a"; // Green for hundreds
        } else {
            color = "&f"; // White for small numbers
        }
        
        return translate(color + formatNumberString(number));
    }
    
    /**
     * Format number as string with appropriate suffixes
     */
    public static String formatNumberString(double number) {
        if (number >= 1000000000) {
            return String.format("%.1fB", number / 1000000000);
        } else if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000);
        } else {
            return String.format("%.0f", number);
        }
    }
    
    /**
     * Create progress bar
     */
    public static Component progressBar(double current, double max, int length, String filledColor, String emptyColor) {
        if (max <= 0) return translate("&cInvalid max value");
        
        double percentage = Math.min(100, (current / max) * 100);
        int filledLength = (int) ((percentage / 100) * length);
        
        StringBuilder bar = new StringBuilder();
        bar.append("&7[");
        
        for (int i = 0; i < length; i++) {
            if (i < filledLength) {
                bar.append(filledColor).append("█");
            } else {
                bar.append(emptyColor).append("█");
            }
        }
        
        bar.append("&7] &f").append(String.format("%.1f%%", percentage));
        
        return translate(bar.toString());
    }
    
    /**
     * Create centered text
     */
    public static Component centerText(String text, int lineLength) {
        if (text == null) return Component.empty();
        
        String stripped = stripColor(text);
        int textLength = stripped.length();
        
        if (textLength >= lineLength) {
            return translate(text);
        }
        
        int spaces = (lineLength - textLength) / 2;
        StringBuilder centered = new StringBuilder();
        
        for (int i = 0; i < spaces; i++) {
            centered.append(" ");
        }
        
        centered.append(text);
        
        return translate(centered.toString());
    }
    
    /**
     * Create title with subtitle
     */
    public static Component createTitle(String title, String subtitle) {
        return translate("&6&l" + title + "\n&7" + subtitle);
    }
    
    /**
     * Create error message
     */
    public static Component error(String message) {
        return translate("&c&lError: &f" + message);
    }
    
    /**
     * Create success message
     */
    public static Component success(String message) {
        return translate("&a&lSuccess: &f" + message);
    }
    
    /**
     * Create warning message
     */
    public static Component warning(String message) {
        return translate("&e&lWarning: &f" + message);
    }
    
    /**
     * Create info message
     */
    public static Component info(String message) {
        return translate("&b&lInfo: &f" + message);
    }
    
    // Legacy compatibility methods
    
    /**
     * Parse color codes from a string (alias for translateLegacy)
     *
     * @param message The message to parse colors for.
     * @return The message with parsed color codes.
     */
    public static String parseColor(String message) {
        return translateLegacy(message);
    }
    
    /**
     * Translate a string using the '&' character as an alternate color code.
     *
     * @param message The message to translate.
     * @return The translated message.
     */
    public static String translateChatColor(String message) {
        return translateLegacy(message);
    }
    
    /**
     * Strips all color codes from a string.
     *
     * @param message The message to strip color from.
     * @return The message without color codes.
     */
    public static String stripChatColor(String message) {
        return stripColor(message);
    }
}
