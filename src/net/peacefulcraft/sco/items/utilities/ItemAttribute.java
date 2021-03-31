package net.peacefulcraft.sco.items.utilities;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.ItemIdentifier;

/**
 * Used as constant way to modify weapon attributes Called in
 * applyEphemeralAttributes
 */
public class ItemAttribute {

    /**
     * Sets attribute of item stack
     * @param item Item we are modifying
     * @param value Value of attribute we want to set. This is added to default value of item.
     * @param slot Slot of inventory this item modifies in
     * @param attribute Attribute of item we are changing
     * @return Modified item stack
     */
    public static ItemStack setAttribute(ItemStack item, double value, EquipmentSlot slot, Attribute attribute) {
        ItemMeta meta = item.getItemMeta();

        String name = "generic." + String.valueOf(attribute);
        AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), name, value, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(attribute, mod);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Sets attribute of item when in main hand by default
     * @param item Item we are modifying
     * @param value Value of attribute we want to set. This is added to default value of item
     * @param attribute Attribute we are changing
     * @return Modified item stack
     */
    public static ItemStack setAttribute(ItemStack item, double value, Attribute attribute) {
        return setAttribute(item, value, EquipmentSlot.HAND, attribute);
    }

    /**
     * Gets attribute of item
     * 
     * @param identifier
     * @param attribute
     * @param slot
     * @return
     */
    public static Double getAttribute(ItemIdentifier identifier, Attribute attribute, EquipmentSlot slot) {
        ItemStack item = ItemIdentifier.generateItem(identifier);
        ItemMeta meta = item.getItemMeta();

        double value = 0;
        Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attribute);
        for(AttributeModifier mod : modifiers) {
            if((slot != null && mod.getSlot().equals(slot)) || slot == null) {
                value += mod.getAmount();
            }
        }

        return value;
    }

    /**
     * Gets attribute of item
     * 
     * @param identifier
     * @param attribute
     * @return
     */
    public static Double getAttribute(ItemIdentifier identifier, Attribute attribute) {
        return getAttribute(identifier, attribute, null);
    }

}
