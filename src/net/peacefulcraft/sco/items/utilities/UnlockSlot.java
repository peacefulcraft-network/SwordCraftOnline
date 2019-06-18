package net.peacefulcraft.sco.items.utilities;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class UnlockSlot implements net.peacefulcraft.sco.items.scoItem
{

	@Override
	public ItemStack create()
	{
		ItemStack unlockSlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta meta = unlockSlot.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Locked Tome Page");
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add("Gain more experience to unlock");
		meta.setLore(desc);
		
		unlockSlot.setItemMeta(meta);
		
		return unlockSlot;
	}

}
