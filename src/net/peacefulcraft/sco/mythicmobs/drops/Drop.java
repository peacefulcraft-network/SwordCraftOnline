package net.peacefulcraft.sco.mythicmobs.drops;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.log.Banners;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.swordskills.utilities.Generator;

public class Drop implements Cloneable {
    private String name;
        public String getName() { return this.name; }
    
    private final String line;
        public String getLine() { return this.line; }

    /**Amount above dropAmount for items to be dropped
     * i.e. dropamount + var
     */
    protected String dropVar = null;
        public String getDropVar() { return this.dropVar; }
        public boolean hasDropVar() { return(this.dropVar == null ? false : true); }

    /**Amount of item to be dropped */
    private String dropAmount = "1";
        public String getDropAmount() { return this.dropAmount; }

    private int amount = 0;
        public double getAmount() { return this.amount; }
        public void setAmount(int amount) { this.amount = amount; }

    /**Percentage 0-1 chance that item drops */
    private double weight = 0.0D;
        public double getWeight() { return this.weight; }
        public boolean hasWeight() { return (this.weight == 0.0D ? false : true); }

    /**Stores Sword Skill info read from file */
    private String[] ssInfo = null;
        public String[] getSSInfo() { return this.ssInfo; }
        public boolean isSkill() { return (this.ssInfo == null ? false : true); }

    private ItemStack item;
        public ItemStack getItem() { return this.item; }

    /**Determines if drop is limited to one iteration */
    private boolean once;
        public boolean isOnce() { return this.once; }

    public Drop(String line) {
        this.line = line;
        this.name = (line.split(" "))[0];
        
        try{
            /* Breaking arguments into format:
             * 0 Name, 1 Amount (x-y) or (x), 2 Chance to drop 0-1
             * SwordSkills information stored in name seperated by '-'
             * Custom Items store in name. I.e. GoldCoin
             * Non-Custom names format: GOLD_INGOT
             * i.e. CriticalStrike-1-common
             */
            String[] split = line.split(" ");
            if(split[0].contains("-")) {
                //Checking if sword skill
                String[] split2 = split[0].split("-");
                if(SkillIdentifier.itemExists(split2[0])) {
                    this.ssInfo = split2;
                    this.item = Generator.generateItem(this.ssInfo[0], Integer.valueOf(this.ssInfo[1]), ItemTier.valueOf(this.ssInfo[2]));
                }
            } else if(ItemIdentifier.itemExists(split[0])) {
                //Checking if custom item
                this.item = ItemIdentifier.generate(split[0], 1);
            } else {
                //Not custom or skill. Make new item stack
                try {
                    this.item = new ItemStack(Material.getMaterial(split[0]));
                } catch(NullPointerException e) {
                    SwordCraftOnline.logSevere(Banners.get(Banners.DROP) + " Attempted to load invalid material.");
                }
            }

            //String format: NAME AMOUNT ISONCE
            //No weight, defaulted to 1.0D
            if(split.length == 3) {
                //If drop amount is variable or not.
                if(split[1].contains("-")) {
                    String[] split2 = split[1].split("-");
                    this.dropAmount = split2[0];
                    this.dropVar = Integer.toString(Integer.valueOf(split2[1]) - Integer.valueOf(split2[0]));
                    this.weight = 1.0D;
                } else {
                    this.dropAmount = split[1];
                    this.weight = 1.0D;
                }
                this.once = Boolean.valueOf(split[2]);
            //String format: NAME AMOUNT WEIGHT ISONCE
            } else if(split.length == 4) {
                //If drop amount is variable or not AND drop contains weight.
                if(split[1].contains("-")) {
                    String[] split2 = split[1].split("-");
                    this.dropAmount = split2[0];
                    this.dropVar = Integer.toString(Integer.valueOf(split2[1]) - Integer.valueOf(split2[0]));
                    this.weight = Double.valueOf(split[2]);
                } else {
                    this.dropAmount = split[1];
                    this.weight = Double.valueOf(split[2]);
                }
                this.once = Boolean.valueOf(split[3]);
            } else if(split.length >= 5) {
                this.dropVar = split[1];
                this.dropAmount = split[2];
                this.weight = Double.valueOf(split[3]);
                this.once = Boolean.valueOf(split[4]);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public Drop(String line, double amount) {
        this(line);
        this.dropAmount = Double.toString(amount);
    }

    public boolean rollChance() {
        if(this.weight != 0.0D && SwordCraftOnline.r.nextFloat() > this.weight) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return getClass().getName().hashCode();
    }

    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        return o.getClass().equals(getClass());
    }

    public Drop addAmount(Drop other) {
        this.amount += other.getAmount();
        return clone();
    }

    public Drop clone() {
        try {
            return (Drop)super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**Returns drops from file reading */
    public static Drop getDrop(String drop) {
        //Breaks drop into components.
        //Format: 0 Name, 1 Amount, 2 Chance, 3 Rank IF SS
        return new Drop(drop);
    }

    /**Gets info for Drop */
    public String getInfo() {
        String s = ChatColor.GOLD + "[" + this.name + ", " + this.dropAmount;
        if(hasDropVar()) { 
            double total = Double.valueOf(this.dropAmount) + Double.valueOf(this.dropVar);
            s += ChatColor.GOLD + "-" + Double.valueOf(total); 
        }
        if(hasWeight()) { s += ChatColor.GOLD + ", " + Double.valueOf(this.weight); }
        s += ChatColor.GOLD + "]";
        return s;
    }
}