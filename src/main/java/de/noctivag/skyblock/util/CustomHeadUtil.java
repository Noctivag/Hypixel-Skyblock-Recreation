package de.noctivag.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Base64;
import java.util.UUID;

/**
 * Utility für Custom Player Heads (Custom Skins für Items)
 */
public class CustomHeadUtil {
    /**
     * Erzeugt einen Custom-Head mit Textur-URL (Base64-String)
     * @param name Anzeigename
     * @param base64 Base64-Textur-String
     * @return ItemStack mit Custom-Head
     */
    public static ItemStack getCustomHead(String name, String base64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(name);
        // Setze Custom-Textur
        try {
            java.lang.reflect.Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            java.util.UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes());
            com.mojang.authlib.GameProfile profile = new com.mojang.authlib.GameProfile(uuid, null);
            profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", base64));
            profileField.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }
}
