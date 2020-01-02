package net.peacefulcraft.sco.inventories;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;

public class InventoryManager {

	private SCOPlayerDataManager data;
	private HashMap<Class, InventoryBase> invCache;	
	
		
	public InventoryManager(SCOPlayerDataManager data) {
		this.data = data;
		invCache = new HashMap<Class, InventoryBase>();
	}
	
	/**
	 * Use black magic to convert invType into an actual inventory for the given player.
	 * @param invType: Type of inventory to get
	 * @return: The Inventory
	 * @throws InventoryNotFoundException if the inventory doesn't exist 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public <T extends InventoryBase> T getInventory(Class<T> type){
		if(!invCache.containsKey(type)) {
			//Load inventory into cache if it's not already there
			try{
				/*
				 * Use reflection to get the constructor for the inventory type passed.
				 * Instantiate a new object of an existing inventory of type T
				 */
				Player p = data.getSCOPlayer().getPlayer();
				T inventory = (T) type.getConstructors()[0].newInstance( p );
				inventory.loadInventory();
				invCache.put(type, inventory);
			
			/*
			 * FileNotFound is thrown when the Inventory doesn't exist and we need to create it.
			 * 
			 * Attempts to create a new, empty inventory of the given type with slot size 9
			 */
			}catch(FileNotFoundException ex) {
				
				try {
					// Attempt to create the inventory new
					InventoryBase.createNewInventory(type, 9, data.getSCOPlayer().getPlayer());
					
					// Try to instantiate a new instance of the inventory ( see above )
					Player p = data.getSCOPlayer().getPlayer();
					T inventory = (T) type.getConstructors()[0].newInstance( p );
					inventory.loadInventory();
					inventory.initializeDefaultLoadout();
					inventory.saveInventory();
					invCache.put(type, inventory);
					
				} catch (FileNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
					// We went even deeper and it still didn't work
					e.printStackTrace();
				}
			
			}catch(RuntimeException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
				// We're in too deep at this point
				ex.printStackTrace();
			}
		}
		
		return (T) invCache.get(type);
	}
	
	public void unregisterInventories() {
		Set<Class> inventories = invCache.keySet();
		for(Class inventory : inventories) {
			try{ invCache.get(inventory).closeInventory(); }
			catch(Exception e) {
				e.printStackTrace();
			}
			invCache.get(inventory).destroy();
		}
	}
}
