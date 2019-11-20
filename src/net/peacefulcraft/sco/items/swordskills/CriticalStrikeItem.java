package net.peacefulcraft.sco.items.swordskills;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CriticalStrikeItem implements SwordSkillItem
{

	@Override
	public ItemStack create(String tier)
	{
		ItemStack item = new ItemStack(Material.FLINT, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Critical Strike");
		
		ArrayList<String> desc;
		desc = SwordSkillItem.addDesc(tier);
		desc.add("Chance to deal critical damage!");
		meta.setLore(desc);
		
		item.setItemMeta(meta);
		return item;
	}

}
