package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class BankSlotItem implements ItemIdentifier, CustomDataHolder {

    private ItemTier tier;
    private int quantity;
    private JsonObject customData;

    public BankSlotItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Bank Slot";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Money Info:";
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<String>();
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_NUGGET;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.LEGENDARY };
    }

    @Override
    public ItemTier getTier() {
        return tier;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isDroppable() {
        return false;
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public JsonObject getCustomData() {
        return customData;
    }

    @Override
    public void setCustomData(JsonObject data) {
        this.customData = data;
    }

    @Override
    public void parseCustomItemData(ItemStack item) {

    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(CustomDataHolder.parseDynamicLore(data));
        item.setItemMeta(meta);
        return item;
    }
    
}
