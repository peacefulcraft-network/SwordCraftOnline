package net.peacefulcraft.sco.mythicmobs.drops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class DropTable {
    private final String fileName;
        public String getFileName() { return this.fileName; }

    private final String internalName;
        public String getInternalName() { return this.internalName; }

    private RandomCollection<Drop> drops = new RandomCollection();
        public boolean hasDrops() { return (this.drops.size() > 0); }

    private int initems = -1;

    private int maxItems = -1;

    private int minItems = -1;

    private double bonusLevelItems;

    private Double bonusLuckItems;

    private boolean hasConditions = false;

    public DropTable(String file, String name, MythicConfig mc) {
        this.fileName = file;
        this.internalName = name;
        int totalItems = mc.getInteger("TotalItems", -2);
        this.maxItems = mc.getInteger("MaxItems", totalItems);
        this.minItems = mc.getInteger("MinItems", totalItems);
        //this.bonusLevelItems = SwordCraftOnline.r.nextDouble(mc.getInteger("BonusLevelItems", 0));
        List<String> strDrops = mc.getStringList("Drops");
        SwordCraftOnline.getPluginInstance().getDropManager().queueSecondPass(() -> {
            for(String s : strDrops) {
                Drop d = new Drop(s);
                this.drops.add(d);
            }
        });

        List<String> nTConditions = mc.getStringList("Conditions");
        for(String s : nTConditions) {
            //TODO: Add Conditions adding
        }
    }

    public DropTable(String file, String name, List<String> drops) {
        this.fileName = file;
        this.internalName = name;
        for(String s : drops) {
            Drop drop = Drop.getDrop(s);
            this.drops.add(drop);
        }
    }
    
    public List<Drop> generate(SCOPlayer s) {
        List<Drop> drops = new ArrayList<Drop>();

        int amountModifiers = 0;
        double bonusLevelMod = SwordCraftOnline.r.nextDouble();
        if(bonusLevelMod != 0.0D) {
            amountModifiers += (int)(s.getLevel() * bonusLevelMod);
        }
        if(s.getDropMod() != 0.0D) {
            amountModifiers *= s.getDropMod();
        }

        if(this.minItems > 0 && this.maxItems > 0) {
            int amount, diff = this.maxItems - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.maxItems + amountModifiers;
            }
            Collection<Drop> d = this.drops.get(amount);
            for(Drop drop : d) {
                drops.add(drop);
            }
            return drops;
        } else if(this.minItems > 0) {
            int amount, diff = this.drops.size() - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.minItems + amountModifiers;
            }
            for(Drop drop : this.drops.get(amount)) {
                drops.add(drop);
            }
            return drops;
        } else if(this.maxItems > 0) {
            int items = 0 - amountModifiers;
            for(Drop drop : this.drops.getView()) {
                if(drop.rollChance()) {
                    items++;
                    drops.add(drop);
                    return drops;
                }
                if(items >= this.maxItems) {
                    break;
                }
            }
        } else {
            for(Drop drop : this.drops.getView()) {
                if(drop.rollChance()) {
                    drops.add(drop);
                    return drops;
                }
            }
        }
        return drops;
    }
}