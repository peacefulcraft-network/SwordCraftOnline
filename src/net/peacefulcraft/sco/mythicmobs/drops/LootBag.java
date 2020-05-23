package net.peacefulcraft.sco.mythicmobs.drops;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;

public class LootBag {
    private ArrayList<Drop> drops;
        public ArrayList<Drop> getDrops() { return this.drops; }
        //public void addDrop(Drop d) { this.drops.add(d); }
        public ArrayList<ItemStack> getItems() {
            ArrayList<ItemStack> out = new ArrayList<>();
            for(Drop d : this.drops) {
                out.add(d.getItem());
            }
            return out;
        }

    private int experience;
        public int getExp() { return this.experience; }
        public void setExp(int i) { this.experience = i; }

    public LootBag() {
        this.drops = new ArrayList<Drop>();

        this.experience = 0;
    }

    /**Adds clone with random amount to lootbag */
    public void addDrop(Drop d) {
        Drop copy = d.clone();
        int amount = Integer.valueOf(copy.getDropAmount());
        if(copy.hasDropVar()) {
            amount += SwordCraftOnline.r.nextInt(Integer.valueOf(copy.getDropVar()));
        }
        copy.setAmount(amount);
        copy.getItem().setAmount(amount);
        this.drops.add(copy);
    }

    /**Removes a random drop from the loot bag */
    public void removeDrop() {
        this.drops.remove(SwordCraftOnline.r.nextInt(this.drops.size()));
    }

    /**Returns the info for lootbag */
    public String getInfo() {
        String s = ChatColor.GOLD + "Drops: ";
        for(Drop d : this.drops) {
            s += ChatColor.GOLD + d.getName() + " " + d.getAmount() + ", ";
        }
        s += "\n" + ChatColor.GOLD + "Experience: " + Integer.valueOf(this.experience);
        return s;
    }
}