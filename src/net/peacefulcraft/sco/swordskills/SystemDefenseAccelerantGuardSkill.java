package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class SystemDefenseAccelerantGuardSkill extends SwordSkill {

    public SystemDefenseAccelerantGuardSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
        this.useModule(new TimedCooldown(45000));
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
        List<Location> scanLocs = LocationUtil.getCylinderLocations(
            mu.getLivingEntity().getLocation(), 
            1,
            10, 
            false);
        
        int fireCount = 0;
        for(Location loc : scanLocs) {
            Block curr = loc.getBlock();
            if(!curr.getType().equals(Material.FIRE)) {
                Block upCurr = curr.getRelative(BlockFace.UP);
                if(!upCurr.getType().equals(Material.FIRE)) {
                    continue;
                } else {
                    curr = upCurr;
                }
            }
            fireCount++;
            curr.setType(Material.AIR);
        }
        if(fireCount == 0) { return; }
        mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            mu.getAttribute(Attribute.GENERIC_ARMOR) + (0.1 * fireCount), 
            13);
        
        if(SwordCraftOnline.r.nextInt(5) <= 3) {
            mu.getLivingEntity().setFireTicks(100);
        }  
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
