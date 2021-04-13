package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class KillQuestStep extends QuestStep {

    private HashMap<MythicMob, Integer> targets = new HashMap<>();

    private HashMap<MythicMob, Integer> kills = new HashMap<>();

    public KillQuestStep(MythicConfig mc) {
        super(mc);

        List<String> targetLis = mc.getStringList("Targets");
        if(targetLis == null || targetLis.isEmpty()) {
            this.logInfo("Invalid Target List Size");
            return;
        }

        for (String s : targetLis) {
            try {
                String[] split = s.split(" ");
                Integer amount = Integer.valueOf(split[1]);

                MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[0]);
                if (mm == null) {
                    this.logInfo("Invalid target name in config.");
                    return;
                }

                this.targets.put(mm, amount);
                this.kills.put(mm, 0);
            } catch (Exception ex) {
                this.logInfo("Invalid target amount in config.");
                return;
            }
        }
    }

    @Override
    public void _setDescription() {
        String npcName = getGiverName();
        String targetStr = "";
        for (MythicMob mm : targets.keySet()) {
            String dis = mm.getDisplayName();
            String num = String.valueOf(targets.get(mm));
            targetStr += num + " " + dis + ", ";
        }
        targetStr = targetStr.substring(0, targetStr.length() - 2);

        // If quest is not activated we use find npc description
        if (!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in\n " + this.getGiverRegion().getName() +" to start quest");
            return;
        }

        // If there is no description to format
        if (this.getDescriptionRaw() == null) {
            this.setDescription("You are tasked to kill " + targetStr + ".");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), targetStr));
            } catch (Exception ex) {
                this.setDescription("You are tasked to kill " + targetStr + ".");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        // Is damage event
        if (!(ev instanceof EntityDamageByEntityEvent)) {
            return false;
        }
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;

        // Is damager a player
        if (!(e.getDamager() instanceof Player)) {
            return false;
        }
        Player p = (Player) e.getDamager();

        // Is player in the game
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if (s == null) {
            return false;
        }

        // Is mob active mob
        Entity entity = e.getEntity();
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(entity.getUniqueId());
        if (am == null) {
            return false;
        }

        // Did mob die
        if (am.getHealth() > 0) {
            return false;
        }

        for (MythicMob mm : targets.keySet()) {
            // If mob is target match
            if (mm.getDisplayName().equalsIgnoreCase(am.getDisplayName())) {
                // If player has not hit kill target
                if (kills.get(mm) != targets.get(mm)) {
                    int killed = kills.get(mm);
                    kills.put(mm, killed + 1);
                }
            }
        }

        // Did we kill all that we needed
        for (MythicMob mm : targets.keySet()) {
            int targetKill = targets.get(mm);
            int actualKill = kills.get(mm);
            if (targetKill != actualKill) {
                return false;
            }
        }

        completeMessage(s);
        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        startupMessage(s);
        return;
    }

    @Override
    public Map<String, Object> getSaveData() {
        Map<String, Object> ret = new HashMap<>();

        Map<String, Integer> k = new HashMap<>();
        for(MythicMob mm : kills.keySet()) {
            k.put(mm.getDisplayName(), kills.get(mm));
        }
        ret.put("Kills", k);
        return ret;
    }
    
}