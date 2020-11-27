package net.peacefulcraft.sco.items.utilityitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.customitems.ICustomItem;
import net.peacefulcraft.sco.items.utilities.Movable;

public class BlockedSlot implements ICustomItem
{

	@Override
	public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
		ItemStack blockedSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE, amount);
		ItemMeta meta = blockedSlot.getItemMeta();
		meta.setDisplayName(" ");
		
		blockedSlot.setItemMeta(meta);
		
		return Movable.setMovable(blockedSlot, movable);
	}

}
