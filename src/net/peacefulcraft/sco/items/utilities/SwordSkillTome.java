package net.peacefulcraft.sco.items.utilities;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SwordSkillTome implements net.peacefulcraft.sco.items.scoItem
{

	@Override
	public ItemStack create()
	{
		ItemStack tome = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta meta = tome.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Sword Skill Tome");
		
		ArrayList<String> desc = new ArrayList<String>();
		
		return tome;
	}

}
