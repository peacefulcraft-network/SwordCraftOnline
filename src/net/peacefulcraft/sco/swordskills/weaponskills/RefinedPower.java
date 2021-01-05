package net.peacefulcraft.sco.swordskills.weaponskills;

import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
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
        return WeaponModifier.calculateModifierAmount(RomanNumber.romanToDecimal(this.level));
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

    public RefinedPower(String level) {
        this.level = level;
    }
}
