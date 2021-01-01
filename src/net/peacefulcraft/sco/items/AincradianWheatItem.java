package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class AincradianWheatItem implements ItemIdentifier {

	@Override
	public String getName() { return "Aincradian Wheat"; }

	@Override
	public String getDisplayName() {
		return ItemTier.getTierColor(ItemTier.COMMON) + "Aincradian Wheat";
	}

	@Override
	public ArrayList<String> getLore() {
		ArrayList<String> desc = ItemTier.addDesc(ItemTier.COMMON);
		desc.add(ItemTier.getTierColor(ItemTier.COMMON) + "Natural wheat found across Aincrad");
		return desc;
	}

	@Override
	public Material getMaterial() { return Material.WHEAT; }

	@Override
	public ItemTier[] getAllowedTiers() { return new ItemTier[]{ ItemTier.COMMON }; }

	@Override
	public ItemTier getTier() { return ItemTier.COMMON; }


	private Integer quantity;
		@Override
		public Integer getQuantity() { return this.quantity; }

		@Override
		public void setQuantity(Integer quantity) { this.quantity = quantity; }

	@Override
	public boolean isDroppable() { return true; }

	@Override
	public boolean isMovable() { return true; }

	public AincradianWheatItem(ItemTier tier, Integer quantity) {
		this.quantity = quantity;
	}
}
