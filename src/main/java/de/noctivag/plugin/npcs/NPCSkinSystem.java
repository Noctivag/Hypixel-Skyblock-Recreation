package de.noctivag.plugin.npcs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
// import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
// import java.util.UUID;

/**
 * NPC Skin System - Custom skins for NPCs
 */
public class NPCSkinSystem {
    private final Plugin plugin;
    private final Map<String, String> npcSkins = new HashMap<>();
    
    public NPCSkinSystem(Plugin plugin) {
        this.plugin = plugin;
        initializeDefaultSkins();
    }
    
    private void initializeDefaultSkins() {
        // Hypixel-style NPC skins
        npcSkins.put("shop", "MHF_Villager");
        npcSkins.put("quest", "MHF_Question");
        npcSkins.put("info", "MHF_Exclamation");
        npcSkins.put("warp", "MHF_ArrowUp");
        npcSkins.put("bank", "MHF_Chest");
        npcSkins.put("auction", "MHF_Gold");
        npcSkins.put("guild", "MHF_Banner");
        npcSkins.put("pet", "MHF_Wolf");
        npcSkins.put("cosmetic", "MHF_Star");
        npcSkins.put("admin", "MHF_Steve");
    }
    
    public void applySkin(Villager villager, String npcType) {
        String skinName = npcSkins.get(npcType);
        if (skinName != null) {
            // Apply custom skin logic here
            // This would require a custom entity or skin plugin
        }
    }
    
    public ItemStack createSkullItem(String skinName, String displayName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(displayName);
            // Set skin texture
            // meta.setOwner(skinName);
            skull.setItemMeta(meta);
        }
        
        return skull;
    }
}
