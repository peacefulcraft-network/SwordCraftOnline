package net.peacefulcraft.sco.swordskills;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class SystemChainThunderstruckSkill extends SwordSkill {

    private int rangeModifier;

    public SystemChainThunderstruckSkill(SwordSkillCaster c, int rangeModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.rangeModifier = rangeModifier;        

        this.useModule(new TimedCooldown(30000));
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        Location start = mu.getLivingEntity().getLocation();
        Location end = mu.getLivingEntity().getTargetBlock((Set<Material>)null, rangeModifier).getLocation();

        List<Location> blocks = LocationUtil.getPathLocations(
            start, 
            end, 
            rangeModifier, 
            2, 
            true);
        for(Location loc : blocks) {
            start.getWorld().strikeLightning(loc);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
