package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.PetType;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.Pet;
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

public class PetGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final PetService petService;

    public PetGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.petService = plugin.getServiceManager().getService(PetService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Pets";
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
        ItemStack titleItem = new ItemStack(Material.BONE);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lPets");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Verwalte deine Haustiere");
        titleLore.add("§7und ihre Fähigkeiten!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Active Pet display
        Pet activePet = petService.getPlayerActivePet(player);
        if (activePet != null) {
            ItemStack activePetItem = new ItemStack(activePet.getPetType().getIcon());
            ItemMeta activePetMeta = activePetItem.getItemMeta();
            activePetMeta.setDisplayName("§a§lAktives Pet: " + activePet.getPetType().getDisplayName());
            List<String> activePetLore = new ArrayList<>();
            activePetLore.add("§7Rarity: " + activePet.getRarity().getGermanName());
            activePetLore.add("§7Level: §e" + activePet.getLevel() + "§7/§e" + activePet.getPetType().getMaxLevel());
            activePetLore.add("§7XP: §b" + activePet.getExperience() + "§7/§b" + activePet.getRequiredExperienceForNextLevel());
            activePetLore.add("§7Beschreibung: " + activePet.getPetType().getDescription());
            activePetLore.add("");
            activePetLore.add("§7Stat-Boni:");
            activePetLore.add("§a• Schaden: +" + String.format("%.1f", activePet.getStatBoost("damage")));
            activePetLore.add("§a• Gesundheit: +" + String.format("%.1f", activePet.getStatBoost("health")));
            activePetLore.add("§a• Geschwindigkeit: +" + String.format("%.1f", activePet.getStatBoost("speed")));
            activePetLore.add("");
            activePetLore.add("§cKlicke um Pet zu deaktivieren!");
            activePetMeta.setLore(activePetLore);
            activePetItem.setItemMeta(activePetMeta);
            inventory.setItem(13, activePetItem);
        } else {
            ItemStack noActivePet = new ItemStack(Material.BARRIER);
            ItemMeta noActiveMeta = noActivePet.getItemMeta();
            noActiveMeta.setDisplayName("§7Kein aktives Pet");
            List<String> noActiveLore = new ArrayList<>();
            noActiveLore.add("§7Du hast derzeit kein");
            noActiveLore.add("§7aktives Pet ausgewählt.");
            noActiveMeta.setLore(noActiveLore);
            noActivePet.setItemMeta(noActiveMeta);
            inventory.setItem(13, noActivePet);
        }

        // Pet collection display
        List<Pet> playerPets = petService.getPlayerPets(player);
        int slot = 20;
        
        for (Pet pet : playerPets) {
            if (slot >= 45) break; // Don't go beyond the border
            
            ItemStack petItem = new ItemStack(pet.getPetType().getIcon());
            ItemMeta petMeta = petItem.getItemMeta();
            
            // Highlight active pet
            if (pet.isActive()) {
                petMeta.setDisplayName("§a§l✓ " + pet.getPetType().getDisplayName() + " §a§l(AKTIV)");
            } else {
                petMeta.setDisplayName(pet.getPetType().getDisplayName());
            }
            
            List<String> petLore = new ArrayList<>();
            petLore.add("§7Rarity: " + pet.getRarity().getGermanName());
            petLore.add("§7Level: §e" + pet.getLevel() + "§7/§e" + pet.getPetType().getMaxLevel());
            petLore.add("§7XP: §b" + pet.getExperience() + "§7/§b" + pet.getRequiredExperienceForNextLevel());
            petLore.add("§7Beschreibung: " + pet.getPetType().getDescription());
            petLore.add("");
            
            // Add feeding status
            if (pet.canBeFed()) {
                petLore.add("§a✓ Kann gefüttert werden");
            } else {
                petLore.add("§c✗ Kann noch nicht gefüttert werden");
            }
            
            petLore.add("");
            if (pet.isActive()) {
                petLore.add("§cKlicke um Pet zu deaktivieren!");
            } else {
                petLore.add("§eKlicke um Pet zu aktivieren!");
            }
            petLore.add("§aKlicke mit Shift um zu füttern!");
            
            petMeta.setLore(petLore);
            petItem.setItemMeta(petMeta);
            inventory.setItem(slot, petItem);
            
            slot++;
        }

        // Add sample pets if none exist
        if (playerPets.isEmpty()) {
            ItemStack noPets = new ItemStack(Material.CHEST);
            ItemMeta noPetsMeta = noPets.getItemMeta();
            noPetsMeta.setDisplayName("§7Keine Pets");
            List<String> noPetsLore = new ArrayList<>();
            noPetsLore.add("§7Du hast noch keine Pets!");
            noPetsLore.add("§7Sammle sie durch verschiedene");
            noPetsLore.add("§7Aktivitäten im Spiel.");
            noPetsMeta.setLore(noPetsLore);
            noPets.setItemMeta(noPetsMeta);
            inventory.setItem(31, noPets);
        }

        // Pet shop button
        ItemStack petShop = new ItemStack(Material.EMERALD);
        ItemMeta petShopMeta = petShop.getItemMeta();
        petShopMeta.setDisplayName("§a§lPet Shop");
        List<String> petShopLore = new ArrayList<>();
        petShopLore.add("§7Kaufe neue Pets");
        petShopLore.add("§7oder Pet-Upgrades!");
        petShopLore.add("");
        petShopLore.add("§eKlicke um Pet Shop zu öffnen!");
        petShopMeta.setLore(petShopLore);
        petShop.setItemMeta(petShopMeta);
        inventory.setItem(40, petShop);

        // Random pet button
        ItemStack randomPet = new ItemStack(Material.NETHER_STAR);
        ItemMeta randomPetMeta = randomPet.getItemMeta();
        randomPetMeta.setDisplayName("§d§lZufälliges Pet");
        List<String> randomPetLore = new ArrayList<>();
        randomPetLore.add("§7Erhalte ein zufälliges Pet!");
        randomPetLore.add("§7Kosten: §61000 Coins");
        randomPetLore.add("");
        randomPetLore.add("§eKlicke um zufälliges Pet zu erhalten!");
        randomPetMeta.setLore(randomPetLore);
        randomPet.setItemMeta(randomPetMeta);
        inventory.setItem(42, randomPet);

        addNavigationButtons();
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
        
        // Handle pet shop
        if (event.getSlot() == 40) {
            openPetShop(clickedPlayer);
            return;
        }
        
        // Handle random pet
        if (event.getSlot() == 42) {
            giveRandomPet(clickedPlayer);
            return;
        }
        
        // Handle pet selection/activation
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handlePetClick(clickedPlayer, event.getSlot(), event.isShiftClick());
            return;
        }
    }

    private void openPetShop(Player player) {
        player.closeInventory();
        PetShopGUI petShopGUI = new PetShopGUI(plugin, player);
        petShopGUI.open();
    }

    private void giveRandomPet(Player player) {
        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Check if player has enough coins
        if (profile.getCoins() < 1000) {
            player.sendMessage("§cDu benötigst 1000 Coins für ein zufälliges Pet!");
            return;
        }

        // Deduct coins
        profile.removeCoins(1000);

        // Create and add random pet
        petService.createRandomPet().thenAccept(pet -> {
            petService.addPetToPlayer(player, pet).thenAccept(success -> {
                if (success) {
                    player.sendMessage("§aDu hast ein zufälliges Pet erhalten!");
                    // Refresh GUI
                    setMenuItems();
                } else {
                    player.sendMessage("§cFehler beim Erstellen des Pets!");
                }
            });
        });
    }

    private void handlePetClick(Player player, int slot, boolean isShiftClick) {
        List<Pet> playerPets = petService.getPlayerPets(player);
        int petIndex = slot - 20;
        
        if (petIndex >= 0 && petIndex < playerPets.size()) {
            Pet pet = playerPets.get(petIndex);
            
            if (isShiftClick) {
                // Feed pet
                petService.feedPet(player, pet).thenAccept(success -> {
                    if (success) {
                        // Refresh GUI
                        setMenuItems();
                    }
                });
            } else {
                // Toggle pet activation
                if (pet.isActive()) {
                    // Deactivate pet
                    petService.setActivePet(player, null).thenAccept(success -> {
                        if (success) {
                            // Refresh GUI
                            setMenuItems();
                        }
                    });
                } else {
                    // Activate pet
                    petService.setActivePet(player, pet).thenAccept(success -> {
                        if (success) {
                            // Refresh GUI
                            setMenuItems();
                        }
                    });
                }
            }
        }
    }
}