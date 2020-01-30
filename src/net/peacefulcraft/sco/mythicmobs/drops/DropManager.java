package net.peacefulcraft.sco.mythicmobs.drops;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import net.peacefulcraft.log.Banners;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class DropManager {
    /**Storing droptables by filename */
    private HashMap<String, DropTable> dropTables = new HashMap<String, DropTable>();
    
    private List<Runnable> secondPass;

    private SwordCraftOnline inst;
        public SwordCraftOnline getInst() { return this.inst; }

    /**Loads tables from folder into map */
    public void loadDropTables() {
        IOLoader<SwordCraftOnline> defaultDropTables = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleDropTables.yml", "DropTables");
        List<File> droptableFiles = IOHandler.getAllFiles(defaultDropTables.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> droptableLoader = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), droptableFiles, "DropTables");
        this.dropTables.clear();

        SwordCraftOnline.logInfo(Banners.get(Banners.DROP_MANAGER) + "Beginning loading...");
        for(IOLoader<SwordCraftOnline> sl : droptableLoader) {
            /*
            for(String s : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                //s is name of file
                if(sl.getCustomConfig().getStringList(s + ".Drops") != null) {
                    String file = sl.getFile().getName();
                    MythicConfig mc = new MythicConfig(s, sl.getCustomConfig());
                    DropTable dt = new DropTable(file, s, mc);
                    this.dropTables.put(s, dt);
                    SwordCraftOnline.logInfo(Banners.get(Banners.DROP_MANAGER) + "Succesfully Loaded: " + s);

                    //readTable(sl.getFile());
                }
            }*/
            String file = sl.getFile().getName();
            MythicConfig mc = new MythicConfig(file, sl.getCustomConfig());
            DropTable dt = new DropTable(file, file, mc);
            this.dropTables.put(file, dt);
            SwordCraftOnline.logInfo(Banners.get(Banners.DROP_MANAGER) + "Succesfully Loaded: " + file);
        }
        SwordCraftOnline.logInfo(Banners.get(Banners.DROP_MANAGER) + "Loading complete!");
        runSecondPass();
    }

    /**Loads and prints droptable information to console. */
    private void readTable(File f) {
        new YamlConfiguration();
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        String name = f.getName().replace(".yml", "");

        String conditions = "";
        for(String s : c.getStringList(name + ".Conditions")) {
            conditions += s + ", ";
        }
        System.out.println("[DROPTABLE DEBUG] CONDITIONS: " + conditions);
        String drop = "";
        for(String s : c.getStringList(name + ".Drops")) {
            drop += s + ", ";
        }
        System.out.println("[DROPTABLE DEBUG] DROPS: " + drop);
        System.out.println("[DROPTABLE DEBUG] MINITEMS: " + c.getInt(name + "Items.MinItems"));
        System.out.println("[DROPTABLE DEBUG] MAXITEMS: " + c.getInt(name + "Items.MaxItems"));
        String items = "";
        for(String s : c.getStringList(name + "Items.Drops")) {
            items += s + ", ";
        }
        System.out.println("[DROPTABLE DEBUG] ITEM DROPS: " + items);
    }

    public DropManager(SwordCraftOnline inst) {
        this.secondPass = new ArrayList<>();
        this.inst = inst;
    }

    public void runSecondPass() {
        this.secondPass.forEach(r -> r.run());
        this.secondPass.clear();;
    }

    public void queueSecondPass(Runnable r) {
        this.secondPass.add(r);
    }

    /** Checks if table contains key*/
    public boolean isInDropTable(String s) {
        return this.dropTables.keySet().contains(s);
    }

    /**Returns drop table from hashmap */
    public DropTable getDropTable(String name) {
        return this.dropTables.get(name);
    }

    public Collection<DropTable> getDropTables() {
        return this.dropTables.values();
    }

    public HashMap<String, DropTable> getDroptableMap() {
        return this.dropTables;
    }

    public static void drop(Location loc, int exp, List<Drop> drops) {
        for(Drop d : drops) {
            loc.getWorld().dropItemNaturally(loc, d.getItem());
        }
        if(exp != 0) {
            int i = exp % 4;
            int per = (exp - exp % 4) / 4;
            for(int y = 0; y < 4; y++) {
                ExperienceOrb eo = (ExperienceOrb)loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                eo.setExperience(per);
            }
            if(i != 0) {
                ExperienceOrb eo = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                eo.setExperience(i);
            }
        }
    }
}