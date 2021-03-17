package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class Trigger implements SwordSkillModule {

    private Boolean triggered = false;
        private void setTriggered(boolean set) { triggered = set; }
        private boolean getTriggered() { return this.triggered; }

    private SwordSkillType type;
    private String passedEvent;

    /**
     * @param type of SwordSkill we are triggering
     * changes which item we detect for
     */
    public Trigger(SwordSkillType type) {
        this.type = type;
        this.passedEvent = null;
    }

    /**
     * @param type of SwordSkill we are triggering
     * changes which item we detect for
     * @param event type we pass through triggering
     */
    public Trigger(SwordSkillType type, String event) {
        this.type = type;
        this.passedEvent = event;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {
        /* No support life cycle steps needed */
    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        return true;
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) {
        // Checking if event should pass
        if(passedEvent != null && ev.getEventName().equalsIgnoreCase(passedEvent)) { return true; }

        if((ev instanceof PlayerInteractEvent) && getTriggered() == false) {
            PlayerInteractEvent evv = (PlayerInteractEvent)ev;
            if(evv.getAction().equals(Action.LEFT_CLICK_AIR) || evv.getAction().equals(Action.LEFT_CLICK_BLOCK)) { return false; }

            ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(evv.getItem());
            if(identifier == null || identifier.getMaterial().equals(Material.AIR)) { return false; }

            if(this.type.equals(SwordSkillType.PRIMARY) 
                && !identifier.getName().equalsIgnoreCase("Primary Skill Activated")) { return false; }
            if(this.type.equals(SwordSkillType.SECONDARY)
                && !identifier.getName().equalsIgnoreCase("Secondary Skill Activated")) { return false; }
            if(this.type.equals(SwordSkillType.SWORD) 
                && !(identifier instanceof WeaponAttributeHolder)) { return false; }
            setTriggered(true);
            return true;
        } else if(!(ev instanceof PlayerInteractEvent) && getTriggered()) {
            return true;
        }
        return false;
    }

    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        if(!(ev instanceof PlayerInteractEvent)) {
            setTriggered(false);
        }
    }

    @Override
    public void onUnregistration(SwordSkill ss) {

    }
    
}
