package net.peacefulcraft.sco.items.customitems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class TeleportCrystal implements ICustomItem
{
	public ItemStack create(Integer amount, Boolean shop)
	{
		ItemStack crystal = new ItemStack(Material.DIAMOND, amount);
		ItemMeta meta = crystal.getItemMeta();
		if(shop) {
			meta.setDisplayName(ChatColor.BLUE + "Teleport Crystal - 2,000");
		} else {
			meta.setDisplayName(ChatColor.BLUE + "Teleport Crystal");
		}
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(ChatColor.DARK_PURPLE + "Right click to teleport to last visited city.");
		meta.setLore(desc);
		
		crystal.setItemMeta(meta);
		
		return crystal;
	}
}
