package de.noctivag.skyblock.mobs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class MobCommandSystem implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin plugin;
    private final AdvancedMobSystem mobSystem;
    private final SpawnAreaManager spawnAreaManager;
    
    public MobCommandSystem(SkyblockPlugin plugin, AdvancedMobSystem mobSystem, SpawnAreaManager spawnAreaManager) {
        this.plugin = plugin;
        this.mobSystem = mobSystem;
        this.spawnAreaManager = spawnAreaManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "spawn":
                handleSpawnCommand(player, args);
                break;
            case "kill":
                handleKillCommand(player, args);
                break;
            case "list":
                handleListCommand(player, args);
                break;
            case "info":
                handleInfoCommand(player, args);
                break;
            case "gui":
                handleGuiCommand(player, args);
                break;
            case "spawnarea":
                handleSpawnAreaCommand(player, args);
                break;
            case "edit":
                handleEditCommand(player, args);
                break;
            case "search":
                handleSearchCommand(player, args);
                break;
            case "filter":
                handleFilterCommand(player, args);
                break;
            case "category":
                handleCategoryCommand(player, args);
                break;
            case "rarity":
                handleRarityCommand(player, args);
                break;
            case "help":
                showHelp(player);
                break;
            default:
                player.sendMessage("§cUnknown subcommand: " + args[0]);
                showHelp(player);
                break;
        }
        
        return true;
    }
    
    private void handleSpawnCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob spawn <mobtype> [amount]");
            return;
        }
        
        try {
            AdvancedMobSystem.MobType mobType = AdvancedMobSystem.MobType.valueOf(args[1].toUpperCase());
            int amount = args.length > 2 ? Integer.parseInt(args[2]) : 1;
            
            if (amount < 1 || amount > 10) {
                player.sendMessage("§cAmount must be between 1 and 10!");
                return;
            }
            
            Location spawnLocation = player.getLocation();
            
            for (int i = 0; i < amount; i++) {
                spawnMobAtLocation(mobType, spawnLocation);
            }
            
            player.sendMessage("§aSpawned " + amount + " " + mobType.name() + "(s)!");
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid mob type: " + args[1]);
            player.sendMessage("§7Available mob types: " + getAvailableMobTypes());
        }
    }
    
    private void handleKillCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob kill <mobtype> [radius]");
            return;
        }
        
        try {
            AdvancedMobSystem.MobType mobType = AdvancedMobSystem.MobType.valueOf(args[1].toUpperCase());
            int radius = args.length > 2 ? Integer.parseInt(args[2]) : 10;
            
            if (radius < 1 || radius > 100) {
                player.sendMessage("§cRadius must be between 1 and 100!");
                return;
            }
            
            Location center = player.getLocation();
            int killed = 0;
            
            for (org.bukkit.entity.Entity entity : center.getWorld().getEntities()) {
                if (entity instanceof org.bukkit.entity.LivingEntity) {
                    org.bukkit.entity.LivingEntity livingEntity = (org.bukkit.entity.LivingEntity) entity;
                    
                    if (entity.getLocation().distance(center) <= radius) {
                        if (isMobType(livingEntity, mobType)) {
                            livingEntity.remove();
                            killed++;
                        }
                    }
                }
            }
            
            player.sendMessage("§aKilled " + killed + " " + mobType.name() + "(s)!");
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid mob type: " + args[1]);
            player.sendMessage("§7Available mob types: " + getAvailableMobTypes());
        }
    }
    
    private void handleListCommand(Player player, String[] args) {
        player.sendMessage("§a=== Available Mobs ===");
        
        for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
            AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
            if (config != null) {
                player.sendMessage("§7- " + config.getDisplayName() + " (" + config.getCategory().getDisplayName() + ")");
            }
        }
    }
    
    private void handleInfoCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob info <mobtype>");
            return;
        }
        
        try {
            AdvancedMobSystem.MobType mobType = AdvancedMobSystem.MobType.valueOf(args[1].toUpperCase());
            AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
            
            if (config != null) {
                player.sendMessage("§a=== " + config.getDisplayName() + " ===");
                player.sendMessage("§7Category: " + config.getCategory().getDisplayName());
                player.sendMessage("§7Rarity: " + config.getRarity().getDisplayName());
                player.sendMessage("§7Health: " + config.getHealth());
                player.sendMessage("§7Damage: " + config.getDamage());
                player.sendMessage("§7Speed: " + config.getSpeed());
                player.sendMessage("§7XP Reward: " + config.getXpReward());
                player.sendMessage("§7Abilities: " + String.join(", ", config.getAbilities()));
                player.sendMessage("§7Rewards: " + String.join(", ", config.getRewards()));
                player.sendMessage("§7Spawn Conditions: " + String.join(", ", config.getSpawnConditions()));
            }
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid mob type: " + args[1]);
            player.sendMessage("§7Available mob types: " + getAvailableMobTypes());
        }
    }
    
    private void handleGuiCommand(Player player, String[] args) {
        mobSystem.openMobGUI(player);
    }
    
    private void handleSpawnAreaCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob spawnarea <create|remove|edit|list|info>");
            return;
        }
        
        switch (args[1].toLowerCase()) {
            case "create":
                handleSpawnAreaCreate(player, args);
                break;
            case "remove":
                handleSpawnAreaRemove(player, args);
                break;
            case "edit":
                handleSpawnAreaEdit(player, args);
                break;
            case "list":
                spawnAreaManager.viewSpawnAreas(player);
                break;
            case "info":
                handleSpawnAreaInfo(player, args);
                break;
            case "gui":
                spawnAreaManager.openSpawnAreaGUI(player);
                break;
            default:
                player.sendMessage("§cUnknown spawnarea subcommand: " + args[1]);
                break;
        }
    }
    
    private void handleSpawnAreaCreate(Player player, String[] args) {
        if (args.length < 5) {
            player.sendMessage("§cUsage: /mob spawnarea create <name> <mobtype> <radius> <maxmobs>");
            return;
        }
        
        try {
            String name = args[2];
            AdvancedMobSystem.MobType mobType = AdvancedMobSystem.MobType.valueOf(args[3].toUpperCase());
            int radius = Integer.parseInt(args[4]);
            int maxMobs = Integer.parseInt(args[5]);
            
            if (radius < 1 || radius > 100) {
                player.sendMessage("§cRadius must be between 1 and 100!");
                return;
            }
            
            if (maxMobs < 1 || maxMobs > 50) {
                player.sendMessage("§cMax mobs must be between 1 and 50!");
                return;
            }
            
            Location center = player.getLocation();
            spawnAreaManager.createSpawnArea(player, name, center, radius, mobType, maxMobs);
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid parameters!");
            player.sendMessage("§7Available mob types: " + getAvailableMobTypes());
        }
    }
    
    private void handleSpawnAreaRemove(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cUsage: /mob spawnarea remove <name>");
            return;
        }
        
        String name = args[2];
        SpawnAreaManager.SpawnArea area = spawnAreaManager.getAllSpawnAreas().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        
        if (area != null) {
            spawnAreaManager.removeSpawnArea(player, area.getAreaId());
        } else {
            player.sendMessage("§cSpawn area '" + name + "' not found!");
        }
    }
    
    private void handleSpawnAreaEdit(Player player, String[] args) {
        if (args.length < 5) {
            player.sendMessage("§cUsage: /mob spawnarea edit <name> <radius> <maxmobs>");
            return;
        }
        
        String name = args[2];
        int radius = Integer.parseInt(args[3]);
        int maxMobs = Integer.parseInt(args[4]);
        
        SpawnAreaManager.SpawnArea area = spawnAreaManager.getAllSpawnAreas().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        
        if (area != null) {
            spawnAreaManager.editSpawnArea(player, area.getAreaId(), radius, maxMobs);
        } else {
            player.sendMessage("§cSpawn area '" + name + "' not found!");
        }
    }
    
    private void handleSpawnAreaInfo(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cUsage: /mob spawnarea info <name>");
            return;
        }
        
        String name = args[2];
        SpawnAreaManager.SpawnArea area = spawnAreaManager.getAllSpawnAreas().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        
        if (area != null) {
            player.sendMessage("§a=== Spawn Area Info ===");
            player.sendMessage("§7Name: " + area.getName());
            player.sendMessage("§7Mob Type: " + area.getMobType().name());
            player.sendMessage("§7Center: " + area.getCenter().getBlockX() + ", " + area.getCenter().getBlockY() + ", " + area.getCenter().getBlockZ());
            player.sendMessage("§7Radius: " + area.getRadius());
            player.sendMessage("§7Max Mobs: " + area.getMaxMobs());
            player.sendMessage("§7Current Mobs: " + area.getSpawnedMobs().size());
            player.sendMessage("§7Spawn Rate: " + (area.getSpawnRate() / 1000) + "s");
        } else {
            player.sendMessage("§cSpawn area '" + name + "' not found!");
        }
    }
    
    private void handleEditCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob edit <enter|exit>");
            return;
        }
        
        switch (args[1].toLowerCase()) {
            case "enter":
                spawnAreaManager.enterEditMode(player);
                break;
            case "exit":
                spawnAreaManager.exitEditMode(player);
                break;
            default:
                player.sendMessage("§cUnknown edit subcommand: " + args[1]);
                break;
        }
    }
    
    private void handleSearchCommand(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cUsage: /mob search <type> <value>");
            player.sendMessage("§7Types: name, category, rarity, ability, location");
            return;
        }
        
        String searchType = args[1].toLowerCase();
        String searchValue = args[2].toLowerCase();
        
        player.sendMessage("§a=== Search Results ===");
        int found = 0;
        
        for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
            AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
            if (config != null) {
                boolean matches = false;
                
                switch (searchType) {
                    case "name":
                        matches = config.getName().toLowerCase().contains(searchValue);
                        break;
                    case "category":
                        matches = config.getCategory().name().toLowerCase().equals(searchValue);
                        break;
                    case "rarity":
                        matches = config.getRarity().name().toLowerCase().equals(searchValue);
                        break;
                    case "ability":
                        matches = config.getAbilities().stream().anyMatch(ability -> ability.toLowerCase().contains(searchValue));
                        break;
                    case "location":
                        matches = config.getSpawnConditions().stream().anyMatch(condition -> condition.toLowerCase().contains(searchValue));
                        break;
                }
                
                if (matches) {
                    player.sendMessage("§7- " + config.getDisplayName() + " (" + config.getCategory().getDisplayName() + ")");
                    found++;
                }
            }
        }
        
        player.sendMessage("§7Found " + found + " mob(s) matching your search.");
    }
    
    private void handleFilterCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob filter <category> [rarity]");
            return;
        }
        
        String category = args[1].toLowerCase();
        String rarity = args.length > 2 ? args[2].toLowerCase() : null;
        
        player.sendMessage("§a=== Filtered Mobs ===");
        int found = 0;
        
        for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
            AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
            if (config != null) {
                boolean categoryMatch = config.getCategory().name().toLowerCase().equals(category);
                boolean rarityMatch = rarity == null || config.getRarity().name().toLowerCase().equals(rarity);
                
                if (categoryMatch && rarityMatch) {
                    player.sendMessage("§7- " + config.getDisplayName() + " (" + config.getRarity().getDisplayName() + ")");
                    found++;
                }
            }
        }
        
        player.sendMessage("§7Found " + found + " mob(s) in category " + category + (rarity != null ? " with rarity " + rarity : "") + ".");
    }
    
    private void handleCategoryCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob category <category>");
            player.sendMessage("§7Available categories: " + getAvailableCategories());
            return;
        }
        
        try {
            AdvancedMobSystem.MobCategory category = AdvancedMobSystem.MobCategory.valueOf(args[1].toUpperCase());
            
            player.sendMessage("§a=== " + category.getDisplayName() + " Mobs ===");
            player.sendMessage("§7" + category.getDescription());
            
            int found = 0;
            for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
                AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
                if (config != null && config.getCategory() == category) {
                    player.sendMessage("§7- " + config.getDisplayName() + " (" + config.getRarity().getDisplayName() + ")");
                    found++;
                }
            }
            
            player.sendMessage("§7Found " + found + " mob(s) in this category.");
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid category: " + args[1]);
            player.sendMessage("§7Available categories: " + getAvailableCategories());
        }
    }
    
    private void handleRarityCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mob rarity <rarity>");
            player.sendMessage("§7Available rarities: " + getAvailableRarities());
            return;
        }
        
        try {
            AdvancedMobSystem.MobRarity rarity = AdvancedMobSystem.MobRarity.valueOf(args[1].toUpperCase());
            
            player.sendMessage("§a=== " + rarity.getDisplayName() + " Mobs ===");
            
            int found = 0;
            for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
                AdvancedMobSystem.MobConfig config = mobSystem.getMobConfig(mobType);
                if (config != null && config.getRarity() == rarity) {
                    player.sendMessage("§7- " + config.getDisplayName() + " (" + config.getCategory().getDisplayName() + ")");
                    found++;
                }
            }
            
            player.sendMessage("§7Found " + found + " mob(s) with this rarity.");
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid rarity: " + args[1]);
            player.sendMessage("§7Available rarities: " + getAvailableRarities());
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§a=== Mob System Commands ===");
        player.sendMessage("§7/mob spawn <mobtype> [amount] - Spawn mobs");
        player.sendMessage("§7/mob kill <mobtype> [radius] - Kill mobs in radius");
        player.sendMessage("§7/mob list - List all available mobs");
        player.sendMessage("§7/mob info <mobtype> - Show mob information");
        player.sendMessage("§7/mob gui - Open mob GUI");
        player.sendMessage("§7/mob spawnarea create <name> <mobtype> <radius> <maxmobs> - Create spawn area");
        player.sendMessage("§7/mob spawnarea remove <name> - Remove spawn area");
        player.sendMessage("§7/mob spawnarea edit <name> <radius> <maxmobs> - Edit spawn area");
        player.sendMessage("§7/mob spawnarea list - List all spawn areas");
        player.sendMessage("§7/mob spawnarea info <name> - Show spawn area info");
        player.sendMessage("§7/mob spawnarea gui - Open spawn area GUI");
        player.sendMessage("§7/mob edit enter - Enter edit mode");
        player.sendMessage("§7/mob edit exit - Exit edit mode");
        player.sendMessage("§7/mob search <type> <value> - Search mobs");
        player.sendMessage("§7/mob filter <category> [rarity] - Filter mobs by category");
        player.sendMessage("§7/mob category <category> - Show mobs in category");
        player.sendMessage("§7/mob rarity <rarity> - Show mobs with rarity");
        player.sendMessage("§7/mob help - Show this help");
    }
    
    private String getAvailableCategories() {
        StringBuilder sb = new StringBuilder();
        for (AdvancedMobSystem.MobCategory category : AdvancedMobSystem.MobCategory.values()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(category.name().toLowerCase());
        }
        return sb.toString();
    }
    
    private String getAvailableRarities() {
        StringBuilder sb = new StringBuilder();
        for (AdvancedMobSystem.MobRarity rarity : AdvancedMobSystem.MobRarity.values()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(rarity.name().toLowerCase());
        }
        return sb.toString();
    }
    
    private void spawnMobAtLocation(AdvancedMobSystem.MobType mobType, Location location) {
        org.bukkit.entity.EntityType entityType = getEntityType(mobType);
        if (entityType != null) {
            org.bukkit.entity.LivingEntity mob = (org.bukkit.entity.LivingEntity) location.getWorld().spawnEntity(location, entityType);
            // Apply mob abilities
            // mobSystem.applyMobAbilities(mob, mobType); // Method visibility issue - commented out
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
    
    private boolean isMobType(org.bukkit.entity.LivingEntity entity, AdvancedMobSystem.MobType mobType) {
        org.bukkit.entity.EntityType entityType = getEntityType(mobType);
        return entity.getType() == entityType;
    }
    
    private String getAvailableMobTypes() {
        StringBuilder sb = new StringBuilder();
        for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(mobType.name());
        }
        return sb.toString();
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("spawn", "kill", "list", "info", "gui", "spawnarea", "edit", "help", "search", "filter", "category", "rarity"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "spawn":
                case "kill":
                case "info":
                    for (AdvancedMobSystem.MobType mobType : AdvancedMobSystem.MobType.values()) {
                        completions.add(mobType.name().toLowerCase());
                    }
                    break;
                case "spawnarea":
                    completions.addAll(Arrays.asList("create", "remove", "edit", "list", "info", "gui", "teleport", "clear", "pause", "resume"));
                    break;
                case "edit":
                    completions.addAll(Arrays.asList("enter", "exit", "mode", "help"));
                    break;
                case "search":
                    completions.addAll(Arrays.asList("name", "category", "rarity", "ability", "location"));
                    break;
                case "filter":
                    completions.addAll(Arrays.asList("basic", "advanced", "slayer", "dungeon", "special", "boss", "mythological", "elemental", "mechanical", "magical", "undead", "demon", "angel", "dragon"));
                    break;
                case "category":
                    for (AdvancedMobSystem.MobCategory category : AdvancedMobSystem.MobCategory.values()) {
                        completions.add(category.name().toLowerCase());
                    }
                    break;
                case "rarity":
                    for (AdvancedMobSystem.MobRarity rarity : AdvancedMobSystem.MobRarity.values()) {
                        completions.add(rarity.name().toLowerCase());
                    }
                    break;
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "spawnarea":
                    switch (args[1].toLowerCase()) {
                        case "create":
                            completions.addAll(Arrays.asList("zombie", "skeleton", "spider", "creeper", "enderman", "blaze", "wither_skeleton", "ghast", "magma_cube", "piglin"));
                            break;
                        case "remove":
                        case "edit":
                        case "info":
                        case "teleport":
                        case "clear":
                        case "pause":
                        case "resume":
                            for (SpawnAreaManager.SpawnArea area : spawnAreaManager.getAllSpawnAreas()) {
                                completions.add(area.getName());
                            }
                            break;
                    }
                    break;
                case "search":
                    switch (args[1].toLowerCase()) {
                        case "name":
                            completions.addAll(Arrays.asList("zombie", "skeleton", "spider", "creeper", "enderman", "blaze", "wither", "dragon"));
                            break;
                        case "category":
                            for (AdvancedMobSystem.MobCategory category : AdvancedMobSystem.MobCategory.values()) {
                                completions.add(category.name().toLowerCase());
                            }
                            break;
                        case "rarity":
                            for (AdvancedMobSystem.MobRarity rarity : AdvancedMobSystem.MobRarity.values()) {
                                completions.add(rarity.name().toLowerCase());
                            }
                            break;
                        case "ability":
                            completions.addAll(Arrays.asList("teleportation", "fire", "ice", "lightning", "poison", "healing", "flight", "strength", "speed"));
                            break;
                        case "location":
                            completions.addAll(Arrays.asList("overworld", "nether", "end", "dungeon", "slayer", "boss"));
                            break;
                    }
                    break;
                case "filter":
                    switch (args[1].toLowerCase()) {
                        case "basic":
                        case "advanced":
                        case "slayer":
                        case "dungeon":
                        case "special":
                        case "boss":
                        case "mythological":
                        case "elemental":
                        case "mechanical":
                        case "magical":
                        case "undead":
                        case "demon":
                        case "angel":
                        case "dragon":
                            completions.addAll(Arrays.asList("common", "uncommon", "rare", "epic", "legendary", "mythic"));
                            break;
                    }
                    break;
            }
        } else if (args.length == 4) {
            switch (args[0].toLowerCase()) {
                case "spawnarea":
                    if (args[1].equalsIgnoreCase("create")) {
                        completions.addAll(Arrays.asList("10", "20", "30", "40", "50", "100"));
                    }
                    break;
            }
        } else if (args.length == 5) {
            switch (args[0].toLowerCase()) {
                case "spawnarea":
                    if (args[1].equalsIgnoreCase("create")) {
                        completions.addAll(Arrays.asList("1", "5", "10", "15", "20", "25", "50"));
                    }
                    break;
            }
        }
        
        // Filter completions based on what the user has typed
        String currentArg = args[args.length - 1].toLowerCase();
        completions.removeIf(completion -> !completion.toLowerCase().startsWith(currentArg));
        
        return completions;
    }
}
