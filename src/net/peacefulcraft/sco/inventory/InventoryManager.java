package net.peacefulcraft.sco.inventory;

import java.util.HashMap;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;

public class InventoryManager {

	private SCOPlayerDataManager data;
	private HashMap<InventoryType, InventoryBase> invCache;	
	
		
	public InventoryManager(SCOPlayerDataManager data) {
		this.data = data;
		invCache = new HashMap<InventoryType, InventoryBase>();
	}
	
	public void registerInventory(InventoryBase inv) {
		invCache.put(inv.getType(), inv);
	}
	
	public void unregisterInventory(InventoryType invType) {
		invCache.remove(invType);
	}
	
	public void unregisterInventory(InventoryBase inv) {
		InventoryBase posInv = invCache.get(inv.getType());
		if(posInv == null) { return; }
		
		if(posInv == inv) { invCache.remove(posInv.getType()); }
	}

	public InventoryBase getInventory(InventoryType activeSkill) {
		// TODO Auto-generated method stub
		// Observer things / return the inventory
		return invCache.get(activeSkill);
	}
}
