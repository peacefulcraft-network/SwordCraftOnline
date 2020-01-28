package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

import java.util.List;
import java.util.UUID;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBossBar;

public interface ServerInterface {
    List<AbstractWorld> getWorlds();

    //void dispatchCommand(String paramString);

    List<AbstractPlayer> getOnlinePlayers();

    AbstractLocation newLocation(AbstractWorld paramAbstractWorld, double paramDouble1, double paramDouble2, double paramDouble3);

    AbstractWorld getWorld(String paramString);

    boolean isValidBiome(Object paramObject);

    AbstractBossBar createBossBar(String paramString, BarColor paramBarColor, BarStyle paramBarStyle);

    AbstractPlayer getPlayer(UUID paramUUID);

    AbstractPlayer getPlayer(String paramString);

    AbstractEntity getEntity(UUID paramUUID);

    AbstractWorld getWorld(UUID paramUUID);
}