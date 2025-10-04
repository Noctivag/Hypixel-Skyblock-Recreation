package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pet System inspired by Hypixel Skyblock
 */
public class PetSystem implements Listener {

    private final SkyblockPlugin plugin;
    @SuppressWarnings("unused")
    private final MultiServerDatabaseManager databaseManager;
    private final HealthManaSystem healthManaSystem;
    private final AdvancedSkillsSystem skillsSystem;
    private final Map<UUID, PlayerPetData> playerPets = new ConcurrentHashMap<>();
    private final Map<UUID, Pet> activePets = new ConcurrentHashMap<>();

    public PetSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                    HealthManaSystem healthManaSystem, AdvancedSkillsSystem skillsSystem) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.healthManaSystem = healthManaSystem;
        this.skillsSystem = skillsSystem;
    }

    /**
     * Initialize the pet system after construction
     */
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startPetUpdateTask();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Load player pet data
        PlayerPetData data = loadPlayerPetData(playerId);
        playerPets.put(playerId, data);

        // Spawn active pet if any
        spawnActivePet(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Save player pet data
        savePlayerPetData(playerId);

        // Despawn active pet
        despawnActivePet(player);

        // Remove from memory
        playerPets.remove(playerId);
        activePets.remove(playerId);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        Player player = event.getEntity().getKiller();
        Pet activePet = activePets.get(player.getUniqueId());

        if (activePet != null) {
            // Give pet XP
            givePetXP(player, activePet, getPetXPFromKill(event.getEntity().getType()));
        }
    }

    private void startPetUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        updateActivePet(player);
                    }
                    Thread.sleep(20L * 5L * 50); // Every 5 seconds = 5,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void updateActivePet(Player player) {
        Pet activePet = activePets.get(player.getUniqueId());
        if (activePet == null) return;

        // Apply pet bonuses
        applyPetBonuses(player, activePet);

        // Update pet stats
        updatePetStats(player, activePet);
    }

    private void applyPetBonuses(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        HealthManaSystem.PlayerHealthManaData healthManaData = healthManaSystem.getPlayerData(playerId);
        AdvancedSkillsSystem.PlayerSkillsData skillsData = skillsSystem.getPlayerSkillsData(playerId);

        if (healthManaData == null || skillsData == null) return;

        // Apply pet stat bonuses
        switch (pet.getType()) {
            case WOLF:
                // Wolf pet bonuses
                healthManaData.setHealthFromSkills(healthManaData.getHealthFromSkills() + pet.getLevel() * 2.0);
                skillsData.addStrengthBonus(pet.getLevel() * 0.5);
                break;

            case CAT:
                // Cat pet bonuses
                skillsData.addSpeedBonus(pet.getLevel() * 0.1);
                skillsData.addLuckBonus(pet.getLevel() * 0.2);
                break;

            case HORSE:
                // Horse pet bonuses
                healthManaData.setManaFromSkills(healthManaData.getManaFromSkills() + pet.getLevel() * 1.5);
                skillsData.addSpeedBonus(pet.getLevel() * 0.2);
                break;

            case PARROT:
                // Parrot pet bonuses
                skillsData.addIntelligenceBonus(pet.getLevel() * 1.0);
                skillsData.addManaRegenBonus(pet.getLevel() * 0.1);
                break;

            case RABBIT:
                // Rabbit pet bonuses
                skillsData.addHealthRegenBonus(pet.getLevel() * 0.1);
                skillsData.addLuckBonus(pet.getLevel() * 0.3);
                break;

            case BEE:
                // Bee pet bonuses
                skillsData.addMagicFindBonus(pet.getLevel() * 0.1);
                skillsData.addPetLuckBonus(pet.getLevel() * 0.2);
                break;

            case ENDERMAN:
                // Enderman pet bonuses
                skillsData.addIntelligenceBonus(pet.getLevel() * 2.0);
                healthManaData.setManaFromSkills(healthManaData.getManaFromSkills() + pet.getLevel() * 3.0);
                break;

            case DRAGON:
                // Dragon pet bonuses
                healthManaData.setHealthFromSkills(healthManaData.getHealthFromSkills() + pet.getLevel() * 5.0);
                skillsData.addStrengthBonus(pet.getLevel() * 2.0);
                skillsData.addDefenseBonus(pet.getLevel() * 1.0);
                break;
        }
    }

    private void updatePetStats(Player player, Pet pet) {
        // Update pet level and stats
        if (pet.getXP() >= getXPRequiredForLevel(pet.getLevel() + 1)) {
            levelUpPet(player, pet);
        }
    }

    @SuppressWarnings("deprecation")
    private void levelUpPet(Player player, Pet pet) {
        int newLevel = pet.getLevel() + 1;
        pet.setLevel(newLevel);
        pet.setXP(pet.getXP() - getXPRequiredForLevel(newLevel));

        // Send level up message
        player.sendMessage("§6§lPET LEVEL UP! §e" + pet.getName() + " §7is now level §e" + newLevel);
        // Send title using legacy API for compatibility
        player.sendTitle("§6§lPET LEVEL UP!", "§e" + pet.getName() + " §7Level §e" + newLevel, 10, 40, 10);

        // Apply new level bonuses
        applyPetBonuses(player, pet);
    }

    private void givePetXP(Player player, Pet pet, double xp) {
        pet.addXP(xp);
        player.sendMessage("§a+" + String.format("%.1f", xp) + " Pet XP");
    }

    private double getPetXPFromKill(EntityType entityType) {
        return switch (entityType) {
            case ZOMBIE, SKELETON, SPIDER -> 5.0;
            case CREEPER -> 8.0;
            case ENDERMAN -> 15.0;
            case BLAZE -> 20.0;
            case GHAST -> 25.0;
            case WITHER_SKELETON -> 50.0;
            case ENDER_DRAGON -> 500.0;
            case WITHER -> 200.0;
            default -> 3.0;
        };
    }

    private double getXPRequiredForLevel(int level) {
        // XP formula for pet leveling
        return level * 100.0 + (level * level * 10.0);
    }

    private void spawnActivePet(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerPetData data = playerPets.get(playerId);

        if (data == null || data.getActivePet() == null) return;

        Pet activePet = data.getActivePet();
        activePets.put(playerId, activePet);

        // Spawn pet entity
        // This would spawn the actual pet entity in the world
        player.sendMessage("§a" + activePet.getName() + " has been summoned!");
    }

    private void despawnActivePet(Player player) {
        UUID playerId = player.getUniqueId();
        Pet activePet = activePets.remove(playerId);

        if (activePet != null) {
            // Despawn pet entity
            player.sendMessage("§c" + activePet.getName() + " has been desummoned!");
        }
    }

    public void setActivePet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        PlayerPetData data = playerPets.get(playerId);

        if (data == null) return;

        // Despawn current pet
        despawnActivePet(player);

        // Set new active pet
        data.setActivePet(pet);
        activePets.put(playerId, pet);

        // Spawn new pet
        spawnActivePet(player);

        player.sendMessage("§aSet " + pet.getName() + " as your active pet!");
    }

    public void addPet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        PlayerPetData data = playerPets.get(playerId);

        if (data == null) return;

        data.addPet(pet);
        player.sendMessage("§aAdded " + pet.getName() + " to your pet collection!");
    }

    public void removePet(Player player, String petId) {
        UUID playerId = player.getUniqueId();
        PlayerPetData data = playerPets.get(playerId);

        if (data == null) return;

        Pet removedPet = data.removePet(petId);
        if (removedPet != null) {
            player.sendMessage("§cRemoved " + removedPet.getName() + " from your collection!");
        }
    }

    public List<Pet> getPlayerPets(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerPetData data = playerPets.get(playerId);

        return data != null ? data.getPets() : new ArrayList<>();
    }

    public Pet getActivePet(Player player) {
        return activePets.get(player.getUniqueId());
    }

    private PlayerPetData loadPlayerPetData(UUID playerId) {
        // Load from database or create new
        PlayerPetData data = new PlayerPetData(playerId);

        // Try file-based storage as a fallback for persistence
        try {
            File dir = new File(plugin.getDataFolder(), "pets");
            if (!dir.exists()) {
                if (!dir.mkdirs()) plugin.getLogger().warning("Failed to create pets data directory: " + dir.getAbsolutePath());
            }
            File f = new File(dir, playerId.toString() + ".yml");
            if (f.exists()) {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                List<?> list = cfg.getList("pets");
                String activeId = cfg.getString("active", "");
                if (list != null) {
                    for (Object o : list) {
                        if (!(o instanceof Map)) continue;
                        @SuppressWarnings("unchecked")
                        Map<String,Object> map = (Map<String,Object>) o;
                        try {
                            Object nameObj = map.get("name");
                            String name = nameObj != null ? String.valueOf(nameObj) : "Pet";

                            Object typeObj = map.get("type");
                            String typeStr = typeObj != null ? String.valueOf(typeObj) : "WOLF";
                            PetType type = PetType.valueOf(typeStr.toUpperCase());

                            Object rarityObj = map.get("rarity");
                            String rarityStr = rarityObj != null ? String.valueOf(rarityObj) : "COMMON";
                            PetRarity rarity = PetRarity.valueOf(rarityStr.toUpperCase());

                            Object idObj = map.get("id");
                            String id = idObj != null ? String.valueOf(idObj) : UUID.randomUUID().toString();
                            Pet pet = new Pet(id, name, type, rarity);

                            Object lvlObj = map.get("level");
                            int level = 1;
                            if (lvlObj instanceof Number) level = ((Number) lvlObj).intValue();
                            else if (lvlObj != null) {
                                try { level = Integer.parseInt(String.valueOf(lvlObj)); } catch (Exception ignored) {}
                            }
                            pet.setLevel(level);

                            Object xpObj = map.get("xp");
                            double xp = 0.0;
                            if (xpObj instanceof Number) xp = ((Number) xpObj).doubleValue();
                            else if (xpObj != null) {
                                try { xp = Double.parseDouble(String.valueOf(xpObj)); } catch (Exception ignored) {}
                            }
                            pet.setXP(xp);

                            data.addPet(pet);
                            if (activeId != null && activeId.equals(pet.getId())) data.setActivePet(pet);
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (Exception ex) {
            plugin.getLogger().warning("Failed to load pet data for " + playerId + ": " + ex.getMessage());
        }

        return data;
    }

    private void savePlayerPetData(UUID playerId) {
        PlayerPetData data = playerPets.get(playerId);
        if (data == null) return;

        try {
            File dir = new File(plugin.getDataFolder(), "pets");
            if (!dir.exists()) {
                if (!dir.mkdirs()) plugin.getLogger().warning("Failed to create pets data directory: " + dir.getAbsolutePath());
            }
            File f = new File(dir, playerId.toString() + ".yml");
            YamlConfiguration cfg = new YamlConfiguration();
            List<Map<String,Object>> list = new ArrayList<>();
            for (Pet pet : data.getPets()) {
                Map<String,Object> map = new HashMap<>();
                map.put("id", pet.getId());
                map.put("name", pet.getName());
                map.put("type", pet.getType().name());
                map.put("rarity", pet.getRarity().name());
                map.put("level", pet.getLevel());
                map.put("xp", pet.getXP());
                list.add(map);
            }
            cfg.set("pets", list);
            cfg.set("active", data.getActivePet() != null ? data.getActivePet().getId() : "");
            cfg.save(f);
        } catch (Exception ex) {
            plugin.getLogger().warning("Failed to save pet data for " + playerId + ": " + ex.getMessage());
        }
    }

    public enum PetType {
        WOLF("§aWolf", "§7A loyal companion", EntityType.WOLF),
        CAT("§eCat", "§7A graceful feline", EntityType.CAT),
        HORSE("§6Horse", "§7A noble steed", EntityType.HORSE),
        PARROT("§dParrot", "§7A colorful bird", EntityType.PARROT),
        RABBIT("§fRabbit", "§7A fluffy bunny", EntityType.RABBIT),
        BEE("§eBee", "§7A busy pollinator", EntityType.BEE),
        ENDERMAN("§5Enderman", "§7A mysterious being", EntityType.ENDERMAN),
        DRAGON("§cDragon", "§7A powerful beast", EntityType.ENDER_DRAGON);

        private final String displayName;
        private final String description;
        private final EntityType entityType;

        PetType(String displayName, String description, EntityType entityType) {
            this.displayName = displayName;
            this.description = description;
            this.entityType = entityType;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public EntityType getEntityType() { return entityType; }
    }

    public static class Pet {
        private String id;
        private final String name;
        private final PetType type;
        private final PetRarity rarity;
        private int level;
        private double xp;
        private final Map<String, Object> stats;
        private final List<PetAbility> abilities;

        public Pet(String name, PetType type, PetRarity rarity) {
            this(UUID.randomUUID().toString(), name, type, rarity);
        }

        public Pet(String id, String name, PetType type, PetRarity rarity) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rarity = rarity;
            this.level = 1;
            this.xp = 0.0;
            this.stats = new HashMap<>();
            this.abilities = new ArrayList<>();

            // Initialize pet stats
            initializeStats();
        }

        private void initializeStats() {
            stats.put("health", 100.0);
            stats.put("damage", 10.0);
            stats.put("defense", 5.0);
            stats.put("speed", 1.0);
            stats.put("luck", 0.0);
        }

        public void addXP(double xp) {
            this.xp += xp;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setXP(double xp) {
            this.xp = xp;
        }

        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public PetType getType() { return type; }
        public PetRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public double getXP() { return xp; }
        public Map<String, Object> getStats() { return stats; }
        public List<PetAbility> getAbilities() { return abilities; }
    }

    public enum PetRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);

        private final String displayName;
        private final double multiplier;

        PetRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    public static class PetAbility {
        private final String name;
        private final String description;
        private final int requiredLevel;
        private final double cooldown;

        public PetAbility(String name, String description, int requiredLevel, double cooldown) {
            this.name = name;
            this.description = description;
            this.requiredLevel = requiredLevel;
            this.cooldown = cooldown;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getRequiredLevel() { return requiredLevel; }
        public double getCooldown() { return cooldown; }
    }

    public static class PlayerPetData {
        private final UUID playerId;
        private final List<Pet> pets;
        private Pet activePet;

        public PlayerPetData(UUID playerId) {
            this.playerId = playerId;
            this.pets = new ArrayList<>();
            this.activePet = null;
        }

        public void addPet(Pet pet) {
            pets.add(pet);
        }

        public Pet removePet(String petId) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet p = it.next();
                if (p.getId().equals(petId)) {
                    it.remove();
                    return p;
                }
            }
            return null;
        }

        public List<Pet> getPets() { return pets; }
        public Pet getActivePet() { return activePet; }
        public void setActivePet(Pet activePet) { this.activePet = activePet; }
    }
}
