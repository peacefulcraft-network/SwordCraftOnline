package net.peacefulcraft.sco.mythicmobs.drops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.swordskills.utilities.Generator;

public class Drop extends WeightedItem implements Cloneable {
    private final String line;
        public String getLine() { return this.line; }

    /**Amount above dropAmount for items to be dropped
     * i.e. dropamount + 1
     */
    protected String dropVar = null;
        public String getDropVar() { return this.dropVar; }

    /**Amount of item to be dropped */
    private String dropAmount = "1";
        public String getDropAmount() { return this.dropAmount; }

    private double amount = 0.0D;
        public double getAmount() { return this.amount; }
        public void setAmount(double amount) { this.amount = amount; }

    /**Percentage 0-1 chance that item drops */
    private double weight;
        public double getWeight() { return this.weight; }

    /**Stores Sword Skill info read from file */
    private String[] ssInfo = null;
        public String[] getSSInfo() { return this.ssInfo; }
        public boolean isSkill() { return (ssInfo == null ? true : false); }

    private ItemStack item;
        public ItemStack getItem() { return this.item; }

    public Drop(String line) {
        this.line = line;
        try{
            /* Breaking arguments into format:
             * 0 Name, 1 Amount (x-y) or (x), 2 Chance to drop 0-1
             * SwordSkills information stored in name seperated by '-'
             * Custom Items store in name. I.e. GoldCoin
             * Non-Custom names format: GOLD_INGOT
             * i.e. CriticalStrike-1-common
             */
            String[] split = line.split(" ");
            //Checking if sword skill
            if(split[0].contains("-")) {
                String[] split2 = split[0].split("-");
                if(SkillIdentifier.itemExists(split2[0])) {
                    this.ssInfo = split2;
                    this.item = Generator.generateItem(this.ssInfo[0], Integer.valueOf(this.ssInfo[1]), ItemTier.valueOf(this.ssInfo[2]));
                }
            } else if(ItemIdentifier.itemExists(split[0])) {
                //Checking if custom item
                this.item = ItemIdentifier.generate(split[0]);
            } else {
                //Not custom or skill. Make new item stack
                this.item = new ItemStack(Material.getMaterial(split[0]));
            }

            if(split.length == 2) {
                if(split[1].contains("-")) {
                    String[] split2 = split[1].split("-");
                    this.dropAmount = split2[0];
                    this.dropVar = Double.toString(Double.valueOf(split2[1]) - Double.valueOf(split2[0]));
                } else {
                    this.dropAmount = split[1];
                }
            } else if(split.length == 3) {
                if(split[1].contains("-")) {
                    String[] split2 = split[1].split("-");
                    this.dropAmount = split2[0];
                    this.dropVar = Double.toString(Double.valueOf(split2[1]) - Double.valueOf(split2[0]));
                    this.weight = Double.valueOf(split[2]);
                } else {
                    this.dropAmount = split[1];
                    this.weight = Double.valueOf(split[2]);
                }
            } else if(split.length >= 4) {
                this.dropVar = split[1];
                this.dropAmount = split[2];
                this.weight = Double.valueOf(split[3]);
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

    /*
    public static ItemStack getDrop(String fileName, String drop) {
        if(drop.contains("}")) {
            String sp1 = drop.substring(0, drop.indexOf("}"));
            String sp2 = drop.substring(drop.indexOf("}"));
            String ns = sp1.replace(" ", "") + sp2;
            drop = ns;
        }
        String[] s = drop.split(" ");
        String name = null;
        if(s[0].contains("{")) {
            name = s[0].substring(0, s[0].indexOf("{"));
        } else {
            name = s[0];
        }

        String oName = name.toUpperCase();
        byte oData = 0;
        if(name.contains(":")) {
            oName = name.split(":")[0].toUpperCase();
            try {
                oData = Byte.valueOf(name.split(":")[1]).byteValue();
            } catch(Exception e) {}
            name = name.split(":")[0];
        }
        
        if(SkillIdentifier.skillExists(name)) {
            
        }
        
    }
    */

    /**Returns drops from file reading */
    public static Drop getDrop(String drop) {
        //Breaks drop into components.
        //Format: 0 Name, 1 Amount, 2 Chance, 3 Rank IF SS
        return new Drop(drop);
    }
}