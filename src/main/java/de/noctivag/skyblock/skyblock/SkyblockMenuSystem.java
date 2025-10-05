package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Skyblock Menu System - Handles main menu GUI
 */
public class SkyblockMenuSystem {
    
    private final SkyblockPlugin plugin;
    
    public SkyblockMenuSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Open main menu for player
     */
    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§6Skyblock Menu");
        
        // Add menu items
        addMenuItems(menu);
        
        player.openInventory(menu);
    }
    
    /**
     * Add items to menu
     */
    private void addMenuItems(Inventory menu) {
        // Skills
        ItemStack skills = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta skillsMeta = skills.getItemMeta();
        skillsMeta.setDisplayName("§bSkills");
        skillsMeta.setLore(Arrays.asList("§7View and manage your skills", "§7Click to open!"));
        skills.setItemMeta(skillsMeta);
        menu.setItem(10, skills);
        
        // Collections
        ItemStack collections = new ItemStack(Material.CHEST);
        ItemMeta collectionsMeta = collections.getItemMeta();
        collectionsMeta.setDisplayName("§aCollections");
        collectionsMeta.setLore(Arrays.asList("§7View your item collections", "§7Click to open!"));
        collections.setItemMeta(collectionsMeta);
        menu.setItem(12, collections);
        
        // Bank
        ItemStack bank = new ItemStack(Material.GOLD_INGOT);
        ItemMeta bankMeta = bank.getItemMeta();
        bankMeta.setDisplayName("§6Bank");
        bankMeta.setLore(Arrays.asList("§7Manage your coins", "§7Click to open!"));
        bank.setItemMeta(bankMeta);
        menu.setItem(14, bank);
        
        // Pets
        ItemStack pets = new ItemStack(Material.BONE);
        ItemMeta petsMeta = pets.getItemMeta();
        petsMeta.setDisplayName("§dPets");
        petsMeta.setLore(Arrays.asList("§7Manage your pets", "§7Click to open!"));
        pets.setItemMeta(petsMeta);
        menu.setItem(16, pets);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        menu.setItem(49, close);
    }
}