package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.skyblock.EnchantingSystem;
import de.noctivag.skyblock.skyblock.EnchantingSystem.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;


/**
 * Enchanting GUI - Hypixel Skyblock Style
 */
public class EnchantingGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    private final EnchantingSystem enchantingSystem;
    private ItemStack selectedItem;
    private CustomEnchantment selectedEnchantment;
    private int selectedLevel = 1;
    
    public EnchantingGUI(SkyblockPlugin SkyblockPlugin, Player player, EnchantingSystem enchantingSystem) {
        super(54, Component.text("§b§l⚡ ENCHANTING SYSTEM ⚡").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        this.enchantingSystem = enchantingSystem;
        setupItems();
    }
    
    private void setupItems() {
        // Header
        ItemStack titleItem = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta titleMeta = titleItem.getItemMeta();
        if (titleMeta != null) {
            titleMeta.displayName(Component.text("§b§l⚡ ENCHANTING SYSTEM ⚡"));
            titleItem.setItemMeta(titleMeta);
        }
        setItem(4, titleItem);
        
        // Item slot
        ItemStack itemSlot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemSlot.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("§7Item Slot"));
            itemSlot.setItemMeta(itemMeta);
        }
        setItem(19, itemSlot);
        
        // Enchant button
        ItemStack enchantButton = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantMeta = enchantButton.getItemMeta();
        if (enchantMeta != null) {
            enchantMeta.displayName(Component.text("§a§lENCHANT ITEM"));
            enchantButton.setItemMeta(enchantMeta);
        }
        setItem(49, enchantButton);
    }
    
    public void selectItem(ItemStack item) {
        this.selectedItem = item;
        refreshGUI();
    }
    
    public void selectEnchantment(CustomEnchantment enchantment) {
        this.selectedEnchantment = enchantment;
        refreshGUI();
    }
    
    public boolean canEnchant() {
        return selectedItem != null && selectedEnchantment != null;
    }
    
    public void performEnchant() {
        if (canEnchant()) {
            enchantingSystem.enchantItem(player, selectedItem, selectedEnchantment, selectedLevel);
            player.sendMessage(Component.text("§a§lENCHANTMENT SUCCESSFUL!"));
        }
    }
    
    private void refreshGUI() {
        clearInventory();
        setupItems();
    }
}
