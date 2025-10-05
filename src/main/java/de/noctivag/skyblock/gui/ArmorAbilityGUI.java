package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.armor.ArmorAbilitySystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class ArmorAbilityGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    private final ArmorAbilitySystem armorAbilitySystem;

    public ArmorAbilityGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l🛡️ Armor Ability System 🛡️").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        // Placeholder - method not implemented
        this.armorAbilitySystem = null;
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Dragon Armor Abilities Section
        setupDragonArmorAbilities();
        
        // Dungeon Armor Abilities Section
        setupDungeonArmorAbilities();
        
        // Slayer Armor Abilities Section
        setupSlayerArmorAbilities();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§l🛡️ Armor Ability System").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Activate special abilities"),
                Component.text("§7with full armor sets!"),
                Component.text(""),
                Component.text("§7• Full set required"),
                Component.text("§7• Right-click to activate"),
                Component.text("§7• Cooldowns apply"),
                Component.text("§7• Passive bonuses included"),
                Component.text(""),
                Component.text("§eRight-click to activate!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupDragonArmorAbilities() {
        // Superior Dragon
        setItem(10, Material.DRAGON_HEAD, "§6§l🐉 Superior Dragon", 
            "§7All-around stat boost",
            "§7• Ability: §aAll stats +25%",
            "§7• Duration: §a30 seconds",
            "§7• Cooldown: §a60 seconds",
            "§7• Passive: §a+10% all stats",
            "",
            "§eRight-click to activate!");

        // Unstable Dragon
        setItem(11, Material.END_CRYSTAL, "§5§l⚡ Unstable Dragon", 
            "§7Random effect activation",
            "§7• Ability: §aRandom stat boost",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a45 seconds",
            "§7• Passive: §a+5% critical chance",
            "",
            "§eRight-click to activate!");

        // Strong Dragon
        setItem(12, Material.DIAMOND_SWORD, "§c§l💪 Strong Dragon", 
            "§7Damage boost",
            "§7• Ability: §aDamage +50%",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a50 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");

        // Young Dragon
        setItem(13, Material.FEATHER, "§f§l🏃 Young Dragon", 
            "§7Speed boost",
            "§7• Ability: §aSpeed +100%",
            "§7• Duration: §a15 seconds",
            "§7• Cooldown: §a40 seconds",
            "§7• Passive: §a+20% speed",
            "",
            "§eRight-click to activate!");

        // Old Dragon
        setItem(14, Material.IRON_CHESTPLATE, "§7§l🛡️ Old Dragon", 
            "§7Defense boost",
            "§7• Ability: §aDefense +75%",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a55 seconds",
            "§7• Passive: §a+25% defense",
            "",
            "§eRight-click to activate!");

        // Protector Dragon
        setItem(15, Material.SHIELD, "§a§l🛡️ Protector Dragon", 
            "§7Maximum defense",
            "§7• Ability: §aMaximum defense",
            "§7• Duration: §a30 seconds",
            "§7• Cooldown: §a70 seconds",
            "§7• Passive: §a+30% defense",
            "",
            "§eRight-click to activate!");

        // Wise Dragon
        setItem(16, Material.ENCHANTING_TABLE, "§b§l🧙 Wise Dragon", 
            "§7Intelligence boost",
            "§7• Ability: §aIntelligence +100%",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a50 seconds",
            "§7• Passive: §a+20% intelligence",
            "",
            "§eRight-click to activate!");
    }

    private void setupDungeonArmorAbilities() {
        // Shadow Assassin
        setItem(19, Material.BLACK_DYE, "§5§l👤 Shadow Assassin", 
            "§7Stealth mode",
            "§7• Ability: §aStealth mode",
            "§7• Duration: §a10 seconds",
            "§7• Cooldown: §a30 seconds",
            "§7• Passive: §a+15% speed",
            "",
            "§eRight-click to activate!");

        // Adaptive Armor
        setItem(20, Material.LEATHER_CHESTPLATE, "§e§l🔄 Adaptive Armor", 
            "§7Adaptation",
            "§7• Ability: §aAdapt to environment",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a60 seconds",
            "§7• Passive: §a+10% all stats",
            "",
            "§eRight-click to activate!");

        // Frozen Blaze
        setItem(21, Material.BLUE_ICE, "§b§l❄️ Frozen Blaze", 
            "§7Ice and fire effects",
            "§7• Ability: §aIce & fire effects",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a45 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");

        // Yeti Armor
        setItem(22, Material.SNOW_BLOCK, "§f§l🧊 Yeti Armor", 
            "§7Cold immunity",
            "§7• Ability: §aCold immunity",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a50 seconds",
            "§7• Passive: §a+20% defense",
            "",
            "§eRight-click to activate!");
    }

    private void setupSlayerArmorAbilities() {
        // Revenant Armor
        setItem(28, Material.ROTTEN_FLESH, "§2§l🧟 Revenant Armor", 
            "§7Undead immunity",
            "§7• Ability: §aUndead immunity",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a40 seconds",
            "§7• Passive: §a+15% damage vs zombies",
            "",
            "§eRight-click to activate!");

        // Tarantula Armor
        setItem(29, Material.STRING, "§8§l🕷️ Tarantula Armor", 
            "§7Spider abilities",
            "§7• Ability: §aSpider abilities",
            "§7• Duration: §a20 seconds",
            "§7• Cooldown: §a45 seconds",
            "§7• Passive: §a+15% damage vs spiders",
            "",
            "§eRight-click to activate!");

        // Sven Armor
        setItem(30, Material.BONE, "§f§l🐺 Sven Armor", 
            "§7Wolf pack abilities",
            "§7• Ability: §aWolf pack abilities",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a50 seconds",
            "§7• Passive: §a+15% damage vs wolves",
            "",
            "§eRight-click to activate!");

        // Voidgloom Armor
        setItem(31, Material.ENDER_PEARL, "§5§l👻 Voidgloom Armor", 
            "§7Enderman abilities",
            "§7• Ability: §aEnderman abilities",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a55 seconds",
            "§7• Passive: §a+15% damage vs endermen",
            "",
            "§eRight-click to activate!");

        // Inferno Armor
        setItem(32, Material.BLAZE_ROD, "§6§l🔥 Inferno Armor", 
            "§7Fire abilities",
            "§7• Ability: §aFire abilities",
            "§7• Duration: §a25 seconds",
            "§7• Cooldown: §a50 seconds",
            "§7• Passive: §a+15% damage vs blazes",
            "",
            "§eRight-click to activate!");
    }

    private void setupStatistics() {
        // Player Statistics
        ArmorAbilitySystem.PlayerArmorAbilities playerAbilities = armorAbilitySystem.getPlayerArmorAbilities(player.getUniqueId());
        setItem(37, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your Armor Ability usage:",
            "§7• Total abilities used: §a" + playerAbilities.getTotalAbilitiesUsed(),
            "§7• Active cooldowns: §a" + playerAbilities.getCooldowns().size(),
            "§7• Most used ability: §aSuperior Dragon",
            "§7• Average cooldown: §a45 seconds",
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(38, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global Armor Ability usage:",
            "§7• Total abilities used: §a1,234,567",
            "§7• Most popular: §aSuperior Dragon",
            "§7• Average per player: §a12.3",
            "§7• Success rate: §a100%",
            "",
            "§eClick to view global stats!");

        // Leaderboard
        setItem(39, Material.GOLD_BLOCK, "§6§l🏆 Armor Ability Leaderboard", 
            "§7Top Armor Ability users:",
            "§7• #1 PlayerName: §a1,234 abilities",
            "§7• #2 PlayerName: §a1,123 abilities",
            "§7• #3 PlayerName: §a1,012 abilities",
            "§7• #4 PlayerName: §a901 abilities",
            "§7• #5 PlayerName: §a890 abilities",
            "",
            "§eClick to view full leaderboard!");
    }

    private void setupNavigation() {
        // Back to Main Menu
        setItem(45, Material.ARROW, "§7§l⬅️ Back to Main Menu", 
            "§7Return to the main menu",
            "",
            "§eClick to go back!");

        // Close
        setItem(49, Material.BARRIER, "§c§l❌ Close", 
            "§7Close this menu",
            "",
            "§eClick to close!");

        // Refresh
        setItem(51, Material.EMERALD, "§a§l🔄 Refresh", 
            "§7Refresh this menu",
            "",
            "§eClick to refresh!");
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
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
