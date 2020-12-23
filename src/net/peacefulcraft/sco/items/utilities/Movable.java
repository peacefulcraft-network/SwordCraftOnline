package net.peacefulcraft.sco.items.utilities;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;

public class Movable {
    
    public static ItemStack setMovable(ItemStack item, boolean movable) {
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("movable", movable);
        return nbti.getItem();
    }
}
