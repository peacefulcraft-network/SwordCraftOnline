package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;

import net.peacefulcraft.sco.items.ItemTier;

public class SwordSkillDesc {
    
    private ItemTier tier;
    private SwordSkillType type;
    private ArrayList<String> desc = new ArrayList<>();

    public SwordSkillDesc(ItemTier tier, SwordSkillType type) {
        this.tier = tier;
        this.type = type;

        String sTier = "";
        switch(this.tier) {
            case COMMON:
                sTier = ItemTier.getTierColor(tier) + "Common";
            break; case UNCOMMON:
                sTier = ItemTier.getTierColor(tier) + "Uncommon";
            break; case RARE:
                sTier = ItemTier.getTierColor(tier) + "Rare";
            break; case LEGENDARY:
                sTier = ItemTier.getTierColor(tier) + "Legendary";
            break; case ETHEREAL:
                sTier = ItemTier.getTierColor(tier) + "Ethereal";
            break; case GODLIKE:
                sTier = ItemTier.getTierColor(tier) + "Godlike";
        }
        switch(this.type) {
            case PRIMARY:
                sTier += ItemTier.getTierColor(tier) + " Primary Sword Skill";
            break; case SECONDARY:
                sTier += ItemTier.getTierColor(tier) + " Secondary Sword Skill";
            break; case SWORD:
                sTier += ItemTier.getTierColor(tier) + " Sword Skill";
            break; case PASSIVE:
                sTier += ItemTier.getTierColor(tier) + " Passive Sword Skill";
        }
        desc.add(sTier);
    }

    /**
     * Adds proper colored string to description
     */
    public void add(String s) {
        desc.add(ItemTier.getTierColor(tier) + s);
    }

    /**
     * Fetches sword skill lore description
     * @return ArrayList of strings
     */
    public ArrayList<String> getDesc() {
        return desc;
    }
}
