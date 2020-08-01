package net.peacefulcraft.sco.quests.quests;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class EscortQuestStep extends QuestStep implements Runnable {

    /** NPC we are escorting */
    private MythicMob npc;

    /** Region we have to bring them to */
    private Region npcRegion;

    private ActiveMob npcAm;

    private BukkitTask followTask;

    private SCOPlayer s;

    public EscortQuestStep(MythicConfig mc) {
        super(mc);

        String npcName = mc.getString("npc", "");
        npcName = mc.getString("NPC", npcName);
        if (npcName == null || npcName.isEmpty()) {
            this.logInfo("No npc field in config.");
            return;
        }
        this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(npcName);
        if (this.npc == null) {
            this.logInfo("Invalid npc name in config.");
            return;
        }

        String regionName = mc.getString("npcRegion", "");
        regionName = mc.getString("NPCRegion", regionName);
        if (regionName == null || regionName.isEmpty()) {
            this.logInfo("No NPCRegion field in config.");
            return;
        }
        this.npcRegion = RegionManager.getRegion(regionName);
        if (npcRegion == null) {
            this.logInfo("Invalid NPCRegion field in config.");
            return;
        }

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String npcName = this.npc.getDisplayName();
        String startRegion = getGiverRegion().getName();
        String targetRegion = this.npcRegion.getName();

        // If quest is not activated we use find npc description
        if (!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in " + startRegion + " to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Help " + npcName + " get to " + targetRegion + " safely!");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), npcName, targetRegion));
            } catch (Exception ex) {
                this.setDescription("Help " + npcName + " get to " + targetRegion + " safely!");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        if (!(ev instanceof PlayerMoveEvent)) {
            return false;
        }
        PlayerMoveEvent e = (PlayerMoveEvent) ev;

        // Is in game
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if (s == null) {
            return false;
        }

        // Regions match
        if (!s.getRegion().equals(this.npcRegion)) {
            return false;
        }

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        Player p = s.getPlayer();

        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(npc.getDisplayName(),
                p.getLocation());
        if (am == null) {
            SwordCraftOnline.logInfo("Error spawning NPC: " + npc.getDisplayName()
                    + " for escort quest tied to player: " + p.getDisplayName());
            return;
        }

        // Saving fields for follow task
        this.npcAm = am;
        this.s = s;

        // Creating follow task that runs every 2 seconds
        this.followTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20,
                40);
    }

    @Override
    public void run() {
        // NPC died
        if (this.npcAm.isDead()) {
            this.setReset(true);
            this.followTask.cancel();
        }

        Creature mob = (Creature) this.npcAm.getEntity().getBukkitEntity();
        mob.setTarget(this.s.getPlayer());
    }

    @Override
    public Map<String, Object> getSaveData() {
        //No data necessary for player progression
        //Escort quests are done at quest completion or death
        return null;
    }
    
}