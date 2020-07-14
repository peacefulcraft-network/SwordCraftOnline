package net.peacefulcraft.sco.tutorial;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.tutorial.TutorialManager.TutorialStage;

public class TutorialBot {

    private MythicConfig config;

    private String file;

    private String internalName;
    
    private List<String> greetings;
    
    private List<String> goodbyes;

    private List<String> conversations;

    private List<String> mannerisms;

    private boolean usesMythicMob;

    private MythicMob mm;
        public void setMythicMob(MythicMob m) { this.mm = m; }

    private ActiveMob am;
        public ActiveMob getActiveMob() { return this.am; }

    private Location loc;
        public Location getLocation() { return this.loc; }

    private TutorialStage stage;
        public TutorialStage getTutorialStage() { return this.stage; }

    public TutorialBot(String file, String internalName, MythicConfig mc) {
        this.config = mc;
        this.file = file;
        this.internalName = internalName;

        this.greetings = mc.getStringList("Greetings");
        this.goodbyes = mc.getStringList("Goodbyes");
        this.conversations = mc.getStringList("Conversations");
        this.mannerisms = mc.getStringList("Mannerisms");

        this.usesMythicMob = mc.getBoolean("UsesMythicMob", true);

        try {
            this.stage = TutorialStage.valueOf(mc.getString("TutorialStage").toUpperCase());
        } catch(Exception ex) {
            SwordCraftOnline.logInfo("[Tutorial Bot] Error occured loading bot TutorialStage variable.");
            this.stage = null;
        }
    }

    /**@return Random greeting from greeting list */
    public String getGreeting() {
        return this.greetings.get(SwordCraftOnline.r.nextInt(greetings.size()-1));
    }

    /**@return Random goodbye from goodbye list */
    public String getGoodbye() {
        return this.goodbyes.get(SwordCraftOnline.r.nextInt(greetings.size()-1));
    }

    /**@return Random mannerism from mannerism list */
    public String getMannerism() {
        return this.mannerisms.get(SwordCraftOnline.r.nextInt(mannerisms.size()-1));
    }

    /**
     * Runs through conversation list over time
     * @param lis List of SCOPlayers to be read the conversation
     */
    public void doConversation(List<SCOPlayer> lis) {
        if(conversations.size() == 0) { return; }
        
        ArrayList<String> copy = new ArrayList<>(conversations);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(copy.isEmpty()) { this.cancel(); }
                String message = TutorialManager.getTutorialChatBar(file) + copy.remove(0);
                Announcer.messageGroup(lis, message, false);
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 120, 10);
    }

    /**
     * Overrides mobmanager spawn for our conditions.
     * @param loc Location to spawn mob
     */
    public void doConversation(SCOPlayer s) {
        List<SCOPlayer> lis = new ArrayList<SCOPlayer>();
        lis.add(s);
        doConversation(lis);
    }

    /**
     * Overrides mobmanager spawn for our conditions.
     * @param loc Location to spawn mob
     */
    public void spawnBot(Location loc) {
        if(!this.usesMythicMob) { return; }
        if(this.mm == null) {
            SwordCraftOnline.logInfo("[Tutorial Bot] Attempted to spawn Bot with no MythicMob set: " + this.file);
            return;
        }
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(this.file, loc);
        if(am == null) {
            SwordCraftOnline.logInfo("[Tutorial Bot] Active Mob instance null. Spawn abandoned: " + this.file);
        } else {
            this.am = am;
            this.loc = loc;
        }
    }
}