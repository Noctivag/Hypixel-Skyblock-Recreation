package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.events.UltimateEventSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;

public class UltimateEventGUI extends AnimatedGUI {
    private final UltimateEventSystem eventSystem;
    private String currentCategory = "all";

    public UltimateEventGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, 54, "§c§l⚔️ ULTIMATE EVENTS ⚔️");
        this.eventSystem = null; // UltimateEventSystem not implemented yet
    }

    @Override
    public void setupItems() {
        // Header
        setAnimatedItem(4, Material.DRAGON_HEAD, "§c§l⚔️ ULTIMATE EVENTS ⚔️", 
            AnimationType.FIRE,
            "§7Kämpfe gegen mächtige Bosses",
            "§7• 15 verschiedene Boss-Typen",
            "§7• Automatische Event-Scheduler",
            "§7• Einzigartige Belohnungen",
            "§7• Arena-System",
            "",
            "§eAktive Events: §a" + eventSystem.getActiveEvents().size());

        // Category buttons
        setAnimatedItem(10, Material.DRAGON_HEAD, "§5§l🐉 DRAGON BOSSES", 
            AnimationType.FIRE,
            "§7Drachen-Bosses",
            "§7• Ender Dragon",
            "§7• Dragon King",
            "§7• Cosmic Guardian",
            "§7• Hydra",
            "§7• Clockwork Dragon",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(11, Material.WITHER_SKELETON_SKULL, "§8§l💀 UNDEAD BOSSES", 
            AnimationType.GLOW,
            "§7Untote Bosses",
            "§7• Wither",
            "§7• Shadow Lord",
            "§7• Void Master",
            "§7• Cerberus",
            "§7• Lich King",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(12, Material.BLAZE_POWDER, "§c§l🔥 ELEMENTAL BOSSES", 
            AnimationType.FIRE,
            "§7Elementar-Bosses",
            "§7• Blaze King",
            "§7• Fire Emperor",
            "§7• Ice Queen",
            "§7• Storm King",
            "§7• Phoenix King",
            "§7• Earth Titan",
            "§7• Wind Spirit",
            "§7• Water Serpent",
            "§7• Lava Golem",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(13, Material.ZOMBIE_HEAD, "§2§l👹 MONSTER BOSSES", 
            AnimationType.PULSE,
            "§7Monster-Bosses",
            "§7• Titan",
            "§7• Elder Guardian",
            "§7• Ravager",
            "§7• Phantom King",
            "§7• Enderman Lord",
            "§7• Minotaur King",
            "§7• Kraken Lord",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(14, Material.NETHER_STAR, "§d§l⭐ ALLE EVENTS", 
            AnimationType.RAINBOW,
            "§7Alle verfügbaren Events",
            "§7• 35 verschiedene Boss-Typen",
            "§7• 6 verschiedene Kategorien",
            "§7• Verschiedene Schwierigkeitsgrade",
            "§7• Einzigartige Belohnungen",
            "",
            "§eKlicke zum Anzeigen");

        // NEW CATEGORIES
        setAnimatedItem(15, Material.BLACK_WOOL, "§8§l🌑 DARK BOSSES", 
            AnimationType.GLOW,
            "§7Dunkle Bosses",
            "§7• Shadow Beast",
            "§7• Vampire Lord",
            "§7• Demon Prince",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(16, Material.ELYTRA, "§f§l👼 CELESTIAL BOSSES", 
            AnimationType.SPARKLE,
            "§7Himmlische Bosses",
            "§7• Angel Archon",
            "§7• Star Weaver",
            "§7• Moon Goddess",
            "§7• Sun Emperor",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(17, Material.IRON_BLOCK, "§7§l⚙️ MECHANICAL BOSSES", 
            AnimationType.PULSE,
            "§7Mechanische Bosses",
            "§7• Steam Giant",
            "§7• Crystal Guardian",
            "§7• Gear Titan",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(18, Material.OAK_LOG, "§a§l🌿 NATURE BOSSES", 
            AnimationType.BOUNCE,
            "§7Natur-Bosses",
            "§7• Forest Guardian",
            "§7• Mountain Spirit",
            "§7• Desert Pharaoh",
            "§7• Ocean Leviathan",
            "",
            "§eKlicke zum Anzeigen");

        // Show events based on current category
        showEvents();

        // Event info
        setAnimatedItem(28, Material.CLOCK, "§e§l⏰ EVENT ZEITPLAN", 
            AnimationType.SPARKLE,
            "§7Zeigt kommende Events",
            "§7• Nächste Events",
            "§7• Event-Timer",
            "§7• Event-Historie",
            "",
            "§eKlicke zum Öffnen");

        setAnimatedItem(30, Material.PAPER, "§b§l📊 EVENT STATISTIKEN", 
            AnimationType.GLOW,
            "§7Deine Event-Statistiken",
            "§7• Teilgenommene Events",
            "§7• Gewonnene Events",
            "§7• Belohnungen erhalten",
            "",
            "§eKlicke zum Anzeigen");

        setAnimatedItem(32, Material.CHEST, "§6§l🎁 EVENT BELOHNUNGEN", 
            AnimationType.SPARKLE,
            "§7Event-Belohnungen",
            "§7• Geld-Belohnungen",
            "§7• Einzigartige Items",
            "§7• Spezielle Belohnungen",
            "",
            "§eKlicke zum Anzeigen");

        // Navigation
        setItem(45, Material.ARROW, "§7§l⬅️ ZURÜCK", "§7Zurück zum Hauptmenü");
        setItem(49, Material.BARRIER, "§c§l❌ SCHLIESSEN", "§7Schließe das Menü");
    }

    private void showEvents() {
        Map<String, UltimateEventSystem.EventArena> arenas = eventSystem.getArenas();
        int slot = 19;
        
        for (Map.Entry<String, UltimateEventSystem.EventArena> entry : arenas.entrySet()) {
            if (slot >= 27) break;
            
            String eventId = entry.getKey();
            UltimateEventSystem.EventArena arena = entry.getValue();
            
            // Check if event is active
            boolean isActive = false; // eventSystem not implemented yet
            boolean canJoin = true; // eventSystem not implemented yet
            
            Material icon = arena.getIcon();
            String name = arena.getDisplayName();
            AnimationType animation = isActive ? AnimationType.FIRE : AnimationType.GLOW;
            
            if (isActive) {
                name = "§a§l🔥 " + name + " §8(§aAKTIV§8)";
            } else {
                name = "§7§l" + name;
            }
            
            List<String> lore = arena.getDescription();
            lore.add("");
            lore.add("§7Status: " + (isActive ? "§a§lAKTIV" : "§7Inaktiv"));
            lore.add("§7Kann beitreten: " + (canJoin ? "§a§lJA" : "§c§lNEIN"));
            lore.add("");
            lore.add("§eKlicke zum Beitreten");
            
            setAnimatedItem(slot, icon, name, animation, lore.toArray(new String[0]));
            slot++;
        }
        
        // Fill remaining slots with glass panes
        while (slot < 27) {
            setItem(slot, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            slot++;
        }
    }

    public void setCategory(String category) {
        this.currentCategory = category;
        setupItems();
    }
}
