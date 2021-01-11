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
    public String getLevel() {
        return this.level;
    }


    @Override
    public void applyEffects(ModifierUser user) {
        user.addToCombatModifier(CombatModifier.CRITICAL_CHANCE, getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.addToCombatModifier(CombatModifier.CRITICAL_CHANCE, -getModifierAmount(), -1);
    }

    @Override
    public Boolean canReforge() {
        return true;
    }

    @Override
    public Integer getMaxPlayerLevel() {
        return 5;
    }

    public RefinedTechnique(String level) {
        this.level = level;
    }
    
}
