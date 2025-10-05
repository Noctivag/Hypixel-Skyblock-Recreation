package de.noctivag.skyblock.mobs;

/**
 * Represents different types of mobs in the system
 */
public enum MobType {
    ZOMBIE("Zombie"),
    SKELETON("Skeleton"),
    SPIDER("Spider"),
    CREEPER("Creeper"),
    ENDERMAN("Enderman"),
    BLAZE("Blaze"),
    GHAST("Ghast"),
    MAGMA_CUBE("Magma Cube"),
    SLIME("Slime"),
    WITCH("Witch"),
    GUARDIAN("Guardian"),
    ELDER_GUARDIAN("Elder Guardian"),
    WITHER_SKELETON("Wither Skeleton"),
    HUSK("Husk"),
    STRAY("Stray"),
    VEX("Vex"),
    VINDICATOR("Vindicator"),
    EVOKER("Evoker"),
    PILLAGER("Pillager"),
    RAVAGER("Ravager"),
    SHULKER("Shulker"),
    ENDERMITE("Endermite"),
    SILVERFISH("Silverfish"),
    PHANTOM("Phantom"),
    DROWNED("Drowned"),
    PIGLIN("Piglin"),
    HOGLIN("Hoglin"),
    ZOGLIN("Zoglin"),
    PIGLIN_BRUTE("Piglin Brute"),
    STRIDER("Strider"),
    WARDEN("Warden"),
    ALLAY("Allay"),
    FROG("Frog"),
    TADPOLE("Tadpole"),
    OTHER("Other");

    private final String displayName;

    MobType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
