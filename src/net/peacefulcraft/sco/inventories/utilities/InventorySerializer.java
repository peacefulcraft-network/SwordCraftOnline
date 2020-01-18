package net.peacefulcraft.sco.inventories.utilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * @author Phil2812 
 * Credit: https://bukkit.org/threads/serialize-inventory-to-single-string-and-vice-versa.92094/
 * --
 * 6/24/2019 parsonswy: Updated to use new item IDs and namespaced enchantments
 * 1/4/2020 parsonswy : Added support for serializing custom display names and lore
 */
public class InventorySerializer {
	public static String InventoryToString (Inventory inventory)
	{
		String serialization = inventory.getSize() + ";";
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack is = inventory.getItem(i);
			if (is != null)
			{
				String serializedItemStack = new String();
				
				// Get the item type
				String isType = String.valueOf(is.getType().name());
				serializedItemStack += "t@" + isType;
				
				
				ItemMeta im = is.getItemMeta();
				
				// Get the item name
				if(im.hasDisplayName()) {
					serializedItemStack += ":n@" + im.getDisplayName();
				}
					
				// Get the lore
				if(im.getLore() != null) {
					String lore = ":l@" + im.getLore().size() + "@";
					for(int l=0; l<im.getLore().size(); l++) {
						lore += im.getLore().get(l);
						
						// If there is not another lore line, don't add the @ to the end
						if( (l+1) < im.getLore().size()) {
							lore += "@";
						}
					}
					serializedItemStack += lore;
				}
					
				if (((org.bukkit.inventory.meta.Damageable) is.getItemMeta()).getDamage() != 0)
				{
					String isDurability = String.valueOf(((org.bukkit.inventory.meta.Damageable) is.getItemMeta()).getDamage());
					serializedItemStack += ":d@" + isDurability;
				}

				if (is.getAmount() != 1)
				{
					String isAmount = String.valueOf(is.getAmount());
					serializedItemStack += ":a@" + isAmount;
				}

				Map<Enchantment,Integer> isEnch = is.getEnchantments();
				if (isEnch.size() > 0)
				{
					for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
					{
						//NamespacedEnchantment
						serializedItemStack += ":e@" + ench.getKey().getKey() + "@" + ench.getValue();
					}
				}

				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		return serialization;
	}

	public static Inventory StringToInventory (String inventoryData)
	{
		String[] serializedBlocks = inventoryData.split(";");
		String invInfo = serializedBlocks[0];
		Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

		for (int i = 1; i < serializedBlocks.length; i++)
		{
			String[] serializedBlock = serializedBlocks[i].split("#");
			int stackPosition = Integer.valueOf(serializedBlock[0]);

			if (stackPosition >= deserializedInventory.getSize())
			{
				continue;
			}

			ItemStack is = null;
			Boolean createdItemStack = false;

			String[] serializedItemStack = serializedBlock[1].split(":");
			for (String itemInfo : serializedItemStack)
			{
				String[] itemAttribute = itemInfo.split("@");
				if (itemAttribute[0].equals("t"))
				{
					is = new ItemStack(Material.getMaterial(itemAttribute[1]));
					createdItemStack = true;
				}
				else if (itemAttribute[0].equals("n") && createdItemStack) 
				{
					is.getItemMeta().setDisplayName(itemAttribute[1]);
				}
				else if(itemAttribute[0].equals("l") && createdItemStack) 
				{
					ArrayList<String> lore = new ArrayList<String>();
					for(int lc = 0; lc < Integer.valueOf(itemAttribute[1]); lc++) {
						lore.add(itemAttribute[lc+2]);
					}
					is.getItemMeta().setLore(lore);
				}
				else if (itemAttribute[0].equals("d") && createdItemStack)
				{
					((Damageable) is).damage(Short.valueOf(itemAttribute[1]));
				}
				else if (itemAttribute[0].equals("a") && createdItemStack)
				{
					is.setAmount(Integer.valueOf(itemAttribute[1]));
				}
				else if (itemAttribute[0].equals("e") && createdItemStack)
				{
					NamespacedKey enchantment = new NamespacedKey(SwordCraftOnline.getPluginInstance(), itemAttribute[0]);
					is.addEnchantment(Enchantment.getByKey(enchantment), Integer.valueOf(itemAttribute[2]));
				}
			}
			deserializedInventory.setItem(stackPosition, is);
		}

		return deserializedInventory;
	}
}