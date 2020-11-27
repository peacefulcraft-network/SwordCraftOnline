package net.peacefulcraft.sco.items.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.customitems.ICustomItem;

public class CraftingBlockedSlot implements ICustomItem {

    @Override
    public ItemStack create(Integer amount, Boolean shop) {
        ItemStack slot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = slot.getItemMeta();
        meta.setDisplayName(" ");

        slot.setItemMeta(meta);
        
        NBTItem nbti = new NBTItem(slot);
        nbti.setBoolean("movable", false);

        return nbti.getItem();
    }
    
}
