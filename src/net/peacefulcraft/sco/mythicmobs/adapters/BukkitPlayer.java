package net.peacefulcraft.sco.mythicmobs.adapters;

import org.bukkit.GameMode;
import org.bukkit.WeatherType;

import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractGameMode;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;


public class BukkitPlayer extends BukkitEntity implements AbstractPlayer {

    private boolean pre8 = false;
    
    public BukkitPlayer(Player p) {
        super((Entity)p);
    }
    
    public boolean isInCreativeMode() {
      return getEntityAsPlayer().getGameMode().equals(GameMode.CREATIVE);
    }
    
    public boolean isInSpectatorMode() {
      if (this.pre8 == true)
        return false; 
      try {
        return getEntityAsPlayer().getGameMode().equals(GameMode.SPECTATOR);
      } catch (NoSuchFieldError er) {
        this.pre8 = true;
        return false;
      } 
    }
    
    public void sendMessage(String message) {
      getEntityAsPlayer().sendMessage(message);
    }
    
    public boolean hasPermission(String perm) {
      return getEntityAsPlayer().hasPermission(perm);
    }
    
    public float getExperience() {
      return getEntityAsPlayer().getExp();
    }
    
    public void setExperience(float exp) {
      getEntityAsPlayer().setExp(exp);
    }
    
    public void hidePlayer(AbstractPlayer target) {
      getEntityAsPlayer().hidePlayer(SwordCraftOnline.getPluginInstance(), (Player)target.getBukkitEntity());
    }
    
    public void showPlayer(AbstractPlayer target) {
      getEntityAsPlayer().showPlayer(SwordCraftOnline.getPluginInstance(), (Player)target.getBukkitEntity());
    }
    
    public boolean canSee(AbstractPlayer target) {
      return getEntityAsPlayer().canSee((Player)target.getBukkitEntity());
    }
    
    public boolean isOnline() {
      return getEntityAsPlayer().isOnline();
    }
    
    public int getLevel() {
      return getEntityAsPlayer().getLevel();
    }
    
    public void setLevel(int level) {
      getEntityAsPlayer().setLevel(level);
    }
    
    public void setHealthScale(double scale) {
      getEntityAsPlayer().setHealthScale(scale);
    }
    
    public void setHealthScaled(boolean scale) {
      getEntityAsPlayer().setHealthScaled(scale);
    }
    
    public void setPersonalTime(long time, boolean relative) {
      getEntityAsPlayer().setPlayerTime(time, relative);
    }
    
    public void resetPersonalTime() {
      getEntityAsPlayer().resetPlayerTime();
    }
    
    public void setPersonalWeather(String type) {
      getEntityAsPlayer().setPlayerWeather(WeatherType.valueOf(type.toUpperCase()));
    }
    
    public void resetPersonalWeather() {
      getEntityAsPlayer().resetPlayerWeather();
    }
    
    public void setAllowFlight(boolean b) {
      getEntityAsPlayer().setAllowFlight(b);
    }
    
    public boolean getAllowFlight() {
      return getEntityAsPlayer().getAllowFlight();
    }
    
    public void setFlying(boolean b) {
      getEntityAsPlayer().setFlying(b);
    }
    
    public void setFlyingSpeed(float f) {
      getEntityAsPlayer().setFlySpeed(f);
    }
    
    public void setWalkSpeed(float f) {
      getEntityAsPlayer().setWalkSpeed(f);
    }
    
    public int getFoodLevel() {
      return getEntityAsPlayer().getFoodLevel();
    }
    
    public void setFoodLevel(int amount) {
      getEntityAsPlayer().setFoodLevel(amount);
    }
    
    public float getFoodSaturation() {
      return getEntityAsPlayer().getSaturation();
    }
    
    public void setFoodSaturation(float amount) {
      getEntityAsPlayer().setSaturation(amount);
    }
    
    public void setGameMode(AbstractGameMode mode) {
      switch (mode) {
        case ADVENTURE:
          getEntityAsPlayer().setGameMode(GameMode.ADVENTURE);
          break;
        case CREATIVE:
          getEntityAsPlayer().setGameMode(GameMode.CREATIVE);
          break;
        case SPECTATOR:
          getEntityAsPlayer().setGameMode(GameMode.SPECTATOR);
          break;
        case SURVIVAL:
          getEntityAsPlayer().setGameMode(GameMode.SURVIVAL);
          break;
      } 
    }
}