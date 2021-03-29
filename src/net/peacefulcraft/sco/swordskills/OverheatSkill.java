package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class OverheatSkill extends SwordSkill {

    public OverheatSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);

        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(17000, (ModifierUser)c, "Overheat", tier));
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

        List<Location> locs = LocationUtil.getCylinderLocations(
            mu.getLivingEntity().getLocation(), 
            1, 
            6, 
            false);
        for(Block curr : LocationUtil.locationsToBlocks(locs)) {
            if(SwordCraftOnline.r.nextBoolean()) { continue; }

            if(!curr.getType().equals(Material.AIR)) {
                if(!curr.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    curr = curr.getRelative(BlockFace.UP);
                } else if(!curr.getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                    curr = curr.getRelative(BlockFace.DOWN);
                } else {
                    continue;
                }
            }

            SwordCraftOnline.getPluginInstance().getFireManager().registerFire(14000, curr.getLocation());
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
