package net.peacefulcraft.sco.inventory;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.tasks.SavePlayerInventory;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.item.SkillIdentifier;

public abstract class InventoryBase{
	
	private Player observer;
		public Player getObserver() { return observer; }
	
	private InventoryType type;
		public InventoryType getType() { return type; }
		
	private Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(Player observer, InventoryType inventoryType){
		this.observer = observer;
		this.type = inventoryType;
		
	}		
	
	/**
	 * Default configuration for inventory
	 */
	public abstract void initializeDefaultLoadout();
	
	/**
	 * Create a new inventory of the desired size, keeping || removing items
	 * accordingly
	 */
	public abstract void resizeInventory(int size);
	
	/**
	 * Examines all items currently in the player's inventory and creates a list of
	 * Sword Skill identifiers that indicate which sword skills the player has.
	 * @return
	 */
	public ArrayList<SkillIdentifier> generateSkillIdentifiers(){
		ArrayList<SkillIdentifier> identifiers = new ArrayList<SkillIdentifier>();
		for(ItemStack item : inventory) {
			NBTItem nbtItem = new NBTItem(item);
			
			String skillName = item.getItemMeta().getDisplayName();
			ItemTier tier = ItemTier.valueOf(nbtItem.getString("tier"));
			int skilLevel = nbtItem.getInteger("skill_level");
			
			SkillIdentifier identifier = new SkillIdentifier(skillName, skilLevel, tier);
			identifiers.add(identifier);
		}
		return identifiers;
	}
	
	/**
	 * Generates SkillIdentifiers based on what is in the player's inventory and
	 * schedules a database task to save the inventory state
	 */
	public void saveInventory() {
		SCOPlayer s = GameManager.findSCOPlayer(observer);
		(new SavePlayerInventory(s, type, generateSkillIdentifiers())).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());
	}
			
	/**
	 * Open inventory for player
	 */
	public void openInventory() {
		observer.openInventory(inventory);
	}
	
	/**
	 * close inventory for player
	 */
	public void closeInventory() {
		observer.closeInventory();
		this.saveInventory();
	};
	
	public void fillRow(int row, int amount, ItemStack item) {
    	for(int i = 8-amount; i<=8; i++) {
    		inventory.setItem( row * 9 + i, item);
    	}
    }
    
    public void emptyRow(int row) {
    	fillRow(row, 1, new ItemStack(Material.AIR));
    }
    
    public void addButton(int row, int col, ItemStack item) {
    	inventory.setItem(row * 9 + col, item);
    }
    
    public void addButton(int row, int col, ItemStack item, String name, String lore, Boolean hidden) {
    	inventory.setItem(row * 9 + col, createButtomItem(item, name, lore, hidden)); 
    }
    
    private ItemStack createButtomItem(ItemStack item, String name, String lore, Boolean hidden) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        if(hidden == true) {
        	im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(im);
        return item;
    }
	
}
