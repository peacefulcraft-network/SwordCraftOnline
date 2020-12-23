package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class AirItem implements ItemIdentifier {

	public AirItem(ItemTier tier, Integer quantity) {}

	@Override
	public Material getMaterial() {
		return Material.AIR;
	}

	@Override
	public ItemTier[] getAllowedTiers() {
		return new ItemTier[] { ItemTier.COMMON };
	}

	@Override
	public ItemTier getTier() {
		return ItemTier.COMMON;
	}

	@Override
	public String getName() {
		return "Air";
	}

	@Override
	public ArrayList<String> getLore() {
		return new ArrayList<String>();
	}

	@Override
	public Integer getQuantity() {
		return 1;
	}

	@Override
	public void setQuantity(Integer quantity) {
	}

	@Override
	public boolean isDroppable() {
		return true;
	}

	@Override
	public boolean isMovable() {
		/**
		 * Air needs to be movable so when the player clicks on an empty slot to put
		 * an item into it, the operation doesn't get cancled from air click event.
		 */
		return true;
	}
	
}
