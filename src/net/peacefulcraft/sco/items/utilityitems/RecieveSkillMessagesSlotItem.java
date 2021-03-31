package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class RecieveSkillMessagesSlotItem implements ItemIdentifier, CustomDataHolder {

    private ItemTier tier;
    private int quantity;
    private JsonObject customData;
    private Material mat;

    public RecieveSkillMessagesSlotItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
        this.mat = Material.GREEN_DYE;
    }

    @Override
    public String getName() {
        return "Recieve Skill Messages Slot";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Sword Skill Messages Setting:";
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<String>();
    }

    @Override
    public Material getMaterial() {
        return mat;
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
        String s = "Sword Skill Messages: " + data.get("recieveSkillMessages").getAsBoolean();
        data.addProperty("lore", s);

        ItemMeta meta = item.getItemMeta();
        meta.setLore(CustomDataHolder.parseDynamicLore(data));
        item.setItemMeta(meta);

        if(data.get("recieveSkillMessages").getAsBoolean()) {
            item.setType(Material.GREEN_DYE);
            mat = Material.GREEN_DYE;
        } else {
            item.setType(Material.RED_DYE);
            mat = Material.RED_DYE;
        }
        return item;
    }
    
}
