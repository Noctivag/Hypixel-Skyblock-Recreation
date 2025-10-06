package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.skills.PlayerSkills;
import de.noctivag.skyblock.skills.SkillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Skills GUI - Displays all available skills and their progress
 */
public class SkillsGUI extends Menu {
    
    public SkillsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lSkills", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Get player skills data
        PlayerSkills playerSkills = plugin.getSkillsSystem().getPlayerSkills(player);
        if (playerSkills == null) {
            player.sendMessage("§cError loading skills data!");
            return;
        }
        
        // Combat Skill
        setupSkillItem(10, Material.DIAMOND_SWORD, SkillType.COMBAT, playerSkills);
        
        // Mining Skill
        setupSkillItem(12, Material.DIAMOND_PICKAXE, SkillType.MINING, playerSkills);
        
        // Farming Skill
        setupSkillItem(14, Material.WHEAT, SkillType.FARMING, playerSkills);
        
        // Foraging Skill
        setupSkillItem(16, Material.OAK_LOG, SkillType.FORAGING, playerSkills);
        
        // Fishing Skill
        setupSkillItem(19, Material.FISHING_ROD, SkillType.FISHING, playerSkills);
        
        // Enchanting Skill
        setupSkillItem(21, Material.ENCHANTING_TABLE, SkillType.ENCHANTING, playerSkills);
        
        // Alchemy Skill
        setupSkillItem(23, Material.BREWING_STAND, SkillType.ALCHEMY, playerSkills);
        
        // Carpentry Skill
        setupSkillItem(25, Material.CRAFTING_TABLE, SkillType.CARPENTRY, playerSkills);
        
        // Runecrafting Skill
        setupSkillItem(28, Material.END_CRYSTAL, SkillType.RUNECRAFTING, playerSkills);
        
        // Taming Skill
        setupSkillItem(30, Material.BONE, SkillType.TAMING, playerSkills);
        
        // Social Skill
        setupSkillItem(32, Material.PLAYER_HEAD, SkillType.SOCIAL, playerSkills);
        
        // Catacombs Skill
        setupSkillItem(34, Material.WITHER_SKELETON_SKULL, SkillType.CATACOMBS, playerSkills);
        
        // Close Button
        setCloseButton(49);
    }
    
    private void setupSkillItem(int slot, Material material, SkillType skillType, PlayerSkills playerSkills) {
        int level = playerSkills.getLevel(skillType);
        long xpProgress = playerSkills.getXPProgress(skillType);
        long xpRequired = playerSkills.getXPRequiredForNextLevel(skillType);
        
        String rarity = getRarityForLevel(level);
        String[] lore = {
            "&7" + skillType.getDescription(),
            "&7Level: &a" + level,
            "&7XP: &e" + xpProgress + "/" + xpRequired,
            "",
            "&7" + skillType.getBenefits(),
            "",
            "&eKlicke zum Öffnen"
        };
        
        setItem(slot, material, skillType.getColor() + skillType.getIcon() + " " + skillType.getDisplayName(), rarity, lore);
    }
    
    private String getRarityForLevel(int level) {
        if (level >= 50) return "mythic";
        if (level >= 40) return "legendary";
        if (level >= 30) return "epic";
        if (level >= 20) return "rare";
        if (level >= 10) return "uncommon";
        return "common";
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Get player skills data
        PlayerSkills playerSkills = plugin.getSkillsSystem().getPlayerSkills(player);
        if (playerSkills == null) {
            player.sendMessage("§cError loading skills data!");
            return;
        }
        
        switch (slot) {
            case 10:
                showSkillDetails(SkillType.COMBAT, playerSkills);
                break;
            case 12:
                showSkillDetails(SkillType.MINING, playerSkills);
                break;
            case 14:
                showSkillDetails(SkillType.FARMING, playerSkills);
                break;
            case 16:
                showSkillDetails(SkillType.FORAGING, playerSkills);
                break;
            case 19:
                showSkillDetails(SkillType.FISHING, playerSkills);
                break;
            case 21:
                showSkillDetails(SkillType.ENCHANTING, playerSkills);
                break;
            case 23:
                showSkillDetails(SkillType.ALCHEMY, playerSkills);
                break;
            case 25:
                showSkillDetails(SkillType.CARPENTRY, playerSkills);
                break;
            case 28:
                showSkillDetails(SkillType.RUNECRAFTING, playerSkills);
                break;
            case 30:
                showSkillDetails(SkillType.TAMING, playerSkills);
                break;
            case 32:
                showSkillDetails(SkillType.SOCIAL, playerSkills);
                break;
            case 34:
                showSkillDetails(SkillType.CATACOMBS, playerSkills);
                break;
            case 49:
                close();
                break;
        }
    }
    
    private void showSkillDetails(SkillType skillType, PlayerSkills playerSkills) {
        int level = playerSkills.getLevel(skillType);
        long xpProgress = playerSkills.getXPProgress(skillType);
        long xpRequired = playerSkills.getXPRequiredForNextLevel(skillType);
        long totalXP = playerSkills.getXP(skillType);
        
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage(skillType.getColor() + skillType.getIcon() + " " + skillType.getDisplayName() + " §7Level " + level);
        player.sendMessage("§7" + skillType.getDescription());
        player.sendMessage("");
        player.sendMessage("§7Total XP: §e" + totalXP);
        player.sendMessage("§7Progress: §e" + xpProgress + "§7/§e" + xpRequired);
        player.sendMessage("§7Benefits: §a" + skillType.getBenefits());
        player.sendMessage("§8§m--------------------------------");
    }
}
