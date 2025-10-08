package de.noctivag.skyblock.talismans;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AdvancedTalismanSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerTalismanData> playerTalismanData = new ConcurrentHashMap<>();
    private final Map<String, Talisman> talismans = new HashMap<>();
    private final Map<String, TalismanCategory> talismanCategories = new HashMap<>();
    
    public AdvancedTalismanSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeTalismans();
    }
    
    private void initializeTalismans() {
        // Initialize categories
        talismanCategories.put("combat", new TalismanCategory("Combat", Material.DIAMOND_SWORD, "§cCombat talismans"));
        talismanCategories.put("mining", new TalismanCategory("Mining", Material.DIAMOND_PICKAXE, "§7Mining talismans"));
        talismanCategories.put("farming", new TalismanCategory("Farming", Material.WHEAT, "§aFarming talismans"));
        talismanCategories.put("foraging", new TalismanCategory("Foraging", Material.OAK_LOG, "§6Foraging talismans"));
        talismanCategories.put("fishing", new TalismanCategory("Fishing", Material.FISHING_ROD, "§bFishing talismans"));
        talismanCategories.put("misc", new TalismanCategory("Miscellaneous", Material.NETHER_STAR, "§dMiscellaneous talismans"));
        
        // Combat Talismans
        talismans.put("zombie_talisman", new Talisman("zombie_talisman", "Zombie Talisman", "§fZombie Talisman", Material.ROTTEN_FLESH,
            "§7Increases damage against zombies by §a+5%", "combat", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("skeleton_talisman", new Talisman("skeleton_talisman", "Skeleton Talisman", "§fSkeleton Talisman", Material.BONE,
            "§7Increases damage against skeletons by §a+5%", "combat", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("spider_talisman", new Talisman("spider_talisman", "Spider Talisman", "§fSpider Talisman", Material.STRING,
            "§7Increases damage against spiders by §a+5%", "combat", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("creeper_talisman", new Talisman("creeper_talisman", "Creeper Talisman", "§fCreeper Talisman", Material.GUNPOWDER,
            "§7Increases damage against creepers by §a+5%", "combat", TalismanRarity.COMMON, 1, 5.0));
        
        // Mining Talismans
        talismans.put("mining_talisman", new Talisman("mining_talisman", "Mining Talisman", "§fMining Talisman", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed by §a+5%", "mining", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("coal_talisman", new Talisman("coal_talisman", "Coal Talisman", "§fCoal Talisman", Material.COAL,
            "§7Increases coal collection speed by §a+5%", "mining", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("iron_talisman", new Talisman("iron_talisman", "Iron Talisman", "§fIron Talisman", Material.IRON_INGOT,
            "§7Increases iron collection speed by §a+5%", "mining", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("gold_talisman", new Talisman("gold_talisman", "Gold Talisman", "§fGold Talisman", Material.GOLD_INGOT,
            "§7Increases gold collection speed by §a+5%", "mining", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("diamond_talisman", new Talisman("diamond_talisman", "Diamond Talisman", "§fDiamond Talisman", Material.DIAMOND,
            "§7Increases diamond collection speed by §a+5%", "mining", TalismanRarity.UNCOMMON, 2, 7.5));
        
        // Farming Talismans
        talismans.put("farming_talisman", new Talisman("farming_talisman", "Farming Talisman", "§fFarming Talisman", Material.WHEAT,
            "§7Increases farming XP gain by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("wheat_talisman", new Talisman("wheat_talisman", "Wheat Talisman", "§fWheat Talisman", Material.WHEAT,
            "§7Increases wheat collection speed by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("carrot_talisman", new Talisman("carrot_talisman", "Carrot Talisman", "§fCarrot Talisman", Material.CARROT,
            "§7Increases carrot collection speed by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("potato_talisman", new Talisman("potato_talisman", "Potato Talisman", "§fPotato Talisman", Material.POTATO,
            "§7Increases potato collection speed by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("pumpkin_talisman", new Talisman("pumpkin_talisman", "Pumpkin Talisman", "§fPumpkin Talisman", Material.PUMPKIN,
            "§7Increases pumpkin collection speed by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("melon_talisman", new Talisman("melon_talisman", "Melon Talisman", "§fMelon Talisman", Material.MELON,
            "§7Increases melon collection speed by §a+5%", "farming", TalismanRarity.COMMON, 1, 5.0));
        
        // Foraging Talismans
        talismans.put("foraging_talisman", new Talisman("foraging_talisman", "Foraging Talisman", "§fForaging Talisman", Material.OAK_LOG,
            "§7Increases foraging XP gain by §a+5%", "foraging", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("oak_talisman", new Talisman("oak_talisman", "Oak Talisman", "§fOak Talisman", Material.OAK_LOG,
            "§7Increases oak log collection speed by §a+5%", "foraging", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("birch_talisman", new Talisman("birch_talisman", "Birch Talisman", "§fBirch Talisman", Material.BIRCH_LOG,
            "§7Increases birch log collection speed by §a+5%", "foraging", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("spruce_talisman", new Talisman("spruce_talisman", "Spruce Talisman", "§fSpruce Talisman", Material.SPRUCE_LOG,
            "§7Increases spruce log collection speed by §a+5%", "foraging", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("jungle_talisman", new Talisman("jungle_talisman", "Jungle Talisman", "§fJungle Talisman", Material.JUNGLE_LOG,
            "§7Increases jungle log collection speed by §a+5%", "foraging", TalismanRarity.COMMON, 1, 5.0));
        
        // Fishing Talismans
        talismans.put("fishing_talisman", new Talisman("fishing_talisman", "Fishing Talisman", "§fFishing Talisman", Material.FISHING_ROD,
            "§7Increases fishing XP gain by §a+5%", "fishing", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("cod_talisman", new Talisman("cod_talisman", "Cod Talisman", "§fCod Talisman", Material.COD,
            "§7Increases cod catch rate by §a+5%", "fishing", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("salmon_talisman", new Talisman("salmon_talisman", "Salmon Talisman", "§fSalmon Talisman", Material.SALMON,
            "§7Increases salmon catch rate by §a+5%", "fishing", TalismanRarity.COMMON, 1, 5.0));
        
        // Miscellaneous Talismans
        talismans.put("speed_talisman", new Talisman("speed_talisman", "Speed Talisman", "§fSpeed Talisman", Material.FEATHER,
            "§7Increases movement speed by §a+5%", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("health_talisman", new Talisman("health_talisman", "Health Talisman", "§fHealth Talisman", Material.APPLE,
            "§7Increases health by §a+5", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("defense_talisman", new Talisman("defense_talisman", "Defense Talisman", "§fDefense Talisman", Material.SHIELD,
            "§7Increases defense by §a+5", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("strength_talisman", new Talisman("strength_talisman", "Strength Talisman", "§fStrength Talisman", Material.IRON_SWORD,
            "§7Increases strength by §a+5", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("intelligence_talisman", new Talisman("intelligence_talisman", "Intelligence Talisman", "§fIntelligence Talisman", Material.BOOK,
            "§7Increases intelligence by §a+5", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("mana_talisman", new Talisman("mana_talisman", "Mana Talisman", "§fMana Talisman", Material.ENDER_PEARL,
            "§7Increases max mana by §a+10", "misc", TalismanRarity.COMMON, 1, 10.0));
        talismans.put("lucky_talisman", new Talisman("lucky_talisman", "Lucky Talisman", "§fLucky Talisman", Material.GOLD_NUGGET,
            "§7Increases luck by §a+5%", "misc", TalismanRarity.COMMON, 1, 5.0));
        talismans.put("experience_talisman", new Talisman("experience_talisman", "Experience Talisman", "§fExperience Talisman", Material.EXPERIENCE_BOTTLE,
            "§7Increases XP gain by §a+5%", "misc", TalismanRarity.COMMON, 1, 5.0));
        
        // Higher tier talismans
        talismans.put("speed_ring", new Talisman("speed_ring", "Speed Ring", "§aSpeed Ring", Material.FEATHER,
            "§7Increases movement speed by §a+10%", "misc", TalismanRarity.UNCOMMON, 2, 10.0));
        talismans.put("health_ring", new Talisman("health_ring", "Health Ring", "§aHealth Ring", Material.APPLE,
            "§7Increases health by §a+10", "misc", TalismanRarity.UNCOMMON, 2, 10.0));
        talismans.put("defense_ring", new Talisman("defense_ring", "Defense Ring", "§aDefense Ring", Material.SHIELD,
            "§7Increases defense by §a+10", "misc", TalismanRarity.UNCOMMON, 2, 10.0));
        talismans.put("strength_ring", new Talisman("strength_ring", "Strength Ring", "§aStrength Ring", Material.IRON_SWORD,
            "§7Increases strength by §a+10", "misc", TalismanRarity.UNCOMMON, 2, 10.0));
        talismans.put("intelligence_ring", new Talisman("intelligence_ring", "Intelligence Ring", "§aIntelligence Ring", Material.BOOK,
            "§7Increases intelligence by §a+10", "misc", TalismanRarity.UNCOMMON, 2, 10.0));
        
        // Rare tier talismans
        talismans.put("speed_artifact", new Talisman("speed_artifact", "Speed Artifact", "§9Speed Artifact", Material.FEATHER,
            "§7Increases movement speed by §a+15%", "misc", TalismanRarity.RARE, 3, 15.0));
        talismans.put("health_artifact", new Talisman("health_artifact", "Health Artifact", "§9Health Artifact", Material.APPLE,
            "§7Increases health by §a+15", "misc", TalismanRarity.RARE, 3, 15.0));
        talismans.put("defense_artifact", new Talisman("defense_artifact", "Defense Artifact", "§9Defense Artifact", Material.SHIELD,
            "§7Increases defense by §a+15", "misc", TalismanRarity.RARE, 3, 15.0));
        talismans.put("strength_artifact", new Talisman("strength_artifact", "Strength Artifact", "§9Strength Artifact", Material.IRON_SWORD,
            "§7Increases strength by §a+15", "misc", TalismanRarity.RARE, 3, 15.0));
        talismans.put("intelligence_artifact", new Talisman("intelligence_artifact", "Intelligence Artifact", "§9Intelligence Artifact", Material.BOOK,
            "§7Increases intelligence by §a+15", "misc", TalismanRarity.RARE, 3, 15.0));
    }
    
    public void openTalismanGUI(Player player) {
        Inventory gui = SkyblockPlugin.getServer().createInventory(null, 54, Component.text("§d§lTalismans"));
        
        // Add category items
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat Talismans", "§7Combat-related talismans");
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining Talismans", "§7Mining-related talismans");
        addGUIItem(gui, 12, Material.WHEAT, "§a§lFarming Talismans", "§7Farming-related talismans");
        addGUIItem(gui, 13, Material.OAK_LOG, "§6§lForaging Talismans", "§7Foraging-related talismans");
        addGUIItem(gui, 14, Material.FISHING_ROD, "§b§lFishing Talismans", "§7Fishing-related talismans");
        addGUIItem(gui, 15, Material.NETHER_STAR, "§d§lMiscellaneous", "§7Miscellaneous talismans");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the talisman menu");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aTalisman GUI opened!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                List<Component> loreComponents = Arrays.stream(lore)
                    .map(Component::text)
                    .collect(java.util.stream.Collectors.toList());
                meta.lore(loreComponents);
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public static class Talisman {
        private final String id, name, displayName, description, category;
        private final Material icon;
        private final TalismanRarity rarity;
        private final int level;
        private final double effectValue;

        public Talisman(String id, String name, String displayName, Material icon,
                       String description, String category, TalismanRarity rarity, int level, double effectValue) {
            this.id = id; this.name = name; this.displayName = displayName; this.icon = icon;
            this.description = description; this.category = category; this.rarity = rarity;
            this.level = level; this.effectValue = effectValue;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public TalismanRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public double getEffectValue() { return effectValue; }

        public int getMagicalPower() {
            return rarity.getMagicalPowerValue();
        }

        public boolean canBeEnriched() {
            return rarity == TalismanRarity.LEGENDARY || rarity == TalismanRarity.MYTHIC;
        }
    }
    
    public static class TalismanCategory {
        private final String name, description;
        private final Material icon;
        
        public TalismanCategory(String name, Material icon, String description) {
            this.name = name; this.icon = icon; this.description = description;
        }
        
        public String getName() { return name; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
    }
    
    public static class PlayerTalismanData {
        private final UUID playerId;
        private final Map<String, Integer> ownedTalismans = new HashMap<>();
        private final Map<String, Integer> talismanLevels = new HashMap<>();
        private final List<String> activeTalismans = new ArrayList<>();
        
        public PlayerTalismanData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public UUID getPlayerId() { return playerId; }
        public Map<String, Integer> getOwnedTalismans() { return ownedTalismans; }
        public Map<String, Integer> getTalismanLevels() { return talismanLevels; }
        public List<String> getActiveTalismans() { return activeTalismans; }
    }
    
    public enum TalismanRarity {
        COMMON("§fCommon", 1, 1), 
        UNCOMMON("§aUncommon", 2, 2), 
        RARE("§9Rare", 3, 3),
        EPIC("§5Epic", 4, 4), 
        LEGENDARY("§6Legendary", 5, 5), 
        MYTHIC("§dMythic", 6, 6);
        
        private final String displayName;
        private final int level;
        private final int magicalPowerValue;
        
        TalismanRarity(String displayName, int level, int magicalPowerValue) {
            this.displayName = displayName; 
            this.level = level;
            this.magicalPowerValue = magicalPowerValue;
        }
        
        public String getDisplayName() { return displayName; }
        public int getLevel() { return level; }
        public int getMagicalPowerValue() { return magicalPowerValue; }
    }
}
