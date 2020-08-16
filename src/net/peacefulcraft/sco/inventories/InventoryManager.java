package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class InventoryManager {

	private SCOPlayer s;
	private HashMap<InventoryType, InventoryBase> invCache;	
		
	public InventoryManager(SCOPlayer s) {
		this.s = s;
		invCache = new HashMap<InventoryType, InventoryBase>();
	}
	
	public void registerInventory(InventoryBase inv) {
		SwordCraftOnline.logDebug("Registered inventory to " + this.s.getUUID());
		invCache.put(inv.getType(), inv);
	}
	
	public InventoryBase getInventory(InventoryType invType) {
		return invCache.get(invType);
	}
	
	public void unregisterInventory(InventoryType invType) {
		invCache.remove(invType);
	}
	
	public void unregisterInventory(InventoryBase inv) {
		InventoryBase posInv = invCache.get(inv.getType());
		if(posInv == null) { return; }
		
		if(posInv == inv) { invCache.remove(posInv.getType()); }
	}
}
