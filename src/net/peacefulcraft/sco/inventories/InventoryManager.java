package net.peacefulcraft.sco.inventories;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;

public class InventoryManager {

	private SCOPlayerDataManager data;
	private HashMap<Class, InventoryBase> invCache;	
	
		
	public InventoryManager(SCOPlayerDataManager data) {
		this.data = data;
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
				 * Instantiate a new object of that inventory type
				 */
				T inventory = (T) type.getConstructors()[0].newInstance(
									data.getSCOPlayer().getPlayer(),
									type
								);
				
				/* 
				 * All InventoryBase children can throw FileNotFoundExceptions because InventoryBase constructor does.
				 * Java doesn't know this because reflection so we need to trick it into 
				 * letting us include the exception in the catch block.
				 */
				if(false) { throw new FileNotFoundException(); }
			
			/*
			 * FileNotFound is thrown when the Inventory doesn't exist and we need to create it.
			 * 
			 * Attempts to create a new, empty inventory of the given type with slot size 9
			 */
			}catch(FileNotFoundException ex) {
				
				try {
					// Attempt to create the inventory new
					InventoryBase.createNewInventory(type, 9, data.getSCOPlayer().getPlayer().getUniqueId());
					
					// Try to instantiate the inventory ( see above )
					T inventory = (T) type.getConstructors()[0].newInstance(
										data.getSCOPlayer().getPlayer(),
										type
									);
				} catch (FileNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
					// Unable to write to disk
					e.printStackTrace();
				}
			
			}catch(RuntimeException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
				// We're in too deep at this point
			}
		}
		
		return (T) invCache.get(type);
	}
}
