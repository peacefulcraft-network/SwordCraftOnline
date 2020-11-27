package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.customitems.ICustomItem;
import net.peacefulcraft.sco.items.utilities.Movable;

public class SwordSkillTome implements ICustomItem
{

	@Override
	public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
		ItemStack tome = new ItemStack(Material.BOOK, amount);
		ItemMeta meta = tome.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Sword Skill Tome");
		
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(ChatColor.DARK_PURPLE + "Right click to store your sword skill knowledge.");
		meta.setLore(desc);
		
		tome.setItemMeta(meta);
		
		return Movable.setMovable(tome, movable);
	}

}
