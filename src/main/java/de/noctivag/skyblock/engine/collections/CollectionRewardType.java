package de.noctivag.skyblock.engine.collections;

/**
 * Represents the type of reward for collections
 */
public enum CollectionRewardType {
    COINS("Coins"),
    EXPERIENCE("Experience"),
    ITEM("Item"),
    RECIPE("Recipe"),
    UNLOCK("Unlock"),
    ACHIEVEMENT("Achievement"),
    TITLE("Title"),
    PET("Pet"),
    MINION("Minion"),
    ENCHANTMENT("Enchantment"),
    REFORGE("Reforge"),
    SKILL_POINT("Skill Point"),
    STAT_BOOST("Stat Boost"),
    SKYBLOCK_XP("SkyBlock XP"),
    STAT_BONUS("Stat Bonus"),
    COIN_BONUS("Coin Bonus"),
    ITEM_REWARD("Item Reward"),
    PERMISSION("Permission"),
    COSMETIC("Cosmetic"),
    OTHER("Other");

    private final String displayName;

    CollectionRewardType(String displayName) {
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
