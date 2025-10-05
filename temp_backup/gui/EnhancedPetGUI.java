package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.pets.PetSystem;
import de.noctivag.skyblock.pets.Pet;
import de.noctivag.skyblock.pets.PetEvolutionSystem;
import de.noctivag.skyblock.pets.PetCandySystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhanced Pet GUI System - Advanced interface for pet management
 * 
 * Features:
 * - Pet collection display
 * - Pet evolution interface
 * - Pet candy system
 * - Pet statistics
 * - Pet comparison
 * - Pet trading
 */
public class EnhancedPetGUI implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final PetSystem petSystem;
    private final PetEvolutionSystem evolutionSystem;
    private final PetCandySystem candySystem;
    
    public EnhancedPetGUI(SkyblockPlugin SkyblockPlugin, PetSystem petSystem, PetEvolutionSystem evolutionSystem, PetCandySystem candySystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.petSystem = petSystem;
        this.evolutionSystem = evolutionSystem;
        this.candySystem = candySystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        
        if (title.contains("Pet System")) {
            event.setCancelled(true);
            handlePetGUIClick(player, event.getSlot());
        } else if (title.contains("Pet Collection")) {
            event.setCancelled(true);
            handleCollectionGUIClick(player, event.getSlot());
        } else if (title.contains("Pet Evolution")) {
            event.setCancelled(true);
            handleEvolutionGUIClick(player, event.getSlot());
        } else if (title.contains("Pet Candy")) {
            event.setCancelled(true);
            handleCandyGUIClick(player, event.getSlot());
        }
    }
    
    public void openMainPetGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§d§lPet System"));
        
        // Main pet display
        Pet activePet = petSystem.getActivePet(player.getUniqueId());
        if (activePet != null) {
            ItemStack petItem = activePet.createPetItem();
            gui.setItem(4, petItem);
        } else {
            addGUIItem(gui, 4, Material.BARRIER, "§c§lNo Active Pet", 
                new ArrayList<>(Arrays.asList("§7You don't have an active pet", "§7• Click on a pet to activate it", "", "§eClick to view your pets")));
        }
        
        // Main menu items
        addGUIItem(gui, 19, Material.CHEST, "§6§lMy Pets", 
            new ArrayList<>(Arrays.asList("§7View and manage your pets", "§7• Activate pets", "§7• View stats", "§7• Compare pets", "", "§eClick to open")));
        
        addGUIItem(gui, 20, Material.DIAMOND, "§b§lPet Shop", 
            new ArrayList<>(Arrays.asList("§7Buy new pets", "§7• Browse by category", "§7• View requirements", "§7• Purchase pets", "", "§eClick to open")));
        
        addGUIItem(gui, 21, Material.EXPERIENCE_BOTTLE, "§a§lPet Leveling", 
            new ArrayList<>(Arrays.asList("§7Level up your pets", "§7• Use XP items", "§7• Apply candy", "§7• View XP requirements", "", "§eClick to open")));
        
        addGUIItem(gui, 22, Material.NETHER_STAR, "§d§lPet Evolution", 
            new ArrayList<>(Arrays.asList()));
        
        addGUIItem(gui, 23, Material.COOKIE, "§e§lPet Candy", 
            new ArrayList<>(Arrays.asList()));
        
        addGUIItem(gui, 24, Material.BOOK, "§9§lPet Guide", 
            new ArrayList<>(Arrays.asList()));
        
        // Statistics section
        addGUIItem(gui, 28, Material.GOLD_INGOT, "§6§lPet Statistics", 
            new ArrayList<>(Arrays.asList()));
        
        addGUIItem(gui, 29, Material.PLAYER_HEAD, "§a§lPet Achievements", 
            new ArrayList<>(Arrays.asList()));
        
        addGUIItem(gui, 30, Material.ENDER_CHEST, "§5§lPet Storage", 
            new ArrayList<>(Arrays.asList()));
        
        // Settings and close
        addGUIItem(gui, 34, Material.REDSTONE_TORCH, "§c§lPet Settings", 
            new ArrayList<>(Arrays.asList()));
        
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            new ArrayList<>(Arrays.asList()));
        
        player.openInventory(gui);
    }
    
    public void openPetCollectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§6§lPet Collection"));
        
        List<Pet> playerPets = petSystem.getPlayerPets(player.getUniqueId());
        
        // Display player's pets
        int slot = 10;
        for (Pet pet : playerPets) {
            if (slot >= 44) break;
            
            ItemStack petItem = pet.createPetItem();
            
            // Add click action to lore
            ItemMeta meta = petItem.getItemMeta();
            if (meta != null) {
                List<String> lore = new ArrayList<>();
                if (meta.lore() != null) {
                    lore.addAll(meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()));
                }
                lore.add("");
                if (pet.isActive()) {
                    lore.add("§cRight-click to deactivate");
                } else {
                    lore.add("§aRight-click to activate");
                }
                lore.add("§eLeft-click to view details");
                
                meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                petItem.setItemMeta(meta);
            }
            
            gui.setItem(slot, petItem);
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip to next row
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Previous", 
            new ArrayList<>(Arrays.asList()));
        addGUIItem(gui, 49, Material.ARROW, "§7§lBack to Main", 
            new ArrayList<>(Arrays.asList()));
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext →", 
            new ArrayList<>(Arrays.asList()));
        
        player.openInventory(gui);
    }
    
    public void openPetEvolutionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§d§lPet Evolution"));
        
        List<Pet> playerPets = petSystem.getPlayerPets(player.getUniqueId());
        
        // Display pets that can evolve
        int slot = 10;
        for (Pet pet : playerPets) {
            if (slot >= 44) break;
            
            PetEvolutionSystem.PetEvolution evolution = evolutionSystem.getPetEvolution(pet.getType().getId());
            if (evolution != null && evolutionSystem.canEvolvePet(player, pet)) {
                ItemStack petItem = pet.createPetItem();
                
                // Add evolution info to lore
                ItemMeta meta = petItem.getItemMeta();
                if (meta != null) {
                List<String> lore = new ArrayList<>();
                if (meta.lore() != null) {
                    lore.addAll(meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()));
                }
                    lore.add("");
                    lore.add("§a§lCAN EVOLVE!");
                    lore.add("§7Evolves into: §e" + evolution.getEvolvedName());
                    lore.add("§7Requirements:");
                    for (PetEvolutionSystem.EvolutionRequirement req : evolution.getRequirements()) {
                        lore.add("§7• " + req.getDescription());
                    }
                    lore.add("");
                    lore.add("§aRight-click to evolve!");
                    
                    meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                    petItem.setItemMeta(meta);
                }
                
                gui.setItem(slot, petItem);
            } else if (evolution != null) {
                // Pet can evolve but requirements not met
                ItemStack petItem = pet.createPetItem();
                
                ItemMeta meta = petItem.getItemMeta();
                if (meta != null) {
                List<String> lore = new ArrayList<>();
                if (meta.lore() != null) {
                    lore.addAll(meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()));
                }
                    lore.add("");
                    lore.add("§c§lCANNOT EVOLVE YET");
                    lore.add("§7Evolves into: §e" + evolution.getEvolvedName());
                    lore.add("§7Missing requirements:");
                    for (PetEvolutionSystem.EvolutionRequirement req : evolution.getRequirements()) {
                        if (req.getType().equals("Level") && pet.getLevel() < req.getAmount()) {
                            lore.add("§7• " + req.getDescription() + " §c(Current: " + pet.getLevel() + ")");
                        } else {
                            lore.add("§7• " + req.getDescription());
                        }
                    }
                    
                    meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                    petItem.setItemMeta(meta);
                }
                
                gui.setItem(slot, petItem);
            }
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip to next row
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            new ArrayList<>(Arrays.asList()));
        addGUIItem(gui, 49, Material.BOOK, "§9§lEvolution Guide", 
            new ArrayList<>(Arrays.asList()));
        
        player.openInventory(gui);
    }
    
    public void openPetCandyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§e§lPet Candy"));
        
        // Display available candy types
        int slot = 10;
        for (PetCandySystem.PetCandy candy : candySystem.getAllPetCandies().values()) {
            if (slot >= 44) break;
            
            ItemStack candyItem = candySystem.createCandyItem(candy.getId());
            if (candyItem != null) {
                gui.setItem(slot, candyItem);
                slot++;
                if (slot % 9 == 8) slot += 2; // Skip to next row
            }
        }
        
        // Show active effects
        List<PetCandySystem.CandyEffect> activeEffects = candySystem.getActiveEffects(player.getUniqueId());
        if (!activeEffects.isEmpty()) {
            List<String> effectLines = new ArrayList<>();
            effectLines.add("§7Currently active candy effects:");
            effectLines.add("");
            for (PetCandySystem.CandyEffect effect : activeEffects) {
                effectLines.add("§7• " + effect.getCandyName() + " §7(" + (effect.getTimeRemaining() / 1000 / 60) + "m left)");
            }
            addGUIItem(gui, 4, Material.GOLDEN_APPLE, "§a§lActive Effects", effectLines);
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            new ArrayList<>(Arrays.asList()));
        addGUIItem(gui, 49, Material.CRAFTING_TABLE, "§6§lCraft Candy", 
            new ArrayList<>(Arrays.asList()));
        
        player.openInventory(gui);
    }
    
    private void handlePetGUIClick(Player player, int slot) {
        switch (slot) {
            case 19: // My Pets
                openPetCollectionGUI(player);
                break;
            case 20: // Pet Shop
                player.sendMessage(Component.text("§ePet Shop coming soon!"));
                break;
            case 21: // Pet Leveling
                player.sendMessage(Component.text("§ePet Leveling coming soon!"));
                break;
            case 22: // Pet Evolution
                openPetEvolutionGUI(player);
                break;
            case 23: // Pet Candy
                openPetCandyGUI(player);
                break;
            case 24: // Pet Guide
                player.sendMessage(Component.text("§ePet Guide coming soon!"));
                break;
            case 28: // Pet Statistics
                player.sendMessage(Component.text("§ePet Statistics coming soon!"));
                break;
            case 29: // Pet Achievements
                player.sendMessage(Component.text("§ePet Achievements coming soon!"));
                break;
            case 30: // Pet Storage
                player.sendMessage(Component.text("§ePet Storage coming soon!"));
                break;
            case 34: // Pet Settings
                player.sendMessage(Component.text("§ePet Settings coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void handleCollectionGUIClick(Player player, int slot) {
        if (slot >= 10 && slot <= 44) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(slot);
            if (item != null && item.hasItemMeta()) {
                // Handle pet activation/deactivation
                player.sendMessage(Component.text("§ePet interaction coming soon!"));
            }
        } else if (slot == 49) {
            openMainPetGUI(player);
        }
    }
    
    private void handleEvolutionGUIClick(Player player, int slot) {
        if (slot >= 10 && slot <= 44) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(slot);
            if (item != null && item.hasItemMeta()) {
                // Handle pet evolution
                player.sendMessage(Component.text("§ePet evolution coming soon!"));
            }
        } else if (slot == 45 || slot == 49) {
            openMainPetGUI(player);
        }
    }
    
    private void handleCandyGUIClick(Player player, int slot) {
        if (slot >= 10 && slot <= 44) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(slot);
            if (item != null && item.hasItemMeta()) {
                // Handle candy usage
                player.sendMessage(Component.text("§eCandy usage coming soon!"));
            }
        } else if (slot == 45 || slot == 49) {
            openMainPetGUI(player);
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
