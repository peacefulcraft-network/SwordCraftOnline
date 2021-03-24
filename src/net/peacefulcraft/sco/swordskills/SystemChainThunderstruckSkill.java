package net.peacefulcraft.sco.swordskills;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class SystemChainThunderstruckSkill extends SwordSkill {

    private int rangeModifier;

    public SystemChainThunderstruckSkill(SwordSkillCaster c, int rangeModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.rangeModifier = rangeModifier;        

        this.useModule(new TimedCooldown(30000, (ModifierUser)c, "System Chain: Thunderstruck", tier));
        this.useModule(new Trigger(SwordSkillType.PRIMARY));
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
        EntityInteractEvent evv = (EntityInteractEvent)ev;

        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) { return; }
        Location start = mu.getLivingEntity().getLocation();
        Location end = mu.getLivingEntity().getTargetBlock((Set<Material>)null, rangeModifier).getLocation();

        List<Location> blocks = LocationUtil.getPathLocations(
            start, 
            end, 
            rangeModifier, 
            2, 
            true);
        
        List<List<Location>> part = Lists.partition(blocks, 5);
        Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), new Runnable(){
            int i = 0;
            @Override
            public void run() {
                for(Location locc : part.get(i)) {
                    start.getWorld().strikeLightning(locc);
                }    
                i++;
            }
            
        }, 20, 20);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
