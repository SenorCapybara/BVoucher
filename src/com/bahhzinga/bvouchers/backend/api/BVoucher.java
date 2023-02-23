package com.bahhzinga.bvouchers.backend.api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import com.bahhzinga.bvouchers.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BVoucher {

    private UUID owner;
    private String displayname;
    private List<String> lore;
    private int customModelData;
    private String action;
    private int uses;
    private String name;
    private static NamespacedKey key_owner = new NamespacedKey(Main.getPlugin(Main.class), "uuid");
    private static NamespacedKey key_uses = new NamespacedKey(Main.getPlugin(Main.class), "uses");
    private static NamespacedKey key_actions = new NamespacedKey(Main.getPlugin(Main.class), "action");
    private static NamespacedKey uniquekey = new NamespacedKey(Main.getPlugin(Main.class), "uniquekey");
    public static NamespacedKey key_name = new NamespacedKey(Main.getPlugin(Main.class), "name");

    public BVoucher (String name, UUID owner, String displayname, List<String> lore, int customModelData, String action, int uses){

        this.name = name;
        this.owner = owner;
        this.displayname = displayname;
        this.lore = lore;
        this.customModelData = customModelData;
        this.action = action;
        this.uses = uses;

    }

    public ItemStack get(){

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        // Set display name of item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.displayname));

        // Set custom model data, supporting custom textures :D
        meta.setCustomModelData(this.customModelData);

        // Set item owner, uses, action and unique id using Persistent Data
        meta.getPersistentDataContainer().set(key_owner, PersistentDataType.STRING, this.owner.toString());
        meta.getPersistentDataContainer().set(key_uses, PersistentDataType.INTEGER, this.uses);
        meta.getPersistentDataContainer().set(key_actions, PersistentDataType.STRING, this.action);
        meta.getPersistentDataContainer().set(uniquekey, PersistentDataType.STRING, UUID.randomUUID().toString());
        meta.getPersistentDataContainer().set(key_name, PersistentDataType.STRING, this.name);

        item.setItemMeta(meta);

        // Set lore
        ArrayList<String> itemLore = new ArrayList<String>();

        for (String content : this.lore){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', content));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public UUID getOwner(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return UUID.fromString(meta.getPersistentDataContainer().get(key_owner, PersistentDataType.STRING));
    }

    public int getUses(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(key_uses, PersistentDataType.INTEGER);
    }

    public String getAction(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(key_actions, PersistentDataType.STRING);
    }

    public String getName(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(key_name, PersistentDataType.STRING);
    }

    public void setOwner(ItemStack item, UUID uuid){
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key_owner, PersistentDataType.STRING, uuid.toString());
        item.setItemMeta(meta);
    }
    public void setAction(ItemStack item, String action){
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key_owner, PersistentDataType.STRING, action);
        item.setItemMeta(meta);
    }

    public void setName(ItemStack item, String name){
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key_name, PersistentDataType.STRING, name);
        item.setItemMeta(meta);
    }

    public void setUses(ItemStack item, int uses){
        ItemMeta meta = item.getItemMeta();
        int current = meta.getPersistentDataContainer().get(key_uses, PersistentDataType.INTEGER);
        meta.getPersistentDataContainer().set(key_uses, PersistentDataType.INTEGER, current-uses);
        item.setItemMeta(meta);

    }

}
