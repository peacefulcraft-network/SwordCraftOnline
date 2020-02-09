package net.peacefulcraft.sco.items.customitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;

/**
 * Item class for Pet Token. Right click to
 * summon players pet.
 */
public class PetToken implements ICustomItem {

	@Override
	public ItemStack create(int amount) {
		ItemStack coin = new ItemStack(Material.IRON_NUGGET, amount);
        ItemMeta meta = coin.getItemMeta();
        meta.setDisplayName("Pet Token");

        ArrayList<String> desc = new ArrayList<String>();
        desc.add("Right Click To Summon Your Pet!");
        meta.setLore(desc);

        coin.setItemMeta(meta);

        return coin;
	}
    
}