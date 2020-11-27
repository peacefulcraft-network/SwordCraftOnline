package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.items.utilityitems.UnlockSlot;
import net.peacefulcraft.sco.storage.tasks.SavePlayerInventory;

public class SwordSkillInventory extends InventoryBase implements Listener{
	
	// private Inventory inventory from InventoryBase
	
	public SwordSkillInventory(SCOPlayer s, HashMap<Integer, ItemStack> items, int size) {
		super(s.getPlayer(), InventoryType.SWORD_SKILL);
		this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, size);
		for(Integer loc : items.keySet()) {
			inventory.setItem(loc, items.get(loc));
		}

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
		SCOPlayer s = GameManager.findSCOPlayer((Player)e.getWhoClicked());
		if(s == null) { return; }
		if(s.hasOverride()) { return; }
		
		if(e.getCurrentItem() == null) { return; }
		
		NBTItem nbti = new NBTItem(e.getCurrentItem());
		if(nbti.hasKey("movable") && nbti.getBoolean("movable") == true) { return; }
		e.setCancelled(true);
	}
	
/****************************************************
 * 
 *	InventoryBase Related Implementation
 *
 ****************************************************/
	
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
 	
	/**
	 * Default loadout for the sword skill inventory
	 */
	public void initializeDefaultLoadout() {
		emptyRow(0);
		fillRow(0, 8-slotUnlock(), new UnlockSlot().create(1, false, false));
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
	
	/**
	 * Examines all items currently in the player's inventory and creates a list of
	 * Sword Skill identifiers that indicate which sword skills the player has.
	 * @return
	 */
	public ArrayList<SkillIdentifier> generateSkillIdentifiers(){
		ArrayList<SkillIdentifier> identifiers = new ArrayList<SkillIdentifier>();
		for(int i=0; i<inventory.getSize(); i++) {
			ItemStack item = inventory.getItem(i);
			if(item == null) { continue; }
			NBTItem nbtItem = new NBTItem(item);
			
			String skillName = item.getItemMeta().getDisplayName();
			ItemTier tier = ItemTier.valueOf(nbtItem.getString("tier"));
			int skilLevel = nbtItem.getInteger("skill_level");
			
			SkillIdentifier identifier = new SkillIdentifier(skillName, skilLevel, tier, i);
			identifiers.add(identifier);
		}
		return identifiers;
	}
	
	@Override
	public void saveInventory() {
		SCOPlayer s = GameManager.findSCOPlayer(this.getObserver());
		(new SavePlayerInventory(s, this.getType(), generateSkillIdentifiers())).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());
	}
}
