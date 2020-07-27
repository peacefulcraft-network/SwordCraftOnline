package net.peacefulcraft.sco.quests.quests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class EscortQuestStep extends QuestStep implements Runnable {

    private MythicMob npc;

    private ActiveMob npcAm;

    private BukkitTask followTask;

    private SCOPlayer s;

    public EscortQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[1]);
        if (mm == null) {
            SwordCraftOnline.logInfo("Issue loading EscortQuestStep. Invalid mob target: " + split[1]);
            this.setInvalid();
            return;
        }

        // TODO: Load location and compare

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String npcName = this.npc.getDisplayName();

        //If quest is not activated we use find npc description
        if(!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in [] to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Help " + npcName + " get to [] safely!");
        } else {
            try {
                // TODO: Add location to fields.
                this.setDescription(String.format(this.getDescriptionRaw(), npcName));
            } catch (Exception ex) {
                this.setDescription("Help " + npcName + " get to [] safely!");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
         if(!(ev instanceof PlayerMoveEvent)) { return false; }
        PlayerMoveEvent e = (PlayerMoveEvent)ev;

        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if(s == null) { return false; } 
        
        //TODO: Check location

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        Player p = s.getPlayer();

        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(npc.getDisplayName(), p.getLocation());
        if(am == null) {
            SwordCraftOnline.logInfo("Error spawning NPC: " + npc.getDisplayName() + " for escort quest tied to player: " + p.getDisplayName());
            return;
        }

        //Saving fields for follow task
        this.npcAm = am;
        this.s = s;

        //Creating follow task that runs every 2 seconds
        this.followTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 40);
    }

    @Override
    public void run() {
        //NPC died
        if(this.npcAm.isDead()) { 
            this.setReset(true);
            this.followTask.cancel();
        }

        Creature mob = (Creature)this.npcAm.getEntity().getBukkitEntity();
        mob.setTarget(this.s.getPlayer());
    }
    
}