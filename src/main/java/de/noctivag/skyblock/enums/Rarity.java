package de.noctivag.skyblock.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * Enum für die Seltenheitsstufen von Accessoires
 * Definiert Farben, Magical Power und Beschreibungen
 */
public enum Rarity {
    
    COMMON("§f", NamedTextColor.WHITE, 3, "Gewöhnlich"),
    UNCOMMON("§a", NamedTextColor.GREEN, 5, "Ungewöhnlich"),
    RARE("§9", NamedTextColor.BLUE, 8, "Selten"),
    EPIC("§5", NamedTextColor.DARK_PURPLE, 12, "Episch"),
    LEGENDARY("§6", NamedTextColor.GOLD, 18, "Legendär"),
    MYTHIC("§d", NamedTextColor.LIGHT_PURPLE, 25, "Mythisch"),
    DIVINE("§b", NamedTextColor.AQUA, 35, "Göttlich"),
    SPECIAL("§c", NamedTextColor.RED, 50, "Speziell"),
    VERY_SPECIAL("§4", NamedTextColor.DARK_RED, 75, "Sehr Speziell");
    
    private final String colorCode;
    private final TextColor textColor;
    private final int magicalPower;
    private final String germanName;
    
    Rarity(String colorCode, TextColor textColor, int magicalPower, String germanName) {
        this.colorCode = colorCode;
        this.textColor = textColor;
        this.magicalPower = magicalPower;
        this.germanName = germanName;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public TextColor getTextColor() {
        return textColor;
    }
    
    public int getMagicalPower() {
        return magicalPower;
    }
    
    public String getGermanName() {
        return germanName;
    }
    
    /**
     * Gibt die nächste Seltenheitsstufe zurück
     * @return Nächste Rarity oder null wenn bereits höchste
     */
    public Rarity getNext() {
        Rarity[] values = values();
        int nextIndex = ordinal() + 1;
        return nextIndex < values.length ? values[nextIndex] : null;
    }
    
    /**
     * Gibt die vorherige Seltenheitsstufe zurück
     * @return Vorherige Rarity oder null wenn bereits niedrigste
     */
    public Rarity getPrevious() {
        int prevIndex = ordinal() - 1;
        return prevIndex >= 0 ? values()[prevIndex] : null;
    }
    
    /**
     * Prüft ob diese Rarity höher ist als die andere
     * @param other Die andere Rarity
     * @return true wenn höher
     */
    public boolean isHigherThan(Rarity other) {
        return ordinal() > other.ordinal();
    }
    
    /**
     * Prüft ob diese Rarity niedriger ist als die andere
     * @param other Die andere Rarity
     * @return true wenn niedriger
     */
    public boolean isLowerThan(Rarity other) {
        return ordinal() < other.ordinal();
    }
}
