package net.peacefulcraft.sco.mythicmobs.mobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * 
 * Weeber mini-boss mob
 * Hunts players who say its name on nightwave
 * 
 */
public class WeeperManager implements Listener {

    private final String PREFIX = ChatColor.BLACK + "[" + ChatColor.MAGIC + "Weeper" + ChatColor.BLACK + "]";
        public final String getPrefix() { return PREFIX; }
    
    private ArrayList<UUID> targets;

    private HashMap<UUID, HashMap<UUID, ActiveMob>> currentWeepers;

    public WeeperManager() {
        targets = new ArrayList<>();
        currentWeepers = new HashMap<>();

        load();
    }

    /**
     * Load player targets
     */
    public void load() {
        targets.clear();

        // Converting loaders from list to single
        IOLoader<SwordCraftOnline> defaultTargets = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        defaultTargets = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        List<File> targetFile = new ArrayList<File>();
        targetFile.add(defaultTargets.getFile());
        
        List<IOLoader<SwordCraftOnline>> loaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), targetFile, "WeeperData");
        IOLoader<SwordCraftOnline> loader = loaders.get(0);

        List<String> lis = new ArrayList<>(loader.getCustomConfig().getStringList("Targets"));
        for (String s : lis) {
            targets.add(UUID.fromString(s));
        }
    }

    /**
     * Save player targets
     */
    public void save() {
        IOLoader<SwordCraftOnline> file = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        FileConfiguration config = file.getCustomConfig();

        config.set("Targets", targets);

        try {
            config.save(file.getFile());
        } catch(IOException ex) {
            SwordCraftOnline.logDebug("[WeeperManager] Failed to save WeeperTargets.yml");
        }
        SwordCraftOnline.logDebug("[WeeperManager] WeeperTargets.yml saved!");
    }

    /**
     * Triggers weeber boss
     */
    public void trigger() {

        int amount = SwordCraftOnline.r.nextInt(targets.size()/2);

        ArrayList<UUID> toProcess = new ArrayList<>();
        ArrayList<UUID> toRemove = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            UUID uuid = targets.get(SwordCraftOnline.r.nextInt(targets.size() - 1));
            SCOPlayer s = GameManager.findSCOPlayer(Bukkit.getServer().getPlayer(uuid));
            if (s == null) { continue; }

            int level = ((s.getLevel() / 20) + 1) * 32;

            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(
                "Weeper", 
                s.getLocation(), 
                level);

            HashMap<UUID, ActiveMob> map = new HashMap<>();
            map.put(am.getUUID(), am);

            // Tracking weepers
            currentWeepers.put(s.getUUID(), map);
            toRemove.add(uuid);

            Announcer.messagePlayer(s, ChatColor.MAGIC + "Don't look at me!", 0);

            // Marking players
            for (Entity e : s.getLocation().getWorld().getNearbyEntities(s.getLocation(), 15, 15, 15)) {
                if (e instanceof Player) {
                    SCOPlayer ss = GameManager.findSCOPlayer((Player)e);
                    if (ss == null) { continue; }

                    toProcess.add(ss.getUUID());

                    Announcer.messagePlayer(ss, ChatColor.MAGIC + "Look away!", 0);
                }
            }
        }

        targets.addAll(toProcess);
        targets.removeAll(toRemove);
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent ev) {
        if (ev.getMessage().contains("weeper")) {

            SCOPlayer s = GameManager.findSCOPlayer(ev.getPlayer());
            if (s == null) { return; }

            if (targets.contains(s.getUUID())) {
                Announcer.messagePlayer(
                    s,
                    // TODO: Add prefix change 
                    //PREFIX, 
                    ChatColor.MAGIC + "You have cursed yourself again... YOU FOOL.",
                    0);
            } else {
                targets.add(s.getUUID());
                Announcer.messagePlayer(
                    s, 
                    ChatColor.MAGIC + "Here I come.", 
                    0);
            }
        }
    }

    @EventHandler
    public void deathEvent(EntityDeathEvent ev) {
        ModifierUser mu = ModifierUser.getModifierUser(ev.getEntity());
        if (mu == null) { return; }

        if (mu instanceof SCOPlayer) {
            UUID uuid = ((SCOPlayer)mu).getUUID();
            currentWeepers.remove(uuid);
        } else if (mu instanceof ActiveMob) {
            UUID uuid = ((ActiveMob)mu).getUUID();
            Iterator<Entry<UUID, HashMap<UUID, ActiveMob>>> iter = currentWeepers.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<UUID, HashMap<UUID, ActiveMob>> entry = iter.next();

                if (entry.getValue().containsKey(uuid)) {
                    iter.remove();
                }
            }
        }
    }

}
