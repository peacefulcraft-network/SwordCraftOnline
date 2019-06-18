package net.peacefulcraft.sco.items.utilities;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class TeleportCrystal implements net.peacefulcraft.sco.items.scoItem
{

	@Override
	public ItemStack create()
	{
		ItemStack crystal = new ItemStack(Material.DIAMOND, 1);
		ItemMeta meta = crystal.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Teleport Crystal");
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(ChatColor.DARK_PURPLE + "Right click to teleport to last visited city.");
		meta.setLore(desc);
		
		crystal.setItemMeta(meta);
		
		return crystal;
	}
	
}
