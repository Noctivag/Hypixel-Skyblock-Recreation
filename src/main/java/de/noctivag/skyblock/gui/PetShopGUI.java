package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.PetType;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.PetService;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PetShopGUI extends Menu implements Listener {

    private final PlayerProfileService playerProfileService;
    private final PetService petService;

    public PetShopGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.petService = plugin.getServiceManager().getService(PetService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Pet Shop";
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Title item
        ItemStack titleItem = new ItemStack(Material.EMERALD);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§a§lPet Shop");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Kaufe neue Pets und");
        titleLore.add("§7Pet-Upgrades!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Player coins display
        ItemStack coinsItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta coinsMeta = coinsItem.getItemMeta();
        coinsMeta.setDisplayName("§6§lDeine Coins");
        List<String> coinsLore = new ArrayList<>();
        coinsLore.add("§7Verfügbare Coins: §e" + String.format("%,.0f", profile.getCoins()));
        coinsMeta.setLore(coinsLore);
        coinsItem.setItemMeta(coinsMeta);
        inventory.setItem(0, coinsItem);

        // Pet categories
        int slot = 20;
        
        // Common Pets
        for (PetType petType : PetType.values()) {
            if (petType.getRarity() == Rarity.COMMON && slot < 45) {
                ItemStack petItem = new ItemStack(petType.getIcon());
                ItemMeta petMeta = petItem.getItemMeta();
                petMeta.setDisplayName(petType.getDisplayName());
                List<String> petLore = new ArrayList<>();
                petLore.add("§7Rarity: " + petType.getRarity().getGermanName());
                petLore.add("§7Beschreibung: " + petType.getDescription());
                petLore.add("§7Preis: §e" + String.format("%,.0f", getPetPrice(petType)) + " Coins");
                petLore.add("");
                petLore.add("§eKlicke um zu kaufen!");
                petMeta.setLore(petLore);
                petItem.setItemMeta(petMeta);
                inventory.setItem(slot, petItem);
                slot++;
            }
        }

        // Uncommon Pets
        slot = 24;
        for (PetType petType : PetType.values()) {
            if (petType.getRarity() == Rarity.UNCOMMON && slot < 45) {
                ItemStack petItem = new ItemStack(petType.getIcon());
                ItemMeta petMeta = petItem.getItemMeta();
                petMeta.setDisplayName(petType.getDisplayName());
                List<String> petLore = new ArrayList<>();
                petLore.add("§7Rarity: " + petType.getRarity().getGermanName());
                petLore.add("§7Beschreibung: " + petType.getDescription());
                petLore.add("§7Preis: §e" + String.format("%,.0f", getPetPrice(petType)) + " Coins");
                petLore.add("");
                petLore.add("§eKlicke um zu kaufen!");
                petMeta.setLore(petLore);
                petItem.setItemMeta(petMeta);
                inventory.setItem(slot, petItem);
                slot++;
            }
        }

        // Rare Pets
        slot = 28;
        for (PetType petType : PetType.values()) {
            if (petType.getRarity() == Rarity.RARE && slot < 45) {
                ItemStack petItem = new ItemStack(petType.getIcon());
                ItemMeta petMeta = petItem.getItemMeta();
                petMeta.setDisplayName(petType.getDisplayName());
                List<String> petLore = new ArrayList<>();
                petLore.add("§7Rarity: " + petType.getRarity().getGermanName());
                petLore.add("§7Beschreibung: " + petType.getDescription());
                petLore.add("§7Preis: §e" + String.format("%,.0f", getPetPrice(petType)) + " Coins");
                petLore.add("");
                petLore.add("§eKlicke um zu kaufen!");
                petMeta.setLore(petLore);
                petItem.setItemMeta(petMeta);
                inventory.setItem(slot, petItem);
                slot++;
            }
        }

        // Epic Pets
        slot = 32;
        for (PetType petType : PetType.values()) {
            if (petType.getRarity() == Rarity.EPIC && slot < 45) {
                ItemStack petItem = new ItemStack(petType.getIcon());
                ItemMeta petMeta = petItem.getItemMeta();
                petMeta.setDisplayName(petType.getDisplayName());
                List<String> petLore = new ArrayList<>();
                petLore.add("§7Rarity: " + petType.getRarity().getGermanName());
                petLore.add("§7Beschreibung: " + petType.getDescription());
                petLore.add("§7Preis: §e" + String.format("%,.0f", getPetPrice(petType)) + " Coins");
                petLore.add("");
                petLore.add("§eKlicke um zu kaufen!");
                petMeta.setLore(petLore);
                petItem.setItemMeta(petMeta);
                inventory.setItem(slot, petItem);
                slot++;
            }
        }

        // Legendary Pets
        slot = 36;
        for (PetType petType : PetType.values()) {
            if (petType.getRarity() == Rarity.LEGENDARY && slot < 45) {
                ItemStack petItem = new ItemStack(petType.getIcon());
                ItemMeta petMeta = petItem.getItemMeta();
                petMeta.setDisplayName(petType.getDisplayName());
                List<String> petLore = new ArrayList<>();
                petLore.add("§7Rarity: " + petType.getRarity().getGermanName());
                petLore.add("§7Beschreibung: " + petType.getDescription());
                petLore.add("§7Preis: §e" + String.format("%,.0f", getPetPrice(petType)) + " Coins");
                petLore.add("");
                petLore.add("§eKlicke um zu kaufen!");
                petMeta.setLore(petLore);
                petItem.setItemMeta(petMeta);
                inventory.setItem(slot, petItem);
                slot++;
            }
        }

        // Pet food
        ItemStack petFood = new ItemStack(Material.WHEAT);
        ItemMeta petFoodMeta = petFood.getItemMeta();
        petFoodMeta.setDisplayName("§a§lPet Futter");
        List<String> petFoodLore = new ArrayList<>();
        petFoodLore.add("§7Füttere deine Pets um");
        petFoodLore.add("§7ihre Erfahrung zu erhöhen!");
        petFoodLore.add("§7Preis: §e100 Coins");
        petFoodLore.add("");
        petFoodLore.add("§eKlicke um zu kaufen!");
        petFoodMeta.setLore(petFoodLore);
        petFood.setItemMeta(petFoodMeta);
        inventory.setItem(40, petFood);

        // Pet upgrade
        ItemStack petUpgrade = new ItemStack(Material.NETHER_STAR);
        ItemMeta petUpgradeMeta = petUpgrade.getItemMeta();
        petUpgradeMeta.setDisplayName("§d§lPet Upgrade");
        List<String> petUpgradeLore = new ArrayList<>();
        petUpgradeLore.add("§7Verbessere die Rarity");
        petUpgradeLore.add("§7deines Pets!");
        petUpgradeLore.add("§7Preis: §e10000 Coins");
        petUpgradeLore.add("");
        petUpgradeLore.add("§eKlicke um zu kaufen!");
        petUpgradeMeta.setLore(petUpgradeLore);
        petUpgrade.setItemMeta(petUpgradeMeta);
        inventory.setItem(42, petUpgrade);

        addNavigationButtons();
    }

    private double getPetPrice(PetType petType) {
        // Base price based on rarity
        switch (petType.getRarity()) {
            case COMMON: return 1000;
            case UNCOMMON: return 5000;
            case RARE: return 25000;
            case EPIC: return 100000;
            case LEGENDARY: return 500000;
            default: return 1000;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle pet food purchase
        if (event.getSlot() == 40) {
            buyPetFood(clickedPlayer);
            return;
        }
        
        // Handle pet upgrade purchase
        if (event.getSlot() == 42) {
            buyPetUpgrade(clickedPlayer);
            return;
        }
        
        // Handle pet purchase
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handlePetPurchase(clickedPlayer, event.getSlot());
            return;
        }
    }

    private void buyPetFood(Player player) {
        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Check if player has enough coins
        if (profile.getCoins() < 100) {
            player.sendMessage("§cDu benötigst 100 Coins für Pet Futter!");
            return;
        }

        // Deduct coins
        profile.removeCoins(100);

        // Give pet food
        ItemStack petFood = new ItemStack(Material.WHEAT);
        ItemMeta petFoodMeta = petFood.getItemMeta();
        petFoodMeta.setDisplayName("§a§lPet Futter");
        List<String> petFoodLore = new ArrayList<>();
        petFoodLore.add("§7Füttere deine Pets um");
        petFoodLore.add("§7ihre Erfahrung zu erhöhen!");
        petFoodMeta.setLore(petFoodLore);
        petFood.setItemMeta(petFoodMeta);
        player.getInventory().addItem(petFood);

        player.sendMessage("§aDu hast Pet Futter gekauft!");
    }

    private void buyPetUpgrade(Player player) {
        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Check if player has enough coins
        if (profile.getCoins() < 10000) {
            player.sendMessage("§cDu benötigst 10000 Coins für ein Pet Upgrade!");
            return;
        }

        // Check if player has any pets
        List<de.noctivag.skyblock.models.Pet> playerPets = petService.getPlayerPets(player);
        if (playerPets.isEmpty()) {
            player.sendMessage("§cDu hast keine Pets zum Upgraden!");
            return;
        }

        // Find a pet that can be upgraded
        de.noctivag.skyblock.models.Pet upgradablePet = null;
        for (de.noctivag.skyblock.models.Pet pet : playerPets) {
            if (pet.getRarity() != Rarity.LEGENDARY) {
                upgradablePet = pet;
                break;
            }
        }

        if (upgradablePet == null) {
            player.sendMessage("§cDu hast keine Pets die upgegradet werden können!");
            return;
        }

        // Deduct coins
        profile.removeCoins(10000);

        // Upgrade pet
        petService.upgradePetRarity(player, upgradablePet).thenAccept(success -> {
            if (success) {
                player.sendMessage("§aDein Pet wurde erfolgreich upgegradet!");
                // Refresh GUI
                setMenuItems();
            } else {
                player.sendMessage("§cFehler beim Upgraden des Pets!");
            }
        });
    }

    private void handlePetPurchase(Player player, int slot) {
        // Find the pet type based on slot
        PetType petType = null;
        int currentSlot = 20;
        
        for (PetType type : PetType.values()) {
            if (currentSlot == slot) {
                petType = type;
                break;
            }
            currentSlot++;
        }

        if (petType == null) {
            return;
        }

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Check if player has enough coins
        double price = getPetPrice(petType);
        if (profile.getCoins() < price) {
            player.sendMessage("§cDu benötigst " + String.format("%,.0f", price) + " Coins für dieses Pet!");
            return;
        }

        // Check if player already has this pet
        List<de.noctivag.skyblock.models.Pet> playerPets = petService.getPlayerPets(player);
        for (de.noctivag.skyblock.models.Pet pet : playerPets) {
            if (pet.getPetType() == petType) {
                player.sendMessage("§cDu hast bereits dieses Pet!");
                return;
            }
        }

        // Deduct coins
        profile.removeCoins(price);

        // Create and add pet
        petService.createPet(petType, Rarity.COMMON).thenAccept(pet -> {
            petService.addPetToPlayer(player, pet).thenAccept(success -> {
                if (success) {
                    player.sendMessage("§aDu hast " + petType.getDisplayName() + " gekauft!");
                    // Refresh GUI
                    setMenuItems();
                } else {
                    player.sendMessage("§cFehler beim Erstellen des Pets!");
                }
            });
        });
    }
}