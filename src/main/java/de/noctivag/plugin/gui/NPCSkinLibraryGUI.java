package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.npcs.NPCSkinManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class NPCSkinLibraryGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final NPCSkinManager skinManager;

    public NPCSkinLibraryGUI(Plugin plugin, Player player) {
        super(54, Component.text("§6§lNPC Skin Library").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.skinManager = new NPCSkinManager(plugin);
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Skin Categories
        setupSkinCategories();
        
        // Popular Skins
        setupPopularSkins();
        
        // Custom Skins
        setupCustomSkins();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lNPC Skin Library").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Browse and select skins for your NPCs"),
                Component.text("§7• Pre-made skins"),
                Component.text("§7• Popular skins"),
                Component.text("§7• Custom uploads"),
                Component.text(""),
                Component.text("§eClick on a skin to preview!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupSkinCategories() {
        // Fantasy Skins
        setItem(10, Material.DRAGON_HEAD, "§5§l🐉 Fantasy Skins", 
            "§7Mystical and magical skins",
            "§7• Wizards and mages",
            "§7• Dragons and creatures",
            "§7• Fantasy characters",
            "",
            "§eClick to browse!");

        // Medieval Skins
        setItem(11, Material.IRON_SWORD, "§6§l⚔️ Medieval Skins", 
            "§7Knights and warriors",
            "§7• Knights and paladins",
            "§7• Warriors and fighters",
            "§7• Medieval characters",
            "",
            "§eClick to browse!");

        // Modern Skins
        setItem(12, Material.DIAMOND, "§b§l💎 Modern Skins", 
            "§7Contemporary characters",
            "§7• Business people",
            "§7• Modern heroes",
            "§7• Contemporary looks",
            "",
            "§eClick to browse!");

        // Sci-Fi Skins
        setItem(13, Material.NETHER_STAR, "§d§l🚀 Sci-Fi Skins", 
            "§7Futuristic characters",
            "§7• Space explorers",
            "§7• Cyberpunk characters",
            "§7• Sci-fi heroes",
            "",
            "§eClick to browse!");

        // Anime Skins
        setItem(14, Material.EMERALD, "§a§l🌸 Anime Skins", 
            "§7Anime-style characters",
            "§7• Popular anime characters",
            "§7• Manga heroes",
            "§7• Anime styles",
            "",
            "§eClick to browse!");

        // Horror Skins
        setItem(15, Material.SKELETON_SKULL, "§4§l👻 Horror Skins", 
            "§7Spooky and scary skins",
            "§7• Monsters and creatures",
            "§7• Horror characters",
            "§7• Spooky looks",
            "",
            "§eClick to browse!");
    }

    private void setupPopularSkins() {
        // Popular Skin 1
        setItem(19, Material.PLAYER_HEAD, "§6§l👑 King Arthur", 
            "§7A legendary king",
            "§7• Royal appearance",
            "§7• Medieval style",
            "§7• Popular choice",
            "",
            "§eClick to select!");

        // Popular Skin 2
        setItem(20, Material.PLAYER_HEAD, "§b§l🧙‍♂️ Gandalf", 
            "§7A wise wizard",
            "§7• Magical appearance",
            "§7• Fantasy style",
            "§7• Popular choice",
            "",
            "§eClick to select!");

        // Popular Skin 3
        setItem(21, Material.PLAYER_HEAD, "§a§l🦸‍♂️ Superhero", 
            "§7A modern hero",
            "§7• Heroic appearance",
            "§7• Modern style",
            "§7• Popular choice",
            "",
            "§eClick to select!");

        // Popular Skin 4
        setItem(22, Material.PLAYER_HEAD, "§d§l👽 Alien", 
            "§7A space explorer",
            "§7• Futuristic appearance",
            "§7• Sci-fi style",
            "§7• Popular choice",
            "",
            "§eClick to select!");

        // Popular Skin 5
        setItem(23, Material.PLAYER_HEAD, "§e§l🌸 Anime Girl", 
            "§7A cute anime character",
            "§7• Anime appearance",
            "§7• Kawaii style",
            "§7• Popular choice",
            "",
            "§eClick to select!");

        // Popular Skin 6
        setItem(24, Material.PLAYER_HEAD, "§4§l🧛‍♂️ Vampire", 
            "§7A mysterious vampire",
            "§7• Gothic appearance",
            "§7• Horror style",
            "§7• Popular choice",
            "",
            "§eClick to select!");
    }

    private void setupCustomSkins() {
        // Upload Custom Skin
        setItem(28, Material.ANVIL, "§6§l📤 Upload Custom Skin", 
            "§7Upload your own skin",
            "§7• From file",
            "§7• From URL",
            "§7• From Minecraft account",
            "",
            "§eClick to upload!");

        // My Skins
        setItem(29, Material.CHEST, "§b§l📁 My Skins", 
            "§7Your uploaded skins",
            "§7• Personal collection",
            "§7• Custom uploads",
            "§7• Saved skins",
            "",
            "§eClick to view!");

        // Skin Editor
        setItem(30, Material.CRAFTING_TABLE, "§e§l🎨 Skin Editor", 
            "§7Edit and customize skins",
            "§7• Color changes",
            "§7• Pattern editing",
            "§7• Custom designs",
            "",
            "§eClick to edit!");

        // Random Skin
        setItem(31, Material.EMERALD, "§a§l🎲 Random Skin", 
            "§7Get a random skin",
            "§7• Surprise selection",
            "§7• Random from library",
            "§7• Fun surprise",
            "",
            "§eClick for random!");
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

        // Search
        setItem(51, Material.COMPASS, "§b§l🔍 Search", 
            "§7Search for specific skins",
            "",
            "§eClick to search!");
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
