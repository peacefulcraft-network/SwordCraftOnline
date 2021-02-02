package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class EnderBlitzSkill extends SwordSkill {

    private int repetitions = 0;
    private int id;

    public EnderBlitzSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(25000));
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
        List<Location> locs = LocationUtil.getRandomLocations(
            mu.getLivingEntity().getLocation(), 
            7, 
            5);
        id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            public void run() {
                if(repetitions == 4) { 
                    Bukkit.getServer().getScheduler().cancelTask(id);
                    return;
                }
                mu.getLivingEntity().teleport(locs.get(repetitions));
                mu.queueChange(
                    Attribute.GENERIC_ATTACK_DAMAGE, 
                    mu.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), 
                    2);
                repetitions++;
            }
        }, 5, 60);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
