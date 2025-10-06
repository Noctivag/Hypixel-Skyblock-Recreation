package de.noctivag.skyblock.pets;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Pet Training System - Hypixel Skyblock Style
 * 
 * Features:
 * - Pet training through specific activities
 * - Training progress tracking
 * - Training rewards and bonuses
 * - Autopet rules system
 * - Pet training GUI interface
 */
public class PetTrainingSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final PetSystem petSystem;
    private final Map<UUID, PlayerPetTraining> playerTraining = new ConcurrentHashMap<>();
    private final Map<UUID, List<AutopetRule>> autopetRules = new ConcurrentHashMap<>();
    
    public PetTrainingSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, PetSystem petSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.petSystem = petSystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Pet Training")) {
            event.setCancelled(true);
            handleTrainingGUIClick(player, event.getSlot());
        } else if (title.contains("Autopet Rules")) {
            event.setCancelled(true);
            handleAutopetGUIClick(player, event.getSlot());
        }
    }
    
    public void openPetTrainingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lPet Training"));
        
        // Training activities
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat Training", 
            Arrays.asList("§7Train your pets through combat", "§7• Kill mobs with pets active", "§7• Gain training XP", "", "§eClick to view"));
        
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining Training", 
            Arrays.asList("§7Train your pets through mining", "§7• Mine ores with pets active", "§7• Gain training XP", "", "§eClick to view"));
        
        addGUIItem(gui, 12, Material.WHEAT, "§a§lFarming Training", 
            Arrays.asList("§7Train your pets through farming", "§7• Harvest crops with pets active", "§7• Gain training XP", "", "§eClick to view"));
        
        addGUIItem(gui, 13, Material.OAK_LOG, "§2§lForaging Training", 
            Arrays.asList("§7Train your pets through foraging", "§7• Chop trees with pets active", "§7• Gain training XP", "", "§eClick to view"));
        
        addGUIItem(gui, 14, Material.FISHING_ROD, "§b§lFishing Training", 
            Arrays.asList("§7Train your pets through fishing", "§7• Catch fish with pets active", "§7• Gain training XP", "", "§eClick to view"));
        
        // Training progress
        PlayerPetTraining training = getPlayerTraining(player.getUniqueId());
        addGUIItem(gui, 19, Material.EXPERIENCE_BOTTLE, "§e§lTraining Progress", 
            Arrays.asList("§7Total Training XP: §e" + String.format("%.1f", training.getTotalTrainingXP()),
                         "§7Pets Trained: §e" + training.getTrainedPets().size(),
                         "§7Training Level: §e" + training.getTrainingLevel(), "", "§eClick to view"));
        
        // Autopet rules
        addGUIItem(gui, 20, Material.REDSTONE_TORCH, "§c§lAutopet Rules", 
            Arrays.asList("§7Configure automatic pet switching", "§7• Set conditions for pet changes", "§7• Automate pet management", "", "§eClick to configure"));
        
        // Training rewards
        addGUIItem(gui, 21, Material.GOLD_INGOT, "§6§lTraining Rewards", 
            Arrays.asList("§7View training rewards and bonuses", "§7• Unlock new abilities", "§7• Gain stat bonuses", "", "§eClick to view"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close pet training", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openAutopetRulesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lAutopet Rules"));
        
        List<AutopetRule> rules = autopetRules.getOrDefault(player.getUniqueId(), new ArrayList<>());
        
        // Display existing rules
        int slot = 10;
        for (AutopetRule rule : rules) {
            if (slot >= 44) break;
            
            ItemStack ruleItem = createAutopetRuleItem(rule);
            gui.setItem(slot, ruleItem);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Add new rule button
        addGUIItem(gui, 22, Material.ANVIL, "§a§lAdd New Rule", 
            Arrays.asList("§7Create a new autopet rule", "§7• Set conditions", "§7• Choose pet to switch to", "", "§eClick to create"));
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to pet training", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void addTrainingXP(Player player, Pet pet, TrainingType type, double xp) {
        PlayerPetTraining training = getPlayerTraining(player.getUniqueId());
        PetTrainingData petTraining = training.getPetTraining("pet_id");
        
        petTraining.addXP(xp);
        training.addTotalTrainingXP(xp);
        
        // Check for training level up
        int oldLevel = petTraining.getTrainingLevel();
        petTraining.checkLevelUp();
        int newLevel = petTraining.getTrainingLevel();
        
        if (newLevel > oldLevel) {
            player.sendMessage(Component.text("§a§lPET TRAINING LEVEL UP!"));
            player.sendMessage("§7Pet: §e" + "Pet");
            player.sendMessage("§7Training: §e" + type.getDisplayName());
            player.sendMessage("§7Level: §e" + oldLevel + " §7→ §e" + newLevel);
            
            // Apply training rewards
            applyTrainingRewards(player, pet, newLevel);
        }
    }
    
    private void applyTrainingRewards(Player player, Pet pet, int trainingLevel) {
        // Apply training-based bonuses to the pet
        switch (trainingLevel) {
            case 5:
                player.sendMessage(Component.text("§aReward: §7+5% Pet Effectiveness"));
                break;
            case 10:
                player.sendMessage(Component.text("§aReward: §7+10% Pet Effectiveness"));
                break;
            case 15:
                player.sendMessage(Component.text("§aReward: §7+15% Pet Effectiveness"));
                break;
            case 20:
                player.sendMessage(Component.text("§aReward: §7+20% Pet Effectiveness"));
                break;
            case 25:
                player.sendMessage(Component.text("§aReward: §7+25% Pet Effectiveness"));
                break;
        }
    }
    
    public void addAutopetRule(Player player, AutopetRule rule) {
        List<AutopetRule> rules = autopetRules.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        rules.add(rule);
        
        player.sendMessage(Component.text("§a§lAUTOPET RULE ADDED!"));
        player.sendMessage("§7Condition: §e" + rule.getCondition().getDisplayName());
        player.sendMessage("§7Pet: §e" + rule.getPetType().getName());
    }
    
    public void removeAutopetRule(Player player, AutopetRule rule) {
        List<AutopetRule> rules = autopetRules.get(player.getUniqueId());
        if (rules != null) {
            rules.remove(rule);
            player.sendMessage(Component.text("§c§lAUTOPET RULE REMOVED!"));
        }
    }
    
    public void checkAutopetRules(Player player) {
        List<AutopetRule> rules = autopetRules.get(player.getUniqueId());
        if (rules == null) return;
        
        for (AutopetRule rule : rules) {
            if (rule.getCondition().isMet(player)) {
                // Switch to the specified pet
                Pet targetPet = petSystem.getPlayerPets(player.getUniqueId()).stream()
                    .filter(pet -> "pet_id".equals(rule.getPetType().getId()))
                    .findFirst()
                    .orElse(null);
                
                if (targetPet != null && !targetPet.isActive()) {
                    petSystem.activatePet(player, targetPet);
                    player.sendMessage(Component.text("§a§lAUTOPET ACTIVATED!"));
                    player.sendMessage("§7Switched to: §e" + "Pet");
                }
            }
        }
    }
    
    private ItemStack createAutopetRuleItem(AutopetRule rule) {
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§c§lAutopet Rule"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Condition: §e" + rule.getCondition().getDisplayName());
            lore.add("§7Pet: §e" + rule.getPetType().getName());
            lore.add("§7Priority: §e" + rule.getPriority());
            lore.add("");
            lore.add("§cRight-click to remove");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void handleTrainingGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // Combat Training
                player.sendMessage(Component.text("§eCombat Training coming soon!"));
                break;
            case 11: // Mining Training
                player.sendMessage(Component.text("§eMining Training coming soon!"));
                break;
            case 12: // Farming Training
                player.sendMessage(Component.text("§eFarming Training coming soon!"));
                break;
            case 13: // Foraging Training
                player.sendMessage(Component.text("§eForaging Training coming soon!"));
                break;
            case 14: // Fishing Training
                player.sendMessage(Component.text("§eFishing Training coming soon!"));
                break;
            case 19: // Training Progress
                player.sendMessage(Component.text("§eTraining Progress coming soon!"));
                break;
            case 20: // Autopet Rules
                openAutopetRulesGUI(player);
                break;
            case 21: // Training Rewards
                player.sendMessage(Component.text("§eTraining Rewards coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void handleAutopetGUIClick(Player player, int slot) {
        if (slot == 22) { // Add New Rule
            player.sendMessage(Component.text("§eAdd New Rule coming soon!"));
        } else if (slot == 45) { // Back
            openPetTrainingGUI(player);
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerPetTraining getPlayerTraining(UUID playerId) {
        return playerTraining.computeIfAbsent(playerId, k -> new PlayerPetTraining(playerId));
    }
    
    // Training Classes
    public enum TrainingType {
        COMBAT("§cCombat", 1.0),
        MINING("§7Mining", 1.2),
        FARMING("§aFarming", 1.1),
        FORAGING("§2Foraging", 1.3),
        FISHING("§bFishing", 1.4);
        
        private final String displayName;
        private final double xpMultiplier;
        
        TrainingType(String displayName, double xpMultiplier) {
            this.displayName = displayName;
            this.xpMultiplier = xpMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getXpMultiplier() { return xpMultiplier; }
    }
    
    public static class PlayerPetTraining {
        private final UUID playerId;
        private final Map<String, PetTrainingData> petTraining = new ConcurrentHashMap<>();
        private double totalTrainingXP;
        private int trainingLevel;
        
        public PlayerPetTraining(UUID playerId) {
            this.playerId = playerId;
            this.totalTrainingXP = 0.0;
            this.trainingLevel = 1;
        }
        
        public void addTotalTrainingXP(double xp) {
            this.totalTrainingXP += xp;
            checkTrainingLevelUp();
        }
        
        private void checkTrainingLevelUp() {
            int newLevel = (int) (totalTrainingXP / 1000) + 1;
            if (newLevel > trainingLevel) {
                trainingLevel = newLevel;
            }
        }
        
        public PetTrainingData getPetTraining(String petTypeId) {
            return petTraining.computeIfAbsent(petTypeId, k -> new PetTrainingData(petTypeId));
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<String, PetTrainingData> getTrainedPets() { return petTraining; }
        public double getTotalTrainingXP() { return totalTrainingXP; }
        public int getTrainingLevel() { return trainingLevel; }
    }
    
    public static class PetTrainingData {
        private final String petTypeId;
        private double trainingXP;
        private int trainingLevel;
        
        public PetTrainingData(String petTypeId) {
            this.petTypeId = petTypeId;
            this.trainingXP = 0.0;
            this.trainingLevel = 1;
        }
        
        public void addXP(double xp) {
            this.trainingXP += xp;
        }
        
        public void checkLevelUp() {
            int newLevel = (int) (trainingXP / 500) + 1;
            if (newLevel > trainingLevel) {
                trainingLevel = newLevel;
            }
        }
        
        // Getters
        public String getPetTypeId() { return petTypeId; }
        public double getTrainingXP() { return trainingXP; }
        public int getTrainingLevel() { return trainingLevel; }
    }
    
    public static class AutopetRule {
        private final AutopetCondition condition;
        private final PetSystem.PetType petType;
        private final int priority;
        
        public AutopetRule(AutopetCondition condition, PetSystem.PetType petType, int priority) {
            this.condition = condition;
            this.petType = petType;
            this.priority = priority;
        }
        
        // Getters
        public AutopetCondition getCondition() { return condition; }
        public PetSystem.PetType getPetType() { return petType; }
        public int getPriority() { return priority; }
    }
    
    public enum AutopetCondition {
        COMBAT("§cIn Combat", player -> {
            // Check if player is in combat
            return player.getHealth() < player.getMaxHealth() * 0.8;
        }),
        MINING("§7While Mining", player -> {
            // Check if player is mining
            return player.getInventory().getItemInMainHand().getType().name().contains("PICKAXE");
        }),
        FARMING("§aWhile Farming", player -> {
            // Check if player is farming
            return player.getInventory().getItemInMainHand().getType().name().contains("HOE");
        }),
        FISHING("§bWhile Fishing", player -> {
            // Check if player is fishing
            return player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD;
        });
        
        private final String displayName;
        private final java.util.function.Predicate<Player> condition;
        
        AutopetCondition(String displayName, java.util.function.Predicate<Player> condition) {
            this.displayName = displayName;
            this.condition = condition;
        }
        
        public String getDisplayName() { return displayName; }
        public boolean isMet(Player player) { return condition.test(player); }
    }
}
