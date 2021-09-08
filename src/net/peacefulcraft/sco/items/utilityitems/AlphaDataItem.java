package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class AlphaDataItem implements ItemIdentifier, CustomDataHolder {

    private ItemTier tier;
    private int quantity;
    private JsonObject customData;

    public AlphaDataItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.COMMON;
        this.quantity = quantity;
        customData = new JsonObject();
    }

    @Override
    public JsonObject getCustomData() {
        return customData;
    }

    @Override
    public void setCustomData(JsonObject data) {
        customData = data;        
    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        int exp = nbti.getInteger("AlphaExperience");        
        int streak = nbti.getInteger("AlphaWinStreak");

        customData.addProperty("AlphaExperience", exp);
        customData.addProperty("AlphaWinStreak", streak);
    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        NBTItem nbti = new NBTItem(item);
        nbti.setInteger("AlphaExperience", data.get("AlphaExperience").getAsInt());
        nbti.setInteger("AlphaWinStreak", data.get("AlphaWinStreak").getAsInt());
        return nbti.getItem();
    }

    @Override
    public String getName() {
        return "Alpha Data";
    }

    @Override
    public String getDisplayName() {
        return "Alpha Data";
    }

    @Override
    public ArrayList<String> getLore() {
        return null;
    }

    @Override
    public Material getMaterial() {
        return Material.STONE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON };
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
    
}
