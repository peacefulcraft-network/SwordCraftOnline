package net.peacefulcraft.sco.swordskills.weaponskills;

import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class RefinedPower implements WeaponModifier {

    private String level;

    @Override
    public String getName() {
        return "Refined Power";
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public Double getModifierAmount() {
        return 0.2 * RomanNumber.romanToDecimal(this.level) + 0.2;
    }

    @Override
    public ModifierType getModifierType() {
        return null;
    }

    @Override
    public CombatModifier getCombatModifierType() {
        return CombatModifier.CRITICAL_MULTIPLIER;
    }

    @Override
    public Boolean getModifierIncoming() {
        return false;
    }

    @Override
    public void applyEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), getModifierAmount(), -1);
        SwordCraftOnline.logDebug("Set modifier: " + getCombatModifierType().toString() + 
            ", increased by: " + getModifierAmount());
        SwordCraftOnline.logDebug("Player modifier value: " + 
            user.getCombatModifier(getCombatModifierType()));
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), -getModifierAmount(), -1);
        SwordCraftOnline.logDebug("Set modifier: " + getCombatModifierType().toString() + 
            ", decreased by: " + getModifierAmount());
        SwordCraftOnline.logDebug("Player modifier value: " + 
            user.getCombatModifier(getCombatModifierType()));
    }

    public RefinedPower(String level) {
        this.level = level;
    }
}
