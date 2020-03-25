package net.peacefulcraft.sco.mythicmobs.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class MobSpawnHandler implements Listener {
    private static List<MobSpawnHandler> vanillaMobs = new ArrayList<MobSpawnHandler>();
        public static void addVanillaMobs(String name, MythicConfig mc) { vanillaMobs.add(new MobSpawnHandler(name, mc)); }
        public static void clearVanillaMobs() { vanillaMobs.clear(); }

    private String name;
        public String getName() { return this.name; }

    private MythicConfig mc;
        public MythicConfig getMythicConfig() { return this.mc; }

    /**
     * Constructs a handler instance. 
     * Stores information helpful to handling mobspawn events.
     */
    public MobSpawnHandler(String name, MythicConfig mc) {
        this.name = name;
        this.mc = mc;
    }

    public MobSpawnHandler() {}
    
    /**Checks if mob spawn should be cancelled */
    private boolean checkRemove(String ent) {
        for(MobSpawnHandler handler : vanillaMobs) {
            if(handler.getName().equalsIgnoreCase(ent) && handler.getMythicConfig().getBoolean("Remove", false)) {
                return true;
            }
        }
        return false;
    }

    /**Checks if mob spawn should be replaced by AM instance */
    private ActiveMob checkReplace(String ent, Location loc) {
        for(MobSpawnHandler handler : vanillaMobs) {
            String replace = handler.getMythicConfig().getString("Replace", null);
            if(handler.getName().equalsIgnoreCase(ent) && replace != null) {
                ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(replace, loc);
                if(am == null) {
                    SwordCraftOnline.logInfo("Attempted to replace " + ent + " with invalid MythicMob.");
                }
                return am;
            }
        }
        return null;
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        Entity ent = e.getEntity();
        if(!(e.getSpawnReason().equals(SpawnReason.NATURAL))) { return; }
        
        
        if(checkRemove(ent.getType().toString())) {
            e.setCancelled(true);
            return;
        }
        ActiveMob am = checkReplace(ent.getType().toString(), e.getLocation());
        if(am == null) { return; }
        e.setCancelled(true);
        return;
    }
}