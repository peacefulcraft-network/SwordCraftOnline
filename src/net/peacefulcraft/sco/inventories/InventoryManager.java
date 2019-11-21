package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;

public class InventoryManager {

	private SCOPlayerDataManager data;
	private HashMap<Class, InventoryBase> invCache;	
	
		
	public InventoryManager(SCOPlayerDataManager data) {
		this.data = data;
	}
	
	/**
	 * @param invType: Type of inventory to get
	 * @return: The Inventory
	 */
	public <T extends InventoryBase> T getInventory(Class<T> type) {
		if(!invCache.containsKey(type)) {
			//Load inventory into cache if it's not already there
			try{
				// LOAD THE INVENTORY
				//invCache.put(type, persistentStore.loadInventory(s.getUUID(), type));
			}catch(RuntimeException ex) {
				//DO something with this failure;
			}
		}
		
		return (T) invCache.get(type);
	}
}
