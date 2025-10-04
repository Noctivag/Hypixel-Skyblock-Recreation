package de.noctivag.plugin.slayers;
import org.bukkit.inventory.ItemStack;

public enum SlayerType {
    ZOMBIE("Zombie"),
    SPIDER("Spider"),
    WOLF("Wolf"),
    ENDERMAN("Enderman"),
    BLAZE("Blaze");

    private final String displayName;

    SlayerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
