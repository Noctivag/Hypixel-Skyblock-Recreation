package de.noctivag.skyblock.rewards;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

public class DailyReward {
    private final int day;
    private final List<RewardItem> rewards;
    private final boolean isSpecialReward;
    private final Material displayIcon;

    public DailyReward(int day, List<RewardItem> rewards, boolean isSpecialReward, Material displayIcon) {
        this.day = day;
        this.rewards = rewards;
        this.isSpecialReward = isSpecialReward;
        this.displayIcon = displayIcon;
    }

    public int getDay() {
        return day;
    }

    public List<RewardItem> getRewards() {
        return rewards;
    }

    public boolean isSpecialReward() {
        return isSpecialReward;
    }

    public Material getDisplayIcon() {
        return displayIcon;
    }

    public static class RewardItem {
        private final RewardType type;
        private final Object value;
        private final int amount;

        public RewardItem(RewardType type, Object value, int amount) {
            this.type = type;
            this.value = value;
            this.amount = amount;
        }

        public RewardType getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public int getAmount() {
            return amount;
        }
    }

    public enum RewardType {
        COINS("Coins", Material.GOLD_INGOT),
        EXP("Erfahrung", Material.EXPERIENCE_BOTTLE),
        ITEM("Item", Material.CHEST),
        KIT("Kit", Material.DIAMOND_CHESTPLATE),
        PERMISSION("Berechtigung", Material.NAME_TAG);

        private final String displayName;
        private final Material icon;

        RewardType(String displayName, Material icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Material getIcon() {
            return icon;
        }
    }
}
