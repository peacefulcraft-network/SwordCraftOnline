package net.peacefulcraft.sco.swordskills.utilities;

import java.util.List;

/**
 * Interface for players and mobs that implement
 * outgoing and incoming damage modifiers.
 */
public interface IStrengthWeakness {
    List<String> getDamageModifiers();

    List<String> getWeaknessModifiers();
}