package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CustomGUI implements Listener
{
	private String name;
    private int size;
    private onClick click;
    List<String> viewing = new ArrayList<String>();
 
    private ItemStack[] items;
 
    public CustomGUI(String name, int size) {
        this.name = name;
        this.size = size * 9;
        items = new ItemStack[this.size];
        //this.click = click;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugins()[0]);
    }
 
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        for (Player p : this.getViewers())
            close(p);
    }
 
    public CustomGUI open(Player p) {
        p.openInventory(getInventory(p));
        viewing.add(p.getName());
        return this;
    }
 
    private Inventory getInventory(Player p) {
        Inventory inv = Bukkit.createInventory(p, size, name);
        for (int i = 0; i < items.length; i++)
            if (items[i] != null)
                inv.setItem(i, items[i]);
        return inv;
    }
 
    public CustomGUI close(Player p) {
        if (p.getOpenInventory().getTitle().equals(name))
            p.closeInventory();
        return this;
    }
 
    public List<Player> getViewers() {
        List<Player> viewers = new ArrayList<Player>();
        for (String s : viewing)
            viewers.add(Bukkit.getPlayer(s));
        return viewers;
    }
 
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (viewing.contains(event.getPlayer().getName()))
            viewing.remove(event.getPlayer().getName());
    }
 
    public CustomGUI addButton(int row, int position, ItemStack item, String name, String lore, Boolean remove, Boolean create) {
        if(create == true) {
        	items[row * 9 + position] = item;
        } else {
        	items[row * 9 + position] = getItem(item, name, lore, remove);
        }
        return this;
    }
    
 
    public interface onClick {
        public abstract boolean click(Player clicker, CustomGUI menu, int row, int slot, ItemStack item);
    }
 

 
    private ItemStack getItem(ItemStack item, String name, String lore, Boolean remove) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        if(remove == true) {
        	im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(im);
        return item;
    }
}
