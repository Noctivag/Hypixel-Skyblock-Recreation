package de.noctivag.skyblock.api;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.rewards.DailyReward;
import org.bukkit.entity.Player;
import java.time.LocalDateTime;

/**
 * API interface for managing daily rewards in the plugin.
 */
public interface RewardAPI {
    /**
     * Claims the daily reward for a player.
     *
     * @param player The player claiming the reward
     */
    void claimReward(Player player);

    /**
     * Gets a specific daily reward.
     *
     * @param day The day number of the reward
     * @return The reward for that day, or null if not found
     */
    DailyReward getReward(int day);

    /**
     * Gets the current streak of a player.
     *
     * @param player The player to check
     * @return The current streak count
     */
    int getCurrentStreak(Player player);

    /**
     * Gets the last time a player claimed their reward.
     *
     * @param player The player to check
     * @return The last claim time, or null if never claimed
     */
    LocalDateTime getLastClaimTime(Player player);

    /**
     * Sets a reward for a specific day.
     *
     * @param day The day number
     * @param reward The reward to set
     */
    void setReward(int day, DailyReward reward);

    /**
     * Removes a reward for a specific day.
     *
     * @param day The day number to remove
     */
    void removeReward(int day);
}
