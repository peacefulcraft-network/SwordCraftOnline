package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.LocationUtil;
import net.peacefulcraft.sco.utilities.Pair;

public class SystemDefenseAccelerantGuardSkill extends SwordSkill {

    private ItemTier tier;

    public SystemDefenseAccelerantGuardSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(45000, (ModifierUser)c, "System Defense Accelerant Guard", tier));
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

        double mult = mu.getMultiplier(ModifierType.FIRE, false);
        double boost = 0.1 * fireCount * mult;

        mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            mu.getAttribute(Attribute.GENERIC_ARMOR) + boost, 
            13);

        SkillAnnouncer.messageSkill(
            mu, 
            new Pair<String, Double>(Attribute.GENERIC_ARMOR.toString(), boost), 
            "System Defense: Accelerant Guard", 
            tier);
        
        if(SwordCraftOnline.r.nextInt(5) <= 3) {
            mu.getLivingEntity().setFireTicks(100);
            SkillAnnouncer.messageSkill(mu, "Accelerant backfired", "System Defense: Accelerant Guard", tier);
        }  
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
