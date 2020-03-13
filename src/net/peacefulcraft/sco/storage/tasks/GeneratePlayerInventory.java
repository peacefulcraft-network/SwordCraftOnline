package net.peacefulcraft.sco.storage.tasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.swordskills.SkillProvider;

/**
 * Sync task that task a list of item identifiers, generates the actual ItemStacks
 * places them into the respective InventoryType
 */
public class GeneratePlayerInventory extends BukkitRunnable{

	private SCOPlayer s;
	private InventoryType t;
	private ArrayList<SkillIdentifier> identifiers;
	
	public GeneratePlayerInventory(SCOPlayer s, InventoryType t, ArrayList<SkillIdentifier> identifiers) {
		this.s = s;
		this.t = t;
		this.identifiers = identifiers;
	}
	
	@Override
	public void run() {
		SwordCraftOnline.logDebug("Generating player " + t + " for " + this.s.getUUID());
		/*
		 * Task can be queued my an async task and it is possible the player disconnected while the
		 * async task was executing so we need to make sure the player is still connected.
		 * If they've disconnected, abort as we don't need to load their inventory now that their gone.
		 */
		if(s.getPlayer() == null || !s.getPlayer().isOnline()) { return; }
		
		ArrayList<SkillProvider> providers = new ArrayList<SkillProvider>(); 
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for(SkillIdentifier identifier : identifiers) {
			SkillProvider provider = identifier.getProvider();
			providers.add(provider);
			
			// Generate & add the item to list for the inventory
			items.put(identifier.getInventoryLocation(), provider.getItem());
		}

		switch(t) {
		case SWORD_SKILL:
			SwordSkillInventory inv = new SwordSkillInventory(s, items, 9);
			s.getInventoryManager().registerInventory(inv);
			s.getSwordSkillManager().syncSkillInventory(inv);
		break; case MAIN_INVENTORY:
			// s.getInventoryManager().registerInventory(new PlayerMainInventory(s, items, 9));
		break; default:
			SwordCraftOnline.logSevere("[TASK][GeneratePlayerInventory] Error, Inventory type " + t + " not recognized. ");
		break;
		}
	}

}
