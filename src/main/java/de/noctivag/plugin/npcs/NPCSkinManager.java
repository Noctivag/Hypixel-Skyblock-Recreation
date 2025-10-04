package de.noctivag.plugin.npcs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * NPC Skin Manager - Manages custom skins for Player-based NPCs
 */
public class NPCSkinManager {
    private final Plugin plugin;
    private final Map<String, NPCSkin> npcSkins = new HashMap<>();
    
    public NPCSkinManager(Plugin plugin) {
        this.plugin = plugin;
        initializeDefaultSkins();
    }
    
    private void initializeDefaultSkins() {
        // Shop NPC Skin
        npcSkins.put("shop", new NPCSkin(
            "ShopKeeper",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "shop_signature",
            "§aShop Keeper",
            "§7A friendly merchant ready to help you with your shopping needs!"
        ));
        
        // Quest NPC Skin
        npcSkins.put("quest", new NPCSkin(
            "QuestGiver",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "quest_signature",
            "§bQuest Master",
            "§7An experienced adventurer with quests for brave souls!"
        ));
        
        // Info NPC Skin
        npcSkins.put("info", new NPCSkin(
            "InfoGuide",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "info_signature",
            "§eInformation Guide",
            "§7A knowledgeable guide ready to answer your questions!"
        ));
        
        // Warp NPC Skin
        npcSkins.put("warp", new NPCSkin(
            "WarpMaster",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "warp_signature",
            "§dWarp Master",
            "§7A mystical being that can transport you anywhere!"
        ));
        
        // Bank NPC Skin
        npcSkins.put("bank", new NPCSkin(
            "Banker",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "bank_signature",
            "§6Bank Manager",
            "§7A professional banker ready to help with your finances!"
        ));
        
        // Auction NPC Skin
        npcSkins.put("auction", new NPCSkin(
            "Auctioneer",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "auction_signature",
            "§cAuction Master",
            "§7A skilled auctioneer ready to help you trade items!"
        ));
        
        // Guild NPC Skin
        npcSkins.put("guild", new NPCSkin(
            "GuildMaster",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "guild_signature",
            "§5Guild Leader",
            "§7A powerful guild master ready to help with guild affairs!"
        ));
        
        // Pet NPC Skin
        npcSkins.put("pet", new NPCSkin(
            "PetKeeper",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "pet_signature",
            "§dPet Caretaker",
            "§7A loving pet keeper ready to help with your companions!"
        ));
        
        // Cosmetic NPC Skin
        npcSkins.put("cosmetic", new NPCSkin(
            "CosmeticDesigner",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "cosmetic_signature",
            "§eCosmetic Artist",
            "§7A creative artist ready to help you customize your appearance!"
        ));
        
        // Admin NPC Skin
        npcSkins.put("admin", new NPCSkin(
            "AdminHelper",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5YjQ5In19fQ==",
            "admin_signature",
            "§4Admin Assistant",
            "§7A helpful admin ready to assist with server management!"
        ));
    }
    
    public NPCSkin getSkin(String npcType) {
        return npcSkins.get(npcType.toLowerCase());
    }
    
    public void setSkin(String npcType, NPCSkin skin) {
        npcSkins.put(npcType.toLowerCase(), skin);
    }
    
    public ItemStack createSkullItem(String npcType) {
        NPCSkin skin = getSkin(npcType);
        if (skin == null) return null;
        
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(skin.getDisplayName());
            meta.setLore(java.util.Arrays.asList(skin.getDescription()));
            // Note: In a real implementation, you would set the skin texture here
            // This requires additional libraries or custom packet manipulation
            skull.setItemMeta(meta);
        }
        
        return skull;
    }
    
    public void applySkinToPlayer(Player player, String npcType) {
        NPCSkin skin = getSkin(npcType);
        if (skin == null) return;
        
        // Apply skin to player
        // Note: This is a simplified version - in a real implementation,
        // you would use libraries like ProtocolLib or custom packet manipulation
        // to change the player's skin texture
        
        // Use modern Component API for display names
        player.displayName(net.kyori.adventure.text.Component.text(skin.getDisplayName()));
        player.playerListName(net.kyori.adventure.text.Component.text(skin.getDisplayName()));
    }
    
    public static class NPCSkin {
        private final String name;
        private final String texture;
        private final String signature;
        private final String displayName;
        private final String description;
        
        public NPCSkin(String name, String texture, String signature, String displayName, String description) {
            this.name = name;
            this.texture = texture;
            this.signature = signature;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getTexture() { return texture; }
        public String getSignature() { return signature; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}
