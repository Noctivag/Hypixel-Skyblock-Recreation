package de.noctivag.plugin.player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
    private int level;
    private double exp;

    public PlayerData(int level, double exp) {
        this.level = level;
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public void addExp(double amount) {
        this.exp += amount;
    }

    public boolean canLevelUp() {
        return exp >= getExpToNextLevel();
    }

    public void levelUp() {
        exp -= getExpToNextLevel();
        level++;
    }

    public double getExpToNextLevel() {
        // Exponentielles Wachstum: Jedes Level braucht mehr EXP
        return 100 * Math.pow(1.5, level - 1);
    }
}
