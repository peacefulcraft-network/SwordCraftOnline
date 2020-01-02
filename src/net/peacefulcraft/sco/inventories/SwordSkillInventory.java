package net.peacefulcraft.sco.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.items.utilities.UnlockSlot;

public class SwordSkillInventory extends InventoryBase implements Listener{
	@Override
	public Class getInventoryType() { return SwordSkillInventory.class; }
		
	public SwordSkillInventory(Player p) {
		super(p, SwordSkillInventory.class);
	}
	
	/**
	 * Called when the player disconnects 
	 */
	public void destroy() {
		
	}

/****************************************************
 * 
 *	Listener Interface Related Implementation
 *
 ****************************************************/
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getCurrentItem() != null) {	
			if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ) {
	        	e.setCancelled(true);
	        } else if(e.getCurrentItem().getItemMeta().hasLore()) {
				for(String s : e.getCurrentItem().getItemMeta().getLore()) {
					if(s.contains("Sword Skill")) {
						return;	
					}
				}
				e.setCancelled(true);
	        } 
		}
	}

	
/****************************************************
 * 
 *	InventoryBase Related Implementation
 *
 ****************************************************/
	
	/**
	 * Default loadout for the sword skill inventory
	 */
	public void initializeDefaultLoadout() {
		emptyRow(0);
		fillRow(0, 8-slotUnlock(), new UnlockSlot().create());
	}
	
		private int slotUnlock() {
			int level = GameManager.findSCOPlayer(getObserver()).getLevel();
			if(level >= 0 && level <= 10) {
				return 1;
			} else if(level > 10 && level <= 20) {
				return 2;
			}
			return 0;
		}

	@Override
	public void resizeInventory(int size) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void openInventory() {
		SwordCraftOnline.getPluginInstance().getServer().getPluginManager().registerEvents(this, SwordCraftOnline.getPluginInstance());
		super.openInventory();
	}
	
	@Override
	public void closeInventory() {
		super.closeInventory();
		InventoryClickEvent.getHandlerList().unregister(this);
	}
}
