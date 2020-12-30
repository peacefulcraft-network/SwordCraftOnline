package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;

public class EphemeralSwordSkillInventory extends BukkitInventoryBase {

	private SwordSkillCaster s;

	public SwordSkillCaster getInventoryHolder() {
		return s;
	}

	public EphemeralSwordSkillInventory(SwordSkillCaster s, Long inventoryId, List<SwordSkillProvider> swordSkills) {

		this.inventory = Bukkit.getServer().createInventory(null, swordSkills.size() / 9 * 9, "Sword Skill Inventory");

		List<ItemIdentifier> castedItems = new ArrayList<ItemIdentifier>();
		// TODO: Find a better way to do this
		for (int i = 0; i < castedItems.size(); i++) {
			castedItems.add(castedItems.get(i));
		}
		this.generateItemsFromIdentifiers(castedItems);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent ev) {
		this.s.getSwordSkillManager().syncSkillInventory(this);
	}

	@Override
	public boolean isInventory(Inventory inventory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory(SCOPlayer s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
		// TODO Auto-generated method stub

	}
}