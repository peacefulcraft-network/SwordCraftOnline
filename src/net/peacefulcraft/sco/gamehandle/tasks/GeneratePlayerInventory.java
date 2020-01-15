package net.peacefulcraft.sco.gamehandle.tasks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.inventory.InventoryType;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.item.SkillIdentifier;
import net.peacefulcraft.sco.swordskill.SkillProvider;

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
		
		/*
		 * Task can be queued my an async task and it is possible the player disconnected while the
		 * async task was executing so we need to make sure the player is still connected.
		 * If they've disconnected, abort as we don't need to load their inventory now that their gone.
		 */
		if(s.getPlayer() == null || !s.getPlayer().isOnline()) { return; }
		
		ArrayList<SkillProvider> providers = new ArrayList<SkillProvider>(); 
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for(SkillIdentifier identifier : identifiers) {
			try {
				
				// Instantiate the provider
				Class<?> classs = Class.forName("net.peacefulcraft.sco.items." + identifier.getSkillName().replaceAll("\\s", "") + "Item");
				Constructor<?> constructor = classs.getConstructor(int.class, ItemTier.class);
				Object[] args = { identifier.getSkillLevel(), identifier.getRarity() };
				
				// Add to provider list to setup skills below
				SkillProvider provider = (SkillProvider) constructor.newInstance(args);
				providers.add(provider);
				
				// Generate & add the item to list for the inventory
				items.put(identifier.getInventoryLocation(), provider.getItem());
				
			} catch (ClassNotFoundException e) {
				SwordCraftOnline.logSevere("Attempted to create item " + identifier.getSkillName() + ", but no coresponding class was found in net.peacefulcraft.sco.items");
			} catch (NoSuchMethodException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " must have a constuctor with arguments (int, ItemTier)");
			} catch (SecurityException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " does not have a public constructor.");
			} catch (InstantiationException | InvocationTargetException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " generated exception during reflective instantiation:");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " is an abstract class and cannot be instantiated.");
			} catch (IllegalArgumentException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
			} catch (ClassCastException e) {
				SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + identifier.getSkillName() + " must implement SwordSkillProvider.");
			}
		}
		
		switch(t) {
		case ACTIVE_SKILL:
			s.getInventoryManager().registerInventory(new SwordSkillInventory(s, items, 9));
		break; case MAIN_INVENTORY:
			// s.getInventoryManager().registerInventory(new PlayerMainInventory(s, items, 9));
		break; default:
			SwordCraftOnline.logSevere("[TASK][GeneratePlayerInventory] Error, Inventory type " + t + " not recognized. ");
		break;
		}
		
		// Register all skills provided by this inventory to the player's skill manager
		for(SkillProvider provider : providers) {
			provider.registerSkill(s);
		}
	}

}
