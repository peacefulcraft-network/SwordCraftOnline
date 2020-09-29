package net.peacefulcraft.sco.mythicmobs.drops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class DropTable {
    private final String fileName;
        public String getFileName() { return this.fileName; }

    private final String internalName;
        public String getInternalName() { return this.internalName; }

    private ArrayList<Drop> drops = new ArrayList<Drop>();
        public boolean hasDrops() { return (this.drops.size() > 0); }

    private int initems = -1;

    private int maxItems = -1;

    private int minItems = -1;

    private double bonusLevelItems;

    private Double bonusLuckItems;

    private boolean hasConditions = false;
        public boolean hasConditions() { return this.hasConditions; }

    private HashMap<String, String> conditions = new HashMap<String, String>();
        public HashMap<String, String> getConditions() { return this.conditions; }

    private int experience = 0;
        public int getExperience() { return this.experience; }

    private ArrayList<Drop> nightwaveDrops = new ArrayList<Drop>();
        public boolean hasNightwaveDrops() { return (this.nightwaveDrops.size() > 0); }

    private int nightwaveChance;
        public int getNightwaveChance() { return this.nightwaveChance; }

    /**Reads config file into data structure 
     * Files should come in same name as mob using them.
     * I.e. SkeletonKing uses SkeletonKing
    */
    public DropTable(String file, String name, MythicConfig mc) {
        this.fileName = file;
        this.internalName = name;
        int totalItems = mc.getInteger("TotalItems", -2);
        this.maxItems = mc.getInteger("MaxItems", totalItems);
        this.minItems = mc.getInteger("MinItems", totalItems);
        this.nightwaveChance = mc.getInteger("NightwaveChance", 40);
        //this.bonusLevelItems = SwordCraftOnline.r.nextDouble(mc.getInteger("BonusLevelItems", 0));
        /**
         * Reading drops into droptable
         */
        List<String> strDrops = mc.getStringList("Drops");
        for(String s : strDrops) {
            if(s.contains("Experience")) {
                this.experience = Integer.valueOf((s.split(" "))[1]);
                continue;
            }
            Drop d = new Drop(s);
            this.drops.add(d);
        }

        /**Reading nightwave specific drops into droptable */
        List<String> strNightwave = mc.getStringList("Nightwave");
        for(String s : strNightwave) {
            Drop d = new Drop(s);
            this.nightwaveDrops.add(d);
        }

        /**
         * Loading conditions
         */
        List<String> nTConditions = mc.getStringList("Conditions");
        if(!nTConditions.isEmpty()) {
            this.hasConditions = true;
            for(String s : nTConditions) {
                String[] split = s.split(" ");
                this.conditions.put(split[0], split[1]);
            }
        }
    }

    /**Loads data structure from list of drops */
    public DropTable(String file, String name, List<String> drops) {
        this.fileName = file;
        this.internalName = name;
        for(String s : drops) {
            Drop drop = Drop.getDrop(s);
            this.drops.add(drop);
        }
    }

    /**
     * Generating lootbag without amount modifiers 
     * For in console testing. DO NOT CALL ON PLAYER.
     */
    public LootBag generate() {        
        LootBag bag = new LootBag();

        //Set bag experience to be multiple of exp bonus - Random of 1/4th amount
        bag.setExp(this.experience - SwordCraftOnline.r.nextInt(this.experience/4));
        //No Player. Amount set to 0.
        int amountModifiers = 0;

        if(this.minItems > 0 && this.maxItems > 0) {
            int amount;
            int diff = this.maxItems - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.maxItems + amountModifiers;
            }

            SwordCraftOnline.logInfo("Diff: " + Integer.valueOf(diff) + " Item amount: " + Integer.valueOf(amount));

            Collection<Drop> d = getRandomDrop(amount, this.drops);
            for(Drop drop : d) {
                /**Design revision: Blocks items containing "once" flag from being repeated */
                if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                    continue;
                }
                bag.addDrop(drop);
            }
            /**If server is in nightwave and random(40) equals 2 we inject a nightwave drop */
            if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                if(this.hasNightwaveDrops()) {
                    List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                    bag.removeDrop();
                    bag.addDrop(lis.get(0));
                }
            }

            return bag;
        } else if(this.minItems > 0) {
            int amount, diff = this.drops.size() - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.minItems + amountModifiers;
            }
            for(Drop drop : getRandomDrop(amount, this.drops)) {
                if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                    continue;
                }
                bag.addDrop(drop);
            }
            if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                if(this.hasNightwaveDrops()) {
                    List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                    bag.removeDrop();
                    bag.addDrop(lis.get(0));
                }
            }
            return bag;
        } else if(this.maxItems > 0) {
            int items = 0 - amountModifiers;
            for(Drop drop : this.drops) {
                if(drop.rollChance()) {
                    items++;
                    if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                        continue;
                    }
                    bag.addDrop(drop);
                    if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                        if(this.hasNightwaveDrops()) {
                            List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                            bag.removeDrop();
                            bag.addDrop(lis.get(0));
                        }
                    }
                    return bag;
                }
                if(items >= this.maxItems) {
                    break;
                }
            }
        } else {
            for(Drop drop : this.drops) {
                if(drop.rollChance()) {
                    if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                        continue;
                    }
                    bag.addDrop(drop);
                    if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                        if(this.hasNightwaveDrops()) {
                            List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                            bag.removeDrop();
                            bag.addDrop(lis.get(0));
                        }
                    }
                    return bag;
                }
            }
        }
        return bag;
    }
    
    /**
     * Generates drops from drop table using weighted collection
     * Using the amount modifiers from the SCOPlayer
     */
    public LootBag generate(SCOPlayer s) {
        LootBag bag = new LootBag();

        //Set bag experience to be multiple of exp bonus
        bag.setExp((int)(s.getExpMod() * this.experience));

        int amountModifiers = 0;
        double bonusLevelMod = SwordCraftOnline.r.nextDouble();
        if(bonusLevelMod != 0.0D) {
            amountModifiers += (int)(s.getLevel() * bonusLevelMod);
        }
        if(s.getDropMod() != 0.0D) {
            amountModifiers *= s.getDropMod();
        }

        if(this.minItems > 0 && this.maxItems > 0) {
            int amount;
            int diff = this.maxItems - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.maxItems + amountModifiers;
            }
            Collection<Drop> d = getRandomDrop(amount, this.drops);
            for(Drop drop : d) {
                if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                    continue;
                }
                bag.addDrop(drop);
            }
            if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                if(this.hasNightwaveDrops()) {
                    List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                    bag.removeDrop();
                    bag.addDrop(lis.get(0));
                }
            }
            return bag;
        } else if(this.minItems > 0) {
            int amount, diff = this.drops.size() - this.minItems;
            if(diff > 0) {
                amount = SwordCraftOnline.r.nextInt(diff) + this.minItems + amountModifiers;
            } else {
                amount = this.minItems + amountModifiers;
            }
            for(Drop drop : getRandomDrop(amount, this.drops)) {
                if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                    continue;
                }
                bag.addDrop(drop);
            }
            if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                if(this.hasNightwaveDrops()) {
                    List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                    bag.removeDrop();
                    bag.addDrop(lis.get(0));
                }
            }
            return bag;
        } else if(this.maxItems > 0) {
            int items = 0 - amountModifiers;
            for(Drop drop : this.drops) {
                if(drop.rollChance()) {
                    items++;
                    if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                        continue;
                    }
                    bag.addDrop(drop);
                    if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                        if(this.hasNightwaveDrops()) {
                            List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                            bag.removeDrop();
                            bag.addDrop(lis.get(0));
                        }
                    }
                    return bag;
                }
                if(items >= this.maxItems) {
                    break;
                }
            }
        } else {
            for(Drop drop : this.drops) {
                if(drop.rollChance()) {
                    if(bag.getItems().contains(drop.getItem()) && drop.isOnce()) { 
                        continue;
                    }
                    bag.addDrop(drop);
                    if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && SwordCraftOnline.r.nextInt(this.nightwaveChance) == 1) {
                        if(this.hasNightwaveDrops()) {
                            List<Drop> lis = getRandomDrop(1, this.nightwaveDrops);
                            bag.removeDrop();
                            bag.addDrop(lis.get(0));
                        }
                    }
                    return bag;
                }
            }
        }
        return bag;
    }

     /**Returns random item from droptable based on weight. */
     private List<Drop> getRandomDrop(int amount, ArrayList<Drop> lis) {
        List<Drop> out = new ArrayList<Drop>();
        for(int j = 0; j < amount; j++) {
            double totalWeight = 0.0D;
            for(Drop d : lis) {
                totalWeight += d.getWeight();
            }

            int randomIndex = -1;
            double rand = SwordCraftOnline.r.nextDouble() * totalWeight;
            for(int i = 0; i < lis.size(); ++i) {
                rand -= lis.get(i).getWeight();
                if(rand <= 0.0D) {
                    randomIndex = i;
                    break;
                }
            }
            Drop temp = lis.get(randomIndex).clone();
            out.add(temp);
        }
        return out;        
    }

    /**Info for droptables */
    public String getInfo() {
        String s = ChatColor.GOLD + "Droptable info for: " + this.fileName + "\n";
        s += ChatColor.GOLD + "Drops: ";
        for(Drop drop : this.drops) {
            s += ChatColor.GOLD + drop.getInfo() + ", ";
        }
        s += "\n" + ChatColor.GOLD + "Experience: " + Integer.valueOf(this.experience) + "\n";
        s += ChatColor.GOLD + "Max Items: " + Integer.valueOf(this.maxItems) + "\n";
        s += ChatColor.GOLD + "Min Items: " + Integer.valueOf(this.minItems) + "\n";
        s += ChatColor.GOLD + "Conditions: ";
        for(String ss : this.conditions.keySet()) {
            s += ChatColor.GOLD + ss + " " + this.conditions.get(ss) + ", ";
        }
        return s;
    }
}
