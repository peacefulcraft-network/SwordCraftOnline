package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class SystemBreakAccelerantBurstSkill extends SwordSkill {

    private ItemTier tier;

    public SystemBreakAccelerantBurstSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.PRIMARY));
        this.useModule(new TimedCooldown(45000, (ModifierUser)c, SwordSkillType.PRIMARY));
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
        List<Location> explodeLocs = LocationUtil.getRandomLocations(
            mu.getLivingEntity().getLocation(), 
            10, 
            fireCount);
        for(Location loc : explodeLocs) {
            loc.getWorld().createExplosion(
                loc, 
                2,
                false,
                false);
        }

        if(SwordCraftOnline.r.nextInt(5) <= 3) {
            mu.getLivingEntity().setFireTicks(100);
            SkillAnnouncer.messageSkill(mu, "Accelerant backfired.", "System Break: Accelerant Burst", tier);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
