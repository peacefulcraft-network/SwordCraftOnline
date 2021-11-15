package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.Pair;

public class WeakenedPulseSkill extends SwordSkill {

    private int weakModifier;
    private ItemTier tier;

    public WeakenedPulseSkill(SwordSkillCaster c, int weakModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.weakModifier = weakModifier;
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
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
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) { return; }

        ModifierUser userMu = (ModifierUser)c;
        double mult = userMu.getMultiplier(ModifierType.PHYSICAL, false);

        int weakLvl = (int) Math.ceil(weakModifier * mult);

        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(PotionEffectType.WEAKNESS, 100, weakLvl)
        );

        SkillAnnouncer.messageSkill(
            mu, 
            "Weakened Pulse", 
            tier, 
            new Pair<String,Integer>(PotionEffectType.WEAKNESS.toString(), 5));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
