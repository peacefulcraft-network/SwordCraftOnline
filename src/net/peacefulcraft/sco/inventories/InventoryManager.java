package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.storage.InventoryDataSaver;

public class InventoryManager {

	private SCOPlayer s;
	private static InventoryDataSaver persistentStore = new InventoryDataSaver();
	private HashMap<Class, InventoryBase> invCache;	
	
		/**
		 * @param invType: Type of inventory to get
		 * @return: The Inventory
		 */
		public <T extends InventoryBase> T getInventory(Class<T> type) {
			if(!invCache.containsKey(type)) {
				//Load inventory into cache if it's not already there
				try{
					invCache.put(type, persistentStore.loadInventory(s.getUUID(), type));
				}catch(RuntimeException ex) {
					//DO something with this failure;
				}
			}
			
			return (T) invCache.get(type);
		}
	
		
	public InventoryManager(SCOPlayer s) {
		this.s = s;
	}
	
}
