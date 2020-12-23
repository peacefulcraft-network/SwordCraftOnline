package net.peacefulcraft.sco.mythicmobs.drops;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

/**
 * Supporting rewards class. Used in questing
 */
public class Reward {

    private String line;

    private ItemStack item = null;

    private int amount;

    private int experience;
        public int getExperience() { return this.experience; }

    public Reward(String line) {
        this.line = line;

        /**
         * String formatting: {item name or swordskill} {amount}
         * if sword skill info is broken by "-". {name}-{tier}
         */
        try {
            String[] split = line.split(" ");
            if(split[0].contains("-")) {
                String[] split2 = split[0].split("-");
                if(ItemIdentifier.itemExists(split2[0])) {
                    this.item = ItemIdentifier.generateItem(split2[0], ItemTier.valueOf(split2[1]), 1);
                    this.amount = Integer.valueOf(split[1]);
                }
            } else if(ItemIdentifier.itemExists(split[0])) {
                this.item = ItemIdentifier.generateItem(split[0], ItemTier.COMMON, 1);
                this.amount = Integer.valueOf(split[1]);
            } else if(split[0].equalsIgnoreCase("Experience") || split[0].equalsIgnoreCase("Exp")){
                this.experience = Integer.valueOf(split[1]);
            } else {                
                SwordCraftOnline.logInfo("No valid item/experience for Reward containing: " + line);
                return;
            }
        } catch(IllegalArgumentException ex) {
            SwordCraftOnline.logInfo("Failed to load reward. Invalid item, tier, amount value.");
            return;
        }
    }

    /**@return Item at amount if item is not null */
    public ItemStack getReward() {
        if(this.item == null) { return null; }
        this.item.setAmount(amount);
        return this.item;
    }

}