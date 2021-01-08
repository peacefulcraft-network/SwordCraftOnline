package net.peacefulcraft.sco.items.weaponitems;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.EphemeralAttributeHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.items.utilities.Durability;
import net.peacefulcraft.sco.items.utilities.ItemAttribute;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier.WeaponModifierType;

public class TestSwordItem implements WeaponAttributeHolder, EphemeralAttributeHolder, CustomDataHolder, ItemIdentifier {

    private Integer quantity;

    private JsonObject obj;

    private JsonObject weaponObj;

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
        return this.obj;
    }

    @Override
    public void setCustomData(JsonObject data) {
        this.obj = data;
    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        this.obj.add("reforge", WeaponAttributeHolder.parseWeaponData(item));
    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        NBTItem nbti = new NBTItem(item);
        nbti.setString("weapon", data.get("weapon").getAsString());

        JsonObject reforgeObj = data.getAsJsonObject("reforge");        
        this.weaponObj.add("reforge", reforgeObj);
        int reforgeCount = data.get("Reforge Count") == null ? 0 : data.get("Reforge Count").getAsInt();
        this.weaponObj.addProperty("Reforge Count", reforgeCount);

        return nbti.getItem();
    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        item = ItemAttribute.setAttribute(item, 5, Attribute.GENERIC_ATTACK_DAMAGE);
        item = Durability.setWeapon(item, 1);
        return item;
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {

    }

    @Override
    public JsonObject getWeaponData() {
        return this.weaponObj;
    }

    @Override
    public Integer getDisposition() {
        return 3;
    }

    public TestSwordItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.obj = new JsonObject();
        this.obj.addProperty("weapon", "sword");

        this.weaponObj = new JsonObject();
        JsonObject passive = new JsonObject();
        passive.addProperty("Refined Power", 1);
        passive.addProperty("Refined Technique", 5);

        JsonObject active = new JsonObject();
        active.addProperty("Refined Power", 3);

        JsonObject maxReforge = new JsonObject();
        maxReforge.addProperty(WeaponModifierType.PASSIVE.toString(), 2);
        maxReforge.addProperty(WeaponModifierType.ACTIVE.toString(), 1);

        this.weaponObj.add(WeaponModifierType.PASSIVE.toString(), passive);
        this.weaponObj.add(WeaponModifierType.ACTIVE.toString(), active);
        this.weaponObj.add("Max Reforge", maxReforge);
        this.weaponObj.addProperty("Disposition", getDisposition());
        this.weaponObj.addProperty("Reforge Count", 0);
    }
    
}
