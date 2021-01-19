package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

public class Trigger implements SwordSkillModule {

    private Boolean triggered = false;
        private void setTriggered(boolean set) { triggered = set; }
        private boolean getTriggered() { return this.triggered; }

    public Trigger() {

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
        if((ev instanceof PlayerInteractEvent) && getTriggered() == false) {
            PlayerInteractEvent evv = (PlayerInteractEvent)ev;
            ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(evv.getItem());
            if(identifier == null || identifier.getMaterial().equals(Material.AIR)) { return false; }

            if(!identifier.getName().equalsIgnoreCase("Secondary Skill Activated")) { return false; }
            return true;
        } else if(!(ev instanceof PlayerInteractEvent) && getTriggered() == true) {
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
