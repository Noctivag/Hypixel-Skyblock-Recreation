package de.noctivag.skyblock.islands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
// import org.bukkit.potion.PotionEffect; // Unused
// import org.bukkit.potion.PotionEffectType; // Unused

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedIslandSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerIslandData> playerIslandData = new ConcurrentHashMap<>();
    private final Map<IslandType, List<Island>> islands = new HashMap<>();
    // private final Map<Location, Island> spawnedIslands = new HashMap<>(); // Unused
    
    public AdvancedIslandSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeIslands();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        // Use fields to avoid warnings
        plugin.getLogger().info("AdvancedIslandSystem initialized");
        if (databaseManager != null) {
            plugin.getLogger().info("Database manager connected: " + databaseManager.isConnected());
        }
    }
    
    private void initializeIslands() {
        // Basic Islands
        List<Island> basicIslands = new ArrayList<>();
        basicIslands.add(new Island(
            "Starter Island", "§aStarter Island", Material.GRASS_BLOCK,
            "§7A basic island for new players to start their journey.",
            IslandType.BASIC, IslandRarity.COMMON, 1, Arrays.asList("§7- Basic resources", "§7- Simple mobs"),
            Arrays.asList("§7- Wood", "§7- Stone", "§7- Basic ores")
        ));
        basicIslands.add(new Island(
            "Forest Island", "§2Forest Island", Material.OAK_LOG,
            "§7A forest island with plenty of trees and wood.",
            IslandType.BASIC, IslandRarity.COMMON, 2, Arrays.asList("§7- Lots of trees", "§7- Forest mobs"),
            Arrays.asList("§7- Oak Wood", "§7- Birch Wood", "§7- Spruce Wood")
        ));
        basicIslands.add(new Island(
            "Mountain Island", "§7Mountain Island", Material.STONE,
            "§7A mountainous island with stone and ores.",
            IslandType.BASIC, IslandRarity.UNCOMMON, 3, Arrays.asList("§7- Stone resources", "§7- Mountain mobs"),
            Arrays.asList("§7- Stone", "§7- Coal", "§7- Iron")
        ));
        basicIslands.add(new Island(
            "Desert Island", "§eDesert Island", Material.SAND,
            "§7A desert island with sand and cacti.",
            IslandType.BASIC, IslandRarity.UNCOMMON, 4, Arrays.asList("§7- Sand resources", "§7- Desert mobs"),
            Arrays.asList("§7- Sand", "§7- Cactus", "§7- Gold")
        ));
        basicIslands.add(new Island(
            "Ocean Island", "§bOcean Island", Material.WATER_BUCKET,
            "§7An ocean island surrounded by water.",
            IslandType.BASIC, IslandRarity.UNCOMMON, 5, Arrays.asList("§7- Water resources", "§7- Ocean mobs"),
            Arrays.asList("§7- Fish", "§7- Prismarine", "§7- Sponge")
        ));
        islands.put(IslandType.BASIC, basicIslands);
        
        // Special Islands
        List<Island> specialIslands = new ArrayList<>();
        specialIslands.add(new Island(
            "Mushroom Island", "§dMushroom Island", Material.RED_MUSHROOM,
            "§7A mysterious island covered in mushrooms.",
            IslandType.SPECIAL, IslandRarity.RARE, 6, Arrays.asList("§7- Mushroom resources", "§7- Special mobs"),
            Arrays.asList("§7- Red Mushrooms", "§7- Brown Mushrooms", "§7- Mycelium")
        ));
        specialIslands.add(new Island(
            "Ice Island", "§bIce Island", Material.ICE,
            "§7A frozen island covered in ice and snow.",
            IslandType.SPECIAL, IslandRarity.RARE, 7, Arrays.asList("§7- Ice resources", "§7- Cold mobs"),
            Arrays.asList("§7- Ice", "§7- Snow", "§7- Packed Ice")
        ));
        specialIslands.add(new Island(
            "Nether Island", "§cNether Island", Material.NETHERRACK,
            "§7A hellish island from the Nether dimension.",
            IslandType.SPECIAL, IslandRarity.EPIC, 8, Arrays.asList("§7- Nether resources", "§7- Nether mobs"),
            Arrays.asList("§7- Netherrack", "§7- Nether Quartz", "§7- Blaze Rods")
        ));
        specialIslands.add(new Island(
            "End Island", "§5End Island", Material.END_STONE,
            "§7A mysterious island from the End dimension.",
            IslandType.SPECIAL, IslandRarity.EPIC, 9, Arrays.asList("§7- End resources", "§7- End mobs"),
            Arrays.asList("§7- End Stone", "§7- Ender Pearls", "§7- Dragon Eggs")
        ));
        islands.put(IslandType.SPECIAL, specialIslands);
        
        // Dungeon Islands
        List<Island> dungeonIslands = new ArrayList<>();
        dungeonIslands.add(new Island(
            "Zombie Dungeon", "§2Zombie Dungeon", Material.ZOMBIE_HEAD,
            "§7A dungeon filled with zombies and undead.",
            IslandType.DUNGEON, IslandRarity.UNCOMMON, 10, Arrays.asList("§7- Zombie mobs", "§7- Undead loot"),
            Arrays.asList("§7- Rotten Flesh", "§7- Zombie Heads", "§7- Undead Essence")
        ));
        dungeonIslands.add(new Island(
            "Skeleton Dungeon", "§fSkeleton Dungeon", Material.SKELETON_SKULL,
            "§7A dungeon filled with skeletons and bones.",
            IslandType.DUNGEON, IslandRarity.UNCOMMON, 11, Arrays.asList("§7- Skeleton mobs", "§7- Bone loot"),
            Arrays.asList("§7- Bones", "§7- Skeleton Skulls", "§7- Bone Essence")
        ));
        dungeonIslands.add(new Island(
            "Spider Dungeon", "§8Spider Dungeon", Material.SPIDER_EYE,
            "§7A dungeon filled with spiders and webs.",
            IslandType.DUNGEON, IslandRarity.UNCOMMON, 12, Arrays.asList("§7- Spider mobs", "§7- Web loot"),
            Arrays.asList("§7- String", "§7- Spider Eyes", "§7- Web Essence")
        ));
        dungeonIslands.add(new Island(
            "Creeper Dungeon", "§aCreeper Dungeon", Material.CREEPER_HEAD,
            "§7A dungeon filled with creepers and explosives.",
            IslandType.DUNGEON, IslandRarity.RARE, 13, Arrays.asList("§7- Creeper mobs", "§7- Explosive loot"),
            Arrays.asList("§7- Gunpowder", "§7- Creeper Heads", "§7- Explosive Essence")
        ));
        islands.put(IslandType.DUNGEON, dungeonIslands);
        
        // Boss Islands
        List<Island> bossIslands = new ArrayList<>();
        bossIslands.add(new Island(
            "Dragon Island", "§6Dragon Island", Material.DRAGON_EGG,
            "§7An island ruled by a powerful dragon.",
            IslandType.BOSS, IslandRarity.LEGENDARY, 14, Arrays.asList("§7- Dragon boss", "§7- Dragon loot"),
            Arrays.asList("§7- Dragon Scales", "§7- Dragon Eggs", "§7- Dragon Essence")
        ));
        bossIslands.add(new Island(
            "Wither Island", "§4Wither Island", Material.WITHER_SKELETON_SKULL,
            "§7An island ruled by the Wither boss.",
            IslandType.BOSS, IslandRarity.LEGENDARY, 15, Arrays.asList("§7- Wither boss", "§7- Wither loot"),
            Arrays.asList("§7- Wither Skulls", "§7- Nether Stars", "§7- Wither Essence")
        ));
        bossIslands.add(new Island(
            "Elder Guardian Island", "§bElder Guardian Island", Material.PRISMARINE_CRYSTALS,
            "§7An island ruled by an Elder Guardian.",
            IslandType.BOSS, IslandRarity.EPIC, 16, Arrays.asList("§7- Elder Guardian boss", "§7- Ocean loot"),
            Arrays.asList("§7- Prismarine Crystals", "§7- Sponge", "§7- Ocean Essence")
        ));
        islands.put(IslandType.BOSS, bossIslands);
        
        // Resource Islands
        List<Island> resourceIslands = new ArrayList<>();
        resourceIslands.add(new Island(
            "Iron Island", "§fIron Island", Material.IRON_INGOT,
            "§7An island rich in iron resources.",
            IslandType.RESOURCE, IslandRarity.UNCOMMON, 17, Arrays.asList("§7- Iron resources", "§7- Iron mobs"),
            Arrays.asList("§7- Iron Ore", "§7- Iron Ingots", "§7- Iron Essence")
        ));
        resourceIslands.add(new Island(
            "Gold Island", "§6Gold Island", Material.GOLD_INGOT,
            "§7An island rich in gold resources.",
            IslandType.RESOURCE, IslandRarity.RARE, 18, Arrays.asList("§7- Gold resources", "§7- Gold mobs"),
            Arrays.asList("§7- Gold Ore", "§7- Gold Ingots", "§7- Gold Essence")
        ));
        resourceIslands.add(new Island(
            "Diamond Island", "§bDiamond Island", Material.DIAMOND,
            "§7An island rich in diamond resources.",
            IslandType.RESOURCE, IslandRarity.EPIC, 19, Arrays.asList("§7- Diamond resources", "§7- Diamond mobs"),
            Arrays.asList("§7- Diamond Ore", "§7- Diamonds", "§7- Diamond Essence")
        ));
        resourceIslands.add(new Island(
            "Emerald Island", "§aEmerald Island", Material.EMERALD,
            "§7An island rich in emerald resources.",
            IslandType.RESOURCE, IslandRarity.EPIC, 20, Arrays.asList("§7- Emerald resources", "§7- Emerald mobs"),
            Arrays.asList("§7- Emerald Ore", "§7- Emeralds", "§7- Emerald Essence")
        ));
        islands.put(IslandType.RESOURCE, resourceIslands);
        
        // Event Islands
        List<Island> eventIslands = new ArrayList<>();
        eventIslands.add(new Island(
            "Halloween Island", "§6Halloween Island", Material.JACK_O_LANTERN,
            "§7A spooky island for Halloween events.",
            IslandType.EVENT, IslandRarity.RARE, 21, Arrays.asList("§7- Halloween theme", "§7- Spooky mobs"),
            Arrays.asList("§7- Pumpkins", "§7- Jack O' Lanterns", "§7- Halloween Essence")
        ));
        eventIslands.add(new Island(
            "Christmas Island", "§fChristmas Island", Material.SNOW_BLOCK,
            "§7A festive island for Christmas events.",
            IslandType.EVENT, IslandRarity.RARE, 22, Arrays.asList("§7- Christmas theme", "§7- Festive mobs"),
            Arrays.asList("§7- Snow", "§7- Christmas Trees", "§7- Christmas Essence")
        ));
        eventIslands.add(new Island(
            "Easter Island", "§eEaster Island", Material.EGG,
            "§7A colorful island for Easter events.",
            IslandType.EVENT, IslandRarity.RARE, 23, Arrays.asList("§7- Easter theme", "§7- Colorful mobs"),
            Arrays.asList("§7- Eggs", "§7- Easter Baskets", "§7- Easter Essence")
        ));
        islands.put(IslandType.EVENT, eventIslands);
        
        // Mythical Islands
        List<Island> mythicalIslands = new ArrayList<>();
        mythicalIslands.add(new Island(
            "Phoenix Island", "§cPhoenix Island", Material.FIRE_CHARGE,
            "§7A mythical island ruled by a phoenix.",
            IslandType.MYTHICAL, IslandRarity.MYTHIC, 24, Arrays.asList("§7- Phoenix boss", "§7- Fire resources"),
            Arrays.asList("§7- Phoenix Feathers", "§7- Fire Essence", "§7- Phoenix Eggs")
        ));
        mythicalIslands.add(new Island(
            "Unicorn Island", "§fUnicorn Island", Material.HORN_CORAL,
            "§7A magical island where unicorns roam.",
            IslandType.MYTHICAL, IslandRarity.MYTHIC, 25, Arrays.asList("§7- Unicorn mobs", "§7- Magic resources"),
            Arrays.asList("§7- Unicorn Horns", "§7- Magic Essence", "§7- Rainbow Crystals")
        ));
        mythicalIslands.add(new Island(
            "Dragon Island", "§5Dragon Island", Material.DRAGON_EGG,
            "§7A legendary island ruled by ancient dragons.",
            IslandType.MYTHICAL, IslandRarity.MYTHIC, 26, Arrays.asList("§7- Ancient Dragon boss", "§7- Dragon resources"),
            Arrays.asList("§7- Ancient Dragon Scales", "§7- Dragon Essence", "§7- Ancient Dragon Eggs")
        ));
        islands.put(IslandType.MYTHICAL, mythicalIslands);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.displayName() != null ? meta.displayName().toString() : "";
        
        if (displayName.contains("Island") || displayName.contains("Islands")) {
            openIslandGUI(player);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        
        // Handle Island GUI clicks
        if (title.contains("§a§lIslands")) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;
            
            String displayName = meta.displayName() != null ? meta.displayName().toString() : "";
            
            // Handle category clicks
            if (displayName.contains("Basic Islands")) {
                openCategoryGUI(player, IslandType.BASIC);
            } else if (displayName.contains("Special Islands")) {
                openCategoryGUI(player, IslandType.SPECIAL);
            } else if (displayName.contains("Dungeon Islands")) {
                openCategoryGUI(player, IslandType.DUNGEON);
            } else if (displayName.contains("Boss Islands")) {
                openCategoryGUI(player, IslandType.BOSS);
            } else if (displayName.contains("Resource Islands")) {
                openCategoryGUI(player, IslandType.RESOURCE);
            } else if (displayName.contains("Event Islands")) {
                openCategoryGUI(player, IslandType.EVENT);
            } else if (displayName.contains("Mythical Islands")) {
                openCategoryGUI(player, IslandType.MYTHICAL);
            } else if (displayName.contains("Close")) {
                player.closeInventory();
            }
        }
        // Handle category GUI clicks
        else if (title.contains("Islands - ")) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;
            
            String displayName = meta.displayName() != null ? meta.displayName().toString() : "";
            
            if (displayName.contains("Back")) {
                openIslandGUI(player);
            } else if (displayName.contains("Close")) {
                player.closeInventory();
            } else {
                // Find the island by display name
                for (List<Island> islandList : islands.values()) {
                    for (Island island : islandList) {
                        if (displayName.contains(island.getDisplayName())) {
                            openIslandGUI(player, island);
                            return;
                        }
                    }
                }
            }
        }
        // Handle individual island GUI clicks
        else if (title.contains("Island - ")) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;
            
            String displayName = meta.displayName() != null ? meta.displayName().toString() : "";
            
            if (displayName.contains("Back")) {
                // Go back to category - need to determine which category
                String islandName = title.replace("§a§lIsland - ", "");
                for (Map.Entry<IslandType, List<Island>> entry : islands.entrySet()) {
                    for (Island island : entry.getValue()) {
                        if (island.getDisplayName().equals(islandName)) {
                            openCategoryGUI(player, entry.getKey());
                            return;
                        }
                    }
                }
            } else if (displayName.contains("Close")) {
                player.closeInventory();
            } else if (displayName.contains("Teleport to Island")) {
                // Extract island name from title
                String islandName = title.replace("§a§lIsland - ", "");
                for (List<Island> islandList : islands.values()) {
                    for (Island island : islandList) {
                        if (island.getDisplayName().equals(islandName)) {
                            teleportToIsland(player, island);
                            return;
                        }
                    }
                }
            } else if (displayName.contains("Buy Island")) {
                // Extract island name from title
                String islandName = title.replace("§a§lIsland - ", "");
                for (List<Island> islandList : islands.values()) {
                    for (Island island : islandList) {
                        if (island.getDisplayName().equals(islandName)) {
                            createIsland(player, island);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public void openIslandGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§a§lIslands"));
        
        // Add island categories
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lBasic Islands", "§7Basic islands for beginners.");
        addGUIItem(gui, 11, Material.NETHER_STAR, "§d§lSpecial Islands", "§7Special islands with unique features.");
        addGUIItem(gui, 12, Material.ZOMBIE_HEAD, "§c§lDungeon Islands", "§7Dungeon islands with challenges.");
        addGUIItem(gui, 13, Material.DRAGON_EGG, "§6§lBoss Islands", "§7Boss islands with powerful enemies.");
        addGUIItem(gui, 14, Material.DIAMOND, "§b§lResource Islands", "§7Resource islands with valuable materials.");
        addGUIItem(gui, 15, Material.FIREWORK_ROCKET, "§e§lEvent Islands", "§7Event islands for special occasions.");
        addGUIItem(gui, 16, Material.END_CRYSTAL, "§5§lMythical Islands", "§7Mythical islands with legendary creatures.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the island menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aIsland GUI geöffnet!");
    }
    
    public void openCategoryGUI(Player player, IslandType category) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§a§lIslands - " + category.getDisplayName()));
        
        List<Island> categoryIslands = islands.get(category);
        if (categoryIslands != null) {
            int slot = 10;
            for (Island island : categoryIslands) {
                if (slot >= 44) break; // Don't overflow into navigation area
                
                addGUIItem(gui, slot, island.getMaterial(), island.getDisplayName(), island.getDescription());
                slot++;
            }
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to main islands.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the island menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openIslandGUI(Player player, Island island) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§a§lIsland - " + island.getDisplayName()));
        
        // Add island information
        addGUIItem(gui, 10, island.getMaterial(), island.getDisplayName(), island.getDescription());
        
        // Add island features
        int slot = 19;
        for (String feature : island.getFeatures()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.PAPER, "§7" + feature, "§7Feature description.");
            slot++;
        }
        
        // Add island resources
        slot = 28;
        for (String resource : island.getResources()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.EMERALD, "§e" + resource, "§7Resource.");
            slot++;
        }
        
        // Add island actions
        addGUIItem(gui, 37, Material.ENDER_PEARL, "§5§lTeleport to Island", "§7Teleport to this island.");
        addGUIItem(gui, 38, Material.CHEST, "§6§lView Island Info", "§7View detailed island information.");
        addGUIItem(gui, 39, Material.EMERALD, "§e§lBuy Island", "§7Buy this island.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to category.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the island menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.asList(net.kyori.adventure.text.Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void teleportToIsland(Player player, Island island) {
        // Create a new world for the island if it doesn't exist
        World world = Bukkit.getWorld("island_" + island.getName().toLowerCase().replace(" ", "_"));
        if (world == null) {
            // Create new world
            world = Bukkit.createWorld(new org.bukkit.WorldCreator("island_" + island.getName().toLowerCase().replace(" ", "_")));
        }
        
        // Teleport player to island
        Location location = new Location(world, 0, 100, 0);
        player.teleport(location);
        player.sendMessage("§aTeleportiert zu " + island.getDisplayName() + "!");
    }
    
    public void createIsland(Player player, Island island) {
        UUID playerId = player.getUniqueId();
        PlayerIslandData data = getPlayerIslandData(playerId);
        
        // Check if player can create this island
        if (!canCreateIsland(player, island)) {
            player.sendMessage("§cDu kannst diese Insel nicht erstellen!");
            return;
        }
        
        // Create island world
        String worldName = "island_" + playerId.toString().replace("-", "");
        World world = Bukkit.createWorld(new org.bukkit.WorldCreator(worldName));
        
        // Generate island terrain
        generateIslandTerrain(world, island);
        
        // Set player as island owner
        data.unlockIsland(island.getName());
        data.visitIsland(island.getName());
        
        // Create island storage
        createIslandStorage(world, playerId);
        
        player.sendMessage("§aInsel " + island.getDisplayName() + " wurde erfolgreich erstellt!");
        player.sendMessage("§7Verwende /island teleport um zu deiner Insel zu gelangen.");
    }
    
    private boolean canCreateIsland(Player player, Island island) {
        // Check level requirements
        if (island.getLevel() > getPlayerLevel(player)) {
            player.sendMessage("§cDu benötigst Level " + island.getLevel() + " um diese Insel zu erstellen!");
            return false;
        }
        
        // Check if player already has an island of this type
        PlayerIslandData data = getPlayerIslandData(player.getUniqueId());
        if (data.isIslandUnlocked(island.getName())) {
            player.sendMessage("§cDu besitzt bereits diese Insel!");
            return false;
        }
        
        return true;
    }
    
    private int getPlayerLevel(Player player) {
        // This would integrate with the leveling system
        // For now, return a default level
        return 1;
    }
    
    private void generateIslandTerrain(World world, Island island) {
        // Generate basic island terrain based on island type
        Location center = new Location(world, 0, 64, 0);
        
        switch (island.getType()) {
            case BASIC:
                generateBasicIsland(world, center);
                break;
            case SPECIAL:
                generateSpecialIsland(world, center, island);
                break;
            case DUNGEON:
                generateDungeonIsland(world, center, island);
                break;
            case BOSS:
                generateBossIsland(world, center, island);
                break;
            case RESOURCE:
                generateResourceIsland(world, center, island);
                break;
            case EVENT:
                generateEventIsland(world, center, island);
                break;
            case MYTHICAL:
                generateMythicalIsland(world, center, island);
                break;
        }
    }
    
    private void generateBasicIsland(World world, Location center) {
        // Generate a basic grass island
        for (int x = -50; x <= 50; x++) {
            for (int z = -50; z <= 50; z++) {
                Location loc = center.clone().add(x, 0, z);
                double distance = center.distance(loc);
                
                if (distance <= 50) {
                    // Create island base
                    loc.getBlock().setType(Material.GRASS_BLOCK);
                    loc.clone().add(0, -1, 0).getBlock().setType(Material.DIRT);
                    loc.clone().add(0, -2, 0).getBlock().setType(Material.STONE);
                }
            }
        }
    }
    
    private void generateSpecialIsland(World world, Location center, Island island) {
        // Generate special island based on type
        generateBasicIsland(world, center);
        
        // Add special features
        if (island.getName().contains("Mushroom")) {
            // Add mushroom blocks
            for (int i = 0; i < 20; i++) {
                Location loc = center.clone().add(
                    (Math.random() - 0.5) * 100,
                    1,
                    (Math.random() - 0.5) * 100
                );
                loc.getBlock().setType(Material.RED_MUSHROOM_BLOCK);
            }
        }
    }
    
    private void generateDungeonIsland(World world, Location center, Island island) {
        generateBasicIsland(world, center);
        
        // Create dungeon structure
        Location dungeonCenter = center.clone().add(0, 1, 0);
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                for (int y = 0; y <= 10; y++) {
                    Location loc = dungeonCenter.clone().add(x, y, z);
                    if (x == -10 || x == 10 || z == -10 || z == 10 || y == 10) {
                        loc.getBlock().setType(Material.STONE_BRICKS);
                    }
                }
            }
        }
    }
    
    private void generateBossIsland(World world, Location center, Island island) {
        generateBasicIsland(world, center);
        
        // Create boss arena
        Location arenaCenter = center.clone().add(0, 1, 0);
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                Location loc = arenaCenter.clone().add(x, 0, z);
                double distance = arenaCenter.distance(loc);
                
                if (distance <= 20) {
                    loc.getBlock().setType(Material.OBSIDIAN);
                }
            }
        }
    }
    
    private void generateResourceIsland(World world, Location center, Island island) {
        generateBasicIsland(world, center);
        
        // Add resource nodes
        if (island.getName().contains("Iron")) {
            addResourceNodes(world, center, Material.IRON_ORE, 50);
        } else if (island.getName().contains("Gold")) {
            addResourceNodes(world, center, Material.GOLD_ORE, 30);
        } else if (island.getName().contains("Diamond")) {
            addResourceNodes(world, center, Material.DIAMOND_ORE, 20);
        }
    }
    
    private void generateEventIsland(World world, Location center, Island island) {
        generateBasicIsland(world, center);
        
        // Add event-specific decorations
        if (island.getName().contains("Halloween")) {
            addResourceNodes(world, center, Material.PUMPKIN, 30);
        } else if (island.getName().contains("Christmas")) {
            addResourceNodes(world, center, Material.SNOW_BLOCK, 40);
        }
    }
    
    private void generateMythicalIsland(World world, Location center, Island island) {
        generateBasicIsland(world, center);
        
        // Add mythical elements
        Location altar = center.clone().add(0, 1, 0);
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                Location loc = altar.clone().add(x, 0, z);
                double distance = altar.distance(loc);
                
                if (distance <= 5) {
                    loc.getBlock().setType(Material.END_STONE);
                }
            }
        }
    }
    
    private void addResourceNodes(World world, Location center, Material material, int count) {
        for (int i = 0; i < count; i++) {
            Location loc = center.clone().add(
                (Math.random() - 0.5) * 100,
                1,
                (Math.random() - 0.5) * 100
            );
            loc.getBlock().setType(material);
        }
    }
    
    private void createIslandStorage(World world, UUID playerId) {
        // Create island storage chest
        Location storageLocation = new Location(world, 10, 65, 10);
        storageLocation.getBlock().setType(Material.CHEST);
        
        // Create island bank
        Location bankLocation = new Location(world, -10, 65, 10);
        bankLocation.getBlock().setType(Material.ENDER_CHEST);
    }
    
    public PlayerIslandData getPlayerIslandData(UUID playerId) {
        return playerIslandData.computeIfAbsent(playerId, k -> new PlayerIslandData(playerId));
    }
    
    public List<Island> getIslands(IslandType category) {
        return islands.getOrDefault(category, new ArrayList<>());
    }
    
    public enum IslandType {
        BASIC("§aBasic", "§7Basic islands for beginners"),
        SPECIAL("§dSpecial", "§7Special islands with unique features"),
        DUNGEON("§cDungeon", "§7Dungeon islands with challenges"),
        BOSS("§6Boss", "§7Boss islands with powerful enemies"),
        RESOURCE("§bResource", "§7Resource islands with valuable materials"),
        EVENT("§eEvent", "§7Event islands for special occasions"),
        MYTHICAL("§5Mythical", "§7Mythical islands with legendary creatures");
        
        private final String displayName;
        private final String description;
        
        IslandType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum IslandRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        IslandRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class Island {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final IslandType type;
        private final IslandRarity rarity;
        private final int level;
        private final List<String> features;
        private final List<String> resources;
        
        public Island(String name, String displayName, Material material, String description,
                     IslandType type, IslandRarity rarity, int level, List<String> features,
                     List<String> resources) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.level = level;
            this.features = features;
            this.resources = resources;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public IslandType getType() { return type; }
        public IslandRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public List<String> getFeatures() { return features; }
        public List<String> getResources() { return resources; }
    }
    
    public static class PlayerIslandData {
        private final UUID playerId;
        private final Map<String, Boolean> unlockedIslands = new HashMap<>();
        private final Map<String, Integer> islandVisits = new HashMap<>();
        private final Map<String, IslandRole> islandRoles = new HashMap<>();
        private final Map<String, IslandPermissions> islandPermissions = new HashMap<>();
        private long lastUpdate;
        
        public PlayerIslandData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public UUID getPlayerId() {
            return playerId;
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void unlockIsland(String islandName) {
            unlockedIslands.put(islandName, true);
            islandRoles.put(islandName, IslandRole.OWNER);
            islandPermissions.put(islandName, new IslandPermissions(true, true, true, true, true));
        }
        
        public void visitIsland(String islandName) {
            islandVisits.put(islandName, islandVisits.getOrDefault(islandName, 0) + 1);
        }
        
        public boolean isIslandUnlocked(String islandName) {
            return unlockedIslands.getOrDefault(islandName, false);
        }
        
        public int getIslandVisits(String islandName) {
            return islandVisits.getOrDefault(islandName, 0);
        }
        
        public IslandRole getIslandRole(String islandName) {
            return islandRoles.getOrDefault(islandName, IslandRole.VISITOR);
        }
        
        public void setIslandRole(String islandName, IslandRole role) {
            islandRoles.put(islandName, role);
            updatePermissionsForRole(islandName, role);
        }
        
        public IslandPermissions getIslandPermissions(String islandName) {
            return islandPermissions.getOrDefault(islandName, new IslandPermissions(false, false, false, false, false));
        }
        
        private void updatePermissionsForRole(String islandName, IslandRole role) {
            switch (role) {
                case OWNER:
                    islandPermissions.put(islandName, new IslandPermissions(true, true, true, true, true));
                    break;
                case CO_OWNER:
                    islandPermissions.put(islandName, new IslandPermissions(true, true, true, false, true));
                    break;
                case MEMBER:
                    islandPermissions.put(islandName, new IslandPermissions(true, false, false, false, false));
                    break;
                case VISITOR:
                    islandPermissions.put(islandName, new IslandPermissions(false, false, false, false, false));
                    break;
            }
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
    
    public enum IslandRole {
        OWNER("§6Owner", "§7Full access to the island"),
        CO_OWNER("§eCo-Owner", "§7Most permissions except ownership"),
        MEMBER("§aMember", "§7Basic building permissions"),
        VISITOR("§7Visitor", "§7View-only access");
        
        private final String displayName;
        private final String description;
        
        IslandRole(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class IslandPermissions {
        private final boolean canBuild;
        private final boolean canBreak;
        private final boolean canInteract;
        private final boolean canManage;
        private final boolean canInvite;
        
        public IslandPermissions(boolean canBuild, boolean canBreak, boolean canInteract, boolean canManage, boolean canInvite) {
            this.canBuild = canBuild;
            this.canBreak = canBreak;
            this.canInteract = canInteract;
            this.canManage = canManage;
            this.canInvite = canInvite;
        }
        
        public boolean canBuild() { return canBuild; }
        public boolean canBreak() { return canBreak; }
        public boolean canInteract() { return canInteract; }
        public boolean canManage() { return canManage; }
        public boolean canInvite() { return canInvite; }
    }
    
    public void invitePlayerToIsland(Player inviter, Player invitee, String islandName) {
        PlayerIslandData inviterData = getPlayerIslandData(inviter.getUniqueId());
        PlayerIslandData inviteeData = getPlayerIslandData(invitee.getUniqueId());
        
        // Check if inviter has permission to invite
        if (!inviterData.getIslandPermissions(islandName).canInvite()) {
            inviter.sendMessage("§cDu hast keine Berechtigung, Spieler zu dieser Insel einzuladen!");
            return;
        }
        
        // Check if invitee is already a member
        if (inviteeData.getIslandRole(islandName) != IslandRole.VISITOR) {
            inviter.sendMessage("§c" + invitee.getName() + " ist bereits Mitglied dieser Insel!");
            return;
        }
        
        // Set invitee as member
        inviteeData.setIslandRole(islandName, IslandRole.MEMBER);
        
        inviter.sendMessage("§a" + invitee.getName() + " wurde als Mitglied zu " + islandName + " hinzugefügt!");
        invitee.sendMessage("§aDu wurdest zu " + islandName + " eingeladen!");
    }
    
    public void promotePlayer(Player promoter, Player target, String islandName, IslandRole newRole) {
        PlayerIslandData promoterData = getPlayerIslandData(promoter.getUniqueId());
        PlayerIslandData targetData = getPlayerIslandData(target.getUniqueId());
        
        // Check if promoter has permission to manage
        if (!promoterData.getIslandPermissions(islandName).canManage()) {
            promoter.sendMessage("§cDu hast keine Berechtigung, Spieler zu befördern!");
            return;
        }
        
        // Check if promoter's role is higher than target's current role
        IslandRole promoterRole = promoterData.getIslandRole(islandName);
        IslandRole targetCurrentRole = targetData.getIslandRole(islandName);
        
        if (!canPromote(promoterRole, targetCurrentRole, newRole)) {
            promoter.sendMessage("§cDu kannst " + target.getName() + " nicht zu " + newRole.getDisplayName() + " befördern!");
            return;
        }
        
        // Promote target
        targetData.setIslandRole(islandName, newRole);
        
        promoter.sendMessage("§a" + target.getName() + " wurde zu " + newRole.getDisplayName() + " befördert!");
        target.sendMessage("§aDu wurdest zu " + newRole.getDisplayName() + " von " + islandName + " befördert!");
    }
    
    private boolean canPromote(IslandRole promoterRole, IslandRole targetCurrentRole, IslandRole newRole) {
        // Owner can promote anyone to any role except owner
        if (promoterRole == IslandRole.OWNER && newRole != IslandRole.OWNER) {
            return true;
        }
        
        // Co-owner can promote members to co-owner
        if (promoterRole == IslandRole.CO_OWNER && newRole == IslandRole.CO_OWNER && targetCurrentRole == IslandRole.MEMBER) {
            return true;
        }
        
        return false;
    }
    
    public void kickPlayerFromIsland(Player kicker, Player target, String islandName) {
        PlayerIslandData kickerData = getPlayerIslandData(kicker.getUniqueId());
        PlayerIslandData targetData = getPlayerIslandData(target.getUniqueId());
        
        // Check if kicker has permission to manage
        if (!kickerData.getIslandPermissions(islandName).canManage()) {
            kicker.sendMessage("§cDu hast keine Berechtigung, Spieler zu kicken!");
            return;
        }
        
        // Check if kicker's role is higher than target's role
        IslandRole kickerRole = kickerData.getIslandRole(islandName);
        IslandRole targetRole = targetData.getIslandRole(islandName);
        
        if (!canKick(kickerRole, targetRole)) {
            kicker.sendMessage("§cDu kannst " + target.getName() + " nicht kicken!");
            return;
        }
        
        // Kick target
        targetData.setIslandRole(islandName, IslandRole.VISITOR);
        
        kicker.sendMessage("§a" + target.getName() + " wurde von " + islandName + " gekickt!");
        target.sendMessage("§cDu wurdest von " + islandName + " gekickt!");
    }
    
    private boolean canKick(IslandRole kickerRole, IslandRole targetRole) {
        // Owner can kick anyone
        if (kickerRole == IslandRole.OWNER) {
            return true;
        }
        
        // Co-owner can kick members and visitors
        if (kickerRole == IslandRole.CO_OWNER && (targetRole == IslandRole.MEMBER || targetRole == IslandRole.VISITOR)) {
            return true;
        }
        
        return false;
    }
}
