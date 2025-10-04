package de.noctivag.plugin.mobs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnAreaManager implements Listener {
    
    private final Plugin plugin;
    private final AdvancedMobSystem mobSystem;
    private final Map<UUID, SpawnArea> spawnAreas = new ConcurrentHashMap<>();
    private final Map<UUID, Player> playersInEditMode = new ConcurrentHashMap<>();
    
    public SpawnAreaManager(Plugin plugin, AdvancedMobSystem mobSystem) {
        this.plugin = plugin;
        this.mobSystem = mobSystem;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Spawn Area")) {
            openSpawnAreaGUI(player);
        }
    }
    
    public void openSpawnAreaGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lSpawn Area Manager");
        
        // Add spawn area management options
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lCreate Spawn Area", "§7Create a new spawn area.");
        addGUIItem(gui, 11, Material.REDSTONE_BLOCK, "§c§lRemove Spawn Area", "§7Remove an existing spawn area.");
        addGUIItem(gui, 12, Material.EMERALD_BLOCK, "§a§lEdit Spawn Area", "§7Edit an existing spawn area.");
        addGUIItem(gui, 13, Material.DIAMOND_BLOCK, "§b§lView Spawn Areas", "§7View all spawn areas.");
        addGUIItem(gui, 14, Material.GOLD_BLOCK, "§6§lSpawn Area Info", "§7View spawn area information.");
        
        // Add mob type selection
        addGUIItem(gui, 19, Material.ZOMBIE_HEAD, "§2§lZombie Spawn", "§7Create zombie spawn area.");
        addGUIItem(gui, 20, Material.SKELETON_SKULL, "§f§lSkeleton Spawn", "§7Create skeleton spawn area.");
        addGUIItem(gui, 21, Material.SPIDER_EYE, "§8§lSpider Spawn", "§7Create spider spawn area.");
        addGUIItem(gui, 22, Material.GUNPOWDER, "§a§lCreeper Spawn", "§7Create creeper spawn area.");
        addGUIItem(gui, 23, Material.ENDER_PEARL, "§5§lEnderman Spawn", "§7Create enderman spawn area.");
        addGUIItem(gui, 24, Material.BLAZE_ROD, "§6§lBlaze Spawn", "§7Create blaze spawn area.");
        addGUIItem(gui, 25, Material.WITHER_SKELETON_SKULL, "§8§lWither Skeleton Spawn", "§7Create wither skeleton spawn area.");
        addGUIItem(gui, 26, Material.GHAST_TEAR, "§f§lGhast Spawn", "§7Create ghast spawn area.");
        addGUIItem(gui, 27, Material.MAGMA_CREAM, "§c§lMagma Cube Spawn", "§7Create magma cube spawn area.");
        addGUIItem(gui, 28, Material.GOLD_INGOT, "§6§lPiglin Spawn", "§7Create piglin spawn area.");
        
        // Add spawn area settings
        addGUIItem(gui, 37, Material.IRON_INGOT, "§7§lRadius: 10", "§7Set spawn area radius.");
        addGUIItem(gui, 38, Material.GOLD_INGOT, "§6§lMax Mobs: 5", "§7Set maximum mobs in area.");
        addGUIItem(gui, 39, Material.DIAMOND, "§b§lSpawn Rate: 5s", "§7Set mob spawn rate.");
        addGUIItem(gui, 40, Material.EMERALD, "§a§lSpawn Height: 64", "§7Set spawn height.");
        addGUIItem(gui, 41, Material.REDSTONE, "§c§lSpawn Conditions", "§7Set spawn conditions.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aSpawn Area Manager geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void createSpawnArea(Player player, String name, Location center, int radius, AdvancedMobSystem.MobType mobType, int maxMobs) {
        UUID areaId = UUID.randomUUID();
        SpawnArea area = new SpawnArea(areaId, name, center, radius, mobType, maxMobs);
        spawnAreas.put(areaId, area);
        
        player.sendMessage("§aSpawn area '" + name + "' created successfully!");
        player.sendMessage("§7- Center: " + center.getBlockX() + ", " + center.getBlockY() + ", " + center.getBlockZ());
        player.sendMessage("§7- Radius: " + radius);
        player.sendMessage("§7- Mob Type: " + mobType.name());
        player.sendMessage("§7- Max Mobs: " + maxMobs);
    }
    
    public void removeSpawnArea(Player player, UUID areaId) {
        SpawnArea area = spawnAreas.remove(areaId);
        if (area != null) {
            player.sendMessage("§aSpawn area '" + area.getName() + "' removed successfully!");
        } else {
            player.sendMessage("§cSpawn area not found!");
        }
    }
    
    public void editSpawnArea(Player player, UUID areaId, int newRadius, int newMaxMobs) {
        SpawnArea area = spawnAreas.get(areaId);
        if (area != null) {
            area.setRadius(newRadius);
            area.setMaxMobs(newMaxMobs);
            player.sendMessage("§aSpawn area '" + area.getName() + "' updated successfully!");
        } else {
            player.sendMessage("§cSpawn area not found!");
        }
    }
    
    public void viewSpawnAreas(Player player) {
        player.sendMessage("§a=== Spawn Areas ===");
        if (spawnAreas.isEmpty()) {
            player.sendMessage("§7No spawn areas found.");
        } else {
            for (SpawnArea area : spawnAreas.values()) {
                player.sendMessage("§7- " + area.getName() + " (" + area.getMobType().name() + ")");
                player.sendMessage("§7  Center: " + area.getCenter().getBlockX() + ", " + area.getCenter().getBlockY() + ", " + area.getCenter().getBlockZ());
                player.sendMessage("§7  Radius: " + area.getRadius() + ", Max Mobs: " + area.getMaxMobs());
            }
        }
    }
    
    public void enterEditMode(Player player) {
        playersInEditMode.put(player.getUniqueId(), player);
        player.sendMessage("§aEdit mode activated! Right-click to select spawn area center.");
        player.sendMessage("§7Use /spawnarea exit to exit edit mode.");
    }
    
    public void exitEditMode(Player player) {
        playersInEditMode.remove(player.getUniqueId());
        player.sendMessage("§aEdit mode deactivated!");
    }
    
    public boolean isInEditMode(Player player) {
        return playersInEditMode.containsKey(player.getUniqueId());
    }
    
    public SpawnArea getSpawnArea(UUID areaId) {
        return spawnAreas.get(areaId);
    }
    
    public List<SpawnArea> getAllSpawnAreas() {
        return new ArrayList<>(spawnAreas.values());
    }
    
    public void startSpawnAreaUpdates() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (SpawnArea area : spawnAreas.values()) {
                    area.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 5L); // Update every 5 seconds
    }
    
    public static class SpawnArea {
        private final UUID areaId;
        private String name;
        private final Location center;
        private int radius;
        private final AdvancedMobSystem.MobType mobType;
        private int maxMobs;
        private final List<org.bukkit.entity.LivingEntity> spawnedMobs = new ArrayList<>();
        private long lastSpawn;
        private int spawnRate = 5000; // 5 seconds
        
        public SpawnArea(UUID areaId, String name, Location center, int radius, AdvancedMobSystem.MobType mobType, int maxMobs) {
            this.areaId = areaId;
            this.name = name;
            this.center = center;
            this.radius = radius;
            this.mobType = mobType;
            this.maxMobs = maxMobs;
            this.lastSpawn = System.currentTimeMillis();
        }
        
        public void update() {
            // Remove dead mobs
            spawnedMobs.removeIf(mob -> mob.isDead() || !mob.isValid());
            
            // Spawn new mobs if needed
            if (spawnedMobs.size() < maxMobs && System.currentTimeMillis() - lastSpawn > spawnRate) {
                spawnMob();
                lastSpawn = System.currentTimeMillis();
            }
        }
        
        private void spawnMob() {
            // Generate random location within radius
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.random() * radius;
            
            double x = center.getX() + Math.cos(angle) * distance;
            double z = center.getZ() + Math.sin(angle) * distance;
            double y = center.getY();
            
            Location spawnLocation = new Location(center.getWorld(), x, y, z);
            
            // Spawn the mob
            org.bukkit.entity.EntityType entityType = getEntityType(mobType);
            if (entityType != null) {
                org.bukkit.entity.LivingEntity mob = (org.bukkit.entity.LivingEntity) center.getWorld().spawnEntity(spawnLocation, entityType);
                spawnedMobs.add(mob);
            }
        }
        
        private org.bukkit.entity.EntityType getEntityType(AdvancedMobSystem.MobType mobType) {
            switch (mobType) {
                case ZOMBIE:
                case REVENANT_HORROR_I:
                case DUNGEON_ZOMBIE:
                    return org.bukkit.entity.EntityType.ZOMBIE;
                case SKELETON:
                case DUNGEON_SKELETON:
                    return org.bukkit.entity.EntityType.SKELETON;
                case SPIDER:
                case TARANTULA_BROODFATHER_I:
                case DUNGEON_SPIDER:
                    return org.bukkit.entity.EntityType.SPIDER;
                case CREEPER:
                    return org.bukkit.entity.EntityType.CREEPER;
                case ENDERMAN:
                case VOIDGLOOM_SERAPH_I:
                case DUNGEON_ENDERMAN:
                    return org.bukkit.entity.EntityType.ENDERMAN;
                case BLAZE:
                case INFERNO_DEMONLORD:
                case DUNGEON_BLAZE:
                    return org.bukkit.entity.EntityType.BLAZE;
                case WITHER_SKELETON:
                    return org.bukkit.entity.EntityType.WITHER_SKELETON;
                case GHAST:
                    return org.bukkit.entity.EntityType.GHAST;
                case MAGMA_CUBE:
                    return org.bukkit.entity.EntityType.MAGMA_CUBE;
                case PIGLIN:
                    return org.bukkit.entity.EntityType.PIGLIN;
                case SVEN_PACKMASTER_I:
                    return org.bukkit.entity.EntityType.WOLF;
                case GOLEM:
                    return org.bukkit.entity.EntityType.IRON_GOLEM;
                case WITHER:
                    return org.bukkit.entity.EntityType.WITHER;
                case ENDER_DRAGON:
                    return org.bukkit.entity.EntityType.ENDER_DRAGON;
                default:
                    return org.bukkit.entity.EntityType.ZOMBIE;
            }
        }
        
        public UUID getAreaId() { return areaId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Location getCenter() { return center; }
        public int getRadius() { return radius; }
        public void setRadius(int radius) { this.radius = radius; }
        public AdvancedMobSystem.MobType getMobType() { return mobType; }
        public int getMaxMobs() { return maxMobs; }
        public void setMaxMobs(int maxMobs) { this.maxMobs = maxMobs; }
        public List<org.bukkit.entity.LivingEntity> getSpawnedMobs() { return spawnedMobs; }
        public long getLastSpawn() { return lastSpawn; }
        public int getSpawnRate() { return spawnRate; }
        public void setSpawnRate(int spawnRate) { this.spawnRate = spawnRate; }
    }
}
