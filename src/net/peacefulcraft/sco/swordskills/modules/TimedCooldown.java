package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.PlayerInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * TimedCooldown
 */
public class TimedCooldown implements SwordSkillModule {

    private long cooldownDelay;
        private void setCooldownDelay(long delay) { this.cooldownDelay = delay; }
    
    private long cooldownEnd = 0L;
        private boolean isCoolingDown() { return this.cooldownEnd > System.currentTimeMillis(); }
        private long getTimeRemaining() { return this.cooldownEnd - System.currentTimeMillis(); }
        private long getCooldownSeconds() { return this.getTimeRemaining() / 1000; }
    
    private String cooldownMessage;
        private String getCooldownMessage() { return this.cooldownMessage; }

    private String eventName = null;
    private String deniedEvent = null;

    private ModifierUser mu;
    private SwordSkillType type;

    /**
     * Basic Constructor
     * @param cooldownDelay
     */
    public TimedCooldown(long cooldownDelay) {
        this(cooldownDelay, null, SwordSkillType.PASSIVE);
    }

    /**
     * Constructor for ModifierUser wielding primary or secondary
     * 
     * @param cooldonwDelay
     * @param mu
     * @param type
     */
    public TimedCooldown(long cooldonwDelay, ModifierUser mu, SwordSkillType type) {
        this.cooldownDelay = cooldonwDelay;
        this.mu = mu;
        this.type = type;
    }

    public TimedCooldown(long cooldownDelay, String cooldonwMessage) {
        this(cooldownDelay);
        this.cooldownMessage = cooldonwMessage;
    }

    /**
     * Constructor for ModifierUser wielding primary or secondary
     * 
     * @param cooldownDelay
     * @param eventName
     * @param deniedEvent
     */
    public TimedCooldown(long cooldownDelay, String eventName, String deniedEvent) {
        this(cooldownDelay, null, SwordSkillType.PASSIVE, eventName, deniedEvent);
    }

    /**
     * Modified constructor to detect specific events
     * that trigger timer
     * @param eventName of Event
     * @param deniedEvent Events we want to stop from altering cooldown
     */
    public TimedCooldown(long cooldownDelay, ModifierUser mu, SwordSkillType type, String eventName, String deniedEvent) {
        this.cooldownDelay = cooldownDelay;
        this.mu = mu;
        this.type = type;

        this.eventName = eventName;
        this.deniedEvent = deniedEvent;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {}
    /* No support life cycle steps needed */

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) { 
        if(!isCoolingDown()) {
            // If we are reset and no longer cooling down
            
            if(mu != null && mu instanceof SCOPlayer) {
                SCOPlayer s = (SCOPlayer)mu;
                PlayerInventory inv = s.getPlayerInventory();
    
                int index = inv.getSkillTriggerIndex(type);
                if(index == -1) { return true; }
                ItemIdentifier ident = inv.getItem(index);
                if(!ident.getName().contains("Cooldown")) { return true; }
    
                String newIdent = type.equals(SwordSkillType.PRIMARY) ? "Primary Skill Activated" : "Secondary Skill Activated";
                inv.setItem(
                    index, 
                    ItemIdentifier.generateIdentifier(newIdent, ItemTier.COMMON, 1));
            }

            return true;
        }

        return false;
    }
    /* No preconditions hook */

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) { return true; }
    /* No pre-trigger hook */

    /**
     * After the skill has been triggered, add the cooldown delay to
     * prevent future Skill activation until such time that the cooldown
     * time has passed.
     */
    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        if(( this.eventName != null && ev.getEventName().equalsIgnoreCase(this.eventName) )
            || ( this.deniedEvent != null && !ev.getEventName().equalsIgnoreCase(this.deniedEvent) )
            || ( this.deniedEvent == null && this.eventName == null )) {
            
            this.cooldownEnd = System.currentTimeMillis() + cooldownDelay;
            
            if(mu != null && mu instanceof SCOPlayer) {
                SCOPlayer s = (SCOPlayer)mu;
                PlayerInventory inv = s.getPlayerInventory();

                int index = inv.getSkillTriggerIndex(type);
                ItemIdentifier ident = inv.getItem(index);
                
                String newIdent = type.equals(SwordSkillType.PRIMARY) ? "Primary Skill " : "Secondary Skill ";
                newIdent = ident.getName().contains("Cooldown") ? newIdent + "Activated" : newIdent + "Cooldown";
                inv.setItem(
                    index, 
                    ItemIdentifier.generateIdentifier(newIdent, ItemTier.COMMON, 1));
            }
        } 
    }

    @Override
    public void onUnregistration(SwordSkill ss) {
        if(mu instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)mu;
            PlayerInventory inv = s.getPlayerInventory();

            int index = inv.getSkillTriggerIndex(type);
            ItemIdentifier ident = inv.getItem(index);
            if(!ident.getName().contains("Cooldown")) { return; }

            String newIdent = type.equals(SwordSkillType.PRIMARY) ? "Primary Skill Activated" : "Secondary Skill Activated";
            inv.setItem(
                index, 
                ItemIdentifier.generateIdentifier(newIdent, ItemTier.COMMON, 1));
        }
    }
    /* No Dequip Actions */


}