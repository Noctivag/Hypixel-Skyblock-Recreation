package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.npcs.AdvancedNPCSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NPCCreationGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final Location location;

    public NPCCreationGUI(Plugin plugin, Player player, Location location) {
        super(54, Component.text("§6§lNPC Creation").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // NPC Types
        setupNPCTypes();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lNPC Creation").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Create a new NPC at:"),
                Component.text("§7World: §e" + location.getWorld().getName()),
                Component.text("§7X: §e" + location.getBlockX()),
                Component.text("§7Y: §e" + location.getBlockY()),
                Component.text("§7Z: §e" + location.getBlockZ()),
                Component.text(""),
                Component.text("§eClick on an NPC type to create!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupNPCTypes() {
        // Shop NPC
        setItem(10, Material.EMERALD, "§a§l🛒 Shop NPC", 
            "§7Creates a shop NPC",
            "§7• Opens shop interface",
            "§7• Sells items to players",
            "§7• Configurable shop items",
            "",
            "§eClick to create!");

        // Quest NPC
        setItem(11, Material.BOOK, "§b§l📚 Quest NPC", 
            "§7Creates a quest NPC",
            "§7• Gives quests to players",
            "§7• Quest management",
            "§7• Reward system",
            "",
            "§eClick to create!");

        // Info NPC
        setItem(12, Material.PAPER, "§e§lℹ️ Info NPC", 
            "§7Creates an info NPC",
            "§7• Provides information",
            "§7• Custom messages",
            "§7• Help system",
            "",
            "§eClick to create!");

        // Warp NPC
        setItem(13, Material.ENDER_PEARL, "§d§l🌍 Warp NPC", 
            "§7Creates a warp NPC",
            "§7• Teleports players",
            "§7• Warp destinations",
            "§7• Cost management",
            "",
            "§eClick to create!");

        // Bank NPC
        setItem(14, Material.GOLD_INGOT, "§6§l🏦 Bank NPC", 
            "§7Creates a bank NPC",
            "§7• Banking interface",
            "§7• Money management",
            "§7• Interest system",
            "",
            "§eClick to create!");

        // Auction NPC
        setItem(15, Material.GOLD_BLOCK, "§c§l🏪 Auction NPC", 
            "§7Creates an auction NPC",
            "§7• Auction house",
            "§7• Buy/sell items",
            "§7• Bidding system",
            "",
            "§eClick to create!");

        // Guild NPC
        setItem(16, Material.SHIELD, "§5§l🏰 Guild NPC", 
            "§7Creates a guild NPC",
            "§7• Guild management",
            "§7• Guild features",
            "§7• Guild wars",
            "",
            "§eClick to create!");

        // Pet NPC
        setItem(19, Material.BONE, "§d§l🐾 Pet NPC", 
            "§7Creates a pet NPC",
            "§7• Pet management",
            "§7• Pet shop",
            "§7• Pet care",
            "",
            "§eClick to create!");

        // Cosmetic NPC
        setItem(20, Material.NETHER_STAR, "§e§l✨ Cosmetic NPC", 
            "§7Creates a cosmetic NPC",
            "§7• Cosmetics shop",
            "§7• Particle effects",
            "§7• Customization",
            "",
            "§eClick to create!");

        // Admin NPC
        setItem(21, Material.COMMAND_BLOCK, "§4§l👑 Admin NPC", 
            "§7Creates an admin NPC",
            "§7• Admin functions",
            "§7• Server management",
            "§7• Admin tools",
            "",
            "§eClick to create!");
    }

    private void setupNavigation() {
        // Back
        setItem(45, Material.ARROW, "§7§l⬅️ Back", 
            "§7Return to previous menu",
            "",
            "§eClick to go back!");

        // Close
        setItem(49, Material.BARRIER, "§c§l❌ Close", 
            "§7Close this menu",
            "",
            "§eClick to close!");
    }

    private void setupDecorativeBorders() {
        // Top border
        for (int i = 0; i < 9; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Side borders
        for (int i = 9; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 17; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Bottom border
        for (int i = 45; i < 54; i++) {
            if (i != 49) { // Don't override close button
                setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            }
        }
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
