package de.noctivag.skyblock.cosmetics;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CosmeticsPurchaseManager {
    private final SkyblockPlugin plugin;
    private final File purchasesFile;
    private FileConfiguration purchasesConfig;
    private final Map<UUID, Set<String>> playerPurchases = new HashMap<>();
    
    public CosmeticsPurchaseManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.purchasesFile = new File(plugin.getDataFolder(), "cosmetics_purchases.yml");
        loadPurchases();
    }
    
    private void loadPurchases() {
        if (!purchasesFile.exists()) {
            try {
                purchasesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create cosmetics purchases file: " + e.getMessage());
            }
        }
        
        purchasesConfig = YamlConfiguration.loadConfiguration(purchasesFile);
        
        // Load player purchases
        for (String playerId : purchasesConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(playerId);
                List<String> purchases = purchasesConfig.getStringList(playerId);
                playerPurchases.put(uuid, new HashSet<>(purchases));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in purchases file: " + playerId);
            }
        }
    }
    
    public void savePurchases() {
        try {
            // Clear config
            for (String key : purchasesConfig.getKeys(false)) {
                purchasesConfig.set(key, null);
            }
            
            // Save player purchases
            for (Map.Entry<UUID, Set<String>> entry : playerPurchases.entrySet()) {
                purchasesConfig.set(entry.getKey().toString(), new ArrayList<>(entry.getValue()));
            }
            
            purchasesConfig.save(purchasesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save cosmetics purchases: " + e.getMessage());
        }
    }
    
    public boolean hasPurchased(Player player, String cosmeticId) {
        return playerPurchases.getOrDefault(player.getUniqueId(), new HashSet<>()).contains(cosmeticId);
    }
    
    public void purchaseCosmetic(Player player, String cosmeticId, double cost) {
        if (hasPurchased(player, cosmeticId)) {
            player.sendMessage("§cDu besitzt dieses Cosmetic bereits!");
            return;
        }
        
        if (!plugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage("§cDu kannst dir dieses Cosmetic nicht leisten: " + 
                plugin.getEconomyManager().formatMoney(cost));
            return;
        }
        
        // Deduct cost
        plugin.getEconomyManager().withdrawMoney(player, cost);
        
        // Add to purchases
        playerPurchases.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(cosmeticId);
        
        // Save
        savePurchases();
        
        player.sendMessage("§aCosmetic gekauft: §e" + cosmeticId);
        player.sendMessage("§7Kosten: §c" + plugin.getEconomyManager().formatMoney(cost));
    }
    
    public void giveCosmetic(Player player, String cosmeticId) {
        playerPurchases.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(cosmeticId);
        savePurchases();
        player.sendMessage("§aCosmetic erhalten: §e" + cosmeticId);
    }
    
    public Set<String> getPlayerPurchases(Player player) {
        return new HashSet<>(playerPurchases.getOrDefault(player.getUniqueId(), new HashSet<>()));
    }
    
    public void removeCosmetic(Player player, String cosmeticId) {
        Set<String> purchases = playerPurchases.get(player.getUniqueId());
        if (purchases != null) {
            purchases.remove(cosmeticId);
            savePurchases();
            player.sendMessage("§cCosmetic entfernt: §e" + cosmeticId);
        }
    }
    
    public void clearPlayerPurchases(Player player) {
        playerPurchases.remove(player.getUniqueId());
        savePurchases();
        player.sendMessage("§cAlle Cosmetics entfernt!");
    }
    
    // Cosmetic definitions with costs
    public static class CosmeticDefinition {
        private final String id;
        private final String name;
        private final String description;
        private final double cost;
        private final String category;
        
        public CosmeticDefinition(String id, String name, String description, double cost, String category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.cost = cost;
            this.category = category;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getCost() { return cost; }
        public String getCategory() { return category; }
    }
    
    public static final Map<String, CosmeticDefinition> COSMETIC_DEFINITIONS = new HashMap<>();
    
    static {
        // Particle Shapes
        COSMETIC_DEFINITIONS.put("particle_circle", new CosmeticDefinition(
            "particle_circle", "§a§lKreis-Partikel", "§7Elegante Kreis-Form Partikel", 100.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_spiral", new CosmeticDefinition(
            "particle_spiral", "§d§lSpiral-Partikel", "§7Hypnotisierende Spiral-Partikel", 150.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_helix", new CosmeticDefinition(
            "particle_helix", "§b§lHelix-Partikel", "§7Dreidimensionale Helix-Partikel", 200.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_heart", new CosmeticDefinition(
            "particle_heart", "§c§lHerz-Partikel", "§7Romantische Herz-Partikel", 120.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_star", new CosmeticDefinition(
            "particle_star", "§6§lStern-Partikel", "§7Funkelnde Stern-Partikel", 180.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_crown", new CosmeticDefinition(
            "particle_crown", "§e§lKronen-Partikel", "§7Majestätische Kronen-Partikel", 250.0, "particles"));
        COSMETIC_DEFINITIONS.put("particle_wings", new CosmeticDefinition(
            "particle_wings", "§f§lFlügel-Partikel", "§7Engelhafte Flügel-Partikel", 300.0, "particles"));
        
        // Sound Effects
        COSMETIC_DEFINITIONS.put("sound_ambient", new CosmeticDefinition(
            "sound_ambient", "§7§lAmbient-Sounds", "§7Beruhigende Umgebungsgeräusche", 80.0, "sounds"));
        COSMETIC_DEFINITIONS.put("sound_magical", new CosmeticDefinition(
            "sound_magical", "§d§lMagische-Sounds", "§7Mystische Zaubergeräusche", 120.0, "sounds"));
        COSMETIC_DEFINITIONS.put("sound_epic", new CosmeticDefinition(
            "sound_epic", "§6§lEpische-Sounds", "§7Dramatische Kampfgeräusche", 150.0, "sounds"));
        
        // Wings
        COSMETIC_DEFINITIONS.put("wings_angel", new CosmeticDefinition(
            "wings_angel", "§f§lEngels-Flügel", "§7Reine weiße Engelsflügel", 500.0, "wings"));
        COSMETIC_DEFINITIONS.put("wings_demon", new CosmeticDefinition(
            "wings_demon", "§4§lDämonen-Flügel", "§7Dunkle Dämonenflügel", 500.0, "wings"));
        COSMETIC_DEFINITIONS.put("wings_dragon", new CosmeticDefinition(
            "wings_dragon", "§c§lDrachen-Flügel", "§7Mächtige Drachenflügel", 750.0, "wings"));
        
        // Halos
        COSMETIC_DEFINITIONS.put("halo_golden", new CosmeticDefinition(
            "halo_golden", "§6§lGoldener Heiligenschein", "§7Leuchtender goldener Heiligenschein", 400.0, "halos"));
        COSMETIC_DEFINITIONS.put("halo_rainbow", new CosmeticDefinition(
            "halo_rainbow", "§d§lRegenbogen-Heiligenschein", "§7Farbenfroher Regenbogen-Heiligenschein", 600.0, "halos"));
        
        // Trails
        COSMETIC_DEFINITIONS.put("trail_fire", new CosmeticDefinition(
            "trail_fire", "§c§lFeuer-Spur", "§7Brennende Feuerspur", 200.0, "trails"));
        COSMETIC_DEFINITIONS.put("trail_ice", new CosmeticDefinition(
            "trail_ice", "§b§lEis-Spur", "§7Frostige Eisspur", 200.0, "trails"));
        COSMETIC_DEFINITIONS.put("trail_lightning", new CosmeticDefinition(
            "trail_lightning", "§e§lBlitz-Spur", "§7Elektrisierende Blitzspur", 300.0, "trails"));
    }
    
    public CosmeticDefinition getCosmeticDefinition(String cosmeticId) {
        return COSMETIC_DEFINITIONS.get(cosmeticId);
    }
    
    public List<CosmeticDefinition> getCosmeticsByCategory(String category) {
        return COSMETIC_DEFINITIONS.values().stream()
            .filter(def -> def.getCategory().equals(category))
            .toList();
    }
    
    public List<CosmeticDefinition> getAllCosmetics() {
        return new ArrayList<>(COSMETIC_DEFINITIONS.values());
    }
}
