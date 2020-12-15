package net.peacefulcraft.sco.items.utilityitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.customitems.ICustomItem;
import net.peacefulcraft.sco.items.utilities.Movable;

public class InfusionStartSlot implements ICustomItem {

    @Override
    public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
        ItemStack slot = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, amount);
        ItemMeta meta = slot.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_PURPLE + "Click to begin infusion");
        slot.setItemMeta(meta);

        return Movable.setMovable(slot, movable);
    }
    
}
