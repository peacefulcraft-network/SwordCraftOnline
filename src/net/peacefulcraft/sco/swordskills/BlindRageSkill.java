package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.Pair;

public class BlindRageSkill extends SwordSkill {

    private ItemTier tier;

    public BlindRageSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(25000, (ModifierUser)c, "Blind Rage", tier));
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
        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 2));
        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1));

        List<Pair<String, Integer>> effects = new ArrayList<>();
        effects.add(new Pair<String, Integer>(PotionEffectType.INCREASE_DAMAGE.toString(), 2));
        effects.add(new Pair<String, Integer>(PotionEffectType.BLINDNESS.toString(), 1));
        SkillAnnouncer.messageSkill(mu, "Blind Rage", tier, effects);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
