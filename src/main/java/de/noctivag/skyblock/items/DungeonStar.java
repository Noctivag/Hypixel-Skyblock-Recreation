package de.noctivag.skyblock.items;

/**
 * Dungeon-Star-Upgrade für Items (Hypixel-like)
 */
public class DungeonStar {
    private int stars = 0; // 0-5
    public DungeonStar() {}
    public DungeonStar(int stars) { this.stars = Math.max(0, Math.min(5, stars)); }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = Math.max(0, Math.min(5, stars)); }
    public void addStar() { if (stars < 5) stars++; }
    public String getStarDisplay() {
        return "§e" + "★".repeat(stars) + "§7" + "★".repeat(5 - stars);
    }
}
