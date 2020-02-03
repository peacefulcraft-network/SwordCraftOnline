package net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss;

import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;

public interface AbstractBossBar {
    String getTitle();
    
    void setTitle(String paramString);
    
    BarColor getColor();
    
    void setColor(String paramString);
    
    BarStyle getStyle();
    
    void setStyle(String paramString);
    
    void removeFlag(String paramString);
    
    void addFlag(String paramString);
    
    boolean hasFlag(String paramString);
    
    void setProgress(double paramDouble);
    
    double getProgress();
    
    void addPlayer(AbstractPlayer paramAbstractPlayer);
    
    void removePlayer(AbstractPlayer paramAbstractPlayer);
    
    void removeAll();
    
    List<AbstractPlayer> getPlayers();
    
    void setVisible(boolean paramBoolean);
    
    boolean isVisible();
    
    void setCreateFog(boolean paramBoolean);
    
    void setDarkenSky(boolean paramBoolean);
    
    void setPlayBossMusic(boolean paramBoolean);
    
    /*
    public enum BarColor {
      PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE;
    }
    
    public enum BarStyle {
      SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20;
    }
    
    public enum BarFlag {
      DARKEN_SKY, PLAY_BOSS_MUSIC, CREATE_FOG;
    }
    */
}