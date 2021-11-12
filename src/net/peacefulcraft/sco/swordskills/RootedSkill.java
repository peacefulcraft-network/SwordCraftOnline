package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.Pair;

public class RootedSkill extends SwordSkill {

    private int regenModifier;
    private ItemTier tier;

    public RootedSkill(SwordSkillCaster c, int regenModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.regenModifier = regenModifier;
        this.tier = tier;

        this.listenFor(SwordSkillTrigger.PLAYER_MOVE);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        
        PlayerMoveEvent evv = (PlayerMoveEvent)ev;
        Location loc = evv.getPlayer().getLocation().clone().add(0, -1, 0);
        if(!loc.getBlock().getType().equals(Material.GRASS_BLOCK)) { 
            mu.getLivingEntity().removePotionEffect(PotionEffectType.REGENERATION);
            return false; 
        }

        if(mu.getLivingEntity().hasPotionEffect(PotionEffectType.REGENERATION)) {
            return false;
        }

        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        double mult = mu.getMultiplier(ModifierType.EARTH, false);

        int regenLvl = (int) Math.ceil(regenModifier * mult);

        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(PotionEffectType.REGENERATION, 999999, regenLvl)
            );
        SkillAnnouncer.messageSkill(
            mu, 
            "Rooted", 
            tier,
            new Pair<String, Integer>(PotionEffectType.REGENERATION.toString(), regenLvl));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
