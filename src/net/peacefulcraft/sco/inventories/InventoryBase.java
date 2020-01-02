package net.peacefulcraft.sco.inventories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;

public abstract class InventoryBase<T extends InventoryBase>{

	private File invData;
	
	private Player observer;
		public Player getObserver() { return observer; }
	
	private Class<T> inventoryType;
		public abstract Class<T> getInventoryType();
		
	private Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(Player observer, Class<T> inventoryType){
		this.observer = observer;
		this.inventoryType = inventoryType;
		
		invData = new File(
			SwordCraftOnline.getPluginInstance().getDataFolder().getPath() 
			+ "/data/" + observer.getUniqueId() + "/" + inventoryType.getName() + ".inv" 
		); // plugins/SwordCraftOnline/data/[uuid]/[SwordSkillInventory].inv
		
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
	 * Object destructor
	 * Called when player leaves so we can remove any assocaited event listeners
	 */
	public abstract void destroy();
	
	/**
	 * Load serialized inventory from disk
	 * Create actual MC inventory that we can .open
	 * @throws FileNotFoundException 
	 */
	public void loadInventory() throws FileNotFoundException {
		if(invData.exists()) {
			try {
				FileReader fr = new FileReader(invData);
				
				long size = invData.length();
				if(size > Integer.MAX_VALUE) {
					// char array can only be of length int so if the file is too big it will not load properly
					fr.close();
					throw new RuntimeException("Datafile " + invData + " exceeds maximium readin size. Aborint file load.");
				}
				
				int length = (int) invData.length();
				char[] data = new char[length];
				
				fr.read(data);
				fr.close();
				inventory = InventorySerializer.StringToInventory( String.valueOf(data) );
				
			} catch (RuntimeException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			// Use InventoryBase.createNewInventory( size ) to create a new inventory
			throw new FileNotFoundException("Inventory " + invData + " does not exist.");
		}
	}
	
	/**
	 * Create a new inventory
	 * @param type: <T extends InventoryBase>
	 * @param size: # of slots
	 * @param u: UUID of player
	 * @throws FileNotFoundException 
	 */
	public static void createNewInventory(Class inventoryType, int size, Player p) throws FileNotFoundException {
		Inventory inventory = Bukkit.getServer().createInventory(null, size, p.getDisplayName() + "'s " + inventoryType.getName());
		saveInventory(inventory, inventoryType, p.getUniqueId());
	}
	
	public static boolean InventoryExists(UUID uuid, Class inventoryType) {
		File dataLoc = new File(
			SwordCraftOnline.getPluginInstance().getDataFolder().getPath() 
			+ "/data/" + uuid + "/" + inventoryType.getName() + ".inv" 
		); // plugins/SwordCraftOnline/data/[uuid]/[SwordSkillInventory].inv
		
		return dataLoc.exists();
	}
	
	/**
	 * proxy the static function for internal use because it's less typing
	 * @throws FileNotFoundException 
	 * @throws FileAlreadyExistsException
	 */
	public void saveInventory() throws FileNotFoundException {
		InventoryBase.saveInventory(inventory, inventoryType, observer.getUniqueId());
	}
		
		/**
		 * Save serialized inventory to disk
		 * @throws FileNotFoundException 
		 */
		private static void saveInventory(Inventory inventory, Class inventoryType, UUID u) throws FileNotFoundException {
			String serializedInventory = InventorySerializer.InventoryToString(inventory);
			File invData = new File(
					SwordCraftOnline.getPluginInstance().getDataFolder().getPath() 
					+ "/data/" + u + "/" + inventoryType.getName() + ".inv" 
				); // plugins/SwordCraftOnline/data/[uuid]/[SwordSkillInventory].inv
			
			PrintWriter pw = new PrintWriter(invData);
			pw.write(serializedInventory);
			pw.close();
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
		try {
			this.saveInventory();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
