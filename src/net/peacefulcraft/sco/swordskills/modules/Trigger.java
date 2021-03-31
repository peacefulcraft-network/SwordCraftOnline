package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class Trigger implements SwordSkillModule {

    private Boolean triggered = false;
        private void setTriggered(boolean set) { triggered = set; }
        private boolean getTriggered() { return this.triggered; }

    private SwordSkillType type;
    private Boolean doesPass;
    private ModifierUser mu;

    /**
     * @param type of SwordSkill we are triggering
     * changes which item we detect for
     */
    public Trigger(SwordSkillType type, ModifierUser mu) {
        this(type, mu, false);
    }

    /**
     * @param type of SwordSkill we are triggering
     * changes which item we detect for
     * @param event type we pass through triggering
     */
    public Trigger(SwordSkillType type, ModifierUser mu, Boolean doesPass) {
        this.type = type;
        this.doesPass = doesPass;
        this.mu = mu;
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
        // Active Mobs can't interact right click so we pass
        if(mu instanceof ActiveMob) { return true; }

        // Checking if event should pass
        if(doesPass && !(ev instanceof PlayerInteractEvent)) { return true; }

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
        if(!doesPass && ev instanceof PlayerInteractEvent) {
            setTriggered(false);
        }
    }

    @Override
    public void onUnregistration(SwordSkill ss) {

    }
    
}
