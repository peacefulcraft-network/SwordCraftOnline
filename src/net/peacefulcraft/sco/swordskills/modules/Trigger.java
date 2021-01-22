package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

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

    public Trigger(SwordSkillType type) {
        this.type = type;
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

            if(this.type.equals(SwordSkillType.PRIMARY) 
                && !identifier.getName().equalsIgnoreCase("Primary Skill Activated")) { return false; }
            if(this.type.equals(SwordSkillType.SECONDARY)
                && !identifier.getName().equalsIgnoreCase("Secondary Skill Activated")) { return false; }
            if(this.type.equals(SwordSkillType.SWORD) 
                && !(identifier instanceof WeaponAttributeHolder)) { return false; }
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
