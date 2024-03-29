package net.peacefulcraft.sco.swordskills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.DirectionTracker;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil.Movement;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class EnderBlitzControlSkill extends SwordSkill {

    private int repetitions = 0;
    private int id;
    private ItemTier tier;

    public EnderBlitzControlSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;

        if(!(c instanceof SCOPlayer)) {
            throw new RuntimeException("Failed to register EnderBlitzControlSkill. Caster not SCOPlayer.");
        }
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);

        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(32000, (ModifierUser)c, "Ender Blits: Control", tier));
        this.useModule(new DirectionTracker((SCOPlayer)c));
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
        SCOPlayer s = (SCOPlayer)c;
        id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            public void run() {
                if(repetitions == 4) {
                    Bukkit.getServer().getScheduler().cancelTask(id);
                    return;
                }
                Location centered = null;
                Location curr = s.getLocation();
                Movement move = s.getMovement();
                double rotation = curr.getY() % 360;

                switch(move){
                    case FORWARD:
                        centered = getCentered(rotation, 5);
                    break; case FORWARD_SPRINT:
                        centered = getCentered(rotation, 8);
                    break; case LEFT:
                        centered = getCentered(rotation - 90, 5);
                    break; case RIGHT:
                        centered = getCentered(rotation + 90, 5);
                    break; case BACKWARD:
                        centered = getCentered(rotation + 180, 5);
                    default:
                        centered = curr;
                }

                s.getLivingEntity().teleport(
                    LocationUtil.getRandomLocations(centered, 5, 1).get(0).add(0, 1, 0)
                );

                s.queueChange(
                    Attribute.GENERIC_ATTACK_DAMAGE, 
                    s.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), 
                    2);
                repetitions++;
            }
        }, 5, 60);

        SkillAnnouncer.messageSkill(
            s, 
            "Blitz triggered", 
            "Ender Blitz: Control", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }

    private Location getCentered(double rotation, int range) {
        SCOPlayer s = (SCOPlayer)c;
        BlockFace relativeDir = DirectionalUtil.getRelativeBlockFace(s.getPlayer(), rotation);
        Location curr = s.getLocation();
        Location end = curr;
        for(int i = 1; i <= range; i++) {
            end = end.getBlock().getRelative(relativeDir).getLocation();
        }
        return end;
    }
    
}
