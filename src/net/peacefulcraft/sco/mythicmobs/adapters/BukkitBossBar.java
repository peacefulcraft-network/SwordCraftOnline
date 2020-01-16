package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBarColor;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBarStyle;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBossBar;

public class BukkitBossBar implements AbstractBossBar {
    BossBar bar;
    
    public BukkitBossBar(String title, String aColor, String aStyle) {
        this(title, AbstractBarColor.getBarColor(aColor), AbstractBarStyle.getBarStyle(aStyle));
    }

    public BukkitBossBar(String title, BarColor aColor, BarStyle aStyle) {
        this.bar = Bukkit.getServer().createBossBar(title, aColor, aStyle, new BarFlag[0]);
    }
    
    public String getTitle() {
        return this.bar.getTitle();
    }
    
    public void setTitle(String title) {
        this.bar.setTitle(title);
    }
    
    public BarColor getColor() {
        return null;
    }
    
    public void setColor(String color) {
        this.bar.setColor(AbstractBarColor.getBarColor(color));
    }
    
    public BarStyle getStyle() {
        return null;
    }
    
    public void setStyle(String style) {}
    
    public void removeFlag(String flag) {
        this.bar.removeFlag(BarFlag.valueOf(flag));
    }
    
    public void addFlag(String flag) {
        this.bar.addFlag(BarFlag.valueOf(flag));
    }
    
    public boolean hasFlag(String flag) {
        return this.bar.hasFlag(BarFlag.valueOf(flag));
    }
    
    public void setProgress(double progress) {
        this.bar.setProgress(progress);
    }
    
    public double getProgress() {
        return this.bar.getProgress();
    }
    
    public void addPlayer(AbstractPlayer player) {
        this.bar.addPlayer((Player)player.getBukkitEntity());
    }
    
    public void removePlayer(AbstractPlayer player) {
        this.bar.removePlayer((Player)player.getBukkitEntity());
    }
    
    public void removeAll() {
        this.bar.removeAll();
    }
    
    public List<AbstractPlayer> getPlayers() {
        List<AbstractPlayer> list = new ArrayList<>();
        this.bar.getPlayers().stream().forEach(player -> list.add(BukkitAdapter.adapt(player)));
        return list;
    }
    
    public void setVisible(boolean visible) {
        this.bar.setVisible(visible);
    }
    
    public boolean isVisible() {
        return this.bar.isVisible();
    }
    
    public void setCreateFog(boolean b) {
        if (b) {
            this.bar.addFlag(BarFlag.CREATE_FOG);
        } else {
            this.bar.removeFlag(BarFlag.CREATE_FOG);
        } 
    }
    
    public void setDarkenSky(boolean b) {
        if (b) {
            this.bar.addFlag(BarFlag.DARKEN_SKY);
        } else {
            this.bar.removeFlag(BarFlag.DARKEN_SKY);
        } 
    }
    
    public void setPlayBossMusic(boolean b) {
        if (b) {
            this.bar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
        } else {
            this.bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
        } 
    }
}