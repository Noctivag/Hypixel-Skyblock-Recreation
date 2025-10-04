package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.weapons.WeaponAbilitySystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class WeaponAbilityGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    private final WeaponAbilitySystem weaponAbilitySystem;

    public WeaponAbilityGUI(SkyblockPlugin plugin, Player player) {
        super(54, Component.text("§c§l⚔️ Weapon Ability System ⚔️").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.weaponAbilitySystem = null; // WeaponAbilitySystem not implemented yet
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Sword Abilities Section
        setupSwordAbilities();
        
        // Bow Abilities Section
        setupBowAbilities();
        
        // Special Weapon Abilities Section
        setupSpecialWeaponAbilities();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§c§l⚔️ Weapon Ability System").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Activate special abilities"),
                Component.text("§7with your weapons!"),
                Component.text(""),
                Component.text("§7• Right-click to activate"),
                Component.text("§7• Cooldowns apply"),
                Component.text("§7• Passive bonuses included"),
                Component.text("§7• Special effects on hit"),
                Component.text(""),
                Component.text("§eRight-click to activate!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupSwordAbilities() {
        // Aspect of the End
        setItem(10, Material.ENDER_PEARL, "§a§l⚡ Aspect of the End", 
            "§7Teleport behind enemies",
            "§7• Ability: §aInstant Transmission",
            "§7• Effect: §aTeleport behind nearest enemy",
            "§7• Cooldown: §a15 seconds",
            "§7• Passive: §a+10% damage",
            "",
            "§eRight-click to activate!");

        // Aspect of the Dragons
        setItem(11, Material.DRAGON_HEAD, "§6§l🐉 Aspect of the Dragons", 
            "§7Dragon breath attack",
            "§7• Ability: §aDragon Rage",
            "§7• Effect: §aDragon breath attack",
            "§7• Cooldown: §a20 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");

        // Midas Sword
        setItem(12, Material.GOLD_INGOT, "§6§l💰 Midas Sword", 
            "§7Gold touch effect",
            "§7• Ability: §aMidas Touch",
            "§7• Effect: §aGold touch effect",
            "§7• Cooldown: §a30 seconds",
            "§7• Passive: §a+20% damage",
            "",
            "§eRight-click to activate!");

        // Hyperion
        setItem(13, Material.NETHER_STAR, "§d§l⭐ Hyperion", 
            "§7Teleport and implosion",
            "§7• Ability: §aWither Impact",
            "§7• Effect: §aTeleport and implosion",
            "§7• Cooldown: §a25 seconds",
            "§7• Passive: §a+25% damage",
            "",
            "§eRight-click to activate!");

        // Valkyrie
        setItem(14, Material.FEATHER, "§6§l👼 Valkyrie", 
            "§7Healing ability",
            "§7• Ability: §aValkyrie's Blessing",
            "§7• Effect: §aHealing ability",
            "§7• Cooldown: §a20 seconds",
            "§7• Passive: §a+20% damage",
            "",
            "§eRight-click to activate!");

        // Scylla
        setItem(15, Material.SKELETON_SKULL, "§5§l💀 Scylla", 
            "§7Health drain",
            "§7• Ability: §aSoul Siphon",
            "§7• Effect: §aHealth drain",
            "§7• Cooldown: §a18 seconds",
            "§7• Passive: §a+18% damage",
            "",
            "§eRight-click to activate!");

        // Astraea
        setItem(16, Material.SHIELD, "§b§l🛡️ Astraea", 
            "§7Protective barrier",
            "§7• Ability: §aDivine Protection",
            "§7• Effect: §aProtective barrier",
            "§7• Cooldown: §a25 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");
    }

    private void setupBowAbilities() {
        // Runaan's Bow
        setItem(19, Material.BOW, "§a§l🏹 Runaan's Bow", 
            "§7Triple shot",
            "§7• Ability: §aTriple Shot",
            "§7• Effect: §aShoot three arrows",
            "§7• Cooldown: §a10 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");

        // Mosquito Bow
        setItem(20, Material.SPIDER_EYE, "§c§l🦟 Mosquito Bow", 
            "§7Life drain shot",
            "§7• Ability: §aBlood Sucker",
            "§7• Effect: §aLife drain shot",
            "§7• Cooldown: §a12 seconds",
            "§7• Passive: §a+12% damage",
            "",
            "§eRight-click to activate!");

        // Bonemerang
        setItem(21, Material.BONE, "§f§l🦴 Bonemerang", 
            "§7Returning bone attack",
            "§7• Ability: §aBone Toss",
            "§7• Effect: §aReturning bone attack",
            "§7• Cooldown: §a8 seconds",
            "§7• Passive: §a+10% damage",
            "",
            "§eRight-click to activate!");

        // Spirit Bow
        setItem(22, Material.GHAST_TEAR, "§b§l👻 Spirit Bow", 
            "§7Spirit energy shot",
            "§7• Ability: §aSpirit Arrow",
            "§7• Effect: §aSpirit energy shot",
            "§7• Cooldown: §a15 seconds",
            "§7• Passive: §a+18% damage",
            "",
            "§eRight-click to activate!");

        // Juju Shortbow
        setItem(23, Material.BOW, "§6§l🏹 Juju Shortbow", 
            "§7Rapid fire mode",
            "§7• Ability: §aRapid Fire",
            "§7• Effect: §aRapid fire mode",
            "§7• Cooldown: §a10 seconds",
            "§7• Passive: §a+20% damage",
            "",
            "§eRight-click to activate!");

        // Thorn Bow
        setItem(24, Material.CACTUS, "§2§l🌵 Thorn Bow", 
            "§7Thorn arrow attack",
            "§7• Ability: §aThorn Shot",
            "§7• Effect: §aThorn arrow attack",
            "§7• Cooldown: §a12 seconds",
            "§7• Passive: §a+15% damage",
            "",
            "§eRight-click to activate!");

        // Last Breath
        setItem(25, Material.SKELETON_SKULL, "§8§l💀 Last Breath", 
            "§7Death magic attack",
            "§7• Ability: §aDeath's Embrace",
            "§7• Effect: §aDeath magic attack",
            "§7• Cooldown: §a30 seconds",
            "§7• Passive: §a+25% damage",
            "",
            "§eRight-click to activate!");
    }

    private void setupSpecialWeaponAbilities() {
        // Fire Veil Wand
        setItem(28, Material.BLAZE_ROD, "§6§l🔥 Fire Veil Wand", 
            "§7Create fire veil",
            "§7• Ability: §aFire Veil",
            "§7• Effect: §aCreate fire veil around you",
            "§7• Cooldown: §a20 seconds",
            "§7• Passive: §a+15% fire damage",
            "",
            "§eRight-click to activate!");

        // Spirit Scepter
        setItem(29, Material.STICK, "§b§l🧙 Spirit Scepter", 
            "§7Spirit magic attack",
            "§7• Ability: §aSpirit Blast",
            "§7• Effect: §aSpirit magic attack",
            "§7• Cooldown: §a22 seconds",
            "§7• Passive: §a+18% magic damage",
            "",
            "§eRight-click to activate!");

        // Frozen Scythe
        setItem(30, Material.ICE, "§b§l❄️ Frozen Scythe", 
            "§7Freeze attack",
            "§7• Ability: §aFrozen Strike",
            "§7• Effect: §aFreeze attack",
            "§7• Cooldown: §a16 seconds",
            "§7• Passive: §a+15% ice damage",
            "",
            "§eRight-click to activate!");

        // Ice Spray Wand
        setItem(31, Material.SNOWBALL, "§b§l❄️ Ice Spray Wand", 
            "§7Ice spray attack",
            "§7• Ability: §aIce Spray",
            "§7• Effect: §aIce spray attack",
            "§7• Cooldown: §a14 seconds",
            "§7• Passive: §a+12% ice damage",
            "",
            "§eRight-click to activate!");

        // Glacial Scepter
        setItem(32, Material.PACKED_ICE, "§b§l🧊 Glacial Scepter", 
            "§7Glacial effect",
            "§7• Ability: §aGlacial Storm",
            "§7• Effect: §aGlacial effect",
            "§7• Cooldown: §a25 seconds",
            "§7• Passive: §a+20% ice damage",
            "",
            "§eRight-click to activate!");

        // Bonzo Staff
        setItem(33, Material.SLIME_BALL, "§a§l🎈 Bonzo Staff", 
            "§7Balloon effect",
            "§7• Ability: §aBalloon Barrage",
            "§7• Effect: §aBalloon effect",
            "§7• Cooldown: §a18 seconds",
            "§7• Passive: §a+15% magic damage",
            "",
            "§eRight-click to activate!");

        // Scarf Studies
        setItem(34, Material.BOOK, "§c§l📚 Scarf Studies", 
            "§7Scarf magic",
            "§7• Ability: §aScarf Magic",
            "§7• Effect: §aScarf magic attack",
            "§7• Cooldown: §a20 seconds",
            "§7• Passive: §a+15% magic damage",
            "",
            "§eRight-click to activate!");
    }

    private void setupStatistics() {
        // Player Statistics
        WeaponAbilitySystem.PlayerWeaponAbilities playerAbilities = weaponAbilitySystem.getPlayerWeaponAbilities(player.getUniqueId());
        setItem(37, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your Weapon Ability usage:",
            "§7• Total abilities used: §a" + playerAbilities.getTotalAbilitiesUsed(),
            "§7• Active cooldowns: §a" + playerAbilities.getCooldowns().size(),
            "§7• Most used ability: §aAspect of the End",
            "§7• Average cooldown: §a18 seconds",
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(38, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global Weapon Ability usage:",
            "§7• Total abilities used: §a2,345,678",
            "§7• Most popular: §aAspect of the End",
            "§7• Average per player: §a23.5",
            "§7• Success rate: §a100%",
            "",
            "§eClick to view global stats!");

        // Leaderboard
        setItem(39, Material.GOLD_BLOCK, "§6§l🏆 Weapon Ability Leaderboard", 
            "§7Top Weapon Ability users:",
            "§7• #1 PlayerName: §a2,345 abilities",
            "§7• #2 PlayerName: §a2,234 abilities",
            "§7• #3 PlayerName: §a2,123 abilities",
            "§7• #4 PlayerName: §a2,012 abilities",
            "§7• #5 PlayerName: §a1,901 abilities",
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
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
