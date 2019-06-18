package net.peacefulcraft.sco.items.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockedSlot implements net.peacefulcraft.sco.items.scoItem
{

	@Override
	public ItemStack create()
	{
		ItemStack blockedSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta meta = blockedSlot.getItemMeta();
		meta.setDisplayName(" ");
		
		blockedSlot.setItemMeta(meta);
		
		return blockedSlot;
	}

}
