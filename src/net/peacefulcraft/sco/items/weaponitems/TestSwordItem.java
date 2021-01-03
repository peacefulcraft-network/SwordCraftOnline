package net.peacefulcraft.sco.items.weaponitems;

import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.EphemeralAttributeHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.utilities.Durability;
import net.peacefulcraft.sco.items.utilities.ItemAttribute;

public class TestSwordItem implements EphemeralAttributeHolder, CustomDataHolder, ItemIdentifier {

    private Integer quantity;

    @Override
    public String getName() {
        return "Test Sword";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.COMMON) + "Test Sword";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> desc = ItemTier.addDesc(ItemTier.COMMON);
        desc.add(ItemTier.getTierColor(ItemTier.COMMON) + "Testing sword");
        return desc;
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_SWORD;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON };
    }

    @Override
    public ItemTier getTier() {
        return ItemTier.COMMON;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isDroppable() {
        return true;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public JsonObject getCustomData() {
        JsonObject obj = new JsonObject();
        obj.addProperty("weapon", "sword");
        return obj;
    }

    @Override
    public void setCustomData(JsonObject data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        NBTItem nbti = new NBTItem(item);
        nbti.setString("weapon", data.get("weapon").getAsString());
        return nbti.getItem();
    }

    public TestSwordItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        item = ItemAttribute.setAttribute(item, 5, Attribute.GENERIC_ATTACK_DAMAGE);
        item = Durability.setWeapon(item, 1);
        return item;
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {
        // TODO Auto-generated method stub

    }
    
}
