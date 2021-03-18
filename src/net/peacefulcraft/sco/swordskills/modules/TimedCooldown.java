package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCooldownProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * TimedCooldown
 * Cooldowns will not be persistent between SwordSkill instances unless the SwordSkill is
 * - Backed by a SwordSkillProvider Item
 * - That SwordSkillProvider Item is also a SwordSKillCooldownProvider
 * - The SwordSkillProvider reports 'ss_cooldown' as an NBT flag in the serializer methods
 */
public class TimedCooldown implements SwordSkillModule {

    private long cooldownDelay;

    private void setCooldownDelay(long delay) {
        this.cooldownDelay = delay;
    }

    private long cooldownEnd = 0L;

    private boolean isCoolingDown() {
        return this.cooldownEnd > System.currentTimeMillis();
    }

    private long getTimeRemaining() {
        return this.cooldownEnd - System.currentTimeMillis();
    }

    private long getCooldownSeconds() {
        return this.getTimeRemaining() / 1000;
    }

    private String cooldownMessage;

    private String getCooldownMessage() {
        return this.cooldownMessage;
    }

    public TimedCooldown(SwordSkillCooldownProvider provider, long cooldonwDelay) {
        this.cooldownEnd = provider.getCooldownEnd();
        this.cooldownDelay = cooldonwDelay;
    }

    public TimedCooldown(SwordSkillCooldownProvider provider, long cooldownDelay, String cooldownMessage) {
        this.cooldownEnd = provider.getCooldownEnd();
        this.cooldownDelay = cooldownDelay;
        this.cooldownMessage = cooldownMessage;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {}
    /* No support life cycle steps needed */

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        return !isCoolingDown();
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) {
        return true;
    }
    /* No pre-trigger hook */

    /**
     * After the skill has been triggered, add the cooldown delay to
     * prevent future Skill activation until such time that the cooldown
     * time has passed.
     */
    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        Long cooldownEnd = System.currentTimeMillis() + this.cooldownDelay;
        this.cooldownEnd = cooldownEnd;

        // Check if this is a player with persistent cooldowns and make the 
        if (ss.getSCOPlayer() != null) {
            SwordSkillInventory ssi = ss.getSCOPlayer().getSwordSkillInventory();
            for(int i=0; i<9; i++) {
                ItemIdentifier item = ssi.getItem(i);

                // Check if this item supplies our skill
                if (ItemIdentifier.compareTo(ss.getProvider(), item, false) == 0) {

                    // Check if this item is not on cooldown
                    if (!((SwordSkillCooldownProvider) item).isOnCooldown()) {
                        ((SwordSkillCooldownProvider) item).markCooldownEnd(cooldownEnd);
                        ssi.setItem(i, item);

                        // Update the SwordSkillProvider signature
                        ((SwordSkillCooldownProvider) ss.getProvider()).markCooldownEnd(cooldownEnd);

                        // This item update is kept locally. If the user logs out their
                        // inventory will be saved with the updated cooldown value.

                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onUnregistration(SwordSkill ss) {}
    /* No Dequip Actions */
}