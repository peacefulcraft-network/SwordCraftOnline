package net.peacefulcraft.sco.items;

import org.bukkit.inventory.ItemStack;

public interface EphemeralAttributeHolder {
	
	/**
	 * Apply non-database-saved, custom attributes to an ItemStack during generation.
	 */
	public void applyEphemeralAttributes(ItemStack item);

	/**
	 * Extract non-database-saved, custom attributes from an ItemStack.
	 */
	public void parseEphemeralAttributes(ItemStack item);
}
