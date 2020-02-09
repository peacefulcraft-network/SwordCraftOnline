package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractItemStack;

public class BukkitItemStack implements AbstractItemStack, Cloneable {
    private final ItemStack item;
    
    public BukkitItemStack(String type) {
        this.item = new ItemStack(Material.matchMaterial(type.toUpperCase()));
    }
    
    public BukkitItemStack(Material m) {
        this.item = new ItemStack(m);
    }
    
    public BukkitItemStack(ItemStack stack) {
        this.item = stack;
    }
    
    public BukkitItemStack durability(int d) {
        checkItemMeta();
        if (this.item.getItemMeta() instanceof Damageable) {
            ItemMeta meta = this.item.getItemMeta();
            ((Damageable)meta).setDamage(d);
            this.item.setItemMeta(meta);
        }  
        return this;
    }
    
    @Deprecated
    public BukkitItemStack data(int d) {
        checkItemMeta();
        if (this.item.getItemMeta() instanceof Damageable) {
            ItemMeta meta = this.item.getItemMeta();
            ((Damageable)meta).setDamage(d);
            this.item.setItemMeta(meta);
        }  
        return this;
    }
    
    public BukkitItemStack amount(int a) {
        this.item.setAmount(a);
        return this;
    }
    
    @SuppressWarnings("Deprecated")
    public BukkitItemStack colorData(DyeColor dc) {
        //this.item.setDurability((short)dc.getWoolData());
        if(this.item.getItemMeta() instanceof Damageable) {
          ItemMeta meta = this.item.getItemMeta();
          ((Damageable)meta).setDamage((short)dc.getWoolData());
          this.item.setItemMeta(meta);
        }
        return this;
    }
    
    public BukkitItemStack color(String color) {
      try {
        if (this.item.getType().equals(Material.LEATHER_CHESTPLATE) || this.item.getType().equals(Material.LEATHER_BOOTS) || this.item.getType().equals(Material.LEATHER_LEGGINGS) || this.item.getType().equals(Material.LEATHER_HELMET)) {
          ItemMeta im = this.item.getItemMeta();
          LeatherArmorMeta la = (LeatherArmorMeta)im;
          if (color.contains(",")) {
            String[] rgb = color.split(",");
            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);
            la.setColor(Color.fromRGB(r, g, b));
          } else {
            DyeColor dColor = DyeColor.valueOf(color);
            la.setColor(dColor.getColor());
          } 
          this.item.setItemMeta((ItemMeta)la);
        } 
      } catch (Exception ex) {
        //Log error MythicMobs.inst().handleException(ex);
      } 
      return this;
    }
    
    public BukkitItemStack display(String d) {
      checkItemMeta();
      ItemMeta im = this.item.getItemMeta();
      im.setDisplayName(d);
      this.item.setItemMeta(im);
      return this;
    }
    
    public BukkitItemStack lore(String[] lore) {
      checkItemMeta();
      ItemMeta im = this.item.getItemMeta();
      List<String> lores = new ArrayList<>();
      for (String l : lore)
        lores.add(l); 
      im.setLore(lores);
      this.item.setItemMeta(im);
      return this;
    }
    
    public BukkitItemStack lore(List<String> lores) {
      checkItemMeta();
      ItemMeta im = this.item.getItemMeta();
      im.setLore(lores);
      this.item.setItemMeta(im);
      return this;
    }
    
    public BukkitItemStack enchant(Enchantment ench, int level) {
      this.item.addUnsafeEnchantment(ench, level);
      return this;
    }
    
    /*
    public BukkitItemStack setSkullTexture(String texture) {
      UUID skinUUID;
      checkItemMeta();
      if (!(this.item.getItemMeta() instanceof SkullMeta))
        return this; 
      SkullMeta im = (SkullMeta)this.item.getItemMeta();
      if (im.getDisplayName() != null) {
        skinUUID = UUIDUtil.getUUIDFromString(im.getDisplayName());
      } else {
        skinUUID = UUID.randomUUID();
      } 
      GameProfile profile = new GameProfile(skinUUID, null);
      profile.getProperties().put("textures", new Property("textures", texture));
      Field profileField = null;
      try {
        profileField = im.getClass().getDeclaredField("profile");
      } catch (NoSuchFieldException|SecurityException e) {
        e.printStackTrace();
      } 
      profileField.setAccessible(true);
      try {
        profileField.set(im, profile);
      } catch (IllegalArgumentException|IllegalAccessException e) {
        e.printStackTrace();
      } 
      this.item.setItemMeta((ItemMeta)im);
      return this;
    }
    */
    
    public int getAmount() {
      return this.item.getAmount();
    }
    /*
    public CompoundTag getNBT() {
      return MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(this.item);
    }
    */
    
    public ItemStack build() {
      return this.item.clone();
    }
    
    private void checkItemMeta() {
      if (!this.item.hasItemMeta())
        this.item.setItemMeta(Bukkit.getItemFactory().getItemMeta(this.item.getType())); 
    }
    
    public BukkitItemStack clone() {
      return new BukkitItemStack(this.item.clone());
    }
    
    public String toString() {
      return "BukkitItemStack{" + this.item.toString() + "}";
    }
}