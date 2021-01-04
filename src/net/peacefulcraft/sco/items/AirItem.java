package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class AirItem implements ItemIdentifier {

	@Override
	public String getName() { return "Air"; }

	@Override
	public String getDisplayName() { return ""; }

	@Override
	public ArrayList<String> getLore() { return new ArrayList<String>(); }

	@Override
	public Material getMaterial() { return Material.AIR; }

	@Override
	public Integer getQuantity() { return 1; }

	@Override
	public void setQuantity(Integer quantity) {}

	@Override
	public ItemTier[] getAllowedTiers() { return new ItemTier[] { ItemTier.COMMON }; }

	@Override
	public ItemTier getTier() { return ItemTier.COMMON; }

	@Override
	public boolean isDroppable() { return true; }

	@Override
	public boolean isMovable() {
		/**
		 * Air needs to be movable so when the player clicks on an empty slot to put
		 * an item into it, the operation doesn't get cancled from air click event.
		 */
		return true;
	}

	public AirItem(ItemTier tier, Integer quantity) {}
}
