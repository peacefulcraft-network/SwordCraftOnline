package net.peacefulcraft.sco.swordskills.weaponskills;

import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class RefinedTechnique implements WeaponModifier {

    private String level;

    @Override
    public String getName() {
        return "Refined Technique";
    }

    @Override
    public Double getModifierAmount() {
        return 0.2 * RomanNumber.romanToDecimal(this.level) + 0.2;
    }

    @Override
    public Boolean getModifierIncoming() {
        return false;
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public ModifierType getModifierType() {
        return null;
    }

    @Override
    public CombatModifier getCombatModifierType() {
        return CombatModifier.CRITICAL_CHANCE;
    }

    @Override
    public void applyEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), -getModifierAmount(), -1);
    }

    public RefinedTechnique(String level) {
        this.level = level;
    }
    
}
