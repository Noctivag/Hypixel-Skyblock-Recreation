package de.noctivag.skyblock.manual;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InGameManualSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerManualData> playerManualData = new ConcurrentHashMap<>();
    private final Map<ManualCategory, List<ManualEntry>> manualEntries = new HashMap<>();
    
    public InGameManualSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeManualEntries();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeManualEntries() {
        // Basic Commands
        List<ManualEntry> basicCommands = new ArrayList<>();
        basicCommands.add(new ManualEntry(
            "Basic Commands", "§aBasic Commands", Material.COMMAND_BLOCK,
            "§7Basic commands for everyday use.",
            Arrays.asList("§7- /help - Show help information", "§7- /info - Show SkyblockPlugin information", "§7- /reload - Reload the SkyblockPlugin"),
            Arrays.asList("§7- /help", "§7- /info", "§7- /reload")
        ));
        manualEntries.put(ManualCategory.BASIC_COMMANDS, basicCommands);
        
        // Mob System
        List<ManualEntry> mobSystem = new ArrayList<>();
        mobSystem.add(new ManualEntry(
            "Mob System", "§cMob System", Material.ZOMBIE_HEAD,
            "§7Complete mob system with spawn areas and management.",
            Arrays.asList("§7- /mob spawn <type> [amount] - Spawn mobs", "§7- /mob kill <type> [radius] - Kill mobs", "§7- /mob list - List all mobs", "§7- /mob info <type> - Show mob info", "§7- /mob gui - Open mob GUI"),
            Arrays.asList("§7- /mob spawn zombie 5", "§7- /mob kill skeleton 10", "§7- /mob list", "§7- /mob info enderman", "§7- /mob gui")
        ));
        mobSystem.add(new ManualEntry(
            "Spawn Areas", "§eSpawn Areas", Material.GRASS_BLOCK,
            "§7Create and manage mob spawn areas.",
            Arrays.asList("§7- /mob spawnarea create <name> <type> <radius> <max> - Create spawn area", "§7- /mob spawnarea remove <name> - Remove spawn area", "§7- /mob spawnarea edit <name> <radius> <max> - Edit spawn area", "§7- /mob spawnarea list - List all areas", "§7- /mob spawnarea gui - Open spawn area GUI"),
            Arrays.asList("§7- /mob spawnarea create zombie_area zombie 20 10", "§7- /mob spawnarea remove zombie_area", "§7- /mob spawnarea edit zombie_area 30 15", "§7- /mob spawnarea list", "§7- /mob spawnarea gui")
        ));
        mobSystem.add(new ManualEntry(
            "Mob Search & Filter", "§bMob Search & Filter", Material.COMPASS,
            "§7Search and filter mobs by various criteria.",
            Arrays.asList("§7- /mob search <type> <value> - Search mobs", "§7- /mob filter <category> [rarity] - Filter mobs", "§7- /mob category <category> - Show category mobs", "§7- /mob rarity <rarity> - Show rarity mobs"),
            Arrays.asList("§7- /mob search name zombie", "§7- /mob filter basic common", "§7- /mob category slayer", "§7- /mob rarity legendary")
        ));
        manualEntries.put(ManualCategory.MOB_SYSTEM, mobSystem);
        
        // Item System
        List<ManualEntry> itemSystem = new ArrayList<>();
        itemSystem.add(new ManualEntry(
            "Item System", "§6Item System", Material.DIAMOND_SWORD,
            "§7Advanced item system with Hypixel Skyblock items.",
            Arrays.asList("§7- /items list - List all items", "§7- /items info <item> - Show item info", "§7- /items gui - Open items GUI", "§7- /items give <item> [amount] - Give item"),
            Arrays.asList("§7- /items list", "§7- /items info hyperion", "§7- /items gui", "§7- /items give aspect_of_the_end 1")
        ));
        itemSystem.add(new ManualEntry(
            "Creative Menu", "§dCreative Menu", Material.CHEST,
            "§7Creative inventory menu for OP players.",
            Arrays.asList("§7- /creative - Open creative menu", "§7- /creative items - Open items menu", "§7- /creative armor - Open armor menu", "§7- /creative weapons - Open weapons menu", "§7- /creative all - Open all items menu"),
            Arrays.asList("§7- /creative", "§7- /creative items", "§7- /creative armor", "§7- /creative weapons", "§7- /creative all")
        ));
        manualEntries.put(ManualCategory.ITEM_SYSTEM, itemSystem);
        
        // Armor System
        List<ManualEntry> armorSystem = new ArrayList<>();
        armorSystem.add(new ManualEntry(
            "Armor System", "§6Armor System", Material.DIAMOND_CHESTPLATE,
            "§7Advanced armor system with Hypixel Skyblock armors.",
            Arrays.asList("§7- /armor list - List all armors", "§7- /armor info <armor> - Show armor info", "§7- /armor gui - Open armor GUI", "§7- /armor give <armor> - Give armor"),
            Arrays.asList("§7- /armor list", "§7- /armor info necron_armor", "§7- /armor gui", "§7- /armor give dragon_armor")
        ));
        manualEntries.put(ManualCategory.ARMOR_SYSTEM, armorSystem);
        
        // Weapon System
        List<ManualEntry> weaponSystem = new ArrayList<>();
        weaponSystem.add(new ManualEntry(
            "Weapon System", "§aWeapon System", Material.BOW,
            "§7Advanced weapon system with Hypixel Skyblock weapons.",
            Arrays.asList("§7- /weapons list - List all weapons", "§7- /weapons info <weapon> - Show weapon info", "§7- /weapons gui - Open weapons GUI", "§7- /weapons give <weapon> - Give weapon"),
            Arrays.asList("§7- /weapons list", "§7- /weapons info hyperion", "§7- /weapons gui", "§7- /weapons give aspect_of_the_dragons")
        ));
        manualEntries.put(ManualCategory.WEAPON_SYSTEM, weaponSystem);
        
        // Skills System
        List<ManualEntry> skillsSystem = new ArrayList<>();
        skillsSystem.add(new ManualEntry(
            "Skills System", "§aSkills System", Material.EXPERIENCE_BOTTLE,
            "§7Complete skills system with all Hypixel Skyblock skills.",
            Arrays.asList("§7- /skills list - List all skills", "§7- /skills info <skill> - Show skill info", "§7- /skills gui - Open skills GUI", "§7- /skills level <skill> - Show skill level"),
            Arrays.asList("§7- /skills list", "§7- /skills info combat", "§7- /skills gui", "§7- /skills level mining")
        ));
        manualEntries.put(ManualCategory.SKILLS_SYSTEM, skillsSystem);
        
        // Collections System
        List<ManualEntry> collectionsSystem = new ArrayList<>();
        collectionsSystem.add(new ManualEntry(
            "Collections System", "§bCollections System", Material.EMERALD,
            "§7Complete collections system with all Hypixel Skyblock collections.",
            Arrays.asList("§7- /collections list - List all collections", "§7- /collections info <collection> - Show collection info", "§7- /collections gui - Open collections GUI", "§7- /collections progress - Show progress"),
            Arrays.asList("§7- /collections list", "§7- /collections info wheat", "§7- /collections gui", "§7- /collections progress")
        ));
        manualEntries.put(ManualCategory.COLLECTIONS_SYSTEM, collectionsSystem);
        
        // Minions System
        List<ManualEntry> minionsSystem = new ArrayList<>();
        minionsSystem.add(new ManualEntry(
            "Minions System", "§eMinions System", Material.IRON_INGOT,
            "§7Complete minions system with all Hypixel Skyblock minions.",
            Arrays.asList("§7- /minions list - List all minions", "§7- /minions info <minion> - Show minion info", "§7- /minions gui - Open minions GUI", "§7- /minions place <minion> - Place minion"),
            Arrays.asList("§7- /minions list", "§7- /minions info wheat_minion", "§7- /minions gui", "§7- /minions place cobblestone_minion")
        ));
        manualEntries.put(ManualCategory.MINIONS_SYSTEM, minionsSystem);
        
        // Pets System
        List<ManualEntry> petsSystem = new ArrayList<>();
        petsSystem.add(new ManualEntry(
            "Pets System", "§dPets System", Material.BONE,
            "§7Complete pets system with all Hypixel Skyblock pets.",
            Arrays.asList("§7- /pets list - List all pets", "§7- /pets info <pet> - Show pet info", "§7- /pets gui - Open pets GUI", "§7- /pets equip <pet> - Equip pet"),
            Arrays.asList("§7- /pets list", "§7- /pets info enderman_pet", "§7- /pets gui", "§7- /pets equip wolf_pet")
        ));
        manualEntries.put(ManualCategory.PETS_SYSTEM, petsSystem);
        
        // Dungeons System
        List<ManualEntry> dungeonsSystem = new ArrayList<>();
        dungeonsSystem.add(new ManualEntry(
            "Dungeons System", "§5Dungeons System", Material.STONE_BRICKS,
            "§7Complete dungeons system with all Hypixel Skyblock dungeons.",
            Arrays.asList("§7- /dungeons list - List all dungeons", "§7- /dungeons info <dungeon> - Show dungeon info", "§7- /dungeons gui - Open dungeons GUI", "§7- /dungeons join <dungeon> - Join dungeon"),
            Arrays.asList("§7- /dungeons list", "§7- /dungeons info catacombs", "§7- /dungeons gui", "§7- /dungeons join catacombs_f1")
        ));
        manualEntries.put(ManualCategory.DUNGEONS_SYSTEM, dungeonsSystem);
        
        // Slayers System
        List<ManualEntry> slayersSystem = new ArrayList<>();
        slayersSystem.add(new ManualEntry(
            "Slayers System", "§cSlayers System", Material.ROTTEN_FLESH,
            "§7Complete slayers system with all Hypixel Skyblock slayers.",
            Arrays.asList("§7- /slayers list - List all slayers", "§7- /slayers info <slayer> - Show slayer info", "§7- /slayers gui - Open slayers GUI", "§7- /slayers start <slayer> - Start slayer quest"),
            Arrays.asList("§7- /slayers list", "§7- /slayers info revenant_horror", "§7- /slayers gui", "§7- /slayers start tarantula_broodfather")
        ));
        manualEntries.put(ManualCategory.SLAYERS_SYSTEM, slayersSystem);
        
        // Guilds System
        List<ManualEntry> guildsSystem = new ArrayList<>();
        guildsSystem.add(new ManualEntry(
            "Guilds System", "§6Guilds System", Material.WHITE_BANNER,
            "§7Complete guilds system with all Hypixel Skyblock guild features.",
            Arrays.asList("§7- /guilds create <name> - Create guild", "§7- /guilds join <name> - Join guild", "§7- /guilds leave - Leave guild", "§7- /guilds info - Show guild info", "§7- /guilds gui - Open guild GUI"),
            Arrays.asList("§7- /guilds create MyGuild", "§7- /guilds join MyGuild", "§7- /guilds leave", "§7- /guilds info", "§7- /guilds gui")
        ));
        manualEntries.put(ManualCategory.GUILDS_SYSTEM, guildsSystem);
        
        // Economy System
        List<ManualEntry> economySystem = new ArrayList<>();
        economySystem.add(new ManualEntry(
            "Economy System", "§6Economy System", Material.GOLD_INGOT,
            "§7Complete economy system with auction house and bazaar.",
            Arrays.asList("§7- /auction list - List auctions", "§7- /auction create <item> <price> - Create auction", "§7- /auction bid <id> <amount> - Bid on auction", "§7- /bazaar list - List bazaar items", "§7- /bazaar buy <item> <amount> - Buy from bazaar"),
            Arrays.asList("§7- /auction list", "§7- /auction create diamond_sword 1000", "§7- /auction bid 1 1500", "§7- /bazaar list", "§7- /bazaar buy wheat 64")
        ));
        manualEntries.put(ManualCategory.ECONOMY_SYSTEM, economySystem);
        
        // Islands System
        List<ManualEntry> islandsSystem = new ArrayList<>();
        islandsSystem.add(new ManualEntry(
            "Islands System", "§aIslands System", Material.GRASS_BLOCK,
            "§7Complete islands system with all Hypixel Skyblock island features.",
            Arrays.asList("§7- /island create - Create island", "§7- /island home - Go to island", "§7- /island invite <player> - Invite player", "§7- /island kick <player> - Kick player", "§7- /island gui - Open island GUI"),
            Arrays.asList("§7- /island create", "§7- /island home", "§7- /island invite PlayerName", "§7- /island kick PlayerName", "§7- /island gui")
        ));
        manualEntries.put(ManualCategory.ISLANDS_SYSTEM, islandsSystem);
        
        // Advanced Systems
        List<ManualEntry> advancedSystems = new ArrayList<>();
        advancedSystems.add(new ManualEntry(
            "Advanced Systems", "§dAdvanced Systems", Material.NETHER_STAR,
            "§7Advanced systems including hunting, bosses, attributes, and sacks.",
            Arrays.asList("§7- /hunting list - List hunting creatures", "§7- /bosses list - List bosses", "§7- /attributes list - List attributes", "§7- /sacks list - List sacks", "§7- /gui - Open main GUI"),
            Arrays.asList("§7- /hunting list", "§7- /bosses list", "§7- /attributes list", "§7- /sacks list", "§7- /gui")
        ));
        manualEntries.put(ManualCategory.ADVANCED_SYSTEMS, advancedSystems);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Manual") || displayName.contains("Help")) {
            openManualGUI(player);
        }
    }
    
    public void openManualGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lIn-Game Manual"));
        
        // Add manual categories
        addGUIItem(gui, 10, Material.COMMAND_BLOCK, "§a§lBasic Commands", "§7Basic commands for everyday use.");
        addGUIItem(gui, 11, Material.ZOMBIE_HEAD, "§c§lMob System", "§7Complete mob system with spawn areas.");
        addGUIItem(gui, 12, Material.DIAMOND_SWORD, "§6§lItem System", "§7Advanced item system with Hypixel Skyblock items.");
        addGUIItem(gui, 13, Material.DIAMOND_CHESTPLATE, "§6§lArmor System", "§7Advanced armor system with Hypixel Skyblock armors.");
        addGUIItem(gui, 14, Material.BOW, "§a§lWeapon System", "§7Advanced weapon system with Hypixel Skyblock weapons.");
        addGUIItem(gui, 15, Material.EXPERIENCE_BOTTLE, "§a§lSkills System", "§7Complete skills system with all Hypixel Skyblock skills.");
        addGUIItem(gui, 16, Material.EMERALD, "§b§lCollections System", "§7Complete collections system with all Hypixel Skyblock collections.");
        addGUIItem(gui, 17, Material.IRON_INGOT, "§e§lMinions System", "§7Complete minions system with all Hypixel Skyblock minions.");
        addGUIItem(gui, 18, Material.BONE, "§d§lPets System", "§7Complete pets system with all Hypixel Skyblock pets.");
        
        addGUIItem(gui, 19, Material.STONE_BRICKS, "§5§lDungeons System", "§7Complete dungeons system with all Hypixel Skyblock dungeons.");
        addGUIItem(gui, 20, Material.ROTTEN_FLESH, "§c§lSlayers System", "§7Complete slayers system with all Hypixel Skyblock slayers.");
        addGUIItem(gui, 21, Material.WHITE_BANNER, "§6§lGuilds System", "§7Complete guilds system with all Hypixel Skyblock guild features.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lEconomy System", "§7Complete economy system with auction house and bazaar.");
        addGUIItem(gui, 23, Material.GRASS_BLOCK, "§a§lIslands System", "§7Complete islands system with all Hypixel Skyblock island features.");
        addGUIItem(gui, 24, Material.NETHER_STAR, "§d§lAdvanced Systems", "§7Advanced systems including hunting, bosses, attributes, and sacks.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the manual.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aIn-Game Manual geöffnet!"));
    }
    
    public void openCategoryGUI(Player player, ManualCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lManual - " + category.getDisplayName());
        
        List<ManualEntry> entries = manualEntries.get(category);
        if (entries != null) {
            int slot = 10;
            for (ManualEntry entry : entries) {
                if (slot >= 44) break; // Don't overflow into navigation area
                
                addGUIItem(gui, slot, entry.getMaterial(), entry.getDisplayName(), entry.getDescription());
                slot++;
            }
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to main manual.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the manual.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openEntryGUI(Player player, ManualEntry entry) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lManual - " + entry.getDisplayName());
        
        // Add entry information
        addGUIItem(gui, 10, entry.getMaterial(), entry.getDisplayName(), entry.getDescription());
        
        // Add commands
        int slot = 19;
        for (String command : entry.getCommands()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.COMMAND_BLOCK, "§7" + command, "§7Click to copy command.");
            slot++;
        }
        
        // Add examples
        slot = 28;
        for (String example : entry.getExamples()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.PAPER, "§e" + example, "§7Example usage.");
            slot++;
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to category.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the manual.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(description).stream()
                .map(desc -> Component.text(desc))
                .collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerManualData getPlayerManualData(UUID playerId) {
        return playerManualData.computeIfAbsent(playerId, k -> new PlayerManualData(playerId));
    }
    
    public List<ManualEntry> getManualEntries(ManualCategory category) {
        return manualEntries.getOrDefault(category, new ArrayList<>());
    }
    
    public enum ManualCategory {
        BASIC_COMMANDS("§aBasic Commands", "§7Basic commands for everyday use"),
        MOB_SYSTEM("§cMob System", "§7Complete mob system with spawn areas"),
        ITEM_SYSTEM("§6Item System", "§7Advanced item system with Hypixel Skyblock items"),
        ARMOR_SYSTEM("§6Armor System", "§7Advanced armor system with Hypixel Skyblock armors"),
        WEAPON_SYSTEM("§aWeapon System", "§7Advanced weapon system with Hypixel Skyblock weapons"),
        SKILLS_SYSTEM("§aSkills System", "§7Complete skills system with all Hypixel Skyblock skills"),
        COLLECTIONS_SYSTEM("§bCollections System", "§7Complete collections system with all Hypixel Skyblock collections"),
        MINIONS_SYSTEM("§eMinions System", "§7Complete minions system with all Hypixel Skyblock minions"),
        PETS_SYSTEM("§dPets System", "§7Complete pets system with all Hypixel Skyblock pets"),
        DUNGEONS_SYSTEM("§5Dungeons System", "§7Complete dungeons system with all Hypixel Skyblock dungeons"),
        SLAYERS_SYSTEM("§cSlayers System", "§7Complete slayers system with all Hypixel Skyblock slayers"),
        GUILDS_SYSTEM("§6Guilds System", "§7Complete guilds system with all Hypixel Skyblock guild features"),
        ECONOMY_SYSTEM("§6Economy System", "§7Complete economy system with auction house and bazaar"),
        ISLANDS_SYSTEM("§aIslands System", "§7Complete islands system with all Hypixel Skyblock island features"),
        ADVANCED_SYSTEMS("§dAdvanced Systems", "§7Advanced systems including hunting, bosses, attributes, and sacks");
        
        private final String displayName;
        private final String description;
        
        ManualCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class ManualEntry {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final List<String> commands;
        private final List<String> examples;
        
        public ManualEntry(String name, String displayName, Material material, String description,
                          List<String> commands, List<String> examples) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.commands = commands;
            this.examples = examples;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public List<String> getCommands() { return commands; }
        public List<String> getExamples() { return examples; }
    }
    
    public static class PlayerManualData {
        private final UUID playerId;
        private final Map<ManualCategory, Boolean> categoryUnlocked = new HashMap<>();
        private final Map<String, Boolean> entryUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerManualData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
            
            // Unlock basic categories by default
            for (ManualCategory category : ManualCategory.values()) {
                categoryUnlocked.put(category, true);
            }
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void unlockCategory(ManualCategory category) {
            categoryUnlocked.put(category, true);
        }
        
        public void unlockEntry(String entryName) {
            entryUnlocked.put(entryName, true);
        }
        
        public boolean isCategoryUnlocked(ManualCategory category) {
            return categoryUnlocked.getOrDefault(category, false);
        }
        
        public boolean isEntryUnlocked(String entryName) {
            return entryUnlocked.getOrDefault(entryName, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
