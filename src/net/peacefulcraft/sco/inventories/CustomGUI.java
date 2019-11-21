package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
 
    public CustomGUI addButton(Row row, int position, ItemStack item, String name, String lore, Boolean remove, Boolean create) {
        if(create == true) {
        	items[row.getRow() * 9 + position] = item;
        } else {
        	items[row.getRow() * 9 + position] = getItem(item, name, lore, remove);
        }
        return this;
    }
    
    /**
     * Fills row in inventory from right to left with item.
     * @param row
     * @param amount
     * @param item
     * @param name
     * @param lore
     * @return
     */
    public CustomGUI fillRow(Row row, int amount, ItemStack item) {
    	for(int i = 8-amount; i<=8; i++) {
    		items[row.getRow() * 9 + i] = item;
    	}
    	return this;
    }
    
    /**
     * Takes row argument and returns empty row
     * @param row
     * @return row
     */
    public CustomGUI emptyRow(Row row) {
    	for(int i = 0; i<=8; i++) {
    		items[row.getRow() * 9 + i] = new ItemStack(Material.AIR);
    	}
    	return this;
    }
 
    public Row getRowFromSlot(int slot) {
        return new Row(slot / 9, items);
    }
 
    public Row getRow(int row) {
        return new Row(row, items);
    }
 
    public interface onClick {
        public abstract boolean click(Player clicker, CustomGUI menu, Row row, int slot, ItemStack item);
    }
 
    public class Row {
        private ItemStack[] rowItems = new ItemStack[9];
        int row;
 
        public Row(int row, ItemStack[] items) {
            this.row = row;
            int j = 0;
            for (int i = (row * 9); i < (row * 9) + 9; i++) {
                rowItems[j] = items[i];
                j++;
            }
        }
 
        public ItemStack[] getRowItems() {
            return rowItems;
        }
 
        public ItemStack getRowItem(int item) {
            return rowItems[item] == null ? new ItemStack(Material.AIR) : rowItems[item];
        }
 
        public int getRow() {
            return row;
        }
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
