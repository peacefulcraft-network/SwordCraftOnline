package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.customitems.ICustomItem;
import net.peacefulcraft.sco.items.utilities.Movable;

public class UnlockSlot implements ICustomItem
{
	@Override
	public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
		ItemStack unlockSlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, amount);
		ItemMeta meta = unlockSlot.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Locked Tome Page");
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add("Gain more experience to unlock");
		meta.setLore(desc);
		
		unlockSlot.setItemMeta(meta);
		
		return Movable.setMovable(unlockSlot, movable);
	}

}
