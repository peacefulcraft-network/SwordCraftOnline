package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class DeityBlinkItem implements SwordSkillProvider {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<String> getLore() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material getMaterial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemTier getTier() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getQuantity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setQuantity(Integer quantity) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDroppable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMovable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public JsonObject getCustomData() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SwordSkillType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setModifiers() {
        // TODO Auto-generated method stub

    }
    
}
