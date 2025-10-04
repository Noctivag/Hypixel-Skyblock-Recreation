package de.noctivag.plugin.cosmetics;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCosmeticData> playerCosmeticData = new ConcurrentHashMap<>();
    private final Map<CosmeticType, List<CosmeticItem>> cosmeticItems = new HashMap<>();
    
    public CosmeticSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeCosmeticItems();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeCosmeticItems() {
        // Particle Effects
        List<CosmeticItem> particleEffects = new ArrayList<>();
        particleEffects.add(new CosmeticItem(
            "Fire Trail", "§cFire Trail", Material.FIRE_CHARGE,
            "§7Leave a trail of fire particles behind you.",
            CosmeticRarity.COMMON, 100, Arrays.asList("§7- Fire particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Fire Charge", "§7- 100 coins")
        ));
        particleEffects.add(new CosmeticItem(
            "Water Trail", "§bWater Trail", Material.WATER_BUCKET,
            "§7Leave a trail of water particles behind you.",
            CosmeticRarity.COMMON, 100, Arrays.asList("§7- Water particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Water Bucket", "§7- 100 coins")
        ));
        particleEffects.add(new CosmeticItem(
            "Lightning Trail", "§eLightning Trail", Material.LIGHTNING_ROD,
            "§7Leave a trail of lightning particles behind you.",
            CosmeticRarity.UNCOMMON, 250, Arrays.asList("§7- Lightning particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Lightning Rod", "§7- 250 coins")
        ));
        particleEffects.add(new CosmeticItem(
            "Heart Trail", "§dHeart Trail", Material.POPPY,
            "§7Leave a trail of heart particles behind you.",
            CosmeticRarity.UNCOMMON, 200, Arrays.asList("§7- Heart particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Poppy", "§7- 200 coins")
        ));
        particleEffects.add(new CosmeticItem(
            "Star Trail", "§6Star Trail", Material.NETHER_STAR,
            "§7Leave a trail of star particles behind you.",
            CosmeticRarity.RARE, 500, Arrays.asList("§7- Star particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Nether Star", "§7- 500 coins")
        ));
        particleEffects.add(new CosmeticItem(
            "Dragon Trail", "§5Dragon Trail", Material.DRAGON_EGG,
            "§7Leave a trail of dragon particles behind you.",
            CosmeticRarity.LEGENDARY, 1000, Arrays.asList("§7- Dragon particles", "§7- Trail effect"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1000 coins")
        ));
        cosmeticItems.put(CosmeticType.PARTICLE_EFFECTS, particleEffects);
        
        // Hats
        List<CosmeticItem> hats = new ArrayList<>();
        hats.add(new CosmeticItem(
            "Crown", "§6Crown", Material.GOLDEN_HELMET,
            "§7A golden crown for royalty.",
            CosmeticRarity.RARE, 500, Arrays.asList("§7- Golden crown", "§7- Royal appearance"),
            Arrays.asList("§7- 1x Golden Helmet", "§7- 500 coins")
        ));
        hats.add(new CosmeticItem(
            "Wizard Hat", "§5Wizard Hat", Material.LEATHER_HELMET,
            "§7A magical wizard hat.",
            CosmeticRarity.UNCOMMON, 300, Arrays.asList("§7- Magical hat", "§7- Wizard appearance"),
            Arrays.asList("§7- 1x Leather Helmet", "§7- 300 coins")
        ));
        hats.add(new CosmeticItem(
            "Pirate Hat", "§8Pirate Hat", Material.LEATHER_HELMET,
            "§7A hat for pirates.",
            CosmeticRarity.UNCOMMON, 250, Arrays.asList("§7- Pirate hat", "§7- Pirate appearance"),
            Arrays.asList("§7- 1x Leather Helmet", "§7- 250 coins")
        ));
        hats.add(new CosmeticItem(
            "Cowboy Hat", "§eCowboy Hat", Material.LEATHER_HELMET,
            "§7A hat for cowboys.",
            CosmeticRarity.UNCOMMON, 200, Arrays.asList("§7- Cowboy hat", "§7- Cowboy appearance"),
            Arrays.asList("§7- 1x Leather Helmet", "§7- 200 coins")
        ));
        hats.add(new CosmeticItem(
            "Top Hat", "§7Top Hat", Material.LEATHER_HELMET,
            "§7A fancy top hat.",
            CosmeticRarity.RARE, 400, Arrays.asList("§7- Fancy hat", "§7- Elegant appearance"),
            Arrays.asList("§7- 1x Leather Helmet", "§7- 400 coins")
        ));
        hats.add(new CosmeticItem(
            "Devil Horns", "§4Devil Horns", Material.LEATHER_HELMET,
            "§7Horns for devils.",
            CosmeticRarity.RARE, 600, Arrays.asList("§7- Devil horns", "§7- Devil appearance"),
            Arrays.asList("§7- 1x Leather Helmet", "§7- 600 coins")
        ));
        cosmeticItems.put(CosmeticType.HATS, hats);
        
        // Wings
        List<CosmeticItem> wings = new ArrayList<>();
        wings.add(new CosmeticItem(
            "Angel Wings", "§fAngel Wings", Material.ELYTRA,
            "§7Wings of an angel.",
            CosmeticRarity.LEGENDARY, 1000, Arrays.asList("§7- Angel wings", "§7- Flight ability"),
            Arrays.asList("§7- 1x Elytra", "§7- 1000 coins")
        ));
        wings.add(new CosmeticItem(
            "Demon Wings", "§4Demon Wings", Material.ELYTRA,
            "§7Wings of a demon.",
            CosmeticRarity.LEGENDARY, 1000, Arrays.asList("§7- Demon wings", "§7- Flight ability"),
            Arrays.asList("§7- 1x Elytra", "§7- 1000 coins")
        ));
        wings.add(new CosmeticItem(
            "Dragon Wings", "§6Dragon Wings", Material.ELYTRA,
            "§7Wings of a dragon.",
            CosmeticRarity.MYTHIC, 2000, Arrays.asList("§7- Dragon wings", "§7- Flight ability"),
            Arrays.asList("§7- 1x Elytra", "§7- 2000 coins")
        ));
        wings.add(new CosmeticItem(
            "Phoenix Wings", "§cPhoenix Wings", Material.ELYTRA,
            "§7Wings of a phoenix.",
            CosmeticRarity.MYTHIC, 2000, Arrays.asList("§7- Phoenix wings", "§7- Flight ability"),
            Arrays.asList("§7- 1x Elytra", "§7- 2000 coins")
        ));
        wings.add(new CosmeticItem(
            "Butterfly Wings", "§dButterfly Wings", Material.ELYTRA,
            "§7Wings of a butterfly.",
            CosmeticRarity.RARE, 800, Arrays.asList("§7- Butterfly wings", "§7- Flight ability"),
            Arrays.asList("§7- 1x Elytra", "§7- 800 coins")
        ));
        cosmeticItems.put(CosmeticType.WINGS, wings);
        
        // Auras
        List<CosmeticItem> auras = new ArrayList<>();
        auras.add(new CosmeticItem(
            "Fire Aura", "§cFire Aura", Material.FIRE_CHARGE,
            "§7Surround yourself with fire.",
            CosmeticRarity.UNCOMMON, 300, Arrays.asList("§7- Fire aura", "§7- Fire resistance"),
            Arrays.asList("§7- 1x Fire Charge", "§7- 300 coins")
        ));
        auras.add(new CosmeticItem(
            "Ice Aura", "§bIce Aura", Material.ICE,
            "§7Surround yourself with ice.",
            CosmeticRarity.UNCOMMON, 300, Arrays.asList("§7- Ice aura", "§7- Cold resistance"),
            Arrays.asList("§7- 1x Ice", "§7- 300 coins")
        ));
        auras.add(new CosmeticItem(
            "Lightning Aura", "§eLightning Aura", Material.LIGHTNING_ROD,
            "§7Surround yourself with lightning.",
            CosmeticRarity.RARE, 500, Arrays.asList("§7- Lightning aura", "§7- Electric effect"),
            Arrays.asList("§7- 1x Lightning Rod", "§7- 500 coins")
        ));
        auras.add(new CosmeticItem(
            "Shadow Aura", "§5Shadow Aura", Material.BLACK_DYE,
            "§7Surround yourself with shadows.",
            CosmeticRarity.RARE, 600, Arrays.asList("§7- Shadow aura", "§7- Stealth effect"),
            Arrays.asList("§7- 1x Black Dye", "§7- 600 coins")
        ));
        auras.add(new CosmeticItem(
            "Light Aura", "§fLight Aura", Material.GLOWSTONE,
            "§7Surround yourself with light.",
            CosmeticRarity.RARE, 500, Arrays.asList("§7- Light aura", "§7- Bright effect"),
            Arrays.asList("§7- 1x Glowstone", "§7- 500 coins")
        ));
        auras.add(new CosmeticItem(
            "Rainbow Aura", "§dRainbow Aura", Material.RED_DYE,
            "§7Surround yourself with rainbow colors.",
            CosmeticRarity.LEGENDARY, 1000, Arrays.asList("§7- Rainbow aura", "§7- Colorful effect"),
            Arrays.asList("§7- 1x Red Dye", "§7- 1000 coins")
        ));
        cosmeticItems.put(CosmeticType.AURAS, auras);
        
        // Pets
        List<CosmeticItem> pets = new ArrayList<>();
        pets.add(new CosmeticItem(
            "Pet Dragon", "§6Pet Dragon", Material.DRAGON_EGG,
            "§7A pet dragon that follows you.",
            CosmeticRarity.LEGENDARY, 1500, Arrays.asList("§7- Pet dragon", "§7- Follows player"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1500 coins")
        ));
        pets.add(new CosmeticItem(
            "Pet Phoenix", "§cPet Phoenix", Material.FIRE_CHARGE,
            "§7A pet phoenix that follows you.",
            CosmeticRarity.LEGENDARY, 1200, Arrays.asList("§7- Pet phoenix", "§7- Follows player"),
            Arrays.asList("§7- 1x Fire Charge", "§7- 1200 coins")
        ));
        pets.add(new CosmeticItem(
            "Pet Wolf", "§7Pet Wolf", Material.BONE,
            "§7A pet wolf that follows you.",
            CosmeticRarity.UNCOMMON, 400, Arrays.asList("§7- Pet wolf", "§7- Follows player"),
            Arrays.asList("§7- 1x Bone", "§7- 400 coins")
        ));
        pets.add(new CosmeticItem(
            "Pet Cat", "§ePet Cat", Material.STRING,
            "§7A pet cat that follows you.",
            CosmeticRarity.UNCOMMON, 300, Arrays.asList("§7- Pet cat", "§7- Follows player"),
            Arrays.asList("§7- 1x String", "§7- 300 coins")
        ));
        pets.add(new CosmeticItem(
            "Pet Parrot", "§aPet Parrot", Material.FEATHER,
            "§7A pet parrot that follows you.",
            CosmeticRarity.RARE, 600, Arrays.asList("§7- Pet parrot", "§7- Follows player"),
            Arrays.asList("§7- 1x Feather", "§7- 600 coins")
        ));
        cosmeticItems.put(CosmeticType.PETS, pets);
        
        // Titles
        List<CosmeticItem> titles = new ArrayList<>();
        titles.add(new CosmeticItem(
            "King", "§6King", Material.GOLDEN_HELMET,
            "§7The title of a king.",
            CosmeticRarity.LEGENDARY, 2000, Arrays.asList("§7- King title", "§7- Royal appearance"),
            Arrays.asList("§7- 1x Golden Helmet", "§7- 2000 coins")
        ));
        titles.add(new CosmeticItem(
            "Wizard", "§5Wizard", Material.ENCHANTED_BOOK,
            "§7The title of a wizard.",
            CosmeticRarity.RARE, 800, Arrays.asList("§7- Wizard title", "§7- Magical appearance"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 800 coins")
        ));
        titles.add(new CosmeticItem(
            "Warrior", "§cWarrior", Material.DIAMOND_SWORD,
            "§7The title of a warrior.",
            CosmeticRarity.RARE, 700, Arrays.asList("§7- Warrior title", "§7- Battle appearance"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 700 coins")
        ));
        titles.add(new CosmeticItem(
            "Mage", "§bMage", Material.BLAZE_ROD,
            "§7The title of a mage.",
            CosmeticRarity.RARE, 750, Arrays.asList("§7- Mage title", "§7- Magical appearance"),
            Arrays.asList("§7- 1x Blaze Rod", "§7- 750 coins")
        ));
        titles.add(new CosmeticItem(
            "Assassin", "§8Assassin", Material.IRON_SWORD,
            "§7The title of an assassin.",
            CosmeticRarity.RARE, 900, Arrays.asList("§7- Assassin title", "§7- Stealth appearance"),
            Arrays.asList("§7- 1x Iron Sword", "§7- 900 coins")
        ));
        titles.add(new CosmeticItem(
            "God", "§dGod", Material.NETHER_STAR,
            "§7The title of a god.",
            CosmeticRarity.MYTHIC, 5000, Arrays.asList("§7- God title", "§7- Divine appearance"),
            Arrays.asList("§7- 1x Nether Star", "§7- 5000 coins")
        ));
        cosmeticItems.put(CosmeticType.TITLES, titles);
        
        // Emotes
        List<CosmeticItem> emotes = new ArrayList<>();
        emotes.add(new CosmeticItem(
            "Dance", "§dDance", Material.MUSIC_DISC_CAT,
            "§7Dance emote.",
            CosmeticRarity.COMMON, 100, Arrays.asList("§7- Dance emote", "§7- Fun animation"),
            Arrays.asList("§7- 1x Music Disc", "§7- 100 coins")
        ));
        emotes.add(new CosmeticItem(
            "Wave", "§eWave", Material.PAPER,
            "§7Wave emote.",
            CosmeticRarity.COMMON, 50, Arrays.asList("§7- Wave emote", "§7- Greeting animation"),
            Arrays.asList("§7- 1x Paper", "§7- 50 coins")
        ));
        emotes.add(new CosmeticItem(
            "Bow", "§6Bow", Material.BOW,
            "§7Bow emote.",
            CosmeticRarity.COMMON, 75, Arrays.asList("§7- Bow emote", "§7- Respect animation"),
            Arrays.asList("§7- 1x Bow", "§7- 75 coins")
        ));
        emotes.add(new CosmeticItem(
            "Clap", "§aClap", Material.STICK,
            "§7Clap emote.",
            CosmeticRarity.COMMON, 60, Arrays.asList("§7- Clap emote", "§7- Appreciation animation"),
            Arrays.asList("§7- 1x Stick", "§7- 60 coins")
        ));
        emotes.add(new CosmeticItem(
            "Laugh", "§cLaugh", Material.POPPY,
            "§7Laugh emote.",
            CosmeticRarity.COMMON, 80, Arrays.asList("§7- Laugh emote", "§7- Joy animation"),
            Arrays.asList("§7- 1x Poppy", "§7- 80 coins")
        ));
        emotes.add(new CosmeticItem(
            "Cry", "§bCry", Material.BLUE_DYE,
            "§7Cry emote.",
            CosmeticRarity.COMMON, 70, Arrays.asList("§7- Cry emote", "§7- Sad animation"),
            Arrays.asList("§7- 1x Blue Dye", "§7- 70 coins")
        ));
        cosmeticItems.put(CosmeticType.EMOTES, emotes);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Cosmetic") || displayName.contains("Cosmetics")) {
            openCosmeticGUI(player);
        }
    }
    
    public void openCosmeticGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lCosmetics");
        
        // Add cosmetic categories
        addGUIItem(gui, 10, Material.FIRE_CHARGE, "§c§lParticle Effects", "§7Particle effects and trails.");
        addGUIItem(gui, 11, Material.LEATHER_HELMET, "§6§lHats", "§7Various hats and headwear.");
        addGUIItem(gui, 12, Material.ELYTRA, "§f§lWings", "§7Wings for flight and appearance.");
        addGUIItem(gui, 13, Material.GLOWSTONE, "§e§lAuras", "§7Auras that surround you.");
        addGUIItem(gui, 14, Material.BONE, "§a§lPets", "§7Pets that follow you.");
        addGUIItem(gui, 15, Material.NAME_TAG, "§d§lTitles", "§7Titles and ranks.");
        addGUIItem(gui, 16, Material.MUSIC_DISC_CAT, "§b§lEmotes", "§7Emotes and animations.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the cosmetics.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aCosmetics GUI geöffnet!");
    }
    
    public void openCategoryGUI(Player player, CosmeticType category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lCosmetics - " + category.getDisplayName());
        
        List<CosmeticItem> items = cosmeticItems.get(category);
        if (items != null) {
            int slot = 10;
            for (CosmeticItem item : items) {
                if (slot >= 44) break; // Don't overflow into navigation area
                
                addGUIItem(gui, slot, item.getMaterial(), item.getDisplayName(), item.getDescription());
                slot++;
            }
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to main cosmetics.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the cosmetics.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openItemGUI(Player player, CosmeticItem item) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lCosmetics - " + item.getDisplayName());
        
        // Add item information
        addGUIItem(gui, 10, item.getMaterial(), item.getDisplayName(), item.getDescription());
        
        // Add features
        int slot = 19;
        for (String feature : item.getFeatures()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.PAPER, "§7" + feature, "§7Feature description.");
            slot++;
        }
        
        // Add requirements
        slot = 28;
        for (String requirement : item.getRequirements()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.EMERALD, "§e" + requirement, "§7Requirement.");
            slot++;
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to category.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the cosmetics.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
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
    
    public PlayerCosmeticData getPlayerCosmeticData(UUID playerId) {
        return playerCosmeticData.computeIfAbsent(playerId, k -> new PlayerCosmeticData(playerId));
    }
    
    public List<CosmeticItem> getCosmeticItems(CosmeticType category) {
        return cosmeticItems.getOrDefault(category, new ArrayList<>());
    }
    
    public enum CosmeticType {
        PARTICLE_EFFECTS("§cParticle Effects", "§7Particle effects and trails"),
        HATS("§6Hats", "§7Various hats and headwear"),
        WINGS("§fWings", "§7Wings for flight and appearance"),
        AURAS("§eAuras", "§7Auras that surround you"),
        PETS("§aPets", "§7Pets that follow you"),
        TITLES("§dTitles", "§7Titles and ranks"),
        EMOTES("§bEmotes", "§7Emotes and animations");
        
        private final String displayName;
        private final String description;
        
        CosmeticType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum CosmeticRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        CosmeticRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CosmeticItem {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final CosmeticRarity rarity;
        private final int price;
        private final List<String> features;
        private final List<String> requirements;
        
        public CosmeticItem(String name, String displayName, Material material, String description,
                          CosmeticRarity rarity, int price, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.price = price;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public CosmeticRarity getRarity() { return rarity; }
        public int getPrice() { return price; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerCosmeticData {
        private final UUID playerId;
        private final Map<CosmeticType, String> activeCosmetics = new HashMap<>();
        private final Map<String, Boolean> unlockedCosmetics = new HashMap<>();
        private long lastUpdate;
        
        public PlayerCosmeticData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void setActiveCosmetic(CosmeticType type, String cosmeticName) {
            activeCosmetics.put(type, cosmeticName);
        }
        
        public void unlockCosmetic(String cosmeticName) {
            unlockedCosmetics.put(cosmeticName, true);
        }
        
        public String getActiveCosmetic(CosmeticType type) {
            return activeCosmetics.get(type);
        }
        
        public boolean isCosmeticUnlocked(String cosmeticName) {
            return unlockedCosmetics.getOrDefault(cosmeticName, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
