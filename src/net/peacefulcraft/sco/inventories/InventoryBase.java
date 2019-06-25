package net.peacefulcraft.sco.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class InventoryBase{

	private Player observer;
		public Player getObserver() { return observer; }
	
	public abstract Class getInventoryType();
	private Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
	public void openInventory() {
		observer.openInventory(inventory);
	}
	
	public void closeInventory() {
		observer.closeInventory();
	};
	
	public InventoryBase(Player observer) {
		this.observer = observer;
	}
	
}
