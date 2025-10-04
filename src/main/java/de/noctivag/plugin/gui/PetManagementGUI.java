package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.pets.PetSystem;
import de.noctivag.plugin.pets.Pet;
import de.noctivag.plugin.pets.PetManagementSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;

/**
 * Pet Management GUI - Hypixel Skyblock Style
 */
public class PetManagementGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final PetSystem petSystem;
    private final PetManagementSystem petManagementSystem;
    private Pet selectedPet;
    private int currentPage = 0;
    private final int itemsPerPage = 28;
    
    public PetManagementGUI(Plugin plugin, Player player, PetSystem petSystem, PetManagementSystem petManagementSystem) {
        super(54, Component.text("§d§l⚡ PET MANAGEMENT ⚡").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.petSystem = petSystem;
        this.petManagementSystem = petManagementSystem;
        setupItems();
    }
    
    private void setupItems() {
        // Header
        setupHeader();
        
        // Pet selection area
        setupPetSelection();
        
        // Pet info area
        setupPetInfo();
        
        // Action buttons
        setupActionButtons();
        
        // Navigation
        setupNavigation();
    }
    
    private void setupHeader() {
        // Title item
        ItemStack titleItem = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta titleMeta = titleItem.getItemMeta();
        if (titleMeta != null) {
            titleMeta.displayName(Component.text("§d§l⚡ PET MANAGEMENT ⚡"));
            List<String> lore = Arrays.asList(
                "§7Manage your pets and their abilities!",
                "§7Activate, level up, and upgrade your pets.",
                "",
                "§eClick on a pet to select it!"
            );
            titleMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            titleItem.setItemMeta(titleMeta);
        }
        setItem(4, titleItem);
    }
    
    private void setupPetSelection() {
        List<Pet> playerPets = petManagementSystem.getPlayerPets(player.getUniqueId());
        Pet activePet = petManagementSystem.getActivePet(player.getUniqueId());
        
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, playerPets.size());
        
        int slot = 19;
        for (int i = startIndex; i < endIndex; i++) {
            Pet pet = playerPets.get(i);
            
            ItemStack petItem = pet.createPetItem();
            ItemMeta petMeta = petItem.getItemMeta();
            if (petMeta != null) {
                String displayName = petMeta.displayName().toString();
                if (pet.equals(activePet)) {
                    displayName = "§a✓ " + displayName;
                } else if (pet.equals(selectedPet)) {
                    displayName = "§e✓ " + displayName;
                }
                petMeta.displayName(Component.text(displayName));
                
                List<String> lore = new ArrayList<>();
                if (petMeta.hasLore()) {
                    lore.addAll(petMeta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()));
                }
                lore.add("");
                if (pet.equals(activePet)) {
                    lore.add("§a✓ Currently Active");
                } else if (pet.equals(selectedPet)) {
                    lore.add("§e✓ Selected");
                } else {
                    lore.add("§7Click to select this pet!");
                }
                
                petMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                petItem.setItemMeta(petMeta);
            }
            setItem(slot, petItem);
            slot++;
        }
    }
    
    private void setupPetInfo() {
        if (selectedPet != null) {
            // Pet stats
            ItemStack statsItem = new ItemStack(Material.BOOK);
            ItemMeta statsMeta = statsItem.getItemMeta();
            if (statsMeta != null) {
                statsMeta.displayName(Component.text("§ePet Statistics"));
                List<String> lore = Arrays.asList(
                    "§7Name: §e" + selectedPet.getType().getName(),
                    "§7Level: §e" + selectedPet.getLevel(),
                    "§7XP: §e" + String.format("%.1f", selectedPet.getXP()),
                    "§7Hunger: §e" + selectedPet.getHunger() + "/100",
                    "§7Happiness: §e" + selectedPet.getHappiness() + "/100",
                    "",
                    "§7Upgrades:",
                    "§7• Speed: §e" + selectedPet.getUpgradeLevel("speed"),
                    "§7• Health: §e" + selectedPet.getUpgradeLevel("health"),
                    "§7• Damage: §e" + selectedPet.getUpgradeLevel("damage"),
                    "§7• XP Boost: §e" + selectedPet.getUpgradeLevel("xp_boost")
                );
                statsMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                statsItem.setItemMeta(statsMeta);
            }
            setItem(25, statsItem);
            
            // Pet abilities
            ItemStack abilitiesItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta abilitiesMeta = abilitiesItem.getItemMeta();
            if (abilitiesMeta != null) {
                abilitiesMeta.displayName(Component.text("§bPet Abilities"));
                List<String> lore = new ArrayList<>();
                lore.add("§7Available Abilities:");
                for (PetSystem.PetAbility ability : selectedPet.getType().getAbilities()) {
                    if (selectedPet.getLevel() >= ability.getUnlockLevel()) {
                        lore.add("§a✓ " + ability.getName() + " §7- " + ability.getDescription());
                    } else {
                        lore.add("§7✗ " + ability.getName() + " §7(Level " + ability.getUnlockLevel() + ")");
                    }
                }
                abilitiesMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                abilitiesItem.setItemMeta(abilitiesMeta);
            }
            setItem(34, abilitiesItem);
        } else {
            // No pet selected
            ItemStack noPet = new ItemStack(Material.BARRIER);
            ItemMeta noPetMeta = noPet.getItemMeta();
            if (noPetMeta != null) {
                noPetMeta.displayName(Component.text("§cNo Pet Selected"));
                List<String> lore = Arrays.asList(
                    "§7Select a pet to view its information",
                    "§7and manage its settings."
                );
                noPetMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                noPet.setItemMeta(noPetMeta);
            }
            setItem(25, noPet);
        }
    }
    
    private void setupActionButtons() {
        if (selectedPet != null) {
            Pet activePet = petManagementSystem.getActivePet(player.getUniqueId());
            
            // Activate/Deactivate button
            ItemStack activateButton = new ItemStack(selectedPet.equals(activePet) ? Material.REDSTONE : Material.EMERALD);
            ItemMeta activateMeta = activateButton.getItemMeta();
            if (activateMeta != null) {
                if (selectedPet.equals(activePet)) {
                    activateMeta.displayName(Component.text("§c§lDEACTIVATE PET"));
                    List<String> lore = Arrays.asList(
                        "§7Deactivate " + selectedPet.getType().getName(),
                        "§7and remove its effects."
                    );
                    activateMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                } else {
                    activateMeta.displayName(Component.text("§a§lACTIVATE PET"));
                    List<String> lore = Arrays.asList(
                        "§7Activate " + selectedPet.getType().getName(),
                        "§7and apply its effects."
                    );
                    activateMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                }
                activateButton.setItemMeta(activateMeta);
            }
            setItem(45, activateButton);
            
            // Level up button
            ItemStack levelUpButton = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta levelUpMeta = levelUpButton.getItemMeta();
            if (levelUpMeta != null) {
                levelUpMeta.displayName(Component.text("§e§lLEVEL UP PET"));
                List<String> lore = Arrays.asList(
                    "§7Level up " + selectedPet.getType().getName(),
                    "§7Cost: §6" + selectedPet.getLevelUpCost() + " coins"
                );
                levelUpMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                levelUpButton.setItemMeta(levelUpMeta);
            }
            setItem(47, levelUpButton);
            
            // Feed button
            ItemStack feedButton = new ItemStack(Material.BREAD);
            ItemMeta feedMeta = feedButton.getItemMeta();
            if (feedMeta != null) {
                feedMeta.displayName(Component.text("§6§lFEED PET"));
                List<String> lore = Arrays.asList(
                    "§7Feed " + selectedPet.getType().getName(),
                    "§7to increase hunger and happiness.",
                    "",
                    "§7Current Hunger: §e" + selectedPet.getHunger() + "/100",
                    "§7Current Happiness: §e" + selectedPet.getHappiness() + "/100"
                );
                feedMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                feedButton.setItemMeta(feedMeta);
            }
            setItem(49, feedButton);
            
            // Upgrade button
            ItemStack upgradeButton = new ItemStack(Material.NETHER_STAR);
            ItemMeta upgradeMeta = upgradeButton.getItemMeta();
            if (upgradeMeta != null) {
                upgradeMeta.displayName(Component.text("§d§lUPGRADE PET"));
                List<String> lore = Arrays.asList(
                    "§7Upgrade " + selectedPet.getType().getName(),
                    "§7to improve its abilities.",
                    "",
                    "§7Available Upgrades:",
                    "§7• Speed: §e" + selectedPet.getUpgradeLevel("speed"),
                    "§7• Health: §e" + selectedPet.getUpgradeLevel("health"),
                    "§7• Damage: §e" + selectedPet.getUpgradeLevel("damage"),
                    "§7• XP Boost: §e" + selectedPet.getUpgradeLevel("xp_boost")
                );
                upgradeMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                upgradeButton.setItemMeta(upgradeMeta);
            }
            setItem(51, upgradeButton);
        } else {
            // No pet selected - show disabled buttons
            ItemStack noSelection = new ItemStack(Material.BARRIER);
            ItemMeta noSelectionMeta = noSelection.getItemMeta();
            if (noSelectionMeta != null) {
                noSelectionMeta.displayName(Component.text("§c§lNO PET SELECTED"));
                List<String> lore = Arrays.asList(
                    "§7Select a pet to manage it."
                );
                noSelectionMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                noSelection.setItemMeta(noSelectionMeta);
            }
            setItem(45, noSelection);
            setItem(47, noSelection);
            setItem(49, noSelection);
            setItem(51, noSelection);
        }
    }
    
    private void setupNavigation() {
        List<Pet> playerPets = petManagementSystem.getPlayerPets(player.getUniqueId());
        
        // Previous page
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            if (prevMeta != null) {
                prevMeta.displayName(Component.text("§e§l← Previous Page"));
                prevPage.setItemMeta(prevMeta);
            }
            setItem(36, prevPage);
        }
        
        // Next page
        if ((currentPage + 1) * itemsPerPage < playerPets.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            if (nextMeta != null) {
                nextMeta.displayName(Component.text("§e§lNext Page →"));
                nextPage.setItemMeta(nextMeta);
            }
            setItem(44, nextPage);
        }
        
        // Page info
        ItemStack pageInfo = new ItemStack(Material.PAPER);
        ItemMeta pageMeta = pageInfo.getItemMeta();
        if (pageMeta != null) {
            pageMeta.displayName(Component.text("§7Page " + (currentPage + 1)));
            List<String> lore = Arrays.asList(
                "§7Showing pets " + (currentPage * itemsPerPage + 1) + "-" + 
                Math.min((currentPage + 1) * itemsPerPage, playerPets.size()) + 
                " of " + playerPets.size()
            );
            pageMeta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            pageInfo.setItemMeta(pageMeta);
        }
        setItem(40, pageInfo);
    }
    
    public void selectPet(Pet pet) {
        this.selectedPet = pet;
        refreshGUI();
    }
    
    public void clearSelection() {
        this.selectedPet = null;
        refreshGUI();
    }
    
    public void nextPage() {
        List<Pet> playerPets = petManagementSystem.getPlayerPets(player.getUniqueId());
        if ((currentPage + 1) * itemsPerPage < playerPets.size()) {
            currentPage++;
            refreshGUI();
        }
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshGUI();
        }
    }
    
    public void activatePet() {
        if (selectedPet != null) {
            Pet activePet = petManagementSystem.getActivePet(player.getUniqueId());
            if (selectedPet.equals(activePet)) {
                petManagementSystem.deactivatePet(player, selectedPet);
            } else {
                petManagementSystem.activatePet(player, selectedPet);
            }
            refreshGUI();
        }
    }
    
    public void levelUpPet() {
        if (selectedPet != null) {
            petManagementSystem.levelUpPet(player, selectedPet);
            refreshGUI();
        }
    }
    
    public void feedPet() {
        if (selectedPet != null) {
            // This would open a food selection GUI
            player.sendMessage("§eSelect food from your inventory to feed your pet!");
        }
    }
    
    public void upgradePet() {
        if (selectedPet != null) {
            // This would open an upgrade selection GUI
            player.sendMessage("§eSelect an upgrade type to upgrade your pet!");
        }
    }
    
    private void refreshGUI() {
        clearInventory();
        setupItems();
    }
}
