package de.noctivag.skyblock.pets;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Pet System - Hypixel Skyblock Style
 */
public class AdvancedPetSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerPets> playerPets = new ConcurrentHashMap<>();
    private final Map<PetType, PetConfig> petConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> petTasks = new ConcurrentHashMap<>();

    public AdvancedPetSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializePetConfigs();
        startPetUpdateTask();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializePetConfigs() {
        petConfigs.put(PetType.WOLF, new PetConfig(
            "Wolf", "§fWolf", Material.WOLF_SPAWN_EGG,
            "§7A loyal wolf companion.",
            PetCategory.COMBAT, 100, Arrays.asList("§7- Increases combat XP"),
            Arrays.asList("§7- 1x Wolf Spawn Egg")
        ));

        petConfigs.put(PetType.CAT, new PetConfig(
            "Cat", "§eCat", Material.CAT_SPAWN_EGG,
            "§7A playful cat companion.",
            PetCategory.FARMING, 100, Arrays.asList("§7- Increases farming XP"),
            Arrays.asList("§7- 1x Cat Spawn Egg")
        ));
    }

    private void startPetUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    for (Map.Entry<UUID, PlayerPets> entry : playerPets.entrySet()) {
                        PlayerPets pets = entry.getValue();
                        pets.update();
                    }
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            openPetGUI(player);
        }
    }

    private void openPetGUI(Player player) {
        player.sendMessage("§aPet GUI geöffnet!");
    }

    public void createPet(Player player, PetType type) {
        UUID petId = UUID.randomUUID();
        PlayerPets playerPets = getPlayerPets(player.getUniqueId());
        Pet pet = new Pet(petId, player.getUniqueId(), type);
        playerPets.addPet(pet);

        player.sendMessage("§aPet erstellt!");
    }

    public PlayerPets getPlayerPets(UUID playerId) {
        return playerPets.computeIfAbsent(playerId, k -> new PlayerPets(playerId));
    }

    public PetConfig getPetConfig(PetType type) {
        return petConfigs.get(type);
    }

    public List<PetType> getAllPetTypes() {
        return new ArrayList<>(petConfigs.keySet());
    }

    public enum PetType {
        WOLF, CAT, PARROT, DOLPHIN, IRON_GOLEM, ENDERMAN, BLAZE, SKELETON, ZOMBIE, CREEPER
    }

    public enum PetCategory {
        COMBAT("§cCombat", 1.0),
        FARMING("§aFarming", 1.2),
        FORAGING("§2Foraging", 1.1),
        FISHING("§bFishing", 1.3),
        MINING("§6Mining", 1.4);

        private final String displayName;
        private final double multiplier;

        PetCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    public static class PetConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final PetCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;

        public PetConfig(String name, String displayName, Material icon, String description,
                        PetCategory category, int maxLevel, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public PetCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }

    public static class Pet {
        private final UUID id;
        private final UUID owner;
        private final PetType type;
        private int level = 1;
        private long experience = 0;
        private long lastUpdate;

        public Pet(UUID id, UUID owner, PetType type) {
            this.id = id;
            this.owner = owner;
            this.type = type;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }

        private void saveToDatabase() {
            // Save pet data to database
        }

        public void addExperience(long exp) {
            experience += exp;
            checkLevelUp();
        }

        private void checkLevelUp() {
            long requiredExp = level * 1000;
            if (experience >= requiredExp) {
                level++;
                experience -= requiredExp;
            }
        }

        public UUID getId() { return id; }
        public UUID getOwner() { return owner; }
        public PetType getType() { return type; }
        public int getLevel() { return level; }
        public long getExperience() { return experience; }
    }

    public static class PlayerPets {
        private final UUID playerId;
        private final Map<UUID, Pet> pets = new ConcurrentHashMap<>();
        private UUID activePet;
        private int totalPets = 0;
        private long lastUpdate;

        public PlayerPets(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }

            for (Pet pet : pets.values()) {
                pet.update();
            }
        }

        private void saveToDatabase() {
            // Save player pets data to database
        }

        public void addPet(Pet pet) {
            pets.put(pet.getId(), pet);
            totalPets++;
        }

        public void removePet(UUID petId) {
            pets.remove(petId);
            totalPets--;

            if (activePet != null && activePet.equals(petId)) {
                activePet = null;
            }
        }

        public void setActivePet(UUID petId) {
            activePet = petId;
        }

        public Pet getPet(UUID petId) {
            return pets.get(petId);
        }

        public Pet getActivePet() {
            return activePet != null ? pets.get(activePet) : null;
        }

        public List<Pet> getAllPets() {
            return new ArrayList<>(pets.values());
        }

        public UUID getPlayerId() { return playerId; }
        public Map<UUID, Pet> getPets() { return pets; }
        public UUID getActivePetId() { return activePet; }
        public int getTotalPets() { return totalPets; }
    }
}
