package net.peacefulcraft.sco.mythicmobs.skills;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;

public interface SkillCaster {
    AbstractEntity getEntity();

    default AbstractLocation getLocation() {
        return getEntity().getLocation();
    }

    void setUsingSkill(boolean paramBoolean);

    boolean isUsingSkill();

    default int getLevel() {
        return 1;
    }
}