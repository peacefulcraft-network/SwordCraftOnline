package net.peacefulcraft.sco.items.utilities;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SwordSkillTome
{

	public ItemStack create()
	{
		ItemStack tome = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = tome.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Sword Skill Tome");
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(ChatColor.DARK_PURPLE + "Right click to store your sword skill knowledge.");
		meta.setLore(desc);
		
		tome.setItemMeta(meta);
		
		return tome;
	}

}
