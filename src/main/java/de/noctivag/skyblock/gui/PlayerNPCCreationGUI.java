package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.npcs.AdvancedNPCSystem;
import de.noctivag.skyblock.npcs.NPCSkinManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class PlayerNPCCreationGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    private final Location location;
    private final NPCSkinManager skinManager;

    public PlayerNPCCreationGUI(SkyblockPlugin plugin, Player player, Location location) {
        super(54, Component.text("§6§lPlayer NPC Creation").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.skinManager = new NPCSkinManager(plugin);
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Player NPC Types with Skins
        setupPlayerNPCTypes();
        
        // Skin Customization
        setupSkinCustomization();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lPlayer NPC Creation").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Create a Player-based NPC at:"),
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

    private void setupPlayerNPCTypes() {
        // Shop NPC with Player Skin
        ItemStack shopSkull = skinManager.createSkullItem("shop");
        if (shopSkull != null) {
            getInventory().setItem(10, shopSkull);
        } else {
            setItem(10, Material.EMERALD, "§a§l🛒 Shop NPC (Player)", 
                "§7Creates a Player-based shop NPC",
                "§7• Custom player skin",
                "§7• Opens shop interface",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Quest NPC with Player Skin
        ItemStack questSkull = skinManager.createSkullItem("quest");
        if (questSkull != null) {
            getInventory().setItem(11, questSkull);
        } else {
            setItem(11, Material.BOOK, "§b§l📚 Quest NPC (Player)", 
                "§7Creates a Player-based quest NPC",
                "§7• Custom player skin",
                "§7• Gives quests to players",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Info NPC with Player Skin
        ItemStack infoSkull = skinManager.createSkullItem("info");
        if (infoSkull != null) {
            getInventory().setItem(12, infoSkull);
        } else {
            setItem(12, Material.PAPER, "§e§lℹ️ Info NPC (Player)", 
                "§7Creates a Player-based info NPC",
                "§7• Custom player skin",
                "§7• Provides information",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Warp NPC with Player Skin
        ItemStack warpSkull = skinManager.createSkullItem("warp");
        if (warpSkull != null) {
            getInventory().setItem(13, warpSkull);
        } else {
            setItem(13, Material.ENDER_PEARL, "§d§l🌍 Warp NPC (Player)", 
                "§7Creates a Player-based warp NPC",
                "§7• Custom player skin",
                "§7• Teleports players",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Bank NPC with Player Skin
        ItemStack bankSkull = skinManager.createSkullItem("bank");
        if (bankSkull != null) {
            getInventory().setItem(14, bankSkull);
        } else {
            setItem(14, Material.GOLD_INGOT, "§6§l🏦 Bank NPC (Player)", 
                "§7Creates a Player-based bank NPC",
                "§7• Custom player skin",
                "§7• Opens banking interface",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Auction NPC with Player Skin
        ItemStack auctionSkull = skinManager.createSkullItem("auction");
        if (auctionSkull != null) {
            getInventory().setItem(15, auctionSkull);
        } else {
            setItem(15, Material.GOLD_BLOCK, "§c§l🏪 Auction NPC (Player)", 
                "§7Creates a Player-based auction NPC",
                "§7• Custom player skin",
                "§7• Opens auction house",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Guild NPC with Player Skin
        ItemStack guildSkull = skinManager.createSkullItem("guild");
        if (guildSkull != null) {
            getInventory().setItem(16, guildSkull);
        } else {
            setItem(16, Material.SHIELD, "§5§l🏰 Guild NPC (Player)", 
                "§7Creates a Player-based guild NPC",
                "§7• Custom player skin",
                "§7• Manages guilds",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Pet NPC with Player Skin
        ItemStack petSkull = skinManager.createSkullItem("pet");
        if (petSkull != null) {
            getInventory().setItem(19, petSkull);
        } else {
            setItem(19, Material.BONE, "§d§l🐾 Pet NPC (Player)", 
                "§7Creates a Player-based pet NPC",
                "§7• Custom player skin",
                "§7• Manages pets",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Cosmetic NPC with Player Skin
        ItemStack cosmeticSkull = skinManager.createSkullItem("cosmetic");
        if (cosmeticSkull != null) {
            getInventory().setItem(20, cosmeticSkull);
        } else {
            setItem(20, Material.NETHER_STAR, "§e§l✨ Cosmetic NPC (Player)", 
                "§7Creates a Player-based cosmetic NPC",
                "§7• Custom player skin",
                "§7• Opens cosmetics",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }

        // Admin NPC with Player Skin
        ItemStack adminSkull = skinManager.createSkullItem("admin");
        if (adminSkull != null) {
            getInventory().setItem(21, adminSkull);
        } else {
            setItem(21, Material.COMMAND_BLOCK, "§4§l👑 Admin NPC (Player)", 
                "§7Creates a Player-based admin NPC",
                "§7• Custom player skin",
                "§7• Admin functions",
                "§7• Looks like a real player",
                "",
                "§eClick to create!");
        }
    }

    private void setupSkinCustomization() {
        // Custom Skin Upload
        setItem(28, Material.ANVIL, "§6§l🎨 Custom Skin", 
            "§7Upload a custom skin for your NPC",
            "§7• Use your own skin",
            "§7• Upload from URL",
            "§7• Choose from library",
            "",
            "§eClick to customize!");

        // Skin Library
        setItem(29, Material.CHEST, "§b§l📚 Skin Library", 
            "§7Browse available skins",
            "§7• Pre-made skins",
            "§7• Popular skins",
            "§7• Category filters",
            "",
            "§eClick to browse!");

        // Skin Preview
        setItem(30, Material.ITEM_FRAME, "§e§l👁️ Preview Skin", 
            "§7Preview the selected skin",
            "§7• 3D preview",
            "§7• Rotation controls",
            "§7• Zoom in/out",
            "",
            "§eClick to preview!");
    }

    private void setupNavigation() {
        // Back to Regular NPC Creation
        setItem(45, Material.ARROW, "§7§l⬅️ Back to Villager NPCs", 
            "§7Return to regular NPC creation",
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
