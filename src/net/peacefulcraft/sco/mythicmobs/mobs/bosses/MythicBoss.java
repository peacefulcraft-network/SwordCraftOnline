package net.peacefulcraft.sco.mythicmobs.mobs.bosses;

import org.bukkit.Location;

public interface MythicBoss {
    public MythicBoss spawn(Location loc);

    public void phaseSelection();
}