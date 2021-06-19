package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.ActiveQuest;
import net.peacefulcraft.sco.quests.QuestStep;

public class KillQuestStep extends QuestStep {

    private HashMap<MythicMob, Integer> targets = new HashMap<>();

    private HashMap<MythicMob, Integer> kills = new HashMap<>();

    public KillQuestStep(MythicConfig mc, String questName) {
        super(mc, questName);

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

        // Targets string in lore processing
        String npcName = getGiverName();
        String targetStr = "";
        for (MythicMob mm : targets.keySet()) {
            String dis = mm.getDisplayName();
            String num = String.valueOf(targets.get(mm));
            targetStr += num + " " + dis + ", ";
        }
        targetStr = targetStr.substring(0, targetStr.length() - 2);

        // Progression string in lore processing
        String progressStr = "Kill Progress:\n";
        for (MythicMob mm : targets.keySet()) {
            String dis = mm.getDisplayName();
            String num = String.valueOf(targets.get(mm));
            String killed = String.valueOf(kills.get(mm));
            progressStr += "  " + killed + "/" + num + " " + dis + "(s)\n";
        }

        // If quest is not activated we use find npc description
        if (!this.isActivated()) {
            this.setDescription(this.name + "\nTalk to " + npcName + " in\n" + this.getGiverRegion().getName() +" to start quest");
            return;
        }

        // If there is no description to format
        if (this.getDescriptionRaw() == null || this.getDescriptionRaw().isEmpty()) {
            this.setDescription(this.name + "\nYou are tasked to kill " + targetStr + ".\n" + progressStr);
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), targetStr));
            } catch (Exception ex) {
                this.setDescription(this.name + "\nYou are tasked to kill " + targetStr + ".");
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

        Entity entity = e.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity liv = (LivingEntity)entity;
        // Is mob active mob
        //
        // If mob died it is no longer registered
        // Override this check with health of entity
        if (liv.getHealth() - e.getDamage() > 0) {
            return false;
        }

        SwordCraftOnline.logDebug("[Kill Quest Step] Starting check loop.");

        // Scrubbing entity display name
        String trueName = liv.getCustomName().split("\\[")[0];
        trueName = trueName.trim();
        trueName = ChatColor.stripColor(trueName);

        boolean wasChanged = false;
        for (MythicMob mm : targets.keySet()) {
            // If mob is target match
            SwordCraftOnline.logDebug("[Kill Quest Step] Checking: " + mm.getDisplayName() + ", against: " + trueName + ".");
            if (mm.getDisplayName().equalsIgnoreCase(trueName)) {
                // If player has not hit kill target
                if (kills.get(mm) != targets.get(mm)) {
                    int killed = kills.get(mm);
                    kills.put(mm, killed + 1);
                    wasChanged = true;
                }
            }
        }

        SwordCraftOnline.logDebug("[Kill Quest Step] Kills Map: " + kills);

        // If kill was counted we want to update the item description
        if(wasChanged) {
            ActiveQuest aq = s.getQuestBookManager().getRegisteredQuest(this.name);
            if(aq != null) {
                SwordCraftOnline.logDebug("[Kill Quest Step] Protected update call.");
                aq.updateItemDescription();
            } else {
                SwordCraftOnline.logDebug("[Kill Quest Step] Active quest not found.");
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

        SwordCraftOnline.logDebug("[Kill Quest Step] Preconditions passed.");

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        startupMessage(s);
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

    @Override
    public void processStepData(JsonObject data) {
        if(data.get("stepData") == JsonNull.INSTANCE) {
            SwordCraftOnline.logDebug("[Kill Quest Step] Error passing step data for processing.");
            return;
        }
        if(data.get("stepData").getAsJsonObject().size() == 0) {
            SwordCraftOnline.logDebug("[Kill Quest Step] Passed step data empty.");
            return;
        }

        JsonObject killData = data.get("stepData").getAsJsonObject().get("killData").getAsJsonObject();
        for(Entry<String, JsonElement> entry : killData.entrySet()) {
            for(MythicMob mm : targets.keySet()) {
                if(mm.getDisplayName().equalsIgnoreCase(entry.getKey())) {
                    this.kills.put(mm, entry.getValue().getAsInt());
                }
            }
        }
        this.updateDescription();
    }
    
}